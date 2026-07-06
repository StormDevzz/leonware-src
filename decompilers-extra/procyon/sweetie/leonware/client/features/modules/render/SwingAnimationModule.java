// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.render;

import lombok.Generated;
import net.minecraft.class_4608;
import sweetie.leonware.client.features.modules.combat.AuraModule;
import net.minecraft.class_811;
import net.minecraft.class_1309;
import net.minecraft.class_1764;
import net.minecraft.class_1802;
import net.minecraft.class_4597;
import net.minecraft.class_1799;
import net.minecraft.class_1268;
import net.minecraft.class_742;
import net.minecraft.class_7833;
import net.minecraft.class_3532;
import net.minecraft.class_1306;
import net.minecraft.class_4587;
import sweetie.leonware.api.module.setting.Setting;
import java.util.function.Supplier;
import java.util.Objects;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Swing Animation", category = Category.RENDER)
public class SwingAnimationModule extends Module
{
    private static final SwingAnimationModule instance;
    public final ModeSetting mode;
    private final BooleanSetting auraOnly;
    public final SliderSetting strength;
    public final BooleanSetting slow;
    public final SliderSetting speed;
    public final BooleanSetting item360;
    public final BooleanSetting item360NoTarget;
    public final BooleanSetting item360Left;
    public final ModeSetting item360LeftAxis;
    public final BooleanSetting item360Right;
    public final ModeSetting item360RightAxis;
    
    public SwingAnimationModule() {
        this.mode = new ModeSetting("Mode").value("Mode 1").values("Mode 1", "Mode 2", "Mode 3", "Mode 4", "Mode 5", "Mode 6", "Mode 7");
        this.auraOnly = new BooleanSetting("Only with aura").value(false);
        this.strength = new SliderSetting("Strength").value(20.0f).range(1.0f, 75.0f).step(0.1f).setVisible(() -> !this.mode.is("Mode 5") && !this.mode.is("Mode 7"));
        this.slow = new BooleanSetting("Slow").value(false);
        final SliderSetting step = new SliderSetting("Speed").value(12.0f).range(1.0f, 50.0f).step(1.0f);
        final BooleanSetting slow = this.slow;
        Objects.requireNonNull(slow);
        this.speed = step.setVisible((Supplier<Boolean>)slow::getValue);
        this.item360 = new BooleanSetting("Item 360").value(false);
        final BooleanSetting value = new BooleanSetting("\u0415\u0441\u043b\u0438 \u043d\u0435\u0442 \u0442\u0430\u0440\u0433\u0435\u0442\u0430").value(true);
        final BooleanSetting item360 = this.item360;
        Objects.requireNonNull(item360);
        this.item360NoTarget = value.setVisible((Supplier<Boolean>)item360::getValue);
        final BooleanSetting value2 = new BooleanSetting("\u041b\u0435\u0432\u0430\u044f \u0440\u0443\u043a\u0430").value(true);
        final BooleanSetting item361 = this.item360;
        Objects.requireNonNull(item361);
        this.item360Left = value2.setVisible((Supplier<Boolean>)item361::getValue);
        this.item360LeftAxis = new ModeSetting("\u041e\u0441\u044c \u043b\u0435\u0432\u043e\u0439 \u0440\u0443\u043a\u0438").value("X").values("X", "Y", "Z").setVisible(() -> this.item360.getValue() && this.item360Left.getValue());
        final BooleanSetting value3 = new BooleanSetting("\u041f\u0440\u0430\u0432\u0430\u044f \u0440\u0443\u043a\u0430").value(false);
        final BooleanSetting item362 = this.item360;
        Objects.requireNonNull(item362);
        this.item360Right = value3.setVisible((Supplier<Boolean>)item362::getValue);
        this.item360RightAxis = new ModeSetting("\u041e\u0441\u044c \u043f\u0440\u0430\u0432\u043e\u0439 \u0440\u0443\u043a\u0438").value("X").values("X", "Y", "Z").setVisible(() -> this.item360.getValue() && this.item360Right.getValue());
        this.addSettings(this.mode, this.auraOnly, this.strength, this.slow, this.speed, this.item360, this.item360NoTarget, this.item360Left, this.item360LeftAxis, this.item360Right, this.item360RightAxis);
    }
    
