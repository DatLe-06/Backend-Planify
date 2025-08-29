package org.example.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.backend.constant.Type;
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

    @Column(nullable = false)
    private Long entityId;

    @Column(length = 100, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;

    @Convert(converter = ActionConverter.class)
    @Column(nullable = false)
    private Enum<?> action;

    @Column(nullable = false)
    private LocalDateTime changedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User changedBy;
}
