package org.example.backend.repository;

import org.example.backend.entity.Role;
import org.example.backend.entity.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(String name);

    Optional<Role> findByName(RoleName name);
}
