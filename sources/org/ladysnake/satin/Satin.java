package org.ladysnake.satin;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apiguardian.api.API;
import org.ladysnake.satin.api.event.ResolutionChangeCallback;
import org.ladysnake.satin.api.event.WorldRendererReloadCallback;
import org.ladysnake.satin.impl.ReloadableShaderEffectManager;
import org.ladysnake.satin.impl.WorldRenderEventsDelegator;

/* JADX INFO: loaded from: leonware-0.0.3.jar:META-INF/jars/satin-3.0.0-alpha.1.jar:org/ladysnake/satin/Satin.class */
public class Satin implements ClientModInitializer {
    public static final String MOD_ID = "satin";
    public static final Logger LOGGER = LogManager.getLogger("Satin");

    @API(status = API.Status.STABLE)
    public static boolean areShadersDisabled() {
        return false;
    }

    public void onInitializeClient() {
        ResolutionChangeCallback.EVENT.register(ReloadableShaderEffectManager.INSTANCE);
        WorldRendererReloadCallback.EVENT.register(ReloadableShaderEffectManager.INSTANCE);
        if (FabricLoader.getInstance().isModLoaded("optifabric")) {
            LOGGER.warn("[Satin] Optifine present in the instance, custom entity post process shaders will not work");
        }
        if (FabricLoader.getInstance().isModLoaded("vivecraft")) {
            LOGGER.warn("[Satin] Vivecraft present in the instance, you may experience degraded performance - try turning eye stencil off in VR settings");
        }
        WorldRenderEvents.BEFORE_ENTITIES.register(WorldRenderEventsDelegator.INSTANCE);
        WorldRenderEvents.AFTER_ENTITIES.register(WorldRenderEventsDelegator.INSTANCE);
        WorldRenderEvents.LAST.register(WorldRenderEventsDelegator.INSTANCE);
    }
}
