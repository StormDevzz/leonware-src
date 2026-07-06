/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  it.unimi.dsi.fastutil.objects.Object2IntMap$Entry
 *  net.minecraft.class_1657
 *  net.minecraft.class_1738
 *  net.minecraft.class_1743
 *  net.minecraft.class_1746
 *  net.minecraft.class_1749
 *  net.minecraft.class_1753
 *  net.minecraft.class_1759
 *  net.minecraft.class_1764
 *  net.minecraft.class_1786
 *  net.minecraft.class_1787
 *  net.minecraft.class_1792
 *  net.minecraft.class_1794
 *  net.minecraft.class_1799
 *  net.minecraft.class_1801
 *  net.minecraft.class_1802
 *  net.minecraft.class_1804
 *  net.minecraft.class_1806
 *  net.minecraft.class_1807
 *  net.minecraft.class_1808
 *  net.minecraft.class_1809
 *  net.minecraft.class_1810
 *  net.minecraft.class_1816
 *  net.minecraft.class_1819
 *  net.minecraft.class_1820
 *  net.minecraft.class_1821
 *  net.minecraft.class_1826
 *  net.minecraft.class_1829
 *  net.minecraft.class_1835
 *  net.minecraft.class_1840
 *  net.minecraft.class_1843
 *  net.minecraft.class_1887
 *  net.minecraft.class_1890
 *  net.minecraft.class_308
 *  net.minecraft.class_332
 *  net.minecraft.class_4587
 *  net.minecraft.class_5538
 *  net.minecraft.class_6880
 *  net.minecraft.class_7430
 *  net.minecraft.class_8162
 *  net.minecraft.class_9304
 *  net.minecraft.class_9362
 */
package sweetie.leonware.client.features.modules.render.nametags;

import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.class_1657;
import net.minecraft.class_1738;
import net.minecraft.class_1743;
import net.minecraft.class_1746;
import net.minecraft.class_1749;
import net.minecraft.class_1753;
import net.minecraft.class_1759;
import net.minecraft.class_1764;
import net.minecraft.class_1786;
import net.minecraft.class_1787;
import net.minecraft.class_1792;
import net.minecraft.class_1794;
import net.minecraft.class_1799;
import net.minecraft.class_1801;
import net.minecraft.class_1802;
import net.minecraft.class_1804;
import net.minecraft.class_1806;
import net.minecraft.class_1807;
import net.minecraft.class_1808;
import net.minecraft.class_1809;
import net.minecraft.class_1810;
import net.minecraft.class_1816;
import net.minecraft.class_1819;
import net.minecraft.class_1820;
import net.minecraft.class_1821;
import net.minecraft.class_1826;
import net.minecraft.class_1829;
import net.minecraft.class_1835;
import net.minecraft.class_1840;
import net.minecraft.class_1843;
import net.minecraft.class_1887;
import net.minecraft.class_1890;
import net.minecraft.class_308;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import net.minecraft.class_5538;
import net.minecraft.class_6880;
import net.minecraft.class_7430;
import net.minecraft.class_8162;
import net.minecraft.class_9304;
import net.minecraft.class_9362;
import sweetie.leonware.api.system.interfaces.QuickImports;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.render.fonts.Fonts;
import sweetie.leonware.client.features.modules.render.nametags.NameTagsModule;

public class NameTagsItems {
    private final NameTagsModule module;
    private static final Set<String> WHITELIST_ENCHANTS = new HashSet<String>(Arrays.asList("minecraft:protection", "minecraft:fire_protection", "minecraft:blast_protection", "minecraft:projectile_protection", "minecraft:thorns", "minecraft:sharpness", "minecraft:fire_aspect", "minecraft:knockback", "minecraft:looting", "minecraft:unbreaking", "minecraft:efficiency", "minecraft:power", "minecraft:punch", "minecraft:infinity", "minecraft:mending"));

    public NameTagsItems(NameTagsModule module) {
        this.module = module;
    }

    private String extractMagistraRoman(class_1799 stack) {
        String[] romans;
        if (stack.method_7960()) {
            return null;
        }
        String name = stack.method_7964().getString();
        if (!name.contains("\u043c\u0430\u0433\u0438\u0441\u0442\u0440\u0430") && !name.contains("\u041c\u0430\u0433\u0438\u0441\u0442\u0440\u0430")) {
            return null;
        }
        for (String roman : romans = new String[]{"X", "IX", "VIII", "VII", "VI", "V", "IV", "III", "II", "I"}) {
            if (!name.endsWith(" " + roman) && !name.contains(" " + roman + " ")) continue;
            return roman;
        }
        return null;
    }

