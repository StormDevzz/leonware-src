// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.movement.noslow.modes;

import net.minecraft.class_2338;
import net.minecraft.class_2846;
import net.minecraft.class_2596;
import net.minecraft.class_2886;
import net.minecraft.class_1268;
import sweetie.leonware.client.features.modules.movement.noslow.NoSlowMode;

public class NoSlowGrim extends NoSlowMode
{
    private int ticks;
    private float ticksS;
    public BypassType bypassType;
    
    public NoSlowGrim() {
        this.ticks = 0;
        this.ticksS = 0.0f;
        this.bypassType = BypassType.TICK;
    }
    
    @Override
    public String getName() {
        return "Grim";
    }
    
    @Override
    public void onUpdate() {
        switch (this.bypassType.ordinal()) {
            case 2: {
                if (this.slowingCancel() && NoSlowGrim.mc.field_1724.method_6115()) {
                    final class_1268 hand = (NoSlowGrim.mc.field_1724.method_6058() == class_1268.field_5808) ? class_1268.field_5810 : class_1268.field_5808;
                    this.sendPacket((class_2596<?>)new class_2886(hand, 0, NoSlowGrim.mc.field_1724.method_36454(), NoSlowGrim.mc.field_1724.method_36455()));
                    break;
                }
                break;
            }
            case 3: {
                if (NoSlowGrim.mc.field_1724.method_6058() == class_1268.field_5810) {
                    return;
                }
                if (NoSlowGrim.mc.field_1724.method_6048() < 4) {
                    return;
                }
                if (NoSlowGrim.mc.field_1724.method_6048() == 5) {
                    this.sendPacket((class_2596<?>)new class_2846(class_2846.class_2847.field_12970, class_2338.field_10980, NoSlowGrim.mc.field_1724.method_5735()));
                    break;
                }
                break;
            }
        }
    }
    
    @Override
    public void onTick() {
        if (NoSlowGrim.mc.field_1724.method_6115()) {
            ++this.ticks;
            ++this.ticksS;
        }
        else {
            this.ticks = 0;
            this.ticksS = 0.0f;
        }
    }
    
    @Override
    public boolean slowingCancel() {
        final boolean tickRule = this.ticks >= 2;
        if (tickRule) {
            this.ticks = 0;
        }
        boolean cancelRule = false;
        switch (this.bypassType.ordinal()) {
            case 0: {
                cancelRule = tickRule;
                break;
            }
            case 1: {
                if (NoSlowGrim.mc.field_1724.method_6115() && !NoSlowGrim.mc.field_1724.method_5765() && this.ticksS >= 1.3f) {
                    cancelRule = true;
                    this.ticksS = 0.26f;
                    break;
                }
                break;
            }
            case 3: {
                if (NoSlowGrim.mc.field_1724.method_6058() == class_1268.field_5810) {
                    return false;
                }
                cancelRule = (NoSlowGrim.mc.field_1724.method_6048() > 6);
                break;
            }
            case 2: {
                cancelRule = true;
                break;
            }
        }
        return cancelRule;
    }
    
    public enum BypassType
    {
        TICK, 
        TICKS, 
        OLD, 
        DROP;
    }
}
