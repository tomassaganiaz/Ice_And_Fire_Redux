package com.github.Redux.iceandfire.event;

import com.github.Redux.iceandfire.block.IDreadBlock;
import com.github.Redux.iceandfire.entity.*;
import com.github.Redux.iceandfire.entity.util.DragonUtils;
import com.github.Redux.iceandfire.entity.util.IDreadMob;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
/** DragonRidingHandler — Dragon Riding Handler */


public class DragonRidingHandler {

	@SubscribeEvent
	public void onArrowCollide(ProjectileImpactEvent event) {
		if (event.getEntity() instanceof EntityArrow && ((EntityArrow) event.getEntity()).shootingEntity != null) {
			if(event.getRayTraceResult() != null && event.getRayTraceResult().entityHit != null) {
				Entity shootingEntity = ((EntityArrow) event.getEntity()).shootingEntity;
				Entity shotEntity = event.getRayTraceResult().entityHit;
				if (shootingEntity instanceof EntityLivingBase && shootingEntity.isRidingOrBeingRiddenBy(shotEntity)) {
					if (shotEntity instanceof EntityTameable && ((EntityTameable) shotEntity).isTamed() && shotEntity.isOnSameTeam(shootingEntity)) {
						event.setCanceled(true);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onEntityMount(EntityMountEvent event) {
		if (event.isMounting() && IDreadMob.isOnSameTeam(event.getEntityMounting())) {
			if (!(event.getEntityBeingMounted() instanceof AbstractHorse || IDreadMob.isOnSameTeam(event.getEntityBeingMounted()))) {
				event.setCanceled(true);
			}
		}
		if (event.getEntityMounting() instanceof  EntityPlayer) {
			if (event.isDismounting()) {
				if (!DragonUtils.canDismount(event.getEntityBeingMounted()) && event.getEntityMounting().isSneaking()) {
					event.setCanceled(true);
					return;
				}
			} else {
				Entity previousRidingEntity = event.getEntityMounting().getRidingEntity();
				if (!DragonUtils.canDismount(previousRidingEntity)) {
					event.setCanceled(true);
					return;
				}
			}
		}
		if (event.getEntityBeingMounted() instanceof EntityDragonBase) {
			EntityDragonBase dragon = (EntityDragonBase)event.getEntityBeingMounted();
			if (event.isDismounting() && event.getEntityMounting() instanceof EntityPlayer && !event.getEntityMounting().world.isRemote) {
				EntityPlayer player = (EntityPlayer)event.getEntityMounting();
				if (dragon.isOwner((EntityPlayer)event.getEntityMounting())) {
					dragon.setPositionAndRotation(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
					player.fallDistance = -dragon.height;
				} else {
					dragon.renderYawOffset = dragon.rotationYaw;
					float modTick_0 = dragon.getAnimationTick() - 25;
					float modTick_1 = dragon.getAnimationTick() > 25 && dragon.getAnimationTick() < 55 ? 8 * MathHelper.clamp(MathHelper.sin((float) (Math.PI + modTick_0 * 0.25)), -0.8F, 0.8F) : 0;
					float modTick_2 = dragon.getAnimationTick() > 30 ? 10 : Math.max(0, dragon.getAnimationTick() - 20);
					float radius = 0.75F * (0.6F * dragon.getRenderSize() / 3) * -3;
					float angle = (0.01745329251F * dragon.renderYawOffset) + 3.15F + (modTick_1 *2F) * 0.015F;
					double extraX = radius * MathHelper.sin((float) (Math.PI + angle));
					double extraZ = radius * MathHelper.cos(angle);
					double extraY = modTick_2 == 0 ? 0 : 0.035F * ((dragon.getRenderSize() / 3) + (modTick_2 * 0.5 * (dragon.getRenderSize() / 3)));
					player.setPosition(dragon.posX + extraX, dragon.posY + extraY, dragon.posZ + extraZ);
				}
			}
		} else if (event.getEntityBeingMounted() instanceof EntityHippogryph) {
			EntityHippogryph hippogryph = (EntityHippogryph) event.getEntityBeingMounted();
			if(event.isDismounting() && event.getEntityMounting() instanceof EntityPlayer && !event.getEntityMounting().world.isRemote && hippogryph.isOwner((EntityPlayer)event.getEntityMounting())) {
				EntityPlayer player = (EntityPlayer) event.getEntityMounting();
				hippogryph.setPositionAndRotation(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
			}
		} else if (event.getEntityBeingMounted() instanceof EntityAmphithere) {
			EntityAmphithere amphithere = (EntityAmphithere) event.getEntityBeingMounted();
			if (event.isDismounting() && event.getEntityMounting() instanceof EntityPlayer && !event.getEntityMounting().world.isRemote && amphithere.isOwner((EntityPlayer)event.getEntityMounting())) {
				EntityPlayer player = (EntityPlayer) event.getEntityMounting();
				amphithere.setPositionAndRotation(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
			}
		}
		AxisAlignedBB bb = event.getEntityBeingMounted().getEntityBoundingBox();
		if (IDreadBlock.containsIndestructibleBlock(event.getWorldObj(), bb)) {
			if (event.isMounting() || event.getEntityBeingMounted() instanceof EntityLivingBase) {
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public void onEntityUseItem(PlayerInteractEvent.RightClickItem event) {
		EntityLivingBase entity = event.getEntityLiving();
		if (entity instanceof EntityPlayer && event.getHand() == EnumHand.MAIN_HAND && entity.rotationPitch > 87 && entity.getRidingEntity() != null && entity.getRidingEntity() instanceof EntityDragonBase) {
			((EntityDragonBase) entity.getRidingEntity()).processInteract((EntityPlayer)entity, event.getHand());
		}
	}

	@SubscribeEvent
	public void onPlayerLeaveEvent(PlayerEvent.PlayerLoggedOutEvent event) {
		if (event.player != null) {
			if (!event.player.getPassengers().isEmpty()) {
				if (event.player.isRiding()) {
					event.player.dismountRidingEntity();
				}
				for (Entity entity : event.player.getPassengers()) {
					entity.dismountRidingEntity();
				}
			}
		}
	}

	public static float updateRotation(float angle, float targetAngle, float maxIncrease) {
		float f = MathHelper.wrapDegrees(targetAngle - angle);
		if (f > maxIncrease) {
			f = maxIncrease;
		}
		if (f < -maxIncrease) {
			f = -maxIncrease;
		}
		return angle + f;
	}
}
