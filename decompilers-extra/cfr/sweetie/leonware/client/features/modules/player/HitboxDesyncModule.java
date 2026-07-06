/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_2338
 *  net.minecraft.class_2350
 *  net.minecraft.class_2374
 *  net.minecraft.class_238
 *  net.minecraft.class_2382
 *  net.minecraft.class_243
 */
package sweetie.leonware.client.features.modules.player;

import lombok.Generated;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2374;
import net.minecraft.class_238;
import net.minecraft.class_2382;
import net.minecraft.class_243;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.TickEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;

@ModuleRegister(name="Hitbox Desync", category=Category.PLAYER)
public class HitboxDesyncModule
extends Module {
    private static final HitboxDesyncModule instance = new HitboxDesyncModule();
    private static final double DONYKA_SEX = 0.20000996883537;

    @Override
    public void onEvent() {
        EventListener tickEvent = TickEvent.getInstance().subscribe(new Listener<TickEvent>(event -> {
            class_2350 f = HitboxDesyncModule.mc.field_1724.method_5735();
            class_238 bb = HitboxDesyncModule.mc.field_1724.method_5829();
            class_243 center = bb.method_1005();
            class_243 offset = new class_243(f.method_23955());
            class_243 fin = this.merge(class_243.method_24954((class_2382)class_2338.method_49638((class_2374)center)).method_1031(0.5, 0.0, 0.5).method_1019(offset.method_1021(0.20000996883537)), f);
            HitboxDesyncModule.mc.field_1724.method_5814(fin.field_1352 == 0.0 ? HitboxDesyncModule.mc.field_1724.method_23317() : fin.field_1352, HitboxDesyncModule.mc.field_1724.method_23318(), fin.field_1350 == 0.0 ? HitboxDesyncModule.mc.field_1724.method_23321() : fin.field_1350);
            this.toggle();
        }));
        this.addEvents(tickEvent);
    }

    private class_243 merge(class_243 a, class_2350 facing) {
        return new class_243(a.field_1352 * (double)Math.abs(facing.method_23955().x()), a.field_1351 * (double)Math.abs(facing.method_23955().y()), a.field_1350 * (double)Math.abs(facing.method_23955().z()));
    }

    @Generated
    public static HitboxDesyncModule getInstance() {
        return instance;
    }
}

