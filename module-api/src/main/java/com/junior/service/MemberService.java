package com.junior.service;

import com.junior.domain.member.Member;
import com.junior.domain.member.MemberStatus;
import com.junior.dto.member.ActivateMemberDto;
import com.junior.dto.member.MemberInfoDto;
import com.junior.dto.member.UpdateNicknameDto;
import com.junior.exception.NotValidMemberException;
import com.junior.exception.StatusCode;
import com.junior.repository.member.MemberRepository;
import com.junior.security.UserPrincipal;
import com.junior.service.s3.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final S3Service s3Service;

    @Transactional
    public void activateMember(UserPrincipal principal, ActivateMemberDto activateMemberDto) {

        Member member = memberRepository.findById(principal.getMember().getId()).orElseThrow(
                () -> new NotValidMemberException(StatusCode.INVALID_MEMBER)
        );


        if (member.getStatus() != MemberStatus.PREACTIVE) {
            log.warn("[{}] Invalid member = {} member.status = {}", Thread.currentThread().getStackTrace()[1].getMethodName(), member.getUsername(), member.getStatus());
            throw new NotValidMemberException(StatusCode.INVALID_MEMBER);
        }

        log.info("[{}}] target: {} nickname: {}, location: {}", Thread.currentThread().getStackTrace()[1].getMethodName(), member.getUsername(), activateMemberDto.nickname(), activateMemberDto.recommendLocation());
        member.activateMember(activateMemberDto);

    }

    public Boolean checkDuplicateNickname(String nickname) {
        log.info("[checkDuplicateNickname] target nickname: {}", nickname);
        return memberRepository.existsByNickname(nickname);
    }

    @Transactional
    public void updateProfileImage(UserPrincipal principal, MultipartFile profileImage) {
        Member member = memberRepository.findById(principal.getMember().getId()).orElseThrow(
                () -> new NotValidMemberException(StatusCode.INVALID_MEMBER)
        );

        if (member.getStatus() != MemberStatus.ACTIVE) {
            log.warn("[{}] Invalid member = {} member.status = {}", Thread.currentThread().getStackTrace()[1].getMethodName(), member.getUsername(), member.getStatus());
            throw new NotValidMemberException(StatusCode.INVALID_MEMBER);
        }


        log.info("[{}] target: {}", Thread.currentThread().getStackTrace()[1].getMethodName(), member.getUsername());

        //프로필 사진을 업데이트 하기 위해 기존 이미지를 삭제
        if (member.getProfileImage() != null) {
            log.info("[{}] delete profile image target: {}", Thread.currentThread().getStackTrace()[1].getMethodName(), member.getUsername());
            s3Service.deleteProfileImage(member.getProfileImage());
            member.deleteProfile();
        }

        if (!profileImage.isEmpty()) {
            String profileUrl = s3Service.saveProfileImage(profileImage);
            member.updateProfile(profileUrl);
        }

    }


    public MemberInfoDto getMemberInfo(UserPrincipal principal) {
        Member member = memberRepository.findById(principal.getMember().getId()).orElseThrow(
                () -> new NotValidMemberException(StatusCode.INVALID_MEMBER)
        );

        if (member.getStatus() != MemberStatus.ACTIVE) {
            log.warn("[{}] Invalid member = {} member.status = {}", Thread.currentThread().getStackTrace()[1].getMethodName(), member.getUsername(), member.getStatus());
            throw new NotValidMemberException(StatusCode.INVALID_MEMBER);
        }

        return MemberInfoDto.builder()
                .nickname(member.getNickname())
                .profileImageUrl(member.getProfileImage())
                .build();

    }

    @Transactional
    public void updateNickname(UserPrincipal principal, UpdateNicknameDto updateNicknameDto) {

        Member member = memberRepository.findById(principal.getMember().getId()).orElseThrow(
                () -> new NotValidMemberException(StatusCode.INVALID_MEMBER)
        );

        if (member.getStatus() != MemberStatus.ACTIVE) {
            log.warn("[{}] Invalid member = {} member.status = {}", Thread.currentThread().getStackTrace()[1].getMethodName(), member.getUsername(), member.getStatus());
            throw new NotValidMemberException(StatusCode.INVALID_MEMBER);
        }

        member.updateNickname(updateNicknameDto);
    }

    @Transactional
    public void deleteMember(UserPrincipal principal) {

        Member member = memberRepository.findById(principal.getMember().getId()).orElseThrow(
                () -> new NotValidMemberException(StatusCode.INVALID_MEMBER)
        );

        if (member.getStatus() != MemberStatus.ACTIVE) {
            log.warn("[{}] Invalid member = {} member.status = {}", Thread.currentThread().getStackTrace()[1].getMethodName(), member.getUsername(), member.getStatus());
            throw new NotValidMemberException(StatusCode.INVALID_MEMBER);
        }

        log.info("[{}] target: {}", Thread.currentThread().getStackTrace()[1].getMethodName(), member.getUsername());
        member.deleteMember();

    }
}
