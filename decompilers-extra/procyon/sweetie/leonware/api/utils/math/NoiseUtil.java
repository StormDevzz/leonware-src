// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.math;

public class NoiseUtil
{
    private static final int PERMUTATION_SIZE = 256;
    private final int[] permutation;
    
    public NoiseUtil() {
        this(System.nanoTime());
    }
    
    public NoiseUtil(final long seed) {
        this.permutation = new int[512];
        final RandomUtil randomUtil = new RandomUtil(seed);
        final int[] p = new int[256];
        for (int i = 0; i < 256; ++i) {
            p[i] = i;
        }
        for (int i = 255; i > 0; --i) {
            final int j = randomUtil.nextInt(i + 1);
            final int temp = p[i];
            p[i] = p[j];
            p[j] = temp;
        }
        for (int i = 0; i < 512; ++i) {
            this.permutation[i] = p[i & 0xFF];
        }
    }
    
    public float perlinNoise(final float x) {
        final int xi = (int)Math.floor(x) & 0xFF;
        final float xf = x - (float)Math.floor(x);
        final float u = this.fade(xf);
        final int a = this.permutation[xi];
        final int b = this.permutation[xi + 1];
        return this.lerp(u, this.grad(a, xf), this.grad(b, xf - 1.0f)) * 2.0f;
    }
    
    public float perlinNoise(final float x, final float y) {
        final int xi = (int)Math.floor(x) & 0xFF;
        final int yi = (int)Math.floor(y) & 0xFF;
        final float xf = x - (float)Math.floor(x);
        final float yf = y - (float)Math.floor(y);
        final float u = this.fade(xf);
        final float v = this.fade(yf);
        final int aa = this.permutation[this.permutation[xi] + yi];
        final int ab = this.permutation[this.permutation[xi] + yi + 1];
        final int ba = this.permutation[this.permutation[xi + 1] + yi];
        final int bb = this.permutation[this.permutation[xi + 1] + yi + 1];
        final float x2 = this.lerp(u, this.grad(aa, xf, yf), this.grad(ba, xf - 1.0f, yf));
        final float x3 = this.lerp(u, this.grad(ab, xf, yf - 1.0f), this.grad(bb, xf - 1.0f, yf - 1.0f));
        return this.lerp(v, x2, x3);
    }
    
    public float perlinNoise(final float x, final float y, final float z) {
        final int xi = (int)Math.floor(x) & 0xFF;
        final int yi = (int)Math.floor(y) & 0xFF;
        final int zi = (int)Math.floor(z) & 0xFF;
        final float xf = x - (float)Math.floor(x);
        final float yf = y - (float)Math.floor(y);
        final float zf = z - (float)Math.floor(z);
        final float u = this.fade(xf);
        final float v = this.fade(yf);
        final float w = this.fade(zf);
        final int aaa = this.permutation[this.permutation[this.permutation[xi] + yi] + zi];
        final int aab = this.permutation[this.permutation[this.permutation[xi] + yi] + zi + 1];
        final int aba = this.permutation[this.permutation[this.permutation[xi] + yi + 1] + zi];
        final int abb = this.permutation[this.permutation[this.permutation[xi] + yi + 1] + zi + 1];
        final int baa = this.permutation[this.permutation[this.permutation[xi + 1] + yi] + zi];
        final int bab = this.permutation[this.permutation[this.permutation[xi + 1] + yi] + zi + 1];
        final int bba = this.permutation[this.permutation[this.permutation[xi + 1] + yi + 1] + zi];
        final int bbb = this.permutation[this.permutation[this.permutation[xi + 1] + yi + 1] + zi + 1];
        float x2 = this.lerp(u, this.grad(aaa, xf, yf, zf), this.grad(baa, xf - 1.0f, yf, zf));
        float x3 = this.lerp(u, this.grad(aba, xf, yf - 1.0f, zf), this.grad(bba, xf - 1.0f, yf - 1.0f, zf));
        final float y2 = this.lerp(v, x2, x3);
        x2 = this.lerp(u, this.grad(aab, xf, yf, zf - 1.0f), this.grad(bab, xf - 1.0f, yf, zf - 1.0f));
        x3 = this.lerp(u, this.grad(abb, xf, yf - 1.0f, zf - 1.0f), this.grad(bbb, xf - 1.0f, yf - 1.0f, zf - 1.0f));
        final float y3 = this.lerp(v, x2, x3);
        return this.lerp(w, y2, y3);
    }
    
    private float fade(final float t) {
        return t * t * t * (t * (t * 6.0f - 15.0f) + 10.0f);
    }
    
    private float lerp(final float amount, final float left, final float right) {
        return left + amount * (right - left);
    }
    
    private float grad(final int hash, final float x) {
        return ((hash & 0x1) == 0x0) ? x : (-x);
    }
    
    private float grad(final int hash, final float x, final float y) {
        switch (hash & 0x3) {
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
            default: {
                return 0.0f;
            }
        }
    }
    
    private float grad(final int hash, final float x, final float y, final float z) {
        final int h = hash & 0xF;
        final float u = (h < 8) ? x : y;
        final float v = (h < 4) ? y : ((h == 12 || h == 14) ? x : z);
        return (((h & 0x1) == 0x0) ? u : (-u)) + (((h & 0x2) == 0x0) ? v : (-v));
    }
    
    public float octaveNoise(final float x, final float y, final int octaves, final float persistence, final float lacunarity) {
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
