package org.example.backend.repository;

import org.example.backend.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("SELECT t FROM Task t JOIN FETCH t.plan WHERE t.id = :id")
    Optional<Task> findByIdWithPlan(@Param("id") Long id);
}
