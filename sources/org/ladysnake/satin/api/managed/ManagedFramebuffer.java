package org.ladysnake.satin.api.managed;

import javax.annotation.Nullable;
import net.minecraft.class_1921;
import net.minecraft.class_276;
import org.apiguardian.api.API;

/* JADX INFO: loaded from: leonware-0.0.3.jar:META-INF/jars/satin-3.0.0-alpha.1.jar:org/ladysnake/satin/api/managed/ManagedFramebuffer.class */
@API(status = API.Status.EXPERIMENTAL, since = "1.4.0")
public interface ManagedFramebuffer {
    @Nullable
    class_276 getFramebuffer();

    void beginWrite(boolean z);

    void copyDepthFrom(class_276 class_276Var);

    void draw();

    void draw(int i, int i2, boolean z);

    void clear();

    class_1921 getRenderLayer(class_1921 class_1921Var);
}
