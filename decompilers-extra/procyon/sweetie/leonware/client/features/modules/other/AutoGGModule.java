// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.other;

import lombok.Generated;
import net.minecraft.class_1297;
import net.minecraft.class_2596;
import java.util.Iterator;
import net.minecraft.class_1309;
import sweetie.leonware.api.event.EventListener;
import net.minecraft.class_1937;
import net.minecraft.class_2663;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.event.Listener;
import net.minecraft.class_1657;
import sweetie.leonware.client.features.modules.combat.AuraModule;
import sweetie.leonware.api.event.events.client.TickEvent;
import sweetie.leonware.api.module.setting.Setting;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Random;
import sweetie.leonware.api.utils.math.TimerUtil;
import java.util.Set;
import java.util.Map;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Auto GG", category = Category.OTHER)
public class AutoGGModule extends Module
{
    private static final AutoGGModule instance;
    private final ModeSetting detectMode;
    private final ModeSetting bullingMode;
    private final ModeSetting chatType;
    private final BooleanSetting nickname;
    private final SliderSetting messageDelay;
    private static final String[] LITE_MESSAGES;
    private static final String[] RAGE_MESSAGES;
    private final Map<Integer, Long> recentTargets;
    private final Map<Integer, String> targetNames;
    private final Set<Integer> sentIds;
    private final TimerUtil cooldownTimer;
    private final Random random;
    private static final long TARGET_MEMORY_MS = 2000L;
    
    public AutoGGModule() {
        this.detectMode = new ModeSetting("Detect").values("Packet", "Health").value("Health");
        this.bullingMode = new ModeSetting("Bulling").values("Lite", "Rage").value("Lite");
        this.chatType = new ModeSetting("Chat").values("Global", "Local", "PM").value("Local");
        this.nickname = new BooleanSetting("Nickname").value(false);
        this.messageDelay = new SliderSetting("Delay").value(500.0f).range(0.0f, 3000.0f).step(50.0f);
        this.recentTargets = new ConcurrentHashMap<Integer, Long>();
        this.targetNames = new ConcurrentHashMap<Integer, String>();
        this.sentIds = new HashSet<Integer>();
        this.cooldownTimer = new TimerUtil();
        this.random = new Random();
        this.addSettings(this.detectMode, this.bullingMode, this.chatType, this.nickname, this.messageDelay);
    }
    
    @Override
    public void onDisable() {
        this.recentTargets.clear();
        this.targetNames.clear();
        this.sentIds.clear();
    }
    
