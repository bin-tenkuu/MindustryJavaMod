package com.bin.content.blocks;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.struct.ObjectIntMap;
import arc.struct.Seq;
import arc.util.Eachable;
import com.bin.TestMod;
import mindustry.Vars;
import mindustry.content.Items;
import mindustry.entities.units.BuildPlan;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.graphics.Pal;
import mindustry.type.Item;
import mindustry.ui.Bar;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.blocks.environment.Floor;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.meta.BlockGroup;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;
import mindustry.world.meta.StatValues;

/**
 * @author bin
 * @version 1.0.0
 */
public class FastestDrill extends Block {
    protected final ObjectIntMap<Item> oreCount = new ObjectIntMap<>();
    protected final Seq<Item> itemArray = new Seq<>();
    protected Item returnItem;

    public FastestDrill(String name) {
        super(name);
        hasItems = true;
        update = true;
        solid = true;
        group = BlockGroup.drills;
        hasLiquids = false;
        itemCapacity = 1;
    }

    @Override
    public void drawPlanConfigTop(BuildPlan req, Eachable<BuildPlan> list) {
        if (req.worldContext) {
            Tile tile = req.tile();
            if (tile != null) {
                countOre(req.tile());
                if (returnItem != null) {
                    Draw.color();
                    Draw.rect(returnItem.uiIcon, req.drawx(), req.drawy());
                }
            }
        }
    }

    @Override
    public void setBars() {
        super.setBars();
       addBar("drillspeed", (FastestDrillBuild e) -> new Bar(
                () -> Core.bundle.format("bar.drillspeed", e.drilled ? "60" : "0"),
                () -> Pal.ammo,
                () -> e.drilled ? 1 : 0
        ));
    }

    @Override
    public boolean canPlaceOn(final Tile tile, final Team team, final int rotation) {
        if (isMultiblock()) {
            Seq<Tile> tiles = tile.getLinkedTilesAs(this, tempTiles);
            for (Tile t : tiles) {
                if (canMine(t)) {
                    return true;
                }
            }
            return false;
        } else {
            return canMine(tile);
        }
    }

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid) {
        Tile tile = Vars.world.tile(x, y);
        if (tile == null) {
            return;
        }
        countOre(tile);
        if (returnItem != null) {
            float width = drawPlaceText(Core.bundle.format("bar.drillspeed", "60"), x, y, valid);
            float dx = (float) (x * 8) + offset - width / 2.0F - 4.0F;
            float dy = (float) (y * 8) + offset + (float) (size * 8) / 2.0F + 5.0F;
            Draw.mixcol(Color.darkGray, 1.0F);
            Draw.rect(returnItem.uiIcon, dx, dy - 1.0F);
            Draw.reset();
            Draw.rect(returnItem.uiIcon, dx, dy);
        } else {
            Tile to = tile.getLinkedTilesAs(this, tempTiles).find((t) -> false);
            Item item = to == null ? null : to.drop();
            if (item != null) {
                drawPlaceText(Core.bundle.get("bar.drilltierreq"), x, y, valid);
            }
        }
    }

    @Override
    public void setStats() {
        super.setStats();
        stats.add(Stat.drillTier, StatValues.blocks(b -> b instanceof Floor && b.itemDrop != null));
        stats.add(Stat.drillSpeed, table -> {
            table.add("60 ");
            table.add(StatUnit.itemsSecond.localized());
        });
    }

    @Override
    public TextureRegion[] icons() {
        return new TextureRegion[]{region};
    }

    private void countOre(Tile tile) {
        returnItem = null;
        oreCount.clear();
        itemArray.clear();

        for (Tile other : tile.getLinkedTilesAs(this, tempTiles)) {
            if (canMine(other)) {
                oreCount.increment(getDrop(other), 0, 1);
            }
        }

        for (Item item : oreCount.keys()) {
            itemArray.add(item);
        }

        itemArray.sort((item1, item2) -> {
            int type = Boolean.compare(item1 != Items.sand, item2 != Items.sand);
            if (type != 0) {
                return type;
            } else {
                int amounts = Integer.compare(oreCount.get(item1, 0), oreCount.get(item2, 0));
                return amounts != 0 ? amounts : Integer.compare(item1.id, item2.id);
            }
        });
        if (itemArray.size != 0) {
            returnItem = itemArray.peek();
        }
    }

    public boolean canMine(Tile tile) {
        return tile != null && tile.drop() != null;
    }

    public Item getDrop(Tile tile) {
        return tile.drop();
    }

    @Override
    protected void initBuilding() {
        buildType = FastestDrillBuild::new;
    }

    public class FastestDrillBuild extends Building {
        public boolean drilled;
        public Item dominantItem;

        public FastestDrillBuild() {
        }

        @Override
        public void drawSelect() {
            if (dominantItem != null) {
                float size = FastestDrill.this.size * 4;
                float dx = x - size;
                float dy = y + size;
                Draw.mixcol(Color.darkGray, 1.0F);
                Draw.rect(dominantItem.uiIcon, dx, dy - 1.0F);
                Draw.reset();
                Draw.rect(dominantItem.uiIcon, dx, dy);
            }
        }

        @Override
        public void onProximityUpdate() {
            countOre(tile);
            dominantItem = returnItem;
        }

        @Override
        public boolean shouldConsume() {
            CoreBlock.CoreBuild core = TestMod.core;
            return core != null && core.shouldConsume();
        }

        @Override
        public void updateTile() {
            if (dominantItem == null) {
                return;
            }
            CoreBlock.CoreBuild core = TestMod.core;
            if (core == null || !core.isAdded() || core.team != Vars.player.team()) {
                TestMod.CACHE.exec();
                return;
            }
            items = core.items;
//            dump(dominantItem);
            if (core.acceptItem(null, dominantItem)) {
//                handleItem(this, dominantItem);
                offload(dominantItem);
                drilled = true;
            } else {
                drilled = false;
            }
        }

        @Override
        public void handleItem(final Building source, final Item item) {
            if (acceptItem(null, item)) {
                items.add(item, 1);
            }
        }

        @Override
        public boolean acceptItem(final Building source, final Item item) {
            CoreBlock.CoreBuild core = TestMod.core;
            return !(source instanceof FastestDrillBuild) && core != null && core.acceptItem(source, item);
        }

        @Override
        public void drawCracks() {
        }

        @Override
        public void draw() {
            super.draw();
            if (dominantItem != null) {
                Draw.color();
                Draw.rect(dominantItem.uiIcon, x, y);
            }
        }
    }
}
