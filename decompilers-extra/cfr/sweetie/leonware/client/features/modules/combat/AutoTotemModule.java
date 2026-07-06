/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1268
 *  net.minecraft.class_1297
 *  net.minecraft.class_1472
 *  net.minecraft.class_1511
 *  net.minecraft.class_1657
 *  net.minecraft.class_1701
 *  net.minecraft.class_1792
 *  net.minecraft.class_1799
 *  net.minecraft.class_1802
 *  net.minecraft.class_1937
 *  net.minecraft.class_2596
 *  net.minecraft.class_2663
 */
package sweetie.leonware.client.features.modules.combat;

import lombok.Generated;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1472;
import net.minecraft.class_1511;
import net.minecraft.class_1657;
import net.minecraft.class_1701;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1937;
import net.minecraft.class_2596;
import net.minecraft.class_2663;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.event.events.client.TickEvent;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.MultiBooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.utils.math.TimerUtil;
import sweetie.leonware.api.utils.other.SlownessManager;
import sweetie.leonware.api.utils.player.InventoryUtil;

@ModuleRegister(name="Auto Totem", category=Category.COMBAT)
public class AutoTotemModule
extends Module {
    private static final AutoTotemModule instance = new AutoTotemModule();
    private final SliderSetting health = new SliderSetting("Health").value(Float.valueOf(5.0f)).range(0.0f, 20.0f).step(0.5f);
    private final MultiBooleanSetting options = new MultiBooleanSetting("Options").value(new BooleanSetting("Swap back").value(true), new BooleanSetting("No ball switch").value(false), new BooleanSetting("Save enchanted").value(false));
    private final MultiBooleanSetting checks = new MultiBooleanSetting("Checks").value(new BooleanSetting("Absorption").value(true), new BooleanSetting("Crystals").value(true), new BooleanSetting("Falling").value(false), new BooleanSetting("Elytra").value(false), new BooleanSetting("TNT Minecarts").value(true), new BooleanSetting("Mace").value(true), new BooleanSetting("Save Life").value(true));
    private final SliderSetting healthWithElytra = new SliderSetting("Health with elytra").value(Float.valueOf(10.0f)).range(0.0f, 20.0f).step(0.5f).setVisible(() -> this.checks.isEnabled("Elytra"));
    private final TimerUtil timerUtil = new TimerUtil();
    private int oldItem = -1;
    private boolean totemIsUsed = false;
    private int nonEnchantedTotems = 0;
    private boolean isTotemPlaced = false;

    public AutoTotemModule() {
        this.addSettings(this.health, this.options, this.checks, this.healthWithElytra);
    }

    @Override
    public void onDisable() {
        this.resetState();
    }

    @Override
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (SlownessManager.isEnabled()) {
                return;
            }
            this.updateTotemCount();
            this.handleTotemSwapping();
        }));
        EventListener tickEvent = TickEvent.getInstance().subscribe(new Listener<TickEvent>(event -> {
            if (!SlownessManager.isEnabled()) {
                return;
            }
            this.updateTotemCount();
            this.handleTotemSwapping();
        }));
        EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener<PacketEvent.PacketEventData>(event -> this.handleTotemUsePacket((PacketEvent.PacketEventData)event)));
        this.addEvents(updateEvent, tickEvent, packetEvent);
    }

    private void updateTotemCount() {
        this.nonEnchantedTotems = InventoryUtil.countNonEnchantedTotems();
    }

    private void handleTotemSwapping() {
        if (!this.timerUtil.finished(400L)) {
            return;
        }
        if (this.shouldPlaceTotem()) {
            this.placeTotem();
            return;
        }
        if (this.shouldReturnItem() && !this.canSwap()) {
            this.returnOriginalItem();
        }
    }

    private boolean shouldPlaceTotem() {
        int slot = InventoryUtil.findBestTotemSlot(this.options.isEnabled("Save enchanted"));
        return this.canSwap() && slot != -1 && !this.hasTotemInHand();
    }

    private boolean shouldReturnItem() {
        return this.oldItem != -1 && this.options.isEnabled("Swap back");
    }

    private void placeTotem() {
        int slot = InventoryUtil.findBestTotemSlot(this.options.isEnabled("Save enchanted"));
        this.saveCurrentItem(slot);
        this.swapToOffhand(slot);
        this.isTotemPlaced = true;
        this.timerUtil.reset();
    }

    private void returnOriginalItem() {
        this.swapToOffhand(this.oldItem);
        this.isTotemPlaced = false;
        this.oldItem = -1;
        this.timerUtil.reset();
    }

    private void saveCurrentItem(int slot) {
        if (!AutoTotemModule.mc.field_1724.method_6079().method_31574(class_1802.field_8162) && this.oldItem == -1) {
            this.oldItem = slot;
        }
    }

    private void handleTotemUsePacket(PacketEvent.PacketEventData event) {
        class_2663 packet;
        if (!event.isReceive()) {
            return;
        }
        class_2596<?> class_25962 = event.packet();
        if (class_25962 instanceof class_2663 && (packet = (class_2663)class_25962).method_11470() == 35 && packet.method_11469((class_1937)AutoTotemModule.mc.field_1687) == AutoTotemModule.mc.field_1724) {
            this.totemIsUsed = true;
            this.isTotemPlaced = false;
        }
    }

    private boolean hasTotemInHand() {
        return AutoTotemModule.mc.field_1724.method_5998(class_1268.field_5808).method_31574(class_1802.field_8288) && this.isNotSaveEnchanted(AutoTotemModule.mc.field_1724.method_5998(class_1268.field_5808)) || AutoTotemModule.mc.field_1724.method_6079().method_31574(class_1802.field_8288) && this.isNotSaveEnchanted(AutoTotemModule.mc.field_1724.method_6079());
    }

    private boolean isNotSaveEnchanted(class_1799 stack) {
        return !this.options.isEnabled("Save enchanted") || !stack.method_7942() || this.nonEnchantedTotems <= 0;
    }

    private boolean canSwap() {
        float healthWithAbsorption = this.calculateEffectiveHealth();
        float finalHealth = (AutoTotemModule.mc.field_1724.method_31548().method_5438(38).method_31574(class_1802.field_8833) && this.checks.isEnabled("Elytra") ? (Float)this.healthWithElytra.getValue() : (Float)this.health.getValue()).floatValue();
        if (this.isOffhandProtectedItem()) {
            return false;
        }
        if (this.isInDanger()) {
            return true;
        }
        return healthWithAbsorption <= finalHealth;
    }

    private float calculateEffectiveHealth() {
        float absorption = this.checks.isEnabled("Absorption") ? AutoTotemModule.mc.field_1724.method_6067() : 0.0f;
        return AutoTotemModule.mc.field_1724.method_6032() + absorption;
    }

    private boolean isOffhandProtectedItem() {
        if (this.shouldIgnoreProtection()) {
            return false;
        }
        return this.options.isEnabled("No ball switch") && AutoTotemModule.mc.field_1724.method_6079().method_31574(class_1802.field_8575);
    }

    private boolean shouldIgnoreProtection() {
        return this.checks.isEnabled("Falling") && AutoTotemModule.mc.field_1724.field_6017 > 5.0f;
    }

    private boolean checkCrystals() {
        if (!this.checks.isEnabled("Crystals")) {
            return false;
        }
        for (class_1297 entity : AutoTotemModule.mc.field_1687.method_18112()) {
            if (!(entity instanceof class_1511) || !(AutoTotemModule.mc.field_1724.method_5739(entity) <= 6.0f)) continue;
            return true;
        }
        return false;
    }

    private boolean checkTntMinecarts() {
        if (!this.checks.isEnabled("TNT Minecarts")) {
            return false;
        }
        for (class_1297 entity : AutoTotemModule.mc.field_1687.method_18112()) {
            if (!(entity instanceof class_1701) || !(AutoTotemModule.mc.field_1724.method_5739(entity) <= 7.0f)) continue;
            return true;
        }
        return false;
    }

    private boolean checkMace() {
        if (!this.checks.isEnabled("Mace")) {
            return false;
        }
        for (class_1297 entity : AutoTotemModule.mc.field_1687.method_18112()) {
            boolean falling;
            if (!(entity instanceof class_1657)) continue;
            class_1657 player = (class_1657)entity;
            if (entity == AutoTotemModule.mc.field_1724 || !player.method_6047().method_31574(class_1802.field_49814) || AutoTotemModule.mc.field_1724.method_5739((class_1297)player) > 10.0f) continue;
            boolean bl = falling = !player.method_24828() && player.field_6017 > 1.0f && player.method_18798().field_1351 < 0.0;
            if (!falling && !player.method_6128()) continue;
            return true;
        }
        return false;
    }

    private boolean checkSaveLife() {
        if (!this.checks.isEnabled("Save Life")) {
            return false;
        }
        for (class_1297 entity : AutoTotemModule.mc.field_1687.method_18112()) {
            class_1472 sheep;
            if (!(entity instanceof class_1472) || (sheep = (class_1472)entity).method_6109() || !(AutoTotemModule.mc.field_1724.method_5739((class_1297)sheep) <= 1.5f)) continue;
            return true;
        }
        if (AutoTotemModule.mc.field_1687.method_27983() == class_1937.field_25181) {
            for (class_1297 entity : AutoTotemModule.mc.field_1687.method_18112()) {
                boolean falling;
                if (!(entity instanceof class_1657)) continue;
                class_1657 player = (class_1657)entity;
                if (entity == AutoTotemModule.mc.field_1724) continue;
                double diffX = Math.abs(AutoTotemModule.mc.field_1724.method_23317() - player.method_23317());
                double diffZ = Math.abs(AutoTotemModule.mc.field_1724.method_23321() - player.method_23321());
                double diffY = player.method_23318() - AutoTotemModule.mc.field_1724.method_23318();
                if (diffX <= 3.0 && diffZ <= 3.0 && diffY > 0.0 && diffY <= 5.0) {
                    return true;
                }
                if (!(AutoTotemModule.mc.field_1724.method_5739((class_1297)player) <= 50.0f) || !(player.method_23318() > AutoTotemModule.mc.field_1724.method_23318()) || !this.hasAtLeastDiamondFullArmor(player)) continue;
                boolean bl = falling = !player.method_24828() && player.field_6017 > 1.0f && player.method_18798().field_1351 < 0.0;
                if (!falling && !player.method_6128()) continue;
                return true;
            }
        }
        return false;
    }

    private boolean isAtLeastDiamondPiece(class_1799 stack) {
        class_1792 item = stack.method_7909();
        return item == class_1802.field_8805 || item == class_1802.field_8058 || item == class_1802.field_8348 || item == class_1802.field_8285 || item == class_1802.field_22027 || item == class_1802.field_22028 || item == class_1802.field_22029 || item == class_1802.field_22030 || item == class_1802.field_8833;
    }

    private boolean hasAtLeastDiamondFullArmor(class_1657 player) {
        int count = 0;
        for (class_1799 stack : player.method_5661()) {
            if (stack.method_7960() || !this.isAtLeastDiamondPiece(stack)) continue;
            ++count;
        }
        return count >= 3;
    }

    private boolean checkFalling() {
        if (!this.checks.isEnabled("Falling")) {
            return false;
        }
        if (AutoTotemModule.mc.field_1724.method_5799()) {
            return false;
        }
        if (AutoTotemModule.mc.field_1724.method_6128()) {
            return false;
        }
        return AutoTotemModule.mc.field_1724.field_6017 > 10.0f;
    }

    private void swapToOffhand(int slot) {
        if (SlownessManager.isEnabled()) {
            SlownessManager.applySlowness(10L, () -> InventoryUtil.swapToOffhand(slot));
        } else {
            InventoryUtil.swapToOffhand(slot);
        }
    }

    private void resetState() {
        this.oldItem = -1;
        this.totemIsUsed = false;
        this.isTotemPlaced = false;
    }

    private boolean isInDanger() {
        return this.checkCrystals() || this.checkFalling() || this.checkTntMinecarts() || this.checkMace() || this.checkSaveLife();
    }

    @Generated
    public static AutoTotemModule getInstance() {
        return instance;
    }
}

