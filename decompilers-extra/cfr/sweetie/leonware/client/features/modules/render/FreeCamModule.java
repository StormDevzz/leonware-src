/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_243
 *  net.minecraft.class_332
 */
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

@ModuleRegister(name="Free Camera", category=Category.RENDER)
public class FreeCamModule
extends Module {
    private static final FreeCamModule instance = new FreeCamModule();
    public final BooleanSetting hideHotbar = new BooleanSetting("\u0423\u0431\u0440\u0430\u0442\u044c \u0445\u043e\u0442\u0431\u0430\u0440").value(false);
    public final BooleanSetting showCoords = new BooleanSetting("\u041f\u043e\u043a\u0430\u0437\u044b\u0432\u0430\u0442\u044c \u043a\u043e\u043e\u0440\u0434\u0438\u043d\u0430\u0442\u044b").value(true);
    public final BooleanSetting freeze = new BooleanSetting("\u0417\u0430\u043c\u043e\u0440\u043e\u0437\u0438\u0442\u044c").value(false);
    public final SliderSetting speedX = new SliderSetting("\u0421\u043a\u043e\u0440\u043e\u0441\u0442\u044c X").value(Float.valueOf(1.0f)).range(0.1f, 20.0f).step(0.1f);
    public final SliderSetting speedY = new SliderSetting("\u0421\u043a\u043e\u0440\u043e\u0441\u0442\u044c Y").value(Float.valueOf(1.0f)).range(0.1f, 20.0f).step(0.1f);
    private class_243 originPos;
    private double fakeX;
    private double fakeY;
    private double fakeZ;
    private double prevFakeX;
    private double prevFakeY;
    private double prevFakeZ;

    public FreeCamModule() {
        this.addSettings(this.hideHotbar, this.showCoords, this.freeze, this.speedX, this.speedY);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        if (FreeCamModule.mc.field_1724 == null) {
            return;
        }
        this.originPos = FreeCamModule.mc.field_1724.method_19538();
        this.fakeX = FreeCamModule.mc.field_1724.method_23317();
        this.fakeY = FreeCamModule.mc.field_1724.method_23318() + (double)FreeCamModule.mc.field_1724.method_18381(FreeCamModule.mc.field_1724.method_18376());
        this.fakeZ = FreeCamModule.mc.field_1724.method_23321();
        this.prevFakeX = this.fakeX;
        this.prevFakeY = this.fakeY;
        this.prevFakeZ = this.fakeZ;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        this.originPos = null;
    }

    @Override
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (FreeCamModule.mc.field_1724 == null) {
                return;
            }
            this.prevFakeX = this.fakeX;
            this.prevFakeY = this.fakeY;
            this.prevFakeZ = this.fakeZ;
            double yaw = Math.toRadians(FreeCamModule.mc.field_1724.method_36454());
            double hSpeed = (double)((Float)this.speedX.getValue()).floatValue() * 0.1;
            double vSpeed = (double)((Float)this.speedY.getValue()).floatValue() * 0.1;
            if (FreeCamModule.mc.field_1690.field_1894.method_1434()) {
                this.fakeX -= Math.sin(yaw) * hSpeed;
                this.fakeZ += Math.cos(yaw) * hSpeed;
            }
            if (FreeCamModule.mc.field_1690.field_1881.method_1434()) {
                this.fakeX += Math.sin(yaw) * hSpeed;
                this.fakeZ -= Math.cos(yaw) * hSpeed;
            }
            if (FreeCamModule.mc.field_1690.field_1913.method_1434()) {
                this.fakeX += Math.cos(yaw) * hSpeed;
                this.fakeZ += Math.sin(yaw) * hSpeed;
            }
            if (FreeCamModule.mc.field_1690.field_1849.method_1434()) {
                this.fakeX -= Math.cos(yaw) * hSpeed;
                this.fakeZ -= Math.sin(yaw) * hSpeed;
            }
            if (FreeCamModule.mc.field_1690.field_1903.method_1434()) {
                this.fakeY += vSpeed;
            }
            if (FreeCamModule.mc.field_1690.field_1832.method_1434()) {
                this.fakeY -= vSpeed;
            }
        }));
        EventListener renderEvent = Render2DEvent.getInstance().subscribe(new Listener<Render2DEvent.Render2DEventData>(event -> {
            if (FreeCamModule.mc.field_1724 == null || this.originPos == null || !((Boolean)this.showCoords.getValue()).booleanValue()) {
                return;
            }
            class_332 context = event.context();
            double dx = this.fakeX - this.originPos.field_1352;
            double dy = this.fakeY - (this.originPos.field_1351 + (double)FreeCamModule.mc.field_1724.method_18381(FreeCamModule.mc.field_1724.method_18376()));
            double dz = this.fakeZ - this.originPos.field_1350;
            String text = String.format("x %.1f y %.1f z %.1f", dx, dy, dz);
            float cx = (float)mc.method_22683().method_4486() / 2.0f;
            float cy = (float)mc.method_22683().method_4502() / 2.0f + 12.0f;
            Fonts.SF_MEDIUM.drawCenteredText(context.method_51448(), text, cx, cy, 7.0f, new Color(255, 255, 255, 200));
        }));
        this.addEvents(updateEvent, renderEvent);
    }

    public boolean shouldHideHotbar() {
        return this.isEnabled() && (Boolean)this.hideHotbar.getValue() != false;
    }

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
}

