package org.example.backend.validation;

import lombok.AllArgsConstructor;
import org.example.backend.constant.PlanRole;
import org.example.backend.entity.*;
import org.example.backend.repository.PlanMemberRepository;
import org.example.backend.utils.MessageUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class PermissionValidate {
    private PlanMemberRepository planMemberRepository;
    private MessageUtils messageUtils;

    public void canManagePriority(Priority priority, User user) {
        if (!priority.getCreatedBy().equals(user)) {
            throw new RuntimeException(messageUtils.getMessage("priority.update.permission.denied"));
        }
    }

    public void canViewMembers(User user, Plan plan) {
        if (!(planMemberRepository.existsByMemberAndPlan(user, plan)
                || plan.getOwner().equals(user))) {
            throw new RuntimeException(messageUtils.getMessage("member.view.permission.denied"));
        }
    }

    public void canCreateAndUpdateMember(User user, Plan plan) {
        if (!(planMemberRepository.existsByMemberAndPlanAndRoleIn(user, plan, List.of(PlanRole.All))
                || plan.getOwner().equals(user))) {
            throw new RuntimeException(messageUtils.getMessage("member.add.permission.denied"));
        }
    }

    public void canViewSubTask(User user, Plan plan) {
        if (!(planMemberRepository.existsByMemberAndPlan(user, plan) || plan.getOwner().equals(user))) {
            throw new RuntimeException(messageUtils.getMessage("task.access.permission.denied"));
        }
    }

    public void canManageSubTask(User user, Plan plan) {
        if (!(planMemberRepository.existsByMemberAndPlanAndRoleIn(user, plan, List.of(PlanRole.All, PlanRole.EDITOR))
                || plan.getOwner().equals(user))) {
            throw new RuntimeException(messageUtils.getMessage("subtask.update.permission.denied"));
        }
    }

    public void canCreateComment(Plan plan, Task task, User creator) {
        if (plan == null) plan = task.getPlan();
        if (!(plan.getOwner().equals(creator)
                || planMemberRepository.existsByMemberAndPlanAndRoleIn(creator, plan,  List.of(PlanRole.All, PlanRole.EDITOR)))) {
            throw new RuntimeException(messageUtils.getMessage("comment.create.permission.denied"));
        }
    }

    public void canUpdateAndDeleteComment(Comment comment, User creator, Plan plan, Task task) {
        if (plan == null) plan = task.getPlan();
        boolean hasRoleOrPlanOwner = plan.getOwner().equals(creator)
                || planMemberRepository.existsByMemberAndPlanAndRoleIn(creator, plan, List.of(PlanRole.All, PlanRole.EDITOR));
        if (!(comment.getCreator().equals(creator) && hasRoleOrPlanOwner)) {
            throw new RuntimeException(messageUtils.getMessage("comment.update.permission.denied"));
        }
    }

    public void canViewComment(Plan plan, Task task, User member) {
        if (plan == null) plan = task.getPlan();
        if (!(plan.getOwner().equals(member) || planMemberRepository.existsByMemberAndPlan(member, plan))) {
            throw new RuntimeException(messageUtils.getMessage("comment.view.permission.denied"));
        }
    }
}
