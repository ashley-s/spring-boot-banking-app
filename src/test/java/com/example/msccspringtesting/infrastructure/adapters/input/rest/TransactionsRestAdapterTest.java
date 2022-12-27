package com.example.msccspringtesting.infrastructure.adapters.input.rest;

import com.example.msccspringtesting.application.ports.input.TransactionUseCase;
import com.example.msccspringtesting.domain.model.Transaction;
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

@WebMvcTest(TransactionsRestAdapter.class)
@Import(value = JwtSecurityConfig.class)
class TransactionsRestAdapterTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TransactionUseCase transactionUseCase;

    @Test
    @DisplayName("This test should return a list of transactions for an account number")
    @WithMockUser(username = "1012")
    void should_return_transactionsList_for_existing_customer() throws Exception {
        Transaction transaction = new Transaction();
        transaction.setAmount(100.00);
        transaction.setReference("20638fc9-65b2-454d-9298-fa5d41f091a4");
        Mockito.when(this.transactionUseCase.getTransactionsByAccountNumber(ArgumentMatchers.anyString())).thenReturn(List.of(transaction));
        this.mockMvc.perform(MockMvcRequestBuilders.get("/v1/accounts/{accountNumber}/transactions", "123456789")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].reference").value("20638fc9-65b2-454d-9298-fa5d41f091a4"));
    }

    @Test
    @DisplayName("This test should return 403 forbidden if the user does not have the required roles")
    @WithMockUser(username = "1012", roles = {"ADMIN"})
    void should_return_403_for_existing_customer_with_incorrect_roles() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/v1/accounts/{accountNumber}/transactions", "123456789")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

}