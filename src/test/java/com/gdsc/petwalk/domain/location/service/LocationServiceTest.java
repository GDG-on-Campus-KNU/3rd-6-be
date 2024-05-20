package com.gdsc.petwalk.domain.location.service;

import com.gdsc.petwalk.domain.location.dto.response.LocationResponseDto;
import com.gdsc.petwalk.domain.member.entity.Member;
import com.gdsc.petwalk.domain.walkinvitation.entity.WalkInvitation;
import com.gdsc.petwalk.domain.walkinvitation.repository.WalkInvitationRepository;
import com.gdsc.petwalk.global.principal.PrincipalDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class LocationServiceTest {

    @Autowired
    private LocationService locationService;

    @Autowired
    private WalkInvitationRepository walkInvitationRepository;

    private PrincipalDetails principalDetails;

    @BeforeEach
    void setUp() {
        // 데이터베이스 초기화
        initDataBase();

        // Create a test member with a specific location
        Member member = Member.builder()
                .latitude(20.0)
                .longitude(20.0)
                .build();

        // Mock the PrincipalDetails to return the test member
        principalDetails = Mockito.mock(PrincipalDetails.class);
        when(principalDetails.getMember()).thenReturn(member);
    }

    // 데이터베이스 초기화 메서드
    private void initDataBase() {
        // 1km 반경 내의 WalkInvitation 2개 생성
        walkInvitationRepository.save(WalkInvitation.builder()
                .title("Within 1km Walk 1")
                .content("Content 1")
                .latitude(20.005)
                .longitude(20.005)
                .detailedLocation("Location 1")
                .walkDateTime(LocalDateTime.now())
                .walkingStatus("Planned")
                .build());

        walkInvitationRepository.save(WalkInvitation.builder()
                .title("Within 1km Walk 2")
                .content("Content 2")
                .latitude(19.995)
                .longitude(19.995)
                .detailedLocation("Location 2")
                .walkDateTime(LocalDateTime.now())
                .walkingStatus("Planned")
                .build());

        // 1km 반경 밖의 WalkInvitation 3개 생성
        walkInvitationRepository.save(WalkInvitation.builder()
                .title("Outside 1km Walk 1")
                .content("Content 3")
                .latitude(20.020)
                .longitude(20.020)
                .detailedLocation("Location 3")
                .walkDateTime(LocalDateTime.now())
                .walkingStatus("Planned")
                .build());

        walkInvitationRepository.save(WalkInvitation.builder()
                .title("Outside 1km Walk 2")
                .content("Content 4")
                .latitude(19.980)
                .longitude(19.980)
                .detailedLocation("Location 4")
                .walkDateTime(LocalDateTime.now())
                .walkingStatus("Planned")
                .build());

        walkInvitationRepository.save(WalkInvitation.builder()
                .title("Outside 1km Walk 3")
                .content("Content 5")
                .latitude(20.030)
                .longitude(20.030)
                .detailedLocation("Location 5")
                .walkDateTime(LocalDateTime.now())
                .walkingStatus("Planned")
                .build());
    }

    @Test
    void findAllWithinRadius() {
        double radius = 1.0; // 1km 반경

        List<LocationResponseDto> locationResponseDtos = locationService.findAllWithinRadius(principalDetails, radius);
        assertEquals(2, locationResponseDtos.size());
    }
}
