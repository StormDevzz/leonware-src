package sweetie.leonware.api.system.configs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.SwitchBootstraps;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.Generated;
import org.newsclub.net.unix.AFVSOCKSocketAddress;
import sweetie.leonware.api.command.CommandManager;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleManager;
import sweetie.leonware.api.module.setting.BindSetting;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ColorSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.MultiBooleanSetting;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.system.backend.ClientInfo;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/system/configs/ConfigManager.class */
public class ConfigManager {
    private static final ConfigManager instance = new ConfigManager();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final Path CONFIG_DIR = Paths.get(ClientInfo.CONFIG_PATH_MAIN, new String[0]);

    @Generated
    public static ConfigManager getInstance() {
        return instance;
    }

    private ConfigManager() {
        try {
            Files.createDirectories(this.CONFIG_DIR, new FileAttribute[0]);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create config dir", e);
        }
    }

    public List<String> getConfigsNames() {
        File[] files;
        File configDir = new File(ClientInfo.CONFIG_PATH_MAIN);
        if (configDir.exists() && (files = configDir.listFiles((dir, name) -> {
            return name.endsWith(".json");
        })) != null) {
            return (List) Arrays.stream(files).map(f -> {
                return f.getName().replace(".json", "");
            }).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private Path getConfigFile(String name) {
        return this.CONFIG_DIR.resolve(name + ".json");
    }

    public void save(String name) {
        JsonObject root = new JsonObject();
        root.addProperty("Prefix", CommandManager.getInstance().getPrefix());
        List<Module> modules = ModuleManager.getInstance().getModules();
        JsonObject modulesJson = new JsonObject();
        for (Module m : modules) {
            modulesJson.add(m.getName(), createConfigFromModule(m));
        }
        root.add("Modules", modulesJson);
        Path file = getConfigFile(name);
        try {
            if (!Files.exists(file, new LinkOption[0])) {
                Files.createFile(file, new FileAttribute[0]);
            }
            Files.writeString(file, GSON.toJson((JsonElement) root), new OpenOption[0]);
        } catch (IOException e) {
            System.err.printf("Failed to save config %s: %s%n", name, e.getMessage());
        }
    }

    public void load(String name) {
        Path file = getConfigFile(name);
        if (Files.exists(file, new LinkOption[0])) {
            try {
                JsonObject root = (JsonObject) GSON.fromJson(Files.readString(file), JsonObject.class);
                if (root.has("Prefix")) {
                    CommandManager.getInstance().setPrefix(root.get("Prefix").getAsString());
                }
                if (root.has("Modules")) {
                    JsonObject modulesJson = root.getAsJsonObject("Modules");
                    modulesJson.entrySet().forEach(entry -> {
                        Module module = ModuleManager.getInstance().getModules().stream().filter(m -> {
                            return m.getName().equalsIgnoreCase((String) entry.getKey());
                        }).findFirst().orElse(null);
                        if (module == null) {
                            System.err.println("Module not found for config: " + ((String) entry.getKey()));
                        } else {
                            applyConfigToModule(module, ((JsonElement) entry.getValue()).getAsJsonObject());
                        }
                    });
                }
            } catch (IOException e) {
                System.err.printf("Failed to load config %s: %s%n", name, e.getMessage());
            }
        }
    }

    public void remove(String name) {
        try {
            Files.deleteIfExists(getConfigFile(name));
        } catch (IOException e) {
            System.err.printf("Failed to remove config %s: %s%n", name, e.getMessage());
        }
    }

    public boolean exists(String name) {
        return Files.exists(getConfigFile(name), new LinkOption[0]);
    }

    private JsonObject createConfigFromModule(Module module) {
        JsonObject json = new JsonObject();
        json.addProperty("enabled", Boolean.valueOf(module.isEnabled()));
        json.addProperty("bind", Integer.valueOf(module.getBind()));
        JsonObject settings = new JsonObject();
        module.getSettings().forEach(s -> {
            JsonElement v = serializeSetting(s);
            if (v != null) {
                settings.add(s.getName(), v);
            }
        });
        json.add("settings", settings);
        return json;
    }

    private void applyConfigToModule(Module module, JsonObject json) {
        if (json.has("enabled")) {
            module.setEnabled(json.get("enabled").getAsBoolean(), true);
        }
        if (json.has("bind")) {
            module.setBind(json.get("bind").getAsInt());
        }
        if (json.has("settings")) {
            JsonObject settings = json.getAsJsonObject("settings");
            module.getSettings().forEach(s -> {
                if (settings.has(s.getName())) {
                    deserializeSetting(s, settings.get(s.getName()));
                }
            });
        }
    }

    private JsonElement serializeSetting(Setting<?> s) {
        Objects.requireNonNull(s);
        switch ((int) SwitchBootstraps.typeSwitch(MethodHandles.lookup(), "typeSwitch", MethodType.methodType(Integer.TYPE, Object.class, Integer.TYPE), BooleanSetting.class, ModeSetting.class, SliderSetting.class, BindSetting.class, MultiBooleanSetting.class, ColorSetting.class).dynamicInvoker().invoke(s, 0) /* invoke-custom */) {
            case 0:
                BooleanSetting b = (BooleanSetting) s;
                return GSON.toJsonTree(b.getValue());
            case 1:
                ModeSetting m = (ModeSetting) s;
                return GSON.toJsonTree(m.getValue());
            case AFVSOCKSocketAddress.VMADDR_CID_HOST /* 2 */:
                SliderSetting sl = (SliderSetting) s;
                return GSON.toJsonTree(sl.getValue());
            case 3:
                BindSetting bi = (BindSetting) s;
                return GSON.toJsonTree(bi.getValue());
            case 4:
                MultiBooleanSetting mb = (MultiBooleanSetting) s;
                JsonObject obj = new JsonObject();
                mb.getValue().forEach(b2 -> {
                    obj.addProperty(b2.getName(), b2.getValue());
                });
                return obj;
            case 5:
                ColorSetting c = (ColorSetting) s;
                Color col = c.getValue();
                JsonObject obj2 = new JsonObject();
                obj2.addProperty("r", Integer.valueOf(col.getRed()));
                obj2.addProperty("g", Integer.valueOf(col.getGreen()));
                obj2.addProperty("b", Integer.valueOf(col.getBlue()));
                obj2.addProperty("a", Integer.valueOf(col.getAlpha()));
                obj2.addProperty("rgbMode", Boolean.valueOf(c.isRgbMode()));
                obj2.addProperty("uiMode", Boolean.valueOf(c.isUiMode()));
                return obj2;
            default:
                return null;
        }
    }

    private void deserializeSetting(Setting<?> s, JsonElement e) {
        try {
            Objects.requireNonNull(s);
            switch ((int) SwitchBootstraps.typeSwitch(MethodHandles.lookup(), "typeSwitch", MethodType.methodType(Integer.TYPE, Object.class, Integer.TYPE), BooleanSetting.class, ModeSetting.class, SliderSetting.class, BindSetting.class, MultiBooleanSetting.class, ColorSetting.class).dynamicInvoker().invoke(s, 0) /* invoke-custom */) {
                case 0:
                    BooleanSetting b = (BooleanSetting) s;
                    b.setValue(Boolean.valueOf(e.getAsBoolean()));
                    break;
                case 1:
                    ModeSetting m = (ModeSetting) s;
                    m.setValue(e.getAsString());
                    break;
                case AFVSOCKSocketAddress.VMADDR_CID_HOST /* 2 */:
                    SliderSetting sl = (SliderSetting) s;
                    sl.setValue(Float.valueOf(e.getAsFloat()));
                    break;
                case 3:
                    BindSetting bi = (BindSetting) s;
                    bi.setValue(Integer.valueOf(e.getAsInt()));
                    break;
                case 4:
                    MultiBooleanSetting mb = (MultiBooleanSetting) s;
                    JsonObject obj = e.getAsJsonObject();
                    mb.getValue().forEach(b2 -> {
                        if (obj.has(b2.getName())) {
                            b2.setValue(Boolean.valueOf(obj.get(b2.getName()).getAsBoolean()));
                        }
                    });
                    break;
                case 5:
                    ColorSetting c = (ColorSetting) s;
                    JsonObject obj2 = e.getAsJsonObject();
                    int r = obj2.get("r").getAsInt();
                    int g = obj2.get("g").getAsInt();
                    int b3 = obj2.get("b").getAsInt();
                    int a = obj2.get("a").getAsInt();
                    c.setValue(new Color(r, g, b3, a));
                    if (obj2.has("rgbMode")) {
                        c.setRgbMode(obj2.get("rgbMode").getAsBoolean());
                    }
                    if (obj2.has("uiMode")) {
                        c.setUiMode(obj2.get("uiMode").getAsBoolean());
                    }
                    break;
            }
        } catch (Exception ex) {
            System.err.printf("Failed to deserialize %s: %s%n", s.getName(), ex.getMessage());
        }
    }
}
