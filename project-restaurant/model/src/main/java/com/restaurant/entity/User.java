package com.restaurant.entity;

import com.restaurant.datetime.TimestampDeserializer;
import com.restaurant.datetime.TimestampSerializer;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.sql.Timestamp;

import static java.lang.Boolean.TRUE;
import static org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion.NON_NULL;

@Entity
@Table(name = "USERS")
@JsonSerialize(include = NON_NULL)
public class User {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "CREATED_DATETIME", nullable = false)
    @JsonSerialize(using=TimestampSerializer.class, include = NON_NULL)
    @JsonDeserialize(using=TimestampDeserializer.class)
    private Timestamp createdDatetime;

    @Column(name = "LAST_MODIFIED_DATETIME", nullable = false)
    @JsonSerialize(using=TimestampSerializer.class, include = NON_NULL)
    @JsonDeserialize(using=TimestampDeserializer.class)
    private Timestamp lastModifiedDatetime;

    @Column(name = "EMAIL", unique = true)
    private String email;

    @Column(name = "PASSWORD", unique = true)
    private String password;

    @Column(name = "FIRST_NAME", length = 50, nullable = false)
    private String firstName;

    @Column(name = "LAST_NAME", length = 50, nullable = false)
    private String lastName;

    @Column(name = "ROLE", length = 50)
    private String role;

    @Column(name = "PAYMENT_METHOD", length = 50)
    private String paymentMethod;

    @Column(name = "WAGE")
    private Integer wage;

    @Column(name = "WEEKLY_HOURS")
    private Integer weeklyHours;

    @Column(name = "ACTIVE")
    private Boolean isActive;

    @PrePersist
    protected void onCreate() {
        createdDatetime = new Timestamp(DateTime.now().getMillis());
        lastModifiedDatetime = new Timestamp(DateTime.now().getMillis());
        if (null == isActive) {
            isActive = TRUE;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        lastModifiedDatetime = new Timestamp(DateTime.now().getMillis());
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getCreatedDatetime() {
        return createdDatetime;
    }
    public void setCreatedDatetime(Timestamp createdDatetime) {
        this.createdDatetime = createdDatetime;
    }

    public Timestamp getLastModifiedDatetime() {
        return lastModifiedDatetime;
    }
    public void setLastModifiedDatetime(Timestamp lastModifiedDatetime) {
        this.lastModifiedDatetime = lastModifiedDatetime;
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

    public String getRole() {
        return role;
    }
    public void setRole(String role) {
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
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public User withId(final Long id) {
        setId(id);
        return this;
    }
    public User withCreatedDatetime(final Timestamp createdDatetime) {
        setCreatedDatetime(createdDatetime);
        return this;
    }
    public User withLastModifiedDatetime(final Timestamp lastModifiedDatetime) {
        setLastModifiedDatetime(lastModifiedDatetime);
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
    public User withRole(final String role) {
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

    @JsonIgnore
    public void clean() {
    }
}
