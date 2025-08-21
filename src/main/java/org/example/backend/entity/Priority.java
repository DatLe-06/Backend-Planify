package org.example.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.backend.constant.Type;

@Getter
@Setter
@Entity
@Table(
        name = "priorities",
        uniqueConstraints = @UniqueConstraint(columnNames = {"name", "type","user_id"})
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
    private Type type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User createdBy;
}

