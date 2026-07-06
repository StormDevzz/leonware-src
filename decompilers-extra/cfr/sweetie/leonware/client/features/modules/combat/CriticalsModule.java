/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1294
 *  net.minecraft.class_1297
 *  net.minecraft.class_1511
 *  net.minecraft.class_2246
 *  net.minecraft.class_2338
 *  net.minecraft.class_238
 *  net.minecraft.class_2596
 *  net.minecraft.class_2824
 *  net.minecraft.class_2828$class_2829
 *  net.minecraft.class_2828$class_2830
 */
package sweetie.leonware.client.features.modules.combat;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import lombok.Generated;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1511;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_2596;
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
import sweetie.leonware.client.features.modules.combat.AuraModule;

@ModuleRegister(name="Criticals", category=Category.COMBAT)
public class CriticalsModule
extends Module {
    private static final CriticalsModule instance = new CriticalsModule();
    public final ModeSetting mode = new ModeSetting("\u0420\u0435\u0436\u0438\u043c").values("Packet", "Strict", "Grim", "GrimV2", "GrimCC", "LowHop", "Jump", "Matrix", "Matrix2", "DeadCode", "UpdatedNCP", "Reallyworld", "WebPacket", "MetaHvH", "LowFly", "YPacket", "PacketRW").value("Packet");
    private final BooleanSetting sprinting = new BooleanSetting("\u0422\u043e\u043b\u044c\u043a\u043e \u043f\u0440\u0438 \u0441\u043f\u0440\u0438\u043d\u0442\u0435").value(false);
    private final BooleanSetting ignoreCrystals = new BooleanSetting("\u0418\u0433\u043d\u043e\u0440\u0438\u0440\u043e\u0432\u0430\u0442\u044c \u043a\u0440\u0438\u0441\u0442\u0430\u043b\u043b\u044b").value(true);
    private final SliderSetting delay = new SliderSetting("\u0417\u0430\u0434\u0435\u0440\u0436\u043a\u0430").value(Float.valueOf(0.0f)).range(0.0f, 1000.0f).step(10.0f);
    private final BooleanSetting autoSneak = new BooleanSetting("Auto Sneak").value(false).setVisible(() -> this.mode.is("YPacket"));
    private final SliderSetting sneakDelay = new SliderSetting("Sneak Delay").value(Float.valueOf(150.0f)).range(0.0f, 1000.0f).step(50.0f).setVisible(() -> this.mode.is("YPacket") && (Boolean)this.autoSneak.getValue() != false);
    private final TimerUtil timer = new TimerUtil();
    private int webCounter = 0;
    private int groundCounter = 0;
    private final Random random = new Random();

    public CriticalsModule() {
        this.addSettings(this.mode, this.sprinting, this.ignoreCrystals, this.delay, this.autoSneak, this.sneakDelay);
    }

    public void triggerCriticals(class_1297 target) {
        boolean grimCond;
        if (!this.isEnabled() || CriticalsModule.mc.field_1724 == null || target == null) {
            return;
        }
        if (((Boolean)this.ignoreCrystals.getValue()).booleanValue() && target instanceof class_1511) {
            return;
        }
        if (((Boolean)this.sprinting.getValue()).booleanValue() && !CriticalsModule.mc.field_1724.method_5624()) {
            return;
        }
        if (!this.timer.finished(((Float)this.delay.getValue()).longValue())) {
            return;
        }
        if (this.mode.is("PacketRW")) {
            boolean inWeb = PlayerUtil.isInWeb();
            boolean slowFalling = CriticalsModule.mc.field_1724.method_6059(class_1294.field_5906);
            if (inWeb) {
                this.sendOffset(0.003);
                this.sendOffset(0.0);
            } else if (slowFalling && CriticalsModule.mc.field_1724.method_18798().field_1351 < 0.0 && CriticalsModule.mc.field_1724.field_6017 > 0.0f) {
                this.sendOffset(0.003);
                this.sendOffset(0.0);
            }
            this.timer.reset();
            return;
        }
        if (this.mode.is("Reallyworld")) {
            if (CriticalsModule.mc.field_1724.field_6017 == 0.0f && !CriticalsModule.mc.field_1724.method_24828()) {
                CriticalsModule.mc.field_1724.field_6017 = 0.001f;
                CriticalsModule.mc.field_1724.field_3944.method_52787((class_2596)new class_2828.class_2830(CriticalsModule.mc.field_1724.method_23317(), CriticalsModule.mc.field_1724.method_23318() - 9.99999999E-5, CriticalsModule.mc.field_1724.method_23321(), CriticalsModule.mc.field_1724.method_36454(), CriticalsModule.mc.field_1724.method_36455(), false, CriticalsModule.mc.field_1724.field_5976));
            }
            this.timer.reset();
            return;
        }
        boolean bl = grimCond = this.mode.is("Grim") && !CriticalsModule.mc.field_1724.method_5771() && !CriticalsModule.mc.field_1724.method_5869();
        if (CriticalsModule.mc.field_1724.method_24828() || CriticalsModule.mc.field_1724.method_31549().field_7479 || grimCond) {
            switch ((String)this.mode.getValue()) {
                case "Packet": {
                    this.sendOffset(0.05);
                    this.sendOffset(0.0);
                    this.sendOffset(0.03);
                    this.sendOffset(0.0);
                    break;
                }
                case "Strict": {
                    this.sendOffset(0.11);
                    this.sendOffset(0.1100013579);
                    this.sendOffset(1.3579E-6);
                    break;
                }
                case "Matrix": {
                    this.sendOffset(0.08);
                    this.sendOffset(0.021);
                    break;
                }
                case "Matrix2": {
                    this.sendOffset(0.11);
                    this.sendOffset(0.1100013579);
                    this.sendOffset(1.3579E-6);
                    CriticalsModule.mc.field_1724.field_6017 = 1337.0f;
                    break;
                }
                case "DeadCode": {
                    mc.method_1562().method_52787((class_2596)new class_2828.class_2829(CriticalsModule.mc.field_1724.method_23317(), CriticalsModule.mc.field_1724.method_23318() + 0.0625, CriticalsModule.mc.field_1724.method_23321(), true, CriticalsModule.mc.field_1724.field_5976));
                    this.sendOffset(0.0);
                    break;
                }
                case "UpdatedNCP": {
                    this.sendOffset(2.71875E-7);
                    this.sendOffset(0.0);
                    break;
                }
                case "Jump": {
                    if (!CriticalsModule.mc.field_1724.method_24828()) break;
                    CriticalsModule.mc.field_1724.method_6043();
                    break;
                }
                case "LowHop": {
                    CriticalsModule.mc.field_1724.method_18800(CriticalsModule.mc.field_1724.method_18798().field_1352, 0.3425, CriticalsModule.mc.field_1724.method_18798().field_1350);
                    CriticalsModule.mc.field_1724.field_6017 = 0.1f;
                    CriticalsModule.mc.field_1724.method_24830(false);
                    break;
                }
                case "Grim": 
                case "GrimV2": {
                    this.sendFull(this.mode.is("GrimV2") ? 1.0E-4 : 0.0);
                    if (!this.mode.is("Grim")) break;
                    this.sendFull(0.0625);
                    this.sendFull(0.045);
                    break;
                }
                case "GrimCC": {
                    this.sendFull(0.0);
                    this.sendFull(0.0625);
                    this.sendFull(0.0625013579);
                    this.sendFull(1.3579E-6);
                    break;
                }
                case "WebPacket": {
                    if (!this.isInWeb()) break;
                    this.webCounter = (this.webCounter + 1) % 2;
                    if (this.webCounter != 1) break;
                    this.sendOffset(-1.0E-6);
                    break;
                }
                case "MetaHvH": {
                    break;
                }
                case "LowFly": {
                    this.sendOffset(1.0E-9);
                    this.sendOffset(0.0);
                    break;
                }
                case "YPacket": {
                    this.sendOffset(0.08);
                    this.sendOffset(0.021);
                    if (!((Boolean)this.autoSneak.getValue()).booleanValue()) break;
                    CriticalsModule.mc.field_1690.field_1832.method_23481(true);
                    new Timer().schedule(new TimerTask(this){

                        @Override
                        public void run() {
                            QuickImports.mc.execute(() -> QuickImports.mc.field_1690.field_1832.method_23481(false));
                        }
                    }, ((Float)this.sneakDelay.getValue()).longValue());
                }
            }
            if (!this.mode.is("Matrix2")) {
                CriticalsModule.mc.field_1724.method_24830(false);
            }
            this.timer.reset();
        }
    }

    @Override
    public void onEvent() {
        EventListener attackEvent = AttackEvent.getInstance().subscribe(new Listener<AttackEvent.AttackEventData>(event -> this.triggerCriticals(event.entity())));
        EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener<PacketEvent.PacketEventData>(event -> {
            if (CriticalsModule.mc.field_1724 == null) {
                return;
            }
            if (this.mode.is("Matrix2") && event.packet() instanceof class_2824 && CriticalsModule.mc.field_1724.field_6017 == 1337.0f) {
                CriticalsModule.mc.field_1724.field_6017 = 0.0f;
            }
        }));
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (!this.mode.is("MetaHvH") || !this.isEnabled() || CriticalsModule.mc.field_1724 == null || CriticalsModule.mc.field_1687 == null) {
                return;
            }
            double speed = Math.sqrt(CriticalsModule.mc.field_1724.method_18798().field_1352 * CriticalsModule.mc.field_1724.method_18798().field_1352 + CriticalsModule.mc.field_1724.method_18798().field_1350 * CriticalsModule.mc.field_1724.method_18798().field_1350);
            boolean isMoving = speed > 0.01;
            this.groundCounter = isMoving ? 0 : this.groundCounter + 1;
            AuraModule aura = AuraModule.getInstance();
            if (aura == null || !aura.isEnabled()) {
                return;
            }
            if (aura.target == null) {
                return;
            }
            if (!isMoving && this.groundCounter >= 20) {
                return;
            }
            if (CriticalsModule.mc.field_1724.method_7261(1.0f) >= 0.87f) {
                boolean canCrit = true;
                if (isMoving) {
                    double angle = Math.atan2(CriticalsModule.mc.field_1724.method_18798().field_1350, CriticalsModule.mc.field_1724.method_18798().field_1352) - Math.toRadians(90.0);
                    double offsetX = -Math.sin(angle) * 0.02;
                    double offsetZ = Math.cos(angle) * 0.02;
                    class_238 box = CriticalsModule.mc.field_1724.method_5829().method_989(offsetX, -0.5, offsetZ);
                    canCrit = CriticalsModule.mc.field_1687.method_20812((class_1297)CriticalsModule.mc.field_1724, box).iterator().hasNext();
                }
                if (canCrit) {
                    class_238 box;
                    boolean hasColl;
                    if (CriticalsModule.mc.field_1724.method_24828()) {
                        CriticalsModule.mc.field_1724.method_18800(CriticalsModule.mc.field_1724.method_18798().field_1352, 0.003, CriticalsModule.mc.field_1724.method_18798().field_1350);
                        CriticalsModule.mc.field_1724.method_24830(false);
                    } else if (CriticalsModule.mc.field_1724.method_18798().field_1351 < 0.0 && (double)CriticalsModule.mc.field_1724.field_6017 < 1.0E-6 && (hasColl = CriticalsModule.mc.field_1687.method_20812((class_1297)CriticalsModule.mc.field_1724, box = CriticalsModule.mc.field_1724.method_5829().method_989(0.0, -0.21, 0.0)).iterator().hasNext())) {
                        CriticalsModule.mc.field_1724.method_18800(CriticalsModule.mc.field_1724.method_18798().field_1352, -0.003, CriticalsModule.mc.field_1724.method_18798().field_1350);
                    }
                }
            }
        }));
        this.addEvents(attackEvent, packetEvent, updateEvent);
    }

    private boolean isInWeb() {
        if (CriticalsModule.mc.field_1724 == null || CriticalsModule.mc.field_1687 == null) {
            return false;
        }
        for (double x = -0.3; x <= 0.3; x += 0.3) {
            for (double z = -0.3; z <= 0.3; z += 0.3) {
                for (double y = (double)CriticalsModule.mc.field_1724.method_18381(CriticalsModule.mc.field_1724.method_18376()); y >= 0.0; y -= 0.1) {
                    class_2338 pos = class_2338.method_49637((double)(CriticalsModule.mc.field_1724.method_23317() + x), (double)(CriticalsModule.mc.field_1724.method_23318() + y), (double)(CriticalsModule.mc.field_1724.method_23321() + z));
                    if (!CriticalsModule.mc.field_1687.method_8320(pos).method_27852(class_2246.field_10343)) continue;
                    return true;
                }
                class_2338 pos = class_2338.method_49637((double)(CriticalsModule.mc.field_1724.method_23317() + x), (double)CriticalsModule.mc.field_1724.method_23318(), (double)(CriticalsModule.mc.field_1724.method_23321() + z));
                if (!CriticalsModule.mc.field_1687.method_8320(pos).method_27852(class_2246.field_10343)) continue;
                return true;
            }
        }
        class_2338 headPos = class_2338.method_49637((double)CriticalsModule.mc.field_1724.method_23317(), (double)(CriticalsModule.mc.field_1724.method_23318() + (double)CriticalsModule.mc.field_1724.method_18381(CriticalsModule.mc.field_1724.method_18376()) + 1.65), (double)CriticalsModule.mc.field_1724.method_23321());
        return CriticalsModule.mc.field_1687.method_8320(headPos).method_27852(class_2246.field_10343);
    }

    private void sendOffset(double yOffset) {
        mc.method_1562().method_52787((class_2596)new class_2828.class_2829(CriticalsModule.mc.field_1724.method_23317(), CriticalsModule.mc.field_1724.method_23318() + yOffset, CriticalsModule.mc.field_1724.method_23321(), false, CriticalsModule.mc.field_1724.field_5976));
    }

    private void sendFull(double yOffset) {
        mc.method_1562().method_52787((class_2596)new class_2828.class_2830(CriticalsModule.mc.field_1724.method_23317(), CriticalsModule.mc.field_1724.method_23318() + yOffset, CriticalsModule.mc.field_1724.method_23321(), CriticalsModule.mc.field_1724.method_36454(), CriticalsModule.mc.field_1724.method_36455(), false, CriticalsModule.mc.field_1724.field_5976));
    }

    @Generated
    public static CriticalsModule getInstance() {
        return instance;
    }
}

