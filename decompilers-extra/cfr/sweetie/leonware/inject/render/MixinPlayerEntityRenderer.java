/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_10055
 *  net.minecraft.class_1007
 *  net.minecraft.class_1657
 *  net.minecraft.class_266
 *  net.minecraft.class_269
 *  net.minecraft.class_2960
 *  net.minecraft.class_310
 *  net.minecraft.class_3883
 *  net.minecraft.class_4587
 *  net.minecraft.class_5617$class_5618
 *  net.minecraft.class_583
 *  net.minecraft.class_591
 *  net.minecraft.class_742
 *  net.minecraft.class_8646
 *  net.minecraft.class_9014
 *  net.minecraft.class_9015
 *  net.minecraft.class_922
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package sweetie.leonware.inject.render;

import net.minecraft.class_10055;
import net.minecraft.class_1007;
import net.minecraft.class_1657;
import net.minecraft.class_266;
import net.minecraft.class_269;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_3883;
import net.minecraft.class_4587;
import net.minecraft.class_5617;
import net.minecraft.class_583;
import net.minecraft.class_591;
import net.minecraft.class_742;
import net.minecraft.class_8646;
import net.minecraft.class_9014;
import net.minecraft.class_9015;
import net.minecraft.class_922;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sweetie.leonware.api.system.configs.FriendManager;
import sweetie.leonware.client.features.modules.other.HealthResolverModule;
import sweetie.leonware.client.features.modules.render.BabyModelModule;
import sweetie.leonware.client.features.modules.render.CustomModelModule;
import sweetie.leonware.client.features.modules.render.custommodel.AmogusFeatureRenderer;
import sweetie.leonware.client.features.modules.render.custommodel.AmogusModel;
import sweetie.leonware.client.features.modules.render.custommodel.CrazyRabbitFeatureRenderer;
import sweetie.leonware.client.features.modules.render.custommodel.CrazyRabbitModel;
import sweetie.leonware.client.features.modules.render.custommodel.CustomModelLayers;
import sweetie.leonware.client.features.modules.render.custommodel.FreddyBearFeatureRenderer;
import sweetie.leonware.client.features.modules.render.custommodel.FreddyBearModel;
import sweetie.leonware.client.features.modules.render.custommodel.Leon2DFeatureRenderer;
import sweetie.leonware.client.features.modules.render.custommodel.Leon2DModel;
import sweetie.leonware.client.features.modules.render.leon.LeonFeatureRenderer;
import sweetie.leonware.client.features.modules.render.santa.SantaHatFeatureRenderer;

