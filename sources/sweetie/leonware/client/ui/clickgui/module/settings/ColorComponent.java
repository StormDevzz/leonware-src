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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/ui/clickgui/module/settings/ColorComponent.class */
public class ColorComponent extends ExpandableComponent.ExpandableSettingComponent {
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
    private static final Set<ColorComponent> rgbActive = Collections.newSetFromMap(new WeakHashMap());

    public ColorComponent(ColorSetting setting) {
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
                rgbActive.add(this);
            }
        }
        updateHeight(getDefaultHeight());
        initHueCache();
    }

    public ColorComponent(Theme.ElementColor elementColor) {
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
        updateHeight(getDefaultHeight());
    }

    static {
        GameLoopEvent.getInstance().subscribe(new Listener(e -> {
            for (ColorComponent comp : new ArrayList(rgbActive)) {
                if (comp.rgbMode) {
                    comp.tickRgb();
                }
            }
        }));
    }

    private void initHueCache() {
        if (this.inited) {
            return;
        }
        Color color = getCurrentColor();
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), (float[]) null);
        this.hueCache = hsb[0];
        this.inited = true;
    }

    private Color getCurrentColor() {
        return this.setting != null ? this.setting.getValue() : this.elementColor.getColor();
    }

    private void setCurrentColor(Color color) {
        if (this.setting == null) {
            this.elementColor.setColor(color);
        } else {
            this.setting.setValue(color);
        }
    }

    @Override // sweetie.leonware.api.system.interfaces.UIApi
    public void render(class_332 context, int mouseX, int mouseY, float delta) {
        class_4587 ms = context.method_51448();
        updateOpen();
        initHueCache();
        if (this.draggingHue) {
            updateHue(mouseX);
        }
        if (this.draggingSatBright) {
            updateSatBright(mouseX, mouseY);
        }
        if (this.draggingAlpha) {
            updateAlpha(mouseX);
        }
        float baseHeight = scaled(getDefaultHeight());
        float fontSize = baseHeight * 0.45f;
        int fullAlpha = (int) (getAlpha() * 255.0f);
        if (this.setting != null) {
            Fonts.PS_MEDIUM.drawText(ms, this.setting.getName(), getX(), (getY() + (baseHeight / 2.0f)) - (fontSize / 2.0f), fontSize, UIColors.textColor(fullAlpha));
            float previewSize = baseHeight * 0.7f;
            float previewX = (getX() + getWidth()) - previewSize;
            float previewY = (getY() + (baseHeight / 2.0f)) - (previewSize / 2.0f);
            float previewRound = previewSize * 0.2f;
            RenderUtil.RECT.draw(ms, previewX, previewY, previewSize, previewSize, previewRound, ColorUtil.setAlpha(getCurrentColor(), (int) ((getCurrentColor().getAlpha() / 255.0f) * fullAlpha)));
            updateHeight(getDefaultHeight());
        }
        float animValue = getAnimValue();
        if (animValue > 0.0d) {
            Color[] colors = getGradientColors(animValue);
            float colorPickerRound = getWidth() * 0.02f;
            RenderUtil.GRADIENT_RECT.draw(ms, getPickerX(), getColorPickerY() + getAnimY(), getPickerWidth(), getColorPickerHeight(), colorPickerRound, colors[0], colors[1], colors[2], colors[3]);
            drawHueBar(ms, animValue);
            drawAlphaBar(ms, animValue);
            drawSelectors(ms);
            drawModeButtons(ms, animValue);
            float alphaHeight = getAlphaHeight() + gap();
            float extraHeight = (getHueHeight() + getColorPickerHeight() + alphaHeight + gap() + getModeButtonHeight() + gap()) * animValue;
            float baseHeightFinal = this.setting != null ? baseHeight : 0.0f;
            setHeight(baseHeightFinal + extraHeight);
        }
    }

    @Override // sweetie.leonware.api.system.interfaces.UIApi
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (this.setting != null && MouseUtil.isHovered(mouseX, mouseY, getX(), getY(), getWidth(), scaled(getDefaultHeight()))) {
            toggleOpen();
            return;
        }
        if (isNotOver()) {
            return;
        }
        float bY = getModeButtonY() + getAnimY();
        float bH = getModeButtonHeight();
        float halfW = (getPickerWidth() - gap()) / 2.0f;
        if (MouseUtil.isHovered(mouseX, mouseY, getPickerX(), bY, halfW, bH)) {
            this.rgbMode = !this.rgbMode;
            this.uiMode = false;
            if (this.setting != null) {
                this.setting.setRgbMode(this.rgbMode);
                this.setting.setUiMode(false);
            }
            if (this.rgbMode) {
                this.rgbStartTime = System.currentTimeMillis();
                rgbActive.add(this);
                return;
            } else {
                rgbActive.remove(this);
                return;
            }
        }
        float uiX = getPickerX() + halfW + gap();
        if (MouseUtil.isHovered(mouseX, mouseY, uiX, bY, halfW, bH)) {
            this.uiMode = !this.uiMode;
            this.rgbMode = false;
            if (this.setting != null) {
                this.setting.setUiMode(this.uiMode);
                this.setting.setRgbMode(false);
            }
            rgbActive.remove(this);
            if (this.uiMode) {
                Color uiColor = UIColors.primary();
                Color current = getCurrentColor();
                setCurrentColor(new Color(uiColor.getRed(), uiColor.getGreen(), uiColor.getBlue(), current.getAlpha()));
                this.hueCache = Color.RGBtoHSB(uiColor.getRed(), uiColor.getGreen(), uiColor.getBlue(), (float[]) null)[0];
                return;
            }
            return;
        }
        if (MouseUtil.isHovered(mouseX, mouseY, getPickerX(), getColorPickerY(), getPickerWidth(), getColorPickerHeight())) {
            this.draggingSatBright = true;
            updateSatBright(mouseX, mouseY);
        } else if (MouseUtil.isHovered(mouseX, mouseY, getPickerX(), getHueY(), getPickerWidth(), getHueHeight())) {
            this.draggingHue = true;
            updateHue(mouseX);
        } else if (MouseUtil.isHovered(mouseX, mouseY, getPickerX(), getAlphaY(), getPickerWidth(), getAlphaHeight())) {
            this.draggingAlpha = true;
            updateAlpha(mouseX);
        }
    }

    @Override // sweetie.leonware.api.system.interfaces.UIApi
    public void mouseReleased(double mouseX, double mouseY, int button) {
        this.draggingHue = false;
        this.draggingSatBright = false;
        this.draggingAlpha = false;
    }

    private void updateHue(double mouseX) {
        this.rgbMode = false;
        this.uiMode = false;
        float rel = Math.max(0.0f, Math.min(1.0f, (float) ((mouseX - ((double) getPickerX())) / ((double) getPickerWidth()))));
        this.hueCache = rel;
        Color color = getCurrentColor();
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), (float[]) null);
        setCurrentColor(new Color(Color.HSBtoRGB(rel, hsb[1], hsb[2]), true));
    }

    private void updateSatBright(double mouseX, double mouseY) {
        this.rgbMode = false;
        this.uiMode = false;
        float sat = (float) ((mouseX - ((double) getPickerX())) / ((double) getPickerWidth()));
        float bri = 1.0f - ((float) ((mouseY - ((double) getColorPickerY())) / ((double) getColorPickerHeight())));
        float sat2 = Math.max(0.0f, Math.min(1.0f, sat));
        float bri2 = Math.max(0.0f, Math.min(1.0f, bri));
        Color color = getCurrentColor();
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), (float[]) null);
        float hue = (hsb[1] == 0.0f || hsb[2] == 0.0f) ? this.hueCache : hsb[0];
        Color newColor = new Color(Color.HSBtoRGB(hue, sat2, bri2));
        setCurrentColor(new Color(newColor.getRed(), newColor.getGreen(), newColor.getBlue(), color.getAlpha()));
    }

    private void updateAlpha(double mouseX) {
        float rel = (float) ((mouseX - ((double) getPickerX())) / ((double) getPickerWidth()));
        int alpha = (int) (Math.max(0.0f, Math.min(1.0f, rel)) * 255.0f);
        Color c = getCurrentColor();
        setCurrentColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha));
    }

    private void drawAlphaBar(class_4587 ms, float animValue) {
        float y = getAlphaY() + getAnimY();
        float h = getAlphaHeight();
        Color c = getCurrentColor();
        Color left = new Color(c.getRed(), c.getGreen(), c.getBlue(), 0);
        Color right = new Color(c.getRed(), c.getGreen(), c.getBlue(), (int) (getAnimValue() * getAlpha() * 255.0f));
        RenderUtil.GRADIENT_RECT.draw(ms, getPickerX(), y, getPickerWidth(), h, h * 0.3f, left, right, left, right);
    }

    private void drawHueBar(class_4587 ms, float animValue) {
        float y = getHueY() + getAnimY();
        float h = getAlphaHeight();
        RenderUtil.TEXTURE_RECT.draw(ms, getPickerX(), y, getPickerWidth(), h, h * 0.3f, new Color(255, 255, 255, (int) (getAnimValue() * getAlpha() * 255.0f)), 0.0f, 0.0f, 1.0f, 1.0f, QuickImports.mc.method_1531().method_4619(FileUtil.getImage("interface/hue")).method_4624());
    }

    private void drawSelectors(class_4587 ms) {
        int alpha = (int) (getAnimValue() * getAlpha() * 255.0f);
        Color currentColor = getCurrentColor();
        Color cursorColor = ColorUtil.setAlpha(Color.WHITE, alpha);
        float[] hsb = Color.RGBtoHSB(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), (float[]) null);
        float lineOffset = scaled(4.0f);
        float lineRound = lineOffset * 0.5f;
        float lineYOffset = (getHueHeight() / 2.0f) - (lineOffset / 2.0f);
        float circleOffset = scaled(2.0f);
        float circleSize = circleOffset * 2.0f;
        float satX = getPickerX() + (hsb[1] * getPickerWidth());
        float briY = getColorPickerY() + ((1.0f - hsb[2]) * getColorPickerHeight());
        RenderUtil.RECT.draw(ms, satX - circleOffset, (briY + getAnimY()) - circleOffset, circleSize, circleSize, circleSize * 0.5f, cursorColor);
        float hueX = getPickerX() + (this.hueCache * getPickerWidth());
        RenderUtil.RECT.draw(ms, hueX - lineOffset, getHueY() + getAnimY() + lineYOffset, lineOffset, lineOffset, lineRound, cursorColor);
        float alphaRel = currentColor.getAlpha() / 255.0f;
        float alphaX = getPickerX() + (alphaRel * getPickerWidth());
        RenderUtil.RECT.draw(ms, alphaX - lineOffset, getAlphaY() + getAnimY() + lineYOffset, lineOffset, lineOffset, lineRound, cursorColor);
    }

    @Override // sweetie.leonware.api.system.interfaces.UIApi
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
    }

    @Override // sweetie.leonware.api.system.interfaces.UIApi
    public void mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
    }

    private float getAnimY() {
        return (-gap()) * (1.0f - getAnimValue());
    }

    private float getColorPickerY() {
        return getY() + (this.setting != null ? scaled(getDefaultHeight()) : 0.0f);
    }

    private float getColorPickerHeight() {
        return getWidth() * getAnimValue() * 0.36f;
    }

    private float getHueY() {
        return getColorPickerY() + getColorPickerHeight() + gap();
    }

    private float getHueHeight() {
        return scaled(5.0f) * getAnimValue();
    }

    private float getAlphaY() {
        return getHueY() + getHueHeight() + gap();
    }

    private float getAlphaHeight() {
        return getHueHeight();
    }

    private float getPickerX() {
        return getX();
    }

    private float getPickerWidth() {
        return getWidth();
    }

    private float getDefaultHeight() {
        return 15.0f;
    }

    private float getAnimValue() {
        return getValue();
    }

    private float getModeButtonHeight() {
        return scaled(10.0f);
    }

    private float getModeButtonY() {
        return getAlphaY() + getAlphaHeight() + gap();
    }

    private void drawModeButtons(class_4587 ms, float animValue) {
        Color alpha;
        Color alpha2;
        int alpha3 = (int) (animValue * getAlpha() * 255.0f);
        float y = getModeButtonY() + getAnimY();
        float h = getModeButtonHeight() * animValue;
        float halfW = (getPickerWidth() - gap()) / 2.0f;
        float round = h * 0.3f;
        float fontSize = h * 0.55f;
        if (this.rgbMode) {
            alpha = new Color(UIColors.primary().getRed(), UIColors.primary().getGreen(), UIColors.primary().getBlue(), alpha3);
        } else {
            alpha = ColorUtil.setAlpha(new Color(40, 40, 55), alpha3);
        }
        Color rgbBg = alpha;
        RenderUtil.RECT.draw(ms, getPickerX(), y, halfW, h, round, rgbBg);
        float rgbTw = Fonts.PS_MEDIUM.getWidth("RGB", fontSize);
        Fonts.PS_MEDIUM.drawText(ms, "RGB", (getPickerX() + (halfW / 2.0f)) - (rgbTw / 2.0f), (y + (h / 2.0f)) - (fontSize / 2.0f), fontSize, ColorUtil.setAlpha(this.rgbMode ? UIColors.textColor() : UIColors.inactiveTextColor(), alpha3));
        float uiX = getPickerX() + halfW + gap();
        if (this.uiMode) {
            alpha2 = new Color(UIColors.primary().getRed(), UIColors.primary().getGreen(), UIColors.primary().getBlue(), alpha3);
        } else {
            alpha2 = ColorUtil.setAlpha(new Color(40, 40, 55), alpha3);
        }
        Color uiBg = alpha2;
        RenderUtil.RECT.draw(ms, uiX, y, halfW, h, round, uiBg);
        float uiTw = Fonts.PS_MEDIUM.getWidth("UI", fontSize);
        Fonts.PS_MEDIUM.drawText(ms, "UI", (uiX + (halfW / 2.0f)) - (uiTw / 2.0f), (y + (h / 2.0f)) - (fontSize / 2.0f), fontSize, ColorUtil.setAlpha(this.uiMode ? UIColors.textColor() : UIColors.inactiveTextColor(), alpha3));
    }

    private void tickRgb() {
        float hue = ((System.currentTimeMillis() - this.rgbStartTime) % 5000) / 5000.0f;
        Color current = getCurrentColor();
        Color rgb = new Color(Color.HSBtoRGB(hue, 0.9f, 1.0f));
        setCurrentColor(new Color(rgb.getRed(), rgb.getGreen(), rgb.getBlue(), current.getAlpha()));
        this.hueCache = hue;
    }

    private Color[] getGradientColors(float anim) {
        int alpha = (int) (anim * getAlpha() * 255.0f);
        Color topLeft = ColorUtil.setAlpha(Color.WHITE, alpha);
        Color bottom = ColorUtil.setAlpha(Color.BLACK, alpha);
        float hue = this.hueCache;
        Color hueColor = new Color(Color.HSBtoRGB(hue, 1.0f, 1.0f));
        Color topRight = ColorUtil.setAlpha(hueColor, alpha);
        return new Color[]{topLeft, topRight, bottom, bottom};
    }
}
