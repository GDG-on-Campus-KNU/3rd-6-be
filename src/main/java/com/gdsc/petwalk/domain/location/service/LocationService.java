package com.gdsc.petwalk.domain.location.service;

import com.gdsc.petwalk.domain.location.dto.response.LocationResponseDto;
import com.gdsc.petwalk.domain.member.entity.Member;
import com.gdsc.petwalk.domain.walkinvitation.entity.WalkInvitation;
import com.gdsc.petwalk.domain.walkinvitation.repository.WalkInvitationRepository;
import com.gdsc.petwalk.global.principal.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class LocationService {

    private final WalkInvitationRepository walkInvitationRepository;

    public List<LocationResponseDto> findAllWithinRadius(PrincipalDetails principalDetails, double radius) {
        Member member = principalDetails.getMember();
        double centerLat = member.getLatitude();
        double centerLng = member.getLongitude();

        List<WalkInvitation> allWithinRadius =
                walkInvitationRepository.findAllWithinRadius(centerLat, centerLng, radius);

        return LocationResponseDto.makeFrom(allWithinRadius);
    }

}
