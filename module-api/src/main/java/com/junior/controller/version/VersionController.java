package com.junior.controller.version;

import com.junior.domain.version.Platform;
import com.junior.dto.version.VersionDto;
import com.junior.exception.StatusCode;
import com.junior.response.CommonResponse;
import com.junior.service.version.VersionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class VersionController {

    private final VersionService versionService;

    @Secured("ADMIN")
    @PostMapping("/api/v1/version/{platform}")
    public ResponseEntity<CommonResponse<Object>> createVersion(@RequestBody VersionDto versionDto, @PathVariable("platform") String platform) {
        versionService.createVersion(versionDto, Platform.valueOf(platform.toUpperCase()));
        return ResponseEntity.status(StatusCode.VERSION_CREATE_SUCCESS.getHttpCode()).body(CommonResponse.success(StatusCode.VERSION_CREATE_SUCCESS, null));
    }
}
