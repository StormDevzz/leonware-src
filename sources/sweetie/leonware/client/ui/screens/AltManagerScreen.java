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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/ui/screens/AltManagerScreen.class */
public class AltManagerScreen extends class_437 {
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
        super(class_2561.method_43470("Alt Manager"));
        this.inputText = "";
        this.inputFocused = false;
        this.errorMessage = null;
        this.errorTime = 0L;
        this.scrollOffset = 0;
        this.lastClickTime = 0L;
        this.lastClickIndex = -1;
    }

    public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
        String str;
        Color colorTextColor;
        Color color;
        class_4587 mat = context.method_51448();
        float sw = this.field_22789;
        float sh = this.field_22790;
        RenderUtil.BLUR_RECT.draw(mat, 0.0f, 0.0f, sw, sh, 0.0f, new Color(0, 0, 0, 180));
        float px = (sw - 280.0f) / 2.0f;
        float py = (sh - 340.0f) / 2.0f;
        RenderUtil.BLUR_RECT.draw(mat, px, py, 280.0f, 340.0f, 8.0f, UIColors.widgetBlur(220));
        RenderUtil.BLUR_RECT.draw(mat, px, py, 280.0f, 340.0f, 8.0f, new Color(18, 18, 18, 200));
        Fonts.PS_MEDIUM.drawCenteredText(mat, "Alt Manager", px + (280.0f / 2.0f), py + 12.0f, 9.0f, UIColors.textColor(255));
        float inputY = py + 36.0f;
        float inputX = px + 12.0f;
        float inputW = 280.0f - 24.0f;
        Color inputBg = this.inputFocused ? new Color(35, 35, 35, 255) : new Color(25, 25, 25, 255);
        RenderUtil.BLUR_RECT.draw(mat, inputX, inputY, inputW, 24.0f, 5.0f, inputBg);
        if (this.inputText.isEmpty() && !this.inputFocused) {
            str = "Введите ник...";
        } else {
            str = this.inputText + ((this.inputFocused && (System.currentTimeMillis() / 500) % 2 == 0) ? "|" : "");
        }
        String displayText = str;
        if (this.inputText.isEmpty() && !this.inputFocused) {
            colorTextColor = new Color(100, 100, 100, 255);
        } else {
            colorTextColor = UIColors.textColor(255);
        }
        Color textColor = colorTextColor;
        Fonts.PS_MEDIUM.drawText(mat, displayText, inputX + 8.0f, inputY + 7.0f, 7.0f, textColor);
        Fonts.PS_MEDIUM.drawText(mat, this.inputText.length() + "/16", (inputX + inputW) - ITEM_H, inputY + 7.0f, 6.0f, new Color(120, 120, 120, 200));
        float btnY = inputY + 24.0f + 8.0f;
        float btnW = (inputW - 6.0f) / 2.0f;
        boolean canSave = AltManager.isValidNick(this.inputText);
        Color saveColor = canSave ? UIColors.gradient(0, 220) : new Color(60, 60, 60, 200);
        RenderUtil.BLUR_RECT.draw(mat, inputX, btnY, btnW, 20.0f, 5.0f, saveColor);
        Fonts.PS_MEDIUM.drawCenteredText(mat, "Сохранить", inputX + (btnW / 2.0f), btnY + 6.0f, 7.0f, new Color(255, 255, 255, canSave ? 255 : 120));
        boolean z = !AltManager.getInstance().getAlts().isEmpty();
        RenderUtil.BLUR_RECT.draw(mat, inputX + btnW + 6.0f, btnY, btnW, 20.0f, 5.0f, new Color(40, 40, 40, 220));
        Fonts.PS_MEDIUM.drawCenteredText(mat, "Рандомный", inputX + btnW + 6.0f + (btnW / 2.0f), btnY + 6.0f, 7.0f, new Color(255, 255, 255, 255));
        if (this.errorMessage != null) {
            float fade = class_3532.method_15363(1.0f - ((System.currentTimeMillis() - this.errorTime) / 2000.0f), 0.0f, 1.0f);
            if (fade > 0.0f) {
                Fonts.PS_MEDIUM.drawCenteredText(mat, this.errorMessage, px + (280.0f / 2.0f), btnY + 26.0f, 6.5f, new Color(255, 80, 80, (int) (fade * 255.0f)));
            } else {
                this.errorMessage = null;
            }
        }
        float listY = btnY + (this.errorMessage != null ? 38.0f : 32.0f);
        float listH = (340.0f - (listY - py)) - 12.0f;
        List<String> alts = AltManager.getInstance().getAlts();
        int current = AltManager.getInstance().getCurrentIndex();
        int maxScroll = Math.max(0, alts.size() - VISIBLE_ALTS);
        this.scrollOffset = class_3532.method_15340(this.scrollOffset, 0, maxScroll);
        int i = this.scrollOffset;
        while (i < Math.min(alts.size(), this.scrollOffset + VISIBLE_ALTS)) {
            float iy = listY + ((i - this.scrollOffset) * ITEM_H);
            boolean isCurrent = i == current;
            boolean hover = ((float) mouseX) >= inputX && ((float) mouseX) <= inputX + inputW && ((float) mouseY) >= iy && ((float) mouseY) <= (iy + ITEM_H) - 2.0f;
            if (isCurrent) {
                color = UIColors.gradient(0, 60);
            } else if (hover) {
                color = new Color(35, 35, 35, 200);
            } else {
                color = new Color(22, 22, 22, 200);
            }
            Color itemBg = color;
            RenderUtil.BLUR_RECT.draw(mat, inputX, iy, inputW, 26.0f, 5.0f, itemBg);
            if (isCurrent) {
                Color c1 = UIColors.gradient(0, 200);
                Color c2 = UIColors.gradient(90, 200);
                RenderUtil.GRADIENT_RECT.draw(mat, inputX, iy, inputW, 1.5f, 0.0f, c1, c2, c1, c2);
            }
            Fonts.PS_MEDIUM.drawText(mat, alts.get(i), inputX + 10.0f, iy + 8.0f, 7.5f, isCurrent ? UIColors.textColor(255) : new Color(180, 180, 180, 255));
            float delX = (inputX + inputW) - 24.0f;
            float delY = iy + 4.0f;
            boolean delHover = ((float) mouseX) >= delX && ((float) mouseX) <= delX + 18.0f && ((float) mouseY) >= delY && ((float) mouseY) <= delY + 18.0f;
            Color delColor1 = delHover ? new Color(220, 50, 50, 200) : new Color(160, 40, 40, 160);
            Color delColor2 = delHover ? new Color(180, 30, 30, 200) : new Color(120, 30, 30, 160);
            RenderUtil.GRADIENT_RECT.draw(mat, delX, delY, 18.0f, 18.0f, 3.0f, delColor1, delColor2, delColor2, delColor1);
            Fonts.PS_MEDIUM.drawCenteredText(mat, "✕", delX + (18.0f / 2.0f), delY + 6.0f, 7.0f, new Color(255, 255, 255, 240));
            i++;
        }
        if (alts.size() > VISIBLE_ALTS) {
            float scrollBarH = (listH * 8.0f) / alts.size();
            float scrollBarY = listY + (((listH - scrollBarH) * this.scrollOffset) / maxScroll);
            RenderUtil.BLUR_RECT.draw(mat, inputX + inputW + 3.0f, scrollBarY, 4.0f, scrollBarH, 2.0f, UIColors.gradient(0, 180));
        }
        float backX = px + 12.0f;
        float backY = (py + 340.0f) - ITEM_H;
        boolean backHover = ((float) mouseX) >= backX && ((float) mouseX) <= backX + 60.0f && ((float) mouseY) >= backY && ((float) mouseY) <= backY + 20.0f;
        RenderUtil.BLUR_RECT.draw(mat, backX, backY, 60.0f, 20.0f, 5.0f, backHover ? new Color(40, 40, 40, 220) : new Color(25, 25, 25, 200));
        Fonts.PS_MEDIUM.drawCenteredText(mat, "Назад", backX + 30.0f, backY + 6.0f, 7.0f, UIColors.textColor(220));
    }

    public boolean method_25402(double mouseX, double mouseY, int button) {
        float sw = this.field_22789;
        float sh = this.field_22790;
        float px = (sw - 280.0f) / 2.0f;
        float py = (sh - 340.0f) / 2.0f;
        float inputX = px + 12.0f;
        float inputY = py + 36.0f;
        float inputW = 280.0f - 24.0f;
        float btnY = inputY + 24.0f + 8.0f;
        float btnW = (inputW - 6.0f) / 2.0f;
        float listY = btnY + 38.0f;
        this.inputFocused = mouseX >= ((double) inputX) && mouseX <= ((double) (inputX + inputW)) && mouseY >= ((double) inputY) && mouseY <= ((double) (inputY + 24.0f));
        if (mouseX >= inputX && mouseX <= inputX + btnW && mouseY >= btnY && mouseY <= btnY + 20.0f) {
            if (AltManager.isValidNick(this.inputText)) {
                boolean added = AltManager.getInstance().addAlt(this.inputText);
                if (!added) {
                    showError("Ник уже существует или список полон");
                    return true;
                }
                this.inputText = "";
                return true;
            }
            if (this.inputText.length() < 3) {
                showError("Минимум 3 символа");
                return true;
            }
            if (this.inputText.length() > 16) {
                showError("Максимум 16 символов");
                return true;
            }
            showError("Только a-z, A-Z, 0-9, _");
            return true;
        }
        if (mouseX >= inputX + btnW + 6.0f && mouseX <= inputX + inputW && mouseY >= btnY && mouseY <= btnY + 20.0f) {
            String nick = AltManager.getInstance().generateRandomNick();
            boolean added2 = AltManager.getInstance().addAlt(nick);
            if (added2) {
                int idx = AltManager.getInstance().getAlts().size() - 1;
                AltManager.getInstance().setCurrentIndex(idx);
                applyAlt(nick);
                return true;
            }
            showError("Не удалось добавить ник");
            return true;
        }
        List<String> alts = AltManager.getInstance().getAlts();
        for (int i = this.scrollOffset; i < Math.min(alts.size(), this.scrollOffset + VISIBLE_ALTS); i++) {
            float iy = listY + ((i - this.scrollOffset) * ITEM_H);
            float delX = (inputX + inputW) - 24.0f;
            float delY = iy + 4.0f;
            if (mouseX >= delX && mouseX <= delX + 18.0f && mouseY >= delY && mouseY <= delY + 18.0f) {
                AltManager.getInstance().removeAlt(i);
                return true;
            }
            if (mouseX >= inputX && mouseX <= (inputX + inputW) - 26.0f && mouseY >= iy && mouseY <= (iy + ITEM_H) - 2.0f) {
                long now = System.currentTimeMillis();
                if (this.lastClickIndex == i && now - this.lastClickTime < 500) {
                    AltManager.getInstance().setCurrentIndex(i);
                    applyAlt(alts.get(i));
                    this.lastClickIndex = -1;
                    return true;
                }
                AltManager.getInstance().setCurrentIndex(i);
                this.lastClickIndex = i;
                this.lastClickTime = now;
                return true;
            }
        }
        float backX = px + 12.0f;
        float backY = (py + 340.0f) - ITEM_H;
        if (mouseX >= backX && mouseX <= backX + 60.0f && mouseY >= backY && mouseY <= backY + 20.0f) {
            this.field_22787.method_1507(new class_442());
            return true;
        }
        return super.method_25402(mouseX, mouseY, button);
    }

    public boolean method_25401(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        this.scrollOffset = class_3532.method_15340(this.scrollOffset - ((int) verticalAmount), 0, Math.max(0, AltManager.getInstance().getAlts().size() - VISIBLE_ALTS));
        return true;
    }

    public boolean method_25404(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256) {
            this.field_22787.method_1507(new class_442());
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
                return true;
            }
            showError("Некорректный ник");
            return true;
        }
        return super.method_25404(keyCode, scanCode, modifiers);
    }

    public boolean method_25400(char chr, int modifiers) {
        if (!this.inputFocused || !AltManager.isValidChar(chr) || this.inputText.length() >= 16) {
            return false;
        }
        this.inputText += chr;
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
            Field[] declaredFields = class_310.class.getDeclaredFields();
            int length = declaredFields.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                Field f = declaredFields[i];
                if (f.getType() != class_320.class) {
                    i++;
                } else {
                    sessionField = f;
                    break;
                }
            }
            if (sessionField == null) {
                showError("Поле Session не найдено в MinecraftClient");
                return;
            }
            sessionField.setAccessible(true);
            sessionField.set(this.field_22787, newSession);
            showError("§aПрименён: " + nick);
        } catch (IllegalAccessException e) {
            showError("IllegalAccess: " + e.getMessage());
        } catch (Exception e2) {
            showError("Ошибка (" + e2.getClass().getSimpleName() + "): " + e2.getMessage());
        } catch (InaccessibleObjectException e3) {
            showError("Нет доступа к полю: " + e3.getMessage());
        }
    }

    public boolean method_25421() {
        return false;
    }
}
