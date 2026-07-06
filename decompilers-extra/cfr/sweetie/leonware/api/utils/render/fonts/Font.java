/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  lombok.Generated
 *  net.minecraft.class_10149
 *  net.minecraft.class_10156
 *  net.minecraft.class_1044
 *  net.minecraft.class_2561
 *  net.minecraft.class_286
 *  net.minecraft.class_287
 *  net.minecraft.class_289
 *  net.minecraft.class_290
 *  net.minecraft.class_293$class_5596
 *  net.minecraft.class_4587
 *  net.minecraft.class_4588
 *  net.minecraft.class_5944
 *  net.minecraft.class_9801
 *  org.joml.Matrix4f
 */
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
import net.minecraft.class_9801;
import org.joml.Matrix4f;
import sweetie.leonware.api.system.backend.Pair;
import sweetie.leonware.api.system.files.FileUtil;
import sweetie.leonware.api.utils.animation.Easing;
import sweetie.leonware.api.utils.color.ColorUtil;
import sweetie.leonware.api.utils.other.ReplaceUtil;
import sweetie.leonware.api.utils.other.TextUtil;
import sweetie.leonware.api.utils.render.ScissorUtil;
import sweetie.leonware.api.utils.render.fonts.FontBuilder;
import sweetie.leonware.api.utils.render.fonts.FontData;
import sweetie.leonware.api.utils.render.fonts.MsdfGlyph;
import sweetie.leonware.client.services.RenderService;

