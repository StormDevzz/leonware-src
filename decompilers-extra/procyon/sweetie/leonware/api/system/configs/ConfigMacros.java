// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.system.configs;

import lombok.Generated;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.Collection;
import java.io.Reader;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.util.List;
import java.io.File;
import com.google.gson.GsonBuilder;

public class ConfigMacros
{
    private static final ConfigMacros instance;
    private final GsonBuilder gson;
    
    public ConfigMacros() {
        this.gson = new GsonBuilder().setPrettyPrinting();
    }
    
    public void load(final File file, final List<MacroManager.Macro> macros) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            }
            catch (final Exception e) {
                System.out.println(e.getMessage());
                return;
            }
        }
        try (final FileReader reader = new FileReader(file)) {
            final Type type = new TypeToken<List<MacroManager.Macro>>(this) {}.getType();
            final List<MacroManager.Macro> loaded = this.gson.create().fromJson(reader, type);
            macros.clear();
            if (loaded != null) {
                macros.addAll(loaded);
            }
        }
        catch (final Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    public void save(final File file, final List<MacroManager.Macro> macros) {
        final File parentDir = file.getParentFile();
        if (parentDir != null) {
            parentDir.mkdirs();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            }
            catch (final Exception e) {
                System.out.println(e.getMessage());
                return;
            }
        }
        try (final FileWriter writer = new FileWriter(file)) {
            this.gson.create().toJson(macros, writer);
        }
        catch (final Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    @Generated
    public static ConfigMacros getInstance() {
        return ConfigMacros.instance;
    }
    
    static {
        instance = new ConfigMacros();
    }
}
