package sweetie.leonware.client.features.modules.combat;

import java.util.Objects;
import lombok.Generated;
import net.minecraft.class_124;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_1713;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1829;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.event.events.player.world.AttackEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.utils.other.SlownessManager;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/combat/AutoOilModule.class */
@ModuleRegister(name = "Auto Oil", category = Category.COMBAT)
public class AutoOilModule extends Module {
    private static final AutoOilModule instance = new AutoOilModule();
    private final ModeSetting oilType = new ModeSetting("Тип масла").values("Урон", "Вампиризм", "Любое").value("Любое");
    private final BooleanSetting onlyWithAura = new BooleanSetting("Только с Aura").value((Boolean) true);
    private final BooleanSetting ignoreNaked;
    private final SliderSetting hitThreshold;
    private final SliderSetting returnDelay;
    private int hitCount;
    private boolean pendingReturn;
    private long returnAt;
    private int originalSwordSlot;

    @Generated
    public static AutoOilModule getInstance() {
        return instance;
    }

    /* JADX WARN: Type inference failed for: r1v7, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    public AutoOilModule() {
        BooleanSetting booleanSettingValue = new BooleanSetting("Не на бомжей").value((Boolean) false);
        BooleanSetting booleanSetting = this.onlyWithAura;
        Objects.requireNonNull(booleanSetting);
        this.ignoreNaked = booleanSettingValue.setVisible(booleanSetting::getValue);
        this.hitThreshold = new SliderSetting("Ударов до масла").value(Float.valueOf(12.0f)).range(1.0f, 50.0f).step(1.0f);
        this.returnDelay = new SliderSetting("Задержка возврата (тики)").value(Float.valueOf(5.0f)).range(1.0f, 20.0f).step(1.0f);
        this.hitCount = 0;
        this.pendingReturn = false;
        this.returnAt = 0L;
        this.originalSwordSlot = -1;
        addSettings(this.oilType, this.onlyWithAura, this.ignoreNaked, this.hitThreshold, this.returnDelay);
    }

    @Override // sweetie.leonware.api.module.Module
    public void onEnable() {
        this.hitCount = 0;
        this.pendingReturn = false;
        this.originalSwordSlot = -1;
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        addEvents(AttackEvent.getInstance().subscribe(new Listener(event -> {
            if (mc.field_1724 == null || this.pendingReturn) {
                return;
            }
            if (this.onlyWithAura.getValue().booleanValue()) {
                if (!AuraModule.getInstance().isEnabled() || AuraModule.getInstance().target == null) {
                    return;
                }
                if (this.ignoreNaked.getValue().booleanValue()) {
                    class_1309 patt0$temp = AuraModule.getInstance().target;
                    if (patt0$temp instanceof class_1657) {
                        class_1657 targetPlayer = (class_1657) patt0$temp;
                        if (isNaked(targetPlayer)) {
                            return;
                        }
                    }
                }
            }
            class_1799 held = mc.field_1724.method_6047();
            if (isMagistraSword(held)) {
                this.hitCount++;
                if (this.hitCount < this.hitThreshold.getValue().intValue()) {
                    return;
                }
                this.hitCount = 0;
                int oilSlot = findOilSlot();
                if (oilSlot == -1) {
                    return;
                }
                this.originalSwordSlot = mc.field_1724.method_31548().field_7545;
                int swordScreenSlot = this.originalSwordSlot + 36;
                int syncId = mc.field_1724.field_7512.field_7763;
                Runnable applyOil = () -> {
                    mc.field_1761.method_2906(syncId, oilSlot, 0, class_1713.field_7790, mc.field_1724);
                    mc.field_1761.method_2906(syncId, swordScreenSlot, 0, class_1713.field_7790, mc.field_1724);
                    this.pendingReturn = true;
                    this.returnAt = System.currentTimeMillis() + ((long) (this.returnDelay.getValue().floatValue() * 50.0f));
                };
                if (SlownessManager.isEnabled()) {
                    SlownessManager.applySlowness(200L, 1L, applyOil);
                } else {
                    applyOil.run();
                }
            }
        })), UpdateEvent.getInstance().subscribe(new Listener(event2 -> {
            if (this.pendingReturn && System.currentTimeMillis() >= this.returnAt && mc.field_1724 != null) {
                class_1799 cursor = mc.field_1724.field_7512.method_34255();
                if (!cursor.method_7960() && this.originalSwordSlot != -1) {
                    int swordScreenSlot = this.originalSwordSlot + 36;
                    int syncId = mc.field_1724.field_7512.field_7763;
                    Runnable returnSword = () -> {
                        mc.field_1761.method_2906(syncId, swordScreenSlot, 0, class_1713.field_7790, mc.field_1724);
                    };
                    if (SlownessManager.isEnabled()) {
                        SlownessManager.applySlowness(100L, 1L, returnSword);
                    } else {
                        returnSword.run();
                    }
                }
                this.pendingReturn = false;
                this.originalSwordSlot = -1;
            }
        })));
    }

    private int findOilSlot() {
        int i = 0;
        while (i < 36) {
            class_1799 stack = mc.field_1724.method_31548().method_5438(i);
            if (!stack.method_7960() && stack.method_7909() == class_1802.field_8575) {
                String rawName = stack.method_7964().getString();
                String name = class_124.method_539(rawName);
                if (name == null) {
                    name = rawName;
                }
                if (name.contains("асло")) {
                    String mode = this.oilType.getValue();
                    if ((!mode.equals("Урон") || name.contains("урон") || name.contains("Урон")) && (!mode.equals("Вампиризм") || name.contains("вампир") || name.contains("Вампир"))) {
                        return i < 9 ? i + 36 : i;
                    }
                } else {
                    continue;
                }
            }
            i++;
        }
        return -1;
    }

    private boolean isMagistraSword(class_1799 stack) {
        if (stack.method_7960()) {
            return false;
        }
        if (!(stack.method_7909() instanceof class_1829) && stack.method_7909() != class_1802.field_22022) {
            return false;
        }
        String name = stack.method_7964().getString().toLowerCase();
        return name.contains("магистра");
    }

    private boolean isNaked(class_1657 player) {
        class_1792 item;
        for (class_1799 armor : player.method_5661()) {
            if (!armor.method_7960() && ((item = armor.method_7909()) == class_1802.field_8805 || item == class_1802.field_8058 || item == class_1802.field_8348 || item == class_1802.field_8285 || item == class_1802.field_22027 || item == class_1802.field_22028 || item == class_1802.field_22029 || item == class_1802.field_22030)) {
                return false;
            }
        }
        return true;
    }
}
