package com.restaurant.base;

public interface ThrowingFunction1<R, T1, E extends Exception> {

    R apply(T1 arg0) throws E;
}
