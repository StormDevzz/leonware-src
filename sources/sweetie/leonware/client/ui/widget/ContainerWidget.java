package sweetie.leonware.client.ui.widget;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import lombok.Generated;
import net.minecraft.class_408;
import net.minecraft.class_4587;
import sweetie.leonware.api.utils.animation.AnimationUtil;
import sweetie.leonware.api.utils.animation.Easing;
import sweetie.leonware.api.utils.color.ColorUtil;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.api.utils.render.ScissorUtil;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/ui/widget/ContainerWidget.class */
public abstract class ContainerWidget extends Widget {
    private final List<ContainerElement> activeElements;
    private final AnimationUtil showAnimation;
    private final AnimationUtil widthAnimation;
    private final AnimationUtil heightAnimation;
    private float width;
    private float height;
    private float containerHeight;
    private final Map<String, ContainerElement> lookupCache;

    protected abstract Map<String, ContainerElement.ColoredString> getCurrentData();

    @Generated
    public void setWidth(float width) {
        this.width = width;
    }

    @Generated
    public void setHeight(float height) {
        this.height = height;
    }

    @Generated
    public void setContainerHeight(float containerHeight) {
        this.containerHeight = containerHeight;
    }

    public ContainerWidget(float x, float y) {
        super(x, y);
        this.activeElements = new ArrayList();
        this.showAnimation = new AnimationUtil();
        this.widthAnimation = new AnimationUtil();
        this.heightAnimation = new AnimationUtil();
        this.lookupCache = new HashMap();
    }

    @Generated
    public List<ContainerElement> getActiveElements() {
        return this.activeElements;
    }

    @Generated
    public AnimationUtil getShowAnimation() {
        return this.showAnimation;
    }

    @Generated
    public AnimationUtil getWidthAnimation() {
        return this.widthAnimation;
    }

    @Generated
    public AnimationUtil getHeightAnimation() {
        return this.heightAnimation;
    }

    @Generated
    public float getWidth() {
        return this.width;
    }

    @Generated
    public float getHeight() {
        return this.height;
    }

    @Generated
    public float getContainerHeight() {
        return this.containerHeight;
    }

    @Generated
    public Map<String, ContainerElement> getLookupCache() {
        return this.lookupCache;
    }

    public boolean shouldShow() {
        int n = this.activeElements.size();
        for (int i = 0; i < n; i++) {
            if (this.activeElements.get(i).getAnimation().getValue() > 0.1d) {
                return true;
            }
        }
        return false;
    }

    public List<ContainerElement> containerElements() {
        updateActiveElements();
        return getActiveElements();
    }

    protected boolean shouldKeep(ContainerElement element, Map<String, ContainerElement.ColoredString> currentData) {
        return currentData.containsKey(element.getFirst());
    }

    private float getOffset() {
        return getGap() * 1.3f;
    }

    private float getDefaultHeight() {
        return getFontSize(true) + (getGap() * 3.0f);
    }

    private int fullAlpha() {
        return (int) (this.showAnimation.getValue() * 255.0d);
    }

    private float getFontSize(boolean header) {
        return scaled(header ? 8.0f : 7.0f);
    }

