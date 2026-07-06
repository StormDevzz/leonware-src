// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.ui.widget.overlay;

import sweetie.leonware.api.utils.render.fonts.Font;
import sweetie.leonware.client.features.modules.other.StreamerModule;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.render.RenderUtil;
import net.minecraft.class_4597;
import net.minecraft.class_327;
import sweetie.leonware.api.system.backend.ClientInfo;
import net.minecraft.class_4587;
import java.awt.Color;
import sweetie.leonware.api.system.files.FileUtil;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ColorSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import java.util.Random;
import net.minecraft.class_2960;
import sweetie.leonware.client.ui.widget.Widget;

public class WatermarkWidget extends Widget
{
    private float watermarkWidth;
    private final class_2960 leonTexture;
    private final Random random;
    private String fullBred;
    private String displayBred;
    private int charIndex;
    private long lastUpdate;
    private boolean deleting;
    private long pauseTime;
    private int phraseIndex;
    private final String cryptoChars = "!@#$%";
    public final ModeSetting style;
    public final SliderSetting akrienScale;
    public final ColorSetting akrienColor;
    public final SliderSetting akrienSubtitleOffset;
    public final ModeSetting mode;
    public final BooleanSetting crypto;
    private final String[] bredPhrases;
    
    public WatermarkWidget() {
        super(3.0f, 3.0f);
        this.watermarkWidth = 0.0f;
        this.leonTexture = FileUtil.getImage("leon/leon");
        this.random = new Random();
        this.fullBred = "\u0410\u043d\u0430\u043b\u0438\u0437 \u0441\u0438\u0441\u0442\u0435\u043c\u044b";
        this.displayBred = "";
        this.charIndex = 0;
        this.lastUpdate = 0L;
        this.deleting = false;
        this.pauseTime = 0L;
        this.phraseIndex = 0;
        this.style = new ModeSetting("\u0421\u0442\u0438\u043b\u044c").values("\u041b\u0435\u043e\u043d", "\u0410\u043a\u0440\u0438\u0435\u043d", "\u0410\u043a\u0440\u0438\u0435\u043d2").value("\u041b\u0435\u043e\u043d");
        this.akrienScale = new SliderSetting("\u0420\u0430\u0437\u043c\u0435\u0440").value(2.5f).range(0.5f, 3.0f).step(0.1f).setVisible(() -> this.style.is("\u0410\u043a\u0440\u0438\u0435\u043d2"));
        this.akrienColor = new ColorSetting("\u0426\u0432\u0435\u0442").value(new Color(49151)).setVisible(() -> this.style.is("\u0410\u043a\u0440\u0438\u0435\u043d2"));
        this.akrienSubtitleOffset = new SliderSetting("\u041f\u043e\u043b\u043e\u0436\u0435\u043d\u0438\u0435 \u0441\u0430\u0431\u0442\u0438\u0442\u043b\u0430").value(2.0f).range(-20.0f, 40.0f).step(0.5f).setVisible(() -> this.style.is("\u0410\u043a\u0440\u0438\u0435\u043d2"));
        this.mode = new ModeSetting("\u0420\u0435\u0436\u0438\u043c \u041b\u0435\u043e\u043d\u0430").values("\u0420\u0430\u0441\u0442\u044f\u043d\u0443\u0442\u044b\u0439", "\u0423\u0437\u043a\u0438\u0439").value("\u0423\u0437\u043a\u0438\u0439").setVisible(() -> this.style.is("\u041b\u0435\u043e\u043d"));
        this.crypto = new BooleanSetting("\u0428\u0438\u0444\u0440\u043e\u0432\u0430\u043d\u0438\u0435").value(true).setVisible(() -> this.style.is("\u041b\u0435\u043e\u043d"));
        this.bredPhrases = new String[] { "\u0418\u0434\u0435\u0442 \u043f\u0440\u043e\u043f\u0435\u043d", "\u0410\u043d\u0430\u043b\u0438\u0437\u0438\u0440\u0443\u044e \u043f\u0432\u043f", "\u041f\u043e\u0434\u043c\u0435\u043d\u0430 \u043f\u0430\u043a\u0435\u0442\u043e\u0432", "\u0411\u0430\u0439\u043f\u0430\u0441\u0438\u043c \u0430\u043d\u0442\u0438\u0447\u0438\u0442", "\u0421\u043d\u043e\u0441\u0438\u043c \u043a\u0430\u0431\u0438\u043d\u044b", "\u041f\u043e\u0438\u0441\u043a \u0434\u044b\u0440 \u0432 \u044f\u0434\u0440\u0435", "\u0418\u043d\u0436\u0435\u043a\u0442\u0438\u043c \u043b\u0435\u043e\u043d\u0430", "\u0421\u043b\u0430\u0434\u043a\u0438\u0445 \u0441\u043d\u043e\u0432", "\u0421\u043a\u0430\u043d\u0438\u0440\u043e\u0432\u0430\u043d\u0438\u0435 \u043c\u0438\u0440\u0430", "\u041f\u0430\u0440\u0441\u0438\u043c \u0441\u0443\u0449\u043d\u043e\u0441\u0442\u0435\u0439", "\u041a\u0440\u0438\u0442\u044b \u0431\u0430\u0439\u043f\u0430\u0441\u043d\u0443\u0442\u044b", "\u0417\u0430\u0433\u0440\u0443\u0437\u043a\u0430 \u043a\u043e\u043d\u0444\u0438\u0433\u0430", "\u0427\u0438\u0441\u0442\u0438\u043c \u043b\u043e\u0433\u0438", "\u0410\u0440\u0435\u0441\u044b \u043f\u043b\u0430\u0447\u0443\u0442", "\u0420\u0430\u0437\u043d\u043e\u0441 \u0441\u0435\u0440\u0432\u0435\u0440\u0430", "\u0410\u0443\u0440\u0430 \u043d\u0430\u0432\u043e\u0434\u0438\u0442\u0441\u044f", "jvm.dll \u0445\u0430\u043a\u043d\u0443\u0442\u0430", "\u041b\u0435\u043e\u043d \u043e\u0434\u043e\u0431\u0440\u044f\u0435\u0442" };
    }
    
