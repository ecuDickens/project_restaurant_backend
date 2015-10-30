package com.restaurant.web;

import com.google.inject.Inject;
import com.restaurant.datetime.DateTimeUtils;
import com.restaurant.entity.PurchaseOrder;
import com.restaurant.exception.HttpException;
import com.restaurant.service.restaurant.RestaurantService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.restaurant.jpa.helper.JpaHelper.buildResponse;
import static javax.ws.rs.core.Response.Status.OK;

@Path("/purchase_orders")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public class PurchaseOrderResource {

    final RestaurantService service;

    @Inject
    public PurchaseOrderResource(RestaurantService service) {
        this.service = service;
    }

    @GET
    public Response getPurchaseOrders(@QueryParam("start_date") final String startDate,
                                      @QueryParam("end_date") final String endDate) throws HttpException {
        final Date parsedStartDate = !isNullOrEmpty(startDate) ? new Date(DateTimeUtils.parse(startDate).getMillis()) : null;
        final Date parsedEndDate = !isNullOrEmpty(endDate) ? new Date(DateTimeUtils.parse(endDate).getMillis()) : null;
        return buildResponse(OK, service.getPurchaseOrders(parsedStartDate, parsedEndDate));
    }

    @GET
    @Path("/{purchase_order_number}")
    public Response getPurchaseOrder(@PathParam("purchase_order_number") final Long poNumber) throws HttpException {
        return buildResponse(OK, service.getPurchaseOrder(poNumber));
    }

    @POST
    public Response placePurchaseOrder(final PurchaseOrder purchaseOrder) throws HttpException {
        return buildResponse(OK, service.createPurchaseOrder(purchaseOrder));
    }
}
