package org.example.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.backend.constant.Color;
import org.hibernate.annotations.SoftDelete;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "plans")
@Getter
@Setter
@SoftDelete(columnName = "is_deleted")
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, unique = true)
    private String title;
    private String description;
    private String coverPublicId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Color color = Color.DEFAULT;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "priority_id")
    private Priority priority;

    @ManyToMany
    @JoinTable(
            name = "plan_tags",
            joinColumns = @JoinColumn(name = "plan_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @Override
    public String toString() {
        return "Plan{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", coverPublicId='" + coverPublicId + '\'' +
                ", color=" + color +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", priority=" + priority +
                ", tags=" + tags +
                ", owner=" + owner +
                '}';
    }
}
