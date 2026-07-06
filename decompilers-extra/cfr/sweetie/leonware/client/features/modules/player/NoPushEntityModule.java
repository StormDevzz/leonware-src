/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1297
 *  net.minecraft.class_1536
 *  net.minecraft.class_1937
 *  net.minecraft.class_2596
 *  net.minecraft.class_2663
 */
package sweetie.leonware.client.features.modules.player;

import lombok.Generated;
import net.minecraft.class_1297;
import net.minecraft.class_1536;
import net.minecraft.class_1937;
import net.minecraft.class_2596;
import net.minecraft.class_2663;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.MultiBooleanSetting;

@ModuleRegister(name="No Push Entity", category=Category.PLAYER)
public class NoPushEntityModule
extends Module {
    private static final NoPushEntityModule instance = new NoPushEntityModule();
    private final MultiBooleanSetting pushing = new MultiBooleanSetting("Pushing").value(new BooleanSetting("Block").value(true), new BooleanSetting("Liquids").value(true), new BooleanSetting("Entity").value(true), new BooleanSetting("Fishing rod").value(true));

    public NoPushEntityModule() {
        this.addSettings(this.pushing);
    }

    @Override
    public void onEvent() {
        EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener<PacketEvent.PacketEventData>(event -> {
            class_1536 hook;
            class_1297 patt1$temp;
            class_2663 packet;
            if (!event.isReceive()) {
                return;
            }
            if (!this.pushing.isEnabled("Fishing rod")) {
                return;
            }
            class_2596<?> patt0$temp = event.packet();
            if (patt0$temp instanceof class_2663 && (packet = (class_2663)patt0$temp).method_11470() == 31 && (patt1$temp = packet.method_11469((class_1937)NoPushEntityModule.mc.field_1687)) instanceof class_1536 && (hook = (class_1536)patt1$temp).method_26957() == NoPushEntityModule.mc.field_1724) {
                PacketEvent.getInstance().setCancel(true);
            }
        }));
        this.addEvents(packetEvent);
    }

    public boolean cancelPush(PushingSource data) {
        boolean bl;
        block10: {
            block9: {
                if (!this.isEnabled()) break block9;
                switch (data.ordinal()) {
                    default: {
                        throw new MatchException(null, null);
                    }
                    case 0: {
                        if (this.pushing.isEnabled("Block")) {
                            break;
                        }
                        break block9;
                    }
                    case 1: {
                        if (this.pushing.isEnabled("Liquids")) {
                            break;
                        }
                        break block9;
                    }
                    case 2: {
                        if (this.pushing.isEnabled("Entity")) {
                            break;
                        }
                        break block9;
                    }
                    case 3: {
                        if (!this.pushing.isEnabled("Fishing rod")) break block9;
                    }
                }
                bl = true;
                break block10;
            }
            bl = false;
        }
        return bl;
    }

    @Generated
    public static NoPushEntityModule getInstance() {
        return instance;
    }

    public static enum PushingSource {
        BLOCK,
        LIQUIDS,
        ENTITY,
        FISHING_ROD;

    }
}

