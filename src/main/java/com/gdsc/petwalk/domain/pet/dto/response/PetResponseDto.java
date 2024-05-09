package com.gdsc.petwalk.domain.pet.dto.response;

import com.gdsc.petwalk.domain.pet.entity.Pet;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@Builder
public record PetResponseDto(String nickname, String gender, Integer age, String photoUrl,
                             String description, String region, String neighborhood) {

    public static List<PetResponseDto> makeFrom(List<Pet> pets){

        List<PetResponseDto> result = new ArrayList<>();

        for (Pet pet : pets) {
            PetResponseDto petResponseDto = PetResponseDto.builder()
                    .nickname(pet.getNickname())
                    .gender(pet.getGender())
                    .age(pet.getAge())
                    .photoUrl(pet.getPhotoUrl())
                    .description(pet.getDescription())
                    .region(pet.getRegion())
                    .neighborhood(pet.getNeighborhood())
                    .build();
            result.add(petResponseDto);
        }

        return result;
    }
}
