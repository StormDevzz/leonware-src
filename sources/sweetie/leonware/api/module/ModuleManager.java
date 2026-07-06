package sweetie.leonware.api.module;

import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import sweetie.leonware.client.features.modules.combat.AimbotModule;
import sweetie.leonware.client.features.modules.combat.AntiBotModule;
import sweetie.leonware.client.features.modules.combat.AuraModule;
import sweetie.leonware.client.features.modules.combat.AutoBuffModule;
import sweetie.leonware.client.features.modules.combat.AutoCartModule;
import sweetie.leonware.client.features.modules.combat.AutoExplosionModule;
import sweetie.leonware.client.features.modules.combat.AutoGAppleModule;
import sweetie.leonware.client.features.modules.combat.AutoOilModule;
import sweetie.leonware.client.features.modules.combat.AutoTotemModule;
import sweetie.leonware.client.features.modules.combat.AutoWebModule;
import sweetie.leonware.client.features.modules.combat.BacktrackModule;
import sweetie.leonware.client.features.modules.combat.CriticalsModule;
import sweetie.leonware.client.features.modules.combat.DebuffModule;
import sweetie.leonware.client.features.modules.combat.FastBowModule;
import sweetie.leonware.client.features.modules.combat.FastRPDModule;
import sweetie.leonware.client.features.modules.combat.HarchaAuraModule;
import sweetie.leonware.client.features.modules.combat.HitBoxModule;
import sweetie.leonware.client.features.modules.combat.ItemSwapModule;
import sweetie.leonware.client.features.modules.combat.MaceDamageModule;
import sweetie.leonware.client.features.modules.combat.NoEntityTraceModule;
import sweetie.leonware.client.features.modules.combat.NoFriendHurtModule;
import sweetie.leonware.client.features.modules.combat.ShieldDesyncModule;
import sweetie.leonware.client.features.modules.combat.SuperBowModule;
import sweetie.leonware.client.features.modules.combat.SurroundModule;
import sweetie.leonware.client.features.modules.combat.VelocityModule;
import sweetie.leonware.client.features.modules.combat.crystalaura.CrystalAuraModule;
import sweetie.leonware.client.features.modules.combat.elytratarget.ElytraTargetModule;
import sweetie.leonware.client.features.modules.movement.AirStuckModule;
import sweetie.leonware.client.features.modules.movement.AntiSweetBerryModule;
import sweetie.leonware.client.features.modules.movement.BoatAbuseModule;
import sweetie.leonware.client.features.modules.movement.BoatFlyModule;
import sweetie.leonware.client.features.modules.movement.ElytraFlyModule;
import sweetie.leonware.client.features.modules.movement.ElytraMotionModule;
import sweetie.leonware.client.features.modules.movement.ElytraRecastModule;
import sweetie.leonware.client.features.modules.movement.FastClimbModule;
import sweetie.leonware.client.features.modules.movement.FastFallModule;
import sweetie.leonware.client.features.modules.movement.HighJumpModule;
import sweetie.leonware.client.features.modules.movement.IceSpeedModule;
import sweetie.leonware.client.features.modules.movement.InventoryMoveModule;
import sweetie.leonware.client.features.modules.movement.JesusModule;
import sweetie.leonware.client.features.modules.movement.MoveFixModule;
import sweetie.leonware.client.features.modules.movement.NoClipModule;
import sweetie.leonware.client.features.modules.movement.NoFallDamageModule;
import sweetie.leonware.client.features.modules.movement.NoJumpDelayModule;
import sweetie.leonware.client.features.modules.movement.NoMagmaDamageModule;
import sweetie.leonware.client.features.modules.movement.NoWebModule;
import sweetie.leonware.client.features.modules.movement.PacketFlyModule;
import sweetie.leonware.client.features.modules.movement.ShulkerHighJumpModule;
import sweetie.leonware.client.features.modules.movement.SlimeFlightModule;
import sweetie.leonware.client.features.modules.movement.SneakModule;
import sweetie.leonware.client.features.modules.movement.SprintModule;
import sweetie.leonware.client.features.modules.movement.StepModule;
import sweetie.leonware.client.features.modules.movement.StrafeModule;
import sweetie.leonware.client.features.modules.movement.TridentFlyModule;
import sweetie.leonware.client.features.modules.movement.WaterSpeedModule;
import sweetie.leonware.client.features.modules.movement.fly.FlightModule;
import sweetie.leonware.client.features.modules.movement.nitrofirework.NitroFireworkModule;
import sweetie.leonware.client.features.modules.movement.noslow.NoSlowModule;
import sweetie.leonware.client.features.modules.movement.speed.SpeedModule;
import sweetie.leonware.client.features.modules.movement.spider.SpiderModule;
import sweetie.leonware.client.features.modules.other.AuctionHelperModule;
import sweetie.leonware.client.features.modules.other.AutoAuthModule;
import sweetie.leonware.client.features.modules.other.AutoGGModule;
import sweetie.leonware.client.features.modules.other.AutoRespawnModule;
import sweetie.leonware.client.features.modules.other.DeathSoundsModule;
import sweetie.leonware.client.features.modules.other.DisablerModule;
import sweetie.leonware.client.features.modules.other.FastBreakModule;
import sweetie.leonware.client.features.modules.other.HealthResolverModule;
import sweetie.leonware.client.features.modules.other.HitSoundsModule;
import sweetie.leonware.client.features.modules.other.JoinerModule;
import sweetie.leonware.client.features.modules.other.KTLeaveModule;
import sweetie.leonware.client.features.modules.other.MouseTweaksModule;
import sweetie.leonware.client.features.modules.other.NoServerPackModule;
import sweetie.leonware.client.features.modules.other.RecorderModule;
import sweetie.leonware.client.features.modules.other.StreamerModule;
import sweetie.leonware.client.features.modules.other.TPAcceptModule;
import sweetie.leonware.client.features.modules.other.TapeMouseModule;
import sweetie.leonware.client.features.modules.other.ToggleSoundsModule;
import sweetie.leonware.client.features.modules.other.UnHook;
import sweetie.leonware.client.features.modules.other.WarpUtilsModule;
import sweetie.leonware.client.features.modules.player.AntiAFKModule;
import sweetie.leonware.client.features.modules.player.AresMineModule;
import sweetie.leonware.client.features.modules.player.AssistantModule;
import sweetie.leonware.client.features.modules.player.AutoArmorModule;
import sweetie.leonware.client.features.modules.player.AutoBambooModule;
import sweetie.leonware.client.features.modules.player.AutoBeansModule;
import sweetie.leonware.client.features.modules.player.AutoBrewPotionsModule;
import sweetie.leonware.client.features.modules.player.AutoEatModule;
import sweetie.leonware.client.features.modules.player.AutoFarmModule;
import sweetie.leonware.client.features.modules.player.AutoFishModule;
import sweetie.leonware.client.features.modules.player.AutoLeaveModule;
import sweetie.leonware.client.features.modules.player.AutoLootModule;
import sweetie.leonware.client.features.modules.player.AutoToolModule;
import sweetie.leonware.client.features.modules.player.BlockFlyModule;
import sweetie.leonware.client.features.modules.player.ChestStealerModule;
import sweetie.leonware.client.features.modules.player.ClickPearlModule;
import sweetie.leonware.client.features.modules.player.CustomCoolDownModule;
import sweetie.leonware.client.features.modules.player.DelayHelperModule;
import sweetie.leonware.client.features.modules.player.EdgeJumpModule;
import sweetie.leonware.client.features.modules.player.EdgeStopModule;
import sweetie.leonware.client.features.modules.player.ElytraSwapModule;
import sweetie.leonware.client.features.modules.player.FakeLagModule;
import sweetie.leonware.client.features.modules.player.GhostHandModule;
import sweetie.leonware.client.features.modules.player.HitboxDesyncModule;
import sweetie.leonware.client.features.modules.player.KeyHunterModule;
import sweetie.leonware.client.features.modules.player.LevitationControlModule;
import sweetie.leonware.client.features.modules.player.MiddleAPTModule;
import sweetie.leonware.client.features.modules.player.MultiTaskModule;
import sweetie.leonware.client.features.modules.player.NoDesyncModule;
import sweetie.leonware.client.features.modules.player.NoInteractModule;
import sweetie.leonware.client.features.modules.player.NoPushEntityModule;
import sweetie.leonware.client.features.modules.player.ScaffoldModule;
import sweetie.leonware.client.features.modules.player.SpammerModule;
import sweetie.leonware.client.features.modules.player.TimerModule;
import sweetie.leonware.client.features.modules.player.WindJumpModule;
import sweetie.leonware.client.features.modules.render.AmbienceModule;
import sweetie.leonware.client.features.modules.render.AspectRatioModule;
import sweetie.leonware.client.features.modules.render.BabyModelModule;
import sweetie.leonware.client.features.modules.render.BadtripModule;
import sweetie.leonware.client.features.modules.render.BlockESPModule;
import sweetie.leonware.client.features.modules.render.BlockHighLightModule;
import sweetie.leonware.client.features.modules.render.CameraClipModule;
import sweetie.leonware.client.features.modules.render.ChamsModule;
import sweetie.leonware.client.features.modules.render.ChinaHatModule;
import sweetie.leonware.client.features.modules.render.ClickGUIModule;
import sweetie.leonware.client.features.modules.render.CustomBobModule;
import sweetie.leonware.client.features.modules.render.CustomCrosshairModule;
import sweetie.leonware.client.features.modules.render.CustomElytraModule;
import sweetie.leonware.client.features.modules.render.CustomModelModule;
import sweetie.leonware.client.features.modules.render.CustomWorldModule;
import sweetie.leonware.client.features.modules.render.DamageMarkersModule;
import sweetie.leonware.client.features.modules.render.EnchantColorModule;
import sweetie.leonware.client.features.modules.render.ExtraTabModule;
import sweetie.leonware.client.features.modules.render.FreeCamModule;
import sweetie.leonware.client.features.modules.render.InterfaceModule;
import sweetie.leonware.client.features.modules.render.InventoryAnimationModule;
import sweetie.leonware.client.features.modules.render.ItemESPModule;
import sweetie.leonware.client.features.modules.render.ItemPhysicsModule;
import sweetie.leonware.client.features.modules.render.JumpCircleModule;
import sweetie.leonware.client.features.modules.render.LeonModule;
import sweetie.leonware.client.features.modules.render.NightVisionModule;
import sweetie.leonware.client.features.modules.render.PointersModule;
import sweetie.leonware.client.features.modules.render.PredictionsModule;
import sweetie.leonware.client.features.modules.render.RemovalsModule;
import sweetie.leonware.client.features.modules.render.SantaHatModule;
import sweetie.leonware.client.features.modules.render.SeeInvisiblesModule;
import sweetie.leonware.client.features.modules.render.ShaderFogModule;
import sweetie.leonware.client.features.modules.render.SkeletonESPModule;
import sweetie.leonware.client.features.modules.render.StorageESPModule;
import sweetie.leonware.client.features.modules.render.SwingAnimationModule;
import sweetie.leonware.client.features.modules.render.TracersModule;
import sweetie.leonware.client.features.modules.render.TrailsModule;
import sweetie.leonware.client.features.modules.render.ViewModelModule;
import sweetie.leonware.client.features.modules.render.WingsModule;
import sweetie.leonware.client.features.modules.render.motionblur.MotionBlurModule;
import sweetie.leonware.client.features.modules.render.nametags.NameTagsModule;
import sweetie.leonware.client.features.modules.render.particles.ParticlesModule;
import sweetie.leonware.client.features.modules.render.shaders.ShadersModule;
import sweetie.leonware.client.features.modules.render.targetesp.TargetEspModule;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/module/ModuleManager.class */
public class ModuleManager {
    private static final ModuleManager instance = new ModuleManager();
    private final List<Module> modules = new ArrayList();

