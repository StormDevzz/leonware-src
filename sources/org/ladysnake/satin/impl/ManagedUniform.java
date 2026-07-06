package org.ladysnake.satin.impl;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_283;
import net.minecraft.class_284;
import net.minecraft.class_5944;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.ladysnake.satin.api.managed.uniform.Uniform1f;
import org.ladysnake.satin.api.managed.uniform.Uniform1i;
import org.ladysnake.satin.api.managed.uniform.Uniform2f;
import org.ladysnake.satin.api.managed.uniform.Uniform2i;
import org.ladysnake.satin.api.managed.uniform.Uniform3f;
import org.ladysnake.satin.api.managed.uniform.Uniform3i;
import org.ladysnake.satin.api.managed.uniform.Uniform4f;
import org.ladysnake.satin.api.managed.uniform.Uniform4i;
import org.ladysnake.satin.api.managed.uniform.UniformMat4;

/* JADX INFO: loaded from: leonware-0.0.3.jar:META-INF/jars/satin-3.0.0-alpha.1.jar:org/ladysnake/satin/impl/ManagedUniform.class */
public final class ManagedUniform extends ManagedUniformBase implements Uniform1i, Uniform2i, Uniform3i, Uniform4i, Uniform1f, Uniform2f, Uniform3f, Uniform4f, UniformMat4 {
    private static final class_284[] NO_TARGETS;
    private final int count;
    private class_284[] targets;
    private int i0;
    private int i1;
    private int i2;
    private int i3;
    private float f0;
    private float f1;
    private float f2;
    private float f3;
    private boolean firstUpload;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ManagedUniform.class.desiredAssertionStatus();
        NO_TARGETS = new class_284[0];
    }

    public ManagedUniform(String name, int count) {
        super(name);
        this.targets = NO_TARGETS;
        this.firstUpload = true;
        this.count = count;
    }

    @Override // org.ladysnake.satin.impl.ManagedUniformBase
    public boolean findUniformTargets(List<class_283> shaders) {
        List<class_284> list = new ArrayList<>();
        for (class_283 shader : shaders) {
            class_284 uniform = shader.method_62922().method_34582(this.name);
            if (uniform != null) {
                if (uniform.method_35661() != this.count) {
                    throw new IllegalStateException("Mismatched number of values, expected " + this.count + " but JSON definition declares " + uniform.method_35661());
                }
                list.add(uniform);
            }
        }
        if (!list.isEmpty()) {
            this.targets = (class_284[]) list.toArray(new class_284[0]);
            syncCurrentValues();
            return true;
        }
        this.targets = NO_TARGETS;
        return false;
    }

    @Override // org.ladysnake.satin.impl.ManagedUniformBase
    public boolean findUniformTarget(class_5944 shader) {
        class_284 uniform = shader.method_34582(this.name);
        if (uniform != null) {
            this.targets = new class_284[]{uniform};
            syncCurrentValues();
            return true;
        }
        this.targets = NO_TARGETS;
        return false;
    }

    private void syncCurrentValues() {
        if (!this.firstUpload) {
            for (class_284 target : this.targets) {
                if (target.method_35663() != null) {
                    target.method_1248(this.i0, this.i1, this.i2, this.i3);
                } else {
                    if (!$assertionsDisabled && target.method_35664() == null) {
                        throw new AssertionError();
                    }
                    target.method_1252(this.f0, this.f1, this.f2, this.f3);
                }
            }
        }
    }

    @Override // org.ladysnake.satin.api.managed.uniform.Uniform1i
    public void set(int value) {
        class_284[] targets = this.targets;
        int nbTargets = targets.length;
        if (nbTargets > 0) {
            if (this.firstUpload || this.i0 != value) {
                for (class_284 target : targets) {
                    target.method_35649(value);
                }
                this.i0 = value;
                this.firstUpload = false;
            }
        }
    }

    @Override // org.ladysnake.satin.api.managed.uniform.Uniform2i
    public void set(int value0, int value1) {
        class_284[] targets = this.targets;
        int nbTargets = targets.length;
        if (nbTargets > 0) {
            if (this.firstUpload || this.i0 != value0 || this.i1 != value1) {
                for (class_284 target : targets) {
                    target.method_35650(value0, value1);
                }
                this.i0 = value0;
                this.i1 = value1;
                this.firstUpload = false;
            }
        }
    }

    @Override // org.ladysnake.satin.api.managed.uniform.Uniform3i
    public void set(int value0, int value1, int value2) {
        class_284[] targets = this.targets;
        int nbTargets = targets.length;
        if (nbTargets > 0) {
            if (this.firstUpload || this.i0 != value0 || this.i1 != value1 || this.i2 != value2) {
                for (class_284 target : targets) {
                    target.method_35651(value0, value1, value2);
                }
                this.i0 = value0;
                this.i1 = value1;
                this.i2 = value2;
                this.firstUpload = false;
            }
        }
    }

    @Override // org.ladysnake.satin.api.managed.uniform.Uniform4i
    public void set(int value0, int value1, int value2, int value3) {
        class_284[] targets = this.targets;
        int nbTargets = targets.length;
        if (nbTargets > 0) {
            if (this.firstUpload || this.i0 != value0 || this.i1 != value1 || this.i2 != value2 || this.i3 != value3) {
                for (class_284 target : targets) {
                    target.method_35656(value0, value1, value2, value3);
                }
                this.i0 = value0;
                this.i1 = value1;
                this.i2 = value2;
                this.i3 = value3;
                this.firstUpload = false;
            }
        }
    }

    @Override // org.ladysnake.satin.api.managed.uniform.Uniform1f
    public void set(float value) {
        class_284[] targets = this.targets;
        int nbTargets = targets.length;
        if (nbTargets > 0) {
            if (this.firstUpload || this.f0 != value) {
                for (class_284 target : targets) {
                    target.method_1251(value);
                }
                this.f0 = value;
                this.firstUpload = false;
            }
        }
    }

    @Override // org.ladysnake.satin.api.managed.uniform.Uniform2f
    public void set(float value0, float value1) {
        class_284[] targets = this.targets;
        int nbTargets = targets.length;
        if (nbTargets > 0) {
            if (this.firstUpload || this.f0 != value0 || this.f1 != value1) {
                for (class_284 target : targets) {
                    target.method_1255(value0, value1);
                }
                this.f0 = value0;
                this.f1 = value1;
                this.firstUpload = false;
            }
        }
    }

    @Override // org.ladysnake.satin.api.managed.uniform.Uniform2f
    public void set(Vector2f value) {
        set(value.x(), value.y());
    }

    @Override // org.ladysnake.satin.api.managed.uniform.Uniform3f
    public void set(float value0, float value1, float value2) {
        class_284[] targets = this.targets;
        int nbTargets = targets.length;
        if (nbTargets > 0) {
            if (this.firstUpload || this.f0 != value0 || this.f1 != value1 || this.f2 != value2) {
                for (class_284 target : targets) {
                    target.method_1249(value0, value1, value2);
                }
                this.f0 = value0;
                this.f1 = value1;
                this.f2 = value2;
                this.firstUpload = false;
            }
        }
    }

    @Override // org.ladysnake.satin.api.managed.uniform.Uniform3f
    public void set(Vector3f value) {
        set(value.x(), value.y(), value.z());
    }

    @Override // org.ladysnake.satin.api.managed.uniform.Uniform4f
    public void set(float value0, float value1, float value2, float value3) {
        class_284[] targets = this.targets;
        int nbTargets = targets.length;
        if (nbTargets > 0) {
            if (this.firstUpload || this.f0 != value0 || this.f1 != value1 || this.f2 != value2 || this.f3 != value3) {
                for (class_284 target : targets) {
                    target.method_35657(value0, value1, value2, value3);
                }
                this.f0 = value0;
                this.f1 = value1;
                this.f2 = value2;
                this.f3 = value3;
                this.firstUpload = false;
            }
        }
    }

    @Override // org.ladysnake.satin.api.managed.uniform.Uniform4f
    public void set(Vector4f value) {
        set(value.x(), value.y(), value.z(), value.w());
    }

    @Override // org.ladysnake.satin.api.managed.uniform.UniformMat4
    public void set(Matrix4f value) {
        class_284[] targets = this.targets;
        int nbTargets = targets.length;
        if (nbTargets > 0) {
            for (class_284 target : targets) {
                target.method_1250(value);
            }
        }
    }

    @Override // org.ladysnake.satin.api.managed.uniform.UniformMat4
    public void setFromArray(float[] values) {
        if (this.count != values.length) {
            throw new IllegalArgumentException("Mismatched values size, expected " + this.count + " but got " + values.length);
        }
        class_284[] targets = this.targets;
        int nbTargets = targets.length;
        if (nbTargets > 0) {
            for (class_284 target : targets) {
                target.method_1253(values);
            }
        }
    }
}
