// 
// Decompiled by Procyon v0.6.0
// 

package com.google.gson.internal.sql;

import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import com.google.gson.stream.JsonReader;
import java.util.Date;
import com.google.gson.TypeAdapterFactory;
import java.sql.Timestamp;
import com.google.gson.TypeAdapter;

class SqlTimestampTypeAdapter extends TypeAdapter<Timestamp>
{
    static final TypeAdapterFactory FACTORY;
    private final TypeAdapter<Date> dateTypeAdapter;
    
    private SqlTimestampTypeAdapter(final TypeAdapter<Date> dateTypeAdapter) {
        this.dateTypeAdapter = dateTypeAdapter;
    }
    
    @Override
    public Timestamp read(final JsonReader in) throws IOException {
        final Date date = this.dateTypeAdapter.read(in);
        return (date != null) ? new Timestamp(date.getTime()) : null;
    }
    
    @Override
    public void write(final JsonWriter out, final Timestamp value) throws IOException {
        this.dateTypeAdapter.write(out, value);
    }
    
    static {
        FACTORY = new TypeAdapterFactory() {
            @Override
            public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> typeToken) {
                if (typeToken.getRawType() == Timestamp.class) {
                    final TypeAdapter<Date> dateTypeAdapter = gson.getAdapter(Date.class);
                    return (TypeAdapter<T>)new SqlTimestampTypeAdapter(dateTypeAdapter, null);
                }
                return null;
            }
        };
    }
}
