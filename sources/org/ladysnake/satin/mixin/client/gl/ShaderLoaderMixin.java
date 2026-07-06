package org.ladysnake.satin.mixin.client.gl;

import java.util.Set;
import net.minecraft.class_10151;
import net.minecraft.class_279;
import net.minecraft.class_2960;
import org.ladysnake.satin.impl.ShaderLoaderExt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/* JADX INFO: loaded from: leonware-0.0.3.jar:META-INF/jars/satin-3.0.0-alpha.1.jar:org/ladysnake/satin/mixin/client/gl/ShaderLoaderMixin.class */
@Mixin({class_10151.class})
public abstract class ShaderLoaderMixin implements ShaderLoaderExt {

    @Shadow
    private class_10151.class_10170 field_54020;

    @Override // org.ladysnake.satin.impl.ShaderLoaderExt
    public class_279 satin$loadUnchecked(class_2960 id, Set<class_2960> availableExternalTargets) throws class_10151.class_10152 {
        return this.field_54020.method_63523(id, availableExternalTargets);
    }
}
