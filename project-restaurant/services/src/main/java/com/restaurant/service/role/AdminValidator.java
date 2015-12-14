package com.restaurant.service.role;

import com.restaurant.entity.Order;
import com.restaurant.entity.User;

/**
 * An admin role should be able to perform any action.
 */
public class AdminValidator implements RoleValidator{
    @Override
    public boolean canPlaceOrder(User submitter, Order order) {
        return true;
    }

    @Override
    public boolean canManageOrder(User submitter, Order order) {
        return true;
    }

    @Override
    public boolean canManageUser(User submitter, User user) {
        return true;
    }

    @Override
    public boolean canManageMenu() {
        return true;
    }

    @Override
    public boolean canManageInventory() {
        return true;
    }
}
