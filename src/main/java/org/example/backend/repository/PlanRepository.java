package org.example.backend.repository;

import org.example.backend.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PlanRepository extends JpaRepository<Plan, Long> {
    boolean existsByTitle(String name);

    boolean existsByTitleAndIdNot(String name, Long id);

    @Query("SELECT p FROM Plan p JOIN FETCH p.owner WHERE p.id = :id")
    Optional<Plan> findByIdWithOwner(@Param("id") Long id);

    @Query(value = "SELECT * FROM planify.plans p WHERE p.id = ?1 AND p.is_deleted = true", nativeQuery = true)
    Optional<Plan> findPlanSoftDeletedById(Long id);

    @Modifying
    @Query(value = "DELETE FROM planify.plans p WHERE p.id = ?1", nativeQuery = true)
    void hardDelete(Long id);

    @Modifying
    @Query(value = "UPDATE planify.plans SET is_deleted = false WHERE id = ?1", nativeQuery = true)
    void restore(Long id);
}
