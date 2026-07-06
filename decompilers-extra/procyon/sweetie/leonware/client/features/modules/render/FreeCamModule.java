// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.render;

import lombok.Generated;
import net.minecraft.class_332;
import sweetie.leonware.api.event.EventListener;
import java.awt.Color;
import sweetie.leonware.api.utils.render.fonts.Fonts;
import sweetie.leonware.api.event.events.render.Render2DEvent;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.setting.Setting;
import net.minecraft.class_243;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Free Camera", category = Category.RENDER)
public class FreeCamModule extends Module
{
    private static final FreeCamModule instance;
    public final BooleanSetting hideHotbar;
    public final BooleanSetting showCoords;
    public final BooleanSetting freeze;
    public final SliderSetting speedX;
    public final SliderSetting speedY;
    private class_243 originPos;
    private double fakeX;
    private double fakeY;
    private double fakeZ;
    private double prevFakeX;
    private double prevFakeY;
    private double prevFakeZ;
    
    public FreeCamModule() {
        this.hideHotbar = new BooleanSetting("\u0423\u0431\u0440\u0430\u0442\u044c \u0445\u043e\u0442\u0431\u0430\u0440").value(false);
        this.showCoords = new BooleanSetting("\u041f\u043e\u043a\u0430\u0437\u044b\u0432\u0430\u0442\u044c \u043a\u043e\u043e\u0440\u0434\u0438\u043d\u0430\u0442\u044b").value(true);
        this.freeze = new BooleanSetting("\u0417\u0430\u043c\u043e\u0440\u043e\u0437\u0438\u0442\u044c").value(false);
        this.speedX = new SliderSetting("\u0421\u043a\u043e\u0440\u043e\u0441\u0442\u044c X").value(1.0f).range(0.1f, 20.0f).step(0.1f);
        this.speedY = new SliderSetting("\u0421\u043a\u043e\u0440\u043e\u0441\u0442\u044c Y").value(1.0f).range(0.1f, 20.0f).step(0.1f);
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
        this.fakeY = FreeCamModule.mc.field_1724.method_23318() + FreeCamModule.mc.field_1724.method_18381(FreeCamModule.mc.field_1724.method_18376());
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
        final EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (FreeCamModule.mc.field_1724 == null) {
                return;
            }
            else {
                this.prevFakeX = this.fakeX;
                this.prevFakeY = this.fakeY;
                this.prevFakeZ = this.fakeZ;
                final double yaw = Math.toRadians(FreeCamModule.mc.field_1724.method_36454());
                final double hSpeed = this.speedX.getValue() * 0.1;
                final double vSpeed = this.speedY.getValue() * 0.1;
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
                return;
            }
        }));
        final EventListener renderEvent = Render2DEvent.getInstance().subscribe(new Listener<Render2DEvent.Render2DEventData>(event -> {
            if (FreeCamModule.mc.field_1724 == null || this.originPos == null || !this.showCoords.getValue()) {
                return;
            }
            else {
                final class_332 context = event.context();
                final double dx = this.fakeX - this.originPos.field_1352;
                final double dy = this.fakeY - (this.originPos.field_1351 + FreeCamModule.mc.field_1724.method_18381(FreeCamModule.mc.field_1724.method_18376()));
                final double dz = this.fakeZ - this.originPos.field_1350;
                final String text = String.format("x %.1f y %.1f z %.1f", dx, dy, dz);
                final float cx = FreeCamModule.mc.method_22683().method_4486() / 2.0f;
                final float cy = FreeCamModule.mc.method_22683().method_4502() / 2.0f + 12.0f;
                Fonts.SF_MEDIUM.drawCenteredText(context.method_51448(), text, cx, cy, 7.0f, new Color(255, 255, 255, 200));
                return;
            }
        }));
        this.addEvents(updateEvent, renderEvent);
    }
    
    public boolean shouldHideHotbar() {
        return this.isEnabled() && this.hideHotbar.getValue();
    }
    
    @Generated
    public static FreeCamModule getInstance() {
        return FreeCamModule.instance;
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
    
    static {
        instance = new FreeCamModule();
    }
}
