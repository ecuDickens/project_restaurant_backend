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
@Table(name = "payments")
@JsonSerialize(include = NON_NULL)
public class Payment {

    @Id
    @GeneratedValue
    @Column(name = "payment_id")
    private Long id;

    @Column(name = "payment_created_date", nullable = false)
    @JsonSerialize(using=TimestampSerializer.class, include = NON_NULL)
    @JsonDeserialize(using=TimestampDeserializer.class)
    private Timestamp createdDate;

    @Column(name = "payment_activity_date", nullable = false)
    @JsonSerialize(using=TimestampSerializer.class, include = NON_NULL)
    @JsonDeserialize(using=TimestampDeserializer.class)
    private Timestamp activityDate;

    @ManyToOne(optional=false)
    @JoinColumn(name="payment_order_number", referencedColumnName="order_number")
    private Order order;

    @ManyToOne(optional=false)
    @JoinColumn(name="payment_method", referencedColumnName="pm_id")
    private PaymentMethod paymentMethod;

    @Column(name = "payment_amount", nullable = false)
    private Integer paymentAmount;

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

    public Integer getPaymentAmount() {
        return paymentAmount;
    }
    public void setPaymentAmount(Integer paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public Payment withId(final Long id) {
        setId(id);
        return this;
    }
    public Payment withCreatedDate(final Timestamp createdDate) {
        setCreatedDate(createdDate);
        return this;
    }
    public Payment withActivityDate(final Timestamp activityDate) {
        setActivityDate(activityDate);
        return this;
    }
    public Payment withPaymentAmount(final Integer paymentAmount) {
        setPaymentAmount(paymentAmount);
        return this;
    }
    public Payment withOrder(final Order order) {
        setOrder(order);
        return this;
    }
    public Payment withPaymentMethod(final PaymentMethod paymentMethod) {
        setPaymentMethod(paymentMethod);
        return this;
    }

    @JsonIgnore
    public void clean() {
    }
}
