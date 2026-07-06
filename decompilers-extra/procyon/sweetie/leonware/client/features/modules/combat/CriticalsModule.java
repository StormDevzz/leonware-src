// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.combat;

import lombok.Generated;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import net.minecraft.class_2824;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.world.AttackEvent;
import sweetie.leonware.api.system.interfaces.QuickImports;
import java.util.TimerTask;
import java.util.Timer;
import net.minecraft.class_2596;
import net.minecraft.class_2828;
import net.minecraft.class_1294;
import sweetie.leonware.api.utils.player.PlayerUtil;
import net.minecraft.class_1511;
import net.minecraft.class_1297;
import sweetie.leonware.api.module.setting.Setting;
import java.util.Random;
import sweetie.leonware.api.utils.math.TimerUtil;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Criticals", category = Category.COMBAT)
public class CriticalsModule extends Module
{
    private static final CriticalsModule instance;
    public final ModeSetting mode;
    private final BooleanSetting sprinting;
    private final BooleanSetting ignoreCrystals;
    private final SliderSetting delay;
    private final BooleanSetting autoSneak;
    private final SliderSetting sneakDelay;
    private final TimerUtil timer;
    private int webCounter;
    private int groundCounter;
    private final Random random;
    
    public CriticalsModule() {
        this.mode = new ModeSetting("\u0420\u0435\u0436\u0438\u043c").values("Packet", "Strict", "Grim", "GrimV2", "GrimCC", "LowHop", "Jump", "Matrix", "Matrix2", "DeadCode", "UpdatedNCP", "Reallyworld", "WebPacket", "MetaHvH", "LowFly", "YPacket", "PacketRW").value("Packet");
        this.sprinting = new BooleanSetting("\u0422\u043e\u043b\u044c\u043a\u043e \u043f\u0440\u0438 \u0441\u043f\u0440\u0438\u043d\u0442\u0435").value(false);
        this.ignoreCrystals = new BooleanSetting("\u0418\u0433\u043d\u043e\u0440\u0438\u0440\u043e\u0432\u0430\u0442\u044c \u043a\u0440\u0438\u0441\u0442\u0430\u043b\u043b\u044b").value(true);
        this.delay = new SliderSetting("\u0417\u0430\u0434\u0435\u0440\u0436\u043a\u0430").value(0.0f).range(0.0f, 1000.0f).step(10.0f);
        this.autoSneak = new BooleanSetting("Auto Sneak").value(false).setVisible(() -> this.mode.is("YPacket"));
        this.sneakDelay = new SliderSetting("Sneak Delay").value(150.0f).range(0.0f, 1000.0f).step(50.0f).setVisible(() -> this.mode.is("YPacket") && this.autoSneak.getValue());
        this.timer = new TimerUtil();
        this.webCounter = 0;
        this.groundCounter = 0;
        this.random = new Random();
        this.addSettings(this.mode, this.sprinting, this.ignoreCrystals, this.delay, this.autoSneak, this.sneakDelay);
    }
    
