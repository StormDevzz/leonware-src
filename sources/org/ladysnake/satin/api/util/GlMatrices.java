package org.ladysnake.satin.api.util;

import com.mojang.blaze3d.systems.RenderSystem;
import java.nio.FloatBuffer;
import java.util.Arrays;
import javax.annotation.Nonnull;
import org.apiguardian.api.API;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

/* JADX INFO: loaded from: leonware-0.0.3.jar:META-INF/jars/satin-3.0.0-alpha.1.jar:org/ladysnake/satin/api/util/GlMatrices.class */
public final class GlMatrices {
    private static final FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
    private static final float[] inArray = new float[16];
    private static final float[] outArray = new float[16];

    @API(status = API.Status.MAINTAINED)
    public static FloatBuffer getTmpBuffer() {
        return buffer.clear();
    }

    @API(status = API.Status.MAINTAINED)
    public static FloatBuffer getProjectionMatrix(FloatBuffer outMat) {
        GL11.glGetFloatv(2983, outMat);
        outMat.rewind();
        return outMat;
    }

    @API(status = API.Status.MAINTAINED)
    public static FloatBuffer getProjectionMatrixInverse(FloatBuffer outMat) {
        getProjectionMatrix(outMat);
        invertMat4FB(outMat, outMat);
        return outMat;
    }

    @API(status = API.Status.MAINTAINED)
    public static FloatBuffer getModelViewMatrix(FloatBuffer outMat) {
        GL11.glGetFloatv(2982, outMat);
        outMat.rewind();
        return outMat;
    }

    @API(status = API.Status.MAINTAINED)
    public static FloatBuffer getModelViewMatrixInverse(FloatBuffer outMat) {
        getModelViewMatrix(outMat);
        invertMat4FB(outMat, outMat);
        return outMat;
    }

    @API(status = API.Status.MAINTAINED)
    @Nonnull
    public static Matrix4f getInverseTransformMatrix(Matrix4f outMat) {
        Matrix4f projection = RenderSystem.getProjectionMatrix();
        Matrix4f modelView = RenderSystem.getModelViewMatrix();
        outMat.identity();
        outMat.mul(projection);
        outMat.mul(modelView);
        outMat.invert();
        return outMat;
    }

