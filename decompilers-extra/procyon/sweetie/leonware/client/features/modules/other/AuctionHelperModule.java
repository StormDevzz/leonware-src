// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.other;

import sweetie.leonware.api.utils.auction.ParseModeChoice;
import lombok.Generated;
import java.awt.Color;
import sweetie.leonware.api.utils.render.RenderUtil;
import net.minecraft.class_332;
import net.minecraft.class_1799;
import net.minecraft.class_1703;
import java.util.Collection;
import net.minecraft.class_1707;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import net.minecraft.class_1735;
import java.util.List;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.utils.auction.PriceParser;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Auction Helper", category = Category.OTHER)
public class AuctionHelperModule extends Module
{
    private static final AuctionHelperModule instance;
    private final PriceParser priceParser;
    private final ModeSetting mode;
    private final SliderSetting slots;
    private final List<class_1735> minPriceSlots;
    
    public AuctionHelperModule() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokespecial   sweetie/leonware/api/module/Module.<init>:()V
        //     4: aload_0         /* this */
        //     5: new             Lsweetie/leonware/api/utils/auction/PriceParser;
        //     8: dup            
        //     9: invokespecial   sweetie/leonware/api/utils/auction/PriceParser.<init>:()V
        //    12: putfield        sweetie/leonware/client/features/modules/other/AuctionHelperModule.priceParser:Lsweetie/leonware/api/utils/auction/PriceParser;
        //    15: aload_0         /* this */
        //    16: new             Lsweetie/leonware/api/module/setting/ModeSetting;
        //    19: dup            
        //    20: ldc             "Mode"
        //    22: invokespecial   sweetie/leonware/api/module/setting/ModeSetting.<init>:(Ljava/lang/String;)V
        //    25: aload_0         /* this */
        //    26: getfield        sweetie/leonware/client/features/modules/other/AuctionHelperModule.priceParser:Lsweetie/leonware/api/utils/auction/PriceParser;
        //    29: getfield        sweetie/leonware/api/utils/auction/PriceParser.currentMode:Lsweetie/leonware/api/utils/auction/ParseModeChoice;
        //    32: invokevirtual   sweetie/leonware/api/module/setting/ModeSetting.value:(Ljava/lang/Enum;)Lsweetie/leonware/api/module/setting/ModeSetting;
        //    35: invokestatic    sweetie/leonware/api/utils/auction/ParseModeChoice.values:()[Lsweetie/leonware/api/utils/auction/ParseModeChoice;
        //    38: invokevirtual   sweetie/leonware/api/module/setting/ModeSetting.values:([Ljava/lang/Enum;)Lsweetie/leonware/api/module/setting/ModeSetting;
        //    41: aload_0         /* this */
        //    42: invokedynamic   BootstrapMethod #0, run:(Lsweetie/leonware/client/features/modules/other/AuctionHelperModule;)Ljava/lang/Runnable;
        //    47: invokevirtual   sweetie/leonware/api/module/setting/ModeSetting.onAction:(Ljava/lang/Runnable;)Lsweetie/leonware/api/module/setting/ModeSetting;
        //    50: putfield        sweetie/leonware/client/features/modules/other/AuctionHelperModule.mode:Lsweetie/leonware/api/module/setting/ModeSetting;
        //    53: aload_0         /* this */
        //    54: new             Lsweetie/leonware/api/module/setting/SliderSetting;
        //    57: dup            
        //    58: ldc             "Slots"
        //    60: invokespecial   sweetie/leonware/api/module/setting/SliderSetting.<init>:(Ljava/lang/String;)V
        //    63: ldc             3.0
        //    65: invokestatic    java/lang/Float.valueOf:(F)Ljava/lang/Float;
        //    68: invokevirtual   sweetie/leonware/api/module/setting/SliderSetting.value:(Ljava/lang/Float;)Lsweetie/leonware/api/module/setting/SliderSetting;
        //    71: fconst_1       
        //    72: ldc             6.0
        //    74: invokevirtual   sweetie/leonware/api/module/setting/SliderSetting.range:(FF)Lsweetie/leonware/api/module/setting/SliderSetting;
        //    77: fconst_1       
        //    78: invokevirtual   sweetie/leonware/api/module/setting/SliderSetting.step:(F)Lsweetie/leonware/api/module/setting/SliderSetting;
        //    81: putfield        sweetie/leonware/client/features/modules/other/AuctionHelperModule.slots:Lsweetie/leonware/api/module/setting/SliderSetting;
        //    84: aload_0         /* this */
        //    85: new             Ljava/util/ArrayList;
        //    88: dup            
        //    89: invokespecial   java/util/ArrayList.<init>:()V
        //    92: putfield        sweetie/leonware/client/features/modules/other/AuctionHelperModule.minPriceSlots:Ljava/util/List;
        //    95: aload_0         /* this */
        //    96: iconst_2       
        //    97: anewarray       Lsweetie/leonware/api/module/setting/Setting;
        //   100: dup            
        //   101: iconst_0       
        //   102: aload_0         /* this */
        //   103: getfield        sweetie/leonware/client/features/modules/other/AuctionHelperModule.mode:Lsweetie/leonware/api/module/setting/ModeSetting;
        //   106: aastore        
        //   107: dup            
        //   108: iconst_1       
        //   109: aload_0         /* this */
        //   110: getfield        sweetie/leonware/client/features/modules/other/AuctionHelperModule.slots:Lsweetie/leonware/api/module/setting/SliderSetting;
        //   113: aastore        
        //   114: invokevirtual   sweetie/leonware/client/features/modules/other/AuctionHelperModule.addSettings:([Lsweetie/leonware/api/module/setting/Setting;)V
        //   117: return         
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException: Cannot invoke "com.strobel.assembler.metadata.TypeReference.getSimpleType()" because the return value of "com.strobel.decompiler.ast.Variable.getType()" is null
        //     at com.strobel.decompiler.languages.java.ast.NameVariables.generateNameForVariable(NameVariables.java:252)
        //     at com.strobel.decompiler.languages.java.ast.NameVariables.assignNamesToVariables(NameVariables.java:185)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.nameVariables(AstMethodBodyBuilder.java:1482)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.populateVariables(AstMethodBodyBuilder.java:1411)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:210)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:93)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:868)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createConstructor(AstBuilder.java:799)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:635)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:605)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:195)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:162)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:137)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:333)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:254)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:144)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @Override
    public void onEvent() {
        final EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> this.handleUpdateEvent()));
        this.addEvents(updateEvent);
    }
    
    public void handleUpdateEvent() {
        final class_1703 field_7512 = AuctionHelperModule.mc.field_1724.field_7512;
        if (!(field_7512 instanceof class_1707)) {
            return;
        }
        final class_1707 chest = (class_1707)field_7512;
        final String title = AuctionHelperModule.mc.field_1755.method_25440().getString();
        final boolean isAuction = title.contains("0B1L2o3v") || title.contains("\u0410\u0443\u043a\u0446\u0438\u043e\u043d") || title.contains("\u041f\u043e\u0438\u0441\u043a") || title.contains("\u041c\u0430\u0440\u043a\u0435\u0442") || title.contains("\ua201\ua000\ua202\ua332\ua202\ua001") || title.contains("[\u2603] \u0410\u0443\u043a\u0446\u0438\u043e\u043d\u044b");
        if (!isAuction) {
            return;
        }
        this.minPriceSlots.clear();
        this.minPriceSlots.addAll(this.getMinPriceSlots(chest));
    }
    
    private List<class_1735> getMinPriceSlots(final class_1707 chest) {
        return chest.field_7761.stream().filter(s -> s.field_7874 <= 44 && !s.method_7677().method_7960() && this.getPrice(s.method_7677()) != -1).sorted((s1, s2) -> Integer.compare(this.getPrice(s1.method_7677()), this.getPrice(s2.method_7677()))).limit(this.slots.getValue().intValue()).toList();
    }
    
    private int getPrice(final class_1799 stack) {
        return this.priceParser.getPrice(stack);
    }
    
    public void onRenderChest(final class_332 context, final class_1735 slot) {
        if (this.minPriceSlots.contains(slot)) {
            final int alpha = (int)(1.0 + 110.0 * Math.abs(Math.sin(System.currentTimeMillis() * 0.005)));
            RenderUtil.RECT.draw(context.method_51448(), (float)slot.field_7873, (float)slot.field_7872, 16.0f, 16.0f, 0.0f, new Color(0, 255, 0, alpha));
        }
    }
    
    @Generated
    public static AuctionHelperModule getInstance() {
        return AuctionHelperModule.instance;
    }
    
    @Generated
    public ModeSetting getMode() {
        return this.mode;
    }
    
    static {
        instance = new AuctionHelperModule();
    }
}
