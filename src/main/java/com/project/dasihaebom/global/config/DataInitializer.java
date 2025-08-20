package com.project.dasihaebom.global.config;

import com.project.dasihaebom.domain.auth.entity.Auth;
import com.project.dasihaebom.domain.auth.repository.AuthRepository;
import com.project.dasihaebom.domain.introduction.entity.Introduction;
import com.project.dasihaebom.domain.introduction.entity.Question;
import com.project.dasihaebom.domain.introduction.repository.IntroductionRepository;
import com.project.dasihaebom.domain.introduction.repository.QuestionRepository;
import com.project.dasihaebom.domain.license.entity.License;
import com.project.dasihaebom.domain.location.entity.Coordinates;
import com.project.dasihaebom.domain.resume.service.command.ResumeCommandService;
import com.project.dasihaebom.domain.user.LoginType;
import com.project.dasihaebom.domain.user.Role;
import com.project.dasihaebom.domain.user.worker.entity.Gender;
import com.project.dasihaebom.domain.user.worker.entity.Worker;
import com.project.dasihaebom.domain.user.worker.repository.WorkerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private static final int MINIMUM_WORKER_COUNT = 20;

    private final QuestionRepository questionRepository;
    private final WorkerRepository workerRepository;
    // ⭐️ licenseRepository는 Cascade 옵션으로 인해 더 이상 직접 사용되지 않을 수 있습니다.
    // private final LicenseRepository licenseRepository;
    private final IntroductionRepository introductionRepository;
    private final AuthRepository authRepository;
    private final ResumeCommandService resumeCommandService;
    private final PasswordEncoder passwordEncoder;

    private record DummyWorkerData(String username, String birthDate, Gender gender, String address) {}
    private record DummyLicenseData(String name, LocalDate issuedAt, String issuer) {}

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // 1. 질문 데이터 초기화
        if (questionRepository.count() == 0) {
            log.info("Initializing question data...");
            List<Question> questions = Arrays.asList(
                    Question.builder().question("Q1. 연령대별 일대기를 알려주세요.").build(),
                    Question.builder().question("Q2. 지원하는 직무나 분야는 무엇인가요?").build(),
                    Question.builder().question("Q3. 주요 경력이나 업무 경험 중 자랑하고 싶은 점이 있나요?").build(),
                    Question.builder().question("Q4. 학력 및 전공을 간단히 알려주세요.").build(),
                    Question.builder().question("Q5. 협업 경험 중 가장 기억에 남는 사례는 무엇인가요?").build(),
                    Question.builder().question("Q6. 자신의 강점과 약점은 무엇인가요?").build()
            );
            questionRepository.saveAll(questions);
        }

        // 2. 구직자 더미 데이터 초기화
        long currentWorkerCount = workerRepository.count();
        if (currentWorkerCount < MINIMUM_WORKER_COUNT) {
            long workersToCreate = MINIMUM_WORKER_COUNT - currentWorkerCount;
            log.info("Current worker count is {}, creating {} more dummy workers to reach {}.",
                    currentWorkerCount, workersToCreate, MINIMUM_WORKER_COUNT);

            List<DummyWorkerData> dummyDataPool = List.of(
                    new DummyWorkerData("김영희", "650312", Gender.FEMALE, "서울특별시 종로구"),
                    new DummyWorkerData("박철수", "600725", Gender.MALE, "서울특별시 마포구"),
                    new DummyWorkerData("이하나", "581105", Gender.FEMALE, "서울특별시 강남구"),
                    new DummyWorkerData("최민준", "620418", Gender.MALE, "경기도 성남시 분당구"),
                    new DummyWorkerData("정다은", "590930", Gender.FEMALE, "인천광역시 서구")
            );

            Map<String, List<DummyLicenseData>> licenseMap = Map.of(
                    "김영희", List.of(new DummyLicenseData("요양보호사", LocalDate.of(2020, 5, 10), "보건복지부")),
                    "박철수", List.of(new DummyLicenseData("운전면허 1종보통", LocalDate.of(1985, 8, 20), "도로교통공단"))
            );

            for (int i = 0; i < workersToCreate; i++) {
                DummyWorkerData data = dummyDataPool.get(i % dummyDataPool.size());
                long newIndex = currentWorkerCount + i + 1;

                Worker worker = createWorker(data, newIndex);
                Auth auth = createAuth("worker" + newIndex, "1234", worker);
                worker.setAuth(auth);

                // ⭐️ [수정] 특정 유저에게 자격증 추가
                if (licenseMap.containsKey(data.username())) {
                    for (DummyLicenseData licenseData : licenseMap.get(data.username())) {
                        // 1. License 생성 시점에 worker를 명시적으로 설정
                        License license = License.builder()
                                .worker(worker)
                                .name(licenseData.name())
                                .issuedAt(licenseData.issuedAt())
                                .issuer(licenseData.issuer())
                                .build();
                        // 2. worker의 List에도 license를 추가하여 객체 관계 동기화
                        worker.addLicense(license);
                    }
                }

                // 모든 유저에게 기본 자기소개서 생성
                createIntroduction(worker, data.username() + "의 자기소개서입니다.", "요약");

                // ⭐️ [수정] 연관된 모든 엔티티가 설정된 후, Worker를 마지막에 한 번만 저장
                // CascadeType.ALL 옵션에 의해 Auth, License가 함께 저장됩니다.
                workerRepository.save(worker);

                // 이력서 캐시 동기화
                resumeCommandService.syncResume(worker.getId());
            }

            log.info("Dummy data initialization complete.");
        }
    }

    // --- Helper Methods ---

    private Worker createWorker(DummyWorkerData data, long index) {
        return Worker.builder()
                .username(data.username())
                .birthDate(data.birthDate())
                .gender(data.gender())
                .address(data.address())
                .phoneNumber(String.format("010-0000-%04d", index))
                .coordinates(createRandomSeoulCoordinates())
                .role(Role.WORKER)
                .loginType(LoginType.LOCAL)
                .build();
    }

    private Auth createAuth(String loginId, String password, Worker worker) {
        return Auth.builder()
                .loginId(loginId)
                .password(passwordEncoder.encode(password))
                .worker(worker)
                .isTemp(false)
                .build();
    }

    // ⭐️ createLicense 헬퍼 메서드는 run() 메서드에 통합되어 제거되었습니다.

    private void createIntroduction(Worker worker, String fullText, String summary) {
        Introduction introduction = Introduction.builder()
                .worker(worker)
                .fullText(fullText)
                .summary(summary)
                .build();
        introductionRepository.save(introduction);
    }

    private Coordinates createRandomSeoulCoordinates() {
        double lat = ThreadLocalRandom.current().nextDouble(37.43, 37.70);
        double lng = ThreadLocalRandom.current().nextDouble(126.73, 127.18);
        return new Coordinates(lat, lng);
    }
}