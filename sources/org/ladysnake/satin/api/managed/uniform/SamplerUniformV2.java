package org.ladysnake.satin.api.managed.uniform;

import java.util.function.IntSupplier;
import org.apiguardian.api.API;

/* JADX INFO: loaded from: leonware-0.0.3.jar:META-INF/jars/satin-3.0.0-alpha.1.jar:org/ladysnake/satin/api/managed/uniform/SamplerUniformV2.class */
public interface SamplerUniformV2 extends SamplerUniform {
    @API(status = API.Status.EXPERIMENTAL, since = "1.4.0")
    void set(IntSupplier intSupplier);
}
