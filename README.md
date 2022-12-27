# Spring Boot Testing with a mini banking app in an Hexagonal Architecture
## Introduction
This was supposed to be my talk at the MSCC DevCon 2022 but due to some unforeseen circumstances, I could not present at the conference. I put up quite some work in that
and definitely want to share that with you all, especially Beginners and Intermediate Developers too.
To be honest, I have heard the hexagonal architecture only this year and I wanted to experience with that architecture instead of the classical way we do things
usually. The application I have built is a mini banking app, a very basic app. I am not following great coding principles here. It is an easy one to follow and also the tests as well in this domain would be
make more sense.
My talk is focused mostly on how to write unit and integration testing for a Spring Boot App and to see the capabilities
offered by the Framework.
## Architecture of the Application
![img.png](img.png)
The main idea, IMHO, behind a hexagonal architecture is to keep the domain logic free/independent of any
framework so that the code can be easily transported that to any framework. The mini application consists of the following features:
1. Get bank accounts for a customer.
2. Get transactions for a particular bank account number.
3. Perform a transfer between two bank accounts.
4. The application will generate an event upon a successful account transfer.
### Setting up of the application
You will need keycloak up and running to be able to run the application. Below are the docker-compose commands:
```yaml
version: '2'
services:
  postgresql:
    image: docker.io/bitnami/postgresql:11
    environment:
      # ALLOW_EMPTY_PASSWORD is recommended only for development.
      - ALLOW_EMPTY_PASSWORD=yes
      - POSTGRESQL_USERNAME=bn_keycloak
      - POSTGRESQL_DATABASE=bitnami_keycloak
    volumes:
      - 'postgresql_data:/bitnami/postgresql'

  keycloak:
    image: docker.io/bitnami/keycloak:19
    depends_on:
      - postgresql
    ports:
      - "80:8080"

volumes:
  postgresql_data:
    driver: local

```
Once up, please import the [realm](test-realm.json). All configuration will be done accordingly.
I am also attaching the postman collection for you to test. Download [here](Banking%20App.postman_collection.json). Once you are done importing the postman collection and
you are getting successful responses, I invite you to go through the code and try to understand. I did my optimum best to write comments throughout the code. I will focus more on the 
tests though in this documentation.
### Testing Strategy
I am sure that you know the testing pyramid and that integration and end-to-end tests are quite expensive to write. So you need
to focus mostly on unit testing as they give you fast feedback, blabla and blabla. LOL!
You can definitely pick up some books and read on this. Let's not waste time.
### Domain layer testing
For this part as per structure, you would agree with me that since this code is free of any Spring related configuration or annotation, we need to 
write unit tests.

![img_1.png](img_1.png)

Let's have a look at the method **createAccountTransfer** in the class **AccountTransferService**.
```java
@ExtendWith(MockitoExtension.class)
class AccountTransferServiceTest {

    @Mock
    private AccountService accountService;
    @Mock
    private AccountTransferOutputPort accountTransferOutputPort;
    @Mock
    private AccountTransferEventPublisher accountTransferEventPublisher;
    @InjectMocks
    private AccountTransferService accountTransferService;
    @Captor
    private ArgumentCaptor<AccountTransfer> accountTransferArgumentCaptor;
    @Captor
    private ArgumentCaptor<AccountTransferEvent> accountTransferEventArgumentCaptor;

    @Test
    @DisplayName("This test should create a successful transfer with funds being deducted from the sender account and funds being credited to the receiver account")
    void should_return_a_successful_account_transfer() {
        Mockito.when(this.accountService.getAccountByAccountNumber(ArgumentMatchers.anyString())).thenReturn(getSenderAccount()).thenReturn(getReceiverAccount());
        Mockito.when(this.accountTransferOutputPort.saveAccountTransfer(ArgumentMatchers.any(AccountTransfer.class))).thenReturn(getSuccessfulAccountTransfer());
        var accountTransfer = this.accountTransferService.createAccountTransfer(getAccountTransfer());
        Assertions.assertThat(accountTransfer).isNotNull();
        Assertions.assertThat(accountTransfer.getStatus()).isEqualTo("SUCCESS");
        Mockito.verify(this.accountService, Mockito.times(2)).getAccountByAccountNumber(ArgumentMatchers.anyString());
        Mockito.verify(this.accountTransferOutputPort, Mockito.times(1)).saveAccountTransfer(this.accountTransferArgumentCaptor.capture());
        var accountTransferModel = this.accountTransferArgumentCaptor.getValue();
        var senderAccount = accountTransferModel.getSenderAccount();
        var receiverAccount = accountTransferModel.getReceiverAccount();
        Assertions.assertThat(senderAccount.getCurrentBalance()).isEqualTo(99.00);
        Assertions.assertThat(receiverAccount.getCurrentBalance()).isEqualTo(101.00);
        Mockito.verify(this.accountTransferEventPublisher, Mockito.times(1)).publishSuccessfulTransferEvent(this.accountTransferEventArgumentCaptor.capture());
        var accountTransferEvent = this.accountTransferEventArgumentCaptor.getValue();
        Assertions.assertThat(accountTransferEvent.getReference()).isEqualTo("139f34e2-daf1-462c-966a-6660018c31c9");
    }
}
```
As from Junit5, to be able to use mockito alongside, you need to use the annotation ExtendWith and the extension MockitoExtension.
With that, you just need annotate your dependency classes with @Mock and the class under test with @InjectMocks.
There is one more particular annotation, Captor, that is really useful in some cases when you need to inspect arguments of method of mocked objects.
You really get to assert whether some objects are being constructed as it should be. 

