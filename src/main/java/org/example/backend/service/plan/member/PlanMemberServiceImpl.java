package org.example.backend.service.plan.member;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.backend.constant.Action;
import org.example.backend.constant.PlanRole;
import org.example.backend.constant.Type;
import org.example.backend.dto.plan.member.CreatePlanMemberRequest;
import org.example.backend.dto.plan.member.PlanMemberResponse;
import org.example.backend.dto.plan.member.UpdatePlanMemberRequest;
import org.example.backend.dto.user.ProfileResponse;
import org.example.backend.entity.Plan;
import org.example.backend.entity.PlanMember;
import org.example.backend.entity.User;
import org.example.backend.exception.custom.PlanNotFoundException;
import org.example.backend.exception.custom.UserNotFoundException;
import org.example.backend.mapper.PlanMemberMapper;
import org.example.backend.mapper.UserMapper;
import org.example.backend.repository.PlanMemberRepository;
import org.example.backend.repository.PlanRepository;
import org.example.backend.repository.UserRepository;
import org.example.backend.service.UploadService;
import org.example.backend.service.history.HistoryService;
import org.example.backend.service.user.UserService;
import org.example.backend.utils.MessageUtils;
import org.example.backend.validation.PermissionValidate;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PlanMemberServiceImpl implements PlanMemberService {
    private final MessageUtils messageUtils;
    private final UploadService uploadService;
    private final UserService userService;
    private final HistoryService historyService;
    private final PlanMemberRepository planMemberRepository;
    private final PlanRepository planRepository;
    private final UserRepository userRepository;
    private final PlanMemberMapper planMemberMapper;
    private final UserMapper userMapper;
    private final PermissionValidate permissionValidate;

    @Transactional
    @Override
    public PlanMemberResponse create(CreatePlanMemberRequest request) {
        User user = userService.getCurrentUser();

        Plan plan = planRepository.findByIdWithOwner(request.getPlanId())
                .orElseThrow(() -> new PlanNotFoundException(messageUtils.getMessage("plan.not.found")));
        User member = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException(messageUtils.getMessage("user.not.found")));

        if (user.equals(member)) throw new IllegalArgumentException(messageUtils.getMessage("plan.member.cannot.be.add"));

        if (planMemberRepository.existsByMemberAndPlan(member, plan)) {
            throw new IllegalArgumentException(messageUtils.getMessage("plan.member.already.exist"));
        }

        ProfileResponse profile = userMapper.toResponse(member, uploadService.buildCloudinaryUrl(member.getAvatarPublicId()));

        PlanMember planMember = planMemberMapper.toEntityForCreate(plan, member, PlanRole.valueOf(request.getRole()));

        permissionValidate.canAddOrUpdateMember(user, plan);
        planMemberRepository.save(planMember);
        historyService.createHistory(Type.PLAN, request.getPlanId(), plan.getTitle(), Action.Member.ADD, user);
        return planMemberMapper.toResponse(planMember, request.getPlanId(), profile, request.getRole());
    }

    @Transactional
    @Override
    public PlanMemberResponse update(Long id, UpdatePlanMemberRequest request) {
        User user = userService.getCurrentUser();

        PlanMember planMember = planMemberRepository.findByIdWithAllData(id)
                .orElseThrow(() -> new EntityNotFoundException(messageUtils.getMessage("plan.member.not.found")));

        Plan plan = planMember.getPlan();
        User member = planMember.getMember();

        if (user.equals(member)) throw new IllegalArgumentException(messageUtils.getMessage("plan.member.cannot.be.update"));
        if (planMember.getRole().equals(PlanRole.All)) throw new IllegalArgumentException(messageUtils.getMessage("plan.member.cannot.be.update"));

        ProfileResponse profile = userMapper.toResponse(member, uploadService.buildCloudinaryUrl(member.getAvatarPublicId()));

        planMemberMapper.toEntityForUpdate(planMember, request.getRole());

        permissionValidate.canAddOrUpdateMember(user, plan);
        planMemberRepository.save(planMember);
        historyService.createHistory(Type.PLAN, request.getPlanId(), plan.getTitle(), Action.Member.UPDATE_ROLE, user);

        return planMemberMapper.toResponse(planMember, plan.getId(), profile, request.getRole());
    }

    @Transactional
    @Override
    public void delete(Long id) {
        User user = userService.getCurrentUser();
        PlanMember planMember = planMemberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(messageUtils.getMessage("plan.member.not.found")));
        Plan plan = planMember.getPlan();
        historyService.createHistory(Type.PLAN, plan.getId(), plan.getTitle(), Action.Member.REMOVE, user);
        planMemberRepository.deleteById(id);
    }

    @Override
    public Set<PlanMemberResponse> getMembersInPlan(Long id) {
        User user = userService.getCurrentUser();
        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> new PlanNotFoundException(messageUtils.getMessage("plan.not.found")));

        permissionValidate.canViewMembers(user, plan);
        Set<PlanMember> planMembers = planMemberRepository.findByPlanId(id);
        return planMembers.stream().map(planMember -> {
            User member = planMember.getMember();
            return planMemberMapper.toResponse(
                    planMember, plan.getId(),
                    userMapper.toResponse(
                            member,
                            uploadService.buildCloudinaryUrl(member.getAvatarPublicId())
                    ),
                    planMember.getRole().name()
            );
        }).collect(Collectors.toSet());
    }
}

