package com.karazin.diary_bot.backend.services.impl;

import com.karazin.diary_bot.backend.exceptions.EntityNotFoundException;
import com.karazin.diary_bot.backend.entities.Post;
import com.karazin.diary_bot.backend.entities.User;
import com.karazin.diary_bot.backend.repositories.PostRepository;
import com.karazin.diary_bot.backend.repositories.UserRepository;
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

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public void savePostText(String text, Long telegramId) {
        User user = userRepository.findByTelegramId(telegramId)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + telegramId + " not found"));
        LocalDateTime date = LocalDateTime.now();
        Post addedPost =  Post.builder()
                .text(text)
                .createdOn(date)
                .updatedOn(date)
                .title(createDateTitle(date, true))
                .user(user)
                .build();
        postRepository.save(addedPost);
        log.info("Saved post: {} to user with telegram id {}", addedPost, telegramId);
    }

    public boolean updatePostTitle(Long postId, String title) {
        if (isValidLength(title)) {
            return false;
        }
        Post updatedPost = getPostById(postId);
        updatedPost.setTitle(title);
        postRepository.save(updatedPost);
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
        postRepository.save(updatedPost);
        log.info("Updated post {}", updatedPost);
    }

    private String createDateTitle(LocalDateTime date, boolean isCreated) {
        String titleDate = date.format(DateTimeFormatter.ofPattern(DATE_TITLE_FORMAT));
        return isCreated ? DATE_TITLE_CREATED + titleDate
                : DATE_TITLE_UPDATED + titleDate;
    }

    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
        log.info("Deleted post with id {}", postId);
    }

    public List<Post> getAllPostsByUserTelegramId(Long telegramId) {
        log.info("Get all posts");
        return postRepository.findAllByUserTelegramIdOrderByCreatedOnDesc(telegramId);
    }

    public Post getPostById(Long postId) {
        return postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException("Post with id " + postId + " not found"));
    }

}
