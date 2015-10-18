package com.choonster.testmod3.client.gui;

import com.choonster.testmod3.command.SurvivalCommandBlockLogic;
import com.choonster.testmod3.tileentity.TileEntitySurvivalCommandBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import static com.choonster.testmod3.client.gui.GuiIDs.SURVIVAL_COMMAND_BLOCK;
import static com.choonster.testmod3.client.gui.GuiIDs.SURVIVAL_COMMAND_BLOCK_MINECART;

public class GuiHandler implements IGuiHandler {
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));

		switch (ID) {
			case SURVIVAL_COMMAND_BLOCK:
			case SURVIVAL_COMMAND_BLOCK_MINECART:
				return null;

			default:
				return null;
		}
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));

		switch (ID) {
			case SURVIVAL_COMMAND_BLOCK:
				SurvivalCommandBlockLogic survivalCommandBlockLogic = ((TileEntitySurvivalCommandBlock) tileEntity).getCommandBlockLogic();
				return new GuiSurvivalCommandBlock(survivalCommandBlockLogic);

			case SURVIVAL_COMMAND_BLOCK_MINECART:
				// Get SurvivalCommandBlockLogic from Minecart using x as entityID (NYI)
				return null;

			default:
				return null;
		}
	}
}