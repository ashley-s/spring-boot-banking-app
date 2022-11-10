package com.example.msccspringtesting.infrastructure.adapters.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.List;

@ConfigurationProperties(prefix = "bank")
@ConstructorBinding
@RequiredArgsConstructor
@Getter
public class PaymentTypesConfig {
    private final List<String> paymentTypes;
}
