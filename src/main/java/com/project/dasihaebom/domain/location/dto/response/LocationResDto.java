package com.project.dasihaebom.domain.location.dto.response;

import lombok.Builder;

public class LocationResDto {

    @Builder
    public record CoordinateInfoResDto(
            Double wLat,
            Double wLng,
            Double cLat,
            Double cLng
    ) {
    }
}
