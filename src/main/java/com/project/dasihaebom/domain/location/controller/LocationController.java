package com.project.dasihaebom.domain.location.controller;

import com.project.dasihaebom.domain.location.service.LocationService;
import com.project.dasihaebom.domain.location.service.LocationService;
import com.project.dasihaebom.global.apiPayload.CustomResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "location", description = "위치 관련 API")
public class LocationController {
    private final LocationService locationCommandService;

    @GetMapping()
    public CustomResponse<Double> getDistance(
            @RequestParam long workerId,
            @RequestParam long corpId
    ) {
        return CustomResponse.onSuccess(locationCommandService.getDistance(workerId, corpId));
    }
}
