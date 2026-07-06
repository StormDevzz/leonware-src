package sweetie.leonware.client.features.modules.combat;

import lombok.Generated;
import net.minecraft.class_1268;
import net.minecraft.class_1291;
import net.minecraft.class_1293;
import net.minecraft.class_1713;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1844;
import net.minecraft.class_2868;
import net.minecraft.class_9334;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.other.RotationUpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BindSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.system.backend.KeyStorage;
import sweetie.leonware.api.utils.math.TimerUtil;
import sweetie.leonware.api.utils.other.SlownessManager;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/combat/DebuffModule.class */
@ModuleRegister(name = "Debaff", category = Category.COMBAT)
public class DebuffModule extends Module {
    private static final DebuffModule instance = new DebuffModule();
    private final ModeSetting workMode = new ModeSetting("Режим работы").values("Всегда", "По бинду").value("Всегда");
    private final BindSetting throwKey = new BindSetting("Клавиша броска").value((Integer) (-999)).setVisible(() -> {
        return Boolean.valueOf(this.workMode.is("По бинду"));
    });
    private final ModeSetting type = new ModeSetting("Тип зелий").values("Только плохие", "Только хорошие", "Все").value("Только плохие");
    private final SliderSetting delay = new SliderSetting("Задержка (тиков)").value(Float.valueOf(2.0f)).range(0.0f, 5.0f).step(1.0f);
    private final ModeSetting swapMode = new ModeSetting("Свап").values("Пакетный", "Ванильный").value("Пакетный");
    private final TimerUtil timer = new TimerUtil();
    private boolean isBindingActive = false;

    @Generated
    public static DebuffModule getInstance() {
        return instance;
    }

    /* JADX WARN: Type inference failed for: r1v5, types: [sweetie.leonware.api.module.setting.BindSetting] */
    public DebuffModule() {
        addSettings(this.workMode, this.throwKey, this.type, this.delay, this.swapMode);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        addEvents(RotationUpdateEvent.getInstance().subscribe(new Listener(event -> {
            int slot;
            if (mc.field_1724 == null) {
                return;
            }
            if ((!this.workMode.is("По бинду") || KeyStorage.isPressed(this.throwKey.getValue().intValue())) && (slot = findPotionSlot()) != -1 && this.timer.finished((long) (this.delay.getValue().floatValue() * 50.0f))) {
                executeThrow(slot);
                this.timer.reset();
            }
        })));
    }

    private void executeThrow(int slot) {
        int oldSlot = mc.field_1724.method_31548().field_7545;
        boolean fromHotbar = slot >= 0 && slot < 9;
        Runnable throwAction = () -> {
            if (fromHotbar) {
                if (this.swapMode.is("Пакетный")) {
                    mc.method_1562().method_52787(new class_2868(slot));
                } else {
                    mc.field_1724.method_31548().field_7545 = slot;
                }
                mc.field_1761.method_2919(mc.field_1724, class_1268.field_5808);
                mc.field_1724.method_6104(class_1268.field_5808);
                if (this.swapMode.is("Пакетный")) {
                    mc.method_1562().method_52787(new class_2868(oldSlot));
                    return;
                } else {
                    mc.field_1724.method_31548().field_7545 = oldSlot;
                    return;
                }
            }
            mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, slot, oldSlot, class_1713.field_7791, mc.field_1724);
            mc.field_1761.method_2919(mc.field_1724, class_1268.field_5808);
            mc.field_1724.method_6104(class_1268.field_5808);
            mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, slot, oldSlot, class_1713.field_7791, mc.field_1724);
        };
        if (!fromHotbar && SlownessManager.isEnabled()) {
            SlownessManager.applySlowness(150L, 50L, throwAction);
        } else {
            throwAction.run();
        }
    }

    private int findPotionSlot() {
        for (int i = 0; i < 36; i++) {
            class_1799 stack = mc.field_1724.method_31548().method_5438(i);
            if ((stack.method_7909() == class_1802.field_8436 || stack.method_7909() == class_1802.field_8150) && isCorrectPotion(stack)) {
                return i;
            }
        }
        return -1;
    }

    private boolean isCorrectPotion(class_1799 stack) {
        boolean hasBad;
        boolean hasGood;
        class_1844 potion = (class_1844) stack.method_57824(class_9334.field_49651);
        if (potion == null) {
            return false;
        }
        hasBad = false;
        hasGood = false;
        for (class_1293 effect : potion.method_57397()) {
            if (((class_1291) effect.method_5579().comp_349()).method_5573()) {
                hasGood = true;
            } else {
                hasBad = true;
            }
        }
        switch (this.type.getValue()) {
            case "Только плохие":
                return hasBad;
            case "Только хорошие":
                return hasGood;
            default:
                return true;
        }
    }
}
