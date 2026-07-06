// 
// Decompiled by Procyon v0.6.0
// 

package com.google.gson;

import java.io.IOException;
import com.google.gson.stream.JsonReader;

public interface ToNumberStrategy
{
    Number readNumber(final JsonReader p0) throws IOException;
}
