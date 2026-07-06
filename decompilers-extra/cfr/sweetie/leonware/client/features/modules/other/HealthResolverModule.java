/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1657
 *  net.minecraft.class_266
 *  net.minecraft.class_5250
 *  net.minecraft.class_8646
 *  net.minecraft.class_9013
 *  net.minecraft.class_9015
 *  net.minecraft.class_9022
 *  net.minecraft.class_9025
 */
package sweetie.leonware.client.features.modules.other;

import lombok.Generated;
import net.minecraft.class_1657;
import net.minecraft.class_266;
import net.minecraft.class_5250;
import net.minecraft.class_8646;
import net.minecraft.class_9013;
import net.minecraft.class_9015;
import net.minecraft.class_9022;
import net.minecraft.class_9025;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.TickEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.utils.player.PlayerUtil;

@ModuleRegister(name="Health Resolver", category=Category.OTHER)
public class HealthResolverModule
extends Module {
    private static final HealthResolverModule instance = new HealthResolverModule();
    private final ModeSetting mode = new ModeSetting("Mode").value("Fun Time").values("Really World", "Fun Time");

    public HealthResolverModule() {
        this.addSettings(this.mode);
    }

    public boolean isRW() {
        return this.mode.is("Really World") && this.isEnabled();
    }

    public boolean isFT() {
        return this.mode.is("Fun Time") && this.isEnabled();
    }

    @Override
    public void onEvent() {
        EventListener tickEvent = TickEvent.getInstance().subscribe(new Listener<TickEvent>(event -> {
            if (!this.isFT()) {
                return;
            }
            if (mc.method_1562() == null && mc.method_1562().method_45734() == null) {
                return;
            }
            for (class_1657 player : HealthResolverModule.mc.field_1687.method_18456()) {
                if (player == HealthResolverModule.mc.field_1724 || !PlayerUtil.isValidName(player.method_5477().getString())) continue;
                class_266 scoreboard = null;
                String parsedHealth = "";
                if (player.method_7327().method_1189(class_8646.field_45158) != null && (scoreboard = player.method_7327().method_1189(class_8646.field_45158)) != null) {
                    class_9013 readableScoreboardScore = player.method_7327().method_55430((class_9015)player, scoreboard);
                    class_5250 mutableText = class_9013.method_55398((class_9013)readableScoreboardScore, (class_9022)scoreboard.method_55380((class_9022)class_9025.field_47566));
                    parsedHealth = mutableText.getString();
                }
                float resolvedHealth = 0.0f;
                try {
                    resolvedHealth = Float.parseFloat(parsedHealth);
                }
                catch (NumberFormatException numberFormatException) {
                    // empty catch block
                }
                if (parsedHealth.isEmpty() || parsedHealth.equals("0")) continue;
                player.method_6033(resolvedHealth);
            }
        }));
        this.addEvents(tickEvent);
    }

    @Generated
    public static HealthResolverModule getInstance() {
        return instance;
    }
}

