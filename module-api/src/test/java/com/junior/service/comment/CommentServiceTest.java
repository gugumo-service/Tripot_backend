package com.junior.service.comment;

import com.junior.repository.comment.CommentRepository;
import com.junior.repository.member.MemberRepository;
import com.junior.repository.story.StoryRepository;
import com.junior.service.member.MemberService;
import com.junior.service.s3.S3Service;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {
    @Mock
    MemberRepository memberRepository;

    @Mock
    StoryRepository storyRepository;

    @Mock
    CommentRepository commentRepository;

    @Mock
    S3Service s3Service;

    @InjectMocks
    MemberService memberService;


    @InjectMocks
    CommentService commentService;

    @Test
    @DisplayName("test")
    public void test() {

    }
}
