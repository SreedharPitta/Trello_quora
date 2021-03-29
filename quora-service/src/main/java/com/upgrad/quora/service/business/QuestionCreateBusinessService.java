package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.QuestionDAO;
import com.upgrad.quora.service.dao.UserDAO;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service
public class QuestionCreateBusinessService {

    @Autowired
    private QuestionDAO questionDAO;

    @Autowired
    private UserAuthenticationBusinessService userAuthenticationBusinessService;

    public QuestionEntity createQuestion(final String authorizationToken, final String content) throws AuthorizationFailedException {
        UserAuthEntity userAuthEntity = userAuthenticationBusinessService.authenticateUser(authorizationToken);
        if (userAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to post a question");
        }
        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setUuid(UUID.randomUUID().toString());
        questionEntity.setContent(content);
        questionEntity.setUser(userAuthEntity.getUser());
        final ZonedDateTime now = ZonedDateTime.now();
        questionEntity.setDate(now);
        QuestionEntity persistedQuestionEntity = questionDAO.createQuestion(questionEntity);
        return persistedQuestionEntity;
    }

}
