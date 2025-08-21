package org.example.backend.repository;

import org.example.backend.constant.Type;
import org.example.backend.entity.Priority;
import org.example.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PriorityRepository extends JpaRepository<Priority, Integer> {
    boolean existsByNameAndTypeAndCreatedBy(String name, Type type, User createdBy);
    @Query("SELECT MAX(p.orderPriority) FROM Priority p WHERE p.createdBy.id = :id")
    Integer findMaxOrderPriority(@Param("id") Long id);
    boolean existsByNameAndTypeAndCreatedByAndIdNot(String name, Type type, User createdBy, Integer id);
    List<Priority> getAllByCreatedByAndTypeOrderByOrderPriorityAscNameAsc(User createdBy, Type type);
    List<Priority> getAllByCreatedByAndTypeOrderByOrderPriorityDescNameAsc(User createdBy, Type type);
}
