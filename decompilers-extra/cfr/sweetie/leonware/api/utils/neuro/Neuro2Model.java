/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package sweetie.leonware.api.utils.neuro;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.Generated;
import sweetie.leonware.api.utils.neuro.Neuro2Pattern;

public class Neuro2Model {
    private static final int MAX_PATTERNS = 15000;
    private final List<Neuro2Pattern> patterns = new CopyOnWriteArrayList<Neuro2Pattern>();
    private boolean trained = false;

    public void addPattern(Neuro2Pattern pattern) {
        this.patterns.add(pattern);
        if (this.patterns.size() > 15000) {
            this.patterns.remove(0);
        }
        this.trained = true;
    }

    public float[] predict(double currentDistance, double targetSpeed) {
        if (this.patterns.isEmpty()) {
            return new float[]{0.0f, 0.0f};
        }
        List<Object> pool = new ArrayList();
        for (Neuro2Pattern p : this.patterns) {
            if (!(Math.abs(p.getDistance() - currentDistance) < 1.5) || !(Math.abs(p.getTargetSpeed() - targetSpeed) < 0.6)) continue;
            pool.add(p);
        }
        if (pool.isEmpty()) {
            int start = Math.max(0, this.patterns.size() - 600);
            pool = this.patterns.subList(start, this.patterns.size());
        }
        Neuro2Pattern best = null;
        double bestScore = Double.MAX_VALUE;
        for (Neuro2Pattern neuro2Pattern : pool) {
            double score = Math.abs(neuro2Pattern.getDistance() - currentDistance) * 2.0 + Math.abs(neuro2Pattern.getTargetSpeed() - targetSpeed) * 1.5;
            if (!(score < bestScore)) continue;
            bestScore = score;
            best = neuro2Pattern;
        }
        if (best == null) {
            return new float[]{0.0f, 0.0f};
        }
        return new float[]{best.getYawOffset(), best.getPitchOffset()};
    }

    public void clear() {
        this.patterns.clear();
        this.trained = false;
    }

    public int getDatasetSize() {
        return this.patterns.size();
    }

    public boolean save(File file) {
        try {
            file.getParentFile().mkdirs();
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));){
                oos.writeObject(new ArrayList<Neuro2Pattern>(this.patterns));
            }
            return true;
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean load(File file) {
        boolean bl;
        if (!file.exists()) {
            return false;
        }
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
        try {
            List loaded = (List)ois.readObject();
            this.patterns.clear();
            this.patterns.addAll(loaded);
            bl = this.trained = !this.patterns.isEmpty();
        }
        catch (Throwable throwable) {
            try {
                try {
                    ois.close();
                }
                catch (Throwable throwable2) {
                    throwable.addSuppressed(throwable2);
                }
                throw throwable;
            }
            catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        ois.close();
        return bl;
    }

    @Generated
    public List<Neuro2Pattern> getPatterns() {
        return this.patterns;
    }

    @Generated
    public boolean isTrained() {
        return this.trained;
    }
}

