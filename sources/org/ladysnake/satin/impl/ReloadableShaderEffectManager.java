package org.ladysnake.satin.impl;

import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import java.util.Set;
import java.util.function.Consumer;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_5912;
import net.minecraft.class_761;
import org.ladysnake.satin.Satin;
import org.ladysnake.satin.api.event.ResolutionChangeCallback;
import org.ladysnake.satin.api.event.WorldRendererReloadCallback;
import org.ladysnake.satin.api.managed.ManagedCoreShader;
import org.ladysnake.satin.api.managed.ManagedShaderEffect;
import org.ladysnake.satin.api.managed.ShaderEffectManager;

/* JADX INFO: loaded from: leonware-0.0.3.jar:META-INF/jars/satin-3.0.0-alpha.1.jar:org/ladysnake/satin/impl/ReloadableShaderEffectManager.class */
public final class ReloadableShaderEffectManager implements ShaderEffectManager, ResolutionChangeCallback, WorldRendererReloadCallback {
    public static final ReloadableShaderEffectManager INSTANCE = new ReloadableShaderEffectManager();
    private final Set<ResettableManagedShaderBase<?>> managedShaders = new ReferenceOpenHashSet();

    @Override // org.ladysnake.satin.api.managed.ShaderEffectManager
    public ManagedShaderEffect manage(class_2960 id) {
        return manage(id, s -> {
        });
    }

    @Override // org.ladysnake.satin.api.managed.ShaderEffectManager
    public ManagedShaderEffect manage(class_2960 id, Consumer<ManagedShaderEffect> initCallback) {
        ResettableManagedShaderEffect ret = new ResettableManagedShaderEffect(id, initCallback);
        this.managedShaders.add(ret);
        return ret;
    }

    @Override // org.ladysnake.satin.api.managed.ShaderEffectManager
    public ManagedCoreShader manageCoreShader(class_2960 location) {
        return manageCoreShader(location, class_290.field_1580);
    }

    @Override // org.ladysnake.satin.api.managed.ShaderEffectManager
    public ManagedCoreShader manageCoreShader(class_2960 location, class_293 vertexFormat) {
        return manageCoreShader(location, vertexFormat, s -> {
        });
    }

    @Override // org.ladysnake.satin.api.managed.ShaderEffectManager
    public ManagedCoreShader manageCoreShader(class_2960 location, class_293 vertexFormat, Consumer<ManagedCoreShader> initCallback) {
        ResettableManagedCoreShader ret = new ResettableManagedCoreShader(location, vertexFormat, initCallback);
        this.managedShaders.add(ret);
        return ret;
    }

    @Override // org.ladysnake.satin.api.managed.ShaderEffectManager
    public void dispose(ManagedShaderEffect shader) {
        shader.release();
        this.managedShaders.remove(shader);
    }

    @Override // org.ladysnake.satin.api.managed.ShaderEffectManager
    public void dispose(ManagedCoreShader shader) {
        shader.release();
        this.managedShaders.remove(shader);
    }

    public void reload(class_5912 shaderResources) {
        for (ResettableManagedShaderBase<?> ss : this.managedShaders) {
            ss.initializeOrLog(shaderResources);
        }
    }

    @Override // org.ladysnake.satin.api.event.ResolutionChangeCallback
    public void onResolutionChanged(int newWidth, int newHeight) {
        runShaderSetup();
    }

    @Override // org.ladysnake.satin.api.event.WorldRendererReloadCallback
    public void onRendererReload(class_761 renderer) {
        class_310.method_1551().method_22683();
        runShaderSetup();
    }

    private void runShaderSetup() {
        if (!Satin.areShadersDisabled() && !this.managedShaders.isEmpty()) {
            for (ResettableManagedShaderBase<?> ss : this.managedShaders) {
                if (ss.isInitialized()) {
                    ss.setup();
                }
            }
        }
    }
}
