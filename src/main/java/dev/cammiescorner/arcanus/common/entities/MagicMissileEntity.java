package dev.cammiescorner.arcanus.common.entities;

import dev.cammiescorner.arcanus.core.registry.ModEntities;
import dev.cammiescorner.arcanus.core.registry.ModParticles;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

public class MagicMissileEntity extends PersistentProjectileEntity {
	public MagicMissileEntity(LivingEntity owner, World world) {
		super(ModEntities.MAGIC_MISSILE, owner, world);
		setNoGravity(true);
		setDamage(1.5D);
	}

	public MagicMissileEntity(World world, double x, double y, double z) {
		super(ModEntities.MAGIC_MISSILE, x, y, z, world);
		setNoGravity(true);
		setDamage(1.5D);
	}

	public MagicMissileEntity(EntityType type, World world) {
		super(type, world);
		setNoGravity(true);
		setDamage(1.5D);
	}

	@Override
	public void tick() {
		super.tick();

		if(!world.isClient()) {
			for(int count = 0; count < 16; count++) {
				double x = getX() + (world.random.nextInt(3) - 1) / 4D;
				double y = getY() + 0.2F + (world.random.nextInt(3) - 1) / 4D;
				double z = getZ() + (world.random.nextInt(3) - 1) / 4D;
				double deltaX = (world.random.nextInt(3) - 1) * world.random.nextDouble();
				double deltaY = (world.random.nextInt(3) - 1) * world.random.nextDouble();
				double deltaZ = (world.random.nextInt(3) - 1) * world.random.nextDouble();

				PlayerLookup.tracking(this).forEach(player -> ((ServerWorld) world).spawnParticles(player, (ParticleEffect) ModParticles.MAGIC_MISSILE, true, x, y, z, 1, deltaX, deltaY, deltaZ, 0.1));
			}
		}

		if(age > 40)
			kill();
	}

	@Override
	protected SoundEvent getHitSound() {
		return SoundEvents.BLOCK_AMETHYST_BLOCK_STEP;
	}

	@Override
	protected void onBlockHit(BlockHitResult blockHitResult) {
		super.onBlockHit(blockHitResult);
		kill();
	}

	@Override
	protected void onEntityHit(EntityHitResult entityHitResult) {
		super.onEntityHit(entityHitResult);

		if(entityHitResult.getEntity() instanceof LivingEntity target)
			target.timeUntilRegen = 0;
	}

	@Override
	public void onPlayerCollision(PlayerEntity player) {
		if(!world.isClient && (inGround || isNoClip()) && shake <= 0)
			discard();
	}

	@Override
	protected ItemStack asItemStack() {
		return null;
	}
}
