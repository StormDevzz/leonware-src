/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  lombok.Generated
 *  net.minecraft.class_10142
 *  net.minecraft.class_10156
 *  net.minecraft.class_1297
 *  net.minecraft.class_1541
 *  net.minecraft.class_1657
 *  net.minecraft.class_1701
 *  net.minecraft.class_243
 *  net.minecraft.class_286
 *  net.minecraft.class_287
 *  net.minecraft.class_289
 *  net.minecraft.class_290
 *  net.minecraft.class_293$class_5596
 *  net.minecraft.class_4587
 *  net.minecraft.class_742
 *  net.minecraft.class_9801
 *  org.joml.Matrix4f
 */
package sweetie.leonware.client.features.modules.render;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.List;
import lombok.Generated;
import net.minecraft.class_10142;
import net.minecraft.class_10156;
import net.minecraft.class_1297;
import net.minecraft.class_1541;
import net.minecraft.class_1657;
import net.minecraft.class_1701;
import net.minecraft.class_243;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_4587;
import net.minecraft.class_742;
import net.minecraft.class_9801;
import org.joml.Matrix4f;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.render.Render3DEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.MultiBooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.system.configs.FriendManager;
import sweetie.leonware.api.utils.color.ColorUtil;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.client.ui.widget.Widget;
import sweetie.leonware.client.ui.widget.WidgetManager;
import sweetie.leonware.client.ui.widget.overlay.StaffsWidget;

