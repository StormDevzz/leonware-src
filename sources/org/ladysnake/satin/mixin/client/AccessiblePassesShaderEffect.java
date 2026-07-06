package org.ladysnake.satin.mixin.client;

import java.util.List;
import java.util.Map;
import net.minecraft.class_279;
import net.minecraft.class_283;
import net.minecraft.class_2960;
import net.minecraft.class_9962;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/* JADX INFO: loaded from: leonware-0.0.3.jar:META-INF/jars/satin-3.0.0-alpha.1.jar:org/ladysnake/satin/mixin/client/AccessiblePassesShaderEffect.class */
@Mixin({class_279.class})
public interface AccessiblePassesShaderEffect {
    @Accessor
    List<class_283> getPasses();

    @Accessor
    Map<class_2960, class_9962.class_9966> getInternalTargets();
}
