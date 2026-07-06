package org.ladysnake.satin.api.managed.uniform;

import net.minecraft.class_1044;
import net.minecraft.class_276;
import org.apiguardian.api.API;

/* JADX INFO: loaded from: leonware-0.0.3.jar:META-INF/jars/satin-3.0.0-alpha.1.jar:org/ladysnake/satin/api/managed/uniform/SamplerUniform.class */
@API(status = API.Status.MAINTAINED)
public interface SamplerUniform {
    @API(status = API.Status.MAINTAINED, since = "1.4.0")
    void set(class_1044 class_1044Var);

    @API(status = API.Status.MAINTAINED, since = "1.4.0")
    void set(class_276 class_276Var);

    @API(status = API.Status.MAINTAINED, since = "1.4.0")
    void set(int i);
}
