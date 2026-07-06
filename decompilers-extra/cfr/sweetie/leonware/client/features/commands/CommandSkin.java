/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  com.mojang.authlib.yggdrasil.ProfileResult
 *  com.mojang.brigadier.arguments.StringArgumentType
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  com.mojang.brigadier.context.CommandContext
 *  net.minecraft.class_2172
 *  net.minecraft.class_640
 *  net.minecraft.class_8685
 */
package sweetie.leonware.client.features.commands;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.yggdrasil.ProfileResult;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
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

@CommandRegister(name="skin")
public class CommandSkin
extends Command {
    public static Supplier<class_8685> customSkinTextures = null;
    public static boolean skinEnabled = false;

    @Override
    public void execute(LiteralArgumentBuilder<class_2172> builder) {
        builder.then(CommandSkin.literal("off").executes(context -> {
            if (!skinEnabled) {
                this.print("\u0421\u043a\u0438\u043d \u0443\u0436\u0435 \u0441\u0431\u0440\u043e\u0448\u0435\u043d!");
            } else {
                customSkinTextures = null;
                skinEnabled = false;
                ConfigSkin.getInstance().save(null);
                this.print("\u0421\u043a\u0438\u043d \u0443\u0441\u043f\u0435\u0448\u043d\u043e \u0441\u0431\u0440\u043e\u0448\u0435\u043d!");
            }
            return this.SINGLE_SUCCESS;
        }));
        builder.then(CommandSkin.literal("set").then(CommandSkin.argument("name", StringArgumentType.string()).executes(context -> {
            String username = StringArgumentType.getString((CommandContext)context, (String)"name");
            try {
                customSkinTextures = CommandSkin.createTextureSupplier(username);
                skinEnabled = true;
                ConfigSkin.getInstance().save(username);
                this.print("\u0423\u0441\u0442\u0430\u043d\u043e\u0432\u043b\u0435\u043d \u0441\u043a\u0438\u043d: " + username);
            }
            catch (Exception e) {
                this.print("\u041d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u0443\u0441\u0442\u0430\u043d\u043e\u0432\u0438\u0442\u044c \u0441\u043a\u0438\u043d :c");
            }
            return this.SINGLE_SUCCESS;
        })));
    }

    public static Supplier<class_8685> createTextureSupplier(String username) {
        ProfileResult hui;
        GameProfile profile;
        UUID uuid = new ProfileRepository().uuidByName(username);
        if (uuid == null) {
            uuid = UUIDUtils.generateOfflinePlayerUuid(username);
        }
        GameProfile gameProfile = profile = (hui = mc.method_1495().fetchProfile(uuid, false)) == null ? null : hui.profile();
        if (profile == null) {
            profile = new GameProfile(uuid, username);
        }
        return class_640.method_52803((GameProfile)profile);
    }

    public static Supplier<class_8685> getCustomSkinTextures() {
        return skinEnabled ? customSkinTextures : null;
    }
}

