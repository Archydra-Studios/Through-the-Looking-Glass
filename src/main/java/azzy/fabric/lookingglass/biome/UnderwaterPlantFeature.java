package azzy.fabric.lookingglass.biome;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.TallSeagrassBlock;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;

public class UnderwaterPlantFeature extends Feature<BiFeatureConfig> {

    public UnderwaterPlantFeature(Codec<BiFeatureConfig> codec) {
        super(codec);
    }

    public boolean generate(StructureWorldAccess structureWorldAccess, ChunkGenerator chunkGenerator, Random random, BlockPos pos, BiFeatureConfig config) {
        boolean success = false;
        int i = random.nextInt(8) - random.nextInt(8);
        int j = random.nextInt(8) - random.nextInt(8);
        int k = structureWorldAccess.getTopY(Heightmap.Type.OCEAN_FLOOR, pos.getX() + i, pos.getZ() + j);
        BlockPos blockPos2 = new BlockPos(pos.getX() + i, k, pos.getZ() + j);
        if (structureWorldAccess.getBlockState(blockPos2).isOf(Blocks.WATER)) {
            boolean tall = random.nextDouble() < config.chance;
            BlockState blockState = tall ? config.secondaryState : config.primaryState;
            if (blockState.canPlaceAt(structureWorldAccess, blockPos2)) {
                if (tall) {
                    BlockState blockState2 = blockState.with(TallSeagrassBlock.HALF, DoubleBlockHalf.UPPER);
                    BlockPos blockPos3 = blockPos2.up();
                    if (structureWorldAccess.getBlockState(blockPos3).isOf(Blocks.WATER)) {
                        structureWorldAccess.setBlockState(blockPos2, blockState, 2);
                        structureWorldAccess.setBlockState(blockPos3, blockState2, 2);
                    }
                } else {
                    structureWorldAccess.setBlockState(blockPos2, blockState, 2);
                }

                success = true;
            }
        }

        return success;
    }
}
