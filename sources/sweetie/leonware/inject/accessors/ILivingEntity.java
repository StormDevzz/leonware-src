package sweetie.leonware.inject.accessors;

import net.minecraft.class_1309;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/inject/accessors/ILivingEntity.class */
@Mixin({class_1309.class})
public interface ILivingEntity {
    @Accessor("jumpingCooldown")
    @Mutable
    void setJumpingCooldown(int i);
}
