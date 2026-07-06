// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.neuro;

import lombok.Generated;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.io.OutputStream;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;

public class Neuro2Model
{
    private static final int MAX_PATTERNS = 15000;
    private final List<Neuro2Pattern> patterns;
    private boolean trained;
    
    public Neuro2Model() {
        this.patterns = new CopyOnWriteArrayList<Neuro2Pattern>();
        this.trained = false;
    }
    
    public void addPattern(final Neuro2Pattern pattern) {
        this.patterns.add(pattern);
        if (this.patterns.size() > 15000) {
            this.patterns.remove(0);
        }
        this.trained = true;
    }
    
    public float[] predict(final double currentDistance, final double targetSpeed) {
        if (this.patterns.isEmpty()) {
            return new float[] { 0.0f, 0.0f };
        }
        List<Neuro2Pattern> pool = new ArrayList<Neuro2Pattern>();
        for (final Neuro2Pattern p : this.patterns) {
            if (Math.abs(p.getDistance() - currentDistance) < 1.5 && Math.abs(p.getTargetSpeed() - targetSpeed) < 0.6) {
                pool.add(p);
            }
        }
        if (pool.isEmpty()) {
            final int start = Math.max(0, this.patterns.size() - 600);
            pool = this.patterns.subList(start, this.patterns.size());
        }
        Neuro2Pattern best = null;
        double bestScore = Double.MAX_VALUE;
        for (final Neuro2Pattern p2 : pool) {
            final double score = Math.abs(p2.getDistance() - currentDistance) * 2.0 + Math.abs(p2.getTargetSpeed() - targetSpeed) * 1.5;
            if (score < bestScore) {
                bestScore = score;
                best = p2;
            }
        }
        if (best == null) {
            return new float[] { 0.0f, 0.0f };
        }
        return new float[] { best.getYawOffset(), best.getPitchOffset() };
    }
    
    public void clear() {
        this.patterns.clear();
        this.trained = false;
    }
    
    public int getDatasetSize() {
        return this.patterns.size();
    }
    
    public boolean save(final File file) {
        try {
            file.getParentFile().mkdirs();
            try (final ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                oos.writeObject(new ArrayList(this.patterns));
            }
            return true;
        }
        catch (final IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean load(final File file) {
        if (!file.exists()) {
            return false;
        }
        try (final ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            final List<Neuro2Pattern> loaded = (List<Neuro2Pattern>)ois.readObject();
            this.patterns.clear();
            this.patterns.addAll(loaded);
            this.trained = !this.patterns.isEmpty();
            return this.trained;
        }
        catch (final Exception e) {
            e.printStackTrace();
            return false;
        }
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
