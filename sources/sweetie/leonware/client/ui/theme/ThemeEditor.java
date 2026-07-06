package sweetie.leonware.client.ui.theme;

import java.awt.Color;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Generated;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import org.slf4j.Marker;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.other.WindowResizeEvent;
import sweetie.leonware.api.system.backend.ClientInfo;
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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/ui/theme/ThemeEditor.class */
public class ThemeEditor extends UIComponent {
    private static final ThemeEditor instance = new ThemeEditor();
    protected Theme editTheme;
    private boolean typing;
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
    private final List<ThemeSelectable> themeSelectables = new ArrayList();
    private final List<ThemeBound> themeBounds = new ArrayList();
    private final List<RowDeleteBound> rowDeleteBounds = new ArrayList();
    private final Theme defaultTheme = new Theme(ClientInfo.NAME);
    private Theme currentTheme = this.defaultTheme;
    private final String placeHolderText = "Имя новой темы";
    private String typingText = "";
    private DeleteButton deleteButton = new DeleteButton(-1.0f, -1.0f, -1.0f);
    private CreateButton createButton = new CreateButton(-1.0f, -1.0f, -1.0f);
    private BackButton backButton = new BackButton(-1.0f, -1.0f, -1.0f, -1.0f);
    private final AnimationUtil openAnimation = new AnimationUtil();

