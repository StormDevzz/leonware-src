package com.google.gson;

import com.google.gson.reflect.TypeToken;

/* JADX INFO: loaded from: leonware-0.0.3.jar:com/google/gson/TypeAdapterFactory.class */
public interface TypeAdapterFactory {
    <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken);
}
