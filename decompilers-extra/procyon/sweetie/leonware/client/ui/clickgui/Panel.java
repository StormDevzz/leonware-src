// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.ui.clickgui;

import lombok.Generated;
import sweetie.leonware.client.ui.clickgui.module.SettingComponent;
import net.minecraft.class_4587;
import sweetie.leonware.api.utils.render.fonts.Fonts;
import sweetie.leonware.api.utils.color.UIColors;
import org.joml.Vector4f;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.api.utils.animation.Easing;
import sweetie.leonware.api.utils.math.MouseUtil;
import net.minecraft.class_332;
import java.util.Iterator;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.other.WindowResizeEvent;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleManager;
import java.util.ArrayList;
import sweetie.leonware.api.utils.animation.AnimationUtil;
import sweetie.leonware.client.ui.clickgui.module.ModuleComponent;
import java.util.List;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.client.ui.UIComponent;

public class Panel extends UIComponent
{
    private final Category category;
    private final List<ModuleComponent> moduleComponents;
    private int categoryIndex;
    private double scroll;
    private final AnimationUtil scrollAnimation;
    
    public Panel(final Category category) {
        this.moduleComponents = new ArrayList<ModuleComponent>();
        this.scroll = 0.0;
        this.scrollAnimation = new AnimationUtil();
        this.category = category;
        for (final Module module : ModuleManager.getInstance().getModules()) {
            if (module.getCategory() == category) {
                final ModuleComponent moduleComponent = new ModuleComponent(module);
                moduleComponent.setRound(this.getRound() * 2.0f);
                this.moduleComponents.add(moduleComponent);
            }
        }
        if (!this.moduleComponents.isEmpty()) {
            this.moduleComponents.getLast().setLast(true);
        }
        int index = this.categoryIndex;
        for (final ModuleComponent module2 : this.moduleComponents) {
            module2.setIndex(index);
            index += 45;
        }
        WindowResizeEvent.getInstance().subscribe(new Listener<WindowResizeEvent>(-1, event -> {}));
    }
    
    @Override
    public void render(final class_332 context, final int mouseX, final int mouseY, final float delta) {
        this.updateThings();
        this.renderThings(context, mouseX, mouseY, delta);
    }
    
    @Override
    public void keyPressed(final int keyCode, final int scanCode, final int modifiers) {
        this.moduleComponents.forEach(moduleComponents -> moduleComponents.keyPressed(keyCode, scanCode, modifiers));
    }
    
    public boolean charTyped(final char chr, final int modifiers) {
        for (final ModuleComponent mc : this.moduleComponents) {
            if (mc.charTyped(chr, modifiers)) {
                return true;
            }
        }
        return false;
    }
    
    public void setSearchFilter(final String query) {
        final String cleanQuery = query.toLowerCase().replace(" ", "");
        for (final ModuleComponent mc : this.moduleComponents) {
            final String moduleName = mc.getModule().getName().toLowerCase().replace(" ", "");
            final boolean visible = cleanQuery.isEmpty() || moduleName.contains(cleanQuery);
            mc.setSearchVisible(visible);
        }
    }
    