    @Override
    public void onEvent() {
    }
    
    private void handleSwordAnim(final class_4587 matrices, final float swingProgress, final float equipProgress, final class_1306 arm) {
        final float g = class_3532.method_15374(class_3532.method_15355(swingProgress) * 3.1415927f);
        final float anim = (float)Math.sin(swingProgress * 1.5707963267948966 * 2.0);
        final float isLeft = (arm == class_1306.field_6182) ? -1.0f : 1.0f;
        final String s = this.mode.getValue();
        switch (s) {
            case "Mode 1": {
                this.applyEquipOffset(matrices, arm, 0.0f);
                this.applyCustomSwingOffset(matrices, arm, swingProgress, this.strength.getValue() / 20.0f);
                break;
            }
            case "Mode 2": {
                this.applyEquipOffset(matrices, arm, 0.0f);
                matrices.method_22907(class_7833.field_40714.rotationDegrees(50.0f));
                matrices.method_22907(class_7833.field_40716.rotationDegrees(isLeft * -60.0f));
                matrices.method_22907(class_7833.field_40718.rotationDegrees(isLeft * (110.0f + this.strength.getValue() * g)));
                break;
            }
            case "Mode 3": {
                this.applyEquipOffset(matrices, arm, 0.0f);
                matrices.method_22907(class_7833.field_40714.rotationDegrees(50.0f));
                matrices.method_22907(class_7833.field_40716.rotationDegrees(isLeft * (-30.0f * (1.0f - g) - 30.0f + (this.strength.getValue() - 20.0f) * g)));
                matrices.method_22907(class_7833.field_40718.rotationDegrees(isLeft * 110.0f));
                break;
            }
            case "Mode 4": {
                this.applyEquipOffset(matrices, arm, 0.0f);
                matrices.method_22907(class_7833.field_40716.rotationDegrees(isLeft * 90.0f));
                matrices.method_22907(class_7833.field_40718.rotationDegrees(isLeft * -30.0f));
                matrices.method_22907(class_7833.field_40714.rotationDegrees(-90.0f - this.strength.getValue() * anim + 10.0f));
                break;
            }
            case "Mode 5": {
                final float rotation = swingProgress * -360.0f;
                this.applyEquipOffset(matrices, arm, 0.0f);
                matrices.method_22907(class_7833.field_40714.rotationDegrees(rotation));
                break;
            }
            case "Mode 6": {
                this.applyEquipOffset(matrices, arm, 0.0f);
                matrices.method_46416(isLeft * -0.14142136f, 0.08f, 0.14142136f);
                matrices.method_22907(class_7833.field_40714.rotationDegrees(-102.25f));
                matrices.method_22907(class_7833.field_40716.rotationDegrees(isLeft * 13.365f));
                matrices.method_22907(class_7833.field_40718.rotationDegrees(isLeft * 78.05f));
                final float blockSwing = class_3532.method_15374(swingProgress * swingProgress * 3.1415927f);
                matrices.method_22907(class_7833.field_40714.rotationDegrees(blockSwing * -(this.strength.getValue() / 2.0f)));
                matrices.method_46416(0.0f, blockSwing * -0.1f * (this.strength.getValue() / 20.0f), blockSwing * -0.05f * (this.strength.getValue() / 20.0f));
                break;
            }
            case "Mode 7": {
                this.celestialMode(matrices, swingProgress, equipProgress, isLeft);
                break;
            }
        }
    }
    
