package com.restaurant.servlet;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;
import com.restaurant.DomainConstants;
import com.restaurant.collect.Registry;
import com.restaurant.spi.Worker;

import javax.servlet.ServletContextEvent;
import java.util.List;

/**
 * Guice ConfigurationLoader
 */
public class GuiceConfig extends BaseServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        // These properties should only be set in a true external web container, NOT in a test container
        // These properties MUST be set BEFORE initializing the context
        System.setProperty("com.restaurant.root", servletContextEvent.getServletContext().getRealPath("/"));
        System.setProperty("com.restaurant.standalone", "false");

        // This initializes the whole app
        super.contextInitialized(servletContextEvent);

        // start all Workers
        final Registry<Worker> workerRegistry = getInjector().getInstance(Key.get(new TypeLiteral<Registry<Worker>>() {} ));
        final ImmutableSet<Worker> workers = workerRegistry.getRegistered();
        final List<ListenableFuture<Boolean>> futures = Lists.newArrayListWithExpectedSize(workers.size());
        if (!"enabled".equals(DomainConstants.XFAST)) {
            for (Worker instance : workers) {
                futures.add(instance.start());
            }
        }

        servletContextEvent.getServletContext().setAttribute("com.restaurant.injector", getInjector());
    }

    @Override
    protected Iterable<Module> getModules() {
        return Lists.<Module>newArrayList(
                new BaseServicesModule(),
                new JerseyServletModule());
    }
}
