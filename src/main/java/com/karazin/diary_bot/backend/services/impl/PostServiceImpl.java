package com.karazin.diary_bot.backend.services.impl;

import com.karazin.diary_bot.backend.dao.impl.PostDAOImpl;
import com.karazin.diary_bot.backend.dao.impl.UserDAOImpl;
import com.karazin.diary_bot.backend.exceptions.EntityNotFoundException;
import com.karazin.diary_bot.backend.entities.Post;
import com.karazin.diary_bot.backend.entities.User;
import com.karazin.diary_bot.backend.services.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
public class PostServiceImpl implements PostService {

    private static final int POST_LENGTH = 50;
    private static final String DATE_TITLE = "Note dated ";
    private static final String DATE_TITLE_FORMAT = "yyyy-MM-dd HH:mm";

    private final PostDAOImpl postDAO;
    private final UserDAOImpl userDAO;

    @Autowired
    public PostServiceImpl(PostDAOImpl postDAO, UserDAOImpl userDAO) {
        this.postDAO = postDAO;
        this.userDAO = userDAO;
    }

    public void savePostText(String text, Long telegramId) {
        User user = userDAO.findByTelegramId(telegramId)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + telegramId + " not found"));
        Post addedPost = new Post();
        addedPost.setText(text);
        LocalDateTime date = LocalDateTime.now();
        addedPost.setCreatedOn(date);
        addedPost.setTitle(DATE_TITLE + date.format(DateTimeFormatter.ofPattern(DATE_TITLE_FORMAT)));
        postDAO.addPost(user.getId(), addedPost);
        log.info("Saved post: {}", addedPost);
    }

    public boolean updatePostTitle(Long postId, String title) {
        if (isValidLength(title)) {
            return false;
        }
        Post updatedPost = getPostById(postId);
        updatedPost.setTitle(title);
        postDAO.updatePost(updatedPost);
        return true;
    }

    public boolean isValidLength(String title) {
        return title.length() > POST_LENGTH;
    }

    public void updatePostText(Long postId, String text, boolean isAllText) {
        Post updatedPost = getPostById(postId);
        if (isAllText) {
            updatedPost.setText(text);
        } else {
            String previousText = updatedPost.getText();
            updatedPost.setText(previousText + "\n" + text);
        }
        LocalDateTime date = LocalDateTime.now();
        updatedPost.setUpdatedOn(date);
        updatedPost.setTitle(DATE_TITLE + date.format(DateTimeFormatter.ofPattern(DATE_TITLE_FORMAT)));
        postDAO.updatePost(updatedPost);
        log.info("Updated post {}", updatedPost);
    }

    public void deletePost(Long postId) {
        Post post = getPostById(postId);
        postDAO.deletePost(post);
    }

    public List<Post> getAllPostsByUserTelegramId(Long telegramId) {
        return postDAO.findAllPosts(telegramId);
    }

    public Post getPostById(Long postId) {
        return postDAO.findPostById(postId).orElseThrow(() -> new EntityNotFoundException("Post with id " + postId + " not found"));
    }

}
