package sweetie.leonware.inject.accessors;

import java.util.Map;
import net.minecraft.class_284;
import net.minecraft.class_5944;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/inject/accessors/IShaderProgram.class */
@Mixin({class_5944.class})
public interface IShaderProgram {
    @Accessor("uniformsByName")
    Map<String, class_284> leonware$getUniforms();
}
