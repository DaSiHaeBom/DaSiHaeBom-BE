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
@Table(name = "auth")
public class Auth extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "loginId", nullable = false, unique = true)
    private String loginId;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "is_temp", nullable = false)
    private boolean isTemp;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "corp_id", unique = true)
    private Corp corp;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id", unique = true)
    private Worker worker;

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }
}
