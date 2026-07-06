// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.render;

import lombok.Generated;
import sweetie.leonware.api.utils.render.RenderUtil;
import net.minecraft.class_4587;
import net.minecraft.class_3532;
import net.minecraft.class_243;
import sweetie.leonware.api.utils.math.ProjectionUtil;
import net.minecraft.class_1799;
import org.joml.Vector2f;
import sweetie.leonware.api.utils.render.fonts.Fonts;
import java.util.Iterator;
import java.util.List;
import net.minecraft.class_1297;
import net.minecraft.class_1542;
import java.util.ArrayList;
import net.minecraft.class_332;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.render.Render2DEvent;
import sweetie.leonware.api.module.setting.Setting;
import java.util.function.Supplier;
import java.util.Objects;
import java.awt.Color;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.setting.ColorSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Item ESP", category = Category.RENDER)
public class ItemESPModule extends Module
{
    private static final ItemESPModule instance;
    private final ColorSetting bgColor;
    private final SliderSetting scale;
    private final BooleanSetting grouping;
    private final SliderSetting groupRadius;
    
    public ItemESPModule() {
        this.bgColor = new ColorSetting("\u0424\u043e\u043d").value(new Color(15, 15, 15, 210));
        this.scale = new SliderSetting("\u041c\u0430\u0441\u0448\u0442\u0430\u0431").value(1.0f).range(0.5f, 2.0f).step(0.05f);
        this.grouping = new BooleanSetting("\u041e\u0431\u044a\u0435\u0434\u0438\u043d\u0435\u043d\u0438\u0435").value(true);
        final SliderSetting step = new SliderSetting("\u0420\u0430\u0434\u0438\u0443\u0441 \u0433\u0440\u0443\u043f\u043f\u044b").value(2.5f).range(0.5f, 8.0f).step(0.5f);
        final BooleanSetting grouping = this.grouping;
        Objects.requireNonNull(grouping);
        this.groupRadius = step.setVisible((Supplier<Boolean>)grouping::getValue);
        this.addSettings(this.bgColor, this.scale, this.grouping, this.groupRadius);
    }
    
    @Override
    public void onEvent() {
        final EventListener render2D = Render2DEvent.getInstance().subscribe(new Listener<Render2DEvent.Render2DEventData>(event -> {
            if (ItemESPModule.mc.field_1687 == null || ItemESPModule.mc.field_1724 == null) {
                return;
            }
            else {
                this.render(event.context(), event.partialTicks());
                return;
            }
        }));
        this.addEvents(render2D);
    }
    
    private void render(final class_332 ctx, final float pt) {
        final List<class_1542> items = new ArrayList<class_1542>();
        for (final class_1297 e : ItemESPModule.mc.field_1687.method_18112()) {
            if (e instanceof final class_1542 ie) {
                items.add(ie);
            }
        }
        if (items.isEmpty()) {
            return;
        }
        if (this.grouping.getValue()) {
            this.renderGrouped(ctx, items, pt);
        }
        else {
            this.renderSingle(ctx, items, pt);
        }
    }
    
    private void renderSingle(final class_332 ctx, final List<class_1542> items, final float pt) {
        final float sc = this.scale.getValue();
        final int sz = (int)(16.0f * sc);
        final int pad = (int)(4.0f * sc);
        final int r = (int)(4.0f * sc);
        final float textH = 8.0f * sc;
        final float textPad = 2.0f * sc;
        for (class_1542 item : items) {
            final Vector2f p = this.project(item, pt);
            if (p == null) {
                continue;
            }
            final class_1799 stack = item.method_6983();
            final String text = this.truncate(stack.method_7964().getString(), 10) + ((stack.method_7947() > 1) ? (" x" + stack.method_7947()) : "");
            final float textW = Fonts.PS_MEDIUM.getWidth(text, 5.5f * sc);
            final float totalW = Math.max((float)(sz + pad * 2), textW + pad * 2);
            final float totalH = sz + pad * 2 + textH + textPad;
            final float rx = p.x - totalW / 2.0f;
            final float ry = p.y - totalH / 2.0f;
            this.drawBg(ctx.method_51448(), rx, ry, totalW, totalH, (float)r);
            this.drawItem(ctx, stack, rx + (totalW - sz) / 2.0f, ry + pad, sc);
            Fonts.PS_MEDIUM.drawCenteredText(ctx.method_51448(), text, rx + totalW / 2.0f, ry + pad + sz + textPad, 5.5f * sc, new Color(220, 220, 220, 200));
        }
    }
    
