package com.google.gson;

import java.lang.reflect.Type;

/* JADX INFO: loaded from: leonware-0.0.3.jar:com/google/gson/JsonDeserializationContext.class */
public interface JsonDeserializationContext {
    <T> T deserialize(JsonElement jsonElement, Type type) throws JsonParseException;
}