    public boolean isBindingActive() {
        for (final ModuleComponent mc : this.moduleComponents) {
            if (mc.isBinding()) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void mouseClicked(final double mouseX, final double mouseY, final int button) {
        if (this.inPanel(mouseX, mouseY)) {
            for (final ModuleComponent module : this.moduleComponents) {
                if (!MouseUtil.isHovered(mouseX, mouseY, module.getX(), module.getY(), module.getWidth(), module.getHeight())) {
                    continue;
                }
                module.mouseClicked(mouseX, mouseY, button);
            }
        }
    }
    
    @Override
    public void mouseReleased(final double mouseX, final double mouseY, final int button) {
        if (this.inPanel(mouseX, mouseY)) {
            for (final ModuleComponent module : this.moduleComponents) {
                if (!MouseUtil.isHovered(mouseX, mouseY, module.getX(), module.getY(), module.getWidth(), module.getHeight())) {
                    continue;
                }
                module.mouseReleased(mouseX, mouseY, button);
            }
        }
    }
    
    @Override
    public void mouseScrolled(final double mouseX, final double mouseY, final double horizontalAmount, final double verticalAmount) {
        if (MouseUtil.isHovered(mouseX, mouseY, this.getX(), this.getY(), this.getWidth(), this.getHeight())) {
            this.scroll += verticalAmount * 20.0;
        }
    }
    
    private void updateThings() {
        this.scrollAnimation.update();
        this.scrollAnimation.run(this.scroll, 600L, Easing.EXPO_OUT);
        final float w = 99.0f;
        final float h = 240.0f;
        this.setWidth(this.scaled(w));
        this.setHeight(this.scaled(h));
        this.moduleComponents.forEach(m -> m.setRound(this.getRound() * 2.0f));
    }
    
    private void renderThings(final class_332 context, final int mouseX, final int mouseY, final float delta) {
        final class_4587 matrixStack = context.method_51448();
        final float fontSize = this.getHeaderHeight() * 0.475f;
        final float moduleY = 0.0f;
        final int fullAlpha = (int)(this.getAlpha() * 255.0f);
        this.calcModules(moduleY);
        RenderUtil.BLUR_RECT.draw(matrixStack, this.getX(), this.getY(), this.getWidth(), this.getHeaderHeight(), new Vector4f(this.getRound(), this.getRound(), 0.0f, 0.0f), UIColors.blur(fullAlpha));
        Fonts.PS_BOLD.drawCenteredText(matrixStack, this.category.getLabel(), this.getX() + this.getWidth() / 2.0f, this.getY() + this.getHeaderHeight() / 2.0f - fontSize / 2.0f, fontSize, UIColors.textColor(fullAlpha));
        for (final ModuleComponent module : this.moduleComponents) {
            module.setAlpha(this.getAlpha());
            module.render(context, mouseX, mouseY, delta);
        }
    }
    
    private void calcModules(float moduleY) {
        for (final ModuleComponent module : this.moduleComponents) {
            if (!module.isSearchVisible()) {
                module.setHeight(0.0f);
            }
            else {
                final float openAnim = module.getAnim();
                if (openAnim > 0.0f) {
                    float settingOffset = 0.0f;
                    for (final SettingComponent setting : module.getSettings()) {
                        final float visibleAnim = (float)setting.getVisibleAnimation().getValue();
                        if (visibleAnim > 0.0) {
                            settingOffset += (setting.getHeight() + this.gap()) * visibleAnim;
                        }
                    }
                    settingOffset *= openAnim;
                    module.setHeight(module.getDefaultHeight() + (settingOffset + this.gap()) * openAnim);
                }
                else {
                    module.setHeight(module.getDefaultHeight());
                }
                module.setWidth(this.getWidth());
                module.setRound(this.getRound() / 2.0f);
                module.setX(this.getX());
                module.setY((float)(this.getY() + this.scrollAnimation.getValue() + moduleY + this.getHeaderHeight()));
                moduleY += module.getHeight();
            }
        }
        this.setHeight(this.getHeaderHeight() + moduleY);
    }
    
    public float getHeaderHeight() {
        return this.scaled(18.0f);
    }
    
    public float getRound() {
        return this.getHeaderHeight() / 2.2f;
    }
    
    public boolean inPanel(final double mouseX, final double mouseY) {
        return MouseUtil.isHovered(mouseX, mouseY, this.getX(), this.getY() + this.getHeaderHeight(), this.getWidth(), this.getHeight() - this.getHeaderHeight());
    }
    
    @Generated
    public Category getCategory() {
        return this.category;
    }
    
    @Generated
    public List<ModuleComponent> getModuleComponents() {
        return this.moduleComponents;
    }
    
    @Generated
    public int getCategoryIndex() {
        return this.categoryIndex;
    }
    
    @Generated
    public double getScroll() {
        return this.scroll;
    }
    
    @Generated
    public AnimationUtil getScrollAnimation() {
        return this.scrollAnimation;
    }
    
    @Generated
    public void setCategoryIndex(final int categoryIndex) {
        this.categoryIndex = categoryIndex;
    }
}
