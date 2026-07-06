package org.ladysnake.satin.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import javax.annotation.CheckForNull;
import net.minecraft.class_10151;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_5912;
import org.apiguardian.api.API;
import org.ladysnake.satin.Satin;
import org.ladysnake.satin.api.managed.uniform.Uniform1f;
import org.ladysnake.satin.api.managed.uniform.Uniform1i;
import org.ladysnake.satin.api.managed.uniform.Uniform2f;
import org.ladysnake.satin.api.managed.uniform.Uniform2i;
import org.ladysnake.satin.api.managed.uniform.Uniform3f;
import org.ladysnake.satin.api.managed.uniform.Uniform3i;
import org.ladysnake.satin.api.managed.uniform.Uniform4f;
import org.ladysnake.satin.api.managed.uniform.Uniform4i;
import org.ladysnake.satin.api.managed.uniform.UniformFinder;
import org.ladysnake.satin.api.managed.uniform.UniformMat4;

/* JADX INFO: loaded from: leonware-0.0.3.jar:META-INF/jars/satin-3.0.0-alpha.1.jar:org/ladysnake/satin/impl/ResettableManagedShaderBase.class */
public abstract class ResettableManagedShaderBase<S> implements UniformFinder {
    private final class_2960 location;
    private final Map<String, ManagedUniform> managedUniforms = new HashMap();
    private final List<ManagedUniformBase> allUniforms = new ArrayList();
    private boolean errored;

    @CheckForNull
    protected S shader;
    static final /* synthetic */ boolean $assertionsDisabled;

    protected abstract void logInitError(class_10151.class_10152 class_10152Var);

    protected abstract S parseShader(class_5912 class_5912Var, class_310 class_310Var, class_2960 class_2960Var) throws class_10151.class_10152;

    protected abstract void doRelease(S s);

    protected abstract boolean setupUniform(ManagedUniformBase managedUniformBase, S s);

    @API(status = API.Status.INTERNAL)
    public abstract void setup();

    static {
        $assertionsDisabled = !ResettableManagedShaderBase.class.desiredAssertionStatus();
    }

    public ResettableManagedShaderBase(class_2960 location) {
        this.location = location;
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: net.minecraft.class_10151$class_10152 */
    @API(status = API.Status.INTERNAL)
    public void initializeOrLog(class_5912 mgr) {
        try {
            initialize(mgr);
        } catch (class_10151.class_10152 e) {
            this.errored = true;
            logInitError(e);
        }
    }

    protected void initialize(class_5912 resourceManager) throws class_10151.class_10152 {
        release();
        this.shader = parseShader(resourceManager, class_310.method_1551(), this.location);
        setup();
    }

    public void release() {
        if (isInitialized()) {
            try {
                if (!$assertionsDisabled && this.shader == null) {
                    throw new AssertionError();
                }
                doRelease(this.shader);
                this.shader = null;
            } catch (Exception e) {
                throw new RuntimeException("Failed to release shader " + String.valueOf(this.location), e);
            }
        }
        this.errored = false;
    }

    protected Collection<ManagedUniformBase> getManagedUniforms() {
        return this.allUniforms;
    }

    public boolean isInitialized() {
        return this.shader != null;
    }

    public boolean isErrored() {
        return this.errored;
    }

    public class_2960 getLocation() {
        return this.location;
    }

    protected <U extends ManagedUniformBase> U manageUniform(Map<String, U> uniformMap, Function<String, U> factory, String uniformName, String uniformKind) {
        U existing = uniformMap.get(uniformName);
        if (existing != null) {
            return existing;
        }
        U ret = factory.apply(uniformName);
        if (this.shader != null) {
            boolean found = setupUniform(ret, this.shader);
            if (!found) {
                Satin.LOGGER.warn("No {} found with name {} in shader {}", uniformKind, uniformName, this.location);
            }
        }
        uniformMap.put(uniformName, ret);
        this.allUniforms.add(ret);
        return ret;
    }

    @Override // org.ladysnake.satin.api.managed.uniform.UniformFinder
    public Uniform1i findUniform1i(String uniformName) {
        return (Uniform1i) manageUniform(this.managedUniforms, name -> {
            return new ManagedUniform(name, 1);
        }, uniformName, "uniform");
    }

    @Override // org.ladysnake.satin.api.managed.uniform.UniformFinder
    public Uniform2i findUniform2i(String uniformName) {
        return (Uniform2i) manageUniform(this.managedUniforms, name -> {
            return new ManagedUniform(name, 2);
        }, uniformName, "uniform");
    }

    @Override // org.ladysnake.satin.api.managed.uniform.UniformFinder
    public Uniform3i findUniform3i(String uniformName) {
        return (Uniform3i) manageUniform(this.managedUniforms, name -> {
            return new ManagedUniform(name, 3);
        }, uniformName, "uniform");
    }

    @Override // org.ladysnake.satin.api.managed.uniform.UniformFinder
    public Uniform4i findUniform4i(String uniformName) {
        return (Uniform4i) manageUniform(this.managedUniforms, name -> {
            return new ManagedUniform(name, 4);
        }, uniformName, "uniform");
    }

    @Override // org.ladysnake.satin.api.managed.uniform.UniformFinder
    public Uniform1f findUniform1f(String uniformName) {
        return (Uniform1f) manageUniform(this.managedUniforms, name -> {
            return new ManagedUniform(name, 1);
        }, uniformName, "uniform");
    }

    @Override // org.ladysnake.satin.api.managed.uniform.UniformFinder
    public Uniform2f findUniform2f(String uniformName) {
        return (Uniform2f) manageUniform(this.managedUniforms, name -> {
            return new ManagedUniform(name, 2);
        }, uniformName, "uniform");
    }

    @Override // org.ladysnake.satin.api.managed.uniform.UniformFinder
    public Uniform3f findUniform3f(String uniformName) {
        return (Uniform3f) manageUniform(this.managedUniforms, name -> {
            return new ManagedUniform(name, 3);
        }, uniformName, "uniform");
    }

    @Override // org.ladysnake.satin.api.managed.uniform.UniformFinder
    public Uniform4f findUniform4f(String uniformName) {
        return (Uniform4f) manageUniform(this.managedUniforms, name -> {
            return new ManagedUniform(name, 4);
        }, uniformName, "uniform");
    }

    @Override // org.ladysnake.satin.api.managed.uniform.UniformFinder
    public UniformMat4 findUniformMat4(String uniformName) {
        return (UniformMat4) manageUniform(this.managedUniforms, name -> {
            return new ManagedUniform(name, 16);
        }, uniformName, "uniform");
    }

    public String toString() {
        return "%s[%s]".formatted(getClass().getSimpleName(), this.location);
    }
}
