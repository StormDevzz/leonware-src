package sweetie.leonware.client.features.modules.other;

import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Generated;
import net.minecraft.class_1657;
import net.minecraft.class_2663;
import net.minecraft.class_746;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.event.events.client.TickEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.utils.math.TimerUtil;
import sweetie.leonware.client.features.modules.combat.AuraModule;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/other/AutoGGModule.class */
@ModuleRegister(name = "Auto GG", category = Category.OTHER)
public class AutoGGModule extends Module {
    private final ModeSetting detectMode = new ModeSetting("Detect").values("Packet", "Health").value("Health");
    private final ModeSetting bullingMode = new ModeSetting("Bulling").values("Lite", "Rage").value("Lite");
    private final ModeSetting chatType = new ModeSetting("Chat").values("Global", "Local", "PM").value("Local");
    private final BooleanSetting nickname = new BooleanSetting("Nickname").value((Boolean) false);
    private final SliderSetting messageDelay = new SliderSetting("Delay").value(Float.valueOf(500.0f)).range(0.0f, 3000.0f).step(50.0f);
    private final Map<Integer, Long> recentTargets = new ConcurrentHashMap();
    private final Map<Integer, String> targetNames = new ConcurrentHashMap();
    private final Set<Integer> sentIds = new HashSet();
    private final TimerUtil cooldownTimer = new TimerUtil();
    private final Random random = new Random();
    private static final long TARGET_MEMORY_MS = 2000;
    private static final AutoGGModule instance = new AutoGGModule();
    private static final String[] LITE_MESSAGES = {"gg ez", "GG!", "gg wp", "nice fight", "get good", "too easy", "gg no re", "was that it?", "better luck next time", "close one", "not even close", "gg well played"};
    private static final String[] RAGE_MESSAGES = {"я тя захаркал тупо1 ротовы3банный ))", "бич без леонвара", "помоему ты обоссан by leonware", "шл9хан без тандерхака", "фриканутый спермоблядун thank you за отсос так же быстро отсос000л как твоя нищуганская заплеванная грязная матушка", "get leonware 2 win", "ГДЕ твой ТХ БИЧ АЛЕ?((", "мой чит кодил панчур поэтому ты и лузнул выбл9док", "пастаюзер гетни леонвар пжлст(jscodeth)", "почему ты такой слабый п00иdоpac", "get good stay with leonware", "обосанный фриканутый спермобл99дский захарканный ро0товы3банный хач с матухой шваброй и женой сестрой втопи свое бедное прыщавое лицо и молча всоси леонвару окей бичуган ???"};

    @Generated
    public static AutoGGModule getInstance() {
        return instance;
    }

    public AutoGGModule() {
        addSettings(this.detectMode, this.bullingMode, this.chatType, this.nickname, this.messageDelay);
    }

    @Override // sweetie.leonware.api.module.Module
    public void onDisable() {
        this.recentTargets.clear();
        this.targetNames.clear();
        this.sentIds.clear();
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener tickEvent = TickEvent.getInstance().subscribe(new Listener(event -> {
            if (mc.field_1687 == null || mc.field_1724 == null) {
                return;
            }
            class_1657 class_1657Var = AuraModule.getInstance().target;
            if (class_1657Var != null && (class_1657Var instanceof class_1657)) {
                class_1657 player = class_1657Var;
                this.recentTargets.put(Integer.valueOf(class_1657Var.method_5628()), Long.valueOf(System.currentTimeMillis()));
                this.targetNames.put(Integer.valueOf(class_1657Var.method_5628()), player.method_7334().getName());
            }
            long now = System.currentTimeMillis();
            this.recentTargets.entrySet().removeIf(e -> {
                return now - ((Long) e.getValue()).longValue() > TARGET_MEMORY_MS;
            });
            this.sentIds.removeIf(id -> {
                return mc.field_1687.method_8469(id.intValue()) == null;
            });
            if (this.detectMode.is("Health")) {
                for (class_746 class_746Var : mc.field_1687.method_18456()) {
                    if (class_746Var != mc.field_1724) {
                        int id2 = class_746Var.method_5628();
                        if (this.recentTargets.containsKey(Integer.valueOf(id2)) && !this.sentIds.contains(Integer.valueOf(id2)) && class_746Var.method_6032() <= 0.0f) {
                            this.sentIds.add(Integer.valueOf(id2));
                            String name = this.targetNames.getOrDefault(Integer.valueOf(id2), class_746Var.method_7334().getName());
                            sendGG(name);
                        }
                    }
                }
            }
        }));
        EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener(event2 -> {
            class_746 class_746VarMethod_11469;
            if (mc.field_1687 == null || mc.field_1724 == null || !this.detectMode.is("Packet") || !event2.isSend()) {
                return;
            }
            class_2663 class_2663VarPacket = event2.packet();
            if (class_2663VarPacket instanceof class_2663) {
                class_2663 packet = class_2663VarPacket;
                if (packet.method_11470() == 3 && (class_746VarMethod_11469 = packet.method_11469(mc.field_1687)) != null && (class_746VarMethod_11469 instanceof class_1657)) {
                    class_1657 player = (class_1657) class_746VarMethod_11469;
                    if (class_746VarMethod_11469 == mc.field_1724) {
                        return;
                    }
                    int id = class_746VarMethod_11469.method_5628();
                    if (this.recentTargets.containsKey(Integer.valueOf(id)) && !this.sentIds.contains(Integer.valueOf(id))) {
                        this.sentIds.add(Integer.valueOf(id));
                        String name = this.targetNames.getOrDefault(Integer.valueOf(id), player.method_7334().getName());
                        sendGG(name);
                    }
                }
            }
        }));
        addEvents(tickEvent, packetEvent);
    }

    private void sendGG(String targetName) {
        String message;
        if (this.cooldownTimer.finished((long) this.messageDelay.getValue().floatValue())) {
            String[] pool = this.bullingMode.is("Rage") ? RAGE_MESSAGES : LITE_MESSAGES;
            message = pool[this.random.nextInt(pool.length)];
            if (this.nickname.getValue().booleanValue()) {
                message = targetName + " " + message;
            }
            switch (this.chatType.getValue()) {
                case "Global":
                    mc.field_1724.field_3944.method_45729("!" + message);
                    break;
                case "Local":
                    mc.field_1724.field_3944.method_45729(message);
                    break;
                case "PM":
                    mc.field_1724.field_3944.method_45729("/m " + targetName + " " + message);
                    break;
            }
            this.cooldownTimer.reset();
        }
    }
}
