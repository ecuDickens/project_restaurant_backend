package com.restaurant.inject;


import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.inject.TypeLiteral;
import com.google.inject.internal.MoreTypes;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

/**
 * Listens for injections of a particular generic raw type
 */
public abstract class GenericTypeListener<T> implements TypeListener {

    private final Class<T> rawTypeClass;

    public GenericTypeListener(Class<T> rawTypeClass) {
        this.rawTypeClass = rawTypeClass;
    }

    @Override
    public <I> void hear(final TypeLiteral<I> typeLiteral, final TypeEncounter<I> encounter) {
        final Class<? super I> rawType = typeLiteral.getRawType();
        if(rawTypeClass.isAssignableFrom(rawType)) {
            final Type type = typeLiteral.getType();
            if(type instanceof ParameterizedType) {
                final List<Type> pTypes = getTypeParameters(type);
                ((TypeEncounter<T>)encounter).register(new InjectionListener<T>() {
                    @Override
                    public void afterInjection(T injectee) {
                        GenericTypeListener.this.afterInjection(injectee, pTypes);
                    }
                });
            }
        }
    }
    public static List<Type> getTypeParameters(Type type) {
        Preconditions.checkArgument(type instanceof ParameterizedType, "No type parameter was found");
        ParameterizedType pType = (ParameterizedType) type;
        Type[] types = pType.getActualTypeArguments();
        return Lists.newArrayList(Iterables.transform(Arrays.asList(types), new Function<Type, Type>() {
            @Override
            public Type apply(Type input) {
                return MoreTypes.canonicalize(input);
            }
        }));
    }

    public abstract void afterInjection(T injectee, List<Type> actualTypeArgs);
}
