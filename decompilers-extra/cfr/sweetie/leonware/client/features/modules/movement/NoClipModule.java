/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_2338
 *  net.minecraft.class_2350
 *  net.minecraft.class_2374
 *  net.minecraft.class_238
 *  net.minecraft.class_2596
 *  net.minecraft.class_2680
 *  net.minecraft.class_2828
 *  net.minecraft.class_2828$class_2830
 *  net.minecraft.class_2846
 *  net.minecraft.class_2846$class_2847
 *  net.minecraft.class_3532
 */
package sweetie.leonware.client.features.modules.movement;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.Generated;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2374;
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

@ModuleRegister(name="No Clip", category=Category.MOVEMENT)
public class NoClipModule
extends Module {
    private static final NoClipModule instance = new NoClipModule();
    private final ModeSetting mode = new ModeSetting("\u0420\u0435\u0436\u0438\u043c").values("Default", "ForceMine", "Pearl Phase").value("Default");
    private final BooleanSetting strict = new BooleanSetting("Strict").value(false).setVisible(() -> this.mode.is("ForceMine"));
    private final List<class_2596<?>> packets = new CopyOnWriteArrayList();
    private boolean semiPacketSent;
    private boolean skipReleaseOnDisable;

    public NoClipModule() {
        this.addSettings(this.mode, this.strict);
    }

    @Override
    public void onEnable() {
        this.packets.clear();
        this.semiPacketSent = false;
        this.skipReleaseOnDisable = false;
    }

    @Override
    public void onDisable() {
        if (this.mode.is("Default")) {
            if (!this.skipReleaseOnDisable && this.semiPacketSent) {
                double x = NoClipModule.mc.field_1724.method_23317();
                double y = NoClipModule.mc.field_1724.method_23318();
                double z = NoClipModule.mc.field_1724.method_23321();
                float yaw = NoClipModule.mc.field_1724.method_36454();
                float pitch = NoClipModule.mc.field_1724.method_36455();
                NoClipModule.mc.field_1724.field_3944.method_52787((class_2596)new class_2828.class_2830(x - 5000.0, y, z - 5000.0, yaw, pitch, false, false));
                NoClipModule.mc.field_1724.field_3944.method_52787((class_2596)new class_2828.class_2830(x, y, z, yaw, pitch, NoClipModule.mc.field_1724.method_24828(), false));
            }
            if (NoClipModule.mc.field_1724 != null && NoClipModule.mc.field_1724.field_3944 != null && !this.packets.isEmpty()) {
                for (class_2596<?> packet : this.packets) {
                    this.sendSilentPacket(packet);
                }
                this.packets.clear();
            }
        }
    }

    @Override
    public void onEvent() {
        EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener<PacketEvent.PacketEventData>(event -> {
            class_2596<?> p;
            if (!this.mode.is("Default")) {
                return;
            }
            if (event.isSend() && (p = event.packet()) instanceof class_2828) {
                this.packets.add(p);
                PacketEvent.getInstance().setCancel(true);
            }
        }));
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (NoClipModule.mc.field_1724 == null || NoClipModule.mc.field_1687 == null) {
                return;
            }
            if (this.mode.is("Default")) {
                boolean semiInsideBlock;
                NoClipModule.mc.field_1724.method_18800(NoClipModule.mc.field_1724.method_18798().field_1352, 0.0, NoClipModule.mc.field_1724.method_18798().field_1350);
                class_238 box = NoClipModule.mc.field_1724.method_5829().method_1014(0.001);
                int minX = class_3532.method_15357((double)box.field_1323);
                int minY = class_3532.method_15357((double)box.field_1322);
                int minZ = class_3532.method_15357((double)box.field_1321);
                int maxX = class_3532.method_15357((double)box.field_1320);
                int maxY = class_3532.method_15357((double)box.field_1325);
                int maxZ = class_3532.method_15357((double)box.field_1324);
                long totalStates = 0L;
                long solidStates = 0L;
                for (int x = minX; x <= maxX; ++x) {
                    for (int y = minY; y <= maxY; ++y) {
                        for (int z = minZ; z <= maxZ; ++z) {
                            class_2338 pos = new class_2338(x, y, z);
                            class_2680 state = NoClipModule.mc.field_1687.method_8320(pos);
                            ++totalStates;
                            if (!state.method_51367()) continue;
                            ++solidStates;
                        }
                    }
                }
                boolean noSolidInAABB = solidStates == 0L;
                boolean bl = semiInsideBlock = solidStates > 0L && solidStates < totalStates;
                if (!this.semiPacketSent && semiInsideBlock) {
                    double x = NoClipModule.mc.field_1724.method_23317();
                    double y = NoClipModule.mc.field_1724.method_23318();
                    double z = NoClipModule.mc.field_1724.method_23321();
                    float yaw = NoClipModule.mc.field_1724.method_36454();
                    float pitch = NoClipModule.mc.field_1724.method_36455();
                    boolean onGround = NoClipModule.mc.field_1724.method_24828();
                    for (int i = 0; i < 2; ++i) {
                        NoClipModule.mc.field_1724.field_3944.method_52787((class_2596)new class_2828.class_2830(x, y, z, yaw, pitch, onGround, false));
                    }
                    this.semiPacketSent = true;
                    return;
                }
                if (this.semiPacketSent && noSolidInAABB) {
                    this.skipReleaseOnDisable = true;
                    this.toggle();
                }
            } else if (this.mode.is("ForceMine")) {
                if (!NoClipModule.mc.field_1724.field_5976 && !this.playerInsideBlock()) {
                    return;
                }
                if (NoClipModule.mc.field_1724.method_5869() || NoClipModule.mc.field_1724.method_5771()) {
                    return;
                }
                for (int x = -2; x < 2; ++x) {
                    for (int y = -1; y < 3; ++y) {
                        for (int z = -2; z < 2; ++z) {
                            if ((x == 0 && y == 0 && z == 0 || x == 0 && y == 1 && z == 0) && !NoClipModule.mc.field_1690.field_1832.method_1434()) continue;
                            class_2338 bp = class_2338.method_49638((class_2374)NoClipModule.mc.field_1724.method_19538()).method_10069(x, y, z);
                            if (!NoClipModule.mc.field_1724.method_5829().method_994(new class_238(bp)) || NoClipModule.mc.field_1687.method_22347(bp)) continue;
                            NoClipModule.mc.field_1724.field_3944.method_52787((class_2596)new class_2846(class_2846.class_2847.field_12973, bp, class_2350.field_11036));
                        }
                    }
                }
            }
        }));
        EventListener collisionEvent = CollisionEvent.getInstance().subscribe(new Listener<CollisionEvent>(event -> {
            if (this.mode.is("Default")) {
                CollisionEvent.getInstance().setCancel(true);
            }
        }));
        this.addEvents(packetEvent, updateEvent, collisionEvent);
    }

    private boolean playerInsideBlock() {
        if (NoClipModule.mc.field_1724 == null || NoClipModule.mc.field_1687 == null) {
            return false;
        }
        return !NoClipModule.mc.field_1687.method_22347(class_2338.method_49638((class_2374)NoClipModule.mc.field_1724.method_19538()));
    }

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
}

