package com.bin.content.blocks;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.struct.ObjectIntMap;
import arc.struct.Seq;
import arc.util.Eachable;
import mindustry.Vars;
import mindustry.content.Items;
import mindustry.entities.units.BuildPlan;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.graphics.Pal;
import mindustry.type.Item;
import mindustry.ui.Bar;
import mindustry.ui.Cicon;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.blocks.environment.Floor;
import mindustry.world.meta.BlockGroup;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;
import mindustry.world.meta.values.BlockFilterValue;

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
    this.hasItems = true;
    this.update = true;
    this.solid = true;
    this.group = BlockGroup.drills;
    this.hasLiquids = false;
    this.itemCapacity = 1;
  }

  @Override public void drawRequestConfigTop(BuildPlan req, Eachable<BuildPlan> list) {
    if (req.worldContext) {
      Tile tile = req.tile();
      if (tile != null) {
        this.countOre(req.tile());
        if (this.returnItem != null) {
          Draw.color();
          Draw.rect(this.returnItem.icon(Cicon.small), req.drawx(), req.drawy());
        }
      }
    }
  }

  @Override public void setBars() {
    super.setBars();
    this.bars.add("drillspeed", (FastestDrillBuild e) -> new Bar(
        () -> Core.bundle.format("bar.drillspeed", e.drilled ? "60" : "0"),
        () -> Pal.ammo,
        () -> e.drilled ? 1 : 0
    ));
  }

  @Override public boolean canPlaceOn(Tile tile, Team team) {
    if (this.isMultiblock()) {
      Seq<Tile> tiles = tile.getLinkedTilesAs(this, tempTiles);
      for (Tile t : tiles) {
        if (this.canMine(t)) {
          return true;
        }
      }
      return false;
    } else {
      return this.canMine(tile);
    }
  }

  @Override public void drawPlace(int x, int y, int rotation, boolean valid) {
    Tile tile = Vars.world.tile(x, y);
    if (tile == null) {
      return;
    }
    this.countOre(tile);
    if (this.returnItem != null) {
      float width = this.drawPlaceText(Core.bundle.format("bar.drillspeed", "60"), x, y, valid);
      float dx = (float)(x * 8) + this.offset - width / 2.0F - 4.0F;
      float dy = (float)(y * 8) + this.offset + (float)(this.size * 8) / 2.0F + 5.0F;
      Draw.mixcol(Color.darkGray, 1.0F);
      Draw.rect(this.returnItem.icon(Cicon.small), dx, dy - 1.0F);
      Draw.reset();
      Draw.rect(this.returnItem.icon(Cicon.small), dx, dy);
    } else {
      Tile to = tile.getLinkedTilesAs(this, tempTiles).find((t) -> false);
      Item item = to == null ? null : to.drop();
      if (item != null) {
        this.drawPlaceText(Core.bundle.get("bar.drilltierreq"), x, y, valid);
      }
    }
  }

  @Override public void setStats() {
    super.setStats();
    this.stats.add(Stat.drillTier, new BlockFilterValue((b) ->
        b instanceof Floor && ((Floor)b).itemDrop != null)
    );
    this.stats.add(Stat.drillSpeed, table -> {
      table.add("60 ");
      table.add(StatUnit.itemsSecond.localized());
    });
  }

  @Override public TextureRegion[] icons() {
    return new TextureRegion[]{this.region};
  }

  private void countOre(Tile tile) {
    this.returnItem = null;
    this.oreCount.clear();
    this.itemArray.clear();

    for (Tile other : tile.getLinkedTilesAs(this, tempTiles)) {
      if (this.canMine(other)) {
        this.oreCount.increment(this.getDrop(other), 0, 1);
      }
    }

    for (Item item : this.oreCount.keys()) {
      this.itemArray.add(item);
    }

    this.itemArray.sort((item1, item2) -> {
      int type = Boolean.compare(item1 != Items.sand, item2 != Items.sand);
      if (type != 0) {
        return type;
      } else {
        int amounts = Integer.compare(this.oreCount.get(item1, 0), this.oreCount.get(item2, 0));
        return amounts != 0 ? amounts : Integer.compare(item1.id, item2.id);
      }
    });
    if (this.itemArray.size != 0) {
      this.returnItem = this.itemArray.peek();
    }
  }

  public boolean canMine(Tile tile) {
    return tile != null && tile.drop() != null;
  }

  public Item getDrop(Tile tile) {
    return tile.drop();
  }

  @Override protected void initBuilding() {
    this.buildType = FastestDrillBuild::new;
  }

  public class FastestDrillBuild extends Building {
    public boolean drilled;
    public Item dominantItem;

    public FastestDrillBuild() {
    }

    @Override public void drawSelect() {
      if (this.dominantItem != null) {
        float size = FastestDrill.this.size * 4;
        float dx = this.x - size;
        float dy = this.y + size;
        Draw.mixcol(Color.darkGray, 1.0F);
        Draw.rect(this.dominantItem.icon(Cicon.small), dx, dy - 1.0F);
        Draw.reset();
        Draw.rect(this.dominantItem.icon(Cicon.small), dx, dy);
      }
    }

    @Override public void onProximityUpdate() {
      FastestDrill.this.countOre(this.tile);
      this.dominantItem = FastestDrill.this.returnItem;
    }

    @Override public boolean shouldConsume() {
      return this.items.total() < this.block.itemCapacity;
    }

    @Override public void updateTile() {
      if (this.dominantItem == null) {
        return;
      }
      this.dump(this.dominantItem);
      if (this.cons.valid()) {
        this.offload(this.dominantItem);
        this.drilled = true;
      } else {
        this.drilled = false;
      }
    }

    @Override public void drawCracks() {
    }

    @Override public void draw() {
      super.draw();
      if (this.dominantItem != null) {
        Draw.color();
        Draw.rect(this.dominantItem.icon(Cicon.medium), this.x, this.y);
      }
    }
  }
}
