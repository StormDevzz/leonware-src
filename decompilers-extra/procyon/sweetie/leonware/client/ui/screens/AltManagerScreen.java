// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.ui.screens;

import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;
import net.minecraft.class_310;
import net.minecraft.class_320;
import java.util.Optional;
import net.minecraft.class_442;
import java.util.List;
import net.minecraft.class_4587;
import net.minecraft.class_3532;
import sweetie.leonware.api.system.configs.AltManager;
import sweetie.leonware.api.utils.render.fonts.Fonts;
import sweetie.leonware.api.utils.color.UIColors;
import java.awt.Color;
import sweetie.leonware.api.utils.render.RenderUtil;
import net.minecraft.class_332;
import net.minecraft.class_2561;
import net.minecraft.class_437;

public class AltManagerScreen extends class_437
{
    private String inputText;
    private boolean inputFocused;
    private String errorMessage;
    private long errorTime;
    private int scrollOffset;
    private static final int VISIBLE_ALTS = 8;
    private static final float ITEM_H = 28.0f;
    private long lastClickTime;
    private int lastClickIndex;
    
    public AltManagerScreen() {
        super((class_2561)class_2561.method_43470("Alt Manager"));
        this.inputText = "";
        this.inputFocused = false;
        this.errorMessage = null;
        this.errorTime = 0L;
        this.scrollOffset = 0;
        this.lastClickTime = 0L;
        this.lastClickIndex = -1;
    }
    
