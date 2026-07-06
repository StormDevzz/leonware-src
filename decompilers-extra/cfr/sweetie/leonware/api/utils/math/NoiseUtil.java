/*
 * Decompiled with CFR 0.152.
 */
package sweetie.leonware.api.utils.math;

import sweetie.leonware.api.utils.math.RandomUtil;

public class NoiseUtil {
    private static final int PERMUTATION_SIZE = 256;
    private final int[] permutation = new int[512];

    public NoiseUtil() {
        this(System.nanoTime());
    }

    public NoiseUtil(long seed) {
        int i;
        RandomUtil randomUtil = new RandomUtil(seed);
        int[] p = new int[256];
        for (i = 0; i < 256; ++i) {
            p[i] = i;
        }
        for (i = 255; i > 0; --i) {
            int j = randomUtil.nextInt(i + 1);
            int temp = p[i];
            p[i] = p[j];
            p[j] = temp;
        }
        for (i = 0; i < 512; ++i) {
            this.permutation[i] = p[i & 0xFF];
        }
    }

    public float perlinNoise(float x) {
        int xi = (int)Math.floor(x) & 0xFF;
        float xf = x - (float)Math.floor(x);
        float u = this.fade(xf);
        int a = this.permutation[xi];
        int b = this.permutation[xi + 1];
        return this.lerp(u, this.grad(a, xf), this.grad(b, xf - 1.0f)) * 2.0f;
    }

    public float perlinNoise(float x, float y) {
        int xi = (int)Math.floor(x) & 0xFF;
        int yi = (int)Math.floor(y) & 0xFF;
        float xf = x - (float)Math.floor(x);
        float yf = y - (float)Math.floor(y);
        float u = this.fade(xf);
        float v = this.fade(yf);
        int aa = this.permutation[this.permutation[xi] + yi];
        int ab = this.permutation[this.permutation[xi] + yi + 1];
        int ba = this.permutation[this.permutation[xi + 1] + yi];
        int bb = this.permutation[this.permutation[xi + 1] + yi + 1];
        float x1 = this.lerp(u, this.grad(aa, xf, yf), this.grad(ba, xf - 1.0f, yf));
        float x2 = this.lerp(u, this.grad(ab, xf, yf - 1.0f), this.grad(bb, xf - 1.0f, yf - 1.0f));
        return this.lerp(v, x1, x2);
    }

    public float perlinNoise(float x, float y, float z) {
        int xi = (int)Math.floor(x) & 0xFF;
        int yi = (int)Math.floor(y) & 0xFF;
        int zi = (int)Math.floor(z) & 0xFF;
        float xf = x - (float)Math.floor(x);
        float yf = y - (float)Math.floor(y);
        float zf = z - (float)Math.floor(z);
        float u = this.fade(xf);
        float v = this.fade(yf);
        float w = this.fade(zf);
        int aaa = this.permutation[this.permutation[this.permutation[xi] + yi] + zi];
        int aab = this.permutation[this.permutation[this.permutation[xi] + yi] + zi + 1];
        int aba = this.permutation[this.permutation[this.permutation[xi] + yi + 1] + zi];
        int abb = this.permutation[this.permutation[this.permutation[xi] + yi + 1] + zi + 1];
        int baa = this.permutation[this.permutation[this.permutation[xi + 1] + yi] + zi];
        int bab = this.permutation[this.permutation[this.permutation[xi + 1] + yi] + zi + 1];
        int bba = this.permutation[this.permutation[this.permutation[xi + 1] + yi + 1] + zi];
        int bbb = this.permutation[this.permutation[this.permutation[xi + 1] + yi + 1] + zi + 1];
        float x1 = this.lerp(u, this.grad(aaa, xf, yf, zf), this.grad(baa, xf - 1.0f, yf, zf));
        float x2 = this.lerp(u, this.grad(aba, xf, yf - 1.0f, zf), this.grad(bba, xf - 1.0f, yf - 1.0f, zf));
        float y1 = this.lerp(v, x1, x2);
        x1 = this.lerp(u, this.grad(aab, xf, yf, zf - 1.0f), this.grad(bab, xf - 1.0f, yf, zf - 1.0f));
        x2 = this.lerp(u, this.grad(abb, xf, yf - 1.0f, zf - 1.0f), this.grad(bbb, xf - 1.0f, yf - 1.0f, zf - 1.0f));
        float y2 = this.lerp(v, x1, x2);
        return this.lerp(w, y1, y2);
    }

    private float fade(float t) {
        return t * t * t * (t * (t * 6.0f - 15.0f) + 10.0f);
    }

    private float lerp(float amount, float left, float right) {
        return left + amount * (right - left);
    }

    private float grad(int hash, float x) {
        return (hash & 1) == 0 ? x : -x;
    }

    private float grad(int hash, float x, float y) {
        switch (hash & 3) {
            case 0: {
                return x + y;
            }
            case 1: {
                return -x + y;
            }
            case 2: {
                return x - y;
            }
            case 3: {
                return -x - y;
            }
        }
        return 0.0f;
    }

    private float grad(int hash, float x, float y, float z) {
        float u;
        int h = hash & 0xF;
        float f = u = h < 8 ? x : y;
        float v = h < 4 ? y : (h == 12 || h == 14 ? x : z);
        return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
    }

    public float octaveNoise(float x, float y, int octaves, float persistence, float lacunarity) {
        float total = 0.0f;
        float frequency = 1.0f;
        float amplitude = 1.0f;
        float maxValue = 0.0f;
        for (int i = 0; i < octaves; ++i) {
            total += this.perlinNoise(x * frequency, y * frequency) * amplitude;
            maxValue += amplitude;
            amplitude *= persistence;
            frequency *= lacunarity;
        }
        return total / maxValue;
    }
}

