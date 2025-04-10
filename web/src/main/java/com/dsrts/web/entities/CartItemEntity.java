package com.dsrts.web.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;

import java.time.Instant;

@Data
@Entity
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

    @CreationTimestamp(source = SourceType.VM)
    @Column(nullable = false)
    private Instant createdOn;
}
