package org.example.backend.repository;

import org.example.backend.constant.PlanRole;
import org.example.backend.entity.Plan;
import org.example.backend.entity.PlanMember;
import org.example.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface PlanMemberRepository extends JpaRepository<PlanMember, Long> {
    boolean findByMemberAndPlanAndRoleIn(User member, Plan plan, Collection<PlanRole> roles);

    List<PlanMember> Plan(Plan plan);
}
