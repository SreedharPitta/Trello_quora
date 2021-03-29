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
public class QuestionEditBusinessService {

    @Autowired
    private QuestionDAO questionDAO;

    @Autowired
    private UserAuthenticationBusinessService userAuthenticationBusinessService;

    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity editQuestion(final String questionId, final String authorization, final String content) throws AuthorizationFailedException, InvalidQuestionException {
        UserAuthEntity userAuthEntity = userAuthenticationBusinessService.authenticateUser(authorization);
        if (userAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to edit the question");
        }
        QuestionEntity questionEntity = questionDAO.getQuestion(questionId);
        if (questionEntity != null) {
            UserEntity user = userAuthEntity.getUser();
            if (user.getUuid().equals(questionEntity.getUser().getUuid())) {
                QuestionEntity updatedQuestionEntity = questionDAO.editQuestion(questionEntity);
                return updatedQuestionEntity;
            } else {
                throw new AuthorizationFailedException("ATHR-003", "Only the question owner can edit the question");
            }
        } else {
            throw new InvalidQuestionException("QUES-001", "Entered question uuid does not exist");
        }
    }
}
