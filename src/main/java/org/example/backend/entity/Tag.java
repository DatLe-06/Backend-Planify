package org.example.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.backend.constant.Type;

@Entity
@Table(
        name = "tags",
        uniqueConstraints = @UniqueConstraint(columnNames = {"name", "type", "user_id"})
)
@Setter
@Getter
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20)
    private String name;
    @Column(length = 200)
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User createdBy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;
}

