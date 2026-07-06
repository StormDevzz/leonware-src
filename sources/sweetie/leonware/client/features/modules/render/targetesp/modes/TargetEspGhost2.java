package sweetie.leonware.client.features.modules.render.targetesp.modes;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_10142;
import net.minecraft.class_243;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_4587;
import net.minecraft.class_7833;
import org.joml.Matrix4f;
import sweetie.leonware.api.event.events.render.Render3DEvent;
import sweetie.leonware.api.system.files.FileUtil;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.client.features.modules.render.targetesp.TargetEspMode;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/targetesp/modes/TargetEspGhost2.class */
public class TargetEspGhost2 extends TargetEspMode {
    private final List<GhostData> ghosts = new ArrayList();
    private long lastUpdateTime = 0;
    private long lastTrailTime = 0;

    @Override // sweetie.leonware.client.features.modules.render.targetesp.TargetEspMode
    public void onUpdate() {
        if (this.ghosts.isEmpty()) {
            initGhosts();
        }
    }

    @Override // sweetie.leonware.client.features.modules.render.targetesp.TargetEspMode
    public void onRender3D(Render3DEvent.Render3DEventData event) {
        if (currentTarget == null || !canDraw()) {
            return;
        }
        float anim = (float) showAnimation.getValue();
        if (anim <= 0.01f) {
            return;
        }
        long now = System.currentTimeMillis();
        if (this.lastUpdateTime == 0) {
            this.lastUpdateTime = now;
            this.lastTrailTime = now;
        }
        long delta = now - this.lastUpdateTime;
        this.lastUpdateTime = now;
        double worldX = getTargetX();
        double worldY = getTargetY() + (((double) currentTarget.method_17682()) / 2.0d);
        double worldZ = getTargetZ();
        float rotSpeed = delta * 0.003f;
        for (int i = 0; i < this.ghosts.size(); i++) {
            GhostData ghost = this.ghosts.get(i);
            ghost.angle += rotSpeed;
            float offset = (float) (((double) i) * 2.0943951023931953d);
            float angle = ghost.angle + offset;
            ghost.position = new class_243(worldX + (Math.sin(angle) * ((double) 0.45f)), worldY + (Math.sin(((double) angle) * 2.0d) * ((double) 0.4f)), worldZ + (Math.cos(angle) * ((double) 0.45f)));
        }
        if (now - this.lastTrailTime >= 30) {
            this.lastTrailTime = now;
            for (GhostData ghost2 : this.ghosts) {
                if (!ghost2.history.isEmpty() && ghost2.position.method_1025(ghost2.history.get(0)) > 10.0d) {
                    ghost2.history.clear();
                }
                ghost2.history.add(0, ghost2.position);
                while (ghost2.history.size() > 20) {
                    ghost2.history.remove(ghost2.history.size() - 1);
                }
            }
        }
        Color color = UIColors.gradient(0);
        int rgb = color.getRGB();
        class_4587 ms = event.matrixStack();
        RenderSystem.setShader(class_10142.field_53880);
        RenderSystem.setShaderTexture(0, FileUtil.getImage("particles/glow"));
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.class_4535.SRC_ALPHA, GlStateManager.class_4534.ONE, GlStateManager.class_4535.ZERO, GlStateManager.class_4534.ONE);
        RenderSystem.disableCull();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        class_287 builder = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1575);
        boolean hasVertices = false;
        for (GhostData ghost3 : this.ghosts) {
            for (int i2 = ghost3.history.size() - 1; i2 >= 0; i2--) {
                if (writePartVertices(builder, ms, ghost3.history.get(i2), i2, ghost3.history.size(), anim, 0.2f, rgb)) {
                    hasVertices = true;
                }
            }
            if (writePartVertices(builder, ms, ghost3.position, 0, 1, anim, 0.2f, rgb)) {
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

    private boolean writePartVertices(class_287 builder, class_4587 ms, class_243 pos, int index, int maxHistory, float anim, float baseSize, int rgb) {
        float progress = maxHistory == 1 ? 0.0f : index / Math.max(1, maxHistory);
        float spriteScale = anim * baseSize * (1.0f - (progress * 0.7f));
        if (spriteScale < 0.03f) {
            return false;
        }
        float alphaVal = anim * (1.0f - (progress * 0.85f));
        if (alphaVal < 0.02f) {
            return false;
        }
        float r = ((rgb >> 16) & 255) / 255.0f;
        float g = ((rgb >> 8) & 255) / 255.0f;
        float b = (rgb & 255) / 255.0f;
        putQuad(builder, ms, pos, spriteScale * 3.4f, r, g, b, alphaVal * 0.28f);
        putQuad(builder, ms, pos, spriteScale * 1.9f, r, g, b, alphaVal * 0.55f);
        putQuad(builder, ms, pos, spriteScale, Math.min(1.0f, r * 1.2f), Math.min(1.0f, g * 1.2f), Math.min(1.0f, b * 1.2f), alphaVal);
        return true;
    }

    private void putQuad(class_287 builder, class_4587 ms, class_243 pos, float size, float r, float g, float b, float a) {
        class_243 cam = mc.method_1561().field_4686.method_19326();
        ms.method_22903();
        ms.method_22904(pos.field_1352 - cam.field_1352, pos.field_1351 - cam.field_1351, pos.field_1350 - cam.field_1350);
        ms.method_22907(class_7833.field_40716.rotationDegrees(-mc.field_1773.method_19418().method_19330()));
        ms.method_22907(class_7833.field_40714.rotationDegrees(mc.field_1773.method_19418().method_19329()));
        Matrix4f matrix = ms.method_23760().method_23761();
        float half = size / 2.0f;
        int ri = (int) (r * 255.0f);
        int gi = (int) (g * 255.0f);
        int bi = (int) (b * 255.0f);
        int ai2 = (int) (a * 255.0f);
        builder.method_22918(matrix, -half, -half, 0.0f).method_22913(0.0f, 1.0f).method_1336(ri, gi, bi, ai2);
        builder.method_22918(matrix, -half, half, 0.0f).method_22913(0.0f, 0.0f).method_1336(ri, gi, bi, ai2);
        builder.method_22918(matrix, half, half, 0.0f).method_22913(1.0f, 0.0f).method_1336(ri, gi, bi, ai2);
        builder.method_22918(matrix, half, -half, 0.0f).method_22913(1.0f, 1.0f).method_1336(ri, gi, bi, ai2);
        ms.method_22909();
    }

    private void initGhosts() {
        this.ghosts.clear();
        for (int i = 0; i < 3; i++) {
            GhostData ghost = new GhostData();
            ghost.angle = (float) (((double) i) * 2.0943951023931953d);
            ghost.position = class_243.field_1353;
            ghost.history = new ArrayList();
            for (int j = 0; j < 10; j++) {
                ghost.history.add(class_243.field_1353);
            }
            this.ghosts.add(ghost);
        }
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/targetesp/modes/TargetEspGhost2$GhostData.class */
    private static class GhostData {
        float angle;
        class_243 position;
        List<class_243> history;

        private GhostData() {
        }
    }
}
