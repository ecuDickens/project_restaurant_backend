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
@Table(name = "purchase_order_items")
@JsonSerialize(include = NON_NULL)
public class PurchaseOrderItem {

    @Id
    @GeneratedValue
    @Column(name = "poi_id")
    private Long id;

    @Column(name = "poi_created_date", nullable = false)
    @JsonSerialize(using=TimestampSerializer.class, include = NON_NULL)
    @JsonDeserialize(using=TimestampDeserializer.class)
    private Timestamp createdDate;

    @Column(name = "poi_activity_date", nullable = false)
    @JsonSerialize(using=TimestampSerializer.class, include = NON_NULL)
    @JsonDeserialize(using=TimestampDeserializer.class)
    private Timestamp activityDate;

    @ManyToOne(optional=false)
    @JoinColumn(name="poi_item_sku", referencedColumnName="item_sku")
    private InventoryItem inventoryItem;

    @ManyToOne(optional=false)
    @JoinColumn(name="poi_purchase_order_id", referencedColumnName="po_id")
    private PurchaseOrder purchaseOrder;

    @Column(name = "poi_quantity", nullable = false)
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

    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }
    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public Integer getQuantity() {
        return quantity;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public PurchaseOrderItem withId(final Long id) {
        setId(id);
        return this;
    }
    public PurchaseOrderItem withCreatedDate(final Timestamp createdDate) {
        setCreatedDate(createdDate);
        return this;
    }
    public PurchaseOrderItem withActivityDate(final Timestamp activityDate) {
        setActivityDate(activityDate);
        return this;
    }
    public PurchaseOrderItem withQuantity(final Integer quantity) {
        setQuantity(quantity);
        return this;
    }
    public PurchaseOrderItem withMenuItem(final PurchaseOrder purchaseOrder) {
        setPurchaseOrder(purchaseOrder);
        return this;
    }
    public PurchaseOrderItem withInventoryItem(final InventoryItem inventoryItem) {
        setInventoryItem(inventoryItem);
        return this;
    }

    @JsonIgnore
    public void clean() {
    }
}
