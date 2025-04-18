package com.dsrts.web.repository;

import com.dsrts.web.entities.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "customers", path = "customer")
public interface CustomerRepository extends JpaRepository<CustomerEntity,Long> {
    public Optional<CustomerEntity> findByEmail(String mail);
}
