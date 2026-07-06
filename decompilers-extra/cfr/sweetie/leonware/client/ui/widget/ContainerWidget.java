/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_408
 *  net.minecraft.class_4587
 */
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
import sweetie.leonware.client.ui.widget.Widget;

public abstract class ContainerWidget
extends Widget {
    private final List<ContainerElement> activeElements = new ArrayList<ContainerElement>();
    private final AnimationUtil showAnimation = new AnimationUtil();
    private final AnimationUtil widthAnimation = new AnimationUtil();
    private final AnimationUtil heightAnimation = new AnimationUtil();
    private float width;
    private float height;
    private float containerHeight;
    private final Map<String, ContainerElement> lookupCache = new HashMap<String, ContainerElement>();

    public ContainerWidget(float x, float y) {
        super(x, y);
    }

    public boolean shouldShow() {
        int n = this.activeElements.size();
        for (int i = 0; i < n; ++i) {
            if (!(this.activeElements.get(i).getAnimation().getValue() > 0.1)) continue;
            return true;
        }
        return false;
    }

    public List<ContainerElement> containerElements() {
        this.updateActiveElements();
        return this.getActiveElements();
    }

    protected abstract Map<String, ContainerElement.ColoredString> getCurrentData();

    protected boolean shouldKeep(ContainerElement element, Map<String, ContainerElement.ColoredString> currentData) {
        return currentData.containsKey(element.getFirst());
    }

    private float getOffset() {
        return this.getGap() * 1.3f;
    }

    private float getDefaultHeight() {
        return this.getFontSize(true) + this.getGap() * 3.0f;
    }

    private int fullAlpha() {
        return (int)(this.showAnimation.getValue() * 255.0);
    }

    private float getFontSize(boolean header) {
        return this.scaled(header ? 8.0f : 7.0f);
    }

    @Override
    public void render(class_4587 matrixStack) {
        float x = this.getDraggable().getX();
        float y = this.getDraggable().getY();
        this.widthAnimation.update();
        this.heightAnimation.update();
        this.showAnimation.update();
        this.showAnimation.run(this.shouldShow() || ContainerWidget.mc.field_1755 instanceof class_408 ? 1.0 : 0.0, this.getDuration(), this.getEasing());
        float animHeight = (float)this.heightAnimation.getValue();
        float animWidth = (float)this.widthAnimation.getValue();
        float offset = this.getOffset() * 1.2f;
        float round = this.getGap() * 2.0f;
        int alpha = this.fullAlpha();
        float headerSize = this.getFontSize(true);
        float headerWidth = this.getMediumFont().getWidth(this.getName(), headerSize);
        this.width = headerWidth + this.getOffset() * 4.0f;
        this.height = this.getDefaultHeight();
        Color blurColor = UIColors.widgetBlur(alpha);
        Color primaryColor = UIColors.primary(alpha);
        Color secColor = UIColors.secondary(alpha);
        RenderUtil.BLUR_RECT.draw(matrixStack, x, y, animWidth, animHeight, round, blurColor);
        this.getMediumFont().drawCenteredGradientText(matrixStack, this.getName(), x + animWidth / 2.0f, y + this.getDefaultHeight() / 2.0f - headerSize / 2.0f, headerSize, primaryColor, secColor, headerWidth / 4.0f);
        ScissorUtil.start(matrixStack, x, y, animWidth, animHeight);
        this.containerHeight = 0.0f;
        float elementY = y + this.getDefaultHeight() + offset / 2.0f;
        float elementSize = this.getFontSize(false);
        float xFirst = x + offset;
        float xSecondBase = x + animWidth - offset;
        List<ContainerElement> elements = this.containerElements();
        float showVal = (float)this.showAnimation.getValue();
        float scaledTwo = this.scaled(2.0f);
        int n = elements.size();
        for (int i = 0; i < n; ++i) {
            ContainerElement containerElement = elements.get(i);
            String first = containerElement.getFirst();
            ContainerElement.ColoredString secondObj = containerElement.getSecond();
            float anim = (float)containerElement.getAnimation().getValue();
            float sex = scaledTwo * (1.0f - anim);
            int elementAlpha = (int)(anim * showVal * 255.0f);
            if (secondObj.hasThreeParts()) {
                String p1 = secondObj.text();
                String p2 = " " + secondObj.textSecond();
                String p3 = secondObj.textThird().isEmpty() ? "" : " " + secondObj.textThird();
                float currentX = xFirst;
                this.getMediumFont().drawText(matrixStack, p1, currentX, elementY - sex, elementSize, ColorUtil.setAlpha(secondObj.color(), elementAlpha));
                this.getMediumFont().drawText(matrixStack, p2, currentX += this.getMediumFont().getWidth(p1, elementSize), elementY - sex, elementSize, ColorUtil.setAlpha(secondObj.colorSecond(), elementAlpha));
                this.getMediumFont().drawText(matrixStack, p3, currentX += this.getMediumFont().getWidth(p2, elementSize), elementY - sex, elementSize, ColorUtil.setAlpha(secondObj.colorThird(), elementAlpha));
                this.updateWidth("", p1 + p2 + p3, offset * 4.0f);
            } else {
                this.getMediumFont().drawText(matrixStack, first, xFirst, elementY - sex, elementSize, UIColors.textColor(elementAlpha));
                String text = secondObj.text();
                float secondWidth = this.getMediumFont().getWidth(text, elementSize);
                this.getMediumFont().drawText(matrixStack, text, xSecondBase - secondWidth, elementY - sex, elementSize, ColorUtil.setAlpha(secondObj.color(), elementAlpha));
                this.updateWidth(first, text, offset * 4.0f);
            }
            float addition = (elementSize + this.getGap()) * anim;
            elementY += addition;
            if (!containerElement.isValid()) continue;
            this.addHeight(addition);
        }
        ScissorUtil.stop(matrixStack);
        this.widthAnimation.run(this.width, this.getDuration(), this.getEasing());
        this.heightAnimation.run(this.height, this.getDuration(), this.getEasing());
        this.getDraggable().setWidth((float)this.widthAnimation.getValue());
        this.getDraggable().setHeight((float)this.heightAnimation.getValue());
    }

    public void addHeight(float value) {
        this.containerHeight += value;
        this.height = this.getDefaultHeight() + this.containerHeight + this.getOffset();
    }

    public void updateWidth(String first, String second, float gap) {
        float fontSize = this.getFontSize(false);
        float total = this.getMediumFont().getWidth(first, fontSize) + gap + this.getMediumFont().getWidth(second, fontSize);
        if (total > this.width) {
            this.width = total;
        }
    }

    private void updateActiveElements() {
        Easing easing = this.getEasing();
        long duration = this.getDuration();
        int n = this.activeElements.size();
        for (int i = 0; i < n; ++i) {
            this.activeElements.get(i).getAnimation().update();
        }
        Map<String, ContainerElement.ColoredString> data = this.getCurrentData();
        if (data == null) {
            data = Collections.emptyMap();
        }
        this.lookupCache.clear();
        int n2 = this.activeElements.size();
        for (int i = 0; i < n2; ++i) {
            ContainerElement e = this.activeElements.get(i);
            this.lookupCache.put(e.getFirst(), e);
        }
        for (Map.Entry<String, ContainerElement.ColoredString> entry : data.entrySet()) {
            String name = entry.getKey();
            ContainerElement.ColoredString value = entry.getValue();
            ContainerElement element = this.lookupCache.get(name);
            if (element == null) {
                ContainerElement newElement = new ContainerElement(name, value);
                newElement.getAnimation().run(1.0, duration, easing);
                this.activeElements.add(newElement);
                continue;
            }
            element.setSecond(value);
            element.setValid(true);
        }
        Map<String, ContainerElement.ColoredString> finalData = data;
        Iterator<ContainerElement> it = this.activeElements.iterator();
        while (it.hasNext()) {
            ContainerElement element = it.next();
            if (finalData.containsKey(element.getFirst())) continue;
            element.setValid(false);
            element.getAnimation().run(0.0, duration, easing);
            if (!(element.getAnimation().getValue() <= 0.01)) continue;
            it.remove();
        }
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

    public static class ContainerElement {
        private String first;
        private ColoredString second;
        private boolean valid = true;
        private final AnimationUtil animation = new AnimationUtil();

        public ContainerElement(String first, ColoredString second) {
            this.first = first;
            this.second = second;
        }

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

        public static class ColoredString {
            private final String text;
            private final String textSecond;
            private final String textThird;
            private final Color color;
            private final Color colorSecond;
            private final Color colorThird;

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
        }
    }
}

