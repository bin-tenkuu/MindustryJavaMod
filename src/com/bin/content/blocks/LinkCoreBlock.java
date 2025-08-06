//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.bin.content.blocks;

import arc.graphics.g2d.Draw;
import arc.scene.ui.layout.Table;
import arc.util.io.Reads;
import arc.util.io.Writes;
import com.bin.Tools;
import mindustry.Vars;
import mindustry.content.Items;
import mindustry.gen.Building;
import mindustry.gen.Teamc;
import mindustry.type.Category;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.blocks.storage.StorageBlock;
import mindustry.world.meta.BlockGroup;
import mindustry.world.meta.BuildVisibility;

import static mindustry.Vars.net;
import static mindustry.Vars.state;

/**
 * @author bin
 */
public class LinkCoreBlock extends Block {

    public LinkCoreBlock() {
        super("Bin_LinkCore");
        hasItems = false;
        update = true;
        solid = true;
        group = BlockGroup.transportation;
        configurable = true;
        underBullets = true;
        noUpdateDisabled = true;
        researchCostMultiplier = 1f;
        config(Item.class, (LinkCoreBuild tile, Item item) -> tile.outputItem = item);
        configClear((LinkCoreBuild tile) -> tile.outputItem = null);

        localizedName = "量子核心连接器";
        description = "通过量子通道与核心相连,同时装载了装卸器";
        requirements(Category.distribution, BuildVisibility.shown, ItemStack.with(Items.copper, 50));
        buildCostMultiplier = 0.1f;
        size = 1;
        health = 320;
    }

    @Override
    public void setBars() {
        super.setBars();
        barMap.remove("items");
    }

    @Override
    public int minimapColor(Tile tile) {
        Item item = ((LinkCoreBuild) tile.build).outputItem;
        return item == null ? 0 : item.color.rgba();
    }

    @Override
    public boolean outputsItems() {
        return true;
    }

    @Override
    protected void initBuilding() {
        subclass = LinkCoreBlock.class;
        buildType = LinkCoreBuild::new;
    }

    public static class LinkCoreBuild extends Building {
        public Item outputItem = null;
        private CoreBlock.CoreBuild core;

        @Override
        public void draw() {
            super.draw();
            if (outputItem == null) {
                Draw.rect("cross", x, y);
            } else {
                Draw.color();
                Draw.rect(outputItem.uiIcon, x, y);
            }
        }

        @Override
        public void updateTile() {
            if (core == null || core.dead()) {
                core = team.core();
                if (core == null) {
                    return;
                }
            }
            var outputItem = this.outputItem;
            if (outputItem == null) {
                return;
            }
            if (!core.items.has(outputItem)) {
                return;
            }
            for (int i = 0; i < proximity.size; ++i) {
                incrementDump(proximity.size);
                Building other = proximity.get(cdump);
                if (other instanceof CoreBlock.CoreBuild
                    || other instanceof LinkCoreBuild) {
                    continue;
                }
                if (other.acceptItem(this, outputItem)) {
                    core.removeStack(outputItem, 1);
                    other.handleItem(this, outputItem);
                    return;
                }

            }

        }

        @Override
        public void buildConfiguration(Table table) {
            Tools.buildItemSelectTable(table, Vars.content.items(), () -> outputItem, this::configure);
        }

        @Override
        public boolean acceptItem(Building source, Item item) {
            return outputItem == null && !(source instanceof LinkCoreBuild) &&
                   core != null && core.acceptItem(source, item);
        }

        public int acceptStack(Item item, int amount, Teamc source) {
            return core == null ? 0 : core.acceptStack(item, amount, source);
        }

        public int removeStack(Item item, int amount) {
            return core == null ? 0 : core.removeStack(item, amount);
        }

        @Override
        public void handleItem(Building source, Item item) {
            if (core == null) {
                return;
            }

            if (team == state.rules.defaultTeam) {
                state.stats.coreItemCount.increment(item);
            }

            final var items = core.items;
            if (net.server() || !net.active()) {
                if (team == state.rules.defaultTeam && state.isCampaign()) {
                    state.rules.sector.info.handleCoreItem(item, 1);
                }

                if (items.get(item) >= core.storageCapacity) {
                    // create item incineration effect at random intervals
                    StorageBlock.incinerateEffect(this, source);
                } else {
                    items.add(item, 1);
                }
            } else if (((state.rules.coreIncinerates && items.get(item) >= core.storageCapacity))) {
                // create item incineration effect at random intervals
                StorageBlock.incinerateEffect(this, source);
            }
        }

        public void handleStack(Item item, int amount, Teamc source) {
            if (core != null) {
                core.handleStack(item, amount, source);
            }
        }

        @Override
        public Item config() {
            return outputItem;
        }

        @Override
        public void write(Writes write) {
            super.write(write);
            write.s(outputItem == null ? -1 : outputItem.id);
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);
            outputItem = Vars.content.item(read.s());
        }
    }
}
