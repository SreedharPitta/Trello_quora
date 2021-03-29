package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class AnswerDAO {

    @PersistenceContext
    private EntityManager entityManager;

    //To Create New Answer
    public AnswerEntity createAnswer(final AnswerEntity answerEntity) {
        entityManager.persist(answerEntity);
        return answerEntity;
    }

    //To Edit an Answer
    public AnswerEntity editAnswer(final AnswerEntity answerEntity) {
        entityManager.merge(answerEntity);
        return answerEntity;
    }

    //To Delete an Answer
    public void deleteAnswer(final AnswerEntity answerEntity) {
        entityManager.remove(answerEntity);
    }

    //To get an Answer By Uuid
    public AnswerEntity getAnswer(final String answerId) {
        try {
            return entityManager.createNamedQuery("answerByUuid", AnswerEntity.class).setParameter("uuid", answerId).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    //To get all Answers to a Question
    public List<AnswerEntity> getQuestionAnswers(final QuestionEntity questionEntity) {
        return entityManager.createNamedQuery("questionAnswers", AnswerEntity.class).setParameter("question", questionEntity).getResultList();
    }
}
