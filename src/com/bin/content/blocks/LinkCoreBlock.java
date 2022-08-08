//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.bin.content.blocks;

import arc.graphics.g2d.Draw;
import arc.scene.ui.layout.Table;
import arc.util.io.Reads;
import arc.util.io.Writes;
import com.bin.TestMod;
import com.bin.Tools;
import mindustry.Vars;
import mindustry.gen.Building;
import mindustry.type.Item;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.blocks.storage.CoreBlock;
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
        barMap.remove("items");
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
            if (outputItem != null) {
                CoreBlock.CoreBuild core = TestMod.core;
                if (core == null) {
                    return;
                }
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
            CoreBlock.CoreBuild core = TestMod.core;
            return outputItem == null && !(source instanceof LinkCoreBuild) &&
                    core != null && core.acceptItem(source, item);
        }

        @Override
        public void handleItem(Building source, Item item) {
            CoreBlock.CoreBuild core = TestMod.core;
            if (core != null && core.acceptItem(source, item)) {
                core.items.add(item, 1);
            } else {
                StorageBlock.incinerateEffect(this, source);
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
