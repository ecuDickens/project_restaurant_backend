package com.restaurant.service.role;

import com.google.inject.Singleton;
import com.restaurant.entity.User;

import static com.restaurant.entity.enums.RoleValues.Role.ADMIN;
import static com.restaurant.entity.enums.RoleValues.Role.EMPLOYEE;

/**
 * Returns an instance of a role validator for a service to use.
 */
@Singleton
public class RoleValidatorFactory {
    public RoleValidator create(final User submitter) {
        if (null != submitter) {
            if (ADMIN.getValue().equals(submitter.getRole().getRole())) {
                return new AdminValidator();
            } else if (EMPLOYEE.getValue().equals(submitter.getRole().getRole())) {
                return new EmployeeValidator();
            }
        }
        return new CustomerValidator();
    }
}
