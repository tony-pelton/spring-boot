package com.dsrts.warehouse.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.validator.constraints.ISBN;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "books")
public class BookEntity {
    @Id
    @GeneratedValue
    private Long id;

    @ISBN
    @Column(unique = true,nullable = false,length = 17)
    private String isbn;

    @CreatedDate
    @Column(nullable = false)
    private Instant createdOn;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant updatedOn;

}
