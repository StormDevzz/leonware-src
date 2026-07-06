// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.player;

import lombok.Generated;
import net.minecraft.class_1802;
import net.minecraft.class_2596;
import net.minecraft.class_1799;
import sweetie.leonware.api.event.EventListener;
import net.minecraft.class_2886;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.setting.Setting;
import java.util.HashMap;
import sweetie.leonware.api.module.setting.BooleanSetting;
import net.minecraft.class_1792;
import java.util.Map;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.setting.MultiBooleanSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Custom CoolDown", category = Category.PLAYER)
public class CustomCoolDownModule extends Module
{
    private static final CustomCoolDownModule instance;
    private final MultiBooleanSetting items;
    private final SliderSetting appleTime;
    private final SliderSetting pearlTime;
    private final SliderSetting horusTime;
    private final Map<class_1792, Long> lastUseMap;
    
    public CustomCoolDownModule() {
        this.items = new MultiBooleanSetting("\u0417\u0430\u0434\u0435\u0440\u0436\u043a\u0430").value(new BooleanSetting("\u0417\u043e\u043b\u043e\u0442\u043e\u0435 \u044f\u0431\u043b\u043e\u043a\u043e").value(true), new BooleanSetting("\u041f\u043b\u043e\u0434 \u0445\u043e\u0440\u0443\u0441\u0430").value(true), new BooleanSetting("\u042d\u043d\u0434\u0435\u0440-\u0436\u0435\u043c\u0447\u044e\u0433").value(true));
        this.appleTime = new SliderSetting("\u041a\u0434 \u0437\u043e\u043b\u043e\u0442\u043e\u0433\u043e \u044f\u0431\u043b\u043e\u043a\u0430").value(4.6f).range(1.0f, 16.0f).step(0.1f).setVisible(() -> this.items.isEnabled("\u0417\u043e\u043b\u043e\u0442\u043e\u0435 \u044f\u0431\u043b\u043e\u043a\u043e"));
        this.pearlTime = new SliderSetting("\u041a\u0434 \u044d\u043d\u0434\u0435\u0440-\u0436\u0435\u043c\u0447\u044e\u0433\u043e\u0432").value(13.5f).range(1.0f, 16.0f).step(0.1f).setVisible(() -> this.items.isEnabled("\u042d\u043d\u0434\u0435\u0440-\u0436\u0435\u043c\u0447\u044e\u0433"));
        this.horusTime = new SliderSetting("\u041a\u0434 \u0445\u043e\u0440\u0443\u0441\u0430").value(3.5f).range(1.0f, 16.0f).step(0.1f).setVisible(() -> this.items.isEnabled("\u041f\u043b\u043e\u0434 \u0445\u043e\u0440\u0443\u0441\u0430"));
        this.lastUseMap = new HashMap<class_1792, Long>();
        this.addSettings(this.items, this.appleTime, this.pearlTime, this.horusTime);
    }
    
    @Override
    public void onEvent() {
        final EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (CustomCoolDownModule.mc.field_1724 == null) {
                return;
            }
            else {
                if (CustomCoolDownModule.mc.field_1724.method_6115()) {
                    final class_1799 activeStack = CustomCoolDownModule.mc.field_1724.method_6030();
                    final class_1792 item = activeStack.method_7909();
                    if (this.isItemEnabled(item) && this.lastUseMap.containsKey(item)) {
                        final long lastUse = this.lastUseMap.get(item);
                        final float delayMs = this.getCooldownForItem(item) * 1000.0f;
                        if (System.currentTimeMillis() - lastUse < delayMs) {
                            CustomCoolDownModule.mc.field_1724.method_6021();
                        }
                        else {
                            this.lastUseMap.remove(item);
                        }
                    }
                }
                return;
            }
        }));
        final EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener<PacketEvent.PacketEventData>(data -> {
            if (CustomCoolDownModule.mc.field_1724 == null || !data.isSend()) {
                return;
            }
            else {
                final class_2596 patt0$temp = data.packet();
                if (patt0$temp instanceof final class_2886 interactPacket) {
                    final class_1799 stack = CustomCoolDownModule.mc.field_1724.method_5998(interactPacket.method_12551());
                    final class_1792 item2 = stack.method_7909();
                    if (this.isItemEnabled(item2)) {
                        final long lastUse2 = this.lastUseMap.getOrDefault(item2, 0L);
                        final float delayMs2 = this.getCooldownForItem(item2) * 1000.0f;
                        if (System.currentTimeMillis() - lastUse2 >= delayMs2) {
                            this.lastUseMap.put(item2, System.currentTimeMillis());
                        }
                    }
                }
                return;
            }
        }));
        this.addEvents(updateEvent, packetEvent);
    }
    
    private float getCooldownForItem(final class_1792 item) {
        if (item == class_1802.field_8463) {
            return this.appleTime.getValue();
        }
        if (item == class_1802.field_8634) {
            return this.pearlTime.getValue();
        }
        if (item == class_1802.field_8233) {
            return this.horusTime.getValue();
        }
        return 0.0f;
    }
    
    private boolean isItemEnabled(final class_1792 item) {
        if (item == class_1802.field_8463) {
            return this.items.isEnabled("\u0417\u043e\u043b\u043e\u0442\u043e\u0435 \u044f\u0431\u043b\u043e\u043a\u043e");
        }
        if (item == class_1802.field_8634) {
            return this.items.isEnabled("\u042d\u043d\u0434\u0435\u0440-\u0436\u0435\u043c\u0447\u044e\u0433");
        }
        return item == class_1802.field_8233 && this.items.isEnabled("\u041f\u043b\u043e\u0434 \u0445\u043e\u0440\u0443\u0441\u0430");
    }
    
    @Generated
    public static CustomCoolDownModule getInstance() {
        return CustomCoolDownModule.instance;
    }
    
    static {
        instance = new CustomCoolDownModule();
    }
}
