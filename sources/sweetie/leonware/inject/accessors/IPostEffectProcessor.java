package sweetie.leonware.inject.accessors;

import java.util.List;
import net.minecraft.class_279;
import net.minecraft.class_283;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/inject/accessors/IPostEffectProcessor.class */
@Mixin({class_279.class})
public interface IPostEffectProcessor {
    @Accessor("passes")
    List<class_283> leonware$getPasses();
}
