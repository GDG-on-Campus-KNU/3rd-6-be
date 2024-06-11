package com.gdsc.petwalk.domain.member.service;

import com.gdsc.petwalk.auth.itself.dto.request.ProfileRequestDto;
import com.gdsc.petwalk.auth.itself.dto.request.SignUpRequestDto;
import com.gdsc.petwalk.domain.member.entity.Member;
import com.gdsc.petwalk.domain.member.entity.Role;
import com.gdsc.petwalk.domain.member.repository.MemberRepository;
import com.gdsc.petwalk.domain.photo.service.PhotoService;
import com.gdsc.petwalk.global.exception.ErrorCode;
import com.gdsc.petwalk.global.exception.custom.MemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PhotoService photoService;
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

    public String signUp(SignUpRequestDto signUpRequest, MultipartFile multipartFile) {

        Optional<Member> optionalExistingMember = memberRepository.findByEmail(signUpRequest.email());

        // 이메일이 중복되면 오류 발생
        if (optionalExistingMember.isPresent()) {
            throw new MemberException(ErrorCode.MEMBER_ALREADY_EXISTS_EMAIL);
        }

        Member member = Member.builder()
                .nickName(signUpRequest.nickName())
                .email(signUpRequest.email())
                .role(Role.ROLE_USER)
                .password(passwordEncoder.encode(signUpRequest.password()))
                .longitude(signUpRequest.longitude())
                .latitude(signUpRequest.latitude())
                .photoUrl(photoService.uploadAndGetUrl(multipartFile))
                .build();

        return memberRepository.save(member).getEmail();
    }

    public void setMemberProfile(ProfileRequestDto profileRequestDto, MultipartFile file){
        Member member = memberRepository.findByEmail(profileRequestDto.email())
                .orElseThrow(() -> new UsernameNotFoundException("해당 Email에 해당하는 유저가 없습니다"));

        member.setProfile(profileRequestDto, photoService.uploadAndGetUrl(file));

        memberRepository.save(member);
    }


    public Member findMemberByEmail(String email) throws UsernameNotFoundException{
        // 예외처리 해야함
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("해당 Email에 해당하는 유저가 없습니다"));

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
