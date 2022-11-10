package com.example.msccspringtesting.infrastructure.adapters.output.persistence.repository;

import com.example.msccspringtesting.infrastructure.adapters.output.persistence.entity.AccountTransferEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountTransferRepository extends CrudRepository<AccountTransferEntity, Long> {
}
