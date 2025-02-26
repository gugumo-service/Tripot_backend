package com.junior.repository.version;

import com.junior.domain.version.Platform;
import com.junior.domain.version.Version;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VersionRepository extends JpaRepository<Version, Long> {

    Optional<Version> findFirstByPlatformOrderByCreatedDateDesc(Platform platform);
}
