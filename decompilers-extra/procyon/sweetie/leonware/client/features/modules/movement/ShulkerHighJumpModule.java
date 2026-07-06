// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.movement;

import lombok.Generated;
import net.minecraft.class_243;
import net.minecraft.class_2586;
import net.minecraft.class_2338;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import net.minecraft.class_2627;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.setting.Setting;
import java.util.function.Supplier;
import java.util.Objects;
import sweetie.leonware.api.utils.math.TimerUtil;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Shulker High Jump", category = Category.MOVEMENT)
public class ShulkerHighJumpModule extends Module
{
    private static final ShulkerHighJumpModule instance;
    private final SliderSetting cooldown;
    private final BooleanSetting useYChange;
    private final SliderSetting jumpHeight;
    private final BooleanSetting jumpX2;
    private final SliderSetting doubleJumpDelay;
    private final TimerUtil jumpTimer;
    private final TimerUtil doubleJumpTimer;
    private boolean hasJumped;
    
    public ShulkerHighJumpModule() {
        this.cooldown = new SliderSetting("\u041f\u0435\u0440\u0435\u0437\u0430\u0440\u044f\u0434\u043a\u0430").value(100.0f).range(0.0f, 1000.0f).step(10.0f);
        this.useYChange = new BooleanSetting("\u0418\u0437\u043c\u0435\u043d\u044f\u0442\u044c \u0432\u044b\u0441\u043e\u0442\u0443").value(true);
        final SliderSetting step = new SliderSetting("\u0421\u0438\u043b\u0430 \u043f\u0440\u044b\u0436\u043a\u0430").value(2.329f).range(0.1f, 5.0f).step(0.1f);
        final BooleanSetting useYChange = this.useYChange;
        Objects.requireNonNull(useYChange);
        this.jumpHeight = step.setVisible((Supplier<Boolean>)useYChange::getValue);
        this.jumpX2 = new BooleanSetting("\u0412\u0442\u043e\u0440\u043e\u0439 \u043f\u0440\u044b\u0436\u043e\u043a").value(false);
        final SliderSetting step2 = new SliderSetting("\u0417\u0430\u0434\u0435\u0440\u0436\u043a\u0430").value(500.0f).range(30.0f, 2000.0f).step(10.0f);
        final BooleanSetting jumpX2 = this.jumpX2;
        Objects.requireNonNull(jumpX2);
        this.doubleJumpDelay = step2.setVisible((Supplier<Boolean>)jumpX2::getValue);
        this.jumpTimer = new TimerUtil();
        this.doubleJumpTimer = new TimerUtil();
        this.hasJumped = false;
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
        final EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (ShulkerHighJumpModule.mc.field_1724 == null || ShulkerHighJumpModule.mc.field_1687 == null) {
                return;
            }
            else if (ShulkerHighJumpModule.mc.field_1724.method_52535()) {
                return;
            }
            else if (ShulkerHighJumpModule.mc.field_1724.method_5715()) {
                return;
            }
            else if (ShulkerHighJumpModule.mc.field_1724.method_31549().field_7479 || ShulkerHighJumpModule.mc.field_1724.method_3144()) {
                return;
            }
            else if (!this.jumpTimer.finished(this.cooldown.getValue().longValue())) {
                return;
            }
            else {
                final double maxDist = 1.0;
                final double maxHeight = 2.0;
                final class_2338 playerPos = ShulkerHighJumpModule.mc.field_1724.method_24515();
                for (int radius = 3, x = -radius; x <= radius; ++x) {
                    for (int y = -radius; y <= radius; ++y) {
                        for (int z = -radius; z <= radius; ++z) {
                            final class_2338 pos = playerPos.method_10069(x, y, z);
                            final class_2586 tile = ShulkerHighJumpModule.mc.field_1687.method_8321(pos);
                            if (tile instanceof final class_2627 shulkerBox) {
                                final double distance = Math.sqrt(Math.pow(ShulkerHighJumpModule.mc.field_1724.method_23317() - (pos.method_10263() + 0.5), 2.0) + Math.pow(ShulkerHighJumpModule.mc.field_1724.method_23321() - (pos.method_10260() + 0.5), 2.0));
                                final double heightDiff = Math.abs(ShulkerHighJumpModule.mc.field_1724.method_23318() - (pos.method_10264() + 0.5));
                                final double currentMaxH = (ShulkerHighJumpModule.mc.field_1724.method_18798().field_1351 > 1.0) ? (maxHeight * 15.0) : maxHeight;
                                if (distance <= maxDist && heightDiff <= currentMaxH && ShulkerHighJumpModule.mc.field_1724.field_6017 == 0.0f) {
                                    final float progress = shulkerBox.method_11312(1.0f);
                                    if (progress > 0.0f && progress != 1.0f) {
                                        this.performJump();
                                    }
                                }
                            }
                        }
                    }
                }
                if (this.jumpX2.getValue() && this.hasJumped && this.doubleJumpTimer.finished(this.doubleJumpDelay.getValue().longValue())) {
                    this.performDoubleJump();
                    this.hasJumped = false;
                }
                if (this.hasJumped && ShulkerHighJumpModule.mc.field_1724.method_24828()) {
                    this.hasJumped = false;
                }
                return;
            }
        }));
        this.addEvents(updateEvent);
    }
    
    private void performJump() {
        if (this.hasJumped) {
            return;
        }
        final class_243 velocity = ShulkerHighJumpModule.mc.field_1724.method_18798();
        double newY = velocity.field_1351;
        if (this.useYChange.getValue()) {
            newY = this.jumpHeight.getValue();
        }
        ShulkerHighJumpModule.mc.field_1724.method_18800(velocity.field_1352, newY, velocity.field_1350);
        this.hasJumped = true;
        this.jumpTimer.reset();
        this.doubleJumpTimer.reset();
    }
    
    private void performDoubleJump() {
        final class_243 velocity = ShulkerHighJumpModule.mc.field_1724.method_18798();
        double newY = velocity.field_1351;
        if (this.useYChange.getValue()) {
            newY = this.jumpHeight.getValue();
        }
        ShulkerHighJumpModule.mc.field_1724.method_18800(velocity.field_1352, newY, velocity.field_1350);
        this.doubleJumpTimer.reset();
    }
    
    @Generated
    public static ShulkerHighJumpModule getInstance() {
        return ShulkerHighJumpModule.instance;
    }
    
    static {
        instance = new ShulkerHighJumpModule();
    }
}
