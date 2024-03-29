package com.gdsc.petwalk.domain.repository;

import com.gdsc.petwalk.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
   Optional<Member> findByEmail(String email);
}
