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
import mindustry.type.Liquid;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.meta.BlockGroup;

/**
 * @author bin
 */
public class LiquidChange extends Block {
    public LiquidChange(String name) {
        super(name);
        hasItems = true;
        hasLiquids = true;
        update = true;
        solid = true;
        group = BlockGroup.transportation;
        configurable = true;
        saveConfig = true;
        noUpdateDisabled = true;
        config(Liquid.class, (Cons2<LiquidChangeBuild, Liquid>) LiquidChangeBuild::config);
        configClear(LiquidChangeBuild::configClear);
        itemCapacity = 100;
        liquidCapacity = 100;
    }

    @Override
    public void setBars() {
        super.setBars();
        bars.remove("liquid");
    }

    @Override
    public void drawRequestConfig(BuildPlan req, Eachable<BuildPlan> list) {
        drawRequestConfigCenter(req, req.config, "center");
    }

    @Override
    public boolean outputsItems() {
        return false;
    }

    @Override
    public int minimapColor(Tile tile) {
        Liquid item = ((LiquidChangeBuild) tile.build).outputItem;
        return item == null ? 0 : item.color.rgba();
    }

    @Override
    protected void initBuilding() {
        buildType = LiquidChangeBuild::new;
    }

    public static class LiquidChangeBuild extends Building {
        public void config(Liquid item) {
            outputItem = item;
        }

        public void configClear() {
            outputItem = null;
        }

        public Liquid outputItem;

        public LiquidChangeBuild() {
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
                if (items.any()) {
                    items.take();
                    liquids.reset(outputItem, block.liquidCapacity);
                    dumpLiquid(outputItem,1);
                }
            } else {
                liquids.clear();
            }
        }

        @Override
        public void consume() {
            items.take();
        }

        @Override
        public void buildConfiguration(Table table) {
            Tools.buildItemSelectTable(table, Vars.content.liquids(), () -> outputItem, this::configure);
        }

        @Override
        public boolean onConfigureTileTapped(Building other) {
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
            return items.total() < getMaximumAccepted(item);
        }

        @Override
        public boolean acceptLiquid(Building source, Liquid liquid) {
            return false;
        }

        @Override
        public Liquid config() {
            return outputItem;
        }

        @Override
        public boolean canDumpLiquid(Building to, Liquid item) {
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
            outputItem = Vars.content.liquid(read.s());
        }
    }
}