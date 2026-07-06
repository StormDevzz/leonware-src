package org.ladysnake.satin.api.managed.uniform;

import org.apiguardian.api.API;
import org.joml.Vector2f;

/* JADX INFO: loaded from: leonware-0.0.3.jar:META-INF/jars/satin-3.0.0-alpha.1.jar:org/ladysnake/satin/api/managed/uniform/Uniform2f.class */
public interface Uniform2f {
    @API(status = API.Status.MAINTAINED, since = "1.4.0")
    void set(float f, float f2);

    @API(status = API.Status.MAINTAINED, since = "1.11.0")
    void set(Vector2f vector2f);
}
