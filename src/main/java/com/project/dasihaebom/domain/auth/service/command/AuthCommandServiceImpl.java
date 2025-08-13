package com.project.dasihaebom.domain.auth.service.command;

import com.project.dasihaebom.domain.auth.converter.AuthConverter;
import com.project.dasihaebom.domain.auth.dto.request.AuthReqDto;
import com.project.dasihaebom.domain.auth.entity.Auth;
import com.project.dasihaebom.domain.auth.exception.AuthErrorCode;
import com.project.dasihaebom.domain.auth.exception.AuthException;
import com.project.dasihaebom.domain.auth.repository.AuthRepository;
import com.project.dasihaebom.domain.user.corp.entity.Corp;
import com.project.dasihaebom.domain.user.worker.entity.Worker;
import com.project.dasihaebom.domain.user.worker.repository.WorkerRepository;
import com.project.dasihaebom.global.client.phonenumber.PhoneNumberClient;
import com.project.dasihaebom.global.util.RedisUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.project.dasihaebom.global.constant.common.CommonConstants.PASSWORD_SIZE;
import static com.project.dasihaebom.global.constant.redis.RedisConstants.KEY_SCOPE_SUFFIX;
import static com.project.dasihaebom.global.constant.scope.ScopeConstants.SCOPE_TEMP_PASSWORD;
import static com.project.dasihaebom.global.constant.valid.PatternConstants.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthCommandServiceImpl implements AuthCommandService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthRepository authRepository;
    private final RedisUtils<String> redisUtils;
    private final WorkerRepository workerRepository;
    private final PhoneNumberClient phoneNumberClient;

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

    @Override
    public String tempPassword(AuthReqDto.AuthTempPasswordReqDto authTempPasswordReqDto) {

        // 휴대폰 인증이 있는지 확인
        final String phoneNumber = authTempPasswordReqDto.phoneNumber();
        // 해당 인증이 비밀번호 변경을 위한 것인지 확인
        if (!Objects.equals(redisUtils.get(phoneNumber + KEY_SCOPE_SUFFIX), SCOPE_TEMP_PASSWORD)) {
            throw new AuthException(AuthErrorCode.PHONE_VALIDATION_DOES_NOT_EXIST);
        }

        // 존재하면 비밀번호 임시 발급 시작
        Auth auth = authRepository.findByPhoneNumber(phoneNumber)
                        .orElseThrow(() -> new AuthException(AuthErrorCode.ACCOUNT_NOT_FOUND));
        // 임시 비밀번호 생성
        final String tempPassword = createTempPassword();
        // 전송 String 생성
        final String msg = createMessageWithTempPassword(tempPassword);

        // 메시지 전송
//        try {
//            phoneNumberClient.sendMessage(msg, phoneNumber);
//        } catch (Exception e) {
//            throw new ValidationException(ValidationErrorCode.MESSAGE_SEND_ERROR);
//        }

        // 전송이 성공하면 비밀번호 변경
        auth.updatePassword(passwordEncoder.encode(tempPassword));

        // 변경 성공하면 인증 정보 삭제
        redisUtils.delete(phoneNumber + KEY_SCOPE_SUFFIX);

        return tempPassword;
    }

    private String createTempPassword() {
        SecureRandom random = new SecureRandom();
        StringBuilder tempPassword = new StringBuilder();

        List<Character> charPassword = new ArrayList<>();

        while(charPassword.size() < PASSWORD_SIZE) {
            charPassword.add(randomChar(LETTERS));
            charPassword.add(randomChar(DIGITS));
        }
        charPassword.add(randomChar(SPECIALS));

        Collections.shuffle(charPassword, random);

        for (Character c : charPassword) {
            tempPassword.append(c);
        }
        return tempPassword.toString();
    }

    private char randomChar(String pool) {
        SecureRandom random = new SecureRandom();
        return pool.charAt(random.nextInt(pool.length()));
    }

    private String createMessageWithTempPassword(String tempPassword) {
        return "[다시해봄]\n" +
                "임시 비밀번호 발급\n\n" +
                "[ " + tempPassword + " ]\n" +
                "로그인 후 비밀번호를\n" +
                "변경해 주세요";
    }
}
