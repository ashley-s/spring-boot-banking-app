package com.example.msccspringtesting.infrastructure.adapters.output.persistence.repository;

import com.example.msccspringtesting.infrastructure.adapters.output.persistence.entity.CustomerEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends CrudRepository<CustomerEntity, Long> {
    Optional<CustomerEntity> findByRefId(String customerNumber);
}
