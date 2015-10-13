package com.restaurant.web;

import com.google.inject.Inject;
import com.restaurant.entity.User;
import com.restaurant.exception.HttpException;
import com.restaurant.matchers.EmailMatcher;
import com.restaurant.service.restaurant.RestaurantService;
import com.restaurant.types.ErrorType;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.restaurant.entity.enums.RoleValues.Role.CUSTOMER;
import static com.restaurant.entity.enums.RoleValues.Role.EMPLOYEE;
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

    // This must be set in the submitter header for new customers to get created.
    private static String NEW_USER_ID = "new_customer_creation";

    @Inject
    public UserResource(final RestaurantService service,
                        final EmailMatcher emailMatcher) {
        this.service = service;
        this.emailMatcher = emailMatcher;
    }

    @POST
    public Response createUser(@HeaderParam(SUBMITTER_HEADER) String submitterId, final User user) throws HttpException {

        final String error = validateUser(submitterId, user);
        if (null != error) {
            return buildResponse(BAD_REQUEST, new ErrorType(error));
        }

        final Long userId = service.createUser(user);
        if (null == userId) {
            return buildResponse(BAD_REQUEST, new ErrorType("Unable to create user"));
        }
        return buildResponse(OK, new User().withId(userId));
    }
    private String validateUser(final String submitterId, final User user) throws HttpException {
        // Name and valid email/password are required.
        if (isNullOrEmpty(user.getFirstName()) || isNullOrEmpty(user.getLastName())) {
            return "First and Last Name required.";
        } else if (isNullOrEmpty(user.getEmail()) || !emailMatcher.validate(user.getEmail()) || isNullOrEmpty(user.getPassword())) {
            return "Valid email address and password required.";
        }

        // Customers can only create themselves once, employees can only create customers, admins can create anybody.
        if (NEW_USER_ID.equals(submitterId)) {
            if ((null != user.getRole() && !CUSTOMER.getValue().equals(user.getRole().getRole())) || null != user.getWage() || null != user.getWeeklyHours()) {
                return "Invalid user information.";
            }
        } else {
            final User submitter = service.getUser(submitterId);
            if (null == submitter || CUSTOMER.getValue().equals(submitter.getRole().getRole())) {
                return "Invalid submitter";
            }
            if (EMPLOYEE.getValue().equals(submitter.getRole().getRole()) && null != user.getRole() && !CUSTOMER.getValue().equals(user.getRole().getRole())) {
                return "Employees can only create customers.";
            }
        }
        return null;
    }


    @GET
    @Path("/{user_id}")
    public Response getUser(@PathParam("user_id") final Long userId) throws HttpException {
        final User user = service.getUser(userId);
        if (null == user) {
            return buildResponse(NO_CONTENT, new ErrorType("User not found"));
        }
        return buildResponse(OK, user);
    }

    @POST
    @Path("/{user_id}")
    public Response updateUser(@PathParam("user_id") final Long userId,
                               final User user) throws HttpException {
        service.updateUser(userId, user);
        return buildResponse(OK);
    }
}
