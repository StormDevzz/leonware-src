package org.ladysnake.satin.mixin.client.blockrenderlayer;

import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import java.util.List;
import net.minecraft.class_1921;
import net.minecraft.class_4668;
import org.ladysnake.satin.impl.BlockRenderLayerRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/* JADX INFO: loaded from: leonware-0.0.3.jar:META-INF/jars/satin-3.0.0-alpha.1.jar:org/ladysnake/satin/mixin/client/blockrenderlayer/RenderLayerMixin.class */
@Mixin({class_1921.class})
public abstract class RenderLayerMixin extends class_4668 {
    private RenderLayerMixin() {
        super((String) null, (Runnable) null, (Runnable) null);
    }

    @ModifyReturnValue(method = {"getBlockLayers"}, at = {@At("RETURN")})
    private static List<class_1921> getBlockLayers(List<class_1921> original) {
        return ImmutableList.builder().addAll(original).addAll(BlockRenderLayerRegistry.INSTANCE.getLayers()).build();
    }
}