    @Generated
    public static ModuleManager getInstance() {
        return instance;
    }

    @Generated
    public List<Module> getModules() {
        return this.modules;
    }

    public void load() {
        register(SprintModule.getInstance(), ClickGUIModule.getInstance(), AmbienceModule.getInstance(), StrafeModule.getInstance(), NoJumpDelayModule.getInstance(), AuraModule.getInstance(), MoveFixModule.getInstance(), AutoRespawnModule.getInstance(), InterfaceModule.getInstance(), AutoLeaveModule.getInstance(), NameTagsModule.getInstance(), AutoToolModule.getInstance(), TPAcceptModule.getInstance(), VelocityModule.getInstance(), NoSlowModule.getInstance(), AutoTotemModule.getInstance(), AutoGAppleModule.getInstance(), InventoryMoveModule.getInstance(), ItemSwapModule.getInstance(), RemovalsModule.getInstance(), ClickPearlModule.getInstance(), ElytraSwapModule.getInstance(), ElytraMotionModule.getInstance(), NitroFireworkModule.getInstance(), SwingAnimationModule.getInstance(), ViewModelModule.getInstance(), UnHook.getInstance(), AspectRatioModule.getInstance(), TridentFlyModule.getInstance(), SurroundModule.getInstance(), WaterSpeedModule.getInstance(), BlockHighLightModule.getInstance(), SkeletonESPModule.getInstance(), KTLeaveModule.getInstance(), ScaffoldModule.getInstance(), BoatAbuseModule.getInstance(), AntiSweetBerryModule.getInstance(), NoFallDamageModule.getInstance(), AutoFarmModule.getInstance(), AutoBambooModule.getInstance(), AutoEatModule.getInstance(), AutoArmorModule.getInstance(), FreeCamModule.getInstance(), CriticalsModule.getInstance(), BacktrackModule.getInstance(), AntiBotModule.getInstance(), HitBoxModule.getInstance(), WarpUtilsModule.getInstance(), ShulkerHighJumpModule.getInstance(), EnchantColorModule.getInstance(), CustomBobModule.getInstance(), ExtraTabModule.getInstance(), NoInteractModule.getInstance(), NoPushEntityModule.getInstance(), DebuffModule.getInstance(), BabyModelModule.getInstance(), BadtripModule.getInstance(), SlimeFlightModule.getInstance(), HighJumpModule.getInstance(), AirStuckModule.getInstance(), ElytraFlyModule.getInstance(), StorageESPModule.getInstance(), ItemESPModule.getInstance(), ItemPhysicsModule.getInstance(), GhostHandModule.getInstance(), AutoFishModule.getInstance(), MultiTaskModule.getInstance(), DelayHelperModule.getInstance(), BlockESPModule.getInstance(), SneakModule.getInstance(), FastFallModule.getInstance(), FastClimbModule.getInstance(), BoatFlyModule.getInstance(), FastBowModule.getInstance(), NoMagmaDamageModule.getInstance(), EdgeStopModule.getInstance(), EdgeJumpModule.getInstance(), SuperBowModule.getInstance(), AutoAuthModule.getInstance(), WindJumpModule.getInstance(), SantaHatModule.getInstance(), ChinaHatModule.getInstance(), LevitationControlModule.getInstance(), CustomCoolDownModule.getInstance(), ElytraRecastModule.getInstance(), MaceDamageModule.getInstance(), SpammerModule.getInstance(), LeonModule.getInstance(), CustomModelModule.getInstance(), IceSpeedModule.getInstance(), CustomElytraModule.getInstance(), JesusModule.getInstance(), WingsModule.getInstance(), AutoBrewPotionsModule.getInstance(), StepModule.getInstance(), HitSoundsModule.getInstance(), DeathSoundsModule.getInstance(), AutoGGModule.getInstance(), BlockFlyModule.getInstance(), AresMineModule.getInstance(), AntiAFKModule.getInstance(), TracersModule.getInstance(), CustomWorldModule.getInstance(), DamageMarkersModule.getInstance(), AimbotModule.getInstance(), HarchaAuraModule.getInstance(), CrystalAuraModule.getInstance(), AutoWebModule.getInstance(), DisablerModule.getInstance(), FastRPDModule.getInstance(), AutoLootModule.getInstance(), MiddleAPTModule.getInstance(), PacketFlyModule.getInstance(), ShieldDesyncModule.getInstance(), AutoCartModule.getInstance(), ChamsModule.getInstance(), ChestStealerModule.getInstance(), AutoOilModule.getInstance(), SpeedModule.getInstance(), AutoBuffModule.getInstance(), JoinerModule.getInstance(), CameraClipModule.getInstance(), PointersModule.getInstance(), MouseTweaksModule.getInstance(), NoDesyncModule.getInstance(), TimerModule.getInstance(), AssistantModule.getInstance(), ElytraTargetModule.getInstance(), AutoExplosionModule.getInstance(), NoWebModule.getInstance(), NoClipModule.getInstance(), PredictionsModule.getInstance(), NoFriendHurtModule.getInstance(), NoEntityTraceModule.getInstance(), ParticlesModule.getInstance(), RecorderModule.getInstance(), HitboxDesyncModule.getInstance(), FastBreakModule.getInstance(), TapeMouseModule.getInstance(), SeeInvisiblesModule.getInstance(), HealthResolverModule.getInstance(), StreamerModule.getInstance(), JumpCircleModule.getInstance(), ToggleSoundsModule.getInstance(), MotionBlurModule.getInstance(), ShadersModule.getInstance(), ShaderFogModule.getInstance(), CustomCrosshairModule.getInstance(), FakeLagModule.getInstance(), NightVisionModule.getInstance(), NoServerPackModule.getInstance(), AuctionHelperModule.getInstance(), TrailsModule.getInstance(), TargetEspModule.getInstance(), SpiderModule.getInstance(), FlightModule.getInstance(), InventoryAnimationModule.getInstance());
        try {
            Class.forName("baritone.api.BaritoneAPI");
            register(KeyHunterModule.getInstance());
            System.out.println("KeyHunter registered - Bf");
        } catch (ClassNotFoundException e) {
            System.out.println("KeyHunter skipped - Bnf");
        }
        try {
            Class.forName("baritone.api.BaritoneAPI");
            register(AutoBeansModule.getInstance());
            System.out.println("AutoBeans registered - Bf");
        } catch (ClassNotFoundException e2) {
            System.out.println("AutoBeans skipped - Bnf");
        }
        this.modules.sort((a, b) -> {
            return a.getName().compareToIgnoreCase(b.getName());
        });
    }

    public void register(Module... modules) {
        this.modules.addAll(List.of((Object[]) modules));
    }
}
