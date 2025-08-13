package com.dsrts.web.repository;

import com.dsrts.web.entities.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "carts",path = "cart")
public interface CartRepository extends JpaRepository<CartEntity,Long> {
    List<CartEntity> findByCustomerEmailAndStatus(String email, CartEntity.CartStatus status);
    List<CartEntity> findByCustomerEmail(@Param("email") String email);
    List<CartEntity> findByStatus(@Param("status") CartEntity.CartStatus status);
}
