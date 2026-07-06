package sweetie.leonware.inject.accessors;

import net.minecraft.class_2828;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/inject/accessors/IPlayerMoveC2SPacket.class */
@Mixin({class_2828.class})
public interface IPlayerMoveC2SPacket {
    @Accessor("x")
    @Mutable
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
    void setOnGround(boolean z);
}
