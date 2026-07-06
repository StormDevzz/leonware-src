/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_2338
 *  net.minecraft.class_2350
 */
package sweetie.leonware.client.features.modules.player;

import lombok.Generated;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;

@ModuleRegister(name="Edge Jump", category=Category.PLAYER)
public class EdgeJumpModule
extends Module {
    private static final EdgeJumpModule instance = new EdgeJumpModule();
    private final BooleanSetting autoSprint = new BooleanSetting("\u0410\u0432\u0442\u043e \u0421\u043f\u0440\u0438\u043d\u0442").value(true);
    private final BooleanSetting onlyWhenMoving = new BooleanSetting("\u0422\u043e\u043b\u044c\u043a\u043e \u041f\u0440\u0438 \u0414\u0432\u0438\u0436\u0435\u043d\u0438\u0438").value(true);
    private final SliderSetting jumpDistance = new SliderSetting("\u0414\u0438\u0441\u0442\u0430\u043d\u0446\u0438\u044f \u041f\u0440\u044b\u0436\u043a\u0430").value(Float.valueOf(1.0f)).range(0.5f, 2.0f).step(0.1f);
    private int cooldown = 0;

    public EdgeJumpModule() {
        this.addSettings(this.autoSprint, this.onlyWhenMoving, this.jumpDistance);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.cooldown = 0;
    }

    @Override
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            boolean isAtEdge;
            if (EdgeJumpModule.mc.field_1724 == null || EdgeJumpModule.mc.field_1687 == null) {
                return;
            }
            if (this.cooldown > 0) {
                --this.cooldown;
                return;
            }
            if (!EdgeJumpModule.mc.field_1724.method_24828() || EdgeJumpModule.mc.field_1724.method_5715()) {
                return;
            }
            if (((Boolean)this.onlyWhenMoving.getValue()).booleanValue() && !this.isMoving()) {
                return;
            }
            class_2338 pos = EdgeJumpModule.mc.field_1724.method_24515();
            class_2350 facing = EdgeJumpModule.mc.field_1724.method_5735();
            class_2338 forwardPos = pos.method_10093(facing);
            boolean bl = isAtEdge = EdgeJumpModule.mc.field_1687.method_8320(pos.method_10074()).method_26215() || EdgeJumpModule.mc.field_1687.method_8320(forwardPos.method_10074()).method_26215();
            if (isAtEdge) {
                boolean canLand;
                class_2338 jumpTarget = pos.method_10079(facing, (int)((Float)this.jumpDistance.getValue()).floatValue());
                boolean bl2 = canLand = !EdgeJumpModule.mc.field_1687.method_8320(jumpTarget.method_10074()).method_26215();
                if (canLand) {
                    if (((Boolean)this.autoSprint.getValue()).booleanValue()) {
                        EdgeJumpModule.mc.field_1724.method_5728(true);
                    }
                    EdgeJumpModule.mc.field_1724.method_6043();
                    this.cooldown = 10;
                }
            }
        }));
        this.addEvents(updateEvent);
    }

    private boolean isMoving() {
        return EdgeJumpModule.mc.field_1724.field_3913.field_3905 != 0.0f || EdgeJumpModule.mc.field_1724.field_3913.field_3907 != 0.0f;
    }

    @Generated
    public static EdgeJumpModule getInstance() {
        return instance;
    }
}

