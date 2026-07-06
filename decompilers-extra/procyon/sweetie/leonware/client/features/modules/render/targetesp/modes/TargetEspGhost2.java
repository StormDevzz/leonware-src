// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.render.targetesp.modes;

import org.joml.Matrix4f;
import net.minecraft.class_7833;
import net.minecraft.class_287;
import net.minecraft.class_4587;
import java.awt.Color;
import java.util.Iterator;
import net.minecraft.class_286;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_289;
import com.mojang.blaze3d.platform.GlStateManager;
import sweetie.leonware.api.system.files.FileUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.class_10142;
import sweetie.leonware.api.utils.color.UIColors;
import net.minecraft.class_243;
import sweetie.leonware.api.event.events.render.Render3DEvent;
import java.util.ArrayList;
import java.util.List;
import sweetie.leonware.client.features.modules.render.targetesp.TargetEspMode;

public class TargetEspGhost2 extends TargetEspMode
{
    private final List<GhostData> ghosts;
    private long lastUpdateTime;
    private long lastTrailTime;
    
    public TargetEspGhost2() {
        this.ghosts = new ArrayList<GhostData>();
        this.lastUpdateTime = 0L;
        this.lastTrailTime = 0L;
    }
    
    @Override
    public void onUpdate() {
        if (this.ghosts.isEmpty()) {
            this.initGhosts();
        }
    }
    
