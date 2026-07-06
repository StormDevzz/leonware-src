/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_310
 *  net.minecraft.class_332
 *  net.minecraft.class_4587
 */
package sweetie.leonware.client.ui.theme;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.other.WindowResizeEvent;
import sweetie.leonware.api.system.configs.ThemeManager;
import sweetie.leonware.api.utils.animation.AnimationUtil;
import sweetie.leonware.api.utils.animation.Easing;
import sweetie.leonware.api.utils.color.ColorUtil;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.math.MouseUtil;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.api.utils.render.fonts.Fonts;
import sweetie.leonware.api.utils.render.fonts.Icons;
import sweetie.leonware.client.ui.UIComponent;
import sweetie.leonware.client.ui.clickgui.module.settings.ColorComponent;
import sweetie.leonware.client.ui.theme.Theme;
import sweetie.leonware.client.ui.theme.ThemeSelectable;

public class ThemeEditor
extends UIComponent {
    private static final ThemeEditor instance = new ThemeEditor();
    private final List<ThemeSelectable> themeSelectables = new ArrayList<ThemeSelectable>();
    private final List<ThemeBound> themeBounds = new ArrayList<ThemeBound>();
    private final List<RowDeleteBound> rowDeleteBounds = new ArrayList<RowDeleteBound>();
    private final Theme defaultTheme;
    private Theme currentTheme = this.defaultTheme = new Theme("LeonWare");
    protected Theme editTheme;
    private final String placeHolderText = "\u0418\u043c\u044f \u043d\u043e\u0432\u043e\u0439 \u0442\u0435\u043c\u044b";
    private String typingText = "";
    private boolean typing;
    private DeleteButton deleteButton = new DeleteButton(-1.0f, -1.0f, -1.0f);
    private CreateButton createButton = new CreateButton(-1.0f, -1.0f, -1.0f);
    private BackButton backButton = new BackButton(-1.0f, -1.0f, -1.0f, -1.0f);
    private final AnimationUtil openAnimation = new AnimationUtil();
    private boolean open;
    private float anim;
    private boolean userMoved;
    private boolean dragging;
    private boolean pressedOnHeader;
    private double pressMouseX;
    private double pressMouseY;
    private float dragOffsetX;
    private float dragOffsetY;
    private static final float DRAG_THRESHOLD = 3.0f;

    private int alphaAnim() {
        return (int)(this.openAnimation.getValue() * (double)this.anim * 255.0);
    }

    public ThemeEditor() {
        this.setWidth(this.scaled(95.0f));
        this.setHeight(this.scaled(150.0f));
        WindowResizeEvent.getInstance().subscribe(new Listener<WindowResizeEvent>(-1, event -> {
            this.setWidth(this.scaled(95.0f));
            if (this.userMoved) {
                this.clampToScreen();
            }
        }));
    }

    public void init() {
        this.refresh();
    }

    private void refresh() {
        ThemeManager.getInstance().refresh();
    }

    public void save(boolean last) {
        if (!last) {
            ThemeManager.getInstance().saveAll();
        } else if (this.currentTheme != null) {
            ThemeManager.getInstance().saveLastSelected(this.currentTheme);
        }
    }

    public void load() {
        ThemeManager.getInstance().refresh();
        Theme last = ThemeManager.getInstance().loadLastSelected();
        if (last != null) {
            this.themeSelectables.add(new ThemeSelectable(last));
            this.currentTheme = last;
        } else {
            this.themeSelectables.add(new ThemeSelectable(this.defaultTheme));
            this.currentTheme = this.defaultTheme;
        }
    }

    @Override
    public void render(class_332 context, int mouseX, int mouseY, float delta) {
        this.openAnimation.update();
        this.openAnimation.run(this.open ? 1.0 : 0.0, 100L, Easing.SINE_OUT);
        if (this.openAnimation.getValue() <= 0.1) {
            return;
        }
        this.themeBounds.clear();
        this.rowDeleteBounds.clear();
        float round = this.getWidth() * 0.05f;
        float headerHeight = this.scaled(this.getHeaderHeight());
        float headerFontSize = headerHeight * 0.52f;
        String text = "Theme Editor";
        float textWidth = Fonts.PS_BOLD.getWidth(text, headerFontSize);
        if (!this.userMoved) {
            this.setWidth(textWidth * 1.5f);
        }
        class_4587 matrixStack = context.method_51448();
        RenderUtil.BLUR_RECT.draw(matrixStack, this.getX(), this.getY(), this.getWidth(), this.getHeight(), round, UIColors.blur(this.alphaAnim()));
        Fonts.PS_BOLD.drawGradientText(matrixStack, text, this.getX() + this.getWidth() / 2.0f - textWidth / 2.0f, this.getY() + headerHeight / 2.0f - headerFontSize / 2.0f, headerFontSize, UIColors.primary(this.alphaAnim()), UIColors.secondary(this.alphaAnim()), Fonts.PS_BOLD.getWidth(text, headerFontSize) / 4.0f);
        this.placeRender(context, mouseX, mouseY, delta);
        float xOffset = this.getX() + this.offset();
        float widthOffset = this.getWidth() - this.offset() * 2.0f;
        float themeY = this.gap() + headerHeight + this.getPlaceTextCoordinates()[3];
        if (this.editTheme == null) {
            for (ThemeSelectable theme : this.themeSelectables) {
                theme.setAlpha((float)(this.openAnimation.getValue() * (double)this.anim));
                theme.setX(xOffset);
                theme.setY(this.getY() + themeY);
                theme.setWidth(widthOffset);
                theme.setHeight(this.scaled(17.0f));
                theme.render(context, mouseX, mouseY, delta);
                if (this.themeSelectables.size() > 1) {
                    this.drawRowDeleteButton(matrixStack, theme, mouseX, mouseY);
                }
                themeY += theme.getHeight() + this.gap();
            }
            this.drawAutoSaveHint(matrixStack, xOffset, this.getY() + themeY, widthOffset);
            this.setHeight(themeY += this.scaled(this.getHintHeight()) + this.gap());
        } else {
            float elementY = themeY;
            float elementHeight = this.scaled(this.getElementHeight());
            float textSize = elementHeight * 0.6f;
            float textY = this.getY() + elementHeight / 2.0f - textSize / 2.0f;
            float elementWidth = this.getWidth() - this.offset();
            float colorSize = elementHeight * 0.8f;
            float colorX = this.getX() + elementWidth - colorSize;
            float colorY = this.getY() + elementHeight / 2.0f - colorSize / 2.0f;
            float roundColor = colorSize * 0.2f;
            for (Theme.ElementColor elementColor : this.editTheme.getElementColors()) {
                float height = elementHeight;
                Fonts.PS_MEDIUM.drawText(matrixStack, elementColor.getName(), xOffset, textY + elementY, textSize, UIColors.textColor(this.alphaAnim()));
                RenderUtil.RECT.draw(matrixStack, colorX, colorY + elementY, colorSize, colorSize, roundColor, ColorUtil.setAlpha(elementColor.getColor(), this.alphaAnim()));
                ColorComponent colorComponent = elementColor.getColorComponent();
                colorComponent.updateOpen();
                float cAnim = colorComponent.getValue();
                if ((double)cAnim > 0.0) {
                    colorComponent.setX(xOffset);
                    colorComponent.setY(textY + elementY + elementHeight);
                    colorComponent.setWidth(widthOffset);
                    colorComponent.setAlpha((float)this.alphaAnim() / 255.0f);
                    colorComponent.render(context, mouseX, mouseY, delta);
                    height += colorComponent.getHeight() + this.gap() * 2.0f * cAnim;
                }
                this.themeBounds.add(new ThemeBound(xOffset, colorY + elementY, elementWidth, height, elementColor));
                elementY += height + this.gap();
            }
            this.drawAutoSaveHint(matrixStack, xOffset, this.getY() + elementY, widthOffset);
            this.setHeight(elementY += this.scaled(this.getHintHeight()) + this.gap());
        }
    }

    private void drawRowDeleteButton(class_4587 matrixStack, ThemeSelectable selectable, int mouseX, int mouseY) {
        float padding = this.gap() * 0.6f;
        float size = selectable.getHeight() - padding * 2.0f;
        float x = selectable.getX() + selectable.getWidth() - size - padding;
        float y = selectable.getY() + padding;
        boolean hovered = MouseUtil.isHovered(mouseX, mouseY, x, y, size, size);
        float iconSize = size * 0.55f;
        Color iconColor = hovered ? UIColors.negativeColor(this.alphaAnim()) : UIColors.inactiveTextColor(this.alphaAnim());
        Fonts.ICONS.drawCenteredText(matrixStack, Icons.TRASH.getLetter(), x + size / 2.0f, y + size / 2.0f - iconSize / 2.0f, iconSize, iconColor, 0.1f);
        this.rowDeleteBounds.add(new RowDeleteBound(x, y, size, selectable.getTheme()));
    }

    private void drawAutoSaveHint(class_4587 matrixStack, float x, float y, float width) {
        float fontSize = this.scaled(this.getHintHeight()) * 0.65f;
        String text = "\u0421\u043e\u0445\u0440\u0430\u043d\u044f\u0435\u0442\u0441\u044f \u0430\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438";
        float w = Fonts.PS_MEDIUM.getWidth(text, fontSize);
        float tx = x + (width - w) / 2.0f;
        float ty = y + (this.scaled(this.getHintHeight()) - fontSize) / 2.0f;
        Fonts.PS_MEDIUM.drawText(matrixStack, text, tx, ty, fontSize, UIColors.inactiveTextColor((int)((float)this.alphaAnim() * 0.7f)));
    }

    private void placeRender(class_332 context, int mouseX, int mouseY, float delta) {
        class_4587 matrixStack = context.method_51448();
        float[] placeCoords = this.getPlaceTextCoordinates();
        float placeX = placeCoords[0];
        float placeY = placeCoords[1];
        float placeWidth = placeCoords[2];
        float placeHeight = placeCoords[3];
        float placeFontSize = placeHeight * 0.4f;
        float placeRound = placeHeight * 0.2f;
        boolean edit = this.editTheme != null;
        RenderUtil.BLUR_RECT.draw(matrixStack, placeX, placeY, placeWidth, placeHeight, placeRound, UIColors.widgetBlur(this.alphaAnim()));
        if (edit) {
            float margin = this.gap();
            float btnSize = placeHeight - margin * 2.0f;
            float deleteX = placeX + placeWidth - btnSize - margin;
            float deleteY = placeY + margin;
            boolean canDelete = this.themeSelectables.size() > 1;
            float backLeft = placeX;
            float backRight = canDelete ? deleteX - margin : placeX + placeWidth;
            float backWidth = backRight - backLeft;
            float backHeight = placeHeight;
            boolean backHovered = MouseUtil.isHovered(mouseX, mouseY, backLeft, placeY, backWidth, backHeight);
            Color textColor = backHovered ? UIColors.primary(this.alphaAnim()) : UIColors.textColor(this.alphaAnim());
            String backText = "< " + this.editTheme.getName();
            Fonts.PS_BOLD.drawText(matrixStack, backText, placeX + this.offset(), placeY + placeHeight / 2.0f - placeFontSize / 2.0f, placeFontSize, textColor);
            this.backButton = new BackButton(backLeft, placeY, backWidth, backHeight);
            if (canDelete) {
                float iconSize = btnSize * 0.5f;
                boolean hovered = MouseUtil.isHovered(mouseX, mouseY, deleteX, deleteY, btnSize, btnSize);
                Color bgColor = hovered ? UIColors.negativeColor(this.alphaAnim()) : UIColors.blur(this.alphaAnim());
                RenderUtil.BLUR_RECT.draw(matrixStack, deleteX, deleteY, btnSize, btnSize, placeRound, bgColor);
                Fonts.ICONS.drawCenteredText(matrixStack, Icons.CROSS.getLetter(), deleteX + btnSize / 2.0f, deleteY + btnSize / 2.0f - iconSize / 2.0f, iconSize, UIColors.textColor(this.alphaAnim()), 0.1f);
                this.deleteButton = new DeleteButton(deleteX, deleteY, btnSize);
            } else {
                this.deleteButton = new DeleteButton(-1.0f, -1.0f, -1.0f);
            }
            this.createButton = new CreateButton(-1.0f, -1.0f, -1.0f);
        } else {
            String cursor = this.typing && System.currentTimeMillis() % 1000L > 500L ? "_" : " ";
            Object typeText = this.typingText.isEmpty() && !this.typing ? "\u0418\u043c\u044f \u043d\u043e\u0432\u043e\u0439 \u0442\u0435\u043c\u044b" : this.typingText + cursor;
            Color textColor = this.typingText.isEmpty() && !this.typing ? UIColors.inactiveTextColor(this.alphaAnim()) : UIColors.textColor(this.alphaAnim());
            Fonts.PS_BOLD.drawText(matrixStack, (String)typeText, placeX + this.offset(), placeY + placeHeight / 2.0f - placeFontSize / 2.0f, placeFontSize, textColor);
            float margin = this.gap();
            float btnSize = placeHeight - margin * 2.0f;
            float btnX = placeX + placeWidth - btnSize - margin;
            float btnY = placeY + margin;
            boolean canCreate = !this.typingText.isEmpty();
            boolean hovered = MouseUtil.isHovered(mouseX, mouseY, btnX, btnY, btnSize, btnSize);
            Color bgColor = canCreate ? (hovered ? UIColors.primary(this.alphaAnim()) : UIColors.gradient(0, this.alphaAnim())) : UIColors.blur(this.alphaAnim());
            RenderUtil.BLUR_RECT.draw(matrixStack, btnX, btnY, btnSize, btnSize, placeRound, bgColor);
            float plusFontSize = btnSize * 0.55f;
            Color plusColor = canCreate ? UIColors.textColor(this.alphaAnim()) : UIColors.inactiveTextColor(this.alphaAnim());
            Fonts.PS_BOLD.drawCenteredText(matrixStack, "+", btnX + btnSize / 2.0f, btnY + btnSize / 2.0f - plusFontSize / 2.0f, plusFontSize, plusColor);
            this.createButton = new CreateButton(btnX, btnY, btnSize);
            this.deleteButton = new DeleteButton(-1.0f, -1.0f, -1.0f);
            this.backButton = new BackButton(-1.0f, -1.0f, -1.0f, -1.0f);
        }
    }

    private boolean tryCreateTheme() {
        if (this.typingText.isEmpty()) {
            return false;
        }
        boolean exists = this.themeSelectables.stream().anyMatch(ts -> ts.getTheme().getName().equalsIgnoreCase(this.typingText));
        if (exists) {
            this.typingText = "";
            this.typing = false;
            return false;
        }
        Theme newTheme = new Theme(ThemeManager.getInstance().safeFileName(this.typingText));
        newTheme.getElementColors().clear();
        for (Theme.ElementColor elementColor : this.currentTheme.getElementColors()) {
            newTheme.getElementColors().add(new Theme.ElementColor(elementColor.getName(), elementColor.getColor()));
        }
        this.themeSelectables.add(new ThemeSelectable(newTheme));
        this.currentTheme = newTheme;
        ThemeManager.getInstance().saveLastSelected(this.currentTheme);
        this.typingText = "";
        this.typing = false;
        return true;
    }

    private void deleteEditTheme() {
        if (this.editTheme == null) {
            return;
        }
        if (this.themeSelectables.size() <= 1) {
            return;
        }
        String name = this.editTheme.getName();
        boolean removed = this.themeSelectables.removeIf(ts -> ts.getTheme() == this.editTheme || ts.getTheme().getName().equalsIgnoreCase(name));
        if (removed) {
            try {
                ThemeManager.getInstance().remove(name);
            }
            catch (Throwable throwable) {
                // empty catch block
            }
            if ((this.currentTheme == this.editTheme || this.currentTheme != null && this.currentTheme.getName().equalsIgnoreCase(name)) && !this.themeSelectables.isEmpty()) {
                this.currentTheme = this.themeSelectables.get(0).getTheme();
                ThemeManager.getInstance().saveLastSelected(this.currentTheme);
            }
        }
        this.editTheme = null;
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!this.open) {
            return;
        }
        if (this.typing && this.editTheme == null) {
            switch (keyCode) {
                case 259: {
                    if (this.typingText.isEmpty()) break;
                    this.typingText = this.typingText.substring(0, this.typingText.length() - 1);
                    break;
                }
                case 257: {
                    this.tryCreateTheme();
                }
            }
        }
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (!this.open) {
            return;
        }
        if (button == 0 && this.isHoveringHeader(mouseX, mouseY)) {
            this.pressedOnHeader = true;
            this.pressMouseX = mouseX;
            this.pressMouseY = mouseY;
            this.dragOffsetX = (float)mouseX - this.getX();
            this.dragOffsetY = (float)mouseY - this.getY();
            return;
        }
        if (this.editTheme != null) {
            if (button == 0 && this.deleteButton.size > 0.0f && MouseUtil.isHovered(mouseX, mouseY, (double)this.deleteButton.x, (double)this.deleteButton.y, (double)this.deleteButton.size, (double)this.deleteButton.size)) {
                this.deleteEditTheme();
                return;
            }
            if (button == 0 && this.backButton.width > 0.0f && MouseUtil.isHovered(mouseX, mouseY, (double)this.backButton.x, (double)this.backButton.y, (double)this.backButton.width, (double)this.backButton.height)) {
                ThemeManager.getInstance().saveAll();
                this.editTheme = null;
                return;
            }
            for (ThemeBound themeBound : this.themeBounds) {
                ColorComponent colorComponent = themeBound.elementColor.getColorComponent();
                if (MouseUtil.isHovered(mouseX, mouseY, (double)themeBound.x, (double)themeBound.y, (double)themeBound.width, (double)this.scaled(this.getElementHeight()))) {
                    colorComponent.toggleOpen();
                    return;
                }
                if (!MouseUtil.isHovered(mouseX, mouseY, (double)themeBound.x, (double)themeBound.y, (double)themeBound.width, (double)themeBound.height)) continue;
                colorComponent.mouseClicked(mouseX, mouseY, button);
            }
        } else {
            if (button == 0 && this.createButton.size > 0.0f && MouseUtil.isHovered(mouseX, mouseY, (double)this.createButton.x, (double)this.createButton.y, (double)this.createButton.size, (double)this.createButton.size)) {
                if (this.typingText.isEmpty()) {
                    this.typing = true;
                } else {
                    this.tryCreateTheme();
                }
                return;
            }
            if (MouseUtil.isHovered(mouseX, mouseY, (double)this.getPlaceTextCoordinates()[0], (double)this.getPlaceTextCoordinates()[1], (double)this.getPlaceTextCoordinates()[2], (double)this.getPlaceTextCoordinates()[3])) {
                this.typing = !this.typing;
                return;
            }
            if (button == 0 && this.themeSelectables.size() > 1) {
                for (RowDeleteBound bound : this.rowDeleteBounds) {
                    if (!MouseUtil.isHovered(mouseX, mouseY, (double)bound.x, (double)bound.y, (double)bound.size, (double)bound.size)) continue;
                    Theme toRemove = bound.theme;
                    String name = toRemove.getName();
                    boolean removed = this.themeSelectables.removeIf(ts -> ts.getTheme() == toRemove || ts.getTheme().getName().equalsIgnoreCase(name));
                    if (removed) {
                        try {
                            ThemeManager.getInstance().remove(name);
                        }
                        catch (Throwable throwable) {
                            // empty catch block
                        }
                        if ((this.currentTheme == toRemove || this.currentTheme != null && this.currentTheme.getName().equalsIgnoreCase(name)) && !this.themeSelectables.isEmpty()) {
                            this.currentTheme = this.themeSelectables.get(0).getTheme();
                            ThemeManager.getInstance().saveLastSelected(this.currentTheme);
                        }
                    }
                    return;
                }
            }
            for (ThemeSelectable theme : this.themeSelectables) {
                if (!MouseUtil.isHovered(mouseX, mouseY, (double)theme.getX(), (double)theme.getY(), (double)theme.getWidth(), (double)theme.getHeight())) continue;
                if (button == 1) {
                    this.editTheme = theme.getTheme();
                    continue;
                }
                if (button != 0) continue;
                this.currentTheme = theme.getTheme();
                ThemeManager.getInstance().saveLastSelected(this.currentTheme);
            }
        }
    }

    public void mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (!this.open) {
            return;
        }
        if (button != 0 || !this.pressedOnHeader) {
            return;
        }
        double dx = mouseX - this.pressMouseX;
        double dy = mouseY - this.pressMouseY;
        if (!this.dragging && dx * dx + dy * dy >= 9.0) {
            this.dragging = true;
            this.userMoved = true;
        }
        if (this.dragging) {
            this.setX((float)mouseX - this.dragOffsetX);
            this.setY((float)mouseY - this.dragOffsetY);
            this.clampToScreen();
        }
    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {
        if (!this.open) {
            return;
        }
        if (button == 0 && this.pressedOnHeader) {
            this.pressedOnHeader = false;
            this.dragging = false;
            return;
        }
        if (this.editTheme != null) {
            for (ThemeBound themeBound : this.themeBounds) {
                themeBound.elementColor.getColorComponent().mouseReleased(mouseX, mouseY, button);
            }
        }
    }

    public boolean charTyped(char chr, int modifiers) {
        if (!this.open) {
            return false;
        }
        if (this.editTheme != null) {
            return false;
        }
        if (!this.typing) {
            return false;
        }
        if (this.typingText.length() >= 16) {
            return false;
        }
        if (!this.isAllowedChar(chr)) {
            return false;
        }
        this.typingText = this.typingText + chr;
        return true;
    }

    private boolean isAllowedChar(char chr) {
        if (chr >= 'a' && chr <= 'z') {
            return true;
        }
        if (chr >= 'A' && chr <= 'Z') {
            return true;
        }
        if (chr >= '0' && chr <= '9') {
            return true;
        }
        return chr == '_' || chr == '-' || chr == ' ';
    }

    private boolean isHoveringHeader(double mouseX, double mouseY) {
        return MouseUtil.isHovered(mouseX, mouseY, (double)this.getX(), (double)this.getY(), (double)this.getWidth(), (double)this.scaled(this.getHeaderHeight()));
    }

    private void clampToScreen() {
        class_310 mc = class_310.method_1551();
        if (mc == null || mc.method_22683() == null) {
            return;
        }
        float margin = this.scaled(3.0f);
        float screenW = mc.method_22683().method_4486();
        float screenH = mc.method_22683().method_4502();
        float x = this.getX();
        float y = this.getY();
        float w = this.getWidth();
        float h = this.getHeight();
        if (w > screenW - margin * 2.0f) {
            w = screenW - margin * 2.0f;
        }
        if (h > screenH - margin * 2.0f) {
            h = screenH - margin * 2.0f;
        }
        if (x < margin) {
            x = margin;
        }
        if (y < margin) {
            y = margin;
        }
        if (x + w > screenW - margin) {
            x = screenW - w - margin;
        }
        if (y + h > screenH - margin) {
            y = screenH - h - margin;
        }
        this.setX(x);
        this.setY(y);
    }

    private float[] getPlaceTextCoordinates() {
        float x = this.getX() + this.offset();
        float y = this.getY() + this.scaled(this.getHeaderHeight());
        float width = this.getWidth() - this.offset() * 2.0f;
        float height = this.scaled(this.getTypingFieldHeight());
        return new float[]{x, y, width, height};
    }

    private float getElementHeight() {
        return 12.0f;
    }

    private float getHeaderHeight() {
        return 19.0f;
    }

    private float getTypingFieldHeight() {
        return 19.0f;
    }

    private float getHintHeight() {
        return 9.0f;
    }

    @Override
    public void mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
    }

    @Generated
    public List<ThemeSelectable> getThemeSelectables() {
        return this.themeSelectables;
    }

    @Generated
    public List<ThemeBound> getThemeBounds() {
        return this.themeBounds;
    }

    @Generated
    public List<RowDeleteBound> getRowDeleteBounds() {
        return this.rowDeleteBounds;
    }

    @Generated
    public Theme getDefaultTheme() {
        return this.defaultTheme;
    }

    @Generated
    public Theme getCurrentTheme() {
        return this.currentTheme;
    }

    @Generated
    public Theme getEditTheme() {
        return this.editTheme;
    }

    @Generated
    public String getPlaceHolderText() {
        return this.placeHolderText;
    }

    @Generated
    public String getTypingText() {
        return this.typingText;
    }

    @Generated
    public boolean isTyping() {
        return this.typing;
    }

    @Generated
    public DeleteButton getDeleteButton() {
        return this.deleteButton;
    }

    @Generated
    public CreateButton getCreateButton() {
        return this.createButton;
    }

    @Generated
    public BackButton getBackButton() {
        return this.backButton;
    }

    @Generated
    public AnimationUtil getOpenAnimation() {
        return this.openAnimation;
    }

    @Generated
    public boolean isOpen() {
        return this.open;
    }

    @Generated
    public float getAnim() {
        return this.anim;
    }

    @Generated
    public boolean isUserMoved() {
        return this.userMoved;
    }

    @Generated
    public boolean isDragging() {
        return this.dragging;
    }

    @Generated
    public boolean isPressedOnHeader() {
        return this.pressedOnHeader;
    }

    @Generated
    public double getPressMouseX() {
        return this.pressMouseX;
    }

    @Generated
    public double getPressMouseY() {
        return this.pressMouseY;
    }

    @Generated
    public float getDragOffsetX() {
        return this.dragOffsetX;
    }

    @Generated
    public float getDragOffsetY() {
        return this.dragOffsetY;
    }

    @Generated
    public static ThemeEditor getInstance() {
        return instance;
    }

    @Generated
    public void setOpen(boolean open) {
        this.open = open;
    }

    @Generated
    public void setAnim(float anim) {
        this.anim = anim;
    }

    private record DeleteButton(float x, float y, float size) {
    }

    private record CreateButton(float x, float y, float size) {
    }

    private record BackButton(float x, float y, float width, float height) {
    }

    private record ThemeBound(float x, float y, float width, float height, Theme.ElementColor elementColor) {
    }

    private record RowDeleteBound(float x, float y, float size, Theme theme) {
    }
}

