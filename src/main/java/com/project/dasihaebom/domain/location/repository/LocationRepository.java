package com.project.dasihaebom.domain.location.repository;

import com.project.dasihaebom.domain.location.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {

    Optional<Location> findByWorkerIdAndCorpId(Long workerId, Long corpId);
}
