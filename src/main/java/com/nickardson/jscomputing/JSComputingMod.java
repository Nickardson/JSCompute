package com.nickardson.jscomputing;

import com.nickardson.jscomputing.common.CommonProxy;
import com.nickardson.jscomputing.common.GuiHandler;
import com.nickardson.jscomputing.common.blocks.BlockComputer;
import com.nickardson.jscomputing.common.network.ChannelHandler;
import com.nickardson.jscomputing.common.tileentity.TileEntityComputer;
import com.nickardson.jscomputing.javascript.JavaScriptEngine;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.MinecraftForge;

@Mod(
        modid = JSComputingMod.MOD_ID,
        name = JSComputingMod.MOD_ID,
        version = JSComputingMod.VERSION
)

public class JSComputingMod
{
    public static final String MOD_ID = "JSComputing";
    public static final String ASSET_ID = "jscomputing";
    public static final String MOD_CHANNEL = ASSET_ID;
    public static final String VERSION = "0.0";

    @Mod.Instance
    public static JSComputingMod instance;

    @SidedProxy(clientSide = "com.nickardson.jscomputing.client.ClientProxy", serverSide = "com.nickardson.jscomputing.CommonProxy")
    public static CommonProxy proxy;

    public static BlockComputer computorz;

    public static CreativeTabs creativeTab = new JSComputingCreativeTab();

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        instance = this;

        computorz = new BlockComputer();

        JavaScriptEngine.setup();

        GameRegistry.registerBlock(computorz, computorz.getUnlocalizedName());
        GameRegistry.registerTileEntity(TileEntityComputer.class, TileEntityComputer.NAME);

        ChannelHandler.init();

        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());

        MinecraftForge.EVENT_BUS.register(new JSEventListener());

        proxy.registerRenderers();
    }
}
