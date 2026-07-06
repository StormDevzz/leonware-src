// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.render;

import net.minecraft.class_742;
import lombok.Generated;
import net.minecraft.class_287;
import org.joml.Matrix4f;
import net.minecraft.class_286;
import sweetie.leonware.api.utils.color.ColorUtil;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_289;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.class_10142;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.system.configs.FriendManager;
import sweetie.leonware.client.ui.widget.overlay.StaffsWidget;
import sweetie.leonware.client.ui.widget.Widget;
import sweetie.leonware.client.ui.widget.WidgetManager;
import net.minecraft.class_243;
import java.util.Iterator;
import java.util.List;
import net.minecraft.class_4587;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import net.minecraft.class_1701;
import java.awt.Color;
import net.minecraft.class_1541;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import sweetie.leonware.api.event.events.render.Render3DEvent;
import sweetie.leonware.api.module.setting.Setting;
import java.util.function.Supplier;
import java.util.Objects;
import sweetie.leonware.api.module.setting.MultiBooleanSetting;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Tracers", category = Category.RENDER)
public class TracersModule extends Module
{
    private static final TracersModule instance;
    private final ModeSetting mode;
    private final SliderSetting height;
    private final BooleanSetting onlyStaff;
    private final BooleanSetting friendColor;
    private final BooleanSetting renderDanger;
    private final MultiBooleanSetting dangerTargets;
    
    public TracersModule() {
        this.mode = new ModeSetting("\u0420\u0435\u0436\u0438\u043c").values("\u0421\u0432\u0435\u0440\u0445\u0443", "\u041f\u0440\u0438\u0446\u0435\u043b", "\u0421\u043d\u0438\u0437\u0443").value("\u041f\u0440\u0438\u0446\u0435\u043b");
        this.height = new SliderSetting("\u0412\u044b\u0441\u043e\u0442\u0430").value(1.0f).range(0.0f, 2.0f).step(0.1f);
        this.onlyStaff = new BooleanSetting("\u0422\u043e\u043b\u044c\u043a\u043e \u0441\u0442\u0430\u0444\u0444").value(false);
        this.friendColor = new BooleanSetting("\u041f\u043e\u043c\u0435\u0447\u0430\u0442\u044c \u0434\u0440\u0443\u0437\u0435\u0439").value(true).setVisible(() -> !this.onlyStaff.getValue());
        this.renderDanger = new BooleanSetting("\u0420\u0435\u043d\u0434\u0435\u0440\u0438\u0442\u044c \u043e\u043f\u0430\u0441\u043d\u043e\u0441\u0442\u044c").value(false);
        final MultiBooleanSetting value = new MultiBooleanSetting("\u041e\u043f\u0430\u0441\u043d\u043e\u0441\u0442\u0438").value(new BooleanSetting("TNT").value(true), new BooleanSetting("TNT Minecart").value(true));
        final BooleanSetting renderDanger = this.renderDanger;
        Objects.requireNonNull(renderDanger);
        this.dangerTargets = value.setVisible((Supplier<Boolean>)renderDanger::getValue);
        this.addSettings(this.mode, this.height, this.onlyStaff, this.friendColor, this.renderDanger, this.dangerTargets);
    }
    
    @Override
    public void onEvent() {
        final EventListener render3DEvent = Render3DEvent.getInstance().subscribe(new Listener<Render3DEvent.Render3DEventData>(event -> {
            if (TracersModule.mc.field_1724 == null || TracersModule.mc.field_1687 == null) {
                return;
            }
            else {
                final class_4587 matrices = event.matrixStack();
                final float tickDelta = event.partialTicks();
                class_1657 player = null;
                final List players = TracersModule.mc.field_1687.method_18456().stream().filter(player -> player != TracersModule.mc.field_1724).filter(player -> !player.method_7325()).filter(player -> !this.onlyStaff.getValue() || this.isStaff((class_1657)player)).toList();
                players.iterator();
                final Iterator iterator;
                while (iterator.hasNext()) {
                    player = iterator.next();
                    final Color color = this.getColorForPlayer(player);
                    final class_243 startPos = this.getStartPosition(tickDelta);
                    final class_243 endPos = this.getTargetPosition(player, tickDelta);
                    this.drawLine(matrices, startPos, endPos, color);
                }
                if (this.renderDanger.getValue()) {
                    TracersModule.mc.field_1687.method_18112().iterator();
                    final Iterator iterator2;
                    while (iterator2.hasNext()) {
                        final class_1297 entity = iterator2.next();
                        if (this.dangerTargets.isEnabled("TNT") && entity instanceof class_1541) {
                            final class_243 startPos2 = this.getStartPosition(tickDelta);
                            final class_243 endPos2 = this.getEntityPosition(entity, tickDelta);
                            this.drawLine(matrices, startPos2, endPos2, new Color(255, 50, 0));
                        }
                        if (this.dangerTargets.isEnabled("TNT Minecart") && entity instanceof class_1701) {
                            final class_243 startPos3 = this.getStartPosition(tickDelta);
                            final class_243 endPos3 = this.getEntityPosition(entity, tickDelta);
                            this.drawLine(matrices, startPos3, endPos3, new Color(255, 120, 0));
                        }
                    }
                }
                return;
            }
        }));
        this.addEvents(render3DEvent);
    }
    
