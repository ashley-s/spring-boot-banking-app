package com.example.msccspringtesting.infrastructure.adapters.output.persistence.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "customers")
@Data
public class CustomerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String refId;
    private String name;
    @Column(columnDefinition="tinyint(1) default 1")
    private boolean isActive;
}
