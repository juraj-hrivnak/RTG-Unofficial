package rtg.api.world.deco.collection;

import net.minecraft.block.BlockFlower.EnumFlowerType;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.init.Blocks;
import net.minecraft.world.gen.feature.WorldGenMegaJungle;
import rtg.api.config.BiomeConfig;
import rtg.api.util.BlockUtil;
import rtg.api.world.deco.*;
import rtg.api.world.deco.helper.DecoHelperThisOrThat;
import rtg.api.world.gen.feature.tree.rtg.TreeRTG;
import rtg.api.world.gen.feature.tree.rtg.TreeRTGCocosNucifera;
import rtg.api.world.gen.feature.tree.rtg.TreeRTGRhizophoraMucronata;


/**
 * @author WhichOnesPink
 */
public class DecoCollectionJungle extends DecoCollectionBase {

    public DecoCollectionJungle(BiomeConfig config) {

        super(config);

        // Blend of the WorldGenMegaJungle collection and some tall RTG Mangrove trees.

        TreeRTG mucronataTree = new TreeRTGRhizophoraMucronata(4, 5, 13f, 0.32f, 0.2f);
        mucronataTree.setLogBlock(BlockUtil.getStateLog(EnumType.JUNGLE));
        mucronataTree.setLeavesBlock(BlockUtil.getStateLeaf(EnumType.JUNGLE));
        mucronataTree.setMinTrunkSize(3);
        mucronataTree.setMaxTrunkSize(4);
        mucronataTree.setMinCrownSize(10);
        mucronataTree.setMaxCrownSize(27);
        this.addTree(mucronataTree);

        DecoTree mangroves = new DecoTree(new TreeRTGRhizophoraMucronata(4, 5, 13f, 0.32f, 0.2f));
        mangroves.setLoops(3);
        mangroves.setTreeType(DecoTree.TreeType.RTG_TREE);
        mangroves.setTreeCondition(DecoTree.TreeCondition.RANDOM_CHANCE);
        mangroves.setTreeConditionChance(3);
        mangroves.setMaxY(160);

        DecoTree megaJungle = new DecoTree(new WorldGenMegaJungle(false, 10, 27, BlockUtil.getStateLog(EnumType.JUNGLE), BlockUtil.getStateLeaf(EnumType.JUNGLE)));
        megaJungle.setLogBlock(BlockUtil.getStateLog(EnumType.JUNGLE));
        megaJungle.setLeavesBlock(BlockUtil.getStateLeaf(EnumType.JUNGLE));
        megaJungle.setMinTrunkSize(3);
        megaJungle.setMaxTrunkSize(4);
        megaJungle.setMinCrownSize(10);
        megaJungle.setMaxCrownSize(27);
        megaJungle.setLoops(3);
        megaJungle.setTreeType(DecoTree.TreeType.WORLDGEN);
        megaJungle.setTreeCondition(DecoTree.TreeCondition.RANDOM_CHANCE);
        megaJungle.setTreeConditionChance(3);
        megaJungle.setMaxY(160);

        DecoHelperThisOrThat decoHelperThisOrThat = new DecoHelperThisOrThat(3, DecoHelperThisOrThat.ChanceType.NOT_EQUALS_ZERO, megaJungle, mangroves);
        this.addDeco(decoHelperThisOrThat);

        // Add some palm trees for variety.

        TreeRTG nuciferaTree = new TreeRTGCocosNucifera();
        nuciferaTree.setMinTrunkSize(7);
        nuciferaTree.setMaxTrunkSize(9);
        nuciferaTree.setMinCrownSize(6);
        nuciferaTree.setMaxCrownSize(8);
        this.addTree(nuciferaTree);

        DecoTree palmCustom = new DecoTree(nuciferaTree);
        palmCustom.setLoops(1);
        palmCustom.setTreeType(DecoTree.TreeType.RTG_TREE);
        palmCustom.setTreeCondition(DecoTree.TreeCondition.RANDOM_CHANCE);
        palmCustom.setTreeConditionChance(4);
        palmCustom.setMaxY(160);
        this.addDeco(palmCustom);

        // Jungle logs.
        DecoFallenTree decoFallenTree = new DecoFallenTree();
        decoFallenTree.setLoops(1);
        decoFallenTree.getDistribution().setNoiseDivisor(100f);
        decoFallenTree.getDistribution().setNoiseFactor(5f);
        decoFallenTree.getDistribution().setNoiseAddend(0.8f);
        decoFallenTree.setLogConditionChance(3);
        decoFallenTree.setLogBlock(BlockUtil.getStateLog(EnumType.JUNGLE));
        decoFallenTree.setLeavesBlock(BlockUtil.getStateLeaf(EnumType.JUNGLE));
        decoFallenTree.setMinSize(4);
        decoFallenTree.setMaxSize(9);
        this.addDeco(decoFallenTree, config.ALLOW_LOGS.get());

        // Flowers.
        DecoFlowersRTG decoFlowersRTG = new DecoFlowersRTG()
                .addFlowers(EnumFlowerType.ORANGE_TULIP) // Only orange tulips fit in with the colour scheme.
                .setChance(4)
                .setMaxY(120)
                .setStrengthFactor(2f);
        this.addDeco(decoFlowersRTG);

        // Tall cacti on red sand - matches the colour scheme nicely.
        DecoJungleCacti decoJungleCacti = new DecoJungleCacti();
        decoJungleCacti.setStrengthFactor(8f);
        decoJungleCacti.setMaxY(120);
        decoJungleCacti.setSandOnly(false);
        decoJungleCacti.setExtraHeight(7);
        this.addDeco(decoJungleCacti, config.ALLOW_CACTUS.get());

        // Mossy boulders for the green.
        DecoBoulder decoBoulder = new DecoBoulder();
        decoBoulder.setBoulderBlock(Blocks.MOSSY_COBBLESTONE.getDefaultState());
        decoBoulder.setChance(16);
        decoBoulder.setMaxY(95);
        decoBoulder.setStrengthFactor(2f);
        this.addDeco(decoBoulder);
    }
}
