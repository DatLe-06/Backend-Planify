package org.example.backend.repository;

import org.example.backend.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status, Integer> {
    boolean existsByNameAndCreatedBy_Id(String name, Long userId);
}
