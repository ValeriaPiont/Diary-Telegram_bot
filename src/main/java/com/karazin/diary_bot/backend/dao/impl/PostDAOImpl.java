package com.karazin.diary_bot.backend.dao.impl;

import com.karazin.diary_bot.backend.dao.PostDAO;
import com.karazin.diary_bot.backend.dao.util.EntityManagerPerform;
import com.karazin.diary_bot.backend.exceptions.RollBackException;
import com.karazin.diary_bot.backend.entities.Post;
import com.karazin.diary_bot.backend.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;

@Repository
public class PostDAOImpl implements PostDAO {

    private final EntityManagerPerform entityManagerPerform;

    @Autowired
    public PostDAOImpl(EntityManagerPerform entityManagerPerform) {
        this.entityManagerPerform = entityManagerPerform;
    }

    public void updatePost(Post post) {
        entityManagerPerform.perform(entityManager -> entityManager.persist(entityManager.merge(post)));
    }

    public void deletePost(Post post) {
        entityManagerPerform.perform(entityManager -> entityManager.remove(entityManager.merge(post)));
    }

    public List<Post> findAllPosts(Long telegramId) {
        return entityManagerPerform.performWith(entityManager -> entityManager.createQuery("select p from Post p LEFT JOIN p.user u where u.telegramId =: telegramId ORDER BY p.updatedOn DESC", Post.class)
                .setParameter("telegramId", telegramId)
                .getResultList()
        );
    }

    public void addPost(Long userId, Post post) {
        EntityManager entityManager = entityManagerPerform.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        try {
            User user = entityManager.find(User.class, userId);
            user.addPost(post);
            entityManager.merge(user);
            entityManager.persist(post);
            transaction.commit();
        } catch (Exception ex) {
            transaction.rollback();
            throw new RollBackException();
        }
    }

    public Optional<Post> findPostById(Long id) {
        return Optional.ofNullable(entityManagerPerform.performWith(entityManager -> entityManager.find(Post.class, id)));
    }

}
