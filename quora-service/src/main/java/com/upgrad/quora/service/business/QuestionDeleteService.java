package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.QuestionDAO;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class QuestionDeleteService {

    @Autowired
    private QuestionDAO questionDAO;

    @Autowired
    private UserAuthenticationBusinessService userAuthenticationBusinessService;

    @Autowired
    private QuestionFetchBusinessService questionFetchBusinessService;

    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity deleteQuestion(final String questionId, final String authorization) throws AuthorizationFailedException, InvalidQuestionException {
        UserAuthEntity userAuthEntity = userAuthenticationBusinessService.authenticateUser(authorization, "User is signed out.Sign in first to delete a question");
        QuestionEntity questionEntity = questionFetchBusinessService.getQuestion(questionId, "Entered question uuid does not exist");
        UserEntity user = userAuthEntity.getUser();
        if (!user.getUuid().equals(questionEntity.getUser().getUuid()) && "nonadmin".equals(user.getRole())) {
            throw new AuthorizationFailedException("ATHR-003", "Only the question owner or admin can delete the question");
        }
        questionDAO.deleteQuestion(questionEntity);
        return questionEntity;
    }
}
