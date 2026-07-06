package sweetie.leonware.api.system.configs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import sweetie.leonware.api.system.backend.ClientInfo;
import sweetie.leonware.api.system.interfaces.QuickImports;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/system/configs/MacroManager.class */
public class MacroManager implements QuickImports {
    private static final MacroManager instance = new MacroManager();
    private final File file = new File(ClientInfo.CONFIG_PATH_OTHER + "/macros.json");
    private final List<Macro> macros = new ArrayList();

    @Generated
    public static MacroManager getInstance() {
        return instance;
    }

    @Generated
    public File getFile() {
        return this.file;
    }

    @Generated
    public List<Macro> getMacros() {
        return this.macros;
    }

    public void load() {
        ConfigMacros.getInstance().load(this.file, this.macros);
    }

    public void save() {
        ConfigMacros.getInstance().save(this.file, this.macros);
    }

    public void add(String name, String message, int key) {
        this.macros.add(new Macro(name, message, key));
        save();
    }

    public void remove(String name) {
        this.macros.removeIf(m -> {
            return m.getName().equalsIgnoreCase(name);
        });
        save();
    }

    public boolean has(String name) {
        return this.macros.stream().anyMatch(m -> {
            return m.getName().equalsIgnoreCase(name);
        });
    }

    public void clear() {
        this.macros.clear();
        save();
    }

    public void onKeyPressed(int key) {
        if (mc.field_1724 == null) {
            return;
        }
        this.macros.stream().filter(m -> {
            return m.getKey() == key;
        }).findFirst().ifPresent(m2 -> {
            if (m2.message.startsWith("/")) {
                mc.field_1724.field_3944.method_45730(m2.message.replace("/", ""));
            } else {
                mc.field_1724.field_3944.method_45729(m2.message);
            }
        });
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/system/configs/MacroManager$Macro.class */
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
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof Macro)) {
                return false;
            }
            Macro other = (Macro) o;
            if (getKey() != other.getKey()) {
                return false;
            }
            Object this$name = getName();
            Object other$name = other.getName();
            if (this$name == null) {
                if (other$name != null) {
                    return false;
                }
            } else if (!this$name.equals(other$name)) {
                return false;
            }
            Object this$message = getMessage();
            Object other$message = other.getMessage();
            return this$message == null ? other$message == null : this$message.equals(other$message);
        }

        @Generated
        public int hashCode() {
            int result = (1 * 59) + getKey();
            Object $name = getName();
            int result2 = (result * 59) + ($name == null ? 43 : $name.hashCode());
            Object $message = getMessage();
            return (result2 * 59) + ($message == null ? 43 : $message.hashCode());
        }

        @Generated
        public String toString() {
            return "MacroManager.Macro(name=" + getName() + ", message=" + getMessage() + ", key=" + getKey() + ")";
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
    }
}
