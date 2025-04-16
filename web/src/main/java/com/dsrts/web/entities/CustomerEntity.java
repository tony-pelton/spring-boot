package com.dsrts.web.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
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
@ToString(exclude = "carts")
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "customers")
public class CustomerEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Email
    @Column(unique = true,nullable = false)
    private String email;

    private String firstName;
    private String lastName;

    @OneToMany(mappedBy = "customer")
    private Set<CartEntity> carts;

    @CreatedDate
    @Column(nullable = false)
    private Instant createdOn;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant lastModified;
}
