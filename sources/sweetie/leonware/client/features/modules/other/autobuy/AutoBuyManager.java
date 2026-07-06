package sweetie.leonware.client.features.modules.other.autobuy;

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
import lombok.Generated;
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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/other/autobuy/AutoBuyManager.class */
public class AutoBuyManager implements QuickImports {
    private static final AutoBuyManager instance = new AutoBuyManager();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String FILE_ENTRIES = "autobuy_entries";
    private static final String FILE_HISTORY = "autobuy_history";
    private static final long CONFIRM_TIMEOUT = 5000;
    private boolean enabled = false;
    private long clickDelay = 300;
    private long confirmDelay = 300;
    private final List<BuyEntry> entries = new ArrayList();
    private final List<HistoryEntry> history = new ArrayList();
    private long pendingClickAt = -1;
    private long pendingConfirmAt = -1;
    private long waitingConfirmSince = -1;
    private int pendingSlotId = -1;
    private boolean waitingConfirm = false;
    private String pendingItemName = null;
    private long pendingItemPrice = -1;
    private boolean debug = true;

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

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/other/autobuy/AutoBuyManager$BuyEntry.class */
    public static class BuyEntry {
        private final String nameContains;
        private final long maxPrice;

        @Generated
        public String getNameContains() {
            return this.nameContains;
        }

        @Generated
        public long getMaxPrice() {
            return this.maxPrice;
        }

        public BuyEntry(String name, long price) {
            this.nameContains = name;
            this.maxPrice = price;
        }
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/other/autobuy/AutoBuyManager$HistoryEntry.class */
    public static class HistoryEntry {
        private final String name;
        private final long price;
        private final long timestamp;

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

        public HistoryEntry(String name, long price, long timestamp) {
            this.name = name;
            this.price = price;
            this.timestamp = timestamp;
        }
    }

    private AutoBuyManager() {
        loadEntries();
        loadHistory();
        UpdateEvent.getInstance().subscribe(new Listener(e -> {
            onUpdate();
        }));
    }

