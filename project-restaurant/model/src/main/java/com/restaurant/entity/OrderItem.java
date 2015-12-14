package com.restaurant.entity;

import com.restaurant.datetime.TimestampDeserializer;
import com.restaurant.datetime.TimestampSerializer;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.sql.Timestamp;

import static org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion.NON_NULL;

@Entity
@Table(name = "order_items")
@JsonSerialize(include = NON_NULL)
public class OrderItem {

    @Id
    @GeneratedValue
    @Column(name = "oi_id")
    private Long id;

    @Column(name = "oi_created_date", nullable = false)
    @JsonSerialize(using=TimestampSerializer.class, include = NON_NULL)
    @JsonDeserialize(using=TimestampDeserializer.class)
    private Timestamp createdDate;

    @Column(name = "oi_activity_date", nullable = false)
    @JsonSerialize(using=TimestampSerializer.class, include = NON_NULL)
    @JsonDeserialize(using=TimestampDeserializer.class)
    private Timestamp activityDate;

    @ManyToOne(optional=false)
    @JoinColumn(name="oi_order_number", referencedColumnName="order_number")
    private Order order;

    @ManyToOne(optional=false)
    @JoinColumn(name="oi_menu_item_id", referencedColumnName="menu_item_id")
    private MenuItem menuItem;

    // Unit Price * quantity.
    @Column(name = "oi_total", nullable = false)
    private Integer total;

    // Want to record the actual total for this order item in case the menu cost changes.
    @Column(name = "oi_unit_price", nullable = false)
    private Integer unitPrice;

    @Column(name = "oi_quantity", nullable = false)
    private Integer quantity;

    @PrePersist
    protected void onCreate() {
        createdDate = new Timestamp(DateTime.now().getMillis());
        activityDate = new Timestamp(DateTime.now().getMillis());
    }

    @PreUpdate
    protected void onUpdate() {
        activityDate = new Timestamp(DateTime.now().getMillis());
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public Timestamp getActivityDate() {
        return activityDate;
    }
    public void setActivityDate(Timestamp activityDate) {
        this.activityDate = activityDate;
    }

    public Order getOrder() {
        return order;
    }
    public void setOrder(Order order) {
        this.order = order;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }
    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    public Integer getTotal() {
        return total;
    }
    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getUnitPrice() {
        return unitPrice;
    }
    public void setUnitPrice(Integer unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public OrderItem withId(final Long id) {
        setId(id);
        return this;
    }
    public OrderItem withCreatedDate(final Timestamp createdDate) {
        setCreatedDate(createdDate);
        return this;
    }
    public OrderItem withActivityDate(final Timestamp activityDate) {
        setActivityDate(activityDate);
        return this;
    }
    public OrderItem withQuantity(final Integer quantity) {
        setQuantity(quantity);
        return this;
    }
    public OrderItem withMenuItem(final MenuItem menuItem) {
        setMenuItem(menuItem);
        return this;
    }
    public OrderItem withOrder(final Order order) {
        setOrder(order);
        return this;
    }
    public OrderItem withUnitPrice(final Integer unitPrice) {
        setUnitPrice(unitPrice);
        return this;
    }
    public OrderItem withTotal(final Integer total) {
        setTotal(total);
        return this;
    }

    @JsonIgnore
    public void clean() {
        menuItem.setOrderItems(null);
    }
}
