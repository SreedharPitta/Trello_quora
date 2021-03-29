package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.QuestionDAO;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionDeleteService {

    @Autowired
    private QuestionDAO questionDAO;

    @Autowired
    private UserAuthenticationBusinessService userAuthenticationBusinessService;

    public QuestionEntity deleteQuestion(final String questionId, final String authorization) throws AuthorizationFailedException, InvalidQuestionException {
        UserAuthEntity userAuthEntity = userAuthenticationBusinessService.authenticateUser(authorization);
        if (userAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to delete a question");
        }
        QuestionEntity questionEntity = questionDAO.getQuestion(questionId);
        if (questionEntity != null) {
            UserEntity user = userAuthEntity.getUser();
            if (!user.getUuid().equals(questionEntity.getUser().getUuid()) || "nonadmin".equals(user.getRole())) {
                throw new AuthorizationFailedException("ATHR-003", "Only the question owner or admin can delete the question");
            } else {
                questionDAO.deleteQuestion(questionEntity);
            }
        } else {
            throw new InvalidQuestionException("QUES-001", "Entered question uuid does not exist");
        }
        return questionEntity;
    }
}
