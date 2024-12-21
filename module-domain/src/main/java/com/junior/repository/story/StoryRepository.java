package com.junior.repository.story;

import com.junior.domain.story.Story;
import com.junior.repository.story.custom.StoryCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoryRepository extends JpaRepository<Story, Long>, StoryCustomRepository {

}
