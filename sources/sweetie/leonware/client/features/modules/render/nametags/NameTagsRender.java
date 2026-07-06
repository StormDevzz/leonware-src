package sweetie.leonware.client.features.modules.render.nametags;

import java.awt.Color;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.minecraft.class_124;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2561;
import net.minecraft.class_2583;
import net.minecraft.class_268;
import net.minecraft.class_332;
import net.minecraft.class_3532;
import net.minecraft.class_4587;
import net.minecraft.class_5250;
import net.minecraft.class_5348;
import net.minecraft.class_640;
import net.minecraft.class_746;
import org.joml.Vector2f;
import sweetie.leonware.api.event.events.render.Render2DEvent;
import sweetie.leonware.api.system.configs.FriendManager;
import sweetie.leonware.api.system.interfaces.QuickImports;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.math.ProjectionUtil;
import sweetie.leonware.api.utils.render.Render2DEngine;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.api.utils.render.display.BoxRender;
import sweetie.leonware.api.utils.render.display.WorldRender;
import sweetie.leonware.api.utils.render.fonts.Fonts;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/nametags/NameTagsRender.class */
public class NameTagsRender implements QuickImports {
    private final NameTagsModule module;
    private final NameTagsItems nameTagsItems;
    private final NameTagsPotions nameTagsPotions;
    private static final Map<String, String> PRIVILEGE_MAPPING = new HashMap();
    private static final Map<String, Color> PRIVILEGE_COLORS = new HashMap();
    private static final Map<String, Color> PRIVILEGE_COLORS_SECOND = new HashMap();

    static {
        PRIVILEGE_MAPPING.put("hydra", "Гидра");
        PRIVILEGE_MAPPING.put("cerberus", "Цербер");
        PRIVILEGE_MAPPING.put("triton", "Тритон");
        PRIVILEGE_MAPPING.put("phoenix", "Феникс");
        PRIVILEGE_MAPPING.put("pandar", "Пандар");
        PRIVILEGE_MAPPING.put("heat", "Жара");
        PRIVILEGE_MAPPING.put("cold", "Холод");
        PRIVILEGE_MAPPING.put("kronos", "Кронос");
        PRIVILEGE_MAPPING.put("summer", "Лето");
        PRIVILEGE_MAPPING.put("winter", "Зима");
        PRIVILEGE_MAPPING.put("phobos", "Фобос");
        PRIVILEGE_MAPPING.put("ares", "Арес");
        PRIVILEGE_MAPPING.put("aristocrat", "Аристократ");
        PRIVILEGE_MAPPING.put("youtuber", "Ютубер");
        PRIVILEGE_MAPPING.put("helper", "Хелпер");
        PRIVILEGE_MAPPING.put("shelper", "Ст. Хелпер");
        PRIVILEGE_MAPPING.put("moder", "Модер");
        PRIVILEGE_MAPPING.put("smoder", "Ст. Модер");
        PRIVILEGE_MAPPING.put("admin", "Админ");
        PRIVILEGE_COLORS.put("hydra", new Color(6225797));
        PRIVILEGE_COLORS.put("cerberus", new Color(4360086));
        PRIVILEGE_COLORS.put("triton", new Color(4286945));
        PRIVILEGE_COLORS.put("phoenix", new Color(16729344));
        PRIVILEGE_COLORS.put("pandar", new Color(14423100));
        PRIVILEGE_COLORS.put("heat", new Color(16737095));
        PRIVILEGE_COLORS.put("cold", new Color(8900331));
        PRIVILEGE_COLORS.put("kronos", new Color(6578687));
        PRIVILEGE_COLORS.put("summer", new Color(16753920));
        PRIVILEGE_COLORS.put("winter", new Color(11393254));
        PRIVILEGE_COLORS.put("phobos", new Color(16084007));
        PRIVILEGE_COLORS.put("ares", new Color(8910119));
        PRIVILEGE_COLORS.put("aristocrat", new Color(10289407));
        PRIVILEGE_COLORS.put("youtuber", new Color(16738740));
        PRIVILEGE_COLORS.put("helper", new Color(65280));
        PRIVILEGE_COLORS.put("shelper", new Color(16776960));
        PRIVILEGE_COLORS.put("moder", new Color(8388736));
        PRIVILEGE_COLORS.put("smoder", new Color(255));
        PRIVILEGE_COLORS.put("admin", new Color(16711680));
        PRIVILEGE_COLORS_SECOND.put("hydra", new Color(8900331));
        PRIVILEGE_COLORS_SECOND.put("cerberus", new Color(6225797));
        PRIVILEGE_COLORS_SECOND.put("triton", new Color(6578687));
        PRIVILEGE_COLORS_SECOND.put("phoenix", new Color(16753920));
        PRIVILEGE_COLORS_SECOND.put("pandar", new Color(16737095));
        PRIVILEGE_COLORS_SECOND.put("heat", new Color(16753920));
        PRIVILEGE_COLORS_SECOND.put("cold", new Color(4360086));
        PRIVILEGE_COLORS_SECOND.put("kronos", new Color(11393254));
        PRIVILEGE_COLORS_SECOND.put("summer", new Color(16729344));
        PRIVILEGE_COLORS_SECOND.put("winter", new Color(8900331));
        PRIVILEGE_COLORS_SECOND.put("phobos", new Color(16729344));
        PRIVILEGE_COLORS_SECOND.put("ares", new Color(4286945));
        PRIVILEGE_COLORS_SECOND.put("aristocrat", new Color(16084007));
        PRIVILEGE_COLORS_SECOND.put("youtuber", new Color(16753920));
        PRIVILEGE_COLORS_SECOND.put("helper", new Color(8388736));
        PRIVILEGE_COLORS_SECOND.put("shelper", new Color(16737095));
        PRIVILEGE_COLORS_SECOND.put("moder", new Color(255));
        PRIVILEGE_COLORS_SECOND.put("smoder", new Color(16711680));
        PRIVILEGE_COLORS_SECOND.put("admin", new Color(255));
    }

