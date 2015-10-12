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
@Table(name = "menu_inventory_items")
@JsonSerialize(include = NON_NULL)
public class MenuInventoryItem {

    @Id
    @GeneratedValue
    @Column(name = "mi_id")
    private Long id;

    @Column(name = "mi_created_date", nullable = false)
    @JsonSerialize(using=TimestampSerializer.class, include = NON_NULL)
    @JsonDeserialize(using=TimestampDeserializer.class)
    private Timestamp createdDate;

    @Column(name = "mi_activity_date", nullable = false)
    @JsonSerialize(using=TimestampSerializer.class, include = NON_NULL)
    @JsonDeserialize(using=TimestampDeserializer.class)
    private Timestamp activityDate;

    @ManyToOne(optional=false)
    @JoinColumn(name="mi_item_sku", referencedColumnName="item_sku")
    private InventoryItem inventoryItem;

    @ManyToOne(optional=false)
    @JoinColumn(name="mi_menu_id", referencedColumnName="menu_item_id")
    private MenuItem menuItem;

    @Column(name = "mi_quantity", nullable = false)
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

    public InventoryItem getInventoryItem() {
        return inventoryItem;
    }
    public void setInventoryItem(InventoryItem inventoryItem) {
        this.inventoryItem = inventoryItem;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }
    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    public Integer getQuantity() {
        return quantity;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public MenuInventoryItem withId(final Long id) {
        setId(id);
        return this;
    }
    public MenuInventoryItem withCreatedDate(final Timestamp createdDate) {
        setCreatedDate(createdDate);
        return this;
    }
    public MenuInventoryItem withActivityDate(final Timestamp activityDate) {
        setActivityDate(activityDate);
        return this;
    }
    public MenuInventoryItem withQuantity(final Integer quantity) {
        setQuantity(quantity);
        return this;
    }
    public MenuInventoryItem withMenuItem(final MenuItem menuItem) {
        setMenuItem(menuItem);
        return this;
    }
    public MenuInventoryItem withInventoryItem(final InventoryItem inventoryItem) {
        setInventoryItem(inventoryItem);
        return this;
    }

    @JsonIgnore
    public void clean() {
    }
}
