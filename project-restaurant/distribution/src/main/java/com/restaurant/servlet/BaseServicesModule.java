package com.restaurant.servlet;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Names;
import com.restaurant.inject.DefaultConfigurationModule;
import com.restaurant.inject.DefaultEnvironmentModule;
import com.restaurant.jpa.JpaEntityManagerServiceImpl;
import com.restaurant.jpa.JpaServiceConstants;
import com.restaurant.jpa.spi.JpaEntityManagerService;
import com.restaurant.service.restaurant.RestaurantService;
import com.restaurant.service.restaurant.RestaurantServiceImpl;
import com.restaurant.service.threadpool.ThreadPoolServiceImpl;
import com.restaurant.spi.ThreadPoolService;

/**
 * Module which defines all Service injections
 */
public class BaseServicesModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new DefaultEnvironmentModule());
        install(new DefaultConfigurationModule());
        bind(JpaEntityManagerService.class)
                .to(JpaEntityManagerServiceImpl.class)
                .in(Singleton.class);
        bind(RestaurantService.class)
                .to(RestaurantServiceImpl.class)
                .in(Singleton.class);
        Multibinder.newSetBinder(binder(), String.class, Names.named(JpaServiceConstants.ENTITY_CLASS_NAMES));
        bind(ThreadPoolService.class)
                .to(ThreadPoolServiceImpl.class)
                .in(Singleton.class);
    }
}
