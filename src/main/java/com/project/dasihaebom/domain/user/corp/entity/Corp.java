package com.project.dasihaebom.domain.user.corp.entity;

import com.project.dasihaebom.domain.auth.entity.CorpAuth;
import com.project.dasihaebom.domain.user.Role;
import com.project.dasihaebom.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.apache.logging.log4j.util.Lazy;

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
    private Role role;

    // User가 삭제될 때, Auth도 같이 삭제
    @OneToOne(mappedBy = "corp", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private CorpAuth corpAuth;
}
