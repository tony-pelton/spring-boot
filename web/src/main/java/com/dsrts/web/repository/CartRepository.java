package com.dsrts.web.repository;

import com.dsrts.web.entities.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "carts",path = "cart")
public interface CartRepository extends JpaRepository<CartEntity,Long> {
}
