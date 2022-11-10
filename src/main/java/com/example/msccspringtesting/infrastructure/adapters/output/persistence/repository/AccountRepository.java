package com.example.msccspringtesting.infrastructure.adapters.output.persistence.repository;

import com.example.msccspringtesting.infrastructure.adapters.output.persistence.entity.AccountEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends CrudRepository<AccountEntity, Long> {

    List<AccountEntity> findAllByCustomerId(long customerId);
    Optional<AccountEntity> findByAccountNumber(String accountNumber);
}
