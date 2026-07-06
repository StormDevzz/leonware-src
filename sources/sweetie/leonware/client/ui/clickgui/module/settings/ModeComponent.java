package sweetie.leonware.client.ui.clickgui.module.settings;

import java.awt.Color;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.utils.animation.AnimationUtil;
import sweetie.leonware.api.utils.animation.Easing;
import sweetie.leonware.api.utils.color.ColorUtil;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.math.MouseUtil;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.api.utils.render.ScissorUtil;
import sweetie.leonware.api.utils.render.fonts.Fonts;
import sweetie.leonware.client.ui.clickgui.module.ExpandableComponent;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/ui/clickgui/module/settings/ModeComponent.class */
public class ModeComponent extends ExpandableComponent.ExpandableSettingComponent {
    private final ModeSetting setting;
    private final List<Bound> bounds;
    private final Map<String, AnimationUtil> modeAnimations;

    public ModeComponent(ModeSetting setting) {
        super(setting);
        this.bounds = new ArrayList();
        this.modeAnimations = new HashMap();
        this.setting = setting;
        updateHeight(getDefaultHeight());
        for (String mode : setting.getModes()) {
            this.modeAnimations.put(mode, new AnimationUtil());
        }
    }

    @Override // sweetie.leonware.api.system.interfaces.UIApi
    public void render(class_332 context, int mouseX, int mouseY, float delta) {
        class_4587 matrixStack = context.method_51448();
        updateOpen();
        float fontSize = getDefaultHeight() * scaled(0.45f);
        float scd = scaled(getDefaultHeight());
        float zavoz = offset();
        float anim = getValue();
        String valueText = this.setting.getValue();
        String name = this.setting.getName();
        float valueWidth = Fonts.PS_MEDIUM.getWidth(valueText, fontSize);
        float nameWidth = Fonts.PS_BOLD.getWidth(name, fontSize);
        float nameX = (((getWidth() / 2.0f) - (nameWidth / 2.0f)) * anim) + (zavoz * (1.0f - anim));
        int fullAlpha = (int) (getAlpha() * 255.0f);
        RenderUtil.BLUR_RECT.draw(matrixStack, getX(), getY(), getWidth(), getHeight(), getWidth() * 0.04f, UIColors.backgroundBlur(fullAlpha));
        Fonts.PS_BOLD.drawWrap(matrixStack, name, getX() + nameX, (getY() + (scd / 2.0f)) - (fontSize / 2.0f), (getWidth() - (zavoz * 2.0f)) - (valueWidth * (1.0f - anim)), fontSize, UIColors.textColor(fullAlpha), scaled(16.0f), Duration.ofMillis(3000L), Duration.ofMillis(500L));
        ScissorUtil.start(matrixStack, getX(), getY(), getWidth(), getHeight());
        Fonts.PS_MEDIUM.drawText(matrixStack, valueText, ((getX() + getWidth()) - zavoz) - (valueWidth * (1.0f - anim)), (getY() + (scd / 2.0f)) - (fontSize / 2.0f), fontSize, UIColors.primary((int) ((1.0f - anim) * getAlpha() * 255.0f)));
        if (anim > 0.0d) {
            float bY = (-scaled(2.0f)) * (1.0f - anim);
            this.bounds.clear();
            float defX = getX() + zavoz;
            float currentX = defX;
            float currentY = getY() + scd + bY;
            float tileSize = fontSize * 0.9f;
            float tileHeight = tileSize * 1.8f;
            float tilePadding = gap();
            int fullAlpha2 = (int) (getAlpha() * anim * 255.0f);
            RenderUtil.OTHER.scaleStart(matrixStack, getX() + (getWidth() / 2.0f), ((getY() + getDefaultHeight()) + (getHeight() / 2.0f)) - bY, 0.95f + (0.05f * anim));
            for (String mode : this.setting.getModes()) {
                AnimationUtil modeAnim = this.modeAnimations.get(mode);
                modeAnim.update();
                modeAnim.run(this.setting.is(mode) ? 1.0d : 0.0d, 500L, Easing.EXPO_OUT);
                float textWidth = Fonts.PS_MEDIUM.getWidth(mode, tileSize);
                float tileWidth = textWidth + tileSize;
                if (currentX + tileWidth + tilePadding > getX() + getWidth()) {
                    currentX = defX;
                    currentY += tileHeight + tilePadding;
                }
                this.bounds.add(new Bound(currentX, currentY, tileWidth, tileHeight, mode));
                Color rectColor = ColorUtil.setAlpha(ColorUtil.interpolate(UIColors.primary(), UIColors.widgetBlur(), modeAnim.getValue()), fullAlpha2);
                RenderUtil.BLUR_RECT.draw(matrixStack, currentX, currentY, tileWidth, tileHeight, tileHeight * 0.2f, rectColor);
                Fonts.PS_MEDIUM.drawCenteredText(matrixStack, mode, currentX + (tileWidth / 2.0f), (currentY + (tileHeight / 2.0f)) - (tileSize / 2.0f), tileSize, UIColors.textColor(fullAlpha2));
                currentX += tileWidth + tilePadding;
            }
            RenderUtil.OTHER.scaleStop(matrixStack);
            float total = ((currentY - getY()) + tileHeight) * anim;
            float impotentMan = Math.max(total, scd + (tileHeight * anim));
            float jopa = gap() * anim * 2.0f;
            setHeight(impotentMan + jopa);
        } else {
            updateHeight(getDefaultHeight());
        }
        ScissorUtil.stop(matrixStack);
    }

