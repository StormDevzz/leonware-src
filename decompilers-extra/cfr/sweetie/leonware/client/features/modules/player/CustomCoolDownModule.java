/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1792
 *  net.minecraft.class_1799
 *  net.minecraft.class_1802
 *  net.minecraft.class_2596
 *  net.minecraft.class_2886
 */
package sweetie.leonware.client.features.modules.player;

import java.util.HashMap;
import java.util.Map;
import lombok.Generated;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2596;
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

@ModuleRegister(name="Custom CoolDown", category=Category.PLAYER)
public class CustomCoolDownModule
extends Module {
    private static final CustomCoolDownModule instance = new CustomCoolDownModule();
    private final MultiBooleanSetting items = new MultiBooleanSetting("\u0417\u0430\u0434\u0435\u0440\u0436\u043a\u0430").value(new BooleanSetting("\u0417\u043e\u043b\u043e\u0442\u043e\u0435 \u044f\u0431\u043b\u043e\u043a\u043e").value(true), new BooleanSetting("\u041f\u043b\u043e\u0434 \u0445\u043e\u0440\u0443\u0441\u0430").value(true), new BooleanSetting("\u042d\u043d\u0434\u0435\u0440-\u0436\u0435\u043c\u0447\u044e\u0433").value(true));
    private final SliderSetting appleTime = new SliderSetting("\u041a\u0434 \u0437\u043e\u043b\u043e\u0442\u043e\u0433\u043e \u044f\u0431\u043b\u043e\u043a\u0430").value(Float.valueOf(4.6f)).range(1.0f, 16.0f).step(0.1f).setVisible(() -> this.items.isEnabled("\u0417\u043e\u043b\u043e\u0442\u043e\u0435 \u044f\u0431\u043b\u043e\u043a\u043e"));
    private final SliderSetting pearlTime = new SliderSetting("\u041a\u0434 \u044d\u043d\u0434\u0435\u0440-\u0436\u0435\u043c\u0447\u044e\u0433\u043e\u0432").value(Float.valueOf(13.5f)).range(1.0f, 16.0f).step(0.1f).setVisible(() -> this.items.isEnabled("\u042d\u043d\u0434\u0435\u0440-\u0436\u0435\u043c\u0447\u044e\u0433"));
    private final SliderSetting horusTime = new SliderSetting("\u041a\u0434 \u0445\u043e\u0440\u0443\u0441\u0430").value(Float.valueOf(3.5f)).range(1.0f, 16.0f).step(0.1f).setVisible(() -> this.items.isEnabled("\u041f\u043b\u043e\u0434 \u0445\u043e\u0440\u0443\u0441\u0430"));
    private final Map<class_1792, Long> lastUseMap = new HashMap<class_1792, Long>();

    public CustomCoolDownModule() {
        this.addSettings(this.items, this.appleTime, this.pearlTime, this.horusTime);
    }

    @Override
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            class_1799 activeStack;
            class_1792 item;
            if (CustomCoolDownModule.mc.field_1724 == null) {
                return;
            }
            if (CustomCoolDownModule.mc.field_1724.method_6115() && this.isItemEnabled(item = (activeStack = CustomCoolDownModule.mc.field_1724.method_6030()).method_7909()) && this.lastUseMap.containsKey(item)) {
                long lastUse = this.lastUseMap.get(item);
                float delayMs = this.getCooldownForItem(item) * 1000.0f;
                if ((float)(System.currentTimeMillis() - lastUse) < delayMs) {
                    CustomCoolDownModule.mc.field_1724.method_6021();
                } else {
                    this.lastUseMap.remove(item);
                }
            }
        }));
        EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener<PacketEvent.PacketEventData>(data -> {
            class_2886 interactPacket;
            class_1799 stack;
            class_1792 item;
            if (CustomCoolDownModule.mc.field_1724 == null || !data.isSend()) {
                return;
            }
            class_2596<?> patt0$temp = data.packet();
            if (patt0$temp instanceof class_2886 && this.isItemEnabled(item = (stack = CustomCoolDownModule.mc.field_1724.method_5998((interactPacket = (class_2886)patt0$temp).method_12551())).method_7909())) {
                long lastUse = this.lastUseMap.getOrDefault(item, 0L);
                float delayMs = this.getCooldownForItem(item) * 1000.0f;
                if ((float)(System.currentTimeMillis() - lastUse) >= delayMs) {
                    this.lastUseMap.put(item, System.currentTimeMillis());
                }
            }
        }));
        this.addEvents(updateEvent, packetEvent);
    }

    private float getCooldownForItem(class_1792 item) {
        if (item == class_1802.field_8463) {
            return ((Float)this.appleTime.getValue()).floatValue();
        }
        if (item == class_1802.field_8634) {
            return ((Float)this.pearlTime.getValue()).floatValue();
        }
        if (item == class_1802.field_8233) {
            return ((Float)this.horusTime.getValue()).floatValue();
        }
        return 0.0f;
    }

    private boolean isItemEnabled(class_1792 item) {
        if (item == class_1802.field_8463) {
            return this.items.isEnabled("\u0417\u043e\u043b\u043e\u0442\u043e\u0435 \u044f\u0431\u043b\u043e\u043a\u043e");
        }
        if (item == class_1802.field_8634) {
            return this.items.isEnabled("\u042d\u043d\u0434\u0435\u0440-\u0436\u0435\u043c\u0447\u044e\u0433");
        }
        if (item == class_1802.field_8233) {
            return this.items.isEnabled("\u041f\u043b\u043e\u0434 \u0445\u043e\u0440\u0443\u0441\u0430");
        }
        return false;
    }

    @Generated
    public static CustomCoolDownModule getInstance() {
        return instance;
    }
}

