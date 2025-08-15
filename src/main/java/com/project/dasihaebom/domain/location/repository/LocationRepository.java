package com.project.dasihaebom.domain.location.repository;

import com.project.dasihaebom.domain.location.entity.Location;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {

    Optional<Location> findByWorkerIdAndCorpId(long workerId, long corpId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    void deleteByCorpId(long corpId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    void deleteByWorkerId(long workerId);
}
