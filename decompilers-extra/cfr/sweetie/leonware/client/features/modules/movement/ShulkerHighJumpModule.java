/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_2338
 *  net.minecraft.class_243
 *  net.minecraft.class_2586
 *  net.minecraft.class_2627
 */
package sweetie.leonware.client.features.modules.movement;

import lombok.Generated;
import net.minecraft.class_2338;
import net.minecraft.class_243;
import net.minecraft.class_2586;
import net.minecraft.class_2627;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.utils.math.TimerUtil;

@ModuleRegister(name="Shulker High Jump", category=Category.MOVEMENT)
public class ShulkerHighJumpModule
extends Module {
    private static final ShulkerHighJumpModule instance = new ShulkerHighJumpModule();
    private final SliderSetting cooldown = new SliderSetting("\u041f\u0435\u0440\u0435\u0437\u0430\u0440\u044f\u0434\u043a\u0430").value(Float.valueOf(100.0f)).range(0.0f, 1000.0f).step(10.0f);
    private final BooleanSetting useYChange = new BooleanSetting("\u0418\u0437\u043c\u0435\u043d\u044f\u0442\u044c \u0432\u044b\u0441\u043e\u0442\u0443").value(true);
    private final SliderSetting jumpHeight = new SliderSetting("\u0421\u0438\u043b\u0430 \u043f\u0440\u044b\u0436\u043a\u0430").value(Float.valueOf(2.329f)).range(0.1f, 5.0f).step(0.1f).setVisible(this.useYChange::getValue);
    private final BooleanSetting jumpX2 = new BooleanSetting("\u0412\u0442\u043e\u0440\u043e\u0439 \u043f\u0440\u044b\u0436\u043e\u043a").value(false);
    private final SliderSetting doubleJumpDelay = new SliderSetting("\u0417\u0430\u0434\u0435\u0440\u0436\u043a\u0430").value(Float.valueOf(500.0f)).range(30.0f, 2000.0f).step(10.0f).setVisible(this.jumpX2::getValue);
    private final TimerUtil jumpTimer = new TimerUtil();
    private final TimerUtil doubleJumpTimer = new TimerUtil();
    private boolean hasJumped = false;

    public ShulkerHighJumpModule() {
        this.addSettings(this.cooldown, this.useYChange, this.jumpHeight, this.jumpX2, this.doubleJumpDelay);
    }

    @Override
    public void onEnable() {
        this.hasJumped = false;
        this.jumpTimer.reset();
        this.doubleJumpTimer.reset();
    }

    @Override
    public void onDisable() {
        this.hasJumped = false;
    }

    @Override
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (ShulkerHighJumpModule.mc.field_1724 == null || ShulkerHighJumpModule.mc.field_1687 == null) {
                return;
            }
            if (ShulkerHighJumpModule.mc.field_1724.method_52535()) {
                return;
            }
            if (ShulkerHighJumpModule.mc.field_1724.method_5715()) {
                return;
            }
            if (ShulkerHighJumpModule.mc.field_1724.method_31549().field_7479 || ShulkerHighJumpModule.mc.field_1724.method_3144()) {
                return;
            }
            if (!this.jumpTimer.finished(((Float)this.cooldown.getValue()).longValue())) {
                return;
            }
            double maxDist = 1.0;
            double maxHeight = 2.0;
            class_2338 playerPos = ShulkerHighJumpModule.mc.field_1724.method_24515();
            int radius = 3;
            for (int x = -radius; x <= radius; ++x) {
                for (int y = -radius; y <= radius; ++y) {
                    for (int z = -radius; z <= radius; ++z) {
                        float progress;
                        double currentMaxH;
                        class_2338 pos = playerPos.method_10069(x, y, z);
                        class_2586 tile = ShulkerHighJumpModule.mc.field_1687.method_8321(pos);
                        if (!(tile instanceof class_2627)) continue;
                        class_2627 shulkerBox = (class_2627)tile;
                        double distance = Math.sqrt(Math.pow(ShulkerHighJumpModule.mc.field_1724.method_23317() - ((double)pos.method_10263() + 0.5), 2.0) + Math.pow(ShulkerHighJumpModule.mc.field_1724.method_23321() - ((double)pos.method_10260() + 0.5), 2.0));
                        double heightDiff = Math.abs(ShulkerHighJumpModule.mc.field_1724.method_23318() - ((double)pos.method_10264() + 0.5));
                        double d = currentMaxH = ShulkerHighJumpModule.mc.field_1724.method_18798().field_1351 > 1.0 ? maxHeight * 15.0 : maxHeight;
                        if (!(distance <= maxDist) || !(heightDiff <= currentMaxH) || ShulkerHighJumpModule.mc.field_1724.field_6017 != 0.0f || !((progress = shulkerBox.method_11312(1.0f)) > 0.0f) || progress == 1.0f) continue;
                        this.performJump();
                    }
                }
            }
            if (((Boolean)this.jumpX2.getValue()).booleanValue() && this.hasJumped && this.doubleJumpTimer.finished(((Float)this.doubleJumpDelay.getValue()).longValue())) {
                this.performDoubleJump();
                this.hasJumped = false;
            }
            if (this.hasJumped && ShulkerHighJumpModule.mc.field_1724.method_24828()) {
                this.hasJumped = false;
            }
        }));
        this.addEvents(updateEvent);
    }

    private void performJump() {
        if (this.hasJumped) {
            return;
        }
        class_243 velocity = ShulkerHighJumpModule.mc.field_1724.method_18798();
        double newY = velocity.field_1351;
        if (((Boolean)this.useYChange.getValue()).booleanValue()) {
            newY = ((Float)this.jumpHeight.getValue()).floatValue();
        }
        ShulkerHighJumpModule.mc.field_1724.method_18800(velocity.field_1352, newY, velocity.field_1350);
        this.hasJumped = true;
        this.jumpTimer.reset();
        this.doubleJumpTimer.reset();
    }

    private void performDoubleJump() {
        class_243 velocity = ShulkerHighJumpModule.mc.field_1724.method_18798();
        double newY = velocity.field_1351;
        if (((Boolean)this.useYChange.getValue()).booleanValue()) {
            newY = ((Float)this.jumpHeight.getValue()).floatValue();
        }
        ShulkerHighJumpModule.mc.field_1724.method_18800(velocity.field_1352, newY, velocity.field_1350);
        this.doubleJumpTimer.reset();
    }

    @Generated
    public static ShulkerHighJumpModule getInstance() {
        return instance;
    }
}

