/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
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

public class ConfigSkin {
    private static final ConfigSkin instance = new ConfigSkin();
    private final Path configPath = Paths.get(ClientInfo.CONFIG_PATH_OTHER, "last_skin");
    private final TimerUtil timerUtil = new TimerUtil();
    private final AtomicBoolean fetchingInProgress = new AtomicBoolean(false);

    public void load() {
        try {
            if (!Files.exists(this.configPath.getParent(), new LinkOption[0])) {
                Files.createDirectories(this.configPath.getParent(), new FileAttribute[0]);
            }
            if (!Files.exists(this.configPath, new LinkOption[0])) {
                Files.createFile(this.configPath, new FileAttribute[0]);
                Files.writeString(this.configPath, (CharSequence)"", new OpenOption[0]);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        this.update();
    }

    public void save(String skinName) {
        try {
            if (skinName != null && !skinName.trim().isEmpty()) {
                Files.writeString(this.configPath, (CharSequence)skinName.trim(), new OpenOption[0]);
            } else {
                Files.writeString(this.configPath, (CharSequence)"", new OpenOption[0]);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String update() {
        try {
            if (Files.exists(this.configPath, new LinkOption[0])) {
                String content = Files.readString(this.configPath);
                boolean em = content.isEmpty();
                if (!em) {
                    CommandSkin.skinEnabled = true;
                }
                return em ? null : content;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void fetchSkin() {
        if (CommandSkin.skinEnabled && this.timerUtil.finished(2000L) && !this.fetchingInProgress.get()) {
            this.timerUtil.reset();
            this.fetchingInProgress.set(true);
            String skinName = this.update();
            Thread thread = new Thread(() -> {
                try {
                    CommandSkin.customSkinTextures = CommandSkin.createTextureSupplier(skinName);
                }
                catch (Exception exception) {
                }
                finally {
                    this.fetchingInProgress.set(false);
                }
            });
            thread.setName("SkinFetch-Thread");
            thread.setDaemon(true);
            thread.start();
        }
    }

    @Generated
    public static ConfigSkin getInstance() {
        return instance;
    }
}

