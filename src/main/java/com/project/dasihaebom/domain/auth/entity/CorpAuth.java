package com.project.dasihaebom.domain.auth.entity;

import com.project.dasihaebom.domain.user.corp.entity.Corp;
import com.project.dasihaebom.domain.user.worker.entity.Worker;
import com.project.dasihaebom.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "corp_auth")
public class CorpAuth extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "is_temp", nullable = false)
    private boolean isTemp;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "corp_id", unique = true, nullable = false)
    private Corp corp;
}
