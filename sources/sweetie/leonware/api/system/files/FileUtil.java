package sweetie.leonware.api.system.files;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;
import lombok.Generated;
import net.minecraft.class_2960;
import sweetie.leonware.api.system.backend.ClientInfo;
import sweetie.leonware.api.system.interfaces.QuickImports;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/system/files/FileUtil.class */
public final class FileUtil implements QuickImports {
    private static final Gson GSON = new Gson();

    @Generated
    private FileUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static InputStream getFromAssets(String input) {
        return FileUtil.class.getResourceAsStream("/assets/" + ClientInfo.NAME.toLowerCase() + "/" + input);
    }

    public static class_2960 getImage(String path) {
        return class_2960.method_60655(ClientInfo.NAME.toLowerCase(), "images/" + path + ".png");
    }

    public static class_2960 getShader(String name) {
        return class_2960.method_60655(ClientInfo.NAME.toLowerCase(), "core/" + name);
    }

    public static <T> T fromJsonToInstance(class_2960 class_2960Var, Class<T> cls) {
        return (T) GSON.fromJson(toString(class_2960Var), (Class) cls);
    }

    public static String toString(class_2960 identifier) {
        return toString(identifier, "\n");
    }

    public static String toString(class_2960 identifier, String delimiter) {
        try {
            InputStream inputStream = mc.method_1478().open(identifier);
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                try {
                    String str = (String) reader.lines().collect(Collectors.joining(delimiter));
                    reader.close();
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    return str;
                } catch (Throwable th) {
                    try {
                        reader.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                    throw th;
                }
            } finally {
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