    private void celestialMode(final class_4587 matrices, final float swingProgress, final float equipProgress, final float isLeft) {
        final float eq = equipProgress / 3.0f;
        matrices.method_46416(isLeft * 0.56f, -0.44f - 0.6f * eq, -0.72f);
        matrices.method_22907(class_7833.field_40716.rotationDegrees(isLeft * 45.0f));
        final float f = class_3532.method_15374(swingProgress * swingProgress * 3.1415927f);
        final float f2 = class_3532.method_15374(class_3532.method_15355(swingProgress) * 3.1415927f);
        matrices.method_22907(class_7833.field_40714.rotationDegrees(f * -20.0f));
        matrices.method_22907(class_7833.field_40718.rotationDegrees(isLeft * f2 * -20.0f));
        matrices.method_22907(class_7833.field_40714.rotationDegrees(f2 * -80.0f));
        matrices.method_46416(isLeft * 0.4f, 0.2f, 0.2f);
        matrices.method_46416(-0.5f * isLeft, 0.08f, 0.0f);
        matrices.method_22907(class_7833.field_40716.rotationDegrees(isLeft * 20.0f));
        matrices.method_22907(class_7833.field_40714.rotationDegrees(-80.0f));
        matrices.method_22907(class_7833.field_40716.rotationDegrees(isLeft * 20.0f));
    }
    
    private void applyCustomSwingOffset(final class_4587 matrices, final class_1306 arm, final float swingProgress, final float strengthModifier) {
        final int i = (arm == class_1306.field_6183) ? 1 : -1;
        final float f = class_3532.method_15374(swingProgress * swingProgress * 3.1415927f);
        final float g = class_3532.method_15374(class_3532.method_15355(swingProgress) * 3.1415927f);
        matrices.method_22907(class_7833.field_40716.rotationDegrees(i * (45.0f + f * -20.0f * strengthModifier)));
        matrices.method_22907(class_7833.field_40718.rotationDegrees(i * g * -20.0f * strengthModifier));
        matrices.method_22907(class_7833.field_40714.rotationDegrees(g * -80.0f * strengthModifier));
        matrices.method_22907(class_7833.field_40716.rotationDegrees(i * -45.0f));
    }
    
