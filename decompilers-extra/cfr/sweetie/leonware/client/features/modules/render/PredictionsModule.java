/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.platform.GlStateManager$class_4534
 *  com.mojang.blaze3d.platform.GlStateManager$class_4535
 *  com.mojang.blaze3d.systems.RenderSystem
 *  lombok.Generated
 *  net.minecraft.class_10142
 *  net.minecraft.class_10156
 *  net.minecraft.class_1297
 *  net.minecraft.class_1657
 *  net.minecraft.class_1667
 *  net.minecraft.class_1676
 *  net.minecraft.class_1684
 *  net.minecraft.class_1685
 *  net.minecraft.class_239$class_240
 *  net.minecraft.class_243
 *  net.minecraft.class_286
 *  net.minecraft.class_287
 *  net.minecraft.class_289
 *  net.minecraft.class_290
 *  net.minecraft.class_293$class_5596
 *  net.minecraft.class_3857
 *  net.minecraft.class_3959
 *  net.minecraft.class_3959$class_242
 *  net.minecraft.class_3959$class_3960
 *  net.minecraft.class_3965
 *  net.minecraft.class_4587
 *  net.minecraft.class_9801
 *  org.joml.Matrix4f
 *  org.joml.Vector2f
 */
package sweetie.leonware.client.features.modules.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Generated;
import net.minecraft.class_10142;
import net.minecraft.class_10156;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_1667;
import net.minecraft.class_1676;
import net.minecraft.class_1684;
import net.minecraft.class_1685;
import net.minecraft.class_239;
import net.minecraft.class_243;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_3857;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_4587;
import net.minecraft.class_9801;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.render.Render2DEvent;
import sweetie.leonware.api.event.events.render.Render3DEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ColorSetting;
import sweetie.leonware.api.module.setting.MultiBooleanSetting;
import sweetie.leonware.api.system.configs.FriendManager;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.math.ProjectionUtil;
import sweetie.leonware.api.utils.player.PlayerUtil;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.api.utils.render.fonts.Font;
import sweetie.leonware.api.utils.render.fonts.Fonts;

