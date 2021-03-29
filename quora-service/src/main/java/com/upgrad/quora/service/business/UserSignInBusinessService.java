package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDAO;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service
public class UserSignInBusinessService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private PasswordCryptographyProvider passwordCryptographyProvider;

    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuthEntity authenticateUser(final String username, final String password) throws AuthenticationFailedException {
        UserEntity userEntity = userDAO.getUserByUserName(username);
        if (userEntity == null) {
            throw new AuthenticationFailedException("ATH-001", "This username does not exist");
        }
        final String encryptedInputPassword = passwordCryptographyProvider.encrypt(password, userEntity.getSalt());
        if (!encryptedInputPassword.equals(userEntity.getPassword())) {
            throw new AuthenticationFailedException("ATH-002", "Password failed");
        }
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedInputPassword);
        UserAuthEntity userAuthEntity = new UserAuthEntity();
        userAuthEntity.setUuid(UUID.randomUUID().toString());
        userAuthEntity.setUser(userEntity);
        final ZonedDateTime now = ZonedDateTime.now();
        final ZonedDateTime expiresAt = now.plusHours(8);
        userAuthEntity.setAccessToken(jwtTokenProvider.generateToken(userEntity.getUuid(), now, expiresAt));
        userAuthEntity.setLoginAt(now);
        userAuthEntity.setExpiresAt(expiresAt);
        return userDAO.createUserAuth(userAuthEntity);
    }
}