    public void handleRenderItem(final class_742 player, final float tickDelta, final float pitch, final class_1268 hand, final float swingProgress, final class_1799 item, final float equipProgress, final class_4587 matrices, final class_4597 vertexConsumers, final int light) {
        if (!player.method_31550()) {
            final boolean bl = hand == class_1268.field_5808;
            final class_1306 arm = bl ? player.method_6068() : player.method_6068().method_5928();
            matrices.method_22903();
            if (item.method_31574(class_1802.field_8399)) {
                final boolean bl2 = class_1764.method_7781(item);
                final boolean bl3 = arm == class_1306.field_6183;
                final int i = bl3 ? 1 : -1;
                if (player.method_6115() && player.method_6014() > 0 && player.method_6058() == hand) {
                    this.applyEquipOffset(matrices, arm, equipProgress);
                    matrices.method_46416(i * -0.4785682f, -0.094387f, 0.05731531f);
                    matrices.method_22907(class_7833.field_40714.rotationDegrees(-11.935f));
                    matrices.method_22907(class_7833.field_40716.rotationDegrees(i * 65.3f));
                    matrices.method_22907(class_7833.field_40718.rotationDegrees(i * -9.785f));
                    final float f = item.method_7935((class_1309)SwingAnimationModule.mc.field_1724) - (SwingAnimationModule.mc.field_1724.method_6014() - tickDelta + 1.0f);
                    float g = f / class_1764.method_7775(item, (class_1309)SwingAnimationModule.mc.field_1724);
                    if (g > 1.0f) {
                        g = 1.0f;
                    }
                    if (g > 0.1f) {
                        final float h = class_3532.method_15374((f - 0.1f) * 1.3f);
                        final float j = g - 0.1f;
                        final float k = h * j;
                        matrices.method_46416(k * 0.0f, k * 0.004f, k * 0.0f);
                    }
                    matrices.method_46416(g * 0.0f, g * 0.0f, g * 0.04f);
                    matrices.method_22905(1.0f, 1.0f, 1.0f + g * 0.2f);
                    matrices.method_22907(class_7833.field_40715.rotationDegrees(i * 45.0f));
                }
                else {
                    final float fx = -0.4f * class_3532.method_15374(class_3532.method_15355(swingProgress) * 3.1415927f);
                    final float gx = 0.2f * class_3532.method_15374(class_3532.method_15355(swingProgress) * 6.2831855f);
                    final float h = -0.2f * class_3532.method_15374(swingProgress * 3.1415927f);
                    matrices.method_46416(i * fx, gx, h);
                    this.applyEquipOffset(matrices, arm, equipProgress);
                    this.applySwingOffset(matrices, arm, swingProgress);
                    if (bl2 && swingProgress < 0.001f && bl) {
                        matrices.method_46416(i * -0.641864f, 0.0f, 0.0f);
                        matrices.method_22907(class_7833.field_40716.rotationDegrees(i * 10.0f));
                    }
                }
                this.applyItem360(matrices, arm);
                this.renderItem((class_1309)player, item, bl3 ? class_811.field_4322 : class_811.field_4321, !bl3, matrices, vertexConsumers, light);
            }
            else {
                final boolean bl2 = arm == class_1306.field_6183;
                final ViewModelModule viewModel = ViewModelModule.getInstance();
                if (viewModel.isEnabled()) {
                    if (bl2) {
                        matrices.method_22904((double)viewModel.rightX.getValue(), (double)viewModel.rightY.getValue(), (double)viewModel.rightZ.getValue());
                    }
                    else {
                        matrices.method_22904(-viewModel.leftX.getValue(), (double)viewModel.leftY.getValue(), (double)viewModel.leftZ.getValue());
                    }
                }
                if (player.method_6115() && player.method_6014() > 0 && player.method_6058() == hand) {
                    final int l = bl2 ? 1 : -1;
                    switch (item.method_7976()) {
                        case field_8952:
                        case field_8949: {
                            this.applyEquipOffset(matrices, arm, equipProgress);
                            break;
                        }
                        case field_8950:
                        case field_8946: {
                            this.applyEatOrDrinkTransformation(matrices, tickDelta, arm, item);
                            this.applyEquipOffset(matrices, arm, equipProgress);
                            break;
                        }
                        case field_8953: {
                            this.applyEquipOffset(matrices, arm, equipProgress);
                            matrices.method_46416(l * -0.2785682f, 0.18344387f, 0.15731531f);
                            matrices.method_22907(class_7833.field_40714.rotationDegrees(-13.935f));
                            matrices.method_22907(class_7833.field_40716.rotationDegrees(l * 35.3f));
                            matrices.method_22907(class_7833.field_40718.rotationDegrees(l * -9.785f));
                            final float mx = item.method_7935((class_1309)SwingAnimationModule.mc.field_1724) - (SwingAnimationModule.mc.field_1724.method_6014() - tickDelta + 1.0f);
                            float fxx = mx / 20.0f;
                            fxx = (fxx * fxx + fxx * 2.0f) / 3.0f;
                            if (fxx > 1.0f) {
                                fxx = 1.0f;
                            }
                            if (fxx > 0.1f) {
                                final float gx2 = class_3532.method_15374((mx - 0.1f) * 1.3f);
                                final float h2 = fxx - 0.1f;
                                final float m = gx2 * h2;
                                matrices.method_46416(m * 0.0f, m * 0.004f, m * 0.0f);
                            }
                            matrices.method_46416(fxx * 0.0f, fxx * 0.0f, fxx * 0.04f);
                            matrices.method_22905(1.0f, 1.0f, 1.0f + fxx * 0.2f);
                            matrices.method_22907(class_7833.field_40715.rotationDegrees(l * 45.0f));
                            break;
                        }
                        case field_8951: {
                            this.applyEquipOffset(matrices, arm, equipProgress);
                            matrices.method_46416(l * -0.5f, 0.7f, 0.1f);
                            matrices.method_22907(class_7833.field_40714.rotationDegrees(-55.0f));
                            matrices.method_22907(class_7833.field_40716.rotationDegrees(l * 35.3f));
                            matrices.method_22907(class_7833.field_40718.rotationDegrees(l * -9.785f));
                            final float m2 = item.method_7935((class_1309)SwingAnimationModule.mc.field_1724) - (SwingAnimationModule.mc.field_1724.method_6014() - tickDelta + 1.0f);
                            float fx2 = m2 / 10.0f;
                            if (fx2 > 1.0f) {
                                fx2 = 1.0f;
                            }
                            if (fx2 > 0.1f) {
                                final float gx3 = class_3532.method_15374((m2 - 0.1f) * 1.3f);
                                final float h3 = fx2 - 0.1f;
                                final float j2 = gx3 * h3;
                                matrices.method_46416(j2 * 0.0f, j2 * 0.004f, j2 * 0.0f);
                            }
                            matrices.method_46416(0.0f, 0.0f, fx2 * 0.2f);
                            matrices.method_22905(1.0f, 1.0f, 1.0f + fx2 * 0.2f);
                            matrices.method_22907(class_7833.field_40715.rotationDegrees(l * 45.0f));
                            break;
                        }
                        case field_42717: {
                            this.applyBrushTransformation(matrices, tickDelta, arm, item, equipProgress);
                            break;
                        }
                    }
                }
                else if (player.method_6123()) {
                    this.applyEquipOffset(matrices, arm, equipProgress);
                    final int l = bl2 ? 1 : -1;
                    matrices.method_46416(l * -0.4f, 0.8f, 0.3f);
                    matrices.method_22907(class_7833.field_40716.rotationDegrees(l * 65.0f));
                    matrices.method_22907(class_7833.field_40718.rotationDegrees(l * -85.0f));
                }
                else if (arm == SwingAnimationModule.mc.field_1690.method_42552().method_41753() && this.isEnabled() && this.auraCheck()) {
                    this.handleSwordAnim(matrices, swingProgress, equipProgress, arm);
                }
                else {
                    final float n = -0.4f * class_3532.method_15374(class_3532.method_15355(swingProgress) * 3.1415927f);
                    final float mxx = 0.2f * class_3532.method_15374(class_3532.method_15355(swingProgress) * 6.2831855f);
                    final float fxxx = -0.2f * class_3532.method_15374(swingProgress * 3.1415927f);
                    final int o = bl2 ? 1 : -1;
                    matrices.method_46416(o * n, mxx, fxxx);
                    this.applyEquipOffset(matrices, arm, equipProgress);
                    this.applySwingOffset(matrices, arm, swingProgress);
                }
                this.applyItem360(matrices, arm);
                this.renderItem((class_1309)player, item, bl2 ? class_811.field_4322 : class_811.field_4321, !bl2, matrices, vertexConsumers, light);
            }
            matrices.method_22909();
        }
    }
    
