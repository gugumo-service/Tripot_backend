package com.junior.repository.story.custom;

import com.junior.domain.member.Member;
import com.junior.domain.story.QStory;
import com.junior.domain.story.Story;
import com.junior.dto.story.QResponseStoryDto;
import com.junior.dto.story.ResponseStoryDto;
import com.junior.dto.story.GeoPointDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;

import java.util.List;

import static com.junior.domain.story.QStory.story;


@Slf4j
@RequiredArgsConstructor
public class StoryCustomRepositoryImpl implements StoryCustomRepository {

    private final JPAQueryFactory query;

    QResponseStoryDto createQResponseStoryDto() {
        return new QResponseStoryDto(story.id, story.title, story.content, story.thumbnailImg, story.latitude, story.longitude, story.city, story.likeCnt, story.isHidden, story.createdDate);
    }

    public Slice<ResponseStoryDto> findAllStories(Long cursorId, Pageable pageable) {
        List<ResponseStoryDto> stories = query.select(createQResponseStoryDto())
                .from(story)
                .where(eqCursorId(cursorId))
                .limit(pageable.getPageSize() + 1)
                .orderBy(story.createdDate.desc())
                .fetch();

        boolean hasNext = isHaveNextStoryList(stories, pageable);

        return new SliceImpl<>(stories, pageable, hasNext);
    }

    @Override
    public Slice<ResponseStoryDto> findStoriesByMemberAndCity(Long cursorId, Pageable pageable, String city, Member member) {
        List<ResponseStoryDto> stories = query.select(createQResponseStoryDto())
                .from(story)
                .where(story.city.eq(city),
                        story.member.eq(member),
                        eqCursorId(cursorId))
                .limit(pageable.getPageSize() + 1)
                .orderBy(story.createdDate.desc())
                .fetch();

        boolean hasNext = isHaveNextStoryList(stories, pageable);

        return new SliceImpl<>(stories, pageable, hasNext);
    }

    @Override
    public Story findStoryByIdAndMember(Long storyId, Member member) {

        List<Story> stories = query.select(QStory.story)
                .from(story)
                .where(QStory.story.member.eq(member),
                        QStory.story.id.eq(storyId))
                .fetch();

        return stories.get(0);
    }

    @Override
    public Slice<ResponseStoryDto> findStoriesByMemberAndMap(Long cursorId, Pageable pageable, GeoPointDto geoPointLt, GeoPointDto geoPointRb, Member findMember) {

        List<ResponseStoryDto> stories = query.select(createQResponseStoryDto())
                .from(story)
                .where(story.latitude.between(
                                Math.min(geoPointLt.latitude(), geoPointRb.latitude()),
                                Math.max(geoPointLt.latitude(), geoPointRb.latitude())
                        ),
                        story.longitude.between(
                                Math.min(geoPointLt.longitude(), geoPointRb.longitude()),
                                Math.max(geoPointLt.longitude(), geoPointRb.longitude())
                        ),
                        eqCursorId(cursorId)
                )
                .limit(pageable.getPageSize() + 1)
                .orderBy(story.createdDate.desc())
                .fetch();

        boolean hasNext = isHaveNextStoryList(stories, pageable);

        return new SliceImpl<>(stories, pageable, hasNext);
    }

    private boolean isHaveNextStoryList(List<ResponseStoryDto> stories, Pageable pageable) {

        boolean hasNext;

        if(stories.size() == pageable.getPageSize() + 1) {
            stories.remove(pageable.getPageSize());
            hasNext = true;
        }
        else {
            hasNext = false;
        }

        return hasNext;
    }

    private BooleanExpression eqCursorId(Long cursorId) {
        if(cursorId != null) {
            return story.id.lt(cursorId);
        }
        return null;
    }


}
