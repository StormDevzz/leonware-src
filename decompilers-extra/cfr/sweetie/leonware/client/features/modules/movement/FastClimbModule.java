/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_2510
 */
package sweetie.leonware.client.features.modules.movement;

import lombok.Generated;
import net.minecraft.class_2510;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.MultiBooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;

@ModuleRegister(name="Fast Climb", category=Category.MOVEMENT)
public class FastClimbModule
extends Module {
    private static final FastClimbModule instance = new FastClimbModule();
    private final MultiBooleanSetting mode = new MultiBooleanSetting("\u0420\u0435\u0436\u0438\u043c\u044b").value(new BooleanSetting("\u0421\u0442\u0443\u043f\u0435\u043d\u044c\u043a\u0438").value(true), new BooleanSetting("\u041b\u0435\u0441\u0442\u043d\u0438\u0446\u044b").value(true));
    private final SliderSetting ladderSpeed = new SliderSetting("\u0421\u043a\u043e\u0440\u043e\u0441\u0442\u044c \u043b\u0435\u0441\u0442\u043d\u0438\u0446").value(Float.valueOf(0.28f)).range(0.15f, 0.6f).step(0.01f);

    public FastClimbModule() {
        this.addSettings(this.mode, this.ladderSpeed);
    }

    @Override
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (FastClimbModule.mc.field_1724 == null || FastClimbModule.mc.field_1687 == null) {
                return;
            }
            if (this.mode.isEnabled("\u0421\u0442\u0443\u043f\u0435\u043d\u044c\u043a\u0438")) {
                this.handleStairs();
            }
            if (this.mode.isEnabled("\u041b\u0435\u0441\u0442\u043d\u0438\u0446\u044b")) {
                this.handleLadder();
            }
        }));
        this.addEvents(updateEvent);
    }

    private void handleStairs() {
        if (FastClimbModule.mc.field_1724.field_3913.method_3128().field_1342 > 0.01f && FastClimbModule.mc.field_1724.method_24828() && FastClimbModule.mc.field_1687.method_8320(FastClimbModule.mc.field_1724.method_24515().method_10074()).method_26204() instanceof class_2510) {
            FastClimbModule.mc.field_1724.method_6043();
        }
    }

    private void handleLadder() {
        if (FastClimbModule.mc.field_1724.method_6101() && FastClimbModule.mc.field_1724.field_3913.method_3128().field_1342 > 0.01f) {
            FastClimbModule.mc.field_1724.method_18800(FastClimbModule.mc.field_1724.method_18798().field_1352, ((Float)this.ladderSpeed.getValue()).doubleValue(), FastClimbModule.mc.field_1724.method_18798().field_1350);
        }
    }

    @Generated
    public static FastClimbModule getInstance() {
        return instance;
    }
}

