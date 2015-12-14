package com.restaurant.web;

import com.google.inject.Inject;
import com.restaurant.datetime.DateTimeUtils;
import com.restaurant.entity.Order;
import com.restaurant.exception.HttpException;
import com.restaurant.service.restaurant.RestaurantService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.restaurant.jpa.helper.JpaHelper.buildResponse;
import static javax.ws.rs.core.Response.Status.OK;

@Path("/orders")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public class OrderResource {

    final RestaurantService service;

    @Inject
    public OrderResource(RestaurantService service) {
        this.service = service;
    }

    @GET
    public Response getOrders(@QueryParam("start_date") final String startDate,
                              @QueryParam("end_date") final String endDate,
                              @QueryParam("user_id") final Long userId) throws HttpException {
        final Date parsedStartDate = !isNullOrEmpty(startDate) ? new Date(DateTimeUtils.parse(startDate).getMillis()) : null;
        final Date parsedEndDate = !isNullOrEmpty(endDate) ? new Date(DateTimeUtils.parse(endDate).getMillis()) : null;
        return buildResponse(OK, service.getOrders(parsedStartDate, parsedEndDate, userId));
    }

    @GET
    @Path("/{order_number}")
    public Response getOrder(@PathParam("order_number") final Long orderNumber) throws HttpException {
        return buildResponse(OK, service.getOrder(orderNumber));
    }

    @POST
    public Response placeOrder(final Order order) throws HttpException {
        return buildResponse(OK, service.createOrder(order));
    }

    @POST
    @Path("/{order_number}/refund")
    public Response refundOrder(@PathParam("order_number") final Long orderNumber, @QueryParam("refund_amount") final Integer refundAmount) throws HttpException {
        return buildResponse(OK, service.refundOrder(orderNumber, refundAmount));
    }

    @POST
    @Path("/{order_number}/charge")
    public Response chargeOrder(@PathParam("order_number") final Long orderNumber, @QueryParam("refund_amount") final Integer chargeAmount) throws HttpException {
        return buildResponse(OK, service.chargeOrder(orderNumber, chargeAmount));
    }
}