    public void method_25394(final class_332 context, final int mouseX, final int mouseY, final float delta) {
        final class_4587 mat = context.method_51448();
        final float sw = (float)this.field_22789;
        final float sh = (float)this.field_22790;
        RenderUtil.BLUR_RECT.draw(mat, 0.0f, 0.0f, sw, sh, 0.0f, new Color(0, 0, 0, 180));
        final float panelW = 280.0f;
        final float panelH = 340.0f;
        final float px = (sw - panelW) / 2.0f;
        final float py = (sh - panelH) / 2.0f;
        RenderUtil.BLUR_RECT.draw(mat, px, py, panelW, panelH, 8.0f, UIColors.widgetBlur(220));
        RenderUtil.BLUR_RECT.draw(mat, px, py, panelW, panelH, 8.0f, new Color(18, 18, 18, 200));
        Fonts.PS_MEDIUM.drawCenteredText(mat, "Alt Manager", px + panelW / 2.0f, py + 12.0f, 9.0f, UIColors.textColor(255));
        final float inputY = py + 36.0f;
        final float inputX = px + 12.0f;
        final float inputW = panelW - 24.0f;
        final float inputH = 24.0f;
        final Color inputBg = this.inputFocused ? new Color(35, 35, 35, 255) : new Color(25, 25, 25, 255);
        RenderUtil.BLUR_RECT.draw(mat, inputX, inputY, inputW, inputH, 5.0f, inputBg);
        final String displayText = (this.inputText.isEmpty() && !this.inputFocused) ? "\u0412\u0432\u0435\u0434\u0438\u0442\u0435 \u043d\u0438\u043a..." : (this.inputText + ((this.inputFocused && System.currentTimeMillis() / 500L % 2L == 0L) ? "|" : ""));
        final Color textColor = (this.inputText.isEmpty() && !this.inputFocused) ? new Color(100, 100, 100, 255) : UIColors.textColor(255);
        Fonts.PS_MEDIUM.drawText(mat, displayText, inputX + 8.0f, inputY + 7.0f, 7.0f, textColor);
        Fonts.PS_MEDIUM.drawText(mat, this.inputText.length() + "/16", inputX + inputW - 28.0f, inputY + 7.0f, 6.0f, new Color(120, 120, 120, 200));
        final float btnY = inputY + inputH + 8.0f;
        final float btnGap = 6.0f;
        final float btnW = (inputW - btnGap) / 2.0f;
        final boolean canSave = AltManager.isValidNick(this.inputText);
        final Color saveColor = canSave ? UIColors.gradient(0, 220) : new Color(60, 60, 60, 200);
        RenderUtil.BLUR_RECT.draw(mat, inputX, btnY, btnW, 20.0f, 5.0f, saveColor);
        Fonts.PS_MEDIUM.drawCenteredText(mat, "\u0421\u043e\u0445\u0440\u0430\u043d\u0438\u0442\u044c", inputX + btnW / 2.0f, btnY + 6.0f, 7.0f, new Color(255, 255, 255, canSave ? 255 : 120));
        final boolean canApply = !AltManager.getInstance().getAlts().isEmpty();
        RenderUtil.BLUR_RECT.draw(mat, inputX + btnW + btnGap, btnY, btnW, 20.0f, 5.0f, new Color(40, 40, 40, 220));
        Fonts.PS_MEDIUM.drawCenteredText(mat, "\u0420\u0430\u043d\u0434\u043e\u043c\u043d\u044b\u0439", inputX + btnW + btnGap + btnW / 2.0f, btnY + 6.0f, 7.0f, new Color(255, 255, 255, 255));
        if (this.errorMessage != null) {
            final float fade = class_3532.method_15363(1.0f - (System.currentTimeMillis() - this.errorTime) / 2000.0f, 0.0f, 1.0f);
            if (fade <= 0.0f) {
                this.errorMessage = null;
            }
            else {
                Fonts.PS_MEDIUM.drawCenteredText(mat, this.errorMessage, px + panelW / 2.0f, btnY + 26.0f, 6.5f, new Color(255, 80, 80, (int)(fade * 255.0f)));
            }
        }
        final float listY = btnY + ((this.errorMessage != null) ? 38.0f : 32.0f);
        final float listH = panelH - (listY - py) - 12.0f;
        final List<String> alts = AltManager.getInstance().getAlts();
        final int current = AltManager.getInstance().getCurrentIndex();
        final int maxScroll = Math.max(0, alts.size() - 8);
        this.scrollOffset = class_3532.method_15340(this.scrollOffset, 0, maxScroll);
        for (int i = this.scrollOffset; i < Math.min(alts.size(), this.scrollOffset + 8); ++i) {
            final float iy = listY + (i - this.scrollOffset) * 28.0f;
            final boolean isCurrent = i == current;
            final boolean hover = mouseX >= inputX && mouseX <= inputX + inputW && mouseY >= iy && mouseY <= iy + 28.0f - 2.0f;
            final Color itemBg = isCurrent ? UIColors.gradient(0, 60) : (hover ? new Color(35, 35, 35, 200) : new Color(22, 22, 22, 200));
            RenderUtil.BLUR_RECT.draw(mat, inputX, iy, inputW, 26.0f, 5.0f, itemBg);
            if (isCurrent) {
                final Color c1 = UIColors.gradient(0, 200);
                final Color c2 = UIColors.gradient(90, 200);
                RenderUtil.GRADIENT_RECT.draw(mat, inputX, iy, inputW, 1.5f, 0.0f, c1, c2, c1, c2);
            }
            Fonts.PS_MEDIUM.drawText(mat, alts.get(i), inputX + 10.0f, iy + 8.0f, 7.5f, isCurrent ? UIColors.textColor(255) : new Color(180, 180, 180, 255));
            final float delX = inputX + inputW - 24.0f;
            final float delY = iy + 4.0f;
            final float delW = 18.0f;
            final float delH = 18.0f;
            final boolean delHover = mouseX >= delX && mouseX <= delX + delW && mouseY >= delY && mouseY <= delY + delH;
            final Color delColor1 = delHover ? new Color(220, 50, 50, 200) : new Color(160, 40, 40, 160);
            final Color delColor2 = delHover ? new Color(180, 30, 30, 200) : new Color(120, 30, 30, 160);
            RenderUtil.GRADIENT_RECT.draw(mat, delX, delY, delW, delH, 3.0f, delColor1, delColor2, delColor2, delColor1);
            Fonts.PS_MEDIUM.drawCenteredText(mat, "\u2715", delX + delW / 2.0f, delY + 6.0f, 7.0f, new Color(255, 255, 255, 240));
        }
        if (alts.size() > 8) {
            final float scrollBarH = listH * 8.0f / alts.size();
            final float scrollBarY = listY + (listH - scrollBarH) * this.scrollOffset / maxScroll;
            RenderUtil.BLUR_RECT.draw(mat, inputX + inputW + 3.0f, scrollBarY, 4.0f, scrollBarH, 2.0f, UIColors.gradient(0, 180));
        }
        final float backX = px + 12.0f;
        final float backY = py + panelH - 28.0f;
        final boolean backHover = mouseX >= backX && mouseX <= backX + 60.0f && mouseY >= backY && mouseY <= backY + 20.0f;
        RenderUtil.BLUR_RECT.draw(mat, backX, backY, 60.0f, 20.0f, 5.0f, backHover ? new Color(40, 40, 40, 220) : new Color(25, 25, 25, 200));
        Fonts.PS_MEDIUM.drawCenteredText(mat, "\u041d\u0430\u0437\u0430\u0434", backX + 30.0f, backY + 6.0f, 7.0f, UIColors.textColor(220));
    }
    
