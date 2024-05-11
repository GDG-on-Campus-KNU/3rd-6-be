package com.gdsc.petwalk.domain.walkinvitation.repository;

import com.gdsc.petwalk.domain.member.entity.Member;
import com.gdsc.petwalk.domain.walkinvitation.entity.WalkInvitation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface WalkInvitationRepository extends JpaRepository<WalkInvitation, Long> {
    List<WalkInvitation> findAllByWriter(Member member);
    List<WalkInvitation> findAllByWalkDateTimeBetween(LocalDateTime start, LocalDateTime end);
}
