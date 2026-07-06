// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.player;

import sweetie.leonware.api.module.setting.Setting;
import lombok.Generated;
import java.util.ArrayList;
import org.joml.Vector2f;
import net.minecraft.class_4587;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.api.utils.render.fonts.Fonts;
import sweetie.leonware.api.utils.math.MathUtil;
import sweetie.leonware.api.utils.math.ProjectionUtil;
import net.minecraft.class_2680;
import java.util.concurrent.TimeUnit;
import net.minecraft.class_1922;
import java.util.Comparator;
import net.minecraft.class_2246;
import net.minecraft.class_2382;
import net.minecraft.class_2338;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_2848;
import net.minecraft.class_1304;
import net.minecraft.class_268;
import java.util.Iterator;
import net.minecraft.class_640;
import net.minecraft.class_1799;
import net.minecraft.class_1703;
import net.minecraft.class_1657;
import net.minecraft.class_1713;
import net.minecraft.class_1802;
import net.minecraft.class_1707;
import java.util.regex.Matcher;
import net.minecraft.class_2596;
import org.joml.Vector2i;
import sweetie.leonware.api.system.client.GpsManager;
import java.util.regex.Pattern;
import net.minecraft.class_7439;
import net.minecraft.class_2767;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.event.events.render.Render2DEvent;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.TickEvent;
import net.minecraft.class_243;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import sweetie.leonware.api.module.setting.BindSetting;
import sweetie.leonware.api.system.backend.Pair;
import sweetie.leonware.api.utils.player.InventoryUtil;
import java.util.Map;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.MultiBooleanSetting;
import java.util.function.Supplier;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Assistant", category = Category.PLAYER)
public class AssistantModule extends Module
{
    private static final AssistantModule instance;
    private final Supplier<Boolean> isHotkeysEnabled;
    private final Supplier<Boolean> isHWKeys;
    private final Supplier<Boolean> isFTKeys;
    private final Supplier<Boolean> isLGKeys;
    private final Supplier<Boolean> isRWKeys;
    private Mode currentMode;
    private String targetRival;
    private final MultiBooleanSetting functions;
    public final BooleanSetting autoPvpLony;
    private final MultiBooleanSetting lonyPrefixes;
    private final ModeSetting mode;
    private final BooleanSetting legit;
    public final BooleanSetting autoPlayerLony;
    public final BooleanSetting antipolet;
    boolean need;
    public boolean fireworkUse;
    private final Map<InventoryUtil.ItemUsage, Pair<BindSetting, Mode>> keyBindings;
    private final ScheduledExecutorService scheduler;
    private final List<Pair<Long, class_243>> consumables;
    private final Map<class_243, String> consumableNames;
    
