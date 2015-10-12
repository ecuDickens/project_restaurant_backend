package com.restaurant.web;

import com.google.inject.Inject;
import com.restaurant.base.ThrowingFunction1;
import com.restaurant.entity.Role;
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
import static com.restaurant.entity.enums.RoleValues.Role.CUSTOMER;
import static com.restaurant.entity.enums.RoleValues.Role.EMPLOYEE;
import static com.restaurant.helper.JpaHelper.SUBMITTER_HEADER;
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

    // This must be set in the submitter header for new customers to get created.
    private static String NEW_USER_ID = "new_customer_creation";

    @Inject
    public UserResource(final JpaHelper jpaHelper,
                        final EmailMatcher emailMatcher) {
        this.jpaHelper = jpaHelper;
        this.emailMatcher = emailMatcher;
    }

    @POST
    public Response createUser(@HeaderParam(SUBMITTER_HEADER) String submitterId, final User user) throws HttpException {

        // Name and valid email/password are required.
        if (isNullOrEmpty(user.getFirstName()) || isNullOrEmpty(user.getLastName())) {
            return buildResponse(BAD_REQUEST, new ErrorType("First and Last Name required."));
        } else if (isNullOrEmpty(user.getEmail()) || !emailMatcher.validate(user.getEmail()) || isNullOrEmpty(user.getPassword())) {
            return buildResponse(BAD_REQUEST, new ErrorType("Valid email address and password required."));
        }

        // Customers can only create themselves once, employees can only create customers, admins can create anybody.
        if (NEW_USER_ID.equals(submitterId)) {
            if ((null != user.getRole() && !CUSTOMER.getValue().equals(user.getRole().getRole())) || null != user.getWage() || null != user.getWeeklyHours()) {
                return buildResponse(BAD_REQUEST, new ErrorType("Invalid user information."));
            }
        } else {
            final User submitter = _getUser(submitterId);
            if (null == submitter || CUSTOMER.getValue().equals(submitter.getRole().getRole())) {
                return buildResponse(BAD_REQUEST, new ErrorType("Invalid submitter"));
            }
            if (EMPLOYEE.getValue().equals(submitter.getRole().getRole()) && null != user.getRole() && !CUSTOMER.getValue().equals(user.getRole().getRole())) {
                return buildResponse(BAD_REQUEST, new ErrorType("Employees can only create customers."));
            }
        }

        final Long userId = _createUser(user);
        if (null == userId) {
            return buildResponse(BAD_REQUEST, new ErrorType("Unable to create account"));
        }
        return buildResponse(OK, new User().withId(userId));
    }

    @GET
    @Path("/{user_id}")
    public Response getUser(@PathParam("user_id") final Long userId) throws HttpException {
        final User user = _getUser(userId);
        if (null == user) {
            return buildResponse(NO_CONTENT, new ErrorType("User not found"));
        }
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


    public Long _createUser(final User user) throws HttpException {
        return jpaHelper.executeJpaTransaction(new ThrowingFunction1<Long, EntityManager, HttpException>() {
            @Override
            public Long apply(EntityManager em) throws HttpException {
                user.setRole(em.getReference(Role.class, null == user.getRole() ? CUSTOMER.getValue() : user.getRole().getRole()));
                em.persist(user);
                em.flush();
                return user.getId();
            }
        });
    }

    public User _getUser(final String userId) throws HttpException {
        try {
            return _getUser(Long.valueOf(userId));
        } catch(NumberFormatException e) {
            return null;
        }
    }
    public User _getUser(final Long userId) throws HttpException {
        final User user = jpaHelper.executeJpa(new ThrowingFunction1<User, EntityManager, HttpException>() {
            @Override
            public User apply(EntityManager em) throws HttpException {
                return em.find(User.class, userId);
            }
        });
        if (null != user) {
            user.clean();
        }
        return user;
    }
}
