package sweetie.leonware.client.features.modules.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/ItemESPModule.class */
@ModuleRegister(name = "Item ESP", category = Category.RENDER)
public class ItemESPModule extends Module {
    private static final ItemESPModule instance = new ItemESPModule();
    private final ColorSetting bgColor = new ColorSetting("Фон").value(new Color(15, 15, 15, 210));
    private final SliderSetting scale = new SliderSetting("Масштаб").value(Float.valueOf(1.0f)).range(0.5f, 2.0f).step(0.05f);
    private final BooleanSetting grouping = new BooleanSetting("Объединение").value((Boolean) true);
    private final SliderSetting groupRadius;

    @Generated
    public static ItemESPModule getInstance() {
        return instance;
    }

    /* JADX WARN: Type inference failed for: r1v12, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    public ItemESPModule() {
        SliderSetting sliderSettingStep = new SliderSetting("Радиус группы").value(Float.valueOf(2.5f)).range(0.5f, 8.0f).step(0.5f);
        BooleanSetting booleanSetting = this.grouping;
        Objects.requireNonNull(booleanSetting);
        this.groupRadius = sliderSettingStep.setVisible(booleanSetting::getValue);
        addSettings(this.bgColor, this.scale, this.grouping, this.groupRadius);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener render2D = Render2DEvent.getInstance().subscribe(new Listener(event -> {
            if (mc.field_1687 == null || mc.field_1724 == null) {
                return;
            }
            render(event.context(), event.partialTicks());
        }));
        addEvents(render2D);
    }

    private void render(class_332 ctx, float pt) {
        List<class_1542> items = new ArrayList<>();
        for (class_1297 e : mc.field_1687.method_18112()) {
            if (e instanceof class_1542) {
                class_1542 ie = (class_1542) e;
                items.add(ie);
            }
        }
        if (items.isEmpty()) {
            return;
        }
        if (this.grouping.getValue().booleanValue()) {
            renderGrouped(ctx, items, pt);
        } else {
            renderSingle(ctx, items, pt);
        }
    }

    private void renderSingle(class_332 ctx, List<class_1542> items, float pt) {
        float sc = this.scale.getValue().floatValue();
        int sz = (int) (16.0f * sc);
        int pad = (int) (4.0f * sc);
        int r = (int) (4.0f * sc);
        float textH = 8.0f * sc;
        float textPad = 2.0f * sc;
        for (class_1542 item : items) {
            Vector2f p = project(item, pt);
            if (p != null) {
                class_1799 stack = item.method_6983();
                String text = truncate(stack.method_7964().getString(), 10) + (stack.method_7947() > 1 ? " x" + stack.method_7947() : "");
                float textW = Fonts.PS_MEDIUM.getWidth(text, 5.5f * sc);
                float totalW = Math.max(sz + (pad * 2), textW + (pad * 2));
                float totalH = sz + (pad * 2) + textH + textPad;
                float rx = p.x - (totalW / 2.0f);
                float ry = p.y - (totalH / 2.0f);
                drawBg(ctx.method_51448(), rx, ry, totalW, totalH, r);
                drawItem(ctx, stack, rx + ((totalW - sz) / 2.0f), ry + pad, sc);
                Fonts.PS_MEDIUM.drawCenteredText(ctx.method_51448(), text, rx + (totalW / 2.0f), ry + pad + sz + textPad, 5.5f * sc, new Color(220, 220, 220, 200));
            }
        }
    }

    private void renderGrouped(class_332 ctx, List<class_1542> items, float pt) {
        List<List<class_1542>> clusters = buildClusters(items);
        float sc = this.scale.getValue().floatValue();
        int itemSz = (int) (16.0f * sc);
        int gap = (int) (3.0f * sc);
        int pad = (int) (6.0f * sc);
        int r = (int) (5.0f * sc);
        for (List<class_1542> cluster : clusters) {
            class_243 center = clusterCenter(cluster, pt);
            Vector2f p = ProjectionUtil.project(center);
            if (onScreen(p)) {
                List<class_1799> stacks = mergeStacks(cluster);
                int count = stacks.size();
                int cols = Math.min(count, 10);
                int rows = (int) Math.ceil(((double) count) / ((double) 10));
                int rectW = (pad * 2) + (cols * itemSz) + ((cols - 1) * gap);
                int rectH = (pad * 2) + (rows * itemSz) + ((rows - 1) * gap);
                float rx = p.x - (rectW / 2.0f);
                float ry = p.y - (rectH / 2.0f);
                drawBg(ctx.method_51448(), rx, ry, rectW, rectH, r);
                for (int i = 0; i < count; i++) {
                    int col = i % 10;
                    int row = i / 10;
                    float ix = rx + pad + (col * (itemSz + gap));
                    float iy = ry + pad + (row * (itemSz + gap));
                    class_1799 stack = stacks.get(i);
                    drawItem(ctx, stack, ix, iy, sc);
                    if (stack.method_7947() > 1) {
                        String countStr = String.valueOf(stack.method_7947());
                        float cSize = 4.5f * sc;
                        float cW = Fonts.PS_MEDIUM.getWidth(countStr, cSize);
                        Fonts.PS_MEDIUM.drawText(ctx.method_51448(), countStr, ((ix + itemSz) - cW) - (1.0f * sc), (iy + itemSz) - (6.0f * sc), cSize, new Color(255, 255, 255, 230));
                    }
                }
            }
        }
    }

    private List<List<class_1542>> buildClusters(List<class_1542> items) {
        float rad = this.groupRadius.getValue().floatValue();
        boolean[] vis = new boolean[items.size()];
        List<List<class_1542>> out = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            if (!vis[i]) {
                List<class_1542> cl = new ArrayList<>();
                cl.add(items.get(i));
                vis[i] = true;
                for (int j = i + 1; j < items.size(); j++) {
                    if (!vis[j] && items.get(i).method_5739(items.get(j)) <= rad) {
                        cl.add(items.get(j));
                        vis[j] = true;
                    }
                }
                out.add(cl);
            }
        }
        return out;
    }

    private class_243 clusterCenter(List<class_1542> cl, float pt) {
        double x = 0.0d;
        double y = 0.0d;
        double z = 0.0d;
        for (class_1542 e : cl) {
            x += class_3532.method_16436(pt, e.field_6014, e.method_23317());
            y += class_3532.method_16436(pt, e.field_6036, e.method_23318()) + (((double) e.method_17682()) * 0.5d);
            z += class_3532.method_16436(pt, e.field_5969, e.method_23321());
        }
        int n = cl.size();
        return new class_243(x / ((double) n), y / ((double) n), z / ((double) n));
    }

    private List<class_1799> mergeStacks(List<class_1542> cl) {
        List<class_1799> out = new ArrayList<>();
        for (class_1542 e : cl) {
            class_1799 s = e.method_6983();
            boolean found = false;
            Iterator<class_1799> it = out.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                class_1799 ex = it.next();
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

    private Vector2f project(class_1542 e, float pt) {
        double x = class_3532.method_16436(pt, e.field_6014, e.method_23317());
        double y = class_3532.method_16436(pt, e.field_6036, e.method_23318()) + (((double) e.method_17682()) * 0.5d);
        double z = class_3532.method_16436(pt, e.field_5969, e.method_23321());
        Vector2f p = ProjectionUtil.project(new class_243(x, y, z));
        if (onScreen(p)) {
            return p;
        }
        return null;
    }

    private boolean onScreen(Vector2f p) {
        if (p == null) {
            return false;
        }
        int sw = mc.method_22683().method_4486();
        int sh = mc.method_22683().method_4502();
        return p.x > -300.0f && p.x < ((float) (sw + 300)) && p.y > -300.0f && p.y < ((float) (sh + 300));
    }

    private void drawBg(class_4587 ms, float x, float y, float w, float h, float r) {
        RenderUtil.RECT.draw(ms, x, y, w, h, r, this.bgColor.getValue());
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
        return s == null ? "" : s.length() <= max ? s : s.substring(0, max) + "..";
    }
}
