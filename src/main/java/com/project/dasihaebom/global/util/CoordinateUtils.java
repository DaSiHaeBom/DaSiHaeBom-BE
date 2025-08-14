package com.project.dasihaebom.global.util;

import java.util.List;

public class CoordinateUtils {

    public static Double getLng(List<Double> coordinates) {
        return coordinates.get(0);
    }

    public static Double getLat(List<Double> coordinates) {
        return coordinates.get(1);
    }
}
