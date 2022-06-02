package com.karazin.diary_bot.backend.services.impl;

import com.karazin.diary_bot.backend.dao.impl.PostDAOImpl;
import com.karazin.diary_bot.backend.dao.impl.UserDAOImpl;
import com.karazin.diary_bot.backend.exceptions.EntityNotFoundException;
import com.karazin.diary_bot.backend.models.Post;
import com.karazin.diary_bot.backend.models.User;
import com.karazin.diary_bot.backend.services.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PostServiceImpl implements PostService {

    private static final int POST_LENGTH = 50;

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
        Post addedPost = postDAO.addPost(user.getId(), text);
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
        postDAO.updatePost(updatedPost);
    }

    public void deletePost(Long postId) {
        Optional<Post> optionalPost = postDAO.findPostById(postId);
        if (optionalPost.isEmpty()) {
            throw new EntityNotFoundException("Post with id " + postId + " not found");
        }
        postDAO.deletePost(optionalPost.get());
    }

    public List<Post> getAllPostsByUserTelegramId(Long telegramId) {
        List<Post> posts = postDAO.findAllPosts(telegramId);
        log.info("All posts: {}", posts);
        return posts;
    }

    public Post getPostById(Long postId) {
        return postDAO.findPostById(postId).orElseThrow(() -> new EntityNotFoundException("Post with id " + postId + " not found"));
    }

}
