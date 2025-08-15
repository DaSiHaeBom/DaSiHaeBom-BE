package com.project.dasihaebom.domain.user.worker.entity;

import com.project.dasihaebom.domain.auth.entity.Auth;
import com.project.dasihaebom.domain.introduction.entity.Answer;
import com.project.dasihaebom.domain.introduction.entity.Introduction;
import com.project.dasihaebom.domain.license.entity.License;
import com.project.dasihaebom.domain.location.entity.Location;
import com.project.dasihaebom.domain.resume.entity.Resume;
import com.project.dasihaebom.domain.user.LoginType;
import com.project.dasihaebom.domain.user.Role;
import com.project.dasihaebom.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.query.named.ResultMemento;

import java.util.List;

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

    @Column(name = "phone_number", nullable = false, unique = true)
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

    @Column(name = "coordinates")
    private List<Double> coordinates;

    // XXXrepsoitory.delete() 등으로 삭제했을 때,
    // User을 참조했던 애들이 같이 지워질 수 있게
    // cascade, orphan 설정 해주세요

    // User가 삭제되면 Auth도 삭제
    @OneToOne(mappedBy = "worker", cascade = CascadeType.ALL, orphanRemoval = true)
    private Auth auth;
    // User가 삭제되면 Location도 삭제
    @OneToMany(mappedBy = "worker", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Location> location;
    // User가 삭제되면 License도 삭제
    @OneToMany(mappedBy = "worker", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<License> license;
    // User가 삭제되면 Introduction도 삭제
    @OneToMany(mappedBy = "worker", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Introduction> introduction;
    // User가 삭제되면 Answer도 삭제
    @OneToMany(mappedBy = "worker", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answers;
    // User가 삭제되면 Resume도 삭제
    @OneToOne(mappedBy = "worker", cascade = CascadeType.ALL, orphanRemoval = true)
    private Resume resume;

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
    public void changeCoordinates(List<Double> coordinates){
        this.coordinates = coordinates;
    }
}
