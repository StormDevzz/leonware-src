// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.render;

import lombok.Generated;
import net.minecraft.class_3532;
import net.minecraft.class_243;
import net.minecraft.class_1542;
import sweetie.leonware.api.module.setting.Setting;
import java.util.HashMap;
import java.util.Map;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Item Physics", category = Category.RENDER)
public class ItemPhysicsModule extends Module
{
    private static final ItemPhysicsModule instance;
    public final SliderSetting scale;
    public final BooleanSetting smartRotation;
    private final Map<Integer, PhysicsData> physicsMap;
    
    public ItemPhysicsModule() {
        this.scale = new SliderSetting("Scale").value(2.0f).range(0.1f, 5.0f).step(0.1f);
        this.smartRotation = new BooleanSetting("SmartRotation").value(true);
        this.physicsMap = new HashMap<Integer, PhysicsData>();
        this.addSettings(this.scale, this.smartRotation);
    }
    
    @Override
    public void onEvent() {
    }
    
    public PhysicsData getPhysics(final class_1542 entity, final float tickDelta) {
        final PhysicsData data = this.physicsMap.computeIfAbsent(entity.method_5628(), id -> new PhysicsData());
        this.updatePhysics(data, entity.method_18798(), entity.method_24828(), tickDelta);
        if (entity.method_31481()) {
            this.physicsMap.remove(entity.method_5628());
        }
        return data;
    }
    
    private void updatePhysics(final PhysicsData data, final class_243 velocity, final boolean onGround, final float tickDelta) {
        final float delta = Math.min(tickDelta, 1.0f);
        if (!onGround) {
            data.heightOffset = 0.0f;
            final double speedXZ = Math.sqrt(velocity.field_1352 * velocity.field_1352 + velocity.field_1350 * velocity.field_1350);
            if (this.smartRotation.getValue()) {
                if (speedXZ > 0.01) {
                    final float targetRotY = (float)(class_3532.method_15349(velocity.field_1350, velocity.field_1352) * 180.0 / 3.141592653589793) - 90.0f;
                    data.rotationY = this.lerpAngle(data.rotationY, targetRotY, 0.2f * delta);
                }
                final double totalSpeed = velocity.method_1033();
                if (totalSpeed > 0.05) {
                    data.rotationX += (float)(totalSpeed * 15.0 * delta);
                    data.rotationZ += (float)(speedXZ * 10.0 * delta);
                }
                data.rotationX += (float)(velocity.field_1351 * 2.0 * delta);
            }
            data.prevVelocity = velocity;
            data.wasOnGround = false;
        }
        else {
            if (!data.wasOnGround) {
                data.rotationX = Math.round(data.rotationX / 90.0f) * 90.0f;
                data.rotationZ = Math.round(data.rotationZ / 90.0f) * 90.0f;
                if (Math.abs(data.rotationX % 180.0f) < 45.0f) {
                    data.rotationX = 90.0f;
                }
                final double impact = data.prevVelocity.method_1033();
                if (impact > 0.3) {
                    data.bouncePhase = 0.0f;
                    data.bounceHeight = (float)Math.min(impact * 0.30000001192092896, 0.4000000059604645);
                }
                data.wasOnGround = true;
            }
            if (data.bounceHeight > 0.001f) {
                data.bouncePhase += delta * 0.5f;
                data.heightOffset = class_3532.method_15374(data.bouncePhase) * data.bounceHeight;
                data.bounceHeight *= 0.95f;
                if (data.bouncePhase > 3.141592653589793) {
                    data.bouncePhase = 0.0f;
                    data.bounceHeight *= 0.5f;
                }
            }
            else {
                data.heightOffset = 0.0f;
                data.rotationX = this.lerp(data.rotationX, 90.0f, 0.1f * delta);
            }
        }
    }
    
    private float lerp(final float start, final float end, final float delta) {
        return start + (end - start) * delta;
    }
    
    private float lerpAngle(final float start, final float end, final float delta) {
        final float diff = (end - start + 540.0f) % 360.0f - 180.0f;
        return start + diff * delta;
    }
    
    @Generated
    public static ItemPhysicsModule getInstance() {
        return ItemPhysicsModule.instance;
    }
    
    static {
        instance = new ItemPhysicsModule();
    }
    
    public static class PhysicsData
    {
        public float rotationX;
        public float rotationY;
        public float rotationZ;
        public float heightOffset;
        public class_243 prevVelocity;
        public boolean wasOnGround;
        public float bouncePhase;
        public float bounceHeight;
        
        public PhysicsData() {
            this.rotationX = 0.0f;
            this.rotationY = 0.0f;
            this.rotationZ = 0.0f;
            this.heightOffset = 0.0f;
            this.prevVelocity = class_243.field_1353;
            this.wasOnGround = false;
            this.bouncePhase = 0.0f;
            this.bounceHeight = 0.0f;
        }
    }
}
