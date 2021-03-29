package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.AnswerDAO;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service
public class AnswerCreateBusinessService {

    @Autowired
    private AnswerDAO answerDAO;

    @Autowired
    private UserAuthenticationBusinessService userAuthenticationBusinessService;

    @Autowired
    private QuestionFetchBusinessService questionFetchBusinessService;

    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity createAnswer(String questionId, String authorization, String answer) throws AuthorizationFailedException, InvalidQuestionException {
        UserAuthEntity userAuthEntity = userAuthenticationBusinessService.authenticateUser(authorization, "User is signed out.Sign in first to post an answer");
        QuestionEntity questionEntity = questionFetchBusinessService.getQuestion(questionId, "The question entered is invalid");
        AnswerEntity answerEntity = new AnswerEntity();
        answerEntity.setUuid(UUID.randomUUID().toString());
        answerEntity.setAns(answer);
        answerEntity.setUser(userAuthEntity.getUser());
        answerEntity.setQuestion(questionEntity);
        final ZonedDateTime now = ZonedDateTime.now();
        answerEntity.setDate(now);
        AnswerEntity persistedAnswerEntity = answerDAO.createAnswer(answerEntity);
        return persistedAnswerEntity;
    }
}

