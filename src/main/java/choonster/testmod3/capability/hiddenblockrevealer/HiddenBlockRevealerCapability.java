package choonster.testmod3.capability.hiddenblockrevealer;

import choonster.testmod3.TestMod3;
import choonster.testmod3.api.capability.hiddenblockrevealer.IHiddenBlockRevealer;
import choonster.testmod3.capability.CapabilityContainerListenerManager;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.LazyOptional;

import static choonster.testmod3.util.InjectionUtil.Null;

/**
 * Items with this capability reveal hidden blocks.
 *
 * @author Choonster
 */
public final class HiddenBlockRevealerCapability {
	/**
	 * The {@link Capability} instance.
	 */
	@CapabilityInject(IHiddenBlockRevealer.class)
	public static final Capability<IHiddenBlockRevealer> HIDDEN_BLOCK_REVEALER_CAPABILITY = Null();

	/**
	 * The default {@link Direction} to use for this capability.
	 */
	public static final Direction DEFAULT_FACING = null;

	/**
	 * The ID of this capability.
	 */
	private static final ResourceLocation ID = new ResourceLocation(TestMod3.MODID, "hidden_block_revealer");

	public static void register() {
		CapabilityManager.INSTANCE.register(IHiddenBlockRevealer.class, new Capability.IStorage<IHiddenBlockRevealer>() {
					@Override
					public INBT writeNBT(final Capability<IHiddenBlockRevealer> capability, final IHiddenBlockRevealer instance, final Direction side) {
						final CompoundNBT tagCompound = new CompoundNBT();
						tagCompound.putBoolean("RevealHiddenBlocks", instance.revealHiddenBlocks());
						return tagCompound;
					}

					@Override
					public void readNBT(final Capability<IHiddenBlockRevealer> capability, final IHiddenBlockRevealer instance, final Direction side, final INBT nbt) {
						final CompoundNBT tagCompound = (CompoundNBT) nbt;
						instance.setRevealHiddenBlocks(tagCompound.getBoolean("RevealHiddenBlocks"));
					}
				}, HiddenBlockRevealer::new
		);

		CapabilityContainerListenerManager.registerListenerFactory(HiddenBlockRevealerContainerListener::new);
	}

	/**
	 * Get the {@link IHiddenBlockRevealer} for the {@link ItemStack}, if any.
	 *
	 * @param stack The ItemStack
	 * @return A lazy optional containing the IHiddenBlockRevealer for the {@link ItemStack}, if any
	 */
	public static LazyOptional<IHiddenBlockRevealer> getHiddenBlockRevealer(final ItemStack stack) {
		return stack.getCapability(HIDDEN_BLOCK_REVEALER_CAPABILITY, DEFAULT_FACING);
	}

	/**
	 * Toggle the reveal state of the player's held {@link IHiddenBlockRevealer}.
	 *
	 * @param stack The ItemStack
	 * @return The new reveal state, or empty if there is no IHiddenBlockRevealer
	 */
	public static LazyOptional<Boolean> toggleRevealHiddenBlocks(final ItemStack stack) {
		return getHiddenBlockRevealer(stack)
				.map(hiddenBlockRevealer -> {
					final boolean revealHiddenBlocks = !hiddenBlockRevealer.revealHiddenBlocks();
					hiddenBlockRevealer.setRevealHiddenBlocks(revealHiddenBlocks);

					return revealHiddenBlocks;
				});
	}

	/**
	 * {@link IItemPropertyGetter} to get whether hidden blocks are being revealed.
	 */
	public static class RevealHiddenBlocksGetter {
		/**
		 * The ID of this getter.
		 */
		private static final ResourceLocation ID = new ResourceLocation(TestMod3.MODID, "reveal_hidden_blocks");

		/**
		 * The getter.
		 */
		private static final IItemPropertyGetter GETTER = (stack, worldIn, entityIn) ->
				HiddenBlockRevealerCapability.getHiddenBlockRevealer(stack)
						.map(hiddenBlockRevealer -> hiddenBlockRevealer.revealHiddenBlocks() ? 1 : 0)
						.orElse(0);

		/**
		 * Add this getter to an {@link Item}.
		 *
		 * @param item The item
		 */
		public static void addToItem(final Item item) {
			item.addPropertyOverride(ID, GETTER);
		}
	}
}