    public void renderItems(class_1657 entity, float x, float y, class_332 context) {
        ArrayList<class_1799> items = new ArrayList<class_1799>();
        class_4587 matrixStack = context.method_51448();
        items.add(entity.method_6047().method_7972());
        items.add(entity.method_6079().method_7972());
        if (!this.module.options.isEnabled("Only hands")) {
            items.addAll((Collection<class_1799>)entity.method_31548().field_7548);
        }
        items.removeIf(itemStack -> itemStack.method_7909() == class_1802.field_8162);
        int itemsSize = items.size();
        float scale = ((Float)this.module.scale.getValue()).floatValue();
        float gap = 2.0f * scale;
        float itemSize = 14.0f * scale;
        float itemSpacing = itemSize + gap * 2.0f;
        float totalWidth = (float)itemsSize * itemSize + (float)(itemsSize - 1) * gap;
        float startX = x - totalWidth / 2.0f;
        y -= itemSize + gap * 2.0f;
        for (int i = 0; i < itemsSize; ++i) {
            String magistraRoman;
            matrixStack.method_22903();
            matrixStack.method_46416(startX + (float)i * itemSpacing, y, 0.0f);
            matrixStack.method_22905(scale, scale, 1.0f);
            class_308.method_24210();
            RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            class_1799 stack = (class_1799)items.get(i);
            context.method_51427(stack, 0, 0);
            if (!((Boolean)this.module.zako64Spoof.getValue()).booleanValue() || !this.shouldSpoofCount(stack)) {
                context.method_51431(QuickImports.mc.field_1772, stack, 0, 0);
            }
            float enchantHeight = 0.0f;
            if (this.module.options.isEnabled("Enchants")) {
                int enchantCount = this.getEnchantmentCount(stack);
                enchantHeight = (float)enchantCount * 5.5f * scale;
            }
            if ((magistraRoman = this.extractMagistraRoman(stack)) != null) {
                this.renderMagistraRoman(matrixStack, magistraRoman, itemSize / 2.0f, -itemSize / 2.0f - gap - enchantHeight);
            }
            if (this.module.options.isEnabled("Enchants")) {
                this.renderEnchantments(stack, matrixStack, -gap, -itemSize / 2.0f);
            }
            matrixStack.method_22909();
        }
    }

    private int getEnchantmentCount(class_1799 stack) {
        int count = 0;
        if (stack.method_7942()) {
            class_9304 enchantments = class_1890.method_57532((class_1799)stack);
            for (Object2IntMap.Entry entry : enchantments.method_57539()) {
                class_6880 enchantment = (class_6880)entry.getKey();
                if (!WHITELIST_ENCHANTS.contains(enchantment.method_55840())) continue;
                ++count;
            }
        }
        return count;
    }

    private void renderMagistraRoman(class_4587 matrices, String roman, float x, float y) {
        float fontSize = 8.5f * ((Float)this.module.scale.getValue()).floatValue();
        float textWidth = Fonts.PS_MEDIUM.getWidth(roman, fontSize);
        float centeredX = x - textWidth / 2.0f;
        Color colorStart = new Color(255, 165, 0);
        Color colorEnd = new Color(255, 80, 0);
        float t = 0.5f + 0.5f * (float)Math.sin((double)System.currentTimeMillis() / 600.0);
        int r = (int)((float)colorStart.getRed() + t * (float)(colorEnd.getRed() - colorStart.getRed()));
        int g = (int)((float)colorStart.getGreen() + t * (float)(colorEnd.getGreen() - colorStart.getGreen()));
        int b = (int)((float)colorStart.getBlue() + t * (float)(colorEnd.getBlue() - colorStart.getBlue()));
        Fonts.PS_MEDIUM.drawText(matrices, roman, centeredX, y, fontSize, new Color(r, g, b));
    }

