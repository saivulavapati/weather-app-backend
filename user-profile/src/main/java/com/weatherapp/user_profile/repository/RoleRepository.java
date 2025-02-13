package com.weatherapp.user_profile.repository;

import com.weatherapp.user_profile.entity.Role;
import com.weatherapp.user_profile.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Integer> {

    Optional<Role> findByRoleName(RoleType roleName);
}
