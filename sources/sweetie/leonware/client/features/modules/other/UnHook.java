package sweetie.leonware.client.features.modules.other;

import java.io.File;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.util.Comparator;
import java.util.Random;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_8518;
import sweetie.leonware.LeonWare;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.TickEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleManager;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.system.DiscordHook;
import sweetie.leonware.api.system.backend.ClientInfo;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/other/UnHook.class */
@ModuleRegister(name = "UnHook", category = Category.OTHER, bind = -999)
public class UnHook extends Module {
    private static final UnHook instance = new UnHook();
    public BooleanSetting deleteCfg = new BooleanSetting("Delete Cfg").value((Boolean) true);
    public BooleanSetting cleanLogs = new BooleanSetting("Clean Logs").value((Boolean) true);
    private int tickCount = 0;
    private boolean counting = false;

    public static UnHook getInstance() {
        return instance;
    }

    public UnHook() {
        addSettings(this.deleteCfg, this.cleanLogs);
    }

    @Override // sweetie.leonware.api.module.Module
    public void onEnable() {
        LeonWare.unhookCode = "leonware" + new Random().nextInt(1000);
        for (int i = 0; i < 10; i++) {
            if (class_310.method_1551().field_1724 != null) {
                class_310.method_1551().field_1724.method_7353(class_2561.method_30163("§cЧерез 5 секунд клиент анхукнется! Код: §f" + LeonWare.unhookCode), false);
            }
        }
        this.tickCount = 0;
        this.counting = true;
    }

    @Override // sweetie.leonware.api.module.Module
    public void onDisable() {
        this.counting = false;
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        TickEvent.getInstance().subscribe(new Listener(event -> {
            if (this.counting) {
                this.tickCount++;
                if (this.tickCount >= 100) {
                    doUnhook();
                    this.counting = false;
                    super.setEnabled(false, false);
                }
            }
        }));
    }

    private void doUnhook() {
        LeonWare.isUnhooked = true;
        LeonWare.unhookSnapshot.clear();
        for (Module m : ModuleManager.getInstance().getModules()) {
            if (m != this) {
                LeonWare.unhookSnapshot.put(m, Boolean.valueOf(m.isEnabled()));
                if (m.isEnabled()) {
                    m.setEnabled(false, false);
                }
            }
        }
        LeonWare.unhookSnapshot.put(this, false);
        DiscordHook.stopRPC();
        if (this.deleteCfg.getValue().booleanValue()) {
            File configFolder = new File(ClientInfo.CONFIG_PATH_MAIN);
            deleteDirectory(configFolder);
        }
        if (this.cleanLogs.getValue().booleanValue()) {
            cleanWindowsLogs();
        }
        class_310 mc = class_310.method_1551();
        mc.method_24288();
        try {
            mc.method_22683().method_4491(mc.method_45573(), class_8518.field_44650);
        } catch (Exception e) {
        }
    }

    private void deleteDirectory(File f) {
        if (f.exists()) {
            try {
                Files.walk(f.toPath(), new FileVisitOption[0]).sorted(Comparator.reverseOrder()).map((v0) -> {
                    return v0.toFile();
                }).forEach((v0) -> {
                    v0.delete();
                });
            } catch (Exception e) {
            }
        }
    }

    private void cleanWindowsLogs() {
        File[] list;
        try {
            String recentPath = System.getenv("APPDATA") + "\\Microsoft\\Windows\\Recent";
            File recentFolder = new File(recentPath);
            if (recentFolder.exists() && recentFolder.isDirectory() && (list = recentFolder.listFiles()) != null) {
                for (File f : list) {
                    if (f.getName().toLowerCase().contains(ClientInfo.NAME.toLowerCase()) || f.getName().toLowerCase().contains("leonware")) {
                        f.delete();
                    }
                }
            }
            Runtime.getRuntime().exec(new String[]{"powershell.exe", "-w", "hidden", "-Command", "Remove-Item -Path $env:APPDATA\\Microsoft\\Windows\\Recent\\* -Force -Recurse -ErrorAction SilentlyContinue"});
            Runtime.getRuntime().exec(new String[]{"powershell.exe", "-w", "hidden", "-Command", "Clear-RecycleBin -Force -ErrorAction SilentlyContinue"});
        } catch (Exception e) {
        }
    }
}
