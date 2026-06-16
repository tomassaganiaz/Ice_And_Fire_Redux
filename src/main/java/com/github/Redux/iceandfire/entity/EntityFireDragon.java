package com.github.Redux.iceandfire.entity;

import com.github.Redux.iceandfire.IceAndFire;
import com.github.Redux.iceandfire.IceAndFireConfig;
import com.github.Redux.iceandfire.entity.explosion.DragonExplosion;
import com.github.Redux.iceandfire.enums.EnumDragonEgg;
import com.github.Redux.iceandfire.item.IafItemRegistry;
import com.github.Redux.iceandfire.message.MessageDragonSyncFire;
import com.github.Redux.iceandfire.misc.IafSoundRegistry;
import com.github.Redux.iceandfire.entity.ai.*;
import com.github.Redux.iceandfire.entity.projectile.EntityDragonFire;
import com.github.Redux.iceandfire.entity.projectile.EntityDragonFireCharge;
import com.github.Redux.iceandfire.entity.projectile.EntityDragonProjectile;
import com.github.Redux.iceandfire.entity.util.DragonUtils;
import com.github.Redux.iceandfire.enums.EnumDragonType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

/** Dragón de fuego — implementa el tipo FIRE con ataques de fuego, resistencia al fuego y quema de bloques */
public class EntityFireDragon extends EntityDragonBase {

    public static final float[] growth_stage_1 = new float[]{1F, 3F};
    public static final float[] growth_stage_2 = new float[]{3F, 7F};
    public static final float[] growth_stage_3 = new float[]{7F, 12.5F};
    public static final float[] growth_stage_4 = new float[]{12.5F, 20F};
    public static final float[] growth_stage_5 = new float[]{20F, 30F};
    public static final ResourceLocation FEMALE_LOOT = LootTableList.register(new ResourceLocation("iceandfire", "dragon/fire_dragon_female"));
    public static final ResourceLocation MALE_LOOT = LootTableList.register(new ResourceLocation("iceandfire", "dragon/fire_dragon_male"));
    public static final ResourceLocation SKELETON_LOOT = LootTableList.register(new ResourceLocation("iceandfire", "dragon/fire_dragon_skeleton"));

    public EntityFireDragon(World worldIn) {
        super(worldIn, EnumDragonType.FIRE, 1, 1 + IceAndFireConfig.DRAGON_SETTINGS.dragonAttackDamage, IceAndFireConfig.DRAGON_SETTINGS.dragonHealth * 0.04, IceAndFireConfig.DRAGON_SETTINGS.dragonHealth, 0.15F, 0.4F);
        this.setSize(0.78F, 1.2F);
        this.setPathPriority(PathNodeType.DANGER_FIRE, 0.0F);
        this.setPathPriority(PathNodeType.DAMAGE_FIRE, 0.0F);
        this.setPathPriority(PathNodeType.LAVA, 8.0F);
        this.isImmuneToFire = true;
        this.ignoreFrustumCheck = true;
        this.growth_stages = new float[][]{growth_stage_1, growth_stage_2, growth_stage_3, growth_stage_4, growth_stage_5};
        this.stepHeight = 1;
    }

    @Override
    protected void addCustomTasks() {
        this.tasks.addTask(2, new DragonAISwim(this));
    }

    public String getVariantName(int variant) {
        switch (variant) {
            default:
                return "red_";
            case 1:
                return "green_";
            case 2:
                return "bronze_";
            case 3:
                return "gray_";
        }
    }

    public Item getVariantScale(int variant) {
        switch (variant) {
            default:
                return EnumDragonEgg.RED.scales;
            case 1:
                return EnumDragonEgg.GREEN.scales;
            case 2:
                return EnumDragonEgg.BRONZE.scales;
            case 3:
                return EnumDragonEgg.GRAY.scales;
        }
    }

    public Item getVariantEgg(int variant) {
        switch (variant) {
            default:
                return EnumDragonEgg.RED.egg;
            case 1:
                return EnumDragonEgg.GREEN.egg;
            case 2:
                return EnumDragonEgg.BRONZE.egg;
            case 3:
                return EnumDragonEgg.GRAY.egg;
        }
    }

    @Override
    public Item getSummoningCrystal() {
        return IafItemRegistry.summoning_crystal_fire;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (IceAndFire.dragonFire.damageType.contentEquals(source.damageType)) {
            return false;
        }
        return super.attackEntityFrom(source, amount);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (!world.isRemote) {
            if ((this.isInLava() || isInWater()) && !this.isFlying() && !this.isChild() && !this.isHovering() && this.canMove()) {
                this.setHovering(true);
                this.flyTicks = 0;
            }
            handleCombatTarget();
        }
    }

    @Override
    protected void breathFireAtPos(BlockPos burningTarget) {
        if (this.isBreathingFire()) {
            if (this.isActuallyBreathingFire()) {
                rotationYaw = renderYawOffset;
                stimulateFire(burningTarget.getX() + 0.5F, burningTarget.getY() + 0.5F, burningTarget.getZ() + 0.5F, 1);
            }
        } else {
            this.setBreathingFire(true);
        }
    }

