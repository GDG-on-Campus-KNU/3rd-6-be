package com.gdsc.petwalk.domain.walkinvitation.service;

import com.gdsc.petwalk.domain.member.entity.Member;
import com.gdsc.petwalk.domain.photo.entity.Photo;
import com.gdsc.petwalk.domain.photo.repository.PhotoRepository;
import com.gdsc.petwalk.domain.walkinvitation.dto.request.WalkInvitaionCreateRequestDto;
import com.gdsc.petwalk.domain.walkinvitation.dto.response.HomeDetailsResponseDto;
import com.gdsc.petwalk.domain.walkinvitation.entity.WalkInvitation;
import com.gdsc.petwalk.domain.walkinvitation.repository.HomeRepository;
import com.gdsc.petwalk.global.principal.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
public class HomeService {

    private final HomeRepository homeRepository;

    public HomeDetailsResponseDto getHomeDetailsById(Long id) {
        //예외 처리 이후 수정
        WalkInvitation walkInvitation = homeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("WalkInvitaion이 없습니다"));

        Member member = walkInvitation.getWriter();
        List<Photo> photos = walkInvitation.getPhotoUrls();
        List<String> photoUrls = walkInvitation.getPhotoUrls().stream().map(Photo::getPhotoUrl).toList();

        return HomeDetailsResponseDto.builder()
                .title(walkInvitation.getTitle())
                .content(walkInvitation.getContent())
                .latitude(walkInvitation.getLatitude())
                .longitude(walkInvitation.getLongitude())
                .detailedLocation(walkInvitation.getDetailedLocation())
                .walkDate(walkInvitation.getWalkDate())
                .walkTime(walkInvitation.getWalkTime())
                .walkInvitationPhotoUrls(photoUrls)
                .memberName(member.getName())
                .memberPhotoUrl(member.getPhotoUrl())
                .build();
    }

    public Long createWalkInvitation(WalkInvitaionCreateRequestDto request,
                                     MultipartFile[] multipartFiles, PrincipalDetails principalDetails) {

        Member member = principalDetails.getMember();
        List<WalkInvitation> walkInvitations = homeRepository.findAllByWriter(member);

        WalkInvitation walkInvitation = WalkInvitation.builder()
                .writer(member)
                .title(request.getTitle())
                .content(request.getContent())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .detailedLocation(request.getDetailedLocation())
                .walkDate(request.getWalkDate())
                .walkTime(request.getWalkTime())
                .photoUrls(null) // photoUrl 구현 로직 추가 필요
                .build();

        walkInvitations.add(walkInvitation);

        return homeRepository.save(walkInvitation).getId();
    }
}
