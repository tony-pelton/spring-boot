package com.dsrts.web.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.validator.constraints.ISBN;

import java.time.Instant;

@Data
@Entity
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

    @CreationTimestamp
    @Column(nullable = false)
    private Instant createdOn;
}
