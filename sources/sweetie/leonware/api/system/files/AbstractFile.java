package sweetie.leonware.api.system.files;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.List;
import sweetie.leonware.api.system.backend.ClientInfo;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/system/files/AbstractFile.class */
public abstract class AbstractFile {
    private List<String> data = new ArrayList();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public abstract String fileName();

    private Path getFilePath() {
        return Path.of(ClientInfo.CONFIG_PATH_OTHER, fileName() + ".json");
    }

    public void save() {
        try {
            Files.createDirectories(Path.of(ClientInfo.CONFIG_PATH_OTHER, new String[0]), new FileAttribute[0]);
            FileWriter writer = new FileWriter(getFilePath().toFile());
            try {
                GSON.toJson(this.data, writer);
                writer.close();
            } finally {
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to save: " + String.valueOf(getFilePath()), e);
        }
    }

    public void load() {
        try {
            Files.createDirectories(Path.of(ClientInfo.CONFIG_PATH_OTHER, new String[0]), new FileAttribute[0]);
            File file = getFilePath().toFile();
            if (!file.exists()) {
                file.createNewFile();
                save();
                return;
            }
            FileReader reader = new FileReader(file);
            try {
                Type listType = new TypeToken<ArrayList<String>>(this) { // from class: sweetie.leonware.api.system.files.AbstractFile.1
                }.getType();
                this.data = (List) GSON.fromJson(reader, listType);
                if (this.data == null) {
                    this.data = new ArrayList();
                }
                reader.close();
            } finally {
            }
        } catch (IOException e) {
            this.data = new ArrayList();
        }
    }

    public void add(String value) {
        if (value != null && !value.trim().isEmpty() && !this.data.contains(value)) {
            this.data.add(value);
            save();
        }
    }

    public boolean remove(String value) {
        boolean b = this.data.remove(value);
        if (b) {
            save();
        }
        return b;
    }

    public List<String> getData() {
        return new ArrayList(this.data);
    }

    public void clear() {
        this.data.clear();
        save();
    }

    public boolean contains(String value) {
        return this.data.contains(value);
    }

    public int size() {
        return this.data.size();
    }
}
