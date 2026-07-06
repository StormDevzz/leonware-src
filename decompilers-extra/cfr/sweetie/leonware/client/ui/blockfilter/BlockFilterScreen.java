/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1799
 *  net.minecraft.class_1935
 *  net.minecraft.class_2246
 *  net.minecraft.class_2248
 *  net.minecraft.class_2561
 *  net.minecraft.class_332
 *  net.minecraft.class_437
 *  net.minecraft.class_4587
 *  net.minecraft.class_7923
 */
package sweetie.leonware.client.ui.blockfilter;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.minecraft.class_1799;
import net.minecraft.class_1935;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_437;
import net.minecraft.class_4587;
import net.minecraft.class_7923;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.api.utils.render.fonts.Fonts;
import sweetie.leonware.client.features.modules.render.BlockESPModule;

public class BlockFilterScreen
extends class_437 {
    private final List<class_2248> allBlocks = new ArrayList<class_2248>();
    private String searchQuery = "";
    private float scroll = 0.0f;
    private static final int COLS = 8;
    private static final float CELL_SIZE = 32.0f;
    private static final float PADDING = 4.0f;
    private static final float PANEL_WIDTH = 296.0f;
    private static final float SEARCH_HEIGHT = 30.0f;
    private static final float PANEL_TOP_PADDING = 10.0f;

    public BlockFilterScreen() {
        super((class_2561)class_2561.method_43470((String)"Block Filter"));
        for (class_2248 block : class_7923.field_41175) {
            if (block == class_2246.field_10124 || block == class_2246.field_10543 || block == class_2246.field_10243) continue;
            this.allBlocks.add(block);
        }
    }

    public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
        class_4587 matrices = context.method_51448();
        float centerX = (float)this.field_22789 / 2.0f;
        float centerY = (float)this.field_22790 / 2.0f;
        float panelH = Math.min((float)this.field_22790 * 0.75f, 500.0f);
        float panelX = centerX - 148.0f;
        float panelY = centerY - panelH / 2.0f;
        Color blurColor = UIColors.currentTheme().getBlurColor();
        RenderUtil.BLUR_RECT.draw(matrices, panelX, panelY, 296.0f, panelH, 12.0f, blurColor);
        Color bgColor = new Color(20, 20, 30, 200);
        RenderUtil.RECT.draw(matrices, panelX, panelY, 296.0f, panelH, 12.0f, bgColor);
        float searchX = panelX + 4.0f;
        float searchY = panelY + 10.0f;
        Color searchBg = new Color(30, 30, 45, 220);
        RenderUtil.RECT.draw(matrices, searchX, searchY, 288.0f, 30.0f, 6.0f, searchBg);
        String displayText = this.searchQuery.isEmpty() ? "\u041f\u043e\u0438\u0441\u043a..." : this.searchQuery;
        Color textColor = this.searchQuery.isEmpty() ? new Color(120, 120, 140) : new Color(220, 220, 240);
        Fonts.PS_REGULAR.drawText(matrices, displayText, searchX + 8.0f, searchY + 15.0f - 5.0f, 10.0f, textColor);
        float gridStartY = searchY + 30.0f + 4.0f;
        float gridH = panelH - (gridStartY - panelY) - 4.0f;
        List<class_2248> filtered = this.getFilteredBlocks();
        Set<class_2248> selected = BlockESPModule.getInstance().getSelectedBlocks();
        context.method_44379((int)panelX, (int)gridStartY, (int)(panelX + 296.0f), (int)(gridStartY + gridH));
        for (int i = 0; i < filtered.size(); ++i) {
            Color cellBg;
            class_2248 block = filtered.get(i);
            int col = i % 8;
            int row = i / 8;
            float cellX = panelX + 4.0f + (float)col * 36.0f;
            float cellY = gridStartY + (float)row * 36.0f + this.scroll;
            if (cellY + 32.0f < gridStartY || cellY > gridStartY + gridH) continue;
            boolean isSelected = selected.contains(block);
            if (isSelected) {
                Color primary = UIColors.currentTheme().getPrimaryColor();
                cellBg = new Color(primary.getRed(), primary.getGreen(), primary.getBlue(), 120);
            } else {
                cellBg = new Color(40, 40, 55, 160);
            }
            RenderUtil.RECT.draw(matrices, cellX, cellY, 32.0f, 32.0f, 4.0f, cellBg);
            class_1799 stack = new class_1799((class_1935)block.method_8389());
            if (stack.method_7960()) continue;
            context.method_51427(stack, (int)(cellX + 16.0f - 8.0f), (int)(cellY + 16.0f - 8.0f));
        }
        context.method_44380();
    }

    public boolean method_25400(char chr, int modifiers) {
        this.searchQuery = this.searchQuery + chr;
        return true;
    }

    public boolean method_25404(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 259 && !this.searchQuery.isEmpty()) {
            this.searchQuery = this.searchQuery.substring(0, this.searchQuery.length() - 1);
            return true;
        }
        if (keyCode == 256) {
            this.method_25419();
            return true;
        }
        return super.method_25404(keyCode, scanCode, modifiers);
    }

    public boolean method_25402(double mouseX, double mouseY, int button) {
        float centerX = (float)this.field_22789 / 2.0f;
        float centerY = (float)this.field_22790 / 2.0f;
        float panelH = Math.min((float)this.field_22790 * 0.75f, 500.0f);
        float panelX = centerX - 148.0f;
        float panelY = centerY - panelH / 2.0f;
        float gridStartY = panelY + 10.0f + 30.0f + 4.0f;
        List<class_2248> filtered = this.getFilteredBlocks();
        Set<class_2248> selected = BlockESPModule.getInstance().getSelectedBlocks();
        for (int i = 0; i < filtered.size(); ++i) {
            class_2248 block = filtered.get(i);
            int col = i % 8;
            int row = i / 8;
            float cellX = panelX + 4.0f + (float)col * 36.0f;
            float cellY = gridStartY + (float)row * 36.0f + this.scroll;
            if (!(mouseX >= (double)cellX) || !(mouseX <= (double)(cellX + 32.0f)) || !(mouseY >= (double)cellY) || !(mouseY <= (double)(cellY + 32.0f))) continue;
            if (selected.contains(block)) {
                selected.remove(block);
            } else {
                selected.add(block);
            }
            return true;
        }
        return super.method_25402(mouseX, mouseY, button);
    }

    public boolean method_25401(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        this.scroll += (float)(verticalAmount * 20.0);
        List<class_2248> filtered = this.getFilteredBlocks();
        int rows = (filtered.size() + 8 - 1) / 8;
        float maxScroll = 0.0f;
        float contentH = (float)rows * 36.0f;
        float panelH = Math.min((float)this.field_22790 * 0.75f, 500.0f);
        float gridH = panelH - 10.0f - 30.0f - 8.0f;
        float minScroll = -(contentH - gridH);
        if (this.scroll > maxScroll) {
            this.scroll = maxScroll;
        }
        if (this.scroll < minScroll) {
            this.scroll = Math.min(minScroll, 0.0f);
        }
        return true;
    }

    public boolean method_25421() {
        return false;
    }

    private List<class_2248> getFilteredBlocks() {
        if (this.searchQuery.isEmpty()) {
            return this.allBlocks;
        }
        String q = this.searchQuery.toLowerCase();
        ArrayList<class_2248> result = new ArrayList<class_2248>();
        for (class_2248 block : this.allBlocks) {
            String name = class_7923.field_41175.method_10221((Object)block).method_12832().replace("_", " ");
            if (!name.contains(q)) continue;
            result.add(block);
        }
        return result;
    }
}

