package com.project.dasihaebom.domain.user.worker.entity;

import com.project.dasihaebom.domain.auth.entity.Auth;
import com.project.dasihaebom.domain.user.LoginType;
import com.project.dasihaebom.domain.user.Role;
import com.project.dasihaebom.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "worker")
public class Worker extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "birth_date", nullable = false)
    private String birthDate;

    @Column(name = "gender", nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "login_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private LoginType loginType;


    // User가 삭제되면 Auth도 삭제
    @OneToOne(mappedBy = "worker", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Auth auth;

    // 엔티티 수정 전용 메서드
    public void changePhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }
    public void changeUsername(String username){
        this.username = username;
    }
    public void changeBirthDate(String birthDate){
        this.birthDate = birthDate;
    }
    public void changeAddress(String address){
        this.address = address;
    }
}
