package org.example.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
        name = "statuses",
        uniqueConstraints = @UniqueConstraint(columnNames = {"name", "user_id"})
)
public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 20, unique = true)
    private String name;
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User createdBy;
}

