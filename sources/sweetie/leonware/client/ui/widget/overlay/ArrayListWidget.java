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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/ui/widget/overlay/ArrayListWidget.class */
public class ArrayListWidget extends Widget {
    public final ModeSetting mode;
    public final ModeSetting simpleRect;
    public final BooleanSetting hideRender;
    private final List<Module> enabledCache;
    private final float[] widthCache;
    private final Vector4f roundVec;
    private static final Color SIMPLE_RECT_BG = new Color(18, 18, 24, 255);

    public ArrayListWidget() {
        super(2.0f, 30.0f);
        this.mode = new ModeSetting("Array List").values("Обычный", "Майнкрафтерский", "Простой").value("Обычный");
        this.simpleRect = new ModeSetting("Simple Rect").values("Rect", "Gradient", "Blur", "Glow").value("Rect");
        this.hideRender = new BooleanSetting("Hide Render").value((Boolean) false);
        this.enabledCache = new ArrayList();
        this.widthCache = new float[256];
        this.roundVec = new Vector4f();
    }

    @Override // sweetie.leonware.client.ui.widget.Widget
    public String getName() {
        return "Array List";
    }

    @Override // sweetie.leonware.client.ui.widget.Widget
    public void render(Render2DEvent.Render2DEventData event) {
        switch (this.mode.getValue()) {
            case "Обычный":
                renderDefault(event);
                break;
            case "Майнкрафтерский":
                renderMinecraft(event);
                break;
            case "Простой":
                renderSimple(event);
                break;
        }
    }

    private void prepareEnabledModules(boolean isMc, float fontSize) {
        this.enabledCache.clear();
        List<Module> all = ModuleManager.getInstance().getModules();
        boolean hideR = this.hideRender.getValue().booleanValue();
        int n = all.size();
        for (int i = 0; i < n; i++) {
            Module m = all.get(i);
            if (m.isEnabled() && (!hideR || m.getCategory() != Category.RENDER)) {
                this.enabledCache.add(m);
            }
        }
        int count = this.enabledCache.size();
        float[] widths = count <= this.widthCache.length ? this.widthCache : new float[count];
        for (int i2 = 0; i2 < count; i2++) {
            String name = this.enabledCache.get(i2).getName();
            widths[i2] = isMc ? mc.field_1772.method_1727(name) : getMediumFont().getWidth(name, fontSize);
        }
        for (int i3 = 1; i3 < count; i3++) {
            Module curM = this.enabledCache.get(i3);
            float curW = widths[i3];
            int j = i3 - 1;
            while (j >= 0 && widths[j] < curW) {
                this.enabledCache.set(j + 1, this.enabledCache.get(j));
                widths[j + 1] = widths[j];
                j--;
            }
            this.enabledCache.set(j + 1, curM);
            widths[j + 1] = curW;
        }
    }

    private void renderSimple(Render2DEvent.Render2DEventData event) {
        float rectWidth;
        float rectHeight;
        float moduleX;
        class_4587 ms = event.matrixStack();
        float x = getDraggable().getX();
        float y = getDraggable().getY();
        float width = getDraggable().getWidth();
        boolean isRightSide = x + (width / 2.0f) > ((float) mc.method_22683().method_4486()) / 2.0f;
        float fontSize = scaled(6.5f);
        float paddingH = scaled(3.5f);
        float paddingV = scaled(2.0f);
        float moduleGap = scaled(0.5f);
        float roundness = scaled(1.0f);
        prepareEnabledModules(false, fontSize);
        float currentY = y;
        float maxSeenWidth = 0.0f;
        Color textColor = UIColors.textColor();
        Color primary = null;
        Color secondary = null;
        Color blur = null;
        String simpleMode = this.simpleRect.getValue();
        if ("Gradient".equals(simpleMode) || "Glow".equals(simpleMode)) {
            primary = UIColors.primary();
            secondary = UIColors.secondary();
        } else if ("Blur".equals(simpleMode)) {
            blur = UIColors.widgetBlur();
        }
        int n = this.enabledCache.size();
        for (int i = 0; i < n; i++) {
            Module module = this.enabledCache.get(i);
            String moduleName = module.getName();
            float textWidth = getMediumFont().getWidth(moduleName, fontSize);
            rectWidth = textWidth + (paddingH * 2.0f);
            rectHeight = fontSize + (paddingV * 2.0f);
            moduleX = isRightSide ? (x + width) - rectWidth : x;
            float textX = moduleX + paddingH;
            float textY = currentY + paddingV + 0.5f;
            switch (simpleMode) {
                case "Rect":
                    RenderUtil.RECT.draw(ms, moduleX, currentY, rectWidth, rectHeight, roundness, SIMPLE_RECT_BG);
                    break;
                case "Gradient":
                    this.roundVec.set(roundness, roundness, roundness, roundness);
                    RenderUtil.GRADIENT_RECT.draw(ms, moduleX, currentY, rectWidth, rectHeight, this.roundVec, primary, secondary, secondary, primary);
                    break;
                case "Glow":
                    this.roundVec.set(roundness, roundness, roundness, roundness);
                    RenderUtil.GLOW_RECT.draw(ms, moduleX, currentY, rectWidth, rectHeight, this.roundVec, primary, secondary, secondary, primary, 10.0f, 0.9f);
                    break;
                case "Blur":
                    RenderUtil.BLUR_RECT.draw(ms, moduleX, currentY, rectWidth, rectHeight, roundness, blur);
                    break;
            }
            getMediumFont().drawText(ms, moduleName, textX, textY, fontSize, textColor);
            if (rectWidth > maxSeenWidth) {
                maxSeenWidth = rectWidth;
            }
            currentY += rectHeight + moduleGap;
        }
        getDraggable().setWidth(maxSeenWidth);
        getDraggable().setHeight(this.enabledCache.isEmpty() ? scaled(10.0f) : (currentY - y) - moduleGap);
    }

