package com.gdsc.petwalk.domain.walkinvitation.service;

import com.gdsc.petwalk.domain.member.entity.Member;
import com.gdsc.petwalk.domain.member.repository.MemberRepository;
import com.gdsc.petwalk.domain.photo.entity.Photo;
import com.gdsc.petwalk.domain.photo.service.PhotoService;
import com.gdsc.petwalk.domain.walkinvitation.dto.request.WalkInvitationCreateRequestDto;
import com.gdsc.petwalk.domain.walkinvitation.dto.response.HomePageResponseDto;
import com.gdsc.petwalk.domain.walkinvitation.dto.response.WalkInvitationDetailsResponseDto;
import com.gdsc.petwalk.domain.walkinvitation.entity.WalkInvitation;
import com.gdsc.petwalk.domain.walkinvitation.entity.WalkingStatus;
import com.gdsc.petwalk.domain.walkinvitation.repository.WalkInvitationRepository;
import com.gdsc.petwalk.global.principal.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
public class WalkInvitationService {

    private final MemberRepository memberRepository;
    private final WalkInvitationRepository walkInvitationRepository;
    private final PhotoService photoService;

    /**
     *
     * 핵심 비지니스 로직 모음
     */
    public WalkInvitationDetailsResponseDto getHomeDetailsById(Long id) {
        WalkInvitation walkInvitation = walkInvitationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 id에 해당하는 글이 없습니다"));

        Member member = walkInvitation.getMember();
        List<String> photoUrls = walkInvitation.getPhotoUrls().stream()
            .map(Photo::getPhotoUrl)
            .toList();

        return getWalkInvitationDetailsResponseDto(walkInvitation, photoUrls, member);
    }

    public Long createWalkInvitation(WalkInvitationCreateRequestDto request,
                                     MultipartFile[] multipartFiles, PrincipalDetails principalDetails) {
        // JPA 영속화
        Member member = memberRepository.findByEmail(principalDetails.getMember().getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("해당 Email에 해당하는 유저가 없습니다"));

        WalkInvitation walkInvitation = WalkInvitation.builder()
            .member(member)
            .title(request.getTitle())
            .content(request.getContent())
            .latitude(request.getLatitude())
            .longitude(request.getLongitude())
            .detailedLocation(request.getDetailedLocation())
            .walkStartDateTime(request.getWalkStartDateTime())
            .walkingStatus(WalkingStatus.BEFORE_WALK)
            .build();
        member.getWalkInvitations().add(walkInvitation);

        List<Photo> photos = photoService.savePhotosToWalkInvitation(multipartFiles, walkInvitation);
        walkInvitation.addPhotos(photos);

        walkInvitationRepository.save(walkInvitation);

        return walkInvitation.getId();
    }


    public List<HomePageResponseDto> getTodayHomePageLists(PrincipalDetails principalDetails) {
        // principalDetails를 가져온 이유는, 자기 주위에 산책글을 보여주기 위함. 이후 로직추가

        // 기본은 지금부터 내일 이 시간까지만 가져오기
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime after24Hours = now.plusHours(24);

        List<WalkInvitation> walkInvitations
                = walkInvitationRepository.findAllByWalkStartDateTimeBetween(now, after24Hours);

        if(walkInvitations.isEmpty()){
            throw new NoSuchElementException("홈 화면에 불러올 글이 없습니다");
        }

        return getHomePageResponseDtoList(walkInvitations);
    }


    /**
     *
     * 포맷 변환 등 메서드 모음
     */
    public String formatLocalDateTime(LocalDateTime dateTime) {
        DateTimeFormatter dayMonthFormatter = DateTimeFormatter.ofPattern("M/d");
        String dayMonth = dateTime.format(dayMonthFormatter);

        String dayOfWeek = dateTime.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN);

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH시mm분");
        String time = dateTime.format(timeFormatter);

        return String.format("%s(%s) %s ~", dayMonth, dayOfWeek, time);
    }

    public String getTimeDifference(LocalDateTime postTime, LocalDateTime currentTime){
        Duration duration = Duration.between(postTime, currentTime);
        long minutes = Math.abs(duration.toMinutes());
        String timeDifference;

        if (minutes < 60) {
            timeDifference = minutes + "분 전";
        } else {
            long hours = Math.abs(duration.toHours());
            timeDifference = hours + "시간 전";
        }

        return timeDifference;
    }

    /**
     *
     * DTO <-> 엔티티 변환 메서드 모음
     */
    public WalkInvitationDetailsResponseDto getWalkInvitationDetailsResponseDto(WalkInvitation walkInvitation, List<String> photoUrls, Member member){
        return WalkInvitationDetailsResponseDto.builder()
                .title(walkInvitation.getTitle())
                .content(walkInvitation.getContent())
                .latitude(walkInvitation.getLatitude())
                .longitude(walkInvitation.getLongitude())
                .detailedLocation(walkInvitation.getDetailedLocation())
                .walkDateTime(formatLocalDateTime(walkInvitation.getWalkStartDateTime()))
                .walkingStatus(walkInvitation.getWalkingStatus().toString())
                .walkInvitationPhotoUrls(photoUrls)
                .memberId(member.getId())
                .memberName(member.getNickName())
                .memberPhotoUrl(member.getPhotoUrl())
                .build();
    }

    public List<HomePageResponseDto> getHomePageResponseDtoList(List<WalkInvitation> walkInvitations){
        return walkInvitations.stream().map(walkInvitation -> {
            return HomePageResponseDto.builder()
                    .id(walkInvitation.getId())
                    .title(walkInvitation.getTitle())
                    .latitude(walkInvitation.getLatitude())
                    .longitude(walkInvitation.getLongitude())
                    .detailedLocation(walkInvitation.getDetailedLocation())
                    .walkDateTime(formatLocalDateTime(walkInvitation.getWalkStartDateTime()))
                    .walkingStatus(walkInvitation.getWalkingStatus())
                    .walkInvitationPhotoUrl(walkInvitation.getPhotoUrls().get(0).getPhotoUrl())
                    .timeDifference(getTimeDifference(walkInvitation.getWalkStartDateTime(), LocalDateTime.now()))
                    .build();
        }).toList();
    }
}
