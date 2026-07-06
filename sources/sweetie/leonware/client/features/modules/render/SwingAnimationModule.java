package sweetie.leonware.client.features.modules.render;

import java.util.Objects;
import lombok.Generated;
import net.minecraft.class_1268;
import net.minecraft.class_1306;
import net.minecraft.class_1309;
import net.minecraft.class_1764;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1839;
import net.minecraft.class_3532;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_4608;
import net.minecraft.class_742;
import net.minecraft.class_7833;
import net.minecraft.class_811;
import org.newsclub.net.unix.AFVSOCKSocketAddress;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.client.features.modules.combat.AuraModule;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/SwingAnimationModule.class */
@ModuleRegister(name = "Swing Animation", category = Category.RENDER)
public class SwingAnimationModule extends Module {
    private static final SwingAnimationModule instance = new SwingAnimationModule();
    public final ModeSetting mode = new ModeSetting("Mode").value("Mode 1").values("Mode 1", "Mode 2", "Mode 3", "Mode 4", "Mode 5", "Mode 6", "Mode 7");
    private final BooleanSetting auraOnly = new BooleanSetting("Only with aura").value((Boolean) false);
    public final SliderSetting strength = new SliderSetting("Strength").value(Float.valueOf(20.0f)).range(1.0f, 75.0f).step(0.1f).setVisible(() -> {
        return Boolean.valueOf((this.mode.is("Mode 5") || this.mode.is("Mode 7")) ? false : true);
    });
    public final BooleanSetting slow = new BooleanSetting("Slow").value((Boolean) false);
    public final SliderSetting speed;
    public final BooleanSetting item360;
    public final BooleanSetting item360NoTarget;
    public final BooleanSetting item360Left;
    public final ModeSetting item360LeftAxis;
    public final BooleanSetting item360Right;
    public final ModeSetting item360RightAxis;

    @Generated
    public static SwingAnimationModule getInstance() {
        return instance;
    }

