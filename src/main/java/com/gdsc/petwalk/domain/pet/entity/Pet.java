package com.gdsc.petwalk.domain.pet.entity;

import com.gdsc.petwalk.domain.member.entity.Member;
import com.gdsc.petwalk.domain.pet.dto.request.PetUpdateRequestDto;
import com.gdsc.petwalk.domain.photo.entity.Photo;
import com.gdsc.petwalk.domain.review.entity.Review;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "pets")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Pet {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pet_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private String nickname;

    private String gender;
    private Integer age;

    private String description;

    @Column(name = "dog_type")
    private String dogType;

    @Column(name = "likes_count", nullable = false)
    private Integer likesCount = 0;

    @OneToMany(mappedBy = "pet")
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "pet")
    private List<Photo> photos = new ArrayList<>();

    @Builder
    public Pet(Long id, Member member, String nickname, String gender, Integer age, String description, String dogType, Integer likesCount, List<Review> reviews, List<Photo> photos) {
        this.id = id;
        this.member = member;
        this.nickname = nickname;
        this.gender = gender;
        this.age = age;
        this.description = description;
        this.dogType = dogType;
        this.likesCount = likesCount;
        this.reviews = reviews;
        this.photos = photos;
    }

    public void setPetOwner(Member member) {
        this.member = member;
        member.getPets().add(this);
    }

    public void update(PetUpdateRequestDto request) {
        if (request.nickname() != null)
            this.nickname = request.nickname();

        if (request.gender() != null) {
            this.gender = request.gender();
        }

        if (request.age() != null) {
            this.age = request.age();
        }

        if (request.description() != null) {
            this.description = request.description();
        }
    }

}
