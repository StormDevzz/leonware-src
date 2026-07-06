/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_418
 */
package sweetie.leonware.client.features.modules.other;

import lombok.Generated;
import net.minecraft.class_418;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.TickEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;

@ModuleRegister(name="Auto Respawn", category=Category.OTHER)
public class AutoRespawnModule
extends Module {
    private static final AutoRespawnModule instance = new AutoRespawnModule();

    @Override
    public void onEvent() {
        EventListener tickEvent = TickEvent.getInstance().subscribe(new Listener<TickEvent>(event -> {
            if (AutoRespawnModule.mc.field_1755 instanceof class_418 && AutoRespawnModule.mc.field_1724.field_6213 > 2) {
                AutoRespawnModule.mc.field_1724.method_7331();
                mc.method_1507(null);
            }
        }));
        this.addEvents(tickEvent);
    }

    @Generated
    public static AutoRespawnModule getInstance() {
        return instance;
    }
}

