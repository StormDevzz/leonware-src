package sweetie.leonware.client.features.modules.render.nametags;

import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/nametags/NameTagsItems.class */
public class NameTagsItems {
    private final NameTagsModule module;
    private static final Set<String> WHITELIST_ENCHANTS = new HashSet(Arrays.asList("minecraft:protection", "minecraft:fire_protection", "minecraft:blast_protection", "minecraft:projectile_protection", "minecraft:thorns", "minecraft:sharpness", "minecraft:fire_aspect", "minecraft:knockback", "minecraft:looting", "minecraft:unbreaking", "minecraft:efficiency", "minecraft:power", "minecraft:punch", "minecraft:infinity", "minecraft:mending"));

    public NameTagsItems(NameTagsModule module) {
        this.module = module;
    }

    private String extractMagistraRoman(class_1799 stack) {
        if (stack.method_7960()) {
            return null;
        }
        String name = stack.method_7964().getString();
        if (!name.contains("магистра") && !name.contains("Магистра")) {
            return null;
        }
        String[] romans = {"X", "IX", "VIII", "VII", "VI", "V", "IV", "III", "II", "I"};
        for (String roman : romans) {
            if (name.endsWith(" " + roman) || name.contains(" " + roman + " ")) {
                return roman;
            }
        }
        return null;
    }

    public void renderItems(class_1657 entity, float x, float y, class_332 context) {
        List<class_1799> items = new ArrayList<>();
        class_4587 matrixStack = context.method_51448();
        items.add(entity.method_6047().method_7972());
        items.add(entity.method_6079().method_7972());
        if (!this.module.options.isEnabled("Only hands")) {
            items.addAll(entity.method_31548().field_7548);
        }
        items.removeIf(itemStack -> {
            return itemStack.method_7909() == class_1802.field_8162;
        });
        int itemsSize = items.size();
        float scale = this.module.scale.getValue().floatValue();
        float gap = 2.0f * scale;
        float itemSize = 14.0f * scale;
        float itemSpacing = itemSize + (gap * 2.0f);
        float totalWidth = (itemsSize * itemSize) + ((itemsSize - 1) * gap);
        float startX = x - (totalWidth / 2.0f);
        float y2 = y - (itemSize + (gap * 2.0f));
        for (int i = 0; i < itemsSize; i++) {
            matrixStack.method_22903();
            matrixStack.method_46416(startX + (i * itemSpacing), y2, 0.0f);
            matrixStack.method_22905(scale, scale, 1.0f);
            class_308.method_24210();
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            class_1799 stack = items.get(i);
            context.method_51427(stack, 0, 0);
            if (!this.module.zako64Spoof.getValue().booleanValue() || !shouldSpoofCount(stack)) {
                context.method_51431(QuickImports.mc.field_1772, stack, 0, 0);
            }
            float enchantHeight = 0.0f;
            if (this.module.options.isEnabled("Enchants")) {
                int enchantCount = getEnchantmentCount(stack);
                enchantHeight = enchantCount * 5.5f * scale;
            }
            String magistraRoman = extractMagistraRoman(stack);
            if (magistraRoman != null) {
                renderMagistraRoman(matrixStack, magistraRoman, itemSize / 2.0f, (((-itemSize) / 2.0f) - gap) - enchantHeight);
            }
            if (this.module.options.isEnabled("Enchants")) {
                renderEnchantments(stack, matrixStack, -gap, (-itemSize) / 2.0f);
            }
            matrixStack.method_22909();
        }
    }

    private int getEnchantmentCount(class_1799 stack) {
        int count = 0;
        if (stack.method_7942()) {
            class_9304 enchantments = class_1890.method_57532(stack);
            for (Object2IntMap.Entry<class_6880<class_1887>> entry : enchantments.method_57539()) {
                class_6880<class_1887> enchantment = (class_6880) entry.getKey();
                if (WHITELIST_ENCHANTS.contains(enchantment.method_55840())) {
                    count++;
                }
            }
        }
        return count;
    }

    private void renderMagistraRoman(class_4587 matrices, String roman, float x, float y) {
        float fontSize = 8.5f * this.module.scale.getValue().floatValue();
        float textWidth = Fonts.PS_MEDIUM.getWidth(roman, fontSize);
        float centeredX = x - (textWidth / 2.0f);
        Color colorStart = new Color(255, 165, 0);
        Color colorEnd = new Color(255, 80, 0);
        float t = 0.5f + (0.5f * ((float) Math.sin(System.currentTimeMillis() / 600.0d)));
        int r = (int) (colorStart.getRed() + (t * (colorEnd.getRed() - colorStart.getRed())));
        int g = (int) (colorStart.getGreen() + (t * (colorEnd.getGreen() - colorStart.getGreen())));
        int b = (int) (colorStart.getBlue() + (t * (colorEnd.getBlue() - colorStart.getBlue())));
        Fonts.PS_MEDIUM.drawText(matrices, roman, centeredX, y, fontSize, new Color(r, g, b));
    }

