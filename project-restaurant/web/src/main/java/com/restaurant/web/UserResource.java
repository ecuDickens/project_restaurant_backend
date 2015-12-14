package com.restaurant.web;

import com.google.inject.Inject;
import com.restaurant.entity.User;
import com.restaurant.exception.HttpException;
import com.restaurant.matchers.EmailMatcher;
import com.restaurant.service.restaurant.RestaurantService;
import com.restaurant.service.role.RoleValidatorFactory;
import com.restaurant.types.ErrorType;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.restaurant.jpa.helper.JpaHelper.SUBMITTER_HEADER;
import static com.restaurant.jpa.helper.JpaHelper.buildResponse;
import static javax.ws.rs.core.Response.Status.*;

/**
 * Contains calls for creating, loading, and updating user and related records.
 */
@Path("/users")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public class UserResource {

    private final RestaurantService service;
    private final EmailMatcher emailMatcher;
    private final RoleValidatorFactory validatorFactory;

    @Inject
    public UserResource(final RestaurantService service,
                        final EmailMatcher emailMatcher,
                        final RoleValidatorFactory validatorFactory) {
        this.service = service;
        this.emailMatcher = emailMatcher;
        this.validatorFactory = validatorFactory;
    }

    @POST
    public Response createUser(@HeaderParam(SUBMITTER_HEADER) final String submitterId,  final User user) throws HttpException {
        final String error = canCreate(submitterId, user);
        if (null != error) {
            return buildResponse(BAD_REQUEST, new ErrorType(error));
        }

        final Long userId = service.createUser(user);
        if (null == userId) {
            return buildResponse(BAD_REQUEST, new ErrorType("Unable to create user"));
        }
        return buildResponse(OK, new User().withId(userId));
    }
    private String canCreate(final String submitterId, final User user) throws HttpException {
        if (isNullOrEmpty(user.getFirstName()) || isNullOrEmpty(user.getLastName())) {
            return "First and Last Name required.";
        } else if (isNullOrEmpty(user.getEmail()) || !emailMatcher.validate(user.getEmail()) || isNullOrEmpty(user.getPassword())) {
            return "Valid email address and password required.";
        }
        return canManageUser(submitterId, user);
    }

    @GET
    @Path("/{user_id}")
    public Response getUser(@HeaderParam(SUBMITTER_HEADER) final String submitterId, @PathParam("user_id") final Long userId) throws HttpException {
        final User user = service.getUser(userId);
        if (null == user) {
            return buildResponse(NO_CONTENT, new ErrorType("User not found"));
        }

        final String error = canManageUser(submitterId, user);
        if (null != error) {
            return buildResponse(BAD_REQUEST, new ErrorType(error));
        }
        return buildResponse(OK, user);
    }

    @POST
    @Path("/{user_id}")
    public Response updateUser(@HeaderParam(SUBMITTER_HEADER) final String submitterId,
                               @PathParam("user_id") final Long userId,
                               final User user) throws HttpException {
        final String error = canManageUser(submitterId, service.getUser(userId));
        if (null != error) {
            return buildResponse(BAD_REQUEST, new ErrorType(error));
        }
        final User updated = service.updateUser(userId, user);
        return buildResponse(OK, updated);
    }

    private String canManageUser(final String submitterId, final User user) throws HttpException {
        if (isNullOrEmpty(submitterId)) {
            return "Submitter id required.";
        }

        final User submitter = service.getUser(submitterId);
        if (!validatorFactory.create(submitter).canManageUser(submitter, user)) {
            return "Submitter cannot perform this action.";
        }
        return null;
    }
}
