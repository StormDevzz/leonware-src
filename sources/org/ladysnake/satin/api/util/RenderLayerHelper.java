package org.ladysnake.satin.api.util;

import java.util.function.Consumer;
import net.minecraft.class_1921;
import net.minecraft.class_4668;
import org.apiguardian.api.API;
import org.ladysnake.satin.impl.BlockRenderLayerRegistry;
import org.ladysnake.satin.impl.RenderLayerDuplicator;
import org.ladysnake.satin.mixin.client.render.RenderPhaseAccessor;

/* JADX INFO: loaded from: leonware-0.0.3.jar:META-INF/jars/satin-3.0.0-alpha.1.jar:org/ladysnake/satin/api/util/RenderLayerHelper.class */
@API(status = API.Status.EXPERIMENTAL, since = "1.4.0")
public final class RenderLayerHelper {
    @API(status = API.Status.EXPERIMENTAL, since = "1.5.0")
    public static String getName(class_4668 phase) {
        return ((RenderPhaseAccessor) phase).getName();
    }

    @API(status = API.Status.EXPERIMENTAL, since = "1.4.0")
    public static class_1921 copy(class_1921 existing, String newName, Consumer<class_1921.class_4688.class_4689> phaseTransform) {
        return RenderLayerDuplicator.copy(existing, newName, phaseTransform);
    }

    @API(status = API.Status.EXPERIMENTAL, since = "1.4.0")
    public static class_1921.class_4688 copyPhaseParameters(class_1921 existing, Consumer<class_1921.class_4688.class_4689> phaseTransform) {
        return RenderLayerDuplicator.copyPhaseParameters(existing, phaseTransform);
    }

    @API(status = API.Status.EXPERIMENTAL, since = "1.5.0")
    @Deprecated
    public static void registerBlockRenderLayer(class_1921 layer) {
        BlockRenderLayerRegistry.INSTANCE.registerRenderLayer(layer);
    }
}
