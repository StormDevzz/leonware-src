package com.google.gson;

import java.lang.reflect.Type;

/* JADX INFO: loaded from: leonware-0.0.3.jar:com/google/gson/InstanceCreator.class */
public interface InstanceCreator<T> {
    T createInstance(Type type);
}
