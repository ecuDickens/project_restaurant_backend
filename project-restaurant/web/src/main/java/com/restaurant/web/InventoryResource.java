package com.restaurant.web;

import com.google.inject.Inject;
import com.restaurant.entity.InventoryItem;
import com.restaurant.exception.HttpException;
import com.restaurant.service.restaurant.RestaurantService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static com.restaurant.jpa.helper.JpaHelper.buildResponse;
import static javax.ws.rs.core.Response.Status.OK;

@Path("/inventory")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public class InventoryResource {

    final RestaurantService service;

    @Inject
    public InventoryResource(RestaurantService service) {
        this.service = service;
    }

    @GET
    public Response getInventoryItems(@QueryParam("show_inactive") final Boolean showInactive) throws HttpException {
        return buildResponse(OK, service.getInventoryItems(showInactive));
    }

    @GET
    @Path("/{sku}")
    public Response getInventoryItem(@PathParam("sku") final String sku) throws HttpException {
        return buildResponse(OK, service.getInventoryItem(sku));
    }

    @POST
    public Response createInventoryItem(final InventoryItem inventoryItem) throws HttpException {
        return buildResponse(OK, service.createInventoryItem(inventoryItem));
    }

    @POST
    @Path("/{sku}")
    public Response updateInventoryItem(@PathParam("sku") final String sku,
                                   final InventoryItem inventoryItem) throws HttpException {
        return buildResponse(OK, service.updateInventoryItem(sku, inventoryItem));
    }
}
