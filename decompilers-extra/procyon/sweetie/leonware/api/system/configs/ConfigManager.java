// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.system.configs;

import java.lang.invoke.CallSite;
import java.lang.reflect.UndeclaredThrowableException;
import java.lang.invoke.MethodHandle;
import java.lang.runtime.SwitchBootstraps;
import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandles;
import com.google.gson.GsonBuilder;
import java.util.Map;
import lombok.Generated;
import java.awt.Color;
import sweetie.leonware.api.module.setting.ColorSetting;
import sweetie.leonware.api.module.setting.MultiBooleanSetting;
import sweetie.leonware.api.module.setting.BindSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.BooleanSetting;
import java.util.Objects;
import sweetie.leonware.api.module.setting.Setting;
import java.util.Iterator;
import java.nio.file.OpenOption;
import java.nio.file.LinkOption;
import com.google.gson.JsonElement;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleManager;
import sweetie.leonware.api.command.CommandManager;
import com.google.gson.JsonObject;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.Collections;
import java.io.File;
import java.util.List;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.Paths;
import sweetie.leonware.api.system.backend.ClientInfo;
import java.nio.file.Path;
import com.google.gson.Gson;

public class ConfigManager
{
    private static final ConfigManager instance;
    private static final Gson GSON;
    private final Path CONFIG_DIR;
    
    private ConfigManager() {
        this.CONFIG_DIR = Paths.get(ClientInfo.CONFIG_PATH_MAIN, new String[0]);
        try {
            Files.createDirectories(this.CONFIG_DIR, (FileAttribute<?>[])new FileAttribute[0]);
        }
        catch (final IOException e) {
            throw new RuntimeException("Failed to create config dir", e);
        }
    }
    
    public List<String> getConfigsNames() {
        final File configDir = new File(ClientInfo.CONFIG_PATH_MAIN);
        if (!configDir.exists()) {
            return Collections.emptyList();
        }
        final File[] files = configDir.listFiles((dir, name) -> name.endsWith(".json"));
        if (files == null) {
            return Collections.emptyList();
        }
        return Arrays.stream(files).map(f -> f.getName().replace(".json", "")).collect((Collector<? super Object, ?, List<String>>)Collectors.toList());
    }
    
    private Path getConfigFile(final String name) {
        return this.CONFIG_DIR.resolve(name + ".json");
    }
    
    public void save(final String name) {
        final JsonObject root = new JsonObject();
        root.addProperty("Prefix", CommandManager.getInstance().getPrefix());
        final List<Module> modules = ModuleManager.getInstance().getModules();
        final JsonObject modulesJson = new JsonObject();
        for (final Module m : modules) {
            modulesJson.add(m.getName(), this.createConfigFromModule(m));
        }
        root.add("Modules", modulesJson);
        final Path file = this.getConfigFile(name);
        try {
            if (!Files.exists(file, new LinkOption[0])) {
                Files.createFile(file, (FileAttribute<?>[])new FileAttribute[0]);
            }
            Files.writeString(file, ConfigManager.GSON.toJson(root), new OpenOption[0]);
        }
        catch (final IOException e) {
            System.err.printf("Failed to save config %s: %s%n", name, e.getMessage());
        }
    }
    
    public void load(final String name) {
        final Path file = this.getConfigFile(name);
        if (!Files.exists(file, new LinkOption[0])) {
            return;
        }
        try {
            final JsonObject root = ConfigManager.GSON.fromJson(Files.readString(file), JsonObject.class);
            if (root.has("Prefix")) {
                CommandManager.getInstance().setPrefix(root.get("Prefix").getAsString());
            }
            if (!root.has("Modules")) {
                return;
            }
            final JsonObject modulesJson = root.getAsJsonObject("Modules");
            modulesJson.entrySet().forEach(entry -> {
                final Module module = ModuleManager.getInstance().getModules().stream().filter(m -> m.getName().equalsIgnoreCase(entry.getKey())).findFirst().orElse(null);
                if (module == null) {
                    System.err.println("Module not found for config: " + (String)entry.getKey());
                }
                else {
                    this.applyConfigToModule(module, entry.getValue().getAsJsonObject());
                }
            });
        }
        catch (final IOException e) {
            System.err.printf("Failed to load config %s: %s%n", name, e.getMessage());
        }
    }
    
    public void remove(final String name) {
        try {
            Files.deleteIfExists(this.getConfigFile(name));
        }
        catch (final IOException e) {
            System.err.printf("Failed to remove config %s: %s%n", name, e.getMessage());
        }
    }
    
