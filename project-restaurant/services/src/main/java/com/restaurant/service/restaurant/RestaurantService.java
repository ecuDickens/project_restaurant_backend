package com.restaurant.service.restaurant;

import com.restaurant.entity.*;
import com.restaurant.exception.HttpException;

import java.util.Date;
import java.util.List;

public interface RestaurantService {

    List<Role> getRoles() throws HttpException;

    Long createUser(User user) throws HttpException;
    User updateUser(Long userId, User user) throws HttpException;
    User getUser(String userId) throws HttpException;
    User getUser(Long userId) throws HttpException;

    List<MenuItem> getMenuItems(Boolean getInactive) throws HttpException;
    MenuItem getMenuItem(Long menuItemId) throws HttpException;
    Long createMenuItem(MenuItem menuItem) throws HttpException;
    MenuItem updateMenuItem(Long menuItemId, MenuItem menuItem) throws HttpException;

    List<InventoryItem> getInventoryItems(Boolean getInactive) throws HttpException;
    InventoryItem getInventoryItem(String sku) throws HttpException;
    String createInventoryItem(InventoryItem inventoryItem) throws HttpException;
    InventoryItem updateInventoryItem(String sku, InventoryItem inventoryItem) throws HttpException;

    List<PurchaseOrder> getPurchaseOrders(Date startDate, Date endDate) throws HttpException;
    PurchaseOrder getPurchaseOrder(Long poId) throws HttpException;
    Long createPurchaseOrder(PurchaseOrder po) throws HttpException;

    List<Order> getOrders(Date startDate, Date endDate, Long userId) throws HttpException;
    Order getOrder(Long orderId) throws HttpException;
    Long createOrder(Order order) throws HttpException;
    Order refundOrder(Long orderNumber) throws HttpException;
    Order chargeOrder(Long orderNumber) throws HttpException;
}
