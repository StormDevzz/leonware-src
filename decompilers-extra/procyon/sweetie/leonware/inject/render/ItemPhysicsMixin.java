// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.inject.render;

import net.minecraft.class_7833;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.injection.Redirect;
import net.minecraft.class_4597;
import net.minecraft.class_4587;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.class_1542;
import org.spongepowered.asm.mixin.Unique;
import sweetie.leonware.client.features.modules.render.ItemPhysicsModule;
import net.minecraft.class_10039;
import java.util.WeakHashMap;
import net.minecraft.class_916;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ class_916.class })
public class ItemPhysicsMixin
{
    @Unique
    private static final WeakHashMap<class_10039, ItemPhysicsModule.PhysicsData> PHYSICS_MAP;
    @Unique
    private static final ThreadLocal<ItemPhysicsModule.PhysicsData> CURRENT_DATA;
    
    @Inject(method = { "updateRenderState(Lnet/minecraft/entity/ItemEntity;Lnet/minecraft/client/render/entity/state/ItemEntityRenderState;F)V" }, at = { @At("RETURN") })
    private void onUpdateRenderState(final class_1542 entity, final class_10039 state, final float tickDelta, final CallbackInfo ci) {
        if (ItemPhysicsModule.getInstance().isEnabled()) {
            state.field_53435 = 0.0f;
            final ItemPhysicsModule.PhysicsData data = ItemPhysicsModule.getInstance().getPhysics(entity, tickDelta);
            ItemPhysicsMixin.PHYSICS_MAP.put(state, data);
        }
    }
    
    @Inject(method = { "render(Lnet/minecraft/client/render/entity/state/ItemEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V" }, at = { @At("HEAD") })
    private void onRenderHead(final class_10039 state, final class_4587 matrices, final class_4597 vertexConsumers, final int light, final CallbackInfo ci) {
        if (ItemPhysicsModule.getInstance().isEnabled()) {
            ItemPhysicsMixin.CURRENT_DATA.set(ItemPhysicsMixin.PHYSICS_MAP.get(state));
        }
        else {
            ItemPhysicsMixin.CURRENT_DATA.remove();
        }
    }
    
    @Redirect(method = { "render(Lnet/minecraft/client/render/entity/state/ItemEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;translate(FFF)V", ordinal = 0))
    private void onTranslate(final class_4587 instance, final float x, final float y, final float z) {
        final ItemPhysicsModule.PhysicsData data = ItemPhysicsMixin.CURRENT_DATA.get();
        if (data != null) {
            instance.method_46416(x, data.heightOffset, z);
        }
        else {
            instance.method_46416(x, y, z);
        }
    }
    
    @Redirect(method = { "render(Lnet/minecraft/client/render/entity/state/ItemEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;multiply(Lorg/joml/Quaternionf;)V"))
    private void onMultiply(final class_4587 instance, final Quaternionf quaternion) {
        final ItemPhysicsModule.PhysicsData data = ItemPhysicsMixin.CURRENT_DATA.get();
        if (data != null) {
            instance.method_22907(class_7833.field_40716.rotationDegrees(data.rotationY));
            instance.method_22907(class_7833.field_40714.rotationDegrees(data.rotationX));
            instance.method_22907(class_7833.field_40718.rotationDegrees(data.rotationZ));
            final float sc = ItemPhysicsModule.getInstance().scale.getValue() * 0.5f;
            instance.method_22905(sc, sc, sc);
        }
        else {
            instance.method_22907(quaternion);
        }
    }
    
    static {
        PHYSICS_MAP = new WeakHashMap<class_10039, ItemPhysicsModule.PhysicsData>();
        CURRENT_DATA = new ThreadLocal<ItemPhysicsModule.PhysicsData>();
    }
}
