// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.render.nametags;

import java.util.HashSet;
import java.util.Arrays;
import sweetie.leonware.api.utils.color.UIColors;
import net.minecraft.class_1792;
import net.minecraft.class_1746;
import net.minecraft.class_1826;
import net.minecraft.class_1809;
import net.minecraft.class_1806;
import net.minecraft.class_1801;
import net.minecraft.class_1843;
import net.minecraft.class_1840;
import net.minecraft.class_1759;
import net.minecraft.class_1749;
import net.minecraft.class_1808;
import net.minecraft.class_1807;
import net.minecraft.class_1804;
import net.minecraft.class_1816;
import net.minecraft.class_5538;
import net.minecraft.class_7430;
import net.minecraft.class_1764;
import net.minecraft.class_1753;
import net.minecraft.class_1787;
import net.minecraft.class_1819;
import net.minecraft.class_8162;
import net.minecraft.class_1786;
import net.minecraft.class_1820;
import net.minecraft.class_1794;
import net.minecraft.class_1821;
import net.minecraft.class_1810;
import net.minecraft.class_9362;
import net.minecraft.class_1835;
import net.minecraft.class_1743;
import net.minecraft.class_1829;
import net.minecraft.class_1738;
import java.awt.Color;
import sweetie.leonware.api.utils.render.fonts.Fonts;
import net.minecraft.class_1887;
import java.util.Iterator;
import net.minecraft.class_9304;
import net.minecraft.class_6880;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.class_1890;
import net.minecraft.class_4587;
import java.util.List;
import sweetie.leonware.api.system.interfaces.QuickImports;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.class_308;
import net.minecraft.class_1802;
import java.util.Collection;
import java.util.ArrayList;
import net.minecraft.class_332;
import net.minecraft.class_1657;
import net.minecraft.class_1799;
import java.util.Set;

public class NameTagsItems
{
    private final NameTagsModule module;
    private static final Set<String> WHITELIST_ENCHANTS;
    
    public NameTagsItems(final NameTagsModule module) {
        this.module = module;
    }
    
    private String extractMagistraRoman(final class_1799 stack) {
        if (stack.method_7960()) {
            return null;
        }
        final String name = stack.method_7964().getString();
        if (!name.contains("\u043c\u0430\u0433\u0438\u0441\u0442\u0440\u0430") && !name.contains("\u041c\u0430\u0433\u0438\u0441\u0442\u0440\u0430")) {
            return null;
        }
        final String[] array;
        final String[] romans = array = new String[] { "X", "IX", "VIII", "VII", "VI", "V", "IV", "III", "II", "I" };
        for (int length = array.length, i = 0; i < length; ++i) {
            final String roman = array[i];
            if (name.endsWith(" " + roman) || name.contains(" " + roman)) {
                return roman;
            }
        }
        return null;
    }
    
    public void renderItems(final class_1657 entity, final float x, float y, final class_332 context) {
        final List<class_1799> items = new ArrayList<class_1799>();
        final class_4587 matrixStack = context.method_51448();
        items.add(entity.method_6047().method_7972());
        items.add(entity.method_6079().method_7972());
        if (!this.module.options.isEnabled("Only hands")) {
            items.addAll((Collection<? extends class_1799>)entity.method_31548().field_7548);
        }
        items.removeIf(itemStack -> itemStack.method_7909() == class_1802.field_8162);
        final int itemsSize = items.size();
        final float scale = this.module.scale.getValue();
        final float gap = 2.0f * scale;
        final float itemSize = 14.0f * scale;
        final float itemSpacing = itemSize + gap * 2.0f;
        final float totalWidth = itemsSize * itemSize + (itemsSize - 1) * gap;
        final float startX = x - totalWidth / 2.0f;
        y -= itemSize + gap * 2.0f;
        for (int i = 0; i < itemsSize; ++i) {
            matrixStack.method_22903();
            matrixStack.method_46416(startX + i * itemSpacing, y, 0.0f);
            matrixStack.method_22905(scale, scale, 1.0f);
            class_308.method_24210();
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            final class_1799 stack = items.get(i);
            context.method_51427(stack, 0, 0);
            if (!this.module.zako64Spoof.getValue() || !this.shouldSpoofCount(stack)) {
                context.method_51431(QuickImports.mc.field_1772, stack, 0, 0);
            }
            float enchantHeight = 0.0f;
            if (this.module.options.isEnabled("Enchants")) {
                final int enchantCount = this.getEnchantmentCount(stack);
                enchantHeight = enchantCount * 5.5f * scale;
            }
            final String magistraRoman = this.extractMagistraRoman(stack);
            if (magistraRoman != null) {
                this.renderMagistraRoman(matrixStack, magistraRoman, itemSize / 2.0f, -itemSize / 2.0f - gap - enchantHeight);
            }
            if (this.module.options.isEnabled("Enchants")) {
                this.renderEnchantments(stack, matrixStack, -gap, -itemSize / 2.0f);
            }
            matrixStack.method_22909();
        }
    }
    
