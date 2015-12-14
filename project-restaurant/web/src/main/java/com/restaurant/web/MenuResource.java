package com.restaurant.web;

import com.google.inject.Inject;
import com.restaurant.entity.MenuItem;
import com.restaurant.entity.User;
import com.restaurant.exception.HttpException;
import com.restaurant.service.restaurant.RestaurantService;
import com.restaurant.service.role.RoleValidatorFactory;
import com.restaurant.types.ErrorType;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.List;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.restaurant.jpa.helper.JpaHelper.SUBMITTER_HEADER;
import static com.restaurant.jpa.helper.JpaHelper.buildResponse;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static javax.ws.rs.core.Response.Status.OK;

@Path("/menu")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public class MenuResource {

    private final RestaurantService service;
    private final RoleValidatorFactory validatorFactory;

    @Inject
    public MenuResource(final RestaurantService service, final RoleValidatorFactory validatorFactory) {
        this.service = service;
        this.validatorFactory = validatorFactory;
    }

    @GET
    public Response getMenu(@HeaderParam(SUBMITTER_HEADER) final String submitterId, @QueryParam("show_inactive") final Boolean showInactive) throws HttpException {
        final String error = canViewMenu(submitterId, showInactive);
        if (null != error) {
            return buildResponse(BAD_REQUEST, new ErrorType(error));
        }

        final List<MenuItem> menuItems = service.getMenuItems(showInactive);
        if (null == menuItems) {
            return buildResponse(NO_CONTENT, new ErrorType("No items found."));
        }
        return buildResponse(OK, menuItems);
    }

    @GET
    @Path("/{menu_item_id}")
    public Response getMenuItem(@HeaderParam(SUBMITTER_HEADER) final String submitterId,  @PathParam("menu_item_id") final Long menuItemId) throws HttpException {
        final String error = canManageMenu(submitterId);
        if (null != error) {
            return buildResponse(BAD_REQUEST, new ErrorType(error));
        }

        final MenuItem menuItem = service.getMenuItem(menuItemId);
        if (null == menuItem) {
            return buildResponse(NO_CONTENT, new ErrorType("Item not found."));
        }
        return buildResponse(OK, menuItem);
    }

    @POST
    public Response createMenuItem(@HeaderParam(SUBMITTER_HEADER) final String submitterId,  final MenuItem menuItem) throws HttpException {
        final String error = canManageMenu(submitterId);
        if (null != error) {
            return buildResponse(BAD_REQUEST, new ErrorType(error));
        }

        if (isNullOrEmpty(menuItem.getDescription()) || null == menuItem.getType() || isNullOrEmpty(menuItem.getName()) || null == menuItem.getPrice()) {
            return buildResponse(BAD_REQUEST, new ErrorType("descrition, type, name, and price is required."));
        }

        final Long menuItemId = service.createMenuItem(menuItem);
        if (null == menuItemId) {
            return buildResponse(BAD_REQUEST, new ErrorType("Unable to create menu item."));
        }
        return buildResponse(OK, new MenuItem().withId(menuItemId));
    }

    @POST
    @Path("/{menu_item_id}")
    public Response updateMenuItem(@HeaderParam(SUBMITTER_HEADER) final String submitterId,
                                   @PathParam("menu_item_id") final Long menuItemId,
                                   final MenuItem menuItem) throws HttpException {
        final String error = canManageMenu(submitterId);
        if (null != error) {
            return buildResponse(BAD_REQUEST, new ErrorType(error));
        }

        return buildResponse(OK, service.updateMenuItem(menuItemId, menuItem));
    }

    private String canViewMenu(final String submitterId, final Boolean showInactive) throws HttpException {
        if (isNullOrEmpty(submitterId)) {
            return "Submitter id required.";
        }

        final User submitter = service.getUser(submitterId);
        if (!validatorFactory.create(submitter).canViewMenu(showInactive)) {
            return "Submitter cannot perform this action.";
        }
        return null;
    }
    private String canManageMenu(final String submitterId) throws HttpException {
        if (isNullOrEmpty(submitterId)) {
            return "Submitter id required.";
        }

        final User submitter = service.getUser(submitterId);
        if (!validatorFactory.create(submitter).canManageMenu()) {
            return "Submitter cannot perform this action.";
        }
        return null;
    }
}
