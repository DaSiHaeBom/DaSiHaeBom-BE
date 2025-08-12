package com.project.dasihaebom.domain.user.corp.service.query;

import com.project.dasihaebom.domain.user.corp.entity.Corp;

import java.util.Optional;

public interface CorpQueryService {

    Optional<Corp> findCorpByLoginId(String loginId);
}
