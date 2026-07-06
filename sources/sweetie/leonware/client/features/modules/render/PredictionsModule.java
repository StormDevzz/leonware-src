package sweetie.leonware.client.features.modules.render;

import java.awt.Color;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import net.minecraft.class_1297;
import net.minecraft.class_239;
import net.minecraft.class_243;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_4587;
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
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.math.ProjectionUtil;
import sweetie.leonware.api.utils.player.PlayerUtil;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.api.utils.render.fonts.Font;
import sweetie.leonware.api.utils.render.fonts.Fonts;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/PredictionsModule.class */
@ModuleRegister(name = "Predictions", category = Category.RENDER)
public class PredictionsModule extends Module {
    private static final PredictionsModule instance = new PredictionsModule();
    private final MultiBooleanSetting render = new MultiBooleanSetting("Render").value(new BooleanSetting("Ender pearl").value((Boolean) true), new BooleanSetting("Trident").value((Boolean) false), new BooleanSetting("Arrow").value((Boolean) false));
    private final BooleanSetting walls = new BooleanSetting("Through walls").value((Boolean) true);
    private final BooleanSetting friend = new BooleanSetting("Friendly indicator").value((Boolean) false);
    private final ColorSetting friendColor = new ColorSetting("Color").value(new Color(0, 255, 0));
    private final List<Points> points = new ArrayList();
    private final String UNKNOWN = "Неизвестный";

    @Generated
    public static PredictionsModule getInstance() {
        return instance;
    }

    public PredictionsModule() {
        addSettings(this.render, this.walls, this.friend, this.friendColor);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener render3DEvent = Render3DEvent.getInstance().subscribe(new Listener(event -> {
            handleRender3D(event);
        }));
        EventListener render2DEvent = Render2DEvent.getInstance().subscribe(new Listener(event2 -> {
            handleRender2D(event2);
        }));
        addEvents(render3DEvent, render2DEvent);
    }

