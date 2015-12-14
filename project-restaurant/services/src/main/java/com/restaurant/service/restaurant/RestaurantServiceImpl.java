package com.restaurant.service.restaurant;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.restaurant.base.ThrowingFunction1;
import com.restaurant.entity.*;
import com.restaurant.exception.HttpException;
import com.restaurant.jpa.helper.JpaHelper;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.restaurant.entity.enums.RoleValues.Role.CUSTOMER;
import static javax.persistence.LockModeType.PESSIMISTIC_WRITE;

@Singleton
public class RestaurantServiceImpl implements RestaurantService {

    private final JpaHelper helper;

    @Inject
    public RestaurantServiceImpl(JpaHelper helper) {
        this.helper = helper;
    }

    @Override
    public List<Role> getRoles() throws HttpException {
        return helper.executeJpa(new ThrowingFunction1<List<Role>, EntityManager, HttpException>() {
            @Override
            public List<Role> apply(EntityManager em) throws HttpException {
                Query query = em.createQuery("SELECT e FROM Role e");
                return (List<Role>) query.getResultList();
            }
        });
    }

    @Override
    public Long createUser(final User user) throws HttpException {
        return helper.executeJpaTransaction(new ThrowingFunction1<Long, EntityManager, HttpException>() {
            @Override
            public Long apply(EntityManager em) throws HttpException {
                user.setRole(em.getReference(Role.class, null == user.getRole() ? CUSTOMER.getValue() : user.getRole().getRole()));
                em.persist(user);
                em.flush();
                return user.getId();
            }
        });
    }

    @Override
    public User updateUser(final Long userId, final User user) throws HttpException {
        return helper.executeJpaTransaction(new ThrowingFunction1<User, EntityManager, HttpException>() {
            @Override
            public User apply(EntityManager em) throws HttpException {
                final User forUpdate = em.find(User.class, userId);
                em.refresh(forUpdate, PESSIMISTIC_WRITE);
                forUpdate
                        .withFirstName(isNullOrEmpty(user.getFirstName()) ? forUpdate.getFirstName() : user.getFirstName())
                        .withLastName(isNullOrEmpty(user.getLastName()) ? forUpdate.getLastName() : user.getLastName())
                        .withEmail(isNullOrEmpty(user.getEmail()) ? forUpdate.getEmail() : user.getEmail())
                        .withPassword(isNullOrEmpty(user.getPassword()) ? forUpdate.getPassword() : user.getPassword())
                        .withRole(em.getReference(Role.class, null == user.getRole() ? forUpdate.getRole().getRole() : user.getRole().getRole()))
                        .withWage(null == user.getWage() ? forUpdate.getWage() : user.getWage())
                        .withWeeklyHours(null == user.getWeeklyHours() ? forUpdate.getWeeklyHours() : user.getWeeklyHours())
                        .withIsActive(null == user.getIsActive() ? forUpdate.getIsActive() : user.getIsActive());
                return forUpdate;
            }
        });
    }

    @Override
    public User getUser(final String userId) throws HttpException {
        try {
            return getUser(Long.valueOf(userId));
        } catch(NumberFormatException e) {
            return null;
        }
    }

    @Override
    public User getUser(final Long userId) throws HttpException {
        final User user = helper.executeJpa(new ThrowingFunction1<User, EntityManager, HttpException>() {
            @Override
            public User apply(EntityManager em) throws HttpException {
                return em.find(User.class, userId);
            }
        });
        if (null != user) {
            user.clean();
        }
        return user;
    }

    @Override
    public List<MenuItem> getMenuItems(final Boolean getInactive) throws HttpException {
        return helper.executeJpa(new ThrowingFunction1<List<MenuItem>, EntityManager, HttpException>() {
            @Override
            public List<MenuItem> apply(EntityManager em) throws HttpException {
                final String query = String.format("SELECT e FROM MenuItem e%s", null == getInactive || !getInactive ? " WHERE e.isActive = true" : "");
                return (List<MenuItem>) em.createQuery(query).getResultList();
            }
        });
    }

    @Override
    public MenuItem getMenuItem(final Long menuItemId) throws HttpException {
        final MenuItem menuItem = helper.executeJpa(new ThrowingFunction1<MenuItem, EntityManager, HttpException>() {
            @Override
            public MenuItem apply(EntityManager em) throws HttpException {
                return em.find(MenuItem.class, menuItemId);
            }
        });
        if (null != menuItem) {
            menuItem.clean();
        }
        return menuItem;
    }

