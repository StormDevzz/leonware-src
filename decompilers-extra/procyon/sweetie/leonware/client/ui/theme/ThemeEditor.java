// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.ui.theme;

import java.util.Objects;
import lombok.Generated;
import net.minecraft.class_310;
import java.awt.Color;
import sweetie.leonware.api.utils.render.fonts.Icons;
import sweetie.leonware.api.utils.math.MouseUtil;
import sweetie.leonware.client.ui.clickgui.module.settings.ColorComponent;
import java.util.Iterator;
import net.minecraft.class_4587;
import sweetie.leonware.api.utils.color.ColorUtil;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.api.utils.render.fonts.Fonts;
import sweetie.leonware.api.utils.animation.Easing;
import net.minecraft.class_332;
import sweetie.leonware.api.system.configs.ThemeManager;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.other.WindowResizeEvent;
import java.util.ArrayList;
import sweetie.leonware.api.utils.animation.AnimationUtil;
import java.util.List;
import sweetie.leonware.client.ui.UIComponent;

public class ThemeEditor extends UIComponent
{
    private static final ThemeEditor instance;
    private final List<ThemeSelectable> themeSelectables;
    private final List<ThemeBound> themeBounds;
    private final List<RowDeleteBound> rowDeleteBounds;
    private final Theme defaultTheme;
    private Theme currentTheme;
    protected Theme editTheme;
    private final String placeHolderText = "\u0418\u043c\u044f \u043d\u043e\u0432\u043e\u0439 \u0442\u0435\u043c\u044b";
    private String typingText;
    private boolean typing;
    private DeleteButton deleteButton;
    private CreateButton createButton;
    private BackButton backButton;
    private final AnimationUtil openAnimation;
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
        return (int)(this.openAnimation.getValue() * this.anim * 255.0);
    }
    
    public ThemeEditor() {
        this.themeSelectables = new ArrayList<ThemeSelectable>();
        this.themeBounds = new ArrayList<ThemeBound>();
        this.rowDeleteBounds = new ArrayList<RowDeleteBound>();
        this.defaultTheme = new Theme("LeonWare");
        this.currentTheme = this.defaultTheme;
        this.typingText = "";
        this.deleteButton = new DeleteButton(-1.0f, -1.0f, -1.0f);
        this.createButton = new CreateButton(-1.0f, -1.0f, -1.0f);
        this.backButton = new BackButton(-1.0f, -1.0f, -1.0f, -1.0f);
        this.openAnimation = new AnimationUtil();
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
    
    public void save(final boolean last) {
        if (!last) {
            ThemeManager.getInstance().saveAll();
        }
        else if (this.currentTheme != null) {
            ThemeManager.getInstance().saveLastSelected(this.currentTheme);
        }
    }
    
    public void load() {
        ThemeManager.getInstance().refresh();
        final Theme last = ThemeManager.getInstance().loadLastSelected();
        if (last != null) {
            this.themeSelectables.add(new ThemeSelectable(last));
            this.currentTheme = last;
        }
        else {
            this.themeSelectables.add(new ThemeSelectable(this.defaultTheme));
            this.currentTheme = this.defaultTheme;
        }
    }
    
    @Override
    public void render(final class_332 context, final int mouseX, final int mouseY, final float delta) {
        this.openAnimation.update();
        this.openAnimation.run(this.open ? 1.0 : 0.0, 100L, Easing.SINE_OUT);
        if (this.openAnimation.getValue() <= 0.1) {
            return;
        }
        this.themeBounds.clear();
        this.rowDeleteBounds.clear();
        final float round = this.getWidth() * 0.05f;
        final float headerHeight = this.scaled(this.getHeaderHeight());
        final float headerFontSize = headerHeight * 0.52f;
        final String text = "Theme Editor";
        final float textWidth = Fonts.PS_BOLD.getWidth(text, headerFontSize);
        if (!this.userMoved) {
            this.setWidth(textWidth * 1.5f);
        }
        final class_4587 matrixStack = context.method_51448();
        RenderUtil.BLUR_RECT.draw(matrixStack, this.getX(), this.getY(), this.getWidth(), this.getHeight(), round, UIColors.blur(this.alphaAnim()));
        Fonts.PS_BOLD.drawGradientText(matrixStack, text, this.getX() + this.getWidth() / 2.0f - textWidth / 2.0f, this.getY() + headerHeight / 2.0f - headerFontSize / 2.0f, headerFontSize, UIColors.primary(this.alphaAnim()), UIColors.secondary(this.alphaAnim()), Fonts.PS_BOLD.getWidth(text, headerFontSize) / 4.0f);
        this.placeRender(context, mouseX, mouseY, delta);
        final float xOffset = this.getX() + this.offset();
        final float widthOffset = this.getWidth() - this.offset() * 2.0f;
        float themeY = this.gap() + headerHeight + this.getPlaceTextCoordinates()[3];
        if (this.editTheme == null) {
            for (final ThemeSelectable theme : this.themeSelectables) {
                theme.setAlpha((float)(this.openAnimation.getValue() * this.anim));
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
            themeY += this.scaled(this.getHintHeight()) + this.gap();
            this.setHeight(themeY);
        }
        else {
            float elementY = themeY;
            final float elementHeight = this.scaled(this.getElementHeight());
            final float textSize = elementHeight * 0.6f;
            final float textY = this.getY() + elementHeight / 2.0f - textSize / 2.0f;
            final float elementWidth = this.getWidth() - this.offset();
            final float colorSize = elementHeight * 0.8f;
            final float colorX = this.getX() + elementWidth - colorSize;
            final float colorY = this.getY() + elementHeight / 2.0f - colorSize / 2.0f;
            final float roundColor = colorSize * 0.2f;
            for (final Theme.ElementColor elementColor : this.editTheme.getElementColors()) {
                float height = elementHeight;
                Fonts.PS_MEDIUM.drawText(matrixStack, elementColor.getName(), xOffset, textY + elementY, textSize, UIColors.textColor(this.alphaAnim()));
                RenderUtil.RECT.draw(matrixStack, colorX, colorY + elementY, colorSize, colorSize, roundColor, ColorUtil.setAlpha(elementColor.getColor(), this.alphaAnim()));
                final ColorComponent colorComponent = elementColor.getColorComponent();
                colorComponent.updateOpen();
                final float cAnim = colorComponent.getValue();
                if (cAnim > 0.0) {
                    colorComponent.setX(xOffset);
                    colorComponent.setY(textY + elementY + elementHeight);
                    colorComponent.setWidth(widthOffset);
                    colorComponent.setAlpha(this.alphaAnim() / 255.0f);
                    colorComponent.render(context, mouseX, mouseY, delta);
                    height += colorComponent.getHeight() + this.gap() * 2.0f * cAnim;
                }
                this.themeBounds.add(new ThemeBound(xOffset, colorY + elementY, elementWidth, height, elementColor));
                elementY += height + this.gap();
            }
            this.drawAutoSaveHint(matrixStack, xOffset, this.getY() + elementY, widthOffset);
            elementY += this.scaled(this.getHintHeight()) + this.gap();
            this.setHeight(elementY);
        }
    }
    
    private void drawRowDeleteButton(final class_4587 matrixStack, final ThemeSelectable selectable, final int mouseX, final int mouseY) {
        final float padding = this.gap() * 0.6f;
        final float size = selectable.getHeight() - padding * 2.0f;
        final float x = selectable.getX() + selectable.getWidth() - size - padding;
        final float y = selectable.getY() + padding;
        final boolean hovered = MouseUtil.isHovered((float)mouseX, (float)mouseY, x, y, size, size);
        final float iconSize = size * 0.55f;
        final Color iconColor = hovered ? UIColors.negativeColor(this.alphaAnim()) : UIColors.inactiveTextColor(this.alphaAnim());
        Fonts.ICONS.drawCenteredText(matrixStack, Icons.TRASH.getLetter(), x + size / 2.0f, y + size / 2.0f - iconSize / 2.0f, iconSize, iconColor, 0.1f);
        this.rowDeleteBounds.add(new RowDeleteBound(x, y, size, selectable.getTheme()));
    }
    
    private void drawAutoSaveHint(final class_4587 matrixStack, final float x, final float y, final float width) {
        final float fontSize = this.scaled(this.getHintHeight()) * 0.65f;
        final String text = "\u0421\u043e\u0445\u0440\u0430\u043d\u044f\u0435\u0442\u0441\u044f \u0430\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438";
        final float w = Fonts.PS_MEDIUM.getWidth(text, fontSize);
        final float tx = x + (width - w) / 2.0f;
        final float ty = y + (this.scaled(this.getHintHeight()) - fontSize) / 2.0f;
        Fonts.PS_MEDIUM.drawText(matrixStack, text, tx, ty, fontSize, UIColors.inactiveTextColor((int)(this.alphaAnim() * 0.7f)));
    }
    
    private void placeRender(final class_332 context, final int mouseX, final int mouseY, final float delta) {
        final class_4587 matrixStack = context.method_51448();
        final float[] placeCoords = this.getPlaceTextCoordinates();
        final float placeX = placeCoords[0];
        final float placeY = placeCoords[1];
        final float placeWidth = placeCoords[2];
        final float placeHeight = placeCoords[3];
        final float placeFontSize = placeHeight * 0.4f;
        final float placeRound = placeHeight * 0.2f;
        final boolean edit = this.editTheme != null;
        RenderUtil.BLUR_RECT.draw(matrixStack, placeX, placeY, placeWidth, placeHeight, placeRound, UIColors.widgetBlur(this.alphaAnim()));
        if (edit) {
            final float margin = this.gap();
            final float btnSize = placeHeight - margin * 2.0f;
            final float deleteX = placeX + placeWidth - btnSize - margin;
            final float deleteY = placeY + margin;
            final boolean canDelete = this.themeSelectables.size() > 1;
            final float backLeft = placeX;
            final float backRight = canDelete ? (deleteX - margin) : (placeX + placeWidth);
            final float backWidth = backRight - backLeft;
            final float backHeight = placeHeight;
            final boolean backHovered = MouseUtil.isHovered((float)mouseX, (float)mouseY, backLeft, placeY, backWidth, backHeight);
            final Color textColor = backHovered ? UIColors.primary(this.alphaAnim()) : UIColors.textColor(this.alphaAnim());
            final String backText = "< " + this.editTheme.getName();
            Fonts.PS_BOLD.drawText(matrixStack, backText, placeX + this.offset(), placeY + placeHeight / 2.0f - placeFontSize / 2.0f, placeFontSize, textColor);
            this.backButton = new BackButton(backLeft, placeY, backWidth, backHeight);
            if (canDelete) {
                final float iconSize = btnSize * 0.5f;
                final boolean hovered = MouseUtil.isHovered((float)mouseX, (float)mouseY, deleteX, deleteY, btnSize, btnSize);
                final Color bgColor = hovered ? UIColors.negativeColor(this.alphaAnim()) : UIColors.blur(this.alphaAnim());
                RenderUtil.BLUR_RECT.draw(matrixStack, deleteX, deleteY, btnSize, btnSize, placeRound, bgColor);
                Fonts.ICONS.drawCenteredText(matrixStack, Icons.CROSS.getLetter(), deleteX + btnSize / 2.0f, deleteY + btnSize / 2.0f - iconSize / 2.0f, iconSize, UIColors.textColor(this.alphaAnim()), 0.1f);
                this.deleteButton = new DeleteButton(deleteX, deleteY, btnSize);
            }
            else {
                this.deleteButton = new DeleteButton(-1.0f, -1.0f, -1.0f);
            }
            this.createButton = new CreateButton(-1.0f, -1.0f, -1.0f);
        }
        else {
            final String cursor = (this.typing && System.currentTimeMillis() % 1000L > 500L) ? "_" : " ";
            final String typeText = (this.typingText.isEmpty() && !this.typing) ? "\u0418\u043c\u044f \u043d\u043e\u0432\u043e\u0439 \u0442\u0435\u043c\u044b" : (this.typingText + cursor);
            final Color textColor2 = (this.typingText.isEmpty() && !this.typing) ? UIColors.inactiveTextColor(this.alphaAnim()) : UIColors.textColor(this.alphaAnim());
            Fonts.PS_BOLD.drawText(matrixStack, typeText, placeX + this.offset(), placeY + placeHeight / 2.0f - placeFontSize / 2.0f, placeFontSize, textColor2);
            final float margin2 = this.gap();
            final float btnSize2 = placeHeight - margin2 * 2.0f;
            final float btnX = placeX + placeWidth - btnSize2 - margin2;
            final float btnY = placeY + margin2;
            final boolean canCreate = !this.typingText.isEmpty();
            final boolean hovered2 = MouseUtil.isHovered((float)mouseX, (float)mouseY, btnX, btnY, btnSize2, btnSize2);
            final Color bgColor2 = canCreate ? (hovered2 ? UIColors.primary(this.alphaAnim()) : UIColors.gradient(0, this.alphaAnim())) : UIColors.blur(this.alphaAnim());
            RenderUtil.BLUR_RECT.draw(matrixStack, btnX, btnY, btnSize2, btnSize2, placeRound, bgColor2);
            final float plusFontSize = btnSize2 * 0.55f;
            final Color plusColor = canCreate ? UIColors.textColor(this.alphaAnim()) : UIColors.inactiveTextColor(this.alphaAnim());
            Fonts.PS_BOLD.drawCenteredText(matrixStack, "+", btnX + btnSize2 / 2.0f, btnY + btnSize2 / 2.0f - plusFontSize / 2.0f, plusFontSize, plusColor);
            this.createButton = new CreateButton(btnX, btnY, btnSize2);
            this.deleteButton = new DeleteButton(-1.0f, -1.0f, -1.0f);
            this.backButton = new BackButton(-1.0f, -1.0f, -1.0f, -1.0f);
        }
    }
    
    private boolean tryCreateTheme() {
        if (this.typingText.isEmpty()) {
            return false;
        }
        final boolean exists = this.themeSelectables.stream().anyMatch(ts -> ts.getTheme().getName().equalsIgnoreCase(this.typingText));
        if (exists) {
            this.typingText = "";
            return this.typing = false;
        }
        final Theme newTheme = new Theme(ThemeManager.getInstance().safeFileName(this.typingText));
        newTheme.getElementColors().clear();
        for (final Theme.ElementColor elementColor : this.currentTheme.getElementColors()) {
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
        final String name = this.editTheme.getName();
        final boolean removed = this.themeSelectables.removeIf(ts -> ts.getTheme() == this.editTheme || ts.getTheme().getName().equalsIgnoreCase(name));
        if (removed) {
            try {
                ThemeManager.getInstance().remove(name);
            }
            catch (final Throwable t) {}
            if ((this.currentTheme == this.editTheme || (this.currentTheme != null && this.currentTheme.getName().equalsIgnoreCase(name))) && !this.themeSelectables.isEmpty()) {
                this.currentTheme = this.themeSelectables.get(0).getTheme();
                ThemeManager.getInstance().saveLastSelected(this.currentTheme);
            }
        }
        this.editTheme = null;
    }
    
    @Override
    public void keyPressed(final int keyCode, final int scanCode, final int modifiers) {
        if (!this.open) {
            return;
        }
        if (this.typing && this.editTheme == null) {
            switch (keyCode) {
                case 259: {
                    if (!this.typingText.isEmpty()) {
                        this.typingText = this.typingText.substring(0, this.typingText.length() - 1);
                        break;
                    }
                    break;
                }
                case 257: {
                    this.tryCreateTheme();
                    break;
                }
            }
        }
    }
    
    @Override
    public void mouseClicked(final double mouseX, final double mouseY, final int button) {
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
            if (button == 0 && this.deleteButton.size > 0.0f && MouseUtil.isHovered(mouseX, mouseY, this.deleteButton.x, this.deleteButton.y, this.deleteButton.size, this.deleteButton.size)) {
                this.deleteEditTheme();
                return;
            }
            if (button == 0 && this.backButton.width > 0.0f && MouseUtil.isHovered(mouseX, mouseY, this.backButton.x, this.backButton.y, this.backButton.width, this.backButton.height)) {
                ThemeManager.getInstance().saveAll();
                this.editTheme = null;
                return;
            }
            for (final ThemeBound themeBound : this.themeBounds) {
                final ColorComponent colorComponent = themeBound.elementColor.getColorComponent();
                if (MouseUtil.isHovered(mouseX, mouseY, themeBound.x, themeBound.y, themeBound.width, this.scaled(this.getElementHeight()))) {
                    colorComponent.toggleOpen();
                    return;
                }
                if (!MouseUtil.isHovered(mouseX, mouseY, themeBound.x, themeBound.y, themeBound.width, themeBound.height)) {
                    continue;
                }
                colorComponent.mouseClicked(mouseX, mouseY, button);
            }
        }
        else {
            if (button == 0 && this.createButton.size > 0.0f && MouseUtil.isHovered(mouseX, mouseY, this.createButton.x, this.createButton.y, this.createButton.size, this.createButton.size)) {
                if (this.typingText.isEmpty()) {
                    this.typing = true;
                }
                else {
                    this.tryCreateTheme();
                }
                return;
            }
            if (MouseUtil.isHovered(mouseX, mouseY, this.getPlaceTextCoordinates()[0], this.getPlaceTextCoordinates()[1], this.getPlaceTextCoordinates()[2], this.getPlaceTextCoordinates()[3])) {
                this.typing = !this.typing;
                return;
            }
            if (button == 0 && this.themeSelectables.size() > 1) {
                for (final RowDeleteBound bound : this.rowDeleteBounds) {
                    if (MouseUtil.isHovered(mouseX, mouseY, bound.x, bound.y, bound.size, bound.size)) {
                        final Theme toRemove = bound.theme;
                        final String name = toRemove.getName();
                        final boolean removed = this.themeSelectables.removeIf(ts -> ts.getTheme() == toRemove || ts.getTheme().getName().equalsIgnoreCase(name));
                        if (removed) {
                            try {
                                ThemeManager.getInstance().remove(name);
                            }
                            catch (final Throwable t) {}
                            if ((this.currentTheme == toRemove || (this.currentTheme != null && this.currentTheme.getName().equalsIgnoreCase(name))) && !this.themeSelectables.isEmpty()) {
                                this.currentTheme = this.themeSelectables.get(0).getTheme();
                                ThemeManager.getInstance().saveLastSelected(this.currentTheme);
                            }
                        }
                        return;
                    }
                }
            }
            for (final ThemeSelectable theme : this.themeSelectables) {
                if (MouseUtil.isHovered(mouseX, mouseY, theme.getX(), theme.getY(), theme.getWidth(), theme.getHeight())) {
                    if (button == 1) {
                        this.editTheme = theme.getTheme();
                    }
                    else {
                        if (button != 0) {
                            continue;
                        }
                        this.currentTheme = theme.getTheme();
                        ThemeManager.getInstance().saveLastSelected(this.currentTheme);
                    }
                }
            }
        }
    }
    
    public void mouseDragged(final double mouseX, final double mouseY, final int button, final double deltaX, final double deltaY) {
        if (!this.open) {
            return;
        }
        if (button != 0 || !this.pressedOnHeader) {
            return;
        }
        final double dx = mouseX - this.pressMouseX;
        final double dy = mouseY - this.pressMouseY;
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
    public void mouseReleased(final double mouseX, final double mouseY, final int button) {
        if (!this.open) {
            return;
        }
        if (button == 0 && this.pressedOnHeader) {
            this.pressedOnHeader = false;
            this.dragging = false;
            return;
        }
        if (this.editTheme != null) {
            for (final ThemeBound themeBound : this.themeBounds) {
                themeBound.elementColor.getColorComponent().mouseReleased(mouseX, mouseY, button);
            }
        }
    }
    
    public boolean charTyped(final char chr, final int modifiers) {
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
        this.typingText += chr;
        return true;
    }
    
    private boolean isAllowedChar(final char chr) {
        return (chr >= 'a' && chr <= 'z') || (chr >= 'A' && chr <= 'Z') || (chr >= '0' && chr <= '9') || chr == '_' || chr == '-' || chr == ' ';
    }
    
    private boolean isHoveringHeader(final double mouseX, final double mouseY) {
        return MouseUtil.isHovered(mouseX, mouseY, this.getX(), this.getY(), this.getWidth(), this.scaled(this.getHeaderHeight()));
    }
    
    private void clampToScreen() {
        final class_310 mc = class_310.method_1551();
        if (mc == null || mc.method_22683() == null) {
            return;
        }
        final float margin = this.scaled(3.0f);
        final float screenW = (float)mc.method_22683().method_4486();
        final float screenH = (float)mc.method_22683().method_4502();
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
        final float x = this.getX() + this.offset();
        final float y = this.getY() + this.scaled(this.getHeaderHeight());
        final float width = this.getWidth() - this.offset() * 2.0f;
        final float height = this.scaled(this.getTypingFieldHeight());
        return new float[] { x, y, width, height };
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
    public void mouseScrolled(final double mouseX, final double mouseY, final double horizontalAmount, final double verticalAmount) {
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
        Objects.requireNonNull(this);
        return "\u0418\u043c\u044f \u043d\u043e\u0432\u043e\u0439 \u0442\u0435\u043c\u044b";
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
        return ThemeEditor.instance;
    }
    
    @Generated
    public void setOpen(final boolean open) {
        this.open = open;
    }
    
    @Generated
    public void setAnim(final float anim) {
        this.anim = anim;
    }
    
    static {
        instance = new ThemeEditor();
    }
    
    record ThemeBound(float x, float y, float width, float height, Theme.ElementColor elementColor) {}
    
    record RowDeleteBound(float x, float y, float size, Theme theme) {}
    
    record DeleteButton(float x, float y, float size) {}
    
    record CreateButton(float x, float y, float size) {}
    
    record BackButton(float x, float y, float width, float height) {}
}
