package com.bin.content.blocks;

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
import mindustry.ui.Cicon;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.meta.BlockGroup;

/**
 * @author bin
 */
public class ItemChange extends Block {
  public ItemChange(String name) {
    super(name);
    this.hasItems = true;
    this.update = true;
    this.solid = true;
    this.group = BlockGroup.transportation;
    this.configurable = true;
    this.saveConfig = true;
    this.noUpdateDisabled = true;
    this.config(Item.class, (ItemChangeBuild tile, Item item) -> tile.outputItem = item);
    this.configClear((ItemChangeBuild tile) -> tile.outputItem = null);
    this.itemCapacity = 1;
  }

  @Override public void setBars() {
    super.setBars();
    this.bars.remove("items");
  }

  @Override public void drawRequestConfig(BuildPlan req, Eachable<BuildPlan> list) {
    this.drawRequestConfigCenter(req, req.config, "center");
  }

  @Override public boolean outputsItems() {
    return true;
  }

  @Override public int minimapColor(Tile tile) {
    Item item = ((ItemChangeBuild)tile.bc()).outputItem;
    return item == null ? 0 : item.color.rgba();
  }

  @Override protected void initBuilding() {
    this.buildType = ItemChangeBuild::new;
  }

  public static class ItemChangeBuild extends Building {
    public Item outputItem;

    public ItemChangeBuild() {
    }

    @Override public void draw() {
      super.draw();
      if (this.outputItem == null) {
        Draw.rect("cross", this.x, this.y);
      } else {
        Draw.color();
        Draw.rect(this.outputItem.icon(Cicon.medium), this.x, this.y);
      }
    }

    @Override public void updateTile() {
      if (this.items.any()) {
        Item take = this.items.take();
        this.offload(this.outputItem == null ? take : this.outputItem);
      }
    }

    @Override public void consume() {
      this.items.take();
    }

    @Override public void buildConfiguration(Table table) {
      Tools.buildItemSelectTable(table, Vars.content.items(), () -> this.outputItem, this::configure);
    }

    @Override public boolean onConfigureTileTapped(Building other) {
      if (this == other) {
        this.deselect();
        this.configure(null);
        return false;
      } else {
        return true;
      }
    }

    @Override public boolean acceptItem(Building source, Item item) {
      return this.items.total() < this.getMaximumAccepted(item);
    }

    @Override public Item config() {
      return this.outputItem;
    }

    @Override public boolean canDump(Building to, Item item) {
      return item.equals(this.outputItem);
    }

    @Override public void write(Writes write) {
      super.write(write);
      write.s(this.outputItem == null ? -1 : this.outputItem.id);
    }

    @Override public void read(Reads read, byte revision) {
      super.read(read, revision);
      this.outputItem = Vars.content.item(read.s());
    }
  }
}