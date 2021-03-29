package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.QuestionDAO;
import com.upgrad.quora.service.dao.UserDAO;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;

@Service
public class QuestionFetchBusinessService {

    @Autowired
    private QuestionDAO questionDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private UserAuthenticationBusinessService userAuthenticationBusinessService;

    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity getQuestion(final String questionId, final String message) throws InvalidQuestionException {
        QuestionEntity questionEntity = questionDAO.getQuestion(questionId);
        if (questionEntity == null) {
            throw new InvalidQuestionException("QUES-001", message);
        } else {
            return questionEntity;
        }
    }

    //To get All Questions Posted by All Users
    @Transactional(propagation = Propagation.REQUIRED)
    public List<QuestionEntity> getAllQuestions(final String authorizationToken) throws AuthorizationFailedException {
        UserAuthEntity userAuthEntity = userAuthenticationBusinessService.authenticateUser(authorizationToken, "User is signed out.Sign in first to get all questions");
        return questionDAO.getAllQuestions();
    }

    //To get All Questions Posted by a Single User
    @Transactional(propagation = Propagation.REQUIRED)
    public List<QuestionEntity> getUserAllQuestions(String userId, String authorization) throws AuthorizationFailedException, UserNotFoundException {
        UserAuthEntity userAuthEntity = userAuthenticationBusinessService.authenticateUser(authorization, "User is signed out.Sign in first to get all questions posted by a specific user");
        UserEntity userEntity = userDAO.getUser(userId);
        if (userEntity == null) {
            throw new UserNotFoundException("USR-001", "User with entered uuid whose question details are to be seen does not exist");
        }
        return questionDAO.getUserAllQuestions(userEntity);
    }
}
