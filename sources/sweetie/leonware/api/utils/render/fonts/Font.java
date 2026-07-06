package sweetie.leonware.api.utils.render.fonts;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import lombok.Generated;
import net.minecraft.class_10149;
import net.minecraft.class_10156;
import net.minecraft.class_1044;
import net.minecraft.class_2561;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import net.minecraft.class_5944;
import org.joml.Matrix4f;
import sweetie.leonware.api.system.backend.Pair;
import sweetie.leonware.api.system.files.FileUtil;
import sweetie.leonware.api.utils.animation.Easing;
import sweetie.leonware.api.utils.color.ColorUtil;
import sweetie.leonware.api.utils.other.ReplaceUtil;
import sweetie.leonware.api.utils.other.TextUtil;
import sweetie.leonware.api.utils.render.ScissorUtil;
import sweetie.leonware.api.utils.render.fonts.FontData;
import sweetie.leonware.api.utils.render.fonts.MsdfGlyph;
import sweetie.leonware.client.services.RenderService;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/render/fonts/Font.class */
public final class Font {
    private static final class_10156 shaderKey = new class_10156(FileUtil.getShader("text"), class_290.field_1575, class_10149.field_53930);
    private final String name;
    private final class_1044 texture;
    private final FontData.AtlasData atlas;
    private final FontData.MetricsData metrics;
    private final Map<Integer, MsdfGlyph> glyphs;
    private final Map<Integer, Map<Integer, Float>> kernings;

    @Generated
    public FontData.AtlasData getAtlas() {
        return this.atlas;
    }

    @Generated
    public FontData.MetricsData getMetrics() {
        return this.metrics;
    }

    public Font(String name, class_1044 texture, FontData.AtlasData atlas, FontData.MetricsData metrics, Map<Integer, MsdfGlyph> glyphs, Map<Integer, Map<Integer, Float>> kernings) {
        this.name = name;
        this.texture = texture;
        this.atlas = atlas;
        this.metrics = metrics;
        this.glyphs = glyphs;
        this.kernings = kernings;
    }

    private Pair<Float, Float> offset(float x, float y) {
        float scale = RenderService.getInstance().getScale();
        float x1 = x;
        float y1 = y;
        boolean isPS = this.name.contains(Fonts.ps);
        boolean isSF = this.name.contains(Fonts.sf);
        if (isSF || isPS) {
            y1 -= scale;
            if (isPS) {
                x1 -= scale / 2.0f;
            }
        }
        return new Pair<>(Float.valueOf(x1), Float.valueOf(y1));
    }

