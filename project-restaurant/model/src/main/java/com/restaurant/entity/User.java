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

import static com.restaurant.collect.MoreIterables.asFluent;
import static java.lang.Boolean.TRUE;
import static javax.persistence.CascadeType.ALL;
import static org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion.NON_NULL;

@Entity
@Table(name = "users")
@JsonSerialize(include = NON_NULL)
public class User {

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_created_date", nullable = false)
    @JsonSerialize(using=TimestampSerializer.class, include = NON_NULL)
    @JsonDeserialize(using=TimestampDeserializer.class)
    private Timestamp createdDate;

    @Column(name = "user_activity_date", nullable = false)
    @JsonSerialize(using=TimestampSerializer.class, include = NON_NULL)
    @JsonDeserialize(using=TimestampDeserializer.class)
    private Timestamp activityDate;

    @Column(name = "user_email", unique = true)
    private String email;

    @Column(name = "user_password", nullable = false)
    private String password;

    @Column(name = "user_first_name", length = 50, nullable = false)
    private String firstName;

    @Column(name = "user_last_name", length = 50, nullable = false)
    private String lastName;

    @OneToOne(optional=false)
    @JoinColumn(name="user_role_id", referencedColumnName="role")
    private Role role;

    @Column(name = "user_payment_method", length = 50)
    private String paymentMethod;

    @Column(name = "user_wage")
    private Integer wage;

    @Column(name = "user_weekly_hours")
    private Integer weeklyHours;

    @Column(name = "user_is_active")
    private Boolean isActive;

    @OneToMany(mappedBy = "user", targetEntity = PaymentMethod.class, fetch = FetchType.EAGER, cascade = ALL)
    private List<PaymentMethod> paymentMethods;

    @OneToMany(mappedBy = "user", targetEntity = Order.class, fetch = FetchType.EAGER, cascade = ALL)
    private List<Order> orders;

    @PrePersist
    protected void onCreate() {
        createdDate = new Timestamp(DateTime.now().getMillis());
        activityDate = new Timestamp(DateTime.now().getMillis());
        if (null == isActive) {
            isActive = TRUE;
        }
        if (null == weeklyHours) {
            weeklyHours = 0;
        }
        if (null == wage) {
            wage = 0;
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

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Integer getWage() {
        return wage;
    }
    public void setWage(Integer wage) {
        this.wage = wage;
    }

    public Integer getWeeklyHours() {
        return weeklyHours;
    }
    public void setWeeklyHours(Integer weeklyHours) {
        this.weeklyHours = weeklyHours;
    }

    public Boolean getIsActive() {
        return isActive;
    }
    public void setIsActive(Boolean active) {
        isActive = active;
    }

    public List<PaymentMethod> getPaymentMethods() {
        return paymentMethods;
    }
    public void setPaymentMethods(List<PaymentMethod> paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    public List<Order> getOrders() {
        return orders;
    }
    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public User withId(final Long id) {
        setId(id);
        return this;
    }
    public User withCreatedDate(final Timestamp createdDate) {
        setCreatedDate(createdDate);
        return this;
    }
    public User withActivityDate(final Timestamp activityDate) {
        setActivityDate(activityDate);
        return this;
    }
    public User withEmail(final String email) {
        setEmail(email);
        return this;
    }
    public User withPassword(final String password) {
        setPassword(password);
        return this;
    }
    public User withFirstName(final String firstName) {
        setFirstName(firstName);
        return this;
    }
    public User withLastName(final String lastName) {
        setLastName(lastName);
        return this;
    }
    public User withRole(final Role role) {
        setRole(role);
        return this;
    }
    public User withPaymentMethod(final String paymentMethod) {
        setPaymentMethod(paymentMethod);
        return this;
    }
    public User withWage(final Integer wage) {
        setWage(wage);
        return this;
    }
    public User withWeeklyHours(final Integer weeklyHours) {
        setWeeklyHours(weeklyHours);
        return this;
    }
    public User withIsActive(final Boolean isActive) {
        setIsActive(isActive);
        return this;
    }
    public User withPaymentMethods(final List<PaymentMethod> paymentMethods) {
        setPaymentMethods(paymentMethods);
        return this;
    }
    public User withOrders(final List<Order> orders) {
        setOrders(orders);
        return this;
    }

    @JsonIgnore
    public void clean() {
        for (PaymentMethod paymentMethod : asFluent(paymentMethods)) {
            paymentMethod.clean();
        }
        for (Order order : asFluent(orders)) {
            order.clean();
        }
    }
}
