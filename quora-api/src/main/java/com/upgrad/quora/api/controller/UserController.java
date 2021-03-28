package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.SigninResponse;
import com.upgrad.quora.api.model.SignoutResponse;
import com.upgrad.quora.api.model.SignupUserRequest;
import com.upgrad.quora.api.model.SignupUserResponse;
import com.upgrad.quora.service.business.UserSignInBusinessService;
import com.upgrad.quora.service.business.UserSignOutBusinessService;
import com.upgrad.quora.service.business.UserSignUpBusinessService;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;

@RestController
@RequestMapping("/")
public class UserController {

    @Autowired
    private UserSignUpBusinessService userSignUpBusinessService;

    @Autowired
    private UserSignInBusinessService userSignInBusinessService;

    @Autowired
    private UserSignOutBusinessService userSignOutBusinessService;


    //New User signup end point
    @RequestMapping(method = RequestMethod.POST, path = "/user/signup", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignupUserResponse> signup(final SignupUserRequest signupUserRequest) throws SignUpRestrictedException {
        final UserEntity createdUserEntity = userSignUpBusinessService.signup(signupUserRequest.getFirstName(), signupUserRequest.getLastName(), signupUserRequest.getUserName(), signupUserRequest.getEmailAddress(), signupUserRequest.getPassword(), signupUserRequest.getCountry(), signupUserRequest.getAboutMe(), signupUserRequest.getDob(), signupUserRequest.getContactNumber());
        SignupUserResponse userResponse = new SignupUserResponse().id(createdUserEntity.getUuid()).status("USER SUCCESSFULLY REGISTERED");
        return new ResponseEntity<SignupUserResponse>(userResponse, HttpStatus.CREATED);
    }

    //User Sign In End Point
    @RequestMapping(method = RequestMethod.POST, path = "/user/signin", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SigninResponse> signin(@RequestHeader("authorization") final String authorizationToken) throws AuthenticationFailedException {
        byte[] decodedBytes = Base64.getDecoder().decode(authorizationToken.split("Basic ")[1]);
        String decodedString = new String(decodedBytes);
        String[] decodedArray = decodedString.split(":");
        UserAuthEntity userAuthEntity = userSignInBusinessService.authenticateUser(decodedArray[0], decodedArray[1]);
        UserEntity user = userAuthEntity.getUser();
        SigninResponse signinResponse = new SigninResponse().id(user.getUuid()).message("SIGNED IN SUCCESSFULLY");
        HttpHeaders headers = new HttpHeaders();
        headers.add("access-token", userAuthEntity.getAccessToken());
        return new ResponseEntity<SigninResponse>(signinResponse, headers, HttpStatus.OK);
    }

    //User Sign Out End Point
    @RequestMapping(method = RequestMethod.POST, path = "/user/signout", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignoutResponse> signout(@RequestHeader("authorization") final String authorization) throws SignOutRestrictedException {
        final UserEntity userEntity = userSignOutBusinessService.signOut(authorization);
        SignoutResponse signoutResponse = new SignoutResponse().id(userEntity.getUuid()).message("SIGNED OUT SUCCESSFULLY");
        return new ResponseEntity<SignoutResponse>(signoutResponse, HttpStatus.OK);
    }
}
