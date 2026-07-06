// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.movement;

import lombok.Generated;
import net.minecraft.class_2596;
import net.minecraft.class_1297;
import net.minecraft.class_2848;
import sweetie.leonware.api.utils.player.MoveUtil;
import net.minecraft.class_1802;
import net.minecraft.class_1304;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.TickEvent;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Elytra Recast", category = Category.MOVEMENT)
public class ElytraRecastModule extends Module
{
    private static final ElytraRecastModule instance;
    public final ModeSetting mode;
    public final SliderSetting groundDelay;
    public final BooleanSetting autoWalk;
    public final BooleanSetting exploit;
    private int groundTick;
    private int jump;
    private boolean boostedLastTick;
    
    public ElytraRecastModule() {
        this.mode = new ModeSetting("Mode").values("1.21.1", "1.21.4+").value("1.21.4+");
        this.groundDelay = new SliderSetting("\u0417\u0430\u0434\u0435\u0440\u0436\u043a\u0430").value(3.0f).range(0.0f, 10.0f).step(1.0f);
        this.autoWalk = new BooleanSetting("Auto Walk").value(false);
        this.exploit = new BooleanSetting("Exploit").value(false);
        this.groundTick = 0;
        this.jump = 0;
        this.boostedLastTick = false;
        this.addSettings(this.mode, this.groundDelay, this.autoWalk, this.exploit);
    }
    
    @Override
    public void onEvent() {
        final EventListener tickEvent = TickEvent.getInstance().subscribe(new Listener<TickEvent>(event -> this.onTick()));
        this.addEvents(tickEvent);
    }
    
    private void onTick() {
        if (ElytraRecastModule.mc.field_1724 == null || ElytraRecastModule.mc.field_1687 == null) {
            return;
        }
        if (this.autoWalk.getValue()) {
            ElytraRecastModule.mc.field_1690.field_1894.method_23481(true);
        }
        if (this.mode.is("1.21.1")) {
            this.handleOldLogic();
        }
        else {
            this.handleNewLogic();
        }
    }
    
    private void handleOldLogic() {
        if (ElytraRecastModule.mc.field_1724.method_24828()) {
            this.groundTick = 5;
            return;
        }
        if (this.groundTick > 0) {
            --this.groundTick;
            return;
        }
        if (ElytraRecastModule.mc.field_1724.method_6128()) {
            return;
        }
        if (!ElytraRecastModule.mc.field_1724.method_6118(class_1304.field_6174).method_31574(class_1802.field_8833)) {
            return;
        }
        if (!MoveUtil.isMoving()) {
            return;
        }
        if (ElytraRecastModule.mc.field_1724.method_6115()) {
            ElytraRecastModule.mc.field_1724.method_6021();
            return;
        }
        if (!ElytraRecastModule.mc.field_1724.method_6128()) {
            ElytraRecastModule.mc.field_1724.method_23669();
            ElytraRecastModule.mc.field_1724.field_3944.method_52787((class_2596)new class_2848((class_1297)ElytraRecastModule.mc.field_1724, class_2848.class_2849.field_12982));
        }
    }
    
    private void handleNewLogic() {
        if (ElytraRecastModule.mc.field_1724.method_24828()) {
            this.groundTick = this.groundDelay.getValue().intValue();
            this.jump = 0;
            return;
        }
        if (this.groundTick > 0) {
            --this.groundTick;
            return;
        }
        if (ElytraRecastModule.mc.field_1724.method_6128()) {
            this.jump = 0;
            return;
        }
        if (!ElytraRecastModule.mc.field_1724.method_6118(class_1304.field_6174).method_31574(class_1802.field_8833)) {
            this.jump = 0;
            return;
        }
        switch (this.jump) {
            case 0: {
                ElytraRecastModule.mc.field_1690.field_1903.method_23481(false);
                this.jump = 1;
                break;
            }
            case 1: {
                ElytraRecastModule.mc.field_1690.field_1903.method_23481(true);
                this.jump = 2;
                break;
            }
            case 2: {
                ElytraRecastModule.mc.field_1690.field_1903.method_23481(false);
                this.jump = 0;
                break;
            }
        }
    }
    
    @Override
    public void onDisable() {
        this.jump = 0;
        this.boostedLastTick = false;
        if (ElytraRecastModule.mc.field_1690 != null && ElytraRecastModule.mc.field_1724 != null) {
            ElytraRecastModule.mc.field_1690.field_1903.method_23481(false);
        }
        super.onDisable();
    }
    
    @Generated
    public static ElytraRecastModule getInstance() {
        return ElytraRecastModule.instance;
    }
    
    static {
        instance = new ElytraRecastModule();
    }
}
