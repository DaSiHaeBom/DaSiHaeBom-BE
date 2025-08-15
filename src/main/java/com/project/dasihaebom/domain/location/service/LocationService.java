package com.project.dasihaebom.domain.location.service;

import java.util.List;

public interface LocationService {

    double getDistance(long workerId, long corpId);

    List<Double> getDistanceList(List<Long> workerId, long corpId);
}
