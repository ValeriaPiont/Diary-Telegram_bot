package com.karazin.diary_bot.backend.services.impl;

import com.karazin.diary_bot.backend.dao.impl.PostDAOImpl;
import com.karazin.diary_bot.backend.dao.impl.UserDAOImpl;
import com.karazin.diary_bot.backend.exceptions.EntityNotFoundException;
import com.karazin.diary_bot.backend.model.Post;
import com.karazin.diary_bot.backend.model.User;
import com.karazin.diary_bot.backend.services.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PostServiceImpl implements PostService {

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

    public void updatePostTitle(Long postId, String title) {
        Optional<Post> optionalPost = postDAO.findPostById(postId);
        if (optionalPost.isEmpty()) {
            throw new EntityNotFoundException("Post with id " + postId + " not found");
        }
        Post updatedPost = optionalPost.get();
        updatedPost.setTitle(title);
        postDAO.updatePost(updatedPost);
    }

    public void updatePostText(Long postId, String text, boolean isAllText) {
        Optional<Post> optionalPost = postDAO.findPostById(postId);
        if (optionalPost.isEmpty()) {
            throw new EntityNotFoundException("Post with id " + postId + " not found");
        }
        Post updatedPost = optionalPost.get();
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
        Optional<Post> optionalPost = postDAO.findPostById(postId);
        if (optionalPost.isEmpty()) {
            throw new EntityNotFoundException("Post with id " + postId + " not found");
        }
        return optionalPost.get();
    }

}
