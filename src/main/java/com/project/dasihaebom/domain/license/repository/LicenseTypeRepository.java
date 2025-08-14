package com.project.dasihaebom.domain.license.repository;

import com.project.dasihaebom.domain.license.dto.response.LicenseResDto;
import com.project.dasihaebom.domain.license.entity.LicenseType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LicenseTypeRepository extends JpaRepository<LicenseType, Long> {

    // THEN 0 ELSE 1 END, l.name -> 검색어가 포함된 것 중에서 검색어로 시작하면 0(먼저), 아니면 1(나중), 같은 그룸끼리는 이름순 정렬
    @Query("SELECT l " +
            "FROM LicenseType l " +
            "WHERE LOWER(l.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "ORDER BY CASE " +
                    "WHEN LOWER(l.name) LIKE LOWER(CONCAT(:keyword, '%')) THEN 0 " +
                    "ELSE 1 " +
                "END, " +
                "l.name ")
    List<LicenseType> findAllByKeyword(@Param("keyword") String keyword);
}
