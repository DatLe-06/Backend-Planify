package org.example.backend.repository;

import org.example.backend.constant.Type;
import org.example.backend.entity.History;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepository extends JpaRepository<History, Long> {
    Page<History> findAllByIdAndTypeOrderByChangedAtDesc(Long targetId, Type type, Pageable pageable);
}
