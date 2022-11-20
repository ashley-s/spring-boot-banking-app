package com.example.msccspringtesting.infrastructure.adapters.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;

@TestConfiguration
@ComponentScan(basePackages = "com.example.msccspringtesting.infrastructure.adapters.output.persistence")
public class TestConfigPersistenceComponent {
}
