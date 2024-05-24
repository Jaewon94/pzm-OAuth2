package com.jaewon.oauth2.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 50)
    private String username;    // google_(sub 값)
    private String password;    // 임의의 값(암호화)
    private String uname;       // 이름
    private String email;       // 이메일

    // oAuth2에서 추가되는 정보
    private String provider;    // google, naver, kakao
    private String providerId;  // sub 값
    @CreationTimestamp
    private Timestamp createDate;

    // 권한정보(USER, MANAGER, ADMIN) : Role
    @ManyToMany
    @JoinTable(
            name = "member_roles",
            joinColumns = @JoinColumn(name = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;
}
