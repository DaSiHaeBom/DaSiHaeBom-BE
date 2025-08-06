package com.project.dasihaebom.domain.location.repository;

import com.project.dasihaebom.domain.location.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
