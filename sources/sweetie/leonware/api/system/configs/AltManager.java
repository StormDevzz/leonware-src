package sweetie.leonware.api.system.configs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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
import java.util.Objects;
import java.util.Random;
import lombok.Generated;
import sweetie.leonware.api.system.backend.ClientInfo;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/system/configs/AltManager.class */
public class AltManager {
    private static final int MAX_ALTS = 64;
    private static final AltManager instance = new AltManager();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final List<String> alts = new ArrayList();
    private int currentIndex = 0;
    private final Path FILE = Paths.get(ClientInfo.CONFIG_PATH_MAIN, new String[0]).getParent().resolve("alts.json");

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

    private AltManager() {
    }

    public String generateRandomNick() {
        Random rnd = new Random();
        int length = 9 + rnd.nextInt(4);
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            if (rnd.nextBoolean()) {
                sb.append("abcdefghijklmnopqrstuvwxyz".charAt(rnd.nextInt("abcdefghijklmnopqrstuvwxyz".length())));
            } else {
                sb.append("0123456789".charAt(rnd.nextInt("0123456789".length())));
            }
        }
        return sb.toString();
    }

    public void load() {
        if (Files.exists(this.FILE, new LinkOption[0])) {
            try {
                JsonObject root = (JsonObject) GSON.fromJson(Files.readString(this.FILE), JsonObject.class);
                if (root.has("alts")) {
                    this.alts.clear();
                    root.getAsJsonArray("alts").forEach(e -> {
                        this.alts.add(e.getAsString());
                    });
                }
                if (root.has("current")) {
                    this.currentIndex = root.get("current").getAsInt();
                }
            } catch (IOException e2) {
                System.err.println("Failed to load alts: " + e2.getMessage());
            }
        }
    }

    public void save() {
        try {
            JsonObject root = new JsonObject();
            JsonArray arr = new JsonArray();
            List<String> list = this.alts;
            Objects.requireNonNull(arr);
            list.forEach(arr::add);
            root.add("alts", arr);
            root.addProperty("current", Integer.valueOf(this.currentIndex));
            if (!Files.exists(this.FILE, new LinkOption[0])) {
                Files.createFile(this.FILE, new FileAttribute[0]);
            }
            Files.writeString(this.FILE, GSON.toJson((JsonElement) root), new OpenOption[0]);
        } catch (IOException e) {
            System.err.println("Failed to save alts: " + e.getMessage());
        }
    }

    public boolean addAlt(String nick) {
        if (!isValidNick(nick) || this.alts.size() >= 64 || this.alts.contains(nick)) {
            return false;
        }
        this.alts.add(nick);
        save();
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
        save();
    }

    public void setCurrentIndex(int index) {
        if (index < 0 || index >= this.alts.size()) {
            return;
        }
        this.currentIndex = index;
        save();
    }

    public String getCurrentAlt() {
        if (this.alts.isEmpty()) {
            return null;
        }
        return this.alts.get(this.currentIndex);
    }

    public static boolean isValidNick(String nick) {
        if (nick != null && nick.length() >= 3 && nick.length() <= 16) {
            return nick.matches("[a-zA-Z0-9_]+");
        }
        return false;
    }

    public static boolean isValidChar(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || ((c >= '0' && c <= '9') || c == '_');
    }
}
