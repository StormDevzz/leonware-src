package sweetie.leonware.client.features.modules.render.particles;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.ArrayDeque;
import java.util.Deque;
import lombok.Generated;
import net.minecraft.class_10142;
import net.minecraft.class_2189;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_243;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_2960;
import net.minecraft.class_4184;
import net.minecraft.class_4587;
import net.minecraft.class_7833;
import org.joml.Matrix4f;
import sweetie.leonware.api.system.files.FileUtil;
import sweetie.leonware.api.system.interfaces.QuickImports;
import sweetie.leonware.api.utils.animation.AnimationUtil;
import sweetie.leonware.api.utils.animation.Easing;
import sweetie.leonware.api.utils.color.ColorUtil;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.math.MathUtil;
import sweetie.leonware.api.utils.math.TimerUtil;
import sweetie.leonware.api.utils.player.PlayerUtil;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/particles/ParticleRender.class */
public class ParticleRender implements QuickImports {
    private float prevX;
    private float prevY;
    private float prevZ;
    private float x;
    private float y;
    private float z;
    private float motionX;
    private float motionY;
    private float motionZ;
    private int lifeTime;
    private int maxLife;
    private float size;
    private int index;
    private class_2960 identifier;
    private boolean dropPhysics;
    private boolean rotating;
    private float spawnDuration;
    private float dyingDuration;
    private boolean trail;
    private boolean dyingEffect;
    public static String[] textures = {"Spark", "Star", "Heart", "Dollar", "Snowflake", "Glow", "Ball"};
    private float prevSize = 0.0f;
    private float prevRotation = 0.0f;
    private float rotateSpeed = 20.0f;
    private final TimerUtil timerUtil = new TimerUtil();
    private final AnimationUtil alphaAnimation = new AnimationUtil();
    private boolean gravityFalls = false;
    private float trailLength = 5.0f;
    private final Deque<class_243> trailPoints = new ArrayDeque();
    private float rotation = MathUtil.randomInRange(-180.0f, 180.0f);

    @Generated
    public ParticleRender prevX(float prevX) {
        this.prevX = prevX;
        return this;
    }

    @Generated
    public ParticleRender prevY(float prevY) {
        this.prevY = prevY;
        return this;
    }

    @Generated
    public ParticleRender prevZ(float prevZ) {
        this.prevZ = prevZ;
        return this;
    }

    @Generated
    public ParticleRender x(float x) {
        this.x = x;
        return this;
    }

    @Generated
    public ParticleRender y(float y) {
        this.y = y;
        return this;
    }

    @Generated
    public ParticleRender z(float z) {
        this.z = z;
        return this;
    }

    @Generated
    public ParticleRender motionX(float motionX) {
        this.motionX = motionX;
        return this;
    }

    @Generated
    public ParticleRender motionY(float motionY) {
        this.motionY = motionY;
        return this;
    }

    @Generated
    public ParticleRender motionZ(float motionZ) {
        this.motionZ = motionZ;
        return this;
    }

    @Generated
    public ParticleRender lifeTime(int lifeTime) {
        this.lifeTime = lifeTime;
        return this;
    }

    @Generated
    public ParticleRender maxLife(int maxLife) {
        this.maxLife = maxLife;
        return this;
    }

    @Generated
    public ParticleRender prevSize(float prevSize) {
        this.prevSize = prevSize;
        return this;
    }

    @Generated
    public ParticleRender rotation(float rotation) {
        this.rotation = rotation;
        return this;
    }

    @Generated
    public ParticleRender prevRotation(float prevRotation) {
        this.prevRotation = prevRotation;
        return this;
    }

    @Generated
    public ParticleRender rotateSpeed(float rotateSpeed) {
        this.rotateSpeed = rotateSpeed;
        return this;
    }

    @Generated
    public ParticleRender size(float size) {
        this.size = size;
        return this;
    }

    @Generated
    public ParticleRender index(int index) {
        this.index = index;
        return this;
    }

    @Generated
    public ParticleRender identifier(class_2960 identifier) {
        this.identifier = identifier;
        return this;
    }

    @Generated
    public ParticleRender dropPhysics(boolean dropPhysics) {
        this.dropPhysics = dropPhysics;
        return this;
    }

