package org.example.backend.repository;

import org.example.backend.entity.Comment;
import org.example.backend.entity.Plan;
import org.example.backend.entity.Task;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT c FROM Comment c " +
            "LEFT JOIN FETCH c.plan " +
            "LEFT JOIN FETCH c.task " +
            "JOIN FETCH c.creator WHERE c.id = :id")
    Optional<Comment> findByIdWithData(@Param("id") Long id);
    List<Comment> findAllByPlanOrderByCreatedAtDesc(Plan plan, Pageable pageable);
    List<Comment> findAllByTaskOrderByCreatedAtDesc(Task task, Pageable pageable);
}