    public AssistantModule() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokespecial   sweetie/leonware/api/module/Module.<init>:()V
        //     4: aload_0         /* this */
        //     5: aload_0         /* this */
        //     6: invokedynamic   BootstrapMethod #0, get:(Lsweetie/leonware/client/features/modules/player/AssistantModule;)Ljava/util/function/Supplier;
        //    11: putfield        sweetie/leonware/client/features/modules/player/AssistantModule.isHotkeysEnabled:Ljava/util/function/Supplier;
        //    14: aload_0         /* this */
        //    15: aload_0         /* this */
        //    16: invokedynamic   BootstrapMethod #1, get:(Lsweetie/leonware/client/features/modules/player/AssistantModule;)Ljava/util/function/Supplier;
        //    21: putfield        sweetie/leonware/client/features/modules/player/AssistantModule.isHWKeys:Ljava/util/function/Supplier;
        //    24: aload_0         /* this */
        //    25: aload_0         /* this */
        //    26: invokedynamic   BootstrapMethod #2, get:(Lsweetie/leonware/client/features/modules/player/AssistantModule;)Ljava/util/function/Supplier;
        //    31: putfield        sweetie/leonware/client/features/modules/player/AssistantModule.isFTKeys:Ljava/util/function/Supplier;
        //    34: aload_0         /* this */
        //    35: aload_0         /* this */
        //    36: invokedynamic   BootstrapMethod #3, get:(Lsweetie/leonware/client/features/modules/player/AssistantModule;)Ljava/util/function/Supplier;
        //    41: putfield        sweetie/leonware/client/features/modules/player/AssistantModule.isLGKeys:Ljava/util/function/Supplier;
        //    44: aload_0         /* this */
        //    45: aload_0         /* this */
        //    46: invokedynamic   BootstrapMethod #4, get:(Lsweetie/leonware/client/features/modules/player/AssistantModule;)Ljava/util/function/Supplier;
        //    51: putfield        sweetie/leonware/client/features/modules/player/AssistantModule.isRWKeys:Ljava/util/function/Supplier;
        //    54: aload_0         /* this */
        //    55: getstatic       sweetie/leonware/client/features/modules/player/AssistantModule$Mode.FUNTIME:Lsweetie/leonware/client/features/modules/player/AssistantModule$Mode;
        //    58: putfield        sweetie/leonware/client/features/modules/player/AssistantModule.currentMode:Lsweetie/leonware/client/features/modules/player/AssistantModule$Mode;
        //    61: aload_0         /* this */
        //    62: aconst_null    
        //    63: putfield        sweetie/leonware/client/features/modules/player/AssistantModule.targetRival:Ljava/lang/String;
        //    66: aload_0         /* this */
        //    67: new             Lsweetie/leonware/api/module/setting/MultiBooleanSetting;
        //    70: dup            
        //    71: ldc             "Functions"
        //    73: invokespecial   sweetie/leonware/api/module/setting/MultiBooleanSetting.<init>:(Ljava/lang/String;)V
        //    76: iconst_2       
        //    77: anewarray       Lsweetie/leonware/api/module/setting/BooleanSetting;
        //    80: dup            
        //    81: iconst_0       
        //    82: new             Lsweetie/leonware/api/module/setting/BooleanSetting;
        //    85: dup            
        //    86: ldc             "Hotkeys"
        //    88: invokespecial   sweetie/leonware/api/module/setting/BooleanSetting.<init>:(Ljava/lang/String;)V
        //    91: iconst_1       
        //    92: invokestatic    java/lang/Boolean.valueOf:(Z)Ljava/lang/Boolean;
        //    95: invokevirtual   sweetie/leonware/api/module/setting/BooleanSetting.value:(Ljava/lang/Boolean;)Lsweetie/leonware/api/module/setting/BooleanSetting;
        //    98: aastore        
        //    99: dup            
        //   100: iconst_1       
        //   101: new             Lsweetie/leonware/api/module/setting/BooleanSetting;
        //   104: dup            
        //   105: ldc             "Timers"
        //   107: invokespecial   sweetie/leonware/api/module/setting/BooleanSetting.<init>:(Ljava/lang/String;)V
        //   110: iconst_0       
        //   111: invokestatic    java/lang/Boolean.valueOf:(Z)Ljava/lang/Boolean;
        //   114: invokevirtual   sweetie/leonware/api/module/setting/BooleanSetting.value:(Ljava/lang/Boolean;)Lsweetie/leonware/api/module/setting/BooleanSetting;
        //   117: aastore        
        //   118: invokevirtual   sweetie/leonware/api/module/setting/MultiBooleanSetting.value:([Lsweetie/leonware/api/module/setting/BooleanSetting;)Lsweetie/leonware/api/module/setting/MultiBooleanSetting;
        //   121: putfield        sweetie/leonware/client/features/modules/player/AssistantModule.functions:Lsweetie/leonware/api/module/setting/MultiBooleanSetting;
        //   124: aload_0         /* this */
        //   125: new             Lsweetie/leonware/api/module/setting/BooleanSetting;
        //   128: dup            
        //   129: ldc             "Auto-PvP"
        //   131: invokespecial   sweetie/leonware/api/module/setting/BooleanSetting.<init>:(Ljava/lang/String;)V
        //   134: iconst_1       
        //   135: invokestatic    java/lang/Boolean.valueOf:(Z)Ljava/lang/Boolean;
        //   138: invokevirtual   sweetie/leonware/api/module/setting/BooleanSetting.value:(Ljava/lang/Boolean;)Lsweetie/leonware/api/module/setting/BooleanSetting;
        //   141: aload_0         /* this */
        //   142: getfield        sweetie/leonware/client/features/modules/player/AssistantModule.isLGKeys:Ljava/util/function/Supplier;
        //   145: invokevirtual   sweetie/leonware/api/module/setting/BooleanSetting.setVisible:(Ljava/util/function/Supplier;)Lsweetie/leonware/api/module/setting/BooleanSetting;
        //   148: putfield        sweetie/leonware/client/features/modules/player/AssistantModule.autoPvpLony:Lsweetie/leonware/api/module/setting/BooleanSetting;
        //   151: aload_0         /* this */
        //   152: new             Lsweetie/leonware/api/module/setting/MultiBooleanSetting;
        //   155: dup            
        //   156: ldc             "Prefixes"
        //   158: invokespecial   sweetie/leonware/api/module/setting/MultiBooleanSetting.<init>:(Ljava/lang/String;)V
        //   161: bipush          8
        //   163: anewarray       Lsweetie/leonware/api/module/setting/BooleanSetting;
        //   166: dup            
        //   167: iconst_0       
        //   168: new             Lsweetie/leonware/api/module/setting/BooleanSetting;
        //   171: dup            
        //   172: ldc             "Player"
        //   174: invokespecial   sweetie/leonware/api/module/setting/BooleanSetting.<init>:(Ljava/lang/String;)V
        //   177: iconst_1       
        //   178: invokestatic    java/lang/Boolean.valueOf:(Z)Ljava/lang/Boolean;
        //   181: invokevirtual   sweetie/leonware/api/module/setting/BooleanSetting.value:(Ljava/lang/Boolean;)Lsweetie/leonware/api/module/setting/BooleanSetting;
        //   184: aastore        
        //   185: dup            
        //   186: iconst_1       
        //   187: new             Lsweetie/leonware/api/module/setting/BooleanSetting;
        //   190: dup            
        //   191: ldc             "Legenda"
        //   193: invokespecial   sweetie/leonware/api/module/setting/BooleanSetting.<init>:(Ljava/lang/String;)V
        //   196: iconst_1       
        //   197: invokestatic    java/lang/Boolean.valueOf:(Z)Ljava/lang/Boolean;
        //   200: invokevirtual   sweetie/leonware/api/module/setting/BooleanSetting.value:(Ljava/lang/Boolean;)Lsweetie/leonware/api/module/setting/BooleanSetting;
        //   203: aastore        
        //   204: dup            
        //   205: iconst_2       
        //   206: new             Lsweetie/leonware/api/module/setting/BooleanSetting;
        //   209: dup            
        //   210: ldc             "Pravitel"
        //   212: invokespecial   sweetie/leonware/api/module/setting/BooleanSetting.<init>:(Ljava/lang/String;)V
        //   215: iconst_1       
        //   216: invokestatic    java/lang/Boolean.valueOf:(Z)Ljava/lang/Boolean;
        //   219: invokevirtual   sweetie/leonware/api/module/setting/BooleanSetting.value:(Ljava/lang/Boolean;)Lsweetie/leonware/api/module/setting/BooleanSetting;
        //   222: aastore        
        //   223: dup            
        //   224: iconst_3       
        //   225: new             Lsweetie/leonware/api/module/setting/BooleanSetting;
        //   228: dup            
        //   229: ldc             "Povelitel"
        //   231: invokespecial   sweetie/leonware/api/module/setting/BooleanSetting.<init>:(Ljava/lang/String;)V
        //   234: iconst_1       
        //   235: invokestatic    java/lang/Boolean.valueOf:(Z)Ljava/lang/Boolean;
        //   238: invokevirtual   sweetie/leonware/api/module/setting/BooleanSetting.value:(Ljava/lang/Boolean;)Lsweetie/leonware/api/module/setting/BooleanSetting;
        //   241: aastore        
        //   242: dup            
        //   243: iconst_4       
        //   244: new             Lsweetie/leonware/api/module/setting/BooleanSetting;
        //   247: dup            
        //   248: ldc             "D.Admin"
        //   250: invokespecial   sweetie/leonware/api/module/setting/BooleanSetting.<init>:(Ljava/lang/String;)V
        //   253: iconst_1       
        //   254: invokestatic    java/lang/Boolean.valueOf:(Z)Ljava/lang/Boolean;
        //   257: invokevirtual   sweetie/leonware/api/module/setting/BooleanSetting.value:(Ljava/lang/Boolean;)Lsweetie/leonware/api/module/setting/BooleanSetting;
        //   260: aastore        
        //   261: dup            
        //   262: iconst_5       
        //   263: new             Lsweetie/leonware/api/module/setting/BooleanSetting;
        //   266: dup            
        //   267: ldc             "Staff"
        //   269: invokespecial   sweetie/leonware/api/module/setting/BooleanSetting.<init>:(Ljava/lang/String;)V
        //   272: iconst_0       
        //   273: invokestatic    java/lang/Boolean.valueOf:(Z)Ljava/lang/Boolean;
        //   276: invokevirtual   sweetie/leonware/api/module/setting/BooleanSetting.value:(Ljava/lang/Boolean;)Lsweetie/leonware/api/module/setting/BooleanSetting;
        //   279: aastore        
        //   280: dup            
        //   281: bipush          6
        //   283: new             Lsweetie/leonware/api/module/setting/BooleanSetting;
        //   286: dup            
        //   287: ldc             "Eternity"
        //   289: invokespecial   sweetie/leonware/api/module/setting/BooleanSetting.<init>:(Ljava/lang/String;)V
        //   292: iconst_1       
        //   293: invokestatic    java/lang/Boolean.valueOf:(Z)Ljava/lang/Boolean;
        //   296: invokevirtual   sweetie/leonware/api/module/setting/BooleanSetting.value:(Ljava/lang/Boolean;)Lsweetie/leonware/api/module/setting/BooleanSetting;
        //   299: aastore        
        //   300: dup            
        //   301: bipush          7
        //   303: new             Lsweetie/leonware/api/module/setting/BooleanSetting;
        //   306: dup            
        //   307: ldc             "Luxe"
        //   309: invokespecial   sweetie/leonware/api/module/setting/BooleanSetting.<init>:(Ljava/lang/String;)V
        //   312: iconst_1       
        //   313: invokestatic    java/lang/Boolean.valueOf:(Z)Ljava/lang/Boolean;
        //   316: invokevirtual   sweetie/leonware/api/module/setting/BooleanSetting.value:(Ljava/lang/Boolean;)Lsweetie/leonware/api/module/setting/BooleanSetting;
        //   319: aastore        
        //   320: invokevirtual   sweetie/leonware/api/module/setting/MultiBooleanSetting.value:([Lsweetie/leonware/api/module/setting/BooleanSetting;)Lsweetie/leonware/api/module/setting/MultiBooleanSetting;
        //   323: aload_0         /* this */
        //   324: invokedynamic   BootstrapMethod #5, get:(Lsweetie/leonware/client/features/modules/player/AssistantModule;)Ljava/util/function/Supplier;
        //   329: invokevirtual   sweetie/leonware/api/module/setting/MultiBooleanSetting.setVisible:(Ljava/util/function/Supplier;)Lsweetie/leonware/api/module/setting/MultiBooleanSetting;
        //   332: putfield        sweetie/leonware/client/features/modules/player/AssistantModule.lonyPrefixes:Lsweetie/leonware/api/module/setting/MultiBooleanSetting;
        //   335: aload_0         /* this */
        //   336: new             Lsweetie/leonware/api/module/setting/ModeSetting;
        //   339: dup            
        //   340: ldc             "Mode"
        //   342: invokespecial   sweetie/leonware/api/module/setting/ModeSetting.<init>:(Ljava/lang/String;)V
        //   345: ldc             "Fun Time"
        //   347: invokevirtual   sweetie/leonware/api/module/setting/ModeSetting.value:(Ljava/lang/String;)Lsweetie/leonware/api/module/setting/ModeSetting;
        //   350: iconst_4       
        //   351: anewarray       Ljava/lang/String;
        //   354: dup            
        //   355: iconst_0       
        //   356: ldc             "Fun Time"
        //   358: aastore        
        //   359: dup            
        //   360: iconst_1       
        //   361: ldc             "Holy World"
        //   363: aastore        
        //   364: dup            
        //   365: iconst_2       
        //   366: ldc             "Lony Grief"
        //   368: aastore        
        //   369: dup            
        //   370: iconst_3       
        //   371: ldc             "Reallyworld"
        //   373: aastore        
        //   374: invokevirtual   sweetie/leonware/api/module/setting/ModeSetting.values:([Ljava/lang/String;)Lsweetie/leonware/api/module/setting/ModeSetting;
        //   377: aload_0         /* this */
        //   378: getfield        sweetie/leonware/client/features/modules/player/AssistantModule.isHotkeysEnabled:Ljava/util/function/Supplier;
        //   381: invokevirtual   sweetie/leonware/api/module/setting/ModeSetting.setVisible:(Ljava/util/function/Supplier;)Lsweetie/leonware/api/module/setting/ModeSetting;
        //   384: aload_0         /* this */
        //   385: invokedynamic   BootstrapMethod #6, run:(Lsweetie/leonware/client/features/modules/player/AssistantModule;)Ljava/lang/Runnable;
        //   390: invokevirtual   sweetie/leonware/api/module/setting/ModeSetting.onAction:(Ljava/lang/Runnable;)Lsweetie/leonware/api/module/setting/ModeSetting;
        //   393: putfield        sweetie/leonware/client/features/modules/player/AssistantModule.mode:Lsweetie/leonware/api/module/setting/ModeSetting;
        //   396: aload_0         /* this */
        //   397: new             Lsweetie/leonware/api/module/setting/BooleanSetting;
        //   400: dup            
        //   401: ldc             "Legit"
        //   403: invokespecial   sweetie/leonware/api/module/setting/BooleanSetting.<init>:(Ljava/lang/String;)V
        //   406: iconst_1       
        //   407: invokestatic    java/lang/Boolean.valueOf:(Z)Ljava/lang/Boolean;
        //   410: invokevirtual   sweetie/leonware/api/module/setting/BooleanSetting.value:(Ljava/lang/Boolean;)Lsweetie/leonware/api/module/setting/BooleanSetting;
        //   413: aload_0         /* this */
        //   414: getfield        sweetie/leonware/client/features/modules/player/AssistantModule.isHotkeysEnabled:Ljava/util/function/Supplier;
        //   417: invokevirtual   sweetie/leonware/api/module/setting/BooleanSetting.setVisible:(Ljava/util/function/Supplier;)Lsweetie/leonware/api/module/setting/BooleanSetting;
        //   420: putfield        sweetie/leonware/client/features/modules/player/AssistantModule.legit:Lsweetie/leonware/api/module/setting/BooleanSetting;
        //   423: aload_0         /* this */
        //   424: new             Lsweetie/leonware/api/module/setting/BooleanSetting;
        //   427: dup            
        //   428: ldc             "Auto-Player"
        //   430: invokespecial   sweetie/leonware/api/module/setting/BooleanSetting.<init>:(Ljava/lang/String;)V
        //   433: iconst_1       
        //   434: invokestatic    java/lang/Boolean.valueOf:(Z)Ljava/lang/Boolean;
        //   437: invokevirtual   sweetie/leonware/api/module/setting/BooleanSetting.value:(Ljava/lang/Boolean;)Lsweetie/leonware/api/module/setting/BooleanSetting;
        //   440: aload_0         /* this */
        //   441: getfield        sweetie/leonware/client/features/modules/player/AssistantModule.isLGKeys:Ljava/util/function/Supplier;
        //   444: invokevirtual   sweetie/leonware/api/module/setting/BooleanSetting.setVisible:(Ljava/util/function/Supplier;)Lsweetie/leonware/api/module/setting/BooleanSetting;
        //   447: putfield        sweetie/leonware/client/features/modules/player/AssistantModule.autoPlayerLony:Lsweetie/leonware/api/module/setting/BooleanSetting;
        //   450: aload_0         /* this */
        //   451: new             Lsweetie/leonware/api/module/setting/BooleanSetting;
        //   454: dup            
        //   455: ldc             "\u0410\u043d\u0442\u0438-\u043f\u043e\u043b\u0435\u0442 \u043e\u0431\u0445\u043e\u0434"
        //   457: invokespecial   sweetie/leonware/api/module/setting/BooleanSetting.<init>:(Ljava/lang/String;)V
        //   460: iconst_0       
        //   461: invokestatic    java/lang/Boolean.valueOf:(Z)Ljava/lang/Boolean;
        //   464: invokevirtual   sweetie/leonware/api/module/setting/BooleanSetting.value:(Ljava/lang/Boolean;)Lsweetie/leonware/api/module/setting/BooleanSetting;
        //   467: aload_0         /* this */
        //   468: getfield        sweetie/leonware/client/features/modules/player/AssistantModule.isRWKeys:Ljava/util/function/Supplier;
        //   471: invokevirtual   sweetie/leonware/api/module/setting/BooleanSetting.setVisible:(Ljava/util/function/Supplier;)Lsweetie/leonware/api/module/setting/BooleanSetting;
        //   474: putfield        sweetie/leonware/client/features/modules/player/AssistantModule.antipolet:Lsweetie/leonware/api/module/setting/BooleanSetting;
        //   477: aload_0         /* this */
        //   478: new             Ljava/util/LinkedHashMap;
        //   481: dup            
        //   482: invokespecial   java/util/LinkedHashMap.<init>:()V
        //   485: putfield        sweetie/leonware/client/features/modules/player/AssistantModule.keyBindings:Ljava/util/Map;
        //   488: aload_0         /* this */
        //   489: iconst_1       
        //   490: invokestatic    java/util/concurrent/Executors.newScheduledThreadPool:(I)Ljava/util/concurrent/ScheduledExecutorService;
        //   493: putfield        sweetie/leonware/client/features/modules/player/AssistantModule.scheduler:Ljava/util/concurrent/ScheduledExecutorService;
        //   496: aload_0         /* this */
        //   497: new             Ljava/util/ArrayList;
        //   500: dup            
        //   501: invokespecial   java/util/ArrayList.<init>:()V
        //   504: putfield        sweetie/leonware/client/features/modules/player/AssistantModule.consumables:Ljava/util/List;
        //   507: aload_0         /* this */
        //   508: new             Ljava/util/HashMap;
        //   511: dup            
        //   512: invokespecial   java/util/HashMap.<init>:()V
        //   515: putfield        sweetie/leonware/client/features/modules/player/AssistantModule.consumableNames:Ljava/util/Map;
        //   518: aload_0         /* this */
        //   519: getfield        sweetie/leonware/client/features/modules/player/AssistantModule.keyBindings:Ljava/util/Map;
        //   522: new             Lsweetie/leonware/api/utils/player/InventoryUtil$ItemUsage;
        //   525: dup            
        //   526: getstatic       net/minecraft/class_1802.field_8449:Lnet/minecraft/class_1792;
        //   529: aload_0         /* this */
        //   530: invokespecial   sweetie/leonware/api/utils/player/InventoryUtil$ItemUsage.<init>:(Lnet/minecraft/class_1792;Lsweetie/leonware/api/module/Module;)V
        //   533: new             Lsweetie/leonware/api/system/backend/Pair;
        //   536: dup            
        //   537: new             Lsweetie/leonware/api/module/setting/BindSetting;
        //   540: dup            
        //   541: ldc_w           "Disorientation"
        //   544: invokespecial   sweetie/leonware/api/module/setting/BindSetting.<init>:(Ljava/lang/String;)V
        //   547: sipush          -999
        //   550: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   553: invokevirtual   sweetie/leonware/api/module/setting/BindSetting.value:(Ljava/lang/Integer;)Lsweetie/leonware/api/module/setting/BindSetting;
        //   556: getstatic       sweetie/leonware/client/features/modules/player/AssistantModule$Mode.FUNTIME:Lsweetie/leonware/client/features/modules/player/AssistantModule$Mode;
        //   559: invokespecial   sweetie/leonware/api/system/backend/Pair.<init>:(Ljava/lang/Object;Ljava/lang/Object;)V
        //   562: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   567: pop            
        //   568: aload_0         /* this */
        //   569: getfield        sweetie/leonware/client/features/modules/player/AssistantModule.keyBindings:Ljava/util/Map;
        //   572: new             Lsweetie/leonware/api/utils/player/InventoryUtil$ItemUsage;
        //   575: dup            
        //   576: getstatic       net/minecraft/class_1802.field_22021:Lnet/minecraft/class_1792;
        //   579: aload_0         /* this */
        //   580: invokespecial   sweetie/leonware/api/utils/player/InventoryUtil$ItemUsage.<init>:(Lnet/minecraft/class_1792;Lsweetie/leonware/api/module/Module;)V
        //   583: new             Lsweetie/leonware/api/system/backend/Pair;
        //   586: dup            
        //   587: new             Lsweetie/leonware/api/module/setting/BindSetting;
        //   590: dup            
        //   591: ldc_w           "Trap"
        //   594: invokespecial   sweetie/leonware/api/module/setting/BindSetting.<init>:(Ljava/lang/String;)V
        //   597: sipush          -999
        //   600: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   603: invokevirtual   sweetie/leonware/api/module/setting/BindSetting.value:(Ljava/lang/Integer;)Lsweetie/leonware/api/module/setting/BindSetting;
        //   606: getstatic       sweetie/leonware/client/features/modules/player/AssistantModule$Mode.FUNTIME:Lsweetie/leonware/client/features/modules/player/AssistantModule$Mode;
        //   609: invokespecial   sweetie/leonware/api/system/backend/Pair.<init>:(Ljava/lang/Object;Ljava/lang/Object;)V
        //   612: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   617: pop            
        //   618: aload_0         /* this */
        //   619: getfield        sweetie/leonware/client/features/modules/player/AssistantModule.keyBindings:Ljava/util/Map;
        //   622: new             Lsweetie/leonware/api/utils/player/InventoryUtil$ItemUsage;
        //   625: dup            
        //   626: getstatic       net/minecraft/class_1802.field_8479:Lnet/minecraft/class_1792;
        //   629: aload_0         /* this */
        //   630: invokespecial   sweetie/leonware/api/utils/player/InventoryUtil$ItemUsage.<init>:(Lnet/minecraft/class_1792;Lsweetie/leonware/api/module/Module;)V
        //   633: new             Lsweetie/leonware/api/system/backend/Pair;
        //   636: dup            
        //   637: new             Lsweetie/leonware/api/module/setting/BindSetting;
        //   640: dup            
        //   641: ldc_w           "Clear dust"
        //   644: invokespecial   sweetie/leonware/api/module/setting/BindSetting.<init>:(Ljava/lang/String;)V
        //   647: sipush          -999
        //   650: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   653: invokevirtual   sweetie/leonware/api/module/setting/BindSetting.value:(Ljava/lang/Integer;)Lsweetie/leonware/api/module/setting/BindSetting;
        //   656: getstatic       sweetie/leonware/client/features/modules/player/AssistantModule$Mode.FUNTIME:Lsweetie/leonware/client/features/modules/player/AssistantModule$Mode;
        //   659: invokespecial   sweetie/leonware/api/system/backend/Pair.<init>:(Ljava/lang/Object;Ljava/lang/Object;)V
        //   662: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   667: pop            
        //   668: aload_0         /* this */
        //   669: getfield        sweetie/leonware/client/features/modules/player/AssistantModule.keyBindings:Ljava/util/Map;
        //   672: new             Lsweetie/leonware/api/utils/player/InventoryUtil$ItemUsage;
        //   675: dup            
        //   676: getstatic       net/minecraft/class_1802.field_8814:Lnet/minecraft/class_1792;
        //   679: aload_0         /* this */
        //   680: invokespecial   sweetie/leonware/api/utils/player/InventoryUtil$ItemUsage.<init>:(Lnet/minecraft/class_1792;Lsweetie/leonware/api/module/Module;)V
        //   683: new             Lsweetie/leonware/api/system/backend/Pair;
        //   686: dup            
        //   687: new             Lsweetie/leonware/api/module/setting/BindSetting;
        //   690: dup            
        //   691: ldc_w           "Fire whirl"
        //   694: invokespecial   sweetie/leonware/api/module/setting/BindSetting.<init>:(Ljava/lang/String;)V
        //   697: sipush          -999
        //   700: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   703: invokevirtual   sweetie/leonware/api/module/setting/BindSetting.value:(Ljava/lang/Integer;)Lsweetie/leonware/api/module/setting/BindSetting;
        //   706: getstatic       sweetie/leonware/client/features/modules/player/AssistantModule$Mode.FUNTIME:Lsweetie/leonware/client/features/modules/player/AssistantModule$Mode;
        //   709: invokespecial   sweetie/leonware/api/system/backend/Pair.<init>:(Ljava/lang/Object;Ljava/lang/Object;)V
        //   712: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   717: pop            
        //   718: aload_0         /* this */
        //   719: getfield        sweetie/leonware/client/features/modules/player/AssistantModule.keyBindings:Ljava/util/Map;
        //   722: new             Lsweetie/leonware/api/utils/player/InventoryUtil$ItemUsage;
        //   725: dup            
        //   726: getstatic       net/minecraft/class_1802.field_8551:Lnet/minecraft/class_1792;
        //   729: aload_0         /* this */
        //   730: invokespecial   sweetie/leonware/api/utils/player/InventoryUtil$ItemUsage.<init>:(Lnet/minecraft/class_1792;Lsweetie/leonware/api/module/Module;)V
        //   733: new             Lsweetie/leonware/api/system/backend/Pair;
        //   736: dup            
        //   737: new             Lsweetie/leonware/api/module/setting/BindSetting;
        //   740: dup            
        //   741: ldc_w           "Plast"
        //   744: invokespecial   sweetie/leonware/api/module/setting/BindSetting.<init>:(Ljava/lang/String;)V
        //   747: sipush          -999
        //   750: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   753: invokevirtual   sweetie/leonware/api/module/setting/BindSetting.value:(Ljava/lang/Integer;)Lsweetie/leonware/api/module/setting/BindSetting;
        //   756: getstatic       sweetie/leonware/client/features/modules/player/AssistantModule$Mode.FUNTIME:Lsweetie/leonware/client/features/modules/player/AssistantModule$Mode;
        //   759: invokespecial   sweetie/leonware/api/system/backend/Pair.<init>:(Ljava/lang/Object;Ljava/lang/Object;)V
        //   762: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   767: pop            
        //   768: aload_0         /* this */
        //   769: getfield        sweetie/leonware/client/features/modules/player/AssistantModule.keyBindings:Ljava/util/Map;
        //   772: new             Lsweetie/leonware/api/utils/player/InventoryUtil$ItemUsage;
        //   775: dup            
        //   776: getstatic       net/minecraft/class_1802.field_8614:Lnet/minecraft/class_1792;
        //   779: aload_0         /* this */
        //   780: invokespecial   sweetie/leonware/api/utils/player/InventoryUtil$ItemUsage.<init>:(Lnet/minecraft/class_1792;Lsweetie/leonware/api/module/Module;)V
        //   783: new             Lsweetie/leonware/api/system/backend/Pair;
        //   786: dup            
        //   787: new             Lsweetie/leonware/api/module/setting/BindSetting;
        //   790: dup            
        //   791: ldc_w           "Divine aura"
        //   794: invokespecial   sweetie/leonware/api/module/setting/BindSetting.<init>:(Ljava/lang/String;)V
        //   797: sipush          -999
        //   800: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   803: invokevirtual   sweetie/leonware/api/module/setting/BindSetting.value:(Ljava/lang/Integer;)Lsweetie/leonware/api/module/setting/BindSetting;
        //   806: getstatic       sweetie/leonware/client/features/modules/player/AssistantModule$Mode.FUNTIME:Lsweetie/leonware/client/features/modules/player/AssistantModule$Mode;
        //   809: invokespecial   sweetie/leonware/api/system/backend/Pair.<init>:(Ljava/lang/Object;Ljava/lang/Object;)V
        //   812: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   817: pop            
        //   818: aload_0         /* this */
        //   819: getfield        sweetie/leonware/client/features/modules/player/AssistantModule.keyBindings:Ljava/util/Map;
        //   822: new             Lsweetie/leonware/api/utils/player/InventoryUtil$ItemUsage;
        //   825: dup            
        //   826: getstatic       net/minecraft/class_1802.field_8662:Lnet/minecraft/class_1792;
        //   829: aload_0         /* this */
        //   830: invokespecial   sweetie/leonware/api/utils/player/InventoryUtil$ItemUsage.<init>:(Lnet/minecraft/class_1792;Lsweetie/leonware/api/module/Module;)V
        //   833: new             Lsweetie/leonware/api/system/backend/Pair;
        //   836: dup            
        //   837: new             Lsweetie/leonware/api/module/setting/BindSetting;
        //   840: dup            
        //   841: ldc_w           "Explosive trap"
        //   844: invokespecial   sweetie/leonware/api/module/setting/BindSetting.<init>:(Ljava/lang/String;)V
        //   847: sipush          -999
        //   850: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   853: invokevirtual   sweetie/leonware/api/module/setting/BindSetting.value:(Ljava/lang/Integer;)Lsweetie/leonware/api/module/setting/BindSetting;
        //   856: getstatic       sweetie/leonware/client/features/modules/player/AssistantModule$Mode.HOLYWORLD:Lsweetie/leonware/client/features/modules/player/AssistantModule$Mode;
        //   859: invokespecial   sweetie/leonware/api/system/backend/Pair.<init>:(Ljava/lang/Object;Ljava/lang/Object;)V
        //   862: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   867: pop            
        //   868: aload_0         /* this */
        //   869: getfield        sweetie/leonware/client/features/modules/player/AssistantModule.keyBindings:Ljava/util/Map;
        //   872: new             Lsweetie/leonware/api/utils/player/InventoryUtil$ItemUsage;
        //   875: dup            
        //   876: getstatic       net/minecraft/class_1802.field_8882:Lnet/minecraft/class_1792;
        //   879: aload_0         /* this */
        //   880: invokespecial   sweetie/leonware/api/utils/player/InventoryUtil$ItemUsage.<init>:(Lnet/minecraft/class_1792;Lsweetie/leonware/api/module/Module;)V
        //   883: new             Lsweetie/leonware/api/system/backend/Pair;
        //   886: dup            
        //   887: new             Lsweetie/leonware/api/module/setting/BindSetting;
        //   890: dup            
        //   891: ldc_w           "Default trap"
        //   894: invokespecial   sweetie/leonware/api/module/setting/BindSetting.<init>:(Ljava/lang/String;)V
        //   897: sipush          -999
        //   900: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   903: invokevirtual   sweetie/leonware/api/module/setting/BindSetting.value:(Ljava/lang/Integer;)Lsweetie/leonware/api/module/setting/BindSetting;
        //   906: getstatic       sweetie/leonware/client/features/modules/player/AssistantModule$Mode.HOLYWORLD:Lsweetie/leonware/client/features/modules/player/AssistantModule$Mode;
        //   909: invokespecial   sweetie/leonware/api/system/backend/Pair.<init>:(Ljava/lang/Object;Ljava/lang/Object;)V
        //   912: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   917: pop            
        //   918: aload_0         /* this */
        //   919: getfield        sweetie/leonware/client/features/modules/player/AssistantModule.keyBindings:Ljava/util/Map;
        //   922: new             Lsweetie/leonware/api/utils/player/InventoryUtil$ItemUsage;
        //   925: dup            
        //   926: getstatic       net/minecraft/class_1802.field_8137:Lnet/minecraft/class_1792;
        //   929: aload_0         /* this */
        //   930: invokespecial   sweetie/leonware/api/utils/player/InventoryUtil$ItemUsage.<init>:(Lnet/minecraft/class_1792;Lsweetie/leonware/api/module/Module;)V
        //   933: new             Lsweetie/leonware/api/system/backend/Pair;
        //   936: dup            
        //   937: new             Lsweetie/leonware/api/module/setting/BindSetting;
        //   940: dup            
        //   941: ldc_w           "Stun"
        //   944: invokespecial   sweetie/leonware/api/module/setting/BindSetting.<init>:(Ljava/lang/String;)V
        //   947: sipush          -999
        //   950: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   953: invokevirtual   sweetie/leonware/api/module/setting/BindSetting.value:(Ljava/lang/Integer;)Lsweetie/leonware/api/module/setting/BindSetting;
        //   956: getstatic       sweetie/leonware/client/features/modules/player/AssistantModule$Mode.HOLYWORLD:Lsweetie/leonware/client/features/modules/player/AssistantModule$Mode;
        //   959: invokespecial   sweetie/leonware/api/system/backend/Pair.<init>:(Ljava/lang/Object;Ljava/lang/Object;)V
        //   962: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   967: pop            
        //   968: aload_0         /* this */
        //   969: getfield        sweetie/leonware/client/features/modules/player/AssistantModule.keyBindings:Ljava/util/Map;
        //   972: new             Lsweetie/leonware/api/utils/player/InventoryUtil$ItemUsage;
        //   975: dup            
        //   976: getstatic       net/minecraft/class_1802.field_8814:Lnet/minecraft/class_1792;
        //   979: aload_0         /* this */
        //   980: invokespecial   sweetie/leonware/api/utils/player/InventoryUtil$ItemUsage.<init>:(Lnet/minecraft/class_1792;Lsweetie/leonware/api/module/Module;)V
        //   983: new             Lsweetie/leonware/api/system/backend/Pair;
        //   986: dup            
        //   987: new             Lsweetie/leonware/api/module/setting/BindSetting;
        //   990: dup            
        //   991: ldc_w           "Explosive thing"
        //   994: invokespecial   sweetie/leonware/api/module/setting/BindSetting.<init>:(Ljava/lang/String;)V
        //   997: sipush          -999
        //  1000: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1003: invokevirtual   sweetie/leonware/api/module/setting/BindSetting.value:(Ljava/lang/Integer;)Lsweetie/leonware/api/module/setting/BindSetting;
        //  1006: getstatic       sweetie/leonware/client/features/modules/player/AssistantModule$Mode.HOLYWORLD:Lsweetie/leonware/client/features/modules/player/AssistantModule$Mode;
        //  1009: invokespecial   sweetie/leonware/api/system/backend/Pair.<init>:(Ljava/lang/Object;Ljava/lang/Object;)V
        //  1012: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  1017: pop            
        //  1018: aload_0         /* this */
        //  1019: getfield        sweetie/leonware/client/features/modules/player/AssistantModule.keyBindings:Ljava/util/Map;
        //  1022: new             Lsweetie/leonware/api/utils/player/InventoryUtil$ItemUsage;
        //  1025: dup            
        //  1026: getstatic       net/minecraft/class_1802.field_8543:Lnet/minecraft/class_1792;
        //  1029: aload_0         /* this */
        //  1030: invokespecial   sweetie/leonware/api/utils/player/InventoryUtil$ItemUsage.<init>:(Lnet/minecraft/class_1792;Lsweetie/leonware/api/module/Module;)V
        //  1033: new             Lsweetie/leonware/api/system/backend/Pair;
        //  1036: dup            
        //  1037: new             Lsweetie/leonware/api/module/setting/BindSetting;
        //  1040: dup            
        //  1041: ldc_w           "Snowball"
        //  1044: invokespecial   sweetie/leonware/api/module/setting/BindSetting.<init>:(Ljava/lang/String;)V
        //  1047: sipush          -999
        //  1050: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1053: invokevirtual   sweetie/leonware/api/module/setting/BindSetting.value:(Ljava/lang/Integer;)Lsweetie/leonware/api/module/setting/BindSetting;
        //  1056: getstatic       sweetie/leonware/client/features/modules/player/AssistantModule$Mode.HOLYWORLD:Lsweetie/leonware/client/features/modules/player/AssistantModule$Mode;
        //  1059: invokespecial   sweetie/leonware/api/system/backend/Pair.<init>:(Ljava/lang/Object;Ljava/lang/Object;)V
        //  1062: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  1067: pop            
        //  1068: aload_0         /* this */
        //  1069: getfield        sweetie/leonware/client/features/modules/player/AssistantModule.keyBindings:Ljava/util/Map;
        //  1072: new             Lsweetie/leonware/api/utils/player/InventoryUtil$ItemUsage;
        //  1075: dup            
        //  1076: getstatic       net/minecraft/class_1802.field_8696:Lnet/minecraft/class_1792;
        //  1079: aload_0         /* this */
        //  1080: invokespecial   sweetie/leonware/api/utils/player/InventoryUtil$ItemUsage.<init>:(Lnet/minecraft/class_1792;Lsweetie/leonware/api/module/Module;)V
        //  1083: new             Lsweetie/leonware/api/system/backend/Pair;
        //  1086: dup            
        //  1087: new             Lsweetie/leonware/api/module/setting/BindSetting;
        //  1090: dup            
        //  1091: ldc_w           "\u041b\u0438\u0432\u0430\u043b\u043e\u0447\u043a\u0430"
        //  1094: invokespecial   sweetie/leonware/api/module/setting/BindSetting.<init>:(Ljava/lang/String;)V
        //  1097: sipush          -999
        //  1100: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1103: invokevirtual   sweetie/leonware/api/module/setting/BindSetting.value:(Ljava/lang/Integer;)Lsweetie/leonware/api/module/setting/BindSetting;
        //  1106: getstatic       sweetie/leonware/client/features/modules/player/AssistantModule$Mode.LONYGRIEF:Lsweetie/leonware/client/features/modules/player/AssistantModule$Mode;
        //  1109: invokespecial   sweetie/leonware/api/system/backend/Pair.<init>:(Ljava/lang/Object;Ljava/lang/Object;)V
        //  1112: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  1117: pop            
        //  1118: aload_0         /* this */
        //  1119: getfield        sweetie/leonware/client/features/modules/player/AssistantModule.keyBindings:Ljava/util/Map;
        //  1122: new             Lsweetie/leonware/api/utils/player/InventoryUtil$ItemUsage;
        //  1125: dup            
        //  1126: getstatic       net/minecraft/class_1802.field_8639:Lnet/minecraft/class_1792;
        //  1129: aload_0         /* this */
        //  1130: invokespecial   sweetie/leonware/api/utils/player/InventoryUtil$ItemUsage.<init>:(Lnet/minecraft/class_1792;Lsweetie/leonware/api/module/Module;)V
        //  1133: new             Lsweetie/leonware/api/system/backend/Pair;
        //  1136: dup            
        //  1137: new             Lsweetie/leonware/api/module/setting/BindSetting;
        //  1140: dup            
        //  1141: ldc_w           "\u0424\u0435\u0439\u0435\u0440\u0432\u0435\u0440\u043a"
        //  1144: invokespecial   sweetie/leonware/api/module/setting/BindSetting.<init>:(Ljava/lang/String;)V
        //  1147: sipush          -999
        //  1150: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1153: invokevirtual   sweetie/leonware/api/module/setting/BindSetting.value:(Ljava/lang/Integer;)Lsweetie/leonware/api/module/setting/BindSetting;
        //  1156: getstatic       sweetie/leonware/client/features/modules/player/AssistantModule$Mode.REALLYWORLD:Lsweetie/leonware/client/features/modules/player/AssistantModule$Mode;
        //  1159: invokespecial   sweetie/leonware/api/system/backend/Pair.<init>:(Ljava/lang/Object;Ljava/lang/Object;)V
        //  1162: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  1167: pop            
        //  1168: aload_0         /* this */
        //  1169: bipush          7
        //  1171: anewarray       Lsweetie/leonware/api/module/setting/Setting;
        //  1174: dup            
        //  1175: iconst_0       
        //  1176: aload_0         /* this */
        //  1177: getfield        sweetie/leonware/client/features/modules/player/AssistantModule.functions:Lsweetie/leonware/api/module/setting/MultiBooleanSetting;
        //  1180: aastore        
        //  1181: dup            
        //  1182: iconst_1       
        //  1183: aload_0         /* this */
        //  1184: getfield        sweetie/leonware/client/features/modules/player/AssistantModule.mode:Lsweetie/leonware/api/module/setting/ModeSetting;
        //  1187: aastore        
        //  1188: dup            
        //  1189: iconst_2       
        //  1190: aload_0         /* this */
        //  1191: getfield        sweetie/leonware/client/features/modules/player/AssistantModule.lonyPrefixes:Lsweetie/leonware/api/module/setting/MultiBooleanSetting;
        //  1194: aastore        
        //  1195: dup            
        //  1196: iconst_3       
        //  1197: aload_0         /* this */
        //  1198: getfield        sweetie/leonware/client/features/modules/player/AssistantModule.legit:Lsweetie/leonware/api/module/setting/BooleanSetting;
        //  1201: aastore        
        //  1202: dup            
        //  1203: iconst_4       
        //  1204: aload_0         /* this */
        //  1205: getfield        sweetie/leonware/client/features/modules/player/AssistantModule.autoPvpLony:Lsweetie/leonware/api/module/setting/BooleanSetting;
        //  1208: aastore        
        //  1209: dup            
        //  1210: iconst_5       
        //  1211: aload_0         /* this */
        //  1212: getfield        sweetie/leonware/client/features/modules/player/AssistantModule.autoPlayerLony:Lsweetie/leonware/api/module/setting/BooleanSetting;
        //  1215: aastore        
        //  1216: dup            
        //  1217: bipush          6
        //  1219: aload_0         /* this */
        //  1220: getfield        sweetie/leonware/client/features/modules/player/AssistantModule.antipolet:Lsweetie/leonware/api/module/setting/BooleanSetting;
        //  1223: aastore        
        //  1224: invokevirtual   sweetie/leonware/client/features/modules/player/AssistantModule.addSettings:([Lsweetie/leonware/api/module/setting/Setting;)V
        //  1227: aload_0         /* this */
        //  1228: getfield        sweetie/leonware/client/features/modules/player/AssistantModule.keyBindings:Ljava/util/Map;
        //  1231: aload_0         /* this */
        //  1232: invokedynamic   BootstrapMethod #7, accept:(Lsweetie/leonware/client/features/modules/player/AssistantModule;)Ljava/util/function/BiConsumer;
        //  1237: invokeinterface java/util/Map.forEach:(Ljava/util/function/BiConsumer;)V
        //  1242: return         
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
        final EventListener tickEvent = TickEvent.getInstance().subscribe(new Listener<TickEvent>(event -> this.handleTickEvent()));
        final EventListener renderEvent = Render2DEvent.getInstance().subscribe(new Listener<Render2DEvent.Render2DEventData>(this::handleRenderEvent));
        final EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener<PacketEvent.PacketEventData>(this::handlePacketEvent));
        this.addEvents(tickEvent, renderEvent, packetEvent);
    }
    
    private void handlePacketEvent(final PacketEvent.PacketEventData event) {
        if (event.isSend() || AssistantModule.mc.field_1687 == null) {
            return;
        }
        final class_2596<?> packet = event.packet();
        if (packet instanceof final class_2767 soundPacket) {
            this.handleSoundPackets(soundPacket);
        }
        final class_2596<?> packet2 = event.packet();
        if (packet2 instanceof final class_7439 p) {
            final String msg = p.comp_763().getString();
            if (msg.contains("\u0410\u043d\u0442\u0438 \u041f\u043e\u043b\u0435\u0442 » \u0412\u044b \u043d\u0435 \u043c\u043e\u0436\u0435\u0442\u0435 \u0432\u0437\u043b\u0435\u0442\u0435\u0442\u044c!")) {
                this.need = true;
            }
        }
        if (this.autoPvpLony.getValue() && this.currentMode == Mode.LONYGRIEF) {
            final class_2596<?> packet3 = event.packet();
            if (packet3 instanceof final class_7439 chatPacket) {
                final String rawMsg = chatPacket.comp_763().getString();
                final String cleanMsg = rawMsg.replaceAll("(?i)§[0-9a-fklmnor]", " ").replace("\u203a", " ").replace(">", " ").trim();
                final Pattern pvpPattern = Pattern.compile("\u0418\u0433\u0440\u043e\u043a\\s+([a-zA-Z0-9_]{3,16})\\s+\u0438\u0449\u0435\u0442", 66);
                final Matcher pvpMatcher = pvpPattern.matcher(cleanMsg);
                if (pvpMatcher.find()) {
                    final String nickname = pvpMatcher.group(1);
                    if (nickname.equalsIgnoreCase(AssistantModule.mc.field_1724.method_5477().getString())) {
                        return;
                    }
                    final String rawPrefix = this.getPlayerRankFromTab(nickname);
                    if (this.shouldClickPvp(rawPrefix)) {
                        this.targetRival = nickname;
                    }
                }
                if (this.autoPlayerLony.getValue()) {
                    final Pattern pattern = Pattern.compile("(?:\u0438\u0433\u0440\u043e\u043a\u043e\u043c|\u0438\u0433\u0440\u043e\u043a)\\s+([^\\s!]+).+?(?:\u043a\u043e\u043e\u0440\u0434\u0438\u043d\u0430\u0442\u044b|\u041a\u043e\u0440\u0434\u0438\u043d\u0430\u0442\u044b|X):?\\s*(-?\\d+)[^\\d-]+-?\\d+[^\\d-]+(-?\\d+)", 66);
                    final Matcher matcher = pattern.matcher(cleanMsg);
                    if (matcher.find()) {
                        final String name = matcher.group(1);
                        try {
                            final int x = Integer.parseInt(matcher.group(2));
                            final int z = Integer.parseInt(matcher.group(3));
                            GpsManager.getInstance().setGps(new Vector2i(x, z), name);
                        }
                        catch (final NumberFormatException ex) {}
                    }
                }
            }
        }
    }
    
    private void handleAutoPvPLogic() {
        if (this.targetRival != null) {
            final class_1703 field_7512 = AssistantModule.mc.field_1724.field_7512;
            if (field_7512 instanceof final class_1707 handler) {
                for (int i = 0; i < handler.field_7761.size(); ++i) {
                    final class_1799 stack = handler.method_7611(i).method_7677();
                    if (!stack.method_7960() && stack.method_31574(class_1802.field_8288)) {
                        AssistantModule.mc.field_1761.method_2906(handler.field_7763, i, 0, class_1713.field_7790, (class_1657)AssistantModule.mc.field_1724);
                        this.targetRival = null;
                        return;
                    }
                }
            }
        }
    }
    
    private String getPlayerRankFromTab(final String name) {
        if (AssistantModule.mc.method_1562() == null) {
            return "";
        }
        for (final class_640 entry : AssistantModule.mc.method_1562().method_2880()) {
            if (entry.method_2966().getName().equalsIgnoreCase(name)) {
                final StringBuilder fullText = new StringBuilder();
                final class_268 team = entry.method_2955();
                if (team != null) {
                    fullText.append(team.method_1144().getString());
                }
                if (entry.method_2971() != null) {
                    fullText.append(entry.method_2971().getString());
                }
                return fullText.toString().replaceAll("(?i)§[0-9a-fklmnor]", "");
            }
        }
        return "";
    }
    
    private boolean shouldClickPvp(final String text) {
        if (text == null || text.isEmpty()) {
            return false;
        }
        final String upper = text.toUpperCase();
        return (this.lonyPrefixes.isEnabled("Player") && (upper.contains("PLAYER") || text.contains("\u1d18\u029f\u1d00\u028f\u1d07\u0280"))) || (this.lonyPrefixes.isEnabled("Legenda") && (upper.contains("LEGENDA") || text.contains("\u029f\u1d07\u0262\u1d07\u0274\u1d05\u1d00"))) || (this.lonyPrefixes.isEnabled("Pravitel") && (upper.contains("PRAVITEL") || text.contains("\u1d18\u0280\u1d00\u1d20\u026a\u1d1b\u1d07\u029f"))) || (this.lonyPrefixes.isEnabled("Povelitel") && (upper.contains("POVELITEL") || text.contains("\u1d18\u1d0f\u1d20\u1d07\u029f\u026a\u1d1b\u1d07\u029f"))) || (this.lonyPrefixes.isEnabled("D.Admin") && (upper.contains("DADMIN") || text.contains("\u1d05\u1d00\u1d05\u1d0d\u026a\u0274"))) || (this.lonyPrefixes.isEnabled("Staff") && (upper.contains("STAFF") || upper.contains("ADMIN") || text.contains("s\u1d1b\u1d00\ua730\ua730"))) || (this.lonyPrefixes.isEnabled("Eternity") && (upper.contains("ETERNITY") || text.contains("\u1d07\u1d1b\u1d07\u0280\u0274\u026a\u1d1b\u028f"))) || (this.lonyPrefixes.isEnabled("Luxe") && (upper.contains("LUXE") || text.contains("\u029f\u1d1cx\u1d07")));
    }
    
    private void handleTickEvent() {
        if (AssistantModule.mc.field_1724 == null) {
            return;
        }
        if (this.antipolet.getValue() && this.need) {
            if (!AssistantModule.mc.field_1724.method_24828() && AssistantModule.mc.field_1724.method_6118(class_1304.field_6174).method_7909() == class_1802.field_8833) {
                AssistantModule.mc.field_1724.field_3944.method_52787((class_2596)new class_2848((class_1297)AssistantModule.mc.field_1724, class_2848.class_2849.field_12982));
                AssistantModule.mc.field_1724.method_23669();
                if (this.fireworkUse) {
                    int fwSlot = InventoryUtil.findItem(class_1802.field_8639, true);
                    if (fwSlot == -1) {
                        fwSlot = InventoryUtil.findItem(class_1802.field_8639, false);
                    }
                    if (fwSlot != -1) {
                        final int oldSlot = AssistantModule.mc.field_1724.method_31548().field_7545;
                        if (fwSlot > 8) {
                            final int hbSlot = InventoryUtil.findBestSlotInHotBar();
                            InventoryUtil.swapSlots(fwSlot, hbSlot);
                            InventoryUtil.swapToSlot(hbSlot);
                            InventoryUtil.useItem(class_1268.field_5808);
                            InventoryUtil.swapToSlot(oldSlot);
                            InventoryUtil.swapSlots(fwSlot, hbSlot);
                        }
                        else {
                            InventoryUtil.swapToSlot(fwSlot);
                            InventoryUtil.useItem(class_1268.field_5808);
                            InventoryUtil.swapToSlot(oldSlot);
                        }
                    }
                    this.fireworkUse = false;
                }
            }
            else {
                this.need = false;
            }
        }
        if (this.autoPvpLony.getValue() && this.currentMode == Mode.LONYGRIEF) {
            this.handleAutoPvPLogic();
        }
        if (!this.isHotkeysEnabled.get() || AssistantModule.mc.field_1755 != null) {
            return;
        }
        this.keyBindings.forEach((usage, pair) -> {
            if (pair.right() == this.currentMode) {
                usage.handleUse(pair.left().getValue(), this.legit.getValue());
            }
        });
    }
    
    private void handleSoundPackets(final class_2767 soundPacket) {
        final String soundPath = soundPacket.method_11894().method_55840();
        if (soundPath.equals("minecraft:block.piston.contract")) {
            final class_243 pos = class_243.method_24953((class_2382)new class_2338((int)soundPacket.method_11890(), (int)soundPacket.method_11889(), (int)soundPacket.method_11893()));
            this.consumables.add(new Pair<Long, class_243>(System.currentTimeMillis() + 15000L, pos));
            this.consumableNames.put(pos, "Trap");
        }
        else if (soundPath.equals("minecraft:block.anvil.place")) {
            this.handleAnvilLogic(soundPacket);
        }
    }
    
    private void handleAnvilLogic(final class_2767 soundPacket) {
        final class_2338 soundPos = new class_2338((int)soundPacket.method_11890(), (int)soundPacket.method_11889(), (int)soundPacket.method_11893());
        this.scheduler.schedule(() -> this.getCube(soundPos, 4, 4).stream().filter(pos -> this.getDistance(soundPos, pos) > 2.0 && AssistantModule.mc.field_1687.method_8320(pos).method_26204() == class_2246.field_10445).min(Comparator.comparing(pos -> this.getDistance(soundPos, pos))).ifPresent(pos -> {
            if (!this.getCube(pos, 1, 1).stream().anyMatch(p -> AssistantModule.mc.field_1687.method_8320(p).method_26204() == class_2246.field_10535)) {
                final long solidCount = this.getCube(pos, 1, 1).stream().filter(p -> {
                    final class_2680 s = AssistantModule.mc.field_1687.method_8320(p);
                    return !s.method_26215() && s.method_26212((class_1922)AssistantModule.mc.field_1687, p);
                }).count();
                if (solidCount == 18L || solidCount == 15L || solidCount == 5L) {
                    final int time = (solidCount == 18L || solidCount == 15L) ? 20000 : 15000;
                    final class_243 addPos = class_243.method_24953((class_2382)pos).method_1031(0.0, (solidCount == 5L) ? -1.5 : 0.0, 0.0);
                    this.consumables.add(new Pair<Long, class_243>(System.currentTimeMillis() + time - 250L, addPos));
                    this.consumableNames.put(addPos, (solidCount == 18L || solidCount == 15L) ? "Plast" : "Trap");
                }
            }
        }), 250L, TimeUnit.MILLISECONDS);
    }
    
    private void handleRenderEvent(final Render2DEvent.Render2DEventData event) {
        if (!this.functions.isEnabled("Timers")) {
            return;
        }
        final class_4587 matrixStack = event.matrixStack();
        Pair<Long, class_243> cons = null;
        this.consumables.removeIf(cons -> cons.left() - System.currentTimeMillis() <= 0.0);
        final Iterator<Pair<Long, class_243>> iterator = this.consumables.iterator();
        while (iterator.hasNext()) {
            cons = iterator.next();
            final Vector2f screenPos = ProjectionUtil.project(cons.right());
            if (screenPos.x != Float.MAX_VALUE) {
                if (screenPos.y == Float.MAX_VALUE) {
                    continue;
                }
                final double time = MathUtil.round((cons.left() - System.currentTimeMillis()) / 1000.0, 1.0);
                final String text = (String)this.consumableNames.getOrDefault(cons.right(), "Timer") + ": " + time;
                final float textWidth = Fonts.PS_BOLD.getWidth(text, 7.0f);
                RenderUtil.BLUR_RECT.draw(matrixStack, screenPos.x - textWidth / 2.0f, screenPos.y, textWidth + 6.0f, 10.0f, 2.0f, UIColors.blur());
                Fonts.PS_BOLD.drawText(matrixStack, text, screenPos.x - textWidth / 2.0f + 3.0f, screenPos.y + 3.0f, 7.0f, UIColors.textColor());
            }
        }
    }
    
    private double getDistance(final class_2338 pos1, final class_2338 pos2) {
        return Math.sqrt(pos1.method_10262((class_2382)pos2));
    }
    
    private List<class_2338> getCube(final class_2338 center, final int xRadius, final int yRadius) {
        final List<class_2338> sphere = new ArrayList<class_2338>();
        for (int x = -xRadius; x <= xRadius; ++x) {
            for (int y = -yRadius; y <= yRadius; ++y) {
                for (int z = -xRadius; z <= xRadius; ++z) {
                    sphere.add(center.method_10069(x, y, z));
                }
            }
        }
        return sphere;
    }
    
    @Generated
    public static AssistantModule getInstance() {
        return AssistantModule.instance;
    }
    
    @Generated
    public MultiBooleanSetting getFunctions() {
        return this.functions;
    }
    
    @Generated
    public MultiBooleanSetting getLonyPrefixes() {
        return this.lonyPrefixes;
    }
    
    @Generated
    public ModeSetting getMode() {
        return this.mode;
    }
    
    static {
        instance = new AssistantModule();
    }
    
    public enum Mode
    {
        FUNTIME, 
        HOLYWORLD, 
        LONYGRIEF, 
        REALLYWORLD;
    }
}
