package com.nickardson.jscomputing;

import com.nickardson.jscomputing.common.CommonProxy;
import com.nickardson.jscomputing.common.GuiHandler;
import com.nickardson.jscomputing.common.blocks.BlockComputer;
import com.nickardson.jscomputing.common.blocks.BlockRobot;
import com.nickardson.jscomputing.common.items.ItemComputerReader;
import com.nickardson.jscomputing.common.network.ChannelHandler;
import com.nickardson.jscomputing.common.tileentity.TileEntityRobot;
import com.nickardson.jscomputing.common.tileentity.TileEntityTerminalComputer;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

@Mod(
        modid = JSComputingMod.MOD_ID,
        name = JSComputingMod.MOD_ID,
        version = JSComputingMod.VERSION
)

public class JSComputingMod
{
    /**
     * The user-friendly identifier.
     */
    public static final String MOD_ID = "JSComputing";

    /**
     * Prefix for assets.
     */
    public static final String ASSET_ID = "jscomputing";

    /**
     * Network ID for the ChannelHandler.
     */
    public static final String MOD_CHANNEL = ASSET_ID;

    /**
     * Mod Version
     */
    public static final String VERSION = "0.0";

    /**
     * The Singleton instance of JSComputing.
     */
    @Mod.Instance
    public static JSComputingMod instance;

    @SidedProxy(clientSide = "com.nickardson.jscomputing.client.ClientProxy", serverSide = "com.nickardson.jscomputing.common.CommonProxy")
    public static CommonProxy proxy;

    /**
     * The Creative tab where all items and blocks go.
     */
    public static CreativeTabs creativeTab = new JSComputingCreativeTab();

    public JSEventListener eventListener;

    /**
     * Container for blocks.
     */
    public static class Blocks {
        public static BlockComputer computer;
        public static BlockRobot robot;

        /**
         * Creates all blocks.
         */
        public static void init() {
            Blocks.register(Blocks.computer = new BlockComputer());
            Blocks.register(Blocks.robot = new BlockRobot());
        }

        /**
         * Register a block with Forge.
         * @param block The block to register.
         */
        public static void register(Block block) {
            GameRegistry.registerBlock(block, block.getUnlocalizedName());
        }
    }

    /**
     * Container for items.
     */
    public static class Items {
        public static ItemComputerReader computerReader;

        /**
         * Creates all items.
         */
        public static void init() {
            Items.register(Items.computerReader = new ItemComputerReader());
        }

        /**
         * Register an item with Forge.
         * @param item The item to register.
         */
        public static void register(Item item) {
            GameRegistry.registerItem(item, item.getUnlocalizedName());
        }
    }

    /**
     * Registers all TileEntities.
     */
    public void initTileEntities() {
        GameRegistry.registerTileEntity(TileEntityTerminalComputer.class, TileEntityTerminalComputer.NAME);
        GameRegistry.registerTileEntity(TileEntityRobot.class, TileEntityRobot.NAME);
    }

    @SuppressWarnings("UnusedParameters")
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        instance = this;

        Blocks.init();
        Items.init();
        initTileEntities();

        ChannelHandler.init();

        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());

        eventListener = new JSEventListener();
        FMLCommonHandler.instance().bus().register(eventListener);

        proxy.registerRenderers();
    }
}
