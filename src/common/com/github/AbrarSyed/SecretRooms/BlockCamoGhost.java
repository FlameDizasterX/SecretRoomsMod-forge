package com.github.AbrarSyed.SecretRooms;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.World;

/**
 * @author AbrarSyed
 */
public class BlockCamoGhost extends BlockCamoFull
{

	protected BlockCamoGhost(int par1)
	{
		super(par1, Material.wood);
        this.setHardness(1.5F);
        this.setStepSound(Block.soundWoodFootstep);
		this.setCreativeTab(CreativeTabs.tabBlock);
	}
    
    @Override
    public void addCreativeItems(ArrayList itemList)
    {
    	itemList.add(new ItemStack(this));
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
	@Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }
    
    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
    	int metadata = par1World.getBlockMetadata(par2, par3, par4);
    	
    	if (metadata == 0)
    		return null;
    	else
    		return AxisAlignedBB.getBoundingBox((double)par2 + minX, (double)par3 + minY, (double)par4 + minZ, (double)par2 + maxX, (double)par3 + maxY, (double)par4 + maxZ);
    }
    
    @Override
    public void onNeighborBlockChange(World world, int i, int j, int k, int l)
    {
        if (l > 0 && Block.blocksList[l].canProvidePower())
        {
            world.scheduleBlockUpdate(i, j, k, blockID, 0);
        }
    }
    
    @Override
    public void updateTick(World world, int i, int j, int k, Random random)
    {
        boolean flag = !world.isRemote && (world.isBlockIndirectlyGettingPowered(i, j, k) || world.isBlockIndirectlyGettingPowered(i, j + 1, k));

        if (flag)
        {
        	world.setBlockMetadata(i, j, k, 1);
        }
        else
        {
        	world.setBlockMetadata(i, j, k, 0);
        }
    }
}
