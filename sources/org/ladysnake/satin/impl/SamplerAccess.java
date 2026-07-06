package org.ladysnake.satin.impl;

import it.unimi.dsi.fastutil.ints.IntList;
import java.util.List;
import net.minecraft.class_10157;

/* JADX INFO: loaded from: leonware-0.0.3.jar:META-INF/jars/satin-3.0.0-alpha.1.jar:org/ladysnake/satin/impl/SamplerAccess.class */
public interface SamplerAccess {
    void satin$removeSampler(String str);

    boolean satin$hasSampler(String str);

    List<class_10157.class_10158> satin$getSamplerNames();

    IntList satin$getSamplerShaderLocs();
}
