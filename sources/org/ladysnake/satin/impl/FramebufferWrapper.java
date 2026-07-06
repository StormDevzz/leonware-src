package org.ladysnake.satin.impl;

import javax.annotation.Nullable;
import net.minecraft.class_1041;
import net.minecraft.class_1921;
import net.minecraft.class_276;
import net.minecraft.class_279;
import net.minecraft.class_310;
import org.ladysnake.satin.Satin;
import org.ladysnake.satin.api.managed.ManagedFramebuffer;
import org.ladysnake.satin.mixin.client.AccessiblePassesShaderEffect;

/* JADX INFO: loaded from: leonware-0.0.3.jar:META-INF/jars/satin-3.0.0-alpha.1.jar:org/ladysnake/satin/impl/FramebufferWrapper.class */
public final class FramebufferWrapper implements ManagedFramebuffer {
    private final RenderLayerSupplier renderLayerSupplier;
    private final String name;

    @Nullable
    private class_276 wrapped;

    FramebufferWrapper(String name) {
        this.name = name;
        this.renderLayerSupplier = RenderLayerSupplier.framebuffer(this.name + System.identityHashCode(this), () -> {
            beginWrite(false);
        }, () -> {
            class_310.method_1551().method_1522().method_1235(false);
        });
    }

    void findTarget(@Nullable class_279 shaderEffect) {
        if (shaderEffect == null) {
            this.wrapped = null;
            return;
        }
        this.wrapped = null;
        ((AccessiblePassesShaderEffect) shaderEffect).getInternalTargets().get(this.name);
        if (this.wrapped == null) {
            Satin.LOGGER.warn("No target framebuffer found with name {} in shader", this.name);
        }
    }

    public String getName() {
        return this.name;
    }

    @Override // org.ladysnake.satin.api.managed.ManagedFramebuffer
    @Nullable
    public class_276 getFramebuffer() {
        return this.wrapped;
    }

    @Override // org.ladysnake.satin.api.managed.ManagedFramebuffer
    public void copyDepthFrom(class_276 buffer) {
        if (this.wrapped != null) {
            this.wrapped.method_29329(buffer);
        }
    }

    @Override // org.ladysnake.satin.api.managed.ManagedFramebuffer
    public void beginWrite(boolean updateViewport) {
        if (this.wrapped != null) {
            this.wrapped.method_1235(updateViewport);
        }
    }

    @Override // org.ladysnake.satin.api.managed.ManagedFramebuffer
    public void draw() {
        class_1041 window = class_310.method_1551().method_22683();
        draw(window.method_4489(), window.method_4506(), true);
    }

    @Override // org.ladysnake.satin.api.managed.ManagedFramebuffer
    public void draw(int width, int height, boolean disableBlend) {
        if (this.wrapped != null) {
            this.wrapped.method_1237(width, height);
        }
    }

    @Override // org.ladysnake.satin.api.managed.ManagedFramebuffer
    public void clear() {
        if (this.wrapped != null) {
            this.wrapped.method_1230();
        }
    }

    @Override // org.ladysnake.satin.api.managed.ManagedFramebuffer
    public class_1921 getRenderLayer(class_1921 baseLayer) {
        return this.renderLayerSupplier.getRenderLayer(baseLayer);
    }
}
