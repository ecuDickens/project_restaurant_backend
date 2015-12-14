package com.restaurant.service.role;

import com.restaurant.entity.Order;
import com.restaurant.entity.User;

/**
 * Defines the actions that need validation depending on role.
 */
public interface RoleValidator {

    /**
     * Denotes whether the supplied submitter can create the supplied order within the given role context.
     *
     * @param submitter the action submitter.
     * @param order the order to place.
     * @return true if a valid action.
     */
    boolean canPlaceOrder(User submitter, Order order);

    /**
     * Denotes whether the supplied submitter can charge/refund the supplied order within the given role context.
     *
     * @param submitter the action submitter.
     * @param order the order to update.
     * @return true if a valid action.
     */
    boolean canManageOrder(User submitter, Order order);

    /**
     * Denotes whether the supplied submitter can create/update the supplied user within the given role context.
     *
     * @param submitter the action submitter.  Can be null if a new customer creating their account.
     * @param user the user to create.
     * @return true if a valid action.
     */
    boolean canManageUser(User submitter, User user);

    /**
     * Denotes whether a role can view menu items.
     * @param showInactive the flag denoting whether to load inactive menu items.
     * @return true if a valid action.
     */
    boolean canViewMenu(Boolean showInactive);

    /**
     * Denotes whether the role context can create/update menu items.
     *
     * @return true if a valid action.
     */
    boolean canManageMenu();

    /**
     * Denotes whether the role context can create/update inventory items and place purchase orders.
     *
     * @return true if a valid action.
     */
    boolean canManageInventory();
}
