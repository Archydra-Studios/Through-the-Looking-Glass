package azzy.fabric.lookingglass.biome;

import azzy.fabric.lookingglass.LookingGlassCommon;
import azzy.fabric.lookingglass.block.LookingGlassBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.TickPriority;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.SingleStateFeatureConfig;

import java.util.Random;

public class UnderwaterStateFeature extends Feature<SingleStateFeatureConfig> {

    public UnderwaterStateFeature(Codec<SingleStateFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, SingleStateFeatureConfig config) {
        if(!world.isWater(pos)) {
            return false;
        }

        if(!world.isWater(pos.down())) {
            world.setBlockState(pos.down(), config.state, 2);
            world.getBlockTickScheduler().schedule(pos.down(), LookingGlassBlocks.BRINE_FISSURE, 0, TickPriority.EXTREMELY_HIGH);
            return true;
        }

        return false;
    }
}