    public boolean method_25402(final double mouseX, final double mouseY, final int button) {
        final float sw = (float)this.field_22789;
        final float sh = (float)this.field_22790;
        final float panelW = 280.0f;
        final float panelH = 340.0f;
        final float px = (sw - panelW) / 2.0f;
        final float py = (sh - panelH) / 2.0f;
        final float inputX = px + 12.0f;
        final float inputY = py + 36.0f;
        final float inputW = panelW - 24.0f;
        final float inputH = 24.0f;
        final float btnY = inputY + inputH + 8.0f;
        final float btnGap = 6.0f;
        final float btnW = (inputW - btnGap) / 2.0f;
        final float listY = btnY + 38.0f;
        this.inputFocused = (mouseX >= inputX && mouseX <= inputX + inputW && mouseY >= inputY && mouseY <= inputY + inputH);
        if (mouseX >= inputX && mouseX <= inputX + btnW && mouseY >= btnY && mouseY <= btnY + 20.0f) {
            if (AltManager.isValidNick(this.inputText)) {
                final boolean added = AltManager.getInstance().addAlt(this.inputText);
                if (!added) {
                    this.showError("\u041d\u0438\u043a \u0443\u0436\u0435 \u0441\u0443\u0449\u0435\u0441\u0442\u0432\u0443\u0435\u0442 \u0438\u043b\u0438 \u0441\u043f\u0438\u0441\u043e\u043a \u043f\u043e\u043b\u043e\u043d");
                }
                else {
                    this.inputText = "";
                }
            }
            else if (this.inputText.length() < 3) {
                this.showError("\u041c\u0438\u043d\u0438\u043c\u0443\u043c 3 \u0441\u0438\u043c\u0432\u043e\u043b\u0430");
            }
            else if (this.inputText.length() > 16) {
                this.showError("\u041c\u0430\u043a\u0441\u0438\u043c\u0443\u043c 16 \u0441\u0438\u043c\u0432\u043e\u043b\u043e\u0432");
            }
            else {
                this.showError("\u0422\u043e\u043b\u044c\u043a\u043e a-z, A-Z, 0-9, _");
            }
            return true;
        }
        if (mouseX >= inputX + btnW + btnGap && mouseX <= inputX + inputW && mouseY >= btnY && mouseY <= btnY + 20.0f) {
            final String nick = AltManager.getInstance().generateRandomNick();
            final boolean added2 = AltManager.getInstance().addAlt(nick);
            if (added2) {
                final int idx = AltManager.getInstance().getAlts().size() - 1;
                AltManager.getInstance().setCurrentIndex(idx);
                this.applyAlt(nick);
            }
            else {
                this.showError("\u041d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u0434\u043e\u0431\u0430\u0432\u0438\u0442\u044c \u043d\u0438\u043a");
            }
            return true;
        }
        final List<String> alts = AltManager.getInstance().getAlts();
        for (int i = this.scrollOffset; i < Math.min(alts.size(), this.scrollOffset + 8); ++i) {
            final float iy = listY + (i - this.scrollOffset) * 28.0f;
            final float delX = inputX + inputW - 24.0f;
            final float delY = iy + 4.0f;
            final float delW = 18.0f;
            final float delH = 18.0f;
            if (mouseX >= delX && mouseX <= delX + delW && mouseY >= delY && mouseY <= delY + delH) {
                AltManager.getInstance().removeAlt(i);
                return true;
            }
            if (mouseX >= inputX && mouseX <= inputX + inputW - 26.0f && mouseY >= iy && mouseY <= iy + 28.0f - 2.0f) {
                final long now = System.currentTimeMillis();
                if (this.lastClickIndex == i && now - this.lastClickTime < 500L) {
                    AltManager.getInstance().setCurrentIndex(i);
                    this.applyAlt(alts.get(i));
                    this.lastClickIndex = -1;
                }
                else {
                    AltManager.getInstance().setCurrentIndex(i);
                    this.lastClickIndex = i;
                    this.lastClickTime = now;
                }
                return true;
            }
        }
        final float backX = px + 12.0f;
        final float backY = py + panelH - 28.0f;
        if (mouseX >= backX && mouseX <= backX + 60.0f && mouseY >= backY && mouseY <= backY + 20.0f) {
            this.field_22787.method_1507((class_437)new class_442());
            return true;
        }
        return super.method_25402(mouseX, mouseY, button);
    }
    
