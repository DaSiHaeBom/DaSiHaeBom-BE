package com.project.dasihaebom.domain.location.service;

import com.project.dasihaebom.domain.location.converter.LocationConverter;
import com.project.dasihaebom.domain.location.dto.response.LocationResDto;
import com.project.dasihaebom.domain.location.entity.Location;
import com.project.dasihaebom.domain.location.repository.LocationRepository;
import com.project.dasihaebom.domain.user.corp.entity.Corp;
import com.project.dasihaebom.domain.user.corp.exception.CorpErrorCode;
import com.project.dasihaebom.domain.user.corp.exception.CorpException;
import com.project.dasihaebom.domain.user.corp.repository.CorpRepository;
import com.project.dasihaebom.domain.user.worker.entity.Worker;
import com.project.dasihaebom.domain.user.worker.exception.WorkerErrorCode;
import com.project.dasihaebom.domain.user.worker.exception.WorkerException;
import com.project.dasihaebom.domain.user.worker.repository.WorkerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.project.dasihaebom.global.util.CoordinateUtils.getLat;
import static com.project.dasihaebom.global.util.CoordinateUtils.getLng;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class LocationServiceImpl implements LocationService {

    private final WorkerRepository workerRepository;
    private final CorpRepository corpRepository;
    private final LocationRepository locationRepository;

    @Override
    // 개인과 기업의 아이디를 입력받으면 거리를 반환
    public double getDistance(long workerId, long corpId) {
        Optional<Location> locationOpt = locationRepository.findByWorkerIdAndCorpId(workerId, corpId);

        // 거리 정보가 비어 있다면 -> 첫 생성
        if (locationOpt.isEmpty()) {
            log.info("거리 정보가 존재하지 않아 거리 정보를 생성합니다.");
            Location location = createDistance(workerId, corpId);
            log.info("거리 정보 생성이 완료되었습니다.");
            return location.getDistance();
        }

        // 이미 존재하는 거리 정보 -> 생성할 필요 없이 바로 반환
        Location location = locationOpt.get();

        return location.getDistance();
    }

    // 거리 정보가 없을 때, 거리 정보를 생성하는 메서드
    private Location createDistance(long workerId, long corpId) {

        Worker worker = workerRepository.findById(workerId)
                .orElseThrow(() -> new WorkerException(WorkerErrorCode.WORKER_NOT_FOUND));

        Corp corp = corpRepository.findById(corpId)
                .orElseThrow(() -> new CorpException(CorpErrorCode.CORP_NOT_FOUND));

        List<Double> workerCoordinates = worker.getCoordinates();
        List<Double> corpCoordinates = corp.getCoordinates();

        double distance = calculateDistance(
                getLat(workerCoordinates),
                getLng(workerCoordinates),
                getLat(corpCoordinates),
                getLng(corpCoordinates));

        Location location = LocationConverter.toLocation(worker, corp, distance);

        locationRepository.save(location);

        return location;
    }

    // 하버사인 공식을 이용해 구면에서의 거리 반환
    private double calculateDistance(double lat1, double lng1, double lat2, double lng2) {
        final double EARTH_RADIUS = 6371.0;

        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);

        double haversineFormulaPart = Math.sin(dLat/2) * Math.sin(dLat/2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLng /2) * Math.sin(dLng /2);
        double angularDistance = 2 * Math.atan2(Math.sqrt(haversineFormulaPart), Math.sqrt(1 - haversineFormulaPart));

        double distanceKm = EARTH_RADIUS * angularDistance;

        return Math.round(distanceKm * 10.0) / 10.0; // 소수점 첫째 자리까지 반올림
    }
}
