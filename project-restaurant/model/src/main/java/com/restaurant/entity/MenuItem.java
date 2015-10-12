package com.restaurant.entity;

import com.restaurant.datetime.TimestampDeserializer;
import com.restaurant.datetime.TimestampSerializer;
import com.restaurant.entity.enums.MenuItemValues;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

import static java.lang.Boolean.TRUE;
import static javax.persistence.CascadeType.ALL;
import static org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion.NON_NULL;

@Entity
@Table(name = "menu_items")
@JsonSerialize(include = NON_NULL)
public class MenuItem {

    @Id
    @GeneratedValue
    @Column(name = "menu_item_id")
    private Long id;

    @Column(name = "menu_item_created_date", nullable = false)
    @JsonSerialize(using=TimestampSerializer.class, include = NON_NULL)
    @JsonDeserialize(using=TimestampDeserializer.class)
    private Timestamp createdDate;

    @Column(name = "menu_item_activity_date", nullable = false)
    @JsonSerialize(using=TimestampSerializer.class, include = NON_NULL)
    @JsonDeserialize(using=TimestampDeserializer.class)
    private Timestamp activityDate;

    @Column(name = "menu_item_name", length = 50, nullable = false)
    private String name;

    @Column(name = "menu_item_description", length = 255)
    private String description;

    @Column(name = "menu_item_type", nullable = false)
    private MenuItemValues.Type type;

    @Column(name = "menu_item_price", nullable = false)
    private Integer price;

    @Column(name = "menu_item_is_active")
    private Boolean isActive;

    @OneToMany(mappedBy = "menuItem", targetEntity = OrderItem.class, fetch = FetchType.LAZY, cascade = ALL)
    private List<OrderItem> orderItems;

    @OneToMany(mappedBy = "menuItem", targetEntity = MenuInventoryItem.class, fetch = FetchType.LAZY, cascade = ALL)
    private List<MenuInventoryItem> menuInventoryItems;

    @PrePersist
    protected void onCreate() {
        createdDate = new Timestamp(DateTime.now().getMillis());
        activityDate = new Timestamp(DateTime.now().getMillis());
        if (null == isActive) {
            isActive = TRUE;
        }
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

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public MenuItemValues.Type getType() {
        return type;
    }
    public void setType(MenuItemValues.Type type) {
        this.type = type;
    }

    public Integer getPrice() {
        return price;
    }
    public void setPrice(Integer price) {
        this.price = price;
    }

    public Boolean getIsActive() {
        return isActive;
    }
    public void setIsActive(Boolean active) {
        isActive = active;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }
    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public List<MenuInventoryItem> getMenuInventoryItems() {
        return menuInventoryItems;
    }
    public void setMenuInventoryItems(List<MenuInventoryItem> menuInventoryItems) {
        this.menuInventoryItems = menuInventoryItems;
    }

    public MenuItem withId(final Long id) {
        setId(id);
        return this;
    }
    public MenuItem withCreatedDate(final Timestamp createdDate) {
        setCreatedDate(createdDate);
        return this;
    }
    public MenuItem withActivityDate(final Timestamp activityDate) {
        setActivityDate(activityDate);
        return this;
    }
    public MenuItem withName(final String name) {
        setName(name);
        return this;
    }
    public MenuItem withDescription(final String description) {
        setDescription(description);
        return this;
    }
    public MenuItem withType(final MenuItemValues.Type type) {
        setType(type);
        return this;
    }
    public MenuItem withPrice(final Integer price) {
        setPrice(price);
        return this;
    }
    public MenuItem withIsActive(final Boolean isActive) {
        setIsActive(isActive);
        return this;
    }
    public MenuItem withOrderItems(final List<OrderItem> orderItems) {
        setOrderItems(orderItems);
        return this;
    }
    public MenuItem withMenuInventoryItems(final List<MenuInventoryItem> menuInventoryItems) {
        setMenuInventoryItems(menuInventoryItems);
        return this;
    }

    @JsonIgnore
    public void clean() {
    }
}