    public NameTagsRender(NameTagsModule module) {
        this.module = module;
        this.nameTagsItems = new NameTagsItems(module);
        this.nameTagsPotions = new NameTagsPotions(module);
    }

    public void onRender(Render2DEvent.Render2DEventData event) {
        for (class_746 class_746Var : mc.field_1687.method_18112()) {
            if (class_746Var instanceof class_1657) {
                class_746 class_746Var2 = (class_1657) class_746Var;
                if (this.module.entityFilter.isValid(class_746Var2) || (class_746Var2 == mc.field_1724 && this.module.targets.isEnabled("Self") && !mc.field_1690.method_31044().method_31034())) {
                    renderTag(class_746Var2, event.context(), event.partialTicks());
                }
            }
        }
    }

    private void renderTag(class_1297 entity, class_332 context, float partialTicks) {
        double xI = class_3532.method_16436(partialTicks, entity.field_6014, entity.method_23317());
        double yI = class_3532.method_16436(partialTicks, entity.field_6036, entity.method_23318());
        double zI = class_3532.method_16436(partialTicks, entity.field_5969, entity.method_23321());
        if (this.module.box3d.getValue().booleanValue()) {
            render3DBox(entity, xI, yI, zI);
        }
        class_238 box = entity.method_5829().method_989(xI - entity.method_23317(), yI - entity.method_23318(), zI - entity.method_23321());
        float minX = Float.MAX_VALUE;
        float minY = Float.MAX_VALUE;
        float maxX = Float.MIN_VALUE;
        float maxY = Float.MIN_VALUE;
        for (int i = 0; i < 8; i++) {
            double cornerX = i % 2 == 0 ? box.field_1323 : box.field_1320;
            double cornerY = (i / 2) % 2 == 0 ? box.field_1322 : box.field_1325;
            double cornerZ = (i / 4) % 2 == 0 ? box.field_1321 : box.field_1324;
            Vector2f projected = ProjectionUtil.project(new class_243(cornerX, cornerY, cornerZ));
            minX = Math.min(minX, projected.x);
            minY = Math.min(minY, projected.y);
            maxX = Math.max(maxX, projected.x);
            maxY = Math.max(maxY, projected.y);
        }
        float scale = this.module.scale.getValue().floatValue();
        class_243 headPos = new class_243(xI, yI + ((double) entity.method_17682()), zI);
        class_243 headProj = WorldRender.worldSpaceToScreenSpace(headPos);
        if (headProj.field_1350 < 0.0d || headProj.field_1350 > 1.0d) {
            return;
        }
        float x = (float) headProj.field_1352;
        float y = ((float) headProj.field_1351) - (15.0f * scale);
        boolean inRegion = (x > 0.0f && x < ((float) mc.method_22683().method_4486())) || (y > 0.0f && y < ((float) mc.method_22683().method_4502()));
        if (inRegion) {
            renderName(entity, x, y, context);
            if (entity instanceof class_1657) {
                class_1657 player = (class_1657) entity;
                if (this.module.information.isEnabled("Items")) {
                    this.nameTagsItems.renderItems(player, x, y, context);
                }
                if (this.module.information.isEnabled("Potions")) {
                    this.nameTagsPotions.renderPotions(player, maxX + (2.0f * scale), minY, context);
                }
                if (this.module.options.isEnabled("Special items")) {
                    this.nameTagsItems.renderSpecialItems(player, x, maxY - (2.0f * scale), context);
                }
            }
        }
    }

