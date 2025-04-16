package com.dsrts.web.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.Set;

@Getter
@Setter
@ToString(exclude = {"customer", "cartItems"})
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "carts")
public class CartEntity {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private CustomerEntity customer;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CartItemEntity> cartItems;

    @CreatedDate
    @Column(nullable = false)
    private Instant createdOn;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant updatedOn;
}
