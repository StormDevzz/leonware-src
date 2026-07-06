// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.other;

import lombok.Generated;
import sweetie.leonware.api.system.configs.FriendManager;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.module.setting.BooleanSetting;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.ConcurrentHashMap;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.MultiBooleanSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Streamer", category = Category.OTHER)
public class StreamerModule extends Module
{
    private static final StreamerModule instance;
    private MultiBooleanSetting hide;
    private final ModeSetting fakeDonate;
    private final ConcurrentHashMap<String, Integer> friendCounter;
    private final AtomicInteger globalCounter;
    
    public StreamerModule() {
        this.hide = new MultiBooleanSetting("Hides").value(new BooleanSetting("Name").value(true), new BooleanSetting("Rainbow").value(true).setVisible(() -> this.getHide().isEnabled("Name")), new BooleanSetting("Hide friends").value(true).setVisible(() -> this.getHide().isEnabled("Name")), new BooleanSetting("No Fun Time").value(false));
        this.fakeDonate = new ModeSetting("\u0424\u0435\u0439\u043a \u0434\u043e\u043d\u0430\u0442").values("Default", "\u0413\u0438\u0434\u0440\u0430", "\u0426\u0435\u0440\u0431\u0435\u0440", "\u0422\u0440\u0438\u0442\u043e\u043d", "\u0424\u0435\u043d\u0438\u043a\u0441", "\u041f\u0430\u043d\u0434\u0430\u0440", "\u0416\u0430\u0440\u0430", "\u0425\u043e\u043b\u043e\u0434", "\u041a\u0440\u043e\u043d\u043e\u0441", "\u041b\u0435\u0442\u043e", "\u0417\u0438\u043c\u0430", "\u0424\u043e\u0431\u043e\u0441", "\u0410\u0440\u0435\u0441", "\u0410\u0440\u0438\u0441\u0442\u043e\u043a\u0440\u0430\u0442", "\u042e\u0442\u0443\u0431\u0435\u0440", "\u0425\u0435\u043b\u043f\u0435\u0440", "\u0421\u0442\u0425\u0435\u043b\u043f\u0435\u0440", "\u041c\u043e\u0434\u0435\u0440", "\u0421\u0442\u041c\u043e\u0434\u0435\u0440", "\u0410\u0434\u043c\u0438\u043d").value("Default");
        this.friendCounter = new ConcurrentHashMap<String, Integer>();
        this.globalCounter = new AtomicInteger(1);
        this.addSettings(this.hide, this.fakeDonate);
    }
    
    public String getProtectedName() {
        if (!this.isEnabled()) {
            return StreamerModule.mc.method_1548().method_1676();
        }
        if (!this.hide.isEnabled("Rainbow")) {
            return "LeonWare";
        }
        final String name = "LeonWare";
        final char[] rainbow = { 'c', '6', 'e', 'a', 'b', '3', '9', 'd' };
        final StringBuilder result = new StringBuilder();
        final long offset = System.currentTimeMillis() / 100L % rainbow.length;
        for (int i = 0; i < name.length(); ++i) {
            final char color = rainbow[(int)((i + offset) % rainbow.length)];
            result.append('§').append(color).append(name.charAt(i));
        }
        return result.toString();
    }
    
    public String getProtectedFriendName(final String name) {
        return (this.isEnabled() && this.hide.isEnabled("Name") && this.hide.isEnabled("Hide friends") && FriendManager.getInstance().contains(name)) ? this.generateProtectedFriendName(name) : name;
    }
    
    public String generateProtectedFriendName(final String originalName) {
        final int id = this.friendCounter.computeIfAbsent(originalName.toLowerCase(), key -> this.globalCounter.getAndIncrement());
        return "\u0414\u0440\u0443\u0433 " + id;
    }
    
    public String getResourceKey() {
        final String s;
        final String val = s = this.fakeDonate.getValue();
        switch (s) {
            case "\u0413\u0438\u0434\u0440\u0430": {
                return "hydra";
            }
            case "\u0426\u0435\u0440\u0431\u0435\u0440": {
                return "cerberus";
            }
            case "\u0422\u0440\u0438\u0442\u043e\u043d": {
                return "triton";
            }
            case "\u0424\u0435\u043d\u0438\u043a\u0441": {
                return "phoenix";
            }
            case "\u041f\u0430\u043d\u0434\u0430\u0440": {
                return "pandar";
            }
            case "\u0416\u0430\u0440\u0430": {
                return "heat";
            }
            case "\u0425\u043e\u043b\u043e\u0434": {
                return "cold";
            }
            case "\u041a\u0440\u043e\u043d\u043e\u0441": {
                return "kronos";
            }
            case "\u041b\u0435\u0442\u043e": {
                return "summer";
            }
            case "\u0417\u0438\u043c\u0430": {
                return "winter";
            }
            case "\u0424\u043e\u0431\u043e\u0441": {
                return "phobos";
            }
            case "\u0410\u0440\u0435\u0441": {
                return "ares";
            }
            case "\u0410\u0440\u0438\u0441\u0442\u043e\u043a\u0440\u0430\u0442": {
                return "aristocrat";
            }
            case "\u042e\u0442\u0443\u0431\u0435\u0440": {
                return "youtuber";
            }
            case "\u0425\u0435\u043b\u043f\u0435\u0440": {
                return "helper";
            }
            case "\u0421\u0442\u0425\u0435\u043b\u043f\u0435\u0440": {
                return "shelper";
            }
            case "\u041c\u043e\u0434\u0435\u0440": {
                return "moder";
            }
            case "\u0421\u0442\u041c\u043e\u0434\u0435\u0440": {
                return "smoder";
            }
            case "\u0410\u0434\u043c\u0438\u043d": {
                return "admin";
            }
            default: {
                return "default";
            }
        }
    }
    
    @Override
    public void onEvent() {
    }
    
    @Generated
    public static StreamerModule getInstance() {
        return StreamerModule.instance;
    }
    
    @Generated
    public MultiBooleanSetting getHide() {
        return this.hide;
    }
    
    @Generated
    public ModeSetting getFakeDonate() {
        return this.fakeDonate;
    }
    
    static {
        instance = new StreamerModule();
    }
}
