package com.github.Redux.iceandfire.entity.explosion;

import com.github.Redux.iceandfire.IceAndFire;
import com.github.Redux.iceandfire.IceAndFireConfig;
import com.github.Redux.iceandfire.api.IEntityEffectCapability;
import com.github.Redux.iceandfire.api.InFCapabilities;
import com.github.Redux.iceandfire.block.BlockFallingReturningState;
import com.github.Redux.iceandfire.block.BlockPath;
import com.github.Redux.iceandfire.block.BlockReturningState;
import com.github.Redux.iceandfire.block.IafBlockRegistry;
import com.github.Redux.iceandfire.entity.EntityDragonBase;
import com.github.Redux.iceandfire.entity.projectile.EntityDragonFire;
import com.github.Redux.iceandfire.entity.projectile.EntityDragonFireCharge;
import com.github.Redux.iceandfire.entity.projectile.EntityDragonIce;
import com.github.Redux.iceandfire.entity.projectile.EntityDragonIceCharge;
import com.github.Redux.iceandfire.entity.projectile.EntityDragonLightning;
import com.github.Redux.iceandfire.entity.projectile.EntityDragonLightningCharge;
import com.github.Redux.iceandfire.entity.tile.TileEntityDragonforgeInput;
import com.github.Redux.iceandfire.entity.util.DragonUtils;
import com.github.Redux.iceandfire.enums.EnumDragonType;
import com.github.Redux.iceandfire.enums.EnumParticle;
import com.github.Redux.iceandfire.integration.LycanitesCompat;
import com.github.Redux.iceandfire.message.MessageParticleFX;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import java.util.*;

/**
 * Explosión única para los 3 tipos de dragón.
 * Reemplaza FireExplosion/IceExplosion/LightningExplosion.
 * El EnumDragonType determina daño, partículas, transformación de bloques y efectos secundarios.
 */
public class DragonExplosion extends Explosion {

	private final EnumDragonType dragonType;
	private final boolean isSmoking;
	private final Random explosionRNG;
	private final World worldObj;
	private final double explosionX;
	private final double explosionY;
	private final double explosionZ;
	private final Entity exploder;
	private final float explosionSize;
	private final List<BlockPos> affectedBlockPositions;
	private final Map<EntityPlayer, Vec3d> playerKnockbackMap;
	private final Vec3d position;
	private final boolean dragonGriefing;

	public DragonExplosion(EnumDragonType dragonType, World world, Entity entity, double x, double y, double z, float size, boolean smoke) {
		super(world, entity, x, y, z, size, true, smoke);
		this.dragonType = dragonType;
		this.explosionRNG = new Random();
		this.affectedBlockPositions = Lists.<BlockPos>newArrayList();
		this.playerKnockbackMap = Maps.<EntityPlayer, Vec3d>newHashMap();
		this.worldObj = world;
		this.exploder = entity;
		this.explosionSize = size;
		this.explosionX = x;
		this.explosionY = y;
		this.explosionZ = z;
		this.isSmoking = smoke;
		this.position = new Vec3d(explosionX, explosionY, explosionZ);
		this.dragonGriefing = worldObj.getGameRules().getBoolean("mobGriefing") && IceAndFireConfig.DRAGON_SETTINGS.dragonGriefing != 2;
	}

