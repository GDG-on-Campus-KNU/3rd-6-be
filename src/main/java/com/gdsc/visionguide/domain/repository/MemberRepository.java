package com.gdsc.visionguide.domain.repository;

import com.gdsc.visionguide.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
   Optional<Member> findByEmail(String email);
}
