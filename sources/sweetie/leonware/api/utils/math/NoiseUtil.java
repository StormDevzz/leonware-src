package sweetie.leonware.api.utils.math;

import org.newsclub.net.unix.AFVSOCKSocketAddress;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/math/NoiseUtil.class */
public class NoiseUtil {
    private static final int PERMUTATION_SIZE = 256;
    private final int[] permutation;

    public NoiseUtil() {
        this(System.nanoTime());
    }

    public NoiseUtil(long seed) {
        this.permutation = new int[512];
        RandomUtil randomUtil = new RandomUtil(seed);
        int[] p = new int[PERMUTATION_SIZE];
        for (int i = 0; i < PERMUTATION_SIZE; i++) {
            p[i] = i;
        }
        for (int i2 = 255; i2 > 0; i2--) {
            int j = randomUtil.nextInt(i2 + 1);
            int temp = p[i2];
            p[i2] = p[j];
            p[j] = temp;
        }
        for (int i3 = 0; i3 < 512; i3++) {
            this.permutation[i3] = p[i3 & 255];
        }
    }

    public float perlinNoise(float x) {
        int xi = ((int) Math.floor(x)) & 255;
        float xf = x - ((float) Math.floor(x));
        float u = fade(xf);
        int a = this.permutation[xi];
        int b = this.permutation[xi + 1];
        return lerp(u, grad(a, xf), grad(b, xf - 1.0f)) * 2.0f;
    }

    public float perlinNoise(float x, float y) {
        int xi = ((int) Math.floor(x)) & 255;
        int yi = ((int) Math.floor(y)) & 255;
        float xf = x - ((float) Math.floor(x));
        float yf = y - ((float) Math.floor(y));
        float u = fade(xf);
        float v = fade(yf);
        int aa = this.permutation[this.permutation[xi] + yi];
        int ab = this.permutation[this.permutation[xi] + yi + 1];
        int ba = this.permutation[this.permutation[xi + 1] + yi];
        int bb = this.permutation[this.permutation[xi + 1] + yi + 1];
        float x1 = lerp(u, grad(aa, xf, yf), grad(ba, xf - 1.0f, yf));
        float x2 = lerp(u, grad(ab, xf, yf - 1.0f), grad(bb, xf - 1.0f, yf - 1.0f));
        return lerp(v, x1, x2);
    }

    public float perlinNoise(float x, float y, float z) {
        int xi = ((int) Math.floor(x)) & 255;
        int yi = ((int) Math.floor(y)) & 255;
        int zi = ((int) Math.floor(z)) & 255;
        float xf = x - ((float) Math.floor(x));
        float yf = y - ((float) Math.floor(y));
        float zf = z - ((float) Math.floor(z));
        float u = fade(xf);
        float v = fade(yf);
        float w = fade(zf);
        int aaa = this.permutation[this.permutation[this.permutation[xi] + yi] + zi];
        int aab = this.permutation[this.permutation[this.permutation[xi] + yi] + zi + 1];
        int aba = this.permutation[this.permutation[this.permutation[xi] + yi + 1] + zi];
        int abb = this.permutation[this.permutation[this.permutation[xi] + yi + 1] + zi + 1];
        int baa = this.permutation[this.permutation[this.permutation[xi + 1] + yi] + zi];
        int bab = this.permutation[this.permutation[this.permutation[xi + 1] + yi] + zi + 1];
        int bba = this.permutation[this.permutation[this.permutation[xi + 1] + yi + 1] + zi];
        int bbb = this.permutation[this.permutation[this.permutation[xi + 1] + yi + 1] + zi + 1];
        float x1 = lerp(u, grad(aaa, xf, yf, zf), grad(baa, xf - 1.0f, yf, zf));
        float x2 = lerp(u, grad(aba, xf, yf - 1.0f, zf), grad(bba, xf - 1.0f, yf - 1.0f, zf));
        float y1 = lerp(v, x1, x2);
        float x12 = lerp(u, grad(aab, xf, yf, zf - 1.0f), grad(bab, xf - 1.0f, yf, zf - 1.0f));
        float x22 = lerp(u, grad(abb, xf, yf - 1.0f, zf - 1.0f), grad(bbb, xf - 1.0f, yf - 1.0f, zf - 1.0f));
        float y2 = lerp(v, x12, x22);
        return lerp(w, y1, y2);
    }

    private float fade(float t) {
        return t * t * t * ((t * ((t * 6.0f) - 15.0f)) + 10.0f);
    }

    private float lerp(float amount, float left, float right) {
        return left + (amount * (right - left));
    }

    private float grad(int hash, float x) {
        return (hash & 1) == 0 ? x : -x;
    }

    private float grad(int hash, float x, float y) {
        switch (hash & 3) {
            case 0:
                return x + y;
            case 1:
                return (-x) + y;
            case AFVSOCKSocketAddress.VMADDR_CID_HOST /* 2 */:
                return x - y;
            case 3:
                return (-x) - y;
            default:
                return 0.0f;
        }
    }

    private float grad(int hash, float x, float y, float z) {
        int h = hash & 15;
        float u = h < 8 ? x : y;
        float v = h < 4 ? y : (h == 12 || h == 14) ? x : z;
        return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
    }

    public float octaveNoise(float x, float y, int octaves, float persistence, float lacunarity) {
        float total = 0.0f;
        float frequency = 1.0f;
        float amplitude = 1.0f;
        float maxValue = 0.0f;
        for (int i = 0; i < octaves; i++) {
            total += perlinNoise(x * frequency, y * frequency) * amplitude;
            maxValue += amplitude;
            amplitude *= persistence;
            frequency *= lacunarity;
        }
        return total / maxValue;
    }
}
