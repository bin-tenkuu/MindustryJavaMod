package com.bin.content.blocks;

import mindustry.content.Items;
import mindustry.gen.Building;
import mindustry.type.Category;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.type.Liquid;
import mindustry.world.Block;
import mindustry.world.meta.BlockGroup;
import mindustry.world.meta.BuildVisibility;
import mindustry.world.meta.Env;

/**
 * @author bin
 * @since 2025/08/06
 */
public class CopyGate extends Block {

    public CopyGate() {
        super("Bin_CopyGate");
        hasItems = true;
        hasLiquids = true;

        update = true;
        solid = true;
        group = BlockGroup.projectors;
        outputsLiquid = true;

        itemCapacity = 0;
        liquidCapacity = 120f;

        envEnabled = Env.any;

        underBullets = true;
        destructible = true;
        instantTransfer = true;
        unloadable = false;
        canOverdrive = false;
        researchCostMultiplier = 1f;
        noUpdateDisabled = true;
        floating = true;
        explosivenessScale = 0;

        localizedName = "复制器";
        description = "全新复制科技, 1进3出, 支持液体";
        requirements(Category.distribution, BuildVisibility.shown, ItemStack.with(Items.copper, 50));
        buildCostMultiplier = 0.1f;
        size = 1;
        health = 320;
    }

    @Override
    public boolean outputsItems() {
        return true;
    }

    @Override
    protected void initBuilding() {
        subclass = CopyGate.class;
        buildType = CopyGateBuild::new;
    }

    public static class CopyGateBuild extends Building {

        @Override
        public boolean acceptItem(Building source, Item item) {
            if (team != source.team || item == null) {
                return false;
            }

            for (int i = 0; i < proximity.size; i++) {
                Building other = proximity.get(i);
                if (other == source || other == this) {
                    continue;
                }
                if (other.acceptItem(this, item)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public void handleItem(Building source, Item item) {
            if (team != source.team || item == null) {
                return;
            }

            for (int i = 0; i < proximity.size; i++) {
                Building other = proximity.get(i);
                if (other == source || other == this) {
                    continue;
                }

                if (other.acceptItem(this, item)) {
                    other.handleItem(this, item);
                }
            }
        }

        @Override
        public boolean acceptLiquid(Building source, Liquid liquid) {
            if (source == null || liquid == null || source instanceof CopyGateBuild) {
                return false;
            }
            return source.acceptLiquid(source, liquid);
        }

        @Override
        public void handleLiquid(Building source, Liquid liquid, float amount) {
            if (source == null || liquid == null || amount <= 1.0E-4F || source instanceof CopyGateBuild) {
                return;
            }
            source.handleLiquid(source, liquid, amount);
        }

    }
}