    private void renderName(class_1297 entity, float x, float y, class_332 context) {
        float width;
        class_1657 class_1657Var;
        Color value;
        class_4587 matrixStack = context.method_51448();
        boolean isMcMode = this.module.textMode.is("Майнкрафт");
        String rawNameStr = entity.method_5477().getString();
        String strippedNameStr = class_124.method_539(rawNameStr);
        if (strippedNameStr == null) {
            strippedNameStr = rawNameStr;
        }
        class_5250 class_5250VarMethod_43470 = class_2561.method_43470(rawNameStr);
        float scale = this.module.scale.getValue().floatValue();
        float size = 8.0f * scale;
        float gap = 2.0f * scale;
        class_2561 mcPrefix = null;
        String privilegeLabel = "";
        Color privilegeColor = Color.WHITE;
        if (entity instanceof class_1657) {
            class_1657 player = (class_1657) entity;
            if (isMcMode) {
                mcPrefix = player.method_5781() != null ? player.method_5781().method_1144() : null;
            } else {
                String privilegeKey = getPlayerPrivilege(player);
                privilegeLabel = PRIVILEGE_MAPPING.getOrDefault(privilegeKey, "");
                privilegeColor = PRIVILEGE_COLORS.getOrDefault(privilegeKey, Color.WHITE);
            }
        }
        if (isMcMode) {
            width = mc.field_1772.method_27525(class_5250VarMethod_43470);
        } else {
            width = Fonts.SF_MEDIUM.getWidth(strippedNameStr, size);
        }
        float nameWidth = width;
        float prefixWidth = 0.0f;
        float spaceWidth = 0.0f;
        boolean hasPrefix = false;
        if (isMcMode && mcPrefix != null) {
            prefixWidth = mc.field_1772.method_27525(mcPrefix);
            spaceWidth = mc.field_1772.method_1727(" ");
            hasPrefix = prefixWidth > 0.5f;
        } else if (!isMcMode && !privilegeLabel.isEmpty()) {
            prefixWidth = Fonts.SF_MEDIUM.getWidth(privilegeLabel, size);
            spaceWidth = Fonts.SF_MEDIUM.getWidth(" ", size);
            hasPrefix = true;
        }
        String hpStr = "";
        class_5348 class_5348VarMethod_43470 = null;
        float hpWidth = 0.0f;
        if (entity instanceof class_1657) {
            class_1657 p = (class_1657) entity;
            class_1657Var = p;
        } else {
            class_1657Var = null;
        }
        class_1657 playerEntity = class_1657Var;
        if (playerEntity != null && this.module.hpMode.is("Цифры")) {
            float hp = playerEntity.method_6032();
            String formattedHp = String.format("%.1f", Float.valueOf(hp)).replace(',', '.');
            if (isMcMode) {
                String colorCode = hp > 15.0f ? "§a" : hp > 10.0f ? "§e" : hp > 5.0f ? "§6" : "§c";
                class_5348VarMethod_43470 = class_2561.method_43470(" " + colorCode + formattedHp);
                hpWidth = mc.field_1772.method_27525(class_5348VarMethod_43470);
            } else {
                hpStr = " " + formattedHp;
                hpWidth = Fonts.SF_MEDIUM.getWidth(hpStr, size);
            }
        }
        float textWidth = (hasPrefix ? prefixWidth + spaceWidth : 0.0f) + nameWidth + hpWidth;
        float totalWidth = textWidth + (gap * 2.0f);
        float totalHeight = size + (gap * 2.0f);
        float x2 = x - (totalWidth / 2.0f);
        if (!FriendManager.getInstance().contains(rawNameStr)) {
            value = this.module.color.getValue();
        } else {
            value = this.module.friendColor.getValue();
        }
        Color bgColor = value;
        if (this.module.rectMode.is("Блюр")) {
            RenderUtil.BLUR_RECT.draw(matrixStack, x2, y, totalWidth, totalHeight, scale, bgColor, 1.0f - this.module.glassy.getValue().floatValue());
        } else {
            Render2DEngine.drawThemeGradient(matrixStack, x2, y, totalWidth, totalHeight, 2.5f * scale);
        }
        if (playerEntity != null && this.module.hpMode.is("Полоска")) {
            float hpBarY = y + totalHeight + (1.5f * scale);
            float hpBarHeight = 2.5f * scale;
            Render2DEngine.drawHealthBar(matrixStack, x2, hpBarY, totalWidth, hpBarHeight, 1.25f * scale, playerEntity.method_6032() / playerEntity.method_6063());
        }
        float textX = x2 + gap;
        float textY = y + gap;
        if (isMcMode) {
            if (hasPrefix) {
                context.method_51439(mc.field_1772, mcPrefix, (int) textX, (int) textY, -1, true);
                textX += prefixWidth + spaceWidth;
            }
            context.method_51439(mc.field_1772, class_5250VarMethod_43470, (int) textX, (int) textY, this.module.textColor.getValue().getRGB(), true);
            if (class_5348VarMethod_43470 != null) {
                context.method_51439(mc.field_1772, class_5348VarMethod_43470, (int) (textX + nameWidth), (int) textY, -1, true);
                return;
            }
            return;
        }
        if (hasPrefix) {
            Color colorSecond = PRIVILEGE_COLORS_SECOND.getOrDefault(getPlayerPrivilege((class_1657) entity), privilegeColor);
            Fonts.SF_MEDIUM.drawGradientText(matrixStack, privilegeLabel, textX, textY, size, privilegeColor, colorSecond, 1.0f);
            textX += prefixWidth + spaceWidth;
        }
        Fonts.SF_MEDIUM.drawText(matrixStack, strippedNameStr, textX, textY, size, this.module.textColor.getValue());
        if (!hpStr.isEmpty()) {
            float pct = Math.max(0.0f, Math.min(1.0f, playerEntity.method_6032() / playerEntity.method_6063()));
            Color hpC = new Color((int) (255.0f * (1.0f - pct)), (int) (255.0f * pct), 0);
            Fonts.SF_MEDIUM.drawText(matrixStack, hpStr, textX + nameWidth, textY, size, hpC);
        }
    }

