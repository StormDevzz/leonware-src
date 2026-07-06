// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.ui.blockfilter;

import java.util.Set;
import net.minecraft.class_4587;
import net.minecraft.class_1935;
import net.minecraft.class_1799;
import sweetie.leonware.client.features.modules.render.BlockESPModule;
import sweetie.leonware.api.utils.render.fonts.Fonts;
import java.awt.Color;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.api.utils.color.UIColors;
import net.minecraft.class_332;
import java.util.Iterator;
import net.minecraft.class_2246;
import net.minecraft.class_7923;
import java.util.ArrayList;
import net.minecraft.class_2561;
import net.minecraft.class_2248;
import java.util.List;
import net.minecraft.class_437;

public class BlockFilterScreen extends class_437
{
    private final List<class_2248> allBlocks;
    private String searchQuery;
    private float scroll;
    private static final int COLS = 8;
    private static final float CELL_SIZE = 32.0f;
    private static final float PADDING = 4.0f;
    private static final float PANEL_WIDTH = 296.0f;
    private static final float SEARCH_HEIGHT = 30.0f;
    private static final float PANEL_TOP_PADDING = 10.0f;
    
    public BlockFilterScreen() {
        super((class_2561)class_2561.method_43470("Block Filter"));
        this.allBlocks = new ArrayList<class_2248>();
        this.searchQuery = "";
        this.scroll = 0.0f;
        for (final class_2248 block : class_7923.field_41175) {
            if (block != class_2246.field_10124 && block != class_2246.field_10543) {
                if (block == class_2246.field_10243) {
                    continue;
                }
                this.allBlocks.add(block);
            }
        }
    }
    
    public void method_25394(final class_332 context, final int mouseX, final int mouseY, final float delta) {
        final class_4587 matrices = context.method_51448();
        final float centerX = this.field_22789 / 2.0f;
        final float centerY = this.field_22790 / 2.0f;
        final float panelH = Math.min(this.field_22790 * 0.75f, 500.0f);
        final float panelX = centerX - 148.0f;
        final float panelY = centerY - panelH / 2.0f;
        final Color blurColor = UIColors.currentTheme().getBlurColor();
        RenderUtil.BLUR_RECT.draw(matrices, panelX, panelY, 296.0f, panelH, 12.0f, blurColor);
        final Color bgColor = new Color(20, 20, 30, 200);
        RenderUtil.RECT.draw(matrices, panelX, panelY, 296.0f, panelH, 12.0f, bgColor);
        final float searchX = panelX + 4.0f;
        final float searchY = panelY + 10.0f;
        final Color searchBg = new Color(30, 30, 45, 220);
        RenderUtil.RECT.draw(matrices, searchX, searchY, 288.0f, 30.0f, 6.0f, searchBg);
        final String displayText = this.searchQuery.isEmpty() ? "\u041f\u043e\u0438\u0441\u043a..." : this.searchQuery;
        final Color textColor = this.searchQuery.isEmpty() ? new Color(120, 120, 140) : new Color(220, 220, 240);
        Fonts.PS_REGULAR.drawText(matrices, displayText, searchX + 8.0f, searchY + 15.0f - 5.0f, 10.0f, textColor);
        final float gridStartY = searchY + 30.0f + 4.0f;
        final float gridH = panelH - (gridStartY - panelY) - 4.0f;
        final List<class_2248> filtered = this.getFilteredBlocks();
        final Set<class_2248> selected = BlockESPModule.getInstance().getSelectedBlocks();
        context.method_44379((int)panelX, (int)gridStartY, (int)(panelX + 296.0f), (int)(gridStartY + gridH));
        for (int i = 0; i < filtered.size(); ++i) {
            final class_2248 block = filtered.get(i);
            final int col = i % 8;
            final int row = i / 8;
            final float cellX = panelX + 4.0f + col * 36.0f;
            final float cellY = gridStartY + row * 36.0f + this.scroll;
            if (cellY + 32.0f >= gridStartY) {
                if (cellY <= gridStartY + gridH) {
                    final boolean isSelected = selected.contains(block);
                    Color cellBg;
                    if (isSelected) {
                        final Color primary = UIColors.currentTheme().getPrimaryColor();
                        cellBg = new Color(primary.getRed(), primary.getGreen(), primary.getBlue(), 120);
                    }
                    else {
                        cellBg = new Color(40, 40, 55, 160);
                    }
                    RenderUtil.RECT.draw(matrices, cellX, cellY, 32.0f, 32.0f, 4.0f, cellBg);
                    final class_1799 stack = new class_1799((class_1935)block.method_8389());
                    if (!stack.method_7960()) {
                        context.method_51427(stack, (int)(cellX + 16.0f - 8.0f), (int)(cellY + 16.0f - 8.0f));
                    }
                }
            }
        }
        context.method_44380();
    }
    
    public boolean method_25400(final char chr, final int modifiers) {
        this.searchQuery += chr;
        return true;
    }
    
    public boolean method_25404(final int keyCode, final int scanCode, final int modifiers) {
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
    
    public boolean method_25402(final double mouseX, final double mouseY, final int button) {
        final float centerX = this.field_22789 / 2.0f;
        final float centerY = this.field_22790 / 2.0f;
        final float panelH = Math.min(this.field_22790 * 0.75f, 500.0f);
        final float panelX = centerX - 148.0f;
        final float panelY = centerY - panelH / 2.0f;
        final float gridStartY = panelY + 10.0f + 30.0f + 4.0f;
        final List<class_2248> filtered = this.getFilteredBlocks();
        final Set<class_2248> selected = BlockESPModule.getInstance().getSelectedBlocks();
        for (int i = 0; i < filtered.size(); ++i) {
            final class_2248 block = filtered.get(i);
            final int col = i % 8;
            final int row = i / 8;
            final float cellX = panelX + 4.0f + col * 36.0f;
            final float cellY = gridStartY + row * 36.0f + this.scroll;
            if (mouseX >= cellX && mouseX <= cellX + 32.0f && mouseY >= cellY && mouseY <= cellY + 32.0f) {
                if (selected.contains(block)) {
                    selected.remove(block);
                }
                else {
                    selected.add(block);
                }
                return true;
            }
        }
        return super.method_25402(mouseX, mouseY, button);
    }
    
    public boolean method_25401(final double mouseX, final double mouseY, final double horizontalAmount, final double verticalAmount) {
        this.scroll += (float)(verticalAmount * 20.0);
        final List<class_2248> filtered = this.getFilteredBlocks();
        final int rows = (filtered.size() + 8 - 1) / 8;
        final float maxScroll = 0.0f;
        final float contentH = rows * 36.0f;
        final float panelH = Math.min(this.field_22790 * 0.75f, 500.0f);
        final float gridH = panelH - 10.0f - 30.0f - 8.0f;
        final float minScroll = -(contentH - gridH);
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
        final String q = this.searchQuery.toLowerCase();
        final List<class_2248> result = new ArrayList<class_2248>();
        for (final class_2248 block : this.allBlocks) {
            final String name = class_7923.field_41175.method_10221((Object)block).method_12832().replace("_", " ");
            if (name.contains(q)) {
                result.add(block);
            }
        }
        return result;
    }
}
