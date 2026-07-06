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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/ui/widget/overlay/WatermarkWidget.class */
public class WatermarkWidget extends Widget {
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

    /* JADX WARN: Type inference failed for: r1v20, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v23, types: [sweetie.leonware.api.module.setting.ColorSetting] */
    /* JADX WARN: Type inference failed for: r1v28, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v32, types: [sweetie.leonware.api.module.setting.ModeSetting] */
    /* JADX WARN: Type inference failed for: r1v35, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    public WatermarkWidget() {
        super(3.0f, 3.0f);
        this.watermarkWidth = 0.0f;
        this.leonTexture = FileUtil.getImage("leon/leon");
        this.random = new Random();
        this.fullBred = "Анализ системы";
        this.displayBred = "";
        this.charIndex = 0;
        this.lastUpdate = 0L;
        this.deleting = false;
        this.pauseTime = 0L;
        this.phraseIndex = 0;
        this.cryptoChars = "!@#$%";
        this.style = new ModeSetting("Стиль").values("Леон", "Акриен", "Акриен2").value("Леон");
        this.akrienScale = new SliderSetting("Размер").value(Float.valueOf(2.5f)).range(0.5f, 3.0f).step(0.1f).setVisible(() -> {
            return Boolean.valueOf(this.style.is("Акриен2"));
        });
        this.akrienColor = new ColorSetting("Цвет").value(new Color(49151)).setVisible(() -> {
            return Boolean.valueOf(this.style.is("Акриен2"));
        });
        this.akrienSubtitleOffset = new SliderSetting("Положение сабтитла").value(Float.valueOf(2.0f)).range(-20.0f, 40.0f).step(0.5f).setVisible(() -> {
            return Boolean.valueOf(this.style.is("Акриен2"));
        });
        this.mode = new ModeSetting("Режим Леона").values("Растянутый", "Узкий").value("Узкий").setVisible(() -> {
            return Boolean.valueOf(this.style.is("Леон"));
        });
        this.crypto = new BooleanSetting("Шифрование").value((Boolean) true).setVisible(() -> {
            return Boolean.valueOf(this.style.is("Леон"));
        });
        this.bredPhrases = new String[]{"Идет пропен", "Анализирую пвп", "Подмена пакетов", "Байпасим античит", "Сносим кабины", "Поиск дыр в ядре", "Инжектим леона", "Сладких снов", "Сканирование мира", "Парсим сущностей", "Криты байпаснуты", "Загрузка конфига", "Чистим логи", "Аресы плачут", "Разнос сервера", "Аура наводится", "jvm.dll хакнута", "Леон одобряет"};
    }

    @Override // sweetie.leonware.client.ui.widget.Widget
    public String getName() {
        return "Watermark";
    }

    @Override // sweetie.leonware.api.system.interfaces.IRenderer
    public void render(class_4587 matrixStack) {
        if (this.style.is("Акриен2")) {
            renderAkrien2(matrixStack);
        } else if (this.style.is("Акриен")) {
            renderAkrien(matrixStack);
        } else {
            renderLeon(matrixStack);
        }
    }

    private void renderAkrien2(class_4587 matrixStack) {
        float x = getDraggable().getX();
        float y = getDraggable().getY();
        String subtitle = "Free v" + ClientInfo.VERSION;
        float scale = this.akrienScale.getValue().floatValue();
        Color titleColor = this.akrienColor.getValue();
        float padX = scaled(4.0f);
        float padTop = scaled(3.0f);
        float subtitleScale = scale * 0.55f;
        float subtitleOffsetPx = this.akrienSubtitleOffset.getValue().floatValue();
        float titleW = mc.field_1772.method_1727("Akrien") * scale;
        float subtitleW = mc.field_1772.method_1727(subtitle) * subtitleScale;
        float bgW = Math.max(titleW, subtitleW) + (padX * 2.0f);
        float titleH = 9.0f * scale;
        float subtitleH = 9.0f * subtitleScale;
        float bgH = padTop + titleH + subtitleOffsetPx + subtitleH + padTop;
        class_4597.class_4598 immediate = mc.method_22940().method_23000();
        matrixStack.method_22903();
        matrixStack.method_46416(x + padX, y + padTop, 0.0f);
        matrixStack.method_22905(scale, scale, 1.0f);
        mc.field_1772.method_27521("Akrien", 0.0f, 0.0f, titleColor.getRGB(), true, matrixStack.method_23760().method_23761(), immediate, class_327.class_6415.field_33993, 0, 15728880);
        immediate.method_22993();
        matrixStack.method_22909();
        matrixStack.method_22903();
        matrixStack.method_46416(x + padX, y + padTop + titleH + subtitleOffsetPx, 0.0f);
        matrixStack.method_22905(subtitleScale, subtitleScale, 1.0f);
        mc.field_1772.method_27521(subtitle, 0.0f, 0.0f, Color.WHITE.getRGB(), true, matrixStack.method_23760().method_23761(), immediate, class_327.class_6415.field_33993, 0, 15728880);
        immediate.method_22993();
        matrixStack.method_22909();
        getDraggable().setWidth(bgW);
        getDraggable().setHeight(bgH);
    }

    private void renderAkrien(class_4587 matrixStack) {
        float x = getDraggable().getX();
        float y = getDraggable().getY();
        String nick = getNick();
        String serverIp = getServerIp();
        int fps = mc.method_47599();
        String prefix = "LeonWare Premium v" + ClientInfo.VERSION + " | ";
        String suffix = " | " + fps + "fps | " + serverIp;
        String text = prefix + nick + suffix;
        float fontSize = scaled(7.5f);
        Font font = getMediumFont();
        float textWidth = font.getWidth(text, fontSize);
        float width = textWidth + scaled(6.0f);
        float height = scaled(12.0f);
        float internalGap = scaled(3.0f);
        RenderUtil.BLUR_RECT.draw(matrixStack, x, y, width, height, 0.0f, new Color(20, 20, 20));
        RenderUtil.RECT.draw(matrixStack, x, y, width, height, 0.0f, new Color(20, 20, 20));
        RenderUtil.RECT.draw(matrixStack, x, y - scaled(1.2f), width, scaled(1.4f), 0.0f, UIColors.primary());
        StreamerModule streamer = StreamerModule.getInstance();
        boolean rainbow = streamer.isEnabled() && streamer.getHide().isEnabled("Name") && streamer.getHide().isEnabled("Rainbow");
        if (rainbow) {
            drawRainbowNickInAkrien(matrixStack, font, fontSize, x, y, internalGap, nick, serverIp, fps);
        } else {
            font.drawText(matrixStack, text, x + internalGap, y + internalGap, fontSize, Color.WHITE);
        }
        getDraggable().setWidth(width);
        getDraggable().setHeight(height);
    }

    private void drawRainbowNickInAkrien(class_4587 matrixStack, Font font, float fontSize, float x, float y, float gap, String nick, String serverIp, int fps) {
        String prefix = "LeonWare Premium v" + ClientInfo.VERSION + " | ";
        String suffix = " | " + fps + "fps | " + serverIp;
        float drawX = x + gap;
        float drawY = y + gap;
        font.drawText(matrixStack, prefix, drawX, drawY, fontSize, Color.WHITE);
        float drawX2 = drawX + font.getWidth(prefix, fontSize);
        char[] rainbow = {'c', '6', 'e', 'a', 'b', '3', '9', 'd'};
        long offset = (System.currentTimeMillis() / 100) % ((long) rainbow.length);
        for (int i = 0; i < nick.length(); i++) {
            char color = rainbow[(int) ((((long) i) + offset) % ((long) rainbow.length))];
            String letter = "§" + color + nick.charAt(i);
            font.drawText(matrixStack, letter, drawX2, drawY, fontSize, Color.WHITE);
            drawX2 += font.getWidth(String.valueOf(nick.charAt(i)), fontSize);
        }
        font.drawText(matrixStack, suffix, drawX2, drawY, fontSize, Color.WHITE);
    }

    private void renderLeon(class_4587 matrixStack) {
        float headWidth;
        float x = getDraggable().getX();
        float y = getDraggable().getY();
        float gap = getGap();
        float targetHeight = scaled(40.0f);
        if (this.mode.is("Растянутый")) {
            headWidth = targetHeight;
        } else {
            headWidth = targetHeight * 0.6370482f;
        }
        boolean isRight = x > ((float) mc.method_22683().method_4486()) / 2.0f;
        float headX = !isRight ? x : (x + getDraggable().getWidth()) - headWidth;
        RenderUtil.TEXTURE_RECT.drawTexture(matrixStack, this.leonTexture, headX, y, headWidth, targetHeight, Color.WHITE);
        float pillsStartX = !isRight ? x + headWidth + gap : x;
        float namePillX = !isRight ? pillsStartX : (headX - gap) - this.watermarkWidth;
        float[] namePill = drawPill(matrixStack, namePillX, y, getClientName() + getClientVersion());
        this.watermarkWidth = namePill[2];
        float secondPillY = y + namePill[3] + 1.7f;
        float[] ipPill = drawPill(matrixStack, pillsStartX, secondPillY, getServerIp());
        updateBredAnimation();
        float thirdPillY = secondPillY + ipPill[3] + 1.7f;
        float[] bredPill = drawPill(matrixStack, pillsStartX, thirdPillY, this.displayBred);
        float maxPillWidth = Math.max(namePill[2], Math.max(ipPill[2], bredPill[2]));
        float totalWidth = headWidth + gap + maxPillWidth;
        float totalHeight = Math.max(targetHeight, (thirdPillY + bredPill[3]) - y);
        getDraggable().setWidth(totalWidth);
        getDraggable().setHeight(totalHeight);
    }

    private String getNick() {
        StreamerModule streamer = StreamerModule.getInstance();
        if (streamer.isEnabled() && streamer.getHide().isEnabled("Name")) {
            return ClientInfo.NAME;
        }
        return mc.method_1548().method_1676();
    }

    private void updateBredAnimation() {
        long now = System.currentTimeMillis();
        if (this.pauseTime <= now && now - this.lastUpdate > 70) {
            if (!this.deleting) {
                if (this.charIndex < this.fullBred.length()) {
                    this.charIndex++;
                    String currentText = this.fullBred.substring(0, this.charIndex);
                    if (this.crypto.getValue().booleanValue() && this.charIndex < this.fullBred.length()) {
                        this.displayBred = currentText.substring(0, this.charIndex - 1) + "!@#$%".charAt(this.random.nextInt("!@#$%".length()));
                    } else {
                        this.displayBred = currentText;
                    }
                } else {
                    this.displayBred = this.fullBred;
                    this.deleting = true;
                    this.pauseTime = now + 700;
                }
            } else if (this.charIndex > 0) {
                this.charIndex--;
                this.displayBred = this.fullBred.substring(0, this.charIndex);
            } else {
                this.deleting = false;
                this.phraseIndex = (this.phraseIndex + 1) % this.bredPhrases.length;
                this.fullBred = this.bredPhrases[this.phraseIndex];
                this.pauseTime = now + 200;
            }
            this.lastUpdate = now;
        }
    }

    private float[] drawPill(class_4587 matrixStack, float x, float y, String content) {
        if (content.isEmpty()) {
            content = " ";
        }
        boolean watermark = content.contains(ClientInfo.NAME);
        Font font = !watermark ? getMediumFont() : getSemiBoldFont();
        float fontSize = scaled(7.5f);
        float contentWidth = font.getWidth(content, fontSize);
        float internalGap = getGap() * 0.9f;
        float backgroundWidth = contentWidth + (internalGap * 2.0f);
        float backgroundHeight = fontSize + (internalGap * 2.0f);
        float round = backgroundHeight * 0.3f;
        RenderUtil.BLUR_RECT.draw(matrixStack, x, y, backgroundWidth, backgroundHeight, round, UIColors.widgetBlur());
        if (!watermark) {
            font.drawText(matrixStack, content, x + internalGap, y + internalGap, fontSize, UIColors.textColor());
        } else {
            font.drawGradientText(matrixStack, getClientName(), x + internalGap, y + internalGap, fontSize, UIColors.primary(), UIColors.secondary(), contentWidth / 4.0f);
            font.drawText(matrixStack, getClientVersion(), x + internalGap + font.getWidth(getClientName(), fontSize), y + internalGap, fontSize, UIColors.inactiveTextColor());
        }
        return new float[]{x, y, backgroundWidth, backgroundHeight};
    }

    private String getServerIp() {
        return mc.method_47392() ? "Одиночная игра" : mc.method_1558() != null ? mc.method_1558().field_3761 : "Вы - нигде, а как?";
    }

    private String getClientVersion() {
        return " v" + ClientInfo.VERSION;
    }

    private String getClientName() {
        return ClientInfo.NAME;
    }
}
