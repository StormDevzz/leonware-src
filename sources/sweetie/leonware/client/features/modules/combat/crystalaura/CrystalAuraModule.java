package sweetie.leonware.client.features.modules.combat.crystalaura;

import java.util.Objects;
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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/combat/crystalaura/CrystalAuraModule.class */
@ModuleRegister(name = "Crystal Aura", category = Category.COMBAT)
public class CrystalAuraModule extends Module {
    private static final CrystalAuraModule instance = new CrystalAuraModule();
    public final CrystalAuraProcessor processor = new CrystalAuraProcessor(this);

    @Generated
    public static CrystalAuraModule getInstance() {
        return instance;
    }

    public CrystalAuraModule() {
        this.processor.initSettings(this);
    }

    @Override // sweetie.leonware.api.module.Module
    public void onDisable() {
        this.processor.reset();
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        UpdateEvent updateEvent = UpdateEvent.getInstance();
        CrystalAuraProcessor crystalAuraProcessor = this.processor;
        Objects.requireNonNull(crystalAuraProcessor);
        EventListener updateEvent2 = updateEvent.subscribe(new Listener(crystalAuraProcessor::onUpdate));
        PacketEvent packetEvent = PacketEvent.getInstance();
        CrystalAuraProcessor crystalAuraProcessor2 = this.processor;
        Objects.requireNonNull(crystalAuraProcessor2);
        EventListener packetEvent2 = packetEvent.subscribe(new Listener(crystalAuraProcessor2::onPacket));
        Render2DEvent render2DEvent = Render2DEvent.getInstance();
        CrystalAuraProcessor crystalAuraProcessor3 = this.processor;
        Objects.requireNonNull(crystalAuraProcessor3);
        EventListener render2DEvent2 = render2DEvent.subscribe(new Listener(crystalAuraProcessor3::onRender2D));
        Render3DEvent render3DEvent = Render3DEvent.getInstance();
        CrystalAuraProcessor crystalAuraProcessor4 = this.processor;
        Objects.requireNonNull(crystalAuraProcessor4);
        EventListener renderEvent = render3DEvent.subscribe(new Listener(crystalAuraProcessor4::onRender3D));
        addEvents(updateEvent2, packetEvent2, render2DEvent2, renderEvent);
    }
}
