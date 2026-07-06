// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.combat;

import lombok.Generated;
import net.minecraft.class_1792;
import java.util.Iterator;
import net.minecraft.class_1829;
import net.minecraft.class_124;
import net.minecraft.class_1802;
import net.minecraft.class_1799;
import net.minecraft.class_1309;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.utils.other.SlownessManager;
import net.minecraft.class_1713;
import net.minecraft.class_1657;
import sweetie.leonware.api.event.events.player.world.AttackEvent;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.module.setting.Setting;
import java.util.function.Supplier;
import java.util.Objects;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Auto Oil", category = Category.COMBAT)
public class AutoOilModule extends Module
{
    private static final AutoOilModule instance;
    private final ModeSetting oilType;
    private final BooleanSetting onlyWithAura;
    private final BooleanSetting ignoreNaked;
    private final SliderSetting hitThreshold;
    private final SliderSetting returnDelay;
    private int hitCount;
    private boolean pendingReturn;
    private long returnAt;
    private int originalSwordSlot;
    
    public AutoOilModule() {
        this.oilType = new ModeSetting("\u0422\u0438\u043f \u043c\u0430\u0441\u043b\u0430").values("\u0423\u0440\u043e\u043d", "\u0412\u0430\u043c\u043f\u0438\u0440\u0438\u0437\u043c", "\u041b\u044e\u0431\u043e\u0435").value("\u041b\u044e\u0431\u043e\u0435");
        this.onlyWithAura = new BooleanSetting("\u0422\u043e\u043b\u044c\u043a\u043e \u0441 Aura").value(true);
        final BooleanSetting value = new BooleanSetting("\u041d\u0435 \u043d\u0430 \u0431\u043e\u043c\u0436\u0435\u0439").value(false);
        final BooleanSetting onlyWithAura = this.onlyWithAura;
        Objects.requireNonNull(onlyWithAura);
        this.ignoreNaked = value.setVisible((Supplier<Boolean>)onlyWithAura::getValue);
        this.hitThreshold = new SliderSetting("\u0423\u0434\u0430\u0440\u043e\u0432 \u0434\u043e \u043c\u0430\u0441\u043b\u0430").value(12.0f).range(1.0f, 50.0f).step(1.0f);
        this.returnDelay = new SliderSetting("\u0417\u0430\u0434\u0435\u0440\u0436\u043a\u0430 \u0432\u043e\u0437\u0432\u0440\u0430\u0442\u0430 (\u0442\u0438\u043a\u0438)").value(5.0f).range(1.0f, 20.0f).step(1.0f);
        this.hitCount = 0;
        this.pendingReturn = false;
        this.returnAt = 0L;
        this.originalSwordSlot = -1;
        this.addSettings(this.oilType, this.onlyWithAura, this.ignoreNaked, this.hitThreshold, this.returnDelay);
    }
    
    @Override
    public void onEnable() {
        this.hitCount = 0;
        this.pendingReturn = false;
        this.originalSwordSlot = -1;
    }
    
