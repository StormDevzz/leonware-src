// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.ui.widget.overlay;

import net.minecraft.class_1657;
import java.awt.Color;
import sweetie.leonware.api.utils.color.ColorUtil;
import java.util.Iterator;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.api.event.events.render.Render2DEvent;
import java.util.ArrayList;
import net.minecraft.class_1799;
import java.util.List;
import sweetie.leonware.client.ui.widget.Widget;

public class ArmorWidget extends Widget
{
    private final List<class_1799> ITEMS;
    
    public ArmorWidget() {
        super(30.0f, 100.0f);
        this.ITEMS = new ArrayList<class_1799>();
    }
    
    @Override
    public String getName() {
        return "Armor";
    }
    
    @Override
    public void render(final Render2DEvent.Render2DEventData event) {
        final class_4587 matrixStack = event.matrixStack();
        final class_332 context = event.context();
        this.updateItems();
        if (this.ITEMS.isEmpty()) {
            return;
        }
        final float x = this.getDraggable().getX();
        final float y = this.getDraggable().getY();
        final float itemSize = this.scaled(13.0f);
        final float gap = this.scaled(3.0f);
        final int screenWidth = ArmorWidget.mc.method_22683().method_4486();
        final float longSide = (itemSize + gap) * 6.0f + gap;
        final float shortSide = itemSize + gap * 2.0f;
        final float threshold = longSide + gap;
        final boolean isVertical = x < threshold || x > screenWidth - threshold;
        float currentWidth;
        float currentHeight;
        if (isVertical) {
            currentWidth = shortSide;
            currentHeight = longSide;
        }
        else {
            currentWidth = longSide;
            currentHeight = shortSide;
        }
        this.updateDraggable(currentHeight, currentWidth);
        RenderUtil.BLUR_RECT.draw(matrixStack, x, y, currentWidth, currentHeight, this.scaled(5.0f), UIColors.widgetBlur());
        float currentX = x + gap;
        float currentY = y + gap;
        final float scaleFactor = itemSize / 16.0f;
        for (final class_1799 item : this.ITEMS) {
            matrixStack.method_22903();
            matrixStack.method_46416(currentX, currentY, 0.0f);
            matrixStack.method_22905(scaleFactor, scaleFactor, 1.0f);
            context.method_51427(item, 0, 0);
            matrixStack.method_22909();
            matrixStack.method_22903();
            matrixStack.method_46416(currentX, currentY, 0.0f);
            final float barHeight = scaleFactor * 2.0f;
            this.drawBar(matrixStack, item, 0.0f, itemSize - barHeight, itemSize, barHeight, barHeight * 0.7f);
            matrixStack.method_22909();
            final float next = itemSize + gap;
            if (isVertical) {
                currentY += next;
            }
            else {
                currentX += next;
            }
        }
    }
    
    private void drawBar(final class_4587 matrixStack, final class_1799 item, final float x, final float y, final float width, final float height, final float offset) {
        if (!item.method_7963()) {
            return;
        }
        final float maxDamage = (float)item.method_7936();
        final float currentDamage = (float)item.method_7919();
        final float progress = (maxDamage - currentDamage) / maxDamage;
        final Color color = ColorUtil.interpolate(UIColors.positiveColor(), UIColors.negativeColor(), progress);
        RenderUtil.RECT.draw(matrixStack, x + offset, y, (width - offset * 2.0f) * progress, height, height * 0.2f, color);
    }
    
    private void updateDraggable(final float height, final float width) {
        this.getDraggable().setHeight(height);
        this.getDraggable().setWidth(width);
    }
    
    private void updateItems() {
        this.ITEMS.clear();
        if (ArmorWidget.mc.field_1724 == null) {
            return;
        }
        final class_1657 player = (class_1657)ArmorWidget.mc.field_1724;
        this.ITEMS.add(player.method_6047());
        this.ITEMS.add(player.method_6079());
        final List<class_1799> armor = (List<class_1799>)player.method_31548().field_7548;
        for (int i = armor.size() - 1; i >= 0; --i) {
            final class_1799 stack = armor.get(i);
            this.ITEMS.add(stack);
        }
    }
    
    @Override
    public void render(final class_4587 matrixStack) {
    }
}
