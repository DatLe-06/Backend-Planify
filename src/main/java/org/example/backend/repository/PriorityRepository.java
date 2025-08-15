package org.example.backend.repository;

import org.example.backend.entity.Priority;
import org.example.backend.entity.Type;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PriorityRepository extends JpaRepository<Priority, Integer> {
    Priority findByIdAndType(Integer id, Type type);
}
