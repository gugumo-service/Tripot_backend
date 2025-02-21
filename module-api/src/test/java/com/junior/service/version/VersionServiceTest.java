package com.junior.service.version;

import com.junior.domain.version.Platform;
import com.junior.domain.version.Version;
import com.junior.dto.version.VersionDto;
import com.junior.repository.version.VersionRepository;
import com.junior.service.BaseServiceTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;


class VersionServiceTest extends BaseServiceTest {

    @Mock
    private VersionRepository versionRepository;

    @InjectMocks
    private VersionService versionService;

    @Test
    @DisplayName("버전 추가 - 버전이 정상적으로 저장되어야 함")
    void createVersion() {

        //given
        VersionDto versionDto = VersionDto.builder()
                .version("1.0.1")
                .forceUpdate(true)
                .build();

        Platform iosPlatform = Platform.IOS;

        //when
        versionService.createVersion(versionDto, iosPlatform);

        //then
        verify(versionRepository).save(any(Version.class));
    }
}