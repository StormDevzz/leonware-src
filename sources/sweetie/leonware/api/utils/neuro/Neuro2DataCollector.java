package sweetie.leonware.api.utils.neuro;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import net.minecraft.class_1309;
import net.minecraft.class_3532;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.system.interfaces.QuickImports;
import sweetie.leonware.api.utils.math.TimerUtil;
import sweetie.leonware.api.utils.rotation.RotationUtil;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.client.features.modules.combat.AuraModule;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/neuro/Neuro2DataCollector.class */
public class Neuro2DataCollector implements QuickImports {
    private static final Neuro2DataCollector instance = new Neuro2DataCollector();
    private static final File MODELS_DIR = new File(System.getProperty("user.dir"), "LeonWare/neuro2_patterns");
    private static final int RECORD_EVERY_N_TICKS = 1;
    private static final float MAX_YAW_FILTER = 60.0f;
    private static final float MAX_PITCH_FILTER = 45.0f;
    private boolean recording = false;
    private final TimerUtil messageTimer = new TimerUtil();
    private int tickCounter = 0;
    private EventListener updateListener;

    @Generated
    public static Neuro2DataCollector getInstance() {
        return instance;
    }

    @Generated
    public boolean isRecording() {
        return this.recording;
    }

    @Generated
    public void setRecording(boolean recording) {
        this.recording = recording;
    }

    public void startRecording() {
        this.recording = true;
        this.tickCounter = 0;
        this.messageTimer.reset();
        this.updateListener = UpdateEvent.getInstance().subscribe(new Listener(event -> {
            class_1309 target;
            if (!this.recording || mc.field_1724 == null || (target = AuraModule.getInstance().target) == null || target.method_29504()) {
                return;
            }
            this.tickCounter++;
            if (this.tickCounter % 1 != 0) {
                return;
            }
            recordTick(target);
            if (this.messageTimer.finished(500L)) {
                this.messageTimer.reset();
                Neuro2Model model = AuraModule.getInstance().getNeuro2Model();
                print("§7[Neuro2] §fЗаписано §a" + model.getDatasetSize() + " §fпаттернов");
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

    private void recordTick(class_1309 target) {
        Rotation perfect = RotationUtil.rotationAt(target.method_19538().method_1031(0.0d, target.method_17682() / 2.0f, 0.0d));
        float perfectYaw = perfect.getYaw();
        float perfectPitch = class_3532.method_15363(perfect.getPitch(), -90.0f, 90.0f);
        float currentYaw = mc.field_1724.method_36454();
        float currentPitch = mc.field_1724.method_36455();
        float yawOffset = class_3532.method_15393(currentYaw - perfectYaw);
        float pitchOffset = currentPitch - perfectPitch;
        if (Math.abs(yawOffset) > MAX_YAW_FILTER || Math.abs(pitchOffset) > MAX_PITCH_FILTER) {
            return;
        }
        double distance = mc.field_1724.method_19538().method_1022(target.method_19538());
        double targetSpeed = target.method_18798().method_37267();
        Neuro2Pattern pattern = new Neuro2Pattern(yawOffset, pitchOffset, distance, targetSpeed, mc.field_1724.method_24828());
        AuraModule.getInstance().getNeuro2Model().addPattern(pattern);
    }

    public boolean save(String name) {
        File file = new File(MODELS_DIR, name + ".neuro2");
        return AuraModule.getInstance().getNeuro2Model().save(file);
    }

    public boolean load(String name) {
        File file = new File(MODELS_DIR, name + ".neuro2");
        return AuraModule.getInstance().getNeuro2Model().load(file);
    }

    public List<String> getSavedNames() {
        List<String> names = new ArrayList<>();
        if (!MODELS_DIR.exists()) {
            return names;
        }
        File[] files = MODELS_DIR.listFiles((d, n) -> {
            return n.endsWith(".neuro2");
        });
        if (files != null) {
            for (File f : files) {
                names.add(f.getName().replace(".neuro2", ""));
            }
        }
        return names;
    }
}
