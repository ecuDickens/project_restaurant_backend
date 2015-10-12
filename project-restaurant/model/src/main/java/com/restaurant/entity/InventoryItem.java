package com.restaurant.entity;

import com.restaurant.datetime.TimestampDeserializer;
import com.restaurant.datetime.TimestampSerializer;
import com.restaurant.entity.enums.InventoryItemValues;
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
@Table(name = "inventory_items")
@JsonSerialize(include = NON_NULL)
public class InventoryItem {

    @Id
    @Column(name = "item_sku")
    private String sku;

    @Column(name = "item_created_date", nullable = false)
    @JsonSerialize(using=TimestampSerializer.class, include = NON_NULL)
    @JsonDeserialize(using=TimestampDeserializer.class)
    private Timestamp createdDate;

    @Column(name = "item_activity_date", nullable = false)
    @JsonSerialize(using=TimestampSerializer.class, include = NON_NULL)
    @JsonDeserialize(using=TimestampDeserializer.class)
    private Timestamp activityDate;

    @Column(name = "item_name", length = 50, nullable = false)
    private String name;

    @Column(name = "item_description", length = 255)
    private String description;

    @Column(name = "item_type", nullable = false)
    private InventoryItemValues.Type type;

    @Column(name = "item_quantity", nullable = false)
    private Integer quantity;

    @Column(name = "item_is_active")
    private Boolean isActive;

    @OneToMany(mappedBy = "inventoryItem", targetEntity = MenuInventoryItem.class, fetch = FetchType.LAZY, cascade = ALL)
    private List<MenuInventoryItem> menuInventoryItems;

    @OneToMany(mappedBy = "inventoryItem", targetEntity = PurchaseOrderItem.class, fetch = FetchType.LAZY, cascade = ALL)
    private List<PurchaseOrderItem> purchaseOrderItems;

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

    public String getSku() {
        return sku;
    }
    public void setSku(String sku) {
        this.sku = sku;
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

    public InventoryItemValues.Type getType() {
        return type;
    }
    public void setType(InventoryItemValues.Type type) {
        this.type = type;
    }

    public Integer getQuantity() {
        return quantity;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Boolean getIsActive() {
        return isActive;
    }
    public void setIsActive(Boolean active) {
        isActive = active;
    }

    public List<MenuInventoryItem> getMenuInventoryItems() {
        return menuInventoryItems;
    }
    public void setMenuInventoryItems(List<MenuInventoryItem> menuInventoryItems) {
        this.menuInventoryItems = menuInventoryItems;
    }

    public List<PurchaseOrderItem> getPurchaseOrderItems() {
        return purchaseOrderItems;
    }
    public void setPurchaseOrderItems(List<PurchaseOrderItem> purchaseOrderItems) {
        this.purchaseOrderItems = purchaseOrderItems;
    }

    public InventoryItem withSku(final String sku) {
        setSku(sku);
        return this;
    }
    public InventoryItem withCreatedDate(final Timestamp createdDate) {
        setCreatedDate(createdDate);
        return this;
    }
    public InventoryItem withActivityDate(final Timestamp activityDate) {
        setActivityDate(activityDate);
        return this;
    }
    public InventoryItem withName(final String name) {
        setName(name);
        return this;
    }
    public InventoryItem withDescription(final String description) {
        setDescription(description);
        return this;
    }
    public InventoryItem withType(final InventoryItemValues.Type type) {
        setType(type);
        return this;
    }
    public InventoryItem withQuantity(final Integer quantity) {
        setQuantity(quantity);
        return this;
    }
    public InventoryItem withIsActive(final Boolean isActive) {
        setIsActive(isActive);
        return this;
    }
    public InventoryItem withMenuInventoryItems(final List<MenuInventoryItem> menuInventoryItems) {
        setMenuInventoryItems(menuInventoryItems);
        return this;
    }
    public InventoryItem withPurchaseOrderItems(final List<PurchaseOrderItem> purchaseOrderItems) {
        setPurchaseOrderItems(purchaseOrderItems);
        return this;
    }

    @JsonIgnore
    public void clean() {
    }
}
