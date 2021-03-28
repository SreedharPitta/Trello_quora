package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDAO;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserProfileBusinessService {

    @Autowired
    private UserDAO userDAO;

    public UserEntity getUser(final String userId, final String accessToken) throws UserNotFoundException, AuthorizationFailedException {
        UserAuthEntity userAuthEntity = userDAO.getUserAuthToken(accessToken);
        if (userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");

        } else {
            //TODO Handle user logout case and double check the same
            if (userAuthEntity.getLogoutAt() != null) {
                throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get user details");
            } else {
                UserEntity userEntity = userAuthEntity.getUser();
                if (userId.equals(userEntity.getUuid())) {
                    return userEntity;
                } else {
                    throw new UserNotFoundException("USR-001", "User with entered uuid does not exist");
                }
            }
        }
    }

}
