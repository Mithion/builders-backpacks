package com.buildersbackpacks.events;

import org.lwjgl.glfw.GLFW;

import com.buildersbackpacks.gui.RadialMenuScreen;
import com.buildersbackpacks.item.ItemBuildersBackpack;
import com.buildersbackpacks.keybind.KeybindInit;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ClientEventHandler {
	private static boolean toolMenuKeyWasDown;
	public static boolean shiftPressed = false;
	
	public static void wipeOpen()
    {
		Minecraft m = Minecraft.getInstance();
        m.keyboardHandler.setSendRepeatsToGui(false);
        while (KeybindInit.backpackUIOpen.isDown())
        {
        }
    }
	
	@SubscribeEvent
    public static void handleKeys(TickEvent.ClientTickEvent ev)
    {
        Minecraft mc = Minecraft.getInstance();

        if (mc.screen == null)
        {
            boolean toolMenuKeyIsDown = KeybindInit.backpackUIOpen.consumeClick();
            if (toolMenuKeyIsDown && !toolMenuKeyWasDown)
            {
                while (KeybindInit.backpackUIOpen.isDown())
                {
                    if (mc.screen == null)
                    {
                        ItemStack inHand = mc.player.getMainHandItem();
                        if (inHand.getItem() instanceof ItemBuildersBackpack)
                        {
                            mc.setScreen(new RadialMenuScreen());
                        }
                    }
                }
            }
            toolMenuKeyWasDown = toolMenuKeyIsDown;
        }
        else
        {
            toolMenuKeyWasDown = true;
        }
    }

	@SuppressWarnings("incomplete-switch")
	public static boolean isKeyDown(KeyBinding keybind)
    {
        if (keybind.isUnbound())
            return false;        
        
        boolean isDown = false;
        switch (keybind.getKey().getType())
        {
            case KEYSYM:
                isDown = InputMappings.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), keybind.getKey().getValue());
                break;
            case MOUSE:
                isDown = GLFW.glfwGetMouseButton(Minecraft.getInstance().getWindow().getWindow(), keybind.getKey().getValue()) == GLFW.GLFW_PRESS;
                break;
        }
        return isDown && keybind.getKeyConflictContext().isActive() && keybind.getKeyModifier().isActive(keybind.getKeyConflictContext());
    }
}
