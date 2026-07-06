/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1268
 *  net.minecraft.class_1306
 *  net.minecraft.class_1309
 *  net.minecraft.class_1764
 *  net.minecraft.class_1799
 *  net.minecraft.class_1802
 *  net.minecraft.class_3532
 *  net.minecraft.class_4587
 *  net.minecraft.class_4597
 *  net.minecraft.class_4608
 *  net.minecraft.class_742
 *  net.minecraft.class_7833
 *  net.minecraft.class_811
 */
package sweetie.leonware.client.features.modules.render;

import lombok.Generated;
import net.minecraft.class_1268;
import net.minecraft.class_1306;
import net.minecraft.class_1309;
import net.minecraft.class_1764;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_3532;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_4608;
import net.minecraft.class_742;
import net.minecraft.class_7833;
import net.minecraft.class_811;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.client.features.modules.combat.AuraModule;
import sweetie.leonware.client.features.modules.render.ViewModelModule;

@ModuleRegister(name="Swing Animation", category=Category.RENDER)
public class SwingAnimationModule
extends Module {
    private static final SwingAnimationModule instance = new SwingAnimationModule();
    public final ModeSetting mode = new ModeSetting("Mode").value("Mode 1").values("Mode 1", "Mode 2", "Mode 3", "Mode 4", "Mode 5", "Mode 6", "Mode 7");
    private final BooleanSetting auraOnly = new BooleanSetting("Only with aura").value(false);
    public final SliderSetting strength = new SliderSetting("Strength").value(Float.valueOf(20.0f)).range(1.0f, 75.0f).step(0.1f).setVisible(() -> !this.mode.is("Mode 5") && !this.mode.is("Mode 7"));
    public final BooleanSetting slow = new BooleanSetting("Slow").value(false);
    public final SliderSetting speed = new SliderSetting("Speed").value(Float.valueOf(12.0f)).range(1.0f, 50.0f).step(1.0f).setVisible(this.slow::getValue);
    public final BooleanSetting item360 = new BooleanSetting("Item 360").value(false);
    public final BooleanSetting item360NoTarget = new BooleanSetting("\u0415\u0441\u043b\u0438 \u043d\u0435\u0442 \u0442\u0430\u0440\u0433\u0435\u0442\u0430").value(true).setVisible(this.item360::getValue);
    public final BooleanSetting item360Left = new BooleanSetting("\u041b\u0435\u0432\u0430\u044f \u0440\u0443\u043a\u0430").value(true).setVisible(this.item360::getValue);
    public final ModeSetting item360LeftAxis = new ModeSetting("\u041e\u0441\u044c \u043b\u0435\u0432\u043e\u0439 \u0440\u0443\u043a\u0438").value("X").values("X", "Y", "Z").setVisible(() -> (Boolean)this.item360.getValue() != false && (Boolean)this.item360Left.getValue() != false);
    public final BooleanSetting item360Right = new BooleanSetting("\u041f\u0440\u0430\u0432\u0430\u044f \u0440\u0443\u043a\u0430").value(false).setVisible(this.item360::getValue);
    public final ModeSetting item360RightAxis = new ModeSetting("\u041e\u0441\u044c \u043f\u0440\u0430\u0432\u043e\u0439 \u0440\u0443\u043a\u0438").value("X").values("X", "Y", "Z").setVisible(() -> (Boolean)this.item360.getValue() != false && (Boolean)this.item360Right.getValue() != false);

    public SwingAnimationModule() {
        this.addSettings(this.mode, this.auraOnly, this.strength, this.slow, this.speed, this.item360, this.item360NoTarget, this.item360Left, this.item360LeftAxis, this.item360Right, this.item360RightAxis);
    }

    @Override
    public void onEvent() {
    }

    private void handleSwordAnim(class_4587 matrices, float swingProgress, float equipProgress, class_1306 arm) {
        float g = class_3532.method_15374((float)(class_3532.method_15355((float)swingProgress) * (float)Math.PI));
        float anim = (float)Math.sin((double)swingProgress * 1.5707963267948966 * 2.0);
        float isLeft = arm == class_1306.field_6182 ? -1.0f : 1.0f;
        switch ((String)this.mode.getValue()) {
            case "Mode 1": {
                this.applyEquipOffset(matrices, arm, 0.0f);
                this.applyCustomSwingOffset(matrices, arm, swingProgress, ((Float)this.strength.getValue()).floatValue() / 20.0f);
                break;
            }
            case "Mode 2": {
                this.applyEquipOffset(matrices, arm, 0.0f);
                matrices.method_22907(class_7833.field_40714.rotationDegrees(50.0f));
                matrices.method_22907(class_7833.field_40716.rotationDegrees(isLeft * -60.0f));
                matrices.method_22907(class_7833.field_40718.rotationDegrees(isLeft * (110.0f + ((Float)this.strength.getValue()).floatValue() * g)));
                break;
            }
            case "Mode 3": {
                this.applyEquipOffset(matrices, arm, 0.0f);
                matrices.method_22907(class_7833.field_40714.rotationDegrees(50.0f));
                matrices.method_22907(class_7833.field_40716.rotationDegrees(isLeft * (-30.0f * (1.0f - g) - 30.0f + (((Float)this.strength.getValue()).floatValue() - 20.0f) * g)));
                matrices.method_22907(class_7833.field_40718.rotationDegrees(isLeft * 110.0f));
                break;
            }
            case "Mode 4": {
                this.applyEquipOffset(matrices, arm, 0.0f);
                matrices.method_22907(class_7833.field_40716.rotationDegrees(isLeft * 90.0f));
                matrices.method_22907(class_7833.field_40718.rotationDegrees(isLeft * -30.0f));
                matrices.method_22907(class_7833.field_40714.rotationDegrees(-90.0f - ((Float)this.strength.getValue()).floatValue() * anim + 10.0f));
                break;
            }
            case "Mode 5": {
                float rotation = swingProgress * -360.0f;
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
                float blockSwing = class_3532.method_15374((float)(swingProgress * swingProgress * (float)Math.PI));
                matrices.method_22907(class_7833.field_40714.rotationDegrees(blockSwing * -(((Float)this.strength.getValue()).floatValue() / 2.0f)));
                matrices.method_46416(0.0f, blockSwing * -0.1f * (((Float)this.strength.getValue()).floatValue() / 20.0f), blockSwing * -0.05f * (((Float)this.strength.getValue()).floatValue() / 20.0f));
                break;
            }
            case "Mode 7": {
                this.celestialMode(matrices, swingProgress, equipProgress, isLeft);
            }
        }
    }

    private void celestialMode(class_4587 matrices, float swingProgress, float equipProgress, float isLeft) {
        float eq = equipProgress / 3.0f;
        matrices.method_46416(isLeft * 0.56f, -0.44f - 0.6f * eq, -0.72f);
        matrices.method_22907(class_7833.field_40716.rotationDegrees(isLeft * 45.0f));
        float f = class_3532.method_15374((float)(swingProgress * swingProgress * (float)Math.PI));
        float f2 = class_3532.method_15374((float)(class_3532.method_15355((float)swingProgress) * (float)Math.PI));
        matrices.method_22907(class_7833.field_40714.rotationDegrees(f * -20.0f));
        matrices.method_22907(class_7833.field_40718.rotationDegrees(isLeft * f2 * -20.0f));
        matrices.method_22907(class_7833.field_40714.rotationDegrees(f2 * -80.0f));
        matrices.method_46416(isLeft * 0.4f, 0.2f, 0.2f);
        matrices.method_46416(-0.5f * isLeft, 0.08f, 0.0f);
        matrices.method_22907(class_7833.field_40716.rotationDegrees(isLeft * 20.0f));
        matrices.method_22907(class_7833.field_40714.rotationDegrees(-80.0f));
        matrices.method_22907(class_7833.field_40716.rotationDegrees(isLeft * 20.0f));
    }

    private void applyCustomSwingOffset(class_4587 matrices, class_1306 arm, float swingProgress, float strengthModifier) {
        int i = arm == class_1306.field_6183 ? 1 : -1;
        float f = class_3532.method_15374((float)(swingProgress * swingProgress * (float)Math.PI));
        float g = class_3532.method_15374((float)(class_3532.method_15355((float)swingProgress) * (float)Math.PI));
        matrices.method_22907(class_7833.field_40716.rotationDegrees((float)i * (45.0f + f * -20.0f * strengthModifier)));
        matrices.method_22907(class_7833.field_40718.rotationDegrees((float)i * g * -20.0f * strengthModifier));
        matrices.method_22907(class_7833.field_40714.rotationDegrees(g * -80.0f * strengthModifier));
        matrices.method_22907(class_7833.field_40716.rotationDegrees((float)i * -45.0f));
    }

    public void handleRenderItem(class_742 player, float tickDelta, float pitch, class_1268 hand, float swingProgress, class_1799 item, float equipProgress, class_4587 matrices, class_4597 vertexConsumers, int light) {
        if (!player.method_31550()) {
            boolean bl = hand == class_1268.field_5808;
            class_1306 arm = bl ? player.method_6068() : player.method_6068().method_5928();
            matrices.method_22903();
            if (item.method_31574(class_1802.field_8399)) {
                int i;
                boolean bl2 = class_1764.method_7781((class_1799)item);
                boolean bl3 = arm == class_1306.field_6183;
                int n = i = bl3 ? 1 : -1;
                if (player.method_6115() && player.method_6014() > 0 && player.method_6058() == hand) {
                    this.applyEquipOffset(matrices, arm, equipProgress);
                    matrices.method_46416((float)i * -0.4785682f, -0.094387f, 0.05731531f);
                    matrices.method_22907(class_7833.field_40714.rotationDegrees(-11.935f));
                    matrices.method_22907(class_7833.field_40716.rotationDegrees((float)i * 65.3f));
                    matrices.method_22907(class_7833.field_40718.rotationDegrees((float)i * -9.785f));
                    float f = (float)item.method_7935((class_1309)SwingAnimationModule.mc.field_1724) - ((float)SwingAnimationModule.mc.field_1724.method_6014() - tickDelta + 1.0f);
                    float g = f / (float)class_1764.method_7775((class_1799)item, (class_1309)SwingAnimationModule.mc.field_1724);
                    if (g > 1.0f) {
                        g = 1.0f;
                    }
                    if (g > 0.1f) {
                        float h = class_3532.method_15374((float)((f - 0.1f) * 1.3f));
                        float j = g - 0.1f;
                        float k = h * j;
                        matrices.method_46416(k * 0.0f, k * 0.004f, k * 0.0f);
                    }
                    matrices.method_46416(g * 0.0f, g * 0.0f, g * 0.04f);
                    matrices.method_22905(1.0f, 1.0f, 1.0f + g * 0.2f);
                    matrices.method_22907(class_7833.field_40715.rotationDegrees((float)i * 45.0f));
                } else {
                    float fx = -0.4f * class_3532.method_15374((float)(class_3532.method_15355((float)swingProgress) * (float)Math.PI));
                    float gx = 0.2f * class_3532.method_15374((float)(class_3532.method_15355((float)swingProgress) * ((float)Math.PI * 2)));
                    float h = -0.2f * class_3532.method_15374((float)(swingProgress * (float)Math.PI));
                    matrices.method_46416((float)i * fx, gx, h);
                    this.applyEquipOffset(matrices, arm, equipProgress);
                    this.applySwingOffset(matrices, arm, swingProgress);
                    if (bl2 && swingProgress < 0.001f && bl) {
                        matrices.method_46416((float)i * -0.641864f, 0.0f, 0.0f);
                        matrices.method_22907(class_7833.field_40716.rotationDegrees((float)i * 10.0f));
                    }
                }
                this.applyItem360(matrices, arm);
                this.renderItem((class_1309)player, item, bl3 ? class_811.field_4322 : class_811.field_4321, !bl3, matrices, vertexConsumers, light);
            } else {
                boolean bl2 = arm == class_1306.field_6183;
                ViewModelModule viewModel = ViewModelModule.getInstance();
                if (viewModel.isEnabled()) {
                    if (bl2) {
                        matrices.method_22904(((Float)viewModel.rightX.getValue()).doubleValue(), ((Float)viewModel.rightY.getValue()).doubleValue(), ((Float)viewModel.rightZ.getValue()).doubleValue());
                    } else {
                        matrices.method_22904(-((Float)viewModel.leftX.getValue()).doubleValue(), ((Float)viewModel.leftY.getValue()).doubleValue(), ((Float)viewModel.leftZ.getValue()).doubleValue());
                    }
                }
                if (player.method_6115() && player.method_6014() > 0 && player.method_6058() == hand) {
                    int l = bl2 ? 1 : -1;
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
                            matrices.method_46416((float)l * -0.2785682f, 0.18344387f, 0.15731531f);
                            matrices.method_22907(class_7833.field_40714.rotationDegrees(-13.935f));
                            matrices.method_22907(class_7833.field_40716.rotationDegrees((float)l * 35.3f));
                            matrices.method_22907(class_7833.field_40718.rotationDegrees((float)l * -9.785f));
                            float mx = (float)item.method_7935((class_1309)SwingAnimationModule.mc.field_1724) - ((float)SwingAnimationModule.mc.field_1724.method_6014() - tickDelta + 1.0f);
                            float fxx = mx / 20.0f;
                            fxx = (fxx * fxx + fxx * 2.0f) / 3.0f;
                            if (fxx > 1.0f) {
                                fxx = 1.0f;
                            }
                            if (fxx > 0.1f) {
                                float gx = class_3532.method_15374((float)((mx - 0.1f) * 1.3f));
                                float h = fxx - 0.1f;
                                float j = gx * h;
                                matrices.method_46416(j * 0.0f, j * 0.004f, j * 0.0f);
                            }
                            matrices.method_46416(fxx * 0.0f, fxx * 0.0f, fxx * 0.04f);
                            matrices.method_22905(1.0f, 1.0f, 1.0f + fxx * 0.2f);
                            matrices.method_22907(class_7833.field_40715.rotationDegrees((float)l * 45.0f));
                            break;
                        }
                        case field_8951: {
                            this.applyEquipOffset(matrices, arm, equipProgress);
                            matrices.method_46416((float)l * -0.5f, 0.7f, 0.1f);
                            matrices.method_22907(class_7833.field_40714.rotationDegrees(-55.0f));
                            matrices.method_22907(class_7833.field_40716.rotationDegrees((float)l * 35.3f));
                            matrices.method_22907(class_7833.field_40718.rotationDegrees((float)l * -9.785f));
                            float m = (float)item.method_7935((class_1309)SwingAnimationModule.mc.field_1724) - ((float)SwingAnimationModule.mc.field_1724.method_6014() - tickDelta + 1.0f);
                            float fx = m / 10.0f;
                            if (fx > 1.0f) {
                                fx = 1.0f;
                            }
                            if (fx > 0.1f) {
                                float gx = class_3532.method_15374((float)((m - 0.1f) * 1.3f));
                                float h = fx - 0.1f;
                                float j = gx * h;
                                matrices.method_46416(j * 0.0f, j * 0.004f, j * 0.0f);
                            }
                            matrices.method_46416(0.0f, 0.0f, fx * 0.2f);
                            matrices.method_22905(1.0f, 1.0f, 1.0f + fx * 0.2f);
                            matrices.method_22907(class_7833.field_40715.rotationDegrees((float)l * 45.0f));
                            break;
                        }
                        case field_42717: {
                            this.applyBrushTransformation(matrices, tickDelta, arm, item, equipProgress);
                        }
                    }
                } else if (player.method_6123()) {
                    this.applyEquipOffset(matrices, arm, equipProgress);
                    int l = bl2 ? 1 : -1;
                    matrices.method_46416((float)l * -0.4f, 0.8f, 0.3f);
                    matrices.method_22907(class_7833.field_40716.rotationDegrees((float)l * 65.0f));
                    matrices.method_22907(class_7833.field_40718.rotationDegrees((float)l * -85.0f));
                } else if (arm == SwingAnimationModule.mc.field_1690.method_42552().method_41753() && this.isEnabled() && this.auraCheck()) {
                    this.handleSwordAnim(matrices, swingProgress, equipProgress, arm);
                } else {
                    float n = -0.4f * class_3532.method_15374((float)(class_3532.method_15355((float)swingProgress) * (float)Math.PI));
                    float mxx = 0.2f * class_3532.method_15374((float)(class_3532.method_15355((float)swingProgress) * ((float)Math.PI * 2)));
                    float fxxx = -0.2f * class_3532.method_15374((float)(swingProgress * (float)Math.PI));
                    int o = bl2 ? 1 : -1;
                    matrices.method_46416((float)o * n, mxx, fxxx);
                    this.applyEquipOffset(matrices, arm, equipProgress);
                    this.applySwingOffset(matrices, arm, swingProgress);
                }
                this.applyItem360(matrices, arm);
                this.renderItem((class_1309)player, item, bl2 ? class_811.field_4322 : class_811.field_4321, !bl2, matrices, vertexConsumers, light);
            }
            matrices.method_22909();
        }
    }

    private void applyItem360(class_4587 matrices, class_1306 arm) {
        String axis;
        boolean isRightArm;
        if (!((Boolean)this.item360.getValue()).booleanValue()) {
            return;
        }
        boolean bl = isRightArm = arm == class_1306.field_6183;
        if (isRightArm && !((Boolean)this.item360Right.getValue()).booleanValue()) {
            return;
        }
        if (!isRightArm && !((Boolean)this.item360Left.getValue()).booleanValue()) {
            return;
        }
        if (((Boolean)this.item360NoTarget.getValue()).booleanValue() && AuraModule.getInstance().target != null) {
            return;
        }
        float angle = System.currentTimeMillis() / 4L % 360L;
        switch (axis = isRightArm ? (String)this.item360RightAxis.getValue() : (String)this.item360LeftAxis.getValue()) {
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
            }
        }
    }

    private void applyBrushTransformation(class_4587 matrices, float tickDelta, class_1306 arm, class_1799 stack, float equipProgress) {
        this.applyEquipOffset(matrices, arm, equipProgress);
        float f = SwingAnimationModule.mc.field_1724.method_6014() % 10;
        float g = f - tickDelta + 1.0f;
        float h = 1.0f - g / 10.0f;
        float n = -15.0f + 75.0f * class_3532.method_15362((float)(h * 2.0f * (float)Math.PI));
        if (arm != class_1306.field_6183) {
            matrices.method_22904(0.1, 0.83, 0.35);
            matrices.method_22907(class_7833.field_40714.rotationDegrees(-80.0f));
            matrices.method_22907(class_7833.field_40716.rotationDegrees(-90.0f));
            matrices.method_22907(class_7833.field_40714.rotationDegrees(n));
            matrices.method_22904(-0.3, 0.22, 0.35);
        } else {
            matrices.method_22904(-0.25, 0.22, 0.35);
            matrices.method_22907(class_7833.field_40714.rotationDegrees(-80.0f));
            matrices.method_22907(class_7833.field_40716.rotationDegrees(90.0f));
            matrices.method_22907(class_7833.field_40718.rotationDegrees(0.0f));
            matrices.method_22907(class_7833.field_40714.rotationDegrees(n));
        }
    }

    private void applyEatOrDrinkTransformation(class_4587 matrices, float tickDelta, class_1306 arm, class_1799 stack) {
        float h;
        float f = (float)SwingAnimationModule.mc.field_1724.method_6014() - tickDelta + 1.0f;
        float g = f / (float)stack.method_7935((class_1309)SwingAnimationModule.mc.field_1724);
        if (g < 0.8f) {
            h = class_3532.method_15379((float)(class_3532.method_15362((float)(f / 4.0f * (float)Math.PI)) * 0.1f));
            matrices.method_46416(0.0f, h, 0.0f);
        }
        h = 1.0f - (float)Math.pow(g, 27.0);
        int i = arm == class_1306.field_6183 ? 1 : -1;
        matrices.method_46416(h * 0.6f * (float)i, h * -0.5f, h * 0.0f);
        matrices.method_22907(class_7833.field_40716.rotationDegrees((float)i * h * 90.0f));
        matrices.method_22907(class_7833.field_40714.rotationDegrees(h * 10.0f));
        matrices.method_22907(class_7833.field_40718.rotationDegrees((float)i * h * 30.0f));
    }

    private void applyEquipOffset(class_4587 matrices, class_1306 arm, float equipProgress) {
        int i = arm == class_1306.field_6183 ? 1 : -1;
        matrices.method_46416((float)i * 0.56f, -0.52f + equipProgress * -0.6f, -0.72f);
    }

    private void applySwingOffset(class_4587 matrices, class_1306 arm, float swingProgress) {
        int i = arm == class_1306.field_6183 ? 1 : -1;
        float f = class_3532.method_15374((float)(swingProgress * swingProgress * (float)Math.PI));
        float g = class_3532.method_15374((float)(class_3532.method_15355((float)swingProgress) * (float)Math.PI));
        matrices.method_22907(class_7833.field_40716.rotationDegrees((float)i * (45.0f + f * -20.0f)));
        matrices.method_22907(class_7833.field_40718.rotationDegrees((float)i * g * -20.0f));
        matrices.method_22907(class_7833.field_40714.rotationDegrees(g * -80.0f));
        matrices.method_22907(class_7833.field_40716.rotationDegrees((float)i * -45.0f));
    }

    public void renderItem(class_1309 entity, class_1799 stack, class_811 renderMode, boolean leftHanded, class_4587 matrices, class_4597 vertexConsumers, int light) {
        if (!stack.method_7960()) {
            mc.method_1480().method_23177(entity, stack, renderMode, leftHanded, matrices, vertexConsumers, entity.method_37908(), light, class_4608.field_21444, entity.method_5628() + renderMode.ordinal());
        }
    }

    public boolean auraCheck() {
        return (Boolean)this.auraOnly.getValue() == false || AuraModule.getInstance().target != null;
    }

    @Generated
    public static SwingAnimationModule getInstance() {
        return instance;
    }
}

