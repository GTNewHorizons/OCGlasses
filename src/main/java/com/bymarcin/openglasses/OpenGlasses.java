package com.bymarcin.openglasses;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bymarcin.openglasses.block.OpenGlassesTerminalBlock;
import com.bymarcin.openglasses.item.OpenGlassesItem;
import com.bymarcin.openglasses.network.GlassesNetworkRegistry;
import com.bymarcin.openglasses.network.packet.BlockInteractPacket;
import com.bymarcin.openglasses.network.packet.CloseOverlayPacket;
import com.bymarcin.openglasses.network.packet.EquipGlassesPacket;
import com.bymarcin.openglasses.network.packet.InteractOverlayPacket;
import com.bymarcin.openglasses.network.packet.KeyboardInteractOverlayPacket;
import com.bymarcin.openglasses.network.packet.OpenOverlayPacket;
import com.bymarcin.openglasses.network.packet.TerminalStatusPacket;
import com.bymarcin.openglasses.network.packet.UnequipGlassesPacket;
import com.bymarcin.openglasses.network.packet.WidgetUpdatePacket;
import com.bymarcin.openglasses.proxy.CommonProxy;
import com.bymarcin.openglasses.tileentity.OpenGlassesTerminalTileEntity;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import li.cil.oc.api.Items;

@Mod(modid = OpenGlasses.MODID, version = OpenGlasses.VERSION, dependencies = "required-after:OpenComputers@[1.4.0,)")
public class OpenGlasses {

    public static final String MODID = "openglasses";
    public static final String VERSION = OCGlassesTags.VERSION;

    public Configuration config;
    public static Logger logger = LogManager.getLogger(OpenGlasses.MODID);

    @SidedProxy(
            clientSide = "com.bymarcin.openglasses.proxy.ClientProxy",
            serverSide = "com.bymarcin.openglasses.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Instance(value = OpenGlasses.MODID)
    public static OpenGlasses instance;

    public static CreativeTabs creativeTab = CreativeTabs.tabRedstone;

    public static boolean baubles = false;
    public static boolean tinkers = false;

    public static OpenGlassesItem openGlasses;
    public static OpenGlassesTerminalBlock openTerminal;

    public static int energyBuffer = 100;
    public static double energyMultiplier = 1;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();
        GlassesNetworkRegistry.initialize();
        energyBuffer = config.get("Energy", "energyBuffer", 100).getInt(100);
        energyMultiplier = config
                .get("Energy", "energyMultiplier", 1.0, "PowerDrain= (NumberOfWidgets / 10) * energyMultiplier")
                .getDouble(1.0);
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        OpenGlasses.baubles = Loader.isModLoaded("Baubles");
        OpenGlasses.tinkers = Loader.isModLoaded("TConstruct");

        GlassesNetworkRegistry.registerPacket(0, BlockInteractPacket.class, Side.SERVER);
        GlassesNetworkRegistry.registerPacket(1, CloseOverlayPacket.class, Side.SERVER);
        GlassesNetworkRegistry.registerPacket(2, EquipGlassesPacket.class, Side.SERVER);
        GlassesNetworkRegistry.registerPacket(3, InteractOverlayPacket.class, Side.SERVER);
        GlassesNetworkRegistry.registerPacket(4, KeyboardInteractOverlayPacket.class, Side.SERVER);
        GlassesNetworkRegistry.registerPacket(5, OpenOverlayPacket.class, Side.SERVER);
        GlassesNetworkRegistry.registerPacket(6, UnequipGlassesPacket.class, Side.SERVER);
        GlassesNetworkRegistry.registerPacket(7, WidgetUpdatePacket.class, Side.CLIENT);
        GlassesNetworkRegistry.registerPacket(8, TerminalStatusPacket.class, Side.CLIENT);

        GameRegistry.registerBlock(openTerminal = new OpenGlassesTerminalBlock(), "openglassesterminal");
        GameRegistry.registerTileEntity(OpenGlassesTerminalTileEntity.class, "openglassesterminal");
        GameRegistry.registerItem(openGlasses = new OpenGlassesItem(), "openglasses");
        proxy.init();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        ItemStack ram = Items.get("ram5").createItemStack(1);
        ItemStack graphics = Items.get("graphicsCard3").createItemStack(1);
        ItemStack wlanCard = Items.get("wlanCard").createItemStack(1);
        ItemStack server = Items.get("geolyzer").createItemStack(1);
        ItemStack screen = Items.get("screen3").createItemStack(1);
        ItemStack cpu = Items.get("cpu3").createItemStack(1);

        GameRegistry
                .addRecipe(new ItemStack(openGlasses), "SCS", " W ", "   ", 'S', screen, 'W', wlanCard, 'C', graphics);
        GameRegistry.addRecipe(new ItemStack(openTerminal), "R  ", "S  ", "M  ", 'S', server, 'R', ram, 'M', cpu);

        config.save();
    }
}
