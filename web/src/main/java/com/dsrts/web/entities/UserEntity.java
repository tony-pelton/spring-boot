package com.dsrts.web.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;

import java.time.Instant;
import java.util.Set;

@Data
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Email
    @Column(unique = true,nullable = false)
    private String email;

    private String firstName;
    private String lastName;

    @OneToMany(mappedBy = "user")
    private Set<CartEntity> cartEntitySet;

    @CreationTimestamp(source = SourceType.VM)
    @Column(nullable = false)
    private Instant createdOn;
}
