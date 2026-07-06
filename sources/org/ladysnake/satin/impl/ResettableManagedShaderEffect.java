package org.ladysnake.satin.impl;

import com.google.common.base.Preconditions;
import com.mojang.blaze3d.systems.RenderSystem;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.class_10151;
import net.minecraft.class_1044;
import net.minecraft.class_276;
import net.minecraft.class_279;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_5912;
import net.minecraft.class_9960;
import org.apiguardian.api.API;
import org.joml.Matrix4f;
import org.ladysnake.satin.Satin;
import org.ladysnake.satin.api.managed.ManagedFramebuffer;
import org.ladysnake.satin.api.managed.ManagedShaderEffect;
import org.ladysnake.satin.api.managed.uniform.SamplerUniformV2;
import org.ladysnake.satin.mixin.client.AccessiblePassesShaderEffect;

/* JADX INFO: loaded from: leonware-0.0.3.jar:META-INF/jars/satin-3.0.0-alpha.1.jar:org/ladysnake/satin/impl/ResettableManagedShaderEffect.class */
public final class ResettableManagedShaderEffect extends ResettableManagedShaderBase<class_279> implements ManagedShaderEffect {
    private final Consumer<ManagedShaderEffect> initCallback;
    private final Map<String, FramebufferWrapper> managedTargets;
    private final Map<String, ManagedPassSamplerUniform> managedSamplers;

    @API(status = API.Status.INTERNAL)
    public ResettableManagedShaderEffect(class_2960 location, Consumer<ManagedShaderEffect> initCallback) {
        super(location);
        this.managedSamplers = new HashMap();
        this.initCallback = initCallback;
        this.managedTargets = new HashMap();
    }

    @Override // org.ladysnake.satin.api.managed.ManagedShaderEffect
    @Nullable
    public class_279 getShaderEffect() {
        return getShaderOrLog();
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: net.minecraft.class_10151$class_10152 */
    @Override // org.ladysnake.satin.api.managed.ManagedShaderEffect
    public void initialize() throws IOException {
        try {
            super.initialize(class_310.method_1551().method_1478());
        } catch (class_10151.class_10152 e) {
            throw new IOException((Throwable) e);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.ladysnake.satin.impl.ResettableManagedShaderBase
    public class_279 parseShader(class_5912 resourceFactory, class_310 mc, class_2960 location) throws class_10151.class_10152 {
        return mc.method_62887().satin$loadUnchecked(location, class_9960.field_53902);
    }

    @Override // org.ladysnake.satin.impl.ResettableManagedShaderBase
    public void setup() {
        Preconditions.checkNotNull((class_279) this.shader);
        for (ManagedUniformBase uniform : getManagedUniforms()) {
            setupUniform(uniform, (class_279) this.shader);
        }
        for (FramebufferWrapper buf : this.managedTargets.values()) {
            buf.findTarget((class_279) this.shader);
        }
        this.initCallback.accept(this);
    }

    @Override // org.ladysnake.satin.api.managed.ManagedShaderEffect
    public void render(float tickDelta) {
        class_279 sg = getShaderEffect();
        if (sg != null) {
            RenderSystem.disableBlend();
            RenderSystem.disableDepthTest();
            RenderSystem.resetTextureMatrix();
            class_310 client = class_310.method_1551();
            sg.method_1258(client.method_1522(), client.field_1773.getPool());
            client.method_1522().method_1235(true);
            RenderSystem.disableBlend();
            RenderSystem.blendFunc(770, 771);
            RenderSystem.enableDepthTest();
        }
    }

    @Override // org.ladysnake.satin.api.managed.ManagedShaderEffect
    public ManagedFramebuffer getTarget(String name) {
        return this.managedTargets.computeIfAbsent(name, n -> {
            FramebufferWrapper ret = new FramebufferWrapper(n);
            if (this.shader != 0) {
                ret.findTarget((class_279) this.shader);
            }
            return ret;
        });
    }

    @Override // org.ladysnake.satin.api.managed.ManagedShaderEffect
    public void setUniformValue(String uniformName, int value) {
        findUniform1i(uniformName).set(value);
    }

    @Override // org.ladysnake.satin.api.managed.ManagedShaderEffect
    public void setUniformValue(String uniformName, int value0, int value1) {
        findUniform2i(uniformName).set(value0, value1);
    }

    @Override // org.ladysnake.satin.api.managed.ManagedShaderEffect
    public void setUniformValue(String uniformName, int value0, int value1, int value2) {
        findUniform3i(uniformName).set(value0, value1, value2);
    }

    @Override // org.ladysnake.satin.api.managed.ManagedShaderEffect
    public void setUniformValue(String uniformName, int value0, int value1, int value2, int value3) {
        findUniform4i(uniformName).set(value0, value1, value2, value3);
    }

    @Override // org.ladysnake.satin.api.managed.ManagedShaderEffect
    public void setUniformValue(String uniformName, float value) {
        findUniform1f(uniformName).set(value);
    }

    @Override // org.ladysnake.satin.api.managed.ManagedShaderEffect
    public void setUniformValue(String uniformName, float value0, float value1) {
        findUniform2f(uniformName).set(value0, value1);
    }

    @Override // org.ladysnake.satin.api.managed.ManagedShaderEffect
    public void setUniformValue(String uniformName, float value0, float value1, float value2) {
        findUniform3f(uniformName).set(value0, value1, value2);
    }

    @Override // org.ladysnake.satin.api.managed.ManagedShaderEffect
    public void setUniformValue(String uniformName, float value0, float value1, float value2, float value3) {
        findUniform4f(uniformName).set(value0, value1, value2, value3);
    }

    @Override // org.ladysnake.satin.api.managed.ManagedShaderEffect
    public void setUniformValue(String uniformName, Matrix4f value) {
        findUniformMat4(uniformName).set(value);
    }

    @Override // org.ladysnake.satin.api.managed.ManagedShaderEffect
    public void setSamplerUniform(String samplerName, class_1044 texture) {
        findSampler(samplerName).set(texture);
    }

    @Override // org.ladysnake.satin.api.managed.ManagedShaderEffect
    public void setSamplerUniform(String samplerName, class_276 textureFbo) {
        findSampler(samplerName).set(textureFbo);
    }

    @Override // org.ladysnake.satin.api.managed.ManagedShaderEffect
    public void setSamplerUniform(String samplerName, int textureName) {
        findSampler(samplerName).set(textureName);
    }

    @Override // org.ladysnake.satin.api.managed.uniform.UniformFinder
    public SamplerUniformV2 findSampler(String samplerName) {
        return (SamplerUniformV2) manageUniform(this.managedSamplers, ManagedPassSamplerUniform::new, samplerName, "sampler");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.ladysnake.satin.impl.ResettableManagedShaderBase
    public boolean setupUniform(ManagedUniformBase uniform, class_279 shader) {
        return uniform.findUniformTargets(((AccessiblePassesShaderEffect) shader).getPasses());
    }

    @Override // org.ladysnake.satin.impl.ResettableManagedShaderBase
    protected void logInitError(class_10151.class_10152 e) {
        Satin.LOGGER.error("Could not create screen shader {}", getLocation(), e);
    }

    @Nullable
    private class_279 getShaderOrLog() {
        if (!isInitialized() && !isErrored()) {
            initializeOrLog(class_310.method_1551().method_1478());
        }
        return (class_279) this.shader;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.ladysnake.satin.impl.ResettableManagedShaderBase
    public void doRelease(class_279 shader) {
    }
}
