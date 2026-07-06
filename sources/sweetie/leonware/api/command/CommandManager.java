package sweetie.leonware.api.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import net.minecraft.class_2172;
import net.minecraft.class_310;
import net.minecraft.class_634;
import net.minecraft.class_637;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sweetie.leonware.client.features.commands.CommandAutoBean;
import sweetie.leonware.client.features.commands.CommandAutoBuy;
import sweetie.leonware.client.features.commands.CommandBd;
import sweetie.leonware.client.features.commands.CommandBind;
import sweetie.leonware.client.features.commands.CommandCfg;
import sweetie.leonware.client.features.commands.CommandConfig;
import sweetie.leonware.client.features.commands.CommandEClip;
import sweetie.leonware.client.features.commands.CommandFriend;
import sweetie.leonware.client.features.commands.CommandGps;
import sweetie.leonware.client.features.commands.CommandHClip;
import sweetie.leonware.client.features.commands.CommandMacro;
import sweetie.leonware.client.features.commands.CommandNeuro;
import sweetie.leonware.client.features.commands.CommandPrefix;
import sweetie.leonware.client.features.commands.CommandSkin;
import sweetie.leonware.client.features.commands.CommandSpammer;
import sweetie.leonware.client.features.commands.CommandStaffs;
import sweetie.leonware.client.features.commands.CommandT;
import sweetie.leonware.client.features.commands.CommandToggle;
import sweetie.leonware.client.features.commands.CommandVClip;
import sweetie.leonware.client.features.modules.other.autobuy.AutoBuyManager;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/command/CommandManager.class */
public class CommandManager {
    private static final CommandManager instance = new CommandManager();
    private String prefix = "$";
    private final List<Command> commands = new ArrayList();
    private final CommandDispatcher<class_2172> dispatcher = new CommandDispatcher<>();
    private final class_637 source = new class_637((class_634) null, class_310.method_1551());

    @Generated
    public static CommandManager getInstance() {
        return instance;
    }

    @Generated
    public CommandDispatcher<class_2172> getDispatcher() {
        return this.dispatcher;
    }

    @Generated
    public class_637 getSource() {
        return this.source;
    }

    @Generated
    public String getPrefix() {
        return this.prefix;
    }

    @Generated
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Generated
    public List<Command> getCommands() {
        return this.commands;
    }

    public void load() {
        AutoBuyManager.getInstance();
        register(new CommandConfig(), new CommandFriend(), new CommandStaffs(), new CommandMacro(), new CommandGps(), new CommandSkin(), new CommandCfg(), new CommandPrefix(), new CommandBind(), new CommandToggle(), new CommandT(), new CommandSpammer(), new CommandAutoBean(), new CommandAutoBuy(), new CommandVClip(), new CommandHClip(), new CommandBd(), new CommandEClip(), new CommandNeuro());
    }

    public void register(Command... commands) {
        for (Command command : commands) {
            command.register(this.dispatcher);
            this.commands.add(command);
        }
    }

    public void executeCommands(String message, CallbackInfo ci) {
        if (message.startsWith(getPrefix())) {
            try {
                getDispatcher().execute(message.substring(getPrefix().length()), getSource());
            } catch (CommandSyntaxException e) {
            }
            ci.cancel();
        }
    }
}
