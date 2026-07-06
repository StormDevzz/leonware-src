// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.commands;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import sweetie.leonware.api.system.configs.ConfigSkin;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.authlib.yggdrasil.ProfileResult;
import java.util.UUID;
import net.minecraft.class_640;
import com.mojang.authlib.GameProfile;
import sweetie.leonware.api.auth.UUIDUtils;
import sweetie.leonware.api.auth.ProfileRepository;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.class_2172;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.class_8685;
import java.util.function.Supplier;
import sweetie.leonware.api.command.CommandRegister;
import sweetie.leonware.api.command.Command;

@CommandRegister(name = "skin")
public class CommandSkin extends Command
{
    public static Supplier<class_8685> customSkinTextures;
    public static boolean skinEnabled;
    
    @Override
    public void execute(final LiteralArgumentBuilder<class_2172> builder) {
        builder.then(Command.literal("off").executes(context -> {
            if (!CommandSkin.skinEnabled) {
                this.print("\u0421\u043a\u0438\u043d \u0443\u0436\u0435 \u0441\u0431\u0440\u043e\u0448\u0435\u043d!");
            }
            else {
                CommandSkin.customSkinTextures = null;
                CommandSkin.skinEnabled = false;
                ConfigSkin.getInstance().save(null);
                this.print("\u0421\u043a\u0438\u043d \u0443\u0441\u043f\u0435\u0448\u043d\u043e \u0441\u0431\u0440\u043e\u0448\u0435\u043d!");
            }
            return this.SINGLE_SUCCESS;
        }));
        builder.then(Command.literal("set").then(Command.argument("name", (com.mojang.brigadier.arguments.ArgumentType<Object>)StringArgumentType.string()).executes(context -> {
            final String username = StringArgumentType.getString(context, "name");
            try {
                CommandSkin.customSkinTextures = createTextureSupplier(username);
                CommandSkin.skinEnabled = true;
                ConfigSkin.getInstance().save(username);
                this.print("\u0423\u0441\u0442\u0430\u043d\u043e\u0432\u043b\u0435\u043d \u0441\u043a\u0438\u043d: " + username);
            }
            catch (final Exception e) {
                this.print("\u041d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u0443\u0441\u0442\u0430\u043d\u043e\u0432\u0438\u0442\u044c \u0441\u043a\u0438\u043d :c");
            }
            return this.SINGLE_SUCCESS;
        })));
    }
    
    public static Supplier<class_8685> createTextureSupplier(final String username) {
        UUID uuid = new ProfileRepository().uuidByName(username);
        if (uuid == null) {
            uuid = UUIDUtils.generateOfflinePlayerUuid(username);
        }
        final ProfileResult hui = CommandSkin.mc.method_1495().fetchProfile(uuid, false);
        GameProfile profile = (hui == null) ? null : hui.profile();
        if (profile == null) {
            profile = new GameProfile(uuid, username);
        }
        return class_640.method_52803(profile);
    }
    
    public static Supplier<class_8685> getCustomSkinTextures() {
        return CommandSkin.skinEnabled ? CommandSkin.customSkinTextures : null;
    }
    
    static {
        CommandSkin.customSkinTextures = null;
        CommandSkin.skinEnabled = false;
    }
}