@ModuleRegister(name="Predictions", category=Category.RENDER)
public class PredictionsModule
extends Module {
    private static final PredictionsModule instance = new PredictionsModule();
    private final MultiBooleanSetting render = new MultiBooleanSetting("Render").value(new BooleanSetting("Ender pearl").value(true), new BooleanSetting("Trident").value(false), new BooleanSetting("Arrow").value(false));
    private final BooleanSetting walls = new BooleanSetting("Through walls").value(true);
    private final BooleanSetting friend = new BooleanSetting("Friendly indicator").value(false);
    private final ColorSetting friendColor = new ColorSetting("Color").value(new Color(0, 255, 0));
    private final List<Points> points = new ArrayList<Points>();
    private final String UNKNOWN = "\u041d\u0435\u0438\u0437\u0432\u0435\u0441\u0442\u043d\u044b\u0439";

    public PredictionsModule() {
        this.addSettings(this.render, this.walls, this.friend, this.friendColor);
    }

    @Override
    public void onEvent() {
        EventListener render3DEvent = Render3DEvent.getInstance().subscribe(new Listener<Render3DEvent.Render3DEventData>(event -> this.handleRender3D((Render3DEvent.Render3DEventData)event)));
        EventListener render2DEvent = Render2DEvent.getInstance().subscribe(new Listener<Render2DEvent.Render2DEventData>(event -> this.handleRender2D((Render2DEvent.Render2DEventData)event)));
        this.addEvents(render3DEvent, render2DEvent);
    }

    private void handleRender2D(Render2DEvent.Render2DEventData event) {
        Font font = Fonts.SF_MEDIUM;
        class_4587 matrixStack = event.matrixStack();
        for (Points point : this.points) {
            Vector2f project = ProjectionUtil.project((float)point.position.field_1352, (float)point.position.field_1351, (float)point.position.field_1350);
            if (project.x == Float.MAX_VALUE && project.y == Float.MAX_VALUE) continue;
            String text = String.format("%s (%.1f \u0441\u0435\u043a)", point.itemName, (double)(point.ticks * 50) / 1000.0);
            String ownerText = "\u041e\u0442 " + point.ownerName;
            float offset = 3.0f;
            float fontSize = 7.0f;
            float textWidth = font.getWidth(text, fontSize);
            float ownerWidth = font.getWidth(ownerText, fontSize);
            float textHeight = fontSize + offset * 2.0f;
            float addRectWidth = ownerWidth + offset * 2.0f;
            float posX = project.x - textWidth / 2.0f - offset;
            float posY = project.y;
            Color bgColor = point.isFriend && (Boolean)this.friend.getValue() != false ? (Color)this.friendColor.getValue() : UIColors.backgroundBlur();
            Color textColor = UIColors.textColor();
            RenderUtil.BLUR_RECT.draw(matrixStack, posX, posY, textWidth + offset * 2.0f, textHeight, 2.0f, bgColor);
            font.drawText(matrixStack, text, posX + offset, posY + offset, fontSize, textColor);
            if (ownerText.contains("\u041d\u0435\u0438\u0437\u0432\u0435\u0441\u0442\u043d\u044b\u0439")) continue;
            RenderUtil.BLUR_RECT.draw(matrixStack, project.x - addRectWidth / 2.0f, posY + textHeight + 2.0f, addRectWidth, textHeight, 2.0f, bgColor);
            font.drawText(matrixStack, ownerText, project.x - ownerWidth / 2.0f, posY + textHeight + 2.0f + offset, fontSize, textColor);
        }
    }

    private void handleRender3D(Render3DEvent.Render3DEventData event) {
        class_4587 matrixStack = event.matrixStack();
        class_243 renderOffset = PredictionsModule.mc.method_1561().field_4686.method_19326();
        this.points.clear();
        matrixStack.method_22903();
        matrixStack.method_22904(-renderOffset.field_1352, -renderOffset.field_1351, -renderOffset.field_1350);
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask((boolean)false);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate((GlStateManager.class_4535)GlStateManager.class_4535.SRC_ALPHA, (GlStateManager.class_4534)GlStateManager.class_4534.ONE_MINUS_SRC_ALPHA, (GlStateManager.class_4535)GlStateManager.class_4535.ONE, (GlStateManager.class_4534)GlStateManager.class_4534.ZERO);
        RenderSystem.setShader((class_10156)class_10142.field_53876);
        for (class_1297 entity : PredictionsModule.mc.field_1687.method_18112()) {
            boolean isFriend;
            class_1685 trident;
            String name = entity instanceof class_1684 ? ((class_3857)entity).method_7495().method_7964().getString() : entity.method_5477().getString();
            boolean isPearl = entity instanceof class_1684;
            boolean isTrident = entity instanceof class_1685 && !(trident = (class_1685)entity).method_7441() && !trident.field_36331;
            boolean isArrow = entity instanceof class_1667;
            if (!(isPearl && this.render.isEnabled("Ender pearl") || isTrident && this.render.isEnabled("Trident")) && (!isArrow || !this.render.isEnabled("Arrow")) || (isArrow || isTrident) && entity.method_18798().method_1027() < 0.001) continue;
            UUID ownerUuid = ((class_1676)entity).field_22478 != null ? ((class_1676)entity).field_22478 : null;
            class_1657 owner = ownerUuid != null ? PredictionsModule.mc.field_1687.method_18470(ownerUuid) : null;
            boolean bl = isFriend = owner != null && (FriendManager.getInstance().contains(owner.method_5477().getString()) || owner.method_5477().getString().equals(mc.method_1548().method_1676()));
            String ownerName = owner != null ? (mc.method_1548().method_1676().equals(owner.method_5477().getString()) ? "\u0412\u0430\u0441" : owner.method_5477().getString()) : "\u041d\u0435\u0438\u0437\u0432\u0435\u0441\u0442\u043d\u044b\u0439";
            this.predictTrajectory(entity, isFriend, name, ownerName, matrixStack);
        }
        RenderSystem.depthMask((boolean)true);
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
        matrixStack.method_22909();
    }

    private void predictTrajectory(class_1297 entity, boolean isFriend, String itemName, String ownerName, class_4587 matrixStack) {
        Color color = isFriend && (Boolean)this.friend.getValue() != false ? Color.GREEN : UIColors.gradient(entity.field_6012 % 360);
        class_243 motion = entity.method_18798();
        class_243 pos = entity.method_19538();
        int ticks = 0;
        for (int i = 0; i <= 149; ++i) {
            class_3965 hit;
            class_243 prevPos = pos;
            pos = pos.method_1019(motion);
            motion = this.getMotion(entity, motion);
            boolean canSee = (Boolean)this.walls.getValue() != false || PlayerUtil.canSee(pos);
            Matrix4f matrix = matrixStack.method_23760().method_23761();
            class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_29344, class_290.field_1576);
            if (canSee) {
                buffer.method_22918(matrix, (float)prevPos.field_1352, (float)prevPos.field_1351, (float)prevPos.field_1350).method_1336(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
            }
            if ((hit = PredictionsModule.mc.field_1687.method_17742(new class_3959(prevPos, pos, class_3959.class_3960.field_17558, class_3959.class_242.field_1348, entity))).method_17783() == class_239.class_240.field_1332) {
                pos = hit.method_17784();
            }
            if (canSee) {
                buffer.method_22918(matrix, (float)pos.field_1352, (float)pos.field_1351, (float)pos.field_1350).method_1336(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
            }
            if (canSee) {
                class_286.method_43433((class_9801)buffer.method_60800());
            }
            if (hit.method_17783() == class_239.class_240.field_1332 || pos.field_1351 < -128.0) {
                this.points.add(new Points(pos, ticks, isFriend, itemName, ownerName));
                break;
            }
            ++ticks;
        }
    }

    private class_243 getMotion(class_1297 entity, class_243 motion) {
        class_243 motion2 = motion;
        class_243 class_2432 = motion2 = entity.method_5799() ? motion2.method_1021(0.8) : motion2.method_1021(0.99);
        if (!entity.method_5740()) {
            motion2 = motion2.method_1023(0.0, 0.03, 0.0);
        }
        return motion2;
    }

    @Generated
    public static PredictionsModule getInstance() {
        return instance;
    }

    private record Points(class_243 position, int ticks, boolean isFriend, String itemName, String ownerName) {
    }
}

