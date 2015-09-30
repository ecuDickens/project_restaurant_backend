package com.restaurant.inject;


import com.google.inject.AbstractModule;
import com.restaurant.DomainConstants;
import com.restaurant.Env;

public class DefaultEnvironmentModule extends AbstractModule {

    private String override = null;

    @Override
    protected void configure() {
        Env env = DomainConstants.DEFAULT_ENV;
        if(null != override) {
            env = env.withEnvironment(override);
        }
        bind(Env.class).toInstance(env);
    }
}
