package com.example.msccspringtesting.infrastructure.adapters.input.rest;

import com.example.msccspringtesting.application.ports.input.AccountUseCase;
import com.example.msccspringtesting.domain.exception.CustomerNotFoundException;
import com.example.msccspringtesting.domain.model.Account;
import com.example.msccspringtesting.infrastructure.adapters.config.JwtSecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountRestAdapter.class)
@Import(value = JwtSecurityConfig.class)
class AccountRestAdapterTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AccountUseCase accountUseCase;

    @Test
    @DisplayName("This test should return a list of accounts for an existing customer")
    @WithMockUser(username = "1012")
    void should_return_accountlist_for_existing_customer() throws Exception {
        Account account = new Account();
        account.setAccountNumber("123456789");
        account.setCurrentBalance(100.00);
        Mockito.when(this.accountUseCase.getAccounts(ArgumentMatchers.anyString())).thenReturn(List.of(account));
        this.mockMvc.perform(MockMvcRequestBuilders.get("/v1/customers/{customerRefId}/accounts", "123")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].accountNumber").value("123456789"));
    }

    @Test
    @WithMockUser(username = "1012")
    @DisplayName("This test should return 404 status code if the user does not exist")
    void should_return_404_for_invalid_customer() throws Exception {
        Account account = new Account();
        account.setAccountNumber("123456789");
        account.setCurrentBalance(100.00);
        Mockito.when(this.accountUseCase.getAccounts(ArgumentMatchers.anyString())).thenThrow(new CustomerNotFoundException("Customer does not exist"));
        this.mockMvc.perform(MockMvcRequestBuilders.get("/v1/customers/{customerRefId}/accounts", "123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.description").value("Customer does not exist"));
    }

    @Test
    @WithMockUser(username = "1012", roles = {"ADMIN"})
    @DisplayName("This test should return 403 forbidden if the user does not have the required roles")
    void should_return_403_for_user_with_incorrect_roles() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/v1/customers/{customerRefId}/accounts", "123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}