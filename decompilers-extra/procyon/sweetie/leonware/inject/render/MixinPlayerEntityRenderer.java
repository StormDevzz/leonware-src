// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.inject.render;

import net.minecraft.class_10042;
import net.minecraft.class_10017;
import net.minecraft.class_9014;
import java.util.Iterator;
import net.minecraft.class_266;
import net.minecraft.class_269;
import net.minecraft.class_9015;
import net.minecraft.class_1657;
import net.minecraft.class_8646;
import sweetie.leonware.client.features.modules.other.HealthResolverModule;
import sweetie.leonware.api.system.configs.FriendManager;
import net.minecraft.class_310;
import sweetie.leonware.client.features.modules.render.CustomModelModule;
import net.minecraft.class_2960;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sweetie.leonware.client.features.modules.render.BabyModelModule;
import net.minecraft.class_4587;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import sweetie.leonware.client.features.modules.render.custommodel.Leon2DFeatureRenderer;
import sweetie.leonware.client.features.modules.render.custommodel.AmogusFeatureRenderer;
import sweetie.leonware.client.features.modules.render.custommodel.FreddyBearFeatureRenderer;
import sweetie.leonware.client.features.modules.render.custommodel.CrazyRabbitFeatureRenderer;
import sweetie.leonware.client.features.modules.render.custommodel.CustomModelLayers;
import sweetie.leonware.client.features.modules.render.leon.LeonFeatureRenderer;
import net.minecraft.class_3887;
import net.minecraft.class_3883;
import sweetie.leonware.client.features.modules.render.santa.SantaHatFeatureRenderer;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.class_583;
import net.minecraft.class_5617;
import sweetie.leonware.client.features.modules.render.custommodel.Leon2DModel;
import sweetie.leonware.client.features.modules.render.custommodel.FreddyBearModel;
import sweetie.leonware.client.features.modules.render.custommodel.AmogusModel;
import org.spongepowered.asm.mixin.Unique;
import sweetie.leonware.client.features.modules.render.custommodel.CrazyRabbitModel;
import net.minecraft.class_1007;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.class_591;
import net.minecraft.class_10055;
import net.minecraft.class_742;
import net.minecraft.class_922;

@Mixin({ class_1007.class })
public abstract class MixinPlayerEntityRenderer extends class_922<class_742, class_10055, class_591>
{
    @Unique
    private CrazyRabbitModel leonware$rabbitModel;
    @Unique
    private AmogusModel leonware$amogusModel;
    @Unique
    private FreddyBearModel leonware$freddyModel;
    @Unique
    private Leon2DModel leonware$leon2dModel;
    
    public MixinPlayerEntityRenderer(final class_5617.class_5618 ctx, final class_591 model, final float shadowRadius) {
        super(ctx, (class_583)model, shadowRadius);
    }
    
    @Inject(method = { "<init>" }, at = { @At("RETURN") })
    private void onInit(final class_5617.class_5618 ctx, final boolean slim, final CallbackInfo ci) {
        this.method_4046((class_3887)new SantaHatFeatureRenderer((class_3883<class_10055, class_591>)this));
        this.method_4046((class_3887)new LeonFeatureRenderer((class_3883<class_10055, class_591>)this));
        this.leonware$rabbitModel = new CrazyRabbitModel(ctx.method_32167(CustomModelLayers.CRAZY_RABBIT));
        this.method_4046((class_3887)new CrazyRabbitFeatureRenderer((class_3883<class_10055, class_591>)this, this.leonware$rabbitModel));
        this.leonware$freddyModel = new FreddyBearModel(ctx.method_32167(CustomModelLayers.FREDDY_BEAR));
        this.method_4046((class_3887)new FreddyBearFeatureRenderer((class_3883<class_10055, class_591>)this, this.leonware$freddyModel));
        this.leonware$amogusModel = new AmogusModel(ctx.method_32167(CustomModelLayers.AMOGUS));
        this.method_4046((class_3887)new AmogusFeatureRenderer((class_3883<class_10055, class_591>)this, this.leonware$amogusModel));
        this.leonware$leon2dModel = new Leon2DModel(ctx.method_32167(CustomModelLayers.LEON_2D));
        this.method_4046((class_3887)new Leon2DFeatureRenderer((class_3883<class_10055, class_591>)this, this.leonware$leon2dModel));
    }
    
