package com.example.msccspringtesting.infrastructure.adapters.input.rest;

import com.example.msccspringtesting.application.ports.input.AccountUseCase;
import com.example.msccspringtesting.domain.model.Account;
import com.example.msccspringtesting.infrastructure.adapters.config.JwtSecurityConfig;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

@WebMvcTest(AccountRestAdapter.class)
@Import(value = JwtSecurityConfig.class)
class AccountRestAdapterTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AccountUseCase accountUseCase;

    @Test
    @WithMockUser(username = "1012")
    void should_return_accountlist_for_existing_customer() throws Exception {
        Account account = new Account();
        account.setAccountNumber("123456789");
        account.setCurrentBalance(100.00);
        Mockito.when(this.accountUseCase.getAccounts(ArgumentMatchers.anyString())).thenReturn(List.of(account));
        this.mockMvc.perform(MockMvcRequestBuilders.get("/v1/customers/{customerRefId}/accounts", "123")
                .contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk());
    }
}