    @Override
    public void onEvent() {
        this.addEvents(AttackEvent.getInstance().subscribe(new Listener<AttackEvent.AttackEventData>(event -> {
            if (AutoOilModule.mc.field_1724 != null) {
                if (!this.pendingReturn) {
                    if (this.onlyWithAura.getValue()) {
                        if (!AuraModule.getInstance().isEnabled()) {
                            return;
                        }
                        else if (AuraModule.getInstance().target == null) {
                            return;
                        }
                        else if (this.ignoreNaked.getValue()) {
                            final class_1309 patt0$temp = AuraModule.getInstance().target;
                            if (patt0$temp instanceof final class_1657 targetPlayer) {
                                if (this.isNaked(targetPlayer)) {
                                    return;
                                }
                            }
                        }
                    }
                    final class_1799 held = AutoOilModule.mc.field_1724.method_6047();
                    if (!(!this.isMagistraSword(held))) {
                        ++this.hitCount;
                        if (this.hitCount >= this.hitThreshold.getValue().intValue()) {
                            this.hitCount = 0;
                            final int oilSlot = this.findOilSlot();
                            if (oilSlot != -1) {
                                this.originalSwordSlot = AutoOilModule.mc.field_1724.method_31548().field_7545;
                                final int swordScreenSlot = this.originalSwordSlot + 36;
                                final int syncId = AutoOilModule.mc.field_1724.field_7512.field_7763;
                                final Runnable applyOil = () -> {
                                    AutoOilModule.mc.field_1761.method_2906(syncId, oilSlot, 0, class_1713.field_7790, (class_1657)AutoOilModule.mc.field_1724);
                                    AutoOilModule.mc.field_1761.method_2906(syncId, swordScreenSlot, 0, class_1713.field_7790, (class_1657)AutoOilModule.mc.field_1724);
                                    this.pendingReturn = true;
                                    this.returnAt = System.currentTimeMillis() + (long)(this.returnDelay.getValue() * 50.0f);
                                    return;
                                };
                                if (SlownessManager.isEnabled()) {
                                    SlownessManager.applySlowness(200L, 1L, applyOil);
                                }
                                else {
                                    applyOil.run();
                                }
                            }
                        }
                    }
                }
            }
        })), UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (!(!this.pendingReturn)) {
                if (System.currentTimeMillis() >= this.returnAt) {
                    if (AutoOilModule.mc.field_1724 != null) {
                        final class_1799 cursor = AutoOilModule.mc.field_1724.field_7512.method_34255();
                        if (!cursor.method_7960() && this.originalSwordSlot != -1) {
                            final int swordScreenSlot2 = this.originalSwordSlot + 36;
                            final int syncId2 = AutoOilModule.mc.field_1724.field_7512.field_7763;
                            final Runnable returnSword = () -> AutoOilModule.mc.field_1761.method_2906(syncId, swordScreenSlot, 0, class_1713.field_7790, (class_1657)AutoOilModule.mc.field_1724);
                            if (SlownessManager.isEnabled()) {
                                SlownessManager.applySlowness(100L, 1L, returnSword);
                            }
                            else {
                                returnSword.run();
                            }
                        }
                        this.pendingReturn = false;
                        this.originalSwordSlot = -1;
                    }
                }
            }
        })));
    }
    
    private int findOilSlot() {
        for (int i = 0; i < 36; ++i) {
            final class_1799 stack = AutoOilModule.mc.field_1724.method_31548().method_5438(i);
            if (!stack.method_7960()) {
                if (stack.method_7909() == class_1802.field_8575) {
                    final String rawName = stack.method_7964().getString();
                    String name = class_124.method_539(rawName);
                    if (name == null) {
                        name = rawName;
                    }
                    if (name.contains("\u0430\u0441\u043b\u043e")) {
                        final String mode = this.oilType.getValue();
                        if (!mode.equals("\u0423\u0440\u043e\u043d") || name.contains("\u0443\u0440\u043e\u043d") || name.contains("\u0423\u0440\u043e\u043d")) {
                            if (!mode.equals("\u0412\u0430\u043c\u043f\u0438\u0440\u0438\u0437\u043c") || name.contains("\u0432\u0430\u043c\u043f\u0438\u0440") || name.contains("\u0412\u0430\u043c\u043f\u0438\u0440")) {
                                return (i < 9) ? (i + 36) : i;
                            }
                        }
                    }
                }
            }
        }
        return -1;
    }
    
    private boolean isMagistraSword(final class_1799 stack) {
        if (stack.method_7960()) {
            return false;
        }
        if (!(stack.method_7909() instanceof class_1829) && stack.method_7909() != class_1802.field_22022) {
            return false;
        }
        final String name = stack.method_7964().getString().toLowerCase();
        return name.contains("\u043c\u0430\u0433\u0438\u0441\u0442\u0440\u0430");
    }
    
    private boolean isNaked(final class_1657 player) {
        for (final class_1799 armor : player.method_5661()) {
            if (armor.method_7960()) {
                continue;
            }
            final class_1792 item = armor.method_7909();
            if (item == class_1802.field_8805 || item == class_1802.field_8058 || item == class_1802.field_8348 || item == class_1802.field_8285 || item == class_1802.field_22027 || item == class_1802.field_22028 || item == class_1802.field_22029 || item == class_1802.field_22030) {
                return false;
            }
        }
        return true;
    }
    
    @Generated
    public static AutoOilModule getInstance() {
        return AutoOilModule.instance;
    }
    
    static {
        instance = new AutoOilModule();
    }
}
