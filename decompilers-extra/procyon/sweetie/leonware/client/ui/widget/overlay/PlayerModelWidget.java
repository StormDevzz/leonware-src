// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.ui.widget.overlay;

import net.minecraft.class_898;
import net.minecraft.class_4597;
import net.minecraft.class_1297;
import net.minecraft.class_308;
import sweetie.leonware.api.utils.math.MathUtil;
import org.joml.Quaternionfc;
import net.minecraft.class_1309;
import net.minecraft.class_4587;
import org.joml.Quaternionf;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.client.ui.widget.Widget;

public class PlayerModelWidget extends Widget
{
    public final SliderSetting size;
    public final BooleanSetting lookAtMouse;
    public final BooleanSetting mimic;
    private final Quaternionf reusedQuat;
    private final Quaternionf reusedQuat2;
    
    public PlayerModelWidget() {
        super(40.0f, 80.0f);
        this.size = new SliderSetting("\u0420\u0430\u0437\u043c\u0435\u0440").value(40.0f).range(10.0f, 100.0f).step(1.0f);
        this.lookAtMouse = new BooleanSetting("\u0421\u043b\u0435\u0434\u0438\u0442\u044c \u0437\u0430 \u043c\u044b\u0448\u044c\u044e").value(false);
        this.mimic = new BooleanSetting("\u041f\u043e\u0432\u0442\u043e\u0440\u044f\u0442\u044c \u0434\u0432\u0438\u0436\u0435\u043d\u0438\u044f").value(true);
        this.reusedQuat = new Quaternionf();
        this.reusedQuat2 = new Quaternionf();
    }
    
    @Override
    public String getName() {
        return "PlayerModel";
    }
    
    @Override
    public void render(final class_4587 matrixStack) {
        if (PlayerModelWidget.mc.field_1724 == null) {
            return;
        }
        final float scale = this.size.getValue();
        final float x = this.getDraggable().getX();
        final float y = this.getDraggable().getY();
        this.getDraggable().setWidth(scale);
        this.getDraggable().setHeight(scale * 2.0f);
        final float renderX = x + scale / 2.0f;
        final float renderY = y + scale * 2.0f;
        float mouseX = 0.0f;
        float mouseY = 0.0f;
        if (this.lookAtMouse.getValue()) {
            final double mx = PlayerModelWidget.mc.field_1729.method_1603() * PlayerModelWidget.mc.method_22683().method_4486() / PlayerModelWidget.mc.method_22683().method_4480();
            final double my = PlayerModelWidget.mc.field_1729.method_1604() * PlayerModelWidget.mc.method_22683().method_4502() / PlayerModelWidget.mc.method_22683().method_4507();
            mouseX = renderX - (float)mx;
            mouseY = renderY - scale * 1.6f - (float)my;
        }
        this.drawEntity(matrixStack, renderX, renderY, scale, mouseX, mouseY, (class_1309)PlayerModelWidget.mc.field_1724);
    }
    
    private void drawEntity(final class_4587 matrixStack, final float x, final float y, final float size, final float mouseX, final float mouseY, final class_1309 entity) {
        final float f = (float)Math.atan(mouseX / 40.0f);
        final float g = (float)Math.atan(mouseY / 40.0f);
        matrixStack.method_22903();
        matrixStack.method_22904((double)x, (double)y, 50.0);
        matrixStack.method_22905(-size, size, size);
        final Quaternionf quaternionf = this.reusedQuat.identity().rotateZ(3.1415927f);
        final Quaternionf quaternionf2 = this.reusedQuat2.identity().rotateX(g * 20.0f * 0.017453292f);
        quaternionf.mul((Quaternionfc)quaternionf2);
        matrixStack.method_22907(quaternionf);
        final float prevBodyYaw = entity.field_6283;
        final float prevYaw = entity.method_36454();
        final float prevPitch = entity.method_36455();
        final float prevPrevHeadYaw = entity.field_6259;
        final float prevHeadYaw = entity.field_6241;
        final float prevLimbSpeed = entity.field_42108.method_48566();
        final float delta = PlayerModelWidget.mc.method_61966().method_60637(false);
        if (this.lookAtMouse.getValue()) {
            final float targetYaw = f * 40.0f;
            final float targetPitch = -g * 20.0f;
            entity.field_6283 = f * 20.0f;
            entity.method_36456(targetYaw);
            entity.method_36457(targetPitch);
            entity.field_6241 = targetYaw;
            entity.field_6259 = targetYaw;
        }
        else if (this.mimic.getValue()) {
            entity.field_6283 = MathUtil.interpolate(entity.field_6220, entity.field_6283, delta);
            entity.method_36456(MathUtil.interpolate(entity.field_5982, entity.method_36454(), delta));
            entity.method_36457(MathUtil.interpolate(entity.field_6004, entity.method_36455(), delta));
            entity.field_6241 = MathUtil.interpolate(entity.field_6259, entity.field_6241, delta);
            entity.field_6259 = entity.field_6241;
        }
        else {
            entity.method_36456(entity.field_6283 = 0.0f);
            entity.method_36457(0.0f);
            entity.field_6241 = 0.0f;
            entity.field_6259 = 0.0f;
            entity.field_42108.method_48567(0.0f);
        }
        class_308.method_24211();
        final class_898 entityRenderDispatcher = PlayerModelWidget.mc.method_1561();
        quaternionf2.conjugate();
        entityRenderDispatcher.method_3948(false);
        final class_4597.class_4598 immediate = PlayerModelWidget.mc.method_22940().method_23000();
        entityRenderDispatcher.method_62424((class_1297)entity, 0.0, 0.0, 0.0, 1.0f, matrixStack, (class_4597)immediate, 15728880);
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