    @Inject(method = { "setupTransforms" }, at = { @At("TAIL") })
    private void onSetupTransformsBaby(final class_10055 state, final class_4587 matrices, final float animationProgress, final float bodyYaw, final CallbackInfo ci) {
        final BabyModelModule mod = BabyModelModule.getInstance();
        if (!mod.isEnabled() || !BabyModelModule.currentShouldScale) {
            return;
        }
        final class_591 model = (class_591)this.method_4038();
        final float hScale = mod.headScale.getValue();
        model.field_3398.field_37938 = hScale;
        model.field_3398.field_37939 = hScale;
        model.field_3398.field_37940 = hScale;
        model.field_3394.field_37938 = hScale;
        model.field_3394.field_37939 = hScale;
        model.field_3394.field_37940 = hScale;
    }
    
    @Inject(method = { "getTexture" }, at = { @At("HEAD") }, cancellable = true)
    private void onGetTexture(final class_10055 state, final CallbackInfoReturnable<class_2960> cir) {
        final CustomModelModule mod = CustomModelModule.getInstance();
        if (!mod.isEnabled()) {
            return;
        }
        final class_310 mc = class_310.method_1551();
        if (mc.field_1724 == null || state.field_53529 == null) {
            return;
        }
        final boolean isSelf = state.field_53529.equals(mc.method_1548().method_1676());
        final boolean isFriend = mod.friends.getValue() && FriendManager.getInstance().contains(state.field_53529);
        if (!isSelf && !isFriend) {
            return;
        }
        if (mod.model.is("CrazyRabbit")) {
            cir.setReturnValue((Object)class_2960.method_60655("leonware", "textures/models/leon/rabbit.png"));
        }
        else if (mod.model.is("Freddy Bear")) {
            cir.setReturnValue((Object)class_2960.method_60655("leonware", "textures/models/leon/freddy.png"));
        }
        else if (mod.model.is("Amogus")) {
            cir.setReturnValue((Object)class_2960.method_60655("leonware", "textures/models/leon/amogus.png"));
        }
        else if (mod.model.is("Leon 2D")) {
            cir.setReturnValue((Object)class_2960.method_60655("leonware", "textures/models/leon/leon2d.png"));
        }
    }
    
    @Inject(method = { "updateRenderState(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;F)V" }, at = { @At("HEAD") })
    private void rwHealthFix(final class_742 abstractClientPlayerEntity, final class_10055 playerEntityRenderState, final float f, final CallbackInfo ci) {
        final class_310 client = class_310.method_1551();
        if (client == null || client.field_1724 == null || client.field_1687 == null) {
            return;
        }
        if (!HealthResolverModule.getInstance().isRW()) {
            return;
        }
        final class_269 scoreboard = abstractClientPlayerEntity.method_7327();
        final class_266 scoreboardObjective = scoreboard.method_1189(class_8646.field_45158);
        if (scoreboardObjective != null) {
            for (class_1657 player : client.field_1687.method_18456()) {
                if (player != null) {
                    if (player.equals((Object)client.field_1724)) {
                        continue;
                    }
                    try {
                        final class_9014 score = scoreboard.method_1180((class_9015)player, scoreboardObjective);
                        final String scoreText = ((score == null) ? 20 : score.method_55409()) + " " + scoreboardObjective.method_1114().getString();
                        final String scoreNumber = scoreText.replaceAll("[^0-9]", "");
                        final int hp = Integer.parseInt(scoreNumber);
                        if (hp < 0 || hp > player.method_6063()) {
                            continue;
                        }
                        player.method_6033((float)hp);
                    }
                    catch (final Exception ex) {}
                }
            }
        }
    }
}
