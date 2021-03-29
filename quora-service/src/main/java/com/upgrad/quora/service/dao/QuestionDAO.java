package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class QuestionDAO {

    @PersistenceContext
    private EntityManager entityManager;

    //To Create a Question
    public QuestionEntity createQuestion(final QuestionEntity questionEntity) {
        entityManager.persist(questionEntity);
        return questionEntity;
    }

    //To Edit a Question
    public QuestionEntity editQuestion(final QuestionEntity questionEntity) {
        entityManager.merge(questionEntity);
        return questionEntity;
    }

    //To Delete a Question
    public void deleteQuestion(final QuestionEntity questionEntity) {
        entityManager.remove(questionEntity);
    }

    //To get a Single Question
    public QuestionEntity getQuestion(final String questionId) {
        try {
            return entityManager.createNamedQuery("questionByUuid", QuestionEntity.class).setParameter("uuid", questionId).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    //To get All Questions
    public List<QuestionEntity> getAllQuestions() {
        return entityManager.createNamedQuery("questions", QuestionEntity.class).getResultList();
    }

    public List<QuestionEntity> getUserAllQuestions(final UserEntity userEntity) {
        return entityManager.createNamedQuery("questionsByUser", QuestionEntity.class).setParameter("user", userEntity).getResultList();
    }
}