    @Override
    public void onEvent() {
        final EventListener tickEvent = TickEvent.getInstance().subscribe(new Listener<TickEvent>(event -> {
            if (AutoGGModule.mc.field_1687 == null || AutoGGModule.mc.field_1724 == null) {
                return;
            }
            else {
                final class_1309 current = AuraModule.getInstance().target;
                if (current != null && current instanceof class_1657) {
                    final class_1657 player = (class_1657)current;
                    this.recentTargets.put(current.method_5628(), System.currentTimeMillis());
                    this.targetNames.put(current.method_5628(), player.method_7334().getName());
                }
                final long now = System.currentTimeMillis();
                this.recentTargets.entrySet().removeIf(e -> now - e.getValue() > 2000L);
                int id = 0;
                this.sentIds.removeIf(id -> AutoGGModule.mc.field_1687.method_8469((int)id) == null);
                if (!this.detectMode.is("Health")) {
                    return;
                }
                else {
                    AutoGGModule.mc.field_1687.method_18456().iterator();
                    final Iterator iterator;
                    while (iterator.hasNext()) {
                        final class_1657 player2 = iterator.next();
                        if (player2 == AutoGGModule.mc.field_1724) {
                            continue;
                        }
                        else {
                            id = player2.method_5628();
                            if (!this.recentTargets.containsKey(id)) {
                                continue;
                            }
                            else if (this.sentIds.contains(id)) {
                                continue;
                            }
                            else if (player2.method_6032() <= 0.0f) {
                                this.sentIds.add(id);
                                final String name = this.targetNames.getOrDefault(id, player2.method_7334().getName());
                                this.sendGG(name);
                            }
                            else {
                                continue;
                            }
                        }
                    }
                    return;
                }
            }
        }));
        final EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener<PacketEvent.PacketEventData>(event -> {
            if (AutoGGModule.mc.field_1687 == null || AutoGGModule.mc.field_1724 == null) {
                return;
            }
            else if (!this.detectMode.is("Packet")) {
                return;
            }
            else if (!event.isSend()) {
                return;
            }
            else {
                final class_2596 patt0$temp = event.packet();
                if (patt0$temp instanceof final class_2663 packet) {
                    if (packet.method_11470() != 3) {
                        return;
                    }
                    else {
                        final class_1297 entity = packet.method_11469((class_1937)AutoGGModule.mc.field_1687);
                        if (entity == null) {
                            return;
                        }
                        else if (entity instanceof final class_1657 player3) {
                            if (entity == AutoGGModule.mc.field_1724) {
                                return;
                            }
                            else {
                                final int id2 = entity.method_5628();
                                if (!this.recentTargets.containsKey(id2)) {
                                    return;
                                }
                                else if (this.sentIds.contains(id2)) {
                                    return;
                                }
                                else {
                                    this.sentIds.add(id2);
                                    final String name2 = this.targetNames.getOrDefault(id2, player3.method_7334().getName());
                                    this.sendGG(name2);
                                    return;
                                }
                            }
                        }
                        else {
                            return;
                        }
                    }
                }
                else {
                    return;
                }
            }
        }));
        this.addEvents(tickEvent, packetEvent);
    }
    
    private void sendGG(final String targetName) {
        if (!this.cooldownTimer.finished((long)(float)this.messageDelay.getValue())) {
            return;
        }
        final String[] pool = this.bullingMode.is("Rage") ? AutoGGModule.RAGE_MESSAGES : AutoGGModule.LITE_MESSAGES;
        String message = pool[this.random.nextInt(pool.length)];
        if (this.nickname.getValue()) {
            message = targetName + " " + message;
        }
        final String s = this.chatType.getValue();
        switch (s) {
            case "Global": {
                AutoGGModule.mc.field_1724.field_3944.method_45729("!" + message);
                break;
            }
            case "Local": {
                AutoGGModule.mc.field_1724.field_3944.method_45729(message);
                break;
            }
            case "PM": {
                AutoGGModule.mc.field_1724.field_3944.method_45729("/m " + targetName + " " + message);
                break;
            }
        }
        this.cooldownTimer.reset();
    }
    
    @Generated
    public static AutoGGModule getInstance() {
        return AutoGGModule.instance;
    }
    
    static {
        instance = new AutoGGModule();
        LITE_MESSAGES = new String[] { "gg ez", "GG!", "gg wp", "nice fight", "get good", "too easy", "gg no re", "was that it?", "better luck next time", "close one", "not even close", "gg well played" };
        RAGE_MESSAGES = new String[] { "\u044f \u0442\u044f \u0437\u0430\u0445\u0430\u0440\u043a\u0430\u043b \u0442\u0443\u043f\u043e1 \u0440\u043e\u0442\u043e\u0432\u044b3\u0431\u0430\u043d\u043d\u044b\u0439 ))", "\u0431\u0438\u0447 \u0431\u0435\u0437 \u043b\u0435\u043e\u043d\u0432\u0430\u0440\u0430", "\u043f\u043e\u043c\u043e\u0435\u043c\u0443 \u0442\u044b \u043e\u0431\u043e\u0441\u0441\u0430\u043d by leonware", "\u0448\u043b9\u0445\u0430\u043d \u0431\u0435\u0437 \u0442\u0430\u043d\u0434\u0435\u0440\u0445\u0430\u043a\u0430", "\u0444\u0440\u0438\u043a\u0430\u043d\u0443\u0442\u044b\u0439 \u0441\u043f\u0435\u0440\u043c\u043e\u0431\u043b\u044f\u0434\u0443\u043d thank you \u0437\u0430 \u043e\u0442\u0441\u043e\u0441 \u0442\u0430\u043a \u0436\u0435 \u0431\u044b\u0441\u0442\u0440\u043e \u043e\u0442\u0441\u043e\u0441000\u043b \u043a\u0430\u043a \u0442\u0432\u043e\u044f \u043d\u0438\u0449\u0443\u0433\u0430\u043d\u0441\u043a\u0430\u044f \u0437\u0430\u043f\u043b\u0435\u0432\u0430\u043d\u043d\u0430\u044f \u0433\u0440\u044f\u0437\u043d\u0430\u044f \u043c\u0430\u0442\u0443\u0448\u043a\u0430", "get leonware 2 win", "\u0413\u0414\u0415 \u0442\u0432\u043e\u0439 \u0422\u0425 \u0411\u0418\u0427 \u0410\u041b\u0415?((", "\u043c\u043e\u0439 \u0447\u0438\u0442 \u043a\u043e\u0434\u0438\u043b \u043f\u0430\u043d\u0447\u0443\u0440 \u043f\u043e\u044d\u0442\u043e\u043c\u0443 \u0442\u044b \u0438 \u043b\u0443\u0437\u043d\u0443\u043b \u0432\u044b\u0431\u043b9\u0434\u043e\u043a", "\u043f\u0430\u0441\u0442\u0430\u044e\u0437\u0435\u0440 \u0433\u0435\u0442\u043d\u0438 \u043b\u0435\u043e\u043d\u0432\u0430\u0440 \u043f\u0436\u043b\u0441\u0442(jscodeth)", "\u043f\u043e\u0447\u0435\u043c\u0443 \u0442\u044b \u0442\u0430\u043a\u043e\u0439 \u0441\u043b\u0430\u0431\u044b\u0439 \u043f00\u0438d\u043epac", "get good stay with leonware", "\u043e\u0431\u043e\u0441\u0430\u043d\u043d\u044b\u0439 \u0444\u0440\u0438\u043a\u0430\u043d\u0443\u0442\u044b\u0439 \u0441\u043f\u0435\u0440\u043c\u043e\u0431\u043b99\u0434\u0441\u043a\u0438\u0439 \u0437\u0430\u0445\u0430\u0440\u043a\u0430\u043d\u043d\u044b\u0439 \u0440\u043e0\u0442\u043e\u0432\u044b3\u0431\u0430\u043d\u043d\u044b\u0439 \u0445\u0430\u0447 \u0441 \u043c\u0430\u0442\u0443\u0445\u043e\u0439 \u0448\u0432\u0430\u0431\u0440\u043e\u0439 \u0438 \u0436\u0435\u043d\u043e\u0439 \u0441\u0435\u0441\u0442\u0440\u043e\u0439 \u0432\u0442\u043e\u043f\u0438 \u0441\u0432\u043e\u0435 \u0431\u0435\u0434\u043d\u043e\u0435 \u043f\u0440\u044b\u0449\u0430\u0432\u043e\u0435 \u043b\u0438\u0446\u043e \u0438 \u043c\u043e\u043b\u0447\u0430 \u0432\u0441\u043e\u0441\u0438 \u043b\u0435\u043e\u043d\u0432\u0430\u0440\u0443 \u043e\u043a\u0435\u0439 \u0431\u0438\u0447\u0443\u0433\u0430\u043d ???" };
    }
}
