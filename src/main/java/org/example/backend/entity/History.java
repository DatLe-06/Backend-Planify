package org.example.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.backend.converter.ActionConverter;

import java.time.LocalDateTime;

@Entity
@Table(name = "histories")
@Setter
@Getter
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    @ManyToOne
    @JoinColumn(name = "plan_id")
    private Plan plan;

    @Convert(converter = ActionConverter.class)
    @Column(nullable = false)
    private Enum<?> action;

    private LocalDateTime changedAt;

    @ManyToOne
    private User changedBy;
}
