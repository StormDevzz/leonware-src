/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_124
 *  net.minecraft.class_1297
 *  net.minecraft.class_1309
 *  net.minecraft.class_1657
 *  net.minecraft.class_238
 *  net.minecraft.class_243
 *  net.minecraft.class_2561
 *  net.minecraft.class_2583
 *  net.minecraft.class_268
 *  net.minecraft.class_332
 *  net.minecraft.class_3532
 *  net.minecraft.class_4587
 *  net.minecraft.class_5250
 *  net.minecraft.class_5348
 *  net.minecraft.class_640
 *  org.joml.Vector2f
 */
package sweetie.leonware.client.features.modules.render.nametags;

import java.awt.Color;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.minecraft.class_124;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
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
import sweetie.leonware.client.features.modules.render.nametags.NameTagsItems;
import sweetie.leonware.client.features.modules.render.nametags.NameTagsModule;
import sweetie.leonware.client.features.modules.render.nametags.NameTagsPotions;

public class NameTagsRender
implements QuickImports {
    private final NameTagsModule module;
    private final NameTagsItems nameTagsItems;
    private final NameTagsPotions nameTagsPotions;
    private static final Map<String, String> PRIVILEGE_MAPPING = new HashMap<String, String>();
    private static final Map<String, Color> PRIVILEGE_COLORS = new HashMap<String, Color>();
    private static final Map<String, Color> PRIVILEGE_COLORS_SECOND = new HashMap<String, Color>();

    public NameTagsRender(NameTagsModule module) {
        this.module = module;
        this.nameTagsItems = new NameTagsItems(module);
        this.nameTagsPotions = new NameTagsPotions(module);
    }

    public void onRender(Render2DEvent.Render2DEventData event) {
        for (class_1297 entity1 : NameTagsRender.mc.field_1687.method_18112()) {
            class_1657 player;
            if (!(entity1 instanceof class_1657) || !this.module.entityFilter.isValid((class_1309)(player = (class_1657)entity1)) && (player != NameTagsRender.mc.field_1724 || !this.module.targets.isEnabled("Self") || NameTagsRender.mc.field_1690.method_31044().method_31034())) continue;
            this.renderTag((class_1297)player, event.context(), event.partialTicks());
        }
    }

    private void renderTag(class_1297 entity, class_332 context, float partialTicks) {
        boolean inRegion;
        double xI = class_3532.method_16436((double)partialTicks, (double)entity.field_6014, (double)entity.method_23317());
        double yI = class_3532.method_16436((double)partialTicks, (double)entity.field_6036, (double)entity.method_23318());
        double zI = class_3532.method_16436((double)partialTicks, (double)entity.field_5969, (double)entity.method_23321());
        if (((Boolean)this.module.box3d.getValue()).booleanValue()) {
            this.render3DBox(entity, xI, yI, zI);
        }
        class_238 box = entity.method_5829().method_989(xI - entity.method_23317(), yI - entity.method_23318(), zI - entity.method_23321());
        float minX = Float.MAX_VALUE;
        float minY = Float.MAX_VALUE;
        float maxX = Float.MIN_VALUE;
        float maxY = Float.MIN_VALUE;
        for (int i = 0; i < 8; ++i) {
            double cornerX = i % 2 == 0 ? box.field_1323 : box.field_1320;
            double cornerY = i / 2 % 2 == 0 ? box.field_1322 : box.field_1325;
            double cornerZ = i / 4 % 2 == 0 ? box.field_1321 : box.field_1324;
            Vector2f projected = ProjectionUtil.project(new class_243(cornerX, cornerY, cornerZ));
            minX = Math.min(minX, projected.x);
            minY = Math.min(minY, projected.y);
            maxX = Math.max(maxX, projected.x);
            maxY = Math.max(maxY, projected.y);
        }
        float scale = ((Float)this.module.scale.getValue()).floatValue();
        class_243 headPos = new class_243(xI, yI + (double)entity.method_17682(), zI);
        class_243 headProj = WorldRender.worldSpaceToScreenSpace(headPos);
        if (headProj.field_1350 < 0.0 || headProj.field_1350 > 1.0) {
            return;
        }
        float x = (float)headProj.field_1352;
        float y = (float)headProj.field_1351 - 15.0f * scale;
        boolean bl = inRegion = x > 0.0f && x < (float)mc.method_22683().method_4486() || y > 0.0f && y < (float)mc.method_22683().method_4502();
        if (inRegion) {
            this.renderName(entity, x, y, context);
            if (!(entity instanceof class_1657)) {
                return;
            }
            class_1657 player = (class_1657)entity;
            if (this.module.information.isEnabled("Items")) {
                this.nameTagsItems.renderItems(player, x, y, context);
            }
            if (this.module.information.isEnabled("Potions")) {
                this.nameTagsPotions.renderPotions(player, maxX + 2.0f * scale, minY, context);
            }
            if (this.module.options.isEnabled("Special items")) {
                this.nameTagsItems.renderSpecialItems(player, x, maxY - 2.0f * scale, context);
            }
        }
    }

    private void renderName(class_1297 entity, float x, float y, class_332 context) {
        Color bgColor;
        class_1657 p;
        class_1657 playerEntity;
        class_4587 matrixStack = context.method_51448();
        boolean isMcMode = this.module.textMode.is("\u041c\u0430\u0439\u043d\u043a\u0440\u0430\u0444\u0442");
        String rawNameStr = entity.method_5477().getString();
        String strippedNameStr = class_124.method_539((String)rawNameStr);
        if (strippedNameStr == null) {
            strippedNameStr = rawNameStr;
        }
        class_5250 nameForMc = class_2561.method_43470((String)rawNameStr);
        float scale = ((Float)this.module.scale.getValue()).floatValue();
        float size = 8.0f * scale;
        float gap = 2.0f * scale;
        class_2561 mcPrefix = null;
        String privilegeLabel = "";
        Color privilegeColor = Color.WHITE;
        if (entity instanceof class_1657) {
            class_1657 player = (class_1657)entity;
            if (isMcMode) {
                mcPrefix = player.method_5781() != null ? player.method_5781().method_1144() : null;
            } else {
                String privilegeKey = this.getPlayerPrivilege(player);
                privilegeLabel = PRIVILEGE_MAPPING.getOrDefault(privilegeKey, "");
                privilegeColor = PRIVILEGE_COLORS.getOrDefault(privilegeKey, Color.WHITE);
            }
        }
        float nameWidth = isMcMode ? (float)NameTagsRender.mc.field_1772.method_27525((class_5348)nameForMc) : Fonts.SF_MEDIUM.getWidth(strippedNameStr, size);
        float prefixWidth = 0.0f;
        float spaceWidth = 0.0f;
        boolean hasPrefix = false;
        if (isMcMode && mcPrefix != null) {
            prefixWidth = NameTagsRender.mc.field_1772.method_27525((class_5348)mcPrefix);
            spaceWidth = NameTagsRender.mc.field_1772.method_1727(" ");
            hasPrefix = prefixWidth > 0.5f;
        } else if (!isMcMode && !privilegeLabel.isEmpty()) {
            prefixWidth = Fonts.SF_MEDIUM.getWidth(privilegeLabel, size);
            spaceWidth = Fonts.SF_MEDIUM.getWidth(" ", size);
            hasPrefix = true;
        }
        Object hpStr = "";
        class_5250 hpTextMC = null;
        float hpWidth = 0.0f;
        class_1657 class_16572 = playerEntity = entity instanceof class_1657 ? (p = (class_1657)entity) : null;
        if (playerEntity != null && this.module.hpMode.is("\u0426\u0438\u0444\u0440\u044b")) {
            float hp = playerEntity.method_6032();
            String formattedHp = String.format("%.1f", Float.valueOf(hp)).replace(',', '.');
            if (isMcMode) {
                String colorCode = hp > 15.0f ? "\u00a7a" : (hp > 10.0f ? "\u00a7e" : (hp > 5.0f ? "\u00a76" : "\u00a7c"));
                hpTextMC = class_2561.method_43470((String)(" " + colorCode + formattedHp));
                hpWidth = NameTagsRender.mc.field_1772.method_27525((class_5348)hpTextMC);
            } else {
                hpStr = " " + formattedHp;
                hpWidth = Fonts.SF_MEDIUM.getWidth((String)hpStr, size);
            }
        }
        float textWidth = (hasPrefix ? prefixWidth + spaceWidth : 0.0f) + nameWidth + hpWidth;
        float totalWidth = textWidth + gap * 2.0f;
        float totalHeight = size + gap * 2.0f;
        x -= totalWidth / 2.0f;
        Color color = bgColor = !FriendManager.getInstance().contains(rawNameStr) ? (Color)this.module.color.getValue() : (Color)this.module.friendColor.getValue();
        if (this.module.rectMode.is("\u0411\u043b\u044e\u0440")) {
            RenderUtil.BLUR_RECT.draw(matrixStack, x, y, totalWidth, totalHeight, scale, bgColor, 1.0f - ((Float)this.module.glassy.getValue()).floatValue());
        } else {
            Render2DEngine.drawThemeGradient(matrixStack, x, y, totalWidth, totalHeight, 2.5f * scale);
        }
        if (playerEntity != null && this.module.hpMode.is("\u041f\u043e\u043b\u043e\u0441\u043a\u0430")) {
            float hpBarY = y + totalHeight + 1.5f * scale;
            float hpBarHeight = 2.5f * scale;
            Render2DEngine.drawHealthBar(matrixStack, x, hpBarY, totalWidth, hpBarHeight, 1.25f * scale, playerEntity.method_6032() / playerEntity.method_6063());
        }
        float textX = x + gap;
        float textY = y + gap;
        if (isMcMode) {
            if (hasPrefix) {
                context.method_51439(NameTagsRender.mc.field_1772, mcPrefix, (int)textX, (int)textY, -1, true);
                textX += prefixWidth + spaceWidth;
            }
            context.method_51439(NameTagsRender.mc.field_1772, (class_2561)nameForMc, (int)textX, (int)textY, ((Color)this.module.textColor.getValue()).getRGB(), true);
            if (hpTextMC != null) {
                context.method_51439(NameTagsRender.mc.field_1772, (class_2561)hpTextMC, (int)(textX + nameWidth), (int)textY, -1, true);
            }
        } else {
            if (hasPrefix) {
                String privilegeKey = this.getPlayerPrivilege((class_1657)entity);
                Color colorSecond = PRIVILEGE_COLORS_SECOND.getOrDefault(privilegeKey, privilegeColor);
                Fonts.SF_MEDIUM.drawGradientText(matrixStack, privilegeLabel, textX, textY, size, privilegeColor, colorSecond, 1.0f);
                textX += prefixWidth + spaceWidth;
            }
            Fonts.SF_MEDIUM.drawText(matrixStack, strippedNameStr, textX, textY, size, (Color)this.module.textColor.getValue());
            if (!((String)hpStr).isEmpty()) {
                float pct = Math.max(0.0f, Math.min(1.0f, playerEntity.method_6032() / playerEntity.method_6063()));
                Color hpC = new Color((int)(255.0f * (1.0f - pct)), (int)(255.0f * pct), 0);
                Fonts.SF_MEDIUM.drawText(matrixStack, (String)hpStr, textX + nameWidth, textY, size, hpC);
            }
        }
    }

    private String getPlayerPrivilege(class_1657 player) {
        String privilege;
        class_2561 displayName;
        HashSet<String> validPrivileges = new HashSet<String>(PRIVILEGE_MAPPING.keySet());
        class_640 entry = NameTagsRender.mc.field_1724.field_3944.method_2871(player.method_5667());
        if (entry != null && (displayName = entry.method_2971()) != null && (privilege = this.parsePrivilegeFromText(displayName, validPrivileges)) != null) {
            return privilege;
        }
        String privilege2 = this.parsePrivilegeFromTeam(player, validPrivileges);
        return privilege2 != null ? privilege2 : "default";
    }

    private String parsePrivilegeFromText(class_2561 text, Set<String> validPrivileges) {
        String privilege;
        String fontId;
        if (text == null) {
            return null;
        }
        class_2583 style = text.method_10866();
        if (style != null && style.method_27708() != null && (fontId = style.method_27708().toString()).contains("custom:groups/") && validPrivileges.contains(privilege = fontId.substring(fontId.lastIndexOf("/") + 1).toLowerCase())) {
            return privilege;
        }
        String textString = text.getString();
        if (textString != null && !textString.isEmpty()) {
            for (String privilege2 : validPrivileges) {
                if (!textString.toLowerCase().contains(privilege2)) continue;
                return privilege2;
            }
        }
        for (class_2561 sibling : text.method_10855()) {
            String result = this.parsePrivilegeFromText(sibling, validPrivileges);
            if (result == null) continue;
            return result;
        }
        return null;
    }

    private String parsePrivilegeFromTeam(class_1657 player, Set<String> validPrivileges) {
        class_2561 prefix;
        class_268 team = player.method_5781();
        if (team != null && (prefix = team.method_1144()) != null) {
            String prefixString = prefix.getString();
            for (String privilege : validPrivileges) {
                if (!prefixString.toLowerCase().contains(privilege)) continue;
                return privilege;
            }
            String privilegeFromPrefix = this.parsePrivilegeFromText(prefix, validPrivileges);
            if (privilegeFromPrefix != null) {
                return privilegeFromPrefix;
            }
        }
        return null;
    }

    private void render3DBox(class_1297 entity, double x, double y, double z) {
        class_238 box = entity.method_5829().method_989(x - entity.method_23317(), y - entity.method_23318(), z - entity.method_23321());
        Color themeColor = UIColors.primary();
        float alpha = ((Float)this.module.boxAlpha.getValue()).floatValue();
        Color fillColor = new Color(themeColor.getRed(), themeColor.getGreen(), themeColor.getBlue(), (int)(alpha * 255.0f));
        Color outlineColor = new Color(themeColor.getRed(), themeColor.getGreen(), themeColor.getBlue(), 255);
        float x1 = (float)box.field_1323;
        float y1 = (float)box.field_1322;
        float z1 = (float)box.field_1321;
        float x2 = (float)box.field_1320;
        float y2 = (float)box.field_1325;
        float z2 = (float)box.field_1324;
        RenderUtil.BOX.drawBox(x1, y1, z1, x2, y2, z2, 1.5f, fillColor, BoxRender.Render.FILL, 0.0f);
        RenderUtil.BOX.drawBox(x1, y1, z1, x2, y2, z2, 1.5f, outlineColor, BoxRender.Render.OUTLINE, 0.0f);
    }

    static {
        PRIVILEGE_MAPPING.put("hydra", "\u0413\u0438\u0434\u0440\u0430");
        PRIVILEGE_MAPPING.put("cerberus", "\u0426\u0435\u0440\u0431\u0435\u0440");
        PRIVILEGE_MAPPING.put("triton", "\u0422\u0440\u0438\u0442\u043e\u043d");
        PRIVILEGE_MAPPING.put("phoenix", "\u0424\u0435\u043d\u0438\u043a\u0441");
        PRIVILEGE_MAPPING.put("pandar", "\u041f\u0430\u043d\u0434\u0430\u0440");
        PRIVILEGE_MAPPING.put("heat", "\u0416\u0430\u0440\u0430");
        PRIVILEGE_MAPPING.put("cold", "\u0425\u043e\u043b\u043e\u0434");
        PRIVILEGE_MAPPING.put("kronos", "\u041a\u0440\u043e\u043d\u043e\u0441");
        PRIVILEGE_MAPPING.put("summer", "\u041b\u0435\u0442\u043e");
        PRIVILEGE_MAPPING.put("winter", "\u0417\u0438\u043c\u0430");
        PRIVILEGE_MAPPING.put("phobos", "\u0424\u043e\u0431\u043e\u0441");
        PRIVILEGE_MAPPING.put("ares", "\u0410\u0440\u0435\u0441");
        PRIVILEGE_MAPPING.put("aristocrat", "\u0410\u0440\u0438\u0441\u0442\u043e\u043a\u0440\u0430\u0442");
        PRIVILEGE_MAPPING.put("youtuber", "\u042e\u0442\u0443\u0431\u0435\u0440");
        PRIVILEGE_MAPPING.put("helper", "\u0425\u0435\u043b\u043f\u0435\u0440");
        PRIVILEGE_MAPPING.put("shelper", "\u0421\u0442. \u0425\u0435\u043b\u043f\u0435\u0440");
        PRIVILEGE_MAPPING.put("moder", "\u041c\u043e\u0434\u0435\u0440");
        PRIVILEGE_MAPPING.put("smoder", "\u0421\u0442. \u041c\u043e\u0434\u0435\u0440");
        PRIVILEGE_MAPPING.put("admin", "\u0410\u0434\u043c\u0438\u043d");
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
        PRIVILEGE_COLORS.put("shelper", new Color(0xFFFF00));
        PRIVILEGE_COLORS.put("moder", new Color(0x800080));
        PRIVILEGE_COLORS.put("smoder", new Color(255));
        PRIVILEGE_COLORS.put("admin", new Color(0xFF0000));
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
        PRIVILEGE_COLORS_SECOND.put("helper", new Color(0x800080));
        PRIVILEGE_COLORS_SECOND.put("shelper", new Color(16737095));
        PRIVILEGE_COLORS_SECOND.put("moder", new Color(255));
        PRIVILEGE_COLORS_SECOND.put("smoder", new Color(0xFF0000));
        PRIVILEGE_COLORS_SECOND.put("admin", new Color(255));
    }
}