    @API(status = API.Status.MAINTAINED)
    public static void invertMat4(float[] matOut, float[] m) {
        float m00 = ((((((m[5] * m[10]) * m[15]) - ((m[5] * m[11]) * m[14])) - ((m[9] * m[6]) * m[15])) + ((m[9] * m[7]) * m[14])) + ((m[13] * m[6]) * m[11])) - ((m[13] * m[7]) * m[10]);
        float m01 = (((((((-m[1]) * m[10]) * m[15]) + ((m[1] * m[11]) * m[14])) + ((m[9] * m[2]) * m[15])) - ((m[9] * m[3]) * m[14])) - ((m[13] * m[2]) * m[11])) + (m[13] * m[3] * m[10]);
        float m02 = ((((((m[1] * m[6]) * m[15]) - ((m[1] * m[7]) * m[14])) - ((m[5] * m[2]) * m[15])) + ((m[5] * m[3]) * m[14])) + ((m[13] * m[2]) * m[7])) - ((m[13] * m[3]) * m[6]);
        float m03 = (((((((-m[1]) * m[6]) * m[11]) + ((m[1] * m[7]) * m[10])) + ((m[5] * m[2]) * m[11])) - ((m[5] * m[3]) * m[10])) - ((m[9] * m[2]) * m[7])) + (m[9] * m[3] * m[6]);
        float m10 = (((((((-m[4]) * m[10]) * m[15]) + ((m[4] * m[11]) * m[14])) + ((m[8] * m[6]) * m[15])) - ((m[8] * m[7]) * m[14])) - ((m[12] * m[6]) * m[11])) + (m[12] * m[7] * m[10]);
        float m11 = ((((((m[0] * m[10]) * m[15]) - ((m[0] * m[11]) * m[14])) - ((m[8] * m[2]) * m[15])) + ((m[8] * m[3]) * m[14])) + ((m[12] * m[2]) * m[11])) - ((m[12] * m[3]) * m[10]);
        float m12 = (((((((-m[0]) * m[6]) * m[15]) + ((m[0] * m[7]) * m[14])) + ((m[4] * m[2]) * m[15])) - ((m[4] * m[3]) * m[14])) - ((m[12] * m[2]) * m[7])) + (m[12] * m[3] * m[6]);
        float m13 = ((((((m[0] * m[6]) * m[11]) - ((m[0] * m[7]) * m[10])) - ((m[4] * m[2]) * m[11])) + ((m[4] * m[3]) * m[10])) + ((m[8] * m[2]) * m[7])) - ((m[8] * m[3]) * m[6]);
        float m20 = ((((((m[4] * m[9]) * m[15]) - ((m[4] * m[11]) * m[13])) - ((m[8] * m[5]) * m[15])) + ((m[8] * m[7]) * m[13])) + ((m[12] * m[5]) * m[11])) - ((m[12] * m[7]) * m[9]);
        float m21 = (((((((-m[0]) * m[9]) * m[15]) + ((m[0] * m[11]) * m[13])) + ((m[8] * m[1]) * m[15])) - ((m[8] * m[3]) * m[13])) - ((m[12] * m[1]) * m[11])) + (m[12] * m[3] * m[9]);
        float m22 = ((((((m[0] * m[5]) * m[15]) - ((m[0] * m[7]) * m[13])) - ((m[4] * m[1]) * m[15])) + ((m[4] * m[3]) * m[13])) + ((m[12] * m[1]) * m[7])) - ((m[12] * m[3]) * m[5]);
        float m23 = (((((((-m[0]) * m[5]) * m[11]) + ((m[0] * m[7]) * m[9])) + ((m[4] * m[1]) * m[11])) - ((m[4] * m[3]) * m[9])) - ((m[8] * m[1]) * m[7])) + (m[8] * m[3] * m[5]);
        float m30 = (((((((-m[4]) * m[9]) * m[14]) + ((m[4] * m[10]) * m[13])) + ((m[8] * m[5]) * m[14])) - ((m[8] * m[6]) * m[13])) - ((m[12] * m[5]) * m[10])) + (m[12] * m[6] * m[9]);
        float m31 = ((((((m[0] * m[9]) * m[14]) - ((m[0] * m[10]) * m[13])) - ((m[8] * m[1]) * m[14])) + ((m[8] * m[2]) * m[13])) + ((m[12] * m[1]) * m[10])) - ((m[12] * m[2]) * m[9]);
        float m32 = (((((((-m[0]) * m[5]) * m[14]) + ((m[0] * m[6]) * m[13])) + ((m[4] * m[1]) * m[14])) - ((m[4] * m[2]) * m[13])) - ((m[12] * m[1]) * m[6])) + (m[12] * m[2] * m[5]);
        float m33 = ((((((m[0] * m[5]) * m[10]) - ((m[0] * m[6]) * m[9])) - ((m[4] * m[1]) * m[10])) + ((m[4] * m[2]) * m[9])) + ((m[8] * m[1]) * m[6])) - ((m[8] * m[2]) * m[5]);
        float det = (m[0] * m00) + (m[1] * m10) + (m[2] * m20) + (m[3] * m30);
        matOut[0] = m00;
        matOut[1] = m01;
        matOut[2] = m02;
        matOut[3] = m03;
        matOut[4] = m10;
        matOut[5] = m11;
        matOut[6] = m12;
        matOut[7] = m13;
        matOut[8] = m20;
        matOut[9] = m21;
        matOut[10] = m22;
        matOut[11] = m23;
        matOut[12] = m30;
        matOut[13] = m31;
        matOut[14] = m32;
        matOut[15] = m33;
        if (det != 0.0d) {
            for (int i = 0; i < 16; i++) {
                int i2 = i;
                matOut[i2] = matOut[i2] / det;
            }
            return;
        }
        Arrays.fill(matOut, 0.0f);
    }

