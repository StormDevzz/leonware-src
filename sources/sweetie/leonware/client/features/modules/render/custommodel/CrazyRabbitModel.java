package sweetie.leonware.client.features.modules.render.custommodel;

import net.minecraft.class_10055;
import net.minecraft.class_1921;
import net.minecraft.class_5603;
import net.minecraft.class_5605;
import net.minecraft.class_5606;
import net.minecraft.class_5607;
import net.minecraft.class_5609;
import net.minecraft.class_5610;
import net.minecraft.class_583;
import net.minecraft.class_591;
import net.minecraft.class_630;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/custommodel/CrazyRabbitModel.class */
public class CrazyRabbitModel extends class_583<class_10055> {
    private final class_630 root;
    private final class_630 rabbitBone;
    public final class_630 head;
    public final class_630 leftArm;
    public final class_630 rightArm;
    public final class_630 leftLeg;
    public final class_630 rightLeg;

    public CrazyRabbitModel(class_630 root) {
        super(root, class_1921::method_23578);
        this.root = root;
        this.rabbitBone = root.method_32086("rabbit_bone");
        this.head = this.rabbitBone.method_32086("head");
        this.leftArm = this.rabbitBone.method_32086("left_arm");
        this.rightArm = this.rabbitBone.method_32086("right_arm");
        this.leftLeg = this.rabbitBone.method_32086("left_leg");
        this.rightLeg = this.rabbitBone.method_32086("right_leg");
    }

    public static class_5607 createTexturedModelData() {
        class_5609 modelData = new class_5609();
        class_5610 root = modelData.method_32111();
        class_5610 rabbitBone = root.method_32117("rabbit_bone", class_5606.method_32108().method_32101(28, 45).method_32098(-5.0f, -13.0f, -5.0f, 10.0f, 11.0f, 8.0f, new class_5605(0.0f)), class_5603.method_32090(0.0f, 24.0f, 0.0f));
        rabbitBone.method_32117("head", class_5606.method_32108().method_32101(0, 45).method_32098(-4.0f, -11.0f, -4.0f, 8.0f, 11.0f, 8.0f, new class_5605(0.0f)).method_32101(0, 0).method_32098(-3.0f, 0.0f, -4.0f, 6.0f, 1.0f, 6.0f, new class_5605(0.0f)).method_32101(56, 0).method_32098(-5.0f, -9.0f, -5.0f, 2.0f, 3.0f, 2.0f, new class_5605(0.0f)).method_32101(56, 0).method_32098(3.0f, -9.0f, -5.0f, 2.0f, 3.0f, 2.0f, new class_5605(0.0f)).method_32101(46, 0).method_32098(1.0f, -20.0f, 0.0f, 3.0f, 9.0f, 1.0f, new class_5605(0.0f)).method_32101(46, 0).method_32098(-4.0f, -20.0f, 0.0f, 3.0f, 9.0f, 1.0f, new class_5605(0.0f)), class_5603.method_32090(0.0f, -14.0f, -1.0f));
        rabbitBone.method_32117("left_arm", class_5606.method_32108().method_32101(0, 64).method_32098(0.0f, 0.0f, -2.0f, 2.0f, 8.0f, 4.0f, new class_5605(0.0f)), class_5603.method_32091(5.0f, -13.0f, -1.0f, 0.0f, 0.0f, -0.0873f));
        rabbitBone.method_32117("right_arm", class_5606.method_32108().method_32101(0, 64).method_32098(-2.0f, 0.0f, -2.0f, 2.0f, 8.0f, 4.0f, new class_5605(0.0f)), class_5603.method_32091(-5.0f, -13.0f, -1.0f, 0.0f, 0.0f, 0.0873f));
        rabbitBone.method_32117("left_leg", class_5606.method_32108().method_32101(0, 74).method_32098(-2.0f, 0.0f, -2.0f, 4.0f, 2.0f, 4.0f, new class_5605(0.0f)), class_5603.method_32090(3.0f, -2.0f, -1.0f));
        rabbitBone.method_32117("right_leg", class_5606.method_32108().method_32101(0, 74).method_32098(-2.0f, 0.0f, -2.0f, 4.0f, 2.0f, 4.0f, new class_5605(0.0f)), class_5603.method_32090(-3.0f, -2.0f, -1.0f));
        return class_5607.method_32110(modelData, 64, 64);
    }

    public void copyPoseFromBase(class_591 base) {
        this.head.field_3654 = base.field_3398.field_3654;
        this.head.field_3675 = base.field_3398.field_3675;
        this.head.field_3674 = base.field_3398.field_3674;
        this.leftArm.field_3654 = base.field_27433.field_3654;
        this.leftArm.field_3675 = base.field_27433.field_3675;
        this.leftArm.field_3674 = base.field_27433.field_3674 - 0.0873f;
        this.rightArm.field_3654 = base.field_3401.field_3654;
        this.rightArm.field_3675 = base.field_3401.field_3675;
        this.rightArm.field_3674 = base.field_3401.field_3674 + 0.0873f;
        this.leftLeg.field_3654 = base.field_3397.field_3654;
        this.leftLeg.field_3675 = base.field_3397.field_3675;
        this.leftLeg.field_3674 = base.field_3397.field_3674;
        this.rightLeg.field_3654 = base.field_3392.field_3654;
        this.rightLeg.field_3675 = base.field_3392.field_3675;
        this.rightLeg.field_3674 = base.field_3392.field_3674;
    }
}
