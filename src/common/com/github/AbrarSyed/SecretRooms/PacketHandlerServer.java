package com.github.AbrarSyed.SecretRooms;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.ModLoader;
import net.minecraft.src.NetworkManager;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.World;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.server.FMLServerHandler;

/**
 * @author AbrarSyed
 */
public class PacketHandlerServer implements IPacketHandler {

	@Override
	public void onPacketData(NetworkManager manager, Packet250CustomPayload packet, Player useless)
	{
		String channel = packet.channel;
		byte[] data = packet.data;
		
		EntityPlayer player = (EntityPlayer) useless;
		World world = player.worldObj;
		
    	if (channel.equals("SRM-TE-Camo"))
    	{

    		if (data.length <= 0)
    		{
    			return;
    		}

    		DataInputStream dataStream = new DataInputStream(new ByteArrayInputStream(data));
    		int coords[] = new int[3];
    		int texture = -1;
    		boolean forged = false;
    		String texturePath = null;
    		try
    		{
    			for(int i = 0; i < 3; i++)
    				coords[i] = dataStream.readInt();

    			texture = dataStream.readInt();
    			forged = dataStream.readBoolean();

    			if (forged)
    			{
    				int texturePathLength = dataStream.readInt();

    				char[] string = new char[texturePathLength];

    				for (int i = 0; i < texturePathLength; i++)
    				{
    					string[i] = dataStream.readChar();
    				}

    				texturePath = new String(string);
    			}
    		}
    		catch(Exception e)
    		{
    			e.printStackTrace();
    		}
    		
    		//System.out.println("SERVER-RECIEVED: "+texture);

    		if (texture == -1) return;

    		TileEntityCamo entity = (TileEntityCamo) world.getBlockTileEntity(coords[0], coords[1], coords[2]);

    		if (entity == null)
    			return;

    		entity.setTexture(texture);

    		if (forged)
    			entity.setTexturePath(texturePath);

    		world.markBlockAsNeedsUpdate(coords[0], coords[1], coords[2]);
    		world.updateTileEntityChunkAndDoNothing(coords[0], coords[1], coords[2], entity);
    		FMLCommonHandler.instance().getSidedDelegate().getServer().getConfigurationManager().sendPacketToAllPlayers(entity.getDescriptionPacket());
    	}
	}

}
