package com.restaurant.entity;

import com.restaurant.datetime.TimestampDeserializer;
import com.restaurant.datetime.TimestampSerializer;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion.NON_NULL;

@Entity
@Table(name = "purchase_orders")
@JsonSerialize(include = NON_NULL)
public class PurchaseOrder {

    @Id
    @GeneratedValue
    @Column(name = "po_id")
    private Long id;

    @Column(name = "po_created_date", nullable = false)
    @JsonSerialize(using=TimestampSerializer.class, include = NON_NULL)
    @JsonDeserialize(using=TimestampDeserializer.class)
    private Timestamp createdDate;

    @Column(name = "po_activity_date", nullable = false)
    @JsonSerialize(using=TimestampSerializer.class, include = NON_NULL)
    @JsonDeserialize(using=TimestampDeserializer.class)
    private Timestamp activityDate;

    @Column(name = "po_total", nullable = false)
    private Integer total;

    @OneToMany(mappedBy = "purchaseOrder", targetEntity = PurchaseOrderItem.class, fetch = FetchType.EAGER, cascade = ALL)
    private List<PurchaseOrderItem> purchaseOrderItems;

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

    public Integer getTotal() {
        return total;
    }
    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<PurchaseOrderItem> getPurchaseOrderItems() {
        return purchaseOrderItems;
    }
    public void setPurchaseOrderItems(List<PurchaseOrderItem> purchaseOrderItems) {
        this.purchaseOrderItems = purchaseOrderItems;
    }

    public PurchaseOrder withId(final Long id) {
        setId(id);
        return this;
    }
    public PurchaseOrder withCreatedDate(final Timestamp createdDate) {
        setCreatedDate(createdDate);
        return this;
    }
    public PurchaseOrder withActivityDate(final Timestamp activityDate) {
        setActivityDate(activityDate);
        return this;
    }
    public PurchaseOrder withTotal(final Integer total) {
        setTotal(total);
        return this;
    }
    public PurchaseOrder withPurchaseOrderItems(final List<PurchaseOrderItem> purchaseOrderItems) {
        setPurchaseOrderItems(purchaseOrderItems);
        return this;
    }

    @JsonIgnore
    public void clean() {
    }
}
