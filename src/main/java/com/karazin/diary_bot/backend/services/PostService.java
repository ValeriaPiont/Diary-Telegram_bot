package com.karazin.diary_bot.backend.services;

import com.karazin.diary_bot.backend.entities.Post;

import java.util.List;

public interface PostService {

    void savePostText(String text, Long userId);

    void deletePost(Long postId);

    List<Post> getAllPostsByUserTelegramId(Long id);

    Post getPostById(Long postId);

    boolean updatePostTitle(Long postId, String title);

    void updatePostText(Long postId, String text, boolean isAllText);

}
