package sweetie.leonware.api.auth;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/auth/HttpUtils.class */
public class HttpUtils {
    private static final String DEFAULT_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36";
    private static final Gson GSON = new GsonBuilder().create();

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/auth/HttpUtils$Pair.class */
    public static final class Pair<T, E> extends Record {
        private final T first;
        private final E second;

        public Pair(T first, E second) {
            this.first = first;
            this.second = second;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Pair.class), Pair.class, "first;second", "FIELD:Lsweetie/leonware/api/auth/HttpUtils$Pair;->first:Ljava/lang/Object;", "FIELD:Lsweetie/leonware/api/auth/HttpUtils$Pair;->second:Ljava/lang/Object;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Pair.class), Pair.class, "first;second", "FIELD:Lsweetie/leonware/api/auth/HttpUtils$Pair;->first:Ljava/lang/Object;", "FIELD:Lsweetie/leonware/api/auth/HttpUtils$Pair;->second:Ljava/lang/Object;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Pair.class, Object.class), Pair.class, "first;second", "FIELD:Lsweetie/leonware/api/auth/HttpUtils$Pair;->first:Ljava/lang/Object;", "FIELD:Lsweetie/leonware/api/auth/HttpUtils$Pair;->second:Ljava/lang/Object;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public T first() {
            return this.first;
        }

        public E second() {
            return this.second;
        }
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/auth/HttpUtils$HttpResponse.class */
    public static final class HttpResponse extends Record {
        private final int code;
        private final String text;

        public HttpResponse(int code, String text) {
            this.code = code;
            this.text = text;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, HttpResponse.class), HttpResponse.class, "code;text", "FIELD:Lsweetie/leonware/api/auth/HttpUtils$HttpResponse;->code:I", "FIELD:Lsweetie/leonware/api/auth/HttpUtils$HttpResponse;->text:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, HttpResponse.class), HttpResponse.class, "code;text", "FIELD:Lsweetie/leonware/api/auth/HttpUtils$HttpResponse;->code:I", "FIELD:Lsweetie/leonware/api/auth/HttpUtils$HttpResponse;->text:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, HttpResponse.class, Object.class), HttpResponse.class, "code;text", "FIELD:Lsweetie/leonware/api/auth/HttpUtils$HttpResponse;->code:I", "FIELD:Lsweetie/leonware/api/auth/HttpUtils$HttpResponse;->text:Ljava/lang/String;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public int code() {
            return this.code;
        }

        public String text() {
            return this.text;
        }
    }

    private static HttpURLConnection makeConnection(String url, String method, String data, Map<String, String> headers, String agent) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod(method);
        connection.setConnectTimeout(2000);
        connection.setReadTimeout(10000);
        connection.setRequestProperty("User-Agent", agent);
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
        connection.setInstanceFollowRedirects(true);
        connection.setDoOutput(true);
        if (data != null && !data.isEmpty()) {
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            try {
                outputStream.writeBytes(data);
                outputStream.close();
            } catch (Throwable th) {
                try {
                    outputStream.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        }
        connection.connect();
        return connection;
    }

    public static HttpResponse request(String url, String method, String data, Map<String, String> headers, String agent) throws IOException {
        HttpURLConnection connection = makeConnection(url, method, data, headers, agent);
        int responseCode = connection.getResponseCode();
        InputStream stream = (responseCode < 200 || responseCode >= 300) ? connection.getErrorStream() : connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
        try {
            StringBuilder response = new StringBuilder();
            while (true) {
                String line = reader.readLine();
                if (line != null) {
                    response.append(line);
                } else {
                    String responseText = response.toString();
                    reader.close();
                    return new HttpResponse(responseCode, responseText);
                }
            }
        } catch (Throwable th) {
            try {
                reader.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    public static HttpResponse get(String url, Map<String, String> headers) throws IOException {
        return request(url, "GET", "", headers, DEFAULT_AGENT);
    }

    public static HttpResponse post(String url, String data, Map<String, String> headers) throws IOException {
        return request(url, "POST", data, headers, DEFAULT_AGENT);
    }

    public static <T> T post(String str, Object obj, Class<T> cls) throws IOException {
        return (T) GSON.fromJson(post(str, GSON.toJson(obj), (Map<String, String>) Map.of("Content-Type", "application/json")).text, (Class) cls);
    }

    public static <T, E> Pair<T, E> postWithFallback(String url, Object data, Class<T> successType, Class<E> errorType) throws IOException {
        HttpResponse response = post(url, GSON.toJson(data), (Map<String, String>) Map.of("Content-Type", "application/json"));
        if (response.code == 200) {
            return new Pair<>(GSON.fromJson(response.text, (Class) successType), null);
        }
        return new Pair<>(null, GSON.fromJson(response.text, (Class) errorType));
    }
}
