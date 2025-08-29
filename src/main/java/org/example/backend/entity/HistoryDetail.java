package org.example.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "history_details")
@Getter
@Setter
public class HistoryDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fieldName;

    private String oldValue;
    private String newValue;

    @ManyToOne
    @JoinColumn(name = "history_id", nullable = false)
    private History history;
}

