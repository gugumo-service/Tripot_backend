package com.junior.domain.like;

import com.junior.domain.member.Member;
import com.junior.domain.story.Story;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity(name = "likes")
@Builder
@Getter
public class Like {

    @Id
    @Column(name = "like_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "story_id")
    private Story story;
}