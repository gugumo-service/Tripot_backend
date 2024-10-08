package com.junior.controller;

import com.junior.dto.ActivateMemberDto;
import com.junior.security.UserPrincipal;
import com.junior.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PatchMapping("/api/v1/member/activate")
    public ResponseEntity<String> activeMember(@AuthenticationPrincipal UserPrincipal userPrincipal, ActivateMemberDto activateMemberDto) {
        memberService.activateMember(userPrincipal, activateMemberDto);

        //TODO: 응답 양식에 따른 수정 필요
        return ResponseEntity.ok("회원 활성화 완료");
    }

}
