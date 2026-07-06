/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1297
 *  net.minecraft.class_1309
 *  net.minecraft.class_1657
 *  net.minecraft.class_3414
 *  net.minecraft.class_3419
 */
package sweetie.leonware.client.features.modules.other;

import java.util.HashSet;
import java.util.Set;
import lombok.Generated;
import net.minecraft.class_1297;
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

@ModuleRegister(name="Death Sounds", category=Category.OTHER)
public class DeathSoundsModule
extends Module {
    private static final DeathSoundsModule instance = new DeathSoundsModule();
    private final ModeSetting sound = new ModeSetting("Sound").value("Schoolboy").values("Schoolboy", "Schoolboy 2", "Wasted");
    private final SliderSetting volume = new SliderSetting("Volume").value(Float.valueOf(60.0f)).range(1.0f, 100.0f).step(1.0f);
    private final BooleanSetting onlyPlayers = new BooleanSetting("Only Players").value(true);
    private final Set<Integer> deadIds = new HashSet<Integer>();

    public DeathSoundsModule() {
        this.addSettings(this.sound, this.volume, this.onlyPlayers);
    }

    @Override
    public void onDisable() {
        this.deadIds.clear();
    }

    @Override
    public void onEvent() {
        EventListener tickEvent = TickEvent.getInstance().subscribe(new Listener<TickEvent>(event -> {
            if (DeathSoundsModule.mc.field_1687 == null || DeathSoundsModule.mc.field_1724 == null) {
                return;
            }
            if (DeathSoundsModule.mc.field_1724.method_29504() && DeathSoundsModule.mc.field_1724.field_6213 == 1) {
                class_3414 soundEvent = switch ((String)this.sound.getValue()) {
                    case "Schoolboy 2" -> SoundUtil.SCHOOLBOY2_EVENT;
                    case "Wasted" -> SoundUtil.WASTED_EVENT;
                    default -> SoundUtil.SCHOOLBOY_EVENT;
                };
                DeathSoundsModule.mc.field_1687.method_8396((class_1657)DeathSoundsModule.mc.field_1724, mc.method_1560().method_24515(), soundEvent, class_3419.field_15248, ((Float)this.volume.getValue()).floatValue() / 100.0f, 1.0f);
            }
            for (class_1297 entity : DeathSoundsModule.mc.field_1687.method_18112()) {
                boolean isDead;
                if (entity == DeathSoundsModule.mc.field_1724 || !(entity instanceof class_1309)) continue;
                class_1309 living = (class_1309)entity;
                if (((Boolean)this.onlyPlayers.getValue()).booleanValue() && !(entity instanceof class_1657)) continue;
                int id = entity.method_5628();
                boolean bl = isDead = living.method_6032() <= 0.0f;
                if (isDead && !this.deadIds.contains(id)) {
                    this.deadIds.add(id);
                    class_3414 soundEvent = switch ((String)this.sound.getValue()) {
                        case "Schoolboy 2" -> SoundUtil.SCHOOLBOY2_EVENT;
                        case "Wasted" -> SoundUtil.WASTED_EVENT;
                        default -> SoundUtil.SCHOOLBOY_EVENT;
                    };
                    DeathSoundsModule.mc.field_1687.method_8396((class_1657)DeathSoundsModule.mc.field_1724, mc.method_1560().method_24515(), soundEvent, class_3419.field_15248, ((Float)this.volume.getValue()).floatValue() / 100.0f, 1.0f);
                    continue;
                }
                if (isDead) continue;
                this.deadIds.remove(id);
            }
        }));
        this.addEvents(tickEvent);
    }

    @Generated
    public static DeathSoundsModule getInstance() {
        return instance;
    }
}

