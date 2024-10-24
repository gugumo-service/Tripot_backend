package com.junior.domain.repository.story;

import com.junior.domain.repository.story.custom.StoryCustomRepository;
import com.junior.domain.story.Story;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoryRepository extends JpaRepository<Story, Long>, StoryCustomRepository {
}
