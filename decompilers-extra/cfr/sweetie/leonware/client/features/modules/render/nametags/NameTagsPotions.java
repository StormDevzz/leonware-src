/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1293
 *  net.minecraft.class_1657
 *  net.minecraft.class_332
 *  net.minecraft.class_4587
 */
package sweetie.leonware.client.features.modules.render.nametags;

import java.awt.Color;
import net.minecraft.class_1293;
import net.minecraft.class_1657;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import sweetie.leonware.api.utils.other.TextUtil;
import sweetie.leonware.api.utils.render.fonts.Fonts;
import sweetie.leonware.client.features.modules.render.nametags.NameTagsModule;

public class NameTagsPotions {
    private final NameTagsModule module;

    public NameTagsPotions(NameTagsModule module) {
        this.module = module;
    }

    public void renderPotions(class_1657 player, float x, float y, class_332 context) {
        class_4587 matrixStack = context.method_51448();
        if (player.method_6026().isEmpty()) {
            return;
        }
        float scale = ((Float)this.module.scale.getValue()).floatValue();
        float gap = 2.0f * scale;
        float fontSize = 7.0f * scale;
        for (class_1293 effect : player.method_6026()) {
            String effectText = this.getEffectName(effect) + " " + TextUtil.getDurationText(effect.method_5584());
            Fonts.PS_MEDIUM.drawText(matrixStack, effectText, x + gap, y, fontSize, (Color)this.module.textColor.getValue());
            y += fontSize + gap;
        }
    }

    private String getEffectName(class_1293 effect) {
        String translationKey = effect.method_5586();
        Object name = translationKey.substring(translationKey.lastIndexOf(".") + 1);
        name = ((String)name).substring(0, 1).toUpperCase() + ((String)name).substring(1);
        if (effect.method_5578() > 0) {
            name = (String)name + " " + (effect.method_5578() + 1);
        }
        return ((String)name).replaceAll("_", " ");
    }
}

