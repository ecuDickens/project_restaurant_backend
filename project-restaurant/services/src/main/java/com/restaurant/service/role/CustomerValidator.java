package com.restaurant.service.role;

import com.restaurant.entity.Order;
import com.restaurant.entity.User;

import static com.restaurant.entity.enums.RoleValues.Role.CUSTOMER;

/**
 * A customer can only create/update their own user information and place their own orders.
 */
public class CustomerValidator implements RoleValidator {
    @Override
    public boolean canPlaceOrder(final User submitter, final Order order) {
        return submitter.getId().equals(order.getUser().getId());
    }

    @Override
    public boolean canManageOrder(final User submitter, final Order order) {
        return false;
    }

    @Override
    public boolean canManageUser(final User submitter, final User user) {
        return (null == submitter && user.getRole().getRole().equals(CUSTOMER.getValue())) ||
                (null != submitter && submitter.getEmail().equals(user.getEmail()));
    }

    @Override
    public boolean canViewMenu(final Boolean showInactive) {
        return null == showInactive || !showInactive;
    }

    @Override
    public boolean canManageMenu() {
        return false;
    }

    @Override
    public boolean canManageInventory() {
        return false;
    }
}
