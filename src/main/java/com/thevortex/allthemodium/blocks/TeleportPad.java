package com.thevortex.allthemodium.blocks;

import java.util.ArrayList;
import java.util.List;

import com.thevortex.allthemodium.AllTheModium;
import com.thevortex.allthemodium.init.ModItems;

import com.thevortex.allthemodium.reference.Reference;
import com.thevortex.allthemodium.registry.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TeleportPad extends Block {
	protected static final VoxelShape TELEPORTPAD_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 3.0D, 16.0D);

	public TeleportPad(Properties properties) {
		super(properties);
		// TODO Auto-generated constructor stub
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return TELEPORTPAD_AABB;
	}

	@Override
	public PushReaction getPistonPushReaction(BlockState state) {
		return PushReaction.BLOCK;
	}


	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos,
			CollisionContext context) {

		return TELEPORTPAD_AABB;
	}

	@Override
	public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player,
								 InteractionHand handIn, BlockHitResult hit) {
		if ((player instanceof ServerPlayer) && (player.isCrouching() == true)) {

			transferPlayer((ServerPlayer) player, pos);
			worldIn.addParticle(ParticleTypes.SOUL_FIRE_FLAME, pos.getX(), pos.getY() + 1, pos.getZ(), 0, 1, 0);
		}
		return super.use(state, worldIn, pos, player, handIn, hit);
	}

	@Override
	public boolean canHarvestBlock(BlockState state, BlockGetter world, BlockPos pos, Player player) {
		if(player.level.dimension().getRegistryName().getNamespace().contains(Reference.MOD_ID)) {
			return false;
		} else {
			return true;
		}
	}

	public void transferPlayer(ServerPlayer player, BlockPos pos) {
		int config = 0;
		/*if(ModList.get().isLoaded("allthetweaks")) {
			config = Configuration.COMMON.mainmode.get();
		}*/
		if (player.level.dimension().equals(AllTheModium.Mining)) {
			ServerLevel targetWorld = player.server.getLevel(AllTheModium.OverWorld);
			int y = 256;
			boolean located = false;
			while (y >= 1) {
				BlockPos posa = new BlockPos(Math.round(pos.getX()), y, Math.round(pos.getZ()));
				Block potential = targetWorld.getBlockState(posa).getBlock();
				if (potential.getRegistryName().getPath().equals("teleport_pad")) {
					located = true;
					break;

				} else {
					y--;
				}
			}
			if (located) {
				targetWorld.addParticle(ParticleTypes.SOUL_FIRE_FLAME, pos.getX(), pos.getY(), pos.getZ(), 0, 1, 0);
				player.teleportTo(targetWorld, pos.getX() + 0.5D, y + 0.25D, pos.getZ() + 0.5D, player.rotA,
						player.yya);

				return;
			} else {

				if ((!targetWorld.getBlockState(pos).hasBlockEntity())
						&& (targetWorld.getBlockState(pos).canEntityDestroy(targetWorld, pos, player))) {
					//targetWorld.setBlockState(pos, ModBlocks.TELEPORT_PAD.getDefaultState());
				}

				targetWorld.addParticle(ParticleTypes.SOUL_FIRE_FLAME, pos.getX(), pos.getY(), pos.getZ(), 0, 1, 0);
				player.teleportTo(targetWorld, pos.getX() + 0.5D, pos.getY() + 0.25D, pos.getZ() + 0.5D, player.rotA,
						player.yya);
			}

		} else if (player.level.dimension().equals(AllTheModium.Nether)) {
			ServerLevel targetWorld = player.server.getLevel(AllTheModium.THE_OTHER);
			BlockPos targetPos = new BlockPos(Math.round(pos.getX()), Math.round(pos.getY()), Math.round(pos.getZ()));

			if (!targetWorld.getBlockState(targetPos).hasBlockEntity()) {

				targetWorld.setBlockAndUpdate(targetPos, ModRegistry.TELEPORT_PAD.get().defaultBlockState());
				targetWorld.addParticle(ParticleTypes.SOUL_FIRE_FLAME, pos.getX(), pos.getY(), pos.getZ(), 0, 1, 0);
				player.teleportTo(targetWorld, targetPos.getX() + 0.5D, targetPos.getY() + 0.25D, targetPos.getZ() + 0.5D, 0, 0);


			}
		} else if (player.level.dimension().equals(AllTheModium.THE_OTHER)) {
			ServerLevel targetWorld = player.server.getLevel(AllTheModium.Nether);
			int y = 128;
			boolean located = false;
			while (y >= 1) {
				BlockPos posa = new BlockPos(Math.round(pos.getX()), y, Math.round(pos.getZ()));
				Block potential = targetWorld.getBlockState(posa).getBlock();
				if (potential.getRegistryName().getPath().equals("teleport_pad")) {
					located = true;
					break;

				} else {
					y--;
				}
			}
			if (located) {
				targetWorld.addParticle(ParticleTypes.SOUL_FIRE_FLAME, pos.getX(), pos.getY(), pos.getZ(), 0, 1, 0);
				player.teleportTo(targetWorld, pos.getX() + 0.5D, y + 0.25D, pos.getZ() + 0.5D, player.rotA,
						player.yya);

				return;
			} else {
				BlockPos newpos = new BlockPos(pos.getX(), 90 , pos.getZ());
				if ((!targetWorld.getBlockState(newpos).hasBlockEntity())
						&& (targetWorld.getBlockState(newpos).canEntityDestroy(targetWorld, newpos, player))) {
					targetWorld.setBlockAndUpdate(newpos, ModRegistry.TELEPORT_PAD.get().defaultBlockState());
				}

				targetWorld.addParticle(ParticleTypes.SOUL_FIRE_FLAME, newpos.getX(), newpos.getY(), newpos.getZ(), 0, 1, 0);
				player.teleportTo(targetWorld, newpos.getX() + 0.5D, newpos.getY() + 0.25D, newpos.getZ() + 0.5D, player.rotA,
						player.yya);

			}
		}

		else if (player.level.dimension().equals(AllTheModium.OverWorld) && (config != 2)) {
			ServerLevel targetWorld = player.server.getLevel(AllTheModium.Mining);
			BlockPos targetPos = new BlockPos(Math.round(pos.getX()), 75, Math.round(pos.getZ()));
			if (!targetWorld.getBlockState(targetPos).hasBlockEntity()) {
				targetWorld.setBlockAndUpdate(targetPos, ModRegistry.TELEPORT_PAD.get().defaultBlockState());
				targetWorld.addParticle(ParticleTypes.SOUL_FIRE_FLAME, pos.getX(), pos.getY(), pos.getZ(), 0, 1, 0);
				player.teleportTo(targetWorld, targetPos.getX() + 0.5D, targetPos.getY() + 0.25D, targetPos.getZ() + 0.5D, 0, 0);

			}
		} /*else if (player.world.getDimensionKey().equals(AllTheModium.The_End)) {
			ServerWorld targetWorld = player.server.getWorld(AllTheModium.THE_BEYOND);
			BlockPos targetPos = new BlockPos(Math.round(pos.getX()), 75, Math.round(pos.getZ()));
			if (targetWorld.getBlockState(targetPos).hasTileEntity() == false) {
				targetWorld.setBlockState(targetPos, ModBlocks.TELEPORT_PAD.getDefaultState());
				targetWorld.addParticle(ParticleTypes.SOUL_FIRE_FLAME, pos.getX(), pos.getY(), pos.getZ(), 0, 1, 0);
				player.teleport(targetWorld, targetPos.getX() + 0.5D, targetPos.getY() + 0.25D, targetPos.getZ() + 0.5D, 0, 0);
				player.connection.sendPacket(new SPlaySoundEventPacket(1032, BlockPos.ZERO, 0, false));
			}
		}else if (player.world.getDimensionKey().equals(AllTheModium.THE_BEYOND)) {
			ServerWorld targetWorld = player.server.getWorld(AllTheModium.The_End);
			int y = 256;
			boolean located = false;
			while (y >= 1) {
				BlockPos posa = new BlockPos(Math.round(pos.getX()), y, Math.round(pos.getZ()));
				Block potential = targetWorld.getBlockState(posa).getBlock();
				if (potential.getRegistryName().getPath().equals("teleport_pad")) {
					located = true;
					break;

				} else {
					y--;
				}
			}
			if (located) {
				targetWorld.addParticle(ParticleTypes.SOUL_FIRE_FLAME, pos.getX(), pos.getY(), pos.getZ(), 0, 1, 0);
				player.teleport(targetWorld, pos.getX() + 0.5D, y + 0.25D, pos.getZ() + 0.5D, player.rotationYaw,
						player.rotationPitch);
				player.connection.sendPacket(new SPlaySoundEventPacket(1032, BlockPos.ZERO, 0, false));
				return;
			}
		}*/

	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		List<ItemStack> list = new ArrayList<ItemStack>();
		return list;
	}
}
