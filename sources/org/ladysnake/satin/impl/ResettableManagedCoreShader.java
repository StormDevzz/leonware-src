package org.ladysnake.satin.impl;

import com.google.common.base.Preconditions;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.class_10149;
import net.minecraft.class_10151;
import net.minecraft.class_10156;
import net.minecraft.class_1921;
import net.minecraft.class_293;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_5912;
import net.minecraft.class_5944;
import org.ladysnake.satin.Satin;
import org.ladysnake.satin.api.managed.ManagedCoreShader;
import org.ladysnake.satin.api.managed.uniform.SamplerUniform;

/* JADX INFO: loaded from: leonware-0.0.3.jar:META-INF/jars/satin-3.0.0-alpha.1.jar:org/ladysnake/satin/impl/ResettableManagedCoreShader.class */
public final class ResettableManagedCoreShader extends ResettableManagedShaderBase<class_5944> implements ManagedCoreShader {
    private final Consumer<ManagedCoreShader> initCallback;
    private final RenderLayerSupplier renderLayerSupplier;
    private final class_293 vertexFormat;
    private final Map<String, ManagedSamplerUniformV1> managedSamplers;

    public ResettableManagedCoreShader(class_2960 location, class_293 vertexFormat, Consumer<ManagedCoreShader> initCallback) {
        super(location);
        this.managedSamplers = new HashMap();
        this.vertexFormat = vertexFormat;
        this.initCallback = initCallback;
        this.renderLayerSupplier = RenderLayerSupplier.shader(String.format("%s_%d", location, Integer.valueOf(System.identityHashCode(this))), vertexFormat, new class_10156(getLocation(), this.vertexFormat, class_10149.field_53930));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX INFO: Thrown type has an unknown type hierarchy: net.minecraft.class_10151$class_10152 */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.ladysnake.satin.impl.ResettableManagedShaderBase
    public class_5944 parseShader(class_5912 resourceManager, class_310 mc, class_2960 location) throws class_10151.class_10152 {
        throw new class_10151.class_10152("unsupported");
    }

    @Override // org.ladysnake.satin.impl.ResettableManagedShaderBase
    public void setup() {
        Preconditions.checkNotNull((class_5944) this.shader);
        for (ManagedUniformBase uniform : getManagedUniforms()) {
            setupUniform(uniform, (class_5944) this.shader);
        }
        this.initCallback.accept(this);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.ladysnake.satin.impl.ResettableManagedShaderBase
    public void doRelease(class_5944 shader) {
        shader.close();
    }

    @Override // org.ladysnake.satin.api.managed.ManagedCoreShader
    public class_5944 getProgram() {
        return (class_5944) this.shader;
    }

    @Override // org.ladysnake.satin.api.managed.ManagedCoreShader
    public class_1921 getRenderLayer(class_1921 baseLayer) {
        return this.renderLayerSupplier.getRenderLayer(baseLayer);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.ladysnake.satin.impl.ResettableManagedShaderBase
    public boolean setupUniform(ManagedUniformBase uniform, class_5944 shader) {
        return uniform.findUniformTarget(shader);
    }

    @Override // org.ladysnake.satin.api.managed.uniform.UniformFinder
    public SamplerUniform findSampler(String samplerName) {
        return (SamplerUniform) manageUniform(this.managedSamplers, ManagedSamplerUniformV1::new, samplerName, "sampler");
    }

    @Override // org.ladysnake.satin.impl.ResettableManagedShaderBase
    protected void logInitError(class_10151.class_10152 e) {
        Satin.LOGGER.error("Could not create shader program {}", getLocation(), e);
    }
}
