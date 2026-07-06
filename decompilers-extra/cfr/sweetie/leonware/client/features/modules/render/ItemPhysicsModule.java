/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1542
 *  net.minecraft.class_243
 *  net.minecraft.class_3532
 */
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

@ModuleRegister(name="Item Physics", category=Category.RENDER)
public class ItemPhysicsModule
extends Module {
    private static final ItemPhysicsModule instance = new ItemPhysicsModule();
    public final SliderSetting scale = new SliderSetting("Scale").value(Float.valueOf(2.0f)).range(0.1f, 5.0f).step(0.1f);
    public final BooleanSetting smartRotation = new BooleanSetting("SmartRotation").value(true);
    private final Map<Integer, PhysicsData> physicsMap = new HashMap<Integer, PhysicsData>();

    public ItemPhysicsModule() {
        this.addSettings(this.scale, this.smartRotation);
    }

    @Override
    public void onEvent() {
    }

    public PhysicsData getPhysics(class_1542 entity, float tickDelta) {
        PhysicsData data = this.physicsMap.computeIfAbsent(entity.method_5628(), id -> new PhysicsData());
        this.updatePhysics(data, entity.method_18798(), entity.method_24828(), tickDelta);
        if (entity.method_31481()) {
            this.physicsMap.remove(entity.method_5628());
        }
        return data;
    }

    private void updatePhysics(PhysicsData data, class_243 velocity, boolean onGround, float tickDelta) {
        float delta = Math.min(tickDelta, 1.0f);
        if (!onGround) {
            data.heightOffset = 0.0f;
            double speedXZ = Math.sqrt(velocity.field_1352 * velocity.field_1352 + velocity.field_1350 * velocity.field_1350);
            if (((Boolean)this.smartRotation.getValue()).booleanValue()) {
                double totalSpeed;
                if (speedXZ > 0.01) {
                    float targetRotY = (float)(class_3532.method_15349((double)velocity.field_1350, (double)velocity.field_1352) * 180.0 / Math.PI) - 90.0f;
                    data.rotationY = this.lerpAngle(data.rotationY, targetRotY, 0.2f * delta);
                }
                if ((totalSpeed = velocity.method_1033()) > 0.05) {
                    data.rotationX += (float)(totalSpeed * 15.0 * (double)delta);
                    data.rotationZ += (float)(speedXZ * 10.0 * (double)delta);
                }
                data.rotationX += (float)(velocity.field_1351 * 2.0 * (double)delta);
            }
            data.prevVelocity = velocity;
            data.wasOnGround = false;
        } else {
            if (!data.wasOnGround) {
                double impact;
                data.rotationX = (float)Math.round(data.rotationX / 90.0f) * 90.0f;
                data.rotationZ = (float)Math.round(data.rotationZ / 90.0f) * 90.0f;
                if (Math.abs(data.rotationX % 180.0f) < 45.0f) {
                    data.rotationX = 90.0f;
                }
                if ((impact = data.prevVelocity.method_1033()) > 0.3) {
                    data.bouncePhase = 0.0f;
                    data.bounceHeight = (float)Math.min(impact * (double)0.3f, (double)0.4f);
                }
                data.wasOnGround = true;
            }
            if (data.bounceHeight > 0.001f) {
                data.bouncePhase += delta * 0.5f;
                data.heightOffset = class_3532.method_15374((float)data.bouncePhase) * data.bounceHeight;
                data.bounceHeight *= 0.95f;
                if ((double)data.bouncePhase > Math.PI) {
                    data.bouncePhase = 0.0f;
                    data.bounceHeight *= 0.5f;
                }
            } else {
                data.heightOffset = 0.0f;
                data.rotationX = this.lerp(data.rotationX, 90.0f, 0.1f * delta);
            }
        }
    }

    private float lerp(float start, float end, float delta) {
        return start + (end - start) * delta;
    }

    private float lerpAngle(float start, float end, float delta) {
        float diff = (end - start + 540.0f) % 360.0f - 180.0f;
        return start + diff * delta;
    }

    @Generated
    public static ItemPhysicsModule getInstance() {
        return instance;
    }

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
}

