package com.project.dasihaebom.global.client.location.coordinate.dto;

import java.util.List;

public record KakaoCoordinateInfoResDto (
        List<InfoItem> documents
) {
    public record InfoItem (
            String address_name,
            String x,
            String y
    ) {}
}