Also do not forget to always verify your mocked objects to make sure that the actual method is being called.

### Integration Testing
This is the most interesting part. In my opinion, where this testing capability is really helpful, is when you need to test your feature and to be able to test it 
especially in large environments where you can't test locally and the only option is to deploy your application for instance on a Kubernetes Cluster 
with 30 microservices. You have to make your build and then release it. All this takes times. 

Instead of booting up the full application context (which takes time) and run tests against the latter, you can perform
sliced tests i.e. perform testing at different layers such as web or DB, which eventually will make your tests faster as 
only specific spring components respective to the layer will be present.

#### Web layer Testing
Let's have a look at one class and 2 methods in it.
```java
@WebMvcTest(AccountTransferRestAdapter.class)
@Import(value = JwtSecurityConfig.class)
class AccountTransferRestAdapterTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AccountTransferUseCase accountTransferUseCase;

    @Test
    @DisplayName("This test should return a successful account transfer provided the user has the roles PAY and the request body is valid")
    @WithMockUser(username = "1012", roles = {"USER", "PAY"})
    void should_return_successful_transfer_for_existing_customer() throws Exception {
        Mockito.when(this.accountTransferUseCase.createAccountTransfer(ArgumentMatchers.any(AccountTransfer.class))).thenReturn(getSuccessfulAccountTransfer());
        this.mockMvc.perform(MockMvcRequestBuilders.post("/v1/transfers").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.writeObjectAsJson(buildAccountTransferRequest())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.reference").value("139f34e2-daf1-462c-966a-6660018c31c9"));
    }

    @Test
    @DisplayName("This test should return 403 as the user does not have the role of PAY")
    @WithMockUser(username = "1012")
    void should_return_403_for_existing_customer_with_incorrect_roles() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/v1/transfers").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.writeObjectAsJson(buildAccountTransferRequest())))
                .andExpect(status().isForbidden());
    }
}
```
This [link](https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/test/autoconfigure/web/servlet/WebMvcTest.html) is very self-explanatory explaining
testing related to Spring MVC components only. As per our application, all our endpoints are protected using this below Http Security Configuration.

```java
@Configuration
public class JwtSecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests(authz -> authz.antMatchers(HttpMethod.GET, "/v1/customers/**", "/v1/accounts/**")
                        .hasRole("USER")
                        .antMatchers(HttpMethod.POST, "/v1/transfers/**")
                        .hasRole("PAY")
                        .anyRequest()
                        .authenticated())
                .oauth2ResourceServer(httpSecurityOAuth2ResourceServerConfigurer -> httpSecurityOAuth2ResourceServerConfigurer.jwt(jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(jwtAuthenticationConverter())));
        return http.build();
    }

    JwtAuthenticationConverter jwtAuthenticationConverter() {
        var jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRealmRoleConverter());
        return jwtAuthenticationConverter;
    }

    /**
     * Reading the JWT token to get roles from the claims.
     */
    static class KeycloakRealmRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
        public Collection<GrantedAuthority> convert(final Jwt jwt) {
            final Map<String, Object> realmAccess = (Map<String, Object>) jwt.getClaims().get("realm_access");
            return ((List<String>)realmAccess.get("roles")).stream()
                    .map(roleName -> "ROLE_" + roleName)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }
    }
}
```
