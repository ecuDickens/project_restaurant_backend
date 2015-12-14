package com.restaurant.web;

import com.google.inject.Inject;
import com.restaurant.datetime.DateTimeUtils;
import com.restaurant.entity.Order;
import com.restaurant.entity.OrderItem;
import com.restaurant.entity.User;
import com.restaurant.exception.HttpException;
import com.restaurant.service.restaurant.RestaurantService;
import com.restaurant.service.role.RoleValidatorFactory;
import com.restaurant.types.ErrorType;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.restaurant.jpa.helper.JpaHelper.SUBMITTER_HEADER;
import static com.restaurant.jpa.helper.JpaHelper.buildResponse;
import static javax.ws.rs.core.Response.Status.*;

@Path("/orders")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public class OrderResource {

    private final RestaurantService service;
    private final RoleValidatorFactory validatorFactory;

    @Inject
    public OrderResource(final RestaurantService service,
                         final RoleValidatorFactory validatorFactory) {
        this.service = service;
        this.validatorFactory = validatorFactory;
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
    public Response getOrder(@HeaderParam(SUBMITTER_HEADER) final String submitterId, @PathParam("order_number") final Long orderNumber) throws HttpException {
        final Order order = service.getOrder(orderNumber);
        if (null == order) {
            return buildResponse(NO_CONTENT, new ErrorType("Order not found."));
        }

        final String error = canPlaceOrder(submitterId, order);
        if (null != error) {
            return buildResponse(BAD_REQUEST, new ErrorType(error));
        }
        return buildResponse(OK, order);
    }

    @POST
    public Response placeOrder(@HeaderParam(SUBMITTER_HEADER) final String submitterId, final Order order) throws HttpException {
        final String error = canPlaceOrder(submitterId, order);
        if (null != error) {
            return buildResponse(BAD_REQUEST, new ErrorType(error));
        }

        if (null == order.getType() || null == order.getUser() || null == order.getOrderItems()) {
            return buildResponse(BAD_REQUEST, new ErrorType("type, user, and order items required."));
        }
        for (OrderItem orderItem : order.getOrderItems()) {
            if (null == orderItem.getUnitPrice()) {
                return buildResponse(BAD_REQUEST, new ErrorType("order item price required."));
            }
        }

        final Long orderNumber = service.createOrder(order);
        if (null == orderNumber) {
            return buildResponse(BAD_REQUEST, new ErrorType("Unable to create order."));
        }
        return buildResponse(OK, new Order().withOrderNumber(orderNumber));
    }

    @POST
    @Path("/{order_number}/refund")
    public Response refundOrder(@HeaderParam(SUBMITTER_HEADER) final String submitterId,
                                @PathParam("order_number") final Long orderNumber,
                                @QueryParam("refund_amount") final Integer refundAmount) throws HttpException {
        final Order order = service.getOrder(orderNumber);
        if (null == order) {
            return buildResponse(NO_CONTENT, new ErrorType("Order not found."));
        }

        final String error = canManageOrder(submitterId, order);
        if (null != error) {
            return buildResponse(BAD_REQUEST, new ErrorType(error));
        }

        return buildResponse(OK, service.refundOrder(orderNumber, refundAmount));
    }

    @POST
    @Path("/{order_number}/charge")
    public Response chargeOrder(@HeaderParam(SUBMITTER_HEADER) final String submitterId,
                                @PathParam("order_number") final Long orderNumber,
                                @QueryParam("charge_amount") final Integer chargeAmount) throws HttpException {
        final Order order = service.getOrder(orderNumber);
        if (null == order) {
            return buildResponse(NO_CONTENT, new ErrorType("Order not found."));
        }

        final String error = canManageOrder(submitterId, order);
        if (null != error) {
            return buildResponse(BAD_REQUEST, new ErrorType(error));
        }

        return buildResponse(OK, service.chargeOrder(orderNumber, chargeAmount));
    }

    private String canPlaceOrder(final String submitterId, final Order order) throws HttpException {
        if (isNullOrEmpty(submitterId)) {
            return "Submitter id required.";
        }

        final User submitter = service.getUser(submitterId);
        if (!validatorFactory.create(submitter).canPlaceOrder(submitter, order)) {
            return "Submitter cannot perform this action.";
        }
        return null;
    }

    private String canManageOrder(final String submitterId, final Order order) throws HttpException {
        if (isNullOrEmpty(submitterId)) {
            return "Submitter id required.";
        }

        final User submitter = service.getUser(submitterId);
        if (!validatorFactory.create(submitter).canManageOrder(submitter, order)) {
            return "Submitter cannot perform this action.";
        }
        return null;
    }
}
