// 
// Decompiled by Procyon v0.6.0
// 

package com.google.gson.internal.bind;

import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.GenericArrayType;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonReader;
import java.lang.reflect.Type;
import com.google.gson.Gson;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.TypeAdapter;

public final class ArrayTypeAdapter<E> extends TypeAdapter<Object>
{
    public static final TypeAdapterFactory FACTORY;
    private final Class<E> componentType;
    private final TypeAdapter<E> componentTypeAdapter;
    
    public ArrayTypeAdapter(final Gson context, final TypeAdapter<E> componentTypeAdapter, final Class<E> componentType) {
        this.componentTypeAdapter = new TypeAdapterRuntimeTypeWrapper<E>(context, componentTypeAdapter, componentType);
        this.componentType = componentType;
    }
    
    @Override
    public Object read(final JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        final ArrayList<E> list = new ArrayList<E>();
        in.beginArray();
        while (in.hasNext()) {
            final E instance = this.componentTypeAdapter.read(in);
            list.add(instance);
        }
        in.endArray();
        final int size = list.size();
        if (this.componentType.isPrimitive()) {
            final Object array = Array.newInstance(this.componentType, size);
            for (int i = 0; i < size; ++i) {
                Array.set(array, i, list.get(i));
            }
            return array;
        }
        final E[] array2 = (E[])Array.newInstance(this.componentType, size);
        return list.toArray(array2);
    }
    
    @Override
    public void write(final JsonWriter out, final Object array) throws IOException {
        if (array == null) {
            out.nullValue();
            return;
        }
        out.beginArray();
        for (int i = 0, length = Array.getLength(array); i < length; ++i) {
            final E value = (E)Array.get(array, i);
            this.componentTypeAdapter.write(out, value);
        }
        out.endArray();
    }
    
    static {
        FACTORY = new TypeAdapterFactory() {
            @Override
            public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> typeToken) {
                final Type type = typeToken.getType();
                if (!(type instanceof GenericArrayType) && (!(type instanceof Class) || !((Class)type).isArray())) {
                    return null;
                }
                final Type componentType = $Gson$Types.getArrayComponentType(type);
                final TypeAdapter<?> componentTypeAdapter = gson.getAdapter(TypeToken.get(componentType));
                final TypeAdapter<T> arrayAdapter = (TypeAdapter<T>)new ArrayTypeAdapter(gson, (TypeAdapter<Object>)componentTypeAdapter, (Class<Object>)$Gson$Types.getRawType(componentType));
                return arrayAdapter;
            }
        };
    }
}
