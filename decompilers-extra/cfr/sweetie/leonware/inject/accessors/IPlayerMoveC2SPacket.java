/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2828
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Mutable
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package sweetie.leonware.inject.accessors;

import net.minecraft.class_2828;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={class_2828.class})
public interface IPlayerMoveC2SPacket {
    @Mutable
    @Accessor(value="x")
    public double getX();

    @Accessor(value="y")
    public double getY();

    @Accessor(value="z")
    public double getZ();

    @Accessor(value="yaw")
    public float getYaw();

    @Accessor(value="pitch")
    public float getPitch();

    @Accessor(value="horizontalCollision")
    public boolean isHorizontalCollision();

    @Accessor(value="onGround")
    public void setOnGround(boolean var1);
}