    @Override
    public String getName() {
        return "Watermark";
    }
    
    @Override
    public void render(final class_4587 matrixStack) {
        if (this.style.is("\u0410\u043a\u0440\u0438\u0435\u043d2")) {
            this.renderAkrien2(matrixStack);
        }
        else if (this.style.is("\u0410\u043a\u0440\u0438\u0435\u043d")) {
            this.renderAkrien(matrixStack);
        }
        else {
            this.renderLeon(matrixStack);
        }
    }
    
    private void renderAkrien2(final class_4587 matrixStack) {
        final float x = this.getDraggable().getX();
        final float y = this.getDraggable().getY();
        final String title = "Akrien";
        final String subtitle = "Free v" + ClientInfo.VERSION;
        final float scale = this.akrienScale.getValue();
        final Color titleColor = this.akrienColor.getValue();
        final float padX = this.scaled(4.0f);
        final float padTop = this.scaled(3.0f);
        final float titleScale = scale;
        final float subtitleScale = scale * 0.55f;
        final float subtitleOffsetPx = this.akrienSubtitleOffset.getValue();
        final float titleW = WatermarkWidget.mc.field_1772.method_1727(title) * titleScale;
        final float subtitleW = WatermarkWidget.mc.field_1772.method_1727(subtitle) * subtitleScale;
        final float bgW = Math.max(titleW, subtitleW) + padX * 2.0f;
        final float titleH = 9.0f * titleScale;
        final float subtitleH = 9.0f * subtitleScale;
        final float bgH = padTop + titleH + subtitleOffsetPx + subtitleH + padTop;
        final class_4597.class_4598 immediate = WatermarkWidget.mc.method_22940().method_23000();
        matrixStack.method_22903();
        matrixStack.method_46416(x + padX, y + padTop, 0.0f);
        matrixStack.method_22905(titleScale, titleScale, 1.0f);
        WatermarkWidget.mc.field_1772.method_27521(title, 0.0f, 0.0f, titleColor.getRGB(), true, matrixStack.method_23760().method_23761(), (class_4597)immediate, class_327.class_6415.field_33993, 0, 15728880);
        immediate.method_22993();
        matrixStack.method_22909();
        matrixStack.method_22903();
        matrixStack.method_46416(x + padX, y + padTop + titleH + subtitleOffsetPx, 0.0f);
        matrixStack.method_22905(subtitleScale, subtitleScale, 1.0f);
        WatermarkWidget.mc.field_1772.method_27521(subtitle, 0.0f, 0.0f, Color.WHITE.getRGB(), true, matrixStack.method_23760().method_23761(), (class_4597)immediate, class_327.class_6415.field_33993, 0, 15728880);
        immediate.method_22993();
        matrixStack.method_22909();
        this.getDraggable().setWidth(bgW);
        this.getDraggable().setHeight(bgH);
    }
    
