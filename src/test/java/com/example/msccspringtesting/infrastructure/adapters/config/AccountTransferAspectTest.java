package com.example.msccspringtesting.infrastructure.adapters.config;

import com.example.msccspringtesting.domain.exception.CustomerNotActiveException;
import com.example.msccspringtesting.domain.exception.TransferTypeDisabledException;
import com.example.msccspringtesting.domain.model.AccountTransfer;
import com.example.msccspringtesting.infrastructure.adapters.config.AccountTransferAspect;
import com.example.msccspringtesting.infrastructure.adapters.config.PaymentTypesConfig;
import com.example.msccspringtesting.infrastructure.adapters.output.persistence.entity.CustomerEntity;
import com.example.msccspringtesting.infrastructure.adapters.output.persistence.repository.CustomerRepository;
import org.aspectj.lang.ProceedingJoinPoint;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AccountTransferAspectTest {

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private PaymentTypesConfig paymentTypesConfig;
    @InjectMocks
    private AccountTransferAspect accountTransferAspect;
    @Mock
    private ProceedingJoinPoint proceedingJoinPoint;

    @BeforeEach
    void setUp() {
        Authentication authentication = Mockito.mock(JwtAuthenticationToken.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Jwt jwt = Mockito.mock(Jwt.class);
        Mockito.when(authentication.getCredentials()).thenReturn(jwt);
        Map<String, Object> claims = Map.of("preferred_username", "1012");
        Mockito.when(jwt.getClaims()).thenReturn(claims);
    }

    @Test
    void should_execute_method_user_active() throws Throwable {
        Mockito.when(this.paymentTypesConfig.getPaymentTypes()).thenReturn(List.of("OWN", "OTHER"));
        AccountTransfer accountTransfer = new AccountTransfer();
        accountTransfer.setTransferType("OWN");
        Mockito.when(this.proceedingJoinPoint.getArgs()).thenReturn(new Object[]{accountTransfer});
        CustomerEntity customer = new CustomerEntity();
        customer.setActive(true);
        Mockito.when(this.customerRepository.findByRefId(ArgumentMatchers.anyString())).thenReturn(Optional.of(customer));
        Assertions.assertThatCode(() -> this.accountTransferAspect.verifyUserBeforeTransfer(this.proceedingJoinPoint)).doesNotThrowAnyException();
    }

    @Test
    void should__not_execute_method_user_not_active() throws Throwable {
        AccountTransfer accountTransfer = new AccountTransfer();
        accountTransfer.setTransferType("OWN");
        Mockito.when(this.proceedingJoinPoint.getArgs()).thenReturn(new Object[]{accountTransfer});
        CustomerEntity customer = new CustomerEntity();
        customer.setActive(false);
        Mockito.when(this.customerRepository.findByRefId(ArgumentMatchers.anyString())).thenReturn(Optional.of(customer));
        Assertions.assertThatThrownBy(() -> this.accountTransferAspect.verifyUserBeforeTransfer(this.proceedingJoinPoint)).isInstanceOf(CustomerNotActiveException.class);
    }

    @Test
    void should__not_execute_method_payment_not_allowed() throws Throwable {
        Mockito.when(this.paymentTypesConfig.getPaymentTypes()).thenReturn(List.of("OWN", "OTHER"));
        AccountTransfer accountTransfer = new AccountTransfer();
        accountTransfer.setTransferType("OTHER1");
        Mockito.when(this.proceedingJoinPoint.getArgs()).thenReturn(new Object[]{accountTransfer});
        CustomerEntity customer = new CustomerEntity();
        customer.setActive(true);
        Mockito.when(this.customerRepository.findByRefId(ArgumentMatchers.anyString())).thenReturn(Optional.of(customer));
        Assertions.assertThatThrownBy(() -> this.accountTransferAspect.verifyUserBeforeTransfer(this.proceedingJoinPoint)).isInstanceOf(TransferTypeDisabledException.class);
    }

}