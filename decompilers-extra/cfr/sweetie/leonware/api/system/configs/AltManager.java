/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package sweetie.leonware.api.system.configs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import lombok.Generated;
import sweetie.leonware.api.system.backend.ClientInfo;

public class AltManager {
    private static final AltManager instance = new AltManager();
    private static final int MAX_ALTS = 64;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final Path FILE;
    private final List<String> alts = new ArrayList<String>();
    private int currentIndex = 0;

    private AltManager() {
        this.FILE = Paths.get(ClientInfo.CONFIG_PATH_MAIN, new String[0]).getParent().resolve("alts.json");
    }

    public String generateRandomNick() {
        Random rnd = new Random();
        String letters = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        int length = 9 + rnd.nextInt(4);
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; ++i) {
            if (rnd.nextBoolean()) {
                sb.append(letters.charAt(rnd.nextInt(letters.length())));
                continue;
            }
            sb.append(digits.charAt(rnd.nextInt(digits.length())));
        }
        return sb.toString();
    }

    public void load() {
        if (!Files.exists(this.FILE, new LinkOption[0])) {
            return;
        }
        try {
            JsonObject root = GSON.fromJson(Files.readString(this.FILE), JsonObject.class);
            if (root.has("alts")) {
                this.alts.clear();
                root.getAsJsonArray("alts").forEach(e -> this.alts.add(e.getAsString()));
            }
            if (root.has("current")) {
                this.currentIndex = root.get("current").getAsInt();
            }
        }
        catch (IOException e2) {
            System.err.println("Failed to load alts: " + e2.getMessage());
        }
    }

    public void save() {
        try {
            JsonObject root = new JsonObject();
            JsonArray arr = new JsonArray();
            this.alts.forEach(arr::add);
            root.add("alts", arr);
            root.addProperty("current", this.currentIndex);
            if (!Files.exists(this.FILE, new LinkOption[0])) {
                Files.createFile(this.FILE, new FileAttribute[0]);
            }
            Files.writeString(this.FILE, (CharSequence)GSON.toJson(root), new OpenOption[0]);
        }
        catch (IOException e) {
            System.err.println("Failed to save alts: " + e.getMessage());
        }
    }

    public boolean addAlt(String nick) {
        if (!AltManager.isValidNick(nick)) {
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

    public void removeAlt(int index) {
        if (index < 0 || index >= this.alts.size()) {
            return;
        }
        this.alts.remove(index);
        if (this.currentIndex >= this.alts.size()) {
            this.currentIndex = Math.max(0, this.alts.size() - 1);
        }
        this.save();
    }

    public void setCurrentIndex(int index) {
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

    public static boolean isValidNick(String nick) {
        if (nick == null) {
            return false;
        }
        if (nick.length() < 3 || nick.length() > 16) {
            return false;
        }
        return nick.matches("[a-zA-Z0-9_]+");
    }

    public static boolean isValidChar(char c) {
        return c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c >= '0' && c <= '9' || c == '_';
    }

    @Generated
    public static AltManager getInstance() {
        return instance;
    }

    @Generated
    public List<String> getAlts() {
        return this.alts;
    }

    @Generated
    public int getCurrentIndex() {
        return this.currentIndex;
    }
}