    @Generated
    public ParticleRender rotating(boolean rotating) {
        this.rotating = rotating;
        return this;
    }

    @Generated
    public ParticleRender spawnDuration(float spawnDuration) {
        this.spawnDuration = spawnDuration;
        return this;
    }

    @Generated
    public ParticleRender dyingDuration(float dyingDuration) {
        this.dyingDuration = dyingDuration;
        return this;
    }

    @Generated
    public ParticleRender gravityFalls(boolean gravityFalls) {
        this.gravityFalls = gravityFalls;
        return this;
    }

    @Generated
    public ParticleRender trail(boolean trail) {
        this.trail = trail;
        return this;
    }

    @Generated
    public ParticleRender trailLength(float trailLength) {
        this.trailLength = trailLength;
        return this;
    }

    @Generated
    public ParticleRender dyingEffect(boolean dyingEffect) {
        this.dyingEffect = dyingEffect;
        return this;
    }

    @Generated
    public float prevX() {
        return this.prevX;
    }

    @Generated
    public float prevY() {
        return this.prevY;
    }

    @Generated
    public float prevZ() {
        return this.prevZ;
    }

    @Generated
    public float x() {
        return this.x;
    }

    @Generated
    public float y() {
        return this.y;
    }

    @Generated
    public float z() {
        return this.z;
    }

    @Generated
    public float motionX() {
        return this.motionX;
    }

    @Generated
    public float motionY() {
        return this.motionY;
    }

    @Generated
    public float motionZ() {
        return this.motionZ;
    }

    @Generated
    public int lifeTime() {
        return this.lifeTime;
    }

    @Generated
    public int maxLife() {
        return this.maxLife;
    }

    @Generated
    public float prevSize() {
        return this.prevSize;
    }

    @Generated
    public float rotation() {
        return this.rotation;
    }

    @Generated
    public float prevRotation() {
        return this.prevRotation;
    }

    @Generated
    public float rotateSpeed() {
        return this.rotateSpeed;
    }

    @Generated
    public float size() {
        return this.size;
    }

    @Generated
    public int index() {
        return this.index;
    }

    @Generated
    public class_2960 identifier() {
        return this.identifier;
    }

    @Generated
    public boolean dropPhysics() {
        return this.dropPhysics;
    }

    @Generated
    public boolean rotating() {
        return this.rotating;
    }

    @Generated
    public float spawnDuration() {
        return this.spawnDuration;
    }

    @Generated
    public float dyingDuration() {
        return this.dyingDuration;
    }

    @Generated
    public TimerUtil timerUtil() {
        return this.timerUtil;
    }

    @Generated
    public AnimationUtil alphaAnimation() {
        return this.alphaAnimation;
    }

    @Generated
    public boolean gravityFalls() {
        return this.gravityFalls;
    }

    @Generated
    public boolean trail() {
        return this.trail;
    }

    @Generated
    public float trailLength() {
        return this.trailLength;
    }

    @Generated
    public boolean dyingEffect() {
        return this.dyingEffect;
    }

    @Generated
    public Deque<class_243> trailPoints() {
        return this.trailPoints;
    }

    public ParticleRender(float x, float y, float z, int lifeTime) {
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
        this.x = x;
        this.y = y;
        this.z = z;
        this.maxLife = MathUtil.randomInRange(Math.max(lifeTime / 2, 0), lifeTime);
    }

    public static class_2960 getTexture(String mode) {
        switch (mode) {
            case "Spark":
                return FileUtil.getImage("particles/spark_" + MathUtil.randomInRange(1, 4));
            default:
                return FileUtil.getImage("particles/" + mode.toLowerCase());
        }
    }

