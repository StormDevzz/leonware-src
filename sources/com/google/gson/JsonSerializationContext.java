package com.google.gson;

import java.lang.reflect.Type;

/* JADX INFO: loaded from: leonware-0.0.3.jar:com/google/gson/JsonSerializationContext.class */
public interface JsonSerializationContext {
    JsonElement serialize(Object obj);

    JsonElement serialize(Object obj, Type type);
}
