package com.gdsc.petwalk.domain.walkinvitation.service;

import com.gdsc.petwalk.domain.member.entity.Member;
import com.gdsc.petwalk.domain.photo.entity.Photo;
import com.gdsc.petwalk.domain.photo.service.PhotoService;
import com.gdsc.petwalk.domain.walkinvitation.dto.request.WalkInvitaionCreateRequestDto;
import com.gdsc.petwalk.domain.walkinvitation.dto.response.HomePageResponseDto;
import com.gdsc.petwalk.domain.walkinvitation.dto.response.WalkInvitationDetailsResponseDto;
import com.gdsc.petwalk.domain.walkinvitation.entity.WalkInvitation;
import com.gdsc.petwalk.domain.walkinvitation.repository.WalkInvitationRepository;
import com.gdsc.petwalk.global.principal.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
public class WalkInvitationService {

    private final WalkInvitationRepository walkInvitationRepository;
    private final PhotoService photoService;

    public WalkInvitationDetailsResponseDto getHomeDetailsById(Long id) {
        //예외 처리 이후 수정
        WalkInvitation walkInvitation = walkInvitationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("WalkInvitaion이 없습니다"));

        Member member = walkInvitation.getWriter();
        List<String> photoUrls = walkInvitation.getPhotoUrls().stream()
            .map(Photo::getPhotoUrl)
            .toList();

        return WalkInvitationDetailsResponseDto.builder()
                .title(walkInvitation.getTitle())
                .content(walkInvitation.getContent())
                .latitude(walkInvitation.getLatitude())
                .longitude(walkInvitation.getLongitude())
                .detailedLocation(walkInvitation.getDetailedLocation())
                .walkDateTime(walkInvitation.getWalkDateTime())
                .walkingStatus(walkInvitation.getWalkingStatus())
                .walkInvitationPhotoUrls(photoUrls)
                .memberName(member.getNickName())
                .memberPhotoUrl(member.getPhotoUrl())
                .build();
    }

    public Long createWalkInvitation(WalkInvitaionCreateRequestDto request,
        MultipartFile[] multipartFiles, PrincipalDetails principalDetails) {

        Member member = principalDetails.getMember();

        WalkInvitation walkInvitation = WalkInvitation.builder()
            .writer(member)
            .title(request.getTitle())
            .content(request.getContent())
            .latitude(request.getLatitude())
            .longitude(request.getLongitude())
            .detailedLocation(request.getDetailedLocation())
            .walkDateTime(request.getWalkDateTime())
            .walkingStatus("산책 대기 중")
            .build();

        walkInvitation.setPhotoUrls(photoService.savePhotos(multipartFiles, walkInvitation));

        walkInvitationRepository.save(walkInvitation);

        return walkInvitation.getId();
    }


    public List<HomePageResponseDto> getTodayHomePageLists(PrincipalDetails principalDetails) {
        // principalDetails를 가져온 이유는, 자기 주위에 산책글을 보여주기 위함. 이후 로직추가

        // 기본은 지금부터 내일 이 시간까지 walkInvitation만 가져오기
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime after24Hours = now.plusHours(24);

        List<WalkInvitation> walkInvitations
                = walkInvitationRepository.findAllByWalkDateTimeBetween(now, after24Hours);

        return HomePageResponseDto.getListFrom(walkInvitations);
    }
}