    @Override
    public Long createMenuItem(final MenuItem menuItem) throws HttpException {
        return helper.executeJpaTransaction(new ThrowingFunction1<Long, EntityManager, HttpException>() {
            @Override
            public Long apply(EntityManager em) throws HttpException {
                em.persist(menuItem);
                for (MenuInventoryItem menuInventoryItem : menuItem.getMenuInventoryItems()) {
                    em.persist(menuInventoryItem);
                }
                em.flush();
                return menuItem.getId();
            }
        });
    }

    @Override
    public MenuItem updateMenuItem(final Long menuItemId, final MenuItem menuItem) throws HttpException {
        return helper.executeJpaTransaction(new ThrowingFunction1<MenuItem, EntityManager, HttpException>() {
            @Override
            public MenuItem apply(EntityManager em) throws HttpException {
                final MenuItem forUpdate = em.find(MenuItem.class, menuItemId);
                em.refresh(forUpdate, PESSIMISTIC_WRITE);
                forUpdate
                        .withIsActive(null == menuItem.getIsActive() ? forUpdate.getIsActive() : menuItem.getIsActive())
                        .withName(isNullOrEmpty(menuItem.getName()) ? forUpdate.getName() : menuItem.getName())
                        .withDescription(isNullOrEmpty(menuItem.getDescription()) ? forUpdate.getDescription() : menuItem.getDescription())
                        .withPrice(null == menuItem.getPrice() ? forUpdate.getPrice() : menuItem.getPrice())
                        .withType(null == menuItem.getType() ? forUpdate.getType() : menuItem.getType());
                for (MenuInventoryItem oldMII : forUpdate.getMenuInventoryItems()) {
                    em.remove(oldMII);
                }
                for (MenuInventoryItem newMII : menuItem.getMenuInventoryItems()) {
                    em.persist(newMII);
                }
                return forUpdate;
            }
        });
    }

    @Override
    public List<InventoryItem> getInventoryItems(final Boolean getInactive) throws HttpException {
        return helper.executeJpa(new ThrowingFunction1<List<InventoryItem>, EntityManager, HttpException>() {
            @Override
            public List<InventoryItem> apply(EntityManager em) throws HttpException {
                final String query = String.format("SELECT e FROM InventoryItem e%s", null == getInactive || !getInactive ? " WHERE e.isActive = true" : "");
                return (List<InventoryItem>) em.createQuery(query).getResultList();
            }
        });
    }

    @Override
    public InventoryItem getInventoryItem(final String sku) throws HttpException {
        final InventoryItem inventoryItem = helper.executeJpa(new ThrowingFunction1<InventoryItem, EntityManager, HttpException>() {
            @Override
            public InventoryItem apply(EntityManager em) throws HttpException {
                return em.find(InventoryItem.class, sku);
            }
        });
        if (null != inventoryItem) {
            inventoryItem.clean();
        }
        return inventoryItem;
    }

    @Override
    public String createInventoryItem(final InventoryItem inventoryItem) throws HttpException {
        return helper.executeJpaTransaction(new ThrowingFunction1<String, EntityManager, HttpException>() {
            @Override
            public String apply(EntityManager em) throws HttpException {
                em.persist(inventoryItem);
                em.flush();
                return inventoryItem.getSku();
            }
        });
    }

    @Override
    public InventoryItem updateInventoryItem(final String sku, final InventoryItem inventoryItem) throws HttpException {
        return helper.executeJpaTransaction(new ThrowingFunction1<InventoryItem, EntityManager, HttpException>() {
            @Override
            public InventoryItem apply(EntityManager em) throws HttpException {
                final InventoryItem forUpdate = em.find(InventoryItem.class, sku);
                em.refresh(forUpdate, PESSIMISTIC_WRITE);
                forUpdate
                        .withIsActive(null == inventoryItem.getIsActive() ? forUpdate.getIsActive() : inventoryItem.getIsActive())
                        .withName(isNullOrEmpty(inventoryItem.getName()) ? forUpdate.getName() : inventoryItem.getName())
                        .withDescription(isNullOrEmpty(inventoryItem.getDescription()) ? forUpdate.getDescription() : inventoryItem.getDescription())
                        .withType(null == inventoryItem.getType() ? forUpdate.getType() : inventoryItem.getType());
                return forUpdate;
            }
        });
    }

