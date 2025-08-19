package org.example.backend.init;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.backend.constant.RoleName;
import org.example.backend.constant.Type;
import org.example.backend.entity.*;
import org.example.backend.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EntityManager entityManager;
    private final RoleRepository roleRepository;
    private final StatusRepository statusRepository;
    private final TagRepository tagRepository;
    private final PriorityRepository priorityRepository;

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        initUser();
        initPriorities();
        initTags();
        initStatuses();
    }

    private void initUser() {
        Role userRole = roleRepository.findByName(RoleName.ROLE_USER).orElseGet(() -> {
            Role newRole = new Role();
            newRole.setName(RoleName.ROLE_USER);
            return roleRepository.save(newRole);
        });

        Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN).orElseGet(() -> {
            Role newRole = new Role();
            newRole.setName(RoleName.ROLE_ADMIN);
            return roleRepository.save(newRole);
        });

        if (userRepository.findByEmail("user@gmail.com").isEmpty()) {
            User user = new User();
            user.setUsername("user");
            user.setEmail("user@gmail.com");
            user.setPassword(passwordEncoder.encode("password"));
            user.setEnabled(true);
            user.setCreatedAt(LocalDateTime.now());
            user.setRole(entityManager.merge(userRole));
            userRepository.save(user);
        }

        if (userRepository.findByEmail("admin@gmail.com").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@gmail.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEnabled(true);
            admin.setCreatedAt(LocalDateTime.now());
            admin.setRole(entityManager.merge(adminRole));

            userRepository.save(admin);
            System.out.println("Tạo tài khoản admin thành công!");
        } else {
            System.out.println("Tài khoản admin đã tồn tại, không cần tạo mới.");
        }
    }

    private void initPriorities() {
        if (priorityRepository.count() == 0) {
            List<Priority> priorities = Arrays.asList(
                    createPriority("Low", "Low priority for project", 1, Type.PLAN),
                    createPriority("Medium", "Medium priority for project", 2, Type.PLAN),
                    createPriority("High", "High priority for project", 3, Type.PLAN),
                    createPriority("Low", "Low priority for task", 1, Type.TASK),
                    createPriority("Medium", "Medium priority for task", 2, Type.TASK),
                    createPriority("High", "High priority for task", 3, Type.TASK)
            );
            priorityRepository.saveAll(priorities);
            System.out.println("✅ Inserted default priorities");
        }
    }

    private void initTags() {
        if (tagRepository.count() == 0) {
            List<Tag> tags = Arrays.asList(
                    createTag("Home", "Home related tasks", Type.PLAN),
                    createTag("Work", "Work related tasks", Type.PLAN),
                    createTag("Job", "Job related tasks", Type.TASK),
                    createTag("Study", "Study related tasks", Type.TASK)
            );
            tagRepository.saveAll(tags);
            System.out.println("✅ Inserted default tags");
        }
    }

    private void initStatuses() {
        if (statusRepository.count() == 0) {
            List<Status> statuses = Arrays.asList(
                    createStatus("Not Started", "Task not started yet"),
                    createStatus("In Progress", "Task is in progress"),
                    createStatus("Completed", "Task completed successfully")
            );
            statusRepository.saveAll(statuses);
            System.out.println("✅ Inserted default statuses");
        }
    }

    private Priority createPriority(String name, String description, int order, Type type) {
        Priority p = new Priority();
        p.setName(name);
        p.setDescription(description);
        p.setOrderPriority(order);
        p.setType(type);
        return p;
    }

    private Tag createTag(String name, String description, Type type) {
        Tag t = new Tag();
        t.setName(name);
        t.setDescription(description);
        t.setType(type);
        return t;
    }

    private Status createStatus(String name, String description) {
        Status s = new Status();
        s.setName(name);
        s.setDescription(description);
        return s;
    }
}