    public boolean method_25401(final double mouseX, final double mouseY, final double horizontalAmount, final double verticalAmount) {
        this.scrollOffset = class_3532.method_15340(this.scrollOffset - (int)verticalAmount, 0, Math.max(0, AltManager.getInstance().getAlts().size() - 8));
        return true;
    }
    
    public boolean method_25404(final int keyCode, final int scanCode, final int modifiers) {
        if (keyCode == 256) {
            this.field_22787.method_1507((class_437)new class_442());
            return true;
        }
        if (this.inputFocused && keyCode == 259 && !this.inputText.isEmpty()) {
            this.inputText = this.inputText.substring(0, this.inputText.length() - 1);
            return true;
        }
        if (this.inputFocused && keyCode == 257) {
            if (AltManager.isValidNick(this.inputText)) {
                AltManager.getInstance().addAlt(this.inputText);
                this.inputText = "";
            }
            else {
                this.showError("\u041d\u0435\u043a\u043e\u0440\u0440\u0435\u043a\u0442\u043d\u044b\u0439 \u043d\u0438\u043a");
            }
            return true;
        }
        return super.method_25404(keyCode, scanCode, modifiers);
    }
    
    public boolean method_25400(final char chr, final int modifiers) {
        if (!this.inputFocused) {
            return false;
        }
        if (!AltManager.isValidChar(chr)) {
            return false;
        }
        if (this.inputText.length() >= 16) {
            return false;
        }
        this.inputText += chr;
        return true;
    }
    
    private void showError(final String msg) {
        this.errorMessage = msg;
        this.errorTime = System.currentTimeMillis();
    }
    
    private void applyAlt(final String nick) {
        try {
            final class_320 oldSession = this.field_22787.method_1548();
            final class_320 newSession = new class_320(nick, oldSession.method_44717(), oldSession.method_1674(), (Optional)Optional.empty(), (Optional)Optional.empty(), oldSession.method_35718());
            Field sessionField = null;
            for (final Field f : class_310.class.getDeclaredFields()) {
                if (f.getType() == class_320.class) {
                    sessionField = f;
                    break;
                }
            }
            if (sessionField == null) {
                this.showError("\u041f\u043e\u043b\u0435 Session \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d\u043e \u0432 MinecraftClient");
                return;
            }
            sessionField.setAccessible(true);
            sessionField.set(this.field_22787, newSession);
            this.showError("§a\u041f\u0440\u0438\u043c\u0435\u043d\u0451\u043d: " + nick);
        }
        catch (final InaccessibleObjectException e) {
            this.showError("\u041d\u0435\u0442 \u0434\u043e\u0441\u0442\u0443\u043f\u0430 \u043a \u043f\u043e\u043b\u044e: " + e.getMessage());
        }
        catch (final IllegalAccessException e2) {
            this.showError("IllegalAccess: " + e2.getMessage());
        }
        catch (final Exception e3) {
            this.showError("\u041e\u0448\u0438\u0431\u043a\u0430 (" + e3.getClass().getSimpleName() + "): " + e3.getMessage());
        }
    }
    
    public boolean method_25421() {
        return false;
    }
}