    @Override
    public List<PurchaseOrder> getPurchaseOrders(final Date startDate, final Date endDate) throws HttpException {
        final List<PurchaseOrder> pos = helper.executeJpa(new ThrowingFunction1<List<PurchaseOrder>, EntityManager, HttpException>() {
            @Override
            public List<PurchaseOrder> apply(EntityManager em) throws HttpException {
                return (List<PurchaseOrder>) em.createQuery("SELECT e FROM PurchaseOrder e").getResultList();
            }
        });

        final List<PurchaseOrder> filtered = Lists.newArrayList();
        for (PurchaseOrder po : pos) {
            if ((null == startDate || po.getCreatedDate().compareTo(startDate) >= 0) &&
                (null == endDate || po.getCreatedDate().compareTo(endDate) <= 0)) {
                filtered.add(po);
            }
        }
        return filtered;
    }

    @Override
    public PurchaseOrder getPurchaseOrder(final Long purchaseOrderId) throws HttpException {
        final PurchaseOrder purchaseOrder = helper.executeJpa(new ThrowingFunction1<PurchaseOrder, EntityManager, HttpException>() {
            @Override
            public PurchaseOrder apply(EntityManager em) throws HttpException {
                return em.find(PurchaseOrder.class, purchaseOrderId);
            }
        });
        if (null != purchaseOrder) {
            purchaseOrder.clean();
        }
        return purchaseOrder;
    }

    @Override
    public Long createPurchaseOrder(final PurchaseOrder po) throws HttpException {
        return helper.executeJpaTransaction(new ThrowingFunction1<Long, EntityManager, HttpException>() {
            @Override
            public Long apply(EntityManager em) throws HttpException {
                em.persist(po);
                for (PurchaseOrderItem poItem : po.getPurchaseOrderItems()) {
                    em.persist(poItem);
                    final InventoryItem inventoryItem = em.find(InventoryItem.class, poItem.getInventoryItem().getSku());
                    if (null == inventoryItem) {
                        em.persist(poItem.getInventoryItem().withQuantity(poItem.getQuantity()));
                    } else {
                        em.refresh(inventoryItem, PESSIMISTIC_WRITE);
                        inventoryItem.setQuantity(inventoryItem.getQuantity() + poItem.getQuantity());
                    }

                }
                em.flush();
                return po.getId();
            }
        });
    }

    @Override
    public List<Order> getOrders(final Date startDate, final Date endDate, final Long userId) throws HttpException {
        final List<Order> orders = helper.executeJpa(new ThrowingFunction1<List<Order>, EntityManager, HttpException>() {
            @Override
            public List<Order> apply(EntityManager em) throws HttpException {
                final String query = String.format("SELECT e FROM Order e%s", null != userId ? " WHERE e.order_user_id = " +userId : "");
                return (List<Order>) em.createQuery(query).getResultList();
            }
        });

        final List<Order> filtered = Lists.newArrayList();
        for (Order order : orders) {
            if ((null == startDate || order.getCreatedDate().compareTo(startDate) >= 0) &&
                (null == endDate || order.getCreatedDate().compareTo(endDate) <= 0)) {
                filtered.add(order);
            }
        }
        return filtered;
    }

    @Override
    public Order getOrder(final Long orderId) throws HttpException {
        final Order order = helper.executeJpa(new ThrowingFunction1<Order, EntityManager, HttpException>() {
            @Override
            public Order apply(EntityManager em) throws HttpException {
                return em.find(Order.class, orderId);
            }
        });
        if (null != order) {
            order.clean();
        }
        return order;
    }

    @Override
    public Long createOrder(final Order order) throws HttpException {
        return helper.executeJpaTransaction(new ThrowingFunction1<Long, EntityManager, HttpException>() {
            @Override
            public Long apply(EntityManager em) throws HttpException {
                em.persist(order);
                for (OrderItem orderItem : order.getOrderItems()) {
                    em.persist(orderItem);
                    final MenuItem menuItem = em.find(MenuItem.class, orderItem.getMenuItem().getId());
                    for (MenuInventoryItem menuInventoryItem : menuItem.getMenuInventoryItems()) {
                        final InventoryItem inventoryItem = menuInventoryItem.getInventoryItem();
                        em.refresh(inventoryItem, PESSIMISTIC_WRITE);
                        inventoryItem.setQuantity(inventoryItem.getQuantity() - (orderItem.getQuantity() * menuInventoryItem.getQuantity()));
                    }

                }
                em.flush();
                return order.getOrderNumber();
            }
        });
    }

    @Override
    public Order refundOrder(final Long orderNumber, final Integer refundAmount) throws HttpException {
        return null;
    }

    @Override
    public Order chargeOrder(final Long orderNumber, final Integer paymentAmount) throws HttpException {
        return null;
    }
}
