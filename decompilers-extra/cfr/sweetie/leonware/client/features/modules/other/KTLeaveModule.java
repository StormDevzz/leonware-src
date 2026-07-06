/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_2338
 *  net.minecraft.class_2350
 *  net.minecraft.class_2596
 *  net.minecraft.class_2846
 *  net.minecraft.class_2846$class_2847
 */
package sweetie.leonware.client.features.modules.other;

import lombok.Generated;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2596;
import net.minecraft.class_2846;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.DisconnectEvent;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.SliderSetting;

@ModuleRegister(name="KTLeave", category=Category.OTHER)
public class KTLeaveModule
extends Module {
    private static final KTLeaveModule instance = new KTLeaveModule();
    public final SliderSetting packets = new SliderSetting("\u041a\u043e\u043b-\u0432\u043e \u043f\u0430\u043a\u0435\u0442\u043e\u0432").value(Float.valueOf(5.0f)).range(1.0f, 7.0f).step(1.0f);
    private int ticks = 0;

    public KTLeaveModule() {
        this.addSettings(this.packets);
    }

    @Override
    public void onEnable() {
        this.ticks = 0;
    }

    @Override
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (KTLeaveModule.mc.field_1724 == null || KTLeaveModule.mc.field_1687 == null || mc.method_1562() == null) {
                this.disable();
                return;
            }
            ++this.ticks;
            if (this.ticks >= 1) {
                this.ticks = 0;
                int count = ((Float)this.packets.getValue()).intValue();
                for (int i = 0; i < count; ++i) {
                    mc.method_1562().method_52787((class_2596)new class_2846(class_2846.class_2847.field_12971, class_2338.field_10980, class_2350.field_11033));
                }
            }
        }));
        EventListener disconnectListener = DisconnectEvent.getInstance().subscribe(new Listener<DisconnectEvent.DisconnectData>(event -> this.disable()));
        this.addEvents(updateEvent, disconnectListener);
    }

    @Generated
    public static KTLeaveModule getInstance() {
        return instance;
    }
}