    private boolean shouldSpoofCount(class_1799 stack) {
        if (stack.method_7960()) {
            return false;
        }
        class_1792 item = stack.method_7909();
        if (stack.method_7909() instanceof class_1738) {
            return true;
        }
        if (stack.method_7909() instanceof class_1829) {
            return true;
        }
        if (stack.method_7909() instanceof class_1743) {
            return true;
        }
        if (stack.method_7909() instanceof class_1835) {
            return true;
        }
        if (stack.method_7909() instanceof class_9362) {
            return true;
        }
        if (stack.method_7909() instanceof class_1810) {
            return true;
        }
        if (stack.method_7909() instanceof class_1821) {
            return true;
        }
        if (stack.method_7909() instanceof class_1794) {
            return true;
        }
        if (stack.method_7909() instanceof class_1820) {
            return true;
        }
        if (stack.method_7909() instanceof class_1786) {
            return true;
        }
        if (stack.method_7909() instanceof class_8162) {
            return true;
        }
        if (stack.method_7909() instanceof class_1819) {
            return true;
        }
        if (stack.method_7909() instanceof class_1787) {
            return true;
        }
        if (stack.method_7909() instanceof class_1753) {
            return true;
        }
        if (stack.method_7909() instanceof class_1764) {
            return true;
        }
        if (stack.method_7909() instanceof class_7430) {
            return true;
        }
        if (stack.method_7909() instanceof class_5538) {
            return true;
        }
        if (stack.method_7909() instanceof class_1816) {
            return true;
        }
        if (stack.method_7909() instanceof class_1804) {
            return true;
        }
        if (stack.method_7909() instanceof class_1807) {
            return true;
        }
        if (stack.method_7909() instanceof class_1808) {
            return true;
        }
        if (stack.method_7909() instanceof class_1749) {
            return true;
        }
        if (stack.method_7909() instanceof class_1759) {
            return true;
        }
        if (stack.method_7909() instanceof class_1840) {
            return true;
        }
        if (stack.method_7909() instanceof class_1843) {
            return true;
        }
        if (stack.method_7909() instanceof class_1801) {
            return true;
        }
        if (stack.method_7909() instanceof class_1806) {
            return true;
        }
        if (stack.method_7909() instanceof class_1809) {
            return true;
        }
        if (stack.method_7909() instanceof class_1826) {
            return true;
        }
        if (stack.method_7909() instanceof class_1746) {
            return true;
        }
        if (item == class_1802.field_18138) {
            return true;
        }
        if (item == class_1802.field_8578) {
            return true;
        }
        if (item == class_1802.field_8560) {
            return true;
        }
        if (item == class_1802.field_8807) {
            return true;
        }
        if (item == class_1802.field_47831) {
            return true;
        }
        if (item == class_1802.field_8547) {
            return true;
        }
        if (item == class_1802.field_49814) {
            return true;
        }
        if (item == class_1802.field_8868) {
            return true;
        }
        if (item == class_1802.field_8884) {
            return true;
        }
        if (item == class_1802.field_42716) {
            return true;
        }
        if (item == class_1802.field_8833) {
            return true;
        }
        return stack.method_7914() == 1;
    }

    public void renderSpecialItems(class_1657 player, float x, float y, class_332 context) {
        ArrayList<class_1799> specialItems = new ArrayList<class_1799>();
        class_4587 matrixStack = context.method_51448();
        for (int i = 0; i < player.method_31548().method_5439(); ++i) {
            class_1799 stack = player.method_31548().method_5438(i);
            String itemName = stack.method_7964().getString();
            boolean isTalik = !(stack.method_7909() != class_1802.field_8288 && stack.method_7909() != class_1802.field_8575 && stack.method_7909() != class_1802.field_8882 || !itemName.contains("\u0421\u0444\u0435\u0440\u0430") && !itemName.contains("\u0420\u0443\u043d\u0430") && !itemName.contains("\u0428\u0430\u0440") && !itemName.contains("\u0422\u0430\u043b\u0438\u0441\u043c\u0430\u043d"));
            boolean isAngelElytra = itemName.contains("\u041a\u0440\u044b\u043b\u044c\u044f \u0430\u043d\u0433\u0435\u043b\u0430") && stack.method_7909() == class_1802.field_8833;
            boolean isKrush = itemName.contains("\u041a\u0440\u0443\u0448");
            if (!isAngelElytra && !isKrush && !isTalik) continue;
            specialItems.add(stack.method_7972());
        }
        if (specialItems.isEmpty()) {
            return;
        }
        float scale = ((Float)this.module.scale.getValue()).floatValue();
        float gap = 2.0f * scale;
        float textSize = 7.0f * scale;
        y += 5.0f * scale;
        for (int i = 0; i < specialItems.size(); ++i) {
            String itemName = ((class_1799)specialItems.get(i)).method_7964().getString();
            float textWidth = Fonts.PS_MEDIUM.getWidth(itemName, textSize);
            float itemY = y + (float)i * (gap * 2.0f + textSize);
            Fonts.PS_MEDIUM.drawText(matrixStack, itemName, x - textWidth / 2.0f, itemY + gap / 1.5f, textSize, new Color(255, 255, 255));
            y += 2.0f;
        }
    }

    private void renderEnchantments(class_1799 stack, class_4587 matrices, float x, float y) {
        float offsetY = 0.0f;
        float fontSize = 5.5f * ((Float)this.module.scale.getValue()).floatValue();
        if (stack.method_7942()) {
            class_9304 enchantments = class_1890.method_57532((class_1799)stack);
            for (Object2IntMap.Entry entry : enchantments.method_57539()) {
                class_6880 enchantment = (class_6880)entry.getKey();
                if (!WHITELIST_ENCHANTS.contains(enchantment.method_55840())) continue;
                int level = entry.getIntValue();
                int max = ((class_1887)enchantment.comp_349()).method_8183();
                Object shortName = enchantment.method_55840();
                shortName = ((String)shortName).substring(((String)shortName).indexOf(58) + 1, Math.min(((String)shortName).indexOf(58) + 4, ((String)shortName).length()));
                shortName = ((String)shortName).substring(0, 1).toUpperCase() + ((String)shortName).substring(1);
                Color color = level < max + 1 ? UIColors.textColor() : UIColors.negativeColor();
                Fonts.PS_MEDIUM.drawText(matrices, (String)shortName + " " + level, x, y + offsetY, fontSize, color);
                offsetY -= fontSize;
            }
        }
    }
}

