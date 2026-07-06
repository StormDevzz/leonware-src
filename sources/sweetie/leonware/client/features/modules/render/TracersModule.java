package sweetie.leonware.client.features.modules.render;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import lombok.Generated;
import net.minecraft.class_10142;
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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/TracersModule.class */
@ModuleRegister(name = "Tracers", category = Category.RENDER)
public class TracersModule extends Module {
    private static final TracersModule instance = new TracersModule();
    private final ModeSetting mode = new ModeSetting("Режим").values("Сверху", "Прицел", "Снизу").value("Прицел");
    private final SliderSetting height = new SliderSetting("Высота").value(Float.valueOf(1.0f)).range(0.0f, 2.0f).step(0.1f);
    private final BooleanSetting onlyStaff = new BooleanSetting("Только стафф").value((Boolean) false);
    private final BooleanSetting friendColor = new BooleanSetting("Помечать друзей").value((Boolean) true).setVisible(() -> {
        return Boolean.valueOf(!this.onlyStaff.getValue().booleanValue());
    });
    private final BooleanSetting renderDanger = new BooleanSetting("Рендерить опасность").value((Boolean) false);
    private final MultiBooleanSetting dangerTargets;

    @Generated
    public static TracersModule getInstance() {
        return instance;
    }

    /* JADX WARN: Type inference failed for: r1v11, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    /* JADX WARN: Type inference failed for: r1v16, types: [sweetie.leonware.api.module.setting.MultiBooleanSetting] */
    public TracersModule() {
        MultiBooleanSetting multiBooleanSettingValue = new MultiBooleanSetting("Опасности").value(new BooleanSetting("TNT").value((Boolean) true), new BooleanSetting("TNT Minecart").value((Boolean) true));
        BooleanSetting booleanSetting = this.renderDanger;
        Objects.requireNonNull(booleanSetting);
        this.dangerTargets = multiBooleanSettingValue.setVisible(booleanSetting::getValue);
        addSettings(this.mode, this.height, this.onlyStaff, this.friendColor, this.renderDanger, this.dangerTargets);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener render3DEvent = Render3DEvent.getInstance().subscribe(new Listener(event -> {
            if (mc.field_1724 == null || mc.field_1687 == null) {
                return;
            }
            class_4587 matrices = event.matrixStack();
            float tickDelta = event.partialTicks();
            List<class_742> players = mc.field_1687.method_18456().stream().filter(player -> {
                return player != mc.field_1724;
            }).filter(player2 -> {
                return !player2.method_7325();
            }).filter(player3 -> {
                if (this.onlyStaff.getValue().booleanValue()) {
                    return isStaff(player3);
                }
                return true;
            }).toList();
            Iterator<class_742> it = players.iterator();
            while (it.hasNext()) {
                class_1657 player4 = (class_1657) it.next();
                Color color = getColorForPlayer(player4);
                class_243 startPos = getStartPosition(tickDelta);
                class_243 endPos = getTargetPosition(player4, tickDelta);
                drawLine(matrices, startPos, endPos, color);
            }
            if (this.renderDanger.getValue().booleanValue()) {
                for (class_1297 entity : mc.field_1687.method_18112()) {
                    if (this.dangerTargets.isEnabled("TNT") && (entity instanceof class_1541)) {
                        class_243 startPos2 = getStartPosition(tickDelta);
                        class_243 endPos2 = getEntityPosition(entity, tickDelta);
                        drawLine(matrices, startPos2, endPos2, new Color(255, 50, 0));
                    }
                    if (this.dangerTargets.isEnabled("TNT Minecart") && (entity instanceof class_1701)) {
                        class_243 startPos3 = getStartPosition(tickDelta);
                        class_243 endPos3 = getEntityPosition(entity, tickDelta);
                        drawLine(matrices, startPos3, endPos3, new Color(255, 120, 0));
                    }
                }
            }
        }));
        addEvents(render3DEvent);
    }

