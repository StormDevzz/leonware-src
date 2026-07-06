package sweetie.leonware.client.features.modules.player;

import java.util.HashMap;
import java.util.Map;
import lombok.Generated;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2886;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.MultiBooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/player/CustomCoolDownModule.class */
@ModuleRegister(name = "Custom CoolDown", category = Category.PLAYER)
public class CustomCoolDownModule extends Module {
    private static final CustomCoolDownModule instance = new CustomCoolDownModule();
    private final MultiBooleanSetting items = new MultiBooleanSetting("Задержка").value(new BooleanSetting("Золотое яблоко").value((Boolean) true), new BooleanSetting("Плод хоруса").value((Boolean) true), new BooleanSetting("Эндер-жемчюг").value((Boolean) true));
    private final SliderSetting appleTime = new SliderSetting("Кд золотого яблока").value(Float.valueOf(4.6f)).range(1.0f, 16.0f).step(0.1f).setVisible(() -> {
        return Boolean.valueOf(this.items.isEnabled("Золотое яблоко"));
    });
    private final SliderSetting pearlTime = new SliderSetting("Кд эндер-жемчюгов").value(Float.valueOf(13.5f)).range(1.0f, 16.0f).step(0.1f).setVisible(() -> {
        return Boolean.valueOf(this.items.isEnabled("Эндер-жемчюг"));
    });
    private final SliderSetting horusTime = new SliderSetting("Кд хоруса").value(Float.valueOf(3.5f)).range(1.0f, 16.0f).step(0.1f).setVisible(() -> {
        return Boolean.valueOf(this.items.isEnabled("Плод хоруса"));
    });
    private final Map<class_1792, Long> lastUseMap = new HashMap();

    @Generated
    public static CustomCoolDownModule getInstance() {
        return instance;
    }

    /* JADX WARN: Type inference failed for: r1v11, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v16, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v6, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    public CustomCoolDownModule() {
        addSettings(this.items, this.appleTime, this.pearlTime, this.horusTime);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener(event -> {
            if (mc.field_1724 != null && mc.field_1724.method_6115()) {
                class_1799 activeStack = mc.field_1724.method_6030();
                class_1792 item = activeStack.method_7909();
                if (isItemEnabled(item) && this.lastUseMap.containsKey(item)) {
                    long lastUse = this.lastUseMap.get(item).longValue();
                    float delayMs = getCooldownForItem(item) * 1000.0f;
                    if (System.currentTimeMillis() - lastUse < delayMs) {
                        mc.field_1724.method_6021();
                    } else {
                        this.lastUseMap.remove(item);
                    }
                }
            }
        }));
        EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener(data -> {
            if (mc.field_1724 == null || !data.isSend()) {
                return;
            }
            class_2886 class_2886VarPacket = data.packet();
            if (class_2886VarPacket instanceof class_2886) {
                class_2886 interactPacket = class_2886VarPacket;
                class_1799 stack = mc.field_1724.method_5998(interactPacket.method_12551());
                class_1792 item = stack.method_7909();
                if (isItemEnabled(item)) {
                    long lastUse = this.lastUseMap.getOrDefault(item, 0L).longValue();
                    float delayMs = getCooldownForItem(item) * 1000.0f;
                    if (System.currentTimeMillis() - lastUse >= delayMs) {
                        this.lastUseMap.put(item, Long.valueOf(System.currentTimeMillis()));
                    }
                }
            }
        }));
        addEvents(updateEvent, packetEvent);
    }

    private float getCooldownForItem(class_1792 item) {
        if (item == class_1802.field_8463) {
            return this.appleTime.getValue().floatValue();
        }
        if (item == class_1802.field_8634) {
            return this.pearlTime.getValue().floatValue();
        }
        if (item == class_1802.field_8233) {
            return this.horusTime.getValue().floatValue();
        }
        return 0.0f;
    }

    private boolean isItemEnabled(class_1792 item) {
        if (item == class_1802.field_8463) {
            return this.items.isEnabled("Золотое яблоко");
        }
        if (item == class_1802.field_8634) {
            return this.items.isEnabled("Эндер-жемчюг");
        }
        if (item == class_1802.field_8233) {
            return this.items.isEnabled("Плод хоруса");
        }
        return false;
    }
}
