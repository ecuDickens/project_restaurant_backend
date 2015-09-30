package com.restaurant.servlet;

import com.google.inject.AbstractModule;
import com.restaurant.web.AccountResource;
import com.restaurant.web.ExerciseResource;
import com.restaurant.web.LoginResource;
import com.restaurant.web.LogoutResource;

public class APIModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(AccountResource.class);
        bind(LoginResource.class);
        bind(LogoutResource.class);
        bind(ExerciseResource.class);
    }
}
