package com.junior.service.qna;

import com.junior.domain.admin.Qna;
import com.junior.domain.member.Member;
import com.junior.dto.qna.CreateQnaDto;
import com.junior.dto.qna.QnaAdminDto;
import com.junior.dto.qna.QnaDetailDto;
import com.junior.dto.qna.UpdateQnaDto;
import com.junior.exception.NotValidMemberException;
import com.junior.exception.QnaException;
import com.junior.exception.StatusCode;
import com.junior.page.PageCustom;
import com.junior.repository.member.MemberRepository;
import com.junior.repository.qna.QnaRepository;
import com.junior.security.UserPrincipal;
import com.junior.service.BaseServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;


class QnaAdminServiceTest extends BaseServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private QnaRepository qnaRepository;

    @InjectMocks
    private QnaAdminService qnaAdminService;

    private static Qna createQna() {
        Qna qna = Qna.builder()
                .id(1L)
                .question("question")
                .answer("answer")
                .build();
        return qna;
    }

    @Test
    @DisplayName("Q&A 저장 - 정상적으로 실행되어야 함")
    void saveQna() {

        //given
        CreateQnaDto createQnaDto = new CreateQnaDto("question", "answer");
        Member testMember = createActiveTestMember();
        UserPrincipal principal = new UserPrincipal(testMember);

        given(memberRepository.findById(anyLong())).willReturn(Optional.ofNullable(testMember));

        //when
        qnaAdminService.saveQna(principal, createQnaDto);

        //then
        verify(qnaRepository).save(any(Qna.class));


    }

    @Test
    @DisplayName("Q&A 저장 - 회원을 찾지 못했을 때 관련 예외처리를 해야 함")
    void failToSaveQnaIfMemberNotFound() {

        //given
        CreateQnaDto createQnaDto = new CreateQnaDto("question", "answer");
        Member testMember = createActiveTestMember();
        UserPrincipal principal = new UserPrincipal(testMember);

        given(memberRepository.findById(anyLong())).willReturn(Optional.empty());

        //when, then
        assertThatThrownBy(() -> qnaAdminService.saveQna(principal, createQnaDto))
                .isInstanceOf(NotValidMemberException.class)
                .withFailMessage("유효하지 않은 회원");


    }

    @Test
    @DisplayName("Q&A 조회 - 정상적으로 실행되어야 함")
    void findQna() {

        //given

        //전체 공지 생성
        List<Qna> entityList = new ArrayList<>();

        entityList.add(createQna());

        List<QnaAdminDto> dtoList = new ArrayList<>();

        dtoList.add(new QnaAdminDto(1L, "question"));

        String q = "";
        Pageable requestPageable = PageRequest.of(1, 15);
        Pageable pageableAfterFind = PageRequest.of(0, 15);

        PageImpl<QnaAdminDto> pageList = new PageImpl<>(dtoList, pageableAfterFind, 0);

        given(qnaRepository.findQna(anyString(), any(Pageable.class))).willReturn(pageList);

        //when

        PageCustom<QnaAdminDto> Qna = qnaAdminService.findQna(q, requestPageable);

        //then

        //Q&A가 정상적으로 리턴되었는지 검증
        assertThat(Qna.getContent().get(0).id()).isEqualTo(1L);
        assertThat(Qna.getContent().get(0).question()).isEqualTo("question");

        //1-indexed 페이징이 정상적으로 동작해야 함
        assertThat(Qna.getPageable().getNumber()).isEqualTo(1);
    }

    @Test
    @DisplayName("Q&A 세부 조회 - 정상적으로 실행되어야 함")
    void findQnaDetail() {

        //given

        Long QnaId = 1L;
        Qna Qna = createQna();


        given(qnaRepository.findById(1L)).willReturn(Optional.ofNullable(Qna));
        given(memberRepository.findById(Qna.getCreatedBy())).willReturn(Optional.ofNullable(Member.builder()
                .id(1L)
                .nickname("nickname")
                .build()));


        //when
        QnaDetailDto QnaDetail = qnaAdminService.findQnaDetail(QnaId);

        //then
        assertThat(QnaDetail.id()).isEqualTo(1L);
        assertThat(QnaDetail.question()).isEqualTo("question");
        assertThat(QnaDetail.answer()).isEqualTo("answer");


    }

    @Test
    @DisplayName("Q&A 세부 조회 - Q&A를 찾지 못했을 때 관련 예외처리를 해야 함")
    void failToFindQnaDetailIfQnaNotFound() {


        //given

        Long QnaId = 1L;
        Qna Qna = createQna();


        given(qnaRepository.findById(1L)).willReturn(Optional.empty());

        //when, then
        assertThatThrownBy(() -> qnaAdminService.findQnaDetail(QnaId))
                .isInstanceOf(QnaException.class)
                .hasMessageContaining(StatusCode.QNA_NOT_FOUND.getCustomMessage());

    }

    @Test
    @DisplayName("Q&A 세부 조회 - 삭제된 Q&A에 대해 관련 예외 처리를 해야 함")
    void failToFindQnaDetailIfQnaDeleted() {

        //given

        Long QnaId = 1L;
        Qna Qna = createQna();
        Qna.softDelete();

        given(qnaRepository.findById(1L)).willReturn(Optional.ofNullable(Qna));

        //when, then
        assertThatThrownBy(() -> qnaAdminService.findQnaDetail(QnaId))
                .isInstanceOf(QnaException.class)
                .hasMessageContaining(StatusCode.QNA_NOT_FOUND.getCustomMessage());

    }

    @Test
    @DisplayName("Q&A 세부 조회 - 찾을 수 없는 회원에 대해 관련 예외처리를 해야 함")
    void failToFindQnaDetailIfMemberNotFound() {

        //given

        Long QnaId = 1L;
        Qna Qna = createQna();


        given(qnaRepository.findById(1L)).willReturn(Optional.ofNullable(Qna));
        given(memberRepository.findById(Qna.getCreatedBy())).willReturn(Optional.empty());


        //when, then
        assertThatThrownBy(() -> qnaAdminService.findQnaDetail(QnaId))
                .isInstanceOf(NotValidMemberException.class)
                .hasMessageContaining(StatusCode.MEMBER_NOT_FOUND.getCustomMessage());

    }

    @Test
    @DisplayName("Q&A 수정 - 정상적으로 실행되어야 함")
    void updateQna() {

        //given
        Long updateQnaId = 1L;
        UpdateQnaDto updateQnaDto = new UpdateQnaDto("new question", "new answer");

        Qna Qna = createQna();

        given(qnaRepository.findById(updateQnaId)).willReturn(Optional.ofNullable(Qna));

        //when
        qnaAdminService.updateQna(updateQnaId, updateQnaDto);

        //then
        Qna updatedQna = qnaRepository.findById(1L).get();

        assertThat(Qna.getQuestion()).isEqualTo("new question");
        assertThat(Qna.getAnswer()).isEqualTo("new answer");


    }

    @Test
    @DisplayName("Q&A 수정 - Q&A를 찾지 못했을 때 관련 예외처리를 해야 함")
    void failToUpdateQnaIfQnaNotFound() {

        //given
        Long updateQnaId = 1L;
        UpdateQnaDto updateQnaDto = new UpdateQnaDto("new question", "new answer");


        //when, then
        assertThatThrownBy(() -> qnaAdminService.updateQna(updateQnaId, updateQnaDto))
                .isInstanceOf(QnaException.class)
                .hasMessageContaining(StatusCode.QNA_NOT_FOUND.getCustomMessage());


    }

    @Test
    @DisplayName("Q&A 삭제 - 정상적으로 실행되어야 함")
    void deleteQna() {

        //given
        Qna Qna = createQna();
        Long deleteQnaId = 1L;

        given(qnaRepository.findById(deleteQnaId)).willReturn(Optional.ofNullable(Qna));

        //when
        qnaAdminService.deleteQna(deleteQnaId);

        //then
        assertThat(Qna.getIsDeleted()).isTrue();
    }

    @Test
    @DisplayName("Q&A 삭제 - Q&A를 찾지 못했을 때 관련 예외처리를 해야 함")
    void failToDeleteQnaIfQnaNotFound() {

        //given
        Long deleteQnaId = 1L;


        //when, then
        assertThatThrownBy(() -> qnaAdminService.deleteQna(deleteQnaId))
                .isInstanceOf(QnaException.class)
                .hasMessageContaining(StatusCode.QNA_NOT_FOUND.getCustomMessage());


    }

}