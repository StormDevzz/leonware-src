// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.system.backend;

import java.io.File;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import lombok.Generated;

public final class ClientInfo
{
    public static final String NAME = "LeonWare";
    public static final String VERSION;
    public static final String GAME_PATH;
    public static final String CONFIG_PATH_AI_MODELS;
    public static final String CONFIG_PATH_OTHER;
    public static final String CONFIG_PATH_THEMES;
    public static final String CONFIG_PATH_MAIN;
    
    @Generated
    private ClientInfo() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    static {
        VERSION = FabricLoader.getInstance().getModContainer("LeonWare".toLowerCase()).get().getMetadata().getVersion().getFriendlyString();
        GAME_PATH = new File(System.getProperty("user.dir")).getAbsolutePath();
        CONFIG_PATH_AI_MODELS = new File(System.getProperty("user.dir"), "LeonWare/ai_models").getAbsolutePath();
        CONFIG_PATH_OTHER = new File(System.getProperty("user.dir"), "LeonWare/other").getAbsolutePath();
        CONFIG_PATH_THEMES = new File(System.getProperty("user.dir"), "LeonWare/themes").getAbsolutePath();
        CONFIG_PATH_MAIN = new File(System.getProperty("user.dir"), "LeonWare/main").getAbsolutePath();
    }
}
