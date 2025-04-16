package com.dsrts.web.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.ISBN;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "books")
public class BookEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true,nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

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
