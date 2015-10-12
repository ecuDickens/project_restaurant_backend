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
@Table(name = "payment_methods")
@JsonSerialize(include = NON_NULL)
public class PaymentMethod {

    @Id
    @GeneratedValue
    @Column(name = "pm_id")
    private Long id;

    @Column(name = "pm_created_date", nullable = false)
    @JsonSerialize(using=TimestampSerializer.class, include = NON_NULL)
    @JsonDeserialize(using=TimestampDeserializer.class)
    private Timestamp createdDate;

    @Column(name = "pm_activity_date", nullable = false)
    @JsonSerialize(using=TimestampSerializer.class, include = NON_NULL)
    @JsonDeserialize(using=TimestampDeserializer.class)
    private Timestamp activityDate;

    @ManyToOne(optional=false)
    @JoinColumn(name="pm_user_id", referencedColumnName="user_id")
    private User user;

    @Column(name = "pm_credit_card_number")
    private String creditCardNumber;

    @OneToMany(mappedBy = "paymentMethod", targetEntity = Payment.class, fetch = FetchType.EAGER, cascade = ALL)
    private List<Payment> payments;

    @OneToMany(mappedBy = "paymentMethod", targetEntity = Refund.class, fetch = FetchType.EAGER, cascade = ALL)
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

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }
    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
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

    public PaymentMethod withId(final Long id) {
        setId(id);
        return this;
    }
    public PaymentMethod withCreatedDate(final Timestamp createdDate) {
        setCreatedDate(createdDate);
        return this;
    }
    public PaymentMethod withActivityDate(final Timestamp activityDate) {
        setActivityDate(activityDate);
        return this;
    }
    public PaymentMethod withRefunds(final List<Refund> refunds) {
        setRefunds(refunds);
        return this;
    }
    public PaymentMethod withPayments(final List<Payment> payments) {
        setPayments(payments);
        return this;
    }
    public PaymentMethod withCreditCardNumber(final String creditCardNumber) {
        setCreditCardNumber(creditCardNumber);
        return this;
    }
    public PaymentMethod withUser(final User user) {
        setUser(user);
        return this;
    }

    @JsonIgnore
    public void clean() {
    }
}
