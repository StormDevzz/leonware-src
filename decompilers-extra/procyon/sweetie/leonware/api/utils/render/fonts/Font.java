// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.render.fonts;

import net.minecraft.class_10149;
import sweetie.leonware.api.system.files.FileUtil;
import lombok.Generated;
import java.util.List;
import sweetie.leonware.api.utils.animation.Easing;
import sweetie.leonware.api.utils.render.ScissorUtil;
import java.time.Duration;
import java.awt.Color;
import net.minecraft.class_5944;
import sweetie.leonware.api.utils.color.ColorUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.class_286;
import net.minecraft.class_287;
import org.joml.Matrix4f;
import net.minecraft.class_4588;
import sweetie.leonware.api.utils.other.TextUtil;
import sweetie.leonware.api.utils.other.ReplaceUtil;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_289;
import net.minecraft.class_2561;
import net.minecraft.class_4587;
import sweetie.leonware.client.services.RenderService;
import sweetie.leonware.api.system.backend.Pair;
import java.util.Map;
import net.minecraft.class_1044;
import net.minecraft.class_10156;

public final class Font
{
    private static final class_10156 shaderKey;
    private final String name;
    private final class_1044 texture;
    private final FontData.AtlasData atlas;
    private final FontData.MetricsData metrics;
    private final Map<Integer, MsdfGlyph> glyphs;
    private final Map<Integer, Map<Integer, Float>> kernings;
    
    public Font(final String name, final class_1044 texture, final FontData.AtlasData atlas, final FontData.MetricsData metrics, final Map<Integer, MsdfGlyph> glyphs, final Map<Integer, Map<Integer, Float>> kernings) {
        this.name = name;
        this.texture = texture;
        this.atlas = atlas;
        this.metrics = metrics;
        this.glyphs = glyphs;
        this.kernings = kernings;
    }
    
    private Pair<Float, Float> offset(final float x, final float y) {
        final float scale = RenderService.getInstance().getScale();
        float x2 = x;
        float y2 = y;
        final boolean isPS = this.name.contains("product_sans");
        final boolean isSF = this.name.contains("sf_pro");
        if (isSF || isPS) {
            y2 -= scale;
            if (isPS) {
                x2 -= scale / 2.0f;
            }
        }
        return new Pair<Float, Float>(x2, y2);
    }
    
