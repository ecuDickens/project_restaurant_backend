package com.restaurant.web;

import com.google.inject.Inject;
import com.restaurant.entity.MenuItem;
import com.restaurant.exception.HttpException;
import com.restaurant.service.restaurant.RestaurantService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static com.restaurant.jpa.helper.JpaHelper.buildResponse;
import static javax.ws.rs.core.Response.Status.OK;

@Path("/menu")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public class MenuResource {

    final RestaurantService service;

    @Inject
    public MenuResource(final RestaurantService service) {
        this.service = service;
    }

    @GET
    public Response getMenu(@QueryParam("show_inactive") final Boolean showInactive) throws HttpException {
        return buildResponse(OK, service.getMenuItems(showInactive));
    }

    @GET
    @Path("/{menu_item_id}")
    public Response getMenuItem(@PathParam("menu_item_id") final Long menuItemId) throws HttpException {
        return buildResponse(OK, service.getMenuItem(menuItemId));
    }

    @POST
    public Response createMenuItem(final MenuItem menuItem) throws HttpException {
        return buildResponse(OK, service.createMenuItem(menuItem));
    }

    @POST
    @Path("/{menu_item_id}")
    public Response updateMenuItem(@PathParam("menu_item_id") final Long menuItemId,
                                   final MenuItem menuItem) throws HttpException {
        return buildResponse(OK, service.updateMenuItem(menuItemId, menuItem));
    }
}
