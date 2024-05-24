package com.jaewon.oauth2.repository;

import com.jaewon.oauth2.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String user);
}