    private void handleRender2D(Render2DEvent.Render2DEventData event) {
        Font font = Fonts.SF_MEDIUM;
        class_4587 matrixStack = event.matrixStack();
        for (Points point : this.points) {
            Vector2f project = ProjectionUtil.project((float) point.position.field_1352, (float) point.position.field_1351, (float) point.position.field_1350);
            if (project.x != Float.MAX_VALUE || project.y != Float.MAX_VALUE) {
                String text = String.format("%s (%.1f сек)", point.itemName, Double.valueOf(((double) (point.ticks * 50)) / 1000.0d));
                String ownerText = "От " + point.ownerName;
                float textWidth = font.getWidth(text, 7.0f);
                float ownerWidth = font.getWidth(ownerText, 7.0f);
                float textHeight = 7.0f + (3.0f * 2.0f);
                float addRectWidth = ownerWidth + (3.0f * 2.0f);
                float posX = (project.x - (textWidth / 2.0f)) - 3.0f;
                float posY = project.y;
                Color bgColor = (point.isFriend && this.friend.getValue().booleanValue()) ? this.friendColor.getValue() : UIColors.backgroundBlur();
                Color textColor = UIColors.textColor();
                RenderUtil.BLUR_RECT.draw(matrixStack, posX, posY, textWidth + (3.0f * 2.0f), textHeight, 2.0f, bgColor);
                font.drawText(matrixStack, text, posX + 3.0f, posY + 3.0f, 7.0f, textColor);
                if (!ownerText.contains("Неизвестный")) {
                    RenderUtil.BLUR_RECT.draw(matrixStack, project.x - (addRectWidth / 2.0f), posY + textHeight + 2.0f, addRectWidth, textHeight, 2.0f, bgColor);
                    font.drawText(matrixStack, ownerText, project.x - (ownerWidth / 2.0f), posY + textHeight + 2.0f + 3.0f, 7.0f, textColor);
                }
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x00c9  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void handleRender3D(sweetie.leonware.api.event.events.render.Render3DEvent.Render3DEventData r9) {
        /*
            Method dump skipped, instruction units count: 477
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: sweetie.leonware.client.features.modules.render.PredictionsModule.handleRender3D(sweetie.leonware.api.event.events.render.Render3DEvent$Render3DEventData):void");
    }

    private void predictTrajectory(class_1297 entity, boolean isFriend, String itemName, String ownerName, class_4587 matrixStack) {
        Color color = (isFriend && this.friend.getValue().booleanValue()) ? Color.GREEN : UIColors.gradient(entity.field_6012 % 360);
        class_243 motion = entity.method_18798();
        class_243 pos = entity.method_19538();
        int ticks = 0;
        for (int i = 0; i <= 149; i++) {
            class_243 prevPos = pos;
            pos = pos.method_1019(motion);
            motion = getMotion(entity, motion);
            boolean canSee = this.walls.getValue().booleanValue() || PlayerUtil.canSee(pos);
            Matrix4f matrix = matrixStack.method_23760().method_23761();
            class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_29344, class_290.field_1576);
            if (canSee) {
                buffer.method_22918(matrix, (float) prevPos.field_1352, (float) prevPos.field_1351, (float) prevPos.field_1350).method_1336(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
            }
            class_3965 hit = mc.field_1687.method_17742(new class_3959(prevPos, pos, class_3959.class_3960.field_17558, class_3959.class_242.field_1348, entity));
            if (hit.method_17783() == class_239.class_240.field_1332) {
                pos = hit.method_17784();
            }
            if (canSee) {
                buffer.method_22918(matrix, (float) pos.field_1352, (float) pos.field_1351, (float) pos.field_1350).method_1336(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
            }
            if (canSee) {
                class_286.method_43433(buffer.method_60800());
            }
            if (hit.method_17783() == class_239.class_240.field_1332 || pos.field_1351 < -128.0d) {
                this.points.add(new Points(pos, ticks, isFriend, itemName, ownerName));
                return;
            }
            ticks++;
        }
    }

    private class_243 getMotion(class_1297 entity, class_243 motion) {
        class_243 motion2 = entity.method_5799() ? motion.method_1021(0.8d) : motion.method_1021(0.99d);
        if (!entity.method_5740()) {
            motion2 = motion2.method_1023(0.0d, 0.03d, 0.0d);
        }
        return motion2;
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/PredictionsModule$Points.class */
    private static final class Points extends Record {
        private final class_243 position;
        private final int ticks;
        private final boolean isFriend;
        private final String itemName;
        private final String ownerName;

        private Points(class_243 position, int ticks, boolean isFriend, String itemName, String ownerName) {
            this.position = position;
            this.ticks = ticks;
            this.isFriend = isFriend;
            this.itemName = itemName;
            this.ownerName = ownerName;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Points.class), Points.class, "position;ticks;isFriend;itemName;ownerName", "FIELD:Lsweetie/leonware/client/features/modules/render/PredictionsModule$Points;->position:Lnet/minecraft/class_243;", "FIELD:Lsweetie/leonware/client/features/modules/render/PredictionsModule$Points;->ticks:I", "FIELD:Lsweetie/leonware/client/features/modules/render/PredictionsModule$Points;->isFriend:Z", "FIELD:Lsweetie/leonware/client/features/modules/render/PredictionsModule$Points;->itemName:Ljava/lang/String;", "FIELD:Lsweetie/leonware/client/features/modules/render/PredictionsModule$Points;->ownerName:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Points.class), Points.class, "position;ticks;isFriend;itemName;ownerName", "FIELD:Lsweetie/leonware/client/features/modules/render/PredictionsModule$Points;->position:Lnet/minecraft/class_243;", "FIELD:Lsweetie/leonware/client/features/modules/render/PredictionsModule$Points;->ticks:I", "FIELD:Lsweetie/leonware/client/features/modules/render/PredictionsModule$Points;->isFriend:Z", "FIELD:Lsweetie/leonware/client/features/modules/render/PredictionsModule$Points;->itemName:Ljava/lang/String;", "FIELD:Lsweetie/leonware/client/features/modules/render/PredictionsModule$Points;->ownerName:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Points.class, Object.class), Points.class, "position;ticks;isFriend;itemName;ownerName", "FIELD:Lsweetie/leonware/client/features/modules/render/PredictionsModule$Points;->position:Lnet/minecraft/class_243;", "FIELD:Lsweetie/leonware/client/features/modules/render/PredictionsModule$Points;->ticks:I", "FIELD:Lsweetie/leonware/client/features/modules/render/PredictionsModule$Points;->isFriend:Z", "FIELD:Lsweetie/leonware/client/features/modules/render/PredictionsModule$Points;->itemName:Ljava/lang/String;", "FIELD:Lsweetie/leonware/client/features/modules/render/PredictionsModule$Points;->ownerName:Ljava/lang/String;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public class_243 position() {
            return this.position;
        }

        public int ticks() {
            return this.ticks;
        }

        public boolean isFriend() {
            return this.isFriend;
        }

        public String itemName() {
            return this.itemName;
        }

        public String ownerName() {
            return this.ownerName;
        }
    }
}
