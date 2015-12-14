package com.restaurant.service.restaurant;

import com.restaurant.entity.*;
import com.restaurant.exception.HttpException;

import java.util.Date;
import java.util.List;

public interface RestaurantService {

    /**
     * Load all roles from the database.
     *
     * @return list of roles
     * @throws HttpException
     */
    List<Role> getRoles() throws HttpException;

    /**
     * Creates the supplied user in the database.
     *
     * @param user the user to save.
     * @return the user id.
     * @throws HttpException
     */
    Long createUser(User user) throws HttpException;

    /**
     * Updates the user. Loads the existing record with the given id and updates it with
     * the fields that have changed from the supplied record.
     *
     * @param userId the user id to udpate.
     * @param user the user record with fields to update.
     * @return the updated user record.
     * @throws HttpException
     */
    User updateUser(Long userId, User user) throws HttpException;

    /**
     * Loads a user from the database.
     *
     * @param userId the user id to load.
     * @return the loaded user record.
     * @throws HttpException
     */
    User getUser(String userId) throws HttpException;
    User getUser(Long userId) throws HttpException;

    /**
     * Loads menu items from the database.
     *
     * @param getInactive if true will load all menu items, if false will load only the active ones.
     * @return list of menu items.
     * @throws HttpException
     */
    List<MenuItem> getMenuItems(Boolean getInactive) throws HttpException;

    /**
     * Load a single menu item.
     *
     * @param menuItemId the menu item id to load.
     * @return the loaded menu item
     * @throws HttpException
     */
    MenuItem getMenuItem(Long menuItemId) throws HttpException;

    /**
     * Creates the supplied menu item in the database.
     *
     * @param menuItem the menu item to create.
     * @return the menu item id.
     * @throws HttpException
     */
    Long createMenuItem(MenuItem menuItem) throws HttpException;

    /**
     * Updates the supplied menu item in the database. Loads the existing record with the given id and updates it with
     * the fields that have changed from the supplied record.
     *
     * @param menuItemId the menu item id to update.
     * @param menuItem the menu item record with the updated fields.
     * @return the updated menu item.
     * @throws HttpException
     */
    MenuItem updateMenuItem(Long menuItemId, MenuItem menuItem) throws HttpException;

    /**
     * Loads inventory items from the database.
     *
     * @param getInactive if true will load all inventory items, if false will load only the active ones.
     * @return list of inventory items.
     * @throws HttpException
     */
    List<InventoryItem> getInventoryItems(Boolean getInactive) throws HttpException;

    /**
     * Load a single inventory item.
     *
     * @param sku the inventory item sku to load.
     * @return the loaded inventory item
     * @throws HttpException
     */
    InventoryItem getInventoryItem(String sku) throws HttpException;

    /**
     * Creates the supplied inventory item in the database.
     *
     * @param inventoryItem the inventory item to create.
     * @return the inventory item sku.
     * @throws HttpException
     */
    String createInventoryItem(InventoryItem inventoryItem) throws HttpException;

    /**
     * Updates the supplied inventory item in the database.  Loads the existing record with the given sku and updates it with
     * the fields that have changed from the supplied record.
     *
     * @param sku the inventory item sku to update.
     * @param inventoryItem the inventory item record with the fields to update.
     * @return the updated inventory item
     * @throws HttpException
     */
    InventoryItem updateInventoryItem(String sku, InventoryItem inventoryItem) throws HttpException;

    /**
     * Loads all purchase orders that fall within the supplied date range.
     *
     * @param startDate the start date for any orders (order date >= start date). Optional.
     * @param endDate the end date for any orders (order date <= end date). Optional.
     * @return the list of matching purchase orders.
     * @throws HttpException
     */
    List<PurchaseOrder> getPurchaseOrders(Date startDate, Date endDate) throws HttpException;

    /**
     * Loads the purchase order with the supplied id.
     *
     * @param poId the purchase order id to load.
     * @return the loaded purchase order.
     * @throws HttpException
     */
    PurchaseOrder getPurchaseOrder(Long poId) throws HttpException;

    /**
     * Creates the supplied purchase order in the backend.  The purchase order should have one or more purchase order items
     * which will also be persisted.  These po items will be associated with either a new or existing inventory item.  If the
     * inventory item doesn't exist it will be created, otherwise its quantity will be increased by the amount of the related po item.
     *
     * @param po the purchase order to create.
     * @return the purchase order id.
     * @throws HttpException
     */
    Long createPurchaseOrder(PurchaseOrder po) throws HttpException;

    /**
     * Loads all orders within the supplied date range and belonging to the supplied user id.
     *
     * @param startDate the start date for any orders (order date >= start date). Optional.
     * @param endDate the end date for any orders (order date <= end date). Optional.
     * @param userId the user id to load orders for. Optional.
     * @return the list of matching orders.
     * @throws HttpException
     */
    List<Order> getOrders(Date startDate, Date endDate, Long userId) throws HttpException;

    /**
     * Loads the order with the supplied order id.
     *
     * @param orderId the order id to load.
     * @return the loaded order.
     * @throws HttpException
     */
    Order getOrder(Long orderId) throws HttpException;

    /**
     * Creates the supplied order.  Will also create all supplied order items and will decrement any related inventory items by the order item quantity.
     *
     * @param order the order to create.
     * @return the order id.
     * @throws HttpException
     */
    Long createOrder(Order order) throws HttpException;

    /**
     * Creates a refund for the supplied order id up to the supplied amount.  Updates the order balance.
     *
     * @param orderNumber the order number to refund.
     * @param refundAmount the amount to refund.  If null will fully refund.
     * @return the updated order.
     * @throws HttpException
     */
    Order refundOrder(Long orderNumber, Integer refundAmount) throws HttpException;

    /**
     * Charge the order up to the supplied amount.  Updates the order balance.
     *
     * @param orderNumber the order number to charge.
     * @param paymentAmount the amount to pay.  If null will full charge.
     * @return the updated order.
     * @throws HttpException
     */
    Order chargeOrder(Long orderNumber, Integer paymentAmount) throws HttpException;
}
