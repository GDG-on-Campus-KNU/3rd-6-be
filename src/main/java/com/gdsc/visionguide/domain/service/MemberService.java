package com.gdsc.visionguide.domain.service;

import com.gdsc.visionguide.auth.itself.dto.request.SignUpRequestDto;
import com.gdsc.visionguide.domain.entity.Member;
import com.gdsc.visionguide.domain.repository.MemberRepository;
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

    public void signUp(SignUpRequestDto signUpRequest) {

        Optional<Member> optionalExistingMember = memberRepository.findByEmail(signUpRequest.email());

        // 이메일이 중복되지 않으면
        if (!optionalExistingMember.isPresent()) {
            Member member = Member.builder()
                    .name(signUpRequest.name())
                    .email(signUpRequest.email())
                    .password(passwordEncoder.encode(signUpRequest.password()))
                    .build();

            memberRepository.save(member).getId();
        } else {
            throw new RuntimeException("member email이 이미 존재합니다");
        }
    }
}
