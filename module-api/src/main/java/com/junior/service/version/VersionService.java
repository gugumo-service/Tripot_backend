package com.junior.service.version;

import com.junior.domain.version.Platform;
import com.junior.domain.version.Version;
import com.junior.dto.version.VersionDto;
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
}
