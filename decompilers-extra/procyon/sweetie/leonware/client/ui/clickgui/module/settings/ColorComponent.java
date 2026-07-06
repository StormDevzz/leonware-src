// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.ui.clickgui.module.settings;

import sweetie.leonware.api.event.Listener;
import java.util.Map;
import java.util.Collections;
import java.util.WeakHashMap;
import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import sweetie.leonware.api.event.events.client.GameLoopEvent;
import sweetie.leonware.api.system.files.FileUtil;
import sweetie.leonware.api.system.interfaces.QuickImports;
import sweetie.leonware.api.utils.math.MouseUtil;
import net.minecraft.class_4587;
import sweetie.leonware.api.utils.color.ColorUtil;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.render.fonts.Fonts;
import net.minecraft.class_332;
import java.awt.Color;
import sweetie.leonware.api.module.setting.Setting;
import java.util.Set;
import sweetie.leonware.api.module.setting.ColorSetting;
import sweetie.leonware.client.ui.theme.Theme;
import sweetie.leonware.client.ui.clickgui.module.ExpandableComponent;

public class ColorComponent extends ExpandableComponent.ExpandableSettingComponent
{
    private final Theme.ElementColor elementColor;
    private final ColorSetting setting;
    private boolean draggingHue;
    private boolean draggingSatBright;
    private boolean draggingAlpha;
    private float hueCache;
    private boolean inited;
    private boolean rgbMode;
    private boolean uiMode;
    private long rgbStartTime;
    private static final Set<ColorComponent> rgbActive;
    
    public ColorComponent(final ColorSetting setting) {
        super(setting);
        this.draggingHue = false;
        this.draggingSatBright = false;
        this.draggingAlpha = false;
        this.hueCache = 0.0f;
        this.rgbMode = false;
        this.uiMode = false;
        this.rgbStartTime = 0L;
        this.setting = setting;
        this.elementColor = null;
        if (setting != null) {
            this.rgbMode = setting.isRgbMode();
            this.uiMode = setting.isUiMode();
            if (this.rgbMode) {
                this.rgbStartTime = System.currentTimeMillis();
                ColorComponent.rgbActive.add(this);
            }
        }
        this.updateHeight(this.getDefaultHeight());
        this.initHueCache();
    }
    
    public ColorComponent(final Theme.ElementColor elementColor) {
        super(null);
        this.draggingHue = false;
        this.draggingSatBright = false;
        this.draggingAlpha = false;
        this.hueCache = 0.0f;
        this.rgbMode = false;
        this.uiMode = false;
        this.rgbStartTime = 0L;
        this.elementColor = elementColor;
        this.setting = null;
        this.updateHeight(this.getDefaultHeight());
    }
    
    private void initHueCache() {
        if (this.inited) {
            return;
        }
        final Color color = this.getCurrentColor();
        final float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        this.hueCache = hsb[0];
        this.inited = true;
    }
    
    private Color getCurrentColor() {
        return (this.setting != null) ? this.setting.getValue() : this.elementColor.getColor();
    }
    
    private void setCurrentColor(final Color color) {
        if (this.setting != null) {
            this.setting.setValue(color);
        }
        else {
            this.elementColor.setColor(color);
        }
    }
    
    @Override
    public void render(final class_332 context, final int mouseX, final int mouseY, final float delta) {
        final class_4587 ms = context.method_51448();
        this.updateOpen();
        this.initHueCache();
        if (this.draggingHue) {
            this.updateHue(mouseX);
        }
        if (this.draggingSatBright) {
            this.updateSatBright(mouseX, mouseY);
        }
        if (this.draggingAlpha) {
            this.updateAlpha(mouseX);
        }
        final float baseHeight = this.scaled(this.getDefaultHeight());
        final float fontSize = baseHeight * 0.45f;
        final int fullAlpha = (int)(this.getAlpha() * 255.0f);
        if (this.setting != null) {
            Fonts.PS_MEDIUM.drawText(ms, this.setting.getName(), this.getX(), this.getY() + baseHeight / 2.0f - fontSize / 2.0f, fontSize, UIColors.textColor(fullAlpha));
            final float previewSize = baseHeight * 0.7f;
            final float previewX = this.getX() + this.getWidth() - previewSize;
            final float previewY = this.getY() + baseHeight / 2.0f - previewSize / 2.0f;
            final float previewRound = previewSize * 0.2f;
            RenderUtil.RECT.draw(ms, previewX, previewY, previewSize, previewSize, previewRound, ColorUtil.setAlpha(this.getCurrentColor(), (int)(this.getCurrentColor().getAlpha() / 255.0f * fullAlpha)));
            this.updateHeight(this.getDefaultHeight());
        }
        final float animValue = this.getAnimValue();
        if (animValue > 0.0) {
            final Color[] colors = this.getGradientColors(animValue);
            final float colorPickerRound = this.getWidth() * 0.02f;
            RenderUtil.GRADIENT_RECT.draw(ms, this.getPickerX(), this.getColorPickerY() + this.getAnimY(), this.getPickerWidth(), this.getColorPickerHeight(), colorPickerRound, colors[0], colors[1], colors[2], colors[3]);
            this.drawHueBar(ms, animValue);
            this.drawAlphaBar(ms, animValue);
            this.drawSelectors(ms);
            this.drawModeButtons(ms, animValue);
            final float alphaHeight = this.getAlphaHeight() + this.gap();
            final float extraHeight = (this.getHueHeight() + this.getColorPickerHeight() + alphaHeight + this.gap() + this.getModeButtonHeight() + this.gap()) * animValue;
            final float baseHeightFinal = (this.setting != null) ? baseHeight : 0.0f;
            this.setHeight(baseHeightFinal + extraHeight);
        }
    }
    
