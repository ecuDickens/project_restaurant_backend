package com.restaurant.servlet;

import com.google.inject.AbstractModule;
import com.restaurant.web.*;

public class APIModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(UserResource.class);
        bind(AccountResource.class);
        bind(LoginResource.class);
        bind(LogoutResource.class);
        bind(ExerciseResource.class);
    }
}
