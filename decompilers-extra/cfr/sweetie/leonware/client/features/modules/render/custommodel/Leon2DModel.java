/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_10055
 *  net.minecraft.class_1921
 *  net.minecraft.class_5603
 *  net.minecraft.class_5606
 *  net.minecraft.class_5607
 *  net.minecraft.class_5609
 *  net.minecraft.class_5610
 *  net.minecraft.class_583
 *  net.minecraft.class_630
 */
package sweetie.leonware.client.features.modules.render.custommodel;

import net.minecraft.class_10055;
import net.minecraft.class_1921;
import net.minecraft.class_5603;
import net.minecraft.class_5606;
import net.minecraft.class_5607;
import net.minecraft.class_5609;
import net.minecraft.class_5610;
import net.minecraft.class_583;
import net.minecraft.class_630;

public class Leon2DModel
extends class_583<class_10055> {
    public final class_630 plane;

    public Leon2DModel(class_630 root) {
        super(root, class_1921::method_23578);
        this.plane = root.method_32086("plane");
    }

    public static class_5607 createTexturedModelData() {
        class_5609 modelData = new class_5609();
        class_5610 root = modelData.method_32111();
        root.method_32117("plane", class_5606.method_32108().method_32101(0, 0).method_32097(-8.0f, -16.0f, -0.5f, 16.0f, 32.0f, 1.0f), class_5603.method_32090((float)0.0f, (float)8.0f, (float)0.0f));
        return class_5607.method_32110((class_5609)modelData, (int)32, (int)64);
    }

    public void setAngles(class_10055 state) {
    }
}

