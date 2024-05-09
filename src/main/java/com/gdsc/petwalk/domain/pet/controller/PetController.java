package com.gdsc.petwalk.domain.pet.controller;

import com.gdsc.petwalk.domain.member.entity.Member;
import com.gdsc.petwalk.domain.pet.dto.request.PetCreateRequestDto;
import com.gdsc.petwalk.domain.pet.dto.request.PetUpdateRequestDto;
import com.gdsc.petwalk.domain.pet.dto.response.PetResponseDto;
import com.gdsc.petwalk.domain.pet.entity.Pet;
import com.gdsc.petwalk.domain.pet.service.PetService;
import com.gdsc.petwalk.global.principal.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Slf4j
public class PetController {

    private final PetService petService;

    @PostMapping("/pets")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Long> createPet(
            @RequestBody PetCreateRequestDto petCreateRequestDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails){

        Long savedId = petService.addPetToMember(petCreateRequestDto, principalDetails);

        return ResponseEntity.ok(savedId);
    }

    @GetMapping("pets/{petId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PetResponseDto> readPet(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable("petId") Long id
    ){
        PetResponseDto myPet = petService.findMyPet(principalDetails, id);

        return ResponseEntity.ok(myPet);
    }

    @GetMapping("/pets")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<PetResponseDto>> myPets(
            @AuthenticationPrincipal PrincipalDetails principalDetails){

        List<PetResponseDto> allPets = petService.findAllPets(principalDetails);

        return ResponseEntity.ok(allPets);
    }

    @DeleteMapping("/pets/{petId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> deletePet(
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @PathVariable("petId") Long id
    ){
        petService.deletePet(principalDetails, id);

        return ResponseEntity.ok().body("펫 삭제에 성공하였습니다");
    }

    @PutMapping("/pets/{petId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> updatePet(
            @RequestBody PetUpdateRequestDto petUpdateRequestDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable("petId") Long id
    ) {
        petService.updatePet(petUpdateRequestDto, principalDetails, id);

        return ResponseEntity.ok().body("펫 업데이트를 성공하였습니다");
    }
}
