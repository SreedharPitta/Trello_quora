package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class UserDAO {

    @PersistenceContext
    private EntityManager entityManager;

    //To create a User
    public UserEntity createUser(final UserEntity userEntity) {
        entityManager.persist(userEntity);
        return userEntity;
    }

    //To get User by uuid
    public UserEntity getUser(final String userUuid) {
        try {
            return entityManager.createNamedQuery("userByUuid", UserEntity.class).setParameter("uuid", userUuid)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    //To get User by Email
    public UserEntity getUserByEmail(final String email) {
        try {
            return entityManager.createNamedQuery("userByEmail", UserEntity.class).setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    //To get User By username
    public UserEntity getUserByUserName(final String username) {
        try {
            return entityManager.createNamedQuery("userByUserName", UserEntity.class).setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    //To Create New User Auth
    public UserAuthEntity createUserAuth(final UserAuthEntity userAuthEntity) {
        entityManager.persist(userAuthEntity);
        return userAuthEntity;
    }

    //To get User Auth by Access Token
    public UserAuthEntity getUserAuthToken(final String accessToken) {
        try {
            return entityManager.createNamedQuery("userAuthByAccessToken", UserAuthEntity.class).setParameter("accessToken", accessToken).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    //To Update User Auth
    public UserAuthEntity updateUserAuth(final UserAuthEntity userAuthEntity) {
        entityManager.merge(userAuthEntity);
        return userAuthEntity;
    }

    //To Delete User
    public void deleterUser(final UserEntity deleteUserEntity) {
        entityManager.remove(deleteUserEntity);
    }
}
