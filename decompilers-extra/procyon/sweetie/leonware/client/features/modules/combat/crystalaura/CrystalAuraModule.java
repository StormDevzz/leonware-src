// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.combat.crystalaura;

import lombok.Generated;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.events.render.Render3DEvent;
import sweetie.leonware.api.event.events.render.Render2DEvent;
import sweetie.leonware.api.event.events.client.PacketEvent;
import java.util.function.Consumer;
import sweetie.leonware.api.event.Listener;
import java.util.Objects;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Crystal Aura", category = Category.COMBAT)
public class CrystalAuraModule extends Module
{
    private static final CrystalAuraModule instance;
    public final CrystalAuraProcessor processor;
    
    public CrystalAuraModule() {
        (this.processor = new CrystalAuraProcessor(this)).initSettings(this);
    }
    
    @Override
    public void onDisable() {
        this.processor.reset();
    }
    
    @Override
    public void onEvent() {
        final UpdateEvent instance = UpdateEvent.getInstance();
        final CrystalAuraProcessor processor = this.processor;
        Objects.requireNonNull(processor);
        final EventListener updateEvent = instance.subscribe(new Listener<UpdateEvent>((Consumer<Object>)processor::onUpdate));
        final PacketEvent instance2 = PacketEvent.getInstance();
        final CrystalAuraProcessor processor2 = this.processor;
        Objects.requireNonNull(processor2);
        final EventListener packetEvent = instance2.subscribe(new Listener<PacketEvent.PacketEventData>((Consumer<Object>)processor2::onPacket));
        final Render2DEvent instance3 = Render2DEvent.getInstance();
        final CrystalAuraProcessor processor3 = this.processor;
        Objects.requireNonNull(processor3);
        final EventListener render2DEvent = instance3.subscribe(new Listener<Render2DEvent.Render2DEventData>((Consumer<Object>)processor3::onRender2D));
        final Render3DEvent instance4 = Render3DEvent.getInstance();
        final CrystalAuraProcessor processor4 = this.processor;
        Objects.requireNonNull(processor4);
        final EventListener renderEvent = instance4.subscribe(new Listener<Render3DEvent.Render3DEventData>((Consumer<Object>)processor4::onRender3D));
        this.addEvents(updateEvent, packetEvent, render2DEvent, renderEvent);
    }
    
    @Generated
    public static CrystalAuraModule getInstance() {
        return CrystalAuraModule.instance;
    }
    
    static {
        instance = new CrystalAuraModule();
    }
}
