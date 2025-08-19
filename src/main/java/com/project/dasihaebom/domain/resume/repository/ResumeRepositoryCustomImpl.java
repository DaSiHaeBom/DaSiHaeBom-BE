package com.project.dasihaebom.domain.resume.repository;

import com.project.dasihaebom.domain.license.entity.QLicense;
import com.project.dasihaebom.domain.resume.dto.request.ResumeSearchCondition;
import com.project.dasihaebom.domain.resume.entity.QResume;
import com.project.dasihaebom.domain.user.corp.entity.Corp;
import com.project.dasihaebom.domain.user.worker.entity.QWorker;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ResumeRepositoryCustomImpl implements ResumeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Tuple> search(ResumeSearchCondition condition, Corp userCorp) {
        QResume resume = QResume.resume;
        QWorker worker = QWorker.worker;

        NumberExpression<Double> distanceExpression = Expressions.numberTemplate(Double.class,
                "ST_DISTANCE_SPHERE(point({0}, {1}), point(worker.coordinates.longitude, worker.coordinates.latitude))",
                userCorp.getCoordinates().getLongitude(), userCorp.getCoordinates().getLatitude()
        ).divide(1000); //km단위로 나열

        OrderSpecifier<?>[] orderSpecifiers = getOrderSpecifiers(condition.getSortBy(), distanceExpression);

        return queryFactory
                .select(resume, distanceExpression)
                .from(resume)
                .join(resume.worker, worker).fetchJoin()
                .where(
                        cursorCondition(condition, distanceExpression),
                        ageCondition(condition.getMinAge(), condition.getMaxAge()),
                        licenseCondition(condition.getLicenses())
                )
                .orderBy(orderSpecifiers)
                .limit(condition.getSize() + 1)
                .fetch();
    }

    private BooleanExpression cursorCondition(ResumeSearchCondition condition, NumberExpression<Double> distanceEx) {
        if ("distance".equalsIgnoreCase(condition.getSortBy())) {
            if (condition.getCursorId() == null || condition.getCursorDistance() == null) return null;
            return distanceEx.gt(condition.getCursorDistance())
                    .or(distanceEx.eq(condition.getCursorDistance()).and(QResume.resume.id.gt(condition.getCursorId())));
        }
        if (condition.getCursorId() == null) return null;
        return QResume.resume.id.lt(condition.getCursorId());
    }

    private OrderSpecifier<?>[] getOrderSpecifiers(String sortBy, NumberExpression<Double> distanceEx) {
        if ("distance".equalsIgnoreCase(sortBy)) {
            return new OrderSpecifier[]{distanceEx.asc(), QResume.resume.id.asc()};
        }
        return new OrderSpecifier[]{QResume.resume.id.desc()};
    }


    private BooleanExpression ageCondition(Integer minAge, Integer maxAge) {

        QResume resume = QResume.resume;

        if (minAge == null && maxAge == null) {
            return null;
        }

        LocalDate now = LocalDate.now();
        BooleanExpression condition = null;

        if (maxAge != null) {
            LocalDate minBirthDate = now.minusYears(maxAge).minusYears(1).plusDays(1);
            condition = resume.birthDate.goe(minBirthDate);
        }

        if (minAge != null) {
            LocalDate maxBirthDate = now.minusYears(minAge);
            BooleanExpression minAgeCondition = resume.birthDate.loe(maxBirthDate);
            condition = (condition == null) ? minAgeCondition : condition.and(minAgeCondition);
        }

        return condition;
    }

    private BooleanExpression licenseCondition(List<String> licenses) {
        // 요청된 자격증 목록이 없으면 필터링하지 않음
        if (licenses == null || licenses.isEmpty()) {
            return null;
        }

        QResume resume = QResume.resume;
        QLicense license = QLicense.license;

        // "license 테이블에서 현재 resume의 worker와 동일한 worker를 가지면서 그 license의 이름이 요청된 자격증 목록에 포함된 것이 존재하는지"
        return license.worker.eq(resume.worker)
                .and(license.name.in(licenses));
    }
}