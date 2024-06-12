package com.gdsc.petwalk.domain.pet.controller;

import com.gdsc.petwalk.domain.member.entity.Member;
import com.gdsc.petwalk.domain.pet.dto.request.PetCreateRequestDto;
import com.gdsc.petwalk.domain.pet.dto.request.PetUpdateRequestDto;
import com.gdsc.petwalk.domain.pet.dto.response.PetResponseDto;
import com.gdsc.petwalk.domain.pet.dto.response.PetResultDto;
import com.gdsc.petwalk.domain.pet.entity.Pet;
import com.gdsc.petwalk.domain.pet.service.PetService;
import com.gdsc.petwalk.global.principal.PrincipalDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pets")
@RequiredArgsConstructor
@Slf4j
public class PetController {

    private final PetService petService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "회원가입 후 펫 생성 로직", description = "회원가입 후 펫 생성 로직")
    @ApiResponse(responseCode = "200", description = "회원가입 후 펫 생성, 성공 시 등록 펫 id 값 반환")
    @Parameter(description = "ex) Bearer eyzaqwd...", name = "Authorization", in = ParameterIn.HEADER)
    public ResponseEntity<PetResultDto<Long>> createPet(
            @RequestPart("petCreateRequestDto") PetCreateRequestDto petCreateRequestDto,
            @RequestPart("uploadPhoto") MultipartFile file,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {

        Long savedId = petService.addPetToMember(petCreateRequestDto, file, principalDetails);

        return ResponseEntity.ok().body(PetResultDto.<Long>builder()
                .status(true)
                .code(200)
                .message("펫 등록 성공!")
                .data(savedId)
                .build());
    }

    @GetMapping("/{petId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PetResponseDto> readPet(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable("petId") Long id
    ) {
        PetResponseDto myPet = petService.findMyPet(principalDetails, id);

        return ResponseEntity.ok(myPet);
    }

    @GetMapping()
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<PetResponseDto>> myPets(
            @AuthenticationPrincipal PrincipalDetails principalDetails) {

        List<PetResponseDto> allPets = petService.findAllPets(principalDetails);

        return ResponseEntity.ok(allPets);
    }

    @DeleteMapping("/{petId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> deletePet(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable("petId") Long id
    ) {
        petService.deletePet(principalDetails, id);

        return ResponseEntity.ok().body("펫 삭제에 성공하였습니다");
    }

    @PutMapping("/{petId}")
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
