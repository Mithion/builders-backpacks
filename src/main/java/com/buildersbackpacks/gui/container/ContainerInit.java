package com.buildersbackpacks.gui.container;

import com.buildersbackpacks.BuildersBackpacks;

import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid=BuildersBackpacks.MOD_ID, bus = Bus.MOD)
public class ContainerInit {
	
	static final String BUILDERS_BACKPACK_ID = BuildersBackpacks.MOD_ID + ":" + "builders_backpack";
	
	@ObjectHolder(BUILDERS_BACKPACK_ID)
    public static final ContainerType<ContainerBuildersBackpack> BUILDERS_BACKPACK = null;
	
	@SubscribeEvent
    public static void registerContainers(
        final RegistryEvent.Register<ContainerType<?>> event) {
        IForgeRegistry<ContainerType<?>> r = event.getRegistry();

        r.register(new ContainerType<>(ContainerBuildersBackpack::new).setRegistryName(BUILDERS_BACKPACK_ID));
    }
}