    private void applyItem360(final class_4587 matrices, final class_1306 arm) {
        if (!this.item360.getValue()) {
            return;
        }
        final boolean isRightArm = arm == class_1306.field_6183;
        if (isRightArm && !this.item360Right.getValue()) {
            return;
        }
        if (!isRightArm && !this.item360Left.getValue()) {
            return;
        }
        if (this.item360NoTarget.getValue() && AuraModule.getInstance().target != null) {
            return;
        }
        final float angle = (float)(System.currentTimeMillis() / 4L % 360L);
        final String s;
        final String axis = s = (isRightArm ? this.item360RightAxis.getValue() : this.item360LeftAxis.getValue());
        switch (s) {
            case "X": {
                matrices.method_22907(class_7833.field_40714.rotationDegrees(angle));
                break;
            }
            case "Y": {
                matrices.method_22907(class_7833.field_40716.rotationDegrees(angle));
                break;
            }
            case "Z": {
                matrices.method_22907(class_7833.field_40718.rotationDegrees(angle));
                break;
            }
        }
    }
    
    private void applyBrushTransformation(final class_4587 matrices, final float tickDelta, final class_1306 arm, final class_1799 stack, final float equipProgress) {
        this.applyEquipOffset(matrices, arm, equipProgress);
        final float f = (float)(SwingAnimationModule.mc.field_1724.method_6014() % 10);
        final float g = f - tickDelta + 1.0f;
        final float h = 1.0f - g / 10.0f;
        final float n = -15.0f + 75.0f * class_3532.method_15362(h * 2.0f * 3.1415927f);
        if (arm != class_1306.field_6183) {
            matrices.method_22904(0.1, 0.83, 0.35);
            matrices.method_22907(class_7833.field_40714.rotationDegrees(-80.0f));
            matrices.method_22907(class_7833.field_40716.rotationDegrees(-90.0f));
            matrices.method_22907(class_7833.field_40714.rotationDegrees(n));
            matrices.method_22904(-0.3, 0.22, 0.35);
        }
        else {
            matrices.method_22904(-0.25, 0.22, 0.35);
            matrices.method_22907(class_7833.field_40714.rotationDegrees(-80.0f));
            matrices.method_22907(class_7833.field_40716.rotationDegrees(90.0f));
            matrices.method_22907(class_7833.field_40718.rotationDegrees(0.0f));
            matrices.method_22907(class_7833.field_40714.rotationDegrees(n));
        }
    }
    
