package rtg.world.biome.realistic.realworld;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import rtg.api.config.BiomeConfig;
import rtg.api.world.RTGWorld;
import rtg.api.world.surface.SurfaceBase;
import rtg.api.world.terrain.TerrainBase;
import rtg.api.world.terrain.heighteffect.BumpyHillsEffect;
import rtg.api.world.terrain.heighteffect.JitterEffect;

import java.util.Random;


public class RealisticBiomeRWSilverBirchHills extends RealisticBiomeRWBase {

    public RealisticBiomeRWSilverBirchHills(Biome biome) {

        super(biome);
    }

    @Override
    public void initConfig() {
        this.getConfig().ALLOW_SCENIC_LAKES.set(false);
    }

    @Override
    public TerrainBase initTerrain() {

        return new TerrainRWSilverBirchHills();
    }

    @Override
    public SurfaceBase initSurface() {

        return new SurfaceRWSilverBirchHills(getConfig(), this.baseBiome().topBlock, this.baseBiome().fillerBlock);
    }

    public static class TerrainRWSilverBirchHills extends TerrainBase {

        private final float baseHeight = 90f;
        private final BumpyHillsEffect onTop = new BumpyHillsEffect();
        private final JitterEffect withJitter;

        public TerrainRWSilverBirchHills() {

            onTop.hillHeight = 30;
            onTop.hillWavelength = 60;
            onTop.spikeHeight = 20;
            onTop.spikeWavelength = 10;

            withJitter = new JitterEffect();
            withJitter.amplitude = 2;
            withJitter.wavelength = 5;
            withJitter.jittered = onTop;
        }

        @Override
        public float generateNoise(RTGWorld rtgWorld, int x, int y, float border, float river) {

            return riverized(baseHeight + withJitter.added(rtgWorld, x, y) + groundNoise(x, y, 6, rtgWorld), river);
            //return terrainGrasslandMountains(x, y, simplex, cell, river, 4f, 80f, 68f);
        }
    }

    public static class SurfaceRWSilverBirchHills extends SurfaceBase {

        public SurfaceRWSilverBirchHills(BiomeConfig config, IBlockState top, IBlockState filler) {

            super(config, top, filler);
        }

        @Override
        public void paintTerrain(ChunkPrimer primer, int i, int j, int x, int z, int depth, RTGWorld rtgWorld, float[] noise, float river, Biome[] base) {

            Random rand = rtgWorld.rand();
            float c = TerrainBase.calcCliff(x, z, noise);
            boolean cliff = c > 1.4f;

            for (int k = 255; k > -1; k--) {
                Block b = primer.getBlockState(x, k, z).getBlock();
                if (b == Blocks.AIR) {
                    depth = -1;
                } else if (b == Blocks.STONE) {
                    depth++;

                    if (cliff) {
                        if (depth > -1 && depth < 2) {
                            if (rand.nextInt(3) == 0) {

                                primer.setBlockState(x, k, z, hcCobble());
                            } else {

                                primer.setBlockState(x, k, z, hcStone());
                            }
                        } else if (depth < 10) {
                            primer.setBlockState(x, k, z, hcStone());
                        }
                    } else {
                        if (depth == 0 && k > 61) {
                            primer.setBlockState(x, k, z, topBlock);
                        } else if (depth < 4) {
                            primer.setBlockState(x, k, z, fillerBlock);
                        }
                    }
                }
            }
        }
    }
}