public final class Font {
    private static final class_10156 shaderKey = new class_10156(FileUtil.getShader("text"), class_290.field_1575, class_10149.field_53930);
    private final String name;
    private final class_1044 texture;
    private final FontData.AtlasData atlas;
    private final FontData.MetricsData metrics;
    private final Map<Integer, MsdfGlyph> glyphs;
    private final Map<Integer, Map<Integer, Float>> kernings;

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
        boolean isPS = this.name.contains("product_sans");
        boolean isSF = this.name.contains("sf_pro");
        if (isSF || isPS) {
            y1 -= scale;
            if (isPS) {
                x1 -= scale / 2.0f;
            }
        }
        return new Pair<Float, Float>(Float.valueOf(x1), Float.valueOf(y1));
    }

    public void drawText(class_4587 matrixStack, class_2561 text, float x, float y, float size, float thickness, float smoothness, float spacing, int outlineColor, float outlineThickness) {
        if (text == null) {
            return;
        }
        try {
            Matrix4f matrix = matrixStack.method_23760().method_23761();
            this.start(outlineThickness, thickness, smoothness, outlineColor);
            class_287 builder = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1575);
            this.applyGlyphs(matrix, (class_4588)builder, TextUtil.parseTextToColoredGlyphs(ReplaceUtil.replaceSymbols(text)), size, (thickness + outlineThickness * 0.5f) * 0.5f * size, spacing, x, y + this.getMetrics().baselineHeight() * size, 0.0f);
            this.end(builder);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public void drawText(class_4587 matrixStack, String text, float x, float y, float size, float thickness, int color, int colorSecond, float offset, float smoothness, float spacing, int outlineColor, float outlineThickness) {
        try {
            Matrix4f matrix = matrixStack.method_23760().method_23761();
            String processedText = ReplaceUtil.protectedString(text);
            this.start(outlineThickness, thickness, smoothness, outlineColor);
            class_287 builder = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1575);
            if (TextUtil.hasFormatting(processedText)) {
                this.applyGlyphs(matrix, (class_4588)builder, TextUtil.parseMinecraftFormattedString(processedText), size, (thickness + outlineThickness * 0.5f) * 0.5f * size, spacing, x, y + this.getMetrics().baselineHeight() * size, 0.0f);
            } else {
                this.applyGlyphs(matrix, (class_4588)builder, processedText, size, (thickness + outlineThickness * 0.5f) * 0.5f * size, spacing, x, y + this.getMetrics().baselineHeight() * size, 0.0f, color, colorSecond, offset);
            }
            this.end(builder);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private void end(class_287 builder) {
        class_286.method_43433((class_9801)builder.method_60800());
        RenderSystem.setShaderTexture((int)0, (int)0);
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }

    private void start(float outlineThickness, float thickness, float smoothness, int outlineColor) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.setShaderTexture((int)0, (int)this.texture.method_4624());
        boolean outlineEnabled = outlineThickness > 0.0f;
        class_5944 shader = RenderSystem.setShader((class_10156)shaderKey);
        shader.method_34582("uRange").method_1251(this.getAtlas().range());
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
        Pair<Float, Float> coordinates = this.offset(x, y);
        this.drawText(matrixStack, text, coordinates.left().floatValue(), coordinates.right().floatValue(), size, thickness, 0.5f, 0.0f, -1, thickness);
    }

    public void drawText(class_4587 matrixStack, String text, float x, float y, float size, Color color, float thickness) {
        Pair<Float, Float> coordinates = this.offset(x, y);
        this.drawText(matrixStack, text, coordinates.left().floatValue(), coordinates.right().floatValue(), size, thickness, color.getRGB(), -1, -1.0f, 0.5f, 0.0f, -1, thickness);
    }

    public void drawGradientText(class_4587 matrixStack, String text, float x, float y, float size, Color colorFirst, Color colorSecond, float offset, float thickness) {
        Pair<Float, Float> coordinates = this.offset(x, y);
        this.drawText(matrixStack, text, coordinates.left().floatValue(), coordinates.right().floatValue(), size, thickness, colorFirst.getRGB(), colorSecond.getRGB(), offset, 0.5f, 0.0f, -1, thickness);
    }

    public void drawGradientText(class_4587 matrixStack, String text, float x, float y, float size, Color color, Color colorSecond, float offset) {
        this.drawGradientText(matrixStack, text, x, y, size, color, colorSecond, offset, 0.0f);
    }

    public void drawText(class_4587 matrixStack, class_2561 text, float x, float y, float size) {
        this.drawText(matrixStack, text, x, y, size, 0.0f);
    }

    public void drawText(class_4587 matrixStack, String text, float x, float y, float size, Color color) {
        this.drawText(matrixStack, text, x, y, size, color, 0.0f);
    }

    public void drawCenteredText(class_4587 matrixStack, String text, float x, float y, float size, Color color, float thickness) {
        this.drawText(matrixStack, text, x - this.getWidth(text, size, thickness) / 2.0f, y, size, color, thickness);
    }

    public void drawCenteredText(class_4587 matrixStack, String text, float x, float y, float size, Color color) {
        this.drawCenteredText(matrixStack, text, x, y, size, color, 0.0f);
    }

    public void drawCenteredGradientText(class_4587 matrixStack, String text, float x, float y, float size, Color color, Color colorSecond, float offset, float thickness) {
        this.drawGradientText(matrixStack, text, x - this.getWidth(text, size, thickness) / 2.0f, y, size, color, colorSecond, offset, thickness);
    }

    public void drawCenteredGradientText(class_4587 matrixStack, String text, float x, float y, float size, Color color, Color colorSecond, float offset) {
        this.drawCenteredGradientText(matrixStack, text, x, y, size, color, colorSecond, offset, 0.0f);
    }

    public void drawWrap(class_4587 matrixStack, String text, float x, float y, float width, float size, Color color, float offset, Duration cycleDuration, Duration pauseDuration) {
        if (color.getAlpha() <= 0) {
            return;
        }
        float textWidth = this.getWidth(text, size);
        if (textWidth <= width) {
            this.drawText(matrixStack, text, x, y, size, color);
        } else {
            ScissorUtil.start(matrixStack, x, y - size / 4.0f, width, size * 1.5f);
            long cycleMillis = cycleDuration.toMillis();
            long pauseMillis = pauseDuration.toMillis();
            long totalCycleTime = cycleMillis + pauseMillis;
            long elapsed = System.currentTimeMillis() % totalCycleTime;
            float progress = elapsed < cycleMillis ? (float)elapsed / (float)cycleMillis : 1.0f;
            float value = Easing.SINE_BOTH.apply(progress) * (textWidth + offset);
            this.drawText(matrixStack, text, x - value, y, size, color);
            this.drawText(matrixStack, text, x - value + (textWidth + offset), y, size, color);
            ScissorUtil.stop(matrixStack);
        }
    }

    public void applyGlyphs(Matrix4f matrix, class_4588 consumer, String text, float size, float thickness, float spacing, float x, float y, float z, int color, int colorSecond, float offset) {
        if (TextUtil.hasFormatting(text)) {
            this.applyGlyphs(matrix, consumer, TextUtil.parseMinecraftFormattedString(text), size, thickness, spacing, x, y, z);
        } else {
            int prevChar = -1;
            float startX = x;
            float totalWidth = this.getWidth(text, size);
            float time = (float)(System.currentTimeMillis() % 3000L) / 3000.0f;
            for (int i = 0; i < text.length(); ++i) {
                char _char = text.charAt(i);
                MsdfGlyph glyph = this.glyphs.get(_char);
                if (glyph == null) continue;
                Map<Integer, Float> kerning = this.kernings.get(prevChar);
                if (kerning != null) {
                    x += kerning.getOrDefault(_char, Float.valueOf(0.0f)).floatValue() * size;
                }
                int currentColor = color;
                if (offset > 1.0f) {
                    currentColor = ColorUtil.gradient(color, colorSecond, x - startX, totalWidth, time, offset);
                }
                x += glyph.apply(matrix, consumer, size, x, y, z, currentColor) + thickness + spacing;
                prevChar = _char;
            }
        }
    }

    public void applyGlyphs(Matrix4f matrix, class_4588 consumer, List<MsdfGlyph.ColoredGlyph> glyphs, float size, float thickness, float spacing, float x, float y, float z) {
        int prevChar = -1;
        for (int i = 0; i < glyphs.size(); ++i) {
            MsdfGlyph.ColoredGlyph glyphData = glyphs.get(i);
            char _char = glyphData.c();
            int color = glyphData.color();
            MsdfGlyph glyph = this.glyphs.get(_char);
            if (glyph == null) continue;
            Map<Integer, Float> kerning = this.kernings.get(prevChar);
            if (kerning != null) {
                x += kerning.getOrDefault(_char, Float.valueOf(0.0f)).floatValue() * size;
            }
            x += glyph.apply(matrix, consumer, size, x, y, z, color) + thickness + spacing;
            prevChar = _char;
        }
    }

    public float getHeight(float size) {
        return size;
    }

    public float getWidth(class_2561 text, float size) {
        return this.getWidth(text, size, 0.0f);
    }

    public float getWidth(class_2561 text, float size, float thickness) {
        if (text == null) {
            return 0.0f;
        }
        List<MsdfGlyph.ColoredGlyph> glyphs = TextUtil.parseTextToColoredGlyphs(text);
        int prevChar = -1;
        float width = 0.0f;
        for (int i = 0; i < glyphs.size(); ++i) {
            char _char = glyphs.get(i).c();
            MsdfGlyph glyph = this.glyphs.get(_char);
            if (glyph == null) continue;
            Map<Integer, Float> kerning = this.kernings.get(prevChar);
            if (kerning != null) {
                width += kerning.getOrDefault(_char, Float.valueOf(0.0f)).floatValue() * size * (1.0f + thickness);
            }
            width += glyph.getWidth(size);
            prevChar = _char;
        }
        return width;
    }

    public float getWidth(String text, float size) {
        return this.getWidth(text, size, 0.0f);
    }

    public float getWidth(String text, float size, float thickness) {
        int prevChar = -1;
        float width = 0.0f;
        String finalText = ReplaceUtil.protectedString(text);
        String cleanText = TextUtil.stripFormatting(finalText);
        for (int i = 0; i < cleanText.length(); ++i) {
            char _char = cleanText.charAt(i);
            MsdfGlyph glyph = this.glyphs.get(_char);
            if (glyph == null) continue;
            Map<Integer, Float> kerning = this.kernings.get(prevChar);
            if (kerning != null) {
                width += kerning.getOrDefault(_char, Float.valueOf(0.0f)).floatValue() * size * (1.0f + thickness);
            }
            width += glyph.getWidth(size) * (1.0f + thickness);
            prevChar = _char;
        }
        return width;
    }

    public static FontBuilder builder() {
        return new FontBuilder();
    }

    @Generated
    public FontData.AtlasData getAtlas() {
        return this.atlas;
    }

    @Generated
    public FontData.MetricsData getMetrics() {
        return this.metrics;
    }
}

