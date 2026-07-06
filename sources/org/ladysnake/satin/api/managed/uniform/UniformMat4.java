package org.ladysnake.satin.api.managed.uniform;

import org.apiguardian.api.API;
import org.joml.Matrix4f;

/* JADX INFO: loaded from: leonware-0.0.3.jar:META-INF/jars/satin-3.0.0-alpha.1.jar:org/ladysnake/satin/api/managed/uniform/UniformMat4.class */
public interface UniformMat4 {
    @API(status = API.Status.MAINTAINED, since = "1.4.0")
    void set(Matrix4f matrix4f);

    @API(status = API.Status.MAINTAINED, since = "1.15.0")
    void setFromArray(float[] fArr);
}
