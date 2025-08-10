package com.project.dasihaebom.domain.introduction.entity;

import com.project.dasihaebom.domain.user.worker.entity.Worker;
import com.project.dasihaebom.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Introduction extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ai_fullText", columnDefinition = "TEXT", nullable = false)
    private String fullText; // 자기소개서 본문

    @Column(name ="ai_summary" ,length = 200, nullable = false)
    private String summary; // 검색용 한 줄 요약

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id")
    private Worker worker;
}
