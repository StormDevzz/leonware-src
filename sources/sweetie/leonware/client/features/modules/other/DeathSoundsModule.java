package sweetie.leonware.client.features.modules.other;

import java.util.HashSet;
import java.util.Set;
import lombok.Generated;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_3414;
import net.minecraft.class_3419;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.TickEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.utils.other.SoundUtil;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/other/DeathSoundsModule.class */
@ModuleRegister(name = "Death Sounds", category = Category.OTHER)
public class DeathSoundsModule extends Module {
    private static final DeathSoundsModule instance = new DeathSoundsModule();
    private final ModeSetting sound = new ModeSetting("Sound").value("Schoolboy").values("Schoolboy", "Schoolboy 2", "Wasted");
    private final SliderSetting volume = new SliderSetting("Volume").value(Float.valueOf(60.0f)).range(1.0f, 100.0f).step(1.0f);
    private final BooleanSetting onlyPlayers = new BooleanSetting("Only Players").value((Boolean) true);
    private final Set<Integer> deadIds = new HashSet();

    @Generated
    public static DeathSoundsModule getInstance() {
        return instance;
    }

    public DeathSoundsModule() {
        addSettings(this.sound, this.volume, this.onlyPlayers);
    }

    @Override // sweetie.leonware.api.module.Module
    public void onDisable() {
        this.deadIds.clear();
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener tickEvent = TickEvent.getInstance().subscribe(new Listener(event -> {
            class_3414 class_3414Var;
            class_3414 class_3414Var2;
            if (mc.field_1687 == null || mc.field_1724 == null) {
                return;
            }
            if (mc.field_1724.method_29504() && mc.field_1724.field_6213 == 1) {
                switch (this.sound.getValue()) {
                    case "Schoolboy 2":
                        class_3414Var2 = SoundUtil.SCHOOLBOY2_EVENT;
                        break;
                    case "Wasted":
                        class_3414Var2 = SoundUtil.WASTED_EVENT;
                        break;
                    default:
                        class_3414Var2 = SoundUtil.SCHOOLBOY_EVENT;
                        break;
                }
                class_3414 soundEvent = class_3414Var2;
                mc.field_1687.method_8396(mc.field_1724, mc.method_1560().method_24515(), soundEvent, class_3419.field_15248, this.volume.getValue().floatValue() / 100.0f, 1.0f);
            }
            for (class_1309 class_1309Var : mc.field_1687.method_18112()) {
                if (class_1309Var != mc.field_1724 && (class_1309Var instanceof class_1309)) {
                    class_1309 living = class_1309Var;
                    if (!this.onlyPlayers.getValue().booleanValue() || (class_1309Var instanceof class_1657)) {
                        int id = class_1309Var.method_5628();
                        boolean isDead = living.method_6032() <= 0.0f;
                        if (isDead && !this.deadIds.contains(Integer.valueOf(id))) {
                            this.deadIds.add(Integer.valueOf(id));
                            switch (this.sound.getValue()) {
                                case "Schoolboy 2":
                                    class_3414Var = SoundUtil.SCHOOLBOY2_EVENT;
                                    break;
                                case "Wasted":
                                    class_3414Var = SoundUtil.WASTED_EVENT;
                                    break;
                                default:
                                    class_3414Var = SoundUtil.SCHOOLBOY_EVENT;
                                    break;
                            }
                            class_3414 soundEvent2 = class_3414Var;
                            mc.field_1687.method_8396(mc.field_1724, mc.method_1560().method_24515(), soundEvent2, class_3419.field_15248, this.volume.getValue().floatValue() / 100.0f, 1.0f);
                        } else if (!isDead) {
                            this.deadIds.remove(Integer.valueOf(id));
                        }
                    }
                }
            }
        }));
        addEvents(tickEvent);
    }
}
