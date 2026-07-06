package com.google.gson;

import java.lang.reflect.Type;

/* JADX INFO: loaded from: leonware-0.0.3.jar:com/google/gson/JsonSerializer.class */
public interface JsonSerializer<T> {
    JsonElement serialize(T t, Type type, JsonSerializationContext jsonSerializationContext);
}
