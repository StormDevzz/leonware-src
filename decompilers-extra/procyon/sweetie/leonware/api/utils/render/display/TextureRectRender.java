// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.render.display;

import net.minecraft.class_10149;
import sweetie.leonware.api.system.files.FileUtil;
import net.minecraft.class_287;
import net.minecraft.class_5944;
import org.joml.Matrix4f;
import net.minecraft.class_286;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_289;
import com.mojang.blaze3d.systems.RenderSystem;
import org.joml.Vector4f;
import net.minecraft.class_2960;
import sweetie.leonware.api.utils.render.RenderUtil;
import net.minecraft.class_742;
import java.awt.Color;
import net.minecraft.class_1657;
import net.minecraft.class_4587;
import net.minecraft.class_10156;
import sweetie.leonware.api.system.interfaces.QuickImports;

public class TextureRectRender implements QuickImports
{
    private static final class_10156 shaderKey;
    
    public void drawHead(final class_4587 matrixStack, final class_1657 player, final float x, final float y, final float width, final float height, final float gap, final float radius, final Color color) {
        final class_2960 skin = ((class_742)player).method_52814().comp_1626();
        final float u = 0.125f;
        final float u2 = 0.625f;
        final float superGap = gap * 2.0f;
        final int texture = TextureRectRender.mc.method_1531().method_4619(skin).method_4624();
        RenderUtil.TEXTURE_RECT.draw(matrixStack, x + gap, y + gap, width - superGap, height - superGap, radius, color, u, u, u, u, texture);
        RenderUtil.TEXTURE_RECT.draw(matrixStack, x, y, width, height, radius, color, u2, u, u, u, texture);
    }
    
    public void draw(final class_4587 matrixStack, final float x, final float y, final float width, final float height, final float radius, final Color color, final float u, final float v, final float texWidth, final float texHeight, final int texture) {
        this.draw(matrixStack, x, y, width, height, new Vector4f(radius, radius, radius, radius), color, u, v, texWidth, texHeight, texture);
    }
    
    public void draw(final class_4587 matrixStack, final float x, final float y, final float width, final float height, final Vector4f radius, final Color color, final float u, final float v, final float texWidth, final float texHeight, final int texture) {
        final Matrix4f matrix4f = matrixStack.method_23760().method_23761();
        final float z = 0.0f;
        final float smoothness = 0.8f;
        final float horizontalPadding = -smoothness / 2.0f + smoothness * 2.0f;
        final float verticalPadding = smoothness / 2.0f + smoothness;
        final float adjustedX = x - horizontalPadding / 2.0f;
        final float adjustedY = y - verticalPadding / 2.0f;
        final float adjustedWidth = width + horizontalPadding;
        final float adjustedHeight = height + verticalPadding;
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.setShaderTexture(0, texture);
        final class_5944 shader = RenderSystem.setShader(TextureRectRender.shaderKey);
        shader.method_34582("uSize").method_1255(width, height);
        shader.method_34582("uRadius").method_35657(radius.x, radius.z, radius.w, radius.y);
        shader.method_34582("uSmoothness").method_1251(smoothness);
        final class_287 builder = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1575);
        builder.method_22918(matrix4f, adjustedX, adjustedY, z).method_22913(u, v).method_39415(color.getRGB());
        builder.method_22918(matrix4f, adjustedX, adjustedY + adjustedHeight, z).method_22913(u, v + texHeight).method_39415(color.getRGB());
        builder.method_22918(matrix4f, adjustedX + adjustedWidth, adjustedY + adjustedHeight, z).method_22913(u + texWidth, v + texHeight).method_39415(color.getRGB());
        builder.method_22918(matrix4f, adjustedX + adjustedWidth, adjustedY, z).method_22913(u + texWidth, v).method_39415(color.getRGB());
        class_286.method_43433(builder.method_60800());
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }
    
    public void drawTexture(final class_4587 matrixStack, final class_2960 identifier, final float x, final float y, final float width, final float height, final Color color) {
        final int textureId = TextureRectRender.mc.method_1531().method_4619(identifier).method_4624();
        this.draw(matrixStack, x, y, width, height, 0.0f, color, 0.0f, 0.0f, 1.0f, 1.0f, textureId);
    }
    
    public void drawTexture(final class_4587 matrixStack, final class_2960 identifier, final float x, final float y, final float width, final float height, final float radius, final Color color) {
        final int textureId = TextureRectRender.mc.method_1531().method_4619(identifier).method_4624();
        this.draw(matrixStack, x, y, width, height, radius, color, 0.0f, 0.0f, 1.0f, 1.0f, textureId);
    }
    
    static {
        shaderKey = new class_10156(FileUtil.getShader("rect/texture_rect"), class_290.field_1575, class_10149.field_53930);
    }
}
