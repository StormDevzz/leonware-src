/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_2960
 */
package sweetie.leonware.api.system.files;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;
import lombok.Generated;
import net.minecraft.class_2960;
import sweetie.leonware.api.system.interfaces.QuickImports;

public final class FileUtil
implements QuickImports {
    private static final Gson GSON = new Gson();

    public static InputStream getFromAssets(String input) {
        return FileUtil.class.getResourceAsStream("/assets/" + "LeonWare".toLowerCase() + "/" + input);
    }

    public static class_2960 getImage(String path) {
        return class_2960.method_60655((String)"LeonWare".toLowerCase(), (String)("images/" + path + ".png"));
    }

    public static class_2960 getShader(String name) {
        return class_2960.method_60655((String)"LeonWare".toLowerCase(), (String)("core/" + name));
    }

    public static <T> T fromJsonToInstance(class_2960 identifier, Class<T> clazz) {
        return GSON.fromJson(FileUtil.toString(identifier), clazz);
    }

    public static String toString(class_2960 identifier) {
        return FileUtil.toString(identifier, "\n");
    }

    /*
     * Enabled aggressive exception aggregation
     */
    public static String toString(class_2960 identifier, String delimiter) {
        try (InputStream inputStream = mc.method_1478().open(identifier);){
            String string;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));){
                string = reader.lines().collect(Collectors.joining(delimiter));
            }
            return string;
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Generated
    private FileUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

