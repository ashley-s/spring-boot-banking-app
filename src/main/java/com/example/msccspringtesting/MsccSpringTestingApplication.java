package com.example.msccspringtesting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableCaching
@EnableAspectJAutoProxy
@EnableAsync
@ConfigurationPropertiesScan(value = "com.example.msccspringtesting.infrastructure.adapters.config")
public class MsccSpringTestingApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsccSpringTestingApplication.class, args);
    }

}
