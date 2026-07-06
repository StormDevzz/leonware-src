package sweetie.leonware.client.ui.widget.overlay;

import net.minecraft.class_1309;
import net.minecraft.class_308;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_898;
import org.joml.Quaternionf;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.utils.math.MathUtil;
import sweetie.leonware.client.ui.widget.Widget;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/ui/widget/overlay/PlayerModelWidget.class */
public class PlayerModelWidget extends Widget {
    public final SliderSetting size;
    public final BooleanSetting lookAtMouse;
    public final BooleanSetting mimic;
    private final Quaternionf reusedQuat;
    private final Quaternionf reusedQuat2;

    public PlayerModelWidget() {
        super(40.0f, 80.0f);
        this.size = new SliderSetting("Размер").value(Float.valueOf(40.0f)).range(10.0f, 100.0f).step(1.0f);
        this.lookAtMouse = new BooleanSetting("Следить за мышью").value((Boolean) false);
        this.mimic = new BooleanSetting("Повторять движения").value((Boolean) true);
        this.reusedQuat = new Quaternionf();
        this.reusedQuat2 = new Quaternionf();
    }

    @Override // sweetie.leonware.client.ui.widget.Widget
    public String getName() {
        return "PlayerModel";
    }

    @Override // sweetie.leonware.api.system.interfaces.IRenderer
    public void render(class_4587 matrixStack) {
        if (mc.field_1724 == null) {
            return;
        }
        float scale = this.size.getValue().floatValue();
        float x = getDraggable().getX();
        float y = getDraggable().getY();
        getDraggable().setWidth(scale);
        getDraggable().setHeight(scale * 2.0f);
        float renderX = x + (scale / 2.0f);
        float renderY = y + (scale * 2.0f);
        float mouseX = 0.0f;
        float mouseY = 0.0f;
        if (this.lookAtMouse.getValue().booleanValue()) {
            double mx = (mc.field_1729.method_1603() * ((double) mc.method_22683().method_4486())) / ((double) mc.method_22683().method_4480());
            double my = (mc.field_1729.method_1604() * ((double) mc.method_22683().method_4502())) / ((double) mc.method_22683().method_4507());
            mouseX = renderX - ((float) mx);
            mouseY = (renderY - (scale * 1.6f)) - ((float) my);
        }
        drawEntity(matrixStack, renderX, renderY, scale, mouseX, mouseY, mc.field_1724);
    }

    private void drawEntity(class_4587 matrixStack, float x, float y, float size, float mouseX, float mouseY, class_1309 entity) {
        float f = (float) Math.atan(mouseX / 40.0f);
        float g = (float) Math.atan(mouseY / 40.0f);
        matrixStack.method_22903();
        matrixStack.method_22904(x, y, 50.0d);
        matrixStack.method_22905(-size, size, size);
        Quaternionf quaternionf = this.reusedQuat.identity().rotateZ(3.1415927f);
        Quaternionf quaternionf2 = this.reusedQuat2.identity().rotateX(g * 20.0f * 0.017453292f);
        quaternionf.mul(quaternionf2);
        matrixStack.method_22907(quaternionf);
        float prevBodyYaw = entity.field_6283;
        float prevYaw = entity.method_36454();
        float prevPitch = entity.method_36455();
        float prevPrevHeadYaw = entity.field_6259;
        float prevHeadYaw = entity.field_6241;
        float prevLimbSpeed = entity.field_42108.method_48566();
        float delta = mc.method_61966().method_60637(false);
        if (this.lookAtMouse.getValue().booleanValue()) {
            float targetYaw = f * 40.0f;
            float targetPitch = (-g) * 20.0f;
            entity.field_6283 = f * 20.0f;
            entity.method_36456(targetYaw);
            entity.method_36457(targetPitch);
            entity.field_6241 = targetYaw;
            entity.field_6259 = targetYaw;
        } else if (this.mimic.getValue().booleanValue()) {
            entity.field_6283 = MathUtil.interpolate(entity.field_6220, entity.field_6283, delta);
            entity.method_36456(MathUtil.interpolate(entity.field_5982, entity.method_36454(), delta));
            entity.method_36457(MathUtil.interpolate(entity.field_6004, entity.method_36455(), delta));
            entity.field_6241 = MathUtil.interpolate(entity.field_6259, entity.field_6241, delta);
            entity.field_6259 = entity.field_6241;
        } else {
            entity.field_6283 = 0.0f;
            entity.method_36456(0.0f);
            entity.method_36457(0.0f);
            entity.field_6241 = 0.0f;
            entity.field_6259 = 0.0f;
            entity.field_42108.method_48567(0.0f);
        }
        class_308.method_24211();
        class_898 entityRenderDispatcher = mc.method_1561();
        quaternionf2.conjugate();
        entityRenderDispatcher.method_3948(false);
        class_4597.class_4598 immediate = mc.method_22940().method_23000();
        entityRenderDispatcher.method_62424(entity, 0.0d, 0.0d, 0.0d, 1.0f, matrixStack, immediate, 15728880);
        immediate.method_22993();
        entityRenderDispatcher.method_3948(true);
        entity.field_6283 = prevBodyYaw;
        entity.method_36456(prevYaw);
        entity.method_36457(prevPitch);
        entity.field_6259 = prevPrevHeadYaw;
        entity.field_6241 = prevHeadYaw;
        entity.field_42108.method_48567(prevLimbSpeed);
        matrixStack.method_22909();
        class_308.method_24210();
    }
}
