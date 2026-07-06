// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.ui.widget.overlay;

import net.minecraft.class_332;
import java.util.Objects;
import net.minecraft.class_4587;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleManager;
import sweetie.leonware.api.event.events.render.Render2DEvent;
import java.util.ArrayList;
import java.awt.Color;
import org.joml.Vector4f;
import sweetie.leonware.api.module.Module;
import java.util.List;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.client.ui.widget.Widget;

public class ArrayListWidget extends Widget
{
    public final ModeSetting mode;
    public final ModeSetting simpleRect;
    public final BooleanSetting hideRender;
    private final List<Module> enabledCache;
    private final float[] widthCache;
    private final Vector4f roundVec;
    private static final Color SIMPLE_RECT_BG;
    
    public ArrayListWidget() {
        super(2.0f, 30.0f);
        this.mode = new ModeSetting("Array List").values("\u041e\u0431\u044b\u0447\u043d\u044b\u0439", "\u041c\u0430\u0439\u043d\u043a\u0440\u0430\u0444\u0442\u0435\u0440\u0441\u043a\u0438\u0439", "\u041f\u0440\u043e\u0441\u0442\u043e\u0439").value("\u041e\u0431\u044b\u0447\u043d\u044b\u0439");
        this.simpleRect = new ModeSetting("Simple Rect").values("Rect", "Gradient", "Blur", "Glow").value("Rect");
        this.hideRender = new BooleanSetting("Hide Render").value(false);
        this.enabledCache = new ArrayList<Module>();
        this.widthCache = new float[256];
        this.roundVec = new Vector4f();
    }
    
    @Override
    public String getName() {
        return "Array List";
    }
    
