package com.bin.content.blocks;

import arc.func.Cons2;
import arc.graphics.g2d.Draw;
import arc.scene.ui.layout.Table;
import arc.util.Eachable;
import arc.util.io.Reads;
import arc.util.io.Writes;
import com.bin.Tools;
import mindustry.Vars;
import mindustry.entities.units.BuildPlan;
import mindustry.gen.Building;
import mindustry.type.Item;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.meta.BlockGroup;

/**
 * @author bin
 */
public class ItemChange extends Block {
    public ItemChange(String name) {
        super(name);
        hasItems = true;
        update = true;
        solid = true;
        group = BlockGroup.transportation;
        configurable = true;
        saveConfig = true;
        noUpdateDisabled = true;
        config(Item.class, (Cons2<ItemChangeBuild, Item>) ItemChangeBuild::config);
        configClear(ItemChangeBuild::configClear);
        itemCapacity = 1;
    }

    @Override
    public void setBars() {
        super.setBars();
        barMap.remove("items");
    }

    @Override
    public void drawPlanConfig(BuildPlan req, Eachable<BuildPlan> list) {
        drawPlanConfigCenter(req, req.config, "center");
    }

    @Override
    public boolean outputsItems() {
        return true;
    }

    @Override
    public int minimapColor(Tile tile) {
        Item item = ((ItemChangeBuild) tile.build).outputItem;
        return item == null ? 0 : item.color.rgba();
    }

    @Override
    protected void initBuilding() {
        buildType = ItemChangeBuild::new;
    }

    public static class ItemChangeBuild extends Building {
        public void config(Item item) {
            outputItem = item;
        }

        public void configClear() {
            outputItem = null;
        }

        public Item outputItem;

        public ItemChangeBuild() {
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
            if (items.any()) {
                Item take = items.take();
                offload(outputItem == null ? take : outputItem);
            }
        }

        @Override
        public void consume() {
            items.take();
        }

        @Override
        public void buildConfiguration(Table table) {
            Tools.buildItemSelectTable(table, Vars.content.items(), () -> outputItem, this::configure);
        }

        @Override
        public boolean onConfigureBuildTapped(Building other) {
            if (this == other) {
                deselect();
                configure(null);
                return false;
            } else {
                return true;
            }
        }

        @Override
        public boolean acceptItem(Building source, Item item) {
            return outputItem != item && items.total() < getMaximumAccepted(item);
        }

        @Override
        public Item config() {
            return outputItem;
        }

        @Override
        public boolean canDump(Building to, Item item) {
            return item.equals(outputItem);
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
