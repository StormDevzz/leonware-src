package sweetie.leonware.client.features.modules.render;

import java.awt.Color;
import lombok.Generated;
import net.minecraft.class_243;
import net.minecraft.class_332;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.event.events.render.Render2DEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.utils.render.fonts.Fonts;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/FreeCamModule.class */
@ModuleRegister(name = "Free Camera", category = Category.RENDER)
public class FreeCamModule extends Module {
    private static final FreeCamModule instance = new FreeCamModule();
    public final BooleanSetting hideHotbar = new BooleanSetting("Убрать хотбар").value((Boolean) false);
    public final BooleanSetting showCoords = new BooleanSetting("Показывать координаты").value((Boolean) true);
    public final BooleanSetting freeze = new BooleanSetting("Заморозить").value((Boolean) false);
    public final SliderSetting speedX = new SliderSetting("Скорость X").value(Float.valueOf(1.0f)).range(0.1f, 20.0f).step(0.1f);
    public final SliderSetting speedY = new SliderSetting("Скорость Y").value(Float.valueOf(1.0f)).range(0.1f, 20.0f).step(0.1f);
    private class_243 originPos;
    private double fakeX;
    private double fakeY;
    private double fakeZ;
    private double prevFakeX;
    private double prevFakeY;
    private double prevFakeZ;

    @Generated
    public static FreeCamModule getInstance() {
        return instance;
    }

    @Generated
    public double getFakeX() {
        return this.fakeX;
    }

    @Generated
    public double getFakeY() {
        return this.fakeY;
    }

    @Generated
    public double getFakeZ() {
        return this.fakeZ;
    }

    @Generated
    public double getPrevFakeX() {
        return this.prevFakeX;
    }

    @Generated
    public double getPrevFakeY() {
        return this.prevFakeY;
    }

    @Generated
    public double getPrevFakeZ() {
        return this.prevFakeZ;
    }

    public FreeCamModule() {
        addSettings(this.hideHotbar, this.showCoords, this.freeze, this.speedX, this.speedY);
    }

    @Override // sweetie.leonware.api.module.Module
    public void onEnable() {
        super.onEnable();
        if (mc.field_1724 == null) {
            return;
        }
        this.originPos = mc.field_1724.method_19538();
        this.fakeX = mc.field_1724.method_23317();
        this.fakeY = mc.field_1724.method_23318() + ((double) mc.field_1724.method_18381(mc.field_1724.method_18376()));
        this.fakeZ = mc.field_1724.method_23321();
        this.prevFakeX = this.fakeX;
        this.prevFakeY = this.fakeY;
        this.prevFakeZ = this.fakeZ;
    }

    @Override // sweetie.leonware.api.module.Module
    public void onDisable() {
        super.onDisable();
        this.originPos = null;
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener(event -> {
            if (mc.field_1724 == null) {
                return;
            }
            this.prevFakeX = this.fakeX;
            this.prevFakeY = this.fakeY;
            this.prevFakeZ = this.fakeZ;
            double yaw = Math.toRadians(mc.field_1724.method_36454());
            double hSpeed = ((double) this.speedX.getValue().floatValue()) * 0.1d;
            double vSpeed = ((double) this.speedY.getValue().floatValue()) * 0.1d;
            if (mc.field_1690.field_1894.method_1434()) {
                this.fakeX -= Math.sin(yaw) * hSpeed;
                this.fakeZ += Math.cos(yaw) * hSpeed;
            }
            if (mc.field_1690.field_1881.method_1434()) {
                this.fakeX += Math.sin(yaw) * hSpeed;
                this.fakeZ -= Math.cos(yaw) * hSpeed;
            }
            if (mc.field_1690.field_1913.method_1434()) {
                this.fakeX += Math.cos(yaw) * hSpeed;
                this.fakeZ += Math.sin(yaw) * hSpeed;
            }
            if (mc.field_1690.field_1849.method_1434()) {
                this.fakeX -= Math.cos(yaw) * hSpeed;
                this.fakeZ -= Math.sin(yaw) * hSpeed;
            }
            if (mc.field_1690.field_1903.method_1434()) {
                this.fakeY += vSpeed;
            }
            if (mc.field_1690.field_1832.method_1434()) {
                this.fakeY -= vSpeed;
            }
        }));
        EventListener renderEvent = Render2DEvent.getInstance().subscribe(new Listener(event2 -> {
            if (mc.field_1724 == null || this.originPos == null || !this.showCoords.getValue().booleanValue()) {
                return;
            }
            class_332 context = event2.context();
            double dx = this.fakeX - this.originPos.field_1352;
            double dy = this.fakeY - (this.originPos.field_1351 + ((double) mc.field_1724.method_18381(mc.field_1724.method_18376())));
            double dz = this.fakeZ - this.originPos.field_1350;
            String text = String.format("x %.1f y %.1f z %.1f", Double.valueOf(dx), Double.valueOf(dy), Double.valueOf(dz));
            float cx = mc.method_22683().method_4486() / 2.0f;
            float cy = (mc.method_22683().method_4502() / 2.0f) + 12.0f;
            Fonts.SF_MEDIUM.drawCenteredText(context.method_51448(), text, cx, cy, 7.0f, new Color(255, 255, 255, 200));
        }));
        addEvents(updateEvent, renderEvent);
    }

    public boolean shouldHideHotbar() {
        return isEnabled() && this.hideHotbar.getValue().booleanValue();
    }
}
