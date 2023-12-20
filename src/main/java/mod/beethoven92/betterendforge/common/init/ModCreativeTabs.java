package mod.beethoven92.betterendforge.common.init;

import mod.beethoven92.betterendforge.BetterEnd;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

import java.util.function.Supplier;


public class ModCreativeTabs 
{
	public static final BetterEndCreativeTab CREATIVE_TAB = 
			new BetterEndCreativeTab(BetterEnd.MOD_ID, () -> new ItemStack(ModBlocks.ETERNAL_PEDESTAL.get()));
	
	public static class BetterEndCreativeTab extends ItemGroup
    {
    	private final Supplier<ItemStack> icon;
    	
		public BetterEndCreativeTab(String label, Supplier<ItemStack> iconSupplier) 
		{
			super(label);
			icon = iconSupplier;
		}

		@Override
		public ItemStack createIcon() 
		{
			return icon.get();
		}
    }
}
