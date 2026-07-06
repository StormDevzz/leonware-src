// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.movement.noslow.modes;

import net.minecraft.class_2596;
import net.minecraft.class_2868;
import sweetie.leonware.client.features.modules.movement.noslow.NoSlowMode;

public class NoSlowSlotUpdate extends NoSlowMode
{
    @Override
    public String getName() {
        return "Slot update";
    }
    
    @Override
    public void onUpdate() {
        this.sendPacket((class_2596<?>)new class_2868(NoSlowSlotUpdate.mc.field_1724.method_31548().field_7545));
    }
    
    @Override
    public void onTick() {
    }
    
    @Override
    public boolean slowingCancel() {
        return true;
    }
}
