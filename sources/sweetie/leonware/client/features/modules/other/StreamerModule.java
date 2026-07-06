package sweetie.leonware.client.features.modules.other;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.Generated;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.MultiBooleanSetting;
import sweetie.leonware.api.system.backend.ClientInfo;
import sweetie.leonware.api.system.configs.FriendManager;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/other/StreamerModule.class */
@ModuleRegister(name = "Streamer", category = Category.OTHER)
public class StreamerModule extends Module {
    private static final StreamerModule instance = new StreamerModule();
    private MultiBooleanSetting hide = new MultiBooleanSetting("Hides").value(new BooleanSetting("Name").value((Boolean) true), new BooleanSetting("Rainbow").value((Boolean) true).setVisible(() -> {
        return Boolean.valueOf(getHide().isEnabled("Name"));
    }), new BooleanSetting("Hide friends").value((Boolean) true).setVisible(() -> {
        return Boolean.valueOf(getHide().isEnabled("Name"));
    }), new BooleanSetting("No Fun Time").value((Boolean) false));
    private final ModeSetting fakeDonate = new ModeSetting("Фейк донат").values("Default", "Гидра", "Цербер", "Тритон", "Феникс", "Пандар", "Жара", "Холод", "Кронос", "Лето", "Зима", "Фобос", "Арес", "Аристократ", "Ютубер", "Хелпер", "СтХелпер", "Модер", "СтМодер", "Админ").value("Default");
    private final ConcurrentHashMap<String, Integer> friendCounter = new ConcurrentHashMap<>();
    private final AtomicInteger globalCounter = new AtomicInteger(1);

    @Generated
    public static StreamerModule getInstance() {
        return instance;
    }

    @Generated
    public MultiBooleanSetting getHide() {
        return this.hide;
    }

    @Generated
    public ModeSetting getFakeDonate() {
        return this.fakeDonate;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public StreamerModule() {
        addSettings(this.hide, this.fakeDonate);
    }

    public String getProtectedName() {
        if (!isEnabled()) {
            return mc.method_1548().method_1676();
        }
        if (!this.hide.isEnabled("Rainbow")) {
            return ClientInfo.NAME;
        }
        char[] rainbow = {'c', '6', 'e', 'a', 'b', '3', '9', 'd'};
        StringBuilder result = new StringBuilder();
        long offset = (System.currentTimeMillis() / 100) % ((long) rainbow.length);
        for (int i = 0; i < ClientInfo.NAME.length(); i++) {
            char color = rainbow[(int) ((((long) i) + offset) % ((long) rainbow.length))];
            result.append((char) 167).append(color).append(ClientInfo.NAME.charAt(i));
        }
        return result.toString();
    }

    public String getProtectedFriendName(String name) {
        return (isEnabled() && this.hide.isEnabled("Name") && this.hide.isEnabled("Hide friends") && FriendManager.getInstance().contains(name)) ? generateProtectedFriendName(name) : name;
    }

    public String generateProtectedFriendName(String originalName) {
        int id = this.friendCounter.computeIfAbsent(originalName.toLowerCase(), key -> {
            return Integer.valueOf(this.globalCounter.getAndIncrement());
        }).intValue();
        return "Друг " + id;
    }

    public String getResourceKey() {
        String val = this.fakeDonate.getValue();
        switch (val) {
            case "Гидра":
                return "hydra";
            case "Цербер":
                return "cerberus";
            case "Тритон":
                return "triton";
            case "Феникс":
                return "phoenix";
            case "Пандар":
                return "pandar";
            case "Жара":
                return "heat";
            case "Холод":
                return "cold";
            case "Кронос":
                return "kronos";
            case "Лето":
                return "summer";
            case "Зима":
                return "winter";
            case "Фобос":
                return "phobos";
            case "Арес":
                return "ares";
            case "Аристократ":
                return "aristocrat";
            case "Ютубер":
                return "youtuber";
            case "Хелпер":
                return "helper";
            case "СтХелпер":
                return "shelper";
            case "Модер":
                return "moder";
            case "СтМодер":
                return "smoder";
            case "Админ":
                return "admin";
            default:
                return "default";
        }
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
    }
}
