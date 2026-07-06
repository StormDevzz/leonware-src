package sweetie.leonware.client.services;

import lombok.Generated;
import net.minecraft.class_408;
import sweetie.leonware.LeonWare;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.KeyEvent;
import sweetie.leonware.api.event.events.client.TickEvent;
import sweetie.leonware.api.event.events.other.ScreenEvent;
import sweetie.leonware.api.event.events.render.Render2DEvent;
import sweetie.leonware.api.module.ModuleManager;
import sweetie.leonware.api.system.client.GpsManager;
import sweetie.leonware.api.system.configs.ConfigSkin;
import sweetie.leonware.api.system.configs.MacroManager;
import sweetie.leonware.api.system.draggable.DraggableManager;
import sweetie.leonware.api.system.interfaces.QuickImports;
import sweetie.leonware.api.utils.other.ScreenUtil;
import sweetie.leonware.api.utils.other.SlownessManager;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/services/HeartbeatService.class */
public class HeartbeatService implements QuickImports {
    private static final HeartbeatService instance = new HeartbeatService();

    @Generated
    public static HeartbeatService getInstance() {
        return instance;
    }

    public void load() {
        keyEvent();
        render2dEvent();
        tickEvent();
        screenEvent();
    }

    private void screenEvent() {
        ScreenEvent.getInstance().subscribe(new Listener(event -> {
            ScreenUtil.drawButton(event);
        }));
    }

    private void tickEvent() {
        TickEvent.getInstance().subscribe(new Listener(event -> {
            SlownessManager.tick();
            ConfigSkin.getInstance().fetchSkin();
        }));
    }

    private void render2dEvent() {
        Render2DEvent.getInstance().subscribe(new Listener(event -> {
            if (mc.field_1755 instanceof class_408) {
                DraggableManager.getInstance().getDraggables().forEach((s, draggable) -> {
                    if (draggable.getModule().isEnabled()) {
                        draggable.onDraw();
                    }
                });
            }
            GpsManager.getInstance().update(event.context());
        }));
    }

    private void keyEvent() {
        KeyEvent.getInstance().subscribe(new Listener(event -> {
            if (LeonWare.isUnhooked || event.action() != 1 || event.key() == -999 || event.key() == -1) {
                return;
            }
            event.action();
            int key = event.key() + (event.mouse() ? -100 : 0);
            if (mc.field_1755 == null) {
                ModuleManager.getInstance().getModules().forEach(module -> {
                    int bind = module.getBind();
                    if (bind == key && module.hasBind()) {
                        module.toggle(true);
                    }
                });
                MacroManager.getInstance().onKeyPressed(key);
            }
        }));
    }
}
