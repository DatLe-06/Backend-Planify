package org.example.backend.repository;

import org.example.backend.entity.HistoryDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface HistoryDetailRepository extends JpaRepository<HistoryDetail, Long> {
    Set<HistoryDetail> findAllByHistoryId(Long historyId);
}
