/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1799
 *  net.minecraft.class_2371
 *  net.minecraft.class_332
 *  net.minecraft.class_4587
 *  net.minecraft.class_746
 */
package sweetie.leonware.client.ui.widget.overlay;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_1799;
import net.minecraft.class_2371;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import net.minecraft.class_746;
import sweetie.leonware.api.event.events.render.Render2DEvent;
import sweetie.leonware.api.utils.color.ColorUtil;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.client.ui.widget.Widget;

public class ArmorWidget
extends Widget {
    private final List<class_1799> ITEMS = new ArrayList<class_1799>();

    public ArmorWidget() {
        super(30.0f, 100.0f);
    }

    @Override
    public String getName() {
        return "Armor";
    }

    @Override
    public void render(Render2DEvent.Render2DEventData event) {
        float currentHeight;
        float currentWidth;
        boolean isVertical;
        class_4587 matrixStack = event.matrixStack();
        class_332 context = event.context();
        this.updateItems();
        if (this.ITEMS.isEmpty()) {
            return;
        }
        float x = this.getDraggable().getX();
        float y = this.getDraggable().getY();
        float itemSize = this.scaled(13.0f);
        float gap = this.scaled(3.0f);
        int screenWidth = mc.method_22683().method_4486();
        float longSide = (itemSize + gap) * 6.0f + gap;
        float shortSide = itemSize + gap * 2.0f;
        float threshold = longSide + gap;
        boolean bl = isVertical = x < threshold || x > (float)screenWidth - threshold;
        if (isVertical) {
            currentWidth = shortSide;
            currentHeight = longSide;
        } else {
            currentWidth = longSide;
            currentHeight = shortSide;
        }
        this.updateDraggable(currentHeight, currentWidth);
        RenderUtil.BLUR_RECT.draw(matrixStack, x, y, currentWidth, currentHeight, this.scaled(5.0f), UIColors.widgetBlur());
        float currentX = x + gap;
        float currentY = y + gap;
        float scaleFactor = itemSize / 16.0f;
        for (class_1799 item : this.ITEMS) {
            matrixStack.method_22903();
            matrixStack.method_46416(currentX, currentY, 0.0f);
            matrixStack.method_22905(scaleFactor, scaleFactor, 1.0f);
            context.method_51427(item, 0, 0);
            matrixStack.method_22909();
            matrixStack.method_22903();
            matrixStack.method_46416(currentX, currentY, 0.0f);
            float barHeight = scaleFactor * 2.0f;
            this.drawBar(matrixStack, item, 0.0f, itemSize - barHeight, itemSize, barHeight, barHeight * 0.7f);
            matrixStack.method_22909();
            float next = itemSize + gap;
            if (isVertical) {
                currentY += next;
                continue;
            }
            currentX += next;
        }
    }

    private void drawBar(class_4587 matrixStack, class_1799 item, float x, float y, float width, float height, float offset) {
        if (!item.method_7963()) {
            return;
        }
        float maxDamage = item.method_7936();
        float currentDamage = item.method_7919();
        float progress = (maxDamage - currentDamage) / maxDamage;
        Color color = ColorUtil.interpolate(UIColors.positiveColor(), UIColors.negativeColor(), progress);
        RenderUtil.RECT.draw(matrixStack, x + offset, y, (width - offset * 2.0f) * progress, height, height * 0.2f, color);
    }

    private void updateDraggable(float height, float width) {
        this.getDraggable().setHeight(height);
        this.getDraggable().setWidth(width);
    }

    private void updateItems() {
        this.ITEMS.clear();
        if (ArmorWidget.mc.field_1724 == null) {
            return;
        }
        class_746 player = ArmorWidget.mc.field_1724;
        this.ITEMS.add(player.method_6047());
        this.ITEMS.add(player.method_6079());
        class_2371 armor = player.method_31548().field_7548;
        for (int i = armor.size() - 1; i >= 0; --i) {
            class_1799 stack = (class_1799)armor.get(i);
            this.ITEMS.add(stack);
        }
    }

    @Override
    public void render(class_4587 matrixStack) {
    }
}

