package com.project.dasihaebom.domain.resume.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ResumeSearchCondition {
    private int size;
    private Long cursorId;
    private Double cursorDistance;
    private String sortBy;
    private Integer minAge;
    private Integer maxAge;
    private List<String> licenses;
    private Double latitude;
    private Double longitude;

    public void setLatitude(Double latitude) {this.latitude = latitude;}

    public void setLongitude(Double longitude) {this.longitude = longitude;}
}
