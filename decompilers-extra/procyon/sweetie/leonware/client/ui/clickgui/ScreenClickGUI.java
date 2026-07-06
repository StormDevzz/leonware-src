// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.ui.clickgui;

import com.google.gson.GsonBuilder;
import lombok.Generated;
import com.google.gson.reflect.TypeToken;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.io.IOException;
import java.nio.file.OpenOption;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.Paths;
import sweetie.leonware.api.system.backend.ClientInfo;
import sweetie.leonware.api.utils.math.MouseUtil;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.render.fonts.Fonts;
import java.awt.Color;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.client.features.modules.render.ClickGUIModule;
import java.util.function.Function;
import sweetie.leonware.client.ui.UIComponent;
import sweetie.leonware.client.services.RenderService;
import sweetie.leonware.api.utils.animation.Easing;
import net.minecraft.class_332;
import java.util.Iterator;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.system.files.FileUtil;
import java.util.ArrayList;
import net.minecraft.class_2561;
import com.google.gson.Gson;
import net.minecraft.class_2960;
import sweetie.leonware.client.ui.theme.ThemeEditor;
import java.util.List;
import sweetie.leonware.api.utils.animation.AnimationUtil;
import sweetie.leonware.api.system.interfaces.QuickImports;
import net.minecraft.class_437;

public class ScreenClickGUI extends class_437 implements QuickImports
{
    private static final ScreenClickGUI instance;
    private float scroll;
    private final AnimationUtil scrollAnimation;
    private boolean open;
    private final AnimationUtil openAnimation;
    private final List<Panel> panels;
    private final ThemeEditor themeEditor;
    private final class_2960 leonTexture;
    private final class_2960 leon67Texture;
    private final class_2960[] girlTextures;
    private String searchQuery;
    private boolean searchTyping;
    private final AnimationUtil searchAnimation;
    private int dragIndex;
    private long dragPressTime;
    private boolean panelDragging;
    private double dragMouseX;
    private double dragStartX;
    private final AnimationUtil dragAlphaAnimation;
    private static final long DRAG_HOLD_MS = 400L;
    private static final Gson GSON;
    private final float[] panelAnimatedX;
    
