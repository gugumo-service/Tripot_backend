package com.junior.domain.story;

import com.junior.domain.base.BaseEntity;
import com.junior.domain.member.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "story_id", nullable = false)
    private Story story;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(length = 500, nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @Builder.Default
    private Boolean isDeleted = false;

    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    @Builder.Default
    private List<Comment> child = new ArrayList<>();

    // 부모 댓글을 추가하면서 부모 댓글에도 자식 댓글 추가
    public void updateParent(Comment parentComment) {
        this.parent = parentComment;
        parentComment.addChild(this);
    }
    public void deleteComment() {
        this.isDeleted = true;
    }

    public void addChild(Comment childComment) {
        this.child.add(childComment);
    }

    public void updateComment(String content) {
        this.content = content;
    }
}
