// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.services;

import sweetie.leonware.api.system.draggable.Draggable;
import sweetie.leonware.api.module.Module;
import lombok.Generated;
import sweetie.leonware.api.system.configs.MacroManager;
import sweetie.leonware.api.module.ModuleManager;
import sweetie.leonware.LeonWare;
import sweetie.leonware.api.event.events.client.KeyEvent;
import sweetie.leonware.api.system.client.GpsManager;
import sweetie.leonware.api.system.draggable.DraggableManager;
import net.minecraft.class_408;
import sweetie.leonware.api.event.events.render.Render2DEvent;
import sweetie.leonware.api.system.configs.ConfigSkin;
import sweetie.leonware.api.utils.other.SlownessManager;
import sweetie.leonware.api.event.events.client.TickEvent;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.utils.other.ScreenUtil;
import sweetie.leonware.api.event.events.other.ScreenEvent;
import sweetie.leonware.api.system.interfaces.QuickImports;

public class HeartbeatService implements QuickImports
{
    private static final HeartbeatService instance;
    
    public void load() {
        this.keyEvent();
        this.render2dEvent();
        this.tickEvent();
        this.screenEvent();
    }
    
    private void screenEvent() {
        ScreenEvent.getInstance().subscribe(new Listener<ScreenEvent.ScreenEventData>(event -> ScreenUtil.drawButton(event)));
    }
    
    private void tickEvent() {
        TickEvent.getInstance().subscribe(new Listener<TickEvent>(event -> {
            SlownessManager.tick();
            ConfigSkin.getInstance().fetchSkin();
        }));
    }
    
    private void render2dEvent() {
        Render2DEvent.getInstance().subscribe(new Listener<Render2DEvent.Render2DEventData>(event -> {
            if (HeartbeatService.mc.field_1755 instanceof class_408) {
                DraggableManager.getInstance().getDraggables().forEach((s, draggable) -> {
                    if (draggable.getModule().isEnabled()) {
                        draggable.onDraw();
                    }
                    return;
                });
            }
            GpsManager.getInstance().update(event.context());
        }));
    }
    
    private void keyEvent() {
        KeyEvent.getInstance().subscribe(new Listener<KeyEvent.KeyEventData>(event -> {
            if (!LeonWare.isUnhooked) {
                if (event.action() == 1 && event.key() != -999 && event.key() != -1) {
                    final int action = event.action();
                    final int key = event.key() + (event.mouse() ? -100 : 0);
                    if (HeartbeatService.mc.field_1755 == null) {
                        ModuleManager.getInstance().getModules().forEach(module -> {
                            final int bind = module.getBind();
                            if (bind == key && module.hasBind()) {
                                module.toggle(true);
                            }
                            return;
                        });
                        MacroManager.getInstance().onKeyPressed(key);
                    }
                }
            }
        }));
    }
    
    @Generated
    public static HeartbeatService getInstance() {
        return HeartbeatService.instance;
    }
    
    static {
        instance = new HeartbeatService();
    }
}
