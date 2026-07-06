package sweetie.leonware.inject.accessors;

import net.minecraft.class_2886;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/inject/accessors/AccessorPlayerInteractItemC2SPacket.class */
@Mixin({class_2886.class})
public interface AccessorPlayerInteractItemC2SPacket {
    @Accessor("yaw")
    @Mutable
    void leonware$setYaw(float f);

    @Accessor("pitch")
    @Mutable
    void leonware$setPitch(float f);
}
