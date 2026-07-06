package sweetie.leonware.inject.accessors;

import net.minecraft.class_636;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/inject/accessors/ClientPlayerInteractionManagerAccessor.class */
@Mixin({class_636.class})
public interface ClientPlayerInteractionManagerAccessor {
    @Invoker("syncSelectedSlot")
    void leonware$syncSelectedSlot();
}
