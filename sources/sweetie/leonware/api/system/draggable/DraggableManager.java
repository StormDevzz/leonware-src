package sweetie.leonware.api.system.draggable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.Generated;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.system.backend.ClientInfo;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/system/draggable/DraggableManager.class */
public class DraggableManager {
    private static final DraggableManager instance = new DraggableManager();
    private final LinkedHashMap<String, Draggable> draggables = new LinkedHashMap<>();
    private final File CONFIG_DIR = new File(ClientInfo.CONFIG_PATH_OTHER + "/drags.json");
    private final Gson GSON = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();

    @Generated
    public static DraggableManager getInstance() {
        return instance;
    }

    @Generated
    public LinkedHashMap<String, Draggable> getDraggables() {
        return this.draggables;
    }

    public Draggable create(Module module, String name, float x, float y) {
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
                return;
            } catch (IOException ex) {
                ex.printStackTrace();
                return;
            }
        }
        System.err.println("File system closed. Could not save drag data.");
    }

    public void load() {
        Draggable currentDraggable;
        if (!this.CONFIG_DIR.exists()) {
            this.CONFIG_DIR.getParentFile().mkdirs();
            return;
        }
        try {
            String json = Files.readString(this.CONFIG_DIR.toPath());
            Map<String, Draggable> loadedDraggables = (Map) this.GSON.fromJson(json, new TypeToken<Map<String, Draggable>>(this) { // from class: sweetie.leonware.api.system.draggable.DraggableManager.1
            }.getType());
            if (loadedDraggables != null) {
                for (Map.Entry<String, Draggable> entry : loadedDraggables.entrySet()) {
                    String name = entry.getKey();
                    Draggable draggable = entry.getValue();
                    if (draggable != null && (currentDraggable = this.draggables.get(name)) != null) {
                        currentDraggable.setX(draggable.getX());
                        currentDraggable.setY(draggable.getY());
                        this.draggables.put(name, currentDraggable);
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
