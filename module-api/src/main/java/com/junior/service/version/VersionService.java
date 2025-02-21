package com.junior.service.version;

import com.junior.domain.version.Platform;
import com.junior.domain.version.Version;
import com.junior.dto.version.VersionCheckDto;
import com.junior.dto.version.VersionCheckResponseDto;
import com.junior.dto.version.VersionDto;
import com.junior.exception.CustomException;
import com.junior.exception.StatusCode;
import com.junior.repository.version.VersionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class VersionService {

    private final VersionRepository versionRepository;

    public void createVersion(VersionDto versionDto, Platform platform) {
        Version version = Version.builder()
                .platform(platform)
                .version(versionDto.version())
                .forceUpdate(versionDto.forceUpdate())
                .build();

        versionRepository.save(version);
    }

    public VersionCheckResponseDto checkVersion(Platform platform, VersionCheckDto versionCheckDto) {

        Version latestVersion = versionRepository.findFirstByPlatformOrderByCreatedDateDesc(platform)
                .orElseThrow(() -> new CustomException(StatusCode.VERSION_NOT_FOUND));

        boolean forceUpdate = false;
        boolean requireUpdate = false;

        if (versionCheckDto.version().compareTo(latestVersion.getVersion()) < 0) {
            requireUpdate = true;
            if (latestVersion.getForceUpdate()) {
                forceUpdate = true;
            }
        }

        return VersionCheckResponseDto.builder()
                .requireUpdate(requireUpdate)
                .forceUpdate(forceUpdate)
                .build();


    }
}
