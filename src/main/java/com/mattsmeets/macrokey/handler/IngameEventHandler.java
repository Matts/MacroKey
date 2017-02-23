package com.mattsmeets.macrokey.handler;

import com.mattsmeets.macrokey.MacroKey;
import com.mattsmeets.macrokey.gui.GuiManageKeybindings;
import com.mattsmeets.macrokey.language.ParseCommand;
import com.mattsmeets.macrokey.object.BoundKey;
import com.mattsmeets.macrokey.object.Layer;
import com.mattsmeets.macrokey.object.ToBeExecutedCommand;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

public class IngameEventHandler implements IGuiHandler {

	public static final int KEYBINDINGS_GUI = 5002;

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == KEYBINDINGS_GUI)
			return new GuiManageKeybindings(Minecraft.getMinecraft().currentScreen, Minecraft.getMinecraft().gameSettings);
		return null;
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent(priority= EventPriority.NORMAL, receiveCanceled=true)
	public void onEvent(InputEvent.KeyInputEvent event)
	{
		KeyBinding[] keyBindings = MacroKey.forgeKeybindings;

		if (keyBindings[0].isPressed())
		{
			Minecraft.getMinecraft().player.openGui(MacroKey.instance, 5002, Minecraft.getMinecraft().world, (int) Minecraft.getMinecraft().player.posX, (int) Minecraft.getMinecraft().player.posY, (int) Minecraft.getMinecraft().player.posZ);
		}

		for(int i = 0; i< Layer.getActiveKeys().size(); i++) {
			if(Keyboard.isKeyDown(Layer.getActiveKeys().get(i).getKeyCode()) & (!Layer.getActiveKeys().get(i).isPressed())){
				Layer.getActiveKeys().get(i).setPressed(true);

				String command = Layer.getActiveKeys().get(i).getCommand();
				if(command.contains("exec") || (command.contains("sleep") & command.contains(";"))) {
					ParseCommand.parse(command);
				}else{
					if (net.minecraftforge.client.ClientCommandHandler.instance.executeCommand(Minecraft.getMinecraft().player, command) != 0) {return;}
					Minecraft.getMinecraft().player.sendChatMessage(command);
				}

			}
			if(!Keyboard.isKeyDown(Layer.getActiveKeys().get(i).getKeyCode())){
				Layer.getActiveKeys().get(i).setPressed(false);
			}
		}
	}



	public static int ticks;
	public static List<ToBeExecutedCommand> keyList;

	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event) {
		if(keyList==null){
			keyList = new ArrayList<ToBeExecutedCommand>();
		}
		if (event.phase == TickEvent.Phase.START) {
			if(Minecraft.getMinecraft().player != null){
				if(keyList.size()>0){
					for(ToBeExecutedCommand key : keyList){
						if(ticks>=key.getTicks()){
							if (net.minecraftforge.client.ClientCommandHandler.instance.executeCommand(Minecraft.getMinecraft().player, key.getCommand()) != 0) {
								keyList.remove(key);
								break;
							}
							Minecraft.getMinecraft().player.sendChatMessage(key.getCommand());

							break;
						}
					}
				}
				for(BoundKey key : Layer.getActiveKeys()){
					if(key.isPressed() && key.isRepeat() && !(key.getCommand().contains("exec") || (key.getCommand().contains("sleep") & key.getCommand().contains(";")))){
						if (net.minecraftforge.client.ClientCommandHandler.instance.executeCommand(Minecraft.getMinecraft().player, key.getCommand()) != 0) {return;}
						Minecraft.getMinecraft().player.sendChatMessage(key.getCommand());
					}
				}

				ticks++;
			}else{
				keyList = null;
			}
		}
	}

}
