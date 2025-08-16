package com.project.dasihaebom.domain.resume.entity;

import com.project.dasihaebom.domain.introduction.entity.Introduction;
import com.project.dasihaebom.domain.license.entity.License;
import com.project.dasihaebom.domain.user.worker.entity.Gender;
import com.project.dasihaebom.domain.user.worker.entity.Worker;
import com.project.dasihaebom.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "resume")
public class Resume extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ================== Worker 정보 복사 ==================
    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "birth_date", nullable = false)
    private String birthDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @Column(name = "address", nullable = false) //
    private String address;

    @Column(name = "phone_number", nullable = false) //
    private String phoneNumber;

    // --- License 정보 복사 (JSON) ---
    @Column(columnDefinition = "TEXT")
    private String licenses;

    // ============= Introduction 정보 복사 =============
    @Column(name = "introduction_full_text", columnDefinition = "TEXT", nullable = false)
    private String introductionFullText;

    @Column(name = "introduction_summary", length = 200, nullable = false)
    private String introductionSummary;

    // ================== 원본 데이터 연결 ==================
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id", unique = true, nullable = false)
    private Worker worker;



    // 비정규화 메서드 갱신(동기화)용
    public void syncData(Worker worker, Introduction introduction, String licensesJson) {
        this.username = worker.getUsername();
        this.birthDate = worker.getBirthDate();
        this.gender = worker.getGender();
        this.address = worker.getAddress();
        this.phoneNumber = worker.getPhoneNumber();
        this.introductionFullText = introduction.getFullText();
        this.introductionSummary = introduction.getSummary();
        this.licenses = licensesJson;
    }

}
