package sweetie.leonware.client.features.modules.movement.fly.modes;

import java.util.function.Supplier;
import lombok.Generated;
import net.minecraft.class_243;
import sweetie.leonware.api.event.events.player.move.MotionEvent;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.utils.math.MathUtil;
import sweetie.leonware.api.utils.math.TimerUtil;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.rotation.manager.RotationManager;
import sweetie.leonware.api.utils.rotation.manager.RotationPlan;
import sweetie.leonware.api.utils.rotation.manager.RotationStrategy;
import sweetie.leonware.api.utils.rotation.rotations.SmoothRotation;
import sweetie.leonware.api.utils.task.TaskPriority;
import sweetie.leonware.client.features.modules.movement.fly.FlightMode;
import sweetie.leonware.client.features.modules.movement.fly.FlightModule;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/movement/fly/modes/FlightGrim.class */
public class FlightGrim extends FlightMode {
    public BypassType bypassType;
    private final FlightModule module;
    private TimerUtil ticks = new TimerUtil();
    private long speedRampStartTime = 0;
    private boolean isSpeedRamping = false;
    private final ModeSetting grimType = new ModeSetting("Grim mode").value((Enum<?>) BypassType.VERTICAL_ELYTRA).values(BypassType.values()).onAction2(() -> {
        BypassType bypassType;
        switch (getGrimType().getValue()) {
            case "Glide elytra":
                bypassType = BypassType.GLIDE_ELYTRA;
                break;
            default:
                bypassType = BypassType.VERTICAL_ELYTRA;
                break;
        }
        this.bypassType = bypassType;
    });

    @Override // sweetie.leonware.api.system.backend.Choice, sweetie.leonware.api.module.setting.ModeSetting.NamedChoice
    public String getName() {
        return "Grim";
    }

    @Generated
    public ModeSetting getGrimType() {
        return this.grimType;
    }

    /* JADX WARN: Type inference failed for: r1v6, types: [sweetie.leonware.api.module.setting.ModeSetting] */
    public FlightGrim(Supplier<Boolean> condition, FlightModule module) {
        this.grimType.setVisible(condition);
        this.module = module;
        addSettings(this.grimType);
    }

    @Override // sweetie.leonware.client.features.modules.movement.fly.FlightMode
    public void onUpdate() {
        if (this.bypassType == BypassType.GLIDE_ELYTRA) {
            return;
        }
        if (mc.field_1724.method_6128() && ((mc.field_1724.method_18798().field_1351 > 0.08d || mc.field_1724.field_6017 > 0.1f) && mc.field_1724.method_18798().field_1352 <= 0.01d && mc.field_1724.method_18798().field_1350 <= 0.01d)) {
            RotationStrategy rotationStrategy = new RotationStrategy(new SmoothRotation(), true);
            mc.field_1724.method_18798().field_1350 = 0.0d;
            mc.field_1724.method_18798().field_1352 = 0.0d;
            RotationManager rotationManager = RotationManager.getInstance();
            Rotation rotation = rotationManager.getRotation();
            RotationPlan configurable = rotationManager.getCurrentRotationPlan();
            float pitch = configurable != null ? rotation.getPitch() : mc.field_1724.method_36455();
            boolean validPitch = mc.field_1724.method_36455() >= -30.0f && mc.field_1724.method_36455() <= 30.0f;
            if (!this.isSpeedRamping) {
                this.speedRampStartTime = System.currentTimeMillis();
                this.isSpeedRamping = true;
            }
            long elapsed = System.currentTimeMillis() - this.speedRampStartTime;
            float progress = Math.min(elapsed / 100, 1.0f);
            double currentBaseSpeed = 0.05d * ((double) progress);
            float normalizedPitch = pitch / 90.0f;
            double speedAddition = 0.06d * ((double) normalizedPitch) * ((double) normalizedPitch);
            double superKuniMan = currentBaseSpeed + speedAddition;
            mc.field_1724.method_18798().field_1351 += superKuniMan;
            if (mc.field_1724.method_18798().field_1351 >= 1.11d) {
                mc.field_1724.method_18798().field_1351 = 1.11d;
            }
            if (!validPitch) {
                RotationManager.getInstance().addRotation(new Rotation(mc.field_1724.method_36454(), 0.0f), rotationStrategy, TaskPriority.NORMAL, this.module);
                return;
            }
            return;
        }
        this.isSpeedRamping = false;
    }

    @Override // sweetie.leonware.client.features.modules.movement.fly.FlightMode
    public void onMotion(MotionEvent.MotionEventData event) {
        if (this.bypassType == BypassType.VERTICAL_ELYTRA || !mc.field_1724.method_6128()) {
            return;
        }
        class_243 pos = mc.field_1724.method_19538();
        float yaw = mc.field_1724.method_36454();
        double forward = 6.087d;
        double motion = MathUtil.getEntityBPS(mc.field_1724);
        float doni = (mc.method_1562().method_45734() == null || !mc.method_1562().method_45734().field_3761.contains("reallyworld")) ? 52.0f : 48.0f;
        if (motion >= doni) {
            forward = 0.0d;
        }
        double dx = (-Math.sin(Math.toRadians(yaw))) * forward;
        double dz = Math.cos(Math.toRadians(yaw)) * forward;
        mc.field_1724.method_18800(dx * ((double) MathUtil.randomInRange(1.1f, 1.21f)), mc.field_1724.method_18798().field_1351 - 0.019999999552965164d, dz * ((double) MathUtil.randomInRange(1.1f, 1.21f)));
        if (this.ticks.finished(50L)) {
            mc.field_1724.method_5814(pos.field_1352 + dx, pos.field_1351, pos.field_1350 + dz);
            this.ticks.reset();
        }
        mc.field_1724.method_18800(dx * ((double) MathUtil.randomInRange(1.1f, 1.21f)), mc.field_1724.method_18798().field_1351 + 0.01600000075995922d, dz * ((double) MathUtil.randomInRange(1.1f, 1.21f)));
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/movement/fly/modes/FlightGrim$BypassType.class */
    public enum BypassType implements ModeSetting.NamedChoice {
        VERTICAL_ELYTRA("Vertical elytra"),
        GLIDE_ELYTRA("Glide elytra");

        private final String name;

        BypassType(String name) {
            this.name = name;
        }

        @Override // sweetie.leonware.api.module.setting.ModeSetting.NamedChoice
        public String getName() {
            return this.name;
        }
    }
}