    private boolean isStaff(class_1657 player) {
        StaffsWidget staffsWidget = null;
        Iterator<Widget> it = WidgetManager.getInstance().getWidgets().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Widget w = it.next();
            if (w instanceof StaffsWidget) {
                StaffsWidget sw = (StaffsWidget) w;
                staffsWidget = sw;
                break;
            }
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
        float f;
        double x1 = mc.field_1724.field_6014 + ((mc.field_1724.method_23317() - mc.field_1724.field_6014) * ((double) tickDelta));
        double y1 = ((double) mc.field_1724.method_18381(mc.field_1724.method_18376())) + mc.field_1724.field_6036 + ((mc.field_1724.method_23318() - mc.field_1724.field_6036) * ((double) tickDelta));
        double z1 = mc.field_1724.field_5969 + ((mc.field_1724.method_23321() - mc.field_1724.field_5969) * ((double) tickDelta));
        switch (this.mode.getValue()) {
            case "Сверху":
                f = 120.0f;
                break;
            case "Снизу":
                f = -120.0f;
                break;
            default:
                f = 0.0f;
                break;
        }
        float yOffset = f;
        class_243 vec = new class_243(0.0d, yOffset, 75.0d).method_1037(-((float) Math.toRadians(mc.field_1773.method_19418().method_19329()))).method_1024(-((float) Math.toRadians(mc.field_1773.method_19418().method_19330()))).method_1031(x1, y1, z1);
        return vec;
    }

    private class_243 getTargetPosition(class_1657 player, float tickDelta) {
        double x = player.field_6014 + ((player.method_23317() - player.field_6014) * ((double) tickDelta));
        double y = player.field_6036 + ((player.method_23318() - player.field_6036) * ((double) tickDelta));
        double z = player.field_5969 + ((player.method_23321() - player.field_5969) * ((double) tickDelta));
        return new class_243(x, y + ((double) this.height.getValue().floatValue()), z);
    }

    private class_243 getEntityPosition(class_1297 entity, float tickDelta) {
        double x = entity.field_6014 + ((entity.method_23317() - entity.field_6014) * ((double) tickDelta));
        double y = entity.field_6036 + ((entity.method_23318() - entity.field_6036) * ((double) tickDelta));
        double z = entity.field_5969 + ((entity.method_23321() - entity.field_5969) * ((double) tickDelta));
        return new class_243(x, y + ((double) (entity.method_17682() / 2.0f)), z);
    }

    private Color getColorForPlayer(class_1657 player) {
        if (this.onlyStaff.getValue().booleanValue()) {
            return new Color(0, 120, 255);
        }
        if (this.friendColor.getValue().booleanValue() && FriendManager.getInstance().contains(player.method_5477().getString())) {
            return UIColors.positiveColor();
        }
        return UIColors.negativeColor();
    }

    private void drawLine(class_4587 matrices, class_243 start, class_243 end, Color color) {
        matrices.method_22903();
        class_243 cameraPos = mc.field_1773.method_19418().method_19326();
        matrices.method_22904(-cameraPos.field_1352, -cameraPos.field_1351, -cameraPos.field_1350);
        Matrix4f matrix = matrices.method_23760().method_23761();
        RenderSystem.setShader(class_10142.field_53876);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.lineWidth(1.5f);
        class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_29344, class_290.field_1576);
        float[] colors = ColorUtil.normalize(color);
        buffer.method_22918(matrix, (float) start.field_1352, (float) start.field_1351, (float) start.field_1350).method_22915(colors[0], colors[1], colors[2], colors[3]);
        buffer.method_22918(matrix, (float) end.field_1352, (float) end.field_1351, (float) end.field_1350).method_22915(colors[0], colors[1], colors[2], colors[3]);
        class_286.method_43433(buffer.method_60800());
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
        RenderSystem.lineWidth(1.0f);
        matrices.method_22909();
    }
}
