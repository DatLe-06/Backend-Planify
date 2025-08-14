package org.example.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
        name = "priorities",
        uniqueConstraints = @UniqueConstraint(columnNames = {"name", "type"})
)
public class Priority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 20)
    private String name;
    private String description;
    private Integer orderPriority;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PriorityType type;
}

