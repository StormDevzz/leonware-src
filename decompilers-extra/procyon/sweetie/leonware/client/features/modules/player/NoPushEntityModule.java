// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.player;

import lombok.Generated;
import net.minecraft.class_1297;
import net.minecraft.class_2596;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import net.minecraft.class_1536;
import net.minecraft.class_1937;
import net.minecraft.class_2663;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.MultiBooleanSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "No Push Entity", category = Category.PLAYER)
public class NoPushEntityModule extends Module
{
    private static final NoPushEntityModule instance;
    private final MultiBooleanSetting pushing;
    
    public NoPushEntityModule() {
        this.pushing = new MultiBooleanSetting("Pushing").value(new BooleanSetting("Block").value(true), new BooleanSetting("Liquids").value(true), new BooleanSetting("Entity").value(true), new BooleanSetting("Fishing rod").value(true));
        this.addSettings(this.pushing);
    }
    
    @Override
    public void onEvent() {
        final EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener<PacketEvent.PacketEventData>(event -> {
            if (!event.isReceive()) {
                return;
            }
            else if (!this.pushing.isEnabled("Fishing rod")) {
                return;
            }
            else {
                final class_2596 patt0$temp = event.packet();
                if (patt0$temp instanceof final class_2663 packet) {
                    if (packet.method_11470() == 31) {
                        final class_1297 patt1$temp = packet.method_11469((class_1937)NoPushEntityModule.mc.field_1687);
                        if (patt1$temp instanceof final class_1536 hook) {
                            if (hook.method_26957() == NoPushEntityModule.mc.field_1724) {
                                PacketEvent.getInstance().setCancel(true);
                            }
                        }
                    }
                }
                return;
            }
        }));
        this.addEvents(packetEvent);
    }
    
    public boolean cancelPush(final PushingSource data) {
        if (this.isEnabled()) {
            switch (data.ordinal()) {
                default: {
                    throw new MatchException(null, null);
                }
                case 0: {
                    if (this.pushing.isEnabled("Block")) {
                        break;
                    }
                    return false;
                }
                case 1: {
                    if (this.pushing.isEnabled("Liquids")) {
                        break;
                    }
                    return false;
                }
                case 2: {
                    if (this.pushing.isEnabled("Entity")) {
                        break;
                    }
                    return false;
                }
                case 3: {
                    if (this.pushing.isEnabled("Fishing rod")) {
                        break;
                    }
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    
    @Generated
    public static NoPushEntityModule getInstance() {
        return NoPushEntityModule.instance;
    }
    
    static {
        instance = new NoPushEntityModule();
    }
    
    public enum PushingSource
    {
        BLOCK, 
        LIQUIDS, 
        ENTITY, 
        FISHING_ROD;
    }
}