    public boolean exists(final String name) {
        return Files.exists(this.getConfigFile(name), new LinkOption[0]);
    }
    
    private JsonObject createConfigFromModule(final Module module) {
        final JsonObject json = new JsonObject();
        json.addProperty("enabled", module.isEnabled());
        json.addProperty("bind", module.getBind());
        final JsonObject settings = new JsonObject();
        module.getSettings().forEach(s -> {
            final JsonElement v = this.serializeSetting(s);
            if (v != null) {
                settings.add(s.getName(), v);
            }
            return;
        });
        json.add("settings", settings);
        return json;
    }
    
    private void applyConfigToModule(final Module module, final JsonObject json) {
        if (json.has("enabled")) {
            module.setEnabled(json.get("enabled").getAsBoolean(), true);
        }
        if (json.has("bind")) {
            module.setBind(json.get("bind").getAsInt());
        }
        if (json.has("settings")) {
            final JsonObject settings = json.getAsJsonObject("settings");
            module.getSettings().forEach(s -> {
                if (settings.has(s.getName())) {
                    this.deserializeSetting(s, settings.get(s.getName()));
                }
            });
        }
    }
    
    private JsonElement serializeSetting(final Setting<?> s) {
        Objects.requireNonNull(s);
        return switch (/* invokedynamic(!) */ProcyonInvokeDynamicHelper_1.invoke(s, false)) {
            case 0 -> {
                final BooleanSetting b = (BooleanSetting)s;
                yield ConfigManager.GSON.toJsonTree(((Setting<Object>)b).getValue());
            }
            case 1 -> {
                final ModeSetting m = (ModeSetting)s;
                yield ConfigManager.GSON.toJsonTree(((Setting<Object>)m).getValue());
            }
            case 2 -> {
                final SliderSetting sl = (SliderSetting)s;
                yield ConfigManager.GSON.toJsonTree(((Setting<Object>)sl).getValue());
            }
            case 3 -> {
                final BindSetting bi = (BindSetting)s;
                yield ConfigManager.GSON.toJsonTree(((Setting<Object>)bi).getValue());
            }
            case 4 -> {
                final MultiBooleanSetting mb = (MultiBooleanSetting)s;
                final JsonObject obj = new JsonObject();
                final BooleanSetting b;
                ((Setting<List>)mb).getValue().forEach(b -> obj.addProperty(b.getName(), b.getValue()));
                yield obj;
            }
            case 5 -> {
                final ColorSetting c = (ColorSetting)s;
                final Color col = c.getValue();
                final JsonObject obj2 = new JsonObject();
                obj2.addProperty("r", col.getRed());
                obj2.addProperty("g", col.getGreen());
                obj2.addProperty("b", col.getBlue());
                obj2.addProperty("a", col.getAlpha());
                obj2.addProperty("rgbMode", c.isRgbMode());
                obj2.addProperty("uiMode", c.isUiMode());
                yield obj2;
            }
            default -> null;
        };
    }
    
    private void deserializeSetting(final Setting<?> s, final JsonElement e) {
        try {
            Objects.requireNonNull(s);
            switch (/* invokedynamic(!) */ProcyonInvokeDynamicHelper_2.invoke(s, false)) {
                case 0: {
                    final BooleanSetting b = (BooleanSetting)s;
                    b.setValue(e.getAsBoolean());
                    break;
                }
                case 1: {
                    final ModeSetting m = (ModeSetting)s;
                    m.setValue(e.getAsString());
                    break;
                }
                case 2: {
                    final SliderSetting sl = (SliderSetting)s;
                    sl.setValue(e.getAsFloat());
                    break;
                }
                case 3: {
                    final BindSetting bi = (BindSetting)s;
                    bi.setValue(e.getAsInt());
                    break;
                }
                case 4: {
                    final MultiBooleanSetting mb = (MultiBooleanSetting)s;
                    final JsonObject obj = e.getAsJsonObject();
                    final BooleanSetting b;
                    ((Setting<List>)mb).getValue().forEach(b -> {
                        if (obj.has(b.getName())) {
                            b.setValue(obj.get(b.getName()).getAsBoolean());
                        }
                        return;
                    });
                    break;
                }
                case 5: {
                    final ColorSetting c = (ColorSetting)s;
                    final JsonObject obj2 = e.getAsJsonObject();
                    final int r = obj2.get("r").getAsInt();
                    final int g = obj2.get("g").getAsInt();
                    final int b2 = obj2.get("b").getAsInt();
                    final int a = obj2.get("a").getAsInt();
                    c.setValue(new Color(r, g, b2, a));
                    if (obj2.has("rgbMode")) {
                        c.setRgbMode(obj2.get("rgbMode").getAsBoolean());
                    }
                    if (obj2.has("uiMode")) {
                        c.setUiMode(obj2.get("uiMode").getAsBoolean());
                    }
                    break;
                }
            }
        }
        catch (final Exception ex) {
            System.err.printf("Failed to deserialize %s: %s%n", s.getName(), ex.getMessage());
        }
    }
    