@ModuleRegister(name="Tracers", category=Category.RENDER)
public class TracersModule
extends Module {
    private static final TracersModule instance = new TracersModule();
    private final ModeSetting mode = new ModeSetting("\u0420\u0435\u0436\u0438\u043c").values("\u0421\u0432\u0435\u0440\u0445\u0443", "\u041f\u0440\u0438\u0446\u0435\u043b", "\u0421\u043d\u0438\u0437\u0443").value("\u041f\u0440\u0438\u0446\u0435\u043b");
    private final SliderSetting height = new SliderSetting("\u0412\u044b\u0441\u043e\u0442\u0430").value(Float.valueOf(1.0f)).range(0.0f, 2.0f).step(0.1f);
    private final BooleanSetting onlyStaff = new BooleanSetting("\u0422\u043e\u043b\u044c\u043a\u043e \u0441\u0442\u0430\u0444\u0444").value(false);
    private final BooleanSetting friendColor = new BooleanSetting("\u041f\u043e\u043c\u0435\u0447\u0430\u0442\u044c \u0434\u0440\u0443\u0437\u0435\u0439").value(true).setVisible(() -> (Boolean)this.onlyStaff.getValue() == false);
    private final BooleanSetting renderDanger = new BooleanSetting("\u0420\u0435\u043d\u0434\u0435\u0440\u0438\u0442\u044c \u043e\u043f\u0430\u0441\u043d\u043e\u0441\u0442\u044c").value(false);
    private final MultiBooleanSetting dangerTargets = new MultiBooleanSetting("\u041e\u043f\u0430\u0441\u043d\u043e\u0441\u0442\u0438").value(new BooleanSetting("TNT").value(true), new BooleanSetting("TNT Minecart").value(true)).setVisible(this.renderDanger::getValue);

    public TracersModule() {
        this.addSettings(this.mode, this.height, this.onlyStaff, this.friendColor, this.renderDanger, this.dangerTargets);
    }

    @Override
    public void onEvent() {
        EventListener render3DEvent = Render3DEvent.getInstance().subscribe(new Listener<Render3DEvent.Render3DEventData>(event -> {
            if (TracersModule.mc.field_1724 == null || TracersModule.mc.field_1687 == null) {
                return;
            }
            class_4587 matrices = event.matrixStack();
            float tickDelta = event.partialTicks();
            List<class_742> players = TracersModule.mc.field_1687.method_18456().stream().filter(player -> player != TracersModule.mc.field_1724).filter(player -> !player.method_7325()).filter(player -> {
                if (((Boolean)this.onlyStaff.getValue()).booleanValue()) {
                    return this.isStaff((class_1657)player);
                }
                return true;
            }).toList();
            for (class_1657 class_16572 : players) {
                Color color = this.getColorForPlayer(class_16572);
                class_243 startPos = this.getStartPosition(tickDelta);
                class_243 endPos = this.getTargetPosition(class_16572, tickDelta);
                this.drawLine(matrices, startPos, endPos, color);
            }
            if (((Boolean)this.renderDanger.getValue()).booleanValue()) {
                for (class_1297 class_12972 : TracersModule.mc.field_1687.method_18112()) {
                    class_243 endPos;
                    class_243 startPos;
                    if (this.dangerTargets.isEnabled("TNT") && class_12972 instanceof class_1541) {
                        startPos = this.getStartPosition(tickDelta);
                        endPos = this.getEntityPosition(class_12972, tickDelta);
                        this.drawLine(matrices, startPos, endPos, new Color(255, 50, 0));
                    }
                    if (!this.dangerTargets.isEnabled("TNT Minecart") || !(class_12972 instanceof class_1701)) continue;
                    startPos = this.getStartPosition(tickDelta);
                    endPos = this.getEntityPosition(class_12972, tickDelta);
                    this.drawLine(matrices, startPos, endPos, new Color(255, 120, 0));
                }
            }
        }));
        this.addEvents(render3DEvent);
    }

    private boolean isStaff(class_1657 player) {
        StaffsWidget staffsWidget = null;
        for (Widget w : WidgetManager.getInstance().getWidgets()) {
            StaffsWidget sw;
            if (!(w instanceof StaffsWidget)) continue;
            staffsWidget = sw = (StaffsWidget)w;
            break;
        }
        if (staffsWidget == null) {
            return false;
        }
        String name = player.method_5477().getString();
        return staffsWidget.getStaffList().stream().anyMatch(s -> {
            String staffName = s.name();
            if (staffName.contains(":")) {
                return staffName.split(":")[0].equalsIgnoreCase(name);
            }
            return staffName.equalsIgnoreCase(name) || staffName.toLowerCase().contains(name.toLowerCase());
        });
    }

    private class_243 getStartPosition(float tickDelta) {
        double x1 = TracersModule.mc.field_1724.field_6014 + (TracersModule.mc.field_1724.method_23317() - TracersModule.mc.field_1724.field_6014) * (double)tickDelta;
        double y1 = (double)TracersModule.mc.field_1724.method_18381(TracersModule.mc.field_1724.method_18376()) + TracersModule.mc.field_1724.field_6036 + (TracersModule.mc.field_1724.method_23318() - TracersModule.mc.field_1724.field_6036) * (double)tickDelta;
        double z1 = TracersModule.mc.field_1724.field_5969 + (TracersModule.mc.field_1724.method_23321() - TracersModule.mc.field_1724.field_5969) * (double)tickDelta;
        float yOffset = switch ((String)this.mode.getValue()) {
            case "\u0421\u0432\u0435\u0440\u0445\u0443" -> 120.0f;
            case "\u0421\u043d\u0438\u0437\u0443" -> -120.0f;
            default -> 0.0f;
        };
        class_243 vec = new class_243(0.0, (double)yOffset, 75.0).method_1037(-((float)Math.toRadians(TracersModule.mc.field_1773.method_19418().method_19329()))).method_1024(-((float)Math.toRadians(TracersModule.mc.field_1773.method_19418().method_19330()))).method_1031(x1, y1, z1);
        return vec;
    }

    private class_243 getTargetPosition(class_1657 player, float tickDelta) {
        double x = player.field_6014 + (player.method_23317() - player.field_6014) * (double)tickDelta;
        double y = player.field_6036 + (player.method_23318() - player.field_6036) * (double)tickDelta;
        double z = player.field_5969 + (player.method_23321() - player.field_5969) * (double)tickDelta;
        return new class_243(x, y + (double)((Float)this.height.getValue()).floatValue(), z);
    }

    private class_243 getEntityPosition(class_1297 entity, float tickDelta) {
        double x = entity.field_6014 + (entity.method_23317() - entity.field_6014) * (double)tickDelta;
        double y = entity.field_6036 + (entity.method_23318() - entity.field_6036) * (double)tickDelta;
        double z = entity.field_5969 + (entity.method_23321() - entity.field_5969) * (double)tickDelta;
        return new class_243(x, y + (double)(entity.method_17682() / 2.0f), z);
    }

    private Color getColorForPlayer(class_1657 player) {
        if (((Boolean)this.onlyStaff.getValue()).booleanValue()) {
            return new Color(0, 120, 255);
        }
        if (((Boolean)this.friendColor.getValue()).booleanValue() && FriendManager.getInstance().contains(player.method_5477().getString())) {
            return UIColors.positiveColor();
        }
        return UIColors.negativeColor();
    }

    private void drawLine(class_4587 matrices, class_243 start, class_243 end, Color color) {
        matrices.method_22903();
        class_243 cameraPos = TracersModule.mc.field_1773.method_19418().method_19326();
        matrices.method_22904(-cameraPos.field_1352, -cameraPos.field_1351, -cameraPos.field_1350);
        Matrix4f matrix = matrices.method_23760().method_23761();
        RenderSystem.setShader((class_10156)class_10142.field_53876);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask((boolean)false);
        RenderSystem.lineWidth((float)1.5f);
        class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_29344, class_290.field_1576);
        float[] colors = ColorUtil.normalize(color);
        buffer.method_22918(matrix, (float)start.field_1352, (float)start.field_1351, (float)start.field_1350).method_22915(colors[0], colors[1], colors[2], colors[3]);
        buffer.method_22918(matrix, (float)end.field_1352, (float)end.field_1351, (float)end.field_1350).method_22915(colors[0], colors[1], colors[2], colors[3]);
        class_286.method_43433((class_9801)buffer.method_60800());
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask((boolean)true);
        RenderSystem.disableBlend();
        RenderSystem.lineWidth((float)1.0f);
        matrices.method_22909();
    }

    @Generated
    public static TracersModule getInstance() {
        return instance;
    }
}

