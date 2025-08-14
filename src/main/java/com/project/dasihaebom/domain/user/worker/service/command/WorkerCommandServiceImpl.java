package com.project.dasihaebom.domain.user.worker.service.command;

import com.project.dasihaebom.domain.auth.service.command.AuthCommandService;
import com.project.dasihaebom.domain.location.converter.LocationConverter;
import com.project.dasihaebom.domain.location.repository.LocationRepository;
import com.project.dasihaebom.domain.user.corp.converter.CorpConverter;
import com.project.dasihaebom.domain.user.corp.entity.Corp;
import com.project.dasihaebom.domain.user.worker.converter.WorkerConverter;
import com.project.dasihaebom.domain.user.worker.dto.request.WorkerReqDto;
import com.project.dasihaebom.domain.user.worker.entity.Worker;
import com.project.dasihaebom.domain.user.worker.exception.WorkerErrorCode;
import com.project.dasihaebom.domain.user.worker.exception.WorkerException;
import com.project.dasihaebom.domain.user.worker.repository.WorkerRepository;
import com.project.dasihaebom.global.client.location.coordinate.CoordinateClient;
import com.project.dasihaebom.global.util.RedisUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static com.project.dasihaebom.global.constant.redis.RedisConstants.KEY_SCOPE_SUFFIX;
import static com.project.dasihaebom.global.constant.scope.ScopeConstants.SCOPE_SIGNUP;
import static com.project.dasihaebom.global.util.UpdateUtils.updateIfChanged;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class WorkerCommandServiceImpl implements WorkerCommandService {

    // Repo
    private final WorkerRepository workerRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // Service
    private final AuthCommandService authCommandService;

    private final CoordinateClient coordinateClient;

    private final RedisUtils<String> redisUtils;
    private final LocationRepository locationRepository;


    @Override
    public void createWorker(WorkerReqDto.WorkerCreateReqDto workerCreateReqDto) {

        // 휴대폰 인증이 있는지 확인
        final String phoneNumber = workerCreateReqDto.phoneNumber();
        // 해당 인증이 회원 가입을 위한 것인지 확인
        if (!Objects.equals(redisUtils.get(phoneNumber + KEY_SCOPE_SUFFIX), SCOPE_SIGNUP)) {
            throw new WorkerException(WorkerErrorCode.PHONE_VALIDATION_DOES_NOT_EXIST);
        }

        final String address = workerCreateReqDto.address();
        List<Double> workerCoordinates = LocationConverter.toCoordinateList(coordinateClient.getKakaoCoordinateInfo(address));
        Worker worker = WorkerConverter.toWorker(workerCreateReqDto, workerCoordinates);

        try {
            workerRepository.save(worker);
            authCommandService.savePassword(worker, encodePassword(workerCreateReqDto.password()));
        } catch (DataIntegrityViolationException e) {
            throw new WorkerException(WorkerErrorCode.WORKER_DUPLICATED);
        }

        // 가입 성공 시 인증 정보 삭제
        redisUtils.delete(phoneNumber + KEY_SCOPE_SUFFIX);
    }

    @Override
    public void updateWorker(WorkerReqDto.WorkerUpdateReqDto workerUpdateReqDto, long workerId) {
        Worker worker = workerRepository.findById(workerId)
                .orElseThrow(() -> new WorkerException(WorkerErrorCode.WORKER_NOT_FOUND));

        // findById로 가져온 객체는 영속성 컨텍스트 안이라서 더티채킹 어쩌고저쩌고쏼라쏼라
        updateIfChanged(workerUpdateReqDto.phoneNumber(), worker.getPhoneNumber(), worker::changePhoneNumber);
        updateIfChanged(workerUpdateReqDto.username(), worker.getUsername(), worker::changeUsername);
        updateIfChanged(workerUpdateReqDto.birthDate(), worker.getBirthDate(), worker::changeBirthDate);

        if (!workerUpdateReqDto.address().equals(worker.getAddress())) {
            updateIfChanged(workerUpdateReqDto.address(), worker.getAddress(), worker::changeAddress);

            // 변경된 주소로 좌표 api 호출
            final String addressToUpdate = workerUpdateReqDto.address();
            final List<Double> coordinatesToUpdate = LocationConverter.toCoordinateList(coordinateClient.getKakaoCoordinateInfo(addressToUpdate));
            // 주소 변경으로 인한 좌표 변경
            worker.changeCoordinates(coordinatesToUpdate);
            // 기존에 연결되어 있던 거리 캐시 삭제
            locationRepository.deleteByWorkerId(workerId);
        }
    }

    private String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
}
