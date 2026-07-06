/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1294
 *  net.minecraft.class_243
 */
package sweetie.leonware.client.features.modules.player;

import lombok.Generated;
import net.minecraft.class_1294;
import net.minecraft.class_243;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;

@ModuleRegister(name="Levitation Control", category=Category.PLAYER)
public class LevitationControlModule
extends Module {
    private static final LevitationControlModule instance = new LevitationControlModule();
    private final SliderSetting up = new SliderSetting("Up Speed").value(Float.valueOf(0.5f)).range(0.0f, 2.0f).step(0.1f);
    private final SliderSetting down = new SliderSetting("Down Speed").value(Float.valueOf(0.5f)).range(0.0f, 2.0f).step(0.1f);
    private final BooleanSetting stuck = new BooleanSetting("Freeze").value(true);

    public LevitationControlModule() {
        this.addSettings(this.up, this.down, this.stuck);
    }

    @Override
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (LevitationControlModule.mc.field_1724 == null || LevitationControlModule.mc.field_1687 == null) {
                return;
            }
            if (!LevitationControlModule.mc.field_1724.method_6059(class_1294.field_5902)) {
                return;
            }
            double verticalVelocity = LevitationControlModule.mc.field_1690.field_1903.method_1434() ? (double)((Float)this.up.getValue()).floatValue() : (LevitationControlModule.mc.field_1690.field_1832.method_1434() ? (double)(-((Float)this.down.getValue()).floatValue()) : ((Boolean)this.stuck.getValue() != false ? 0.0 : LevitationControlModule.mc.field_1724.method_18798().field_1351));
            if (Math.abs(LevitationControlModule.mc.field_1724.method_18798().field_1351 - verticalVelocity) > 0.001) {
                class_243 currentVel = LevitationControlModule.mc.field_1724.method_18798();
                LevitationControlModule.mc.field_1724.method_18800(currentVel.field_1352, verticalVelocity, currentVel.field_1350);
            }
        }));
        this.addEvents(updateEvent);
    }

    @Generated
    public static LevitationControlModule getInstance() {
        return instance;
    }
}