    private void renderAkrien(final class_4587 matrixStack) {
        final float x = this.getDraggable().getX();
        final float y = this.getDraggable().getY();
        final String nick = this.getNick();
        final String serverIp = this.getServerIp();
        final int fps = WatermarkWidget.mc.method_47599();
        final String prefix = "LeonWare Premium v" + ClientInfo.VERSION + " | ";
        final String suffix = " | " + fps + "fps | " + serverIp;
        final String text = prefix + nick + suffix;
        final float fontSize = this.scaled(7.5f);
        final Font font = this.getMediumFont();
        final float textWidth = font.getWidth(text, fontSize);
        final float width = textWidth + this.scaled(6.0f);
        final float height = this.scaled(12.0f);
        final float internalGap = this.scaled(3.0f);
        RenderUtil.BLUR_RECT.draw(matrixStack, x, y, width, height, 0.0f, new Color(20, 20, 20));
        RenderUtil.RECT.draw(matrixStack, x, y, width, height, 0.0f, new Color(20, 20, 20));
        RenderUtil.RECT.draw(matrixStack, x, y - this.scaled(1.2f), width, this.scaled(1.4f), 0.0f, UIColors.primary());
        final StreamerModule streamer = StreamerModule.getInstance();
        final boolean rainbow = streamer.isEnabled() && streamer.getHide().isEnabled("Name") && streamer.getHide().isEnabled("Rainbow");
        if (rainbow) {
            this.drawRainbowNickInAkrien(matrixStack, font, fontSize, x, y, internalGap, nick, serverIp, fps);
        }
        else {
            font.drawText(matrixStack, text, x + internalGap, y + internalGap, fontSize, Color.WHITE);
        }
        this.getDraggable().setWidth(width);
        this.getDraggable().setHeight(height);
    }
    
    private void drawRainbowNickInAkrien(final class_4587 matrixStack, final Font font, final float fontSize, final float x, final float y, final float gap, final String nick, final String serverIp, final int fps) {
        final String prefix = "LeonWare Premium v" + ClientInfo.VERSION + " | ";
        final String suffix = " | " + fps + "fps | " + serverIp;
        float drawX = x + gap;
        final float drawY = y + gap;
        font.drawText(matrixStack, prefix, drawX, drawY, fontSize, Color.WHITE);
        drawX += font.getWidth(prefix, fontSize);
        final char[] rainbow = { 'c', '6', 'e', 'a', 'b', '3', '9', 'd' };
        final long offset = System.currentTimeMillis() / 100L % rainbow.length;
        for (int i = 0; i < nick.length(); ++i) {
            final char color = rainbow[(int)((i + offset) % rainbow.length)];
            final String letter = "§" + color + nick.charAt(i);
            font.drawText(matrixStack, letter, drawX, drawY, fontSize, Color.WHITE);
            drawX += font.getWidth(String.valueOf(nick.charAt(i)), fontSize);
        }
        font.drawText(matrixStack, suffix, drawX, drawY, fontSize, Color.WHITE);
    }
    
