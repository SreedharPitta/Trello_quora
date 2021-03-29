package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.AnswerDAO;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnswerEditBusinessService {

    @Autowired
    private AnswerDAO answerDAO;

    @Autowired
    private UserAuthenticationBusinessService userAuthenticationBusinessService;

    public AnswerEntity editAnswer(String answerId, String authorization, String content) throws AuthorizationFailedException, AnswerNotFoundException {
        UserAuthEntity userAuthEntity = userAuthenticationBusinessService.authenticateUser(authorization, "User is signed out.Sign in first to edit an answer");
        AnswerEntity answerEntity = answerDAO.getAnswer(answerId);
        if (answerEntity == null) {
            throw new AnswerNotFoundException("ANS-001", "Entered answer uuid does not exist");
        }
        UserEntity userEntity = userAuthEntity.getUser();
        if (!answerEntity.getUser().getUuid().equals(userEntity.getUuid())) {
            throw new AuthorizationFailedException("ATHR-003", "Only the answer owner can edit the answer");
        }
        answerEntity.setAns(content);
        AnswerEntity updatedAnswerEntity = answerDAO.editAnswer(answerEntity);
        return updatedAnswerEntity;
    }
}
