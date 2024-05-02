package com.gdsc.petwalk.domain.chat.entity;

import com.gdsc.petwalk.domain.match.entity.Match;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "sessions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Session {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sessions_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "match_id")
    private Match match;

    @Column(name = "created_time")
    private LocalDateTime createdTime;

    @OneToMany(mappedBy = "session")
    private List<Message> messages = new ArrayList<>();
}
