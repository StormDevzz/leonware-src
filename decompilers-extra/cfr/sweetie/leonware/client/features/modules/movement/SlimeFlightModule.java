/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1268
 *  net.minecraft.class_1802
 *  net.minecraft.class_2246
 *  net.minecraft.class_2338
 *  net.minecraft.class_239
 *  net.minecraft.class_3965
 */
package sweetie.leonware.client.features.modules.movement;

import lombok.Generated;
import net.minecraft.class_1268;
import net.minecraft.class_1802;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_239;
import net.minecraft.class_3965;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.move.MotionEvent;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.utils.player.InventoryUtil;

@ModuleRegister(name="Slime Flight", category=Category.MOVEMENT)
public class SlimeFlightModule
extends Module {
    private static final SlimeFlightModule instance = new SlimeFlightModule();
    private int slimeJumpCooldown = 0;
    private boolean startSetPitch = false;

    @Override
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (this.startSetPitch && SlimeFlightModule.mc.field_1724 != null) {
                SlimeFlightModule.mc.field_1724.method_36457(54.0f);
                this.startSetPitch = false;
            }
        }));
        EventListener motionEvent = MotionEvent.getInstance().subscribe(new Listener<MotionEvent.MotionEventData>(event -> this.handleSlimeLogic()));
        this.addEvents(updateEvent, motionEvent);
    }

    private void handleSlimeLogic() {
        if (SlimeFlightModule.mc.field_1724 == null || SlimeFlightModule.mc.field_1687 == null) {
            return;
        }
        class_2338 playerPos = SlimeFlightModule.mc.field_1724.method_24515();
        boolean slimeNearby = this.isSlimeAdjacent(playerPos);
        if (!slimeNearby || !SlimeFlightModule.mc.field_1724.field_5976 || SlimeFlightModule.mc.field_1724.method_18798().field_1351 <= -1.0) {
            return;
        }
        class_239 hitResult = SlimeFlightModule.mc.field_1765;
        if (!(hitResult instanceof class_3965)) {
            return;
        }
        class_3965 blockHitResult = (class_3965)hitResult;
        if (SlimeFlightModule.mc.field_1687.method_8320(blockHitResult.method_17777()).method_26215()) {
            return;
        }
        int slimeSlot = InventoryUtil.findItem(class_1802.field_8828, true);
        if (slimeSlot == -1) {
            return;
        }
        SlimeFlightModule.mc.field_1724.method_31548().field_7545 = slimeSlot;
        this.startSetPitch = true;
        if (SlimeFlightModule.mc.field_1761 != null) {
            SlimeFlightModule.mc.field_1761.method_2896(SlimeFlightModule.mc.field_1724, class_1268.field_5808, blockHitResult);
            SlimeFlightModule.mc.field_1724.method_6104(class_1268.field_5808);
        }
        if (this.slimeJumpCooldown >= 1) {
            SlimeFlightModule.mc.field_1724.method_18800(SlimeFlightModule.mc.field_1724.method_18798().field_1352, 0.63, SlimeFlightModule.mc.field_1724.method_18798().field_1350);
            this.slimeJumpCooldown = 0;
        } else {
            ++this.slimeJumpCooldown;
        }
    }

    private boolean isSlimeAdjacent(class_2338 pos) {
        return SlimeFlightModule.mc.field_1687.method_8320(pos.method_10078()).method_27852(class_2246.field_10030) || SlimeFlightModule.mc.field_1687.method_8320(pos.method_10067()).method_27852(class_2246.field_10030) || SlimeFlightModule.mc.field_1687.method_8320(pos.method_10095()).method_27852(class_2246.field_10030) || SlimeFlightModule.mc.field_1687.method_8320(pos.method_10072()).method_27852(class_2246.field_10030);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        this.slimeJumpCooldown = 0;
        this.startSetPitch = false;
    }

    @Generated
    public static SlimeFlightModule getInstance() {
        return instance;
    }
}

