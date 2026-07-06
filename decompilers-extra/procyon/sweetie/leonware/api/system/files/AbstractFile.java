// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.system.files;

import com.google.gson.GsonBuilder;
import java.util.Collection;
import java.lang.reflect.Type;
import java.io.File;
import java.io.Reader;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;
import sweetie.leonware.api.system.backend.ClientInfo;
import java.nio.file.Path;
import java.util.ArrayList;
import com.google.gson.Gson;
import java.util.List;

public abstract class AbstractFile
{
    private List<String> data;
    private static final Gson GSON;
    
    public AbstractFile() {
        this.data = new ArrayList<String>();
    }
    
    public abstract String fileName();
    
    private Path getFilePath() {
        return Path.of(ClientInfo.CONFIG_PATH_OTHER, this.fileName() + ".json");
    }
    
    public void save() {
        try {
            Files.createDirectories(Path.of(ClientInfo.CONFIG_PATH_OTHER, new String[0]), (FileAttribute<?>[])new FileAttribute[0]);
            try (final FileWriter writer = new FileWriter(this.getFilePath().toFile())) {
                AbstractFile.GSON.toJson(this.data, writer);
            }
        }
        catch (final IOException e) {
            throw new RuntimeException("Failed to save: " + String.valueOf(this.getFilePath()), (Throwable)e);
        }
    }
    
    public void load() {
        try {
            Files.createDirectories(Path.of(ClientInfo.CONFIG_PATH_OTHER, new String[0]), (FileAttribute<?>[])new FileAttribute[0]);
            final File file = this.getFilePath().toFile();
            if (!file.exists()) {
                file.createNewFile();
                this.save();
                return;
            }
            try (final FileReader reader = new FileReader(file)) {
                final Type listType = new TypeToken<ArrayList<String>>(this) {}.getType();
                this.data = AbstractFile.GSON.fromJson(reader, listType);
                if (this.data == null) {
                    this.data = new ArrayList<String>();
                }
            }
        }
        catch (final IOException e) {
            this.data = new ArrayList<String>();
        }
    }
    
    public void add(final String value) {
        if (value != null && !value.trim().isEmpty() && !this.data.contains(value)) {
            this.data.add(value);
            this.save();
        }
    }
    
    public boolean remove(final String value) {
        final boolean b = this.data.remove(value);
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
    
    public boolean contains(final String value) {
        return this.data.contains(value);
    }
    
    public int size() {
        return this.data.size();
    }
    
    static {
        GSON = new GsonBuilder().setPrettyPrinting().create();
    }
}
