/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_2596
 *  net.minecraft.class_2828
 *  net.minecraft.class_2828$class_2829
 *  net.minecraft.class_2828$class_2830
 *  net.minecraft.class_2828$class_2831
 *  net.minecraft.class_2828$class_5911
 */
package sweetie.leonware.client.features.modules.movement;

import lombok.Generated;
import net.minecraft.class_2596;
import net.minecraft.class_2828;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.utils.other.NetworkUtil;
import sweetie.leonware.api.utils.other.TextUtil;
import sweetie.leonware.api.utils.player.GrimNoFallSystem;
import sweetie.leonware.inject.accessors.ILivingEntity;
import sweetie.leonware.inject.accessors.IPlayerMoveC2SPacket;

@ModuleRegister(name="No Fall Damage", category=Category.MOVEMENT)
public class NoFallDamageModule
extends Module {
    private static final NoFallDamageModule instance = new NoFallDamageModule();
    private final ModeSetting mode = new ModeSetting("Mode").values("\u0412\u0430\u043d\u0438\u043b\u044c\u043d\u044b\u0439", "\u0413\u0440\u0438\u043c 2.3.72", "\u0421\u0442\u043e\u043f \u0432\u0435\u043b\u043e\u0441\u0438\u0442\u0438").value("\u0412\u0430\u043d\u0438\u043b\u044c\u043d\u044b\u0439");
    private boolean started = false;
    private boolean skipTick = true;
    private boolean sentMessage = false;

    public NoFallDamageModule() {
        this.addSettings(this.mode);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.started = false;
        this.skipTick = true;
        this.sentMessage = false;
        if (this.mode.is("\u0413\u0440\u0438\u043c 2.3.72") && GrimNoFallSystem.getTakenFallDamage() < 1) {
            TextUtil.sendMessage("\u041f\u043e\u043b\u0443\u0447\u0438 \u0443\u0440\u043e\u043d \u043e\u0442 \u043f\u0430\u0434\u0435\u043d\u0438\u044f 5 \u0440\u0430\u0437 \u0434\u043b\u044f \u0430\u043a\u0442\u0438\u0432\u0430\u0446\u0438\u0438 \u0413\u0440\u0438\u043c \u0440\u0435\u0436\u0438\u043c\u0430");
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        this.started = false;
        this.skipTick = true;
        this.sentMessage = false;
    }

    @Override
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (NoFallDamageModule.mc.field_1724 == null || NoFallDamageModule.mc.field_1687 == null) {
                return;
            }
            if (this.mode.is("\u0413\u0440\u0438\u043c 2.3.72")) {
                if (!this.sentMessage && GrimNoFallSystem.getTakenFallDamage() < 1) {
                    TextUtil.sendMessage("\u041f\u043e\u043b\u0443\u0447\u0438 \u0443\u0440\u043e\u043d \u043e\u0442 \u043f\u0430\u0434\u0435\u043d\u0438\u044f 5 \u0440\u0430\u0437 \u0434\u043b\u044f \u0430\u043a\u0442\u0438\u0432\u0430\u0446\u0438\u0438 \u0413\u0440\u0438\u043c \u0440\u0435\u0436\u0438\u043c\u0430");
                    this.sentMessage = true;
                }
                if (!NoFallDamageModule.mc.field_1724.method_24828() && NoFallDamageModule.mc.field_1724.field_6017 > 3.0f && !this.started) {
                    this.started = true;
                }
                if (this.started) {
                    NoFallDamageModule.mc.field_1690.field_1903.method_23481(false);
                    if (NoFallDamageModule.mc.field_1724.method_24828()) {
                        if (this.skipTick) {
                            this.skipTick = false;
                            return;
                        }
                        NoFallDamageModule.mc.field_1724.method_6043();
                        ((ILivingEntity)NoFallDamageModule.mc.field_1724).setJumpingCooldown(10);
                        this.started = false;
                        this.skipTick = true;
                        GrimNoFallSystem.updateFallDamage();
                        TextUtil.sendMessage("\u041f\u0440\u043e\u0433\u0440\u0435\u0441\u0441: " + GrimNoFallSystem.getTakenFallDamage() + "/5");
                    }
                }
            } else if (this.mode.is("\u0421\u0442\u043e\u043f \u0432\u0435\u043b\u043e\u0441\u0438\u0442\u0438") && NoFallDamageModule.mc.field_1724.field_6017 > 0.0f && this.getDistanceToGround() > 4.0) {
                NoFallDamageModule.mc.field_1724.method_18800(0.0, 0.0, 0.0);
            }
        }));
        EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener<PacketEvent.PacketEventData>(event -> {
            if (!event.isSend()) {
                return;
            }
            class_2596<?> patt0$temp = event.packet();
            if (!(patt0$temp instanceof class_2828)) {
                return;
            }
            class_2828 packet = (class_2828)patt0$temp;
            boolean shouldSpoof = false;
            boolean spoofOnGround = false;
            if (this.mode.is("\u0412\u0430\u043d\u0438\u043b\u044c\u043d\u044b\u0439")) {
                shouldSpoof = true;
                spoofOnGround = true;
            } else if (this.mode.is("\u0413\u0440\u0438\u043c 2.3.72") && this.started) {
                shouldSpoof = true;
                spoofOnGround = false;
            }
            if (shouldSpoof) {
                PacketEvent.getInstance().setCancel(true);
                this.sendModifiedPacket(packet, spoofOnGround);
            }
        }));
        this.addEvents(updateEvent, packetEvent);
    }

    private void sendModifiedPacket(class_2828 original, boolean onGround) {
        class_2828.class_2829 newPacket;
        IPlayerMoveC2SPacket accessor = (IPlayerMoveC2SPacket)original;
        if (original instanceof class_2828.class_2829) {
            newPacket = new class_2828.class_2829(accessor.getX(), accessor.getY(), accessor.getZ(), onGround, accessor.isHorizontalCollision());
        } else if (original instanceof class_2828.class_2830) {
            newPacket = new class_2828.class_2830(accessor.getX(), accessor.getY(), accessor.getZ(), accessor.getYaw(), accessor.getPitch(), onGround, accessor.isHorizontalCollision());
        } else if (original instanceof class_2828.class_2831) {
            newPacket = new class_2828.class_2831(accessor.getYaw(), accessor.getPitch(), onGround, accessor.isHorizontalCollision());
        } else if (original instanceof class_2828.class_5911) {
            newPacket = new class_2828.class_5911(onGround, accessor.isHorizontalCollision());
        } else {
            return;
        }
        NetworkUtil.sendSilentPacket(newPacket);
    }

    private double getDistanceToGround() {
        if (NoFallDamageModule.mc.field_1724 == null || NoFallDamageModule.mc.field_1687 == null) {
            return 0.0;
        }
        for (int i = 0; i < 256; ++i) {
            if (NoFallDamageModule.mc.field_1687.method_18026(NoFallDamageModule.mc.field_1724.method_5829().method_989(0.0, (double)(-i), 0.0))) continue;
            return i;
        }
        return 256.0;
    }

    @Generated
    public static NoFallDamageModule getInstance() {
        return instance;
    }
}

