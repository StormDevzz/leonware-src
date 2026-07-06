package org.ladysnake.satin.impl;

import java.util.List;
import java.util.Objects;
import java.util.function.IntSupplier;
import net.minecraft.class_1044;
import net.minecraft.class_276;
import net.minecraft.class_283;
import net.minecraft.class_5944;

/* JADX INFO: loaded from: leonware-0.0.3.jar:META-INF/jars/satin-3.0.0-alpha.1.jar:org/ladysnake/satin/impl/ManagedSamplerUniformV1.class */
public final class ManagedSamplerUniformV1 extends ManagedSamplerUniformBase {
    public ManagedSamplerUniformV1(String name) {
        super(name);
    }

    @Override // org.ladysnake.satin.impl.ManagedUniformBase
    public boolean findUniformTargets(List<class_283> shaders) {
        throw new UnsupportedOperationException();
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

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.ladysnake.satin.impl.ManagedSamplerUniformBase, org.ladysnake.satin.api.managed.uniform.SamplerUniformV2
    public void set(IntSupplier value) {
        class_5944[] class_5944VarArr = this.targets;
        if (class_5944VarArr.length > 0 && this.value != value) {
            for (class_5944 class_5944Var : class_5944VarArr) {
                class_5944Var.method_62899(this.name, value.getAsInt());
            }
            this.value = value;
        }
    }
}