	/**
	 * Fase 1: recorre una cuadrícula 16×16×16 calculando qué bloques destruir
	 * y qué entidades dañar, con resistencia de bloques y distancia.
	 * Si impacta una dragonforge, omite el daño a entidades (shouldAffectEntities=false).
	 */
	@Override
	public void doExplosionA() {
		boolean canGrief = DragonUtils.canGrief(false);
		boolean shouldAffectEntities = true;
		BlockPos.MutableBlockPos mutPos = new BlockPos.MutableBlockPos();
		Set<BlockPos> checkedTiles = new HashSet<>();
		HashMap<BlockPos, Float> resistanceMap = new HashMap<>();
		Set<BlockPos> affectedSet = new HashSet<>();
		for (int j = 0; j < 16; ++j) {
			for (int k = 0; k < 16; ++k) {
				for (int l = 0; l < 16; ++l) {
					if (j == 0 || j == 15 || k == 0 || k == 15 || l == 0 || l == 15) {
						double d0 = (float)j / 15.0F * 2.0F - 1.0F;
						double d1 = (float)k / 3.0F * 2.0F - 1.0F;
						double d2 = (float)l / 15.0F * 2.0F - 1.0F;
						double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
						d0 = d0 / d3;
						d1 = d1 / d3;
						d2 = d2 / d3;
						float f = this.explosionSize * (0.7F + this.worldObj.rand.nextFloat() * 0.6F);
						double d4 = this.explosionX;
						double d6 = this.explosionY;
						double d8 = this.explosionZ;

						for (; f > 0.0F; f -= 0.22500001F) {
							mutPos = mutPos.setPos(d4, d6, d8);

							BlockPos immutPos = null;
							if(!checkedTiles.contains(mutPos)) {
								TileEntity tileEntity = worldObj.getTileEntity(mutPos);
								if (tileEntity instanceof TileEntityDragonforgeInput) {
									((TileEntityDragonforgeInput) tileEntity).onHitWithFlame(dragonType, this.exploder);
									if (exploder == null || exploder instanceof EntityPlayer || exploder instanceof EntityDragonBase && ((EntityDragonBase) exploder).isTamed()) {
										shouldAffectEntities = false;
									}
								}
								immutPos = mutPos.toImmutable();
								checkedTiles.add(immutPos);
							}

							IBlockState iblockstate = null;
							Float resistance = resistanceMap.get(mutPos);
							if (resistance == null) {
								iblockstate = this.worldObj.getBlockState(mutPos);
								Block block = iblockstate.getBlock();
								if (block != Blocks.AIR) {
									float f2 = this.exploder != null ? this.exploder.getExplosionResistance(this, this.worldObj, mutPos, iblockstate) : block.getExplosionResistance(worldObj, mutPos, null, this);
									resistance = (f2 + 0.3F) * 0.3F;
								}
								else resistance = 0.0F;
								if (immutPos == null) immutPos = mutPos.toImmutable();
								resistanceMap.put(immutPos, resistance);
							}
							f -= resistance;

							if (f <= 0.0F) break;

							if (canGrief) {
								if (!affectedSet.contains(mutPos)) {
									if (iblockstate == null) iblockstate = this.worldObj.getBlockState(mutPos);
									Block block = iblockstate.getBlock();
									if ((this.exploder == null || this.exploder.canExplosionDestroyBlock(this, this.worldObj, mutPos, iblockstate, f)) && block.canEntityDestroy(iblockstate, this.worldObj, mutPos, this.exploder)) {
										if (immutPos == null) immutPos = mutPos.toImmutable();
										affectedSet.add(immutPos);
									}
								}
							}

							d4 += d0 * 0.30000001192092896D;
							d6 += d1 * 0.30000001192092896D;
							d8 += d2 * 0.30000001192092896D;
						}
					}
				}
			}
		}
		this.affectedBlockPositions.addAll(affectedSet);

		if (!shouldAffectEntities) {
			return;
		}

		// daño a entidades dentro del radio de explosión
		float f3 = this.explosionSize * 2.0F;
		int k1 = MathHelper.floor(this.explosionX - f3 - 1.0D);
		int l1 = MathHelper.floor(this.explosionX + f3 + 1.0D);
		int i2 = MathHelper.floor(this.explosionY - f3 - 1.0D);
		int i1 = MathHelper.floor(this.explosionY + f3 + 1.0D);
		int j2 = MathHelper.floor(this.explosionZ - f3 - 1.0D);
		int j1 = MathHelper.floor(this.explosionZ + f3 + 1.0D);
		List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this.exploder, new AxisAlignedBB(k1, i2, j2, l1, i1, j1));
		net.minecraftforge.event.ForgeEventFactory.onExplosionDetonate(this.worldObj, this, list, f3);
		Vec3d Vec3d = new Vec3d(this.explosionX, this.explosionY, this.explosionZ);

