package com.gdsc.petwalk.domain.member.service;

import com.gdsc.petwalk.auth.itself.dto.request.SignUpRequestDto;
import com.gdsc.petwalk.domain.member.entity.Member;
import com.gdsc.petwalk.domain.member.entity.Role;
import com.gdsc.petwalk.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member saveOrUpdate(Member member) {
        Optional<Member> optionalExistingMember = memberRepository.findByEmail(member.getEmail());

        if (optionalExistingMember.isPresent()) {
            Member existingMember = optionalExistingMember.get();
            existingMember.updateMember(member);

            return memberRepository.save(existingMember);
        } else {
            return memberRepository.save(member);
        }
    }

    public Long signUp(SignUpRequestDto signUpRequest) {

        Optional<Member> optionalExistingMember = memberRepository.findByEmail(signUpRequest.email());

        // 이메일이 중복되지 않으면
        if (!optionalExistingMember.isPresent()) {
            Member member = Member.builder()
                    .name(signUpRequest.name())
                    .email(signUpRequest.email())
                    .role(Role.ROLE_USER)
                    .password(passwordEncoder.encode(signUpRequest.password()))
                    .build();

            return memberRepository.save(member).getId();
        } else {
            // 예외처리 해야함
            throw new RuntimeException("member email이 이미 존재합니다");
        }
    }
    public Member findMemberByEmail(String email){
        // 예외처리 해야함
        Member member = memberRepository.findByEmail(email)
                .orElseThrow();

        return member;
    }

    public String saveRefresh(String email, String refreshToken){
        Member member = memberRepository.findByEmail(email)
                .orElseThrow();

        member.updateRefreshToken(refreshToken);

        memberRepository.save(member);

        return member.getRefresh();
    }

}
