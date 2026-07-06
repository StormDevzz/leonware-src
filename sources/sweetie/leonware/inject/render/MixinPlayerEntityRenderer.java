package sweetie.leonware.inject.render;

import net.minecraft.class_10017;
import net.minecraft.class_10042;
import net.minecraft.class_10055;
import net.minecraft.class_1007;
import net.minecraft.class_1657;
import net.minecraft.class_266;
import net.minecraft.class_269;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_4587;
import net.minecraft.class_5617;
import net.minecraft.class_591;
import net.minecraft.class_742;
import net.minecraft.class_8646;
import net.minecraft.class_9014;
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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/inject/render/MixinPlayerEntityRenderer.class */
@Mixin({class_1007.class})
public abstract class MixinPlayerEntityRenderer extends class_922<class_742, class_10055, class_591> {

    @Unique
    private CrazyRabbitModel leonware$rabbitModel;

    @Unique
    private AmogusModel leonware$amogusModel;

    @Unique
    private FreddyBearModel leonware$freddyModel;

    @Unique
    private Leon2DModel leonware$leon2dModel;

    protected /* bridge */ /* synthetic */ float method_55831(class_10017 class_10017Var) {
        return super.method_55832((class_10042) class_10017Var);
    }

    public MixinPlayerEntityRenderer(class_5617.class_5618 ctx, class_591 model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    @Inject(method = {"<init>"}, at = {@At("RETURN")})
    private void onInit(class_5617.class_5618 ctx, boolean slim, CallbackInfo ci) {
        method_4046(new SantaHatFeatureRenderer((class_1007) this));
        method_4046(new LeonFeatureRenderer((class_1007) this));
        this.leonware$rabbitModel = new CrazyRabbitModel(ctx.method_32167(CustomModelLayers.CRAZY_RABBIT));
        method_4046(new CrazyRabbitFeatureRenderer((class_1007) this, this.leonware$rabbitModel));
        this.leonware$freddyModel = new FreddyBearModel(ctx.method_32167(CustomModelLayers.FREDDY_BEAR));
        method_4046(new FreddyBearFeatureRenderer((class_1007) this, this.leonware$freddyModel));
        this.leonware$amogusModel = new AmogusModel(ctx.method_32167(CustomModelLayers.AMOGUS));
        method_4046(new AmogusFeatureRenderer((class_1007) this, this.leonware$amogusModel));
        this.leonware$leon2dModel = new Leon2DModel(ctx.method_32167(CustomModelLayers.LEON_2D));
        method_4046(new Leon2DFeatureRenderer((class_1007) this, this.leonware$leon2dModel));
    }

    @Inject(method = {"setupTransforms"}, at = {@At("TAIL")})
    private void onSetupTransformsBaby(class_10055 state, class_4587 matrices, float animationProgress, float bodyYaw, CallbackInfo ci) {
        BabyModelModule mod = BabyModelModule.getInstance();
        if (mod.isEnabled() && BabyModelModule.currentShouldScale) {
            class_591 model = method_4038();
            float hScale = mod.headScale.getValue().floatValue();
            model.field_3398.field_37938 = hScale;
            model.field_3398.field_37939 = hScale;
            model.field_3398.field_37940 = hScale;
            model.field_3394.field_37938 = hScale;
            model.field_3394.field_37939 = hScale;
            model.field_3394.field_37940 = hScale;
        }
    }

    @Inject(method = {"getTexture"}, at = {@At("HEAD")}, cancellable = true)
    private void onGetTexture(class_10055 state, CallbackInfoReturnable<class_2960> cir) {
        CustomModelModule mod = CustomModelModule.getInstance();
        if (mod.isEnabled()) {
            class_310 mc = class_310.method_1551();
            if (mc.field_1724 == null || state.field_53529 == null) {
                return;
            }
            boolean isSelf = state.field_53529.equals(mc.method_1548().method_1676());
            boolean isFriend = mod.friends.getValue().booleanValue() && FriendManager.getInstance().contains(state.field_53529);
            if (isSelf || isFriend) {
                if (mod.model.is("CrazyRabbit")) {
                    cir.setReturnValue(class_2960.method_60655("leonware", "textures/models/leon/rabbit.png"));
                    return;
                }
                if (mod.model.is("Freddy Bear")) {
                    cir.setReturnValue(class_2960.method_60655("leonware", "textures/models/leon/freddy.png"));
                } else if (mod.model.is("Amogus")) {
                    cir.setReturnValue(class_2960.method_60655("leonware", "textures/models/leon/amogus.png"));
                } else if (mod.model.is("Leon 2D")) {
                    cir.setReturnValue(class_2960.method_60655("leonware", "textures/models/leon/leon2d.png"));
                }
            }
        }
    }

    @Inject(method = {"updateRenderState(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;F)V"}, at = {@At("HEAD")})
    private void rwHealthFix(class_742 abstractClientPlayerEntity, class_10055 playerEntityRenderState, float f, CallbackInfo ci) {
        class_269 scoreboard;
        class_266 scoreboardObjective;
        class_310 client = class_310.method_1551();
        if (client != null && client.field_1724 != null && client.field_1687 != null && HealthResolverModule.getInstance().isRW() && (scoreboardObjective = (scoreboard = abstractClientPlayerEntity.method_7327()).method_1189(class_8646.field_45158)) != null) {
            for (class_1657 player : client.field_1687.method_18456()) {
                if (player != null && !player.equals(client.field_1724)) {
                    try {
                        class_9014 score = scoreboard.method_1180(player, scoreboardObjective);
                        String scoreText = (score == null ? 20 : score.method_55409()) + " " + scoreboardObjective.method_1114().getString();
                        String scoreNumber = scoreText.replaceAll("[^0-9]", "");
                        int hp = Integer.parseInt(scoreNumber);
                        if (hp >= 0 && hp <= player.method_6063()) {
                            player.method_6033(hp);
                        }
                    } catch (Exception e) {
                    }
                }
            }
        }
    }
}
