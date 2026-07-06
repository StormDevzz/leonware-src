package org.ladysnake.satin.api.managed;

import java.io.IOException;
import javax.annotation.Nullable;
import net.minecraft.class_1044;
import net.minecraft.class_276;
import net.minecraft.class_279;
import org.apiguardian.api.API;
import org.joml.Matrix4f;
import org.ladysnake.satin.api.managed.uniform.SamplerUniformV2;
import org.ladysnake.satin.api.managed.uniform.UniformFinder;

/* JADX INFO: loaded from: leonware-0.0.3.jar:META-INF/jars/satin-3.0.0-alpha.1.jar:org/ladysnake/satin/api/managed/ManagedShaderEffect.class */
public interface ManagedShaderEffect extends UniformFinder {
    @API(status = API.Status.MAINTAINED, since = "1.0.0")
    @Nullable
    class_279 getShaderEffect();

    @API(status = API.Status.MAINTAINED, since = "1.0.0")
    void initialize() throws IOException;

    @API(status = API.Status.MAINTAINED, since = "1.0.0")
    boolean isInitialized();

    @API(status = API.Status.MAINTAINED, since = "1.0.0")
    boolean isErrored();

    @API(status = API.Status.EXPERIMENTAL, since = "1.0.0")
    void release();

    @API(status = API.Status.STABLE, since = "1.0.0")
    void render(float f);

    @API(status = API.Status.EXPERIMENTAL, since = "1.4.0")
    ManagedFramebuffer getTarget(String str);

    @API(status = API.Status.STABLE, since = "1.0.0")
    void setUniformValue(String str, int i);

    @API(status = API.Status.STABLE, since = "1.0.0")
    void setUniformValue(String str, int i, int i2);

    @API(status = API.Status.STABLE, since = "1.0.0")
    void setUniformValue(String str, int i, int i2, int i3);

    @API(status = API.Status.STABLE, since = "1.0.0")
    void setUniformValue(String str, int i, int i2, int i3, int i4);

    @API(status = API.Status.STABLE, since = "1.0.0")
    void setUniformValue(String str, float f);

    @API(status = API.Status.STABLE, since = "1.0.0")
    void setUniformValue(String str, float f, float f2);

    @API(status = API.Status.STABLE, since = "1.0.0")
    void setUniformValue(String str, float f, float f2, float f3);

    @API(status = API.Status.STABLE, since = "1.0.0")
    void setUniformValue(String str, float f, float f2, float f3, float f4);

    @API(status = API.Status.STABLE, since = "1.0.0")
    void setUniformValue(String str, Matrix4f matrix4f);

    @API(status = API.Status.STABLE, since = "1.0.0")
    void setSamplerUniform(String str, class_1044 class_1044Var);

    @API(status = API.Status.STABLE, since = "1.0.0")
    void setSamplerUniform(String str, class_276 class_276Var);

    @API(status = API.Status.STABLE, since = "1.0.0")
    void setSamplerUniform(String str, int i);

    @Override // org.ladysnake.satin.api.managed.uniform.UniformFinder
    SamplerUniformV2 findSampler(String str);
}
