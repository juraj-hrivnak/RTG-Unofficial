package rtg.world.biome.realistic.vanilla;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt.DirtType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import rtg.api.config.BiomeConfig;
import rtg.api.util.BlockUtil;
import rtg.api.util.noise.SimplexNoise;
import rtg.api.world.RTGWorld;
import rtg.api.world.biome.RealisticBiomeBase;
import rtg.api.world.deco.collection.DecoCollectionBirchForest;
import rtg.api.world.surface.SurfaceBase;
import rtg.api.world.terrain.TerrainBase;
import rtg.api.world.terrain.heighteffect.GroundEffect;

import java.util.Random;


public class RealisticBiomeVanillaBirchForest extends RealisticBiomeBase {

    public static Biome biome = Biomes.BIRCH_FOREST;
    public static Biome river = Biomes.RIVER;

    public RealisticBiomeVanillaBirchForest() {

        super(biome);
    }

    @Override
    public void initConfig() {
        this.getConfig().addProperty(this.getConfig().ALLOW_LOGS).set(true);
        this.getConfig().addProperty(this.getConfig().FALLEN_LOG_DENSITY_MULTIPLIER);
        this.getConfig().addProperty(this.getConfig().SURFACE_MIX_BLOCK).set("");
    }

    @Override
    public TerrainBase initTerrain() {

        return new TerrainVanillaBirchForest();
    }

    @Override
    public SurfaceBase initSurface() {

        return new SurfaceVanillaBirchForest(getConfig(), biome.topBlock, biome.fillerBlock, 0f, 1.5f, 60f, 65f, 1.5f, BlockUtil.getStateDirt(DirtType.PODZOL), 0.15f);
    }

    @Override
    public void initDecos() {
        this.addDecoCollection(new DecoCollectionBirchForest(this.getConfig()));
    }

//    @Override
//    public void overrideDecorations() {
//        baseBiome().decorator.grassPerChunk = -999;
//        baseBiome().decorator.flowersPerChunk = -999;
//    }

    public static class TerrainVanillaBirchForest extends TerrainBase {


        private final GroundEffect groundEffect = new GroundEffect(4f);

        public TerrainVanillaBirchForest() {

        }

        @Override
        public float generateNoise(RTGWorld rtgWorld, int x, int y, float border, float river) {
            //return terrainPlains(x, y, simplex, river, 160f, 10f, 60f, 80f, 65f);
            return riverized(65f + groundEffect.added(rtgWorld, x, y), river);
        }
    }

    public static class SurfaceVanillaBirchForest extends SurfaceBase {

        private final float min;

        private float sCliff = 1.5f;
        private float sHeight = 60f;
        private float sStrength = 65f;
        private float cCliff = 1.5f;

        private final IBlockState mixBlock;
        private final float mixHeight;

        public SurfaceVanillaBirchForest(BiomeConfig config, IBlockState top, IBlockState fill, float minCliff, float stoneCliff,
                                         float stoneHeight, float stoneStrength, float clayCliff, IBlockState mix, float mixSize) {

            super(config, top, fill);
            min = minCliff;

            sCliff = stoneCliff;
            sHeight = stoneHeight;
            sStrength = stoneStrength;
            cCliff = clayCliff;

            mixBlock = this.getConfigBlock(config.SURFACE_MIX_BLOCK.get(), mix);
            mixHeight = mixSize;
        }

        @Override
        public void paintTerrain(ChunkPrimer primer, int i, int j, int x, int z, int depth, RTGWorld rtgWorld, float[] noise, float river, Biome[] base) {

            Random rand = rtgWorld.rand();
            SimplexNoise simplex = rtgWorld.simplexInstance(0);
            float c = TerrainBase.calcCliff(x, z, noise);
            int cliff = 0;
            boolean m = false;

            Block b;
            for (int k = 255; k > -1; k--) {
                b = primer.getBlockState(x, k, z).getBlock();
                if (b == Blocks.AIR) {
                    depth = -1;
                } else if (b == Blocks.STONE) {
                    depth++;

                    if (depth == 0) {

                        float p = simplex.noise3f(i / 8f, j / 8f, k / 8f) * 0.5f;
                        if (c > min && c > sCliff - ((k - sHeight) / sStrength) + p) {
                            cliff = 1;
                        }
                        if (c > cCliff) {
                            cliff = 2;
                        }

                        if (cliff == 1) {
                            if (rand.nextInt(3) == 0) {

                                primer.setBlockState(x, k, z, hcCobble());
                            } else {

                                primer.setBlockState(x, k, z, hcStone());
                            }
                        } else if (cliff == 2) {
                            primer.setBlockState(x, k, z, getShadowStoneBlock());
                        } else if (k < 63) {
                            if (k < 62) {
                                primer.setBlockState(x, k, z, fillerBlock);
                            } else {
                                primer.setBlockState(x, k, z, topBlock);
                            }
                        } else if (simplex.noise2f(i / 12f, j / 12f) > mixHeight) {
                            primer.setBlockState(x, k, z, mixBlock);
                            m = true;
                        } else {
                            primer.setBlockState(x, k, z, topBlock);
                        }
                    } else if (depth < 6) {
                        if (cliff == 1) {
                            primer.setBlockState(x, k, z, hcStone());
                        } else if (cliff == 2) {
                            primer.setBlockState(x, k, z, getShadowStoneBlock());
                        } else {
                            primer.setBlockState(x, k, z, fillerBlock);
                        }
                    }
                }
            }
        }
    }
}
