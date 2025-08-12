package com.project.dasihaebom.global.security.userdetails;

import com.project.dasihaebom.domain.auth.entity.Auth;
import com.project.dasihaebom.domain.auth.repository.AuthRepository;
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
    private final AuthRepository authRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

        log.info("[ CustomUserDetailsService ] UserId 를 이용하여 User 를 검색합니다.");

        // Worker 로그인
        Optional<Worker> workerOpt = workerRepository.findByPhoneNumber(userId);
        if (workerOpt.isPresent()) {
            Worker worker = workerOpt.get();
            Auth workerAuth = authRepository.findByWorker(worker)
                    .orElseThrow(() -> new UsernameNotFoundException("개인 사용자 없음"));
            return new CustomUserDetails(worker.getId(), worker.getPhoneNumber(), workerAuth.getPassword(), worker.getRole());
        }

        // Corp 로그인
        Optional<Corp> corpOpt = corpRepository.findByLoginId(userId);
        if (corpOpt.isPresent()) {
            Corp corp = corpOpt.get();
            Auth corpAuth = authRepository.findByCorp(corp)
                    .orElseThrow(() -> new UsernameNotFoundException("기업 사용자 없음"));
            return new CustomUserDetails(corp.getId(), corp.getLoginId(), corpAuth.getPassword(), corp.getRole());
        }

        // 아무것도 찾지 못했을 때
        throw new UsernameNotFoundException("사용자 없음");
    }
}
