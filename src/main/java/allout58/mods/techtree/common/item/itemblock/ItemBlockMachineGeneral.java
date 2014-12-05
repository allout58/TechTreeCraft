package allout58.mods.bigfactories.common.item.itemblock;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

/**
 * Created by James Hollowell on 8/2/2014.
 */
public class ItemBlockMachineGeneral extends ItemBlock
{
    public ItemBlockMachineGeneral(Block p_i45328_1_)
    {
        super(p_i45328_1_);
        setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack)
    {
        String name = "";
        switch (itemstack.getItemDamage())
        {
            case 0:
            {
                name = "input";
                break;
            }
            case 1:
            {
                name = "output";
                break;
            }
            default:
                name = "nothing";
        }
        return getUnlocalizedName() + "." + name;
    }

    @Override
    public int getMetadata(int meta)
    {
        return meta;
    }
}
