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
@Table(name = "refunds")
@JsonSerialize(include = NON_NULL)
public class Refund {

    @Id
    @GeneratedValue
    @Column(name = "refund_id")
    private Long id;

    @Column(name = "refund_created_date", nullable = false)
    @JsonSerialize(using=TimestampSerializer.class, include = NON_NULL)
    @JsonDeserialize(using=TimestampDeserializer.class)
    private Timestamp createdDate;

    @Column(name = "refund_activity_date", nullable = false)
    @JsonSerialize(using=TimestampSerializer.class, include = NON_NULL)
    @JsonDeserialize(using=TimestampDeserializer.class)
    private Timestamp activityDate;

    @ManyToOne(optional=false)
    @JoinColumn(name="refund_order_number", referencedColumnName="order_number")
    private Order order;

    @ManyToOne(optional=false)
    @JoinColumn(name="refund_payment_method", referencedColumnName="pm_id")
    private PaymentMethod paymentMethod;

    @Column(name = "refund_amount", nullable = false)
    private Integer refundAmount;

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

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }
    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Integer getRefundAmount() {
        return refundAmount;
    }
    public void setRefundAmount(Integer refundAmount) {
        this.refundAmount = refundAmount;
    }

    public Refund withId(final Long id) {
        setId(id);
        return this;
    }
    public Refund withCreatedDate(final Timestamp createdDate) {
        setCreatedDate(createdDate);
        return this;
    }
    public Refund withActivityDate(final Timestamp activityDate) {
        setActivityDate(activityDate);
        return this;
    }
    public Refund withRefundAmount(final Integer paymentAmount) {
        setRefundAmount(paymentAmount);
        return this;
    }
    public Refund withOrder(final Order order) {
        setOrder(order);
        return this;
    }
    public Refund withPaymentMethod(final PaymentMethod paymentMethod) {
        setPaymentMethod(paymentMethod);
        return this;
    }

    @JsonIgnore
    public void clean() {
    }
}
