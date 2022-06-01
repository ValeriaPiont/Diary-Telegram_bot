package com.karazin.diary_bot.backend.dao.impl;

import com.karazin.diary_bot.backend.dao.PostDAO;
import com.karazin.diary_bot.backend.dao.util.EntityManagerPerform;
import com.karazin.diary_bot.backend.exceptions.RollBackException;
import com.karazin.diary_bot.backend.model.Post;
import com.karazin.diary_bot.backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Repository
public class PostDAOImpl implements PostDAO {

    private final EntityManagerPerform entityManagerPerform;
    private static final String DATE_TITLE = "Note dated ";
    private static final String DATE_TITLE_FORMAT = "yyyy-MM-dd HH:mm";

    @Autowired
    public PostDAOImpl(EntityManagerPerform entityManagerPerform) {
        this.entityManagerPerform = entityManagerPerform;
    }

    public void updatePost(Post post) {
        post.setCreatedOn(LocalDateTime.now());
        entityManagerPerform.perform(entityManager -> entityManager.persist(entityManager.merge(post)));
    }

    public void deletePost(Post post) {
        entityManagerPerform.perform(entityManager -> entityManager.remove(entityManager.merge(post)));
    }

    public List<Post> findAllPosts(Long telegramId) {
        return entityManagerPerform.performWith(entityManager -> entityManager.createQuery("select p from Post p LEFT JOIN p.user u where u.telegramId =: telegramId ORDER BY p.createdOn DESC", Post.class)
                .setParameter("telegramId", telegramId)
                .getResultList()
        );
    }

    public Post addPost(Long userId, String text) {
        EntityManager entityManager = entityManagerPerform.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        try {
            User user = entityManager.find(User.class, userId);
            Post post = new Post();
            post.setText(text);
            LocalDateTime date = LocalDateTime.now();
            post.setCreatedOn(date);
            post.setTitle(DATE_TITLE + date.format(DateTimeFormatter.ofPattern(DATE_TITLE_FORMAT)));
            user.addPost(post);
            entityManager.merge(user);
            entityManager.persist(post);
            transaction.commit();
            return post;
        } catch (Exception ex) {
            transaction.rollback();
            throw new RollBackException();
        }

    }

    public Optional<Post> findPostById(Long id) {
        return Optional.ofNullable(entityManagerPerform.performWith(entityManager -> entityManager.find(Post.class, id)));
    }

}