    public void drawText(final class_4587 matrixStack, final class_2561 text, final float x, final float y, final float size, final float thickness, final float smoothness, final float spacing, final int outlineColor, final float outlineThickness) {
        if (text == null) {
            return;
        }
        try {
            final Matrix4f matrix = matrixStack.method_23760().method_23761();
            this.start(outlineThickness, thickness, smoothness, outlineColor);
            final class_287 builder = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1575);
            this.applyGlyphs(matrix, (class_4588)builder, TextUtil.parseTextToColoredGlyphs(ReplaceUtil.replaceSymbols(text)), size, (thickness + outlineThickness * 0.5f) * 0.5f * size, spacing, x, y + this.getMetrics().baselineHeight() * size, 0.0f);
            this.end(builder);
        }
        catch (final Exception ex) {}
    }
    
    public void drawText(final class_4587 matrixStack, final String text, final float x, final float y, final float size, final float thickness, final int color, final int colorSecond, final float offset, final float smoothness, final float spacing, final int outlineColor, final float outlineThickness) {
        try {
            final Matrix4f matrix = matrixStack.method_23760().method_23761();
            final String processedText = ReplaceUtil.protectedString(text);
            this.start(outlineThickness, thickness, smoothness, outlineColor);
            final class_287 builder = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1575);
            if (TextUtil.hasFormatting(processedText)) {
                this.applyGlyphs(matrix, (class_4588)builder, TextUtil.parseMinecraftFormattedString(processedText), size, (thickness + outlineThickness * 0.5f) * 0.5f * size, spacing, x, y + this.getMetrics().baselineHeight() * size, 0.0f);
            }
            else {
                this.applyGlyphs(matrix, (class_4588)builder, processedText, size, (thickness + outlineThickness * 0.5f) * 0.5f * size, spacing, x, y + this.getMetrics().baselineHeight() * size, 0.0f, color, colorSecond, offset);
            }
            this.end(builder);
        }
        catch (final Exception ex) {}
    }
    
    private void end(final class_287 builder) {
        class_286.method_43433(builder.method_60800());
        RenderSystem.setShaderTexture(0, 0);
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }
    
    private void start(final float outlineThickness, final float thickness, final float smoothness, final int outlineColor) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.setShaderTexture(0, this.texture.method_4624());
        final boolean outlineEnabled = outlineThickness > 0.0f;
        final class_5944 shader = RenderSystem.setShader(Font.shaderKey);
        shader.method_34582("uRange").method_1251(this.getAtlas().range());
        shader.method_34582("uThickness").method_1251(thickness);
        shader.method_34582("uSmoothness").method_1251(smoothness);
        shader.method_34582("uOutline").method_35649((int)(outlineEnabled ? 1 : 0));
        if (outlineEnabled) {
            shader.method_34582("uOutlineThickness").method_1251(outlineThickness);
            final float[] outlineComponents = ColorUtil.normalize(outlineColor);
            shader.method_34582("uOutlineColor").method_35657(outlineComponents[0], outlineComponents[1], outlineComponents[2], outlineComponents[3]);
        }
    }
    
    public void drawText(final class_4587 matrixStack, final class_2561 text, final float x, final float y, final float size, final float thickness) {
        final Pair<Float, Float> coordinates = this.offset(x, y);
        this.drawText(matrixStack, text, coordinates.left(), coordinates.right(), size, thickness, 0.5f, 0.0f, -1, thickness);
    }
    
    public void drawText(final class_4587 matrixStack, final String text, final float x, final float y, final float size, final Color color, final float thickness) {
        final Pair<Float, Float> coordinates = this.offset(x, y);
        this.drawText(matrixStack, text, coordinates.left(), coordinates.right(), size, thickness, color.getRGB(), -1, -1.0f, 0.5f, 0.0f, -1, thickness);
    }
    
    public void drawGradientText(final class_4587 matrixStack, final String text, final float x, final float y, final float size, final Color colorFirst, final Color colorSecond, final float offset, final float thickness) {
        final Pair<Float, Float> coordinates = this.offset(x, y);
        this.drawText(matrixStack, text, coordinates.left(), coordinates.right(), size, thickness, colorFirst.getRGB(), colorSecond.getRGB(), offset, 0.5f, 0.0f, -1, thickness);
    }
    
    public void drawGradientText(final class_4587 matrixStack, final String text, final float x, final float y, final float size, final Color color, final Color colorSecond, final float offset) {
        this.drawGradientText(matrixStack, text, x, y, size, color, colorSecond, offset, 0.0f);
    }
    
    public void drawText(final class_4587 matrixStack, final class_2561 text, final float x, final float y, final float size) {
        this.drawText(matrixStack, text, x, y, size, 0.0f);
    }
    
    public void drawText(final class_4587 matrixStack, final String text, final float x, final float y, final float size, final Color color) {
        this.drawText(matrixStack, text, x, y, size, color, 0.0f);
    }
    
    public void drawCenteredText(final class_4587 matrixStack, final String text, final float x, final float y, final float size, final Color color, final float thickness) {
        this.drawText(matrixStack, text, x - this.getWidth(text, size, thickness) / 2.0f, y, size, color, thickness);
    }
    
    public void drawCenteredText(final class_4587 matrixStack, final String text, final float x, final float y, final float size, final Color color) {
        this.drawCenteredText(matrixStack, text, x, y, size, color, 0.0f);
    }
    
    public void drawCenteredGradientText(final class_4587 matrixStack, final String text, final float x, final float y, final float size, final Color color, final Color colorSecond, final float offset, final float thickness) {
        this.drawGradientText(matrixStack, text, x - this.getWidth(text, size, thickness) / 2.0f, y, size, color, colorSecond, offset, thickness);
    }
    
    public void drawCenteredGradientText(final class_4587 matrixStack, final String text, final float x, final float y, final float size, final Color color, final Color colorSecond, final float offset) {
        this.drawCenteredGradientText(matrixStack, text, x, y, size, color, colorSecond, offset, 0.0f);
    }
    
    public void drawWrap(final class_4587 matrixStack, final String text, final float x, final float y, final float width, final float size, final Color color, final float offset, final Duration cycleDuration, final Duration pauseDuration) {
        if (color.getAlpha() <= 0) {
            return;
        }
        final float textWidth = this.getWidth(text, size);
        if (textWidth <= width) {
            this.drawText(matrixStack, text, x, y, size, color);
        }
        else {
            ScissorUtil.start(matrixStack, x, y - size / 4.0f, width, size * 1.5f);
            final long cycleMillis = cycleDuration.toMillis();
            final long pauseMillis = pauseDuration.toMillis();
            final long totalCycleTime = cycleMillis + pauseMillis;
            final long elapsed = System.currentTimeMillis() % totalCycleTime;
            final float progress = (elapsed < cycleMillis) ? (elapsed / (float)cycleMillis) : 1.0f;
            final float value = Easing.SINE_BOTH.apply(progress) * (textWidth + offset);
            this.drawText(matrixStack, text, x - value, y, size, color);
            this.drawText(matrixStack, text, x - value + (textWidth + offset), y, size, color);
            ScissorUtil.stop(matrixStack);
        }
    }
    
    public void applyGlyphs(final Matrix4f matrix, final class_4588 consumer, final String text, final float size, final float thickness, final float spacing, float x, final float y, final float z, final int color, final int colorSecond, final float offset) {
        if (TextUtil.hasFormatting(text)) {
            this.applyGlyphs(matrix, consumer, TextUtil.parseMinecraftFormattedString(text), size, thickness, spacing, x, y, z);
        }
        else {
            int prevChar = -1;
            final float startX = x;
            final float totalWidth = this.getWidth(text, size);
            final float time = System.currentTimeMillis() % 3000L / 3000.0f;
            for (int i = 0; i < text.length(); ++i) {
                final int _char = text.charAt(i);
                final MsdfGlyph glyph = this.glyphs.get(_char);
                if (glyph != null) {
                    final Map<Integer, Float> kerning = this.kernings.get(prevChar);
                    if (kerning != null) {
                        x += kerning.getOrDefault(_char, 0.0f) * size;
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
    }
    
    public void applyGlyphs(final Matrix4f matrix, final class_4588 consumer, final List<MsdfGlyph.ColoredGlyph> glyphs, final float size, final float thickness, final float spacing, float x, final float y, final float z) {
        int prevChar = -1;
        for (int i = 0; i < glyphs.size(); ++i) {
            final MsdfGlyph.ColoredGlyph glyphData = glyphs.get(i);
            final int _char = glyphData.c();
            final int color = glyphData.color();
            final MsdfGlyph glyph = this.glyphs.get(_char);
            if (glyph != null) {
                final Map<Integer, Float> kerning = this.kernings.get(prevChar);
                if (kerning != null) {
                    x += kerning.getOrDefault(_char, 0.0f) * size;
                }
                x += glyph.apply(matrix, consumer, size, x, y, z, color) + thickness + spacing;
                prevChar = _char;
            }
        }
    }
    
    public float getHeight(final float size) {
        return size;
    }
    
    public float getWidth(final class_2561 text, final float size) {
        return this.getWidth(text, size, 0.0f);
    }
    
    public float getWidth(final class_2561 text, final float size, final float thickness) {
        if (text == null) {
            return 0.0f;
        }
        final List<MsdfGlyph.ColoredGlyph> glyphs = TextUtil.parseTextToColoredGlyphs(text);
        int prevChar = -1;
        float width = 0.0f;
        for (int i = 0; i < glyphs.size(); ++i) {
            final int _char = glyphs.get(i).c();
            final MsdfGlyph glyph = this.glyphs.get(_char);
            if (glyph != null) {
                final Map<Integer, Float> kerning = this.kernings.get(prevChar);
                if (kerning != null) {
                    width += kerning.getOrDefault(_char, 0.0f) * size * (1.0f + thickness);
                }
                width += glyph.getWidth(size);
                prevChar = _char;
            }
        }
        return width;
    }
    
    public float getWidth(final String text, final float size) {
        return this.getWidth(text, size, 0.0f);
    }
    
    public float getWidth(final String text, final float size, final float thickness) {
        int prevChar = -1;
        float width = 0.0f;
        final String finalText = ReplaceUtil.protectedString(text);
        final String cleanText = TextUtil.stripFormatting(finalText);
        for (int i = 0; i < cleanText.length(); ++i) {
            final int _char = cleanText.charAt(i);
            final MsdfGlyph glyph = this.glyphs.get(_char);
            if (glyph != null) {
                final Map<Integer, Float> kerning = this.kernings.get(prevChar);
                if (kerning != null) {
                    width += kerning.getOrDefault(_char, 0.0f) * size * (1.0f + thickness);
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
    
    @Generated
    public FontData.AtlasData getAtlas() {
        return this.atlas;
    }
    
    @Generated
    public FontData.MetricsData getMetrics() {
        return this.metrics;
    }
    
    static {
        shaderKey = new class_10156(FileUtil.getShader("text"), class_290.field_1575, class_10149.field_53930);
    }
}
