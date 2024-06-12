package com.gdsc.petwalk.domain.pet.dto.request;

public record PetCreateRequestDto(String nickname, String gender, Integer age,
                                  String description, String dogType) {

}

