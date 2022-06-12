package com.karazin.diary_bot.backend.repositories;

import com.karazin.diary_bot.backend.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByUserTelegramIdOrderByCreatedOnDesc(Long telegramId);

}
