// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.system.files;

import lombok.Generated;
import java.io.IOException;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import net.minecraft.class_2960;
import java.io.InputStream;
import com.google.gson.Gson;
import sweetie.leonware.api.system.interfaces.QuickImports;

public final class FileUtil implements QuickImports
{
    private static final Gson GSON;
    
    public static InputStream getFromAssets(final String input) {
        return FileUtil.class.getResourceAsStream("/assets/" + "LeonWare".toLowerCase() + "/" + input);
    }
    
    public static class_2960 getImage(final String path) {
        return class_2960.method_60655("LeonWare".toLowerCase(), "images/" + path + ".png");
    }
    
    public static class_2960 getShader(final String name) {
        return class_2960.method_60655("LeonWare".toLowerCase(), "core/" + name);
    }
    
    public static <T> T fromJsonToInstance(final class_2960 identifier, final Class<T> clazz) {
        return FileUtil.GSON.fromJson(toString(identifier), clazz);
    }
    
    public static String toString(final class_2960 identifier) {
        return toString(identifier, "\n");
    }
    
    public static String toString(final class_2960 identifier, final String delimiter) {
        try (final InputStream inputStream = FileUtil.mc.method_1478().open(identifier);
             final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            return reader.lines().collect(Collectors.joining(delimiter));
        }
        catch (final IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    @Generated
    private FileUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    static {
        GSON = new Gson();
    }
}
