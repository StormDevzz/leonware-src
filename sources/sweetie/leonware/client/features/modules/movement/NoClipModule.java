package sweetie.leonware.client.features.modules.movement;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.Generated;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_2596;
import net.minecraft.class_2680;
import net.minecraft.class_2828;
import net.minecraft.class_2846;
import net.minecraft.class_3532;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.event.events.player.move.CollisionEvent;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/movement/NoClipModule.class */
@ModuleRegister(name = "No Clip", category = Category.MOVEMENT)
public class NoClipModule extends Module {
    private static final NoClipModule instance = new NoClipModule();
    private final ModeSetting mode = new ModeSetting("Режим").values("Default", "ForceMine", "Pearl Phase").value("Default");
    private final BooleanSetting strict = new BooleanSetting("Strict").value((Boolean) false).setVisible(() -> {
        return Boolean.valueOf(this.mode.is("ForceMine"));
    });
    private final List<class_2596<?>> packets = new CopyOnWriteArrayList();
    private boolean semiPacketSent;
    private boolean skipReleaseOnDisable;

    @Generated
    public static NoClipModule getInstance() {
        return instance;
    }

    @Generated
    public ModeSetting getMode() {
        return this.mode;
    }

    @Generated
    public BooleanSetting getStrict() {
        return this.strict;
    }

    /* JADX WARN: Type inference failed for: r1v5, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    public NoClipModule() {
        addSettings(this.mode, this.strict);
    }

    @Override // sweetie.leonware.api.module.Module
    public void onEnable() {
        this.packets.clear();
        this.semiPacketSent = false;
        this.skipReleaseOnDisable = false;
    }

    @Override // sweetie.leonware.api.module.Module
    public void onDisable() {
        if (this.mode.is("Default")) {
            if (!this.skipReleaseOnDisable && this.semiPacketSent) {
                double x = mc.field_1724.method_23317();
                double y = mc.field_1724.method_23318();
                double z = mc.field_1724.method_23321();
                float yaw = mc.field_1724.method_36454();
                float pitch = mc.field_1724.method_36455();
                mc.field_1724.field_3944.method_52787(new class_2828.class_2830(x - 5000.0d, y, z - 5000.0d, yaw, pitch, false, false));
                mc.field_1724.field_3944.method_52787(new class_2828.class_2830(x, y, z, yaw, pitch, mc.field_1724.method_24828(), false));
            }
            if (mc.field_1724 != null && mc.field_1724.field_3944 != null && !this.packets.isEmpty()) {
                for (class_2596<?> packet : this.packets) {
                    sendSilentPacket(packet);
                }
                this.packets.clear();
            }
        }
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener(event -> {
            if (this.mode.is("Default") && event.isSend()) {
                class_2596<?> p = event.packet();
                if (p instanceof class_2828) {
                    this.packets.add(p);
                    PacketEvent.getInstance().setCancel(true);
                }
            }
        }));
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener(event2 -> {
            if (mc.field_1724 == null || mc.field_1687 == null) {
                return;
            }
            if (!this.mode.is("Default")) {
                if (this.mode.is("ForceMine")) {
                    if ((!mc.field_1724.field_5976 && !playerInsideBlock()) || mc.field_1724.method_5869() || mc.field_1724.method_5771()) {
                        return;
                    }
                    for (int x = -2; x < 2; x++) {
                        for (int y = -1; y < 3; y++) {
                            for (int z = -2; z < 2; z++) {
                                if (((x != 0 || y != 0 || z != 0) && (x != 0 || y != 1 || z != 0)) || mc.field_1690.field_1832.method_1434()) {
                                    class_2338 bp = class_2338.method_49638(mc.field_1724.method_19538()).method_10069(x, y, z);
                                    if (mc.field_1724.method_5829().method_994(new class_238(bp)) && !mc.field_1687.method_22347(bp)) {
                                        mc.field_1724.field_3944.method_52787(new class_2846(class_2846.class_2847.field_12973, bp, class_2350.field_11036));
                                    }
                                }
                            }
                        }
                    }
                    return;
                }
                return;
            }
            mc.field_1724.method_18800(mc.field_1724.method_18798().field_1352, 0.0d, mc.field_1724.method_18798().field_1350);
            class_238 box = mc.field_1724.method_5829().method_1014(0.001d);
            int minX = class_3532.method_15357(box.field_1323);
            int minY = class_3532.method_15357(box.field_1322);
            int minZ = class_3532.method_15357(box.field_1321);
            int maxX = class_3532.method_15357(box.field_1320);
            int maxY = class_3532.method_15357(box.field_1325);
            int maxZ = class_3532.method_15357(box.field_1324);
            long totalStates = 0;
            long solidStates = 0;
            for (int x2 = minX; x2 <= maxX; x2++) {
                for (int y2 = minY; y2 <= maxY; y2++) {
                    for (int z2 = minZ; z2 <= maxZ; z2++) {
                        class_2338 pos = new class_2338(x2, y2, z2);
                        class_2680 state = mc.field_1687.method_8320(pos);
                        totalStates++;
                        if (state.method_51367()) {
                            solidStates++;
                        }
                    }
                }
            }
            boolean noSolidInAABB = solidStates == 0;
            boolean semiInsideBlock = solidStates > 0 && solidStates < totalStates;
            if (!this.semiPacketSent && semiInsideBlock) {
                double x3 = mc.field_1724.method_23317();
                double y3 = mc.field_1724.method_23318();
                double z3 = mc.field_1724.method_23321();
                float yaw = mc.field_1724.method_36454();
                float pitch = mc.field_1724.method_36455();
                boolean onGround = mc.field_1724.method_24828();
                for (int i = 0; i < 2; i++) {
                    mc.field_1724.field_3944.method_52787(new class_2828.class_2830(x3, y3, z3, yaw, pitch, onGround, false));
                }
                this.semiPacketSent = true;
                return;
            }
            if (this.semiPacketSent && noSolidInAABB) {
                this.skipReleaseOnDisable = true;
                toggle();
            }
        }));
        EventListener collisionEvent = CollisionEvent.getInstance().subscribe(new Listener(event3 -> {
            if (this.mode.is("Default")) {
                CollisionEvent.getInstance().setCancel(true);
            }
        }));
        addEvents(packetEvent, updateEvent, collisionEvent);
    }

    private boolean playerInsideBlock() {
        return (mc.field_1724 == null || mc.field_1687 == null || mc.field_1687.method_22347(class_2338.method_49638(mc.field_1724.method_19538()))) ? false : true;
    }
}
