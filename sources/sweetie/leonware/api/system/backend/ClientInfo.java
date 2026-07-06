package sweetie.leonware.api.system.backend;

import java.io.File;
import lombok.Generated;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/system/backend/ClientInfo.class */
public final class ClientInfo {
    public static final String NAME = "LeonWare";
    public static final String VERSION = ((ModContainer) FabricLoader.getInstance().getModContainer(NAME.toLowerCase()).get()).getMetadata().getVersion().getFriendlyString();
    public static final String GAME_PATH = new File(System.getProperty("user.dir")).getAbsolutePath();
    public static final String CONFIG_PATH_AI_MODELS = new File(System.getProperty("user.dir"), "LeonWare/ai_models").getAbsolutePath();
    public static final String CONFIG_PATH_OTHER = new File(System.getProperty("user.dir"), "LeonWare/other").getAbsolutePath();
    public static final String CONFIG_PATH_THEMES = new File(System.getProperty("user.dir"), "LeonWare/themes").getAbsolutePath();
    public static final String CONFIG_PATH_MAIN = new File(System.getProperty("user.dir"), "LeonWare/main").getAbsolutePath();

    @Generated
    private ClientInfo() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
