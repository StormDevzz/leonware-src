// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.ui.widget;

import lombok.Generated;
import java.util.Iterator;
import sweetie.leonware.api.utils.animation.Easing;
import java.util.Collections;
import java.awt.Color;
import sweetie.leonware.api.utils.color.ColorUtil;
import sweetie.leonware.api.utils.render.ScissorUtil;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.api.utils.color.UIColors;
import net.minecraft.class_408;
import net.minecraft.class_4587;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import sweetie.leonware.api.utils.animation.AnimationUtil;
import java.util.List;

public abstract class ContainerWidget extends Widget
{
    private final List<ContainerElement> activeElements;
    private final AnimationUtil showAnimation;
    private final AnimationUtil widthAnimation;
    private final AnimationUtil heightAnimation;
    private float width;
    private float height;
    private float containerHeight;
    private final Map<String, ContainerElement> lookupCache;
    
    public ContainerWidget(final float x, final float y) {
        super(x, y);
        this.activeElements = new ArrayList<ContainerElement>();
        this.showAnimation = new AnimationUtil();
        this.widthAnimation = new AnimationUtil();
        this.heightAnimation = new AnimationUtil();
        this.lookupCache = new HashMap<String, ContainerElement>();
    }
    
    public boolean shouldShow() {
        for (int i = 0, n = this.activeElements.size(); i < n; ++i) {
            if (this.activeElements.get(i).getAnimation().getValue() > 0.1) {
                return true;
            }
        }
        return false;
    }
    
    public List<ContainerElement> containerElements() {
        this.updateActiveElements();
        return this.getActiveElements();
    }
    
    protected abstract Map<String, ContainerElement.ColoredString> getCurrentData();
    
