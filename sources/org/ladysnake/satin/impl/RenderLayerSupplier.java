package org.ladysnake.satin.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.class_10156;
import net.minecraft.class_1921;
import net.minecraft.class_293;
import net.minecraft.class_4668;
import org.jetbrains.annotations.Nullable;
import org.ladysnake.satin.mixin.client.render.RenderPhaseAccessor;

/* JADX INFO: loaded from: leonware-0.0.3.jar:META-INF/jars/satin-3.0.0-alpha.1.jar:org/ladysnake/satin/impl/RenderLayerSupplier.class */
public class RenderLayerSupplier {
    private final Consumer<class_1921.class_4688.class_4689> transform;
    private final Map<class_1921, class_1921> renderLayerCache;
    private final String uniqueName;

    @Nullable
    private final class_293 vertexFormat;

    public static RenderLayerSupplier framebuffer(String name, Runnable setupState, Runnable cleanupState) {
        class_4668.class_4678 target = new class_4668.class_4678(name + "_target", setupState, cleanupState);
        return new RenderLayerSupplier(name, builder -> {
            builder.method_23610(target);
        });
    }

    public static RenderLayerSupplier shader(String name, class_293 vertexFormat, class_10156 shaderKey) {
        class_4668.class_5942 class_5942Var = new class_4668.class_5942(shaderKey);
        return new RenderLayerSupplier(name, vertexFormat, builder -> {
            builder.method_34578((class_4668.class_5942) class_5942Var);
        });
    }

    public RenderLayerSupplier(String name, Consumer<class_1921.class_4688.class_4689> transformer) {
        this(name, null, transformer);
    }

    public RenderLayerSupplier(String name, @Nullable class_293 vertexFormat, Consumer<class_1921.class_4688.class_4689> transformer) {
        this.renderLayerCache = new HashMap();
        this.uniqueName = name;
        this.vertexFormat = vertexFormat;
        this.transform = transformer;
    }

    public class_1921 getRenderLayer(class_1921 baseLayer) {
        class_1921 existing = this.renderLayerCache.get(baseLayer);
        if (existing != null) {
            return existing;
        }
        String newName = ((RenderPhaseAccessor) baseLayer).getName() + "_" + this.uniqueName;
        class_1921 newLayer = RenderLayerDuplicator.copy(baseLayer, newName, this.vertexFormat, this.transform);
        this.renderLayerCache.put(baseLayer, newLayer);
        return newLayer;
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:META-INF/jars/satin-3.0.0-alpha.1.jar:org/ladysnake/satin/impl/RenderLayerSupplier$Helper.class */
    private static class Helper extends class_4668 {
        private Helper(String name, Runnable beginAction, Runnable endAction) {
            super(name, beginAction, endAction);
        }
    }
}
