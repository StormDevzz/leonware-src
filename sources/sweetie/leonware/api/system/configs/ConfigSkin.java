package sweetie.leonware.api.system.configs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.Generated;
import sweetie.leonware.api.system.backend.ClientInfo;
import sweetie.leonware.api.utils.math.TimerUtil;
import sweetie.leonware.client.features.commands.CommandSkin;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/system/configs/ConfigSkin.class */
public class ConfigSkin {
    private static final ConfigSkin instance = new ConfigSkin();
    private final Path configPath = Paths.get(ClientInfo.CONFIG_PATH_OTHER, "last_skin");
    private final TimerUtil timerUtil = new TimerUtil();
    private final AtomicBoolean fetchingInProgress = new AtomicBoolean(false);

    @Generated
    public static ConfigSkin getInstance() {
        return instance;
    }

    public void load() {
        try {
            if (!Files.exists(this.configPath.getParent(), new LinkOption[0])) {
                Files.createDirectories(this.configPath.getParent(), new FileAttribute[0]);
            }
            if (!Files.exists(this.configPath, new LinkOption[0])) {
                Files.createFile(this.configPath, new FileAttribute[0]);
                Files.writeString(this.configPath, "", new OpenOption[0]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        update();
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0021 A[Catch: IOException -> 0x0032, TryCatch #0 {IOException -> 0x0032, blocks: (B:4:0x0004, B:6:0x000e, B:7:0x0021), top: B:12:0x0004 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void save(java.lang.String r5) {
        /*
            r4 = this;
            r0 = r5
            if (r0 == 0) goto L21
            r0 = r5
            java.lang.String r0 = r0.trim()     // Catch: java.io.IOException -> L32
            boolean r0 = r0.isEmpty()     // Catch: java.io.IOException -> L32
            if (r0 != 0) goto L21
            r0 = r4
            java.nio.file.Path r0 = r0.configPath     // Catch: java.io.IOException -> L32
            r1 = r5
            java.lang.String r1 = r1.trim()     // Catch: java.io.IOException -> L32
            r2 = 0
            java.nio.file.OpenOption[] r2 = new java.nio.file.OpenOption[r2]     // Catch: java.io.IOException -> L32
            java.nio.file.Path r0 = java.nio.file.Files.writeString(r0, r1, r2)     // Catch: java.io.IOException -> L32
            goto L2f
        L21:
            r0 = r4
            java.nio.file.Path r0 = r0.configPath     // Catch: java.io.IOException -> L32
            java.lang.String r1 = ""
            r2 = 0
            java.nio.file.OpenOption[] r2 = new java.nio.file.OpenOption[r2]     // Catch: java.io.IOException -> L32
            java.nio.file.Path r0 = java.nio.file.Files.writeString(r0, r1, r2)     // Catch: java.io.IOException -> L32
        L2f:
            goto L37
        L32:
            r6 = move-exception
            r0 = r6
            r0.printStackTrace()
        L37:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: sweetie.leonware.api.system.configs.ConfigSkin.save(java.lang.String):void");
    }

    public String update() {
        try {
            if (Files.exists(this.configPath, new LinkOption[0])) {
                String content = Files.readString(this.configPath);
                boolean em = content.isEmpty();
                if (!em) {
                    CommandSkin.skinEnabled = true;
                }
                if (em) {
                    return null;
                }
                return content;
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void fetchSkin() {
        if (CommandSkin.skinEnabled && this.timerUtil.finished(2000L) && !this.fetchingInProgress.get()) {
            this.timerUtil.reset();
            this.fetchingInProgress.set(true);
            String skinName = update();
            Thread thread = new Thread(() -> {
                try {
                    CommandSkin.customSkinTextures = CommandSkin.createTextureSupplier(skinName);
                } catch (Exception e) {
                } finally {
                    this.fetchingInProgress.set(false);
                }
            });
            thread.setName("SkinFetch-Thread");
            thread.setDaemon(true);
            thread.start();
        }
    }
}