    @Generated
    public static ConfigManager getInstance() {
        return ConfigManager.instance;
    }
    
    static {
        instance = new ConfigManager();
        GSON = new GsonBuilder().setPrettyPrinting().create();
    }
    
    // This helper class was generated by Procyon to approximate the behavior of an
    // 'invokedynamic' instruction that it doesn't know how to interpret.
    private static final class ProcyonInvokeDynamicHelper_1
    {
        private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();
        private static MethodHandle handle;
        private static volatile int fence;
        
        private static MethodHandle handle() {
            final MethodHandle handle = ProcyonInvokeDynamicHelper_1.handle;
            if (handle != null)
                return handle;
            return ProcyonInvokeDynamicHelper_1.ensureHandle();
        }
        
        private static MethodHandle ensureHandle() {
            ProcyonInvokeDynamicHelper_1.fence = 0;
            MethodHandle handle = ProcyonInvokeDynamicHelper_1.handle;
            if (handle == null) {
                MethodHandles.Lookup lookup = ProcyonInvokeDynamicHelper_1.LOOKUP;
                try {
                    handle = ((CallSite)SwitchBootstraps.typeSwitch(lookup, "typeSwitch", MethodType.methodType(int.class, Object.class, int.class), BooleanSetting.class, ModeSetting.class, SliderSetting.class, BindSetting.class, MultiBooleanSetting.class, ColorSetting.class)).dynamicInvoker();
                }
                catch (Throwable t) {
                    throw new UndeclaredThrowableException(t);
                }
                ProcyonInvokeDynamicHelper_1.fence = 1;
                ProcyonInvokeDynamicHelper_1.handle = handle;
                ProcyonInvokeDynamicHelper_1.fence = 0;
            }
            return handle;
        }
        
        private static int invoke(Object p0, int p1) {
            try {
                return ProcyonInvokeDynamicHelper_1.handle().invokeExact(p0, p1);
            }
            catch (Throwable t) {
                throw new UndeclaredThrowableException(t);
            }
        }
    }
    
    // This helper class was generated by Procyon to approximate the behavior of an
    // 'invokedynamic' instruction that it doesn't know how to interpret.
    private static final class ProcyonInvokeDynamicHelper_2
    {
        private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();
        private static MethodHandle handle;
        private static volatile int fence;
        
        private static MethodHandle handle() {
            final MethodHandle handle = ProcyonInvokeDynamicHelper_2.handle;
            if (handle != null)
                return handle;
            return ProcyonInvokeDynamicHelper_2.ensureHandle();
        }
        
        private static MethodHandle ensureHandle() {
            ProcyonInvokeDynamicHelper_2.fence = 0;
            MethodHandle handle = ProcyonInvokeDynamicHelper_2.handle;
            if (handle == null) {
                MethodHandles.Lookup lookup = ProcyonInvokeDynamicHelper_2.LOOKUP;
                try {
                    handle = ((CallSite)SwitchBootstraps.typeSwitch(lookup, "typeSwitch", MethodType.methodType(int.class, Object.class, int.class), BooleanSetting.class, ModeSetting.class, SliderSetting.class, BindSetting.class, MultiBooleanSetting.class, ColorSetting.class)).dynamicInvoker();
                }
                catch (Throwable t) {
                    throw new UndeclaredThrowableException(t);
                }
                ProcyonInvokeDynamicHelper_2.fence = 1;
                ProcyonInvokeDynamicHelper_2.handle = handle;
                ProcyonInvokeDynamicHelper_2.fence = 0;
            }
            return handle;
        }
        
        private static int invoke(Object p0, int p1) {
            try {
                return ProcyonInvokeDynamicHelper_2.handle().invokeExact(p0, p1);
            }
            catch (Throwable t) {
                throw new UndeclaredThrowableException(t);
            }
        }
    }
}
