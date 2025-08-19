package org.example.backend.repository;

import org.example.backend.entity.Priority;
import org.example.backend.constant.Type;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriorityRepository extends JpaRepository<Priority, Integer> {
    Priority findByIdAndType(Integer id, Type type);
}
