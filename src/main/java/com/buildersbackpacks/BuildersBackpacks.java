package com.buildersbackpacks;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.buildersbackpacks.config.GeneralModConfig;
import com.buildersbackpacks.events.ClientEventHandler;
import com.buildersbackpacks.gui.GuiBuildersBackpack;
import com.buildersbackpacks.gui.GuiInit;
import com.buildersbackpacks.gui.container.ContainerInit;
import com.buildersbackpacks.item.ItemBuildersBackpack;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ForgeConfig.Server;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ObjectHolder;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("buildersbackpacks")
public class BuildersBackpacks
{
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "buildersbackpacks";
    
    @ObjectHolder(MOD_ID + ":builders_backpack")
    public static final ItemBuildersBackpack BUILDERS_BACKPACK = null;
    
    public static final GeneralModConfig CONFIG = new ForgeConfigSpec.Builder().configure(GeneralModConfig::new).getLeft();

    public BuildersBackpacks() {
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
        	FMLJavaModLoadingContext.get().getModEventBus().register(GuiInit.class);
        	MinecraftForge.EVENT_BUS.register(ClientEventHandler.class);
        });
        
        
    }

    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> itemRegistryEvent) {
            itemRegistryEvent.getRegistry().register(new ItemBuildersBackpack().setRegistryName(new ResourceLocation(MOD_ID, "builders_backpack")));
        }
    }
}