    public boolean update() {
        float gravity = this.gravityFalls ? ((float) this.alphaAnimation.getValue()) * 0.3f : 1.0f;
        this.prevX = this.x;
        this.prevY = this.y;
        this.prevZ = this.z;
        this.x += this.motionX;
        this.y += this.motionY * gravity;
        this.z += this.motionZ;
        double speed = Math.sqrt((this.motionX * this.motionX) + (this.motionZ * this.motionZ));
        float halfSize = this.prevSize;
        if (posBlock(this.x, (this.y - halfSize) - 0.05f, this.z)) {
            this.motionY = (-this.motionY) / 1.1f;
            this.motionX /= 1.1f;
            this.motionZ /= 1.1f;
        } else if (posBlock((this.x - ((float) speed)) - halfSize, this.y, (this.z - ((float) speed)) - halfSize) || posBlock(this.x + ((float) speed) + halfSize, this.y, this.z + ((float) speed) + halfSize) || posBlock(this.x + ((float) speed) + halfSize, this.y, (this.z - ((float) speed)) - halfSize) || posBlock((this.x - ((float) speed)) - halfSize, this.y, this.z + ((float) speed) + halfSize) || posBlock(this.x + ((float) speed) + halfSize, this.y, this.z) || posBlock((this.x - ((float) speed)) - halfSize, this.y, this.z) || posBlock(this.x, this.y, this.z + ((float) speed) + halfSize) || posBlock(this.x, this.y, (this.z - ((float) speed)) - halfSize)) {
            this.motionX = -this.motionX;
            this.motionZ = -this.motionZ;
            this.maxLife--;
        } else if (this.dropPhysics) {
            this.motionY -= 0.02f;
        }
        this.prevRotation = this.rotation;
        this.rotation -= this.prevRotation > 0.0f ? -this.rotateSpeed : this.rotateSpeed;
        if (!this.gravityFalls) {
            this.motionX /= 1.1f;
            this.motionY /= 1.1f;
            this.motionZ /= 1.1f;
        }
        if (this.trail) {
            this.trailPoints.addFirst(new class_243(this.x, this.y, this.z));
            while (this.trailPoints.size() > this.trailLength) {
                this.trailPoints.removeLast();
            }
        }
        return mc.field_1724.method_19538().method_1022(new class_243((double) this.x, (double) this.y, (double) this.z)) >= 80.0d || (this.alphaAnimation.getValue() <= 0.0d && this.timerUtil.finished(((this.spawnDuration + this.dyingDuration) + ((float) this.maxLife)) * 50.0f));
    }

    private float alphaPC() {
        return (float) this.alphaAnimation.getValue();
    }

    private int alpha() {
        return (int) (255.0f * alphaPC());
    }

    public void updateAlpha() {
        this.alphaAnimation.update();
        float alphaAnim = alphaPC();
        if (alphaAnim <= 0.0d && !this.timerUtil.finished(this.spawnDuration * 50.0f)) {
            this.alphaAnimation.run(1.0d, (long) (this.spawnDuration * 50.0f), Easing.QUINT_OUT);
        }
        if (alphaAnim >= 1.0d && this.timerUtil.finished((this.spawnDuration + this.maxLife) * 50.0f)) {
            this.alphaAnimation.run(0.0d, (long) (this.dyingDuration * 50.0f), Easing.QUINT_OUT);
        }
    }

