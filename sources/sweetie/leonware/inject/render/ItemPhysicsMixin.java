package sweetie.leonware.inject.render;

import java.util.WeakHashMap;
import net.minecraft.class_10039;
import net.minecraft.class_1542;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_7833;
import net.minecraft.class_916;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sweetie.leonware.client.features.modules.render.ItemPhysicsModule;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/inject/render/ItemPhysicsMixin.class */
@Mixin({class_916.class})
public class ItemPhysicsMixin {

    @Unique
    private static final WeakHashMap<class_10039, ItemPhysicsModule.PhysicsData> PHYSICS_MAP = new WeakHashMap<>();

    @Unique
    private static final ThreadLocal<ItemPhysicsModule.PhysicsData> CURRENT_DATA = new ThreadLocal<>();

    @Inject(method = {"updateRenderState(Lnet/minecraft/entity/ItemEntity;Lnet/minecraft/client/render/entity/state/ItemEntityRenderState;F)V"}, at = {@At("RETURN")})
    private void onUpdateRenderState(class_1542 entity, class_10039 state, float tickDelta, CallbackInfo ci) {
        if (ItemPhysicsModule.getInstance().isEnabled()) {
            state.field_53435 = 0.0f;
            ItemPhysicsModule.PhysicsData data = ItemPhysicsModule.getInstance().getPhysics(entity, tickDelta);
            PHYSICS_MAP.put(state, data);
        }
    }

    @Inject(method = {"render(Lnet/minecraft/client/render/entity/state/ItemEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"}, at = {@At("HEAD")})
    private void onRenderHead(class_10039 state, class_4587 matrices, class_4597 vertexConsumers, int light, CallbackInfo ci) {
        if (ItemPhysicsModule.getInstance().isEnabled()) {
            CURRENT_DATA.set(PHYSICS_MAP.get(state));
        } else {
            CURRENT_DATA.remove();
        }
    }

    @Redirect(method = {"render(Lnet/minecraft/client/render/entity/state/ItemEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;translate(FFF)V", ordinal = 0))
    private void onTranslate(class_4587 instance, float x, float y, float z) {
        ItemPhysicsModule.PhysicsData data = CURRENT_DATA.get();
        if (data != null) {
            instance.method_46416(x, data.heightOffset, z);
        } else {
            instance.method_46416(x, y, z);
        }
    }

    @Redirect(method = {"render(Lnet/minecraft/client/render/entity/state/ItemEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;multiply(Lorg/joml/Quaternionf;)V"))
    private void onMultiply(class_4587 instance, Quaternionf quaternion) {
        ItemPhysicsModule.PhysicsData data = CURRENT_DATA.get();
        if (data != null) {
            instance.method_22907(class_7833.field_40716.rotationDegrees(data.rotationY));
            instance.method_22907(class_7833.field_40714.rotationDegrees(data.rotationX));
            instance.method_22907(class_7833.field_40718.rotationDegrees(data.rotationZ));
            float sc = ItemPhysicsModule.getInstance().scale.getValue().floatValue() * 0.5f;
            instance.method_22905(sc, sc, sc);
            return;
        }
        instance.method_22907(quaternion);
    }
}
