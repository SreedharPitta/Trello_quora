package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.AnswerDAO;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AnswerDeleteBusinessService {

    @Autowired
    private AnswerDAO answerDAO;

    @Autowired
    private UserAuthenticationBusinessService userAuthenticationBusinessService;

    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity deleteAnswer(String answerId, String authorization) throws AuthorizationFailedException, AnswerNotFoundException {
        UserAuthEntity userAuthEntity = userAuthenticationBusinessService.authenticateUser(authorization, "User is signed out.Sign in first to delete an answer");
        AnswerEntity answerEntity = answerDAO.getAnswer(answerId);
        if (answerEntity == null) {
            throw new AnswerNotFoundException("ANS-001", "Entered answer uuid does not exist");
        }
        UserEntity user = userAuthEntity.getUser();
        if (!user.getUuid().equals(answerEntity.getUser().getUuid()) && "nonadmin".equals(user.getRole())) {
            throw new AuthorizationFailedException("ATHR-003", "Only the answer owner or admin can delete the answer");
        }
        answerDAO.deleteAnswer(answerEntity);
        return answerEntity;
    }
}