    @Override
    public void onRender3D(final Render3DEvent.Render3DEventData event) {
        if (TargetEspGhost2.currentTarget == null || !this.canDraw()) {
            return;
        }
        final float anim = (float)TargetEspGhost2.showAnimation.getValue();
        if (anim <= 0.01f) {
            return;
        }
        final long now = System.currentTimeMillis();
        if (this.lastUpdateTime == 0L) {
            this.lastUpdateTime = now;
            this.lastTrailTime = now;
        }
        final long delta = now - this.lastUpdateTime;
        this.lastUpdateTime = now;
        final double worldX = TargetEspMode.getTargetX();
        final double worldY = getTargetY() + TargetEspGhost2.currentTarget.method_17682() / 2.0;
        final double worldZ = TargetEspMode.getTargetZ();
        final float rotSpeed = delta * 0.003f;
        final float radius = 0.45f;
        final float vertAmp = 0.4f;
        final int maxTrail = 20;
        for (int i = 0; i < this.ghosts.size(); ++i) {
            final GhostData ghostData;
            final GhostData ghost = ghostData = this.ghosts.get(i);
            ghostData.angle += rotSpeed;
            final float offset = (float)(i * 2.0943951023931953);
            final float angle = ghost.angle + offset;
            ghost.position = new class_243(worldX + Math.sin(angle) * radius, worldY + Math.sin(angle * 2.0) * vertAmp, worldZ + Math.cos(angle) * radius);
        }
        if (now - this.lastTrailTime >= 30L) {
            this.lastTrailTime = now;
            for (final GhostData ghost : this.ghosts) {
                if (!ghost.history.isEmpty() && ghost.position.method_1025((class_243)ghost.history.get(0)) > 10.0) {
                    ghost.history.clear();
                }
                ghost.history.add(0, ghost.position);
                while (ghost.history.size() > maxTrail) {
                    ghost.history.remove(ghost.history.size() - 1);
                }
            }
        }
        final Color color = UIColors.gradient(0);
        final int rgb = color.getRGB();
        final class_4587 ms = event.matrixStack();
        RenderSystem.setShader(class_10142.field_53880);
        RenderSystem.setShaderTexture(0, FileUtil.getImage("particles/glow"));
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.class_4535.SRC_ALPHA, GlStateManager.class_4534.ONE, GlStateManager.class_4535.ZERO, GlStateManager.class_4534.ONE);
        RenderSystem.disableCull();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        final class_287 builder = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1575);
        boolean hasVertices = false;
        for (final GhostData ghost2 : this.ghosts) {
            for (int j = ghost2.history.size() - 1; j >= 0; --j) {
                if (this.writePartVertices(builder, ms, ghost2.history.get(j), j, ghost2.history.size(), anim, 0.2f, rgb)) {
                    hasVertices = true;
                }
            }
            if (this.writePartVertices(builder, ms, ghost2.position, 0, 1, anim, 0.2f, rgb)) {
                hasVertices = true;
            }
        }
        if (hasVertices) {
            class_286.method_43433(builder.method_60800());
        }
        RenderSystem.setShaderTexture(0, 0);
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
        RenderSystem.blendFunc(GlStateManager.class_4535.SRC_ALPHA, GlStateManager.class_4534.ONE_MINUS_SRC_ALPHA);
        RenderSystem.enableCull();
    }
    
    private boolean writePartVertices(final class_287 builder, final class_4587 ms, final class_243 pos, final int index, final int maxHistory, final float anim, final float baseSize, final int rgb) {
        final float progress = (maxHistory == 1) ? 0.0f : (index / (float)Math.max(1, maxHistory));
        final float spriteScale = anim * baseSize * (1.0f - progress * 0.7f);
        if (spriteScale < 0.03f) {
            return false;
        }
        final float alphaVal = anim * (1.0f - progress * 0.85f);
        if (alphaVal < 0.02f) {
            return false;
        }
        final float r = (rgb >> 16 & 0xFF) / 255.0f;
        final float g = (rgb >> 8 & 0xFF) / 255.0f;
        final float b = (rgb & 0xFF) / 255.0f;
        this.putQuad(builder, ms, pos, spriteScale * 3.4f, r, g, b, alphaVal * 0.28f);
        this.putQuad(builder, ms, pos, spriteScale * 1.9f, r, g, b, alphaVal * 0.55f);
        this.putQuad(builder, ms, pos, spriteScale, Math.min(1.0f, r * 1.2f), Math.min(1.0f, g * 1.2f), Math.min(1.0f, b * 1.2f), alphaVal);
        return true;
    }
    
    private void putQuad(final class_287 builder, final class_4587 ms, final class_243 pos, final float size, final float r, final float g, final float b, final float a) {
        final class_243 cam = TargetEspGhost2.mc.method_1561().field_4686.method_19326();
        ms.method_22903();
        ms.method_22904(pos.field_1352 - cam.field_1352, pos.field_1351 - cam.field_1351, pos.field_1350 - cam.field_1350);
        ms.method_22907(class_7833.field_40716.rotationDegrees(-TargetEspGhost2.mc.field_1773.method_19418().method_19330()));
        ms.method_22907(class_7833.field_40714.rotationDegrees(TargetEspGhost2.mc.field_1773.method_19418().method_19329()));
        final Matrix4f matrix = ms.method_23760().method_23761();
        final float half = size / 2.0f;
        final int ri = (int)(r * 255.0f);
        final int gi = (int)(g * 255.0f);
        final int bi = (int)(b * 255.0f);
        final int ai = (int)(a * 255.0f);
        builder.method_22918(matrix, -half, -half, 0.0f).method_22913(0.0f, 1.0f).method_1336(ri, gi, bi, ai);
        builder.method_22918(matrix, -half, half, 0.0f).method_22913(0.0f, 0.0f).method_1336(ri, gi, bi, ai);
        builder.method_22918(matrix, half, half, 0.0f).method_22913(1.0f, 0.0f).method_1336(ri, gi, bi, ai);
        builder.method_22918(matrix, half, -half, 0.0f).method_22913(1.0f, 1.0f).method_1336(ri, gi, bi, ai);
        ms.method_22909();
    }
    
    private void initGhosts() {
        this.ghosts.clear();
        for (int i = 0; i < 3; ++i) {
            final GhostData ghost = new GhostData();
            ghost.angle = (float)(i * 2.0943951023931953);
            ghost.position = class_243.field_1353;
            ghost.history = new ArrayList<class_243>();
            for (int j = 0; j < 10; ++j) {
                ghost.history.add(class_243.field_1353);
            }
            this.ghosts.add(ghost);
        }
    }
    
    private static class GhostData
    {
        float angle;
        class_243 position;
        List<class_243> history;
    }
}
