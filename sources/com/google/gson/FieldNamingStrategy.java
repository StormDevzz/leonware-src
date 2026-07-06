package com.google.gson;

import java.lang.reflect.Field;

/* JADX INFO: loaded from: leonware-0.0.3.jar:com/google/gson/FieldNamingStrategy.class */
public interface FieldNamingStrategy {
    String translateName(Field field);
}
