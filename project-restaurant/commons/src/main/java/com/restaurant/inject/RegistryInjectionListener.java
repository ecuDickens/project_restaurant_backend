package com.restaurant.inject;

import com.google.inject.spi.InjectionListener;
import com.restaurant.collect.InMemoryRegistry;
import com.restaurant.collect.Registry;

/**
 * Listens to injections of a particular type and registers each injected instance into an
 * internal registry for retrieval later. The registry holds a weak reference to each instance
 * so that they may still be garbage collected at any time.
 */
public class RegistryInjectionListener<T> implements InjectionListener<T> {

    private final InMemoryRegistry<T> registry = new InMemoryRegistry<T>();

    @Override
    public void afterInjection(final T injectee) {
        registry.register(injectee);
    }

    /**
     * Returns a registry that contains instances that have been injected and tracked by
     * this listener.
     *
     * @return an Registry of registered instances
     */
    public Registry<T> getRegistry() {
        return registry;
    }
}
