// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.movement;

import lombok.Generated;
import net.minecraft.class_2680;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.events.player.move.CollisionEvent;
import net.minecraft.class_2350;
import net.minecraft.class_2846;
import net.minecraft.class_238;
import net.minecraft.class_2374;
import net.minecraft.class_2338;
import net.minecraft.class_3532;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.PacketEvent;
import java.util.Iterator;
import net.minecraft.class_2828;
import sweetie.leonware.api.module.setting.Setting;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.class_2596;
import java.util.List;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "No Clip", category = Category.MOVEMENT)
public class NoClipModule extends Module
{
    private static final NoClipModule instance;
    private final ModeSetting mode;
    private final BooleanSetting strict;
    private final List<class_2596<?>> packets;
    private boolean semiPacketSent;
    private boolean skipReleaseOnDisable;
    
    public NoClipModule() {
        this.mode = new ModeSetting("\u0420\u0435\u0436\u0438\u043c").values("Default", "ForceMine", "Pearl Phase").value("Default");
        this.strict = new BooleanSetting("Strict").value(false).setVisible(() -> this.mode.is("ForceMine"));
        this.packets = new CopyOnWriteArrayList<class_2596<?>>();
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
                final double x = NoClipModule.mc.field_1724.method_23317();
                final double y = NoClipModule.mc.field_1724.method_23318();
                final double z = NoClipModule.mc.field_1724.method_23321();
                final float yaw = NoClipModule.mc.field_1724.method_36454();
                final float pitch = NoClipModule.mc.field_1724.method_36455();
                NoClipModule.mc.field_1724.field_3944.method_52787((class_2596)new class_2828.class_2830(x - 5000.0, y, z - 5000.0, yaw, pitch, false, false));
                NoClipModule.mc.field_1724.field_3944.method_52787((class_2596)new class_2828.class_2830(x, y, z, yaw, pitch, NoClipModule.mc.field_1724.method_24828(), false));
            }
            if (NoClipModule.mc.field_1724 != null && NoClipModule.mc.field_1724.field_3944 != null && !this.packets.isEmpty()) {
                for (final class_2596<?> packet : this.packets) {
                    this.sendSilentPacket(packet);
                }
                this.packets.clear();
            }
        }
    }
    
    @Override
    public void onEvent() {
        final EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener<PacketEvent.PacketEventData>(event -> {
            if (!this.mode.is("Default")) {
                return;
            }
            else {
                if (event.isSend()) {
                    final class_2596<?> p = event.packet();
                    if (p instanceof class_2828) {
                        this.packets.add(p);
                        PacketEvent.getInstance().setCancel(true);
                    }
                }
                return;
            }
        }));
        final EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (NoClipModule.mc.field_1724 == null || NoClipModule.mc.field_1687 == null) {
                return;
            }
            else {
                if (this.mode.is("Default")) {
                    NoClipModule.mc.field_1724.method_18800(NoClipModule.mc.field_1724.method_18798().field_1352, 0.0, NoClipModule.mc.field_1724.method_18798().field_1350);
                    final class_238 box = NoClipModule.mc.field_1724.method_5829().method_1014(0.001);
                    final int minX = class_3532.method_15357(box.field_1323);
                    final int minY = class_3532.method_15357(box.field_1322);
                    final int minZ = class_3532.method_15357(box.field_1321);
                    final int maxX = class_3532.method_15357(box.field_1320);
                    final int maxY = class_3532.method_15357(box.field_1325);
                    final int maxZ = class_3532.method_15357(box.field_1324);
                    long totalStates = 0L;
                    long solidStates = 0L;
                    for (int x = minX; x <= maxX; ++x) {
                        for (int y = minY; y <= maxY; ++y) {
                            for (int z = minZ; z <= maxZ; ++z) {
                                final class_2338 pos = new class_2338(x, y, z);
                                final class_2680 state = NoClipModule.mc.field_1687.method_8320(pos);
                                ++totalStates;
                                if (state.method_51367()) {
                                    ++solidStates;
                                }
                            }
                        }
                    }
                    final boolean noSolidInAABB = solidStates == 0L;
                    final boolean semiInsideBlock = solidStates > 0L && solidStates < totalStates;
                    if (!this.semiPacketSent && semiInsideBlock) {
                        final double x2 = NoClipModule.mc.field_1724.method_23317();
                        final double y2 = NoClipModule.mc.field_1724.method_23318();
                        final double z2 = NoClipModule.mc.field_1724.method_23321();
                        final float yaw = NoClipModule.mc.field_1724.method_36454();
                        final float pitch = NoClipModule.mc.field_1724.method_36455();
                        final boolean onGround = NoClipModule.mc.field_1724.method_24828();
                        for (int i = 0; i < 2; ++i) {
                            NoClipModule.mc.field_1724.field_3944.method_52787((class_2596)new class_2828.class_2830(x2, y2, z2, yaw, pitch, onGround, false));
                        }
                        this.semiPacketSent = true;
                    }
                    else if (this.semiPacketSent && noSolidInAABB) {
                        this.skipReleaseOnDisable = true;
                        this.toggle();
                    }
                }
                else if (this.mode.is("ForceMine")) {
                    if (NoClipModule.mc.field_1724.field_5976 || this.playerInsideBlock()) {
                        if (!NoClipModule.mc.field_1724.method_5869() && !NoClipModule.mc.field_1724.method_5771()) {
                            for (int x3 = -2; x3 < 2; ++x3) {
                                for (int y3 = -1; y3 < 3; ++y3) {
                                    for (int z3 = -2; z3 < 2; ++z3) {
                                        if (((x3 != 0 || y3 != 0 || z3 != 0) && (x3 != 0 || y3 != 1 || z3 != 0)) || NoClipModule.mc.field_1690.field_1832.method_1434()) {
                                            final class_2338 bp = class_2338.method_49638((class_2374)NoClipModule.mc.field_1724.method_19538()).method_10069(x3, y3, z3);
                                            if (NoClipModule.mc.field_1724.method_5829().method_994(new class_238(bp)) && !NoClipModule.mc.field_1687.method_22347(bp)) {
                                                NoClipModule.mc.field_1724.field_3944.method_52787((class_2596)new class_2846(class_2846.class_2847.field_12973, bp, class_2350.field_11036));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                return;
            }
        }));
        final EventListener collisionEvent = CollisionEvent.getInstance().subscribe(new Listener<CollisionEvent>(event -> {
            if (this.mode.is("Default")) {
                CollisionEvent.getInstance().setCancel(true);
            }
            return;
        }));
        this.addEvents(packetEvent, updateEvent, collisionEvent);
    }
    
    private boolean playerInsideBlock() {
        return NoClipModule.mc.field_1724 != null && NoClipModule.mc.field_1687 != null && !NoClipModule.mc.field_1687.method_22347(class_2338.method_49638((class_2374)NoClipModule.mc.field_1724.method_19538()));
    }
    
    @Generated
    public static NoClipModule getInstance() {
        return NoClipModule.instance;
    }
    
    @Generated
    public ModeSetting getMode() {
        return this.mode;
    }
    
    @Generated
    public BooleanSetting getStrict() {
        return this.strict;
    }
    
    static {
        instance = new NoClipModule();
    }
}
