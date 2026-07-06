// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.player;

import lombok.Generated;
import net.minecraft.class_2248;
import net.minecraft.class_239;
import net.minecraft.class_3965;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Auto Tool", category = Category.PLAYER)
public class AutoToolModule extends Module
{
    private static final AutoToolModule instance;
    private int lastSlot;
    
    public AutoToolModule() {
        this.lastSlot = -1;
    }
    
    @Override
    public void onEvent() {
        final EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (AutoToolModule.mc.field_1724 == null || AutoToolModule.mc.field_1687 == null || AutoToolModule.mc.field_1761 == null || AutoToolModule.mc.field_1724.method_7337()) {
                this.lastSlot = -1;
                return;
            }
            else {
                final boolean breaking = AutoToolModule.mc.field_1761.method_2923();
                if (breaking && this.lastSlot == -1) {
                    this.lastSlot = AutoToolModule.mc.field_1724.method_31548().field_7545;
                }
                AutoToolModule.mc.field_1724.method_31548().field_7545 = (breaking ? this.getBestToolSlot() : ((this.lastSlot != -1) ? this.lastSlot : AutoToolModule.mc.field_1724.method_31548().field_7545));
                if (!breaking) {
                    this.lastSlot = -1;
                }
                return;
            }
        }));
        this.addEvents(updateEvent);
    }
    
    private int getBestToolSlot() {
        final class_239 field_1765 = AutoToolModule.mc.field_1765;
        if (field_1765 instanceof final class_3965 hit) {
            final class_2248 block = AutoToolModule.mc.field_1687.method_8320(hit.method_17777()).method_26204();
            int bestSlot = AutoToolModule.mc.field_1724.method_31548().field_7545;
            float bestSpeed = 1.0f;
            for (int i = 0; i < 9; ++i) {
                final float speed = AutoToolModule.mc.field_1724.method_31548().method_5438(i).method_7924(block.method_9564());
                if (speed > bestSpeed) {
                    bestSpeed = speed;
                    bestSlot = i;
                }
            }
            return bestSlot;
        }
        return AutoToolModule.mc.field_1724.method_31548().field_7545;
    }
    
    @Override
    public void onDisable() {
        this.lastSlot = -1;
    }
    
    @Generated
    public static AutoToolModule getInstance() {
        return AutoToolModule.instance;
    }
    
    static {
        instance = new AutoToolModule();
    }
}
