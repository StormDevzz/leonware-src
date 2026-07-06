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
import net.minecraft.class_2663;
import net.minecraft.class_746;
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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/combat/AutoTotemModule.class */
@ModuleRegister(name = "Auto Totem", category = Category.COMBAT)
public class AutoTotemModule extends Module {
    private static final AutoTotemModule instance = new AutoTotemModule();
    private final SliderSetting health = new SliderSetting("Health").value(Float.valueOf(5.0f)).range(0.0f, 20.0f).step(0.5f);
    private final MultiBooleanSetting options = new MultiBooleanSetting("Options").value(new BooleanSetting("Swap back").value((Boolean) true), new BooleanSetting("No ball switch").value((Boolean) false), new BooleanSetting("Save enchanted").value((Boolean) false));
    private final MultiBooleanSetting checks = new MultiBooleanSetting("Checks").value(new BooleanSetting("Absorption").value((Boolean) true), new BooleanSetting("Crystals").value((Boolean) true), new BooleanSetting("Falling").value((Boolean) false), new BooleanSetting("Elytra").value((Boolean) false), new BooleanSetting("TNT Minecarts").value((Boolean) true), new BooleanSetting("Mace").value((Boolean) true), new BooleanSetting("Save Life").value((Boolean) true));
    private final SliderSetting healthWithElytra = new SliderSetting("Health with elytra").value(Float.valueOf(10.0f)).range(0.0f, 20.0f).step(0.5f).setVisible(() -> {
        return Boolean.valueOf(this.checks.isEnabled("Elytra"));
    });
    private final TimerUtil timerUtil = new TimerUtil();
    private int oldItem = -1;
    private boolean totemIsUsed = false;
    private int nonEnchantedTotems = 0;
    private boolean isTotemPlaced = false;

    @Generated
    public static AutoTotemModule getInstance() {
        return instance;
    }

