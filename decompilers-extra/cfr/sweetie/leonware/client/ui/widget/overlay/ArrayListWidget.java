/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_332
 *  net.minecraft.class_4587
 *  org.joml.Vector4f
 */
package sweetie.leonware.client.ui.widget.overlay;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import org.joml.Vector4f;
import sweetie.leonware.api.event.events.render.Render2DEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleManager;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.client.ui.widget.Widget;

public class ArrayListWidget
extends Widget {
    public final ModeSetting mode = new ModeSetting("Array List").values("\u041e\u0431\u044b\u0447\u043d\u044b\u0439", "\u041c\u0430\u0439\u043d\u043a\u0440\u0430\u0444\u0442\u0435\u0440\u0441\u043a\u0438\u0439", "\u041f\u0440\u043e\u0441\u0442\u043e\u0439").value("\u041e\u0431\u044b\u0447\u043d\u044b\u0439");
    public final ModeSetting simpleRect = new ModeSetting("Simple Rect").values("Rect", "Gradient", "Blur", "Glow").value("Rect");
    public final BooleanSetting hideRender = new BooleanSetting("Hide Render").value(false);
    private final List<Module> enabledCache = new ArrayList<Module>();
    private final float[] widthCache = new float[256];
    private final Vector4f roundVec = new Vector4f();
    private static final Color SIMPLE_RECT_BG = new Color(18, 18, 24, 255);

    public ArrayListWidget() {
        super(2.0f, 30.0f);
    }

    @Override
    public String getName() {
        return "Array List";
    }

    @Override
    public void render(Render2DEvent.Render2DEventData event) {
        switch ((String)this.mode.getValue()) {
            case "\u041e\u0431\u044b\u0447\u043d\u044b\u0439": {
                this.renderDefault(event);
                break;
            }
            case "\u041c\u0430\u0439\u043d\u043a\u0440\u0430\u0444\u0442\u0435\u0440\u0441\u043a\u0438\u0439": {
                this.renderMinecraft(event);
                break;
            }
            case "\u041f\u0440\u043e\u0441\u0442\u043e\u0439": {
                this.renderSimple(event);
            }
        }
    }

    private void prepareEnabledModules(boolean isMc, float fontSize) {
        int i;
        this.enabledCache.clear();
        List<Module> all = ModuleManager.getInstance().getModules();
        boolean hideR = (Boolean)this.hideRender.getValue();
        int n = all.size();
        for (int i2 = 0; i2 < n; ++i2) {
            Module m = all.get(i2);
            if (!m.isEnabled() || hideR && m.getCategory() == Category.RENDER) continue;
            this.enabledCache.add(m);
        }
        int count = this.enabledCache.size();
        float[] widths = count <= this.widthCache.length ? this.widthCache : new float[count];
        for (i = 0; i < count; ++i) {
            String name = this.enabledCache.get(i).getName();
            widths[i] = isMc ? (float)ArrayListWidget.mc.field_1772.method_1727(name) : this.getMediumFont().getWidth(name, fontSize);
        }
        for (i = 1; i < count; ++i) {
            int j;
            Module curM = this.enabledCache.get(i);
            float curW = widths[i];
            for (j = i - 1; j >= 0 && widths[j] < curW; --j) {
                this.enabledCache.set(j + 1, this.enabledCache.get(j));
                widths[j + 1] = widths[j];
            }
            this.enabledCache.set(j + 1, curM);
            widths[j + 1] = curW;
        }
    }

    private void renderSimple(Render2DEvent.Render2DEventData event) {
        class_4587 ms = event.matrixStack();
        float x = this.getDraggable().getX();
        float y = this.getDraggable().getY();
        float width = this.getDraggable().getWidth();
        boolean isRightSide = x + width / 2.0f > (float)mc.method_22683().method_4486() / 2.0f;
        float fontSize = this.scaled(6.5f);
        float paddingH = this.scaled(3.5f);
        float paddingV = this.scaled(2.0f);
        float moduleGap = this.scaled(0.5f);
        float roundness = this.scaled(1.0f);
        this.prepareEnabledModules(false, fontSize);
        float currentY = y;
        float maxSeenWidth = 0.0f;
        Color textColor = UIColors.textColor();
        Color primary = null;
        Color secondary = null;
        Color blur = null;
        String simpleMode = (String)this.simpleRect.getValue();
        if ("Gradient".equals(simpleMode) || "Glow".equals(simpleMode)) {
            primary = UIColors.primary();
            secondary = UIColors.secondary();
        } else if ("Blur".equals(simpleMode)) {
            blur = UIColors.widgetBlur();
        }
        int n = this.enabledCache.size();
        for (int i = 0; i < n; ++i) {
            Module module = this.enabledCache.get(i);
            String moduleName = module.getName();
            float textWidth = this.getMediumFont().getWidth(moduleName, fontSize);
            float rectWidth = textWidth + paddingH * 2.0f;
            float rectHeight = fontSize + paddingV * 2.0f;
            float moduleX = isRightSide ? x + width - rectWidth : x;
            float textX = moduleX + paddingH;
            float textY = currentY + paddingV + 0.5f;
            switch (simpleMode) {
                case "Rect": {
                    RenderUtil.RECT.draw(ms, moduleX, currentY, rectWidth, rectHeight, roundness, SIMPLE_RECT_BG);
                    break;
                }
                case "Gradient": {
                    this.roundVec.set(roundness, roundness, roundness, roundness);
                    RenderUtil.GRADIENT_RECT.draw(ms, moduleX, currentY, rectWidth, rectHeight, this.roundVec, primary, secondary, secondary, primary);
                    break;
                }
                case "Glow": {
                    this.roundVec.set(roundness, roundness, roundness, roundness);
                    RenderUtil.GLOW_RECT.draw(ms, moduleX, currentY, rectWidth, rectHeight, this.roundVec, primary, secondary, secondary, primary, 10.0f, 0.9f);
                    break;
                }
                case "Blur": {
                    RenderUtil.BLUR_RECT.draw(ms, moduleX, currentY, rectWidth, rectHeight, roundness, blur);
                }
            }
            this.getMediumFont().drawText(ms, moduleName, textX, textY, fontSize, textColor);
            if (rectWidth > maxSeenWidth) {
                maxSeenWidth = rectWidth;
            }
            currentY += rectHeight + moduleGap;
        }
        this.getDraggable().setWidth(maxSeenWidth);
        this.getDraggable().setHeight(this.enabledCache.isEmpty() ? this.scaled(10.0f) : currentY - y - moduleGap);
    }

    private void renderDefault(Render2DEvent.Render2DEventData event) {
        class_4587 ms = event.matrixStack();
        float x = this.getDraggable().getX();
        float y = this.getDraggable().getY();
        float width = this.getDraggable().getWidth();
        boolean isRightSide = x + width / 2.0f > (float)mc.method_22683().method_4486() / 2.0f;
        float fontSize = this.scaled(6.0f);
        float paddingH = this.scaled(2.5f);
        float paddingV = this.scaled(1.2f);
        float stripeWidth = 1.5f;
        this.prepareEnabledModules(false, fontSize);
        float currentY = y;
        float maxSeenWidth = 0.0f;
        Color bg = UIColors.backgroundBlur();
        Color primary = UIColors.primary();
        Color secondary = UIColors.secondary();
        int n = this.enabledCache.size();
        for (int i = 0; i < n; ++i) {
            Module module = this.enabledCache.get(i);
            String moduleName = module.getName();
            float textWidth = this.getMediumFont().getWidth(moduleName, fontSize);
            float rectWidth = textWidth + paddingH * 2.0f + stripeWidth;
            float rectHeight = fontSize + paddingV * 2.0f;
            float moduleX = isRightSide ? x + width - rectWidth : x;
            float stripeX = isRightSide ? moduleX + rectWidth - stripeWidth : moduleX;
            float textX = isRightSide ? moduleX + paddingH : moduleX + stripeWidth + paddingH;
            RenderUtil.RECT.draw(ms, moduleX, currentY, rectWidth, rectHeight, 0.0f, bg);
            RenderUtil.GRADIENT_RECT.draw(ms, stripeX, currentY, stripeWidth, rectHeight, 0.0f, primary, primary, secondary, secondary);
            this.getMediumFont().drawGradientText(ms, moduleName, textX, currentY + paddingV + 0.5f, fontSize, primary, secondary, textWidth);
            if (rectWidth > maxSeenWidth) {
                maxSeenWidth = rectWidth;
            }
            currentY += rectHeight;
        }
        this.getDraggable().setWidth(maxSeenWidth);
        this.getDraggable().setHeight(this.enabledCache.isEmpty() ? this.scaled(10.0f) : currentY - y);
    }

    private void renderMinecraft(Render2DEvent.Render2DEventData event) {
        class_4587 ms = event.matrixStack();
        class_332 context = event.context();
        float x = this.getDraggable().getX();
        float y = this.getDraggable().getY();
        float width = this.getDraggable().getWidth();
        boolean isRightSide = x + width / 2.0f > (float)mc.method_22683().method_4486() / 2.0f;
        float stripeWidth = 1.5f;
        this.prepareEnabledModules(true, 0.0f);
        float currentY = y;
        float maxSeenWidth = 0.0f;
        long time = System.currentTimeMillis();
        int n = this.enabledCache.size();
        for (int i = 0; i < n; ++i) {
            Module module = this.enabledCache.get(i);
            String moduleName = module.getName();
            float textWidth = ArrayListWidget.mc.field_1772.method_1727(moduleName);
            float rectWidth = textWidth + 4.0f + stripeWidth;
            Objects.requireNonNull(ArrayListWidget.mc.field_1772);
            float rectHeight = 9 + 1;
            float moduleX = isRightSide ? x + width - rectWidth : x;
            float stripeX = isRightSide ? moduleX + rectWidth - stripeWidth : moduleX;
            float textX = isRightSide ? moduleX + 2.0f : moduleX + stripeWidth + 2.0f;
            float hue = (float)((time - (long)i * 150L) % 3000L) / 3000.0f;
            Color rainbow = Color.getHSBColor(hue < 0.0f ? hue + 1.0f : hue, 0.8f, 1.0f);
            RenderUtil.RECT.draw(ms, stripeX, currentY, stripeWidth, rectHeight, 0.0f, rainbow);
            context.method_51433(ArrayListWidget.mc.field_1772, moduleName, (int)textX, (int)(currentY + 0.5f), rainbow.getRGB(), true);
            if (rectWidth > maxSeenWidth) {
                maxSeenWidth = rectWidth;
            }
            currentY += rectHeight;
        }
        this.getDraggable().setWidth(maxSeenWidth);
        this.getDraggable().setHeight(this.enabledCache.isEmpty() ? this.scaled(10.0f) : currentY - y);
    }

    @Override
    public void render(class_4587 matrixStack) {
    }
}

