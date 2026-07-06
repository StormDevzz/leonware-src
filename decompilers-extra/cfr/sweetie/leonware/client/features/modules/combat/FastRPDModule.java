/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1268
 *  net.minecraft.class_1657
 */
package sweetie.leonware.client.features.modules.combat;

import lombok.Generated;
import net.minecraft.class_1268;
import net.minecraft.class_1657;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;

@ModuleRegister(name="Fast RPD", category=Category.COMBAT)
public class FastRPDModule
extends Module {
    private static final FastRPDModule instance = new FastRPDModule();
    private final BooleanSetting antiKick = new BooleanSetting("Anti Kick").value(false);
    private final SliderSetting clickDelay = new SliderSetting("\u0417\u0430\u0434\u0435\u0440\u0436\u043a\u0430 (\u043c\u0441)").value(Float.valueOf(1.0f)).range(1.0f, 100.0f).step(1.0f).setVisible(() -> (Boolean)this.antiKick.getValue());
    private long lastClickTime = 0L;

    public FastRPDModule() {
        this.addSettings(this.antiKick, this.clickDelay);
    }

    @Override
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            long delay;
            if (FastRPDModule.mc.field_1724 == null || FastRPDModule.mc.field_1687 == null || FastRPDModule.mc.field_1761 == null) {
                return;
            }
            long currentTime = System.currentTimeMillis();
            long l = delay = (Boolean)this.antiKick.getValue() != false ? ((Float)this.clickDelay.getValue()).longValue() : 1L;
            if (currentTime - this.lastClickTime >= delay) {
                FastRPDModule.mc.field_1761.method_2919((class_1657)FastRPDModule.mc.field_1724, class_1268.field_5808);
                this.lastClickTime = currentTime;
            }
        }));
        this.addEvents(updateEvent);
    }

    @Generated
    public static FastRPDModule getInstance() {
        return instance;
    }
}

