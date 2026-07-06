// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware;

import java.util.HashMap;
import lombok.Generated;
import sweetie.leonware.api.system.files.FileManager;
import sweetie.leonware.api.system.DiscordHook;
import sweetie.leonware.api.system.configs.ConfigSkin;
import sweetie.leonware.client.services.RenderService;
import sweetie.leonware.client.services.HeartbeatService;
import sweetie.leonware.client.features.modules.render.custommodel.Leon2DModel;
import sweetie.leonware.client.features.modules.render.custommodel.AmogusModel;
import sweetie.leonware.client.features.modules.render.custommodel.FreddyBearModel;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import sweetie.leonware.client.features.modules.render.custommodel.CrazyRabbitModel;
import sweetie.leonware.client.features.modules.render.custommodel.CustomModelLayers;
import sweetie.leonware.api.system.render.BackgroundManager;
import sweetie.leonware.client.ui.theme.ThemeEditor;
import sweetie.leonware.api.command.CommandManager;
import sweetie.leonware.api.utils.rotation.manager.RotationManager;
import sweetie.leonware.client.ui.widget.WidgetManager;
import sweetie.leonware.api.system.configs.MacroManager;
import sweetie.leonware.api.system.configs.FriendManager;
import sweetie.leonware.api.system.draggable.DraggableManager;
import sweetie.leonware.api.system.configs.AltManager;
import sweetie.leonware.api.system.configs.ConfigManager;
import sweetie.leonware.api.utils.render.KawaseBlurProgram;
import sweetie.leonware.api.utils.render.fonts.Fonts;
import sweetie.leonware.api.module.ModuleManager;
import sweetie.leonware.api.utils.other.SoundUtil;
import sweetie.leonware.api.module.Module;
import java.util.Map;
import net.fabricmc.api.ClientModInitializer;

public class LeonWare implements ClientModInitializer
{
    private static LeonWare instance;
    public static boolean isUnhooked;
    public static String unhookCode;
    public static Map<Module, Boolean> unhookSnapshot;
    
    public void onInitializeClient() {
        LeonWare.instance = this;
        SoundUtil.load();
        this.loadManagers();
        this.loadServices();
        this.loadFiles();
    }
    
    public void postLoad() {
        ModuleManager.getInstance().getModules().sort((a, b) -> Float.compare(Fonts.PS_MEDIUM.getWidth(b.getName(), 7.0f), Fonts.PS_MEDIUM.getWidth(a.getName(), 7.0f)));
        KawaseBlurProgram.load();
    }
    
    private void loadFiles() {
        ConfigManager.getInstance().load("autoConfig");
        AltManager.getInstance().load();
        DraggableManager.getInstance().load();
        FriendManager.getInstance().load();
        MacroManager.getInstance().load();
    }
    
    private void loadManagers() {
        WidgetManager.getInstance().load();
        RotationManager.getInstance().load();
        ModuleManager.getInstance().load();
        CommandManager.getInstance().load();
        ThemeEditor.getInstance().load();
        BackgroundManager.load();
        EntityModelLayerRegistry.registerModelLayer(CustomModelLayers.CRAZY_RABBIT, CrazyRabbitModel::createTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(CustomModelLayers.FREDDY_BEAR, FreddyBearModel::createTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(CustomModelLayers.AMOGUS, AmogusModel::createTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(CustomModelLayers.LEON_2D, Leon2DModel::createTexturedModelData);
    }
    
    private void loadServices() {
        HeartbeatService.getInstance().load();
        RenderService.getInstance().load();
        ConfigSkin.getInstance().load();
        DiscordHook.startRPC();
    }
    
    public void onClose() {
        ConfigManager.getInstance().save("autoConfig");
        FileManager.getInstance().save();
        ThemeEditor.getInstance().save(true);
        DraggableManager.getInstance().save();
        MacroManager.getInstance().save();
        DiscordHook.stopRPC();
    }
    
    @Generated
    public static LeonWare getInstance() {
        return LeonWare.instance;
    }
    
    static {
        LeonWare.instance = new LeonWare();
        LeonWare.isUnhooked = false;
        LeonWare.unhookCode = "";
        LeonWare.unhookSnapshot = new HashMap<Module, Boolean>();
    }
}
