package org.example.backend.repository;

import org.example.backend.entity.PlanMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanMemberRepository extends JpaRepository<PlanMember, Long> {
}