    private void onUpdate() {
        if (!this.enabled || mc.field_1724 == null || mc.field_1755 == null) {
            return;
        }
        class_1703 class_1703Var = mc.field_1724.field_7512;
        if (class_1703Var instanceof class_1707) {
            class_1707 chest = (class_1707) class_1703Var;
            String title = mc.field_1755.method_25440().getString();
            long now = System.currentTimeMillis();
            if (this.waitingConfirm) {
                if (this.waitingConfirmSince > 0 && now - this.waitingConfirmSince > CONFIRM_TIMEOUT) {
                    if (this.debug) {
                        print("§7[AutoBuy] §cТаймаут ожидания окна подтверждения. Сброс.");
                    }
                    resetState();
                    return;
                }
                int buySlot = findBuyConfirmSlot(chest);
                if (buySlot < 0) {
                    return;
                }
                if (this.pendingConfirmAt < 0) {
                    this.pendingConfirmAt = now + this.confirmDelay;
                    if (this.debug) {
                        print("§7[AutoBuy] §fОкно подтверждения найдено (слот " + buySlot + "). Нажму через " + this.confirmDelay + " мс...");
                        return;
                    }
                    return;
                }
                if (now >= this.pendingConfirmAt) {
                    if (this.debug) {
                        print("§7[AutoBuy] §aКликаем 'КУПИТЬ ЗА' слот " + buySlot + "...");
                    }
                    try {
                        mc.field_1761.method_2906(chest.field_7763, buySlot, 0, class_1713.field_7790, mc.field_1724);
                        addHistory(this.pendingItemName != null ? this.pendingItemName : "?", this.pendingItemPrice);
                        print("§7[AutoBuy] §aПокупка совершена: §f" + this.pendingItemName + " §7за §f" + this.pendingItemPrice);
                    } catch (Exception ex) {
                        if (this.debug) {
                            print("§7[AutoBuy] §cОшибка клика подтверждения: " + ex.getMessage());
                        }
                    }
                    resetState();
                    return;
                }
                return;
            }
            if (isAuctionTitle(title)) {
                if (this.pendingClickAt <= 0 || now >= this.pendingClickAt) {
                    if (this.pendingSlotId >= 0 && this.pendingClickAt > 0 && now >= this.pendingClickAt) {
                        class_1735 target = findSlotById(chest, this.pendingSlotId);
                        if (target != null && !target.method_7677().method_7960()) {
                            String nm = target.method_7677().method_7964().getString();
                            long pr = getAuctionPrice(target.method_7677());
                            if (this.debug) {
                                int i = this.pendingSlotId;
                                print("§7[AutoBuy] §aКликаем по предмету: §f" + nm + " §7цена: §f" + pr + " §7(слот " + this + ")");
                            }
                            try {
                                mc.field_1761.method_2906(chest.field_7763, this.pendingSlotId, 0, class_1713.field_7790, mc.field_1724);
                                this.pendingItemName = nm;
                                this.pendingItemPrice = pr;
                                this.waitingConfirm = true;
                                this.waitingConfirmSince = now;
                                if (this.debug) {
                                    print("§7[AutoBuy] §7Ждём окно подтверждения...");
                                }
                            } catch (Exception ex2) {
                                if (this.debug) {
                                    print("§7[AutoBuy] §cОшибка клика по предмету: " + ex2.getMessage());
                                }
                                resetState();
                            }
                        } else {
                            if (this.debug) {
                                print("§7[AutoBuy] §eПредмет пропал из слота " + this.pendingSlotId + " до клика. Сброс.");
                            }
                            resetState();
                        }
                        this.pendingClickAt = -1L;
                        this.pendingSlotId = -1;
                        return;
                    }
                    if (this.pendingSlotId >= 0) {
                        return;
                    }
                    for (class_1735 slot : chest.field_7761) {
                        if (slot.field_7874 <= 44 && !slot.method_7677().method_7960()) {
                            class_1799 stack = slot.method_7677();
                            String itemName = stack.method_7964().getString();
                            long price = getAuctionPrice(stack);
                            if (price < 0) {
                                continue;
                            } else {
                                for (BuyEntry entry : this.entries) {
                                    if (itemName.contains(entry.getNameContains()) && price <= entry.getMaxPrice()) {
                                        if (this.debug) {
                                            long maxPrice = entry.getMaxPrice();
                                            long j = this.clickDelay;
                                            print("§7[AutoBuy] §eНашли: §f" + itemName + " §7за §f" + price + " §7(макс: " + this + "). Клик через " + maxPrice + " мс...");
                                        }
                                        this.pendingSlotId = slot.field_7874;
                                        this.pendingClickAt = now + this.clickDelay;
                                        return;
                                    }
                                }
                            }
                        }
                    }
                }
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
        return title.contains("Аукцион") || title.contains("Поиск") || title.contains("Маркет") || title.contains("0B1L2o3v") || title.contains("ꈁꀀꈂꌲꈂꀁ") || title.contains("[☃] Аукционы");
    }

    private int findBuyConfirmSlot(class_1707 chest) {
        int inventoryStart = chest.field_7761.size() - 36;
        for (class_1735 slot : chest.field_7761) {
            if (slot.field_7874 < inventoryStart) {
                class_1799 stack = slot.method_7677();
                if (!stack.method_7960() && stack.method_7909() == class_1802.field_8407) {
                    String name = stack.method_7964().getString().replaceAll("(?i)§[0-9a-fklmnor]", "").replace("§r", "").trim();
                    if (name.contains("КУПИТЬ ЗА")) {
                        if (this.debug) {
                            print("§7[AutoBuy] §7Найдена кнопка подтверждения: §f\"" + name + "\" §7в слоте " + slot.field_7874);
                        }
                        return slot.field_7874;
                    }
                }
            }
        }
        return -1;
    }

    private class_1735 findSlotById(class_1707 chest, int id) {
        for (class_1735 s : chest.field_7761) {
            if (s.field_7874 == id) {
                return s;
            }
        }
        return null;
    }

    private long getAuctionPrice(class_1799 stack) {
        try {
            for (class_2561 text : stack.method_7950(class_1792.class_9635.field_51353, mc.field_1724, class_1836.field_41070)) {
                String str = text.getString().replace("§r", "").replaceAll("(?i)§[0-9a-fklmnor]", "").replace("¤", "").trim();
                if (str.contains("Цена в долларах:")) {
                    String cleaned = str.replaceAll("Цена в долларах:", "").replaceAll("[▯⌷⎕\\s]", "").replaceAll("[^0-9]", "").trim();
                    if (cleaned.isEmpty()) {
                        continue;
                    } else {
                        try {
                            return Long.parseLong(cleaned);
                        } catch (NumberFormatException e) {
                        }
                    }
                }
            }
            return -1L;
        } catch (Exception ex) {
            print("§7[AutoBuy] §cОшибка парсинга цены: " + ex.getMessage());
            return -1L;
        }
    }

    public void addEntry(String nameContains, long maxPrice) {
        this.entries.removeIf(e -> {
            return e.getNameContains().equalsIgnoreCase(nameContains);
        });
        this.entries.add(new BuyEntry(nameContains, maxPrice));
        saveEntries();
    }

    public boolean removeEntry(String nameContains) {
        boolean removed = this.entries.removeIf(e -> {
            return e.getNameContains().equalsIgnoreCase(nameContains);
        });
        if (removed) {
            saveEntries();
        }
        return removed;
    }

    public void clearEntries() {
        this.entries.clear();
        saveEntries();
    }

    public void addHistory(String name, long price) {
        this.history.add(0, new HistoryEntry(name, price, System.currentTimeMillis()));
        if (this.history.size() > 50) {
            this.history.subList(50, this.history.size()).clear();
        }
        saveHistory();
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
            FileWriter w = new FileWriter(entriesPath().toFile());
            try {
                GSON.toJson(this.entries, w);
                w.close();
            } finally {
            }
        } catch (IOException ex) {
            if (mc.field_1724 == null) {
                System.out.println("[AutoBuy] Error saving entries: " + ex.getMessage());
            } else {
                print("§7[AutoBuy] §cОшибка сохранения списка: " + ex.getMessage());
            }
        }
    }

    private void loadEntries() {
        try {
            Files.createDirectories(Path.of(ClientInfo.CONFIG_PATH_OTHER, new String[0]), new FileAttribute[0]);
            File f = entriesPath().toFile();
            if (f.exists()) {
                FileReader r = new FileReader(f);
                try {
                    Type t = new TypeToken<ArrayList<BuyEntry>>(this) { // from class: sweetie.leonware.client.features.modules.other.autobuy.AutoBuyManager.1
                    }.getType();
                    List<BuyEntry> loaded = (List) GSON.fromJson(r, t);
                    if (loaded != null) {
                        this.entries.addAll(loaded);
                    }
                    r.close();
                } finally {
                }
            }
        } catch (Exception ex) {
            System.out.println("[AutoBuy] Error loading entries: " + ex.getMessage());
        }
    }

    private void saveHistory() {
        try {
            Files.createDirectories(Path.of(ClientInfo.CONFIG_PATH_OTHER, new String[0]), new FileAttribute[0]);
            FileWriter w = new FileWriter(historyPath().toFile());
            try {
                GSON.toJson(this.history, w);
                w.close();
            } finally {
            }
        } catch (IOException ex) {
            if (mc.field_1724 == null) {
                System.out.println("[AutoBuy] Error saving history: " + ex.getMessage());
            } else {
                print("§7[AutoBuy] §cОшибка сохранения истории: " + ex.getMessage());
            }
        }
    }

    private void loadHistory() {
        try {
            Files.createDirectories(Path.of(ClientInfo.CONFIG_PATH_OTHER, new String[0]), new FileAttribute[0]);
            File f = historyPath().toFile();
            if (f.exists()) {
                FileReader r = new FileReader(f);
                try {
                    Type t = new TypeToken<ArrayList<HistoryEntry>>(this) { // from class: sweetie.leonware.client.features.modules.other.autobuy.AutoBuyManager.2
                    }.getType();
                    List<HistoryEntry> loaded = (List) GSON.fromJson(r, t);
                    if (loaded != null) {
                        this.history.addAll(loaded);
                    }
                    r.close();
                } finally {
                }
            }
        } catch (Exception ex) {
            System.out.println("[AutoBuy] Error loading history: " + ex.getMessage());
        }
    }
}