		for (Entity entity : list) {
			if (isImmuneProjectile(entity)) continue;
			if (entity.isImmuneToExplosions() || entity.isEntityEqual(exploder)) continue;
			double d12 = entity.getDistance(this.explosionX, this.explosionY, this.explosionZ) / f3;

			if (d12 <= 1.0D) {
				double d5 = entity.posX - this.explosionX;
				double d7 = entity.posY + entity.getEyeHeight() - this.explosionY;
				double d9 = entity.posZ - this.explosionZ;
				double d13 = MathHelper.sqrt(d5 * d5 + d7 * d7 + d9 * d9);
				double d14 = this.worldObj.getBlockDensity(Vec3d, entity.getEntityBoundingBox());
				double d10 = (1.0D - d12) * d14;
				if (d13 != 0.0D) {
					d5 = d5 / d13;
					d7 = d7 / d13;
					d9 = d9 / d13;
					if (exploder instanceof EntityLivingBase) {
						if (!DragonUtils.isControllingPassenger(exploder, entity)) {
							// daño reducido para aliados (1/6), completo para enemigos (1/3)
							DamageSource dmg = getDamageSource();
							int dmgConfig = getExplosionDamageConfig();
							if (DragonUtils.isOwner(entity, exploder) || DragonUtils.hasSameOwner(entity, exploder)) {
								entity.attackEntityFrom(dmg, ((float) ((int) ((d10 * d10 + d10) / 2.0D * dmgConfig * (double) f3 + 1.0D))) / 6);
							} else if (!entity.isEntityEqual(exploder)) {
								entity.attackEntityFrom(dmg, (float) ((int) ((d10 * d10 + d10) / 2.0D * dmgConfig * (double) f3 + 1.0D)) / 3);
								applyExtraEffect(entity);
							}
							if (this.exploder instanceof EntityDragonBase && entity.isDead) {
								((EntityDragonBase) this.exploder).attackDecision = true;
							}
						}
					} else {
						entity.attackEntityFrom(getDamageSource(), (float) ((int) ((d10 * d10 + d10) / 2.0D * getExplosionDamageConfig() * (double) f3 + 1.0D)) / 3);
					}
				}
				double d11 = 1.0D;

				if (entity instanceof EntityLivingBase) {
					d11 = EnchantmentProtection.getBlastDamageReduction((EntityLivingBase) entity, d10);
				}
				entity.motionX += d5 * d11;
				entity.motionY += d7 * d11;
				entity.motionZ += d9 * d11;

				if (entity instanceof EntityPlayer && !((EntityPlayer) entity).capabilities.disableDamage) {
					this.playerKnockbackMap.put((EntityPlayer) entity, new Vec3d(d5 * d10, d7 * d10, d9 * d10));
				}
			}
		}
	}

	/**
	 * Fase 2: partículas de explosión + transformación de bloques según tipo (fuego → ceniza, hielo → escarcha, relámpago → grietas)
	 */
	@Override
	public void doExplosionB(boolean spawnParticles) {
		if (this.affectedBlockPositions.isEmpty()) return;
		if (this.isSmoking) {
			List<MessageParticleFX.Particle> particles = new ArrayList<>();
			for (BlockPos blockpos : this.affectedBlockPositions) {
				if (spawnParticles && this.worldObj.rand.nextFloat() > 0.95F) {
					double d0 = blockpos.getX() + this.worldObj.rand.nextFloat();
					double d1 = blockpos.getY() + this.worldObj.rand.nextFloat();
					double d2 = blockpos.getZ() + this.worldObj.rand.nextFloat();
					double d3 = d0 - this.explosionX;
					double d4 = d1 - this.explosionY;
					double d5 = d2 - this.explosionZ;
					double d6 = MathHelper.sqrt(d3 * d3 + d4 * d4 + d5 * d5);
					d3 = d3 / d6;
					d4 = d4 / d6;
					d5 = d5 / d6;
					double d7 = 0.5D / (d6 / this.explosionSize + 0.1D);
					d7 = d7 * (this.worldObj.rand.nextFloat() * this.worldObj.rand.nextFloat() + 0.3F);
					d3 = d3 * d7;
					d4 = d4 * d7;
					d5 = d5 * d7;

					particles.add(MessageParticleFX.createParticle(d0, d1, d2, d3, d4, d5));
				}

				if (this.dragonGriefing) {
					transformBlock(blockpos);
				}
			}
			if (!particles.isEmpty() && this.exploder != null) {
				List<EnumParticle> types = getParticleTypes();
				if (dragonType == EnumDragonType.LIGHTNING && exploder instanceof EntityPlayerMP) {
					MessageParticleFX message = new MessageParticleFX(types, particles);
					IceAndFire.NETWORK_WRAPPER.sendTo(message, (EntityPlayerMP) this.exploder);
					IceAndFire.NETWORK_WRAPPER.sendToAllTracking(message, this.exploder);
				} else {
					IceAndFire.NETWORK_WRAPPER.sendToAllTracking(new MessageParticleFX(types, particles), this.exploder);
				}
			}
		}

		placePostExplosionBlocks();
	}

	private boolean isImmuneProjectile(Entity entity) {
		switch (dragonType) {
			case FIRE:
				return entity instanceof EntityDragonFire || entity instanceof EntityDragonFireCharge;
			case ICE:
				return entity instanceof EntityDragonIce || entity instanceof EntityDragonIceCharge;
			case LIGHTNING:
				return entity instanceof EntityDragonLightning || entity instanceof EntityDragonLightningCharge;
			default:
				return false;
		}
	}

	private DamageSource getDamageSource() {
		switch (dragonType) {
			case FIRE:
				return IceAndFire.dragonFire;
			case ICE:
				return IceAndFire.dragonIce;
			case LIGHTNING:
				return IceAndFire.dragonLightning;
			default:
				return IceAndFire.dragonFire;
		}
	}

	private int getExplosionDamageConfig() {
		switch (dragonType) {
			case FIRE:
				return IceAndFireConfig.DRAGON_SETTINGS.dragonFireExplosionDamage;
			case ICE:
				return IceAndFireConfig.DRAGON_SETTINGS.dragonIceExplosionDamage;
			case LIGHTNING:
				return IceAndFireConfig.DRAGON_SETTINGS.dragonLightningExplosionDamage;
			default:
				return IceAndFireConfig.DRAGON_SETTINGS.dragonFireExplosionDamage;
		}
	}

	/** Efecto secundario por tipo: fuego=ignición, hielo=ralentí, relámpago=rayo */
	private void applyExtraEffect(Entity entity) {
		switch (dragonType) {
			case FIRE:
				entity.setFire(5);
				break;
			case ICE:
				if (entity instanceof EntityLivingBase) {
					IEntityEffectCapability capability = InFCapabilities.getEntityEffectCapability((EntityLivingBase) entity);
					if (capability != null) {
						capability.setFrozen(200);
					}
				}
				break;
			case LIGHTNING:
				if (entity instanceof EntityLivingBase) {
					if (IceAndFireConfig.DRAGON_SETTINGS.lightningDragonKnockback) {
						double xRatio = exploder.posX - entity.posX;
						double zRatio = exploder.posZ - entity.posZ;
						((EntityLivingBase) entity).knockBack(entity, 0.3F, xRatio, zRatio);
					}
					if (IceAndFireConfig.DRAGON_SETTINGS.lightningDragonParalysis) {
						LycanitesCompat.applyParalysis(entity, IceAndFireConfig.DRAGON_SETTINGS.lightningDragonParalysisTicks);
					}
				}
				break;
		}
	}

	private void transformBlock(BlockPos blockpos) {
		IBlockState state = this.worldObj.getBlockState(blockpos);
		Block block = state.getBlock();
		if (block == Blocks.AIR) return;
		if (!DragonUtils.canDragonBreak(worldObj, state.getBlock(), blockpos)) return;
		Material mat = state.getMaterial();

		switch (dragonType) {
			case FIRE:
				transformFireBlock(blockpos, block, mat, state);
				break;
			case ICE:
				transformIceBlock(blockpos, block, mat, state);
				break;
			case LIGHTNING:
				transformLightningBlock(blockpos, block, mat, state);
				break;
		}
	}

	/** Convierte bloques quemados por dragón de fuego en ceniza/chared */
	private void transformFireBlock(BlockPos blockpos, Block block, Material mat, IBlockState state) {
		if (block == Blocks.GRASS_PATH) {
			worldObj.setBlockState(blockpos, IafBlockRegistry.charedGrassPath.getDefaultState().withProperty(BlockPath.REVERTS, IceAndFireConfig.DRAGON_SETTINGS.dragonAffectedBlocksRevert));
		} else if (block == Blocks.GRASS) {
			worldObj.setBlockState(blockpos, IafBlockRegistry.charedGrass.getDefaultState().withProperty(BlockReturningState.REVERTS, IceAndFireConfig.DRAGON_SETTINGS.dragonAffectedBlocksRevert));
		} else if (block instanceof BlockGrass || block instanceof BlockDirt) {
			worldObj.setBlockState(blockpos, IafBlockRegistry.charedDirt.getDefaultState().withProperty(BlockReturningState.REVERTS, IceAndFireConfig.DRAGON_SETTINGS.dragonAffectedBlocksRevert));
		} else if (block instanceof BlockLeaves || mat == Material.WATER) {
			worldObj.setBlockState(blockpos, Blocks.AIR.getDefaultState());
		} else if (block instanceof BlockGravel) {
			worldObj.setBlockState(blockpos, IafBlockRegistry.charedGravel.getDefaultState().withProperty(BlockFallingReturningState.REVERTS, IceAndFireConfig.DRAGON_SETTINGS.dragonAffectedBlocksRevert));
		} else if (mat == Material.WOOD) {
			worldObj.setBlockState(blockpos, IafBlockRegistry.ash.getDefaultState());
		} else if (mat == Material.ROCK && (block != IafBlockRegistry.charedCobblestone && block != Blocks.COBBLESTONE && block != Blocks.MOSSY_COBBLESTONE && block != Blocks.COBBLESTONE_WALL)) {
			worldObj.setBlockState(blockpos, IafBlockRegistry.charedStone.getDefaultState().withProperty(BlockReturningState.REVERTS, IceAndFireConfig.DRAGON_SETTINGS.dragonAffectedBlocksRevert));
		} else if (mat == Material.ROCK) {
			worldObj.setBlockState(blockpos, IafBlockRegistry.charedCobblestone.getDefaultState().withProperty(BlockReturningState.REVERTS, IceAndFireConfig.DRAGON_SETTINGS.dragonAffectedBlocksRevert));
		}
	}

	/** Convierte bloques congelados por dragón de hielo en variantes frozen */
	private void transformIceBlock(BlockPos blockpos, Block block, Material mat, IBlockState state) {
		if (block == Blocks.GRASS_PATH) {
			worldObj.setBlockState(blockpos, IafBlockRegistry.frozenGrassPath.getDefaultState().withProperty(BlockPath.REVERTS, IceAndFireConfig.DRAGON_SETTINGS.dragonAffectedBlocksRevert));
		} else if (block == Blocks.GRASS) {
			worldObj.setBlockState(blockpos, IafBlockRegistry.frozenGrass.getDefaultState().withProperty(BlockReturningState.REVERTS, IceAndFireConfig.DRAGON_SETTINGS.dragonAffectedBlocksRevert));
		} else if (block instanceof BlockGrass || block instanceof BlockDirt) {
			worldObj.setBlockState(blockpos, IafBlockRegistry.frozenDirt.getDefaultState().withProperty(BlockReturningState.REVERTS, IceAndFireConfig.DRAGON_SETTINGS.dragonAffectedBlocksRevert));
		} else if (block instanceof BlockLeaves) {
			worldObj.setBlockState(blockpos, Blocks.AIR.getDefaultState());
		} else if (block instanceof BlockGravel) {
			worldObj.setBlockState(blockpos, IafBlockRegistry.frozenGravel.getDefaultState().withProperty(BlockFallingReturningState.REVERTS, IceAndFireConfig.DRAGON_SETTINGS.dragonAffectedBlocksRevert));
		} else if (mat == Material.WATER) {
			worldObj.setBlockState(blockpos, Blocks.ICE.getDefaultState());
		} else if (mat == Material.WOOD) {
			worldObj.setBlockState(blockpos, IafBlockRegistry.frozenSplinters.getDefaultState());
		} else if (mat == Material.ROCK && (block != IafBlockRegistry.frozenCobblestone && block != Blocks.COBBLESTONE && block != Blocks.MOSSY_COBBLESTONE && block != Blocks.COBBLESTONE_WALL)) {
			worldObj.setBlockState(blockpos, IafBlockRegistry.frozenStone.getDefaultState().withProperty(BlockReturningState.REVERTS, IceAndFireConfig.DRAGON_SETTINGS.dragonAffectedBlocksRevert));
		} else if (mat == Material.ROCK) {
			worldObj.setBlockState(blockpos, IafBlockRegistry.frozenCobblestone.getDefaultState().withProperty(BlockReturningState.REVERTS, IceAndFireConfig.DRAGON_SETTINGS.dragonAffectedBlocksRevert));
		}
	}

	/** Convierte bloques agrietados por dragón de relámpago en variantes cracked */
	private void transformLightningBlock(BlockPos blockpos, Block block, Material mat, IBlockState state) {
		if (block == Blocks.GRASS_PATH) {
			worldObj.setBlockState(blockpos, IafBlockRegistry.crackledGrassPath.getDefaultState().withProperty(BlockPath.REVERTS, IceAndFireConfig.DRAGON_SETTINGS.dragonAffectedBlocksRevert));
		} else if (block == Blocks.GRASS) {
			worldObj.setBlockState(blockpos, IafBlockRegistry.crackledGrass.getDefaultState().withProperty(BlockReturningState.REVERTS, IceAndFireConfig.DRAGON_SETTINGS.dragonAffectedBlocksRevert));
		} else if (block instanceof BlockGrass || block instanceof BlockDirt) {
			worldObj.setBlockState(blockpos, IafBlockRegistry.crackledDirt.getDefaultState().withProperty(BlockReturningState.REVERTS, IceAndFireConfig.DRAGON_SETTINGS.dragonAffectedBlocksRevert));
		} else if (block instanceof BlockLeaves || mat == Material.WATER) {
			worldObj.setBlockState(blockpos, Blocks.AIR.getDefaultState());
		} else if (block instanceof BlockGravel) {
			worldObj.setBlockState(blockpos, IafBlockRegistry.crackledGravel.getDefaultState().withProperty(BlockFallingReturningState.REVERTS, IceAndFireConfig.DRAGON_SETTINGS.dragonAffectedBlocksRevert));
		} else if (mat == Material.WOOD) {
			worldObj.setBlockState(blockpos, IafBlockRegistry.ash.getDefaultState());
		} else if (mat == Material.ROCK && (block != IafBlockRegistry.crackledCobblestone && block != Blocks.COBBLESTONE && block != Blocks.MOSSY_COBBLESTONE && block != Blocks.COBBLESTONE_WALL)) {
			worldObj.setBlockState(blockpos, IafBlockRegistry.crackledStone.getDefaultState().withProperty(BlockReturningState.REVERTS, IceAndFireConfig.DRAGON_SETTINGS.dragonAffectedBlocksRevert));
		} else if (mat == Material.ROCK) {
			worldObj.setBlockState(blockpos, IafBlockRegistry.crackledCobblestone.getDefaultState().withProperty(BlockReturningState.REVERTS, IceAndFireConfig.DRAGON_SETTINGS.dragonAffectedBlocksRevert));
		} else if (mat == Material.SAND) {
			worldObj.setBlockState(blockpos, IafBlockRegistry.fulgurite.getDefaultState());
		}
	}

	private List<EnumParticle> getParticleTypes() {
		List<EnumParticle> types = new ArrayList<>();
		switch (dragonType) {
			case FIRE:
				types.add(EnumParticle.FLAME);
				types.add(EnumParticle.SMOKE_NORMAL);
				break;
			case ICE:
				types.add(EnumParticle.SNOWFLAKE);
				types.add(EnumParticle.CLOUD);
				break;
			case LIGHTNING:
				types.add(EnumParticle.SPARK);
				types.add(EnumParticle.SMOKE_NORMAL);
				break;
		}
		return types;
	}

	private void placePostExplosionBlocks() {
		if (!this.dragonGriefing) return;
		switch (dragonType) {
			case FIRE:
				for (BlockPos blockpos1 : this.affectedBlockPositions) {
					if (this.explosionRNG.nextInt(3) == 0 && this.worldObj.getBlockState(blockpos1).getBlock() == Blocks.AIR && this.worldObj.getBlockState(blockpos1.down()).isFullBlock()) {
						this.worldObj.setBlockState(blockpos1, Blocks.FIRE.getDefaultState());
					}
				}
				break;
			case ICE:
				for (BlockPos blockpos1 : this.affectedBlockPositions) {
					if (this.explosionRNG.nextInt(3) == 0 && this.worldObj.getBlockState(blockpos1).getBlock() == Blocks.AIR && this.worldObj.getBlockState(blockpos1.down()).isFullBlock()) {
						this.worldObj.setBlockState(blockpos1, Blocks.SNOW_LAYER.getDefaultState().withProperty(BlockSnow.LAYERS, this.explosionRNG.nextInt(7) + 1));
					}
				}
				break;
			case LIGHTNING:
				break;
		}
	}

	@Override
	public Map<EntityPlayer, Vec3d> getPlayerKnockbackMap() {
		return this.playerKnockbackMap;
	}

	@Override
	public EntityLivingBase getExplosivePlacedBy() {
		return this.exploder == null ? null : (this.exploder instanceof EntityTNTPrimed ? ((EntityTNTPrimed) this.exploder).getTntPlacedBy() : (this.exploder instanceof EntityLivingBase ? (EntityLivingBase) this.exploder : null));
	}

	@Override
	public void clearAffectedBlockPositions() {
		this.affectedBlockPositions.clear();
	}

	@Override
	public List<BlockPos> getAffectedBlockPositions() {
		return this.affectedBlockPositions;
	}

	@Override
	public Vec3d getPosition() {
		return this.position;
	}
}
