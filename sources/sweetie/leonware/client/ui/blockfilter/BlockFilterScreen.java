package sweetie.leonware.client.ui.blockfilter;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.minecraft.class_1799;
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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/ui/blockfilter/BlockFilterScreen.class */
public class BlockFilterScreen extends class_437 {
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
        super(class_2561.method_43470("Block Filter"));
        this.allBlocks = new ArrayList();
        this.searchQuery = "";
        this.scroll = 0.0f;
        for (class_2248 block : class_7923.field_41175) {
            if (block != class_2246.field_10124 && block != class_2246.field_10543 && block != class_2246.field_10243) {
                this.allBlocks.add(block);
            }
        }
    }

    public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
        Color cellBg;
        class_4587 matrices = context.method_51448();
        float centerX = this.field_22789 / 2.0f;
        float centerY = this.field_22790 / 2.0f;
        float panelH = Math.min(this.field_22790 * 0.75f, 500.0f);
        float panelX = centerX - 148.0f;
        float panelY = centerY - (panelH / 2.0f);
        Color blurColor = UIColors.currentTheme().getBlurColor();
        RenderUtil.BLUR_RECT.draw(matrices, panelX, panelY, PANEL_WIDTH, panelH, 12.0f, blurColor);
        Color bgColor = new Color(20, 20, 30, 200);
        RenderUtil.RECT.draw(matrices, panelX, panelY, PANEL_WIDTH, panelH, 12.0f, bgColor);
        float searchX = panelX + PADDING;
        float searchY = panelY + PANEL_TOP_PADDING;
        Color searchBg = new Color(30, 30, 45, 220);
        RenderUtil.RECT.draw(matrices, searchX, searchY, 288.0f, SEARCH_HEIGHT, 6.0f, searchBg);
        String displayText = this.searchQuery.isEmpty() ? "Поиск..." : this.searchQuery;
        Color textColor = this.searchQuery.isEmpty() ? new Color(120, 120, 140) : new Color(220, 220, 240);
        Fonts.PS_REGULAR.drawText(matrices, displayText, searchX + 8.0f, (searchY + 15.0f) - 5.0f, PANEL_TOP_PADDING, textColor);
        float gridStartY = searchY + SEARCH_HEIGHT + PADDING;
        float gridH = (panelH - (gridStartY - panelY)) - PADDING;
        List<class_2248> filtered = getFilteredBlocks();
        Set<class_2248> selected = BlockESPModule.getInstance().getSelectedBlocks();
        context.method_44379((int) panelX, (int) gridStartY, (int) (panelX + PANEL_WIDTH), (int) (gridStartY + gridH));
        for (int i = 0; i < filtered.size(); i++) {
            class_2248 block = filtered.get(i);
            int col = i % COLS;
            int row = i / COLS;
            float cellX = panelX + PADDING + (col * 36.0f);
            float cellY = gridStartY + (row * 36.0f) + this.scroll;
            if (cellY + CELL_SIZE >= gridStartY && cellY <= gridStartY + gridH) {
                boolean isSelected = selected.contains(block);
                if (isSelected) {
                    Color primary = UIColors.currentTheme().getPrimaryColor();
                    cellBg = new Color(primary.getRed(), primary.getGreen(), primary.getBlue(), 120);
                } else {
                    cellBg = new Color(40, 40, 55, 160);
                }
                RenderUtil.RECT.draw(matrices, cellX, cellY, CELL_SIZE, CELL_SIZE, PADDING, cellBg);
                class_1799 stack = new class_1799(block.method_8389());
                if (!stack.method_7960()) {
                    context.method_51427(stack, (int) ((cellX + 16.0f) - 8.0f), (int) ((cellY + 16.0f) - 8.0f));
                }
            }
        }
        context.method_44380();
    }

    public boolean method_25400(char chr, int modifiers) {
        this.searchQuery += chr;
        return true;
    }

    public boolean method_25404(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 259 && !this.searchQuery.isEmpty()) {
            this.searchQuery = this.searchQuery.substring(0, this.searchQuery.length() - 1);
            return true;
        }
        if (keyCode == 256) {
            method_25419();
            return true;
        }
        return super.method_25404(keyCode, scanCode, modifiers);
    }

    public boolean method_25402(double mouseX, double mouseY, int button) {
        float centerX = this.field_22789 / 2.0f;
        float centerY = this.field_22790 / 2.0f;
        float panelH = Math.min(this.field_22790 * 0.75f, 500.0f);
        float panelX = centerX - 148.0f;
        float panelY = centerY - (panelH / 2.0f);
        float gridStartY = panelY + PANEL_TOP_PADDING + SEARCH_HEIGHT + PADDING;
        List<class_2248> filtered = getFilteredBlocks();
        Set<class_2248> selected = BlockESPModule.getInstance().getSelectedBlocks();
        for (int i = 0; i < filtered.size(); i++) {
            class_2248 block = filtered.get(i);
            int col = i % COLS;
            int row = i / COLS;
            float cellX = panelX + PADDING + (col * 36.0f);
            float cellY = gridStartY + (row * 36.0f) + this.scroll;
            if (mouseX >= cellX && mouseX <= cellX + CELL_SIZE && mouseY >= cellY && mouseY <= cellY + CELL_SIZE) {
                if (selected.contains(block)) {
                    selected.remove(block);
                    return true;
                }
                selected.add(block);
                return true;
            }
        }
        return super.method_25402(mouseX, mouseY, button);
    }

    public boolean method_25401(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        this.scroll += (float) (verticalAmount * 20.0d);
        List<class_2248> filtered = getFilteredBlocks();
        int rows = ((filtered.size() + COLS) - 1) / COLS;
        float contentH = rows * 36.0f;
        float panelH = Math.min(this.field_22790 * 0.75f, 500.0f);
        float gridH = ((panelH - PANEL_TOP_PADDING) - SEARCH_HEIGHT) - 8.0f;
        float minScroll = -(contentH - gridH);
        if (this.scroll > 0.0f) {
            this.scroll = 0.0f;
        }
        if (this.scroll < minScroll) {
            this.scroll = Math.min(minScroll, 0.0f);
            return true;
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
        List<class_2248> result = new ArrayList<>();
        for (class_2248 block : this.allBlocks) {
            String name = class_7923.field_41175.method_10221(block).method_12832().replace("_", " ");
            if (name.contains(q)) {
                result.add(block);
            }
        }
        return result;
    }
}