    private String getPlayerPrivilege(class_1657 player) {
        class_2561 displayName;
        String privilege;
        Set<String> validPrivileges = new HashSet<>(PRIVILEGE_MAPPING.keySet());
        class_640 entry = mc.field_1724.field_3944.method_2871(player.method_5667());
        if (entry != null && (displayName = entry.method_2971()) != null && (privilege = parsePrivilegeFromText(displayName, validPrivileges)) != null) {
            return privilege;
        }
        String privilege2 = parsePrivilegeFromTeam(player, validPrivileges);
        return privilege2 != null ? privilege2 : "default";
    }

    private String parsePrivilegeFromText(class_2561 text, Set<String> validPrivileges) {
        if (text == null) {
            return null;
        }
        class_2583 style = text.method_10866();
        if (style != null && style.method_27708() != null) {
            String fontId = style.method_27708().toString();
            if (fontId.contains("custom:groups/")) {
                String privilege = fontId.substring(fontId.lastIndexOf("/") + 1).toLowerCase();
                if (validPrivileges.contains(privilege)) {
                    return privilege;
                }
            }
        }
        String textString = text.getString();
        if (textString != null && !textString.isEmpty()) {
            for (String privilege2 : validPrivileges) {
                if (textString.toLowerCase().contains(privilege2)) {
                    return privilege2;
                }
            }
        }
        for (class_2561 sibling : text.method_10855()) {
            String result = parsePrivilegeFromText(sibling, validPrivileges);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    private String parsePrivilegeFromTeam(class_1657 player, Set<String> validPrivileges) {
        class_2561 prefix;
        class_268 team = player.method_5781();
        if (team != null && (prefix = team.method_1144()) != null) {
            String prefixString = prefix.getString();
            for (String privilege : validPrivileges) {
                if (prefixString.toLowerCase().contains(privilege)) {
                    return privilege;
                }
            }
            String privilegeFromPrefix = parsePrivilegeFromText(prefix, validPrivileges);
            if (privilegeFromPrefix != null) {
                return privilegeFromPrefix;
            }
            return null;
        }
        return null;
    }

    private void render3DBox(class_1297 entity, double x, double y, double z) {
        class_238 box = entity.method_5829().method_989(x - entity.method_23317(), y - entity.method_23318(), z - entity.method_23321());
        Color themeColor = UIColors.primary();
        float alpha = this.module.boxAlpha.getValue().floatValue();
        Color fillColor = new Color(themeColor.getRed(), themeColor.getGreen(), themeColor.getBlue(), (int) (alpha * 255.0f));
        Color outlineColor = new Color(themeColor.getRed(), themeColor.getGreen(), themeColor.getBlue(), 255);
        float x1 = (float) box.field_1323;
        float y1 = (float) box.field_1322;
        float z1 = (float) box.field_1321;
        float x2 = (float) box.field_1320;
        float y2 = (float) box.field_1325;
        float z2 = (float) box.field_1324;
        RenderUtil.BOX.drawBox(x1, y1, z1, x2, y2, z2, 1.5f, fillColor, BoxRender.Render.FILL, 0.0f);
        RenderUtil.BOX.drawBox(x1, y1, z1, x2, y2, z2, 1.5f, outlineColor, BoxRender.Render.OUTLINE, 0.0f);
    }
}
