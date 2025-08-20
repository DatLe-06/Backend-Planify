package org.example.backend.validation;

import lombok.AllArgsConstructor;
import org.example.backend.constant.PlanRole;
import org.example.backend.entity.Plan;
import org.example.backend.entity.User;
import org.example.backend.repository.PlanMemberRepository;
import org.example.backend.utils.MessageUtils;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
@AllArgsConstructor
public class PermissionValidate {
    private PlanMemberRepository planMemberRepository;
    private MessageUtils messageUtils;

    public void canViewMembers(User user, Plan plan) {
        if (notExistsByMemberAndPlanAndRoleIn(user, plan, List.of(PlanRole.values()))
                && !plan.getOwner().equals(user)) {
            throw new RuntimeException(messageUtils.getMessage("member.view.permission.denied"));
        }
    }

    public void canAddOrUpdateMember(User user, Plan plan) {
        if (notExistsByMemberAndPlanAndRoleIn(user, plan, List.of(PlanRole.All))
                && !plan.getOwner().equals(user)) {
            throw new RuntimeException(messageUtils.getMessage("member.add.permission.denied"));
        }
    }

    public void accessTaskOrSubTask(User user, Plan plan) {
        if (notExistsByMemberAndPlanAndRoleIn(user, plan, List.of(PlanRole.values()))
                && !plan.getOwner().equals(user)) {
            throw new RuntimeException(messageUtils.getMessage("task.access.permission.denied"));
        }
    }

    public void canManageSubTask(User user, Plan plan) {
        if (notExistsByMemberAndPlanAndRoleIn(user, plan, List.of(PlanRole.All, PlanRole.EDITOR))
                && !plan.getOwner().equals(user)) {
            throw new RuntimeException(messageUtils.getMessage("subtask.update.permission.denied"));
        }
    }

    private boolean notExistsByMemberAndPlanAndRoleIn(User member, Plan plan, Collection<PlanRole> roles) {
        return !planMemberRepository.existsByMemberAndPlanAndRoleIn(member, plan, roles);
    }
}
