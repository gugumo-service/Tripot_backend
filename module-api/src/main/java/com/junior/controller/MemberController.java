package com.junior.controller;

import com.junior.dto.ActivateMemberDto;
import com.junior.security.UserPrincipal;
import com.junior.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /**
     * PREACTIVE 상태의 회원을 활성화시키는 기능
     * @param userPrincipal
     * @param activateMemberDto
     * @return 회원 활성화 완료
     */
    @PatchMapping("/api/v1/members/activate")
    public ResponseEntity<String> activeMember(@AuthenticationPrincipal UserPrincipal userPrincipal, ActivateMemberDto activateMemberDto) {
        memberService.activateMember(userPrincipal, activateMemberDto);

        //TODO: 응답 양식에 따른 수정 필요
        return ResponseEntity.ok("회원 활성화 완료");
    }

    /**
     * 사용가능한 닉네임인지 확인하는 기능
     * @param nickname
     * @return true: valid한 닉네임
     * @return false: valid하지 않은 닉네임(중복 닉네임이 존재함)
     */
    @GetMapping("/api/v1/members/nicknames/check-valid")
    public ResponseEntity<Boolean> checkNicknameValid(@RequestParam String nickname) {
        return ResponseEntity.ok(!memberService.checkDuplicateNickname(nickname));
    }
}
