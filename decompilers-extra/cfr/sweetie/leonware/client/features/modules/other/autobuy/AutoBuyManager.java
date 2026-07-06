/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1657
 *  net.minecraft.class_1703
 *  net.minecraft.class_1707
 *  net.minecraft.class_1713
 *  net.minecraft.class_1735
 *  net.minecraft.class_1792$class_9635
 *  net.minecraft.class_1799
 *  net.minecraft.class_1802
 *  net.minecraft.class_1836
 *  net.minecraft.class_2561
 */
package sweetie.leonware.client.features.modules.other.autobuy;

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
import lombok.Generated;
import net.minecraft.class_1657;
import net.minecraft.class_1703;
import net.minecraft.class_1707;
import net.minecraft.class_1713;
import net.minecraft.class_1735;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1836;
import net.minecraft.class_2561;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.system.backend.ClientInfo;
import sweetie.leonware.api.system.interfaces.QuickImports;

public class AutoBuyManager
implements QuickImports {
    private static final AutoBuyManager instance = new AutoBuyManager();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String FILE_ENTRIES = "autobuy_entries";
    private static final String FILE_HISTORY = "autobuy_history";
    private static final long CONFIRM_TIMEOUT = 5000L;
    private boolean enabled = false;
    private long clickDelay = 300L;
    private long confirmDelay = 300L;
    private final List<BuyEntry> entries = new ArrayList<BuyEntry>();
    private final List<HistoryEntry> history = new ArrayList<HistoryEntry>();
    private long pendingClickAt = -1L;
    private long pendingConfirmAt = -1L;
    private long waitingConfirmSince = -1L;
    private int pendingSlotId = -1;
    private boolean waitingConfirm = false;
    private String pendingItemName = null;
    private long pendingItemPrice = -1L;
    private boolean debug = true;

    private AutoBuyManager() {
        this.loadEntries();
        this.loadHistory();
        UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(e -> this.onUpdate()));
    }

    private void onUpdate() {
        if (!this.enabled) {
            return;
        }
        if (AutoBuyManager.mc.field_1724 == null || AutoBuyManager.mc.field_1755 == null) {
            return;
        }
        class_1703 class_17032 = AutoBuyManager.mc.field_1724.field_7512;
        if (!(class_17032 instanceof class_1707)) {
            return;
        }
        class_1707 chest = (class_1707)class_17032;
        String title = AutoBuyManager.mc.field_1755.method_25440().getString();
        long now = System.currentTimeMillis();
        if (this.waitingConfirm) {
            if (this.waitingConfirmSince > 0L && now - this.waitingConfirmSince > 5000L) {
                if (this.debug) {
                    this.print("\u00a77[AutoBuy] \u00a7c\u0422\u0430\u0439\u043c\u0430\u0443\u0442 \u043e\u0436\u0438\u0434\u0430\u043d\u0438\u044f \u043e\u043a\u043d\u0430 \u043f\u043e\u0434\u0442\u0432\u0435\u0440\u0436\u0434\u0435\u043d\u0438\u044f. \u0421\u0431\u0440\u043e\u0441.");
                }
                this.resetState();
                return;
            }
            int buySlot = this.findBuyConfirmSlot(chest);
            if (buySlot < 0) {
                return;
            }
            if (this.pendingConfirmAt < 0L) {
                this.pendingConfirmAt = now + this.confirmDelay;
                if (this.debug) {
                    this.print("\u00a77[AutoBuy] \u00a7f\u041e\u043a\u043d\u043e \u043f\u043e\u0434\u0442\u0432\u0435\u0440\u0436\u0434\u0435\u043d\u0438\u044f \u043d\u0430\u0439\u0434\u0435\u043d\u043e (\u0441\u043b\u043e\u0442 " + buySlot + "). \u041d\u0430\u0436\u043c\u0443 \u0447\u0435\u0440\u0435\u0437 " + this.confirmDelay + " \u043c\u0441...");
                }
                return;
            }
            if (now >= this.pendingConfirmAt) {
                block28: {
                    if (this.debug) {
                        this.print("\u00a77[AutoBuy] \u00a7a\u041a\u043b\u0438\u043a\u0430\u0435\u043c '\u041a\u0423\u041f\u0418\u0422\u042c \u0417\u0410' \u0441\u043b\u043e\u0442 " + buySlot + "...");
                    }
                    try {
                        AutoBuyManager.mc.field_1761.method_2906(chest.field_7763, buySlot, 0, class_1713.field_7790, (class_1657)AutoBuyManager.mc.field_1724);
                        this.addHistory(this.pendingItemName != null ? this.pendingItemName : "?", this.pendingItemPrice);
                        this.print("\u00a77[AutoBuy] \u00a7a\u041f\u043e\u043a\u0443\u043f\u043a\u0430 \u0441\u043e\u0432\u0435\u0440\u0448\u0435\u043d\u0430: \u00a7f" + this.pendingItemName + " \u00a77\u0437\u0430 \u00a7f" + this.pendingItemPrice);
                    }
                    catch (Exception ex) {
                        if (!this.debug) break block28;
                        this.print("\u00a77[AutoBuy] \u00a7c\u041e\u0448\u0438\u0431\u043a\u0430 \u043a\u043b\u0438\u043a\u0430 \u043f\u043e\u0434\u0442\u0432\u0435\u0440\u0436\u0434\u0435\u043d\u0438\u044f: " + ex.getMessage());
                    }
                }
                this.resetState();
            }
            return;
        }
        if (!this.isAuctionTitle(title)) {
            return;
        }
        if (this.pendingClickAt > 0L && now < this.pendingClickAt) {
            return;
        }
        if (this.pendingSlotId >= 0 && this.pendingClickAt > 0L && now >= this.pendingClickAt) {
            class_1735 target = this.findSlotById(chest, this.pendingSlotId);
            if (target != null && !target.method_7677().method_7960()) {
                String nm = target.method_7677().method_7964().getString();
                long pr = this.getAuctionPrice(target.method_7677());
                if (this.debug) {
                    this.print("\u00a77[AutoBuy] \u00a7a\u041a\u043b\u0438\u043a\u0430\u0435\u043c \u043f\u043e \u043f\u0440\u0435\u0434\u043c\u0435\u0442\u0443: \u00a7f" + nm + " \u00a77\u0446\u0435\u043d\u0430: \u00a7f" + pr + " \u00a77(\u0441\u043b\u043e\u0442 " + this.pendingSlotId + ")");
                }
                try {
                    AutoBuyManager.mc.field_1761.method_2906(chest.field_7763, this.pendingSlotId, 0, class_1713.field_7790, (class_1657)AutoBuyManager.mc.field_1724);
                    this.pendingItemName = nm;
                    this.pendingItemPrice = pr;
                    this.waitingConfirm = true;
                    this.waitingConfirmSince = now;
                    if (this.debug) {
                        this.print("\u00a77[AutoBuy] \u00a77\u0416\u0434\u0451\u043c \u043e\u043a\u043d\u043e \u043f\u043e\u0434\u0442\u0432\u0435\u0440\u0436\u0434\u0435\u043d\u0438\u044f...");
                    }
                }
                catch (Exception ex) {
                    if (this.debug) {
                        this.print("\u00a77[AutoBuy] \u00a7c\u041e\u0448\u0438\u0431\u043a\u0430 \u043a\u043b\u0438\u043a\u0430 \u043f\u043e \u043f\u0440\u0435\u0434\u043c\u0435\u0442\u0443: " + ex.getMessage());
                    }
                    this.resetState();
                }
            } else {
                if (this.debug) {
                    this.print("\u00a77[AutoBuy] \u00a7e\u041f\u0440\u0435\u0434\u043c\u0435\u0442 \u043f\u0440\u043e\u043f\u0430\u043b \u0438\u0437 \u0441\u043b\u043e\u0442\u0430 " + this.pendingSlotId + " \u0434\u043e \u043a\u043b\u0438\u043a\u0430. \u0421\u0431\u0440\u043e\u0441.");
                }
                this.resetState();
            }
            this.pendingClickAt = -1L;
            this.pendingSlotId = -1;
            return;
        }
        if (this.pendingSlotId >= 0) {
            return;
        }
        for (class_1735 slot : chest.field_7761) {
            if (slot.field_7874 > 44 || slot.method_7677().method_7960()) continue;
            class_1799 stack = slot.method_7677();
            String itemName = stack.method_7964().getString();
            long price = this.getAuctionPrice(stack);
            if (price < 0L) continue;
            for (BuyEntry entry : this.entries) {
                if (!itemName.contains(entry.getNameContains()) || price > entry.getMaxPrice()) continue;
                if (this.debug) {
                    this.print("\u00a77[AutoBuy] \u00a7e\u041d\u0430\u0448\u043b\u0438: \u00a7f" + itemName + " \u00a77\u0437\u0430 \u00a7f" + price + " \u00a77(\u043c\u0430\u043a\u0441: " + entry.getMaxPrice() + "). \u041a\u043b\u0438\u043a \u0447\u0435\u0440\u0435\u0437 " + this.clickDelay + " \u043c\u0441...");
                }
                this.pendingSlotId = slot.field_7874;
                this.pendingClickAt = now + this.clickDelay;
                return;
            }
        }
    }

    private void resetState() {
        this.pendingClickAt = -1L;
        this.pendingConfirmAt = -1L;
        this.pendingSlotId = -1;
        this.waitingConfirm = false;
        this.waitingConfirmSince = -1L;
        this.pendingItemName = null;
        this.pendingItemPrice = -1L;
    }

    private boolean isAuctionTitle(String title) {
        return title.contains("\u0410\u0443\u043a\u0446\u0438\u043e\u043d") || title.contains("\u041f\u043e\u0438\u0441\u043a") || title.contains("\u041c\u0430\u0440\u043a\u0435\u0442") || title.contains("0B1L2o3v") || title.contains("\ua201\ua000\ua202\ua332\ua202\ua001") || title.contains("[\u2603] \u0410\u0443\u043a\u0446\u0438\u043e\u043d\u044b");
    }

    private int findBuyConfirmSlot(class_1707 chest) {
        int inventoryStart = chest.field_7761.size() - 36;
        for (class_1735 slot : chest.field_7761) {
            String name;
            class_1799 stack;
            if (slot.field_7874 >= inventoryStart || (stack = slot.method_7677()).method_7960() || stack.method_7909() != class_1802.field_8407 || !(name = stack.method_7964().getString().replaceAll("(?i)\u00a7[0-9a-fklmnor]", "").replace("\u00a7r", "").trim()).contains("\u041a\u0423\u041f\u0418\u0422\u042c \u0417\u0410")) continue;
            if (this.debug) {
                this.print("\u00a77[AutoBuy] \u00a77\u041d\u0430\u0439\u0434\u0435\u043d\u0430 \u043a\u043d\u043e\u043f\u043a\u0430 \u043f\u043e\u0434\u0442\u0432\u0435\u0440\u0436\u0434\u0435\u043d\u0438\u044f: \u00a7f\"" + name + "\" \u00a77\u0432 \u0441\u043b\u043e\u0442\u0435 " + slot.field_7874);
            }
            return slot.field_7874;
        }
        return -1;
    }

    private class_1735 findSlotById(class_1707 chest, int id) {
        for (class_1735 s : chest.field_7761) {
            if (s.field_7874 != id) continue;
            return s;
        }
        return null;
    }

    private long getAuctionPrice(class_1799 stack) {
        try {
            for (class_2561 text : stack.method_7950(class_1792.class_9635.field_51353, (class_1657)AutoBuyManager.mc.field_1724, (class_1836)class_1836.field_41070)) {
                String cleaned;
                String str = text.getString().replace("\u00a7r", "").replaceAll("(?i)\u00a7[0-9a-fklmnor]", "").replace("\u00a4", "").trim();
                if (!str.contains("\u0426\u0435\u043d\u0430 \u0432 \u0434\u043e\u043b\u043b\u0430\u0440\u0430\u0445:") || (cleaned = str.replaceAll("\u0426\u0435\u043d\u0430 \u0432 \u0434\u043e\u043b\u043b\u0430\u0440\u0430\u0445:", "").replaceAll("[\u25af\u2337\u2395\\s]", "").replaceAll("[^0-9]", "").trim()).isEmpty()) continue;
                try {
                    return Long.parseLong(cleaned);
                }
                catch (NumberFormatException numberFormatException) {
                }
            }
        }
        catch (Exception ex) {
            this.print("\u00a77[AutoBuy] \u00a7c\u041e\u0448\u0438\u0431\u043a\u0430 \u043f\u0430\u0440\u0441\u0438\u043d\u0433\u0430 \u0446\u0435\u043d\u044b: " + ex.getMessage());
        }
        return -1L;
    }

    public void addEntry(String nameContains, long maxPrice) {
        this.entries.removeIf(e -> e.getNameContains().equalsIgnoreCase(nameContains));
        this.entries.add(new BuyEntry(nameContains, maxPrice));
        this.saveEntries();
    }

    public boolean removeEntry(String nameContains) {
        boolean removed = this.entries.removeIf(e -> e.getNameContains().equalsIgnoreCase(nameContains));
        if (removed) {
            this.saveEntries();
        }
        return removed;
    }

    public void clearEntries() {
        this.entries.clear();
        this.saveEntries();
    }

    public void addHistory(String name, long price) {
        this.history.add(0, new HistoryEntry(name, price, System.currentTimeMillis()));
        if (this.history.size() > 50) {
            this.history.subList(50, this.history.size()).clear();
        }
        this.saveHistory();
    }

    private Path entriesPath() {
        return Path.of(ClientInfo.CONFIG_PATH_OTHER, "autobuy_entries.json");
    }

    private Path historyPath() {
        return Path.of(ClientInfo.CONFIG_PATH_OTHER, "autobuy_history.json");
    }

    private void saveEntries() {
        try {
            Files.createDirectories(Path.of(ClientInfo.CONFIG_PATH_OTHER, new String[0]), new FileAttribute[0]);
            try (FileWriter w = new FileWriter(this.entriesPath().toFile());){
                GSON.toJson(this.entries, (Appendable)w);
            }
        }
        catch (IOException ex) {
            if (AutoBuyManager.mc.field_1724 != null) {
                this.print("\u00a77[AutoBuy] \u00a7c\u041e\u0448\u0438\u0431\u043a\u0430 \u0441\u043e\u0445\u0440\u0430\u043d\u0435\u043d\u0438\u044f \u0441\u043f\u0438\u0441\u043a\u0430: " + ex.getMessage());
            }
            System.out.println("[AutoBuy] Error saving entries: " + ex.getMessage());
        }
    }

    private void loadEntries() {
        try {
            Files.createDirectories(Path.of(ClientInfo.CONFIG_PATH_OTHER, new String[0]), new FileAttribute[0]);
            File f = this.entriesPath().toFile();
            if (!f.exists()) {
                return;
            }
            try (FileReader r = new FileReader(f);){
                Type t = new TypeToken<ArrayList<BuyEntry>>(this){}.getType();
                List loaded = (List)GSON.fromJson((Reader)r, t);
                if (loaded != null) {
                    this.entries.addAll(loaded);
                }
            }
        }
        catch (Exception ex) {
            System.out.println("[AutoBuy] Error loading entries: " + ex.getMessage());
        }
    }

    private void saveHistory() {
        try {
            Files.createDirectories(Path.of(ClientInfo.CONFIG_PATH_OTHER, new String[0]), new FileAttribute[0]);
            try (FileWriter w = new FileWriter(this.historyPath().toFile());){
                GSON.toJson(this.history, (Appendable)w);
            }
        }
        catch (IOException ex) {
            if (AutoBuyManager.mc.field_1724 != null) {
                this.print("\u00a77[AutoBuy] \u00a7c\u041e\u0448\u0438\u0431\u043a\u0430 \u0441\u043e\u0445\u0440\u0430\u043d\u0435\u043d\u0438\u044f \u0438\u0441\u0442\u043e\u0440\u0438\u0438: " + ex.getMessage());
            }
            System.out.println("[AutoBuy] Error saving history: " + ex.getMessage());
        }
    }

    private void loadHistory() {
        try {
            Files.createDirectories(Path.of(ClientInfo.CONFIG_PATH_OTHER, new String[0]), new FileAttribute[0]);
            File f = this.historyPath().toFile();
            if (!f.exists()) {
                return;
            }
            try (FileReader r = new FileReader(f);){
                Type t = new TypeToken<ArrayList<HistoryEntry>>(this){}.getType();
                List loaded = (List)GSON.fromJson((Reader)r, t);
                if (loaded != null) {
                    this.history.addAll(loaded);
                }
            }
        }
        catch (Exception ex) {
            System.out.println("[AutoBuy] Error loading history: " + ex.getMessage());
        }
    }

    @Generated
    public static AutoBuyManager getInstance() {
        return instance;
    }

    @Generated
    public boolean isEnabled() {
        return this.enabled;
    }

    @Generated
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Generated
    public long getClickDelay() {
        return this.clickDelay;
    }

    @Generated
    public void setClickDelay(long clickDelay) {
        this.clickDelay = clickDelay;
    }

    @Generated
    public long getConfirmDelay() {
        return this.confirmDelay;
    }

    @Generated
    public void setConfirmDelay(long confirmDelay) {
        this.confirmDelay = confirmDelay;
    }

    @Generated
    public List<BuyEntry> getEntries() {
        return this.entries;
    }

    @Generated
    public List<HistoryEntry> getHistory() {
        return this.history;
    }

    @Generated
    public boolean isDebug() {
        return this.debug;
    }

    @Generated
    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public static class BuyEntry {
        private final String nameContains;
        private final long maxPrice;

        public BuyEntry(String name, long price) {
            this.nameContains = name;
            this.maxPrice = price;
        }

        @Generated
        public String getNameContains() {
            return this.nameContains;
        }

        @Generated
        public long getMaxPrice() {
            return this.maxPrice;
        }
    }

    public static class HistoryEntry {
        private final String name;
        private final long price;
        private final long timestamp;

        public HistoryEntry(String name, long price, long timestamp) {
            this.name = name;
            this.price = price;
            this.timestamp = timestamp;
        }

        @Generated
        public String getName() {
            return this.name;
        }

        @Generated
        public long getPrice() {
            return this.price;
        }

        @Generated
        public long getTimestamp() {
            return this.timestamp;
        }
    }
}

