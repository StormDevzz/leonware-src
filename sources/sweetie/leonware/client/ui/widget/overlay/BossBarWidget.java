package sweetie.leonware.client.ui.widget.overlay;

import java.awt.Color;
import java.util.Map;
import java.util.UUID;
import net.minecraft.class_1259;
import net.minecraft.class_337;
import net.minecraft.class_345;
import net.minecraft.class_4587;
import org.newsclub.net.unix.AFVSOCKSocketAddress;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.client.ui.widget.Widget;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/ui/widget/overlay/BossBarWidget.class */
public class BossBarWidget extends Widget {
    private float widgetWidth;
    private float widgetHeight;

    public BossBarWidget() {
        super(3.0f, 50.0f);
        this.widgetWidth = 0.0f;
        this.widgetHeight = 0.0f;
    }

    @Override // sweetie.leonware.client.ui.widget.Widget
    public String getName() {
        return "BossBar";
    }

    @Override // sweetie.leonware.api.system.interfaces.IRenderer
    public void render(class_4587 matrixStack) {
        float x = getDraggable().getX();
        float y = getDraggable().getY();
        float gap = getGap();
        if (mc.field_1724 == null || mc.field_1687 == null || mc.field_1705 == null) {
            getDraggable().setWidth(0.0f);
            getDraggable().setHeight(0.0f);
            return;
        }
        class_337 bossBarHud = mc.field_1705.method_1740();
        if (bossBarHud == null) {
            getDraggable().setWidth(0.0f);
            getDraggable().setHeight(0.0f);
            return;
        }
        Map<UUID, class_345> bossBars = bossBarHud.field_2060;
        if (bossBars.isEmpty()) {
            getDraggable().setWidth(0.0f);
            getDraggable().setHeight(0.0f);
            return;
        }
        float currentY = y;
        float maxWidth = 0.0f;
        float totalHeight = 0.0f;
        for (class_345 bossBar : bossBars.values()) {
            float[] barDimensions = renderBossBar(matrixStack, x, currentY, bossBar);
            maxWidth = Math.max(maxWidth, barDimensions[2]);
            totalHeight += barDimensions[3] + gap;
            currentY += barDimensions[3] + gap;
        }
        if (totalHeight > 0.0f) {
            totalHeight -= gap;
        }
        this.widgetWidth = maxWidth;
        this.widgetHeight = totalHeight;
        getDraggable().setWidth(this.widgetWidth);
        getDraggable().setHeight(this.widgetHeight);
    }

    private float[] renderBossBar(class_4587 matrixStack, float x, float y, class_345 bossBar) {
        float fontSize = scaled(6.0f);
        float barHeight = scaled(6.0f);
        float barWidth = scaled(120.0f);
        float gap = getGap() * 0.8f;
        String name = bossBar.method_5414().getString();
        float progress = bossBar.method_5412();
        Color barColor = getBossBarColor(bossBar.method_5420());
        Color backgroundColor = new Color(12, 12, 18, 220);
        float textWidth = getMediumFont().getWidth(name, fontSize);
        float contentWidth = Math.max(barWidth + (gap * 2.0f), textWidth + (gap * 3.0f));
        float backgroundHeight = fontSize + barHeight + (gap * 2.0f);
        float round = backgroundHeight * 0.3f;
        RenderUtil.BLUR_RECT.draw(matrixStack, x, y, contentWidth, backgroundHeight, round, backgroundColor);
        float textX = x + ((contentWidth - textWidth) / 2.0f);
        float textY = y + gap;
        getMediumFont().drawText(matrixStack, name, textX, textY, fontSize, Color.WHITE);
        float barX = x + ((contentWidth - barWidth) / 2.0f);
        float barY = y + fontSize + (gap * 1.5f);
        float barRound = barHeight * 0.2f;
        RenderUtil.BLUR_RECT.draw(matrixStack, barX, barY, barWidth, barHeight, barRound, new Color(20, 20, 25, 180));
        float progressWidth = barWidth * progress;
        if (progressWidth > 0.0f) {
            RenderUtil.BLUR_RECT.draw(matrixStack, barX, barY, progressWidth, barHeight, barRound, barColor);
        }
        return new float[]{x, y, contentWidth, backgroundHeight};
    }

    /* JADX INFO: renamed from: sweetie.leonware.client.ui.widget.overlay.BossBarWidget$1, reason: invalid class name */
    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/ui/widget/overlay/BossBarWidget$1.class */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$net$minecraft$entity$boss$BossBar$Color = new int[class_1259.class_1260.values().length];

        static {
            try {
                $SwitchMap$net$minecraft$entity$boss$BossBar$Color[class_1259.class_1260.field_5788.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$net$minecraft$entity$boss$BossBar$Color[class_1259.class_1260.field_5780.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$net$minecraft$entity$boss$BossBar$Color[class_1259.class_1260.field_5784.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$net$minecraft$entity$boss$BossBar$Color[class_1259.class_1260.field_5785.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$net$minecraft$entity$boss$BossBar$Color[class_1259.class_1260.field_5782.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$net$minecraft$entity$boss$BossBar$Color[class_1259.class_1260.field_5783.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$net$minecraft$entity$boss$BossBar$Color[class_1259.class_1260.field_5786.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
        }
    }

    private Color getBossBarColor(class_1259.class_1260 color) {
        switch (AnonymousClass1.$SwitchMap$net$minecraft$entity$boss$BossBar$Color[color.ordinal()]) {
            case 1:
                return new Color(255, 182, 193);
            case AFVSOCKSocketAddress.VMADDR_CID_HOST /* 2 */:
                return new Color(100, 149, 237);
            case 3:
                return new Color(255, 69, 69);
            case 4:
                return new Color(50, 205, 50);
            case 5:
                return new Color(255, 215, 0);
            case 6:
                return new Color(147, 112, 219);
            case 7:
                return new Color(255, 255, 255);
            default:
                return new Color(128, 128, 128);
        }
    }
}
