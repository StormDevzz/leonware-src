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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/custommodel/FreddyBearModel.class */
public class FreddyBearModel extends class_583<class_10055> {
    private final class_630 fredbody;
    public final class_630 fredhead;
    public final class_630 armLeft;
    public final class_630 armRight;
    public final class_630 legLeft;
    public final class_630 legRight;

    public FreddyBearModel(class_630 root) {
        super(root, class_1921::method_23578);
        this.fredbody = root.method_32086("fredbody");
        this.fredhead = this.fredbody.method_32086("fredhead");
        this.armLeft = this.fredbody.method_32086("armLeft");
        this.armRight = this.fredbody.method_32086("armRight");
        this.legLeft = this.fredbody.method_32086("legLeft");
        this.legRight = this.fredbody.method_32086("legRight");
    }

    public static class_5607 createTexturedModelData() {
        class_5609 modelData = new class_5609();
        class_5610 root = modelData.method_32111();
        class_5610 fredbody = root.method_32117("fredbody", class_5606.method_32108().method_32101(0, 0).method_32098(-1.0f, -14.0f, -1.0f, 2.0f, 24.0f, 2.0f, new class_5605(0.0f)), class_5603.method_32090(0.0f, -9.0f, 0.0f));
        fredbody.method_32117("torso", class_5606.method_32108().method_32101(8, 0).method_32098(-6.0f, -9.0f, -4.0f, 12.0f, 18.0f, 8.0f, new class_5605(0.0f)), class_5603.method_32091(0.0f, 0.0f, 0.0f, 0.01745f, 0.0f, 0.0f));
        fredbody.method_32117("crotch", class_5606.method_32108().method_32101(56, 0).method_32098(-5.5f, 0.0f, -3.5f, 11.0f, 3.0f, 7.0f, new class_5605(0.0f)), class_5603.method_32090(0.0f, 9.5f, 0.0f));
        class_5610 legRight = fredbody.method_32117("legRight", class_5606.method_32108().method_32101(90, 8).method_32098(-1.0f, 0.0f, -1.0f, 2.0f, 10.0f, 2.0f, new class_5605(0.0f)), class_5603.method_32090(-3.3f, 12.5f, 0.0f));
        legRight.method_32117("legRightpad", class_5606.method_32108().method_32101(73, 33).method_32098(-3.0f, 0.0f, -3.0f, 6.0f, 9.0f, 6.0f, new class_5605(0.0f)), class_5603.method_32090(0.0f, 0.5f, 0.0f));
        class_5610 legRight2 = legRight.method_32117("legRight2", class_5606.method_32108().method_32101(20, 35).method_32098(-1.0f, 0.0f, -1.0f, 2.0f, 8.0f, 2.0f, new class_5605(0.0f)), class_5603.method_32091(0.0f, 9.6f, 0.0f, 0.0349f, 0.0f, 0.0f));
        legRight2.method_32117("legRightpad2", class_5606.method_32108().method_32101(0, 39).method_32098(-2.5f, 0.0f, -3.0f, 5.0f, 7.0f, 6.0f, new class_5605(0.0f)), class_5603.method_32090(0.0f, 0.5f, 0.0f));
        legRight2.method_32117("footRight", class_5606.method_32108().method_32101(22, 39).method_32098(-2.5f, 0.0f, -6.0f, 5.0f, 3.0f, 8.0f, new class_5605(0.0f)), class_5603.method_32091(0.0f, 8.0f, 0.0f, -0.0349f, 0.0f, 0.0f));
        class_5610 legLeft = fredbody.method_32117("legLeft", class_5606.method_32108().method_32101(54, 10).method_32098(-1.0f, 0.0f, -1.0f, 2.0f, 10.0f, 2.0f, new class_5605(0.0f)), class_5603.method_32090(3.3f, 12.5f, 0.0f));
        legLeft.method_32117("legLeftpad", class_5606.method_32108().method_32101(48, 39).method_32098(-3.0f, 0.0f, -3.0f, 6.0f, 9.0f, 6.0f, new class_5605(0.0f)), class_5603.method_32090(0.0f, 0.5f, 0.0f));
        class_5610 legLeft2 = legLeft.method_32117("legLeft2", class_5606.method_32108().method_32101(72, 48).method_32098(-1.0f, 0.0f, -1.0f, 2.0f, 8.0f, 2.0f, new class_5605(0.0f)), class_5603.method_32091(0.0f, 9.6f, 0.0f, 0.0349f, 0.0f, 0.0f));
        legLeft2.method_32117("legLeftpad2", class_5606.method_32108().method_32101(16, 50).method_32098(-2.5f, 0.0f, -3.0f, 5.0f, 7.0f, 6.0f, new class_5605(0.0f)), class_5603.method_32090(0.0f, 0.5f, 0.0f));
        legLeft2.method_32117("footLeft", class_5606.method_32108().method_32101(72, 50).method_32098(-2.5f, 0.0f, -6.0f, 5.0f, 3.0f, 8.0f, new class_5605(0.0f)), class_5603.method_32091(0.0f, 8.0f, 0.0f, -0.0349f, 0.0f, 0.0f));
        class_5610 armRight = fredbody.method_32117("armRight", class_5606.method_32108().method_32101(48, 0).method_32098(-1.0f, 0.0f, -1.0f, 2.0f, 10.0f, 2.0f, new class_5605(0.0f)), class_5603.method_32091(-6.5f, -8.0f, 0.0f, 0.0f, 0.0f, 0.2618f));
        armRight.method_32117("armRightpad", class_5606.method_32108().method_32101(70, 10).method_32098(-2.5f, 0.0f, -2.5f, 5.0f, 9.0f, 5.0f, new class_5605(0.0f)), class_5603.method_32090(0.0f, 0.5f, 0.0f));
        class_5610 armRight2 = armRight.method_32117("armRight2", class_5606.method_32108().method_32101(90, 20).method_32098(-1.0f, 0.0f, -1.0f, 2.0f, 8.0f, 2.0f, new class_5605(0.0f)), class_5603.method_32091(0.0f, 9.6f, 0.0f, -0.1745f, 0.0f, 0.0f));
        armRight2.method_32117("armRightpad2", class_5606.method_32108().method_32101(0, 26).method_32098(-2.5f, 0.0f, -2.5f, 5.0f, 7.0f, 5.0f, new class_5605(0.0f)), class_5603.method_32090(0.0f, 0.5f, 0.0f));
        armRight2.method_32117("handRight", class_5606.method_32108().method_32101(20, 26).method_32098(-2.0f, 0.0f, -2.5f, 4.0f, 4.0f, 5.0f, new class_5605(0.0f)), class_5603.method_32091(0.0f, 8.0f, 0.0f, 0.0f, 0.0f, -0.0523f));
        class_5610 armLeft = fredbody.method_32117("armLeft", class_5606.method_32108().method_32101(62, 10).method_32098(-1.0f, 0.0f, -1.0f, 2.0f, 10.0f, 2.0f, new class_5605(0.0f)), class_5603.method_32091(6.5f, -8.0f, 0.0f, 0.0f, 0.0f, -0.2618f));
        armLeft.method_32117("armLeftpad", class_5606.method_32108().method_32101(38, 54).method_32098(-2.5f, 0.0f, -2.5f, 5.0f, 9.0f, 5.0f, new class_5605(0.0f)), class_5603.method_32090(0.0f, 0.5f, 0.0f));
        class_5610 armLeft2 = armLeft.method_32117("armLeft2", class_5606.method_32108().method_32101(90, 48).method_32098(-1.0f, 0.0f, -1.0f, 2.0f, 8.0f, 2.0f, new class_5605(0.0f)), class_5603.method_32091(0.0f, 9.6f, 0.0f, -0.1745f, 0.0f, 0.0f));
        armLeft2.method_32117("armLeftpad2", class_5606.method_32108().method_32101(0, 58).method_32098(-2.5f, 0.0f, -2.5f, 5.0f, 7.0f, 5.0f, new class_5605(0.0f)), class_5603.method_32090(0.0f, 0.5f, 0.0f));
        armLeft2.method_32117("handLeft", class_5606.method_32108().method_32101(58, 56).method_32098(-1.0f, 0.0f, -2.5f, 4.0f, 4.0f, 5.0f, new class_5605(0.0f)), class_5603.method_32091(0.0f, 8.0f, 0.0f, 0.0f, 0.0f, 0.0523f));
        class_5610 fredhead = fredbody.method_32117("fredhead", class_5606.method_32108().method_32101(39, 22).method_32098(-5.5f, -8.0f, -4.5f, 11.0f, 8.0f, 9.0f, new class_5605(0.0f)), class_5603.method_32090(0.0f, -13.0f, -0.5f));
        fredhead.method_32117("frednose", class_5606.method_32108().method_32101(17, 67).method_32098(-4.0f, -2.0f, -3.0f, 8.0f, 4.0f, 3.0f, new class_5605(0.0f)), class_5603.method_32090(0.0f, -2.0f, -4.5f));
        fredhead.method_32117("jaw", class_5606.method_32108().method_32101(49, 65).method_32098(-5.0f, 0.0f, -4.5f, 10.0f, 3.0f, 9.0f, new class_5605(0.0f)), class_5603.method_32091(0.0f, 0.5f, 0.0f, 0.0872f, 0.0f, 0.0f));
        class_5610 earRight = fredhead.method_32117("earRight", class_5606.method_32108().method_32101(8, 0).method_32098(-1.0f, -3.0f, -0.5f, 2.0f, 3.0f, 1.0f, new class_5605(0.0f)), class_5603.method_32091(-4.5f, -5.5f, 0.0f, 0.0523f, 0.0f, -1.0472f));
        earRight.method_32117("earRightpad", class_5606.method_32108().method_32101(85, 0).method_32098(-2.0f, -5.0f, -1.0f, 4.0f, 4.0f, 2.0f, new class_5605(0.0f)), class_5603.method_32090(0.0f, -1.0f, 0.0f));
        class_5610 earLeft = fredhead.method_32117("earLeft", class_5606.method_32108().method_32101(40, 0).method_32098(-1.0f, -3.0f, -0.5f, 2.0f, 3.0f, 1.0f, new class_5605(0.0f)), class_5603.method_32091(4.5f, -5.5f, 0.0f, 0.0523f, 0.0f, 1.0472f));
        earLeft.method_32117("earRightpad_1", class_5606.method_32108().method_32101(40, 39).method_32098(-2.0f, -5.0f, -1.0f, 4.0f, 4.0f, 2.0f, new class_5605(0.0f)), class_5603.method_32090(0.0f, -1.0f, 0.0f));
        class_5610 hat = fredhead.method_32117("hat", class_5606.method_32108().method_32101(70, 24).method_32098(-3.0f, -0.5f, -3.0f, 6.0f, 1.0f, 6.0f, new class_5605(0.0f)), class_5603.method_32091(0.0f, -8.4f, 0.0f, -0.0174f, 0.0f, 0.0f));
        hat.method_32117("hat2", class_5606.method_32108().method_32101(78, 61).method_32098(-2.0f, -4.0f, -2.0f, 4.0f, 4.0f, 4.0f, new class_5605(0.0f)), class_5603.method_32091(0.0f, 0.1f, 0.0f, -0.0174f, 0.0f, 0.0f));
        return class_5607.method_32110(modelData, 100, 80);
    }

    /* JADX INFO: renamed from: setAngles, reason: merged with bridge method [inline-methods] */
    public void method_2819(class_10055 state) {
    }

    public void copyPoseFromBase(class_591 base) {
        this.fredhead.field_3654 = base.field_3398.field_3654;
        this.fredhead.field_3675 = base.field_3398.field_3675;
        this.fredhead.field_3674 = base.field_3398.field_3674;
        this.armLeft.field_3654 = base.field_27433.field_3654;
        this.armLeft.field_3675 = base.field_27433.field_3675;
        this.armLeft.field_3674 = base.field_27433.field_3674 - 0.2618f;
        this.armRight.field_3654 = base.field_3401.field_3654;
        this.armRight.field_3675 = base.field_3401.field_3675;
        this.armRight.field_3674 = base.field_3401.field_3674 + 0.2618f;
        this.legLeft.field_3654 = base.field_3397.field_3654;
        this.legLeft.field_3675 = base.field_3397.field_3675;
        this.legLeft.field_3674 = base.field_3397.field_3674;
        this.legRight.field_3654 = base.field_3392.field_3654;
        this.legRight.field_3675 = base.field_3392.field_3675;
        this.legRight.field_3674 = base.field_3392.field_3674;
    }
}
