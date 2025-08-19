package org.example.backend.validation;

import lombok.AllArgsConstructor;
import org.example.backend.constant.PlanRole;
import org.example.backend.entity.Plan;
import org.example.backend.entity.User;
import org.example.backend.repository.PlanMemberRepository;
import org.example.backend.utils.MessageUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class PermissionValidate {
    private PlanMemberRepository planMemberRepository;
    private MessageUtils messageUtils;

    public void accessTaskOrSubTask(User user, Plan plan) {
        if (!planMemberRepository.findByMemberAndPlanAndRoleIn(user, plan, List.of(PlanRole.All, PlanRole.VIEWER, PlanRole.EDITOR))
                && !plan.getOwner().equals(user)) {
            throw new RuntimeException(messageUtils.getMessage("task.access.permission.denied"));
        }
    }

    public void canManageSubTask(User user, Plan plan) {
        if (!planMemberRepository.findByMemberAndPlanAndRoleIn(user, plan, List.of(PlanRole.All, PlanRole.EDITOR))
                && !plan.getOwner().equals(user)) {
            throw new RuntimeException(messageUtils.getMessage("subtask.update.permission.denied"));
        }
    }
}
