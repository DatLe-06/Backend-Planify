package org.example.backend.repository;

import org.example.backend.entity.Priority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriorityRepository extends JpaRepository<Priority, Integer> {
    boolean existsByName(String name);
}
