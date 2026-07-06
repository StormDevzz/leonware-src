// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.player;

import lombok.Generated;
import net.minecraft.class_238;
import net.minecraft.class_2350;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import net.minecraft.class_2382;
import net.minecraft.class_2374;
import net.minecraft.class_2338;
import net.minecraft.class_243;
import sweetie.leonware.api.event.events.client.TickEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Hitbox Desync", category = Category.PLAYER)
public class HitboxDesyncModule extends Module
{
    private static final HitboxDesyncModule instance;
    private static final double DONYKA_SEX = 0.20000996883537;
    
    @Override
    public void onEvent() {
        final EventListener tickEvent = TickEvent.getInstance().subscribe(new Listener<TickEvent>(event -> {
            final class_2350 f = HitboxDesyncModule.mc.field_1724.method_5735();
            final class_238 bb = HitboxDesyncModule.mc.field_1724.method_5829();
            final class_243 center = bb.method_1005();
            final class_243 offset = new class_243(f.method_23955());
            final class_243 fin = this.merge(class_243.method_24954((class_2382)class_2338.method_49638((class_2374)center)).method_1031(0.5, 0.0, 0.5).method_1019(offset.method_1021(0.20000996883537)), f);
            HitboxDesyncModule.mc.field_1724.method_5814((fin.field_1352 == 0.0) ? HitboxDesyncModule.mc.field_1724.method_23317() : fin.field_1352, HitboxDesyncModule.mc.field_1724.method_23318(), (fin.field_1350 == 0.0) ? HitboxDesyncModule.mc.field_1724.method_23321() : fin.field_1350);
            this.toggle();
            return;
        }));
        this.addEvents(tickEvent);
    }
    
    private class_243 merge(final class_243 a, final class_2350 facing) {
        return new class_243(a.field_1352 * Math.abs(facing.method_23955().x()), a.field_1351 * Math.abs(facing.method_23955().y()), a.field_1350 * Math.abs(facing.method_23955().z()));
    }
    
    @Generated
    public static HitboxDesyncModule getInstance() {
        return HitboxDesyncModule.instance;
    }
    
    static {
        instance = new HitboxDesyncModule();
    }
}
