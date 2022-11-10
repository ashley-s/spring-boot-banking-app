package com.example.msccspringtesting.domain.model;

import lombok.Data;

@Data
public class Customer {
    private long id;
    private String refId;
    private String name;
    private boolean isActive;
}
