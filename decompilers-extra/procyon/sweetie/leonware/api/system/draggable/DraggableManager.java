// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.system.draggable;

import lombok.Generated;
import java.util.Iterator;
import com.google.gson.reflect.TypeToken;
import java.util.Map;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import sweetie.leonware.api.module.Module;
import com.google.gson.GsonBuilder;
import sweetie.leonware.api.system.backend.ClientInfo;
import com.google.gson.Gson;
import java.io.File;
import java.util.LinkedHashMap;

public class DraggableManager
{
    private static final DraggableManager instance;
    private final LinkedHashMap<String, Draggable> draggables;
    private final File CONFIG_DIR;
    private final Gson GSON;
    
    public DraggableManager() {
        this.draggables = new LinkedHashMap<String, Draggable>();
        this.CONFIG_DIR = new File(ClientInfo.CONFIG_PATH_OTHER + "/drags.json");
        this.GSON = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
    }
    
    public Draggable create(final Module module, final String name, final float x, final float y) {
        this.draggables.put(name, new Draggable(module, name, x, y));
        return this.draggables.get(name);
    }
    
    public void save() {
        if (!this.CONFIG_DIR.exists()) {
            this.CONFIG_DIR.getParentFile().mkdirs();
        }
        if (this.CONFIG_DIR.toPath().getFileSystem().isOpen()) {
            try {
                Files.writeString(this.CONFIG_DIR.toPath(), this.GSON.toJson(this.draggables), new OpenOption[0]);
            }
            catch (final IOException ex) {
                ex.printStackTrace();
            }
        }
        else {
            System.err.println("File system closed. Could not save drag data.");
        }
    }
    
    public void load() {
        if (!this.CONFIG_DIR.exists()) {
            this.CONFIG_DIR.getParentFile().mkdirs();
            return;
        }
        try {
            final String json = Files.readString(this.CONFIG_DIR.toPath());
            final Map<String, Draggable> loadedDraggables = this.GSON.fromJson(json, new TypeToken<Map<String, Draggable>>(this) {}.getType());
            if (loadedDraggables != null) {
                for (final Map.Entry<String, Draggable> entry : loadedDraggables.entrySet()) {
                    final String name = entry.getKey();
                    final Draggable draggable = entry.getValue();
                    if (draggable != null) {
                        final Draggable currentDraggable = this.draggables.get(name);
                        if (currentDraggable == null) {
                            continue;
                        }
                        currentDraggable.setX(draggable.getX());
                        currentDraggable.setY(draggable.getY());
                        this.draggables.put(name, currentDraggable);
                    }
                }
            }
        }
        catch (final IOException ex) {
            ex.printStackTrace();
        }
    }
    
    @Generated
    public static DraggableManager getInstance() {
        return DraggableManager.instance;
    }
    
    @Generated
    public LinkedHashMap<String, Draggable> getDraggables() {
        return this.draggables;
    }
    
    static {
        instance = new DraggableManager();
    }
}
