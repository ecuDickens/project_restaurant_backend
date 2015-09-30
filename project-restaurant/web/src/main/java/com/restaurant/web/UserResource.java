package com.restaurant.web;

import com.google.inject.Inject;
import com.restaurant.base.ThrowingFunction1;
import com.restaurant.entity.Account;
import com.restaurant.entity.User;
import com.restaurant.exception.HttpException;
import com.restaurant.helper.JpaHelper;
import com.restaurant.matchers.EmailMatcher;
import com.restaurant.types.ErrorType;

import javax.persistence.EntityManager;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.restaurant.helper.JpaHelper.buildResponse;
import static javax.persistence.LockModeType.PESSIMISTIC_WRITE;
import static javax.ws.rs.core.Response.Status.*;

/**
 * Contains calls for creating, loading, and updating user and related child records.
 * Child calls are passed to the related child resource for processing.
 */
@Path("/users")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public class UserResource {

    private final JpaHelper jpaHelper;
    private final EmailMatcher emailMatcher;

    @Inject
    public UserResource(final JpaHelper jpaHelper,
                        final EmailMatcher emailMatcher) {
        this.jpaHelper = jpaHelper;
        this.emailMatcher = emailMatcher;
    }

    @POST
    public Response createUser(final User user) throws HttpException {

        if (isNullOrEmpty(user.getFirstName()) || isNullOrEmpty(user.getLastName())) {
            return buildResponse(BAD_REQUEST, new ErrorType("First and Last Name required"));
        } else if (isNullOrEmpty(user.getEmail()) || !emailMatcher.validate(user.getEmail()) | isNullOrEmpty(user.getPassword())) {
            return buildResponse(BAD_REQUEST, new ErrorType("Valid email address and password required"));
        }

        final Long accountId = jpaHelper.executeJpaTransaction(new ThrowingFunction1<Long, EntityManager, HttpException>() {
            @Override
            public Long apply(EntityManager em) throws HttpException {
                em.persist(user);
                em.flush();
                return user.getId();
            }
        });

        if (null == accountId) {
            return buildResponse(BAD_REQUEST, new ErrorType("Unable to create account"));
        }
        return buildResponse(OK, new Account().withId(accountId));
    }

    @GET
    @Path("/{user_id}")
    public Response getUser(@PathParam("user_id") final Long userId) throws HttpException {
        final User user = jpaHelper.executeJpa(new ThrowingFunction1<User, EntityManager, HttpException>() {
            @Override
            public User apply(EntityManager em) throws HttpException {
                return em.find(User.class, userId);
            }
        });
        if (null == user) {
            return buildResponse(NO_CONTENT, new ErrorType("User not found"));
        }
        user.clean();
        return buildResponse(OK, user);
    }

    @POST
    @Path("/{user_id}")
    public Response updateUser(@PathParam("user_id") final Long userId,
                               final User user) throws HttpException {
        jpaHelper.executeJpaTransaction(new ThrowingFunction1<User, EntityManager, HttpException>() {
            @Override
            public User apply(EntityManager em) throws HttpException {
                final User forUpdate = em.find(User.class, userId);
                em.refresh(forUpdate, PESSIMISTIC_WRITE);
                forUpdate
                        .withFirstName(isNullOrEmpty(user.getFirstName()) ? forUpdate.getFirstName() : user.getFirstName())
                        .withLastName(isNullOrEmpty(user.getLastName()) ? forUpdate.getLastName() : user.getLastName())
                        .withIsActive(null == user.getIsActive() ? forUpdate.getIsActive() : user.getIsActive())
                        .withLastName(isNullOrEmpty(user.getLastName()) ? forUpdate.getLastName() : user.getLastName());
                return forUpdate;
            }
        });
        return buildResponse(OK);
    }
}
