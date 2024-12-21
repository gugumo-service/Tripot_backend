package com.junior.repository.member;

import com.junior.TestConfig;
import com.junior.domain.member.Member;
import com.junior.domain.member.MemberRole;
import com.junior.domain.member.MemberStatus;
import com.junior.domain.member.SignUpType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@DataJpaTest
@Import(TestConfig.class)
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void init() {

        Member testMember = Member.builder().nickname("테스트닉")
                .username("KAKAO 3748293466")
                .role(MemberRole.USER)
                .status(MemberStatus.ACTIVE)
                .signUpType(SignUpType.KAKAO)
                .recommendLocation("서울")
                .build();

        memberRepository.save(testMember);
    }

    @AfterEach
    void clean() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("정확히 동일한 username을 가진 회원이 있을 때에만 true를 반환")
    void existsByUsername() {

        boolean findExistUsername = memberRepository.existsByUsername("KAKAO 3748293466");
        boolean findNotExistUsername = memberRepository.existsByUsername("KAKAO 3748293465");

        assertThat(findExistUsername).isTrue();
        assertThat(findNotExistUsername).isFalse();
    }

    @Test
    @DisplayName("정확히 동일한 nickname을 가진 회원이 있을 때에만 true를 반환")
    void existsByNickname() {

        boolean findExistNickname = memberRepository.existsByNickname("테스트닉");
        boolean findNotExistNickname = memberRepository.existsByNickname("테스트");

        assertThat(findExistNickname).isTrue();
        assertThat(findNotExistNickname).isFalse();


    }

    @Test
    @DisplayName("정확히 동일한 username을 가진 회원만을 조회")
    void findByUsername() {

        Optional<Member> existMember = memberRepository.findByUsername("KAKAO 3748293466");
        Optional<Member> notExistMember = memberRepository.findByUsername("KAKAO 3748293465");

        assertThat(existMember.isPresent()).isTrue();
        assertThat(notExistMember.isPresent()).isFalse();
        assertThatThrownBy(notExistMember::get)
                .isInstanceOf(NoSuchElementException.class);

        assertThat(existMember.get().getUsername()).isEqualTo("KAKAO 3748293466");


    }
}