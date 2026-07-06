/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1747
 */
package sweetie.leonware.client.features.modules.player;

import lombok.Generated;
import net.minecraft.class_1747;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;

@ModuleRegister(name="Delay Helper", category=Category.PLAYER)
public class DelayHelperModule
extends Module {
    private static final DelayHelperModule instance = new DelayHelperModule();
    private final BooleanSetting fastUse = new BooleanSetting("\u0411\u044b\u0441\u0442\u0440\u043e\u0435 \u0438\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u0435").value(true);
    private final BooleanSetting fastPlace = new BooleanSetting("\u0411\u044b\u0441\u0442\u0440\u0430\u044f \u0443\u0441\u0442\u0430\u043d\u043e\u0432\u043a\u0430").value(true);

    public DelayHelperModule() {
        this.addSettings(this.fastUse, this.fastPlace);
    }

    @Override
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (DelayHelperModule.mc.field_1724 == null) {
                return;
            }
            if (((Boolean)this.fastUse.getValue()).booleanValue() && !(DelayHelperModule.mc.field_1724.method_6047().method_7909() instanceof class_1747) && !(DelayHelperModule.mc.field_1724.method_6079().method_7909() instanceof class_1747)) {
                DelayHelperModule.mc.field_1752 = 0;
            }
            if (((Boolean)this.fastPlace.getValue()).booleanValue() && (DelayHelperModule.mc.field_1724.method_6047().method_7909() instanceof class_1747 || DelayHelperModule.mc.field_1724.method_6079().method_7909() instanceof class_1747)) {
                DelayHelperModule.mc.field_1752 = 0;
            }
        }));
        this.addEvents(updateEvent);
    }

    @Generated
    public static DelayHelperModule getInstance() {
        return instance;
    }
}

