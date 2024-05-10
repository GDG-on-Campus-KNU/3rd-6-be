package com.gdsc.petwalk.domain.pet.service;

import com.gdsc.petwalk.domain.member.entity.Member;
import com.gdsc.petwalk.domain.member.repository.MemberRepository;
import com.gdsc.petwalk.domain.pet.dto.request.PetCreateRequestDto;
import com.gdsc.petwalk.domain.pet.dto.request.PetUpdateRequestDto;
import com.gdsc.petwalk.domain.pet.dto.response.PetResponseDto;
import com.gdsc.petwalk.domain.pet.entity.Pet;
import com.gdsc.petwalk.domain.pet.repository.PetRepository;
import com.gdsc.petwalk.global.principal.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PetService {
    private final PetRepository petRepository;

    public Long addPetToMember(PetCreateRequestDto request, PrincipalDetails principalDetails) {

        Member member = principalDetails.getMember();

        Pet pet = Pet.builder()
                .member(member)
                .nickname(request.nickname())
                .gender(request.gender())
                .age(request.age())
                .photoUrl(request.photoUrl())
                .description(request.description())
                .likesCount(0)
                .region(request.region())
                .neighborhood(request.neighborhood())
                .build();

        pet.setPetOwner(member);
        Pet savePet = petRepository.save(pet);

        return savePet.getId();
    }

    public PetResponseDto findMyPet(PrincipalDetails principalDetails, Long id) {
        // 예외처리
        Pet pet = petRepository.findById(id).orElseThrow();

        if (pet.getMember().getId().equals(principalDetails.getId())) {
            return PetResponseDto.builder()
                    .nickname(pet.getNickname())
                    .gender(pet.getGender())
                    .age(pet.getAge())
                    .photoUrl(pet.getPhotoUrl())
                    .description(pet.getDescription())
                    .region(pet.getRegion())
                    .neighborhood(pet.getNeighborhood())
                    .build();
        }
        // 예외처리

        return null;
    }

    public List<PetResponseDto> findAllPets(PrincipalDetails principalDetails) {
        Member member = principalDetails.getMember();
        List<Pet> pets = petRepository.findByMember(member);

        return PetResponseDto.makeFrom(pets);
    }

    public void deletePet(PrincipalDetails principalDetails, Long id) {
        // 예외처리
        Pet pet = petRepository.findById(id).orElseThrow();

        if (pet.getMember().getId().equals(principalDetails.getId())) {
            petRepository.deleteById(id);
        }

        // 예외처리
    }


    public void updatePet(PetUpdateRequestDto request, PrincipalDetails principalDetails, Long id) {
        // 예외처리
        Pet pet = petRepository.findById(id).orElseThrow();

        if (pet.getMember().getId().equals(principalDetails.getId())) {
            pet.update(request);
            petRepository.save(pet);
        }
    }
}
