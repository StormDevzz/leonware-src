/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package sweetie.leonware.api.system.configs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import sweetie.leonware.api.system.backend.ClientInfo;
import sweetie.leonware.api.system.configs.ConfigMacros;
import sweetie.leonware.api.system.interfaces.QuickImports;

public class MacroManager
implements QuickImports {
    private static final MacroManager instance = new MacroManager();
    private final File file = new File(ClientInfo.CONFIG_PATH_OTHER + "/macros.json");
    private final List<Macro> macros = new ArrayList<Macro>();

    public void load() {
        ConfigMacros.getInstance().load(this.file, this.macros);
    }

    public void save() {
        ConfigMacros.getInstance().save(this.file, this.macros);
    }

    public void add(String name, String message, int key) {
        this.macros.add(new Macro(name, message, key));
        this.save();
    }

    public void remove(String name) {
        this.macros.removeIf(m -> m.getName().equalsIgnoreCase(name));
        this.save();
    }

    public boolean has(String name) {
        return this.macros.stream().anyMatch(m -> m.getName().equalsIgnoreCase(name));
    }

    public void clear() {
        this.macros.clear();
        this.save();
    }

    public void onKeyPressed(int key) {
        if (MacroManager.mc.field_1724 == null) {
            return;
        }
        this.macros.stream().filter(m -> m.getKey() == key).findFirst().ifPresent(m -> {
            if (m.message.startsWith("/")) {
                MacroManager.mc.field_1724.field_3944.method_45730(m.message.replace("/", ""));
            } else {
                MacroManager.mc.field_1724.field_3944.method_45729(m.message);
            }
        });
    }

    @Generated
    public File getFile() {
        return this.file;
    }

    @Generated
    public List<Macro> getMacros() {
        return this.macros;
    }

    @Generated
    public static MacroManager getInstance() {
        return instance;
    }

    public static final class Macro {
        private final String name;
        private final String message;
        private final int key;

        @Generated
        public Macro(String name, String message, int key) {
            this.name = name;
            this.message = message;
            this.key = key;
        }

        @Generated
        public String getName() {
            return this.name;
        }

        @Generated
        public String getMessage() {
            return this.message;
        }

        @Generated
        public int getKey() {
            return this.key;
        }

        @Generated
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof Macro)) {
                return false;
            }
            Macro other = (Macro)o;
            if (this.getKey() != other.getKey()) {
                return false;
            }
            String this$name = this.getName();
            String other$name = other.getName();
            if (this$name == null ? other$name != null : !this$name.equals(other$name)) {
                return false;
            }
            String this$message = this.getMessage();
            String other$message = other.getMessage();
            return !(this$message == null ? other$message != null : !this$message.equals(other$message));
        }

        @Generated
        public int hashCode() {
            int PRIME = 59;
            int result = 1;
            result = result * 59 + this.getKey();
            String $name = this.getName();
            result = result * 59 + ($name == null ? 43 : $name.hashCode());
            String $message = this.getMessage();
            result = result * 59 + ($message == null ? 43 : $message.hashCode());
            return result;
        }

        @Generated
        public String toString() {
            return "MacroManager.Macro(name=" + this.getName() + ", message=" + this.getMessage() + ", key=" + this.getKey() + ")";
        }
    }
}