    private void renderDefault(Render2DEvent.Render2DEventData event) {
        class_4587 ms = event.matrixStack();
        float x = getDraggable().getX();
        float y = getDraggable().getY();
        float width = getDraggable().getWidth();
        boolean isRightSide = x + (width / 2.0f) > ((float) mc.method_22683().method_4486()) / 2.0f;
        float fontSize = scaled(6.0f);
        float paddingH = scaled(2.5f);
        float paddingV = scaled(1.2f);
        prepareEnabledModules(false, fontSize);
        float currentY = y;
        float maxSeenWidth = 0.0f;
        Color bg = UIColors.backgroundBlur();
        Color primary = UIColors.primary();
        Color secondary = UIColors.secondary();
        int n = this.enabledCache.size();
        for (int i = 0; i < n; i++) {
            Module module = this.enabledCache.get(i);
            String moduleName = module.getName();
            float textWidth = getMediumFont().getWidth(moduleName, fontSize);
            float rectWidth = textWidth + (paddingH * 2.0f) + 1.5f;
            float rectHeight = fontSize + (paddingV * 2.0f);
            float moduleX = isRightSide ? (x + width) - rectWidth : x;
            float stripeX = isRightSide ? (moduleX + rectWidth) - 1.5f : moduleX;
            float textX = isRightSide ? moduleX + paddingH : moduleX + 1.5f + paddingH;
            RenderUtil.RECT.draw(ms, moduleX, currentY, rectWidth, rectHeight, 0.0f, bg);
            RenderUtil.GRADIENT_RECT.draw(ms, stripeX, currentY, 1.5f, rectHeight, 0.0f, primary, primary, secondary, secondary);
            getMediumFont().drawGradientText(ms, moduleName, textX, currentY + paddingV + 0.5f, fontSize, primary, secondary, textWidth);
            if (rectWidth > maxSeenWidth) {
                maxSeenWidth = rectWidth;
            }
            currentY += rectHeight;
        }
        getDraggable().setWidth(maxSeenWidth);
        getDraggable().setHeight(this.enabledCache.isEmpty() ? scaled(10.0f) : currentY - y);
    }

    private void renderMinecraft(Render2DEvent.Render2DEventData event) {
        class_4587 ms = event.matrixStack();
        class_332 context = event.context();
        float x = getDraggable().getX();
        float y = getDraggable().getY();
        float width = getDraggable().getWidth();
        boolean isRightSide = x + (width / 2.0f) > ((float) mc.method_22683().method_4486()) / 2.0f;
        prepareEnabledModules(true, 0.0f);
        float currentY = y;
        float maxSeenWidth = 0.0f;
        long time = System.currentTimeMillis();
        int n = this.enabledCache.size();
        for (int i = 0; i < n; i++) {
            Module module = this.enabledCache.get(i);
            String moduleName = module.getName();
            float textWidth = mc.field_1772.method_1727(moduleName);
            float rectWidth = textWidth + 4.0f + 1.5f;
            Objects.requireNonNull(mc.field_1772);
            float rectHeight = 9 + 1;
            float moduleX = isRightSide ? (x + width) - rectWidth : x;
            float stripeX = isRightSide ? (moduleX + rectWidth) - 1.5f : moduleX;
            float textX = isRightSide ? moduleX + 2.0f : moduleX + 1.5f + 2.0f;
            float hue = ((time - (((long) i) * 150)) % 3000) / 3000.0f;
            Color rainbow = Color.getHSBColor(hue < 0.0f ? hue + 1.0f : hue, 0.8f, 1.0f);
            RenderUtil.RECT.draw(ms, stripeX, currentY, 1.5f, rectHeight, 0.0f, rainbow);
            context.method_51433(mc.field_1772, moduleName, (int) textX, (int) (currentY + 0.5f), rainbow.getRGB(), true);
            if (rectWidth > maxSeenWidth) {
                maxSeenWidth = rectWidth;
            }
            currentY += rectHeight;
        }
        getDraggable().setWidth(maxSeenWidth);
        getDraggable().setHeight(this.enabledCache.isEmpty() ? scaled(10.0f) : currentY - y);
    }

    @Override // sweetie.leonware.api.system.interfaces.IRenderer
    public void render(class_4587 matrixStack) {
    }
}
