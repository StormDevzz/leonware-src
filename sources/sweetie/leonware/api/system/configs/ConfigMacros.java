package sweetie.leonware.api.system.configs;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.List;
import lombok.Generated;
import sweetie.leonware.api.system.configs.MacroManager;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/system/configs/ConfigMacros.class */
public class ConfigMacros {
    private static final ConfigMacros instance = new ConfigMacros();
    private final GsonBuilder gson = new GsonBuilder().setPrettyPrinting();

    @Generated
    public static ConfigMacros getInstance() {
        return instance;
    }

    public void load(File file, List<MacroManager.Macro> macros) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return;
            }
        }
        try {
            FileReader reader = new FileReader(file);
            try {
                Type type = new TypeToken<List<MacroManager.Macro>>(this) { // from class: sweetie.leonware.api.system.configs.ConfigMacros.1
                }.getType();
                List<MacroManager.Macro> loaded = (List) this.gson.create().fromJson(reader, type);
                macros.clear();
                if (loaded != null) {
                    macros.addAll(loaded);
                }
                reader.close();
            } finally {
            }
        } catch (Exception e2) {
            System.out.println(e2.getMessage());
        }
    }

    public void save(File file, List<MacroManager.Macro> macros) {
        File parentDir = file.getParentFile();
        if (parentDir != null) {
            parentDir.mkdirs();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return;
            }
        }
        try {
            FileWriter writer = new FileWriter(file);
            try {
                this.gson.create().toJson(macros, writer);
                writer.close();
            } finally {
            }
        } catch (Exception e2) {
            System.out.println(e2.getMessage());
        }
    }
}
