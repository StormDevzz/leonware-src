package org.ladysnake.satin.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.IntSupplier;
import net.minecraft.class_1044;
import net.minecraft.class_276;
import net.minecraft.class_283;
import net.minecraft.class_2960;
import net.minecraft.class_5944;
import net.minecraft.class_9916;
import net.minecraft.class_9925;
import org.ladysnake.satin.api.managed.uniform.SamplerUniformV2;

/* JADX INFO: loaded from: leonware-0.0.3.jar:META-INF/jars/satin-3.0.0-alpha.1.jar:org/ladysnake/satin/impl/ManagedPassSamplerUniform.class */
public final class ManagedPassSamplerUniform extends ManagedSamplerUniformBase implements SamplerUniformV2, class_283.class_9971 {
    public ManagedPassSamplerUniform(String name) {
        super(name);
    }

    public void method_62259(class_9916 pass, Map<class_2960, class_9925<class_276>> internalTargets) {
    }

    public void method_62260(class_5944 program, Map<class_2960, class_9925<class_276>> internalTargets) {
        program.method_62899(this.name, this.value.getAsInt());
    }

    @Override // org.ladysnake.satin.impl.ManagedUniformBase
    public boolean findUniformTargets(List<class_283> passes) {
        List<SamplerAccess> targets = new ArrayList<>(passes.size());
        boolean found = false;
        for (class_283 pass : passes) {
            pass.method_62258(this);
            found = true;
        }
        this.targets = (SamplerAccess[]) targets.toArray(new SamplerAccess[0]);
        syncCurrentValues();
        return found;
    }

    @Override // org.ladysnake.satin.api.managed.uniform.SamplerUniform
    public void set(class_1044 texture) {
        Objects.requireNonNull(texture);
        set(texture::method_4624);
    }

    @Override // org.ladysnake.satin.api.managed.uniform.SamplerUniform
    public void set(class_276 textureFbo) {
        Objects.requireNonNull(textureFbo);
        set(textureFbo::method_30277);
    }

    @Override // org.ladysnake.satin.api.managed.uniform.SamplerUniform
    public void set(int textureName) {
        set(() -> {
            return textureName;
        });
    }

    @Override // org.ladysnake.satin.impl.ManagedSamplerUniformBase, org.ladysnake.satin.api.managed.uniform.SamplerUniformV2
    public void set(IntSupplier value) {
        SamplerAccess[] targets = this.targets;
        if (targets.length > 0 && this.value != value) {
            this.value = value;
        }
    }
}