    private void renderLeon(final class_4587 matrixStack) {
        final float x = this.getDraggable().getX();
        final float y = this.getDraggable().getY();
        final float gap = this.getGap();
        final float headHeight;
        final float targetHeight = headHeight = this.scaled(40.0f);
        float headWidth;
        if (this.mode.is("\u0420\u0430\u0441\u0442\u044f\u043d\u0443\u0442\u044b\u0439")) {
            headWidth = targetHeight;
        }
        else {
            final float aspectRatio = 0.6370482f;
            headWidth = targetHeight * aspectRatio;
        }
        final boolean isRight = x > WatermarkWidget.mc.method_22683().method_4486() / 2.0f;
        final float headX = isRight ? (x + this.getDraggable().getWidth() - headWidth) : x;
        RenderUtil.TEXTURE_RECT.drawTexture(matrixStack, this.leonTexture, headX, y, headWidth, headHeight, Color.WHITE);
        final float pillsStartX = isRight ? x : (x + headWidth + gap);
        final float namePillX = isRight ? (headX - gap - this.watermarkWidth) : pillsStartX;
        final float[] namePill = this.drawPill(matrixStack, namePillX, y, this.getClientName() + this.getClientVersion());
        this.watermarkWidth = namePill[2];
        final float verticalSpacing = 1.7f;
        final float secondPillY = y + namePill[3] + verticalSpacing;
        final float[] ipPill = this.drawPill(matrixStack, pillsStartX, secondPillY, this.getServerIp());
        this.updateBredAnimation();
        final float thirdPillY = secondPillY + ipPill[3] + verticalSpacing;
        final float[] bredPill = this.drawPill(matrixStack, pillsStartX, thirdPillY, this.displayBred);
        final float maxPillWidth = Math.max(namePill[2], Math.max(ipPill[2], bredPill[2]));
        final float totalWidth = headWidth + gap + maxPillWidth;
        final float totalHeight = Math.max(headHeight, thirdPillY + bredPill[3] - y);
        this.getDraggable().setWidth(totalWidth);
        this.getDraggable().setHeight(totalHeight);
    }
    
    private String getNick() {
        final StreamerModule streamer = StreamerModule.getInstance();
        if (streamer.isEnabled() && streamer.getHide().isEnabled("Name")) {
            return "LeonWare";
        }
        return WatermarkWidget.mc.method_1548().method_1676();
    }
    
    private void updateBredAnimation() {
        final long now = System.currentTimeMillis();
        if (this.pauseTime > now) {
            return;
        }
        if (now - this.lastUpdate > 70L) {
            if (!this.deleting) {
                if (this.charIndex < this.fullBred.length()) {
                    ++this.charIndex;
                    final String currentText = this.fullBred.substring(0, this.charIndex);
                    if (this.crypto.getValue() && this.charIndex < this.fullBred.length()) {
                        this.displayBred = currentText.substring(0, this.charIndex - 1) + "!@#$%".charAt(this.random.nextInt("!@#$%".length()));
                    }
                    else {
                        this.displayBred = currentText;
                    }
                }
                else {
                    this.displayBred = this.fullBred;
                    this.deleting = true;
                    this.pauseTime = now + 700L;
                }
            }
            else if (this.charIndex > 0) {
                --this.charIndex;
                this.displayBred = this.fullBred.substring(0, this.charIndex);
            }
            else {
                this.deleting = false;
                this.phraseIndex = (this.phraseIndex + 1) % this.bredPhrases.length;
                this.fullBred = this.bredPhrases[this.phraseIndex];
                this.pauseTime = now + 200L;
            }
            this.lastUpdate = now;
        }
    }
    
    private float[] drawPill(final class_4587 matrixStack, final float x, final float y, String content) {
        if (content.isEmpty()) {
            content = " ";
        }
        final boolean watermark = content.contains("LeonWare");
        final Font font = watermark ? this.getSemiBoldFont() : this.getMediumFont();
        final float fontSize = this.scaled(7.5f);
        final float contentWidth = font.getWidth(content, fontSize);
        final float internalGap = this.getGap() * 0.9f;
        final float backgroundWidth = contentWidth + internalGap * 2.0f;
        final float backgroundHeight = fontSize + internalGap * 2.0f;
        final float round = backgroundHeight * 0.3f;
        RenderUtil.BLUR_RECT.draw(matrixStack, x, y, backgroundWidth, backgroundHeight, round, UIColors.widgetBlur());
        if (!watermark) {
            font.drawText(matrixStack, content, x + internalGap, y + internalGap, fontSize, UIColors.textColor());
        }
        else {
            font.drawGradientText(matrixStack, this.getClientName(), x + internalGap, y + internalGap, fontSize, UIColors.primary(), UIColors.secondary(), contentWidth / 4.0f);
            font.drawText(matrixStack, this.getClientVersion(), x + internalGap + font.getWidth(this.getClientName(), fontSize), y + internalGap, fontSize, UIColors.inactiveTextColor());
        }
        return new float[] { x, y, backgroundWidth, backgroundHeight };
    }
    
    private String getServerIp() {
        if (WatermarkWidget.mc.method_47392()) {
            return "\u041e\u0434\u0438\u043d\u043e\u0447\u043d\u0430\u044f \u0438\u0433\u0440\u0430";
        }
        if (WatermarkWidget.mc.method_1558() != null) {
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
