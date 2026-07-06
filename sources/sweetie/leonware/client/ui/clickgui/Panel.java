package sweetie.leonware.client.ui.clickgui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import lombok.Generated;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import org.joml.Vector4f;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.other.WindowResizeEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleManager;
import sweetie.leonware.api.utils.animation.AnimationUtil;
import sweetie.leonware.api.utils.animation.Easing;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.math.MouseUtil;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.api.utils.render.fonts.Fonts;
import sweetie.leonware.client.ui.UIComponent;
import sweetie.leonware.client.ui.clickgui.module.ModuleComponent;
import sweetie.leonware.client.ui.clickgui.module.SettingComponent;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/ui/clickgui/Panel.class */
public class Panel extends UIComponent {
    private final Category category;
    private int categoryIndex;
    private final List<ModuleComponent> moduleComponents = new ArrayList();
    private double scroll = 0.0d;
    private final AnimationUtil scrollAnimation = new AnimationUtil();

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
    public void setCategoryIndex(int categoryIndex) {
        this.categoryIndex = categoryIndex;
    }

    @Generated
    public double getScroll() {
        return this.scroll;
    }

    @Generated
    public AnimationUtil getScrollAnimation() {
        return this.scrollAnimation;
    }

    public Panel(Category category) {
        this.category = category;
        for (Module module : ModuleManager.getInstance().getModules()) {
            if (module.getCategory() == category) {
                ModuleComponent moduleComponent = new ModuleComponent(module);
                moduleComponent.setRound(getRound() * 2.0f);
                this.moduleComponents.add(moduleComponent);
            }
        }
        if (!this.moduleComponents.isEmpty()) {
            ((ModuleComponent) this.moduleComponents.getLast()).setLast(true);
        }
        int index = this.categoryIndex;
        Iterator<ModuleComponent> it = this.moduleComponents.iterator();
        while (it.hasNext()) {
            it.next().setIndex(index);
            index += 45;
        }
        WindowResizeEvent.getInstance().subscribe(new Listener(-1, event -> {
        }));
    }

    @Override // sweetie.leonware.api.system.interfaces.UIApi
    public void render(class_332 context, int mouseX, int mouseY, float delta) {
        updateThings();
        renderThings(context, mouseX, mouseY, delta);
    }

    @Override // sweetie.leonware.api.system.interfaces.UIApi
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        this.moduleComponents.forEach(moduleComponents -> {
            moduleComponents.keyPressed(keyCode, scanCode, modifiers);
        });
    }

    public boolean charTyped(char chr, int modifiers) {
        for (ModuleComponent mc : this.moduleComponents) {
            if (mc.charTyped(chr, modifiers)) {
                return true;
            }
        }
        return false;
    }

    public void setSearchFilter(String query) {
        String cleanQuery = query.toLowerCase().replace(" ", "");
        for (ModuleComponent mc : this.moduleComponents) {
            String moduleName = mc.getModule().getName().toLowerCase().replace(" ", "");
            boolean visible = cleanQuery.isEmpty() || moduleName.contains(cleanQuery);
            mc.setSearchVisible(visible);
        }
    }

    public boolean isBindingActive() {
        for (ModuleComponent mc : this.moduleComponents) {
            if (mc.isBinding()) {
                return true;
            }
        }
        return false;
    }

    @Override // sweetie.leonware.api.system.interfaces.UIApi
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (inPanel(mouseX, mouseY)) {
            for (ModuleComponent module : this.moduleComponents) {
                if (MouseUtil.isHovered(mouseX, mouseY, module.getX(), module.getY(), module.getWidth(), module.getHeight())) {
                    module.mouseClicked(mouseX, mouseY, button);
                }
            }
        }
    }

    @Override // sweetie.leonware.api.system.interfaces.UIApi
    public void mouseReleased(double mouseX, double mouseY, int button) {
        if (inPanel(mouseX, mouseY)) {
            for (ModuleComponent module : this.moduleComponents) {
                if (MouseUtil.isHovered(mouseX, mouseY, module.getX(), module.getY(), module.getWidth(), module.getHeight())) {
                    module.mouseReleased(mouseX, mouseY, button);
                }
            }
        }
    }

    @Override // sweetie.leonware.api.system.interfaces.UIApi
    public void mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (MouseUtil.isHovered(mouseX, mouseY, getX(), getY(), getWidth(), getHeight())) {
            this.scroll += verticalAmount * 20.0d;
        }
    }

    private void updateThings() {
        this.scrollAnimation.update();
        this.scrollAnimation.run(this.scroll, 600L, Easing.EXPO_OUT);
        setWidth(scaled(99.0f));
        setHeight(scaled(240.0f));
        this.moduleComponents.forEach(m -> {
            m.setRound(getRound() * 2.0f);
        });
    }

    private void renderThings(class_332 context, int mouseX, int mouseY, float delta) {
        class_4587 matrixStack = context.method_51448();
        float fontSize = getHeaderHeight() * 0.475f;
        int fullAlpha = (int) (getAlpha() * 255.0f);
        calcModules(0.0f);
        RenderUtil.BLUR_RECT.draw(matrixStack, getX(), getY(), getWidth(), getHeaderHeight(), new Vector4f(getRound(), getRound(), 0.0f, 0.0f), UIColors.blur(fullAlpha));
        Fonts.PS_BOLD.drawCenteredText(matrixStack, this.category.getLabel(), getX() + (getWidth() / 2.0f), (getY() + (getHeaderHeight() / 2.0f)) - (fontSize / 2.0f), fontSize, UIColors.textColor(fullAlpha));
        for (ModuleComponent module : this.moduleComponents) {
            module.setAlpha(getAlpha());
            module.render(context, mouseX, mouseY, delta);
        }
    }

    private void calcModules(float moduleY) {
        for (ModuleComponent module : this.moduleComponents) {
            if (!module.isSearchVisible()) {
                module.setHeight(0.0f);
            } else {
                float openAnim = module.getAnim();
                if (openAnim > 0.0f) {
                    float settingOffset = 0.0f;
                    for (SettingComponent setting : module.getSettings()) {
                        float visibleAnim = (float) setting.getVisibleAnimation().getValue();
                        if (visibleAnim > 0.0d) {
                            settingOffset += (setting.getHeight() + gap()) * visibleAnim;
                        }
                    }
                    module.setHeight(module.getDefaultHeight() + (((settingOffset * openAnim) + gap()) * openAnim));
                } else {
                    module.setHeight(module.getDefaultHeight());
                }
                module.setWidth(getWidth());
                module.setRound(getRound() / 2.0f);
                module.setX(getX());
                module.setY((float) (((double) getY()) + this.scrollAnimation.getValue() + ((double) moduleY) + ((double) getHeaderHeight())));
                moduleY += module.getHeight();
            }
        }
        setHeight(getHeaderHeight() + moduleY);
    }

    public float getHeaderHeight() {
        return scaled(18.0f);
    }

    public float getRound() {
        return getHeaderHeight() / 2.2f;
    }

    public boolean inPanel(double mouseX, double mouseY) {
        return MouseUtil.isHovered(mouseX, mouseY, getX(), getY() + getHeaderHeight(), getWidth(), getHeight() - getHeaderHeight());
    }
}
