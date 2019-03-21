package com.example.moneysaver.helper;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

public abstract class  MyRunnable<T> implements Serializable {

    public abstract void run(T t);

    public Class<T> getGenericType() {
        Class<T> persistentClass = (Class<T>)
                ((ParameterizedType) getClass().getGenericSuperclass())
                        .getActualTypeArguments()[0];

        return persistentClass;
    }
}
