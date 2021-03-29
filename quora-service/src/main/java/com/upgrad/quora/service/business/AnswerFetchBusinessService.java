package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.AnswerDAO;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnswerFetchBusinessService {

    @Autowired
    private AnswerDAO answerDAO;

    @Autowired
    private UserAuthenticationBusinessService userAuthenticationBusinessService;

    @Autowired
    private QuestionFetchBusinessService questionFetchBusinessService;


    public Map<QuestionEntity, List<AnswerEntity>> getQuestionAnswers(String questionId, String authorization) throws AuthorizationFailedException, InvalidQuestionException {
        UserAuthEntity userAuthEntity = userAuthenticationBusinessService.authenticateUser(authorization, "User is signed out.Sign in first to get the answers");
        QuestionEntity questionEntity = questionFetchBusinessService.getQuestion(questionId, "The question with entered uuid whose details are to be seen does not exist");
        List<AnswerEntity> answerEntities = answerDAO.getQuestionAnswers(questionEntity);
        Map<QuestionEntity, List<AnswerEntity>> questionEntityListMap = new HashMap<QuestionEntity, List<AnswerEntity>>();
        questionEntityListMap.put(questionEntity, answerEntities);
        return questionEntityListMap;
    }
}
