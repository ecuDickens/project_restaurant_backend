package com.restaurant.entity;

import com.restaurant.datetime.TimestampDeserializer;
import com.restaurant.datetime.TimestampSerializer;
import com.restaurant.entity.enums.OrderValues;
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
@Table(name = "orders")
@JsonSerialize(include = NON_NULL)
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "order_number")
    private Long orderNumber;

    @Column(name = "order_created_date", nullable = false)
    @JsonSerialize(using=TimestampSerializer.class, include = NON_NULL)
    @JsonDeserialize(using=TimestampDeserializer.class)
    private Timestamp createdDate;

    @Column(name = "order_activity_date", nullable = false)
    @JsonSerialize(using=TimestampSerializer.class, include = NON_NULL)
    @JsonDeserialize(using=TimestampDeserializer.class)
    private Timestamp activityDate;

    @ManyToOne(optional=false)
    @JoinColumn(name="order_user_id", referencedColumnName="user_id")
    private User user;

    @Column(name="order_payment_method_id", nullable = false)
    private String paymentMethod;

    @Column(name = "order_total", nullable = false)
    private Integer total;

    @Column(name = "order_item_total", nullable = false)
    private Integer itemTotal;

    @Column(name = "order_tax", nullable = false)
    private Integer tax;

    @Column(name = "order_tip", nullable = false)
    private Integer tip;

    @Column(name = "order_type", nullable = false)
    private OrderValues.Type type;

    @OneToMany(mappedBy = "order", targetEntity = OrderItem.class, fetch = FetchType.EAGER, cascade = ALL)
    private List<OrderItem> orderItems;

    @OneToMany(mappedBy = "order", targetEntity = Payment.class, fetch = FetchType.EAGER, cascade = ALL)
    private List<Payment> payments;

    @OneToMany(mappedBy = "order", targetEntity = Refund.class, fetch = FetchType.EAGER, cascade = ALL)
    private List<Refund> refunds;

    @PrePersist
    protected void onCreate() {
        createdDate = new Timestamp(DateTime.now().getMillis());
        activityDate = new Timestamp(DateTime.now().getMillis());
    }

    @PreUpdate
    protected void onUpdate() {
        activityDate = new Timestamp(DateTime.now().getMillis());
    }

    public Long getOrderNumber() {
        return orderNumber;
    }
    public void setOrderNumber(Long orderNumber) {
        this.orderNumber = orderNumber;
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

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Integer getTotal() {
        return total;
    }
    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getItemTotal() {
        return itemTotal;
    }
    public void setItemTotal(Integer itemTotal) {
        this.itemTotal = itemTotal;
    }

    public Integer getTax() {
        return tax;
    }
    public void setTax(Integer tax) {
        this.tax = tax;
    }

    public Integer getTip() {
        return tip;
    }
    public void setTip(Integer tip) {
        this.tip = tip;
    }

    public OrderValues.Type getType() {
        return type;
    }
    public void setType(OrderValues.Type type) {
        this.type = type;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }
    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public List<Payment> getPayments() {
        return payments;
    }
    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public List<Refund> getRefunds() {
        return refunds;
    }
    public void setRefunds(List<Refund> refunds) {
        this.refunds = refunds;
    }

    public Order withOrderNumber(final Long orderNumber) {
        setOrderNumber(orderNumber);
        return this;
    }
    public Order withCreatedDate(final Timestamp createdDate) {
        setCreatedDate(createdDate);
        return this;
    }
    public Order withActivityDate(final Timestamp activityDate) {
        setActivityDate(activityDate);
        return this;
    }
    public Order withUser(final User user) {
        setUser(user);
        return this;
    }
    public Order withPaymentMethod(final String paymentMethod) {
        setPaymentMethod(paymentMethod);
        return this;
    }
    public Order withTotal(final Integer total) {
        setTotal(total);
        return this;
    }
    public Order withTip(final Integer tip) {
        setTip(tip);
        return this;
    }
    public Order withTax(final Integer tax) {
        setTax(tax);
        return this;
    }
    public Order withItemTotal(final Integer itemTotal) {
        setItemTotal(itemTotal);
        return this;
    }
    public Order withType(final OrderValues.Type type) {
        setType(type);
        return this;
    }
    public Order withOrderItems(final List<OrderItem> orderItems) {
        setOrderItems(orderItems);
        return this;
    }
    public Order withPayments(final List<Payment> payments) {
        setPayments(payments);
        return this;
    }
    public Order withRefunds(final List<Refund> refunds) {
        setRefunds(refunds);
        return this;
    }

    @JsonIgnore
    public void clean() {
    }
}
