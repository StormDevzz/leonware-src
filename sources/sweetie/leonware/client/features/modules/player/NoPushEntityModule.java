package sweetie.leonware.client.features.modules.player;

import lombok.Generated;
import net.minecraft.class_1536;
import net.minecraft.class_2663;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.MultiBooleanSetting;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/player/NoPushEntityModule.class */
@ModuleRegister(name = "No Push Entity", category = Category.PLAYER)
public class NoPushEntityModule extends Module {
    private static final NoPushEntityModule instance = new NoPushEntityModule();
    private final MultiBooleanSetting pushing = new MultiBooleanSetting("Pushing").value(new BooleanSetting("Block").value((Boolean) true), new BooleanSetting("Liquids").value((Boolean) true), new BooleanSetting("Entity").value((Boolean) true), new BooleanSetting("Fishing rod").value((Boolean) true));

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/player/NoPushEntityModule$PushingSource.class */
    public enum PushingSource {
        BLOCK,
        LIQUIDS,
        ENTITY,
        FISHING_ROD
    }

    @Generated
    public static NoPushEntityModule getInstance() {
        return instance;
    }

    public NoPushEntityModule() {
        addSettings(this.pushing);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener(event -> {
            if (event.isReceive() && this.pushing.isEnabled("Fishing rod")) {
                class_2663 class_2663VarPacket = event.packet();
                if (class_2663VarPacket instanceof class_2663) {
                    class_2663 packet = class_2663VarPacket;
                    if (packet.method_11470() == 31) {
                        class_1536 class_1536VarMethod_11469 = packet.method_11469(mc.field_1687);
                        if (class_1536VarMethod_11469 instanceof class_1536) {
                            class_1536 hook = class_1536VarMethod_11469;
                            if (hook.method_26957() == mc.field_1724) {
                                PacketEvent.getInstance().setCancel(true);
                            }
                        }
                    }
                }
            }
        }));
        addEvents(packetEvent);
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: java.lang.MatchException */
    /* JADX WARN: Removed duplicated region for block: B:19:0x006b A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean cancelPush(sweetie.leonware.client.features.modules.player.NoPushEntityModule.PushingSource r6) throws java.lang.MatchException {
        /*
            r5 = this;
            r0 = r5
            boolean r0 = r0.isEnabled()
            if (r0 == 0) goto L6f
            r0 = r6
            int r0 = r0.ordinal()
            switch(r0) {
                case 0: goto L32;
                case 1: goto L41;
                case 2: goto L50;
                case 3: goto L5f;
                default: goto L28;
            }
        L28:
            java.lang.MatchException r0 = new java.lang.MatchException
            r1 = r0
            r2 = 0
            r3 = 0
            r1.<init>(r2, r3)
            throw r0
        L32:
            r0 = r5
            sweetie.leonware.api.module.setting.MultiBooleanSetting r0 = r0.pushing
            java.lang.String r1 = "Block"
            boolean r0 = r0.isEnabled(r1)
            if (r0 == 0) goto L6f
            goto L6b
        L41:
            r0 = r5
            sweetie.leonware.api.module.setting.MultiBooleanSetting r0 = r0.pushing
            java.lang.String r1 = "Liquids"
            boolean r0 = r0.isEnabled(r1)
            if (r0 == 0) goto L6f
            goto L6b
        L50:
            r0 = r5
            sweetie.leonware.api.module.setting.MultiBooleanSetting r0 = r0.pushing
            java.lang.String r1 = "Entity"
            boolean r0 = r0.isEnabled(r1)
            if (r0 == 0) goto L6f
            goto L6b
        L5f:
            r0 = r5
            sweetie.leonware.api.module.setting.MultiBooleanSetting r0 = r0.pushing
            java.lang.String r1 = "Fishing rod"
            boolean r0 = r0.isEnabled(r1)
            if (r0 == 0) goto L6f
        L6b:
            r0 = 1
            goto L70
        L6f:
            r0 = 0
        L70:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: sweetie.leonware.client.features.modules.player.NoPushEntityModule.cancelPush(sweetie.leonware.client.features.modules.player.NoPushEntityModule$PushingSource):boolean");
    }
}
