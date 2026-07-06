// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.command;

import lombok.Generated;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sweetie.leonware.client.features.commands.CommandNeuro;
import sweetie.leonware.client.features.commands.CommandEClip;
import sweetie.leonware.client.features.commands.CommandBd;
import sweetie.leonware.client.features.commands.CommandHClip;
import sweetie.leonware.client.features.commands.CommandVClip;
import sweetie.leonware.client.features.commands.CommandAutoBuy;
import sweetie.leonware.client.features.commands.CommandAutoBean;
import sweetie.leonware.client.features.commands.CommandSpammer;
import sweetie.leonware.client.features.commands.CommandT;
import sweetie.leonware.client.features.commands.CommandToggle;
import sweetie.leonware.client.features.commands.CommandBind;
import sweetie.leonware.client.features.commands.CommandPrefix;
import sweetie.leonware.client.features.commands.CommandCfg;
import sweetie.leonware.client.features.commands.CommandSkin;
import sweetie.leonware.client.features.commands.CommandGps;
import sweetie.leonware.client.features.commands.CommandMacro;
import sweetie.leonware.client.features.commands.CommandStaffs;
import sweetie.leonware.client.features.commands.CommandFriend;
import sweetie.leonware.client.features.commands.CommandConfig;
import sweetie.leonware.client.features.modules.other.autobuy.AutoBuyManager;
import net.minecraft.class_634;
import net.minecraft.class_310;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_637;
import net.minecraft.class_2172;
import com.mojang.brigadier.CommandDispatcher;

public class CommandManager
{
    private static final CommandManager instance;
    private final CommandDispatcher<class_2172> dispatcher;
    private final class_637 source;
    private String prefix;
    private final List<Command> commands;
    
    public CommandManager() {
        this.prefix = "$";
        this.commands = new ArrayList<Command>();
        this.dispatcher = (CommandDispatcher<class_2172>)new CommandDispatcher();
        this.source = new class_637((class_634)null, class_310.method_1551());
    }
    
    public void load() {
        AutoBuyManager.getInstance();
        this.register(new CommandConfig(), new CommandFriend(), new CommandStaffs(), new CommandMacro(), new CommandGps(), new CommandSkin(), new CommandCfg(), new CommandPrefix(), new CommandBind(), new CommandToggle(), new CommandT(), new CommandSpammer(), new CommandAutoBean(), new CommandAutoBuy(), new CommandVClip(), new CommandHClip(), new CommandBd(), new CommandEClip(), new CommandNeuro());
    }
    
    public void register(final Command... commands) {
        for (final Command command : commands) {
            command.register(this.dispatcher);
            this.commands.add(command);
        }
    }
    
    public void executeCommands(final String message, final CallbackInfo ci) {
        if (message.startsWith(this.getPrefix())) {
            try {
                this.getDispatcher().execute(message.substring(this.getPrefix().length()), (Object)this.getSource());
            }
            catch (final CommandSyntaxException ex) {}
            ci.cancel();
        }
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
    public List<Command> getCommands() {
        return this.commands;
    }
    
    @Generated
    public static CommandManager getInstance() {
        return CommandManager.instance;
    }
    
    @Generated
    public String getPrefix() {
        return this.prefix;
    }
    
    @Generated
    public void setPrefix(final String prefix) {
        this.prefix = prefix;
    }
    
    static {
        instance = new CommandManager();
    }
}