    /* JADX WARN: Type inference failed for: r1v12, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    public AutoTotemModule() {
        addSettings(this.health, this.options, this.checks, this.healthWithElytra);
    }

    @Override // sweetie.leonware.api.module.Module
    public void onDisable() {
        resetState();
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener(event -> {
            if (SlownessManager.isEnabled()) {
                return;
            }
            updateTotemCount();
            handleTotemSwapping();
        }));
        EventListener tickEvent = TickEvent.getInstance().subscribe(new Listener(event2 -> {
            if (SlownessManager.isEnabled()) {
                updateTotemCount();
                handleTotemSwapping();
            }
        }));
        EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener(event3 -> {
            handleTotemUsePacket(event3);
        }));
        addEvents(updateEvent, tickEvent, packetEvent);
    }

    private void updateTotemCount() {
        this.nonEnchantedTotems = InventoryUtil.countNonEnchantedTotems();
    }

    private void handleTotemSwapping() {
        if (this.timerUtil.finished(400L)) {
            if (shouldPlaceTotem()) {
                placeTotem();
            } else if (shouldReturnItem() && !canSwap()) {
                returnOriginalItem();
            }
        }
    }

    private boolean shouldPlaceTotem() {
        int slot = InventoryUtil.findBestTotemSlot(this.options.isEnabled("Save enchanted"));
        return (!canSwap() || slot == -1 || hasTotemInHand()) ? false : true;
    }

    private boolean shouldReturnItem() {
        return this.oldItem != -1 && this.options.isEnabled("Swap back");
    }

    private void placeTotem() {
        int slot = InventoryUtil.findBestTotemSlot(this.options.isEnabled("Save enchanted"));
        saveCurrentItem(slot);
        swapToOffhand(slot);
        this.isTotemPlaced = true;
        this.timerUtil.reset();
    }

    private void returnOriginalItem() {
        swapToOffhand(this.oldItem);
        this.isTotemPlaced = false;
        this.oldItem = -1;
        this.timerUtil.reset();
    }

    private void saveCurrentItem(int slot) {
        if (!mc.field_1724.method_6079().method_31574(class_1802.field_8162) && this.oldItem == -1) {
            this.oldItem = slot;
        }
    }

    private void handleTotemUsePacket(PacketEvent.PacketEventData event) {
        if (event.isReceive()) {
            class_2663 class_2663VarPacket = event.packet();
            if (class_2663VarPacket instanceof class_2663) {
                class_2663 packet = class_2663VarPacket;
                if (packet.method_11470() == 35 && packet.method_11469(mc.field_1687) == mc.field_1724) {
                    this.totemIsUsed = true;
                    this.isTotemPlaced = false;
                }
            }
        }
    }

    private boolean hasTotemInHand() {
        return (mc.field_1724.method_5998(class_1268.field_5808).method_31574(class_1802.field_8288) && isNotSaveEnchanted(mc.field_1724.method_5998(class_1268.field_5808))) || (mc.field_1724.method_6079().method_31574(class_1802.field_8288) && isNotSaveEnchanted(mc.field_1724.method_6079()));
    }

    private boolean isNotSaveEnchanted(class_1799 stack) {
        return (this.options.isEnabled("Save enchanted") && stack.method_7942() && this.nonEnchantedTotems > 0) ? false : true;
    }

    private boolean canSwap() {
        float healthWithAbsorption = calculateEffectiveHealth();
        float finalHealth = ((mc.field_1724.method_31548().method_5438(38).method_31574(class_1802.field_8833) && this.checks.isEnabled("Elytra")) ? this.healthWithElytra.getValue() : this.health.getValue()).floatValue();
        if (isOffhandProtectedItem()) {
            return false;
        }
        return isInDanger() || healthWithAbsorption <= finalHealth;
    }

    private float calculateEffectiveHealth() {
        float absorption = this.checks.isEnabled("Absorption") ? mc.field_1724.method_6067() : 0.0f;
        return mc.field_1724.method_6032() + absorption;
    }

    private boolean isOffhandProtectedItem() {
        return !shouldIgnoreProtection() && this.options.isEnabled("No ball switch") && mc.field_1724.method_6079().method_31574(class_1802.field_8575);
    }

    private boolean shouldIgnoreProtection() {
        return this.checks.isEnabled("Falling") && mc.field_1724.field_6017 > 5.0f;
    }

    private boolean checkCrystals() {
        if (!this.checks.isEnabled("Crystals")) {
            return false;
        }
        for (class_1297 entity : mc.field_1687.method_18112()) {
            if ((entity instanceof class_1511) && mc.field_1724.method_5739(entity) <= 6.0f) {
                return true;
            }
        }
        return false;
    }

    private boolean checkTntMinecarts() {
        if (!this.checks.isEnabled("TNT Minecarts")) {
            return false;
        }
        for (class_1297 entity : mc.field_1687.method_18112()) {
            if ((entity instanceof class_1701) && mc.field_1724.method_5739(entity) <= 7.0f) {
                return true;
            }
        }
        return false;
    }

    private boolean checkMace() {
        if (!this.checks.isEnabled("Mace")) {
            return false;
        }
        for (class_746 class_746Var : mc.field_1687.method_18112()) {
            if (class_746Var instanceof class_1657) {
                class_1657 player = (class_1657) class_746Var;
                if (class_746Var != mc.field_1724 && player.method_6047().method_31574(class_1802.field_49814) && mc.field_1724.method_5739(player) <= 10.0f) {
                    boolean falling = !player.method_24828() && player.field_6017 > 1.0f && player.method_18798().field_1351 < 0.0d;
                    if (falling || player.method_6128()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean checkSaveLife() {
        if (!this.checks.isEnabled("Save Life")) {
            return false;
        }
        for (class_1472 class_1472Var : mc.field_1687.method_18112()) {
            if (class_1472Var instanceof class_1472) {
                class_1472 sheep = class_1472Var;
                if (!sheep.method_6109() && mc.field_1724.method_5739(sheep) <= 1.5f) {
                    return true;
                }
            }
        }
        if (mc.field_1687.method_27983() == class_1937.field_25181) {
            for (class_746 class_746Var : mc.field_1687.method_18112()) {
                if (class_746Var instanceof class_1657) {
                    class_1657 player = (class_1657) class_746Var;
                    if (class_746Var == mc.field_1724) {
                        continue;
                    } else {
                        double diffX = Math.abs(mc.field_1724.method_23317() - player.method_23317());
                        double diffZ = Math.abs(mc.field_1724.method_23321() - player.method_23321());
                        double diffY = player.method_23318() - mc.field_1724.method_23318();
                        if (diffX <= 3.0d && diffZ <= 3.0d && diffY > 0.0d && diffY <= 5.0d) {
                            return true;
                        }
                        if (mc.field_1724.method_5739(player) <= 50.0f && player.method_23318() > mc.field_1724.method_23318() && hasAtLeastDiamondFullArmor(player)) {
                            boolean falling = !player.method_24828() && player.field_6017 > 1.0f && player.method_18798().field_1351 < 0.0d;
                            if (falling || player.method_6128()) {
                                return true;
                            }
                        }
                    }
                }
            }
            return false;
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
            if (!stack.method_7960() && isAtLeastDiamondPiece(stack)) {
                count++;
            }
        }
        return count >= 3;
    }

    private boolean checkFalling() {
        return this.checks.isEnabled("Falling") && !mc.field_1724.method_5799() && !mc.field_1724.method_6128() && mc.field_1724.field_6017 > 10.0f;
    }

    private void swapToOffhand(int slot) {
        if (SlownessManager.isEnabled()) {
            SlownessManager.applySlowness(10L, () -> {
                InventoryUtil.swapToOffhand(slot);
            });
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
        return checkCrystals() || checkFalling() || checkTntMinecarts() || checkMace() || checkSaveLife();
    }
}
