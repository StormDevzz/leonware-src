package org.ladysnake.satin.api.managed;

import net.minecraft.class_1921;
import net.minecraft.class_5944;
import org.apiguardian.api.API;
import org.ladysnake.satin.api.managed.uniform.UniformFinder;

/* JADX INFO: loaded from: leonware-0.0.3.jar:META-INF/jars/satin-3.0.0-alpha.1.jar:org/ladysnake/satin/api/managed/ManagedCoreShader.class */
@API(status = API.Status.EXPERIMENTAL, since = "1.6.0")
public interface ManagedCoreShader extends UniformFinder {
    class_5944 getProgram();

    void release();

    class_1921 getRenderLayer(class_1921 class_1921Var);
}