@Mixin(value={class_1007.class})
public abstract class MixinPlayerEntityRenderer
extends class_922<class_742, class_10055, class_591> {
    @Unique
    private CrazyRabbitModel leonware$rabbitModel;
    @Unique
    private AmogusModel leonware$amogusModel;
    @Unique
    private FreddyBearModel leonware$freddyModel;
    @Unique
    private Leon2DModel leonware$leon2dModel;

    public MixinPlayerEntityRenderer(class_5617.class_5618 ctx, class_591 model, float shadowRadius) {
        super(ctx, (class_583)model, shadowRadius);
    }

    @Inject(method={"<init>"}, at={@At(value="RETURN")})
    private void onInit(class_5617.class_5618 ctx, boolean slim, CallbackInfo ci) {
        this.method_4046(new SantaHatFeatureRenderer((class_3883<class_10055, class_591>)((class_1007)this)));
        this.method_4046(new LeonFeatureRenderer((class_3883<class_10055, class_591>)((class_1007)this)));
        this.leonware$rabbitModel = new CrazyRabbitModel(ctx.method_32167(CustomModelLayers.CRAZY_RABBIT));
        this.method_4046(new CrazyRabbitFeatureRenderer((class_3883<class_10055, class_591>)((class_1007)this), this.leonware$rabbitModel));
        this.leonware$freddyModel = new FreddyBearModel(ctx.method_32167(CustomModelLayers.FREDDY_BEAR));
        this.method_4046(new FreddyBearFeatureRenderer((class_3883<class_10055, class_591>)((class_1007)this), this.leonware$freddyModel));
        this.leonware$amogusModel = new AmogusModel(ctx.method_32167(CustomModelLayers.AMOGUS));
        this.method_4046(new AmogusFeatureRenderer((class_3883<class_10055, class_591>)((class_1007)this), this.leonware$amogusModel));
        this.leonware$leon2dModel = new Leon2DModel(ctx.method_32167(CustomModelLayers.LEON_2D));
        this.method_4046(new Leon2DFeatureRenderer((class_3883<class_10055, class_591>)((class_1007)this), this.leonware$leon2dModel));
    }

    @Inject(method={"setupTransforms"}, at={@At(value="TAIL")})
    private void onSetupTransformsBaby(class_10055 state, class_4587 matrices, float animationProgress, float bodyYaw, CallbackInfo ci) {
        float hScale;
        BabyModelModule mod = BabyModelModule.getInstance();
        if (!mod.isEnabled() || !BabyModelModule.currentShouldScale) {
            return;
        }
        class_591 model = (class_591)this.method_4038();
        model.field_3398.field_37938 = hScale = ((Float)mod.headScale.getValue()).floatValue();
        model.field_3398.field_37939 = hScale;
        model.field_3398.field_37940 = hScale;
        model.field_3394.field_37938 = hScale;
        model.field_3394.field_37939 = hScale;
        model.field_3394.field_37940 = hScale;
    }

    @Inject(method={"getTexture"}, at={@At(value="HEAD")}, cancellable=true)
    private void onGetTexture(class_10055 state, CallbackInfoReturnable<class_2960> cir) {
        boolean isFriend;
        CustomModelModule mod = CustomModelModule.getInstance();
        if (!mod.isEnabled()) {
            return;
        }
        class_310 mc = class_310.method_1551();
        if (mc.field_1724 == null || state.field_53529 == null) {
            return;
        }
        boolean isSelf = state.field_53529.equals(mc.method_1548().method_1676());
        boolean bl = isFriend = (Boolean)mod.friends.getValue() != false && FriendManager.getInstance().contains(state.field_53529);
        if (!isSelf && !isFriend) {
            return;
        }
        if (mod.model.is("CrazyRabbit")) {
            cir.setReturnValue((Object)class_2960.method_60655((String)"leonware", (String)"textures/models/leon/rabbit.png"));
        } else if (mod.model.is("Freddy Bear")) {
            cir.setReturnValue((Object)class_2960.method_60655((String)"leonware", (String)"textures/models/leon/freddy.png"));
        } else if (mod.model.is("Amogus")) {
            cir.setReturnValue((Object)class_2960.method_60655((String)"leonware", (String)"textures/models/leon/amogus.png"));
        } else if (mod.model.is("Leon 2D")) {
            cir.setReturnValue((Object)class_2960.method_60655((String)"leonware", (String)"textures/models/leon/leon2d.png"));
        }
    }

    @Inject(method={"updateRenderState(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;F)V"}, at={@At(value="HEAD")})
    private void rwHealthFix(class_742 abstractClientPlayerEntity, class_10055 playerEntityRenderState, float f, CallbackInfo ci) {
        class_310 client = class_310.method_1551();
        if (client == null || client.field_1724 == null || client.field_1687 == null) {
            return;
        }
        if (!HealthResolverModule.getInstance().isRW()) {
            return;
        }
        class_269 scoreboard = abstractClientPlayerEntity.method_7327();
        class_266 scoreboardObjective = scoreboard.method_1189(class_8646.field_45158);
        if (scoreboardObjective != null) {
            for (class_1657 player : client.field_1687.method_18456()) {
                if (player == null || player.equals((Object)client.field_1724)) continue;
                try {
                    class_9014 score = scoreboard.method_1180((class_9015)player, scoreboardObjective);
                    String scoreText = (score == null ? 20 : score.method_55409()) + " " + scoreboardObjective.method_1114().getString();
                    String scoreNumber = scoreText.replaceAll("[^0-9]", "");
                    int hp = Integer.parseInt(scoreNumber);
                    if (hp < 0 || !((float)hp <= player.method_6063())) continue;
                    player.method_6033((float)hp);
                }
                catch (Exception exception) {}
            }
        }
    }
}