    protected boolean shouldKeep(final ContainerElement element, final Map<String, ContainerElement.ColoredString> currentData) {
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
    
    private float getFontSize(final boolean header) {
        return this.scaled(header ? 8.0f : 7.0f);
    }
    
    @Override
    public void render(final class_4587 matrixStack) {
        final float x = this.getDraggable().getX();
        final float y = this.getDraggable().getY();
        this.widthAnimation.update();
        this.heightAnimation.update();
        this.showAnimation.update();
        this.showAnimation.run((this.shouldShow() || ContainerWidget.mc.field_1755 instanceof class_408) ? 1.0 : 0.0, this.getDuration(), this.getEasing());
        final float animHeight = (float)this.heightAnimation.getValue();
        final float animWidth = (float)this.widthAnimation.getValue();
        final float offset = this.getOffset() * 1.2f;
        final float round = this.getGap() * 2.0f;
        final int alpha = this.fullAlpha();
        final float headerSize = this.getFontSize(true);
        final float headerWidth = this.getMediumFont().getWidth(this.getName(), headerSize);
        this.width = headerWidth + this.getOffset() * 4.0f;
        this.height = this.getDefaultHeight();
        final Color blurColor = UIColors.widgetBlur(alpha);
        final Color primaryColor = UIColors.primary(alpha);
        final Color secColor = UIColors.secondary(alpha);
        RenderUtil.BLUR_RECT.draw(matrixStack, x, y, animWidth, animHeight, round, blurColor);
        this.getMediumFont().drawCenteredGradientText(matrixStack, this.getName(), x + animWidth / 2.0f, y + this.getDefaultHeight() / 2.0f - headerSize / 2.0f, headerSize, primaryColor, secColor, headerWidth / 4.0f);
        ScissorUtil.start(matrixStack, x, y, animWidth, animHeight);
        this.containerHeight = 0.0f;
        float elementY = y + this.getDefaultHeight() + offset / 2.0f;
        final float elementSize = this.getFontSize(false);
        final float xFirst = x + offset;
        final float xSecondBase = x + animWidth - offset;
        final List<ContainerElement> elements = this.containerElements();
        final float showVal = (float)this.showAnimation.getValue();
        final float scaledTwo = this.scaled(2.0f);
        for (int i = 0, n = elements.size(); i < n; ++i) {
            final ContainerElement containerElement = elements.get(i);
            final String first = containerElement.getFirst();
            final ContainerElement.ColoredString secondObj = containerElement.getSecond();
            final float anim = (float)containerElement.getAnimation().getValue();
            final float sex = scaledTwo * (1.0f - anim);
            final int elementAlpha = (int)(anim * showVal * 255.0f);
            if (secondObj.hasThreeParts()) {
                final String p1 = secondObj.text();
                final String p2 = " " + secondObj.textSecond();
                final String p3 = secondObj.textThird().isEmpty() ? "" : (" " + secondObj.textThird());
                float currentX = xFirst;
                this.getMediumFont().drawText(matrixStack, p1, currentX, elementY - sex, elementSize, ColorUtil.setAlpha(secondObj.color(), elementAlpha));
                currentX += this.getMediumFont().getWidth(p1, elementSize);
                this.getMediumFont().drawText(matrixStack, p2, currentX, elementY - sex, elementSize, ColorUtil.setAlpha(secondObj.colorSecond(), elementAlpha));
                currentX += this.getMediumFont().getWidth(p2, elementSize);
                this.getMediumFont().drawText(matrixStack, p3, currentX, elementY - sex, elementSize, ColorUtil.setAlpha(secondObj.colorThird(), elementAlpha));
                this.updateWidth("", p1 + p2 + p3, offset * 4.0f);
            }
            else {
                this.getMediumFont().drawText(matrixStack, first, xFirst, elementY - sex, elementSize, UIColors.textColor(elementAlpha));
                final String text = secondObj.text();
                final float secondWidth = this.getMediumFont().getWidth(text, elementSize);
                this.getMediumFont().drawText(matrixStack, text, xSecondBase - secondWidth, elementY - sex, elementSize, ColorUtil.setAlpha(secondObj.color(), elementAlpha));
                this.updateWidth(first, text, offset * 4.0f);
            }
            final float addition = (elementSize + this.getGap()) * anim;
            elementY += addition;
            if (containerElement.isValid()) {
                this.addHeight(addition);
            }
        }
        ScissorUtil.stop(matrixStack);
        this.widthAnimation.run(this.width, this.getDuration(), this.getEasing());
        this.heightAnimation.run(this.height, this.getDuration(), this.getEasing());
        this.getDraggable().setWidth((float)this.widthAnimation.getValue());
        this.getDraggable().setHeight((float)this.heightAnimation.getValue());
    }
    
    public void addHeight(final float value) {
        this.containerHeight += value;
        this.height = this.getDefaultHeight() + this.containerHeight + this.getOffset();
    }
    
    public void updateWidth(final String first, final String second, final float gap) {
        final float fontSize = this.getFontSize(false);
        final float total = this.getMediumFont().getWidth(first, fontSize) + gap + this.getMediumFont().getWidth(second, fontSize);
        if (total > this.width) {
            this.width = total;
        }
    }
    
    private void updateActiveElements() {
        final Easing easing = this.getEasing();
        final long duration = this.getDuration();
        for (int i = 0, n = this.activeElements.size(); i < n; ++i) {
            this.activeElements.get(i).getAnimation().update();
        }
        Map<String, ContainerElement.ColoredString> data = this.getCurrentData();
        if (data == null) {
            data = Collections.emptyMap();
        }
        this.lookupCache.clear();
        for (int j = 0, n2 = this.activeElements.size(); j < n2; ++j) {
            final ContainerElement e = this.activeElements.get(j);
            this.lookupCache.put(e.getFirst(), e);
        }
        for (final Map.Entry<String, ContainerElement.ColoredString> entry : data.entrySet()) {
            final String name = entry.getKey();
            final ContainerElement.ColoredString value = entry.getValue();
            final ContainerElement element = this.lookupCache.get(name);
            if (element == null) {
                final ContainerElement newElement = new ContainerElement(name, value);
                newElement.getAnimation().run(1.0, duration, easing);
                this.activeElements.add(newElement);
            }
            else {
                element.setSecond(value);
                element.setValid(true);
            }
        }
        final Map<String, ContainerElement.ColoredString> finalData = data;
        final Iterator<ContainerElement> it = this.activeElements.iterator();
        while (it.hasNext()) {
            final ContainerElement element2 = it.next();
            if (!finalData.containsKey(element2.getFirst())) {
                element2.setValid(false);
                element2.getAnimation().run(0.0, duration, easing);
                if (element2.getAnimation().getValue() > 0.01) {
                    continue;
                }
                it.remove();
            }
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
    public void setWidth(final float width) {
        this.width = width;
    }
    
    @Generated
    public void setHeight(final float height) {
        this.height = height;
    }
    
    @Generated
    public void setContainerHeight(final float containerHeight) {
        this.containerHeight = containerHeight;
    }
    
    public static class ContainerElement
    {
        private String first;
        private ColoredString second;
        private boolean valid;
        private final AnimationUtil animation;
        
        public ContainerElement(final String first, final ColoredString second) {
            this.valid = true;
            this.animation = new AnimationUtil();
            this.first = first;
            this.second = second;
        }
        
        @Generated
        public void setFirst(final String first) {
            this.first = first;
        }
        
        @Generated
        public void setSecond(final ColoredString second) {
            this.second = second;
        }
        
        @Generated
        public void setValid(final boolean valid) {
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
        
        public static class ColoredString
        {
            private final String text;
            private final String textSecond;
            private final String textThird;
            private final Color color;
            private final Color colorSecond;
            private final Color colorThird;
            
            public ColoredString(final String p1, final Color c1, final String p2, final Color c2, final String p3, final Color c3) {
                this.text = p1;
                this.color = c1;
                this.textSecond = p2;
                this.colorSecond = c2;
                this.textThird = p3;
                this.colorThird = c3;
            }
            
            public ColoredString(final String text, final Color color, final String textSecond, final Color colorSecond) {
                this(text, color, textSecond, colorSecond, null, null);
            }
            
            public ColoredString(final String text, final Color color) {
                this(text, color, null, null, null, null);
            }
            
            public ColoredString(final String text) {
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