    @Override
    public void stimulateFire(double burnX, double burnY, double burnZ, int syncType) {
        if (syncType == 1 && !world.isRemote) {
            //sync with client
            IceAndFire.NETWORK_WRAPPER.sendToAll(new MessageDragonSyncFire(this.getEntityId(), burnX, burnY, burnZ, 0));
        }
        if (this.world.isRemote && this.ticksExisted % 5 == 0 && this.isActuallyBreathingFire()) {
            this.playSoundClientSide(IafSoundRegistry.FIREDRAGON_BREATH, 4, 1);
        }
        this.getNavigator().clearPath();
        this.burnParticleX = burnX;
        this.burnParticleY = burnY;
        this.burnParticleZ = burnZ;
        Vec3d headPos = getHeadPosition();
        double d2 = burnX - headPos.x;
        double d3 = burnY - headPos.y;
        double d4 = burnZ - headPos.z;
        double distance = Math.max(5 * this.getDistance(burnX, burnY, burnZ), 0);
        double conqueredDistance = burnProgress / 40D * distance;
        int increment = (int) Math.ceil(conqueredDistance / 100);
        for (int i = 0; i < conqueredDistance; i += increment) {
            double progressX = headPos.x + d2 * (i / (float) distance);
            double progressY = headPos.y + d3 * (i / (float) distance);
            double progressZ = headPos.z + d4 * (i / (float) distance);
            if (canPositionBeSeen(progressX, progressY, progressZ)) {
                if (world.isRemote && rand.nextInt(5) == 0) {
                    IceAndFire.PROXY.spawnDragonParticle(this);
                }
            } else {
                if (!world.isRemote) {
                    RayTraceResult result = this.world.rayTraceBlocks(new Vec3d(this.posX, this.posY + (double) this.getEyeHeight(), this.posZ), new Vec3d(progressX, progressY, progressZ), false, true, false);
                    if (result != null) {
                        BlockPos pos = result.getBlockPos();
                        DragonExplosion explosion = new DragonExplosion(EnumDragonType.FIRE, this.world, this, pos.getX(), pos.getY(), pos.getZ(), this.getDragonStage() * 2.5F, this.world.getGameRules().getBoolean("mobGriefing"));
                        explosion.doExplosionA();
                        explosion.doExplosionB(true);
                    }
                }
            }
        }
        if (burnProgress >= 40D && canPositionBeSeen(burnX, burnY, burnZ)) {
            double spawnX = burnX + (rand.nextFloat() * 3.0) - 1.5;
            double spawnY = burnY + (rand.nextFloat() * 3.0) - 1.5;
            double spawnZ = burnZ + (rand.nextFloat() * 3.0) - 1.5;
            if (!world.isRemote) {
                DragonExplosion explosion = new DragonExplosion(EnumDragonType.FIRE, this.world, this, spawnX, spawnY, spawnZ, this.getDragonStage() * 2.5F, this.world.getGameRules().getBoolean("mobGriefing"));
                explosion.doExplosionA();
                explosion.doExplosionB(true);
            }
        }
    }

    @Override
    public ResourceLocation getDeadLootTable() {
        if (this.getDeathStage() >= (this.getAgeInDays() / 5) / 2) {
            return SKELETON_LOOT;
        } else {
            return isMale() ? MALE_LOOT : FEMALE_LOOT;
        }
    }

    @Override
    protected ItemStack getSkull() {
        return new ItemStack(IafItemRegistry.dragon_skull, 1, 0);
    }

    @Override
    protected ItemStack getHorn() {
        return new ItemStack(IafItemRegistry.dragon_horn_fire);
    }

    @Override
    public Item getBlood() {
        return IafItemRegistry.fire_dragon_blood;
    }

    @Override
    public Item getHeart() {
        return IafItemRegistry.fire_dragon_heart;
    }

    @Override
    public Item getFlesh() {
        return IafItemRegistry.fire_dragon_flesh;
    }

    @Override
    protected int getBaseEggTypeValue() {
        return 0;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.isTeen() ? IafSoundRegistry.FIREDRAGON_TEEN_IDLE : this.isAdult() ? IafSoundRegistry.FIREDRAGON_ADULT_IDLE : IafSoundRegistry.FIREDRAGON_CHILD_IDLE;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return this.isTeen() ? IafSoundRegistry.FIREDRAGON_TEEN_HURT : this.isAdult() ? IafSoundRegistry.FIREDRAGON_ADULT_HURT : IafSoundRegistry.FIREDRAGON_CHILD_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return this.isTeen() ? IafSoundRegistry.FIREDRAGON_TEEN_DEATH : this.isAdult() ? IafSoundRegistry.FIREDRAGON_ADULT_DEATH : IafSoundRegistry.FIREDRAGON_CHILD_DEATH;
    }

    @Override
    public SoundEvent getRoarSound() {
        return this.isTeen() ? IafSoundRegistry.FIREDRAGON_TEEN_ROAR : this.isAdult() ? IafSoundRegistry.FIREDRAGON_ADULT_ROAR : IafSoundRegistry.FIREDRAGON_CHILD_ROAR;
    }

    @Override
    public SoundEvent getShortBreathSound() {
        return IafSoundRegistry.FIREDRAGON_BREATH_SHORT;
    }

    @Override
    protected EntityDragonProjectile createChargeProjectile(World world, double d2, double d3, double d4) {
        EntityDragonFireCharge charge = new EntityDragonFireCharge(world, this, d2, d3, d4);
        return charge;
    }

    @Override
    protected EntityDragonProjectile createBreathProjectile(World world, double d2, double d3, double d4) {
        EntityDragonFire breath = new EntityDragonFire(world, this, d2, d3, d4);
        return breath;
    }

    @Override
    protected DragonExplosion createExplosion(World world, double x, double y, double z, float size, boolean griefing) {
        return new DragonExplosion(EnumDragonType.FIRE, world, this, x, y, z, size, griefing);
    }

    @Override
    protected Item getStewItem() {
        return IafItemRegistry.fire_stew;
    }

}