    @API(status = API.Status.MAINTAINED)
    public static void invertMat4FB(FloatBuffer fbInvOut, FloatBuffer fbMatIn) {
        invertMat4FBFA(fbInvOut, fbMatIn, outArray, inArray);
    }

    private static void invertMat4FBFA(FloatBuffer fbInvOut, FloatBuffer fbMatIn, float[] faInv, float[] faMat) {
        fbMatIn.get(faMat);
        invertMat4(faInv, faMat);
        fbInvOut.put(faInv);
        fbInvOut.rewind();
    }

    @API(status = API.Status.MAINTAINED)
    public static float[] multiplyMat4(float[] dest, float[] left, float[] right) {
        float m00 = (left[0] * right[0]) + (left[4] * right[1]) + (left[8] * right[2]) + (left[12] * right[3]);
        float m01 = (left[1] * right[0]) + (left[5] * right[1]) + (left[9] * right[2]) + (left[13] * right[3]);
        float m02 = (left[2] * right[0]) + (left[6] * right[1]) + (left[10] * right[2]) + (left[14] * right[3]);
        float m03 = (left[3] * right[0]) + (left[7] * right[1]) + (left[11] * right[2]) + (left[15] * right[3]);
        float m10 = (left[0] * right[4]) + (left[4] * right[5]) + (left[8] * right[6]) + (left[12] * right[7]);
        float m11 = (left[1] * right[4]) + (left[5] * right[5]) + (left[9] * right[6]) + (left[13] * right[7]);
        float m12 = (left[2] * right[4]) + (left[6] * right[5]) + (left[10] * right[6]) + (left[14] * right[7]);
        float m13 = (left[3] * right[4]) + (left[7] * right[5]) + (left[11] * right[6]) + (left[15] * right[7]);
        float m20 = (left[0] * right[8]) + (left[4] * right[9]) + (left[8] * right[10]) + (left[12] * right[11]);
        float m21 = (left[1] * right[8]) + (left[5] * right[9]) + (left[9] * right[10]) + (left[13] * right[11]);
        float m22 = (left[2] * right[8]) + (left[6] * right[9]) + (left[10] * right[10]) + (left[14] * right[11]);
        float m23 = (left[3] * right[8]) + (left[7] * right[9]) + (left[11] * right[10]) + (left[15] * right[11]);
        float m30 = (left[0] * right[12]) + (left[4] * right[13]) + (left[8] * right[14]) + (left[12] * right[15]);
        float m31 = (left[1] * right[12]) + (left[5] * right[13]) + (left[9] * right[14]) + (left[13] * right[15]);
        float m32 = (left[2] * right[12]) + (left[6] * right[13]) + (left[10] * right[14]) + (left[14] * right[15]);
        float m33 = (left[3] * right[12]) + (left[7] * right[13]) + (left[11] * right[14]) + (left[15] * right[15]);
        dest[0] = m00;
        dest[1] = m01;
        dest[2] = m02;
        dest[3] = m03;
        dest[4] = m10;
        dest[5] = m11;
        dest[6] = m12;
        dest[7] = m13;
        dest[8] = m20;
        dest[9] = m21;
        dest[10] = m22;
        dest[11] = m23;
        dest[12] = m30;
        dest[13] = m31;
        dest[14] = m32;
        dest[15] = m33;
        return dest;
    }

    @API(status = API.Status.MAINTAINED)
    public static void multiplyMat4FB(FloatBuffer fbInvOut, FloatBuffer fbMatLeft, FloatBuffer fbMatRight) {
        float[] matLeft = inArray;
        float[] matRight = outArray;
        fbMatLeft.get(matLeft);
        fbMatRight.get(matRight);
        multiplyMat4(matLeft, matLeft, matRight);
        fbInvOut.put(matLeft);
    }
}
