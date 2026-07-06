// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.system.configs;

import lombok.Generated;
import java.util.ArrayList;
import sweetie.leonware.api.system.backend.ClientInfo;
import java.util.List;
import java.io.File;
import sweetie.leonware.api.system.interfaces.QuickImports;

public class MacroManager implements QuickImports
{
    private static final MacroManager instance;
    private final File file;
    private final List<Macro> macros;
    
    public MacroManager() {
        this.file = new File(ClientInfo.CONFIG_PATH_OTHER + "/macros.json");
        this.macros = new ArrayList<Macro>();
    }
    
    public void load() {
        ConfigMacros.getInstance().load(this.file, this.macros);
    }
    
    public void save() {
        ConfigMacros.getInstance().save(this.file, this.macros);
    }
    
    public void add(final String name, final String message, final int key) {
        this.macros.add(new Macro(name, message, key));
        this.save();
    }
    
    public void remove(final String name) {
        this.macros.removeIf(m -> m.getName().equalsIgnoreCase(name));
        this.save();
    }
    
    public boolean has(final String name) {
        return this.macros.stream().anyMatch(m -> m.getName().equalsIgnoreCase(name));
    }
    
    public void clear() {
        this.macros.clear();
        this.save();
    }
    
    public void onKeyPressed(final int key) {
        if (MacroManager.mc.field_1724 == null) {
            return;
        }
        this.macros.stream().filter(m -> m.getKey() == key).findFirst().ifPresent(m -> {
            if (m.message.startsWith("/")) {
                MacroManager.mc.field_1724.field_3944.method_45730(m.message.replace("/", ""));
            }
            else {
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
        return MacroManager.instance;
    }
    
    static {
        instance = new MacroManager();
    }
    
    public static final class Macro
    {
        private final String name;
        private final String message;
        private final int key;
        
        @Generated
        public Macro(final String name, final String message, final int key) {
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
        @Override
        public boolean equals(final Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof Macro)) {
                return false;
            }
            final Macro other = (Macro)o;
            if (this.getKey() != other.getKey()) {
                return false;
            }
            final Object this$name = this.getName();
            final Object other$name = other.getName();
            Label_0068: {
                if (this$name == null) {
                    if (other$name == null) {
                        break Label_0068;
                    }
                }
                else if (this$name.equals(other$name)) {
                    break Label_0068;
                }
                return false;
            }
            final Object this$message = this.getMessage();
            final Object other$message = other.getMessage();
            if (this$message == null) {
                if (other$message == null) {
                    return true;
                }
            }
            else if (this$message.equals(other$message)) {
                return true;
            }
            return false;
        }
        
        @Generated
        @Override
        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            result = result * 59 + this.getKey();
            final Object $name = this.getName();
            result = result * 59 + (($name == null) ? 43 : $name.hashCode());
            final Object $message = this.getMessage();
            result = result * 59 + (($message == null) ? 43 : $message.hashCode());
            return result;
        }
        
        @Generated
        @Override
        public String toString() {
            return "MacroManager.Macro(name=" + this.getName() + ", message=" + this.getMessage() + ", key=" + this.getKey();
        }
    }
}
