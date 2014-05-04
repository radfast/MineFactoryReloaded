package powercrystals.minefactoryreloaded.tile.machine;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import cofh.util.position.Area;
import cofh.util.position.BlockPosition;
import powercrystals.minefactoryreloaded.core.HarvestAreaManager;
import powercrystals.minefactoryreloaded.gui.client.GuiFactoryInventory;
import powercrystals.minefactoryreloaded.gui.client.GuiFactoryPowered;
import powercrystals.minefactoryreloaded.gui.container.ContainerFactoryPowered;
import powercrystals.minefactoryreloaded.setup.Machine;
import powercrystals.minefactoryreloaded.tile.base.TileEntityFactoryPowered;

public class TileEntityFisher extends TileEntityFactoryPowered
{
	private boolean _isJammed;
	
	public TileEntityFisher()
	{
		super(Machine.Fisher);
		_areaManager = new HarvestAreaManager(this, 1, 0, 0);
		setManageSolids(true);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public GuiFactoryInventory getGui(InventoryPlayer inventoryPlayer)
	{
		return new GuiFactoryPowered(getContainer(inventoryPlayer), this);
	}
	
	@Override
	public ContainerFactoryPowered getContainer(InventoryPlayer inventoryPlayer)
	{
		return new ContainerFactoryPowered(this, inventoryPlayer);
	}
	
	@Override
	public ForgeDirection getDirectionFacing()
	{
		return ForgeDirection.DOWN;
	}
	
	@Override
	public boolean activateMachine()
	{
		if(_isJammed || worldObj.getWorldTime() % 137 == 0)
		{
			Area fishingHole = _areaManager.getHarvestArea();
			for(BlockPosition bp: fishingHole.getPositionsBottomFirst())
			{
				if(worldObj.getBlockId(bp.x, bp.y, bp.z) != Block.waterStill.blockID)
				{
					_isJammed = true;
					setIdleTicks(getIdleTicksMax());
					return false;
				}
			}
		}
		
		_isJammed = false;
		
		setWorkDone(getWorkDone() + 1);
		
		if(getWorkDone() > getWorkMax())
		{
			doDrop(new ItemStack(Item.fishRaw));
			setWorkDone(0);
		}
		return true;
	}
	
	@Override
	public ForgeDirection getDropDirection()
	{
		return ForgeDirection.UP;
	}
	
	@Override
	public int getWorkMax()
	{
		return 900;
	}
	
	@Override
	public int getIdleTicksMax()
	{
		return 200;
	}
	
	@Override
	public int getSizeInventory()
	{
		return 0;
	}
}
