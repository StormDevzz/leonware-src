package sweetie.leonware.inject.accessors;

import net.minecraft.class_2561;
import net.minecraft.class_8113;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/inject/accessors/ITextDisplayEntity.class */
@Mixin({class_8113.class_8123.class})
public interface ITextDisplayEntity {
    @Invoker("getText")
    class_2561 invokeGetText();
}
