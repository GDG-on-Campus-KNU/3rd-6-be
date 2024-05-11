package com.gdsc.petwalk.domain.pet.dto.request;

public record PetUpdateRequestDto(String nickname, String gender, Integer age, String photoUrl,
                                  String description) {
}
