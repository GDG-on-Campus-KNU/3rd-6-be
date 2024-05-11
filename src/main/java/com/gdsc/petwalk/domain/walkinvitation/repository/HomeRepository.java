package com.gdsc.petwalk.domain.walkinvitation.repository;

import com.gdsc.petwalk.domain.member.entity.Member;
import com.gdsc.petwalk.domain.walkinvitation.entity.WalkInvitation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HomeRepository extends JpaRepository<WalkInvitation, Long> {
    List<WalkInvitation> findAllByWriter(Member member);
}
