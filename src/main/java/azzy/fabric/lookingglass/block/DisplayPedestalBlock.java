package azzy.fabric.lookingglass.block;

import azzy.fabric.lookingglass.util.ParticleUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecated")
public class DisplayPedestalBlock extends LookingGlassBlock {
    private static final VoxelShape SHAPE = Block.createCuboidShape(5, 0, 5, 11, 12, 11);

    public DisplayPedestalBlock(Settings settings) {
        super(settings, false);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    /**
     * When the pedestal is placed, check to see if the unstable altar structure is complete.  If it is, set the BlockState appropriately.
     *
     * @param world     ServerWorld
     * @param pos       Placed Position
     * @param state     BlockState
     * @param placer    Player
     * @param itemStack Pedestal Stack
     */
    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        boolean unstableAltarFound = false;
        BlockPos unstableAltarPos = null;
        BlockState unstableAltarState;
        Block unstableAltarBlock = null;
        List<BlockPos> pedestalPositionList = new ArrayList<>();
        List<BlockState> pedestalBlockStateList = new ArrayList<>();

        // Don't want to do these in the client.
//        if(!world.isClient)
//            return;

        BlockPos[] cardinalPositions = {new BlockPos(-3, 0, 0), new BlockPos(0, 0, -3), new BlockPos(3, 0, 0), new BlockPos(0, 0, 3)};

        for (BlockPos tmpPos : cardinalPositions) {
            int posX = pos.getX() + tmpPos.getX();
            int posZ = pos.getZ() + tmpPos.getZ();
            int posY = pos.getY();
            unstableAltarPos = new BlockPos(posX, posY, posZ);
            unstableAltarState = world.getBlockState(unstableAltarPos);
            unstableAltarBlock = unstableAltarState.getBlock();
            if (LookingGlassBlocks.UNSTABLE_ENCHANTER_BLOCK.equals(unstableAltarBlock)) {
                unstableAltarFound = true;
                break;
            }
        }
        // Didn't find unstable altar anywhere.  Silently return doing nothing.
        if (!unstableAltarFound) {
            return;
        }

        // Found the unstable altar.  Now check in all 4 cardinal directions to see if we can find display pedestals.
        for (BlockPos cardinalPosition : cardinalPositions) {
            int posX = unstableAltarPos.getX() + cardinalPosition.getX();
            int posY = unstableAltarPos.getY();
            int posZ = unstableAltarPos.getZ() + cardinalPosition.getZ();

            BlockPos pedestalPos = new BlockPos(posX, posY, posZ);
            BlockState pedestalBlockState = world.getBlockState(pedestalPos);
            Block pedestalBlock = pedestalBlockState.getBlock();

            // This block isn't a display pedestal.  Keep moving.
            if (!LookingGlassBlocks.DISPLAY_PEDESTAL_BLOCK.equals(pedestalBlock))
                continue;

            pedestalPositionList.add(pedestalPos);
            pedestalBlockStateList.add(pedestalBlockState);
        }

        if (pedestalPositionList.size() < 4) {
            // We didn't find all 4 pedestals.  Return silently doing nothing.
            return;
        }

        ((UnstableEnchanterBlock) unstableAltarBlock).overrideDefaultState(LookingGlassBlocks.UNSTABLE_ENCHANTER_BLOCK.getDefaultState().with(UnstableEnchanterBlock.MULTI_BLOCK_FORMED, true));

        if (!world.isClient) {
            ParticleUtils.spawnParticles(ParticleUtils.GREEN, ((ServerWorld) world), unstableAltarPos, 1.2, 30);
            ParticleUtils.spawnParticles(ParticleUtils.GREEN, ((ServerWorld) world), pedestalPositionList.get(0), 1.2, 30);
            ParticleUtils.spawnParticles(ParticleUtils.GREEN, ((ServerWorld) world), pedestalPositionList.get(1), 1.2, 30);
            ParticleUtils.spawnParticles(ParticleUtils.GREEN, ((ServerWorld) world), pedestalPositionList.get(2), 1.2, 30);
            ParticleUtils.spawnParticles(ParticleUtils.GREEN, ((ServerWorld) world), pedestalPositionList.get(3), 1.2, 30);
        }
    }
}
