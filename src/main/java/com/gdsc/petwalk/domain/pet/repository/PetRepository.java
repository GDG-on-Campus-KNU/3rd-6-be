package com.gdsc.petwalk.domain.pet.repository;

import com.gdsc.petwalk.domain.member.entity.Member;
import com.gdsc.petwalk.domain.pet.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findByMember(Member member);
}
