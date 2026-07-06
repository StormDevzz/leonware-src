// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.system.configs;

import com.google.gson.GsonBuilder;
import lombok.Generated;
import java.nio.file.OpenOption;
import java.nio.file.attribute.FileAttribute;
import com.google.gson.JsonElement;
import java.util.function.Consumer;
import java.util.Objects;
import com.google.gson.JsonArray;
import java.io.IOException;
import com.google.gson.JsonObject;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.util.Random;
import java.nio.file.Paths;
import sweetie.leonware.api.system.backend.ClientInfo;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Path;
import com.google.gson.Gson;

public class AltManager
{
    private static final AltManager instance;
    private static final int MAX_ALTS = 64;
    private static final Gson GSON;
    private final Path FILE;
    private final List<String> alts;
    private int currentIndex;
    
    private AltManager() {
        this.alts = new ArrayList<String>();
        this.currentIndex = 0;
        this.FILE = Paths.get(ClientInfo.CONFIG_PATH_MAIN, new String[0]).getParent().resolve("alts.json");
    }
    
    public String generateRandomNick() {
        final Random rnd = new Random();
        final String letters = "abcdefghijklmnopqrstuvwxyz";
        final String digits = "0123456789";
        final int length = 9 + rnd.nextInt(4);
        final StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; ++i) {
            if (rnd.nextBoolean()) {
                sb.append(letters.charAt(rnd.nextInt(letters.length())));
            }
            else {
                sb.append(digits.charAt(rnd.nextInt(digits.length())));
            }
        }
        return sb.toString();
    }
    
    public void load() {
        if (!Files.exists(this.FILE, new LinkOption[0])) {
            return;
        }
        try {
            final JsonObject root = AltManager.GSON.fromJson(Files.readString(this.FILE), JsonObject.class);
            if (root.has("alts")) {
                this.alts.clear();
                final IOException e;
                root.getAsJsonArray("alts").forEach(e -> this.alts.add(e.getAsString()));
            }
            if (root.has("current")) {
                this.currentIndex = root.get("current").getAsInt();
            }
        }
        catch (final IOException e) {
            System.err.println("Failed to load alts: " + e.getMessage());
        }
    }
    
    public void save() {
        try {
            final JsonObject root = new JsonObject();
            final JsonArray arr = new JsonArray();
            final List<String> alts = this.alts;
            final JsonArray obj = arr;
            Objects.requireNonNull(obj);
            alts.forEach(obj::add);
            root.add("alts", arr);
            root.addProperty("current", this.currentIndex);
            if (!Files.exists(this.FILE, new LinkOption[0])) {
                Files.createFile(this.FILE, (FileAttribute<?>[])new FileAttribute[0]);
            }
            Files.writeString(this.FILE, AltManager.GSON.toJson(root), new OpenOption[0]);
        }
        catch (final IOException e) {
            System.err.println("Failed to save alts: " + e.getMessage());
        }
    }
    
    public boolean addAlt(final String nick) {
        if (!isValidNick(nick)) {
            return false;
        }
        if (this.alts.size() >= 64) {
            return false;
        }
        if (this.alts.contains(nick)) {
            return false;
        }
        this.alts.add(nick);
        this.save();
        return true;
    }
    
    public void removeAlt(final int index) {
        if (index < 0 || index >= this.alts.size()) {
            return;
        }
        this.alts.remove(index);
        if (this.currentIndex >= this.alts.size()) {
            this.currentIndex = Math.max(0, this.alts.size() - 1);
        }
        this.save();
    }
    
    public void setCurrentIndex(final int index) {
        if (index < 0 || index >= this.alts.size()) {
            return;
        }
        this.currentIndex = index;
        this.save();
    }
    
    public String getCurrentAlt() {
        if (this.alts.isEmpty()) {
            return null;
        }
        return this.alts.get(this.currentIndex);
    }
    
    public static boolean isValidNick(final String nick) {
        return nick != null && nick.length() >= 3 && nick.length() <= 16 && nick.matches("[a-zA-Z0-9_]+");
    }
    
    public static boolean isValidChar(final char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || c == '_';
    }
    
    @Generated
    public static AltManager getInstance() {
        return AltManager.instance;
    }
    
    @Generated
    public List<String> getAlts() {
        return this.alts;
    }
    
    @Generated
    public int getCurrentIndex() {
        return this.currentIndex;
    }
    
    static {
        instance = new AltManager();
        GSON = new GsonBuilder().setPrettyPrinting().create();
    }
}