    @Generated
    public static ThemeEditor getInstance() {
        return instance;
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
        return "Имя новой темы";
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
    public void setOpen(boolean open) {
        this.open = open;
    }

    @Generated
    public float getAnim() {
        return this.anim;
    }

    @Generated
    public void setAnim(float anim) {
        this.anim = anim;
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

    private int alphaAnim() {
        return (int) (this.openAnimation.getValue() * ((double) this.anim) * 255.0d);
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/ui/theme/ThemeEditor$ThemeBound.class */
    private static final class ThemeBound extends Record {
        private final float x;
        private final float y;
        private final float width;
        private final float height;
        private final Theme.ElementColor elementColor;

        private ThemeBound(float x, float y, float width, float height, Theme.ElementColor elementColor) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.elementColor = elementColor;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, ThemeBound.class), ThemeBound.class, "x;y;width;height;elementColor", "FIELD:Lsweetie/leonware/client/ui/theme/ThemeEditor$ThemeBound;->x:F", "FIELD:Lsweetie/leonware/client/ui/theme/ThemeEditor$ThemeBound;->y:F", "FIELD:Lsweetie/leonware/client/ui/theme/ThemeEditor$ThemeBound;->width:F", "FIELD:Lsweetie/leonware/client/ui/theme/ThemeEditor$ThemeBound;->height:F", "FIELD:Lsweetie/leonware/client/ui/theme/ThemeEditor$ThemeBound;->elementColor:Lsweetie/leonware/client/ui/theme/Theme$ElementColor;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, ThemeBound.class), ThemeBound.class, "x;y;width;height;elementColor", "FIELD:Lsweetie/leonware/client/ui/theme/ThemeEditor$ThemeBound;->x:F", "FIELD:Lsweetie/leonware/client/ui/theme/ThemeEditor$ThemeBound;->y:F", "FIELD:Lsweetie/leonware/client/ui/theme/ThemeEditor$ThemeBound;->width:F", "FIELD:Lsweetie/leonware/client/ui/theme/ThemeEditor$ThemeBound;->height:F", "FIELD:Lsweetie/leonware/client/ui/theme/ThemeEditor$ThemeBound;->elementColor:Lsweetie/leonware/client/ui/theme/Theme$ElementColor;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, ThemeBound.class, Object.class), ThemeBound.class, "x;y;width;height;elementColor", "FIELD:Lsweetie/leonware/client/ui/theme/ThemeEditor$ThemeBound;->x:F", "FIELD:Lsweetie/leonware/client/ui/theme/ThemeEditor$ThemeBound;->y:F", "FIELD:Lsweetie/leonware/client/ui/theme/ThemeEditor$ThemeBound;->width:F", "FIELD:Lsweetie/leonware/client/ui/theme/ThemeEditor$ThemeBound;->height:F", "FIELD:Lsweetie/leonware/client/ui/theme/ThemeEditor$ThemeBound;->elementColor:Lsweetie/leonware/client/ui/theme/Theme$ElementColor;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public float x() {
            return this.x;
        }

        public float y() {
            return this.y;
        }

        public float width() {
            return this.width;
        }

        public float height() {
            return this.height;
        }

        public Theme.ElementColor elementColor() {
            return this.elementColor;
        }
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/ui/theme/ThemeEditor$RowDeleteBound.class */
    private static final class RowDeleteBound extends Record {
        private final float x;
        private final float y;
        private final float size;
        private final Theme theme;

        private RowDeleteBound(float x, float y, float size, Theme theme) {
            this.x = x;
            this.y = y;
            this.size = size;
            this.theme = theme;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, RowDeleteBound.class), RowDeleteBound.class, "x;y;size;theme", "FIELD:Lsweetie/leonware/client/ui/theme/ThemeEditor$RowDeleteBound;->x:F", "FIELD:Lsweetie/leonware/client/ui/theme/ThemeEditor$RowDeleteBound;->y:F", "FIELD:Lsweetie/leonware/client/ui/theme/ThemeEditor$RowDeleteBound;->size:F", "FIELD:Lsweetie/leonware/client/ui/theme/ThemeEditor$RowDeleteBound;->theme:Lsweetie/leonware/client/ui/theme/Theme;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, RowDeleteBound.class), RowDeleteBound.class, "x;y;size;theme", "FIELD:Lsweetie/leonware/client/ui/theme/ThemeEditor$RowDeleteBound;->x:F", "FIELD:Lsweetie/leonware/client/ui/theme/ThemeEditor$RowDeleteBound;->y:F", "FIELD:Lsweetie/leonware/client/ui/theme/ThemeEditor$RowDeleteBound;->size:F", "FIELD:Lsweetie/leonware/client/ui/theme/ThemeEditor$RowDeleteBound;->theme:Lsweetie/leonware/client/ui/theme/Theme;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, RowDeleteBound.class, Object.class), RowDeleteBound.class, "x;y;size;theme", "FIELD:Lsweetie/leonware/client/ui/theme/ThemeEditor$RowDeleteBound;->x:F", "FIELD:Lsweetie/leonware/client/ui/theme/ThemeEditor$RowDeleteBound;->y:F", "FIELD:Lsweetie/leonware/client/ui/theme/ThemeEditor$RowDeleteBound;->size:F", "FIELD:Lsweetie/leonware/client/ui/theme/ThemeEditor$RowDeleteBound;->theme:Lsweetie/leonware/client/ui/theme/Theme;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public float x() {
            return this.x;
        }

        public float y() {
            return this.y;
        }

        public float size() {
            return this.size;
        }

        public Theme theme() {
            return this.theme;
        }
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/ui/theme/ThemeEditor$DeleteButton.class */
    private static final class DeleteButton extends Record {
        private final float x;
        private final float y;
        private final float size;

