package com.junior.repository.story;

import com.junior.domain.like.Like;
import com.junior.repository.story.custom.LikeCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository  extends JpaRepository<Like, Long>, LikeCustomRepository {
}