    @Override
    public void render(final Render2DEvent.Render2DEventData event) {
        final String s = this.mode.getValue();
        switch (s) {
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
                break;
            }
        }
    }
    
    private void prepareEnabledModules(final boolean isMc, final float fontSize) {
        this.enabledCache.clear();
        final List<Module> all = ModuleManager.getInstance().getModules();
        final boolean hideR = this.hideRender.getValue();
        for (int i = 0, n = all.size(); i < n; ++i) {
            final Module m = all.get(i);
            if (m.isEnabled()) {
                if (!hideR || m.getCategory() != Category.RENDER) {
                    this.enabledCache.add(m);
                }
            }
        }
        final int count = this.enabledCache.size();
        final float[] widths = (count <= this.widthCache.length) ? this.widthCache : new float[count];
        for (int j = 0; j < count; ++j) {
            final String name = this.enabledCache.get(j).getName();
            widths[j] = (isMc ? ((float)ArrayListWidget.mc.field_1772.method_1727(name)) : this.getMediumFont().getWidth(name, fontSize));
        }
        for (int j = 1; j < count; ++j) {
            final Module curM = this.enabledCache.get(j);
            float curW;
            int k;
            for (curW = widths[j], k = j - 1; k >= 0 && widths[k] < curW; --k) {
                this.enabledCache.set(k + 1, this.enabledCache.get(k));
                widths[k + 1] = widths[k];
            }
            this.enabledCache.set(k + 1, curM);
            widths[k + 1] = curW;
        }
    }
    
    private void renderSimple(final Render2DEvent.Render2DEventData event) {
        final class_4587 ms = event.matrixStack();
        final float x = this.getDraggable().getX();
        final float y = this.getDraggable().getY();
        final float width = this.getDraggable().getWidth();
        final boolean isRightSide = x + width / 2.0f > ArrayListWidget.mc.method_22683().method_4486() / 2.0f;
        final float fontSize = this.scaled(6.5f);
        final float paddingH = this.scaled(3.5f);
        final float paddingV = this.scaled(2.0f);
        final float moduleGap = this.scaled(0.5f);
        final float roundness = this.scaled(1.0f);
        this.prepareEnabledModules(false, fontSize);
        float currentY = y;
        float maxSeenWidth = 0.0f;
        final Color textColor = UIColors.textColor();
        Color primary = null;
        Color secondary = null;
        Color blur = null;
        final String simpleMode = this.simpleRect.getValue();
        if ("Gradient".equals(simpleMode) || "Glow".equals(simpleMode)) {
            primary = UIColors.primary();
            secondary = UIColors.secondary();
        }
        else if ("Blur".equals(simpleMode)) {
            blur = UIColors.widgetBlur();
        }
        for (int i = 0, n = this.enabledCache.size(); i < n; ++i) {
            final Module module = this.enabledCache.get(i);
            final String moduleName = module.getName();
            final float textWidth = this.getMediumFont().getWidth(moduleName, fontSize);
            final float rectWidth = textWidth + paddingH * 2.0f;
            final float rectHeight = fontSize + paddingV * 2.0f;
            final float moduleX = isRightSide ? (x + width - rectWidth) : x;
            final float textX = moduleX + paddingH;
            final float textY = currentY + paddingV + 0.5f;
            final String s = simpleMode;
            switch (s) {
                case "Rect": {
                    RenderUtil.RECT.draw(ms, moduleX, currentY, rectWidth, rectHeight, roundness, ArrayListWidget.SIMPLE_RECT_BG);
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
                    break;
                }
            }
            this.getMediumFont().drawText(ms, moduleName, textX, textY, fontSize, textColor);
            if (rectWidth > maxSeenWidth) {
                maxSeenWidth = rectWidth;
            }
            currentY += rectHeight + moduleGap;
        }
        this.getDraggable().setWidth(maxSeenWidth);
        this.getDraggable().setHeight(this.enabledCache.isEmpty() ? this.scaled(10.0f) : (currentY - y - moduleGap));
    }
    
    private void renderDefault(final Render2DEvent.Render2DEventData event) {
        final class_4587 ms = event.matrixStack();
        final float x = this.getDraggable().getX();
        final float y = this.getDraggable().getY();
        final float width = this.getDraggable().getWidth();
        final boolean isRightSide = x + width / 2.0f > ArrayListWidget.mc.method_22683().method_4486() / 2.0f;
        final float fontSize = this.scaled(6.0f);
        final float paddingH = this.scaled(2.5f);
        final float paddingV = this.scaled(1.2f);
        final float stripeWidth = 1.5f;
        this.prepareEnabledModules(false, fontSize);
        float currentY = y;
        float maxSeenWidth = 0.0f;
        final Color bg = UIColors.backgroundBlur();
        final Color primary = UIColors.primary();
        final Color secondary = UIColors.secondary();
        for (int i = 0, n = this.enabledCache.size(); i < n; ++i) {
            final Module module = this.enabledCache.get(i);
            final String moduleName = module.getName();
            final float textWidth = this.getMediumFont().getWidth(moduleName, fontSize);
            final float rectWidth = textWidth + paddingH * 2.0f + stripeWidth;
            final float rectHeight = fontSize + paddingV * 2.0f;
            final float moduleX = isRightSide ? (x + width - rectWidth) : x;
            final float stripeX = isRightSide ? (moduleX + rectWidth - stripeWidth) : moduleX;
            final float textX = isRightSide ? (moduleX + paddingH) : (moduleX + stripeWidth + paddingH);
            RenderUtil.RECT.draw(ms, moduleX, currentY, rectWidth, rectHeight, 0.0f, bg);
            RenderUtil.GRADIENT_RECT.draw(ms, stripeX, currentY, stripeWidth, rectHeight, 0.0f, primary, primary, secondary, secondary);
            this.getMediumFont().drawGradientText(ms, moduleName, textX, currentY + paddingV + 0.5f, fontSize, primary, secondary, textWidth);
            if (rectWidth > maxSeenWidth) {
                maxSeenWidth = rectWidth;
            }
            currentY += rectHeight;
        }
        this.getDraggable().setWidth(maxSeenWidth);
        this.getDraggable().setHeight(this.enabledCache.isEmpty() ? this.scaled(10.0f) : (currentY - y));
    }
    
    private void renderMinecraft(final Render2DEvent.Render2DEventData event) {
        final class_4587 ms = event.matrixStack();
        final class_332 context = event.context();
        final float x = this.getDraggable().getX();
        final float y = this.getDraggable().getY();
        final float width = this.getDraggable().getWidth();
        final boolean isRightSide = x + width / 2.0f > ArrayListWidget.mc.method_22683().method_4486() / 2.0f;
        final float stripeWidth = 1.5f;
        this.prepareEnabledModules(true, 0.0f);
        float currentY = y;
        float maxSeenWidth = 0.0f;
        final long time = System.currentTimeMillis();
        for (int i = 0, n = this.enabledCache.size(); i < n; ++i) {
            final Module module = this.enabledCache.get(i);
            final String moduleName = module.getName();
            final float textWidth = (float)ArrayListWidget.mc.field_1772.method_1727(moduleName);
            final float rectWidth = textWidth + 4.0f + stripeWidth;
            Objects.requireNonNull(ArrayListWidget.mc.field_1772);
            final float rectHeight = 9 + 1;
            final float moduleX = isRightSide ? (x + width - rectWidth) : x;
            final float stripeX = isRightSide ? (moduleX + rectWidth - stripeWidth) : moduleX;
            final float textX = isRightSide ? (moduleX + 2.0f) : (moduleX + stripeWidth + 2.0f);
            final float hue = (time - i * 150L) % 3000L / 3000.0f;
            final Color rainbow = Color.getHSBColor((hue < 0.0f) ? (hue + 1.0f) : hue, 0.8f, 1.0f);
            RenderUtil.RECT.draw(ms, stripeX, currentY, stripeWidth, rectHeight, 0.0f, rainbow);
            context.method_51433(ArrayListWidget.mc.field_1772, moduleName, (int)textX, (int)(currentY + 0.5f), rainbow.getRGB(), true);
            if (rectWidth > maxSeenWidth) {
                maxSeenWidth = rectWidth;
            }
            currentY += rectHeight;
        }
        this.getDraggable().setWidth(maxSeenWidth);
        this.getDraggable().setHeight(this.enabledCache.isEmpty() ? this.scaled(10.0f) : (currentY - y));
    }
    
    @Override
    public void render(final class_4587 matrixStack) {
    }
    
    static {
        SIMPLE_RECT_BG = new Color(18, 18, 24, 255);
    }
}
