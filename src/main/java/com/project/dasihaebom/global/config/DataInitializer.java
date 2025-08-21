package com.project.dasihaebom.global.config;

import com.project.dasihaebom.domain.auth.entity.Auth;
import com.project.dasihaebom.domain.auth.repository.AuthRepository;
import com.project.dasihaebom.domain.introduction.entity.Introduction;
import com.project.dasihaebom.domain.introduction.entity.Question;
import com.project.dasihaebom.domain.introduction.repository.IntroductionRepository;
import com.project.dasihaebom.domain.introduction.repository.QuestionRepository;
import com.project.dasihaebom.domain.license.entity.License;
import com.project.dasihaebom.domain.location.converter.LocationConverter;
import com.project.dasihaebom.domain.location.entity.Coordinates;
import com.project.dasihaebom.domain.resume.service.command.ResumeCommandService;
import com.project.dasihaebom.domain.user.Address;
import com.project.dasihaebom.domain.user.LoginType;
import com.project.dasihaebom.domain.user.Role;
import com.project.dasihaebom.domain.user.worker.entity.Gender;
import com.project.dasihaebom.domain.user.worker.entity.Worker;
import com.project.dasihaebom.domain.user.worker.repository.WorkerRepository;
import com.project.dasihaebom.global.client.location.coordinate.CoordinateClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static com.project.dasihaebom.global.util.CoordinateUtils.getLat;
import static com.project.dasihaebom.global.util.CoordinateUtils.getLng;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CoordinateClient coordinateClient;

    // 0. 최대 생성 제한
    private static final int MINIMUM_WORKER_COUNT = 30;

    // 1. 이름 리스트
    private final List<String> dummyName = List.of(
            "김영수", "박순자", "이정자", "최영식", "정광호",
            "오미자", "윤경자", "한상철", "조옥순", "강성남",
            "송재호", "배명숙", "임춘자", "신동수", "문영희",
            "권태호", "안경자", "서창수", "남영호", "노명자",
            "하상호", "장순희", "전병수", "백영자", "유동근",
            "홍순호", "곽정희", "채성호", "양정자", "표영호"
    );

    // 2. 성별 리스트
    private final List<Gender> dummyGender = List.of(
            Gender.MALE, Gender.FEMALE, Gender.FEMALE, Gender.MALE, Gender.MALE,
            Gender.FEMALE, Gender.FEMALE, Gender.MALE, Gender.FEMALE, Gender.MALE,
            Gender.MALE, Gender.FEMALE, Gender.FEMALE, Gender.MALE, Gender.FEMALE,
            Gender.MALE, Gender.FEMALE, Gender.MALE, Gender.MALE, Gender.FEMALE,
            Gender.MALE, Gender.FEMALE, Gender.MALE, Gender.FEMALE, Gender.MALE,
            Gender.MALE, Gender.FEMALE, Gender.MALE, Gender.FEMALE, Gender.MALE
    );

    // 3. 생년월일 리스트 (YYMMDD 형식)
    private final List<String> dummyBirthDate = List.of(
            "650312", "600725", "581105", "620418", "590930",
            "560215", "630728", "641120", "600927", "570304",
            "650522", "581231", "630103", "591215", "611028",
            "650604", "600817", "570925", "640219", "590830",
            "561224", "621015", "580731", "651104", "600619",
            "570306", "631210", "641227", "601102", "650415"
    );

    // 4. 주소 리스트
    private final List<String> dummyAddress = List.of(
            "서울특별시 종로구 세종대로", "서울특별시 마포구 월드컵북로",
            "서울특별시 강남구 테헤란로", "경기도 성남시 분당구 불정로",
            "인천광역시 부평구 부평대로", "부산광역시 해운대구 해운대해변로",
            "부산광역시 동래구 충렬대로", "대구광역시 중구 동성로",
            "대구광역시 수성구 달구벌대로", "광주광역시 동구 금남로",
            "광주광역시 광산구 첨단과기로", "대전광역시 서구 둔산로",
            "대전광역시 유성구 대학로", "울산광역시 남구 삼산로",
            "울산광역시 중구 번영로", "경기도 수원시 장안구 팔달로",
            "경기도 고양시 일산동구 중앙로", "경기도 용인시 수지구 풍덕천로",
            "강원특별자치도 춘천시 중앙로", "강원특별자치도 강릉시 경강로",
            "충청북도 청주시 상당구 상당로", "충청북도 충주시 중앙로",
            "충청남도 천안시 동남구 만남로", "충청남도 공주시 웅진로",
            "전라북도 전주시 완산구 팔달로", "전라북도 군산시 중앙로",
            "전라남도 목포시 평화로", "전라남도 여수시 중앙로",
            "경상북도 포항시 북구 중흥로", "경상북도 경주시 태종로"
    );

    private static final List<DummyLicenseData> dummyLicense = List.of(
            new DummyLicenseData("정보처리기사", LocalDate.of(2000, 7, 7), "한국산업인력공단"),
            new DummyLicenseData("전기기사", LocalDate.of(1976, 3, 6), "한국산업인력공단"),
            new DummyLicenseData("요양보호사", LocalDate.of(1980, 10, 28), "보건복지부"),
            new DummyLicenseData("간호조무사", LocalDate.of(1999, 12, 15), "보건복지부"),
            new DummyLicenseData("사회복지사", LocalDate.of(1985, 9, 19), "보건복지부")
    );

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
            log.info("현재 유저 수 : {}, 생성할 유저 수 : {}, 목표 유저 수 : {}", currentWorkerCount, workersToCreate, MINIMUM_WORKER_COUNT);

            List<DummyWorkerData> dummyDataPool = new ArrayList<>();
            for (int i = 0; i < MINIMUM_WORKER_COUNT; i++) {
                dummyDataPool.add(new DummyWorkerData(dummyName.get(i), dummyBirthDate.get(i), dummyGender.get(i), dummyAddress.get(i)));
            }

            Random rnd = new Random();

            for (int i = 0; i < workersToCreate; i++) {
                DummyWorkerData data = dummyDataPool.get(i % dummyDataPool.size());
                long newIndex = currentWorkerCount + i + 1;

                Worker worker = createWorker(data, newIndex);
                Auth auth = createAuth("worker" + newIndex, "1234", worker);
                worker.setAuth(auth);

                // ⭐️ 0~3개의 자격증 랜덤 배정
                int licenseCount = rnd.nextInt(4); // 0,1,2,3 중 하나
                List<DummyLicenseData> shuffledLicenses = new ArrayList<>(dummyLicense);
                Collections.shuffle(shuffledLicenses);

                for (int j = 0; j < licenseCount; j++) {
                    DummyLicenseData licenseData = shuffledLicenses.get(j);

                    License license = License.builder()
                            .worker(worker)
                            .name(licenseData.name)
                            .issuedAt(licenseData.issuedAt)
                            .issuer(licenseData.issuer)
                            .build();

                    worker.addLicense(license);
                }

                // 모든 유저에게 기본 자기소개서 생성
                createIntroduction(worker, data.username() + "의 자기소개서입니다.", "요약");

                // ⭐️ [수정] 연관된 모든 엔티티가 설정된 후, Worker를 마지막에 한 번만 저장
                // CascadeType.ALL 옵션에 의해 Auth, License가 함께 저장됩니다.
                workerRepository.save(worker);

                // 이력서 캐시 동기화
                resumeCommandService.syncResume(worker.getId());

                log.info("현재 유저 수 : {}, 생성할 유저 수 : {}", workerRepository.count(), workersToCreate);
            }

            log.info("Dummy data initialization complete.");
        }
    }

    // --- Helper Methods ---

    private Worker createWorker(DataInitializer.DummyWorkerData data, long index) {
        return Worker.builder()
                .username(data.username())
                .birthDate(data.birthDate())
                .gender(data.gender())
                .address(new Address(data.address, "xxx동 xxx호"))
                .phoneNumber(String.format("0100000%04d", index))
                .coordinates(createCoordinates(data.address))
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

    private Coordinates createCoordinates(String address) {
        List<Double> workerCoordinates = LocationConverter.toCoordinateList(coordinateClient.getKakaoCoordinateInfo(address));

        return new Coordinates(getLat(workerCoordinates), getLng(workerCoordinates));
    }
}