package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDAO;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDeleteBusinessService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private UserAuthenticationBusinessService userAuthenticationBusinessService;

    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity userDelete(final String userId, final String accessToken) throws AuthorizationFailedException, UserNotFoundException {
        UserAuthEntity userAuthEntity = userAuthenticationBusinessService.authenticateUser(accessToken, "User is signed out");
        UserEntity adminUser = userAuthEntity.getUser();
        if ("nonadmin".equals(adminUser.getRole())) {
            throw new AuthorizationFailedException("ATHR-003", "Unauthorized Access, Entered user is not an admin");
        }
        UserEntity deleteUserEntity = userDAO.getUser(userId);
        if (deleteUserEntity == null) {
            throw new UserNotFoundException("USR-001", "User with entered uuid to be deleted does not exist");
        }
        userDAO.deleterUser(deleteUserEntity);
        return deleteUserEntity;
    }
}
