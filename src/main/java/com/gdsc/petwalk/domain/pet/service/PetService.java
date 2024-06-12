package com.gdsc.petwalk.domain.pet.service;

import com.gdsc.petwalk.domain.member.entity.Member;
import com.gdsc.petwalk.domain.member.repository.MemberRepository;
import com.gdsc.petwalk.domain.pet.dto.request.PetCreateRequestDto;
import com.gdsc.petwalk.domain.pet.dto.request.PetUpdateRequestDto;
import com.gdsc.petwalk.domain.pet.dto.response.PetResponseDto;
import com.gdsc.petwalk.domain.pet.entity.Pet;
import com.gdsc.petwalk.domain.pet.repository.PetRepository;
import com.gdsc.petwalk.domain.photo.service.PhotoService;
import com.gdsc.petwalk.global.principal.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PetService {
    private final PetRepository petRepository;
    private final PhotoService photoService;

    public Long addPetToMember(PetCreateRequestDto request, MultipartFile file, PrincipalDetails principalDetails) {

        Member member = principalDetails.getMember();

        Pet pet = Pet.builder()
                .member(member)
                .nickname(request.nickname())
                .gender(request.gender())
                .age(request.age())
                .description(request.description())
                .dogType(request.dogType())
                .likesCount(0)
                .build();

        photoService.savePhotoToPet(file, pet);

        member.getPets().add(pet);
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
                    .photoUrl(pet.getPhotos().get(0).getPhotoUrl())
                    .description(pet.getDescription())
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
