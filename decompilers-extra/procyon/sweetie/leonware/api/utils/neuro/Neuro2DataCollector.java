// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.neuro;

import lombok.Generated;
import java.util.ArrayList;
import java.util.List;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import net.minecraft.class_3532;
import sweetie.leonware.api.utils.rotation.RotationUtil;
import net.minecraft.class_1309;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.client.features.modules.combat.AuraModule;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.utils.math.TimerUtil;
import java.io.File;
import sweetie.leonware.api.system.interfaces.QuickImports;

public class Neuro2DataCollector implements QuickImports
{
    private static final Neuro2DataCollector instance;
    private static final File MODELS_DIR;
    private static final int RECORD_EVERY_N_TICKS = 1;
    private static final float MAX_YAW_FILTER = 60.0f;
    private static final float MAX_PITCH_FILTER = 45.0f;
    private boolean recording;
    private final TimerUtil messageTimer;
    private int tickCounter;
    private EventListener updateListener;
    
    public Neuro2DataCollector() {
        this.recording = false;
        this.messageTimer = new TimerUtil();
        this.tickCounter = 0;
    }
    
    public void startRecording() {
        this.recording = true;
        this.tickCounter = 0;
        this.messageTimer.reset();
        this.updateListener = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (this.recording && Neuro2DataCollector.mc.field_1724 != null) {
                final class_1309 target = AuraModule.getInstance().target;
                if (target != null && !target.method_29504()) {
                    ++this.tickCounter;
                    if (this.tickCounter % 1 == 0) {
                        this.recordTick(target);
                        if (this.messageTimer.finished(500L)) {
                            this.messageTimer.reset();
                            final Neuro2Model model = AuraModule.getInstance().getNeuro2Model();
                            this.print("§7[Neuro2] §f\u0417\u0430\u043f\u0438\u0441\u0430\u043d\u043e §a" + model.getDatasetSize() + " §f\u043f\u0430\u0442\u0442\u0435\u0440\u043d\u043e\u0432");
                        }
                    }
                }
            }
        }));
    }
    
    public void stopRecording() {
        this.recording = false;
        if (this.updateListener != null) {
            this.updateListener.unsubscribe();
            this.updateListener = null;
        }
    }
    
    private void recordTick(final class_1309 target) {
        final Rotation perfect = RotationUtil.rotationAt(target.method_19538().method_1031(0.0, (double)(target.method_17682() / 2.0f), 0.0));
        final float perfectYaw = perfect.getYaw();
        final float perfectPitch = class_3532.method_15363(perfect.getPitch(), -90.0f, 90.0f);
        final float currentYaw = Neuro2DataCollector.mc.field_1724.method_36454();
        final float currentPitch = Neuro2DataCollector.mc.field_1724.method_36455();
        final float yawOffset = class_3532.method_15393(currentYaw - perfectYaw);
        final float pitchOffset = currentPitch - perfectPitch;
        if (Math.abs(yawOffset) > 60.0f || Math.abs(pitchOffset) > 45.0f) {
            return;
        }
        final double distance = Neuro2DataCollector.mc.field_1724.method_19538().method_1022(target.method_19538());
        final double targetSpeed = target.method_18798().method_37267();
        final Neuro2Pattern pattern = new Neuro2Pattern(yawOffset, pitchOffset, distance, targetSpeed, Neuro2DataCollector.mc.field_1724.method_24828());
        AuraModule.getInstance().getNeuro2Model().addPattern(pattern);
    }
    
    public boolean save(final String name) {
        final File file = new File(Neuro2DataCollector.MODELS_DIR, name + ".neuro2");
        return AuraModule.getInstance().getNeuro2Model().save(file);
    }
    
    public boolean load(final String name) {
        final File file = new File(Neuro2DataCollector.MODELS_DIR, name + ".neuro2");
        return AuraModule.getInstance().getNeuro2Model().load(file);
    }
    
    public List<String> getSavedNames() {
        final List<String> names = new ArrayList<String>();
        if (!Neuro2DataCollector.MODELS_DIR.exists()) {
            return names;
        }
        final File[] files = Neuro2DataCollector.MODELS_DIR.listFiles((d, n) -> n.endsWith(".neuro2"));
        if (files != null) {
            for (final File f : files) {
                names.add(f.getName().replace(".neuro2", ""));
            }
        }
        return names;
    }
    
    @Generated
    public static Neuro2DataCollector getInstance() {
        return Neuro2DataCollector.instance;
    }
    
    @Generated
    public boolean isRecording() {
        return this.recording;
    }
    
    @Generated
    public void setRecording(final boolean recording) {
        this.recording = recording;
    }
    
    static {
        instance = new Neuro2DataCollector();
        MODELS_DIR = new File(System.getProperty("user.dir"), "LeonWare/neuro2_patterns");
    }
}
