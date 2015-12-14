package com.restaurant.web;

import com.google.inject.Inject;
import com.restaurant.entity.InventoryItem;
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
import static javax.ws.rs.core.Response.Status.*;

@Path("/inventory")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public class InventoryResource {

    private final RestaurantService service;
    private final RoleValidatorFactory validatorFactory;

    @Inject
    public InventoryResource(final RestaurantService service,
                             final RoleValidatorFactory validatorFactory) {
        this.service = service;
        this.validatorFactory = validatorFactory;
    }

    @GET
    public Response getInventoryItems(@HeaderParam(SUBMITTER_HEADER) final String submitterId,  @QueryParam("show_inactive") final Boolean showInactive) throws HttpException {
        final String error = canManageInventory(submitterId);
        if (null != error) {
            return buildResponse(BAD_REQUEST, new ErrorType(error));
        }

        final List<InventoryItem> inventoryItems = service.getInventoryItems(showInactive);
        if (null == inventoryItems) {
            return buildResponse(NO_CONTENT, new ErrorType("No items found."));
        }
        return buildResponse(OK, inventoryItems);
    }

    @GET
    @Path("/{sku}")
    public Response getInventoryItem(@HeaderParam(SUBMITTER_HEADER) final String submitterId,  @PathParam("sku") final String sku) throws HttpException {
        final String error = canManageInventory(submitterId);
        if (null != error) {
            return buildResponse(BAD_REQUEST, new ErrorType(error));
        }

        final InventoryItem inventoryItem = service.getInventoryItem(sku);
        if (null == inventoryItem) {
            return buildResponse(NO_CONTENT, new ErrorType("Item not found."));
        }
        return buildResponse(OK, inventoryItem);
    }

    @POST
    public Response createInventoryItem(@HeaderParam(SUBMITTER_HEADER) final String submitterId,  final InventoryItem inventoryItem) throws HttpException {
        final String error = canManageInventory(submitterId);
        if (null != error) {
            return buildResponse(BAD_REQUEST, new ErrorType(error));
        }

        if (isNullOrEmpty(inventoryItem.getSku()) || isNullOrEmpty(inventoryItem.getDescription())) {
            return buildResponse(BAD_REQUEST, new ErrorType("sku and description is required."));
        }

        final String sku = service.createInventoryItem(inventoryItem);
        if (null == sku) {
            return buildResponse(BAD_REQUEST, new ErrorType("Unable to create inventory item."));
        }
        return buildResponse(OK, new InventoryItem().withSku(sku));
    }

    @POST
    @Path("/{sku}")
    public Response updateInventoryItem(@HeaderParam(SUBMITTER_HEADER) final String submitterId,  @PathParam("sku") final String sku,
                                        final InventoryItem inventoryItem) throws HttpException {
        final String error = canManageInventory(submitterId);
        if (null != error) {
            return buildResponse(BAD_REQUEST, new ErrorType(error));
        }

        return buildResponse(OK, service.updateInventoryItem(sku, inventoryItem));
    }

    private String canManageInventory(final String submitterId) throws HttpException {
        if (isNullOrEmpty(submitterId)) {
            return "Submitter id required.";
        }

        final User submitter = service.getUser(submitterId);
        if (!validatorFactory.create(submitter).canManageInventory()) {
            return "Submitter cannot perform this action.";
        }
        return null;
    }
}
