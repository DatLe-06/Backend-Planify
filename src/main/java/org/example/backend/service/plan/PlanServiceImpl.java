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
import org.example.backend.service.priority.PriorityService;
import org.example.backend.service.tag.TagService;
import org.example.backend.service.user.UserService;
import org.example.backend.utils.MessageUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PlanServiceImpl implements PlanService {
    private final PlanRepository planRepository;
    private final MessageUtils messageUtils;
    private final HistoryService historyService;
    private final PlanMapper planMapper;
    private final UploadService uploadService;
    private final UserService userService;
    private final PriorityService priorityService;
    private final TagService tagService;

    @Override
    public Plan findById(Long id) {
        return planRepository.findById(id)
                .orElseThrow(() -> new PlanNotFoundException(messageUtils.getMessage("plan.not.found")));
    }

    @Transactional
    @Override
    public PlanResponse create(AddPlanRequest request) {
        User user = userService.getCurrentUser();

        validateDuplicateTitle(request.getTitle(), null);

        Plan plan = new Plan();
        String coverPublicId = handleCoverUpload(request.getCoverFile(), null, user);
        Priority priority = handlePriority(request);
        Set<Tag> tags = handleTags(request);

        planMapper.toEntity(request, priority, tags, coverPublicId, user, plan);
        planRepository.save(plan);

        historyService.createHistory(Type.PLAN, plan.getId(), plan.getTitle(), Action.Plan.CREATE, plan.getOwner());
        return planMapper.toResponse(plan, uploadService.buildCloudinaryUrl(coverPublicId));
    }

    @Transactional
    @Override
    public PlanResponse update(Long id, UpdatePlanRequest request) {
        User user = userService.getCurrentUser();
        Plan plan = findById(id);

        validateDuplicateTitle(request.getTitle(), id);

        String coverPublicId = handleCoverUpload(request.getCoverFile(), plan.getCoverPublicId(), user);
        Priority priority = handlePriority(request);
        Set<Tag> tags = handleTags(request);

        planMapper.toEntity(request, priority, tags, coverPublicId, user, plan);

        planRepository.save(plan);
        historyService.createHistory(Type.PLAN, plan.getId(), plan.getTitle(), Action.Plan.UPDATE, plan.getOwner());
        return planMapper.toResponse(plan, uploadService.buildCloudinaryUrl(coverPublicId));
    }

    @Transactional
    @Override
    public String delete(Long id) {
        Plan plan = findById(id);
        String coverPublicId = plan.getCoverPublicId();
        if (coverPublicId != null) uploadService.delete(coverPublicId);

        History history = historyService.createHistory(Type.PLAN, plan.getId(), plan.getTitle(), Action.Plan.DELETE, plan.getOwner());
        planRepository.delete(plan);

        return messageUtils.getMessage("delete.plan.success", history.getName());
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
        return request.getTagIds() != null ? tagService.getAllByIds(request.getTagIds()) : null;
    }
}
