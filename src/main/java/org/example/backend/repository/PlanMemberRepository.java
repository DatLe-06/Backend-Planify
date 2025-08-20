package org.example.backend.repository;

import org.example.backend.constant.PlanRole;
import org.example.backend.entity.Plan;
import org.example.backend.entity.PlanMember;
import org.example.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PlanMemberRepository extends JpaRepository<PlanMember, Long> {
    List<PlanMember> Plan(Plan plan);
    Set<PlanMember> findByPlanId(Long planId);
    boolean existsByMemberAndPlanAndRoleIn(User member, Plan plan, Collection<PlanRole> roles);
    @Query("SELECT pM FROM PlanMember pM JOIN FETCH pM.member JOIN FETCH pM.plan WHERE pM.id = :id")
    Optional<PlanMember> findByIdWithAllData(@Param("id") Long id);

    boolean existsByMemberAndPlan(User member, Plan plan);
}
