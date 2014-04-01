package com.nickardson.jscomputing;

import com.nickardson.jscomputing.common.CommonProxy;
import com.nickardson.jscomputing.common.blocks.BlockComputer;
import com.nickardson.jscomputing.common.tileentity.TileEntityComputer;
import com.nickardson.jscomputing.javascript.JavaScriptEngine;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
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
    public static final String VERSION = "0.0";

    @SidedProxy(clientSide = "com.nickardson.jscomputing.client.ClientProxy", serverSide = "com.nickardson.jscomputing.CommonProxy")
    public static CommonProxy proxy;

    public static Block computorz;

    public static CreativeTabs creativeTab = new JSComputingCreativeTab();

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        computorz = new BlockComputer();

        JavaScriptEngine.setup();

        GameRegistry.registerBlock(computorz, computorz.getUnlocalizedName());
        GameRegistry.registerTileEntity(TileEntityComputer.class, TileEntityComputer.NAME);

        MinecraftForge.EVENT_BUS.register(new JSEventListener());

        proxy.registerRenderers();
    }
}
