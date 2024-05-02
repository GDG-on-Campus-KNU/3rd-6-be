package com.gdsc.petwalk.domain.pet.entity;

import com.gdsc.petwalk.domain.member.entity.Member;
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

    @Column(name = "photo_url")
    private String photoUrl;

    private String description;

    @Column(name = "likes_count", nullable = false)
    private Integer likesCount = 0;

    @OneToMany(mappedBy = "pet")
    private List<Review> reviews = new ArrayList<>();

    private String region;
    private String neighborhood;
}