    public void drawText(class_4587 matrixStack, class_2561 text, float x, float y, float size, float thickness, float smoothness, float spacing, int outlineColor, float outlineThickness) {
        if (text == null) {
            return;
        }
        try {
            Matrix4f matrix = matrixStack.method_23760().method_23761();
            start(outlineThickness, thickness, smoothness, outlineColor);
            class_287 builder = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1575);
            applyGlyphs(matrix, builder, TextUtil.parseTextToColoredGlyphs(ReplaceUtil.replaceSymbols(text)), size, (thickness + (outlineThickness * 0.5f)) * 0.5f * size, spacing, x, y + (getMetrics().baselineHeight() * size), 0.0f);
            end(builder);
        } catch (Exception e) {
        }
    }

    public void drawText(class_4587 matrixStack, String text, float x, float y, float size, float thickness, int color, int colorSecond, float offset, float smoothness, float spacing, int outlineColor, float outlineThickness) {
        try {
            Matrix4f matrix = matrixStack.method_23760().method_23761();
            String processedText = ReplaceUtil.protectedString(text);
            start(outlineThickness, thickness, smoothness, outlineColor);
            class_287 builder = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1575);
            if (TextUtil.hasFormatting(processedText)) {
                applyGlyphs(matrix, builder, TextUtil.parseMinecraftFormattedString(processedText), size, (thickness + (outlineThickness * 0.5f)) * 0.5f * size, spacing, x, y + (getMetrics().baselineHeight() * size), 0.0f);
            } else {
                applyGlyphs(matrix, builder, processedText, size, (thickness + (outlineThickness * 0.5f)) * 0.5f * size, spacing, x, y + (getMetrics().baselineHeight() * size), 0.0f, color, colorSecond, offset);
            }
            end(builder);
        } catch (Exception e) {
        }
    }

    private void end(class_287 builder) {
        class_286.method_43433(builder.method_60800());
        RenderSystem.setShaderTexture(0, 0);
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }

    private void start(float outlineThickness, float thickness, float smoothness, int outlineColor) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.setShaderTexture(0, this.texture.method_4624());
        boolean outlineEnabled = outlineThickness > 0.0f;
        class_5944 shader = RenderSystem.setShader(shaderKey);
        shader.method_34582("uRange").method_1251(getAtlas().range());
        shader.method_34582("uThickness").method_1251(thickness);
        shader.method_34582("uSmoothness").method_1251(smoothness);
        shader.method_34582("uOutline").method_35649(outlineEnabled ? 1 : 0);
        if (outlineEnabled) {
            shader.method_34582("uOutlineThickness").method_1251(outlineThickness);
            float[] outlineComponents = ColorUtil.normalize(outlineColor);
            shader.method_34582("uOutlineColor").method_35657(outlineComponents[0], outlineComponents[1], outlineComponents[2], outlineComponents[3]);
        }
    }

    public void drawText(class_4587 matrixStack, class_2561 text, float x, float y, float size, float thickness) {
        Pair<Float, Float> coordinates = offset(x, y);
        drawText(matrixStack, text, coordinates.left().floatValue(), coordinates.right().floatValue(), size, thickness, 0.5f, 0.0f, -1, thickness);
    }

    public void drawText(class_4587 matrixStack, String text, float x, float y, float size, Color color, float thickness) {
        Pair<Float, Float> coordinates = offset(x, y);
        drawText(matrixStack, text, coordinates.left().floatValue(), coordinates.right().floatValue(), size, thickness, color.getRGB(), -1, -1.0f, 0.5f, 0.0f, -1, thickness);
    }

    public void drawGradientText(class_4587 matrixStack, String text, float x, float y, float size, Color colorFirst, Color colorSecond, float offset, float thickness) {
        Pair<Float, Float> coordinates = offset(x, y);
        drawText(matrixStack, text, coordinates.left().floatValue(), coordinates.right().floatValue(), size, thickness, colorFirst.getRGB(), colorSecond.getRGB(), offset, 0.5f, 0.0f, -1, thickness);
    }

    public void drawGradientText(class_4587 matrixStack, String text, float x, float y, float size, Color color, Color colorSecond, float offset) {
        drawGradientText(matrixStack, text, x, y, size, color, colorSecond, offset, 0.0f);
    }

    public void drawText(class_4587 matrixStack, class_2561 text, float x, float y, float size) {
        drawText(matrixStack, text, x, y, size, 0.0f);
    }

    public void drawText(class_4587 matrixStack, String text, float x, float y, float size, Color color) {
        drawText(matrixStack, text, x, y, size, color, 0.0f);
    }

    public void drawCenteredText(class_4587 matrixStack, String text, float x, float y, float size, Color color, float thickness) {
        drawText(matrixStack, text, x - (getWidth(text, size, thickness) / 2.0f), y, size, color, thickness);
    }

    public void drawCenteredText(class_4587 matrixStack, String text, float x, float y, float size, Color color) {
        drawCenteredText(matrixStack, text, x, y, size, color, 0.0f);
    }

    public void drawCenteredGradientText(class_4587 matrixStack, String text, float x, float y, float size, Color color, Color colorSecond, float offset, float thickness) {
        drawGradientText(matrixStack, text, x - (getWidth(text, size, thickness) / 2.0f), y, size, color, colorSecond, offset, thickness);
    }

    public void drawCenteredGradientText(class_4587 matrixStack, String text, float x, float y, float size, Color color, Color colorSecond, float offset) {
        drawCenteredGradientText(matrixStack, text, x, y, size, color, colorSecond, offset, 0.0f);
    }

    public void drawWrap(class_4587 matrixStack, String text, float x, float y, float width, float size, Color color, float offset, Duration cycleDuration, Duration pauseDuration) {
        float f;
        if (color.getAlpha() <= 0) {
            return;
        }
        float textWidth = getWidth(text, size);
        if (textWidth <= width) {
            drawText(matrixStack, text, x, y, size, color);
            return;
        }
        ScissorUtil.start(matrixStack, x, y - (size / 4.0f), width, size * 1.5f);
        long cycleMillis = cycleDuration.toMillis();
        long pauseMillis = pauseDuration.toMillis();
        long totalCycleTime = cycleMillis + pauseMillis;
        long elapsed = System.currentTimeMillis() % totalCycleTime;
        if (elapsed < cycleMillis) {
            f = elapsed / cycleMillis;
        } else {
            f = 1.0f;
        }
        float progress = f;
        float value = Easing.SINE_BOTH.apply(progress) * (textWidth + offset);
        drawText(matrixStack, text, x - value, y, size, color);
        drawText(matrixStack, text, (x - value) + textWidth + offset, y, size, color);
        ScissorUtil.stop(matrixStack);
    }

    public void applyGlyphs(Matrix4f matrix, class_4588 consumer, String text, float size, float thickness, float spacing, float x, float y, float z, int color, int colorSecond, float offset) {
        if (TextUtil.hasFormatting(text)) {
            applyGlyphs(matrix, consumer, TextUtil.parseMinecraftFormattedString(text), size, thickness, spacing, x, y, z);
            return;
        }
        int prevChar = -1;
        float totalWidth = getWidth(text, size);
        float time = (System.currentTimeMillis() % 3000) / 3000.0f;
        for (int i = 0; i < text.length(); i++) {
            int _char = text.charAt(i);
            MsdfGlyph glyph = this.glyphs.get(Integer.valueOf(_char));
            if (glyph != null) {
                Map<Integer, Float> kerning = this.kernings.get(Integer.valueOf(prevChar));
                if (kerning != null) {
                    x += kerning.getOrDefault(Integer.valueOf(_char), Float.valueOf(0.0f)).floatValue() * size;
                }
                int currentColor = color;
                if (offset > 1.0f) {
                    currentColor = ColorUtil.gradient(color, colorSecond, x - x, totalWidth, time, offset);
                }
                x += glyph.apply(matrix, consumer, size, x, y, z, currentColor) + thickness + spacing;
                prevChar = _char;
            }
        }
    }

    public void applyGlyphs(Matrix4f matrix, class_4588 consumer, List<MsdfGlyph.ColoredGlyph> glyphs, float size, float thickness, float spacing, float x, float y, float z) {
        int prevChar = -1;
        for (int i = 0; i < glyphs.size(); i++) {
            MsdfGlyph.ColoredGlyph glyphData = glyphs.get(i);
            int _char = glyphData.c();
            int color = glyphData.color();
            MsdfGlyph glyph = this.glyphs.get(Integer.valueOf(_char));
            if (glyph != null) {
                Map<Integer, Float> kerning = this.kernings.get(Integer.valueOf(prevChar));
                if (kerning != null) {
                    x += kerning.getOrDefault(Integer.valueOf(_char), Float.valueOf(0.0f)).floatValue() * size;
                }
                x += glyph.apply(matrix, consumer, size, x, y, z, color) + thickness + spacing;
                prevChar = _char;
            }
        }
    }

    public float getHeight(float size) {
        return size;
    }

    public float getWidth(class_2561 text, float size) {
        return getWidth(text, size, 0.0f);
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: java.lang.MatchException */
    public float getWidth(class_2561 text, float size, float thickness) throws MatchException {
        if (text == null) {
            return 0.0f;
        }
        List<MsdfGlyph.ColoredGlyph> glyphs = TextUtil.parseTextToColoredGlyphs(text);
        int prevChar = -1;
        float width = 0.0f;
        for (int i = 0; i < glyphs.size(); i++) {
            int _char = glyphs.get(i).c();
            MsdfGlyph glyph = this.glyphs.get(Integer.valueOf(_char));
            if (glyph != null) {
                Map<Integer, Float> kerning = this.kernings.get(Integer.valueOf(prevChar));
                if (kerning != null) {
                    width += kerning.getOrDefault(Integer.valueOf(_char), Float.valueOf(0.0f)).floatValue() * size * (1.0f + thickness);
                }
                width += glyph.getWidth(size);
                prevChar = _char;
            }
        }
        return width;
    }

    public float getWidth(String text, float size) {
        return getWidth(text, size, 0.0f);
    }

    public float getWidth(String text, float size, float thickness) {
        int prevChar = -1;
        float width = 0.0f;
        String finalText = ReplaceUtil.protectedString(text);
        String cleanText = TextUtil.stripFormatting(finalText);
        for (int i = 0; i < cleanText.length(); i++) {
            int _char = cleanText.charAt(i);
            MsdfGlyph glyph = this.glyphs.get(Integer.valueOf(_char));
            if (glyph != null) {
                Map<Integer, Float> kerning = this.kernings.get(Integer.valueOf(prevChar));
                if (kerning != null) {
                    width += kerning.getOrDefault(Integer.valueOf(_char), Float.valueOf(0.0f)).floatValue() * size * (1.0f + thickness);
                }
                width += glyph.getWidth(size) * (1.0f + thickness);
                prevChar = _char;
            }
        }
        return width;
    }

    public static FontBuilder builder() {
        return new FontBuilder();
    }
}
