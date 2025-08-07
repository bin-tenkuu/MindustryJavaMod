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
        description = """
                全新复制科技
                [green]物品[]/[blue]液体[]进入时在其他方向同时复制一份
                """;
        requirements(Category.distribution, BuildVisibility.shown, ItemStack.with(Items.copper, 50));
        buildCostMultiplier = 0.1f;
        size = 1;
        health = 320;
    }

    @Override
    public void setBars() {
        super.setBars();
        removeBar("liquid");
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

    public class CopyGateBuild extends Building {

        @Override
        public boolean acceptItem(Building source, Item item) {
            if (source == null || team != source.team || item == null || source instanceof CopyGateBuild) {
                return false;
            }

            for (var i = 0; i < proximity.size; i++) {
                var other = proximity.get(i);
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
            if (source == null || team != source.team || item == null || source instanceof CopyGateBuild) {
                return;
            }

            for (var i = 0; i < proximity.size; i++) {
                var other = proximity.get(i);
                if (other == source || other == this) {
                    continue;
                }

                if (other.acceptItem(this, item)) {
                    other.handleItem(this, item);
                }
            }
        }

        @Override
        public void updateTile() {
            for (var i = 0; i < this.proximity.size; ++i) {
                var other = this.proximity.get(i);
                if (other == null || !other.block.hasLiquids) {
                    continue;
                }
                var liquids = other.liquids;
                if (liquids == null || liquids.currentAmount() <= 1E-4F) {
                    continue;
                }
                var liquid = liquids.current();
                other = other.getLiquidDestination(this, liquid);
                if (other == null) {
                    continue;
                }
                liquids = other.liquids;
                if (other.acceptLiquid(this, liquid)) {
                    var flow = Math.min(liquidCapacity, other.block.liquidCapacity - liquids.get(liquid));
                    if (flow <= 1E-4F) {
                        continue;
                    }
                    other.handleLiquid(this, liquid, flow);
                }
            }
        }

        @Override
        public boolean acceptLiquid(Building source, Liquid liquid) {
            return false;
        }

    }
}
