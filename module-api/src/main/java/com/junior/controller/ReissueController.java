package com.junior.controller;

import com.junior.dto.ReissueDto;
import com.junior.service.ReissueService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReissueController {

    private final ReissueService reissueService;

    @PostMapping("/api/v1/reissue")
    public ResponseEntity<String> reissue(@RequestBody ReissueDto reissueDto, HttpServletResponse response) {

        reissueService.reissue(reissueDto, response);

        return ResponseEntity.ok("AccessToken 재발급 완료");

    }

}
