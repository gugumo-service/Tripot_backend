package com.junior.repository.notification;

import com.junior.dto.notification.ResponseNotificationDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

import static com.junior.domain.story.QStory.story;
import static com.junior.domain.notification.QNotification.notification;

@Slf4j
@RequiredArgsConstructor
public class NotificationCustomRepositoryImpl implements NotificationCustomRepository{

    private final JPAQueryFactory query;

    private boolean isHaveNextStoryList(List<ResponseNotificationDto> stories, Pageable pageable) {

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
            return notification.id.lt(cursorId);
        }
        return null;
    }
//    @Override
    public Slice<ResponseNotificationDto> findAllNotificationByMemberAndIsReadFalse(Long memberId, Pageable pageable, Long cursorId) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        booleanBuilder.and(notification.memberId.eq(memberId));
        booleanBuilder.and(eqCursorId(cursorId));
        booleanBuilder.and(notification.isDeleted.eq(false));

        List<ResponseNotificationDto> notifications = query.select(Projections.constructor(
                        ResponseNotificationDto.class,
                        notification.id,
                        notification.storyId,
                        notification.content,
                        notification.profileImgPath,
                        notification.memberId,
                        notification.isRead,
                        notification.createdDate,
                        notification.notificationType
                ))
                .from(notification)
                .where(booleanBuilder)
                .orderBy(notification.createdDate.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = isHaveNextStoryList(notifications, pageable);

        return new SliceImpl<>(notifications, pageable, hasNext);
    }
}
