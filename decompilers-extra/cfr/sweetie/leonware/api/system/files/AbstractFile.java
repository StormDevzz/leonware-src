/*
 * Decompiled with CFR 0.152.
 */
package sweetie.leonware.api.system.files;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.List;
import sweetie.leonware.api.system.backend.ClientInfo;

public abstract class AbstractFile {
    private List<String> data = new ArrayList<String>();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public abstract String fileName();

    private Path getFilePath() {
        return Path.of(ClientInfo.CONFIG_PATH_OTHER, this.fileName() + ".json");
    }

    public void save() {
        try {
            Files.createDirectories(Path.of(ClientInfo.CONFIG_PATH_OTHER, new String[0]), new FileAttribute[0]);
            try (FileWriter writer = new FileWriter(this.getFilePath().toFile());){
                GSON.toJson(this.data, (Appendable)writer);
            }
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to save: " + String.valueOf(this.getFilePath()), e);
        }
    }

    public void load() {
        try {
            Files.createDirectories(Path.of(ClientInfo.CONFIG_PATH_OTHER, new String[0]), new FileAttribute[0]);
            File file = this.getFilePath().toFile();
            if (!file.exists()) {
                file.createNewFile();
                this.save();
                return;
            }
            try (FileReader reader = new FileReader(file);){
                Type listType = new TypeToken<ArrayList<String>>(this){}.getType();
                this.data = (List)GSON.fromJson((Reader)reader, listType);
                if (this.data == null) {
                    this.data = new ArrayList<String>();
                }
            }
        }
        catch (IOException e) {
            this.data = new ArrayList<String>();
        }
    }

    public void add(String value) {
        if (value != null && !value.trim().isEmpty() && !this.data.contains(value)) {
            this.data.add(value);
            this.save();
        }
    }

    public boolean remove(String value) {
        boolean b = this.data.remove(value);
        if (b) {
            this.save();
        }
        return b;
    }

    public List<String> getData() {
        return new ArrayList<String>(this.data);
    }

    public void clear() {
        this.data.clear();
        this.save();
    }

    public boolean contains(String value) {
        return this.data.contains(value);
    }

    public int size() {
        return this.data.size();
    }
}

