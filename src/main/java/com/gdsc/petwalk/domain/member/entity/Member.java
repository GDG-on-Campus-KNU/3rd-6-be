package com.gdsc.petwalk.domain.member.entity;

import com.gdsc.petwalk.domain.board.entity.Board;
import com.gdsc.petwalk.domain.board.entity.Comment;
import com.gdsc.petwalk.domain.chat.entity.Message;
import com.gdsc.petwalk.domain.match.entity.Match;
import com.gdsc.petwalk.domain.pet.entity.Pet;
import com.gdsc.petwalk.domain.review.entity.Review;
import com.gdsc.petwalk.domain.walkinvitation.entity.WalkInvitation;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "members")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "name")
    private String name; // 사용자 이름

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role; // 사용자의 역할

    @Column(nullable = false)
    private double latitude; // 위도

    @Column(nullable = false)
    private double longitude; //경도

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "refresh_token")
    private String refresh;

    @OneToMany(mappedBy = "member", fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Pet> pets = new ArrayList<>();

    @OneToMany(mappedBy = "requester")
    private List<Match> requestMatches = new ArrayList<>();

    @OneToMany(mappedBy = "acceptor")
    private List<Match> acceptMatches = new ArrayList<>();

    @OneToMany(mappedBy = "reviewer")
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "sender")
    private List<Message> messages = new ArrayList<>();

    @OneToMany(mappedBy = "writer")
    private List<Board> boards = new ArrayList<>();

    @OneToMany(mappedBy = "commenter")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "writer")
    private List<WalkInvitation> walkInvitations = new ArrayList<>();

    @Builder
    public Member(Long id, String name, String email, String password, Role role, double latitude, double longitude, String photoUrl, String refresh, List<Pet> pets, List<Match> requestMatches, List<Match> acceptMatches, List<Review> reviews, List<Message> messages, List<Board> boards, List<Comment> comments, List<WalkInvitation> walkInvitations) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.latitude = latitude;
        this.longitude = longitude;
        this.photoUrl = photoUrl;
        this.refresh = refresh;
        this.pets = pets;
        this.requestMatches = requestMatches;
        this.acceptMatches = acceptMatches;
        this.reviews = reviews;
        this.messages = messages;
        this.boards = boards;
        this.comments = comments;
        this.walkInvitations = walkInvitations;
    }

    public void updateMember(Member member) {
        if (member.getEmail() != null) {
            this.email = member.getEmail();
        }

        if (member.getName() != null) {
            this.name = member.getName();
        }

        if (member.getRole() != null) {
            this.role = member.getRole();
        }
    }

    public void updateRefreshToken(String refresh) {
        this.refresh = refresh;
    }
}
