package com.project.dasihaebom.domain.resume.repository;

import com.project.dasihaebom.domain.resume.dto.request.ResumeSearchCondition;
import com.project.dasihaebom.domain.user.corp.entity.Corp;
import com.querydsl.core.Tuple;

import java.util.List;

public interface ResumeRepositoryCustom {

    List<Tuple> search(ResumeSearchCondition condition, Corp userCorp);
}
