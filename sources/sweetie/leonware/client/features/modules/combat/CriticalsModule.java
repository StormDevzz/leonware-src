package sweetie.leonware.client.features.modules.combat;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import lombok.Generated;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1511;
import net.minecraft.class_238;
import net.minecraft.class_2824;
import net.minecraft.class_2828;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.event.events.player.world.AttackEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.system.interfaces.QuickImports;
import sweetie.leonware.api.utils.math.TimerUtil;
import sweetie.leonware.api.utils.player.PlayerUtil;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/combat/CriticalsModule.class */
@ModuleRegister(name = "Criticals", category = Category.COMBAT)
public class CriticalsModule extends Module {
    private static final CriticalsModule instance = new CriticalsModule();
    public final ModeSetting mode = new ModeSetting("Режим").values("Packet", "Strict", "Grim", "GrimV2", "GrimCC", "LowHop", "Jump", "Matrix", "Matrix2", "DeadCode", "UpdatedNCP", "Reallyworld", "WebPacket", "MetaHvH", "LowFly", "YPacket", "PacketRW").value("Packet");
    private final BooleanSetting sprinting = new BooleanSetting("Только при спринте").value((Boolean) false);
    private final BooleanSetting ignoreCrystals = new BooleanSetting("Игнорировать кристаллы").value((Boolean) true);
    private final SliderSetting delay = new SliderSetting("Задержка").value(Float.valueOf(0.0f)).range(0.0f, 1000.0f).step(10.0f);
    private final BooleanSetting autoSneak = new BooleanSetting("Auto Sneak").value((Boolean) false).setVisible(() -> {
        return Boolean.valueOf(this.mode.is("YPacket"));
    });
    private final SliderSetting sneakDelay = new SliderSetting("Sneak Delay").value(Float.valueOf(150.0f)).range(0.0f, 1000.0f).step(50.0f).setVisible(() -> {
        return Boolean.valueOf(this.mode.is("YPacket") && this.autoSneak.getValue().booleanValue());
    });
    private final TimerUtil timer = new TimerUtil();
    private int webCounter = 0;
    private int groundCounter = 0;
    private final Random random = new Random();

    @Generated
    public static CriticalsModule getInstance() {
        return instance;
    }