    private int getEnchantmentCount(final class_1799 stack) {
        int count = 0;
        if (stack.method_7942()) {
            final class_9304 enchantments = class_1890.method_57532(stack);
            for (final Object2IntMap.Entry<class_6880<class_1887>> entry : enchantments.method_57539()) {
                final class_6880<class_1887> enchantment = (class_6880<class_1887>)entry.getKey();
                if (NameTagsItems.WHITELIST_ENCHANTS.contains(enchantment.method_55840())) {
                    ++count;
                }
            }
        }
        return count;
    }
    
    private void renderMagistraRoman(final class_4587 matrices, final String roman, final float x, final float y) {
        final float fontSize = 8.5f * this.module.scale.getValue();
        final float textWidth = Fonts.PS_MEDIUM.getWidth(roman, fontSize);
        final float centeredX = x - textWidth / 2.0f;
        final Color colorStart = new Color(255, 165, 0);
        final Color colorEnd = new Color(255, 80, 0);
        final float t = 0.5f + 0.5f * (float)Math.sin(System.currentTimeMillis() / 600.0);
        final int r = (int)(colorStart.getRed() + t * (colorEnd.getRed() - colorStart.getRed()));
        final int g = (int)(colorStart.getGreen() + t * (colorEnd.getGreen() - colorStart.getGreen()));
        final int b = (int)(colorStart.getBlue() + t * (colorEnd.getBlue() - colorStart.getBlue()));
        Fonts.PS_MEDIUM.drawText(matrices, roman, centeredX, y, fontSize, new Color(r, g, b));
    }
    
    private boolean shouldSpoofCount(final class_1799 stack) {
        if (stack.method_7960()) {
            return false;
        }
        final class_1792 item = stack.method_7909();
        return stack.method_7909() instanceof class_1738 || stack.method_7909() instanceof class_1829 || stack.method_7909() instanceof class_1743 || stack.method_7909() instanceof class_1835 || stack.method_7909() instanceof class_9362 || stack.method_7909() instanceof class_1810 || stack.method_7909() instanceof class_1821 || stack.method_7909() instanceof class_1794 || stack.method_7909() instanceof class_1820 || stack.method_7909() instanceof class_1786 || stack.method_7909() instanceof class_8162 || stack.method_7909() instanceof class_1819 || stack.method_7909() instanceof class_1787 || stack.method_7909() instanceof class_1753 || stack.method_7909() instanceof class_1764 || stack.method_7909() instanceof class_7430 || stack.method_7909() instanceof class_5538 || stack.method_7909() instanceof class_1816 || stack.method_7909() instanceof class_1804 || stack.method_7909() instanceof class_1807 || stack.method_7909() instanceof class_1808 || stack.method_7909() instanceof class_1749 || stack.method_7909() instanceof class_1759 || stack.method_7909() instanceof class_1840 || stack.method_7909() instanceof class_1843 || stack.method_7909() instanceof class_1801 || stack.method_7909() instanceof class_1806 || stack.method_7909() instanceof class_1809 || stack.method_7909() instanceof class_1826 || stack.method_7909() instanceof class_1746 || item == class_1802.field_18138 || item == class_1802.field_8578 || item == class_1802.field_8560 || item == class_1802.field_8807 || item == class_1802.field_47831 || item == class_1802.field_8547 || item == class_1802.field_49814 || item == class_1802.field_8868 || item == class_1802.field_8884 || item == class_1802.field_42716 || item == class_1802.field_8833 || stack.method_7914() == 1;
    }
    
