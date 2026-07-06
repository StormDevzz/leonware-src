package org.ladysnake.satin.api.util;

import java.io.IOException;
import javax.annotation.Nullable;
import net.minecraft.class_2960;
import net.minecraft.class_3300;
import org.apiguardian.api.API;
import org.ladysnake.satin.impl.ValidatingShaderLoader;

/* JADX INFO: loaded from: leonware-0.0.3.jar:META-INF/jars/satin-3.0.0-alpha.1.jar:org/ladysnake/satin/api/util/ShaderLoader.class */
public interface ShaderLoader {
    @API(status = API.Status.MAINTAINED)
    int loadShader(class_3300 class_3300Var, @Nullable class_2960 class_2960Var, @Nullable class_2960 class_2960Var2) throws IOException;

    @API(status = API.Status.MAINTAINED)
    static ShaderLoader getInstance() {
        return ValidatingShaderLoader.INSTANCE;
    }
}
