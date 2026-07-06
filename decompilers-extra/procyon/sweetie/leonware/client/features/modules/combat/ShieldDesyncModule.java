// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.combat;

import net.minecraft.class_2886;
import net.minecraft.class_742;
import lombok.Generated;
import net.minecraft.class_1743;
import net.minecraft.class_1657;
import java.util.function.Predicate;
import net.minecraft.class_1297;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import net.minecraft.class_2596;
import sweetie.leonware.api.utils.other.NetworkUtil;
import net.minecraft.class_2350;
import net.minecraft.class_2338;
import net.minecraft.class_2846;
import sweetie.leonware.api.utils.rotation.manager.RotationManager;
import net.minecraft.class_1268;
import net.minecraft.class_1819;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Shield Desync", category = Category.COMBAT)
public class ShieldDesyncModule extends Module
{
    private static final ShieldDesyncModule instance;
    private final ModeSetting mode;
    private final BooleanSetting onlyWhenBlocking;
    
    public ShieldDesyncModule() {
        this.mode = new ModeSetting("Mode").values(Mode.HVH.toString(), Mode.AXE.toString()).value(Mode.HVH.toString());
        this.onlyWhenBlocking = new BooleanSetting("Only when blocking").value(true).setVisible(() -> this.mode.is(Mode.HVH.toString()));
        this.addSettings(this.mode, this.onlyWhenBlocking);
    }
    
    @Override
    public void onEvent() {
        final EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> this.onUpdate()));
        this.addEvents(updateEvent);
    }
    
    private void onUpdate() {
        if (ShieldDesyncModule.mc.field_1724 == null || ShieldDesyncModule.mc.field_1687 == null) {
            return;
        }
        final boolean hasShield = ShieldDesyncModule.mc.field_1724.method_6079().method_7909() instanceof class_1819 || ShieldDesyncModule.mc.field_1724.method_6047().method_7909() instanceof class_1819;
        if (!hasShield) {
            return;
        }
        final class_1268 shieldHand = (ShieldDesyncModule.mc.field_1724.method_6079().method_7909() instanceof class_1819) ? class_1268.field_5810 : class_1268.field_5808;
        if (this.mode.is(Mode.HVH.toString())) {
            this.handleHvHMode(shieldHand);
        }
        else if (this.mode.is(Mode.AXE.toString())) {
            this.handleAxeMode();
        }
    }
    
    private void handleHvHMode(final class_1268 shieldHand) {
        if (this.onlyWhenBlocking.getValue() && !ShieldDesyncModule.mc.field_1724.method_6115()) {
            return;
        }
        final RotationManager rotationManager = RotationManager.getInstance();
        final Rotation rotation = rotationManager.getRotation();
        final float yaw = (rotation != null) ? rotation.getYaw() : ShieldDesyncModule.mc.field_1724.method_36454();
        final float pitch = (rotation != null) ? rotation.getPitch() : ShieldDesyncModule.mc.field_1724.method_36455();
        ShieldDesyncModule.mc.field_1690.field_1904.method_23481(true);
        ShieldDesyncModule.mc.field_1690.field_1904.method_23481(false);
        ShieldDesyncModule.mc.field_1690.field_1904.method_23481(true);
        ShieldDesyncModule.mc.field_1724.method_6075();
        ShieldDesyncModule.mc.field_1690.field_1904.method_23481(true);
        NetworkUtil.sendPacket((class_2596<?>)new class_2846(class_2846.class_2847.field_12974, class_2338.field_10980, class_2350.field_11033));
        NetworkUtil.sendPacket(s -> new class_2886(shieldHand, s, yaw, pitch));
    }
    
    private void handleAxeMode() {
        if (!ShieldDesyncModule.mc.field_1724.method_6115()) {
            return;
        }
        final boolean enemyHasAxe = ShieldDesyncModule.mc.field_1687.method_18456().stream().filter(player -> player != ShieldDesyncModule.mc.field_1724).filter(player -> ShieldDesyncModule.mc.field_1724.method_5739((class_1297)player) <= 4.0).anyMatch(this::hasAxeInHand);
        if (enemyHasAxe) {
            ShieldDesyncModule.mc.field_1724.method_6075();
            NetworkUtil.sendPacket((class_2596<?>)new class_2846(class_2846.class_2847.field_12974, class_2338.field_10980, class_2350.field_11033));
        }
    }
    
    private boolean hasAxeInHand(final class_1657 player) {
        return player.method_6047().method_7909() instanceof class_1743 || player.method_6079().method_7909() instanceof class_1743;
    }
    
    @Generated
    public static ShieldDesyncModule getInstance() {
        return ShieldDesyncModule.instance;
    }
    
    static {
        instance = new ShieldDesyncModule();
    }
    
    private enum Mode
    {
        HVH("HvH"), 
        AXE("Axe");
        
        private final String name;
        
        private Mode(final String name) {
            this.name = name;
        }
        
        @Override
        public String toString() {
            return this.name;
        }
    }
}
