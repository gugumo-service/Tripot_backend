package com.junior.service.story;

import com.junior.domain.member.Member;
import com.junior.dto.story.ResponseStoryCntByCityDto;
import com.junior.dto.story.ResponseStoryListDto;
import com.junior.repository.story.StoryRepository;
import com.junior.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PublicStoryService {

    private final StoryRepository storyRepository;

    public Slice<ResponseStoryListDto> findStoriesByFilter(UserPrincipal userPrincipal, Long cursorId, int size, String city, String search) {

        Member member = userPrincipal.getMember();

        Pageable pageable = PageRequest.of(0, size);

        return storyRepository.findAllStories(member, cursorId, pageable, city, search);
    }

    public String getRecommendedRandomCity() {
        Optional<String> recommendedRandomCity = storyRepository.getRecommendedRandomCity();

        return recommendedRandomCity.orElse("");
    }

    public String getRecommendedRecentPopularCity() {
        Optional<String> recommendedRecentPopularCity = storyRepository.getRecommendedRecentPopularCity();

        return recommendedRecentPopularCity.orElse("");
    }

    public Slice<ResponseStoryListDto> getRecentPopularStories(UserPrincipal userPrincipal, Long cursorId, int size) {

        Member member = userPrincipal.getMember();

        Pageable pageable = PageRequest.of(0, size);

        return storyRepository.getRecentPopularStories(member, cursorId, pageable);
    }
}