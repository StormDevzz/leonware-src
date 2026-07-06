// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.inject.accessors;

import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.Mutable;
import net.minecraft.class_2828;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ class_2828.class })
public interface IPlayerMoveC2SPacket
{
    @Mutable
    @Accessor("x")
    double getX();
    
    @Accessor("y")
    double getY();
    
    @Accessor("z")
    double getZ();
    
    @Accessor("yaw")
    float getYaw();
    
    @Accessor("pitch")
    float getPitch();
    
    @Accessor("horizontalCollision")
    boolean isHorizontalCollision();
    
    @Accessor("onGround")
    void setOnGround(final boolean p0);
}
