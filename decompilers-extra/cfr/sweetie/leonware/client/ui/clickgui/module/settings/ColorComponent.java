/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_332
 *  net.minecraft.class_4587
 */
package sweetie.leonware.client.ui.clickgui.module.settings;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.GameLoopEvent;
import sweetie.leonware.api.module.setting.ColorSetting;
import sweetie.leonware.api.system.files.FileUtil;
import sweetie.leonware.api.system.interfaces.QuickImports;
import sweetie.leonware.api.utils.color.ColorUtil;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.math.MouseUtil;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.api.utils.render.fonts.Fonts;
import sweetie.leonware.client.ui.clickgui.module.ExpandableComponent;
import sweetie.leonware.client.ui.theme.Theme;

public class ColorComponent
extends ExpandableComponent.ExpandableSettingComponent {
    private final Theme.ElementColor elementColor;
    private final ColorSetting setting;
    private boolean draggingHue = false;
    private boolean draggingSatBright = false;
    private boolean draggingAlpha = false;
    private float hueCache = 0.0f;
    private boolean inited;
    private boolean rgbMode = false;
    private boolean uiMode = false;
    private long rgbStartTime = 0L;
    private static final Set<ColorComponent> rgbActive = Collections.newSetFromMap(new WeakHashMap());

    public ColorComponent(ColorSetting setting) {
        super(setting);
        this.setting = setting;
        this.elementColor = null;
        if (setting != null) {
            this.rgbMode = setting.isRgbMode();
            this.uiMode = setting.isUiMode();
            if (this.rgbMode) {
                this.rgbStartTime = System.currentTimeMillis();
                rgbActive.add(this);
            }
        }
        this.updateHeight(this.getDefaultHeight());
        this.initHueCache();
    }

    public ColorComponent(Theme.ElementColor elementColor) {
        super(null);
        this.elementColor = elementColor;
        this.setting = null;
        this.updateHeight(this.getDefaultHeight());
    }

    private void initHueCache() {
        if (this.inited) {
            return;
        }
        Color color = this.getCurrentColor();
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        this.hueCache = hsb[0];
        this.inited = true;
    }

    private Color getCurrentColor() {
        return this.setting != null ? (Color)this.setting.getValue() : this.elementColor.getColor();
    }

    private void setCurrentColor(Color color) {
        if (this.setting != null) {
            this.setting.setValue(color);
        } else {
            this.elementColor.setColor(color);
        }
    }

    @Override
    public void render(class_332 context, int mouseX, int mouseY, float delta) {
        float animValue;
        class_4587 ms = context.method_51448();
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
        float baseHeight = this.scaled(this.getDefaultHeight());
        float fontSize = baseHeight * 0.45f;
        int fullAlpha = (int)(this.getAlpha() * 255.0f);
        if (this.setting != null) {
            Fonts.PS_MEDIUM.drawText(ms, this.setting.getName(), this.getX(), this.getY() + baseHeight / 2.0f - fontSize / 2.0f, fontSize, UIColors.textColor(fullAlpha));
            float previewSize = baseHeight * 0.7f;
            float previewX = this.getX() + this.getWidth() - previewSize;
            float previewY = this.getY() + baseHeight / 2.0f - previewSize / 2.0f;
            float previewRound = previewSize * 0.2f;
            RenderUtil.RECT.draw(ms, previewX, previewY, previewSize, previewSize, previewRound, ColorUtil.setAlpha(this.getCurrentColor(), (int)((float)this.getCurrentColor().getAlpha() / 255.0f * (float)fullAlpha)));
            this.updateHeight(this.getDefaultHeight());
        }
        if ((double)(animValue = this.getAnimValue()) > 0.0) {
            Color[] colors = this.getGradientColors(animValue);
            float colorPickerRound = this.getWidth() * 0.02f;
            RenderUtil.GRADIENT_RECT.draw(ms, this.getPickerX(), this.getColorPickerY() + this.getAnimY(), this.getPickerWidth(), this.getColorPickerHeight(), colorPickerRound, colors[0], colors[1], colors[2], colors[3]);
            this.drawHueBar(ms, animValue);
            this.drawAlphaBar(ms, animValue);
            this.drawSelectors(ms);
            this.drawModeButtons(ms, animValue);
            float alphaHeight = this.getAlphaHeight() + this.gap();
            float extraHeight = (this.getHueHeight() + this.getColorPickerHeight() + alphaHeight + this.gap() + this.getModeButtonHeight() + this.gap()) * animValue;
            float baseHeightFinal = this.setting != null ? baseHeight : 0.0f;
            this.setHeight(baseHeightFinal + extraHeight);
        }
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (this.setting != null && MouseUtil.isHovered(mouseX, mouseY, (double)this.getX(), (double)this.getY(), (double)this.getWidth(), (double)this.scaled(this.getDefaultHeight()))) {
            this.toggleOpen();
            return;
        }
        if (this.isNotOver()) {
            return;
        }
        float bY = this.getModeButtonY() + this.getAnimY();
        float bH = this.getModeButtonHeight();
        float halfW = (this.getPickerWidth() - this.gap()) / 2.0f;
        if (MouseUtil.isHovered(mouseX, mouseY, (double)this.getPickerX(), (double)bY, (double)halfW, (double)bH)) {
            this.rgbMode = !this.rgbMode;
            this.uiMode = false;
            if (this.setting != null) {
                this.setting.setRgbMode(this.rgbMode);
                this.setting.setUiMode(false);
            }
            if (this.rgbMode) {
                this.rgbStartTime = System.currentTimeMillis();
                rgbActive.add(this);
            } else {
                rgbActive.remove(this);
            }
            return;
        }
        float uiX = this.getPickerX() + halfW + this.gap();
        if (MouseUtil.isHovered(mouseX, mouseY, (double)uiX, (double)bY, (double)halfW, (double)bH)) {
            this.uiMode = !this.uiMode;
            this.rgbMode = false;
            if (this.setting != null) {
                this.setting.setUiMode(this.uiMode);
                this.setting.setRgbMode(false);
            }
            rgbActive.remove(this);
            if (this.uiMode) {
                Color uiColor = UIColors.primary();
                Color current = this.getCurrentColor();
                this.setCurrentColor(new Color(uiColor.getRed(), uiColor.getGreen(), uiColor.getBlue(), current.getAlpha()));
                this.hueCache = Color.RGBtoHSB(uiColor.getRed(), uiColor.getGreen(), uiColor.getBlue(), null)[0];
            }
            return;
        }
        if (MouseUtil.isHovered(mouseX, mouseY, (double)this.getPickerX(), (double)this.getColorPickerY(), (double)this.getPickerWidth(), (double)this.getColorPickerHeight())) {
            this.draggingSatBright = true;
            this.updateSatBright(mouseX, mouseY);
        } else if (MouseUtil.isHovered(mouseX, mouseY, (double)this.getPickerX(), (double)this.getHueY(), (double)this.getPickerWidth(), (double)this.getHueHeight())) {
            this.draggingHue = true;
            this.updateHue(mouseX);
        } else if (MouseUtil.isHovered(mouseX, mouseY, (double)this.getPickerX(), (double)this.getAlphaY(), (double)this.getPickerWidth(), (double)this.getAlphaHeight())) {
            this.draggingAlpha = true;
            this.updateAlpha(mouseX);
        }
    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {
        this.draggingHue = false;
        this.draggingSatBright = false;
        this.draggingAlpha = false;
    }

    private void updateHue(double mouseX) {
        this.rgbMode = false;
        this.uiMode = false;
        float rel = (float)((mouseX - (double)this.getPickerX()) / (double)this.getPickerWidth());
        this.hueCache = rel = Math.max(0.0f, Math.min(1.0f, rel));
        Color color = this.getCurrentColor();
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        this.setCurrentColor(new Color(Color.HSBtoRGB(rel, hsb[1], hsb[2]), true));
    }

    private void updateSatBright(double mouseX, double mouseY) {
        this.rgbMode = false;
        this.uiMode = false;
        float sat = (float)((mouseX - (double)this.getPickerX()) / (double)this.getPickerWidth());
        float bri = 1.0f - (float)((mouseY - (double)this.getColorPickerY()) / (double)this.getColorPickerHeight());
        sat = Math.max(0.0f, Math.min(1.0f, sat));
        bri = Math.max(0.0f, Math.min(1.0f, bri));
        Color color = this.getCurrentColor();
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        float hue = hsb[1] == 0.0f || hsb[2] == 0.0f ? this.hueCache : hsb[0];
        Color newColor = new Color(Color.HSBtoRGB(hue, sat, bri));
        this.setCurrentColor(new Color(newColor.getRed(), newColor.getGreen(), newColor.getBlue(), color.getAlpha()));
    }

    private void updateAlpha(double mouseX) {
        float rel = (float)((mouseX - (double)this.getPickerX()) / (double)this.getPickerWidth());
        rel = Math.max(0.0f, Math.min(1.0f, rel));
        int alpha = (int)(rel * 255.0f);
        Color c = this.getCurrentColor();
        this.setCurrentColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha));
    }

    private void drawAlphaBar(class_4587 ms, float animValue) {
        float y = this.getAlphaY() + this.getAnimY();
        float h = this.getAlphaHeight();
        Color c = this.getCurrentColor();
        Color left = new Color(c.getRed(), c.getGreen(), c.getBlue(), 0);
        Color right = new Color(c.getRed(), c.getGreen(), c.getBlue(), (int)(this.getAnimValue() * this.getAlpha() * 255.0f));
        RenderUtil.GRADIENT_RECT.draw(ms, this.getPickerX(), y, this.getPickerWidth(), h, h * 0.3f, left, right, left, right);
    }

    private void drawHueBar(class_4587 ms, float animValue) {
        float y = this.getHueY() + this.getAnimY();
        float h = this.getAlphaHeight();
        RenderUtil.TEXTURE_RECT.draw(ms, this.getPickerX(), y, this.getPickerWidth(), h, h * 0.3f, new Color(255, 255, 255, (int)(this.getAnimValue() * this.getAlpha() * 255.0f)), 0.0f, 0.0f, 1.0f, 1.0f, QuickImports.mc.method_1531().method_4619(FileUtil.getImage("interface/hue")).method_4624());
    }

    private void drawSelectors(class_4587 ms) {
        float lineOffset;
        float lineWidth;
        int alpha = (int)(this.getAnimValue() * this.getAlpha() * 255.0f);
        Color currentColor = this.getCurrentColor();
        Color cursorColor = ColorUtil.setAlpha(Color.WHITE, alpha);
        float[] hsb = Color.RGBtoHSB(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), null);
        float lineHeight = lineWidth = (lineOffset = this.scaled(4.0f));
        float lineRound = lineOffset * 0.5f;
        float lineYOffset = this.getHueHeight() / 2.0f - lineHeight / 2.0f;
        float circleOffset = this.scaled(2.0f);
        float circleSize = circleOffset * 2.0f;
        float satX = this.getPickerX() + hsb[1] * this.getPickerWidth();
        float briY = this.getColorPickerY() + (1.0f - hsb[2]) * this.getColorPickerHeight();
        RenderUtil.RECT.draw(ms, satX - circleOffset, briY + this.getAnimY() - circleOffset, circleSize, circleSize, circleSize * 0.5f, cursorColor);
        float hueX = this.getPickerX() + this.hueCache * this.getPickerWidth();
        RenderUtil.RECT.draw(ms, hueX - lineOffset, this.getHueY() + this.getAnimY() + lineYOffset, lineWidth, lineHeight, lineRound, cursorColor);
        float alphaRel = (float)currentColor.getAlpha() / 255.0f;
        float alphaX = this.getPickerX() + alphaRel * this.getPickerWidth();
        RenderUtil.RECT.draw(ms, alphaX - lineOffset, this.getAlphaY() + this.getAnimY() + lineYOffset, lineWidth, lineHeight, lineRound, cursorColor);
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
    }

    @Override
    public void mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
    }

    private float getAnimY() {
        return -this.gap() * (1.0f - this.getAnimValue());
    }

    private float getColorPickerY() {
        return this.getY() + (this.setting != null ? this.scaled(this.getDefaultHeight()) : 0.0f);
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

    private void drawModeButtons(class_4587 ms, float animValue) {
        int alpha = (int)(animValue * this.getAlpha() * 255.0f);
        float y = this.getModeButtonY() + this.getAnimY();
        float h = this.getModeButtonHeight() * animValue;
        float halfW = (this.getPickerWidth() - this.gap()) / 2.0f;
        float round = h * 0.3f;
        float fontSize = h * 0.55f;
        Color rgbBg = this.rgbMode ? new Color(UIColors.primary().getRed(), UIColors.primary().getGreen(), UIColors.primary().getBlue(), alpha) : ColorUtil.setAlpha(new Color(40, 40, 55), alpha);
        RenderUtil.RECT.draw(ms, this.getPickerX(), y, halfW, h, round, rgbBg);
        float rgbTw = Fonts.PS_MEDIUM.getWidth("RGB", fontSize);
        Fonts.PS_MEDIUM.drawText(ms, "RGB", this.getPickerX() + halfW / 2.0f - rgbTw / 2.0f, y + h / 2.0f - fontSize / 2.0f, fontSize, ColorUtil.setAlpha(this.rgbMode ? UIColors.textColor() : UIColors.inactiveTextColor(), alpha));
        float uiX = this.getPickerX() + halfW + this.gap();
        Color uiBg = this.uiMode ? new Color(UIColors.primary().getRed(), UIColors.primary().getGreen(), UIColors.primary().getBlue(), alpha) : ColorUtil.setAlpha(new Color(40, 40, 55), alpha);
        RenderUtil.RECT.draw(ms, uiX, y, halfW, h, round, uiBg);
        float uiTw = Fonts.PS_MEDIUM.getWidth("UI", fontSize);
        Fonts.PS_MEDIUM.drawText(ms, "UI", uiX + halfW / 2.0f - uiTw / 2.0f, y + h / 2.0f - fontSize / 2.0f, fontSize, ColorUtil.setAlpha(this.uiMode ? UIColors.textColor() : UIColors.inactiveTextColor(), alpha));
    }

    private void tickRgb() {
        float hue = (float)((System.currentTimeMillis() - this.rgbStartTime) % 5000L) / 5000.0f;
        Color current = this.getCurrentColor();
        Color rgb = new Color(Color.HSBtoRGB(hue, 0.9f, 1.0f));
        this.setCurrentColor(new Color(rgb.getRed(), rgb.getGreen(), rgb.getBlue(), current.getAlpha()));
        this.hueCache = hue;
    }

    private Color[] getGradientColors(float anim) {
        int alpha = (int)(anim * this.getAlpha() * 255.0f);
        Color topLeft = ColorUtil.setAlpha(Color.WHITE, alpha);
        Color bottom = ColorUtil.setAlpha(Color.BLACK, alpha);
        float hue = this.hueCache;
        Color hueColor = new Color(Color.HSBtoRGB(hue, 1.0f, 1.0f));
        Color topRight = ColorUtil.setAlpha(hueColor, alpha);
        return new Color[]{topLeft, topRight, bottom, bottom};
    }

    static {
        GameLoopEvent.getInstance().subscribe(new Listener<GameLoopEvent>(e -> {
            for (ColorComponent comp : new ArrayList<ColorComponent>(rgbActive)) {
                if (!comp.rgbMode) continue;
                comp.tickRgb();
            }
        }));
    }
}

