package com.project.dasihaebom.global.security.userdetails;

import com.project.dasihaebom.domain.auth.entity.CorpAuth;
import com.project.dasihaebom.domain.auth.entity.WorkerAuth;
import com.project.dasihaebom.domain.auth.exception.AuthErrorCode;
import com.project.dasihaebom.domain.auth.exception.AuthException;
import com.project.dasihaebom.domain.auth.repository.CorpAuthRepository;
import com.project.dasihaebom.domain.auth.repository.WorkerAuthRepository;
import com.project.dasihaebom.domain.user.corp.entity.Corp;
import com.project.dasihaebom.domain.user.corp.repository.CorpRepository;
import com.project.dasihaebom.domain.user.worker.entity.Worker;
import com.project.dasihaebom.domain.user.worker.repository.WorkerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final WorkerRepository workerRepository;
    private final CorpRepository corpRepository;
    private final WorkerAuthRepository workerAuthRepository;
    private final CorpAuthRepository corpAuthRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

        log.info("[ CustomUserDetailsService ] UserId 를 이용하여 User 를 검색합니다.");

        // Worker 로그인
        Optional<Worker> workerOpt = workerRepository.findByPhoneNumber(userId);
        if (workerOpt.isPresent()) {
            Worker worker = workerOpt.get();
            WorkerAuth workerAuth = workerAuthRepository.findByWorker(worker)
                    .orElseThrow(() -> new AuthException(AuthErrorCode.AUTH_NOT_FOUND));
            return new CustomUserDetails(worker.getId(), worker.getPhoneNumber(), workerAuth.getPassword(), worker.getRole());
        }

        // Corp 로그인
        Optional<Corp> corpOpt = corpRepository.findByLoginId(userId);
        if (corpOpt.isPresent()) {
            Corp corp = corpOpt.get();
            CorpAuth corpAuth = corpAuthRepository.findByCorp(corp)
                    .orElseThrow(() -> new AuthException(AuthErrorCode.AUTH_NOT_FOUND));
            return new CustomUserDetails(corp.getId(), corp.getLoginId(), corpAuth.getPassword(), corp.getRole());
        }

        // 아무것도 찾지 못했을 때
        throw new AuthException(AuthErrorCode.AUTH_NOT_FOUND);
    }
}