    @Override // sweetie.leonware.api.system.interfaces.IRenderer
    public void render(class_4587 matrixStack) {
        float x = getDraggable().getX();
        float y = getDraggable().getY();
        this.widthAnimation.update();
        this.heightAnimation.update();
        this.showAnimation.update();
        this.showAnimation.run((shouldShow() || (mc.field_1755 instanceof class_408)) ? 1.0d : 0.0d, getDuration(), getEasing());
        float animHeight = (float) this.heightAnimation.getValue();
        float animWidth = (float) this.widthAnimation.getValue();
        float offset = getOffset() * 1.2f;
        float round = getGap() * 2.0f;
        int alpha = fullAlpha();
        float headerSize = getFontSize(true);
        float headerWidth = getMediumFont().getWidth(getName(), headerSize);
        this.width = headerWidth + (getOffset() * 4.0f);
        this.height = getDefaultHeight();
        Color blurColor = UIColors.widgetBlur(alpha);
        Color primaryColor = UIColors.primary(alpha);
        Color secColor = UIColors.secondary(alpha);
        RenderUtil.BLUR_RECT.draw(matrixStack, x, y, animWidth, animHeight, round, blurColor);
        getMediumFont().drawCenteredGradientText(matrixStack, getName(), x + (animWidth / 2.0f), (y + (getDefaultHeight() / 2.0f)) - (headerSize / 2.0f), headerSize, primaryColor, secColor, headerWidth / 4.0f);
        ScissorUtil.start(matrixStack, x, y, animWidth, animHeight);
        this.containerHeight = 0.0f;
        float elementY = y + getDefaultHeight() + (offset / 2.0f);
        float elementSize = getFontSize(false);
        float xFirst = x + offset;
        float xSecondBase = (x + animWidth) - offset;
        List<ContainerElement> elements = containerElements();
        float showVal = (float) this.showAnimation.getValue();
        float scaledTwo = scaled(2.0f);
        int n = elements.size();
        for (int i = 0; i < n; i++) {
            ContainerElement containerElement = elements.get(i);
            String first = containerElement.getFirst();
            ContainerElement.ColoredString secondObj = containerElement.getSecond();
            float anim = (float) containerElement.getAnimation().getValue();
            float sex = scaledTwo * (1.0f - anim);
            int elementAlpha = (int) (anim * showVal * 255.0f);
            if (secondObj.hasThreeParts()) {
                String p1 = secondObj.text();
                String p2 = " " + secondObj.textSecond();
                String p3 = secondObj.textThird().isEmpty() ? "" : " " + secondObj.textThird();
                getMediumFont().drawText(matrixStack, p1, xFirst, elementY - sex, elementSize, ColorUtil.setAlpha(secondObj.color(), elementAlpha));
                float currentX = xFirst + getMediumFont().getWidth(p1, elementSize);
                getMediumFont().drawText(matrixStack, p2, currentX, elementY - sex, elementSize, ColorUtil.setAlpha(secondObj.colorSecond(), elementAlpha));
                getMediumFont().drawText(matrixStack, p3, currentX + getMediumFont().getWidth(p2, elementSize), elementY - sex, elementSize, ColorUtil.setAlpha(secondObj.colorThird(), elementAlpha));
                updateWidth("", p1 + p2 + p3, offset * 4.0f);
            } else {
                getMediumFont().drawText(matrixStack, first, xFirst, elementY - sex, elementSize, UIColors.textColor(elementAlpha));
                String text = secondObj.text();
                float secondWidth = getMediumFont().getWidth(text, elementSize);
                getMediumFont().drawText(matrixStack, text, xSecondBase - secondWidth, elementY - sex, elementSize, ColorUtil.setAlpha(secondObj.color(), elementAlpha));
                updateWidth(first, text, offset * 4.0f);
            }
            float addition = (elementSize + getGap()) * anim;
            elementY += addition;
            if (containerElement.isValid()) {
                addHeight(addition);
            }
        }
        ScissorUtil.stop(matrixStack);
        this.widthAnimation.run(this.width, getDuration(), getEasing());
        this.heightAnimation.run(this.height, getDuration(), getEasing());
        getDraggable().setWidth((float) this.widthAnimation.getValue());
        getDraggable().setHeight((float) this.heightAnimation.getValue());
    }

    public void addHeight(float value) {
        this.containerHeight += value;
        this.height = getDefaultHeight() + this.containerHeight + getOffset();
    }

    public void updateWidth(String first, String second, float gap) {
        float fontSize = getFontSize(false);
        float total = getMediumFont().getWidth(first, fontSize) + gap + getMediumFont().getWidth(second, fontSize);
        if (total > this.width) {
            this.width = total;
        }
    }

