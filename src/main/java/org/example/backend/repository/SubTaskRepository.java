package org.example.backend.repository;

import org.example.backend.entity.SubTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubTaskRepository extends JpaRepository<SubTask, Long> {
    List<SubTask> findAllByTask_Id(Long taskId);
}
