package org.ladysnake.satin.impl;

import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import java.util.Set;
import net.minecraft.class_1921;

/* JADX INFO: loaded from: leonware-0.0.3.jar:META-INF/jars/satin-3.0.0-alpha.1.jar:org/ladysnake/satin/impl/BlockRenderLayerRegistry.class */
public final class BlockRenderLayerRegistry {
    public static final BlockRenderLayerRegistry INSTANCE = new BlockRenderLayerRegistry();
    private final Set<class_1921> renderLayers = new ObjectArraySet();
    private volatile boolean registryLocked = false;

    private BlockRenderLayerRegistry() {
    }

    public void registerRenderLayer(class_1921 layer) {
        if (this.registryLocked) {
            throw new IllegalStateException(String.format("RenderLayer %s was added too late.", layer));
        }
        this.renderLayers.add(layer);
    }

    public Set<class_1921> getLayers() {
        this.registryLocked = true;
        return this.renderLayers;
    }
}
