/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package sweetie.leonware.api.system.configs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
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

public class ConfigManager {
    private static final ConfigManager instance = new ConfigManager();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final Path CONFIG_DIR = Paths.get(ClientInfo.CONFIG_PATH_MAIN, new String[0]);

    private ConfigManager() {
        try {
            Files.createDirectories(this.CONFIG_DIR, new FileAttribute[0]);
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to create config dir", e);
        }
    }

    public List<String> getConfigsNames() {
        File configDir = new File(ClientInfo.CONFIG_PATH_MAIN);
        if (!configDir.exists()) {
            return Collections.emptyList();
        }
        File[] files = configDir.listFiles((dir, name) -> name.endsWith(".json"));
        if (files == null) {
            return Collections.emptyList();
        }
        return Arrays.stream(files).map(f -> f.getName().replace(".json", "")).collect(Collectors.toList());
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
            modulesJson.add(m.getName(), this.createConfigFromModule(m));
        }
        root.add("Modules", modulesJson);
        Path file = this.getConfigFile(name);
        try {
            if (!Files.exists(file, new LinkOption[0])) {
                Files.createFile(file, new FileAttribute[0]);
            }
            Files.writeString(file, (CharSequence)GSON.toJson(root), new OpenOption[0]);
        }
        catch (IOException e) {
            System.err.printf("Failed to save config %s: %s%n", name, e.getMessage());
        }
    }

    public void load(String name) {
        Path file = this.getConfigFile(name);
        if (!Files.exists(file, new LinkOption[0])) {
            return;
        }
        try {
            JsonObject root = GSON.fromJson(Files.readString(file), JsonObject.class);
            if (root.has("Prefix")) {
                CommandManager.getInstance().setPrefix(root.get("Prefix").getAsString());
            }
            if (!root.has("Modules")) {
                return;
            }
            JsonObject modulesJson = root.getAsJsonObject("Modules");
            modulesJson.entrySet().forEach(entry -> {
                Module module = ModuleManager.getInstance().getModules().stream().filter(m -> m.getName().equalsIgnoreCase((String)entry.getKey())).findFirst().orElse(null);
                if (module == null) {
                    System.err.println("Module not found for config: " + (String)entry.getKey());
                } else {
                    this.applyConfigToModule(module, ((JsonElement)entry.getValue()).getAsJsonObject());
                }
            });
        }
        catch (IOException e) {
            System.err.printf("Failed to load config %s: %s%n", name, e.getMessage());
        }
    }

    public void remove(String name) {
        try {
            Files.deleteIfExists(this.getConfigFile(name));
        }
        catch (IOException e) {
            System.err.printf("Failed to remove config %s: %s%n", name, e.getMessage());
        }
    }

    public boolean exists(String name) {
        return Files.exists(this.getConfigFile(name), new LinkOption[0]);
    }

    private JsonObject createConfigFromModule(Module module) {
        JsonObject json = new JsonObject();
        json.addProperty("enabled", module.isEnabled());
        json.addProperty("bind", module.getBind());
        JsonObject settings = new JsonObject();
        module.getSettings().forEach(s -> {
            JsonElement v = this.serializeSetting((Setting<?>)s);
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
                    this.deserializeSetting((Setting<?>)s, settings.get(s.getName()));
                }
            });
        }
    }

    private JsonElement serializeSetting(Setting<?> s) {
        Setting<?> setting = s;
        Objects.requireNonNull(setting);
        Setting<?> setting2 = setting;
        int n = 0;
        return switch (SwitchBootstraps.typeSwitch("typeSwitch", new Object[]{BooleanSetting.class, ModeSetting.class, SliderSetting.class, BindSetting.class, MultiBooleanSetting.class, ColorSetting.class}, setting2, n)) {
            case 0 -> {
                BooleanSetting b = (BooleanSetting)setting2;
                yield GSON.toJsonTree(b.getValue());
            }
            case 1 -> {
                ModeSetting m = (ModeSetting)setting2;
                yield GSON.toJsonTree(m.getValue());
            }
            case 2 -> {
                SliderSetting sl = (SliderSetting)setting2;
                yield GSON.toJsonTree(sl.getValue());
            }
            case 3 -> {
                BindSetting bi = (BindSetting)setting2;
                yield GSON.toJsonTree(bi.getValue());
            }
            case 4 -> {
                MultiBooleanSetting mb = (MultiBooleanSetting)setting2;
                JsonObject obj = new JsonObject();
                ((List)mb.getValue()).forEach(b -> obj.addProperty(b.getName(), (Boolean)b.getValue()));
                yield obj;
            }
            case 5 -> {
                ColorSetting c = (ColorSetting)setting2;
                Color col = (Color)c.getValue();
                JsonObject obj = new JsonObject();
                obj.addProperty("r", col.getRed());
                obj.addProperty("g", col.getGreen());
                obj.addProperty("b", col.getBlue());
                obj.addProperty("a", col.getAlpha());
                obj.addProperty("rgbMode", c.isRgbMode());
                obj.addProperty("uiMode", c.isUiMode());
                yield obj;
            }
            default -> null;
        };
    }

    private void deserializeSetting(Setting<?> s, JsonElement e) {
        try {
            Setting<?> setting = s;
            Objects.requireNonNull(setting);
            Setting<?> setting2 = setting;
            int n = 0;
            switch (SwitchBootstraps.typeSwitch("typeSwitch", new Object[]{BooleanSetting.class, ModeSetting.class, SliderSetting.class, BindSetting.class, MultiBooleanSetting.class, ColorSetting.class}, setting2, n)) {
                case 0: {
                    BooleanSetting b2 = (BooleanSetting)setting2;
                    b2.setValue(e.getAsBoolean());
                    break;
                }
                case 1: {
                    ModeSetting m = (ModeSetting)setting2;
                    m.setValue(e.getAsString());
                    break;
                }
                case 2: {
                    SliderSetting sl = (SliderSetting)setting2;
                    sl.setValue(Float.valueOf(e.getAsFloat()));
                    break;
                }
                case 3: {
                    BindSetting bi = (BindSetting)setting2;
                    bi.setValue(e.getAsInt());
                    break;
                }
                case 4: {
                    MultiBooleanSetting mb = (MultiBooleanSetting)setting2;
                    JsonObject obj = e.getAsJsonObject();
                    ((List)mb.getValue()).forEach(b -> {
                        if (obj.has(b.getName())) {
                            b.setValue(obj.get(b.getName()).getAsBoolean());
                        }
                    });
                    break;
                }
                case 5: {
                    ColorSetting c = (ColorSetting)setting2;
                    JsonObject obj = e.getAsJsonObject();
                    int r = obj.get("r").getAsInt();
                    int g = obj.get("g").getAsInt();
                    int b3 = obj.get("b").getAsInt();
                    int a = obj.get("a").getAsInt();
                    c.setValue(new Color(r, g, b3, a));
                    if (obj.has("rgbMode")) {
                        c.setRgbMode(obj.get("rgbMode").getAsBoolean());
                    }
                    if (obj.has("uiMode")) {
                        c.setUiMode(obj.get("uiMode").getAsBoolean());
                    }
                    break;
                }
            }
        }
        catch (Exception ex) {
            System.err.printf("Failed to deserialize %s: %s%n", s.getName(), ex.getMessage());
        }
    }

    @Generated
    public static ConfigManager getInstance() {
        return instance;
    }
}

