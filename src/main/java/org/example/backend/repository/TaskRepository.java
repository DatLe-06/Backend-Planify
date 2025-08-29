package org.example.backend.repository;

import org.example.backend.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("SELECT t FROM Task t JOIN FETCH t.plan WHERE t.id = :id")
    Optional<Task> findByIdWithPlan(@Param("id") Long id);

    @Modifying
    @Query(value = "UPDATE tasks SET is_deleted = false WHERE id = ?1", nativeQuery = true)
    void restore(Long id);

    @Query(value = "SELECT * FROM tasks t WHERE t.id = ?1 AND t.is_deleted = true", nativeQuery = true)
    Optional<Task> findTaskSoftDeletedById(Long id);

    @Modifying
    @Query(value = "DELETE FROM tasks WHERE id = ?1", nativeQuery = true)
    void hardDelete(Long id);
}
