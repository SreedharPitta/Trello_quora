package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDAO;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Service
public class UserSignOutBusinessService {

    @Autowired
    private UserDAO userDAO;

    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity signOut(final String accessToken) throws SignOutRestrictedException {
        UserAuthEntity userAuthEntity = userDAO.getUserAuthToken(accessToken);
        if (userAuthEntity == null) {
            throw new SignOutRestrictedException("SGR-001", "User is not Signed in");
        } else {
            final ZonedDateTime now = ZonedDateTime.now();
            userAuthEntity.setExpiresAt(now);
            userAuthEntity.setLogoutAt(now);
            UserAuthEntity updatedUserAuthEntity = userDAO.updateUserAuth(userAuthEntity);
            return updatedUserAuthEntity.getUser();
        }
    }
}
