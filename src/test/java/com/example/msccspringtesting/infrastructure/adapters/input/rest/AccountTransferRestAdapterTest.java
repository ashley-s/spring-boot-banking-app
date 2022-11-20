package com.example.msccspringtesting.infrastructure.adapters.input.rest;

import com.example.msccspringtesting.application.ports.input.AccountTransferUseCase;
import com.example.msccspringtesting.domain.exception.AccountTransferFailedException;
import com.example.msccspringtesting.domain.exception.FundsInsufficientException;
import com.example.msccspringtesting.domain.model.Account;
import com.example.msccspringtesting.domain.model.AccountTransfer;
import com.example.msccspringtesting.infrastructure.adapters.config.JwtSecurityConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @Test
    @DisplayName("This test should return a successful account transfer provided the user has the roles PAY and the request body is valid")
    @WithMockUser(username = "1012", roles = {"USER", "PAY"})
    void should_return_400_for_existing_customer_with_insufficent_funds() throws Exception {
        Mockito.when(this.accountTransferUseCase.createAccountTransfer(ArgumentMatchers.any(AccountTransfer.class))).thenThrow(new FundsInsufficientException("Insufficient funds"));
        this.mockMvc.perform(MockMvcRequestBuilders.post("/v1/transfers").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.writeObjectAsJson(buildAccountTransferRequest())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.description").value("Insufficient funds"));
    }

    @Test
    @DisplayName("This test should return a successful account transfer provided the user has the roles PAY and the request body is valid")
    @WithMockUser(username = "1012", roles = {"USER", "PAY"})
    void should_return_500_for_unexpected_error() throws Exception {
        Mockito.when(this.accountTransferUseCase.createAccountTransfer(ArgumentMatchers.any(AccountTransfer.class))).thenThrow(new AccountTransferFailedException("Internal Server Error has occurred"));
        this.mockMvc.perform(MockMvcRequestBuilders.post("/v1/transfers").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.writeObjectAsJson(buildAccountTransferRequest())))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.description").value("Internal Server Error has occurred"));
    }

    @Test
    @DisplayName("This test should return a successful account transfer provided the user has the roles PAY and the request body is valid")
    @WithMockUser(username = "1012", roles = {"USER", "PAY"})
    void should_return_400_for_invalid_request_body() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/v1/transfers").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.writeObjectAsJson(buildInvalidAccountTransferRequest())))
                .andExpect(status().isBadRequest());
    }

    private static AccountTransfer getSuccessfulAccountTransfer() {
        Account senderAccount = new Account();
        senderAccount.setAccountNumber("12345789");
        Account receiverAccount = new Account();
        receiverAccount.setAccountNumber("456789123");
        AccountTransfer accountTransfer = new AccountTransfer();
        accountTransfer.setReceiverAccount(receiverAccount);
        accountTransfer.setSenderAccount(senderAccount);
        accountTransfer.setAmount(1.00);
        accountTransfer.setTransferType("OWN");
        accountTransfer.setStatus("SUCCESS");
        accountTransfer.setReference("139f34e2-daf1-462c-966a-6660018c31c9");
        return accountTransfer;
    }

    private String writeObjectAsJson(Object o) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(o);
    }

    private static AccountTransfer buildAccountTransferRequest() {
        Account senderAccount = new Account();
        senderAccount.setAccountNumber("12345789");
        Account receiverAccount = new Account();
        receiverAccount.setAccountNumber("456789123");
        AccountTransfer accountTransfer = new AccountTransfer();
        accountTransfer.setReceiverAccount(receiverAccount);
        accountTransfer.setSenderAccount(senderAccount);
        accountTransfer.setAmount(1.00);
        accountTransfer.setTransferType("OWN");
        return accountTransfer;
    }

    private static AccountTransfer buildInvalidAccountTransferRequest() {
        Account senderAccount = new Account();
        senderAccount.setAccountNumber("12345789");
        Account receiverAccount = new Account();
        receiverAccount.setAccountNumber("456789123");
        AccountTransfer accountTransfer = new AccountTransfer();
        accountTransfer.setReceiverAccount(receiverAccount);
        accountTransfer.setSenderAccount(senderAccount);
        accountTransfer.setAmount(1.00);
        return accountTransfer;
    }

}