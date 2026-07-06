package org.ladysnake.satin.api.managed.uniform;

import org.apiguardian.api.API;
import org.joml.Vector4f;

/* JADX INFO: loaded from: leonware-0.0.3.jar:META-INF/jars/satin-3.0.0-alpha.1.jar:org/ladysnake/satin/api/managed/uniform/Uniform4f.class */
public interface Uniform4f {
    @API(status = API.Status.MAINTAINED, since = "1.4.0")
    void set(float f, float f2, float f3, float f4);

    @API(status = API.Status.MAINTAINED, since = "1.12.0")
    void set(Vector4f vector4f);
}
