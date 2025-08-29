package org.example.backend.service.plan;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.backend.constant.Action;
import org.example.backend.constant.CloudinaryTarget;
import org.example.backend.constant.Type;
import org.example.backend.dto.plan.AddPlanRequest;
import org.example.backend.dto.plan.PlanRequest;
import org.example.backend.dto.plan.PlanResponse;
import org.example.backend.dto.plan.UpdatePlanRequest;
import org.example.backend.entity.*;
import org.example.backend.exception.custom.PlanNameDuplicateException;
import org.example.backend.exception.custom.PlanNotFoundException;
import org.example.backend.mapper.PlanMapper;
import org.example.backend.repository.PlanRepository;
import org.example.backend.service.UploadService;
import org.example.backend.service.history.HistoryService;
import org.example.backend.service.priority.PriorityServiceImpl;
import org.example.backend.service.tag.TagServiceImpl;
import org.example.backend.service.user.UserService;
import org.example.backend.utils.MessageUtils;
import org.example.backend.utils.ReflectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PlanServiceImpl implements PlanService {
    private final PlanRepository planRepository;
    private final MessageUtils messageUtils;
    private final PlanMapper planMapper;
    private final UploadService uploadService;
    private final UserService userService;
    private final PriorityServiceImpl priorityService;
    private final TagServiceImpl tagServiceImpl;
    private final HistoryService historyService;

    @Override
    public Plan findById(Long id) {
        return planRepository.findById(id)
                .orElseThrow(() -> new PlanNotFoundException(messageUtils.getMessage("plan.not.found")));
    }

    @Transactional
    @Override
    public PlanResponse create(AddPlanRequest request) {
        User creator = userService.getCurrentUser();

        validateDuplicateTitle(request.getTitle(), null);

        Plan plan = new Plan();
        String coverPublicId = handleCoverUpload(request.getCoverFile(), null, creator);
        Priority priority = handlePriority(request);
        Set<Tag> tags = handleTags(request);

        planMapper.toEntity(request, priority, tags, coverPublicId, creator, plan);
        Plan createdPlan = planRepository.save(plan);

        historyService.createHistory(createdPlan.getId(), createdPlan.getTitle(), Type.PLAN, Action.Plan.CREATE, creator, Collections.emptyMap());
        return planMapper.toResponse(plan, uploadService.buildCloudinaryUrl(coverPublicId));
    }

    @Transactional
    @Override
    public PlanResponse update(Long id, UpdatePlanRequest request) {
        User creator = userService.getCurrentUser();
        Plan plan = findById(id);
        Plan oldPlan = new Plan();

        validateDuplicateTitle(request.getTitle(), id);

        String coverPublicId = handleCoverUpload(request.getCoverFile(), plan.getCoverPublicId(), creator);
        Priority priority = handlePriority(request);
        Set<Tag> tags = handleTags(request);

        planMapper.toEntity(request, priority, tags, coverPublicId, creator, plan);
        Map<String, String[]> changes = null;
        try {
            changes = ReflectionUtils.detectChanges(oldPlan, plan);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        if (changes.isEmpty()) throw new IllegalArgumentException(messageUtils.getMessage("plan.nothing.change"));

        Plan updatedPlan = planRepository.save(plan);
        historyService.createHistory(updatedPlan.getId(), updatedPlan.getTitle(), Type.PLAN, Action.Plan.UPDATE, creator, changes);
        return planMapper.toResponse(plan, uploadService.buildCloudinaryUrl(coverPublicId));
    }

    @Transactional
    @Override
    public PlanResponse restore(Long id) {
        User creator = userService.getCurrentUser();
        Plan plan = planRepository.findPlanSoftDeletedById(id).orElseThrow(() -> new PlanNotFoundException(messageUtils.getMessage("plan.not.found")));
        historyService.createHistory(plan.getId(), plan.getTitle(), Type.PLAN, Action.Plan.RESTORE, creator, Collections.emptyMap());
        planRepository.restore(id);
        Plan restoredPlan = findById(id);
        return planMapper.toResponse(restoredPlan, uploadService.buildCloudinaryUrl(plan.getCoverPublicId()));
    }

    @Transactional
    @Override
    public void softDelete(Long id) {
        User creator = userService.getCurrentUser();
        Plan plan = findById(id);
        historyService.createHistory(plan.getId(), plan.getTitle(), Type.PLAN, Action.Plan.SOFT_DELETE, creator, Collections.emptyMap());
        planRepository.delete(plan);
    }

    @Transactional
    @Override
    public void hardDelete(Long id) {
        User creator = userService.getCurrentUser();
        Plan plan = planRepository.findPlanSoftDeletedById(id).orElseThrow(() -> new PlanNotFoundException(messageUtils.getMessage("plan.not.found")));
        String coverPublicId = plan.getCoverPublicId();
        if (coverPublicId != null) uploadService.delete(coverPublicId);

        historyService.createHistory(plan.getId(), plan.getTitle(), Type.PLAN, Action.Plan.HARD_DELETE, creator, Collections.emptyMap());
        planRepository.hardDelete(plan.getId());
    }

    @Override
    public PlanResponse getById(Long id) {
        return planRepository.findById(id)
                .map(plan -> planMapper.toResponse(plan, uploadService.buildCloudinaryUrl(plan.getCoverPublicId())))
                .orElseThrow(() -> new PlanNotFoundException(messageUtils.getMessage("plan.not.found")));
    }

    @Override
    public List<PlanResponse> getAll() {
        return planRepository.findAll().stream()
                .map(plan -> planMapper.toResponse(plan, uploadService.buildCloudinaryUrl(plan.getCoverPublicId())))
                .collect(Collectors.toList());
    }

    private void validateDuplicateTitle(String title, Long id) {
        boolean exists = (id == null) ?
                planRepository.existsByTitle(title) :
                planRepository.existsByTitleAndIdNot(title, id);

        if (exists) {
            throw new PlanNameDuplicateException(messageUtils.getMessage("plan.name.duplicate", title));
        }
    }

    private String handleCoverUpload(MultipartFile coverFile, String oldCoverId, User user) {
        if (oldCoverId != null) uploadService.delete(oldCoverId);
        return coverFile != null ? uploadService.upload(coverFile, user, CloudinaryTarget.COVER_PLAN) : null;
    }

    private Priority handlePriority(PlanRequest request) {
        return request.getPriorityId() != null ? priorityService.findById(request.getPriorityId()) : null;
    }

    private Set<Tag> handleTags(PlanRequest request) {
        return request.getTagIds() != null ? tagServiceImpl.getAllByIds(request.getTagIds()) : null;
    }
}
