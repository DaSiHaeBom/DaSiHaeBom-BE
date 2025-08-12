package com.project.dasihaebom.domain.user.corp.entity;

import com.project.dasihaebom.domain.auth.entity.Auth;
import com.project.dasihaebom.domain.user.Role;
import com.project.dasihaebom.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "corp")
public class Corp extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login_id", nullable = false)
    private String loginId;

    @Column(name = "ceo_name", nullable = false)
    private String ceoName;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "corp_number", nullable = false)
    private String corpNumber;

    @Column(name = "corp_name", nullable = false)
    private String corpName;

    @Column(name = "corp_address", nullable = false)
    private String corpAddress;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    // User가 삭제될 때, Auth도 같이 삭제
    @OneToOne(mappedBy = "corp", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Auth auth;


    // 엔티티 수정 전용 메서드
    public void changeCeoName(String ceoName) {
        this.ceoName = ceoName;
    }
    public void changePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public void changeCorpNumber(String corpNumber) {
        this.corpNumber = corpNumber;
    }
    public void changeCorpName(String corpName) {
        this.corpName = corpName;
    }
    public void changeCorpAddress(String corpAddress) {
        this.corpAddress = corpAddress;
    }
}
