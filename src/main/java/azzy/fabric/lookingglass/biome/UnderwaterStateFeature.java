package azzy.fabric.lookingglass.biome;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.TickPriority;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.SingleStateFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.Random;

public class UnderwaterStateFeature extends Feature<SingleStateFeatureConfig> {

    public UnderwaterStateFeature(Codec<SingleStateFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<SingleStateFeatureConfig> context) {
        final StructureWorldAccess world = context.getWorld();
        final BlockPos pos = context.getOrigin();
        final SingleStateFeatureConfig config = context.getConfig();
        if(!world.isWater(pos)) {
            return false;
        }

        if(!world.isWater(pos.down())) {
            world.setBlockState(pos.down(), config.state, 2);
            world.getBlockTickScheduler().schedule(pos.down(), config.state.getBlock(), 0, TickPriority.EXTREMELY_HIGH);
            return true;
        }

        return false;
    }
}
