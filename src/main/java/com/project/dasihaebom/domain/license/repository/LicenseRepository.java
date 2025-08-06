package com.project.dasihaebom.domain.license.repository;

import com.project.dasihaebom.domain.license.entity.License;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LicenseRepository extends JpaRepository<License, Long> {
}
