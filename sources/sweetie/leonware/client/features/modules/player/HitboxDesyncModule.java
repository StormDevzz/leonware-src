package sweetie.leonware.client.features.modules.player;

import lombok.Generated;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_243;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.TickEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/player/HitboxDesyncModule.class */
@ModuleRegister(name = "Hitbox Desync", category = Category.PLAYER)
public class HitboxDesyncModule extends Module {
    private static final HitboxDesyncModule instance = new HitboxDesyncModule();
    private static final double DONYKA_SEX = 0.20000996883537d;

    @Generated
    public static HitboxDesyncModule getInstance() {
        return instance;
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener tickEvent = TickEvent.getInstance().subscribe(new Listener(event -> {
            class_2350 f = mc.field_1724.method_5735();
            class_238 bb = mc.field_1724.method_5829();
            class_243 center = bb.method_1005();
            class_243 offset = new class_243(f.method_23955());
            class_243 fin = merge(class_243.method_24954(class_2338.method_49638(center)).method_1031(0.5d, 0.0d, 0.5d).method_1019(offset.method_1021(DONYKA_SEX)), f);
            mc.field_1724.method_5814(fin.field_1352 == 0.0d ? mc.field_1724.method_23317() : fin.field_1352, mc.field_1724.method_23318(), fin.field_1350 == 0.0d ? mc.field_1724.method_23321() : fin.field_1350);
            toggle();
        }));
        addEvents(tickEvent);
    }

    private class_243 merge(class_243 a, class_2350 facing) {
        return new class_243(a.field_1352 * ((double) Math.abs(facing.method_23955().x())), a.field_1351 * ((double) Math.abs(facing.method_23955().y())), a.field_1350 * ((double) Math.abs(facing.method_23955().z())));
    }
}
