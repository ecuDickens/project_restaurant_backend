package com.restaurant.service.role;

import com.restaurant.entity.Order;
import com.restaurant.entity.User;

import static com.restaurant.entity.enums.RoleValues.Role.CUSTOMER;

/**
 * Employees can create/update their own and customer information, place their own or customer orders and manage (charge/refund) any order.
 */
public class EmployeeValidator implements RoleValidator {
    @Override
    public boolean canPlaceOrder(final User submitter, final Order order) {
        return submitter.getId().equals(order.getUser().getId()) || order.getUser().getRole().getRole().equals(CUSTOMER.getValue());
    }

    @Override
    public boolean canManageOrder(User submitter, Order order) {
        return true;
    }

    @Override
    public boolean canManageUser(User submitter, User user) {
        return null != submitter && (submitter.getId().equals(user.getId()) || user.getRole().getRole().equals(CUSTOMER.getValue()));
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