    @Override
    public void mouseClicked(final double mouseX, final double mouseY, final int button) {
        if (this.setting != null && MouseUtil.isHovered(mouseX, mouseY, this.getX(), this.getY(), this.getWidth(), this.scaled(this.getDefaultHeight()))) {
            this.toggleOpen();
            return;
        }
        if (this.isNotOver()) {
            return;
        }
        final float bY = this.getModeButtonY() + this.getAnimY();
        final float bH = this.getModeButtonHeight();
        final float halfW = (this.getPickerWidth() - this.gap()) / 2.0f;
        if (MouseUtil.isHovered(mouseX, mouseY, this.getPickerX(), bY, halfW, bH)) {
            this.rgbMode = !this.rgbMode;
            this.uiMode = false;
            if (this.setting != null) {
                this.setting.setRgbMode(this.rgbMode);
                this.setting.setUiMode(false);
            }
            if (this.rgbMode) {
                this.rgbStartTime = System.currentTimeMillis();
                ColorComponent.rgbActive.add(this);
            }
            else {
                ColorComponent.rgbActive.remove(this);
            }
            return;
        }
        final float uiX = this.getPickerX() + halfW + this.gap();
        if (MouseUtil.isHovered(mouseX, mouseY, uiX, bY, halfW, bH)) {
            this.uiMode = !this.uiMode;
            this.rgbMode = false;
            if (this.setting != null) {
                this.setting.setUiMode(this.uiMode);
                this.setting.setRgbMode(false);
            }
            ColorComponent.rgbActive.remove(this);
            if (this.uiMode) {
                final Color uiColor = UIColors.primary();
                final Color current = this.getCurrentColor();
                this.setCurrentColor(new Color(uiColor.getRed(), uiColor.getGreen(), uiColor.getBlue(), current.getAlpha()));
                this.hueCache = Color.RGBtoHSB(uiColor.getRed(), uiColor.getGreen(), uiColor.getBlue(), null)[0];
            }
            return;
        }
        if (MouseUtil.isHovered(mouseX, mouseY, this.getPickerX(), this.getColorPickerY(), this.getPickerWidth(), this.getColorPickerHeight())) {
            this.draggingSatBright = true;
            this.updateSatBright(mouseX, mouseY);
        }
        else if (MouseUtil.isHovered(mouseX, mouseY, this.getPickerX(), this.getHueY(), this.getPickerWidth(), this.getHueHeight())) {
            this.draggingHue = true;
            this.updateHue(mouseX);
        }
        else if (MouseUtil.isHovered(mouseX, mouseY, this.getPickerX(), this.getAlphaY(), this.getPickerWidth(), this.getAlphaHeight())) {
            this.draggingAlpha = true;
            this.updateAlpha(mouseX);
        }
    }
    
    @Override
    public void mouseReleased(final double mouseX, final double mouseY, final int button) {
        this.draggingHue = false;
        this.draggingSatBright = false;
        this.draggingAlpha = false;
    }
    
    private void updateHue(final double mouseX) {
        this.rgbMode = false;
        this.uiMode = false;
        float rel = (float)((mouseX - this.getPickerX()) / this.getPickerWidth());
        rel = Math.max(0.0f, Math.min(1.0f, rel));
        this.hueCache = rel;
        final Color color = this.getCurrentColor();
        final float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        this.setCurrentColor(new Color(Color.HSBtoRGB(rel, hsb[1], hsb[2]), true));
    }
    
