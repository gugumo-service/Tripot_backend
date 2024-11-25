package com.junior.repository.notice;

import com.junior.TestConfig;
import com.junior.domain.admin.Notice;
import com.junior.dto.notice.NoticeAdminDto;
import com.junior.dto.notice.NoticeUserDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestConfig.class)
class NoticeRepositoryTest {

    @Autowired
    private NoticeRepository noticeRepository;

    @BeforeEach
    void init() throws InterruptedException {

        for (int i = 1; i <= 100; i++) {

            Notice notice = Notice.builder()
                    .title("title " + i)
                    .content("content " + i)
                    .build();

            //생성일자 정렬을 위해 잠시 sleep
            Thread.sleep(5);

            noticeRepository.save(notice);

        }

    }

    @AfterEach
    void clean(){
        noticeRepository.deleteAll();
    }

    @Test
    @DisplayName("공지사항 dto를 페이징하여 가져올 수 있어야 함")
    void findNotice_success_first() {

        //given
        String q = "";

        //해당 테스트는 0-indexed 로 진행
        PageRequest pageRequest = PageRequest.of(0, 15);


        //when
        Page<NoticeAdminDto> page = noticeRepository.findNotice(q, pageRequest);
        List<NoticeAdminDto> content = page.getContent();

        //then

        //100개의 페이지를 페이지 크기 15로 잘랐을 때 첫 페이지의 크기는 15이다.
        assertThat(content.size()).isEqualTo(15);

        //페이지는 createDate의 내림차순, 따라서 가장 마지막에 넣은 공지사항 100이 첫 요소여야 한다.
        assertThat(content.get(0).title()).isEqualTo("title 100");

        //요소의 총 개수 100개를 정상적으로 조회할 수 있어야 함
        assertThat(page.getTotalElements()).isEqualTo(100);
    }

    @Test
    @DisplayName("공지사항의 마지막 페이징이 정상적으로 되어야 함")
    void findNotice_success_last() {

        //given
        String q = "";

        //해당 테스트는 0-indexed 로 진행
        PageRequest pageRequest = PageRequest.of(6, 15);


        //when
        Page<NoticeAdminDto> page = noticeRepository.findNotice(q, pageRequest);
        List<NoticeAdminDto> content = page.getContent();

        //then

        //100개의 페이지를 페이지 크기 15로 잘랐을 때 마지막 페이지의 크기는 (100%15) 10이다.
        assertThat(content.size()).isEqualTo(10);

        //가장 마지막 페이지의 첫 요소는 title 10이어야 함
        assertThat(content.get(0).title()).isEqualTo("title 10");

        //요소의 총 개수 100개를 정상적으로 조회할 수 있어야 함
        assertThat(page.getTotalElements()).isEqualTo(100);
    }

    @Test
    @DisplayName("공지사항의 검색이 정상적으로 동작해야 함")
    void findNotice_search_success() {

        //given
        String q = "15";

        //해당 테스트는 0-indexed 로 진행
        PageRequest pageRequest = PageRequest.of(0, 15);


        //when
        Page<NoticeAdminDto> page = noticeRepository.findNotice(q, pageRequest);
        List<NoticeAdminDto> content = page.getContent();

        //then

        //100개의 페이지를 페이지 크기 15로 잘랐을 때 마지막 페이지의 크기는 (100%15) 10이다.
        assertThat(content.size()).isEqualTo(1);

        //가장 마지막 페이지의 첫 요소는 title 10이어야 함
        assertThat(content.get(0).title()).isEqualTo("title 15");

        //요소의 총 개수 100개를 정상적으로 조회할 수 있어야 함
        assertThat(page.getTotalElements()).isEqualTo(1);
    }

    @Test
    @DisplayName("삭제된 공지사항을 조회에서 제외되어야 함")
    void findNotice_deleted_notice() {

        //given
        String q = "";

        //해당 테스트는 0-indexed 로 진행
        PageRequest pageRequest = PageRequest.of(0, 15);

        //title 100 삭제
        Notice deleteNotice = noticeRepository.findById(100L).get();
        deleteNotice.softDelete();


        //when
        Page<NoticeAdminDto> page = noticeRepository.findNotice(q, pageRequest);
        List<NoticeAdminDto> content = page.getContent();

        //then


        //title 100이 삭제되었으므로 0번 페이지의 첫 요소는 title 100이 아니어야 한다.
        assertThat(content.get(0).title()).isNotEqualTo("title 100");

        //총 요소의 개수: 100 - 1 = 99
        assertThat(page.getTotalElements()).isEqualTo(100);

    }

    @Test
    @DisplayName("공지사항 무한스크롤 구현이 정상적으로 동작해야 함")
    public void findNotice_slice_success() throws Exception {
        //given
        Long cursorId = 92L;
        int size = 5;


        PageRequest pageRequest = PageRequest.of(0, size);
        //when
        Slice<NoticeUserDto> notice = noticeRepository.findNotice(cursorId, pageRequest);
        List<NoticeUserDto> content = notice.getContent();

        //then
        assertThat(content.size()).isEqualTo(5);
        assertThat(content.get(0).title()).isEqualTo("title 91");


    }


}