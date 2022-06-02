package com.karazin.diary_bot.backend.dao;

import com.karazin.diary_bot.backend.models.Post;

import java.util.List;
import java.util.Optional;

/**
 * defines and API of Data-Access Object for entity Post
 */
public interface PostDAO {

    /**
     * Updates a post from the database
     *
     * @param post instance
     */
    void updatePost(Post post);

    /**
     * Deletes a post from the database
     *
     * @param post instance
     */
    void deletePost(Post post);

    /**
     * Retrieves all post from the database by user id
     *
     * @param userId user Telegram (chat) id
     * @return user list
     */
    List<Post> findAllPosts(Long userId);

    /**
     * Adds a new post to an existing user. This method does not require additional SQL select methods to load
     *
     * @param userId user Telegram (chat) id
     * @param text   text for post
     * @return post instance
     */
    Post addPost(Long userId, String text);

    /**
     * Retrieves a post from the database by its id
     *
     * @param id post id
     * @return post optional instance
     */
    Optional<Post> findPostById(Long id);


}