    private void updateSatBright(final double mouseX, final double mouseY) {
        this.rgbMode = false;
        this.uiMode = false;
        float sat = (float)((mouseX - this.getPickerX()) / this.getPickerWidth());
        float bri = 1.0f - (float)((mouseY - this.getColorPickerY()) / this.getColorPickerHeight());
        sat = Math.max(0.0f, Math.min(1.0f, sat));
        bri = Math.max(0.0f, Math.min(1.0f, bri));
        final Color color = this.getCurrentColor();
        final float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        final float hue = (hsb[1] == 0.0f || hsb[2] == 0.0f) ? this.hueCache : hsb[0];
        final Color newColor = new Color(Color.HSBtoRGB(hue, sat, bri));
        this.setCurrentColor(new Color(newColor.getRed(), newColor.getGreen(), newColor.getBlue(), color.getAlpha()));
    }
    
    private void updateAlpha(final double mouseX) {
        float rel = (float)((mouseX - this.getPickerX()) / this.getPickerWidth());
        rel = Math.max(0.0f, Math.min(1.0f, rel));
        final int alpha = (int)(rel * 255.0f);
        final Color c = this.getCurrentColor();
        this.setCurrentColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha));
    }
    
    private void drawAlphaBar(final class_4587 ms, final float animValue) {
        final float y = this.getAlphaY() + this.getAnimY();
        final float h = this.getAlphaHeight();
        final Color c = this.getCurrentColor();
        final Color left = new Color(c.getRed(), c.getGreen(), c.getBlue(), 0);
        final Color right = new Color(c.getRed(), c.getGreen(), c.getBlue(), (int)(this.getAnimValue() * this.getAlpha() * 255.0f));
        RenderUtil.GRADIENT_RECT.draw(ms, this.getPickerX(), y, this.getPickerWidth(), h, h * 0.3f, left, right, left, right);
    }
    
    private void drawHueBar(final class_4587 ms, final float animValue) {
        final float y = this.getHueY() + this.getAnimY();
        final float h = this.getAlphaHeight();
        RenderUtil.TEXTURE_RECT.draw(ms, this.getPickerX(), y, this.getPickerWidth(), h, h * 0.3f, new Color(255, 255, 255, (int)(this.getAnimValue() * this.getAlpha() * 255.0f)), 0.0f, 0.0f, 1.0f, 1.0f, QuickImports.mc.method_1531().method_4619(FileUtil.getImage("interface/hue")).method_4624());
    }
    
    private void drawSelectors(final class_4587 ms) {
        final int alpha = (int)(this.getAnimValue() * this.getAlpha() * 255.0f);
        final Color currentColor = this.getCurrentColor();
        final Color cursorColor = ColorUtil.setAlpha(Color.WHITE, alpha);
        final float[] hsb = Color.RGBtoHSB(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), null);
        final float lineOffset = this.scaled(4.0f);
        final float lineHeight;
        final float lineWidth = lineHeight = lineOffset;
        final float lineRound = lineOffset * 0.5f;
        final float lineYOffset = this.getHueHeight() / 2.0f - lineHeight / 2.0f;
        final float circleOffset = this.scaled(2.0f);
        final float circleSize = circleOffset * 2.0f;
        final float satX = this.getPickerX() + hsb[1] * this.getPickerWidth();
        final float briY = this.getColorPickerY() + (1.0f - hsb[2]) * this.getColorPickerHeight();
        RenderUtil.RECT.draw(ms, satX - circleOffset, briY + this.getAnimY() - circleOffset, circleSize, circleSize, circleSize * 0.5f, cursorColor);
        final float hueX = this.getPickerX() + this.hueCache * this.getPickerWidth();
        RenderUtil.RECT.draw(ms, hueX - lineOffset, this.getHueY() + this.getAnimY() + lineYOffset, lineWidth, lineHeight, lineRound, cursorColor);
        final float alphaRel = currentColor.getAlpha() / 255.0f;
        final float alphaX = this.getPickerX() + alphaRel * this.getPickerWidth();
        RenderUtil.RECT.draw(ms, alphaX - lineOffset, this.getAlphaY() + this.getAnimY() + lineYOffset, lineWidth, lineHeight, lineRound, cursorColor);
    }
    
    @Override
    public void keyPressed(final int keyCode, final int scanCode, final int modifiers) {
    }
    
    @Override
    public void mouseScrolled(final double mouseX, final double mouseY, final double horizontalAmount, final double verticalAmount) {
    }
    
    private float getAnimY() {
        return -this.gap() * (1.0f - this.getAnimValue());
    }
    
    private float getColorPickerY() {
        return this.getY() + ((this.setting != null) ? this.scaled(this.getDefaultHeight()) : 0.0f);
    }
    
    private float getColorPickerHeight() {
        return this.getWidth() * this.getAnimValue() * 0.36f;
    }
    
    private float getHueY() {
        return this.getColorPickerY() + this.getColorPickerHeight() + this.gap();
    }
    
    private float getHueHeight() {
        return this.scaled(5.0f) * this.getAnimValue();
    }
    
    private float getAlphaY() {
        return this.getHueY() + this.getHueHeight() + this.gap();
    }
    
    private float getAlphaHeight() {
        return this.getHueHeight();
    }
    
    private float getPickerX() {
        return this.getX();
    }
    
    private float getPickerWidth() {
        return this.getWidth();
    }
    
    private float getDefaultHeight() {
        return 15.0f;
    }
    
    private float getAnimValue() {
        return this.getValue();
    }
    
    private float getModeButtonHeight() {
        return this.scaled(10.0f);
    }
    
    private float getModeButtonY() {
        return this.getAlphaY() + this.getAlphaHeight() + this.gap();
    }
    
    private void drawModeButtons(final class_4587 ms, final float animValue) {
        final int alpha = (int)(animValue * this.getAlpha() * 255.0f);
        final float y = this.getModeButtonY() + this.getAnimY();
        final float h = this.getModeButtonHeight() * animValue;
        final float halfW = (this.getPickerWidth() - this.gap()) / 2.0f;
        final float round = h * 0.3f;
        final float fontSize = h * 0.55f;
        final Color rgbBg = this.rgbMode ? new Color(UIColors.primary().getRed(), UIColors.primary().getGreen(), UIColors.primary().getBlue(), alpha) : ColorUtil.setAlpha(new Color(40, 40, 55), alpha);
        RenderUtil.RECT.draw(ms, this.getPickerX(), y, halfW, h, round, rgbBg);
        final float rgbTw = Fonts.PS_MEDIUM.getWidth("RGB", fontSize);
        Fonts.PS_MEDIUM.drawText(ms, "RGB", this.getPickerX() + halfW / 2.0f - rgbTw / 2.0f, y + h / 2.0f - fontSize / 2.0f, fontSize, ColorUtil.setAlpha(this.rgbMode ? UIColors.textColor() : UIColors.inactiveTextColor(), alpha));
        final float uiX = this.getPickerX() + halfW + this.gap();
        final Color uiBg = this.uiMode ? new Color(UIColors.primary().getRed(), UIColors.primary().getGreen(), UIColors.primary().getBlue(), alpha) : ColorUtil.setAlpha(new Color(40, 40, 55), alpha);
        RenderUtil.RECT.draw(ms, uiX, y, halfW, h, round, uiBg);
        final float uiTw = Fonts.PS_MEDIUM.getWidth("UI", fontSize);
        Fonts.PS_MEDIUM.drawText(ms, "UI", uiX + halfW / 2.0f - uiTw / 2.0f, y + h / 2.0f - fontSize / 2.0f, fontSize, ColorUtil.setAlpha(this.uiMode ? UIColors.textColor() : UIColors.inactiveTextColor(), alpha));
    }
    
    private void tickRgb() {
        final float hue = (System.currentTimeMillis() - this.rgbStartTime) % 5000L / 5000.0f;
        final Color current = this.getCurrentColor();
        final Color rgb = new Color(Color.HSBtoRGB(hue, 0.9f, 1.0f));
        this.setCurrentColor(new Color(rgb.getRed(), rgb.getGreen(), rgb.getBlue(), current.getAlpha()));
        this.hueCache = hue;
    }
    
    private Color[] getGradientColors(final float anim) {
        final int alpha = (int)(anim * this.getAlpha() * 255.0f);
        final Color topLeft = ColorUtil.setAlpha(Color.WHITE, alpha);
        final Color bottom = ColorUtil.setAlpha(Color.BLACK, alpha);
        final float hue = this.hueCache;
        final Color hueColor = new Color(Color.HSBtoRGB(hue, 1.0f, 1.0f));
        final Color topRight = ColorUtil.setAlpha(hueColor, alpha);
        return new Color[] { topLeft, topRight, bottom, bottom };
    }
    
    static {
        rgbActive = Collections.newSetFromMap(new WeakHashMap<ColorComponent, Boolean>());
        GameLoopEvent.getInstance().subscribe(new Listener<GameLoopEvent>(e -> {
            new ArrayList(ColorComponent.rgbActive).iterator();
            final Iterator iterator;
            while (iterator.hasNext()) {
                final ColorComponent comp = iterator.next();
                if (comp.rgbMode) {
                    comp.tickRgb();
                }
            }
        }));
    }
}
