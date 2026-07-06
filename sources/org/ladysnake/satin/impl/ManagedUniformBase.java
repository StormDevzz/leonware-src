package org.ladysnake.satin.impl;

import java.util.List;
import net.minecraft.class_283;
import net.minecraft.class_5944;

/* JADX INFO: loaded from: leonware-0.0.3.jar:META-INF/jars/satin-3.0.0-alpha.1.jar:org/ladysnake/satin/impl/ManagedUniformBase.class */
public abstract class ManagedUniformBase {
    protected final String name;

    public abstract boolean findUniformTargets(List<class_283> list);

    public abstract boolean findUniformTarget(class_5944 class_5944Var);

    public ManagedUniformBase(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