    private boolean isStaff(final class_1657 player) {
        StaffsWidget staffsWidget = null;
        for (final Widget w : WidgetManager.getInstance().getWidgets()) {
            if (w instanceof final StaffsWidget staffsWidget2) {
                final StaffsWidget sw = staffsWidget = staffsWidget2;
                break;
            }
        }
        if (staffsWidget == null) {
            return false;
        }
        final String name = player.method_5477().getString();
        return staffsWidget.getStaffList().stream().anyMatch(s -> {
            final String staffName = s.name();
            if (staffName.contains(":")) {
                return staffName.split(":")[0].equalsIgnoreCase(name);
            }
            else {
                return staffName.equalsIgnoreCase(name) || staffName.toLowerCase().contains(name.toLowerCase());
            }
        });
    }
    
    private class_243 getStartPosition(final float tickDelta) {
        final double x1 = TracersModule.mc.field_1724.field_6014 + (TracersModule.mc.field_1724.method_23317() - TracersModule.mc.field_1724.field_6014) * tickDelta;
        final double y1 = TracersModule.mc.field_1724.method_18381(TracersModule.mc.field_1724.method_18376()) + TracersModule.mc.field_1724.field_6036 + (TracersModule.mc.field_1724.method_23318() - TracersModule.mc.field_1724.field_6036) * tickDelta;
        final double z1 = TracersModule.mc.field_1724.field_5969 + (TracersModule.mc.field_1724.method_23321() - TracersModule.mc.field_1724.field_5969) * tickDelta;
        final String s = this.mode.getValue();
        final float yOffset = switch (s) {
            case "\u0421\u0432\u0435\u0440\u0445\u0443" -> 120.0f;
            case "\u0421\u043d\u0438\u0437\u0443" -> -120.0f;
            default -> 0.0f;
        };
        final class_243 vec = new class_243(0.0, (double)yOffset, 75.0).method_1037(-(float)Math.toRadians(TracersModule.mc.field_1773.method_19418().method_19329())).method_1024(-(float)Math.toRadians(TracersModule.mc.field_1773.method_19418().method_19330())).method_1031(x1, y1, z1);
        return vec;
    }
    
    private class_243 getTargetPosition(final class_1657 player, final float tickDelta) {
        final double x = player.field_6014 + (player.method_23317() - player.field_6014) * tickDelta;
        final double y = player.field_6036 + (player.method_23318() - player.field_6036) * tickDelta;
        final double z = player.field_5969 + (player.method_23321() - player.field_5969) * tickDelta;
        return new class_243(x, y + this.height.getValue(), z);
    }
    
    private class_243 getEntityPosition(final class_1297 entity, final float tickDelta) {
        final double x = entity.field_6014 + (entity.method_23317() - entity.field_6014) * tickDelta;
        final double y = entity.field_6036 + (entity.method_23318() - entity.field_6036) * tickDelta;
        final double z = entity.field_5969 + (entity.method_23321() - entity.field_5969) * tickDelta;
        return new class_243(x, y + entity.method_17682() / 2.0f, z);
    }
    
    private Color getColorForPlayer(final class_1657 player) {
        if (this.onlyStaff.getValue()) {
            return new Color(0, 120, 255);
        }
        if (this.friendColor.getValue() && FriendManager.getInstance().contains(player.method_5477().getString())) {
            return UIColors.positiveColor();
        }
        return UIColors.negativeColor();
    }
    
    private void drawLine(final class_4587 matrices, final class_243 start, final class_243 end, final Color color) {
        matrices.method_22903();
        final class_243 cameraPos = TracersModule.mc.field_1773.method_19418().method_19326();
        matrices.method_22904(-cameraPos.field_1352, -cameraPos.field_1351, -cameraPos.field_1350);
        final Matrix4f matrix = matrices.method_23760().method_23761();
        RenderSystem.setShader(class_10142.field_53876);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.lineWidth(1.5f);
        final class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_29344, class_290.field_1576);
        final float[] colors = ColorUtil.normalize(color);
        buffer.method_22918(matrix, (float)start.field_1352, (float)start.field_1351, (float)start.field_1350).method_22915(colors[0], colors[1], colors[2], colors[3]);
        buffer.method_22918(matrix, (float)end.field_1352, (float)end.field_1351, (float)end.field_1350).method_22915(colors[0], colors[1], colors[2], colors[3]);
        class_286.method_43433(buffer.method_60800());
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
        RenderSystem.lineWidth(1.0f);
        matrices.method_22909();
    }
    
    @Generated
    public static TracersModule getInstance() {
        return TracersModule.instance;
    }
    
    static {
        instance = new TracersModule();
    }
}
