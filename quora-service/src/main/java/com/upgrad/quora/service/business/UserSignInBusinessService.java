package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDAO;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service
public class UserSignInBusinessService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private PasswordCryptographyProvider passwordCryptographyProvider;


    public UserAuthEntity authenticateUser(final String username, final String password) throws AuthenticationFailedException {
        UserEntity userEntity = userDAO.getUserByUserName(username);
        if (userEntity == null) {
            throw new AuthenticationFailedException("ATH-001", "This username does not exist");
        }
        final String encryptedInputPassword = passwordCryptographyProvider.encrypt(password, userEntity.getSalt());
        if (encryptedInputPassword.equals(userEntity.getPassword())) {
            JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedInputPassword);
            UserAuthEntity userAuthToken = new UserAuthEntity();
            userAuthToken.setUuid(UUID.randomUUID().toString());
            userAuthToken.setUser(userEntity);
            final ZonedDateTime now = ZonedDateTime.now();
            final ZonedDateTime expiresAt = now.plusHours(8);
            userAuthToken.setAccessToken(jwtTokenProvider.generateToken(userEntity.getUuid(), now, expiresAt));
            userAuthToken.setLoginAt(now);
            userAuthToken.setExpiresAt(expiresAt);
            return userAuthToken;
        } else {
            throw new AuthenticationFailedException("ATH-002", "Password failed");
        }
    }
}
