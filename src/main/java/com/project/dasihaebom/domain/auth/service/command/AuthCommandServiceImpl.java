package com.project.dasihaebom.domain.auth.service.command;

import com.project.dasihaebom.domain.auth.converter.AuthConverter;
import com.project.dasihaebom.domain.auth.dto.request.AuthReqDto;
import com.project.dasihaebom.domain.auth.entity.Auth;
import com.project.dasihaebom.domain.auth.exception.AuthErrorCode;
import com.project.dasihaebom.domain.auth.exception.AuthException;
import com.project.dasihaebom.domain.auth.repository.AuthRepository;
import com.project.dasihaebom.domain.user.corp.entity.Corp;
import com.project.dasihaebom.domain.user.corp.exception.CorpErrorCode;
import com.project.dasihaebom.domain.user.corp.exception.CorpException;
import com.project.dasihaebom.domain.user.corp.repository.CorpRepository;
import com.project.dasihaebom.domain.user.corp.service.query.CorpQueryService;
import com.project.dasihaebom.domain.user.worker.entity.Worker;
import com.project.dasihaebom.domain.user.worker.repository.WorkerRepository;
import com.project.dasihaebom.domain.user.worker.service.query.WorkerQueryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthCommandServiceImpl implements AuthCommandService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthRepository authRepository;

    @Override
    public void savePassword(Object user, String encodedPassword) {

        // 지금 비밀번호를 저장할 객체가 Worker 인가?
        if (user instanceof Worker worker) {
            Auth workerAuth = AuthConverter.toWorkerAuth(encodedPassword, worker);
            authRepository.save(workerAuth);
        }
        if (user instanceof Corp corp) {
            Auth corpAuth = AuthConverter.toCorpAuth(encodedPassword, corp);
            authRepository.save(corpAuth);
        }
    }

    @Override
    public void changePassword(AuthReqDto.AuthPasswordChangeReqDto authPasswordChangeReqDto, String loginId) {

        // 새 비밀번호와 비밀번호 입력 확인이 다를 경우
        if (!authPasswordChangeReqDto.newPassword().equals(authPasswordChangeReqDto.newPasswordConfirmation())) {
            throw new AuthException(AuthErrorCode.NEW_PASSWORD_DOES_NOT_MATCH);
        }

        // Auth 객체 찾기 / Auth가 역정규화 되어 있어 바로 찾을 수 있음
        Auth auth = authRepository.findByLoginId(loginId)
                .orElseThrow(() -> new AuthException(AuthErrorCode.ACCOUNT_NOT_FOUND));

        // 현재 비밀번호가 틀렸을 때
        if (!passwordEncoder.matches(authPasswordChangeReqDto.currentPassword(), auth.getPassword())) {
            throw new AuthException(AuthErrorCode.CURRENT_PASSWORD_DOES_NOT_MATCH);
        }

        // 기존의 비밀번호와 새 비밀번호가 동일할 때
        if (passwordEncoder.matches(authPasswordChangeReqDto.newPassword(), auth.getPassword())) {
            throw new AuthException(AuthErrorCode.NEW_PASSWORD_IS_CURRENT_PASSWORD);
        }

        // 영속 상태인 객체 에서 auth를 가져왔으므로 auth도 영속 더티 채킹 O
        auth.updatePassword(passwordEncoder.encode(authPasswordChangeReqDto.newPassword()));
    }
}
