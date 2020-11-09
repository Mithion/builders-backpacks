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
        m.keyboardListener.enableRepeatEvents(false);
        while (KeybindInit.backpackUIOpen.isPressed())
        {
        }
    }
	
	@SubscribeEvent
    public static void handleKeys(TickEvent.ClientTickEvent ev)
    {
        Minecraft mc = Minecraft.getInstance();

        if (mc.currentScreen == null)
        {
            boolean toolMenuKeyIsDown = KeybindInit.backpackUIOpen.isKeyDown();
            if (toolMenuKeyIsDown && !toolMenuKeyWasDown)
            {
                while (KeybindInit.backpackUIOpen.isPressed())
                {
                    if (mc.currentScreen == null)
                    {
                        ItemStack inHand = mc.player.getHeldItemMainhand();
                        if (inHand.getItem() instanceof ItemBuildersBackpack)
                        {
                            mc.displayGuiScreen(new RadialMenuScreen());
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
        if (keybind.isInvalid())
            return false;        
        
        boolean isDown = false;
        switch (keybind.getKey().getType())
        {
            case KEYSYM:
                isDown = InputMappings.isKeyDown(Minecraft.getInstance().getMainWindow().getHandle(), keybind.getKey().getKeyCode());
                break;
            case MOUSE:
                isDown = GLFW.glfwGetMouseButton(Minecraft.getInstance().getMainWindow().getHandle(), keybind.getKey().getKeyCode()) == GLFW.GLFW_PRESS;
                break;
        }
        return isDown && keybind.getKeyConflictContext().isActive() && keybind.getKeyModifier().isActive(keybind.getKeyConflictContext());
    }
}
