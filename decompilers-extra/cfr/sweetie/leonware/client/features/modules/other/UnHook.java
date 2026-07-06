/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2561
 *  net.minecraft.class_310
 *  net.minecraft.class_3262
 *  net.minecraft.class_8518
 */
package sweetie.leonware.client.features.modules.other;

import java.io.File;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Random;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_3262;
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

@ModuleRegister(name="UnHook", category=Category.OTHER, bind=-999)
public class UnHook
extends Module {
    private static final UnHook instance = new UnHook();
    public BooleanSetting deleteCfg = new BooleanSetting("Delete Cfg").value(true);
    public BooleanSetting cleanLogs = new BooleanSetting("Clean Logs").value(true);
    private int tickCount = 0;
    private boolean counting = false;

    public static UnHook getInstance() {
        return instance;
    }

    public UnHook() {
        this.addSettings(this.deleteCfg, this.cleanLogs);
    }

    @Override
    public void onEnable() {
        LeonWare.unhookCode = "leonware" + new Random().nextInt(1000);
        for (int i = 0; i < 10; ++i) {
            if (class_310.method_1551().field_1724 == null) continue;
            class_310.method_1551().field_1724.method_7353(class_2561.method_30163((String)("\u00a7c\u0427\u0435\u0440\u0435\u0437 5 \u0441\u0435\u043a\u0443\u043d\u0434 \u043a\u043b\u0438\u0435\u043d\u0442 \u0430\u043d\u0445\u0443\u043a\u043d\u0435\u0442\u0441\u044f! \u041a\u043e\u0434: \u00a7f" + LeonWare.unhookCode)), false);
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
            if (!this.counting) {
                return;
            }
            ++this.tickCount;
            if (this.tickCount >= 100) {
                this.doUnhook();
                this.counting = false;
                super.setEnabled(false, false);
            }
        }));
    }

    private void doUnhook() {
        LeonWare.isUnhooked = true;
        LeonWare.unhookSnapshot.clear();
        for (Module m : ModuleManager.getInstance().getModules()) {
            if (m == this) continue;
            LeonWare.unhookSnapshot.put(m, m.isEnabled());
            if (!m.isEnabled()) continue;
            m.setEnabled(false, false);
        }
        LeonWare.unhookSnapshot.put(this, false);
        DiscordHook.stopRPC();
        if (((Boolean)this.deleteCfg.getValue()).booleanValue()) {
            File configFolder = new File(ClientInfo.CONFIG_PATH_MAIN);
            this.deleteDirectory(configFolder);
        }
        if (((Boolean)this.cleanLogs.getValue()).booleanValue()) {
            this.cleanWindowsLogs();
        }
        class_310 mc = class_310.method_1551();
        mc.method_24288();
        try {
            mc.method_22683().method_4491((class_3262)mc.method_45573(), class_8518.field_44650);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private void deleteDirectory(File f) {
        if (!f.exists()) {
            return;
        }
        try {
            Files.walk(f.toPath(), new FileVisitOption[0]).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private void cleanWindowsLogs() {
        try {
            File[] list;
            String recentPath = System.getenv("APPDATA") + "\\Microsoft\\Windows\\Recent";
            File recentFolder = new File(recentPath);
            if (recentFolder.exists() && recentFolder.isDirectory() && (list = recentFolder.listFiles()) != null) {
                for (File f : list) {
                    if (!f.getName().toLowerCase().contains("LeonWare".toLowerCase()) && !f.getName().toLowerCase().contains("leonware")) continue;
                    f.delete();
                }
            }
            Runtime.getRuntime().exec(new String[]{"powershell.exe", "-w", "hidden", "-Command", "Remove-Item -Path $env:APPDATA\\Microsoft\\Windows\\Recent\\* -Force -Recurse -ErrorAction SilentlyContinue"});
            Runtime.getRuntime().exec(new String[]{"powershell.exe", "-w", "hidden", "-Command", "Clear-RecycleBin -Force -ErrorAction SilentlyContinue"});
        }
        catch (Exception exception) {
            // empty catch block
        }
    }
}

