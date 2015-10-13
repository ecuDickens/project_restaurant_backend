package com.restaurant.service.restaurant;

import com.google.inject.Inject;
import com.restaurant.base.ThrowingFunction1;
import com.restaurant.entity.MenuItem;
import com.restaurant.entity.Role;
import com.restaurant.entity.User;
import com.restaurant.exception.HttpException;
import com.restaurant.jpa.helper.JpaHelper;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.util.List;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.restaurant.entity.enums.RoleValues.Role.CUSTOMER;
import static javax.persistence.LockModeType.PESSIMISTIC_WRITE;

public class RestaurantServiceImpl implements RestaurantService {

    private final JpaHelper helper;

    @Inject
    public RestaurantServiceImpl(JpaHelper helper) {
        this.helper = helper;
    }

    @Override
    public List<Role> getRoles() throws HttpException {
        return helper.executeJpa(new ThrowingFunction1<List<Role>, EntityManager, HttpException>() {
            @Override
            public List<Role> apply(EntityManager em) throws HttpException {
                Query query = em.createQuery("SELECT e FROM Role e");
                return (List<Role>) query.getResultList();
            }
        });
    }

    @Override
    public Long createUser(final User user) throws HttpException {
        return helper.executeJpaTransaction(new ThrowingFunction1<Long, EntityManager, HttpException>() {
            @Override
            public Long apply(EntityManager em) throws HttpException {
                user.setRole(em.getReference(Role.class, null == user.getRole() ? CUSTOMER.getValue() : user.getRole().getRole()));
                em.persist(user);
                em.flush();
                return user.getId();
            }
        });
    }

    @Override
    public User updateUser(final Long userId, final User user) throws HttpException {
        return helper.executeJpaTransaction(new ThrowingFunction1<User, EntityManager, HttpException>() {
            @Override
            public User apply(EntityManager em) throws HttpException {
                final User forUpdate = em.find(User.class, userId);
                em.refresh(forUpdate, PESSIMISTIC_WRITE);
                forUpdate
                        .withFirstName(isNullOrEmpty(user.getFirstName()) ? forUpdate.getFirstName() : user.getFirstName())
                        .withLastName(isNullOrEmpty(user.getLastName()) ? forUpdate.getLastName() : user.getLastName())
                        .withEmail(isNullOrEmpty(user.getEmail()) ? forUpdate.getEmail() : user.getEmail())
                        .withPassword(isNullOrEmpty(user.getPassword()) ? forUpdate.getPassword() : user.getPassword())
                        .withRole(em.getReference(Role.class, null == user.getRole() ? forUpdate.getRole().getRole() : user.getRole().getRole()))
                        .withWage(null == user.getWage() ? forUpdate.getWage() : user.getWage())
                        .withWeeklyHours(null == user.getWeeklyHours() ? forUpdate.getWeeklyHours() : user.getWeeklyHours())
                        .withIsActive(null == user.getIsActive() ? forUpdate.getIsActive() : user.getIsActive());
                return forUpdate;
            }
        });
    }

    @Override
    public User getUser(final String userId) throws HttpException {
        try {
            return getUser(Long.valueOf(userId));
        } catch(NumberFormatException e) {
            return null;
        }
    }

    @Override
    public User getUser(final Long userId) throws HttpException {
        final User user = helper.executeJpa(new ThrowingFunction1<User, EntityManager, HttpException>() {
            @Override
            public User apply(EntityManager em) throws HttpException {
                return em.find(User.class, userId);
            }
        });
        if (null != user) {
            user.clean();
        }
        return user;
    }

    @Override
    public List<MenuItem> getMenuItems(final Boolean getInactive) throws HttpException {
        return helper.executeJpa(new ThrowingFunction1<List<MenuItem>, EntityManager, HttpException>() {
            @Override
            public List<MenuItem> apply(EntityManager em) throws HttpException {
                final String query = String.format("SELECT e FROM MenuItem e%s", null == getInactive || !getInactive ? " WHERE e.isActive = true" : "");
                return (List<MenuItem>) em.createQuery(query).getResultList();
            }
        });
    }
}
