package com.gdsc.petwalk.domain.walkinvitation.repository;

import com.gdsc.petwalk.domain.member.entity.Member;
import com.gdsc.petwalk.domain.walkinvitation.entity.WalkInvitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface WalkInvitationRepository extends JpaRepository<WalkInvitation, Long> {
    List<WalkInvitation> findAllByWriter(Member member);

    List<WalkInvitation> findAllByWalkStartDateTimeBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT u FROM walk_invitations u WHERE " +
            "(6371 * acos(cos(radians(:centerLat)) * cos(radians(u.latitude)) " +
            "* cos(radians(u.longitude) - radians(:centerLng)) + " +
            "sin(radians(:centerLat)) * sin(radians(u.latitude)))) < :radius")
    List<WalkInvitation> findAllWithinRadius(@Param("centerLat") double centerLat,
                                             @Param("centerLng") double centerLng,
                                             @Param("radius") double radius);
}
