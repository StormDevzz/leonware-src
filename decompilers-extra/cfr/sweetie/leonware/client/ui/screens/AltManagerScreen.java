/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2561
 *  net.minecraft.class_310
 *  net.minecraft.class_320
 *  net.minecraft.class_332
 *  net.minecraft.class_3532
 *  net.minecraft.class_437
 *  net.minecraft.class_442
 *  net.minecraft.class_4587
 */
package sweetie.leonware.client.ui.screens;

import java.awt.Color;
import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;
import java.util.List;
import java.util.Optional;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_320;
import net.minecraft.class_332;
import net.minecraft.class_3532;
import net.minecraft.class_437;
import net.minecraft.class_442;
import net.minecraft.class_4587;
import sweetie.leonware.api.system.configs.AltManager;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.api.utils.render.fonts.Fonts;

public class AltManagerScreen
extends class_437 {
    private String inputText = "";
    private boolean inputFocused = false;
    private String errorMessage = null;
    private long errorTime = 0L;
    private int scrollOffset = 0;
    private static final int VISIBLE_ALTS = 8;
    private static final float ITEM_H = 28.0f;
    private long lastClickTime = 0L;
    private int lastClickIndex = -1;

    public AltManagerScreen() {
        super((class_2561)class_2561.method_43470((String)"Alt Manager"));
    }

    public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
        class_4587 mat = context.method_51448();
        float sw = this.field_22789;
        float sh = this.field_22790;
        RenderUtil.BLUR_RECT.draw(mat, 0.0f, 0.0f, sw, sh, 0.0f, new Color(0, 0, 0, 180));
        float panelW = 280.0f;
        float panelH = 340.0f;
        float px = (sw - panelW) / 2.0f;
        float py = (sh - panelH) / 2.0f;
        RenderUtil.BLUR_RECT.draw(mat, px, py, panelW, panelH, 8.0f, UIColors.widgetBlur(220));
        RenderUtil.BLUR_RECT.draw(mat, px, py, panelW, panelH, 8.0f, new Color(18, 18, 18, 200));
        Fonts.PS_MEDIUM.drawCenteredText(mat, "Alt Manager", px + panelW / 2.0f, py + 12.0f, 9.0f, UIColors.textColor(255));
        float inputY = py + 36.0f;
        float inputX = px + 12.0f;
        float inputW = panelW - 24.0f;
        float inputH = 24.0f;
        Color inputBg = this.inputFocused ? new Color(35, 35, 35, 255) : new Color(25, 25, 25, 255);
        RenderUtil.BLUR_RECT.draw(mat, inputX, inputY, inputW, inputH, 5.0f, inputBg);
        String displayText = this.inputText.isEmpty() && !this.inputFocused ? "\u0412\u0432\u0435\u0434\u0438\u0442\u0435 \u043d\u0438\u043a..." : this.inputText + (this.inputFocused && System.currentTimeMillis() / 500L % 2L == 0L ? "|" : "");
        Color textColor = this.inputText.isEmpty() && !this.inputFocused ? new Color(100, 100, 100, 255) : UIColors.textColor(255);
        Fonts.PS_MEDIUM.drawText(mat, displayText, inputX + 8.0f, inputY + 7.0f, 7.0f, textColor);
        Fonts.PS_MEDIUM.drawText(mat, this.inputText.length() + "/16", inputX + inputW - 28.0f, inputY + 7.0f, 6.0f, new Color(120, 120, 120, 200));
        float btnY = inputY + inputH + 8.0f;
        float btnGap = 6.0f;
        float btnW = (inputW - btnGap) / 2.0f;
        boolean canSave = AltManager.isValidNick(this.inputText);
        Color saveColor = canSave ? UIColors.gradient(0, 220) : new Color(60, 60, 60, 200);
        RenderUtil.BLUR_RECT.draw(mat, inputX, btnY, btnW, 20.0f, 5.0f, saveColor);
        Fonts.PS_MEDIUM.drawCenteredText(mat, "\u0421\u043e\u0445\u0440\u0430\u043d\u0438\u0442\u044c", inputX + btnW / 2.0f, btnY + 6.0f, 7.0f, new Color(255, 255, 255, canSave ? 255 : 120));
        boolean canApply = !AltManager.getInstance().getAlts().isEmpty();
        RenderUtil.BLUR_RECT.draw(mat, inputX + btnW + btnGap, btnY, btnW, 20.0f, 5.0f, new Color(40, 40, 40, 220));
        Fonts.PS_MEDIUM.drawCenteredText(mat, "\u0420\u0430\u043d\u0434\u043e\u043c\u043d\u044b\u0439", inputX + btnW + btnGap + btnW / 2.0f, btnY + 6.0f, 7.0f, new Color(255, 255, 255, 255));
        if (this.errorMessage != null) {
            float fade = class_3532.method_15363((float)(1.0f - (float)(System.currentTimeMillis() - this.errorTime) / 2000.0f), (float)0.0f, (float)1.0f);
            if (fade <= 0.0f) {
                this.errorMessage = null;
            } else {
                Fonts.PS_MEDIUM.drawCenteredText(mat, this.errorMessage, px + panelW / 2.0f, btnY + 26.0f, 6.5f, new Color(255, 80, 80, (int)(fade * 255.0f)));
            }
        }
        float listY = btnY + (this.errorMessage != null ? 38.0f : 32.0f);
        float listH = panelH - (listY - py) - 12.0f;
        List<String> alts = AltManager.getInstance().getAlts();
        int current = AltManager.getInstance().getCurrentIndex();
        int maxScroll = Math.max(0, alts.size() - 8);
        for (int i = this.scrollOffset = class_3532.method_15340((int)this.scrollOffset, (int)0, (int)maxScroll); i < Math.min(alts.size(), this.scrollOffset + 8); ++i) {
            boolean hover;
            float iy = listY + (float)(i - this.scrollOffset) * 28.0f;
            boolean isCurrent = i == current;
            boolean bl = hover = (float)mouseX >= inputX && (float)mouseX <= inputX + inputW && (float)mouseY >= iy && (float)mouseY <= iy + 28.0f - 2.0f;
            Color itemBg = isCurrent ? UIColors.gradient(0, 60) : (hover ? new Color(35, 35, 35, 200) : new Color(22, 22, 22, 200));
            RenderUtil.BLUR_RECT.draw(mat, inputX, iy, inputW, 26.0f, 5.0f, itemBg);
            if (isCurrent) {
                Color c1 = UIColors.gradient(0, 200);
                Color c2 = UIColors.gradient(90, 200);
                RenderUtil.GRADIENT_RECT.draw(mat, inputX, iy, inputW, 1.5f, 0.0f, c1, c2, c1, c2);
            }
            Fonts.PS_MEDIUM.drawText(mat, alts.get(i), inputX + 10.0f, iy + 8.0f, 7.5f, isCurrent ? UIColors.textColor(255) : new Color(180, 180, 180, 255));
            float delX = inputX + inputW - 24.0f;
            float delY = iy + 4.0f;
            float delW = 18.0f;
            float delH = 18.0f;
            boolean delHover = (float)mouseX >= delX && (float)mouseX <= delX + delW && (float)mouseY >= delY && (float)mouseY <= delY + delH;
            Color delColor1 = delHover ? new Color(220, 50, 50, 200) : new Color(160, 40, 40, 160);
            Color delColor2 = delHover ? new Color(180, 30, 30, 200) : new Color(120, 30, 30, 160);
            RenderUtil.GRADIENT_RECT.draw(mat, delX, delY, delW, delH, 3.0f, delColor1, delColor2, delColor2, delColor1);
            Fonts.PS_MEDIUM.drawCenteredText(mat, "\u2715", delX + delW / 2.0f, delY + 6.0f, 7.0f, new Color(255, 255, 255, 240));
        }
        if (alts.size() > 8) {
            float scrollBarH = listH * 8.0f / (float)alts.size();
            float scrollBarY = listY + (listH - scrollBarH) * (float)this.scrollOffset / (float)maxScroll;
            RenderUtil.BLUR_RECT.draw(mat, inputX + inputW + 3.0f, scrollBarY, 4.0f, scrollBarH, 2.0f, UIColors.gradient(0, 180));
        }
        float backX = px + 12.0f;
        float backY = py + panelH - 28.0f;
        boolean backHover = (float)mouseX >= backX && (float)mouseX <= backX + 60.0f && (float)mouseY >= backY && (float)mouseY <= backY + 20.0f;
        RenderUtil.BLUR_RECT.draw(mat, backX, backY, 60.0f, 20.0f, 5.0f, backHover ? new Color(40, 40, 40, 220) : new Color(25, 25, 25, 200));
        Fonts.PS_MEDIUM.drawCenteredText(mat, "\u041d\u0430\u0437\u0430\u0434", backX + 30.0f, backY + 6.0f, 7.0f, UIColors.textColor(220));
    }

    public boolean method_25402(double mouseX, double mouseY, int button) {
        float sw = this.field_22789;
        float sh = this.field_22790;
        float panelW = 280.0f;
        float panelH = 340.0f;
        float px = (sw - panelW) / 2.0f;
        float py = (sh - panelH) / 2.0f;
        float inputX = px + 12.0f;
        float inputY = py + 36.0f;
        float inputW = panelW - 24.0f;
        float inputH = 24.0f;
        float btnY = inputY + inputH + 8.0f;
        float btnGap = 6.0f;
        float btnW = (inputW - btnGap) / 2.0f;
        float listY = btnY + 38.0f;
        boolean bl = this.inputFocused = mouseX >= (double)inputX && mouseX <= (double)(inputX + inputW) && mouseY >= (double)inputY && mouseY <= (double)(inputY + inputH);
        if (mouseX >= (double)inputX && mouseX <= (double)(inputX + btnW) && mouseY >= (double)btnY && mouseY <= (double)(btnY + 20.0f)) {
            if (AltManager.isValidNick(this.inputText)) {
                boolean added = AltManager.getInstance().addAlt(this.inputText);
                if (!added) {
                    this.showError("\u041d\u0438\u043a \u0443\u0436\u0435 \u0441\u0443\u0449\u0435\u0441\u0442\u0432\u0443\u0435\u0442 \u0438\u043b\u0438 \u0441\u043f\u0438\u0441\u043e\u043a \u043f\u043e\u043b\u043e\u043d");
                } else {
                    this.inputText = "";
                }
            } else if (this.inputText.length() < 3) {
                this.showError("\u041c\u0438\u043d\u0438\u043c\u0443\u043c 3 \u0441\u0438\u043c\u0432\u043e\u043b\u0430");
            } else if (this.inputText.length() > 16) {
                this.showError("\u041c\u0430\u043a\u0441\u0438\u043c\u0443\u043c 16 \u0441\u0438\u043c\u0432\u043e\u043b\u043e\u0432");
            } else {
                this.showError("\u0422\u043e\u043b\u044c\u043a\u043e a-z, A-Z, 0-9, _");
            }
            return true;
        }
        if (mouseX >= (double)(inputX + btnW + btnGap) && mouseX <= (double)(inputX + inputW) && mouseY >= (double)btnY && mouseY <= (double)(btnY + 20.0f)) {
            String nick = AltManager.getInstance().generateRandomNick();
            boolean added = AltManager.getInstance().addAlt(nick);
            if (added) {
                int idx = AltManager.getInstance().getAlts().size() - 1;
                AltManager.getInstance().setCurrentIndex(idx);
                this.applyAlt(nick);
            } else {
                this.showError("\u041d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u0434\u043e\u0431\u0430\u0432\u0438\u0442\u044c \u043d\u0438\u043a");
            }
            return true;
        }
        List<String> alts = AltManager.getInstance().getAlts();
        for (int i = this.scrollOffset; i < Math.min(alts.size(), this.scrollOffset + 8); ++i) {
            float iy = listY + (float)(i - this.scrollOffset) * 28.0f;
            float delX = inputX + inputW - 24.0f;
            float delY = iy + 4.0f;
            float delW = 18.0f;
            float delH = 18.0f;
            if (mouseX >= (double)delX && mouseX <= (double)(delX + delW) && mouseY >= (double)delY && mouseY <= (double)(delY + delH)) {
                AltManager.getInstance().removeAlt(i);
                return true;
            }
            if (!(mouseX >= (double)inputX) || !(mouseX <= (double)(inputX + inputW - 26.0f)) || !(mouseY >= (double)iy) || !(mouseY <= (double)(iy + 28.0f - 2.0f))) continue;
            long now = System.currentTimeMillis();
            if (this.lastClickIndex == i && now - this.lastClickTime < 500L) {
                AltManager.getInstance().setCurrentIndex(i);
                this.applyAlt(alts.get(i));
                this.lastClickIndex = -1;
            } else {
                AltManager.getInstance().setCurrentIndex(i);
                this.lastClickIndex = i;
                this.lastClickTime = now;
            }
            return true;
        }
        float backX = px + 12.0f;
        float backY = py + panelH - 28.0f;
        if (mouseX >= (double)backX && mouseX <= (double)(backX + 60.0f) && mouseY >= (double)backY && mouseY <= (double)(backY + 20.0f)) {
            this.field_22787.method_1507((class_437)new class_442());
            return true;
        }
        return super.method_25402(mouseX, mouseY, button);
    }

    public boolean method_25401(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        this.scrollOffset = class_3532.method_15340((int)(this.scrollOffset - (int)verticalAmount), (int)0, (int)Math.max(0, AltManager.getInstance().getAlts().size() - 8));
        return true;
    }

    public boolean method_25404(int keyCode, int scanCode, int modifiers) {
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
            } else {
                this.showError("\u041d\u0435\u043a\u043e\u0440\u0440\u0435\u043a\u0442\u043d\u044b\u0439 \u043d\u0438\u043a");
            }
            return true;
        }
        return super.method_25404(keyCode, scanCode, modifiers);
    }

    public boolean method_25400(char chr, int modifiers) {
        if (!this.inputFocused) {
            return false;
        }
        if (!AltManager.isValidChar(chr)) {
            return false;
        }
        if (this.inputText.length() >= 16) {
            return false;
        }
        this.inputText = this.inputText + chr;
        return true;
    }

    private void showError(String msg) {
        this.errorMessage = msg;
        this.errorTime = System.currentTimeMillis();
    }

    private void applyAlt(String nick) {
        try {
            class_320 oldSession = this.field_22787.method_1548();
            class_320 newSession = new class_320(nick, oldSession.method_44717(), oldSession.method_1674(), Optional.empty(), Optional.empty(), oldSession.method_35718());
            Field sessionField = null;
            for (Field f : class_310.class.getDeclaredFields()) {
                if (f.getType() != class_320.class) continue;
                sessionField = f;
                break;
            }
            if (sessionField == null) {
                this.showError("\u041f\u043e\u043b\u0435 Session \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d\u043e \u0432 MinecraftClient");
                return;
            }
            sessionField.setAccessible(true);
            sessionField.set(this.field_22787, newSession);
            this.showError("\u00a7a\u041f\u0440\u0438\u043c\u0435\u043d\u0451\u043d: " + nick);
        }
        catch (InaccessibleObjectException e) {
            this.showError("\u041d\u0435\u0442 \u0434\u043e\u0441\u0442\u0443\u043f\u0430 \u043a \u043f\u043e\u043b\u044e: " + e.getMessage());
        }
        catch (IllegalAccessException e) {
            this.showError("IllegalAccess: " + e.getMessage());
        }
        catch (Exception e) {
            this.showError("\u041e\u0448\u0438\u0431\u043a\u0430 (" + e.getClass().getSimpleName() + "): " + e.getMessage());
        }
    }

    public boolean method_25421() {
        return false;
    }
}

