package sweetie.leonware.inject.accessors;

import net.minecraft.class_757;
import net.minecraft.class_9920;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/inject/accessors/IGameRenderer.class */
@Mixin({class_757.class})
public interface IGameRenderer {
    @Accessor("pool")
    class_9920 _getPool();
}
