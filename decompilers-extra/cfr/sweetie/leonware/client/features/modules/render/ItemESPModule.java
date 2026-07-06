/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1297
 *  net.minecraft.class_1542
 *  net.minecraft.class_1799
 *  net.minecraft.class_243
 *  net.minecraft.class_332
 *  net.minecraft.class_3532
 *  net.minecraft.class_4587
 *  org.joml.Vector2f
 */
package sweetie.leonware.client.features.modules.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import net.minecraft.class_1297;
import net.minecraft.class_1542;
import net.minecraft.class_1799;
import net.minecraft.class_243;
import net.minecraft.class_332;
import net.minecraft.class_3532;
import net.minecraft.class_4587;
import org.joml.Vector2f;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.render.Render2DEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ColorSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.utils.math.ProjectionUtil;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.api.utils.render.fonts.Fonts;

@ModuleRegister(name="Item ESP", category=Category.RENDER)
public class ItemESPModule
extends Module {
    private static final ItemESPModule instance = new ItemESPModule();
    private final ColorSetting bgColor = new ColorSetting("\u0424\u043e\u043d").value(new Color(15, 15, 15, 210));
    private final SliderSetting scale = new SliderSetting("\u041c\u0430\u0441\u0448\u0442\u0430\u0431").value(Float.valueOf(1.0f)).range(0.5f, 2.0f).step(0.05f);
    private final BooleanSetting grouping = new BooleanSetting("\u041e\u0431\u044a\u0435\u0434\u0438\u043d\u0435\u043d\u0438\u0435").value(true);
    private final SliderSetting groupRadius = new SliderSetting("\u0420\u0430\u0434\u0438\u0443\u0441 \u0433\u0440\u0443\u043f\u043f\u044b").value(Float.valueOf(2.5f)).range(0.5f, 8.0f).step(0.5f).setVisible(this.grouping::getValue);

    public ItemESPModule() {
        this.addSettings(this.bgColor, this.scale, this.grouping, this.groupRadius);
    }

    @Override
    public void onEvent() {
        EventListener render2D = Render2DEvent.getInstance().subscribe(new Listener<Render2DEvent.Render2DEventData>(event -> {
            if (ItemESPModule.mc.field_1687 == null || ItemESPModule.mc.field_1724 == null) {
                return;
            }
            this.render(event.context(), event.partialTicks());
        }));
        this.addEvents(render2D);
    }

    private void render(class_332 ctx, float pt) {
        ArrayList<class_1542> items = new ArrayList<class_1542>();
        for (class_1297 e : ItemESPModule.mc.field_1687.method_18112()) {
            if (!(e instanceof class_1542)) continue;
            class_1542 ie = (class_1542)e;
            items.add(ie);
        }
        if (items.isEmpty()) {
            return;
        }
        if (((Boolean)this.grouping.getValue()).booleanValue()) {
            this.renderGrouped(ctx, items, pt);
        } else {
            this.renderSingle(ctx, items, pt);
        }
    }

    private void renderSingle(class_332 ctx, List<class_1542> items, float pt) {
        float sc = ((Float)this.scale.getValue()).floatValue();
        int sz = (int)(16.0f * sc);
        int pad = (int)(4.0f * sc);
        int r = (int)(4.0f * sc);
        float textH = 8.0f * sc;
        float textPad = 2.0f * sc;
        for (class_1542 item : items) {
            Vector2f p = this.project(item, pt);
            if (p == null) continue;
            class_1799 stack = item.method_6983();
            String text = this.truncate(stack.method_7964().getString(), 10) + (String)(stack.method_7947() > 1 ? " x" + stack.method_7947() : "");
            float textW = Fonts.PS_MEDIUM.getWidth(text, 5.5f * sc);
            float totalW = Math.max((float)(sz + pad * 2), textW + (float)(pad * 2));
            float totalH = (float)(sz + pad * 2) + textH + textPad;
            float rx = p.x - totalW / 2.0f;
            float ry = p.y - totalH / 2.0f;
            this.drawBg(ctx.method_51448(), rx, ry, totalW, totalH, r);
            this.drawItem(ctx, stack, rx + (totalW - (float)sz) / 2.0f, ry + (float)pad, sc);
            Fonts.PS_MEDIUM.drawCenteredText(ctx.method_51448(), text, rx + totalW / 2.0f, ry + (float)pad + (float)sz + textPad, 5.5f * sc, new Color(220, 220, 220, 200));
        }
    }

    private void renderGrouped(class_332 ctx, List<class_1542> items, float pt) {
        List<List<class_1542>> clusters = this.buildClusters(items);
        float sc = ((Float)this.scale.getValue()).floatValue();
        int itemSz = (int)(16.0f * sc);
        int gap = (int)(3.0f * sc);
        int pad = (int)(6.0f * sc);
        int r = (int)(5.0f * sc);
        int maxCols = 10;
        for (List<class_1542> cluster : clusters) {
            class_243 center = this.clusterCenter(cluster, pt);
            Vector2f p = ProjectionUtil.project(center);
            if (!this.onScreen(p)) continue;
            List<class_1799> stacks = this.mergeStacks(cluster);
            int count = stacks.size();
            int cols = Math.min(count, maxCols);
            int rows = (int)Math.ceil((double)count / (double)maxCols);
            int rectW = pad * 2 + cols * itemSz + (cols - 1) * gap;
            int rectH = pad * 2 + rows * itemSz + (rows - 1) * gap;
            float rx = p.x - (float)rectW / 2.0f;
            float ry = p.y - (float)rectH / 2.0f;
            this.drawBg(ctx.method_51448(), rx, ry, rectW, rectH, r);
            for (int i = 0; i < count; ++i) {
                int col = i % maxCols;
                int row = i / maxCols;
                float ix = rx + (float)pad + (float)(col * (itemSz + gap));
                float iy = ry + (float)pad + (float)(row * (itemSz + gap));
                class_1799 stack = stacks.get(i);
                this.drawItem(ctx, stack, ix, iy, sc);
                if (stack.method_7947() <= 1) continue;
                String countStr = String.valueOf(stack.method_7947());
                float cSize = 4.5f * sc;
                float cW = Fonts.PS_MEDIUM.getWidth(countStr, cSize);
                Fonts.PS_MEDIUM.drawText(ctx.method_51448(), countStr, ix + (float)itemSz - cW - 1.0f * sc, iy + (float)itemSz - 6.0f * sc, cSize, new Color(255, 255, 255, 230));
            }
        }
    }

    private List<List<class_1542>> buildClusters(List<class_1542> items) {
        float rad = ((Float)this.groupRadius.getValue()).floatValue();
        boolean[] vis = new boolean[items.size()];
        ArrayList<List<class_1542>> out = new ArrayList<List<class_1542>>();
        for (int i = 0; i < items.size(); ++i) {
            if (vis[i]) continue;
            ArrayList<class_1542> cl = new ArrayList<class_1542>();
            cl.add(items.get(i));
            vis[i] = true;
            for (int j = i + 1; j < items.size(); ++j) {
                if (vis[j] || !(items.get(i).method_5739((class_1297)items.get(j)) <= rad)) continue;
                cl.add(items.get(j));
                vis[j] = true;
            }
            out.add(cl);
        }
        return out;
    }

    private class_243 clusterCenter(List<class_1542> cl, float pt) {
        double x = 0.0;
        double y = 0.0;
        double z = 0.0;
        for (class_1542 e : cl) {
            x += class_3532.method_16436((double)pt, (double)e.field_6014, (double)e.method_23317());
            y += class_3532.method_16436((double)pt, (double)e.field_6036, (double)e.method_23318()) + (double)e.method_17682() * 0.5;
            z += class_3532.method_16436((double)pt, (double)e.field_5969, (double)e.method_23321());
        }
        int n = cl.size();
        return new class_243(x / (double)n, y / (double)n, z / (double)n);
    }

    private List<class_1799> mergeStacks(List<class_1542> cl) {
        ArrayList<class_1799> out = new ArrayList<class_1799>();
        for (class_1542 e : cl) {
            class_1799 s = e.method_6983();
            boolean found = false;
            for (class_1799 ex : out) {
                if (!class_1799.method_7984((class_1799)ex, (class_1799)s)) continue;
                ex.method_7933(s.method_7947());
                found = true;
                break;
            }
            if (found) continue;
            out.add(s.method_7972());
        }
        return out;
    }

    private Vector2f project(class_1542 e, float pt) {
        double z;
        double y;
        double x = class_3532.method_16436((double)pt, (double)e.field_6014, (double)e.method_23317());
        Vector2f p = ProjectionUtil.project(new class_243(x, y = class_3532.method_16436((double)pt, (double)e.field_6036, (double)e.method_23318()) + (double)e.method_17682() * 0.5, z = class_3532.method_16436((double)pt, (double)e.field_5969, (double)e.method_23321())));
        return this.onScreen(p) ? p : null;
    }

    private boolean onScreen(Vector2f p) {
        if (p == null) {
            return false;
        }
        int sw = mc.method_22683().method_4486();
        int sh = mc.method_22683().method_4502();
        return p.x > -300.0f && p.x < (float)(sw + 300) && p.y > -300.0f && p.y < (float)(sh + 300);
    }

    private void drawBg(class_4587 ms, float x, float y, float w, float h, float r) {
        RenderUtil.RECT.draw(ms, x, y, w, h, r, (Color)this.bgColor.getValue());
    }

    private void drawItem(class_332 ctx, class_1799 stack, float x, float y, float sc) {
        class_4587 ms = ctx.method_51448();
        ms.method_22903();
        ms.method_46416(x, y, 0.0f);
        ms.method_22905(sc, sc, 1.0f);
        ctx.method_51427(stack, 0, 0);
        ms.method_22909();
    }

    private String truncate(String s, int max) {
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
        return instance;
    }
}