    private void renderGrouped(final class_332 ctx, final List<class_1542> items, final float pt) {
        final List<List<class_1542>> clusters = this.buildClusters(items);
        final float sc = this.scale.getValue();
        final int itemSz = (int)(16.0f * sc);
        final int gap = (int)(3.0f * sc);
        final int pad = (int)(6.0f * sc);
        final int r = (int)(5.0f * sc);
        final int maxCols = 10;
        for (final List<class_1542> cluster : clusters) {
            final class_243 center = this.clusterCenter(cluster, pt);
            final Vector2f p = ProjectionUtil.project(center);
            if (!this.onScreen(p)) {
                continue;
            }
            final List<class_1799> stacks = this.mergeStacks(cluster);
            final int count = stacks.size();
            final int cols = Math.min(count, maxCols);
            final int rows = (int)Math.ceil(count / (double)maxCols);
            final int rectW = pad * 2 + cols * itemSz + (cols - 1) * gap;
            final int rectH = pad * 2 + rows * itemSz + (rows - 1) * gap;
            final float rx = p.x - rectW / 2.0f;
            final float ry = p.y - rectH / 2.0f;
            this.drawBg(ctx.method_51448(), rx, ry, (float)rectW, (float)rectH, (float)r);
            for (int i = 0; i < count; ++i) {
                final int col = i % maxCols;
                final int row = i / maxCols;
                final float ix = rx + pad + col * (itemSz + gap);
                final float iy = ry + pad + row * (itemSz + gap);
                final class_1799 stack = stacks.get(i);
                this.drawItem(ctx, stack, ix, iy, sc);
                if (stack.method_7947() > 1) {
                    final String countStr = String.valueOf(stack.method_7947());
                    final float cSize = 4.5f * sc;
                    final float cW = Fonts.PS_MEDIUM.getWidth(countStr, cSize);
                    Fonts.PS_MEDIUM.drawText(ctx.method_51448(), countStr, ix + itemSz - cW - 1.0f * sc, iy + itemSz - 6.0f * sc, cSize, new Color(255, 255, 255, 230));
                }
            }
        }
    }
    
    private List<List<class_1542>> buildClusters(final List<class_1542> items) {
        final float rad = this.groupRadius.getValue();
        final boolean[] vis = new boolean[items.size()];
        final List<List<class_1542>> out = new ArrayList<List<class_1542>>();
        for (int i = 0; i < items.size(); ++i) {
            if (!vis[i]) {
                final List<class_1542> cl = new ArrayList<class_1542>();
                cl.add(items.get(i));
                vis[i] = true;
                for (int j = i + 1; j < items.size(); ++j) {
                    if (!vis[j] && items.get(i).method_5739((class_1297)items.get(j)) <= rad) {
                        cl.add(items.get(j));
                        vis[j] = true;
                    }
                }
                out.add(cl);
            }
        }
        return out;
    }
    
    private class_243 clusterCenter(final List<class_1542> cl, final float pt) {
        double x = 0.0;
        double y = 0.0;
        double z = 0.0;
        for (final class_1542 e : cl) {
            x += class_3532.method_16436((double)pt, e.field_6014, e.method_23317());
            y += class_3532.method_16436((double)pt, e.field_6036, e.method_23318()) + e.method_17682() * 0.5;
            z += class_3532.method_16436((double)pt, e.field_5969, e.method_23321());
        }
        final int n = cl.size();
        return new class_243(x / n, y / n, z / n);
    }
    
    private List<class_1799> mergeStacks(final List<class_1542> cl) {
        final List<class_1799> out = new ArrayList<class_1799>();
        for (final class_1542 e : cl) {
            final class_1799 s = e.method_6983();
            boolean found = false;
            for (final class_1799 ex : out) {
                if (class_1799.method_7984(ex, s)) {
                    ex.method_7933(s.method_7947());
                    found = true;
                    break;
                }
            }
            if (!found) {
                out.add(s.method_7972());
            }
        }
        return out;
    }
    
    private Vector2f project(final class_1542 e, final float pt) {
        final double x = class_3532.method_16436((double)pt, e.field_6014, e.method_23317());
        final double y = class_3532.method_16436((double)pt, e.field_6036, e.method_23318()) + e.method_17682() * 0.5;
        final double z = class_3532.method_16436((double)pt, e.field_5969, e.method_23321());
        final Vector2f p = ProjectionUtil.project(new class_243(x, y, z));
        return this.onScreen(p) ? p : null;
    }
    
    private boolean onScreen(final Vector2f p) {
        if (p == null) {
            return false;
        }
        final int sw = ItemESPModule.mc.method_22683().method_4486();
        final int sh = ItemESPModule.mc.method_22683().method_4502();
        return p.x > -300.0f && p.x < sw + 300 && p.y > -300.0f && p.y < sh + 300;
    }
    
    private void drawBg(final class_4587 ms, final float x, final float y, final float w, final float h, final float r) {
        RenderUtil.RECT.draw(ms, x, y, w, h, r, this.bgColor.getValue());
    }
    
    private void drawItem(final class_332 ctx, final class_1799 stack, final float x, final float y, final float sc) {
        final class_4587 ms = ctx.method_51448();
        ms.method_22903();
        ms.method_46416(x, y, 0.0f);
        ms.method_22905(sc, sc, 1.0f);
        ctx.method_51427(stack, 0, 0);
        ms.method_22909();
    }
    
    private String truncate(final String s, final int max) {
        if (s == null) {
            return "";
        }
        if (s.length() <= max) {
            return s;
        }
        return s.substring(0, max) + "..";
    }
    
    @Generated
    public static ItemESPModule getInstance() {
        return ItemESPModule.instance;
    }
    
    static {
        instance = new ItemESPModule();
    }
}