    private void applyEatOrDrinkTransformation(final class_4587 matrices, final float tickDelta, final class_1306 arm, final class_1799 stack) {
        final float f = SwingAnimationModule.mc.field_1724.method_6014() - tickDelta + 1.0f;
        final float g = f / stack.method_7935((class_1309)SwingAnimationModule.mc.field_1724);
        if (g < 0.8f) {
            final float h = class_3532.method_15379(class_3532.method_15362(f / 4.0f * 3.1415927f) * 0.1f);
            matrices.method_46416(0.0f, h, 0.0f);
        }
        final float h = 1.0f - (float)Math.pow(g, 27.0);
        final int i = (arm == class_1306.field_6183) ? 1 : -1;
        matrices.method_46416(h * 0.6f * i, h * -0.5f, h * 0.0f);
        matrices.method_22907(class_7833.field_40716.rotationDegrees(i * h * 90.0f));
        matrices.method_22907(class_7833.field_40714.rotationDegrees(h * 10.0f));
        matrices.method_22907(class_7833.field_40718.rotationDegrees(i * h * 30.0f));
    }
    
    private void applyEquipOffset(final class_4587 matrices, final class_1306 arm, final float equipProgress) {
        final int i = (arm == class_1306.field_6183) ? 1 : -1;
        matrices.method_46416(i * 0.56f, -0.52f + equipProgress * -0.6f, -0.72f);
    }
    
    private void applySwingOffset(final class_4587 matrices, final class_1306 arm, final float swingProgress) {
        final int i = (arm == class_1306.field_6183) ? 1 : -1;
        final float f = class_3532.method_15374(swingProgress * swingProgress * 3.1415927f);
        final float g = class_3532.method_15374(class_3532.method_15355(swingProgress) * 3.1415927f);
        matrices.method_22907(class_7833.field_40716.rotationDegrees(i * (45.0f + f * -20.0f)));
        matrices.method_22907(class_7833.field_40718.rotationDegrees(i * g * -20.0f));
        matrices.method_22907(class_7833.field_40714.rotationDegrees(g * -80.0f));
        matrices.method_22907(class_7833.field_40716.rotationDegrees(i * -45.0f));
    }
    
    public void renderItem(final class_1309 entity, final class_1799 stack, final class_811 renderMode, final boolean leftHanded, final class_4587 matrices, final class_4597 vertexConsumers, final int light) {
        if (!stack.method_7960()) {
            SwingAnimationModule.mc.method_1480().method_23177(entity, stack, renderMode, leftHanded, matrices, vertexConsumers, entity.method_37908(), light, class_4608.field_21444, entity.method_5628() + renderMode.ordinal());
        }
    }
    
    public boolean auraCheck() {
        return !this.auraOnly.getValue() || AuraModule.getInstance().target != null;
    }
    
    @Generated
    public static SwingAnimationModule getInstance() {
        return SwingAnimationModule.instance;
    }
    
    static {
        instance = new SwingAnimationModule();
    }
}