    public ScreenClickGUI() {
        super(class_2561.method_30163(""));
        this.scrollAnimation = new AnimationUtil();
        this.openAnimation = new AnimationUtil();
        this.panels = new ArrayList<Panel>();
        this.themeEditor = ThemeEditor.getInstance();
        this.leonTexture = FileUtil.getImage("leon/leon");
        this.leon67Texture = FileUtil.getImage("leon/leon67");
        this.girlTextures = new class_2960[] { FileUtil.getImage("leon/girl1"), FileUtil.getImage("leon/girl2"), FileUtil.getImage("leon/girl3"), FileUtil.getImage("leon/girl5"), FileUtil.getImage("leon/girl7"), FileUtil.getImage("leon/girl8"), FileUtil.getImage("leon/girl9"), FileUtil.getImage("leon/girl10"), FileUtil.getImage("leon/girl11"), FileUtil.getImage("leon/girl12"), FileUtil.getImage("leon/girl13"), FileUtil.getImage("leon/girl16"), FileUtil.getImage("leon/girl17"), FileUtil.getImage("leon/girl18"), FileUtil.getImage("leon/girl19"), FileUtil.getImage("leon/girl20"), FileUtil.getImage("leon/gir18") };
        this.searchQuery = "";
        this.searchTyping = false;
        this.searchAnimation = new AnimationUtil();
        this.dragIndex = -1;
        this.dragPressTime = 0L;
        this.panelDragging = false;
        this.dragMouseX = 0.0;
        this.dragStartX = 0.0;
        this.dragAlphaAnimation = new AnimationUtil();
        this.panelAnimatedX = new float[20];
        final List<String> savedOrder = this.loadPanelOrder();
        if (savedOrder != null && savedOrder.size() == Category.values().length) {
            for (final String label : savedOrder) {
                for (final Category cat : Category.values()) {
                    if (cat.getLabel().equals(label)) {
                        final Panel panel = new Panel(cat);
                        panel.setCategoryIndex(this.panels.size() * 45);
                        this.panels.add(panel);
                        break;
                    }
                }
            }
        }
        if (this.panels.size() != Category.values().length) {
            this.panels.clear();
            for (int i = 0; i < Category.values().length; ++i) {
                final Category category = Category.values()[i];
                final Panel panel2 = new Panel(category);
                panel2.setCategoryIndex(i * 45);
                this.panels.add(panel2);
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
    
    public void method_25394(final class_332 context, final int mouseX, final int mouseY, final float delta) {
        super.method_25394(context, mouseX, mouseY, delta);
        this.scrollAnimation.update();
        this.scrollAnimation.run(this.scroll, 600L, Easing.EXPO_OUT);
        this.openAnimation.update();
        this.openAnimation.run(this.open ? 1.0 : 0.0, 500L, Easing.EXPO_OUT);
        final float openAnim = (float)this.openAnimation.getValue();
        if (!this.open && openAnim < 0.1) {
            this.method_25419();
        }
        this.dragAlphaAnimation.update();
        this.dragAlphaAnimation.run(this.panelDragging ? 1.0 : 0.0, 200L, Easing.EXPO_OUT);
        this.updateDragState(mouseX);
        final float windowHeight = (float)ScreenClickGUI.mc.method_22683().method_4502();
        final float windowWidth = (float)ScreenClickGUI.mc.method_22683().method_4486();
        final float off = RenderService.getInstance().scaled(12.0f);
        final float totalWidth = this.panels.stream().map((Function<? super Object, ? extends Float>)UIComponent::getWidth).reduce(0.0f, Float::sum) + (this.panels.size() - 1) * (off / 2.0f);
        final float sex = this.open ? (-1.0f + openAnim) : (1.0f - openAnim);
        final float panelY = (float)(windowHeight / 12.0f + this.scrollAnimation.getValue()) * openAnim + windowHeight * sex;
        final float firstX = (windowWidth - totalWidth) / 2.0f;
        if (this.themeEditor.isOpen()) {
            this.themeEditor.setAnim(openAnim);
            if (!this.themeEditor.isUserMoved()) {
                this.themeEditor.setX(windowWidth - this.themeEditor.getWidth() * this.themeEditor.getAnim());
                this.themeEditor.setY(windowHeight / 2.0f - this.themeEditor.getHeight() / 2.0f);
            }
        }
        for (int i = 0; i < this.panels.size(); ++i) {
            final Panel panel = this.panels.get(i);
            final float targetX = firstX + i * (panel.getWidth() + off / 2.0f);
            if (this.panelDragging && i == this.dragIndex) {
                final float currentX = (float)(this.dragStartX + (mouseX - this.dragMouseX));
                panel.setAlpha(openAnim * 0.75f);
                panel.setY(panelY);
                panel.setX(currentX);
                this.panelAnimatedX[i] = currentX;
                final int hoverIdx = this.getHoverIndex(mouseX, firstX, off);
                if (hoverIdx >= 0 && hoverIdx < this.panels.size() && hoverIdx != this.dragIndex) {
                    final float swappedCurrentX = this.panelAnimatedX[hoverIdx];
                    final Panel moved = this.panels.remove(this.dragIndex);
                    this.panels.add(hoverIdx, moved);
                    final float oldDragTarget = firstX + this.dragIndex * (panel.getWidth() + off / 2.0f);
                    this.panelAnimatedX[this.dragIndex] = ((swappedCurrentX != 0.0f) ? swappedCurrentX : oldDragTarget);
                    this.dragIndex = hoverIdx;
                    this.dragStartX = firstX + hoverIdx * (panel.getWidth() + off / 2.0f);
                    this.dragMouseX = mouseX;
                    this.panelAnimatedX[hoverIdx] = currentX;
                }
            }
            else {
                final float animSpeed = 0.22f;
                if (this.panelAnimatedX[i] == 0.0f) {
                    this.panelAnimatedX[i] = targetX;
                }
                this.panelAnimatedX[i] += (targetX - this.panelAnimatedX[i]) * animSpeed;
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
    
    private void renderImage(final class_332 context, final float openAnim) {
        if (openAnim <= 0.01f) {
            return;
        }
        final float windowWidth = (float)ScreenClickGUI.mc.method_22683().method_4486();
        final float windowHeight = (float)ScreenClickGUI.mc.method_22683().method_4502();
        final ClickGUIModule clickGUIModule = ClickGUIModule.getInstance();
        final float imgSize = clickGUIModule.imageSizeSlider.getValue() * openAnim;
        final float originalW = 423.0f;
        final float originalH = 664.0f;
        final float aspect = originalW / originalH;
        float imgW = imgSize * aspect;
        final float imgH = imgSize;
        final int imgAlpha = (int)(openAnim * 255.0f);
        class_2960 texture = null;
        float widthMultiplier = 1.0f;
        if (clickGUIModule.imageSetting.is("\u041b\u0435\u043e\u043d")) {
            texture = this.leonTexture;
        }
        else if (clickGUIModule.imageSetting.is("\u0428\u043b\u044e\u0445\u0430")) {
            texture = this.leon67Texture;
            widthMultiplier = 1.35f;
        }
        else {
            for (int i = 0; i < this.girlTextures.length; ++i) {
                if (clickGUIModule.imageSetting.is("\u0414\u0435\u0432\u043e\u0447\u043a\u0430" + (i + 1))) {
                    texture = this.girlTextures[i];
                    widthMultiplier = 1.25f;
                    break;
                }
            }
        }
        if (texture != null) {
            imgW *= widthMultiplier;
            final float imgX = clickGUIModule.imageXSlider.getValue() * (windowWidth - imgW);
            final float imgY = clickGUIModule.imageYSlider.getValue() * (windowHeight - imgH);
            RenderUtil.TEXTURE_RECT.drawTexture(context.method_51448(), texture, imgX, imgY, imgW, imgH, new Color(255, 255, 255, imgAlpha));
        }
    }
    
    private void updateDragState(final int mouseX) {
        if (this.dragIndex == -1) {
            return;
        }
        if (!this.panelDragging) {
            final long held = System.currentTimeMillis() - this.dragPressTime;
            if (held >= 400L) {
                this.panelDragging = true;
                this.dragMouseX = mouseX;
                final float off = RenderService.getInstance().scaled(12.0f);
                final float windowWidth = (float)ScreenClickGUI.mc.method_22683().method_4486();
                final float totalWidth = this.panels.stream().map((Function<? super Object, ? extends Float>)UIComponent::getWidth).reduce(0.0f, Float::sum) + (this.panels.size() - 1) * (off / 2.0f);
                final float firstX = (windowWidth - totalWidth) / 2.0f;
                final Panel panel = this.panels.get(this.dragIndex);
                this.dragStartX = firstX + this.dragIndex * (panel.getWidth() + off / 2.0f);
            }
        }
    }
    
    private int getHoverIndex(final double mouseX, final float firstX, final float off) {
        if (this.panels.isEmpty()) {
            return -1;
        }
        final float panelW = this.panels.get(0).getWidth();
        final float step = panelW + off / 2.0f;
        for (int i = 0; i < this.panels.size(); ++i) {
            final float slotStart = firstX + i * step;
            final float slotEnd = slotStart + panelW;
            if (mouseX >= slotStart && mouseX <= slotEnd) {
                return i;
            }
        }
        return -1;
    }
    
    private int getInsertIndex(final double mouseX, final float firstX, final float off) {
        if (this.panels.isEmpty()) {
            return 0;
        }
        final float panelW = this.panels.get(0).getWidth();
        final float step = panelW + off / 2.0f;
        for (int i = 0; i <= this.panels.size(); ++i) {
            final float slotCenter = firstX + i * step;
            if (mouseX < slotCenter) {
                return i;
            }
        }
        return this.panels.size();
    }
    
    private void renderSearch(final class_332 context, final int mouseX, final int mouseY, final float openAnim) {
        if (openAnim < 0.05f) {
            return;
        }
        this.searchAnimation.update();
        this.searchAnimation.run(this.searchTyping ? 1.0 : 0.0, 250L, Easing.EXPO_OUT);
        final float windowWidth = (float)ScreenClickGUI.mc.method_22683().method_4486();
        final float windowHeight = (float)ScreenClickGUI.mc.method_22683().method_4502();
        final int fullAlpha = (int)(openAnim * 255.0f);
        final float baseW = RenderService.getInstance().scaled(80.0f);
        final String dispText = this.searchTyping ? (this.searchQuery.isEmpty() ? ((System.currentTimeMillis() % 800L > 400L) ? "|" : "") : (this.searchQuery + ((System.currentTimeMillis() % 800L > 400L) ? "|" : ""))) : (this.searchQuery.isEmpty() ? "\u041f\u043e\u0438\u0441\u043a" : this.searchQuery);
        final float fontSize = RenderService.getInstance().scaled(6.0f);
        final float textWidth = Fonts.PS_MEDIUM.getWidth(dispText.isEmpty() ? "\u041f\u043e\u0438\u0441\u043a" : dispText, fontSize);
        final float expandedW = Math.max(baseW, textWidth + RenderService.getInstance().scaled(16.0f));
        final float searchAnimVal = (float)this.searchAnimation.getValue();
        final float rectW = baseW + (expandedW - baseW) * searchAnimVal;
        final float rectH = RenderService.getInstance().scaled(14.0f);
        final float rectX = windowWidth / 2.0f - rectW / 2.0f;
        final float rectY = windowHeight - rectH - RenderService.getInstance().scaled(10.0f);
        final float rectRound = rectH * 0.35f;
        final Color searchBg = UIColors.widgetBlur(fullAlpha);
        RenderUtil.BLUR_RECT.draw(context.method_51448(), rectX, rectY, rectW, rectH, rectRound, searchBg);
        final float textX = rectX + rectW / 2.0f - Fonts.PS_MEDIUM.getWidth(dispText, fontSize) / 2.0f;
        final float textY = rectY + rectH / 2.0f - fontSize / 2.0f;
        final Color textColor = UIColors.textColor(fullAlpha);
        Fonts.PS_MEDIUM.drawText(context.method_51448(), dispText, textX, textY, fontSize, textColor);
        if (!this.searchQuery.isEmpty()) {
            final float btnSize = rectH * 0.7f;
            final float btnX = rectX + rectW + RenderService.getInstance().scaled(3.0f);
            final float btnY = rectY + rectH / 2.0f - btnSize / 2.0f;
            final float btnRound = btnSize / 2.0f;
            RenderUtil.BLUR_RECT.draw(context.method_51448(), btnX, btnY, btnSize, btnSize, btnRound, UIColors.blur(fullAlpha));
            final float xFont = btnSize * 0.5f;
            Fonts.PS_MEDIUM.drawCenteredText(context.method_51448(), "x", btnX + btnSize / 2.0f, btnY + btnSize / 2.0f - xFont / 2.0f, xFont, UIColors.inactiveTextColor(fullAlpha));
        }
    }
    
    private float[] getSearchResetBtnBounds() {
        final float windowWidth = (float)ScreenClickGUI.mc.method_22683().method_4486();
        final float windowHeight = (float)ScreenClickGUI.mc.method_22683().method_4502();
        final float rectH = RenderService.getInstance().scaled(14.0f);
        final float baseW = RenderService.getInstance().scaled(80.0f);
        final float btnSize = rectH * 0.7f;
        float btnX;
        if (!this.searchQuery.isEmpty()) {
            final float fontSize = RenderService.getInstance().scaled(6.0f);
            final float textWidth = Fonts.PS_MEDIUM.getWidth(this.searchQuery, fontSize);
            final float expandedW = Math.max(baseW, textWidth + RenderService.getInstance().scaled(16.0f));
            final float rectX = windowWidth / 2.0f - expandedW / 2.0f;
            btnX = rectX + expandedW + RenderService.getInstance().scaled(3.0f);
        }
        else {
            btnX = windowWidth / 2.0f - baseW / 2.0f + baseW + RenderService.getInstance().scaled(3.0f);
        }
        final float btnY = windowHeight - rectH - RenderService.getInstance().scaled(10.0f) + rectH / 2.0f - btnSize / 2.0f;
        return new float[] { btnX, btnY, btnSize, btnSize };
    }
    
    private float[] getSearchRectBounds() {
        final float windowWidth = (float)ScreenClickGUI.mc.method_22683().method_4486();
        final float windowHeight = (float)ScreenClickGUI.mc.method_22683().method_4502();
        final float rectH = RenderService.getInstance().scaled(14.0f);
        final float baseW = RenderService.getInstance().scaled(80.0f);
        final float fontSize = RenderService.getInstance().scaled(6.0f);
        final String dispText = this.searchQuery.isEmpty() ? "\u041f\u043e\u0438\u0441\u043a" : this.searchQuery;
        final float textWidth = Fonts.PS_MEDIUM.getWidth(dispText, fontSize);
        final float expandedW = Math.max(baseW, textWidth + RenderService.getInstance().scaled(16.0f));
        final float rectX = windowWidth / 2.0f - expandedW / 2.0f;
        final float rectY = windowHeight - rectH - RenderService.getInstance().scaled(10.0f);
        return new float[] { rectX, rectY, expandedW, rectH };
    }
    
    public void clearSearch() {
        this.searchQuery = "";
        this.searchTyping = false;
        this.applySearchFilter();
    }
    
    private void applySearchFilter() {
        this.panels.forEach(p -> p.setSearchFilter(this.searchQuery));
    }
    
    public boolean method_25404(final int keyCode, final int scanCode, final int modifiers) {
        if (keyCode == 256) {
            boolean isBinding = false;
            for (final Panel p : this.panels) {
                if (p.isBindingActive()) {
                    isBinding = true;
                }
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
        else {
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
    }
    
    public boolean method_25402(final double mouseX, final double mouseY, final int button) {
        if (this.panelDragging) {
            return true;
        }
        if (!this.searchQuery.isEmpty()) {
            final float[] btn = this.getSearchResetBtnBounds();
            if (MouseUtil.isHovered(mouseX, mouseY, btn[0], btn[1], btn[2], btn[3])) {
                this.clearSearch();
                return true;
            }
        }
        final float[] rect = this.getSearchRectBounds();
        if (MouseUtil.isHovered(mouseX, mouseY, rect[0], rect[1], rect[2], rect[3])) {
            return this.searchTyping = true;
        }
        this.searchTyping = false;
        Panel panel = null;
        if (button == 0) {
            for (int i = 0; i < this.panels.size(); ++i) {
                panel = this.panels.get(i);
                if (MouseUtil.isHovered(mouseX, mouseY, panel.getX(), panel.getY(), panel.getWidth(), panel.getHeaderHeight())) {
                    this.dragIndex = i;
                    this.dragPressTime = System.currentTimeMillis();
                    this.panelDragging = false;
                    this.dragMouseX = mouseX;
                    break;
                }
            }
        }
        this.panels.forEach(panel -> panel.mouseClicked(mouseX, mouseY, button));
        this.themeEditor.mouseClicked(mouseX, mouseY, button);
        if (!this.searchQuery.isEmpty() && !this.searchTyping) {
            this.clearSearch();
        }
        return super.method_25402(mouseX, mouseY, button);
    }
    
    public boolean method_25406(final double mouseX, final double mouseY, final int button) {
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
    
    public boolean method_25403(final double mouseX, final double mouseY, final int button, final double deltaX, final double deltaY) {
        this.themeEditor.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        return super.method_25403(mouseX, mouseY, button, deltaX, deltaY);
    }
    
    public boolean method_25401(final double mouseX, final double mouseY, final double horizontalAmount, final double verticalAmount) {
        final float amout = RenderService.getInstance().scaled(15.0f);
        this.scroll += (float)(verticalAmount * amout);
        return super.method_25401(mouseX, mouseY, horizontalAmount, verticalAmount);
    }
    
    public boolean method_25400(final char chr, final int modifiers) {
        for (final Panel panel : this.panels) {
            if (panel.charTyped(chr, modifiers)) {
                return true;
            }
        }
        if (this.themeEditor.charTyped(chr, modifiers)) {
            return true;
        }
        if (this.searchTyping) {
            this.searchQuery += chr;
            this.applySearchFilter();
            return true;
        }
        return super.method_25400(chr, modifiers);
    }
    
    private void savePanelOrder() {
        try {
            final Path dir = Paths.get(ClientInfo.CONFIG_PATH_OTHER, new String[0]);
            Files.createDirectories(dir, (FileAttribute<?>[])new FileAttribute[0]);
            final Path file = dir.resolve("panel_order.json");
            final List<String> order = new ArrayList<String>();
            for (final Panel p : this.panels) {
                order.add(p.getCategory().getLabel());
            }
            Files.writeString(file, ScreenClickGUI.GSON.toJson(order), new OpenOption[0]);
        }
        catch (final IOException ex) {}
    }
    
    private List<String> loadPanelOrder() {
        try {
            final Path file = Paths.get(ClientInfo.CONFIG_PATH_OTHER, "panel_order.json");
            if (!Files.exists(file, new LinkOption[0])) {
                return null;
            }
            final String json = Files.readString(file);
            return ScreenClickGUI.GSON.fromJson(json, new TypeToken<List<String>>(this) {}.getType());
        }
        catch (final Exception e) {
            return null;
        }
    }
    
    public void method_48267() {
    }
    
    public boolean method_25422() {
        return false;
    }
    
    public void method_25420(final class_332 context, final int mouseX, final int mouseY, final float delta) {
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
        return ScreenClickGUI.instance;
    }
    
    static {
        instance = new ScreenClickGUI();
        GSON = new GsonBuilder().setPrettyPrinting().create();
    }
}
