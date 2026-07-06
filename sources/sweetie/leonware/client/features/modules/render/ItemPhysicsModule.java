package sweetie.leonware.client.features.modules.render;

import java.util.HashMap;
import java.util.Map;
import lombok.Generated;
import net.minecraft.class_1542;
import net.minecraft.class_243;
import net.minecraft.class_3532;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/ItemPhysicsModule.class */
@ModuleRegister(name = "Item Physics", category = Category.RENDER)
public class ItemPhysicsModule extends Module {
    private static final ItemPhysicsModule instance = new ItemPhysicsModule();
    public final SliderSetting scale = new SliderSetting("Scale").value(Float.valueOf(2.0f)).range(0.1f, 5.0f).step(0.1f);
    public final BooleanSetting smartRotation = new BooleanSetting("SmartRotation").value((Boolean) true);
    private final Map<Integer, PhysicsData> physicsMap = new HashMap();

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/ItemPhysicsModule$PhysicsData.class */
    public static class PhysicsData {
        public float rotationX = 0.0f;
        public float rotationY = 0.0f;
        public float rotationZ = 0.0f;
        public float heightOffset = 0.0f;
        public class_243 prevVelocity = class_243.field_1353;
        public boolean wasOnGround = false;
        public float bouncePhase = 0.0f;
        public float bounceHeight = 0.0f;
    }

    @Generated
    public static ItemPhysicsModule getInstance() {
        return instance;
    }

    public ItemPhysicsModule() {
        addSettings(this.scale, this.smartRotation);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
    }

    public PhysicsData getPhysics(class_1542 entity, float tickDelta) {
        PhysicsData data = this.physicsMap.computeIfAbsent(Integer.valueOf(entity.method_5628()), id -> {
            return new PhysicsData();
        });
        updatePhysics(data, entity.method_18798(), entity.method_24828(), tickDelta);
        if (entity.method_31481()) {
            this.physicsMap.remove(Integer.valueOf(entity.method_5628()));
        }
        return data;
    }

    private void updatePhysics(PhysicsData data, class_243 velocity, boolean onGround, float tickDelta) {
        float delta = Math.min(tickDelta, 1.0f);
        if (!onGround) {
            data.heightOffset = 0.0f;
            double speedXZ = Math.sqrt((velocity.field_1352 * velocity.field_1352) + (velocity.field_1350 * velocity.field_1350));
            if (this.smartRotation.getValue().booleanValue()) {
                if (speedXZ > 0.01d) {
                    float targetRotY = ((float) ((class_3532.method_15349(velocity.field_1350, velocity.field_1352) * 180.0d) / 3.141592653589793d)) - 90.0f;
                    data.rotationY = lerpAngle(data.rotationY, targetRotY, 0.2f * delta);
                }
                double totalSpeed = velocity.method_1033();
                if (totalSpeed > 0.05d) {
                    data.rotationX += (float) (totalSpeed * 15.0d * ((double) delta));
                    data.rotationZ += (float) (speedXZ * 10.0d * ((double) delta));
                }
                data.rotationX += (float) (velocity.field_1351 * 2.0d * ((double) delta));
            }
            data.prevVelocity = velocity;
            data.wasOnGround = false;
            return;
        }
        if (!data.wasOnGround) {
            data.rotationX = Math.round(data.rotationX / 90.0f) * 90.0f;
            data.rotationZ = Math.round(data.rotationZ / 90.0f) * 90.0f;
            if (Math.abs(data.rotationX % 180.0f) < 45.0f) {
                data.rotationX = 90.0f;
            }
            double impact = data.prevVelocity.method_1033();
            if (impact > 0.3d) {
                data.bouncePhase = 0.0f;
                data.bounceHeight = (float) Math.min(impact * 0.30000001192092896d, 0.4000000059604645d);
            }
            data.wasOnGround = true;
        }
        if (data.bounceHeight > 0.001f) {
            data.bouncePhase += delta * 0.5f;
            data.heightOffset = class_3532.method_15374(data.bouncePhase) * data.bounceHeight;
            data.bounceHeight *= 0.95f;
            if (data.bouncePhase > 3.141592653589793d) {
                data.bouncePhase = 0.0f;
                data.bounceHeight *= 0.5f;
                return;
            }
            return;
        }
        data.heightOffset = 0.0f;
        data.rotationX = lerp(data.rotationX, 90.0f, 0.1f * delta);
    }

    private float lerp(float start, float end, float delta) {
        return start + ((end - start) * delta);
    }

    private float lerpAngle(float start, float end, float delta) {
        float diff = (((end - start) + 540.0f) % 360.0f) - 180.0f;
        return start + (diff * delta);
    }
}
