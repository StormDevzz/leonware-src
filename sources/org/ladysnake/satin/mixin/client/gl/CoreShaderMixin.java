package org.ladysnake.satin.mixin.client.gl;

import it.unimi.dsi.fastutil.ints.IntList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import net.minecraft.class_10157;
import net.minecraft.class_5944;
import org.ladysnake.satin.impl.SamplerAccess;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;

/* JADX INFO: loaded from: leonware-0.0.3.jar:META-INF/jars/satin-3.0.0-alpha.1.jar:org/ladysnake/satin/mixin/client/gl/CoreShaderMixin.class */
@Mixin({class_5944.class})
public abstract class CoreShaderMixin implements SamplerAccess {

    @Shadow
    @Final
    private List<class_10157.class_10158> field_53838;

    @Override // org.ladysnake.satin.impl.SamplerAccess
    @Accessor("samplers")
    public abstract List<class_10157.class_10158> satin$getSamplerNames();

    @Override // org.ladysnake.satin.impl.SamplerAccess
    @Accessor("samplerLocations")
    public abstract IntList satin$getSamplerShaderLocs();

    @Override // org.ladysnake.satin.impl.SamplerAccess
    public void satin$removeSampler(String name) {
        Iterator<class_10157.class_10158> iterator = this.field_53838.iterator();
        while (iterator.hasNext()) {
            class_10157.class_10158 sampler = iterator.next();
            if (Objects.equals(sampler.comp_3121(), name)) {
                iterator.remove();
            }
        }
    }

    @Override // org.ladysnake.satin.impl.SamplerAccess
    public boolean satin$hasSampler(String name) {
        for (class_10157.class_10158 sampler : this.field_53838) {
            if (sampler.comp_3121().equals(name)) {
                return true;
            }
        }
        return false;
    }
}
