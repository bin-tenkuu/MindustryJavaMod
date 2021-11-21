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
import mindustry.gen.Building;
import mindustry.type.Item;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.blocks.storage.StorageBlock;
import mindustry.world.meta.BlockGroup;

/**
 * @author bin
 */
public class LinkCoreBlock extends Block {

    public LinkCoreBlock(String name) {
        super(name);
        hasItems = true;
        update = true;
        solid = true;
        group = BlockGroup.transportation;
        configurable = true;
        noUpdateDisabled = true;
        config(Item.class, (LinkCoreBuild tile, Item item) -> tile.outputItem = item);
        configClear((LinkCoreBuild tile) -> tile.outputItem = null);
    }

    @Override
    public void setBars() {
        super.setBars();
        bars.remove("items");
    }

    @Override
    public int minimapColor(Tile tile) {
        Item item = ((LinkCoreBuild) tile.build).outputItem;
        return item == null ? 0 : item.color.rgba();
    }

    @Override
    protected void initBuilding() {
        buildType = LinkCoreBuild::new;
    }

    public static class LinkCoreBuild extends Building {
        public Item outputItem = null;
        private Building core;

        public LinkCoreBuild() {

        }

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
            if (core == null) {
                core = core();
            }
            if (outputItem != null && core != null) {
                items = core.items;
                dump(outputItem);
            }
        }

        @Override
        public void buildConfiguration(Table table) {
            Tools.buildItemSelectTable(table, Vars.content.items(), () -> outputItem, this::configure);
        }

        @Override
        public boolean acceptItem(Building source, Item item) {
            return core != null && outputItem == null && !(source instanceof LinkCoreBuild) &&
                    core.items.get(item) < core.getMaximumAccepted(item);
        }

        @Override
        public void handleItem(Building source, Item item) {
            if (core == null) {
                return;
            }
            if (core.items.get(item) >= core.getMaximumAccepted(item)) {
                StorageBlock.incinerateEffect(this, source);
            } else {
                core.items.add(item, 1);
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
