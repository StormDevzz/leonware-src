// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.other;

import java.util.function.Function;
import java.nio.file.Path;
import java.util.Comparator;
import java.nio.file.Files;
import java.nio.file.FileVisitOption;
import java.util.Iterator;
import net.minecraft.class_3262;
import net.minecraft.class_8518;
import java.io.File;
import sweetie.leonware.api.system.backend.ClientInfo;
import sweetie.leonware.api.system.DiscordHook;
import sweetie.leonware.api.module.ModuleManager;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.TickEvent;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import sweetie.leonware.LeonWare;
import java.util.Random;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "UnHook", category = Category.OTHER, bind = -999)
public class UnHook extends Module
{
    private static final UnHook instance;
    public BooleanSetting deleteCfg;
    public BooleanSetting cleanLogs;
    private int tickCount;
    private boolean counting;
    
    public static UnHook getInstance() {
        return UnHook.instance;
    }
    
    public UnHook() {
        this.deleteCfg = new BooleanSetting("Delete Cfg").value(true);
        this.cleanLogs = new BooleanSetting("Clean Logs").value(true);
        this.tickCount = 0;
        this.counting = false;
        this.addSettings(this.deleteCfg, this.cleanLogs);
    }
    
    @Override
    public void onEnable() {
        LeonWare.unhookCode = "leonware" + new Random().nextInt(1000);
        for (int i = 0; i < 10; ++i) {
            if (class_310.method_1551().field_1724 != null) {
                class_310.method_1551().field_1724.method_7353(class_2561.method_30163("§c\u0427\u0435\u0440\u0435\u0437 5 \u0441\u0435\u043a\u0443\u043d\u0434 \u043a\u043b\u0438\u0435\u043d\u0442 \u0430\u043d\u0445\u0443\u043a\u043d\u0435\u0442\u0441\u044f! \u041a\u043e\u0434: §f" + LeonWare.unhookCode), false);
            }
        }
        this.tickCount = 0;
        this.counting = true;
    }
    
    @Override
    public void onDisable() {
        this.counting = false;
    }
    
    @Override
    public void onEvent() {
        TickEvent.getInstance().subscribe(new Listener<TickEvent>(event -> {
            if (!(!this.counting)) {
                ++this.tickCount;
                if (this.tickCount >= 100) {
                    this.doUnhook();
                    super.setEnabled(this.counting = false, false);
                }
            }
        }));
    }
    
    private void doUnhook() {
        LeonWare.isUnhooked = true;
        LeonWare.unhookSnapshot.clear();
        for (final Module m : ModuleManager.getInstance().getModules()) {
            if (m == this) {
                continue;
            }
            LeonWare.unhookSnapshot.put(m, m.isEnabled());
            if (!m.isEnabled()) {
                continue;
            }
            m.setEnabled(false, false);
        }
        LeonWare.unhookSnapshot.put(this, false);
        DiscordHook.stopRPC();
        if (this.deleteCfg.getValue()) {
            final File configFolder = new File(ClientInfo.CONFIG_PATH_MAIN);
            this.deleteDirectory(configFolder);
        }
        if (this.cleanLogs.getValue()) {
            this.cleanWindowsLogs();
        }
        final class_310 mc = class_310.method_1551();
        mc.method_24288();
        try {
            mc.method_22683().method_4491((class_3262)mc.method_45573(), class_8518.field_44650);
        }
        catch (final Exception ex) {}
    }
    
    private void deleteDirectory(final File f) {
        if (!f.exists()) {
            return;
        }
        try {
            Files.walk(f.toPath(), new FileVisitOption[0]).sorted(Comparator.reverseOrder()).map((Function<? super Path, ?>)Path::toFile).forEach(File::delete);
        }
        catch (final Exception ex) {}
    }
    
    private void cleanWindowsLogs() {
        try {
            final String recentPath = System.getenv("APPDATA") + "\\Microsoft\\Windows\\Recent";
            final File recentFolder = new File(recentPath);
            if (recentFolder.exists() && recentFolder.isDirectory()) {
                final File[] list = recentFolder.listFiles();
                if (list != null) {
                    for (final File f : list) {
                        if (f.getName().toLowerCase().contains("LeonWare".toLowerCase()) || f.getName().toLowerCase().contains("leonware")) {
                            f.delete();
                        }
                    }
                }
            }
            Runtime.getRuntime().exec(new String[] { "powershell.exe", "-w", "hidden", "-Command", "Remove-Item -Path $env:APPDATA\\Microsoft\\Windows\\Recent\\* -Force -Recurse -ErrorAction SilentlyContinue" });
            Runtime.getRuntime().exec(new String[] { "powershell.exe", "-w", "hidden", "-Command", "Clear-RecycleBin -Force -ErrorAction SilentlyContinue" });
        }
        catch (final Exception ex) {}
    }
    
    static {
        instance = new UnHook();
    }
}
