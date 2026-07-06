/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package sweetie.leonware.client.features.modules.combat.crystalaura;

import lombok.Generated;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.event.events.render.Render2DEvent;
import sweetie.leonware.api.event.events.render.Render3DEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.client.features.modules.combat.crystalaura.CrystalAuraProcessor;

@ModuleRegister(name="Crystal Aura", category=Category.COMBAT)
public class CrystalAuraModule
extends Module {
    private static final CrystalAuraModule instance = new CrystalAuraModule();
    public final CrystalAuraProcessor processor = new CrystalAuraProcessor(this);

    public CrystalAuraModule() {
        this.processor.initSettings(this);
    }

    @Override
    public void onDisable() {
        this.processor.reset();
    }

    @Override
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(this.processor::onUpdate));
        EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener<PacketEvent.PacketEventData>(this.processor::onPacket));
        EventListener render2DEvent = Render2DEvent.getInstance().subscribe(new Listener<Render2DEvent.Render2DEventData>(this.processor::onRender2D));
        EventListener renderEvent = Render3DEvent.getInstance().subscribe(new Listener<Render3DEvent.Render3DEventData>(this.processor::onRender3D));
        this.addEvents(updateEvent, packetEvent, render2DEvent, renderEvent);
    }

    @Generated
    public static CrystalAuraModule getInstance() {
        return instance;
    }
}