    @Override // sweetie.leonware.api.system.interfaces.UIApi
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (MouseUtil.isHovered(mouseX, mouseY, getX(), getY(), getWidth(), scaled(getDefaultHeight()))) {
            toggleOpen();
            return;
        }
        if (isNotOver()) {
            return;
        }
        for (Bound bound : this.bounds) {
            if (MouseUtil.isHovered(mouseX, mouseY, bound.x, bound.y, bound.width, bound.height)) {
                this.setting.setValue(bound.value);
            }
        }
    }

    private float getDefaultHeight() {
        return 15.0f;
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/ui/clickgui/module/settings/ModeComponent$Bound.class */
    private static final class Bound extends Record {
        private final float x;
        private final float y;
        private final float width;
        private final float height;
        private final String value;

        private Bound(float x, float y, float width, float height, String value) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.value = value;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Bound.class), Bound.class, "x;y;width;height;value", "FIELD:Lsweetie/leonware/client/ui/clickgui/module/settings/ModeComponent$Bound;->x:F", "FIELD:Lsweetie/leonware/client/ui/clickgui/module/settings/ModeComponent$Bound;->y:F", "FIELD:Lsweetie/leonware/client/ui/clickgui/module/settings/ModeComponent$Bound;->width:F", "FIELD:Lsweetie/leonware/client/ui/clickgui/module/settings/ModeComponent$Bound;->height:F", "FIELD:Lsweetie/leonware/client/ui/clickgui/module/settings/ModeComponent$Bound;->value:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Bound.class), Bound.class, "x;y;width;height;value", "FIELD:Lsweetie/leonware/client/ui/clickgui/module/settings/ModeComponent$Bound;->x:F", "FIELD:Lsweetie/leonware/client/ui/clickgui/module/settings/ModeComponent$Bound;->y:F", "FIELD:Lsweetie/leonware/client/ui/clickgui/module/settings/ModeComponent$Bound;->width:F", "FIELD:Lsweetie/leonware/client/ui/clickgui/module/settings/ModeComponent$Bound;->height:F", "FIELD:Lsweetie/leonware/client/ui/clickgui/module/settings/ModeComponent$Bound;->value:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Bound.class, Object.class), Bound.class, "x;y;width;height;value", "FIELD:Lsweetie/leonware/client/ui/clickgui/module/settings/ModeComponent$Bound;->x:F", "FIELD:Lsweetie/leonware/client/ui/clickgui/module/settings/ModeComponent$Bound;->y:F", "FIELD:Lsweetie/leonware/client/ui/clickgui/module/settings/ModeComponent$Bound;->width:F", "FIELD:Lsweetie/leonware/client/ui/clickgui/module/settings/ModeComponent$Bound;->height:F", "FIELD:Lsweetie/leonware/client/ui/clickgui/module/settings/ModeComponent$Bound;->value:Ljava/lang/String;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
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

        public String value() {
            return this.value;
        }
    }

    @Override // sweetie.leonware.api.system.interfaces.UIApi
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
    }

    @Override // sweetie.leonware.api.system.interfaces.UIApi
    public void mouseReleased(double mouseX, double mouseY, int button) {
    }

    @Override // sweetie.leonware.api.system.interfaces.UIApi
    public void mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
    }
}
