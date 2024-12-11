package com.junior.repository.story.custom;

import com.junior.domain.like.Like;
import com.junior.domain.like.QLike;
import com.junior.domain.member.Member;
import com.junior.domain.story.QStory;
import com.junior.domain.story.Story;
import com.junior.dto.story.*;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
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
        return new QResponseStoryDto(story.id, story.title, story.content, story.thumbnailImg, story.latitude, story.longitude, story.city, story.member, null, story.likeCnt, story.isHidden, story.createdDate, story.imgUrls);
    }

    QResponseStoryListDto createQResponseStoryListDto() {
        return new QResponseStoryListDto(story.thumbnailImg, story.title, story.content, story.city, story.id, story.latitude, story.longitude);
    }

//    public Slice<ResponseStoryDto> findAllStories_old(Long cursorId, Pageable pageable) {
//        List<ResponseStoryDto> stories = query.select(createQResponseStoryDto())
//                .from(story)
//                .where(eqCursorId(cursorId),
//                        story.isHidden.eq(false))
//                .limit(pageable.getPageSize() + 1)
//                .orderBy(story.createdDate.desc())
//                .fetch();
//
//        boolean hasNext = isHaveNextStoryList(stories, pageable);
//
//        return new SliceImpl<>(stories, pageable, hasNext);
//    }

    @Override
    public Slice<ResponseStoryListDto> findAllStories(Long cursorId, Pageable pageable, String city) {

        BooleanBuilder builder = new BooleanBuilder();

        builder.and(eqCursorId(cursorId));
        builder.and(story.isHidden.eq(false));

        if(city != null && !city.isEmpty()) {
            builder.and(story.city.eq(city));
        }

        List<ResponseStoryListDto> stories = query.select(createQResponseStoryListDto())
                .from(story)
                .where(builder)
                .limit(pageable.getPageSize() + 1)
                .orderBy(story.createdDate.desc())
                .fetch();

        boolean hasNext = isHaveNextStoryList(stories, pageable);

        return new SliceImpl<>(stories, pageable, hasNext);
    }

//    @Override
//    public Slice<ResponseStoryDto> findStoriesByMemberAndCity(Long cursorId, Pageable pageable, String city, Member findMember) {
//        List<ResponseStoryDto> stories = query.select(createQResponseStoryDto())
//                .from(story)
//                .where(story.city.eq(city),
//                        story.member.eq(findMember),
//                        eqCursorId(cursorId))
//                .limit(pageable.getPageSize() + 1)
//                .orderBy(story.createdDate.desc())
//                .fetch();
//
//        boolean hasNext = isHaveNextStoryList(stories, pageable);
//
//        return new SliceImpl<>(stories, pageable, hasNext);
//    }

    @Override
    public Story findStoryByIdAndMember(Long storyId, Member member) {

        /*
            Dto 가 아닌 엔티티 그 자체를 반환
            게시글을 수정할 때 더티체킹이 진행되기 위함
         */
        List<Story> stories = query.select(QStory.story)
                .from(story)
                .where(QStory.story.member.eq(member),
                        QStory.story.id.eq(storyId))
                .fetch();

        return stories.get(0);
    }

//    @Override
//    public Slice<ResponseStoryDto> findStoriesByMemberAndMapWithPaging(Long cursorId, Pageable pageable, GeoPointDto geoPointLt, GeoPointDto geoPointRb, Member findMember) {
//
//        List<ResponseStoryDto> stories = query.select(createQResponseStoryDto())
//                .from(story)
//                .where(story.latitude.between(
//                                Math.min(geoPointLt.latitude(), geoPointRb.latitude()),
//                                Math.max(geoPointLt.latitude(), geoPointRb.latitude())
//                        ),
//                        story.longitude.between(
//                                Math.min(geoPointLt.longitude(), geoPointRb.longitude()),
//                                Math.max(geoPointLt.longitude(), geoPointRb.longitude())
//                        ),
//                        eqCursorId(cursorId)
//                )
//                .limit(pageable.getPageSize() + 1)
//                .orderBy(story.createdDate.desc())
//                .fetch();
//
//        boolean hasNext = isHaveNextStoryList(stories, pageable);
//
//        return new SliceImpl<>(stories, pageable, hasNext);
//    }

    @Override
    public List<ResponseStoryListDto> findStoryByMap(Member findMember, GeoPointDto geoPointLt, GeoPointDto geoPointRb) {

        return query.select(createQResponseStoryListDto())
                .from(story)
                .where(story.latitude.between(
                                Math.min(geoPointLt.latitude(), geoPointRb.latitude()),
                                Math.max(geoPointLt.latitude(), geoPointRb.latitude())
                        ),
                        story.longitude.between(
                                Math.min(geoPointLt.longitude(), geoPointRb.longitude()),
                                Math.max(geoPointLt.longitude(), geoPointRb.longitude())
                        )
                )
                .orderBy(story.createdDate.desc())
                .fetch();
    }

    @Override
    public Slice<ResponseStoryListDto> findStoriesByMemberAndCityAndSearch(Long cursorId, Pageable pageable, Member findMember, String city, String search) {

        /*
            BooleanBuilder 란
            queryDsl 에서 동적 조건을 간단하게 구성하기 위한 클래스
            and(), or(), not() 등 사용 가능
         */
        BooleanBuilder isContainSearch = new BooleanBuilder();

        if(city != null && !city.isEmpty()) {
            isContainSearch.and(story.city.eq(city));
        }

        if(search != null && !search.isEmpty()) {
            isContainSearch.and((story.title.contains(search)).or(story.content.contains(search)));
        }

        List<ResponseStoryListDto> stories = query.select(createQResponseStoryListDto())
                .from(story)
                .where(story.member.eq(findMember),
                        isContainSearch,
                        eqCursorId(cursorId))
                .limit(pageable.getPageSize() + 1)
                .orderBy(story.createdDate.desc())
                .fetch();

        boolean hasNext = isHaveNextStoryList(stories, pageable);

        return new SliceImpl<>(stories, pageable, hasNext);
    }

    @Override
    public List<ResponseStoryCntByCityDto> getStoryCntByCity(Member findMember) {

        return query.select(new QResponseStoryCntByCityDto(story.city, story.count().intValue()))
                .from(story)
                .groupBy(story.city)
                .orderBy(story.city.asc())
                .fetch();
    }

    @Override
    public Boolean isLikedMember(Member findMember, Story findStory) {

        QLike like =  QLike.like;

        return query.selectOne()
                .from(like)
                .where(like.member.eq(findMember),
                        like.story.eq(findStory))
                .fetchFirst() != null;
    }

    private boolean isHaveNextStoryList(List<ResponseStoryListDto> stories, Pageable pageable) {

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
