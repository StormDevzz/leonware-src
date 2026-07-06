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
import sweetie.leonware.inject.accessors.IPlayerMoveC2SPacket;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/movement/NoFallDamageModule.class */
@ModuleRegister(name = "No Fall Damage", category = Category.MOVEMENT)
public class NoFallDamageModule extends Module {
    private static final NoFallDamageModule instance = new NoFallDamageModule();
    private final ModeSetting mode = new ModeSetting("Mode").values("Ванильный", "Грим 2.3.72", "Стоп велосити").value("Ванильный");
    private boolean started = false;
    private boolean skipTick = true;
    private boolean sentMessage = false;

    @Generated
    public static NoFallDamageModule getInstance() {
        return instance;
    }

    public NoFallDamageModule() {
        addSettings(this.mode);
    }

    @Override // sweetie.leonware.api.module.Module
    public void onEnable() {
        super.onEnable();
        this.started = false;
        this.skipTick = true;
        this.sentMessage = false;
        if (this.mode.is("Грим 2.3.72") && GrimNoFallSystem.getTakenFallDamage() < 1) {
            TextUtil.sendMessage("Получи урон от падения 5 раз для активации Грим режима");
        }
    }

    @Override // sweetie.leonware.api.module.Module
    public void onDisable() {
        super.onDisable();
        this.started = false;
        this.skipTick = true;
        this.sentMessage = false;
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener(event -> {
            if (mc.field_1724 == null || mc.field_1687 == null) {
                return;
            }
            if (this.mode.is("Грим 2.3.72")) {
                if (!this.sentMessage && GrimNoFallSystem.getTakenFallDamage() < 1) {
                    TextUtil.sendMessage("Получи урон от падения 5 раз для активации Грим режима");
                    this.sentMessage = true;
                }
                if (!mc.field_1724.method_24828() && mc.field_1724.field_6017 > 3.0f && !this.started) {
                    this.started = true;
                }
                if (this.started) {
                    mc.field_1690.field_1903.method_23481(false);
                    if (mc.field_1724.method_24828()) {
                        if (this.skipTick) {
                            this.skipTick = false;
                            return;
                        }
                        mc.field_1724.method_6043();
                        mc.field_1724.setJumpingCooldown(10);
                        this.started = false;
                        this.skipTick = true;
                        GrimNoFallSystem.updateFallDamage();
                        TextUtil.sendMessage("Прогресс: " + GrimNoFallSystem.getTakenFallDamage() + "/5");
                        return;
                    }
                    return;
                }
                return;
            }
            if (this.mode.is("Стоп велосити") && mc.field_1724.field_6017 > 0.0f && getDistanceToGround() > 4.0d) {
                mc.field_1724.method_18800(0.0d, 0.0d, 0.0d);
            }
        }));
        EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener(event2 -> {
            if (event2.isSend()) {
                class_2596<?> class_2596VarPacket = event2.packet();
                if (class_2596VarPacket instanceof class_2828) {
                    class_2828 packet = (class_2828) class_2596VarPacket;
                    boolean shouldSpoof = false;
                    boolean spoofOnGround = false;
                    if (this.mode.is("Ванильный")) {
                        shouldSpoof = true;
                        spoofOnGround = true;
                    } else if (this.mode.is("Грим 2.3.72") && this.started) {
                        shouldSpoof = true;
                        spoofOnGround = false;
                    }
                    if (shouldSpoof) {
                        PacketEvent.getInstance().setCancel(true);
                        sendModifiedPacket(packet, spoofOnGround);
                    }
                }
            }
        }));
        addEvents(updateEvent, packetEvent);
    }

    private void sendModifiedPacket(class_2828 original, boolean onGround) {
        class_2828.class_2829 class_5911Var;
        IPlayerMoveC2SPacket accessor = (IPlayerMoveC2SPacket) original;
        if (original instanceof class_2828.class_2829) {
            class_5911Var = new class_2828.class_2829(accessor.getX(), accessor.getY(), accessor.getZ(), onGround, accessor.isHorizontalCollision());
        } else if (original instanceof class_2828.class_2830) {
            class_5911Var = new class_2828.class_2830(accessor.getX(), accessor.getY(), accessor.getZ(), accessor.getYaw(), accessor.getPitch(), onGround, accessor.isHorizontalCollision());
        } else if (original instanceof class_2828.class_2831) {
            class_5911Var = new class_2828.class_2831(accessor.getYaw(), accessor.getPitch(), onGround, accessor.isHorizontalCollision());
        } else if (original instanceof class_2828.class_5911) {
            class_5911Var = new class_2828.class_5911(onGround, accessor.isHorizontalCollision());
        } else {
            return;
        }
        NetworkUtil.sendSilentPacket((class_2596<?>) class_5911Var);
    }

    private double getDistanceToGround() {
        if (mc.field_1724 == null || mc.field_1687 == null) {
            return 0.0d;
        }
        for (int i = 0; i < 256; i++) {
            if (!mc.field_1687.method_18026(mc.field_1724.method_5829().method_989(0.0d, -i, 0.0d))) {
                return i;
            }
        }
        return 256.0d;
    }
}
