package com.project.dasihaebom.domain.location.converter;

import com.project.dasihaebom.domain.location.dto.response.LocationResDto;
import com.project.dasihaebom.domain.location.entity.Location;
import com.project.dasihaebom.domain.user.corp.entity.Corp;
import com.project.dasihaebom.domain.user.worker.entity.Worker;
import com.project.dasihaebom.global.client.location.coordinate.dto.KakaoCoordinateInfoResDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

import static com.project.dasihaebom.global.util.CoordinateUtils.getLat;
import static com.project.dasihaebom.global.util.CoordinateUtils.getLng;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LocationConverter {

    public static Location toLocation(Worker worker, Corp corp, double distance) {
        return Location.builder()
                .worker(worker)
                .corp(corp)
                .distance(distance)
                .build();
    }

    // TODO : 정리 하기
    public static List<Double> toCoordinateList(KakaoCoordinateInfoResDto kakaoCoordinateInfoResDto ) {
        KakaoCoordinateInfoResDto.InfoItem coordinatesInfo = kakaoCoordinateInfoResDto.documents().get(0);

        return List.of(Double.parseDouble(coordinatesInfo.x()), Double.parseDouble(coordinatesInfo.y()));
    }
}
