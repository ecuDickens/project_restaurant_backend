package com.restaurant.servlet;

import com.google.inject.AbstractModule;
import com.restaurant.web.*;

public class APIModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(UserResource.class);
        bind(RoleResource.class);
        bind(InventoryResource.class);
        bind(MenuResource.class);
        bind(OrderResource.class);
        bind(PurchaseOrderResource.class);
    }
}
