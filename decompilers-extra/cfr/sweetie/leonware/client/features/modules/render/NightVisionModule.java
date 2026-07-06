/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1293
 *  net.minecraft.class_1294
 */
package sweetie.leonware.client.features.modules.render;

import lombok.Generated;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.TickEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;

@ModuleRegister(name="Night Vision", category=Category.RENDER)
public class NightVisionModule
extends Module {
    private static final NightVisionModule instance = new NightVisionModule();

    @Override
    public void onDisable() {
        this.remove();
    }

    @Override
    public void onEvent() {
        EventListener tickEvent = TickEvent.getInstance().subscribe(new Listener<TickEvent>(event -> this.add()));
        this.addEvents(tickEvent);
    }

    private void remove() {
        if (NightVisionModule.mc.field_1724 == null) {
            return;
        }
        NightVisionModule.mc.field_1724.method_6016(class_1294.field_5925);
    }

    private void add() {
        if (NightVisionModule.mc.field_1724 == null) {
            return;
        }
        NightVisionModule.mc.field_1724.method_6092(new class_1293(class_1294.field_5925, -1, 0, false, false, false));
    }

    @Generated
    public static NightVisionModule getInstance() {
        return instance;
    }
}

