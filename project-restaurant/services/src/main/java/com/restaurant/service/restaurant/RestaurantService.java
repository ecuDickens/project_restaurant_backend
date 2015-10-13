package com.restaurant.service.restaurant;

import com.restaurant.entity.MenuItem;
import com.restaurant.entity.Role;
import com.restaurant.entity.User;
import com.restaurant.exception.HttpException;

import java.util.List;

public interface RestaurantService {

    /**
     * Loads all currently created roles.
     *
     * @return all roles.
     * @throws HttpException
     */
    List<Role> getRoles() throws HttpException;

    /**
     * Creates the user record in the DBS, returning the generated id.
     *
     * @param user the user to create.
     * @return the generated id.
     * @throws HttpException
     */
    Long createUser(User user) throws HttpException;

    /**
     * Updates any changed fields with the supplied ones.
     *
     * @param userId the related user id.
     * @param user the fields to update.
     * @return the updated user.
     * @throws HttpException
     */
    User updateUser(Long userId, User user) throws HttpException;

    /**
     * Parses the supplied id and attempts to retrieve the related user.
     *
     * @param userId the user id.
     * @return the related user.
     * @throws HttpException
     */
    User getUser(String userId) throws HttpException;

    /**
     * Loads the user related to the supplied id.
     *
     * @param userId the user id.
     * @return the related user.
     * @throws HttpException
     */
    User getUser(Long userId) throws HttpException;

    /**
     * Loads all active menu items (all menu items if getInactive is true).
     *
     * @param getInactive flag denoting whether to load inactive menu items.
     * @return list of menu items.
     * @throws HttpException
     */
    List<MenuItem> getMenuItems(Boolean getInactive) throws HttpException;
}
