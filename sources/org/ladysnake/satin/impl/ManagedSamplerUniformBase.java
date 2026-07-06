package org.ladysnake.satin.impl;

import java.util.List;
import java.util.function.IntSupplier;
import net.minecraft.class_10157;
import net.minecraft.class_5944;
import org.ladysnake.satin.api.managed.uniform.SamplerUniform;

/* JADX INFO: loaded from: leonware-0.0.3.jar:META-INF/jars/satin-3.0.0-alpha.1.jar:org/ladysnake/satin/impl/ManagedSamplerUniformBase.class */
public abstract class ManagedSamplerUniformBase extends ManagedUniformBase implements SamplerUniform {
    protected SamplerAccess[] targets;
    protected int[] locations;
    protected IntSupplier value;

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract void set(IntSupplier intSupplier);

    public ManagedSamplerUniformBase(String name) {
        super(name);
        this.targets = new SamplerAccess[0];
        this.locations = new int[0];
    }

    private int getSamplerLoc(SamplerAccess access) {
        List<class_10157.class_10158> samplerNames = access.satin$getSamplerNames();
        for (int i = 0; i < samplerNames.size(); i++) {
            if (samplerNames.get(i).comp_3121().equals(this.name)) {
                return access.satin$getSamplerShaderLocs().getInt(i);
            }
        }
        return -1;
    }

    @Override // org.ladysnake.satin.impl.ManagedUniformBase
    public boolean findUniformTarget(class_5944 shader) {
        return findUniformTarget((SamplerAccess) shader);
    }

    private boolean findUniformTarget(SamplerAccess access) {
        if (access.satin$hasSampler(this.name)) {
            this.targets = new SamplerAccess[]{access};
            this.locations = new int[]{getSamplerLoc(access)};
            syncCurrentValues();
            return true;
        }
        return false;
    }

    protected void syncCurrentValues() {
        IntSupplier value = this.value;
        if (value != null) {
            this.value = null;
            set(value);
        }
    }
}
