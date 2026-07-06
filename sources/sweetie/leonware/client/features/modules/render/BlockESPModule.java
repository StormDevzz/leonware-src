package sweetie.leonware.client.features.modules.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.Generated;
import net.minecraft.class_2189;
import net.minecraft.class_2213;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2288;
import net.minecraft.class_2338;
import net.minecraft.class_2680;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.render.Render3DEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ColorSetting;
import sweetie.leonware.api.module.setting.RunSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.utils.math.TimerUtil;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.api.utils.render.display.BoxRender;
import sweetie.leonware.client.ui.blockfilter.BlockFilterScreen;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/BlockESPModule.class */
@ModuleRegister(name = "Block ESP", category = Category.RENDER)
public class BlockESPModule extends Module {
    private static final BlockESPModule instance = new BlockESPModule();
    private final SliderSetting range = new SliderSetting("Дистанция").value(Float.valueOf(64.0f)).range(8.0f, 128.0f).step(8.0f);
    private final SliderSetting limitCount = new SliderSetting("Лимит блоков").value(Float.valueOf(250.0f)).range(50.0f, 1000.0f).step(50.0f);
    private final ColorSetting color = new ColorSetting("Цвет").value(new Color(0, 200, 255, 200));
    private final BooleanSetting filled = new BooleanSetting("Заливка").value((Boolean) true);
    private final BooleanSetting outline = new BooleanSetting("Контур").value((Boolean) true);
    private final BooleanSetting illegals = new BooleanSetting("Бедрок/Барьеры").value((Boolean) true);
    private final RunSetting filterButton = new RunSetting("Фильтр блоков").value(() -> {
        mc.method_1507(new BlockFilterScreen());
    });
    private Set<class_2248> selectedBlocks = new HashSet();
    private List<BlockVec> foundBlocks = new ArrayList();
    private final ExecutorService searchThread = Executors.newSingleThreadExecutor();
    private final TimerUtil searchTimer = new TimerUtil();
    private boolean canContinue = true;

    @Generated
    public static BlockESPModule getInstance() {
        return instance;
    }

    @Generated
    public Set<class_2248> getSelectedBlocks() {
        return this.selectedBlocks;
    }

    public BlockESPModule() {
        this.selectedBlocks.add(class_2246.field_10260);
        this.selectedBlocks.add(class_2246.field_10442);
        this.selectedBlocks.add(class_2246.field_29029);
        addSettings(this.range, this.limitCount, this.color, this.filled, this.outline, this.illegals, this.filterButton);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener render3DEvent = Render3DEvent.getInstance().subscribe(new Listener(event -> {
            if (mc.field_1687 == null || mc.field_1724 == null) {
                return;
            }
            if (this.searchTimer.finished(1000L) && this.canContinue) {
                this.canContinue = false;
                CompletableFuture.supplyAsync(this::scan, this.searchThread).thenAccept(this::sync);
            }
            if (this.foundBlocks.isEmpty()) {
                return;
            }
            Color c = this.color.getValue();
            int count = 0;
            float maxCount = this.limitCount.getValue().floatValue();
            for (BlockVec vec : new ArrayList(this.foundBlocks)) {
                if (count < maxCount) {
                    double distSq = mc.field_1724.method_5649(vec.x + 0.5d, vec.y + 0.5d, vec.z + 0.5d);
                    if (distSq <= Math.pow(this.range.getValue().floatValue(), 2.0d)) {
                        float x1 = (float) vec.x;
                        float y1 = (float) vec.y;
                        float z1 = (float) vec.z;
                        float x2 = x1 + 1.0f;
                        float y2 = y1 + 1.0f;
                        float z2 = z1 + 1.0f;
                        if (this.filled.getValue().booleanValue()) {
                            Color fill = new Color(c.getRed(), c.getGreen(), c.getBlue(), 45);
                            RenderUtil.BOX.drawBox(x1, y1, z1, x2, y2, z2, 1.0f, fill, BoxRender.Render.FILL, 0.0f);
                        }
                        if (this.outline.getValue().booleanValue()) {
                            RenderUtil.BOX.drawBox(x1, y1, z1, x2, y2, z2, 1.5f, c, BoxRender.Render.OUTLINE, 0.0f);
                        }
                        count++;
                    }
                } else {
                    return;
                }
            }
        }));
        addEvents(render3DEvent);
    }

    private List<BlockVec> scan() {
        List<BlockVec> tempBlocks = new ArrayList<>();
        if (mc.field_1687 == null || mc.field_1724 == null) {
            return tempBlocks;
        }
        int r = this.range.getValue().intValue();
        int px = (int) mc.field_1724.method_23317();
        int pz = (int) mc.field_1724.method_23321();
        int minY = mc.field_1687.method_31607();
        int maxY = mc.field_1687.method_31607() + mc.field_1687.method_31605();
        for (int x = px - r; x <= px + r; x++) {
            for (int z = pz - r; z <= pz + r; z++) {
                for (int y = minY; y <= maxY; y++) {
                    class_2338 pos = new class_2338(x, y, z);
                    class_2680 state = mc.field_1687.method_8320(pos);
                    class_2248 block = state.method_26204();
                    if (shouldAdd(block, pos)) {
                        tempBlocks.add(new BlockVec(x, y, z));
                    }
                }
            }
        }
        return tempBlocks;
    }

    private void sync(List<BlockVec> b) {
        this.foundBlocks = b;
        this.canContinue = true;
        this.searchTimer.reset();
    }

    private boolean shouldAdd(class_2248 block, class_2338 pos) {
        if (block instanceof class_2189) {
            return false;
        }
        if (this.selectedBlocks.contains(block)) {
            return true;
        }
        if (this.illegals.getValue().booleanValue()) {
            if ((block instanceof class_2288) || (block instanceof class_2213)) {
                return true;
            }
            return block == class_2246.field_9987 && pos.method_10264() > 4 && pos.method_10264() < 123;
        }
        return false;
    }

    @Override // sweetie.leonware.api.module.Module
    public void onEnable() {
        this.foundBlocks.clear();
        this.canContinue = true;
        this.searchTimer.reset();
    }

    @Override // sweetie.leonware.api.module.Module
    public void onDisable() {
        this.foundBlocks.clear();
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/BlockESPModule$BlockVec.class */
    public static class BlockVec {
        public final double x;
        public final double y;
        public final double z;

        public BlockVec(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }
}
