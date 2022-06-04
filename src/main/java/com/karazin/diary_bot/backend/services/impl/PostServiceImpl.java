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
    private static final String DATE_TITLE_CREATED = "Note created ";
    private static final String DATE_TITLE_UPDATED = "Note updated ";
    private static final String DATE_TITLE_FORMAT = "dd-MM-yyyy HH:mm";

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
        addedPost.setUpdatedOn(date);
        addedPost.setTitle(createDateTitle(date, true));
        postDAO.addPost(user.getId(), addedPost);
        log.info("Saved post: {} to user with telegram id {}", addedPost, telegramId);
    }

    public boolean updatePostTitle(Long postId, String title) {
        if (isValidLength(title)) {
            return false;
        }
        Post updatedPost = getPostById(postId);
        updatedPost.setTitle(title);
        postDAO.updatePost(updatedPost);
        log.info("Post with id {} was updated (title)", postId);
        return true;
    }

    public boolean isValidLength(String title) {
        return title.length() > POST_LENGTH;
    }

    public void updatePostText(Long postId, String text, boolean isAllText) {
        Post updatedPost = getPostById(postId);
        log.info("Incoming post {}", updatedPost);
        if (isAllText) {
            updatedPost.setText(text);
        } else {
            String previousText = updatedPost.getText();
            updatedPost.setText(previousText + "\n" + text);
        }
        LocalDateTime date = LocalDateTime.now();
        updatedPost.setUpdatedOn(date);
        if(updatedPost.getTitle().contains(DATE_TITLE_CREATED) || updatedPost.getTitle().contains(DATE_TITLE_UPDATED) ){
            updatedPost.setTitle(createDateTitle(date, false));
        }
        postDAO.updatePost(updatedPost);
        log.info("Updated post {}", updatedPost);
    }

    private String createDateTitle(LocalDateTime date, boolean isCreated) {
        String titleDate = date.format(DateTimeFormatter.ofPattern(DATE_TITLE_FORMAT));
        return isCreated ? DATE_TITLE_CREATED + titleDate
                : DATE_TITLE_UPDATED + titleDate;
    }

    public void deletePost(Long postId) {
        Post post = getPostById(postId);
        postDAO.deletePost(post);
        log.info("Deleted post with id {}", post);
    }

    public List<Post> getAllPostsByUserTelegramId(Long telegramId) {
        log.info("Get all posts");
        return postDAO.findAllPosts(telegramId);
    }

    public Post getPostById(Long postId) {
        return postDAO.findPostById(postId).orElseThrow(() -> new EntityNotFoundException("Post with id " + postId + " not found"));
    }

}
