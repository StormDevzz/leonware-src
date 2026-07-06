// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.movement;

import lombok.Generated;
import net.minecraft.class_2596;
import net.minecraft.class_2350;
import net.minecraft.class_2338;
import net.minecraft.class_2846;
import net.minecraft.class_1802;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.TickEvent;
import sweetie.leonware.api.module.setting.Setting;
import java.util.function.Supplier;
import java.util.Objects;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Trident Fly", category = Category.MOVEMENT)
public class TridentFlyModule extends Module
{
    private static final TridentFlyModule instance;
    private final BooleanSetting allowNoWater;
    private final BooleanSetting instant;
    private final BooleanSetting spam;
    private final SliderSetting ticks;
    private boolean wasUsingTrident;
    
    public TridentFlyModule() {
        this.allowNoWater = new BooleanSetting("Allow No Water").value(true);
        this.instant = new BooleanSetting("Instant").value(true);
        this.spam = new BooleanSetting("Spam").value(false);
        final SliderSetting step = new SliderSetting("Ticks").value(3.0f).range(0.0f, 20.0f).step(1.0f);
        final BooleanSetting spam = this.spam;
        Objects.requireNonNull(spam);
        this.ticks = step.setVisible((Supplier<Boolean>)spam::getValue);
        this.wasUsingTrident = false;
        this.addSettings(this.allowNoWater, this.instant, this.spam, this.ticks);
    }
    
    @Override
    public void onEvent() {
        final EventListener tickEvent = TickEvent.getInstance().subscribe(new Listener<TickEvent>(event -> this.handleTridentSpam()));
        this.addEvents(tickEvent);
    }
    
    private void handleTridentSpam() {
        if (!this.spam.getValue() || TridentFlyModule.mc.field_1724 == null) {
            return;
        }
        final boolean isUsingTrident = TridentFlyModule.mc.field_1724.method_6115() && TridentFlyModule.mc.field_1724.method_6047().method_7909() == class_1802.field_8547;
        if (isUsingTrident && !this.wasUsingTrident) {
            this.wasUsingTrident = true;
        }
        else if (this.wasUsingTrident && !isUsingTrident) {
            this.wasUsingTrident = false;
        }
        else if (isUsingTrident && TridentFlyModule.mc.field_1724.method_6048() >= this.ticks.getValue()) {
            this.sendPacket(new class_2846(class_2846.class_2847.field_12974, class_2338.field_10980, class_2350.field_11033));
            TridentFlyModule.mc.field_1724.method_6075();
            this.wasUsingTrident = false;
        }
    }
    
    public boolean shouldAllowNoWater() {
        return this.isEnabled() && this.allowNoWater.getValue();
    }
    
    public boolean shouldInstantPullback() {
        return this.isEnabled() && this.instant.getValue();
    }
    
    private void sendPacket(final class_2846 packet) {
        if (TridentFlyModule.mc.method_1562() != null) {
            TridentFlyModule.mc.method_1562().method_52787((class_2596)packet);
        }
    }
    
    @Generated
    public static TridentFlyModule getInstance() {
        return TridentFlyModule.instance;
    }
    
    static {
        instance = new TridentFlyModule();
    }
}
