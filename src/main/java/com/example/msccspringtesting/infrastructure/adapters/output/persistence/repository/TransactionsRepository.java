package com.example.msccspringtesting.infrastructure.adapters.output.persistence.repository;

import com.example.msccspringtesting.infrastructure.adapters.output.persistence.entity.TransactionEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionsRepository extends CrudRepository<TransactionEntity, Long> {
    List<TransactionEntity> findAllByAccountOwnerAccountNumber(String accountNumber);
}
