package com.gdsc.visionguide.domain.service;

import com.gdsc.visionguide.domain.entity.Member;
import com.gdsc.visionguide.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

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
}
