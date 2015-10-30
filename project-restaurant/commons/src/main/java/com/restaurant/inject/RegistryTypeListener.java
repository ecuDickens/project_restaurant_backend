package com.restaurant.inject;

import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

public class RegistryTypeListener<T> implements TypeListener {

    private final RegistryInjectionListener<T> listener;
    private final Class<? super T> typesToRegister;

    public RegistryTypeListener(final RegistryInjectionListener<T> listener, final Class<? super T> typesToRegister) {
        this.listener = listener;
        this.typesToRegister = typesToRegister;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <I> void hear(final TypeLiteral<I> typeLiteral, final TypeEncounter<I> encounter) {
        final Class<? super I> rawType = typeLiteral.getRawType();
        if(typesToRegister.isAssignableFrom(rawType)) {
            ((TypeEncounter<T>)encounter).register(listener);
        }
    }
}
