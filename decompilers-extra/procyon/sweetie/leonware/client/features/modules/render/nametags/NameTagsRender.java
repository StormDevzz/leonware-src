// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.render.nametags;

import java.util.HashMap;
import sweetie.leonware.api.utils.render.display.BoxRender;
import sweetie.leonware.api.utils.color.UIColors;
import net.minecraft.class_268;
import net.minecraft.class_2583;
import net.minecraft.class_640;
import java.util.Set;
import java.util.Collection;
import java.util.HashSet;
import net.minecraft.class_4587;
import sweetie.leonware.api.utils.render.Render2DEngine;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.api.system.configs.FriendManager;
import sweetie.leonware.api.utils.render.fonts.Fonts;
import net.minecraft.class_5348;
import net.minecraft.class_2561;
import net.minecraft.class_124;
import org.joml.Vector2f;
import net.minecraft.class_238;
import sweetie.leonware.api.utils.render.display.WorldRender;
import sweetie.leonware.api.utils.math.ProjectionUtil;
import net.minecraft.class_243;
import net.minecraft.class_3532;
import net.minecraft.class_332;
import java.util.Iterator;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_1297;
import sweetie.leonware.api.event.events.render.Render2DEvent;
import java.awt.Color;
import java.util.Map;
import sweetie.leonware.api.system.interfaces.QuickImports;

public class NameTagsRender implements QuickImports
{
    private final NameTagsModule module;
    private final NameTagsItems nameTagsItems;
    private final NameTagsPotions nameTagsPotions;
    private static final Map<String, String> PRIVILEGE_MAPPING;
    private static final Map<String, Color> PRIVILEGE_COLORS;
    private static final Map<String, Color> PRIVILEGE_COLORS_SECOND;
    
    public NameTagsRender(final NameTagsModule module) {
        this.module = module;
        this.nameTagsItems = new NameTagsItems(module);
        this.nameTagsPotions = new NameTagsPotions(module);
    }
    
    public void onRender(final Render2DEvent.Render2DEventData event) {
        for (final class_1297 entity1 : NameTagsRender.mc.field_1687.method_18112()) {
            if (entity1 instanceof final class_1657 player) {
                if (!this.module.entityFilter.isValid((class_1309)player) && (player != NameTagsRender.mc.field_1724 || !this.module.targets.isEnabled("Self") || NameTagsRender.mc.field_1690.method_31044().method_31034())) {
                    continue;
                }
                this.renderTag((class_1297)player, event.context(), event.partialTicks());
            }
        }
    }
    