    public void triggerCriticals(final class_1297 target) {
        if (!this.isEnabled() || CriticalsModule.mc.field_1724 == null || target == null) {
            return;
        }
        if (this.ignoreCrystals.getValue() && target instanceof class_1511) {
            return;
        }
        if (this.sprinting.getValue() && !CriticalsModule.mc.field_1724.method_5624()) {
            return;
        }
        if (!this.timer.finished(this.delay.getValue().longValue())) {
            return;
        }
        if (this.mode.is("PacketRW")) {
            final boolean inWeb = PlayerUtil.isInWeb();
            final boolean slowFalling = CriticalsModule.mc.field_1724.method_6059(class_1294.field_5906);
            if (inWeb) {
                this.sendOffset(0.003);
                this.sendOffset(0.0);
            }
            else if (slowFalling && CriticalsModule.mc.field_1724.method_18798().field_1351 < 0.0 && CriticalsModule.mc.field_1724.field_6017 > 0.0f) {
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
        final boolean grimCond = this.mode.is("Grim") && !CriticalsModule.mc.field_1724.method_5771() && !CriticalsModule.mc.field_1724.method_5869();
        if (CriticalsModule.mc.field_1724.method_24828() || CriticalsModule.mc.field_1724.method_31549().field_7479 || grimCond) {
            final String s = this.mode.getValue();
            switch (s) {
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
                    CriticalsModule.mc.method_1562().method_52787((class_2596)new class_2828.class_2829(CriticalsModule.mc.field_1724.method_23317(), CriticalsModule.mc.field_1724.method_23318() + 0.0625, CriticalsModule.mc.field_1724.method_23321(), true, CriticalsModule.mc.field_1724.field_5976));
                    this.sendOffset(0.0);
                    break;
                }
                case "UpdatedNCP": {
                    this.sendOffset(2.71875E-7);
                    this.sendOffset(0.0);
                    break;
                }
                case "Jump": {
                    if (CriticalsModule.mc.field_1724.method_24828()) {
                        CriticalsModule.mc.field_1724.method_6043();
                        break;
                    }
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
                    if (this.mode.is("Grim")) {
                        this.sendFull(0.0625);
                        this.sendFull(0.045);
                        break;
                    }
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
                    if (!this.isInWeb()) {
                        break;
                    }
                    this.webCounter = (this.webCounter + 1) % 2;
                    if (this.webCounter == 1) {
                        this.sendOffset(-1.0E-6);
                        break;
                    }
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
                    if (this.autoSneak.getValue()) {
                        CriticalsModule.mc.field_1690.field_1832.method_23481(true);
                        new Timer().schedule(new TimerTask(this) {
                            @Override
                            public void run() {
                                QuickImports.mc.execute(() -> QuickImports.mc.field_1690.field_1832.method_23481(false));
                            }
                        }, this.sneakDelay.getValue().longValue());
                        break;
                    }
                    break;
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
        final EventListener attackEvent = AttackEvent.getInstance().subscribe(new Listener<AttackEvent.AttackEventData>(event -> this.triggerCriticals(event.entity())));
        final EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener<PacketEvent.PacketEventData>(event -> {
            if (CriticalsModule.mc.field_1724 == null) {
                return;
            }
            else {
                if (this.mode.is("Matrix2") && event.packet() instanceof class_2824 && CriticalsModule.mc.field_1724.field_6017 == 1337.0f) {
                    CriticalsModule.mc.field_1724.field_6017 = 0.0f;
                }
                return;
            }
        }));
        final EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (!this.mode.is("MetaHvH") || !this.isEnabled() || CriticalsModule.mc.field_1724 == null || CriticalsModule.mc.field_1687 == null) {
                return;
            }
            else {
                final double speed = Math.sqrt(CriticalsModule.mc.field_1724.method_18798().field_1352 * CriticalsModule.mc.field_1724.method_18798().field_1352 + CriticalsModule.mc.field_1724.method_18798().field_1350 * CriticalsModule.mc.field_1724.method_18798().field_1350);
                final boolean isMoving = speed > 0.01;
                this.groundCounter = (isMoving ? 0 : (this.groundCounter + 1));
                final AuraModule aura = AuraModule.getInstance();
                if (aura == null || !aura.isEnabled()) {
                    return;
                }
                else if (aura.target == null) {
                    return;
                }
                else if (!isMoving && this.groundCounter >= 20) {
                    return;
                }
                else {
                    if (CriticalsModule.mc.field_1724.method_7261(1.0f) >= 0.87f) {
                        boolean canCrit = true;
                        if (isMoving) {
                            final double angle = Math.atan2(CriticalsModule.mc.field_1724.method_18798().field_1350, CriticalsModule.mc.field_1724.method_18798().field_1352) - Math.toRadians(90.0);
                            final double offsetX = -Math.sin(angle) * 0.02;
                            final double offsetZ = Math.cos(angle) * 0.02;
                            final class_238 box = CriticalsModule.mc.field_1724.method_5829().method_989(offsetX, -0.5, offsetZ);
                            canCrit = CriticalsModule.mc.field_1687.method_20812((class_1297)CriticalsModule.mc.field_1724, box).iterator().hasNext();
                        }
                        if (canCrit) {
                            if (CriticalsModule.mc.field_1724.method_24828()) {
                                CriticalsModule.mc.field_1724.method_18800(CriticalsModule.mc.field_1724.method_18798().field_1352, 0.003, CriticalsModule.mc.field_1724.method_18798().field_1350);
                                CriticalsModule.mc.field_1724.method_24830(false);
                            }
                            else if (CriticalsModule.mc.field_1724.method_18798().field_1351 < 0.0 && CriticalsModule.mc.field_1724.field_6017 < 1.0E-6) {
                                final class_238 box2 = CriticalsModule.mc.field_1724.method_5829().method_989(0.0, -0.21, 0.0);
                                final boolean hasColl = CriticalsModule.mc.field_1687.method_20812((class_1297)CriticalsModule.mc.field_1724, box2).iterator().hasNext();
                                if (hasColl) {
                                    CriticalsModule.mc.field_1724.method_18800(CriticalsModule.mc.field_1724.method_18798().field_1352, -0.003, CriticalsModule.mc.field_1724.method_18798().field_1350);
                                }
                            }
                        }
                    }
                    return;
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
                for (double y = CriticalsModule.mc.field_1724.method_18381(CriticalsModule.mc.field_1724.method_18376()); y >= 0.0; y -= 0.1) {
                    final class_2338 pos = class_2338.method_49637(CriticalsModule.mc.field_1724.method_23317() + x, CriticalsModule.mc.field_1724.method_23318() + y, CriticalsModule.mc.field_1724.method_23321() + z);
                    if (CriticalsModule.mc.field_1687.method_8320(pos).method_27852(class_2246.field_10343)) {
                        return true;
                    }
                }
                final class_2338 pos2 = class_2338.method_49637(CriticalsModule.mc.field_1724.method_23317() + x, CriticalsModule.mc.field_1724.method_23318(), CriticalsModule.mc.field_1724.method_23321() + z);
                if (CriticalsModule.mc.field_1687.method_8320(pos2).method_27852(class_2246.field_10343)) {
                    return true;
                }
            }
        }
        final class_2338 headPos = class_2338.method_49637(CriticalsModule.mc.field_1724.method_23317(), CriticalsModule.mc.field_1724.method_23318() + CriticalsModule.mc.field_1724.method_18381(CriticalsModule.mc.field_1724.method_18376()) + 1.65, CriticalsModule.mc.field_1724.method_23321());
        return CriticalsModule.mc.field_1687.method_8320(headPos).method_27852(class_2246.field_10343);
    }
    
    private void sendOffset(final double yOffset) {
        CriticalsModule.mc.method_1562().method_52787((class_2596)new class_2828.class_2829(CriticalsModule.mc.field_1724.method_23317(), CriticalsModule.mc.field_1724.method_23318() + yOffset, CriticalsModule.mc.field_1724.method_23321(), false, CriticalsModule.mc.field_1724.field_5976));
    }
    
    private void sendFull(final double yOffset) {
        CriticalsModule.mc.method_1562().method_52787((class_2596)new class_2828.class_2830(CriticalsModule.mc.field_1724.method_23317(), CriticalsModule.mc.field_1724.method_23318() + yOffset, CriticalsModule.mc.field_1724.method_23321(), CriticalsModule.mc.field_1724.method_36454(), CriticalsModule.mc.field_1724.method_36455(), false, CriticalsModule.mc.field_1724.field_5976));
    }
    
    @Generated
    public static CriticalsModule getInstance() {
        return CriticalsModule.instance;
    }
    
    static {
        instance = new CriticalsModule();
    }
}
