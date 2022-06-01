package com.karazin.diary_bot.backend.dao.util;

import com.karazin.diary_bot.backend.exceptions.RollBackException;
import lombok.Getter;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.function.Consumer;
import java.util.function.Function;

@Component
public class EntityManagerPerform {

    @Getter
    private final EntityManagerFactory entityManagerFactory;

    public EntityManagerPerform(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public void perform(Consumer<EntityManager> operation) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        try {
            operation.accept(entityManager);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new RollBackException();
        } finally {
            entityManager.close();
        }
    }

    public <T> T performWith(Function<EntityManager, T> entityManagerFunction) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        try {
            T result = entityManagerFunction.apply(entityManager);
            entityManager.getTransaction().commit();
            return result;
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            return null;
        } finally {
            entityManager.close();
        }
    }

}
