package com.github.Redux.iceandfire.event;

import com.github.Redux.iceandfire.IceAndFire;
import com.github.Redux.iceandfire.IceAndFireConfig;
import com.github.Redux.iceandfire.block.*;
import com.github.Redux.iceandfire.entity.EntityDragonBase;
import com.github.Redux.iceandfire.entity.tile.TileEntityGhostChest;
import com.github.Redux.iceandfire.entity.tile.TileEntitySpawnerBase;
import com.github.Redux.iceandfire.integration.CompatLoadUtil;
import com.github.Redux.iceandfire.integration.VariedCommoditiesCompat;
import com.github.Redux.iceandfire.item.IafItemRegistry;
import com.github.Redux.iceandfire.item.ItemGhostSword;
import com.github.Redux.iceandfire.item.ItemTideTrident;
import com.github.Redux.iceandfire.message.MessageSwingGhostSword;
import com.github.Redux.iceandfire.structures.WorldGenLightningDragonCave;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.conditions.RandomChance;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import static net.minecraft.item.ItemMonsterPlacer.getNamedIdFrom;
/** DragonBlockHandler — Dragon Block Handler */


public class DragonBlockHandler {

	@SubscribeEvent
	public void onPlayerRightClick(PlayerInteractEvent.RightClickBlock event) {
		if (event.getWorld().getBlockState(event.getPos()).getBlock() instanceof net.minecraft.block.BlockCauldron) return;
		IBlockState state = event.getWorld().getBlockState(event.getPos());
		Block block = state.getBlock();
		BlockPos pos = event.getPos();
		World world = event.getWorld();
		EntityPlayer player = event.getEntityPlayer();

		if (event.getEntityPlayer() != null && block instanceof BlockChest) {
			float dist = IceAndFireConfig.DRAGON_SETTINGS.dragonGoldSearchLength;
			java.util.List<Entity> list = event.getWorld().getEntitiesWithinAABBExcludingEntity(player, player.getEntityBoundingBox().expand(dist, dist, dist));
			list.sort(new net.minecraft.entity.ai.EntityAINearestAttackableTarget.Sorter(player));
			if (!list.isEmpty()) {
				for (Entity entity : list) {
					if (entity instanceof EntityDragonBase) {
						EntityDragonBase dragon = (EntityDragonBase) entity;
						if (!dragon.isTamed() && !dragon.isModelDead() && !dragon.isOwner(player) && !player.capabilities.isCreativeMode) {
							dragon.setSleeping(false);
							dragon.setSitting(false);
							dragon.setAttackTarget(player);
						}
					}
				}
			}
		}
		TileEntity tileEntity = event.getWorld().getTileEntity(pos);
		if (tileEntity instanceof TileEntityGhostChest && block instanceof BlockGhostChest && !player.isSpectator()) {
			if (!world.getBlockState(pos.up()).doesSideBlockChestOpening(world, pos.up(), EnumFacing.DOWN)) {
				((TileEntityGhostChest) tileEntity).checkSpawn(player);
			}
		}
		ItemStack stack = event.getItemStack();
		if (!stack.isEmpty() && stack.getItem() instanceof ItemMonsterPlacer && player.isCreative() && (block instanceof BlockDreadSpawner || block instanceof BlockMonsterSpawner)) {
			if (tileEntity instanceof TileEntitySpawnerBase) {
				MobSpawnerBaseLogic mobSpawnerBaseLogic = ((TileEntitySpawnerBase) tileEntity).getSpawnerBaseLogic();
				mobSpawnerBaseLogic.setEntityId(getNamedIdFrom(stack));
				tileEntity.markDirty();
				event.getWorld().notifyBlockUpdate(pos, state, state, 3);
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public void onPlayerLeftClick(PlayerInteractEvent.LeftClickEmpty event) {
		ItemStack stack = event.getItemStack();
		if (!stack.isEmpty() && stack.getItem() == IafItemRegistry.ghost_sword) {
			IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageSwingGhostSword());
		}
	}

	@SubscribeEvent
	public void onPlayerLeftClick(PlayerInteractEvent.LeftClickBlock event) {
		if (event.getEntityPlayer() != null && event.getEntityPlayer().capabilities.isCreativeMode) {
			return;
		}
		IBlockState state = event.getWorld().getBlockState(event.getPos());
		if (IDreadBlock.isIndestructible(state)) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void onBreakBlock(BlockEvent.BreakEvent event) {
		if (event.getPlayer() == null) {
			return;
		}
		IBlockState state = event.getState();
		Block block = state.getBlock();
		EntityPlayer player = event.getPlayer();
		World world = event.getWorld();
		BlockPos pos = event.getPos();
		TileEntity tileEntity = world.getTileEntity(pos);
		if (block == IafBlockRegistry.goldPile || block == IafBlockRegistry.silverPile || block == IafBlockRegistry.diamondPile) {
			float dist = IceAndFireConfig.DRAGON_SETTINGS.dragonGoldSearchLength;
			java.util.List<Entity> list = event.getWorld().getEntitiesWithinAABBExcludingEntity(player, player.getEntityBoundingBox().expand(dist, dist, dist));
			list.sort(new net.minecraft.entity.ai.EntityAINearestAttackableTarget.Sorter(player));
			if (!list.isEmpty()) {
				for (Entity entity : list) {
					if (entity instanceof EntityDragonBase) {
						EntityDragonBase dragon = (EntityDragonBase) entity;
						if (!dragon.isTamed() && !dragon.isModelDead() && !dragon.isOwner(player) && !player.capabilities.isCreativeMode) {
							dragon.setSleeping(false);
							dragon.setSitting(false);
							dragon.setAttackTarget(player);
						}
					}
				}
			}
		} else if (block == IafBlockRegistry.monster_spawner || block == IafBlockRegistry.dread_spawner) {
			if (tileEntity instanceof TileEntitySpawnerBase) {
				int requiredSpawnCount = ((TileEntitySpawnerBase) tileEntity).getRequiredSpawnCount();
				if (requiredSpawnCount > 0) {
					event.setCanceled(true);
				}
			}
		}
		if (tileEntity instanceof TileEntityGhostChest && block instanceof BlockGhostChest) {
			((TileEntityGhostChest) tileEntity).checkSpawn(player);
		}
		if (player == null || !player.capabilities.isCreativeMode) {
			if (IDreadBlock.isIndestructible(state)) {
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public void onLivingDestroyBlock(net.minecraftforge.event.entity.living.LivingDestroyBlockEvent event) {
		IBlockState state = event.getState();
		if (IDreadBlock.isIndestructible(state)) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void onEnderTeleport(net.minecraftforge.event.entity.living.EnderTeleportEvent event) {
		BlockPos pos = new BlockPos(event.getTargetX(), event.getTargetY(), event.getTargetZ());
		if (IDreadBlock.isBlockInsideMausoleum(event.getEntity().world, pos)) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void onChestGenerated(LootTableLoadEvent event) {
		final ResourceLocation eventName = event.getName();
		final boolean baseConditionSet = eventName.equals(net.minecraft.world.storage.loot.LootTableList.CHESTS_SIMPLE_DUNGEON)
				|| eventName.equals(net.minecraft.world.storage.loot.LootTableList.CHESTS_ABANDONED_MINESHAFT)
				|| eventName.equals(net.minecraft.world.storage.loot.LootTableList.CHESTS_DESERT_PYRAMID)
				|| eventName.equals(net.minecraft.world.storage.loot.LootTableList.CHESTS_JUNGLE_TEMPLE)
				|| eventName.equals(net.minecraft.world.storage.loot.LootTableList.CHESTS_STRONGHOLD_CORRIDOR)
				|| eventName.equals(net.minecraft.world.storage.loot.LootTableList.CHESTS_STRONGHOLD_CROSSING);
		final boolean copperConditionSet = baseConditionSet
				|| eventName.equals(net.minecraft.world.storage.loot.LootTableList.CHESTS_IGLOO_CHEST)
				|| eventName.equals(net.minecraft.world.storage.loot.LootTableList.CHESTS_WOODLAND_MANSION)
				|| eventName.equals(net.minecraft.world.storage.loot.LootTableList.CHESTS_VILLAGE_BLACKSMITH);

		if (baseConditionSet) {
			LootCondition chance = new RandomChance(0.35f);
			LootEntryItem item = new LootEntryItem(IafItemRegistry.manuscript, 20, 5, new LootFunction[0], new LootCondition[0], "iceandfire:manuscript");
			LootPool pool = new LootPool(new LootEntry[]{item}, new LootCondition[]{chance}, new RandomValueRange(1, 4), new RandomValueRange(0, 3), "iaf_manuscript");
			event.getTable().addPool(pool);
		}
		if (copperConditionSet && IceAndFireConfig.WORLDGEN.generateCopperOre) {
			LootCondition chance = new RandomChance(0.6f);
			LootEntryItem ingot = new LootEntryItem(IafItemRegistry.copperIngot, 10, 14, new LootFunction[0], new LootCondition[0], "iceandfire:copper_ingot");
			LootPool pool = new LootPool(new LootEntry[]{ingot}, new LootCondition[]{chance}, new RandomValueRange(1, 3), new RandomValueRange(0, 3), "iaf_copper");
			event.getTable().addPool(pool);
		}
		if (eventName.equals(WorldGenLightningDragonCave.LIGHTNINGDRAGON_CHEST) || eventName.equals(WorldGenLightningDragonCave.LIGHTNINGDRAGON_MALE_CHEST)) {
			LootPool pool = event.getTable().getPool("lightning_dragon_cave");
			if (pool != null) {
				Item nugget = IafItemRegistry.copperNugget;
				Item ingot = IafItemRegistry.copperIngot;
				Item sword = IafItemRegistry.copper_sword;
				Item helmet = IafItemRegistry.copper_helmet;
				Item chestplate = IafItemRegistry.copper_chestplate;
				Item leggings = IafItemRegistry.copper_leggings;
				Item boots = IafItemRegistry.copper_boots;
				if (CompatLoadUtil.isVariedCommoditiesLoaded()) {
					nugget = VariedCommoditiesCompat.getDiamondCoin();
					ingot = Items.DIAMOND;
					sword = Items.DIAMOND_SWORD;
					helmet = Items.DIAMOND_HELMET;
					chestplate = Items.DIAMOND_CHESTPLATE;
					leggings = Items.DIAMOND_LEGGINGS;
					boots = Items.DIAMOND_BOOTS;
				}
				pool.addEntry(new LootEntryItem(nugget, 16, 0, new LootFunction[] {new SetCount(new LootCondition[0], new RandomValueRange(1, 16))}, new LootCondition[0], "nugget"));
				pool.addEntry(new LootEntryItem(ingot, 10, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1, 10))}, new LootCondition[0], "ingot"));
				pool.addEntry(new LootEntryItem(sword, 5, 0, new LootFunction[0], new LootCondition[0], "sword"));
				pool.addEntry(new LootEntryItem(helmet, 5, 0, new LootFunction[0], new LootCondition[0], "helmet"));
				pool.addEntry(new LootEntryItem(chestplate, 5, 0, new LootFunction[0], new LootCondition[0], "chestplate"));
				pool.addEntry(new LootEntryItem(leggings, 5, 0, new LootFunction[0], new LootCondition[0], "leggings"));
				pool.addEntry(new LootEntryItem(boots, 5, 0, new LootFunction[0], new LootCondition[0], "boots"));
			}
		}
	}

	@SubscribeEvent
	public void onPlayerPickup(EntityItemPickupEvent event) {
		ItemStack pickedUpStack = event.getItem().getItem();
		EntityPlayer player = event.getEntityPlayer();

		if (pickedUpStack.getItem() instanceof ItemTideTrident && !ItemTideTrident.isOriginal(pickedUpStack)) {
			for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
				ItemStack slotStack = player.inventory.getStackInSlot(i);
				if (slotStack.getItem() instanceof ItemTideTrident && ItemTideTrident.hasMatchingUUID(slotStack, pickedUpStack)) {
					boolean empty = ItemTideTrident.isEmpty(slotStack);
					if (empty) {
						int itemDamage = slotStack.getItemDamage() + 1;
						if (itemDamage > slotStack.getMaxDamage()) {
							player.renderBrokenItemStack(slotStack);
							slotStack.setCount(0);
						} else {
							ItemTideTrident.setEmpty(slotStack, ItemTideTrident.isEmpty(pickedUpStack));
							slotStack.setItemDamage(itemDamage);
						}
						player.onItemPickup(event.getItem(), 1);
						player.world.playSound(null, event.getItem().posX, event.getItem().posY, event.getItem().posZ, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, (player.world.rand.nextFloat() - player.world.rand.nextFloat()) * 0.7F + 0.0F);
						pickedUpStack.setCount(0);
						return;
					}
				}
			}
		}
	}

	public static void onSwingGhostSword(final EntityPlayer playerEntity, final ItemStack stack) {
		if (!stack.isEmpty() && stack.getItem() == IafItemRegistry.ghost_sword) {
			ItemGhostSword.spawnGhostSwordEntity(stack, playerEntity);
		}
	}
}
