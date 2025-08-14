package com.project.dasihaebom.domain.license.entity;

import com.project.dasihaebom.domain.user.worker.entity.Worker;
import com.project.dasihaebom.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "license")
public class License extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "issued_at", nullable = false)
    private LocalDate issuedAt;

    @Column(name = "issuer", nullable = false)
    private String issuer;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id", unique = true)
    private Worker worker;
}
