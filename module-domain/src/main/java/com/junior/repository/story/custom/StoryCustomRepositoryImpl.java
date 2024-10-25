package com.junior.repository.story.custom;

import com.junior.dto.story.QResponseStoryDto;
import com.junior.dto.story.ResponseStoryDto;
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
        return new QResponseStoryDto(story.id, story.title, story.content, story.latitude, story.longitude, story.city, story.likeCnt, story.isHidden);
    }

    @Override
    public List<ResponseStoryDto> findStoryByTitleDevTest(String title) {

        return query.select(createQResponseStoryDto())
                .from(story)
                .where(story.title.eq(title))
                .fetch();
    }

    @Override
    public Page<ResponseStoryDto> findAllStories(String sort, String category, Pageable pageable) {

        List<ResponseStoryDto> stories = query.select(createQResponseStoryDto())
                .from(story)
                .fetch();

        return new PageImpl<>(stories, pageable, stories.size());
    }

    public Slice<ResponseStoryDto> findAllStories(Long cursorId, Pageable pageable) {
        List<ResponseStoryDto> stories = query.select(createQResponseStoryDto())
                .from(story)
                .where(eqCursorId(cursorId))
                .limit(pageable.getPageSize() + 1)
                .orderBy(story.id.asc())
                .fetch();

        boolean hasNext = false;
        if(stories.size() == pageable.getPageSize()+1) {
            stories.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(stories, pageable, hasNext);
    }

    private BooleanExpression eqCursorId(Long cursorId) {
        if(cursorId != null) {
            return story.id.gt(cursorId);
        }
        return null;
    }
}
