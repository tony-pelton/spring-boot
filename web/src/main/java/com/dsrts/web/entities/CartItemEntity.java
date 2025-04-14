package com.dsrts.web.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "cart_item")
public class CartItemEntity {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private CartEntity cart;

    @ManyToOne
    private BookEntity book;

    @Column(nullable = false)
    private Integer qty;

    @CreatedDate
    @Column(nullable = false)
    private Instant createdOn;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant updatedOn;
}
