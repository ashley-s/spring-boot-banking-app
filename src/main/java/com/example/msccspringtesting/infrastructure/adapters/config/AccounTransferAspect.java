package com.example.msccspringtesting.infrastructure.adapters.config;

import com.example.msccspringtesting.domain.exception.CustomerNotActiveException;
import com.example.msccspringtesting.domain.exception.TransferTypeDisabledException;
import com.example.msccspringtesting.domain.model.AccountTransfer;
import com.example.msccspringtesting.infrastructure.adapters.output.persistence.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class AccounTransferAspect {

    private final CustomerRepository customerRepository;
    private final PaymentTypesConfig paymentTypesConfig;

    @Around("execution(public com.example.msccspringtesting.domain.model.AccountTransfer createAccountTransfer(..))")
    public Object verifyUserEligilibityBeforeTransfer(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        log.info("before executing method");
        Object[] args = proceedingJoinPoint.getArgs();
        var accountTransfer = (AccountTransfer) args[0];
        String customerNumber = this.getUserName();
        boolean isActive = this.customerRepository.findByRefId(customerNumber).orElseThrow(RuntimeException::new).isActive();
        if (isActive){
            String transferType = accountTransfer.getTransferType();
            boolean isTransferTypeAllowed = this.paymentTypesConfig.getPaymentTypes().contains(transferType);
            if (!isTransferTypeAllowed) {
                throw new TransferTypeDisabledException("Transfer type " + transferType + " is not allowed");
            }
            return proceedingJoinPoint.proceed();
        } else {
            throw new CustomerNotActiveException("Customer is not active. Cannot proceed for account transfer.");
        }
    }

    private String getUserName() {
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        var jwt = (Jwt) authenticationToken.getCredentials();
        return (String) jwt.getClaims().get("preferred_username");
    }
}