    /* JADX WARN: Type inference failed for: r1v13, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    /* JADX WARN: Type inference failed for: r1v18, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    public CriticalsModule() {
        addSettings(this.mode, this.sprinting, this.ignoreCrystals, this.delay, this.autoSneak, this.sneakDelay);
    }

    public void triggerCriticals(class_1297 target) {
        if (!isEnabled() || mc.field_1724 == null || target == null) {
            return;
        }
        if (this.ignoreCrystals.getValue().booleanValue() && (target instanceof class_1511)) {
            return;
        }
        if ((!this.sprinting.getValue().booleanValue() || mc.field_1724.method_5624()) && this.timer.finished(this.delay.getValue().longValue())) {
            if (this.mode.is("PacketRW")) {
                boolean inWeb = PlayerUtil.isInWeb();
                boolean slowFalling = mc.field_1724.method_6059(class_1294.field_5906);
                if (inWeb) {
                    sendOffset(0.003d);
                    sendOffset(0.0d);
                } else if (slowFalling && mc.field_1724.method_18798().field_1351 < 0.0d && mc.field_1724.field_6017 > 0.0f) {
                    sendOffset(0.003d);
                    sendOffset(0.0d);
                }
                this.timer.reset();
                return;
            }
            if (this.mode.is("Reallyworld")) {
                if (mc.field_1724.field_6017 == 0.0f && !mc.field_1724.method_24828()) {
                    mc.field_1724.field_6017 = 0.001f;
                    mc.field_1724.field_3944.method_52787(new class_2828.class_2830(mc.field_1724.method_23317(), mc.field_1724.method_23318() - 9.99999999E-5d, mc.field_1724.method_23321(), mc.field_1724.method_36454(), mc.field_1724.method_36455(), false, mc.field_1724.field_5976));
                }
                this.timer.reset();
                return;
            }
            boolean grimCond = (!this.mode.is("Grim") || mc.field_1724.method_5771() || mc.field_1724.method_5869()) ? false : true;
            if (mc.field_1724.method_24828() || mc.field_1724.method_31549().field_7479 || grimCond) {
                switch (this.mode.getValue()) {
                    case "Packet":
                        sendOffset(0.05d);
                        sendOffset(0.0d);
                        sendOffset(0.03d);
                        sendOffset(0.0d);
                        break;
                    case "Strict":
                        sendOffset(0.11d);
                        sendOffset(0.1100013579d);
                        sendOffset(1.3579E-6d);
                        break;
                    case "Matrix":
                        sendOffset(0.08d);
                        sendOffset(0.021d);
                        break;
                    case "Matrix2":
                        sendOffset(0.11d);
                        sendOffset(0.1100013579d);
                        sendOffset(1.3579E-6d);
                        mc.field_1724.field_6017 = 1337.0f;
                        break;
                    case "DeadCode":
                        mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 0.0625d, mc.field_1724.method_23321(), true, mc.field_1724.field_5976));
                        sendOffset(0.0d);
                        break;
                    case "UpdatedNCP":
                        sendOffset(2.71875E-7d);
                        sendOffset(0.0d);
                        break;
                    case "Jump":
                        if (mc.field_1724.method_24828()) {
                            mc.field_1724.method_6043();
                            break;
                        }
                        break;
                    case "LowHop":
                        mc.field_1724.method_18800(mc.field_1724.method_18798().field_1352, 0.3425d, mc.field_1724.method_18798().field_1350);
                        mc.field_1724.field_6017 = 0.1f;
                        mc.field_1724.method_24830(false);
                        break;
                    case "Grim":
                    case "GrimV2":
                        sendFull(this.mode.is("GrimV2") ? 1.0E-4d : 0.0d);
                        if (this.mode.is("Grim")) {
                            sendFull(0.0625d);
                            sendFull(0.045d);
                            break;
                        }
                        break;
                    case "GrimCC":
                        sendFull(0.0d);
                        sendFull(0.0625d);
                        sendFull(0.0625013579d);
                        sendFull(1.3579E-6d);
                        break;
                    case "WebPacket":
                        if (isInWeb()) {
                            this.webCounter = (this.webCounter + 1) % 2;
                            if (this.webCounter == 1) {
                                sendOffset(-1.0E-6d);
                            }
                            break;
                        }
                        break;
                    case "LowFly":
                        sendOffset(1.0E-9d);
                        sendOffset(0.0d);
                        break;
                    case "YPacket":
                        sendOffset(0.08d);
                        sendOffset(0.021d);
                        if (this.autoSneak.getValue().booleanValue()) {
                            mc.field_1690.field_1832.method_23481(true);
                            new Timer().schedule(new TimerTask(this) { // from class: sweetie.leonware.client.features.modules.combat.CriticalsModule.1
                                @Override // java.util.TimerTask, java.lang.Runnable
                                public void run() {
                                    QuickImports.mc.execute(() -> {
                                        QuickImports.mc.field_1690.field_1832.method_23481(false);
                                    });
                                }
                            }, this.sneakDelay.getValue().longValue());
                            break;
                        }
                        break;
                }
                if (!this.mode.is("Matrix2")) {
                    mc.field_1724.method_24830(false);
                }
                this.timer.reset();
            }
        }
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener attackEvent = AttackEvent.getInstance().subscribe(new Listener(event -> {
            triggerCriticals(event.entity());
        }));
        EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener(event2 -> {
            if (mc.field_1724 != null && this.mode.is("Matrix2") && (event2.packet() instanceof class_2824) && mc.field_1724.field_6017 == 1337.0f) {
                mc.field_1724.field_6017 = 0.0f;
            }
        }));
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener(event3 -> {
            if (!this.mode.is("MetaHvH") || !isEnabled() || mc.field_1724 == null || mc.field_1687 == null) {
                return;
            }
            double speed = Math.sqrt((mc.field_1724.method_18798().field_1352 * mc.field_1724.method_18798().field_1352) + (mc.field_1724.method_18798().field_1350 * mc.field_1724.method_18798().field_1350));
            boolean isMoving = speed > 0.01d;
            this.groundCounter = isMoving ? 0 : this.groundCounter + 1;
            AuraModule aura = AuraModule.getInstance();
            if (aura == null || !aura.isEnabled() || aura.target == null) {
                return;
            }
            if ((isMoving || this.groundCounter < 20) && mc.field_1724.method_7261(1.0f) >= 0.87f) {
                boolean canCrit = true;
                if (isMoving) {
                    double angle = Math.atan2(mc.field_1724.method_18798().field_1350, mc.field_1724.method_18798().field_1352) - Math.toRadians(90.0d);
                    double offsetX = (-Math.sin(angle)) * 0.02d;
                    double offsetZ = Math.cos(angle) * 0.02d;
                    class_238 box = mc.field_1724.method_5829().method_989(offsetX, -0.5d, offsetZ);
                    canCrit = mc.field_1687.method_20812(mc.field_1724, box).iterator().hasNext();
                }
                if (canCrit) {
                    if (mc.field_1724.method_24828()) {
                        mc.field_1724.method_18800(mc.field_1724.method_18798().field_1352, 0.003d, mc.field_1724.method_18798().field_1350);
                        mc.field_1724.method_24830(false);
                    } else if (mc.field_1724.method_18798().field_1351 < 0.0d && mc.field_1724.field_6017 < 1.0E-6d) {
                        class_238 box2 = mc.field_1724.method_5829().method_989(0.0d, -0.21d, 0.0d);
                        boolean hasColl = mc.field_1687.method_20812(mc.field_1724, box2).iterator().hasNext();
                        if (hasColl) {
                            mc.field_1724.method_18800(mc.field_1724.method_18798().field_1352, -0.003d, mc.field_1724.method_18798().field_1350);
                        }
                    }
                }
            }
        }));
        addEvents(attackEvent, packetEvent, updateEvent);
    }

    /* JADX WARN: Code restructure failed: missing block: B:22:0x0090, code lost:
    
        r0 = net.minecraft.class_2338.method_49637(sweetie.leonware.client.features.modules.combat.CriticalsModule.mc.field_1724.method_23317() + r10, sweetie.leonware.client.features.modules.combat.CriticalsModule.mc.field_1724.method_23318(), sweetie.leonware.client.features.modules.combat.CriticalsModule.mc.field_1724.method_23321() + r12);
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x00c5, code lost:
    
        if (sweetie.leonware.client.features.modules.combat.CriticalsModule.mc.field_1687.method_8320(r0).method_27852(net.minecraft.class_2246.field_10343) == false) goto L26;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x00c8, code lost:
    
        return true;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private boolean isInWeb() {
        /*
            Method dump skipped, instruction units count: 292
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: sweetie.leonware.client.features.modules.combat.CriticalsModule.isInWeb():boolean");
    }

    private void sendOffset(double yOffset) {
        mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + yOffset, mc.field_1724.method_23321(), false, mc.field_1724.field_5976));
    }

    private void sendFull(double yOffset) {
        mc.method_1562().method_52787(new class_2828.class_2830(mc.field_1724.method_23317(), mc.field_1724.method_23318() + yOffset, mc.field_1724.method_23321(), mc.field_1724.method_36454(), mc.field_1724.method_36455(), false, mc.field_1724.field_5976));
    }
}
