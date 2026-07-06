package com.google.gson;

import com.google.gson.stream.JsonReader;
import java.io.IOException;

/* JADX INFO: loaded from: leonware-0.0.3.jar:com/google/gson/ToNumberStrategy.class */
public interface ToNumberStrategy {
    Number readNumber(JsonReader jsonReader) throws IOException;
}
