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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/neuro/Neuro2Model.class */
public class Neuro2Model {
    private static final int MAX_PATTERNS = 15000;
    private final List<Neuro2Pattern> patterns = new CopyOnWriteArrayList();
    private boolean trained = false;

    @Generated
    public List<Neuro2Pattern> getPatterns() {
        return this.patterns;
    }

    @Generated
    public boolean isTrained() {
        return this.trained;
    }

    public void addPattern(Neuro2Pattern pattern) {
        this.patterns.add(pattern);
        if (this.patterns.size() > MAX_PATTERNS) {
            this.patterns.remove(0);
        }
        this.trained = true;
    }

    public float[] predict(double currentDistance, double targetSpeed) {
        if (this.patterns.isEmpty()) {
            return new float[]{0.0f, 0.0f};
        }
        List<Neuro2Pattern> pool = new ArrayList();
        for (Neuro2Pattern p : this.patterns) {
            if (Math.abs(p.getDistance() - currentDistance) < 1.5d && Math.abs(p.getTargetSpeed() - targetSpeed) < 0.6d) {
                pool.add(p);
            }
        }
        if (pool.isEmpty()) {
            int start = Math.max(0, this.patterns.size() - 600);
            pool = this.patterns.subList(start, this.patterns.size());
        }
        Neuro2Pattern best = null;
        double bestScore = Double.MAX_VALUE;
        for (Neuro2Pattern p2 : pool) {
            double score = (Math.abs(p2.getDistance() - currentDistance) * 2.0d) + (Math.abs(p2.getTargetSpeed() - targetSpeed) * 1.5d);
            if (score < bestScore) {
                bestScore = score;
                best = p2;
            }
        }
        return best == null ? new float[]{0.0f, 0.0f} : new float[]{best.getYawOffset(), best.getPitchOffset()};
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
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            try {
                oos.writeObject(new ArrayList(this.patterns));
                oos.close();
                return true;
            } finally {
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean load(File file) {
        if (!file.exists()) {
            return false;
        }
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
            try {
                List<Neuro2Pattern> loaded = (List) ois.readObject();
                this.patterns.clear();
                this.patterns.addAll(loaded);
                this.trained = !this.patterns.isEmpty();
                boolean z = this.trained;
                ois.close();
                return z;
            } finally {
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