    public void render(class_4587 matrixStack) {
        if (PlayerUtil.canSee(new class_243(this.x, this.y, this.z))) {
            Matrix4f matrix4f = matrixStack.method_23760().method_23761();
            class_4184 camera = mc.field_1773.method_19418();
            Color primaryColor = ColorUtil.setAlpha(UIColors.gradient(this.index * 90), alpha());
            class_243 interpolatedPos = interpolatePosition(this.prevX, this.prevY, this.prevZ, this.x, this.y, this.z);
            float halfSize = MathUtil.interpolate(this.prevSize, this.size * alphaPC());
            this.prevSize = halfSize;
            RenderSystem.setShaderTexture(0, this.identifier);
            matrixStack.method_22904(interpolatedPos.field_1352, interpolatedPos.field_1351, interpolatedPos.field_1350);
            matrixStack.method_22907(class_7833.field_40716.rotationDegrees(-camera.method_19330()));
            matrixStack.method_22907(class_7833.field_40714.rotationDegrees(camera.method_19329()));
            if (this.rotating) {
                matrixStack.method_22907(class_7833.field_40718.rotationDegrees(MathUtil.interpolate(this.prevRotation, this.rotation)));
            }
            class_287 bufferBuilder = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1575);
            bufferBuilder.method_22918(matrix4f, halfSize, -halfSize, 0.0f).method_22913(0.0f, 1.0f).method_39415(primaryColor.getRGB());
            bufferBuilder.method_22918(matrix4f, -halfSize, -halfSize, 0.0f).method_22913(1.0f, 1.0f).method_39415(primaryColor.getRGB());
            bufferBuilder.method_22918(matrix4f, -halfSize, halfSize, 0.0f).method_22913(1.0f, 0.0f).method_39415(primaryColor.getRGB());
            bufferBuilder.method_22918(matrix4f, halfSize, halfSize, 0.0f).method_22913(0.0f, 0.0f).method_39415(primaryColor.getRGB());
            class_286.method_43433(bufferBuilder.method_60800());
        }
    }

    public void renderTrail(class_4587 matrixStack) {
        if (this.trail && this.trailPoints.size() > 1 && PlayerUtil.canSee(new class_243(this.x, this.y, this.z))) {
            RenderSystem.setShader(class_10142.field_53876);
            RenderSystem.lineWidth(1.5f);
            class_287 buf = class_289.method_1348().method_60827(class_293.class_5596.field_29344, class_290.field_1576);
            Matrix4f mat = matrixStack.method_23760().method_23761();
            class_243 cam = mc.method_1561().field_4686.method_19326();
            Color col = ColorUtil.setAlpha(UIColors.gradient(this.index * 90), alpha());
            double interpX = MathUtil.interpolate(this.prevX, this.x);
            double interpY = MathUtil.interpolate(this.prevY, this.y);
            double interpZ = MathUtil.interpolate(this.prevZ, this.z);
            class_243 last = null;
            for (class_243 p : this.trailPoints) {
                double smoothX = MathUtil.interpolate(p.field_1352, interpX, 0.05d);
                double smoothY = MathUtil.interpolate(p.field_1351, interpY, 0.05d);
                double smoothZ = MathUtil.interpolate(p.field_1350, interpZ, 0.05d);
                class_243 smooth = new class_243(smoothX, smoothY, smoothZ);
                if (last != null) {
                    buf.method_22918(mat, (float) (last.field_1352 - cam.field_1352), (float) (last.field_1351 - cam.field_1351), (float) (last.field_1350 - cam.field_1350)).method_1336(col.getRed(), col.getGreen(), col.getBlue(), col.getAlpha());
                    buf.method_22918(mat, (float) (smooth.field_1352 - cam.field_1352), (float) (smooth.field_1351 - cam.field_1351), (float) (smooth.field_1350 - cam.field_1350)).method_1336(col.getRed(), col.getGreen(), col.getBlue(), col.getAlpha());
                }
                last = smooth;
            }
            class_286.method_43433(buf.method_60800());
        }
    }

    private boolean posBlock(float x, float y, float z) {
        class_2248 block = mc.field_1687 != null ? mc.field_1687.method_8320(class_2338.method_49637(x, y, z)).method_26204() : null;
        return (block == null || (block instanceof class_2189) || block == class_2246.field_10382 || block == class_2246.field_10164 || block == class_2246.field_10376 || block == class_2246.field_10238 || block == class_2246.field_10479 || block == class_2246.field_10214 || block == class_2246.field_10112 || block == class_2246.field_10428 || block == class_2246.field_10597 || block == class_2246.field_10477 || block == class_2246.field_10449 || block == class_2246.field_10182 || block == class_2246.field_10251 || block == class_2246.field_10559) ? false : true;
    }

    private class_243 interpolatePosition(float prevX, float prevY, float prevZ, float currentX, float currentY, float currentZ) {
        class_243 cameraPos = mc.method_1561().field_4686.method_19326();
        double cameraX = cameraPos.field_1352;
        double cameraY = cameraPos.field_1351;
        double cameraZ = cameraPos.field_1350;
        double interpolatedX = ((double) MathUtil.interpolate(prevX, currentX)) - cameraX;
        double interpolatedY = ((double) MathUtil.interpolate(prevY, currentY)) - cameraY;
        double interpolatedZ = ((double) MathUtil.interpolate(prevZ, currentZ)) - cameraZ;
        return new class_243(interpolatedX, interpolatedY, interpolatedZ);
    }
}
