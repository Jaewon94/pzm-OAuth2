package com.jaewon.oauth2.service;

import com.jaewon.oauth2.entity.Member;
import com.jaewon.oauth2.entity.PrincipalUser;
import com.jaewon.oauth2.entity.Role;
import com.jaewon.oauth2.repository.MemberRepository;
import com.jaewon.oauth2.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        // 강제로 회원가입을 진행하기 위한 정보 추출
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String provider = userRequest.getClientRegistration().getRegistrationId();
        String providerId = oAuth2User.getAttribute("sub");
        String username = provider + "_" + providerId;


        // 데이터베이스에 데이터를 저장하기 전에? -> 가입 유무를 확인
        Optional<Member> optional = memberRepository.findByUsername(username);
        Member member = null;
        if (optional.isPresent()) {
            System.out.println("로그인을 이미 한적이 있습니다. 당신은 자동 회원가입이 되었습니다.");
            member = optional.get();
        } else {
            System.out.println("처음 OAuth2 로그인을한 사용자 입니다.");

            String password = passwordEncoder.encode(username);
            String uname = (String) oAuth2User.getAttribute("name");
            String email = (String) oAuth2User.getAttribute("email");

            Set<Role> roles = new HashSet<>();  // USER
            Role userRole = roleRepository.findByName("USER");
            roles.add(userRole);

            member = new Member();
            member.setUsername(username);
            member.setPassword(password);
            member.setUname(uname);
            member.setProvider(provider);
            member.setProviderId(providerId);
            member.setRoles(roles);
            member.setEmail(email);
            memberRepository.save(member);

        }

        // 로그인 성공 -> 세션 (SecurityContextHolder) <---Member member
        //                  // OAuth2User(interface)                 Member
            return new PrincipalUser(member);   // 세션으로 만들어진다.
//        return ;    // UserDetail --> User(class)
    }
}

//        System.out.println("userRequest = " + userRequest);
//        System.out.println("userRequest.getClientRegistration() = " + userRequest.getClientRegistration()); // google 정보 ~
//        System.out.println("userRequest.getAccessToken() = " + userRequest.getAccessToken());
//        System.out.println("super.loadUser(userRequest).getAttributes() = " + super.loadUser(userRequest).getAttributes());  // -----엑세스토큰 --> 요청 ----email, profile--->