    /* JADX WARN: Type inference failed for: r1v16, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v21, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    /* JADX WARN: Type inference failed for: r1v24, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    /* JADX WARN: Type inference failed for: r1v28, types: [sweetie.leonware.api.module.setting.ModeSetting] */
    /* JADX WARN: Type inference failed for: r1v31, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    /* JADX WARN: Type inference failed for: r1v35, types: [sweetie.leonware.api.module.setting.ModeSetting] */
    /* JADX WARN: Type inference failed for: r1v9, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    public SwingAnimationModule() {
        SliderSetting sliderSettingStep = new SliderSetting("Speed").value(Float.valueOf(12.0f)).range(1.0f, 50.0f).step(1.0f);
        BooleanSetting booleanSetting = this.slow;
        Objects.requireNonNull(booleanSetting);
        this.speed = sliderSettingStep.setVisible(booleanSetting::getValue);
        this.item360 = new BooleanSetting("Item 360").value((Boolean) false);
        BooleanSetting booleanSettingValue = new BooleanSetting("Если нет таргета").value((Boolean) true);
        BooleanSetting booleanSetting2 = this.item360;
        Objects.requireNonNull(booleanSetting2);
        this.item360NoTarget = booleanSettingValue.setVisible(booleanSetting2::getValue);
        BooleanSetting booleanSettingValue2 = new BooleanSetting("Левая рука").value((Boolean) true);
        BooleanSetting booleanSetting3 = this.item360;
        Objects.requireNonNull(booleanSetting3);
        this.item360Left = booleanSettingValue2.setVisible(booleanSetting3::getValue);
        this.item360LeftAxis = new ModeSetting("Ось левой руки").value("X").values("X", "Y", "Z").setVisible(() -> {
            return Boolean.valueOf(this.item360.getValue().booleanValue() && this.item360Left.getValue().booleanValue());
        });
        BooleanSetting booleanSettingValue3 = new BooleanSetting("Правая рука").value((Boolean) false);
        BooleanSetting booleanSetting4 = this.item360;
        Objects.requireNonNull(booleanSetting4);
        this.item360Right = booleanSettingValue3.setVisible(booleanSetting4::getValue);
        this.item360RightAxis = new ModeSetting("Ось правой руки").value("X").values("X", "Y", "Z").setVisible(() -> {
            return Boolean.valueOf(this.item360.getValue().booleanValue() && this.item360Right.getValue().booleanValue());
        });
        addSettings(this.mode, this.auraOnly, this.strength, this.slow, this.speed, this.item360, this.item360NoTarget, this.item360Left, this.item360LeftAxis, this.item360Right, this.item360RightAxis);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
    }

    private void handleSwordAnim(class_4587 matrices, float swingProgress, float equipProgress, class_1306 arm) {
        float g;
        float anim;
        float isLeft;
        g = class_3532.method_15374(class_3532.method_15355(swingProgress) * 3.1415927f);
        anim = (float) Math.sin(((double) swingProgress) * 1.5707963267948966d * 2.0d);
        isLeft = arm == class_1306.field_6182 ? -1.0f : 1.0f;
        switch (this.mode.getValue()) {
            case "Mode 1":
                applyEquipOffset(matrices, arm, 0.0f);
                applyCustomSwingOffset(matrices, arm, swingProgress, this.strength.getValue().floatValue() / 20.0f);
                break;
            case "Mode 2":
                applyEquipOffset(matrices, arm, 0.0f);
                matrices.method_22907(class_7833.field_40714.rotationDegrees(50.0f));
                matrices.method_22907(class_7833.field_40716.rotationDegrees(isLeft * (-60.0f)));
                matrices.method_22907(class_7833.field_40718.rotationDegrees(isLeft * (110.0f + (this.strength.getValue().floatValue() * g))));
                break;
            case "Mode 3":
                applyEquipOffset(matrices, arm, 0.0f);
                matrices.method_22907(class_7833.field_40714.rotationDegrees(50.0f));
                matrices.method_22907(class_7833.field_40716.rotationDegrees(isLeft * ((((-30.0f) * (1.0f - g)) - 30.0f) + ((this.strength.getValue().floatValue() - 20.0f) * g))));
                matrices.method_22907(class_7833.field_40718.rotationDegrees(isLeft * 110.0f));
                break;
            case "Mode 4":
                applyEquipOffset(matrices, arm, 0.0f);
                matrices.method_22907(class_7833.field_40716.rotationDegrees(isLeft * 90.0f));
                matrices.method_22907(class_7833.field_40718.rotationDegrees(isLeft * (-30.0f)));
                matrices.method_22907(class_7833.field_40714.rotationDegrees(((-90.0f) - (this.strength.getValue().floatValue() * anim)) + 10.0f));
                break;
            case "Mode 5":
                float rotation = swingProgress * (-360.0f);
                applyEquipOffset(matrices, arm, 0.0f);
                matrices.method_22907(class_7833.field_40714.rotationDegrees(rotation));
                break;
            case "Mode 6":
                applyEquipOffset(matrices, arm, 0.0f);
                matrices.method_46416(isLeft * (-0.14142136f), 0.08f, 0.14142136f);
                matrices.method_22907(class_7833.field_40714.rotationDegrees(-102.25f));
                matrices.method_22907(class_7833.field_40716.rotationDegrees(isLeft * 13.365f));
                matrices.method_22907(class_7833.field_40718.rotationDegrees(isLeft * 78.05f));
                float blockSwing = class_3532.method_15374(swingProgress * swingProgress * 3.1415927f);
                matrices.method_22907(class_7833.field_40714.rotationDegrees(blockSwing * (-(this.strength.getValue().floatValue() / 2.0f))));
                matrices.method_46416(0.0f, blockSwing * (-0.1f) * (this.strength.getValue().floatValue() / 20.0f), blockSwing * (-0.05f) * (this.strength.getValue().floatValue() / 20.0f));
                break;
            case "Mode 7":
                celestialMode(matrices, swingProgress, equipProgress, isLeft);
                break;
        }
    }

    private void celestialMode(class_4587 matrices, float swingProgress, float equipProgress, float isLeft) {
        float eq = equipProgress / 3.0f;
        matrices.method_46416(isLeft * 0.56f, (-0.44f) - (0.6f * eq), -0.72f);
        matrices.method_22907(class_7833.field_40716.rotationDegrees(isLeft * 45.0f));
        float f = class_3532.method_15374(swingProgress * swingProgress * 3.1415927f);
        float f2 = class_3532.method_15374(class_3532.method_15355(swingProgress) * 3.1415927f);
        matrices.method_22907(class_7833.field_40714.rotationDegrees(f * (-20.0f)));
        matrices.method_22907(class_7833.field_40718.rotationDegrees(isLeft * f2 * (-20.0f)));
        matrices.method_22907(class_7833.field_40714.rotationDegrees(f2 * (-80.0f)));
        matrices.method_46416(isLeft * 0.4f, 0.2f, 0.2f);
        matrices.method_46416((-0.5f) * isLeft, 0.08f, 0.0f);
        matrices.method_22907(class_7833.field_40716.rotationDegrees(isLeft * 20.0f));
        matrices.method_22907(class_7833.field_40714.rotationDegrees(-80.0f));
        matrices.method_22907(class_7833.field_40716.rotationDegrees(isLeft * 20.0f));
    }

    private void applyCustomSwingOffset(class_4587 matrices, class_1306 arm, float swingProgress, float strengthModifier) {
        int i = arm == class_1306.field_6183 ? 1 : -1;
        float f = class_3532.method_15374(swingProgress * swingProgress * 3.1415927f);
        float g = class_3532.method_15374(class_3532.method_15355(swingProgress) * 3.1415927f);
        matrices.method_22907(class_7833.field_40716.rotationDegrees(i * (45.0f + (f * (-20.0f) * strengthModifier))));
        matrices.method_22907(class_7833.field_40718.rotationDegrees(i * g * (-20.0f) * strengthModifier));
        matrices.method_22907(class_7833.field_40714.rotationDegrees(g * (-80.0f) * strengthModifier));
        matrices.method_22907(class_7833.field_40716.rotationDegrees(i * (-45.0f)));
    }

    public void handleRenderItem(class_742 player, float tickDelta, float pitch, class_1268 hand, float swingProgress, class_1799 item, float equipProgress, class_4587 matrices, class_4597 vertexConsumers, int light) {
        if (!player.method_31550()) {
            boolean bl = hand == class_1268.field_5808;
            class_1306 arm = bl ? player.method_6068() : player.method_6068().method_5928();
            matrices.method_22903();
            if (item.method_31574(class_1802.field_8399)) {
                boolean bl2 = class_1764.method_7781(item);
                boolean bl3 = arm == class_1306.field_6183;
                int i = bl3 ? 1 : -1;
                if (player.method_6115() && player.method_6014() > 0 && player.method_6058() == hand) {
                    applyEquipOffset(matrices, arm, equipProgress);
                    matrices.method_46416(i * (-0.4785682f), -0.094387f, 0.05731531f);
                    matrices.method_22907(class_7833.field_40714.rotationDegrees(-11.935f));
                    matrices.method_22907(class_7833.field_40716.rotationDegrees(i * 65.3f));
                    matrices.method_22907(class_7833.field_40718.rotationDegrees(i * (-9.785f)));
                    float f = item.method_7935(mc.field_1724) - ((mc.field_1724.method_6014() - tickDelta) + 1.0f);
                    float g = f / class_1764.method_7775(item, mc.field_1724);
                    if (g > 1.0f) {
                        g = 1.0f;
                    }
                    if (g > 0.1f) {
                        float h = class_3532.method_15374((f - 0.1f) * 1.3f);
                        float k = h * (g - 0.1f);
                        matrices.method_46416(k * 0.0f, k * 0.004f, k * 0.0f);
                    }
                    matrices.method_46416(g * 0.0f, g * 0.0f, g * 0.04f);
                    matrices.method_22905(1.0f, 1.0f, 1.0f + (g * 0.2f));
                    matrices.method_22907(class_7833.field_40715.rotationDegrees(i * 45.0f));
                } else {
                    float fx = (-0.4f) * class_3532.method_15374(class_3532.method_15355(swingProgress) * 3.1415927f);
                    float gx = 0.2f * class_3532.method_15374(class_3532.method_15355(swingProgress) * 6.2831855f);
                    float h2 = (-0.2f) * class_3532.method_15374(swingProgress * 3.1415927f);
                    matrices.method_46416(i * fx, gx, h2);
                    applyEquipOffset(matrices, arm, equipProgress);
                    applySwingOffset(matrices, arm, swingProgress);
                    if (bl2 && swingProgress < 0.001f && bl) {
                        matrices.method_46416(i * (-0.641864f), 0.0f, 0.0f);
                        matrices.method_22907(class_7833.field_40716.rotationDegrees(i * 10.0f));
                    }
                }
                applyItem360(matrices, arm);
                renderItem(player, item, bl3 ? class_811.field_4322 : class_811.field_4321, !bl3, matrices, vertexConsumers, light);
            } else {
                boolean bl22 = arm == class_1306.field_6183;
                ViewModelModule viewModel = ViewModelModule.getInstance();
                if (viewModel.isEnabled()) {
                    if (bl22) {
                        matrices.method_22904(viewModel.rightX.getValue().doubleValue(), viewModel.rightY.getValue().doubleValue(), viewModel.rightZ.getValue().doubleValue());
                    } else {
                        matrices.method_22904(-viewModel.leftX.getValue().doubleValue(), viewModel.leftY.getValue().doubleValue(), viewModel.leftZ.getValue().doubleValue());
                    }
                }
                if (player.method_6115() && player.method_6014() > 0 && player.method_6058() == hand) {
                    int l = bl22 ? 1 : -1;
                    switch (AnonymousClass1.$SwitchMap$net$minecraft$item$consume$UseAction[item.method_7976().ordinal()]) {
                        case 1:
                        case AFVSOCKSocketAddress.VMADDR_CID_HOST /* 2 */:
                            applyEquipOffset(matrices, arm, equipProgress);
                            break;
                        case 3:
                        case 4:
                            applyEatOrDrinkTransformation(matrices, tickDelta, arm, item);
                            applyEquipOffset(matrices, arm, equipProgress);
                            break;
                        case 5:
                            applyEquipOffset(matrices, arm, equipProgress);
                            matrices.method_46416(l * (-0.2785682f), 0.18344387f, 0.15731531f);
                            matrices.method_22907(class_7833.field_40714.rotationDegrees(-13.935f));
                            matrices.method_22907(class_7833.field_40716.rotationDegrees(l * 35.3f));
                            matrices.method_22907(class_7833.field_40718.rotationDegrees(l * (-9.785f)));
                            float mx = item.method_7935(mc.field_1724) - ((mc.field_1724.method_6014() - tickDelta) + 1.0f);
                            float fxx = mx / 20.0f;
                            float fxx2 = ((fxx * fxx) + (fxx * 2.0f)) / 3.0f;
                            if (fxx2 > 1.0f) {
                                fxx2 = 1.0f;
                            }
                            if (fxx2 > 0.1f) {
                                float gx2 = class_3532.method_15374((mx - 0.1f) * 1.3f);
                                float h3 = fxx2 - 0.1f;
                                float j = gx2 * h3;
                                matrices.method_46416(j * 0.0f, j * 0.004f, j * 0.0f);
                            }
                            matrices.method_46416(fxx2 * 0.0f, fxx2 * 0.0f, fxx2 * 0.04f);
                            matrices.method_22905(1.0f, 1.0f, 1.0f + (fxx2 * 0.2f));
                            matrices.method_22907(class_7833.field_40715.rotationDegrees(l * 45.0f));
                            break;
                        case 6:
                            applyEquipOffset(matrices, arm, equipProgress);
                            matrices.method_46416(l * (-0.5f), 0.7f, 0.1f);
                            matrices.method_22907(class_7833.field_40714.rotationDegrees(-55.0f));
                            matrices.method_22907(class_7833.field_40716.rotationDegrees(l * 35.3f));
                            matrices.method_22907(class_7833.field_40718.rotationDegrees(l * (-9.785f)));
                            float m = item.method_7935(mc.field_1724) - ((mc.field_1724.method_6014() - tickDelta) + 1.0f);
                            float fx2 = m / 10.0f;
                            if (fx2 > 1.0f) {
                                fx2 = 1.0f;
                            }
                            if (fx2 > 0.1f) {
                                float gx3 = class_3532.method_15374((m - 0.1f) * 1.3f);
                                float h4 = fx2 - 0.1f;
                                float j2 = gx3 * h4;
                                matrices.method_46416(j2 * 0.0f, j2 * 0.004f, j2 * 0.0f);
                            }
                            matrices.method_46416(0.0f, 0.0f, fx2 * 0.2f);
                            matrices.method_22905(1.0f, 1.0f, 1.0f + (fx2 * 0.2f));
                            matrices.method_22907(class_7833.field_40715.rotationDegrees(l * 45.0f));
                            break;
                        case 7:
                            applyBrushTransformation(matrices, tickDelta, arm, item, equipProgress);
                            break;
                    }
                } else if (player.method_6123()) {
                    applyEquipOffset(matrices, arm, equipProgress);
                    int l2 = bl22 ? 1 : -1;
                    matrices.method_46416(l2 * (-0.4f), 0.8f, 0.3f);
                    matrices.method_22907(class_7833.field_40716.rotationDegrees(l2 * 65.0f));
                    matrices.method_22907(class_7833.field_40718.rotationDegrees(l2 * (-85.0f)));
                } else if (arm == mc.field_1690.method_42552().method_41753() && isEnabled() && auraCheck()) {
                    handleSwordAnim(matrices, swingProgress, equipProgress, arm);
                } else {
                    float n = (-0.4f) * class_3532.method_15374(class_3532.method_15355(swingProgress) * 3.1415927f);
                    float mxx = 0.2f * class_3532.method_15374(class_3532.method_15355(swingProgress) * 6.2831855f);
                    float fxxx = (-0.2f) * class_3532.method_15374(swingProgress * 3.1415927f);
                    int o = bl22 ? 1 : -1;
                    matrices.method_46416(o * n, mxx, fxxx);
                    applyEquipOffset(matrices, arm, equipProgress);
                    applySwingOffset(matrices, arm, swingProgress);
                }
                applyItem360(matrices, arm);
                renderItem(player, item, bl22 ? class_811.field_4322 : class_811.field_4321, !bl22, matrices, vertexConsumers, light);
            }
            matrices.method_22909();
        }
    }

    /* JADX INFO: renamed from: sweetie.leonware.client.features.modules.render.SwingAnimationModule$1, reason: invalid class name */
    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/SwingAnimationModule$1.class */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$net$minecraft$item$consume$UseAction = new int[class_1839.values().length];

        static {
            try {
                $SwitchMap$net$minecraft$item$consume$UseAction[class_1839.field_8952.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$net$minecraft$item$consume$UseAction[class_1839.field_8949.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$net$minecraft$item$consume$UseAction[class_1839.field_8950.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$net$minecraft$item$consume$UseAction[class_1839.field_8946.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$net$minecraft$item$consume$UseAction[class_1839.field_8953.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$net$minecraft$item$consume$UseAction[class_1839.field_8951.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$net$minecraft$item$consume$UseAction[class_1839.field_42717.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
        }
    }

    private void applyItem360(class_4587 matrices, class_1306 arm) {
        float angle;
        if (this.item360.getValue().booleanValue()) {
            boolean isRightArm = arm == class_1306.field_6183;
            if (!isRightArm || this.item360Right.getValue().booleanValue()) {
                if (isRightArm || this.item360Left.getValue().booleanValue()) {
                    if (this.item360NoTarget.getValue().booleanValue() && AuraModule.getInstance().target != null) {
                        return;
                    }
                    angle = (System.currentTimeMillis() / 4) % 360;
                    String axis = isRightArm ? this.item360RightAxis.getValue() : this.item360LeftAxis.getValue();
                    switch (axis) {
                        case "X":
                            matrices.method_22907(class_7833.field_40714.rotationDegrees(angle));
                            break;
                        case "Y":
                            matrices.method_22907(class_7833.field_40716.rotationDegrees(angle));
                            break;
                        case "Z":
                            matrices.method_22907(class_7833.field_40718.rotationDegrees(angle));
                            break;
                    }
                }
            }
        }
    }

    private void applyBrushTransformation(class_4587 matrices, float tickDelta, class_1306 arm, class_1799 stack, float equipProgress) {
        applyEquipOffset(matrices, arm, equipProgress);
        float f = mc.field_1724.method_6014() % 10;
        float g = (f - tickDelta) + 1.0f;
        float h = 1.0f - (g / 10.0f);
        float n = (-15.0f) + (75.0f * class_3532.method_15362(h * 2.0f * 3.1415927f));
        if (arm != class_1306.field_6183) {
            matrices.method_22904(0.1d, 0.83d, 0.35d);
            matrices.method_22907(class_7833.field_40714.rotationDegrees(-80.0f));
            matrices.method_22907(class_7833.field_40716.rotationDegrees(-90.0f));
            matrices.method_22907(class_7833.field_40714.rotationDegrees(n));
            matrices.method_22904(-0.3d, 0.22d, 0.35d);
            return;
        }
        matrices.method_22904(-0.25d, 0.22d, 0.35d);
        matrices.method_22907(class_7833.field_40714.rotationDegrees(-80.0f));
        matrices.method_22907(class_7833.field_40716.rotationDegrees(90.0f));
        matrices.method_22907(class_7833.field_40718.rotationDegrees(0.0f));
        matrices.method_22907(class_7833.field_40714.rotationDegrees(n));
    }

    private void applyEatOrDrinkTransformation(class_4587 matrices, float tickDelta, class_1306 arm, class_1799 stack) {
        float f = (mc.field_1724.method_6014() - tickDelta) + 1.0f;
        float g = f / stack.method_7935(mc.field_1724);
        if (g < 0.8f) {
            matrices.method_46416(0.0f, class_3532.method_15379(class_3532.method_15362((f / 4.0f) * 3.1415927f) * 0.1f), 0.0f);
        }
        float h = 1.0f - ((float) Math.pow(g, 27.0d));
        int i = arm == class_1306.field_6183 ? 1 : -1;
        matrices.method_46416(h * 0.6f * i, h * (-0.5f), h * 0.0f);
        matrices.method_22907(class_7833.field_40716.rotationDegrees(i * h * 90.0f));
        matrices.method_22907(class_7833.field_40714.rotationDegrees(h * 10.0f));
        matrices.method_22907(class_7833.field_40718.rotationDegrees(i * h * 30.0f));
    }

    private void applyEquipOffset(class_4587 matrices, class_1306 arm, float equipProgress) {
        int i = arm == class_1306.field_6183 ? 1 : -1;
        matrices.method_46416(i * 0.56f, (-0.52f) + (equipProgress * (-0.6f)), -0.72f);
    }

    private void applySwingOffset(class_4587 matrices, class_1306 arm, float swingProgress) {
        int i = arm == class_1306.field_6183 ? 1 : -1;
        float f = class_3532.method_15374(swingProgress * swingProgress * 3.1415927f);
        float g = class_3532.method_15374(class_3532.method_15355(swingProgress) * 3.1415927f);
        matrices.method_22907(class_7833.field_40716.rotationDegrees(i * (45.0f + (f * (-20.0f)))));
        matrices.method_22907(class_7833.field_40718.rotationDegrees(i * g * (-20.0f)));
        matrices.method_22907(class_7833.field_40714.rotationDegrees(g * (-80.0f)));
        matrices.method_22907(class_7833.field_40716.rotationDegrees(i * (-45.0f)));
    }

    public void renderItem(class_1309 entity, class_1799 stack, class_811 renderMode, boolean leftHanded, class_4587 matrices, class_4597 vertexConsumers, int light) {
        if (!stack.method_7960()) {
            mc.method_1480().method_23177(entity, stack, renderMode, leftHanded, matrices, vertexConsumers, entity.method_37908(), light, class_4608.field_21444, entity.method_5628() + renderMode.ordinal());
        }
    }

    public boolean auraCheck() {
        return (this.auraOnly.getValue().booleanValue() && AuraModule.getInstance().target == null) ? false : true;
    }
}
