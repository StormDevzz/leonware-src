/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_2561
 *  net.minecraft.class_2960
 *  net.minecraft.class_332
 *  net.minecraft.class_437
 */
package sweetie.leonware.client.ui.clickgui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.awt.Color;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_332;
import net.minecraft.class_437;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.system.backend.ClientInfo;
import sweetie.leonware.api.system.files.FileUtil;
import sweetie.leonware.api.system.interfaces.QuickImports;
import sweetie.leonware.api.utils.animation.AnimationUtil;
import sweetie.leonware.api.utils.animation.Easing;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.math.MouseUtil;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.api.utils.render.fonts.Fonts;
import sweetie.leonware.client.features.modules.render.ClickGUIModule;
import sweetie.leonware.client.services.RenderService;
import sweetie.leonware.client.ui.UIComponent;
import sweetie.leonware.client.ui.clickgui.Panel;
import sweetie.leonware.client.ui.theme.ThemeEditor;

public class ScreenClickGUI
extends class_437
implements QuickImports {
    private static final ScreenClickGUI instance = new ScreenClickGUI();
    private float scroll;
    private final AnimationUtil scrollAnimation = new AnimationUtil();
    private boolean open;
    private final AnimationUtil openAnimation = new AnimationUtil();
    private final List<Panel> panels = new ArrayList<Panel>();
    private final ThemeEditor themeEditor = ThemeEditor.getInstance();
    private final class_2960 leonTexture = FileUtil.getImage("leon/leon");
    private final class_2960 leon67Texture = FileUtil.getImage("leon/leon67");
    private final class_2960[] girlTextures = new class_2960[]{FileUtil.getImage("leon/girl1"), FileUtil.getImage("leon/girl2"), FileUtil.getImage("leon/girl3"), FileUtil.getImage("leon/girl5"), FileUtil.getImage("leon/girl7"), FileUtil.getImage("leon/girl8"), FileUtil.getImage("leon/girl9"), FileUtil.getImage("leon/girl10"), FileUtil.getImage("leon/girl11"), FileUtil.getImage("leon/girl12"), FileUtil.getImage("leon/girl13"), FileUtil.getImage("leon/girl16"), FileUtil.getImage("leon/girl17"), FileUtil.getImage("leon/girl18"), FileUtil.getImage("leon/girl19"), FileUtil.getImage("leon/girl20"), FileUtil.getImage("leon/gir18")};
    private String searchQuery = "";
    private boolean searchTyping = false;
    private final AnimationUtil searchAnimation = new AnimationUtil();
    private int dragIndex = -1;
    private long dragPressTime = 0L;
    private boolean panelDragging = false;
    private double dragMouseX = 0.0;
    private double dragStartX = 0.0;
    private final AnimationUtil dragAlphaAnimation = new AnimationUtil();
    private static final long DRAG_HOLD_MS = 400L;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final float[] panelAnimatedX = new float[20];

    public ScreenClickGUI() {
        super(class_2561.method_30163((String)""));
        List<String> savedOrder = this.loadPanelOrder();
        if (savedOrder != null && savedOrder.size() == Category.values().length) {
            block0: for (String label : savedOrder) {
                for (Category cat : Category.values()) {
                    if (!cat.getLabel().equals(label)) continue;
                    Panel panel = new Panel(cat);
                    panel.setCategoryIndex(this.panels.size() * 45);
                    this.panels.add(panel);
                    continue block0;
                }
            }
        }
        if (this.panels.size() != Category.values().length) {
            this.panels.clear();
            for (int i = 0; i < Category.values().length; ++i) {
                Category category = Category.values()[i];
                Panel panel = new Panel(category);
                panel.setCategoryIndex(i * 45);
                this.panels.add(panel);
            }
        }
    }

    public void method_25419() {
        ThemeEditor.getInstance().save(false);
        this.open = false;
        this.savePanelOrder();
        super.method_25419();
    }

    protected void method_25426() {
        ThemeEditor.getInstance().init();
        this.open = true;
        super.method_25426();
    }

    public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
        super.method_25394(context, mouseX, mouseY, delta);
        this.scrollAnimation.update();
        this.scrollAnimation.run(this.scroll, 600L, Easing.EXPO_OUT);
        this.openAnimation.update();
        this.openAnimation.run(this.open ? 1.0 : 0.0, 500L, Easing.EXPO_OUT);
        float openAnim = (float)this.openAnimation.getValue();
        if (!this.open && (double)openAnim < 0.1) {
            this.method_25419();
        }
        this.dragAlphaAnimation.update();
        this.dragAlphaAnimation.run(this.panelDragging ? 1.0 : 0.0, 200L, Easing.EXPO_OUT);
        this.updateDragState(mouseX);
        float windowHeight = mc.method_22683().method_4502();
        float windowWidth = mc.method_22683().method_4486();
        float off = RenderService.getInstance().scaled(12.0f);
        float totalWidth = this.panels.stream().map(UIComponent::getWidth).reduce(Float.valueOf(0.0f), Float::sum).floatValue() + (float)(this.panels.size() - 1) * (off / 2.0f);
        float sex = !this.open ? 1.0f - openAnim : -1.0f + openAnim;
        float panelY = (float)((double)(windowHeight / 12.0f) + this.scrollAnimation.getValue()) * openAnim + windowHeight * sex;
        float firstX = (windowWidth - totalWidth) / 2.0f;
        if (this.themeEditor.isOpen()) {
            this.themeEditor.setAnim(openAnim);
            if (!this.themeEditor.isUserMoved()) {
                this.themeEditor.setX(windowWidth - this.themeEditor.getWidth() * this.themeEditor.getAnim());
                this.themeEditor.setY(windowHeight / 2.0f - this.themeEditor.getHeight() / 2.0f);
            }
        }
        for (int i = 0; i < this.panels.size(); ++i) {
            Panel panel = this.panels.get(i);
            float targetX = firstX + (float)i * (panel.getWidth() + off / 2.0f);
            if (this.panelDragging && i == this.dragIndex) {
                float currentX = (float)(this.dragStartX + ((double)mouseX - this.dragMouseX));
                panel.setAlpha(openAnim * 0.75f);
                panel.setY(panelY);
                panel.setX(currentX);
                this.panelAnimatedX[i] = currentX;
                int hoverIdx = this.getHoverIndex(mouseX, firstX, off);
                if (hoverIdx >= 0 && hoverIdx < this.panels.size() && hoverIdx != this.dragIndex) {
                    float swappedCurrentX = this.panelAnimatedX[hoverIdx];
                    Panel moved = this.panels.remove(this.dragIndex);
                    this.panels.add(hoverIdx, moved);
                    float oldDragTarget = firstX + (float)this.dragIndex * (panel.getWidth() + off / 2.0f);
                    this.panelAnimatedX[this.dragIndex] = swappedCurrentX != 0.0f ? swappedCurrentX : oldDragTarget;
                    this.dragIndex = hoverIdx;
                    this.dragStartX = firstX + (float)hoverIdx * (panel.getWidth() + off / 2.0f);
                    this.dragMouseX = mouseX;
                    this.panelAnimatedX[hoverIdx] = currentX;
                }
            } else {
                float animSpeed = 0.22f;
                if (this.panelAnimatedX[i] == 0.0f) {
                    this.panelAnimatedX[i] = targetX;
                }
                this.panelAnimatedX[i] = this.panelAnimatedX[i] + (targetX - this.panelAnimatedX[i]) * animSpeed;
                if (Math.abs(this.panelAnimatedX[i] - targetX) < 0.5f) {
                    this.panelAnimatedX[i] = targetX;
                }
                panel.setAlpha(openAnim);
                panel.setY(panelY);
                panel.setX(this.panelAnimatedX[i]);
            }
            panel.render(context, mouseX, mouseY, delta);
        }
        this.themeEditor.render(context, mouseX, mouseY, delta);
        this.renderImage(context, openAnim);
        this.renderSearch(context, mouseX, mouseY, openAnim);
    }

    private void renderImage(class_332 context, float openAnim) {
        if (openAnim <= 0.01f) {
            return;
        }
        float windowWidth = mc.method_22683().method_4486();
        float windowHeight = mc.method_22683().method_4502();
        ClickGUIModule clickGUIModule = ClickGUIModule.getInstance();
        float imgSize = ((Float)clickGUIModule.imageSizeSlider.getValue()).floatValue() * openAnim;
        float originalW = 423.0f;
        float originalH = 664.0f;
        float aspect = originalW / originalH;
        float imgW = imgSize * aspect;
        float imgH = imgSize;
        int imgAlpha = (int)(openAnim * 255.0f);
        class_2960 texture = null;
        float widthMultiplier = 1.0f;
        if (clickGUIModule.imageSetting.is("\u041b\u0435\u043e\u043d")) {
            texture = this.leonTexture;
        } else if (clickGUIModule.imageSetting.is("\u0428\u043b\u044e\u0445\u0430")) {
            texture = this.leon67Texture;
            widthMultiplier = 1.35f;
        } else {
            for (int i = 0; i < this.girlTextures.length; ++i) {
                if (!clickGUIModule.imageSetting.is("\u0414\u0435\u0432\u043e\u0447\u043a\u0430" + (i + 1))) continue;
                texture = this.girlTextures[i];
                widthMultiplier = 1.25f;
                break;
            }
        }
        if (texture != null) {
            float imgX = ((Float)clickGUIModule.imageXSlider.getValue()).floatValue() * (windowWidth - (imgW *= widthMultiplier));
            float imgY = ((Float)clickGUIModule.imageYSlider.getValue()).floatValue() * (windowHeight - imgH);
            RenderUtil.TEXTURE_RECT.drawTexture(context.method_51448(), texture, imgX, imgY, imgW, imgH, new Color(255, 255, 255, imgAlpha));
        }
    }

    private void updateDragState(int mouseX) {
        long held;
        if (this.dragIndex == -1) {
            return;
        }
        if (!this.panelDragging && (held = System.currentTimeMillis() - this.dragPressTime) >= 400L) {
            this.panelDragging = true;
            this.dragMouseX = mouseX;
            float off = RenderService.getInstance().scaled(12.0f);
            float windowWidth = mc.method_22683().method_4486();
            float totalWidth = this.panels.stream().map(UIComponent::getWidth).reduce(Float.valueOf(0.0f), Float::sum).floatValue() + (float)(this.panels.size() - 1) * (off / 2.0f);
            float firstX = (windowWidth - totalWidth) / 2.0f;
            Panel panel = this.panels.get(this.dragIndex);
            this.dragStartX = firstX + (float)this.dragIndex * (panel.getWidth() + off / 2.0f);
        }
    }

    private int getHoverIndex(double mouseX, float firstX, float off) {
        if (this.panels.isEmpty()) {
            return -1;
        }
        float panelW = this.panels.get(0).getWidth();
        float step = panelW + off / 2.0f;
        for (int i = 0; i < this.panels.size(); ++i) {
            float slotStart = firstX + (float)i * step;
            float slotEnd = slotStart + panelW;
            if (!(mouseX >= (double)slotStart) || !(mouseX <= (double)slotEnd)) continue;
            return i;
        }
        return -1;
    }

    private int getInsertIndex(double mouseX, float firstX, float off) {
        if (this.panels.isEmpty()) {
            return 0;
        }
        float panelW = this.panels.get(0).getWidth();
        float step = panelW + off / 2.0f;
        for (int i = 0; i <= this.panels.size(); ++i) {
            float slotCenter = firstX + (float)i * step;
            if (!(mouseX < (double)slotCenter)) continue;
            return i;
        }
        return this.panels.size();
    }

    private void renderSearch(class_332 context, int mouseX, int mouseY, float openAnim) {
        if (openAnim < 0.05f) {
            return;
        }
        this.searchAnimation.update();
        this.searchAnimation.run(this.searchTyping ? 1.0 : 0.0, 250L, Easing.EXPO_OUT);
        float windowWidth = mc.method_22683().method_4486();
        float windowHeight = mc.method_22683().method_4502();
        int fullAlpha = (int)(openAnim * 255.0f);
        float baseW = RenderService.getInstance().scaled(80.0f);
        String dispText = this.searchTyping ? (this.searchQuery.isEmpty() ? (System.currentTimeMillis() % 800L > 400L ? "|" : "") : this.searchQuery + (System.currentTimeMillis() % 800L > 400L ? "|" : "")) : (this.searchQuery.isEmpty() ? "\u041f\u043e\u0438\u0441\u043a" : this.searchQuery);
        float fontSize = RenderService.getInstance().scaled(6.0f);
        float textWidth = Fonts.PS_MEDIUM.getWidth(dispText.isEmpty() ? "\u041f\u043e\u0438\u0441\u043a" : dispText, fontSize);
        float expandedW = Math.max(baseW, textWidth + RenderService.getInstance().scaled(16.0f));
        float searchAnimVal = (float)this.searchAnimation.getValue();
        float rectW = baseW + (expandedW - baseW) * searchAnimVal;
        float rectH = RenderService.getInstance().scaled(14.0f);
        float rectX = windowWidth / 2.0f - rectW / 2.0f;
        float rectY = windowHeight - rectH - RenderService.getInstance().scaled(10.0f);
        float rectRound = rectH * 0.35f;
        Color searchBg = UIColors.widgetBlur(fullAlpha);
        RenderUtil.BLUR_RECT.draw(context.method_51448(), rectX, rectY, rectW, rectH, rectRound, searchBg);
        float textX = rectX + rectW / 2.0f - Fonts.PS_MEDIUM.getWidth(dispText, fontSize) / 2.0f;
        float textY = rectY + rectH / 2.0f - fontSize / 2.0f;
        Color textColor = UIColors.textColor(fullAlpha);
        Fonts.PS_MEDIUM.drawText(context.method_51448(), dispText, textX, textY, fontSize, textColor);
        if (!this.searchQuery.isEmpty()) {
            float btnSize = rectH * 0.7f;
            float btnX = rectX + rectW + RenderService.getInstance().scaled(3.0f);
            float btnY = rectY + rectH / 2.0f - btnSize / 2.0f;
            float btnRound = btnSize / 2.0f;
            RenderUtil.BLUR_RECT.draw(context.method_51448(), btnX, btnY, btnSize, btnSize, btnRound, UIColors.blur(fullAlpha));
            float xFont = btnSize * 0.5f;
            Fonts.PS_MEDIUM.drawCenteredText(context.method_51448(), "x", btnX + btnSize / 2.0f, btnY + btnSize / 2.0f - xFont / 2.0f, xFont, UIColors.inactiveTextColor(fullAlpha));
        }
    }

    private float[] getSearchResetBtnBounds() {
        float btnX;
        float windowWidth = mc.method_22683().method_4486();
        float windowHeight = mc.method_22683().method_4502();
        float rectH = RenderService.getInstance().scaled(14.0f);
        float baseW = RenderService.getInstance().scaled(80.0f);
        float btnSize = rectH * 0.7f;
        if (!this.searchQuery.isEmpty()) {
            float fontSize = RenderService.getInstance().scaled(6.0f);
            float textWidth = Fonts.PS_MEDIUM.getWidth(this.searchQuery, fontSize);
            float expandedW = Math.max(baseW, textWidth + RenderService.getInstance().scaled(16.0f));
            float rectX = windowWidth / 2.0f - expandedW / 2.0f;
            btnX = rectX + expandedW + RenderService.getInstance().scaled(3.0f);
        } else {
            btnX = windowWidth / 2.0f - baseW / 2.0f + baseW + RenderService.getInstance().scaled(3.0f);
        }
        float btnY = windowHeight - rectH - RenderService.getInstance().scaled(10.0f) + rectH / 2.0f - btnSize / 2.0f;
        return new float[]{btnX, btnY, btnSize, btnSize};
    }

    private float[] getSearchRectBounds() {
        float windowWidth = mc.method_22683().method_4486();
        float windowHeight = mc.method_22683().method_4502();
        float rectH = RenderService.getInstance().scaled(14.0f);
        float baseW = RenderService.getInstance().scaled(80.0f);
        float fontSize = RenderService.getInstance().scaled(6.0f);
        String dispText = this.searchQuery.isEmpty() ? "\u041f\u043e\u0438\u0441\u043a" : this.searchQuery;
        float textWidth = Fonts.PS_MEDIUM.getWidth(dispText, fontSize);
        float expandedW = Math.max(baseW, textWidth + RenderService.getInstance().scaled(16.0f));
        float rectX = windowWidth / 2.0f - expandedW / 2.0f;
        float rectY = windowHeight - rectH - RenderService.getInstance().scaled(10.0f);
        return new float[]{rectX, rectY, expandedW, rectH};
    }

    public void clearSearch() {
        this.searchQuery = "";
        this.searchTyping = false;
        this.applySearchFilter();
    }

    private void applySearchFilter() {
        this.panels.forEach(p -> p.setSearchFilter(this.searchQuery));
    }

    public boolean method_25404(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256) {
            boolean isBinding = false;
            for (Panel p : this.panels) {
                if (!p.isBindingActive()) continue;
                isBinding = true;
            }
            if (isBinding) {
                this.panels.forEach(panel -> panel.keyPressed(keyCode, scanCode, modifiers));
                return true;
            }
            if (this.searchTyping) {
                this.clearSearch();
                return true;
            }
            this.open = false;
            ScreenClickGUI.mc.field_1729.method_1612();
            return true;
        }
        if (this.searchTyping && keyCode == 259) {
            if (!this.searchQuery.isEmpty()) {
                this.searchQuery = this.searchQuery.substring(0, this.searchQuery.length() - 1);
                this.applySearchFilter();
            }
            return true;
        }
        this.panels.forEach(panel -> panel.keyPressed(keyCode, scanCode, modifiers));
        this.themeEditor.keyPressed(keyCode, scanCode, modifiers);
        return super.method_25404(keyCode, scanCode, modifiers);
    }

    public boolean method_25402(double mouseX, double mouseY, int button) {
        float[] btn;
        if (this.panelDragging) {
            return true;
        }
        if (!this.searchQuery.isEmpty() && MouseUtil.isHovered(mouseX, mouseY, (double)(btn = this.getSearchResetBtnBounds())[0], (double)btn[1], (double)btn[2], (double)btn[3])) {
            this.clearSearch();
            return true;
        }
        float[] rect = this.getSearchRectBounds();
        if (MouseUtil.isHovered(mouseX, mouseY, (double)rect[0], (double)rect[1], (double)rect[2], (double)rect[3])) {
            this.searchTyping = true;
            return true;
        }
        this.searchTyping = false;
        if (button == 0) {
            for (int i = 0; i < this.panels.size(); ++i) {
                Panel panel2 = this.panels.get(i);
                if (!MouseUtil.isHovered(mouseX, mouseY, (double)panel2.getX(), (double)panel2.getY(), (double)panel2.getWidth(), (double)panel2.getHeaderHeight())) continue;
                this.dragIndex = i;
                this.dragPressTime = System.currentTimeMillis();
                this.panelDragging = false;
                this.dragMouseX = mouseX;
                break;
            }
        }
        this.panels.forEach(panel -> panel.mouseClicked(mouseX, mouseY, button));
        this.themeEditor.mouseClicked(mouseX, mouseY, button);
        if (!this.searchQuery.isEmpty() && !this.searchTyping) {
            this.clearSearch();
        }
        return super.method_25402(mouseX, mouseY, button);
    }

    public boolean method_25406(double mouseX, double mouseY, int button) {
        if (button == 0 && this.panelDragging && this.dragIndex >= 0) {
            this.savePanelOrder();
            this.dragIndex = -1;
            this.panelDragging = false;
            return true;
        }
        if (button == 0) {
            this.dragIndex = -1;
            this.panelDragging = false;
        }
        this.panels.forEach(panel -> panel.mouseReleased(mouseX, mouseY, button));
        this.themeEditor.mouseReleased(mouseX, mouseY, button);
        return super.method_25406(mouseX, mouseY, button);
    }

    public boolean method_25403(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        this.themeEditor.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        return super.method_25403(mouseX, mouseY, button, deltaX, deltaY);
    }

    public boolean method_25401(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        float amout = RenderService.getInstance().scaled(15.0f);
        this.scroll += (float)(verticalAmount * (double)amout);
        return super.method_25401(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    public boolean method_25400(char chr, int modifiers) {
        for (Panel panel : this.panels) {
            if (!panel.charTyped(chr, modifiers)) continue;
            return true;
        }
        if (this.themeEditor.charTyped(chr, modifiers)) {
            return true;
        }
        if (this.searchTyping) {
            this.searchQuery = this.searchQuery + chr;
            this.applySearchFilter();
            return true;
        }
        return super.method_25400(chr, modifiers);
    }

    private void savePanelOrder() {
        try {
            Path dir = Paths.get(ClientInfo.CONFIG_PATH_OTHER, new String[0]);
            Files.createDirectories(dir, new FileAttribute[0]);
            Path file = dir.resolve("panel_order.json");
            ArrayList<String> order = new ArrayList<String>();
            for (Panel p : this.panels) {
                order.add(p.getCategory().getLabel());
            }
            Files.writeString(file, (CharSequence)GSON.toJson(order), new OpenOption[0]);
        }
        catch (IOException iOException) {
            // empty catch block
        }
    }

    private List<String> loadPanelOrder() {
        try {
            Path file = Paths.get(ClientInfo.CONFIG_PATH_OTHER, "panel_order.json");
            if (!Files.exists(file, new LinkOption[0])) {
                return null;
            }
            String json = Files.readString(file);
            return (List)GSON.fromJson(json, new TypeToken<List<String>>(this){}.getType());
        }
        catch (Exception e) {
            return null;
        }
    }

    public void method_48267() {
    }

    public boolean method_25422() {
        return false;
    }

    public void method_25420(class_332 context, int mouseX, int mouseY, float delta) {
    }

    public boolean method_25421() {
        return false;
    }

    protected void method_57734() {
    }

    @Generated
    public float getScroll() {
        return this.scroll;
    }

    @Generated
    public AnimationUtil getScrollAnimation() {
        return this.scrollAnimation;
    }

    @Generated
    public boolean isOpen() {
        return this.open;
    }

    @Generated
    public AnimationUtil getOpenAnimation() {
        return this.openAnimation;
    }

    @Generated
    public List<Panel> getPanels() {
        return this.panels;
    }

    @Generated
    public ThemeEditor getThemeEditor() {
        return this.themeEditor;
    }

    @Generated
    public class_2960 getLeonTexture() {
        return this.leonTexture;
    }

    @Generated
    public class_2960 getLeon67Texture() {
        return this.leon67Texture;
    }

    @Generated
    public class_2960[] getGirlTextures() {
        return this.girlTextures;
    }

    @Generated
    public String getSearchQuery() {
        return this.searchQuery;
    }

    @Generated
    public boolean isSearchTyping() {
        return this.searchTyping;
    }

    @Generated
    public AnimationUtil getSearchAnimation() {
        return this.searchAnimation;
    }

    @Generated
    public int getDragIndex() {
        return this.dragIndex;
    }

    @Generated
    public long getDragPressTime() {
        return this.dragPressTime;
    }

    @Generated
    public boolean isPanelDragging() {
        return this.panelDragging;
    }

    @Generated
    public double getDragMouseX() {
        return this.dragMouseX;
    }

    @Generated
    public double getDragStartX() {
        return this.dragStartX;
    }

    @Generated
    public AnimationUtil getDragAlphaAnimation() {
        return this.dragAlphaAnimation;
    }

    @Generated
    public float[] getPanelAnimatedX() {
        return this.panelAnimatedX;
    }

    @Generated
    public static ScreenClickGUI getInstance() {
        return instance;
    }
}

