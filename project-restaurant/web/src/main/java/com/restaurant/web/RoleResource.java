package com.restaurant.web;

import com.google.inject.Inject;
import com.restaurant.exception.HttpException;
import com.restaurant.service.restaurant.RestaurantService;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static com.restaurant.jpa.helper.JpaHelper.buildResponse;
import static javax.ws.rs.core.Response.Status.OK;

@Path("/roles")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public class RoleResource {

    private final RestaurantService service;

    @Inject
    public RoleResource(final RestaurantService service) {
        this.service = service;
    }

    @GET
    public Response getRoles() throws HttpException {
        return buildResponse(OK, service.getRoles());
    }
}
