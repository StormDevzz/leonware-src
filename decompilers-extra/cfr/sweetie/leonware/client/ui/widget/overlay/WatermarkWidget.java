/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2960
 *  net.minecraft.class_327$class_6415
 *  net.minecraft.class_4587
 *  net.minecraft.class_4597
 *  net.minecraft.class_4597$class_4598
 */
package sweetie.leonware.client.ui.widget.overlay;

import java.awt.Color;
import java.util.Random;
import net.minecraft.class_2960;
import net.minecraft.class_327;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ColorSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.system.backend.ClientInfo;
import sweetie.leonware.api.system.files.FileUtil;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.api.utils.render.fonts.Font;
import sweetie.leonware.client.features.modules.other.StreamerModule;
import sweetie.leonware.client.ui.widget.Widget;

public class WatermarkWidget
extends Widget {
    private float watermarkWidth = 0.0f;
    private final class_2960 leonTexture = FileUtil.getImage("leon/leon");
    private final Random random = new Random();
    private String fullBred = "\u0410\u043d\u0430\u043b\u0438\u0437 \u0441\u0438\u0441\u0442\u0435\u043c\u044b";
    private String displayBred = "";
    private int charIndex = 0;
    private long lastUpdate = 0L;
    private boolean deleting = false;
    private long pauseTime = 0L;
    private int phraseIndex = 0;
    private final String cryptoChars = "!@#$%";
    public final ModeSetting style = new ModeSetting("\u0421\u0442\u0438\u043b\u044c").values("\u041b\u0435\u043e\u043d", "\u0410\u043a\u0440\u0438\u0435\u043d", "\u0410\u043a\u0440\u0438\u0435\u043d2").value("\u041b\u0435\u043e\u043d");
    public final SliderSetting akrienScale = new SliderSetting("\u0420\u0430\u0437\u043c\u0435\u0440").value(Float.valueOf(2.5f)).range(0.5f, 3.0f).step(0.1f).setVisible(() -> this.style.is("\u0410\u043a\u0440\u0438\u0435\u043d2"));
    public final ColorSetting akrienColor = new ColorSetting("\u0426\u0432\u0435\u0442").value(new Color(49151)).setVisible(() -> this.style.is("\u0410\u043a\u0440\u0438\u0435\u043d2"));
    public final SliderSetting akrienSubtitleOffset = new SliderSetting("\u041f\u043e\u043b\u043e\u0436\u0435\u043d\u0438\u0435 \u0441\u0430\u0431\u0442\u0438\u0442\u043b\u0430").value(Float.valueOf(2.0f)).range(-20.0f, 40.0f).step(0.5f).setVisible(() -> this.style.is("\u0410\u043a\u0440\u0438\u0435\u043d2"));
    public final ModeSetting mode = new ModeSetting("\u0420\u0435\u0436\u0438\u043c \u041b\u0435\u043e\u043d\u0430").values("\u0420\u0430\u0441\u0442\u044f\u043d\u0443\u0442\u044b\u0439", "\u0423\u0437\u043a\u0438\u0439").value("\u0423\u0437\u043a\u0438\u0439").setVisible(() -> this.style.is("\u041b\u0435\u043e\u043d"));
    public final BooleanSetting crypto = new BooleanSetting("\u0428\u0438\u0444\u0440\u043e\u0432\u0430\u043d\u0438\u0435").value(true).setVisible(() -> this.style.is("\u041b\u0435\u043e\u043d"));
    private final String[] bredPhrases = new String[]{"\u0418\u0434\u0435\u0442 \u043f\u0440\u043e\u043f\u0435\u043d", "\u0410\u043d\u0430\u043b\u0438\u0437\u0438\u0440\u0443\u044e \u043f\u0432\u043f", "\u041f\u043e\u0434\u043c\u0435\u043d\u0430 \u043f\u0430\u043a\u0435\u0442\u043e\u0432", "\u0411\u0430\u0439\u043f\u0430\u0441\u0438\u043c \u0430\u043d\u0442\u0438\u0447\u0438\u0442", "\u0421\u043d\u043e\u0441\u0438\u043c \u043a\u0430\u0431\u0438\u043d\u044b", "\u041f\u043e\u0438\u0441\u043a \u0434\u044b\u0440 \u0432 \u044f\u0434\u0440\u0435", "\u0418\u043d\u0436\u0435\u043a\u0442\u0438\u043c \u043b\u0435\u043e\u043d\u0430", "\u0421\u043b\u0430\u0434\u043a\u0438\u0445 \u0441\u043d\u043e\u0432", "\u0421\u043a\u0430\u043d\u0438\u0440\u043e\u0432\u0430\u043d\u0438\u0435 \u043c\u0438\u0440\u0430", "\u041f\u0430\u0440\u0441\u0438\u043c \u0441\u0443\u0449\u043d\u043e\u0441\u0442\u0435\u0439", "\u041a\u0440\u0438\u0442\u044b \u0431\u0430\u0439\u043f\u0430\u0441\u043d\u0443\u0442\u044b", "\u0417\u0430\u0433\u0440\u0443\u0437\u043a\u0430 \u043a\u043e\u043d\u0444\u0438\u0433\u0430", "\u0427\u0438\u0441\u0442\u0438\u043c \u043b\u043e\u0433\u0438", "\u0410\u0440\u0435\u0441\u044b \u043f\u043b\u0430\u0447\u0443\u0442", "\u0420\u0430\u0437\u043d\u043e\u0441 \u0441\u0435\u0440\u0432\u0435\u0440\u0430", "\u0410\u0443\u0440\u0430 \u043d\u0430\u0432\u043e\u0434\u0438\u0442\u0441\u044f", "jvm.dll \u0445\u0430\u043a\u043d\u0443\u0442\u0430", "\u041b\u0435\u043e\u043d \u043e\u0434\u043e\u0431\u0440\u044f\u0435\u0442"};

    public WatermarkWidget() {
        super(3.0f, 3.0f);
    }

    @Override
    public String getName() {
        return "Watermark";
    }

    @Override
    public void render(class_4587 matrixStack) {
        if (this.style.is("\u0410\u043a\u0440\u0438\u0435\u043d2")) {
            this.renderAkrien2(matrixStack);
        } else if (this.style.is("\u0410\u043a\u0440\u0438\u0435\u043d")) {
            this.renderAkrien(matrixStack);
        } else {
            this.renderLeon(matrixStack);
        }
    }

    private void renderAkrien2(class_4587 matrixStack) {
        float x = this.getDraggable().getX();
        float y = this.getDraggable().getY();
        String title = "Akrien";
        String subtitle = "Free v" + ClientInfo.VERSION;
        float scale = ((Float)this.akrienScale.getValue()).floatValue();
        Color titleColor = (Color)this.akrienColor.getValue();
        float padX = this.scaled(4.0f);
        float padTop = this.scaled(3.0f);
        float titleScale = scale;
        float subtitleScale = scale * 0.55f;
        float subtitleOffsetPx = ((Float)this.akrienSubtitleOffset.getValue()).floatValue();
        float titleW = (float)WatermarkWidget.mc.field_1772.method_1727(title) * titleScale;
        float subtitleW = (float)WatermarkWidget.mc.field_1772.method_1727(subtitle) * subtitleScale;
        float bgW = Math.max(titleW, subtitleW) + padX * 2.0f;
        float titleH = 9.0f * titleScale;
        float subtitleH = 9.0f * subtitleScale;
        float bgH = padTop + titleH + subtitleOffsetPx + subtitleH + padTop;
        class_4597.class_4598 immediate = mc.method_22940().method_23000();
        matrixStack.method_22903();
        matrixStack.method_46416(x + padX, y + padTop, 0.0f);
        matrixStack.method_22905(titleScale, titleScale, 1.0f);
        WatermarkWidget.mc.field_1772.method_27521(title, 0.0f, 0.0f, titleColor.getRGB(), true, matrixStack.method_23760().method_23761(), (class_4597)immediate, class_327.class_6415.field_33993, 0, 0xF000F0);
        immediate.method_22993();
        matrixStack.method_22909();
        matrixStack.method_22903();
        matrixStack.method_46416(x + padX, y + padTop + titleH + subtitleOffsetPx, 0.0f);
        matrixStack.method_22905(subtitleScale, subtitleScale, 1.0f);
        WatermarkWidget.mc.field_1772.method_27521(subtitle, 0.0f, 0.0f, Color.WHITE.getRGB(), true, matrixStack.method_23760().method_23761(), (class_4597)immediate, class_327.class_6415.field_33993, 0, 0xF000F0);
        immediate.method_22993();
        matrixStack.method_22909();
        this.getDraggable().setWidth(bgW);
        this.getDraggable().setHeight(bgH);
    }

    private void renderAkrien(class_4587 matrixStack) {
        boolean rainbow;
        float x = this.getDraggable().getX();
        float y = this.getDraggable().getY();
        String nick = this.getNick();
        String serverIp = this.getServerIp();
        int fps = mc.method_47599();
        String prefix = "LeonWare Premium v" + ClientInfo.VERSION + " | ";
        String suffix = " | " + fps + "fps | " + serverIp;
        String text = prefix + nick + suffix;
        float fontSize = this.scaled(7.5f);
        Font font = this.getMediumFont();
        float textWidth = font.getWidth(text, fontSize);
        float width = textWidth + this.scaled(6.0f);
        float height = this.scaled(12.0f);
        float internalGap = this.scaled(3.0f);
        RenderUtil.BLUR_RECT.draw(matrixStack, x, y, width, height, 0.0f, new Color(20, 20, 20));
        RenderUtil.RECT.draw(matrixStack, x, y, width, height, 0.0f, new Color(20, 20, 20));
        RenderUtil.RECT.draw(matrixStack, x, y - this.scaled(1.2f), width, this.scaled(1.4f), 0.0f, UIColors.primary());
        StreamerModule streamer = StreamerModule.getInstance();
        boolean bl = rainbow = streamer.isEnabled() && streamer.getHide().isEnabled("Name") && streamer.getHide().isEnabled("Rainbow");
        if (rainbow) {
            this.drawRainbowNickInAkrien(matrixStack, font, fontSize, x, y, internalGap, nick, serverIp, fps);
        } else {
            font.drawText(matrixStack, text, x + internalGap, y + internalGap, fontSize, Color.WHITE);
        }
        this.getDraggable().setWidth(width);
        this.getDraggable().setHeight(height);
    }

    private void drawRainbowNickInAkrien(class_4587 matrixStack, Font font, float fontSize, float x, float y, float gap, String nick, String serverIp, int fps) {
        String prefix = "LeonWare Premium v" + ClientInfo.VERSION + " | ";
        String suffix = " | " + fps + "fps | " + serverIp;
        float drawX = x + gap;
        float drawY = y + gap;
        font.drawText(matrixStack, prefix, drawX, drawY, fontSize, Color.WHITE);
        drawX += font.getWidth(prefix, fontSize);
        char[] rainbow = new char[]{'c', '6', 'e', 'a', 'b', '3', '9', 'd'};
        long offset = System.currentTimeMillis() / 100L % (long)rainbow.length;
        for (int i = 0; i < nick.length(); ++i) {
            char color = rainbow[(int)(((long)i + offset) % (long)rainbow.length)];
            String letter = "\u00a7" + color + nick.charAt(i);
            font.drawText(matrixStack, letter, drawX, drawY, fontSize, Color.WHITE);
            drawX += font.getWidth(String.valueOf(nick.charAt(i)), fontSize);
        }
        font.drawText(matrixStack, suffix, drawX, drawY, fontSize, Color.WHITE);
    }

    private void renderLeon(class_4587 matrixStack) {
        float headWidth;
        float targetHeight;
        float x = this.getDraggable().getX();
        float y = this.getDraggable().getY();
        float gap = this.getGap();
        float headHeight = targetHeight = this.scaled(40.0f);
        if (this.mode.is("\u0420\u0430\u0441\u0442\u044f\u043d\u0443\u0442\u044b\u0439")) {
            headWidth = targetHeight;
        } else {
            float aspectRatio = 0.6370482f;
            headWidth = targetHeight * aspectRatio;
        }
        boolean isRight = x > (float)mc.method_22683().method_4486() / 2.0f;
        float headX = !isRight ? x : x + this.getDraggable().getWidth() - headWidth;
        RenderUtil.TEXTURE_RECT.drawTexture(matrixStack, this.leonTexture, headX, y, headWidth, headHeight, Color.WHITE);
        float pillsStartX = !isRight ? x + headWidth + gap : x;
        float namePillX = !isRight ? pillsStartX : headX - gap - this.watermarkWidth;
        float[] namePill = this.drawPill(matrixStack, namePillX, y, this.getClientName() + this.getClientVersion());
        this.watermarkWidth = namePill[2];
        float verticalSpacing = 1.7f;
        float secondPillY = y + namePill[3] + verticalSpacing;
        float[] ipPill = this.drawPill(matrixStack, pillsStartX, secondPillY, this.getServerIp());
        this.updateBredAnimation();
        float thirdPillY = secondPillY + ipPill[3] + verticalSpacing;
        float[] bredPill = this.drawPill(matrixStack, pillsStartX, thirdPillY, this.displayBred);
        float maxPillWidth = Math.max(namePill[2], Math.max(ipPill[2], bredPill[2]));
        float totalWidth = headWidth + gap + maxPillWidth;
        float totalHeight = Math.max(headHeight, thirdPillY + bredPill[3] - y);
        this.getDraggable().setWidth(totalWidth);
        this.getDraggable().setHeight(totalHeight);
    }

    private String getNick() {
        StreamerModule streamer = StreamerModule.getInstance();
        if (streamer.isEnabled() && streamer.getHide().isEnabled("Name")) {
            return "LeonWare";
        }
        return mc.method_1548().method_1676();
    }

    private void updateBredAnimation() {
        long now = System.currentTimeMillis();
        if (this.pauseTime > now) {
            return;
        }
        if (now - this.lastUpdate > 70L) {
            if (!this.deleting) {
                if (this.charIndex < this.fullBred.length()) {
                    ++this.charIndex;
                    String currentText = this.fullBred.substring(0, this.charIndex);
                    this.displayBred = ((Boolean)this.crypto.getValue()).booleanValue() && this.charIndex < this.fullBred.length() ? currentText.substring(0, this.charIndex - 1) + "!@#$%".charAt(this.random.nextInt("!@#$%".length())) : currentText;
                } else {
                    this.displayBred = this.fullBred;
                    this.deleting = true;
                    this.pauseTime = now + 700L;
                }
            } else if (this.charIndex > 0) {
                --this.charIndex;
                this.displayBred = this.fullBred.substring(0, this.charIndex);
            } else {
                this.deleting = false;
                this.phraseIndex = (this.phraseIndex + 1) % this.bredPhrases.length;
                this.fullBred = this.bredPhrases[this.phraseIndex];
                this.pauseTime = now + 200L;
            }
            this.lastUpdate = now;
        }
    }

    private float[] drawPill(class_4587 matrixStack, float x, float y, String content) {
        boolean watermark;
        if (content.isEmpty()) {
            content = " ";
        }
        Font font = !(watermark = content.contains("LeonWare")) ? this.getMediumFont() : this.getSemiBoldFont();
        float fontSize = this.scaled(7.5f);
        float contentWidth = font.getWidth(content, fontSize);
        float internalGap = this.getGap() * 0.9f;
        float backgroundWidth = contentWidth + internalGap * 2.0f;
        float backgroundHeight = fontSize + internalGap * 2.0f;
        float round = backgroundHeight * 0.3f;
        RenderUtil.BLUR_RECT.draw(matrixStack, x, y, backgroundWidth, backgroundHeight, round, UIColors.widgetBlur());
        if (!watermark) {
            font.drawText(matrixStack, content, x + internalGap, y + internalGap, fontSize, UIColors.textColor());
        } else {
            font.drawGradientText(matrixStack, this.getClientName(), x + internalGap, y + internalGap, fontSize, UIColors.primary(), UIColors.secondary(), contentWidth / 4.0f);
            font.drawText(matrixStack, this.getClientVersion(), x + internalGap + font.getWidth(this.getClientName(), fontSize), y + internalGap, fontSize, UIColors.inactiveTextColor());
        }
        return new float[]{x, y, backgroundWidth, backgroundHeight};
    }

    private String getServerIp() {
        if (mc.method_47392()) {
            return "\u041e\u0434\u0438\u043d\u043e\u0447\u043d\u0430\u044f \u0438\u0433\u0440\u0430";
        }
        if (mc.method_1558() != null) {
            return WatermarkWidget.mc.method_1558().field_3761;
        }
        return "\u0412\u044b - \u043d\u0438\u0433\u0434\u0435, \u0430 \u043a\u0430\u043a?";
    }

    private String getClientVersion() {
        return " v" + ClientInfo.VERSION;
    }

    private String getClientName() {
        return "LeonWare";
    }
}