    private void updateActiveElements() {
        Easing easing = getEasing();
        long duration = getDuration();
        int n = this.activeElements.size();
        for (int i = 0; i < n; i++) {
            this.activeElements.get(i).getAnimation().update();
        }
        Map<String, ContainerElement.ColoredString> data = getCurrentData();
        if (data == null) {
            data = Collections.emptyMap();
        }
        this.lookupCache.clear();
        int n2 = this.activeElements.size();
        for (int i2 = 0; i2 < n2; i2++) {
            ContainerElement e = this.activeElements.get(i2);
            this.lookupCache.put(e.getFirst(), e);
        }
        for (Map.Entry<String, ContainerElement.ColoredString> entry : data.entrySet()) {
            String name = entry.getKey();
            ContainerElement.ColoredString value = entry.getValue();
            ContainerElement element = this.lookupCache.get(name);
            if (element == null) {
                ContainerElement newElement = new ContainerElement(name, value);
                newElement.getAnimation().run(1.0d, duration, easing);
                this.activeElements.add(newElement);
            } else {
                element.setSecond(value);
                element.setValid(true);
            }
        }
        Map<String, ContainerElement.ColoredString> finalData = data;
        Iterator<ContainerElement> it = this.activeElements.iterator();
        while (it.hasNext()) {
            ContainerElement element2 = it.next();
            if (!finalData.containsKey(element2.getFirst())) {
                element2.setValid(false);
                element2.getAnimation().run(0.0d, duration, easing);
                if (element2.getAnimation().getValue() <= 0.01d) {
                    it.remove();
                }
            }
        }
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/ui/widget/ContainerWidget$ContainerElement.class */
    public static class ContainerElement {
        private String first;
        private ColoredString second;
        private boolean valid = true;
        private final AnimationUtil animation = new AnimationUtil();

        @Generated
        public void setFirst(String first) {
            this.first = first;
        }

        @Generated
        public void setSecond(ColoredString second) {
            this.second = second;
        }

        @Generated
        public void setValid(boolean valid) {
            this.valid = valid;
        }

        @Generated
        public String getFirst() {
            return this.first;
        }

        @Generated
        public ColoredString getSecond() {
            return this.second;
        }

        @Generated
        public boolean isValid() {
            return this.valid;
        }

        @Generated
        public AnimationUtil getAnimation() {
            return this.animation;
        }

        public ContainerElement(String first, ColoredString second) {
            this.first = first;
            this.second = second;
        }

        /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/ui/widget/ContainerWidget$ContainerElement$ColoredString.class */
        public static class ColoredString {
            private final String text;
            private final String textSecond;
            private final String textThird;
            private final Color color;
            private final Color colorSecond;
            private final Color colorThird;

            @Generated
            public String text() {
                return this.text;
            }

            @Generated
            public String textSecond() {
                return this.textSecond;
            }

            @Generated
            public String textThird() {
                return this.textThird;
            }

            @Generated
            public Color color() {
                return this.color;
            }

            @Generated
            public Color colorSecond() {
                return this.colorSecond;
            }

            @Generated
            public Color colorThird() {
                return this.colorThird;
            }

            public ColoredString(String p1, Color c1, String p2, Color c2, String p3, Color c3) {
                this.text = p1;
                this.color = c1;
                this.textSecond = p2;
                this.colorSecond = c2;
                this.textThird = p3;
                this.colorThird = c3;
            }

            public ColoredString(String text, Color color, String textSecond, Color colorSecond) {
                this(text, color, textSecond, colorSecond, null, null);
            }

            public ColoredString(String text, Color color) {
                this(text, color, null, null, null, null);
            }

            public ColoredString(String text) {
                this(text, UIColors.textColor(), null, null, null, null);
            }

            public boolean hasThreeParts() {
                return this.textThird != null;
            }

            public boolean hasTwoParts() {
                return this.textSecond != null && this.textThird == null;
            }
        }
    }
}
