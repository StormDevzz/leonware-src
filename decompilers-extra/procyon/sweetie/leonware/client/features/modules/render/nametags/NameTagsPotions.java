// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.render.nametags;

import java.util.Iterator;
import net.minecraft.class_4587;
import java.awt.Color;
import sweetie.leonware.api.utils.render.fonts.Fonts;
import sweetie.leonware.api.utils.other.TextUtil;
import net.minecraft.class_1293;
import net.minecraft.class_332;
import net.minecraft.class_1657;

public class NameTagsPotions
{
    private final NameTagsModule module;
    
    public NameTagsPotions(final NameTagsModule module) {
        this.module = module;
    }
    
    public void renderPotions(final class_1657 player, final float x, float y, final class_332 context) {
        final class_4587 matrixStack = context.method_51448();
        if (player.method_6026().isEmpty()) {
            return;
        }
        final float scale = this.module.scale.getValue();
        final float gap = 2.0f * scale;
        final float fontSize = 7.0f * scale;
        for (class_1293 effect : player.method_6026()) {
            final String effectText = this.getEffectName(effect) + " " + TextUtil.getDurationText(effect.method_5584());
            Fonts.PS_MEDIUM.drawText(matrixStack, effectText, x + gap, y, fontSize, this.module.textColor.getValue());
            y += fontSize + gap;
        }
    }
    
    private String getEffectName(final class_1293 effect) {
        final String translationKey = effect.method_5586();
        String name = translationKey.substring(translationKey.lastIndexOf(".") + 1);
        name = name.substring(0, 1).toUpperCase() + name.substring(1);
        if (effect.method_5578() > 0) {
            name = name + " " + (effect.method_5578() + 1);
        }
        return name.replaceAll("_", " ");
    }
}
