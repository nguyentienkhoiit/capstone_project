package com.capstone.backend.repository;

import com.capstone.backend.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourcePermissionRepository extends JpaRepository<Resource, Long> {
}