    private boolean shouldSpoofCount(class_1799 stack) {
        if (stack.method_7960()) {
            return false;
        }
        class_1792 item = stack.method_7909();
        return (stack.method_7909() instanceof class_1738) || (stack.method_7909() instanceof class_1829) || (stack.method_7909() instanceof class_1743) || (stack.method_7909() instanceof class_1835) || (stack.method_7909() instanceof class_9362) || (stack.method_7909() instanceof class_1810) || (stack.method_7909() instanceof class_1821) || (stack.method_7909() instanceof class_1794) || (stack.method_7909() instanceof class_1820) || (stack.method_7909() instanceof class_1786) || (stack.method_7909() instanceof class_8162) || (stack.method_7909() instanceof class_1819) || (stack.method_7909() instanceof class_1787) || (stack.method_7909() instanceof class_1753) || (stack.method_7909() instanceof class_1764) || (stack.method_7909() instanceof class_7430) || (stack.method_7909() instanceof class_5538) || (stack.method_7909() instanceof class_1816) || (stack.method_7909() instanceof class_1804) || (stack.method_7909() instanceof class_1807) || (stack.method_7909() instanceof class_1808) || (stack.method_7909() instanceof class_1749) || (stack.method_7909() instanceof class_1759) || (stack.method_7909() instanceof class_1840) || (stack.method_7909() instanceof class_1843) || (stack.method_7909() instanceof class_1801) || (stack.method_7909() instanceof class_1806) || (stack.method_7909() instanceof class_1809) || (stack.method_7909() instanceof class_1826) || (stack.method_7909() instanceof class_1746) || item == class_1802.field_18138 || item == class_1802.field_8578 || item == class_1802.field_8560 || item == class_1802.field_8807 || item == class_1802.field_47831 || item == class_1802.field_8547 || item == class_1802.field_49814 || item == class_1802.field_8868 || item == class_1802.field_8884 || item == class_1802.field_42716 || item == class_1802.field_8833 || stack.method_7914() == 1;
    }

    public void renderSpecialItems(class_1657 player, float x, float y, class_332 context) {
        List<class_1799> specialItems = new ArrayList<>();
        class_4587 matrixStack = context.method_51448();
        for (int i = 0; i < player.method_31548().method_5439(); i++) {
            class_1799 stack = player.method_31548().method_5438(i);
            String itemName = stack.method_7964().getString();
            boolean isTalik = (stack.method_7909() == class_1802.field_8288 || stack.method_7909() == class_1802.field_8575 || stack.method_7909() == class_1802.field_8882) && (itemName.contains("Сфера") || itemName.contains("Руна") || itemName.contains("Шар") || itemName.contains("Талисман"));
            boolean isAngelElytra = itemName.contains("Крылья ангела") && stack.method_7909() == class_1802.field_8833;
            boolean isKrush = itemName.contains("Круш");
            if (isAngelElytra || isKrush || isTalik) {
                specialItems.add(stack.method_7972());
            }
        }
        if (specialItems.isEmpty()) {
            return;
        }
        float scale = this.module.scale.getValue().floatValue();
        float gap = 2.0f * scale;
        float textSize = 7.0f * scale;
        float y2 = y + (5.0f * scale);
        for (int i2 = 0; i2 < specialItems.size(); i2++) {
            String itemName2 = specialItems.get(i2).method_7964().getString();
            float textWidth = Fonts.PS_MEDIUM.getWidth(itemName2, textSize);
            float itemY = y2 + (i2 * ((gap * 2.0f) + textSize));
            Fonts.PS_MEDIUM.drawText(matrixStack, itemName2, x - (textWidth / 2.0f), itemY + (gap / 1.5f), textSize, new Color(255, 255, 255));
            y2 += 2.0f;
        }
    }

    private void renderEnchantments(class_1799 stack, class_4587 matrices, float x, float y) {
        float offsetY = 0.0f;
        float fontSize = 5.5f * this.module.scale.getValue().floatValue();
        if (stack.method_7942()) {
            class_9304 enchantments = class_1890.method_57532(stack);
            for (Object2IntMap.Entry<class_6880<class_1887>> entry : enchantments.method_57539()) {
                class_6880<class_1887> enchantment = (class_6880) entry.getKey();
                if (WHITELIST_ENCHANTS.contains(enchantment.method_55840())) {
                    int level = entry.getIntValue();
                    int max = ((class_1887) enchantment.comp_349()).method_8183();
                    String shortName = enchantment.method_55840();
                    String shortName2 = shortName.substring(shortName.indexOf(58) + 1, Math.min(shortName.indexOf(58) + 4, shortName.length()));
                    String shortName3 = shortName2.substring(0, 1).toUpperCase() + shortName2.substring(1);
                    Color color = level < max + 1 ? UIColors.textColor() : UIColors.negativeColor();
                    Fonts.PS_MEDIUM.drawText(matrices, shortName3 + " " + level, x, y + offsetY, fontSize, color);
                    offsetY -= fontSize;
                }
            }
        }
    }
}
