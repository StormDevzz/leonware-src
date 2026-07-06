package org.ladysnake.satin.api.managed;

import java.util.function.Consumer;
import net.minecraft.class_293;
import net.minecraft.class_2960;
import org.apiguardian.api.API;
import org.ladysnake.satin.impl.ReloadableShaderEffectManager;

/* JADX INFO: loaded from: leonware-0.0.3.jar:META-INF/jars/satin-3.0.0-alpha.1.jar:org/ladysnake/satin/api/managed/ShaderEffectManager.class */
public interface ShaderEffectManager {
    @API(status = API.Status.STABLE, since = "1.0.0")
    ManagedShaderEffect manage(class_2960 class_2960Var);

    @API(status = API.Status.STABLE, since = "1.0.0")
    ManagedShaderEffect manage(class_2960 class_2960Var, Consumer<ManagedShaderEffect> consumer);

    @API(status = API.Status.EXPERIMENTAL, since = "1.6.0")
    ManagedCoreShader manageCoreShader(class_2960 class_2960Var);

    @API(status = API.Status.EXPERIMENTAL, since = "1.6.0")
    ManagedCoreShader manageCoreShader(class_2960 class_2960Var, class_293 class_293Var);

    @API(status = API.Status.EXPERIMENTAL, since = "1.6.0")
    ManagedCoreShader manageCoreShader(class_2960 class_2960Var, class_293 class_293Var, Consumer<ManagedCoreShader> consumer);

    @API(status = API.Status.STABLE, since = "1.0.0")
    void dispose(ManagedShaderEffect managedShaderEffect);

    @API(status = API.Status.EXPERIMENTAL, since = "1.4.0")
    void dispose(ManagedCoreShader managedCoreShader);

    @API(status = API.Status.STABLE)
    static ShaderEffectManager getInstance() {
        return ReloadableShaderEffectManager.INSTANCE;
    }
}
