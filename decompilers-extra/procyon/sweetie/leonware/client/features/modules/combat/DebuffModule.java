// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.combat;

import lombok.Generated;
import java.util.Iterator;
import net.minecraft.class_1291;
import net.minecraft.class_1293;
import net.minecraft.class_9334;
import net.minecraft.class_1844;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import sweetie.leonware.api.utils.other.SlownessManager;
import net.minecraft.class_1713;
import net.minecraft.class_1657;
import net.minecraft.class_1268;
import net.minecraft.class_2596;
import net.minecraft.class_2868;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.system.backend.KeyStorage;
import sweetie.leonware.api.event.events.other.RotationUpdateEvent;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.utils.math.TimerUtil;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.setting.BindSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Debaff", category = Category.COMBAT)
public class DebuffModule extends Module
{
    private static final DebuffModule instance;
    private final ModeSetting workMode;
    private final BindSetting throwKey;
    private final ModeSetting type;
    private final SliderSetting delay;
    private final ModeSetting swapMode;
    private final TimerUtil timer;
    private boolean isBindingActive;
    
    public DebuffModule() {
        this.workMode = new ModeSetting("\u0420\u0435\u0436\u0438\u043c \u0440\u0430\u0431\u043e\u0442\u044b").values("\u0412\u0441\u0435\u0433\u0434\u0430", "\u041f\u043e \u0431\u0438\u043d\u0434\u0443").value("\u0412\u0441\u0435\u0433\u0434\u0430");
        this.throwKey = new BindSetting("\u041a\u043b\u0430\u0432\u0438\u0448\u0430 \u0431\u0440\u043e\u0441\u043a\u0430").value(-999).setVisible(() -> this.workMode.is("\u041f\u043e \u0431\u0438\u043d\u0434\u0443"));
        this.type = new ModeSetting("\u0422\u0438\u043f \u0437\u0435\u043b\u0438\u0439").values("\u0422\u043e\u043b\u044c\u043a\u043e \u043f\u043b\u043e\u0445\u0438\u0435", "\u0422\u043e\u043b\u044c\u043a\u043e \u0445\u043e\u0440\u043e\u0448\u0438\u0435", "\u0412\u0441\u0435").value("\u0422\u043e\u043b\u044c\u043a\u043e \u043f\u043b\u043e\u0445\u0438\u0435");
        this.delay = new SliderSetting("\u0417\u0430\u0434\u0435\u0440\u0436\u043a\u0430 (\u0442\u0438\u043a\u043e\u0432)").value(2.0f).range(0.0f, 5.0f).step(1.0f);
        this.swapMode = new ModeSetting("\u0421\u0432\u0430\u043f").values("\u041f\u0430\u043a\u0435\u0442\u043d\u044b\u0439", "\u0412\u0430\u043d\u0438\u043b\u044c\u043d\u044b\u0439").value("\u041f\u0430\u043a\u0435\u0442\u043d\u044b\u0439");
        this.timer = new TimerUtil();
        this.isBindingActive = false;
        this.addSettings(this.workMode, this.throwKey, this.type, this.delay, this.swapMode);
    }
    
    @Override
    public void onEvent() {
        this.addEvents(RotationUpdateEvent.getInstance().subscribe(new Listener<RotationUpdateEvent>(event -> {
            if (DebuffModule.mc.field_1724 != null) {
                if (!this.workMode.is("\u041f\u043e \u0431\u0438\u043d\u0434\u0443") || KeyStorage.isPressed(this.throwKey.getValue())) {
                    final int slot = this.findPotionSlot();
                    if (slot != -1) {
                        if (!(!this.timer.finished((long)(this.delay.getValue() * 50.0f)))) {
                            this.executeThrow(slot);
                            this.timer.reset();
                        }
                    }
                }
            }
        })));
    }
    
    private void executeThrow(final int slot) {
        final int oldSlot = DebuffModule.mc.field_1724.method_31548().field_7545;
        final boolean fromHotbar = slot >= 0 && slot < 9;
        final Runnable throwAction = () -> {
            if (fromHotbar) {
                if (this.swapMode.is("\u041f\u0430\u043a\u0435\u0442\u043d\u044b\u0439")) {
                    DebuffModule.mc.method_1562().method_52787((class_2596)new class_2868(slot));
                }
                else {
                    DebuffModule.mc.field_1724.method_31548().field_7545 = slot;
                }
                DebuffModule.mc.field_1761.method_2919((class_1657)DebuffModule.mc.field_1724, class_1268.field_5808);
                DebuffModule.mc.field_1724.method_6104(class_1268.field_5808);
                if (this.swapMode.is("\u041f\u0430\u043a\u0435\u0442\u043d\u044b\u0439")) {
                    DebuffModule.mc.method_1562().method_52787((class_2596)new class_2868(oldSlot));
                }
                else {
                    DebuffModule.mc.field_1724.method_31548().field_7545 = oldSlot;
                }
            }
            else {
                DebuffModule.mc.field_1761.method_2906(DebuffModule.mc.field_1724.field_7512.field_7763, slot, oldSlot, class_1713.field_7791, (class_1657)DebuffModule.mc.field_1724);
                DebuffModule.mc.field_1761.method_2919((class_1657)DebuffModule.mc.field_1724, class_1268.field_5808);
                DebuffModule.mc.field_1724.method_6104(class_1268.field_5808);
                DebuffModule.mc.field_1761.method_2906(DebuffModule.mc.field_1724.field_7512.field_7763, slot, oldSlot, class_1713.field_7791, (class_1657)DebuffModule.mc.field_1724);
            }
            return;
        };
        if (!fromHotbar && SlownessManager.isEnabled()) {
            SlownessManager.applySlowness(150L, 50L, throwAction);
        }
        else {
            throwAction.run();
        }
    }
    
    private int findPotionSlot() {
        for (int i = 0; i < 36; ++i) {
            final class_1799 stack = DebuffModule.mc.field_1724.method_31548().method_5438(i);
            if ((stack.method_7909() == class_1802.field_8436 || stack.method_7909() == class_1802.field_8150) && this.isCorrectPotion(stack)) {
                return i;
            }
        }
        return -1;
    }
    
    private boolean isCorrectPotion(final class_1799 stack) {
        final class_1844 potion = (class_1844)stack.method_57824(class_9334.field_49651);
        if (potion == null) {
            return false;
        }
        boolean hasBad = false;
        boolean hasGood = false;
        for (final class_1293 effect : potion.method_57397()) {
            if (((class_1291)effect.method_5579().comp_349()).method_5573()) {
                hasGood = true;
            }
            else {
                hasBad = true;
            }
        }
        final String s = this.type.getValue();
        return switch (s) {
            case "\u0422\u043e\u043b\u044c\u043a\u043e \u043f\u043b\u043e\u0445\u0438\u0435" -> hasBad;
            case "\u0422\u043e\u043b\u044c\u043a\u043e \u0445\u043e\u0440\u043e\u0448\u0438\u0435" -> hasGood;
            default -> true;
        };
    }
    
    @Generated
    public static DebuffModule getInstance() {
        return DebuffModule.instance;
    }
    
    static {
        instance = new DebuffModule();
    }
}
