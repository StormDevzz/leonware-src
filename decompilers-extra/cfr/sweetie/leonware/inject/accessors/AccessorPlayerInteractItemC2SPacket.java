/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2886
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Mutable
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package sweetie.leonware.inject.accessors;

import net.minecraft.class_2886;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={class_2886.class})
public interface AccessorPlayerInteractItemC2SPacket {
    @Mutable
    @Accessor(value="yaw")
    public void leonware$setYaw(float var1);

    @Mutable
    @Accessor(value="pitch")
    public void leonware$setPitch(float var1);
}

