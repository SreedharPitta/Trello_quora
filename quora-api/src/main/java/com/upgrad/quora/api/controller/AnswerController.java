package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.AnswerCreateBusinessService;
import com.upgrad.quora.service.business.AnswerDeleteBusinessService;
import com.upgrad.quora.service.business.AnswerEditBusinessService;
import com.upgrad.quora.service.business.AnswerFetchBusinessService;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/")
public class AnswerController {

    @Autowired
    private AnswerCreateBusinessService answerCreateBusinessService;

    @Autowired
    private AnswerEditBusinessService answerEditBusinessService;

    @Autowired
    private AnswerDeleteBusinessService answerDeleteBusinessService;

    @Autowired
    private AnswerFetchBusinessService answerFetchBusinessService;

    @RequestMapping(method = RequestMethod.POST, path = "/question/{questionId}/answer/create", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerResponse> createAnswer(@PathVariable("questionId") final String questionId, @RequestHeader("authorization") final String authorization, final AnswerRequest answerRequest) throws AuthorizationFailedException, InvalidQuestionException {
        AnswerEntity answerEntity = answerCreateBusinessService.createAnswer(questionId, authorization, answerRequest.getAnswer());
        AnswerResponse answerResponse = new AnswerResponse().id(answerEntity.getUuid()).status("ANSWER CREATED");
        return new ResponseEntity<AnswerResponse>(answerResponse, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/answer/edit/{answerId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerEditResponse> editAnswerContent(@PathVariable("answerId") final String answerId, @RequestHeader("authorization") final String authorization, final AnswerEditRequest answerEditRequest) throws AuthorizationFailedException, AnswerNotFoundException {
        AnswerEntity answerEntity = answerEditBusinessService.editAnswer(answerId, authorization, answerEditRequest.getContent());
        AnswerEditResponse answerEditResponse = new AnswerEditResponse().id(answerEntity.getUuid()).status("ANSWER EDITED");
        return new ResponseEntity<AnswerEditResponse>(answerEditResponse, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/answer/delete/{answerId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerDeleteResponse> deleteAnswer(@PathVariable("answerId") final String answerId, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, AnswerNotFoundException {
        AnswerEntity answerEntity = answerDeleteBusinessService.deleteAnswer(answerId, authorization);
        AnswerDeleteResponse answerDeleteResponse = new AnswerDeleteResponse().id(answerEntity.getUuid()).status("ANSWER DELETED");
        return new ResponseEntity<AnswerDeleteResponse>(answerDeleteResponse, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "answer/all/{questionId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<AnswerDetailsResponse>> getAllAnswersToQuestion(@PathVariable("questionId") final String questionId, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, InvalidQuestionException {

        Map<QuestionEntity, List<AnswerEntity>> questionAnswerEntities = answerFetchBusinessService.getQuestionAnswers(questionId, authorization);
        List<AnswerDetailsResponse> answerDetailsResponses = new ArrayList<AnswerDetailsResponse>();
        Map.Entry<QuestionEntity, List<AnswerEntity>> entry = questionAnswerEntities.entrySet().iterator().next();
        QuestionEntity questionEntity = entry.getKey();
        for (AnswerEntity answerEntity : entry.getValue()) {
            answerDetailsResponses.add(new AnswerDetailsResponse().id(answerEntity.getUuid()).questionContent(questionEntity.getContent()).answerContent(answerEntity.getAns()));
        }
        return new ResponseEntity<List<AnswerDetailsResponse>>(answerDetailsResponses, HttpStatus.OK);
    }
}