        private DeleteButton(float x, float y, float size) {
            this.x = x;
            this.y = y;
            this.size = size;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, DeleteButton.class), DeleteButton.class, "x;y;size", "FIELD:Lsweetie/leonware/client/ui/theme/ThemeEditor$DeleteButton;->x:F", "FIELD:Lsweetie/leonware/client/ui/theme/ThemeEditor$DeleteButton;->y:F", "FIELD:Lsweetie/leonware/client/ui/theme/ThemeEditor$DeleteButton;->size:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, DeleteButton.class), DeleteButton.class, "x;y;size", "FIELD:Lsweetie/leonware/client/ui/theme/ThemeEditor$DeleteButton;->x:F", "FIELD:Lsweetie/leonware/client/ui/theme/ThemeEditor$DeleteButton;->y:F", "FIELD:Lsweetie/leonware/client/ui/theme/ThemeEditor$DeleteButton;->size:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, DeleteButton.class, Object.class), DeleteButton.class, "x;y;size", "FIELD:Lsweetie/leonware/client/ui/theme/ThemeEditor$DeleteButton;->x:F", "FIELD:Lsweetie/leonware/client/ui/theme/ThemeEditor$DeleteButton;->y:F", "FIELD:Lsweetie/leonware/client/ui/theme/ThemeEditor$DeleteButton;->size:F").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public float x() {
            return this.x;
        }

        public float y() {
            return this.y;
        }

        public float size() {
            return this.size;
        }
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/ui/theme/ThemeEditor$CreateButton.class */
    private static final class CreateButton extends Record {
        private final float x;
        private final float y;
        private final float size;

        private CreateButton(float x, float y, float size) {
            this.x = x;
            this.y = y;
            this.size = size;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, CreateButton.class), CreateButton.class, "x;y;size", "FIELD:Lsweetie/leonware/client/ui/theme/ThemeEditor$CreateButton;->x:F", "FIELD:Lsweetie/leonware/client/ui/theme/ThemeEditor$CreateButton;->y:F", "FIELD:Lsweetie/leonware/client/ui/theme/ThemeEditor$CreateButton;->size:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, CreateButton.class), CreateButton.class, "x;y;size", "FIELD:Lsweetie/leonware/client/ui/theme/ThemeEditor$CreateButton;->x:F", "FIELD:Lsweetie/leonware/client/ui/theme/ThemeEditor$CreateButton;->y:F", "FIELD:Lsweetie/leonware/client/ui/theme/ThemeEditor$CreateButton;->size:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, CreateButton.class, Object.class), CreateButton.class, "x;y;size", "FIELD:Lsweetie/leonware/client/ui/theme/ThemeEditor$CreateButton;->x:F", "FIELD:Lsweetie/leonware/client/ui/theme/ThemeEditor$CreateButton;->y:F", "FIELD:Lsweetie/leonware/client/ui/theme/ThemeEditor$CreateButton;->size:F").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public float x() {
            return this.x;
        }

        public float y() {
            return this.y;
        }

        public float size() {
            return this.size;
        }
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/ui/theme/ThemeEditor$BackButton.class */
    private static final class BackButton extends Record {
        private final float x;
        private final float y;
        private final float width;
        private final float height;

        private BackButton(float x, float y, float width, float height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, BackButton.class), BackButton.class, "x;y;width;height", "FIELD:Lsweetie/leonware/client/ui/theme/ThemeEditor$BackButton;->x:F", "FIELD:Lsweetie/leonware/client/ui/theme/ThemeEditor$BackButton;->y:F", "FIELD:Lsweetie/leonware/client/ui/theme/ThemeEditor$BackButton;->width:F", "FIELD:Lsweetie/leonware/client/ui/theme/ThemeEditor$BackButton;->height:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, BackButton.class), BackButton.class, "x;y;width;height", "FIELD:Lsweetie/leonware/client/ui/theme/ThemeEditor$BackButton;->x:F", "FIELD:Lsweetie/leonware/client/ui/theme/ThemeEditor$BackButton;->y:F", "FIELD:Lsweetie/leonware/client/ui/theme/ThemeEditor$BackButton;->width:F", "FIELD:Lsweetie/leonware/client/ui/theme/ThemeEditor$BackButton;->height:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, BackButton.class, Object.class), BackButton.class, "x;y;width;height", "FIELD:Lsweetie/leonware/client/ui/theme/ThemeEditor$BackButton;->x:F", "FIELD:Lsweetie/leonware/client/ui/theme/ThemeEditor$BackButton;->y:F", "FIELD:Lsweetie/leonware/client/ui/theme/ThemeEditor$BackButton;->width:F", "FIELD:Lsweetie/leonware/client/ui/theme/ThemeEditor$BackButton;->height:F").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public float x() {
            return this.x;
        }

        public float y() {
            return this.y;
        }

        public float width() {
            return this.width;
        }

        public float height() {
            return this.height;
        }
    }

    public ThemeEditor() {
        setWidth(scaled(95.0f));
        setHeight(scaled(150.0f));
        WindowResizeEvent.getInstance().subscribe(new Listener(-1, event -> {
            setWidth(scaled(95.0f));
            if (this.userMoved) {
                clampToScreen();
            }
        }));
    }

    public void init() {
        refresh();
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

    @Override // sweetie.leonware.api.system.interfaces.UIApi
    public void render(class_332 context, int mouseX, int mouseY, float delta) {
        this.openAnimation.update();
        this.openAnimation.run(this.open ? 1.0d : 0.0d, 100L, Easing.SINE_OUT);
        if (this.openAnimation.getValue() <= 0.1d) {
            return;
        }
        this.themeBounds.clear();
        this.rowDeleteBounds.clear();
        float round = getWidth() * 0.05f;
        float headerHeight = scaled(getHeaderHeight());
        float headerFontSize = headerHeight * 0.52f;
        float textWidth = Fonts.PS_BOLD.getWidth("Theme Editor", headerFontSize);
        if (!this.userMoved) {
            setWidth(textWidth * 1.5f);
        }
        class_4587 matrixStack = context.method_51448();
        RenderUtil.BLUR_RECT.draw(matrixStack, getX(), getY(), getWidth(), getHeight(), round, UIColors.blur(alphaAnim()));
        Fonts.PS_BOLD.drawGradientText(matrixStack, "Theme Editor", (getX() + (getWidth() / 2.0f)) - (textWidth / 2.0f), (getY() + (headerHeight / 2.0f)) - (headerFontSize / 2.0f), headerFontSize, UIColors.primary(alphaAnim()), UIColors.secondary(alphaAnim()), Fonts.PS_BOLD.getWidth("Theme Editor", headerFontSize) / 4.0f);
        placeRender(context, mouseX, mouseY, delta);
        float xOffset = getX() + offset();
        float widthOffset = getWidth() - (offset() * 2.0f);
        float themeY = gap() + headerHeight + getPlaceTextCoordinates()[3];
        if (this.editTheme == null) {
            for (ThemeSelectable theme : this.themeSelectables) {
                theme.setAlpha((float) (this.openAnimation.getValue() * ((double) this.anim)));
                theme.setX(xOffset);
                theme.setY(getY() + themeY);
                theme.setWidth(widthOffset);
                theme.setHeight(scaled(17.0f));
                theme.render(context, mouseX, mouseY, delta);
                if (this.themeSelectables.size() > 1) {
                    drawRowDeleteButton(matrixStack, theme, mouseX, mouseY);
                }
                themeY += theme.getHeight() + gap();
            }
            drawAutoSaveHint(matrixStack, xOffset, getY() + themeY, widthOffset);
            setHeight(themeY + scaled(getHintHeight()) + gap());
            return;
        }
        float elementY = themeY;
        float elementHeight = scaled(getElementHeight());
        float textSize = elementHeight * 0.6f;
        float textY = (getY() + (elementHeight / 2.0f)) - (textSize / 2.0f);
        float elementWidth = getWidth() - offset();
        float colorSize = elementHeight * 0.8f;
        float colorX = (getX() + elementWidth) - colorSize;
        float colorY = (getY() + (elementHeight / 2.0f)) - (colorSize / 2.0f);
        float roundColor = colorSize * 0.2f;
        for (Theme.ElementColor elementColor : this.editTheme.getElementColors()) {
            float height = elementHeight;
            Fonts.PS_MEDIUM.drawText(matrixStack, elementColor.getName(), xOffset, textY + elementY, textSize, UIColors.textColor(alphaAnim()));
            RenderUtil.RECT.draw(matrixStack, colorX, colorY + elementY, colorSize, colorSize, roundColor, ColorUtil.setAlpha(elementColor.getColor(), alphaAnim()));
            ColorComponent colorComponent = elementColor.getColorComponent();
            colorComponent.updateOpen();
            float cAnim = colorComponent.getValue();
            if (cAnim > 0.0d) {
                colorComponent.setX(xOffset);
                colorComponent.setY(textY + elementY + elementHeight);
                colorComponent.setWidth(widthOffset);
                colorComponent.setAlpha(alphaAnim() / 255.0f);
                colorComponent.render(context, mouseX, mouseY, delta);
                height += colorComponent.getHeight() + (gap() * 2.0f * cAnim);
            }
            this.themeBounds.add(new ThemeBound(xOffset, colorY + elementY, elementWidth, height, elementColor));
            elementY += height + gap();
        }
        drawAutoSaveHint(matrixStack, xOffset, getY() + elementY, widthOffset);
        setHeight(elementY + scaled(getHintHeight()) + gap());
    }

    private void drawRowDeleteButton(class_4587 matrixStack, ThemeSelectable selectable, int mouseX, int mouseY) {
        float padding = gap() * 0.6f;
        float size = selectable.getHeight() - (padding * 2.0f);
        float x = ((selectable.getX() + selectable.getWidth()) - size) - padding;
        float y = selectable.getY() + padding;
        boolean hovered = MouseUtil.isHovered(mouseX, mouseY, x, y, size, size);
        float iconSize = size * 0.55f;
        Color iconColor = hovered ? UIColors.negativeColor(alphaAnim()) : UIColors.inactiveTextColor(alphaAnim());
        Fonts.ICONS.drawCenteredText(matrixStack, Icons.TRASH.getLetter(), x + (size / 2.0f), (y + (size / 2.0f)) - (iconSize / 2.0f), iconSize, iconColor, 0.1f);
        this.rowDeleteBounds.add(new RowDeleteBound(x, y, size, selectable.getTheme()));
    }

    private void drawAutoSaveHint(class_4587 matrixStack, float x, float y, float width) {
        float fontSize = scaled(getHintHeight()) * 0.65f;
        float w = Fonts.PS_MEDIUM.getWidth("Сохраняется автоматически", fontSize);
        float tx = x + ((width - w) / 2.0f);
        float ty = y + ((scaled(getHintHeight()) - fontSize) / 2.0f);
        Fonts.PS_MEDIUM.drawText(matrixStack, "Сохраняется автоматически", tx, ty, fontSize, UIColors.inactiveTextColor((int) (alphaAnim() * 0.7f)));
    }

    private void placeRender(class_332 context, int mouseX, int mouseY, float delta) {
        Color colorTextColor;
        Color colorBlur;
        class_4587 matrixStack = context.method_51448();
        float[] placeCoords = getPlaceTextCoordinates();
        float placeX = placeCoords[0];
        float placeY = placeCoords[1];
        float placeWidth = placeCoords[2];
        float placeHeight = placeCoords[3];
        float placeFontSize = placeHeight * 0.4f;
        float placeRound = placeHeight * 0.2f;
        boolean edit = this.editTheme != null;
        RenderUtil.BLUR_RECT.draw(matrixStack, placeX, placeY, placeWidth, placeHeight, placeRound, UIColors.widgetBlur(alphaAnim()));
        if (edit) {
            float margin = gap();
            float btnSize = placeHeight - (margin * 2.0f);
            float deleteX = ((placeX + placeWidth) - btnSize) - margin;
            float deleteY = placeY + margin;
            boolean canDelete = this.themeSelectables.size() > 1;
            float backRight = canDelete ? deleteX - margin : placeX + placeWidth;
            float backWidth = backRight - placeX;
            boolean backHovered = MouseUtil.isHovered(mouseX, mouseY, placeX, placeY, backWidth, placeHeight);
            Color textColor = backHovered ? UIColors.primary(alphaAnim()) : UIColors.textColor(alphaAnim());
            String backText = "< " + this.editTheme.getName();
            Fonts.PS_BOLD.drawText(matrixStack, backText, placeX + offset(), (placeY + (placeHeight / 2.0f)) - (placeFontSize / 2.0f), placeFontSize, textColor);
            this.backButton = new BackButton(placeX, placeY, backWidth, placeHeight);
            if (canDelete) {
                float iconSize = btnSize * 0.5f;
                boolean hovered = MouseUtil.isHovered(mouseX, mouseY, deleteX, deleteY, btnSize, btnSize);
                Color bgColor = hovered ? UIColors.negativeColor(alphaAnim()) : UIColors.blur(alphaAnim());
                RenderUtil.BLUR_RECT.draw(matrixStack, deleteX, deleteY, btnSize, btnSize, placeRound, bgColor);
                Fonts.ICONS.drawCenteredText(matrixStack, Icons.CROSS.getLetter(), deleteX + (btnSize / 2.0f), (deleteY + (btnSize / 2.0f)) - (iconSize / 2.0f), iconSize, UIColors.textColor(alphaAnim()), 0.1f);
                this.deleteButton = new DeleteButton(deleteX, deleteY, btnSize);
            } else {
                this.deleteButton = new DeleteButton(-1.0f, -1.0f, -1.0f);
            }
            this.createButton = new CreateButton(-1.0f, -1.0f, -1.0f);
            return;
        }
        String cursor = (!this.typing || System.currentTimeMillis() % 1000 <= 500) ? " " : "_";
        String typeText = (!this.typingText.isEmpty() || this.typing) ? this.typingText + cursor : "Имя новой темы";
        if (this.typingText.isEmpty() && !this.typing) {
            colorTextColor = UIColors.inactiveTextColor(alphaAnim());
        } else {
            colorTextColor = UIColors.textColor(alphaAnim());
        }
        Color textColor2 = colorTextColor;
        Fonts.PS_BOLD.drawText(matrixStack, typeText, placeX + offset(), (placeY + (placeHeight / 2.0f)) - (placeFontSize / 2.0f), placeFontSize, textColor2);
        float margin2 = gap();
        float btnSize2 = placeHeight - (margin2 * 2.0f);
        float btnX = ((placeX + placeWidth) - btnSize2) - margin2;
        float btnY = placeY + margin2;
        boolean canCreate = !this.typingText.isEmpty();
        boolean hovered2 = MouseUtil.isHovered(mouseX, mouseY, btnX, btnY, btnSize2, btnSize2);
        if (canCreate) {
            colorBlur = hovered2 ? UIColors.primary(alphaAnim()) : UIColors.gradient(0, alphaAnim());
        } else {
            colorBlur = UIColors.blur(alphaAnim());
        }
        Color bgColor2 = colorBlur;
        RenderUtil.BLUR_RECT.draw(matrixStack, btnX, btnY, btnSize2, btnSize2, placeRound, bgColor2);
        float plusFontSize = btnSize2 * 0.55f;
        Color plusColor = canCreate ? UIColors.textColor(alphaAnim()) : UIColors.inactiveTextColor(alphaAnim());
        Fonts.PS_BOLD.drawCenteredText(matrixStack, Marker.ANY_NON_NULL_MARKER, btnX + (btnSize2 / 2.0f), (btnY + (btnSize2 / 2.0f)) - (plusFontSize / 2.0f), plusFontSize, plusColor);
        this.createButton = new CreateButton(btnX, btnY, btnSize2);
        this.deleteButton = new DeleteButton(-1.0f, -1.0f, -1.0f);
        this.backButton = new BackButton(-1.0f, -1.0f, -1.0f, -1.0f);
    }

    private boolean tryCreateTheme() {
        if (this.typingText.isEmpty()) {
            return false;
        }
        boolean exists = this.themeSelectables.stream().anyMatch(ts -> {
            return ts.getTheme().getName().equalsIgnoreCase(this.typingText);
        });
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
        if (this.editTheme != null && this.themeSelectables.size() > 1) {
            String name = this.editTheme.getName();
            boolean removed = this.themeSelectables.removeIf(ts -> {
                return ts.getTheme() == this.editTheme || ts.getTheme().getName().equalsIgnoreCase(name);
            });
            if (removed) {
                try {
                    ThemeManager.getInstance().remove(name);
                } catch (Throwable th) {
                }
                if ((this.currentTheme == this.editTheme || (this.currentTheme != null && this.currentTheme.getName().equalsIgnoreCase(name))) && !this.themeSelectables.isEmpty()) {
                    this.currentTheme = this.themeSelectables.get(0).getTheme();
                    ThemeManager.getInstance().saveLastSelected(this.currentTheme);
                }
            }
            this.editTheme = null;
        }
    }

    @Override // sweetie.leonware.api.system.interfaces.UIApi
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.open && this.typing && this.editTheme == null) {
            switch (keyCode) {
                case 257:
                    tryCreateTheme();
                    break;
                case 259:
                    if (!this.typingText.isEmpty()) {
                        this.typingText = this.typingText.substring(0, this.typingText.length() - 1);
                    }
                    break;
            }
        }
    }

    @Override // sweetie.leonware.api.system.interfaces.UIApi
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (this.open) {
            if (button == 0 && isHoveringHeader(mouseX, mouseY)) {
                this.pressedOnHeader = true;
                this.pressMouseX = mouseX;
                this.pressMouseY = mouseY;
                this.dragOffsetX = ((float) mouseX) - getX();
                this.dragOffsetY = ((float) mouseY) - getY();
                return;
            }
            if (this.editTheme != null) {
                if (button == 0 && this.deleteButton.size > 0.0f && MouseUtil.isHovered(mouseX, mouseY, this.deleteButton.x, this.deleteButton.y, this.deleteButton.size, this.deleteButton.size)) {
                    deleteEditTheme();
                    return;
                }
                if (button == 0 && this.backButton.width > 0.0f && MouseUtil.isHovered(mouseX, mouseY, this.backButton.x, this.backButton.y, this.backButton.width, this.backButton.height)) {
                    ThemeManager.getInstance().saveAll();
                    this.editTheme = null;
                    return;
                }
                for (ThemeBound themeBound : this.themeBounds) {
                    ColorComponent colorComponent = themeBound.elementColor.getColorComponent();
                    if (MouseUtil.isHovered(mouseX, mouseY, themeBound.x, themeBound.y, themeBound.width, scaled(getElementHeight()))) {
                        colorComponent.toggleOpen();
                        return;
                    } else if (MouseUtil.isHovered(mouseX, mouseY, themeBound.x, themeBound.y, themeBound.width, themeBound.height)) {
                        colorComponent.mouseClicked(mouseX, mouseY, button);
                    }
                }
                return;
            }
            if (button == 0 && this.createButton.size > 0.0f && MouseUtil.isHovered(mouseX, mouseY, this.createButton.x, this.createButton.y, this.createButton.size, this.createButton.size)) {
                if (this.typingText.isEmpty()) {
                    this.typing = true;
                    return;
                } else {
                    tryCreateTheme();
                    return;
                }
            }
            if (MouseUtil.isHovered(mouseX, mouseY, getPlaceTextCoordinates()[0], getPlaceTextCoordinates()[1], getPlaceTextCoordinates()[2], getPlaceTextCoordinates()[3])) {
                this.typing = !this.typing;
                return;
            }
            if (button == 0 && this.themeSelectables.size() > 1) {
                for (RowDeleteBound bound : this.rowDeleteBounds) {
                    if (MouseUtil.isHovered(mouseX, mouseY, bound.x, bound.y, bound.size, bound.size)) {
                        Theme toRemove = bound.theme;
                        String name = toRemove.getName();
                        boolean removed = this.themeSelectables.removeIf(ts -> {
                            return ts.getTheme() == toRemove || ts.getTheme().getName().equalsIgnoreCase(name);
                        });
                        if (removed) {
                            try {
                                ThemeManager.getInstance().remove(name);
                            } catch (Throwable th) {
                            }
                            if ((this.currentTheme == toRemove || (this.currentTheme != null && this.currentTheme.getName().equalsIgnoreCase(name))) && !this.themeSelectables.isEmpty()) {
                                this.currentTheme = this.themeSelectables.get(0).getTheme();
                                ThemeManager.getInstance().saveLastSelected(this.currentTheme);
                                return;
                            }
                            return;
                        }
                        return;
                    }
                }
            }
            for (ThemeSelectable theme : this.themeSelectables) {
                if (MouseUtil.isHovered(mouseX, mouseY, theme.getX(), theme.getY(), theme.getWidth(), theme.getHeight())) {
                    if (button == 1) {
                        this.editTheme = theme.getTheme();
                    } else if (button == 0) {
                        this.currentTheme = theme.getTheme();
                        ThemeManager.getInstance().saveLastSelected(this.currentTheme);
                    }
                }
            }
        }
    }

    public void mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.open && button == 0 && this.pressedOnHeader) {
            double dx = mouseX - this.pressMouseX;
            double dy = mouseY - this.pressMouseY;
            if (!this.dragging && (dx * dx) + (dy * dy) >= 9.0d) {
                this.dragging = true;
                this.userMoved = true;
            }
            if (this.dragging) {
                setX(((float) mouseX) - this.dragOffsetX);
                setY(((float) mouseY) - this.dragOffsetY);
                clampToScreen();
            }
        }
    }

    @Override // sweetie.leonware.api.system.interfaces.UIApi
    public void mouseReleased(double mouseX, double mouseY, int button) {
        if (this.open) {
            if (button == 0 && this.pressedOnHeader) {
                this.pressedOnHeader = false;
                this.dragging = false;
            } else if (this.editTheme != null) {
                for (ThemeBound themeBound : this.themeBounds) {
                    themeBound.elementColor.getColorComponent().mouseReleased(mouseX, mouseY, button);
                }
            }
        }
    }

    public boolean charTyped(char chr, int modifiers) {
        if (!this.open || this.editTheme != null || !this.typing || this.typingText.length() >= 16 || !isAllowedChar(chr)) {
            return false;
        }
        this.typingText += chr;
        return true;
    }

    private boolean isAllowedChar(char chr) {
        if (chr >= 'a' && chr <= 'z') {
            return true;
        }
        if (chr < 'A' || chr > 'Z') {
            return (chr >= '0' && chr <= '9') || chr == '_' || chr == '-' || chr == ' ';
        }
        return true;
    }

    private boolean isHoveringHeader(double mouseX, double mouseY) {
        return MouseUtil.isHovered(mouseX, mouseY, getX(), getY(), getWidth(), scaled(getHeaderHeight()));
    }

    private void clampToScreen() {
        class_310 mc = class_310.method_1551();
        if (mc == null || mc.method_22683() == null) {
            return;
        }
        float margin = scaled(DRAG_THRESHOLD);
        float screenW = mc.method_22683().method_4486();
        float screenH = mc.method_22683().method_4502();
        float x = getX();
        float y = getY();
        float w = getWidth();
        float h = getHeight();
        if (w > screenW - (margin * 2.0f)) {
            w = screenW - (margin * 2.0f);
        }
        if (h > screenH - (margin * 2.0f)) {
            h = screenH - (margin * 2.0f);
        }
        if (x < margin) {
            x = margin;
        }
        if (y < margin) {
            y = margin;
        }
        if (x + w > screenW - margin) {
            x = (screenW - w) - margin;
        }
        if (y + h > screenH - margin) {
            y = (screenH - h) - margin;
        }
        setX(x);
        setY(y);
    }

    private float[] getPlaceTextCoordinates() {
        float x = getX() + offset();
        float y = getY() + scaled(getHeaderHeight());
        float width = getWidth() - (offset() * 2.0f);
        float height = scaled(getTypingFieldHeight());
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

    @Override // sweetie.leonware.api.system.interfaces.UIApi
    public void mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
    }
}