    private void renderTag(final class_1297 entity, final class_332 context, final float partialTicks) {
        final double xI = class_3532.method_16436((double)partialTicks, entity.field_6014, entity.method_23317());
        final double yI = class_3532.method_16436((double)partialTicks, entity.field_6036, entity.method_23318());
        final double zI = class_3532.method_16436((double)partialTicks, entity.field_5969, entity.method_23321());
        if (this.module.box3d.getValue()) {
            this.render3DBox(entity, xI, yI, zI);
        }
        final class_238 box = entity.method_5829().method_989(xI - entity.method_23317(), yI - entity.method_23318(), zI - entity.method_23321());
        float minX = Float.MAX_VALUE;
        float minY = Float.MAX_VALUE;
        float maxX = Float.MIN_VALUE;
        float maxY = Float.MIN_VALUE;
        for (int i = 0; i < 8; ++i) {
            final double cornerX = (i % 2 == 0) ? box.field_1323 : box.field_1320;
            final double cornerY = (i / 2 % 2 == 0) ? box.field_1322 : box.field_1325;
            final double cornerZ = (i / 4 % 2 == 0) ? box.field_1321 : box.field_1324;
            final Vector2f projected = ProjectionUtil.project(new class_243(cornerX, cornerY, cornerZ));
            minX = Math.min(minX, projected.x);
            minY = Math.min(minY, projected.y);
            maxX = Math.max(maxX, projected.x);
            maxY = Math.max(maxY, projected.y);
        }
        final float scale = this.module.scale.getValue();
        final class_243 headPos = new class_243(xI, yI + entity.method_17682(), zI);
        final class_243 headProj = WorldRender.worldSpaceToScreenSpace(headPos);
        if (headProj.field_1350 < 0.0 || headProj.field_1350 > 1.0) {
            return;
        }
        final float x = (float)headProj.field_1352;
        final float y = (float)headProj.field_1351 - 15.0f * scale;
        final boolean inRegion = (x > 0.0f && x < NameTagsRender.mc.method_22683().method_4486()) || (y > 0.0f && y < NameTagsRender.mc.method_22683().method_4502());
        if (inRegion) {
            this.renderName(entity, x, y, context);
            if (!(entity instanceof class_1657)) {
                return;
            }
            final class_1657 player = (class_1657)entity;
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
    
    private void renderName(final class_1297 entity, float x, final float y, final class_332 context) {
        final class_4587 matrixStack = context.method_51448();
        final boolean isMcMode = this.module.textMode.is("\u041c\u0430\u0439\u043d\u043a\u0440\u0430\u0444\u0442");
        final String rawNameStr = entity.method_5477().getString();
        String strippedNameStr = class_124.method_539(rawNameStr);
        if (strippedNameStr == null) {
            strippedNameStr = rawNameStr;
        }
        final class_2561 nameForMc = (class_2561)class_2561.method_43470(rawNameStr);
        final float scale = this.module.scale.getValue();
        final float size = 8.0f * scale;
        final float gap = 2.0f * scale;
        class_2561 mcPrefix = null;
        String privilegeLabel = "";
        Color privilegeColor = Color.WHITE;
        if (entity instanceof final class_1657 player) {
            if (isMcMode) {
                mcPrefix = ((player.method_5781() != null) ? player.method_5781().method_1144() : null);
            }
            else {
                final String privilegeKey = this.getPlayerPrivilege(player);
                privilegeLabel = NameTagsRender.PRIVILEGE_MAPPING.getOrDefault(privilegeKey, "");
                privilegeColor = NameTagsRender.PRIVILEGE_COLORS.getOrDefault(privilegeKey, Color.WHITE);
            }
        }
        final float nameWidth = isMcMode ? ((float)NameTagsRender.mc.field_1772.method_27525((class_5348)nameForMc)) : Fonts.SF_MEDIUM.getWidth(strippedNameStr, size);
        float prefixWidth = 0.0f;
        float spaceWidth = 0.0f;
        boolean hasPrefix = false;
        if (isMcMode && mcPrefix != null) {
            prefixWidth = (float)NameTagsRender.mc.field_1772.method_27525((class_5348)mcPrefix);
            spaceWidth = (float)NameTagsRender.mc.field_1772.method_1727(" ");
            hasPrefix = (prefixWidth > 0.5f);
        }
        else if (!isMcMode && !privilegeLabel.isEmpty()) {
            prefixWidth = Fonts.SF_MEDIUM.getWidth(privilegeLabel, size);
            spaceWidth = Fonts.SF_MEDIUM.getWidth(" ", size);
            hasPrefix = true;
        }
        String hpStr = "";
        class_2561 hpTextMC = null;
        float hpWidth = 0.0f;
        class_1657 class_1657;
        if (entity instanceof final class_1657 class_1658) {
            final class_1657 p = class_1657 = class_1658;
        }
        else {
            class_1657 = null;
        }
        final class_1657 playerEntity = class_1657;
        if (playerEntity != null && this.module.hpMode.is("\u0426\u0438\u0444\u0440\u044b")) {
            final float hp = playerEntity.method_6032();
            final String formattedHp = String.format("%.1f", hp).replace(',', '.');
            if (isMcMode) {
                final String colorCode = (hp > 15.0f) ? "§a" : ((hp > 10.0f) ? "§e" : ((hp > 5.0f) ? "§6" : "§c"));
                hpTextMC = (class_2561)class_2561.method_43470(" " + colorCode + formattedHp);
                hpWidth = (float)NameTagsRender.mc.field_1772.method_27525((class_5348)hpTextMC);
            }
            else {
                hpStr = " " + formattedHp;
                hpWidth = Fonts.SF_MEDIUM.getWidth(hpStr, size);
            }
        }
        final float textWidth = (hasPrefix ? (prefixWidth + spaceWidth) : 0.0f) + nameWidth + hpWidth;
        final float totalWidth = textWidth + gap * 2.0f;
        final float totalHeight = size + gap * 2.0f;
        x -= totalWidth / 2.0f;
        final Color bgColor = FriendManager.getInstance().contains(rawNameStr) ? this.module.friendColor.getValue() : this.module.color.getValue();
        if (this.module.rectMode.is("\u0411\u043b\u044e\u0440")) {
            RenderUtil.BLUR_RECT.draw(matrixStack, x, y, totalWidth, totalHeight, scale, bgColor, 1.0f - this.module.glassy.getValue());
        }
        else {
            Render2DEngine.drawThemeGradient(matrixStack, x, y, totalWidth, totalHeight, 2.5f * scale);
        }
        if (playerEntity != null && this.module.hpMode.is("\u041f\u043e\u043b\u043e\u0441\u043a\u0430")) {
            final float hpBarY = y + totalHeight + 1.5f * scale;
            final float hpBarHeight = 2.5f * scale;
            Render2DEngine.drawHealthBar(matrixStack, x, hpBarY, totalWidth, hpBarHeight, 1.25f * scale, playerEntity.method_6032() / playerEntity.method_6063());
        }
        float textX = x + gap;
        final float textY = y + gap;
        if (isMcMode) {
            if (hasPrefix) {
                context.method_51439(NameTagsRender.mc.field_1772, mcPrefix, (int)textX, (int)textY, -1, true);
                textX += prefixWidth + spaceWidth;
            }
            context.method_51439(NameTagsRender.mc.field_1772, nameForMc, (int)textX, (int)textY, this.module.textColor.getValue().getRGB(), true);
            if (hpTextMC != null) {
                context.method_51439(NameTagsRender.mc.field_1772, hpTextMC, (int)(textX + nameWidth), (int)textY, -1, true);
            }
        }
        else {
            if (hasPrefix) {
                final String privilegeKey2 = this.getPlayerPrivilege((class_1657)entity);
                final Color colorSecond = NameTagsRender.PRIVILEGE_COLORS_SECOND.getOrDefault(privilegeKey2, privilegeColor);
                Fonts.SF_MEDIUM.drawGradientText(matrixStack, privilegeLabel, textX, textY, size, privilegeColor, colorSecond, 1.0f);
                textX += prefixWidth + spaceWidth;
            }
            Fonts.SF_MEDIUM.drawText(matrixStack, strippedNameStr, textX, textY, size, this.module.textColor.getValue());
            if (!hpStr.isEmpty()) {
                final float pct = Math.max(0.0f, Math.min(1.0f, playerEntity.method_6032() / playerEntity.method_6063()));
                final Color hpC = new Color((int)(255.0f * (1.0f - pct)), (int)(255.0f * pct), 0);
                Fonts.SF_MEDIUM.drawText(matrixStack, hpStr, textX + nameWidth, textY, size, hpC);
            }
        }
    }
    
    private String getPlayerPrivilege(final class_1657 player) {
        final Set<String> validPrivileges = new HashSet<String>(NameTagsRender.PRIVILEGE_MAPPING.keySet());
        final class_640 entry = NameTagsRender.mc.field_1724.field_3944.method_2871(player.method_5667());
        if (entry != null) {
            final class_2561 displayName = entry.method_2971();
            if (displayName != null) {
                final String privilege = this.parsePrivilegeFromText(displayName, validPrivileges);
                if (privilege != null) {
                    return privilege;
                }
            }
        }
        final String privilege2 = this.parsePrivilegeFromTeam(player, validPrivileges);
        return (privilege2 != null) ? privilege2 : "default";
    }
    
    private String parsePrivilegeFromText(final class_2561 text, final Set<String> validPrivileges) {
        if (text == null) {
            return null;
        }
        final class_2583 style = text.method_10866();
        if (style != null && style.method_27708() != null) {
            final String fontId = style.method_27708().toString();
            if (fontId.contains("custom:groups/")) {
                final String privilege = fontId.substring(fontId.lastIndexOf("/") + 1).toLowerCase();
                if (validPrivileges.contains(privilege)) {
                    return privilege;
                }
            }
        }
        final String textString = text.getString();
        if (textString != null && !textString.isEmpty()) {
            for (final String privilege2 : validPrivileges) {
                if (textString.toLowerCase().contains(privilege2)) {
                    return privilege2;
                }
            }
        }
        for (final class_2561 sibling : text.method_10855()) {
            final String result = this.parsePrivilegeFromText(sibling, validPrivileges);
            if (result != null) {
                return result;
            }
        }
        return null;
    }
    
    private String parsePrivilegeFromTeam(final class_1657 player, final Set<String> validPrivileges) {
        final class_268 team = player.method_5781();
        if (team != null) {
            final class_2561 prefix = team.method_1144();
            if (prefix != null) {
                final String prefixString = prefix.getString();
                for (final String privilege : validPrivileges) {
                    if (prefixString.toLowerCase().contains(privilege)) {
                        return privilege;
                    }
                }
                final String privilegeFromPrefix = this.parsePrivilegeFromText(prefix, validPrivileges);
                if (privilegeFromPrefix != null) {
                    return privilegeFromPrefix;
                }
            }
        }
        return null;
    }
    
    private void render3DBox(final class_1297 entity, final double x, final double y, final double z) {
        final class_238 box = entity.method_5829().method_989(x - entity.method_23317(), y - entity.method_23318(), z - entity.method_23321());
        final Color themeColor = UIColors.primary();
        final float alpha = this.module.boxAlpha.getValue();
        final Color fillColor = new Color(themeColor.getRed(), themeColor.getGreen(), themeColor.getBlue(), (int)(alpha * 255.0f));
        final Color outlineColor = new Color(themeColor.getRed(), themeColor.getGreen(), themeColor.getBlue(), 255);
        final float x2 = (float)box.field_1323;
        final float y2 = (float)box.field_1322;
        final float z2 = (float)box.field_1321;
        final float x3 = (float)box.field_1320;
        final float y3 = (float)box.field_1325;
        final float z3 = (float)box.field_1324;
        RenderUtil.BOX.drawBox(x2, y2, z2, x3, y3, z3, 1.5f, fillColor, BoxRender.Render.FILL, 0.0f);
        RenderUtil.BOX.drawBox(x2, y2, z2, x3, y3, z3, 1.5f, outlineColor, BoxRender.Render.OUTLINE, 0.0f);
    }
    
    static {
        PRIVILEGE_MAPPING = new HashMap<String, String>();
        PRIVILEGE_COLORS = new HashMap<String, Color>();
        PRIVILEGE_COLORS_SECOND = new HashMap<String, Color>();
        NameTagsRender.PRIVILEGE_MAPPING.put("hydra", "\u0413\u0438\u0434\u0440\u0430");
        NameTagsRender.PRIVILEGE_MAPPING.put("cerberus", "\u0426\u0435\u0440\u0431\u0435\u0440");
        NameTagsRender.PRIVILEGE_MAPPING.put("triton", "\u0422\u0440\u0438\u0442\u043e\u043d");
        NameTagsRender.PRIVILEGE_MAPPING.put("phoenix", "\u0424\u0435\u043d\u0438\u043a\u0441");
        NameTagsRender.PRIVILEGE_MAPPING.put("pandar", "\u041f\u0430\u043d\u0434\u0430\u0440");
        NameTagsRender.PRIVILEGE_MAPPING.put("heat", "\u0416\u0430\u0440\u0430");
        NameTagsRender.PRIVILEGE_MAPPING.put("cold", "\u0425\u043e\u043b\u043e\u0434");
        NameTagsRender.PRIVILEGE_MAPPING.put("kronos", "\u041a\u0440\u043e\u043d\u043e\u0441");
        NameTagsRender.PRIVILEGE_MAPPING.put("summer", "\u041b\u0435\u0442\u043e");
        NameTagsRender.PRIVILEGE_MAPPING.put("winter", "\u0417\u0438\u043c\u0430");
        NameTagsRender.PRIVILEGE_MAPPING.put("phobos", "\u0424\u043e\u0431\u043e\u0441");
        NameTagsRender.PRIVILEGE_MAPPING.put("ares", "\u0410\u0440\u0435\u0441");
        NameTagsRender.PRIVILEGE_MAPPING.put("aristocrat", "\u0410\u0440\u0438\u0441\u0442\u043e\u043a\u0440\u0430\u0442");
        NameTagsRender.PRIVILEGE_MAPPING.put("youtuber", "\u042e\u0442\u0443\u0431\u0435\u0440");
        NameTagsRender.PRIVILEGE_MAPPING.put("helper", "\u0425\u0435\u043b\u043f\u0435\u0440");
        NameTagsRender.PRIVILEGE_MAPPING.put("shelper", "\u0421\u0442. \u0425\u0435\u043b\u043f\u0435\u0440");
        NameTagsRender.PRIVILEGE_MAPPING.put("moder", "\u041c\u043e\u0434\u0435\u0440");
        NameTagsRender.PRIVILEGE_MAPPING.put("smoder", "\u0421\u0442. \u041c\u043e\u0434\u0435\u0440");
        NameTagsRender.PRIVILEGE_MAPPING.put("admin", "\u0410\u0434\u043c\u0438\u043d");
        NameTagsRender.PRIVILEGE_COLORS.put("hydra", new Color(6225797));
        NameTagsRender.PRIVILEGE_COLORS.put("cerberus", new Color(4360086));
        NameTagsRender.PRIVILEGE_COLORS.put("triton", new Color(4286945));
        NameTagsRender.PRIVILEGE_COLORS.put("phoenix", new Color(16729344));
        NameTagsRender.PRIVILEGE_COLORS.put("pandar", new Color(14423100));
        NameTagsRender.PRIVILEGE_COLORS.put("heat", new Color(16737095));
        NameTagsRender.PRIVILEGE_COLORS.put("cold", new Color(8900331));
        NameTagsRender.PRIVILEGE_COLORS.put("kronos", new Color(6578687));
        NameTagsRender.PRIVILEGE_COLORS.put("summer", new Color(16753920));
        NameTagsRender.PRIVILEGE_COLORS.put("winter", new Color(11393254));
        NameTagsRender.PRIVILEGE_COLORS.put("phobos", new Color(16084007));
        NameTagsRender.PRIVILEGE_COLORS.put("ares", new Color(8910119));
        NameTagsRender.PRIVILEGE_COLORS.put("aristocrat", new Color(10289407));
        NameTagsRender.PRIVILEGE_COLORS.put("youtuber", new Color(16738740));
        NameTagsRender.PRIVILEGE_COLORS.put("helper", new Color(65280));
        NameTagsRender.PRIVILEGE_COLORS.put("shelper", new Color(16776960));
        NameTagsRender.PRIVILEGE_COLORS.put("moder", new Color(8388736));
        NameTagsRender.PRIVILEGE_COLORS.put("smoder", new Color(255));
        NameTagsRender.PRIVILEGE_COLORS.put("admin", new Color(16711680));
        NameTagsRender.PRIVILEGE_COLORS_SECOND.put("hydra", new Color(8900331));
        NameTagsRender.PRIVILEGE_COLORS_SECOND.put("cerberus", new Color(6225797));
        NameTagsRender.PRIVILEGE_COLORS_SECOND.put("triton", new Color(6578687));
        NameTagsRender.PRIVILEGE_COLORS_SECOND.put("phoenix", new Color(16753920));
        NameTagsRender.PRIVILEGE_COLORS_SECOND.put("pandar", new Color(16737095));
        NameTagsRender.PRIVILEGE_COLORS_SECOND.put("heat", new Color(16753920));
        NameTagsRender.PRIVILEGE_COLORS_SECOND.put("cold", new Color(4360086));
        NameTagsRender.PRIVILEGE_COLORS_SECOND.put("kronos", new Color(11393254));
        NameTagsRender.PRIVILEGE_COLORS_SECOND.put("summer", new Color(16729344));
        NameTagsRender.PRIVILEGE_COLORS_SECOND.put("winter", new Color(8900331));
        NameTagsRender.PRIVILEGE_COLORS_SECOND.put("phobos", new Color(16729344));
        NameTagsRender.PRIVILEGE_COLORS_SECOND.put("ares", new Color(4286945));
        NameTagsRender.PRIVILEGE_COLORS_SECOND.put("aristocrat", new Color(16084007));
        NameTagsRender.PRIVILEGE_COLORS_SECOND.put("youtuber", new Color(16753920));
        NameTagsRender.PRIVILEGE_COLORS_SECOND.put("helper", new Color(8388736));
        NameTagsRender.PRIVILEGE_COLORS_SECOND.put("shelper", new Color(16737095));
        NameTagsRender.PRIVILEGE_COLORS_SECOND.put("moder", new Color(255));
        NameTagsRender.PRIVILEGE_COLORS_SECOND.put("smoder", new Color(16711680));
        NameTagsRender.PRIVILEGE_COLORS_SECOND.put("admin", new Color(255));
    }
}