    public void renderSpecialItems(final class_1657 player, final float x, float y, final class_332 context) {
        final List<class_1799> specialItems = new ArrayList<class_1799>();
        final class_4587 matrixStack = context.method_51448();
        for (int i = 0; i < player.method_31548().method_5439(); ++i) {
            final class_1799 stack = player.method_31548().method_5438(i);
            final String itemName = stack.method_7964().getString();
            final boolean isTalik = (stack.method_7909() == class_1802.field_8288 || stack.method_7909() == class_1802.field_8575 || stack.method_7909() == class_1802.field_8882) && (itemName.contains("\u0421\u0444\u0435\u0440\u0430") || itemName.contains("\u0420\u0443\u043d\u0430") || itemName.contains("\u0428\u0430\u0440") || itemName.contains("\u0422\u0430\u043b\u0438\u0441\u043c\u0430\u043d"));
            final boolean isAngelElytra = itemName.contains("\u041a\u0440\u044b\u043b\u044c\u044f \u0430\u043d\u0433\u0435\u043b\u0430") && stack.method_7909() == class_1802.field_8833;
            final boolean isKrush = itemName.contains("\u041a\u0440\u0443\u0448");
            if (isAngelElytra || isKrush || isTalik) {
                specialItems.add(stack.method_7972());
            }
        }
        if (specialItems.isEmpty()) {
            return;
        }
        final float scale = this.module.scale.getValue();
        final float gap = 2.0f * scale;
        final float textSize = 7.0f * scale;
        y += 5.0f * scale;
        for (int j = 0; j < specialItems.size(); ++j) {
            final String itemName2 = specialItems.get(j).method_7964().getString();
            final float textWidth = Fonts.PS_MEDIUM.getWidth(itemName2, textSize);
            final float itemY = y + j * (gap * 2.0f + textSize);
            Fonts.PS_MEDIUM.drawText(matrixStack, itemName2, x - textWidth / 2.0f, itemY + gap / 1.5f, textSize, new Color(255, 255, 255));
            y += 2.0f;
        }
    }
    
    private void renderEnchantments(final class_1799 stack, final class_4587 matrices, final float x, final float y) {
        float offsetY = 0.0f;
        final float fontSize = 5.5f * this.module.scale.getValue();
        if (stack.method_7942()) {
            final class_9304 enchantments = class_1890.method_57532(stack);
            for (Object2IntMap.Entry<class_6880<class_1887>> entry : enchantments.method_57539()) {
                final class_6880<class_1887> enchantment = (class_6880<class_1887>)entry.getKey();
                if (!NameTagsItems.WHITELIST_ENCHANTS.contains(enchantment.method_55840())) {
                    continue;
                }
                final int level = entry.getIntValue();
                final int max = ((class_1887)enchantment.comp_349()).method_8183();
                String shortName = enchantment.method_55840();
                shortName = shortName.substring(shortName.indexOf(58) + 1, Math.min(shortName.indexOf(58) + 4, shortName.length()));
                shortName = shortName.substring(0, 1).toUpperCase() + shortName.substring(1);
                final Color color = (level < max + 1) ? UIColors.textColor() : UIColors.negativeColor();
                Fonts.PS_MEDIUM.drawText(matrices, shortName + " " + level, x, y + offsetY, fontSize, color);
                offsetY -= fontSize;
            }
        }
    }
    
    static {
        WHITELIST_ENCHANTS = new HashSet<String>(Arrays.asList("minecraft:protection", "minecraft:fire_protection", "minecraft:blast_protection", "minecraft:projectile_protection", "minecraft:thorns", "minecraft:sharpness", "minecraft:fire_aspect", "minecraft:knockback", "minecraft:looting", "minecraft:unbreaking", "minecraft:efficiency", "minecraft:power", "minecraft:punch", "minecraft:infinity", "minecraft:mending"));
    }
}
