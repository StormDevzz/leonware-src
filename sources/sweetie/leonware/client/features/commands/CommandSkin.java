package sweetie.leonware.client.features.commands;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.yggdrasil.ProfileResult;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.UUID;
import java.util.function.Supplier;
import net.minecraft.class_2172;
import net.minecraft.class_640;
import net.minecraft.class_8685;
import sweetie.leonware.api.auth.ProfileRepository;
import sweetie.leonware.api.auth.UUIDUtils;
import sweetie.leonware.api.command.Command;
import sweetie.leonware.api.command.CommandRegister;
import sweetie.leonware.api.system.configs.ConfigSkin;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/commands/CommandSkin.class */
@CommandRegister(name = "skin")
public class CommandSkin extends Command {
    public static Supplier<class_8685> customSkinTextures = null;
    public static boolean skinEnabled = false;

    @Override // sweetie.leonware.api.command.Command
    public void execute(LiteralArgumentBuilder<class_2172> builder) {
        builder.then(literal("off").executes(context -> {
            if (!skinEnabled) {
                print("Скин уже сброшен!");
            } else {
                customSkinTextures = null;
                skinEnabled = false;
                ConfigSkin.getInstance().save(null);
                print("Скин успешно сброшен!");
            }
            return this.SINGLE_SUCCESS;
        }));
        builder.then(literal("set").then(argument("name", StringArgumentType.string()).executes(context2 -> {
            String username = StringArgumentType.getString(context2, "name");
            try {
                customSkinTextures = createTextureSupplier(username);
                skinEnabled = true;
                ConfigSkin.getInstance().save(username);
                print("Установлен скин: " + username);
            } catch (Exception e) {
                print("Не удалось установить скин :c");
            }
            return this.SINGLE_SUCCESS;
        })));
    }

    public static Supplier<class_8685> createTextureSupplier(String username) {
        UUID uuid = new ProfileRepository().uuidByName(username);
        if (uuid == null) {
            uuid = UUIDUtils.generateOfflinePlayerUuid(username);
        }
        ProfileResult hui = mc.method_1495().fetchProfile(uuid, false);
        GameProfile profile = hui == null ? null : hui.profile();
        if (profile == null) {
            profile = new GameProfile(uuid, username);
        }
        return class_640.method_52803(profile);
    }

    public static Supplier<class_8685> getCustomSkinTextures() {
        if (skinEnabled) {
            return customSkinTextures;
        }
        return null;
    }
}
