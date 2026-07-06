package com.google.gson;

import java.lang.reflect.Type;

/* JADX INFO: loaded from: leonware-0.0.3.jar:com/google/gson/JsonDeserializer.class */
public interface JsonDeserializer<T> {
    T deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException;
}
