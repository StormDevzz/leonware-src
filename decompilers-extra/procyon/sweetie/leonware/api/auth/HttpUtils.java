// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.auth;

import com.google.gson.GsonBuilder;
import java.io.InputStream;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.util.Iterator;
import java.io.DataOutputStream;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.Map;
import com.google.gson.Gson;

public class HttpUtils
{
    private static final String DEFAULT_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36";
    private static final Gson GSON;
    
    private static HttpURLConnection makeConnection(final String url, final String method, final String data, final Map<String, String> headers, final String agent) throws IOException {
        final HttpURLConnection connection = (HttpURLConnection)new URL(url).openConnection();
        connection.setRequestMethod(method);
        connection.setConnectTimeout(2000);
        connection.setReadTimeout(10000);
        connection.setRequestProperty("User-Agent", agent);
        if (headers != null) {
            for (final Map.Entry<String, String> entry : headers.entrySet()) {
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
        connection.setInstanceFollowRedirects(true);
        connection.setDoOutput(true);
        if (data != null && !data.isEmpty()) {
            try (final DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {
                outputStream.writeBytes(data);
            }
        }
        connection.connect();
        return connection;
    }
    
    public static HttpResponse request(final String url, final String method, final String data, final Map<String, String> headers, final String agent) throws IOException {
        final HttpURLConnection connection = makeConnection(url, method, data, headers, agent);
        final int responseCode = connection.getResponseCode();
        final InputStream stream = (responseCode >= 200 && responseCode < 300) ? connection.getInputStream() : connection.getErrorStream();
        String responseText;
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            final StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            responseText = response.toString();
        }
        return new HttpResponse(responseCode, responseText);
    }
    
    public static HttpResponse get(final String url, final Map<String, String> headers) throws IOException {
        return request(url, "GET", "", headers, "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36");
    }
    
    public static HttpResponse post(final String url, final String data, final Map<String, String> headers) throws IOException {
        return request(url, "POST", data, headers, "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36");
    }
    
    public static <T> T post(final String url, final Object data, final Class<T> responseType) throws IOException {
        final HttpResponse response = post(url, HttpUtils.GSON.toJson(data), Map.of("Content-Type", "application/json"));
        return HttpUtils.GSON.fromJson(response.text, responseType);
    }
    
    public static <T, E> Pair<T, E> postWithFallback(final String url, final Object data, final Class<T> successType, final Class<E> errorType) throws IOException {
        final HttpResponse response = post(url, HttpUtils.GSON.toJson(data), Map.of("Content-Type", "application/json"));
        if (response.code == 200) {
            return new Pair<T, E>(HttpUtils.GSON.fromJson(response.text, successType), null);
        }
        return new Pair<T, E>(null, HttpUtils.GSON.fromJson(response.text, errorType));
    }
    
    static {
        GSON = new GsonBuilder().create();
    }
    
    record Pair<T, E>(T first, E second) {}
    
    record HttpResponse(int code, String text) {}
}
