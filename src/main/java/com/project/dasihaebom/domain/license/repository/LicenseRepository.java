package com.project.dasihaebom.domain.license.repository;

import com.project.dasihaebom.domain.license.entity.License;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LicenseRepository extends JpaRepository<License, Long> {

    List<License> findAllByWorkerId(long workerId);
}
