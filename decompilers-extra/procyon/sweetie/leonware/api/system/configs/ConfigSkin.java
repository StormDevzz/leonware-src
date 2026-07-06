// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.system.configs;

import lombok.Generated;
import sweetie.leonware.client.features.commands.CommandSkin;
import java.io.IOException;
import java.nio.file.OpenOption;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import sweetie.leonware.api.system.backend.ClientInfo;
import java.util.concurrent.atomic.AtomicBoolean;
import sweetie.leonware.api.utils.math.TimerUtil;
import java.nio.file.Path;

public class ConfigSkin
{
    private static final ConfigSkin instance;
    private final Path configPath;
    private final TimerUtil timerUtil;
    private final AtomicBoolean fetchingInProgress;
    
    public ConfigSkin() {
        this.configPath = Paths.get(ClientInfo.CONFIG_PATH_OTHER, "last_skin");
        this.timerUtil = new TimerUtil();
        this.fetchingInProgress = new AtomicBoolean(false);
    }
    
    public void load() {
        try {
            if (!Files.exists(this.configPath.getParent(), new LinkOption[0])) {
                Files.createDirectories(this.configPath.getParent(), (FileAttribute<?>[])new FileAttribute[0]);
            }
            if (!Files.exists(this.configPath, new LinkOption[0])) {
                Files.createFile(this.configPath, (FileAttribute<?>[])new FileAttribute[0]);
                Files.writeString(this.configPath, "", new OpenOption[0]);
            }
        }
        catch (final IOException e) {
            e.printStackTrace();
        }
        this.update();
    }
    
    public void save(final String skinName) {
        try {
            if (skinName != null && !skinName.trim().isEmpty()) {
                Files.writeString(this.configPath, skinName.trim(), new OpenOption[0]);
            }
            else {
                Files.writeString(this.configPath, "", new OpenOption[0]);
            }
        }
        catch (final IOException e) {
            e.printStackTrace();
        }
    }
    
    public String update() {
        try {
            if (Files.exists(this.configPath, new LinkOption[0])) {
                final String content = Files.readString(this.configPath);
                final boolean em = content.isEmpty();
                if (!em) {
                    CommandSkin.skinEnabled = true;
                }
                return em ? null : content;
            }
        }
        catch (final IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public void fetchSkin() {
        if (CommandSkin.skinEnabled && this.timerUtil.finished(2000L) && !this.fetchingInProgress.get()) {
            this.timerUtil.reset();
            this.fetchingInProgress.set(true);
            final String skinName = this.update();
            final Thread thread = new Thread(() -> {
                try {
                    CommandSkin.customSkinTextures = CommandSkin.createTextureSupplier(skinName);
                }
                catch (final Exception ex) {}
                finally {
                    this.fetchingInProgress.set(false);
                }
                return;
            });
            thread.setName("SkinFetch-Thread");
            thread.setDaemon(true);
            thread.start();
        }
    }
    
    @Generated
    public static ConfigSkin getInstance() {
        return ConfigSkin.instance;
    }
    
    static {
        instance = new ConfigSkin();
    }
}
