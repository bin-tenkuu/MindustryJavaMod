//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.content.blocks;

import arc.graphics.g2d.Draw;
import arc.scene.ui.layout.Table;
import arc.util.io.Reads;
import arc.util.io.Writes;
import com.Tools;
import mindustry.Vars;
import mindustry.gen.Building;
import mindustry.type.Item;
import mindustry.ui.Cicon;
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
    this.hasItems = true;
    this.update = true;
    this.solid = true;
    this.group = BlockGroup.transportation;
    this.configurable = true;
    this.noUpdateDisabled = true;
    this.config(Item.class, (LinkCoreBuild tile, Item item) -> tile.outputItem = item);
    this.configClear((LinkCoreBuild tile) -> tile.outputItem = null);
  }

  @Override public void setBars() {
    super.setBars();
    this.bars.remove("items");
  }

  @Override public int minimapColor(Tile tile) {
    Item item = ((LinkCoreBuild)tile.bc()).outputItem;
    return item == null ? 0 : item.color.rgba();
  }

//  @Override public void drawPlace(int x, int y, int rotation, boolean valid) {
//    super.drawPlace(x, y, rotation, valid);
//  }

  @Override protected void initBuilding() {
    this.buildType = LinkCoreBuild::new;
  }

  public static class LinkCoreBuild extends Building {
    public Item outputItem = null;
    private Building core;

    public LinkCoreBuild() {

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
      this.core = this.core();
      if (this.outputItem != null) {
        this.items = this.core.items;
        this.dump(this.outputItem);
      }
    }

    @Override public void buildConfiguration(Table table) {
      Tools.buildItemSelectTable(table, Vars.content.items(), () -> this.outputItem, this::configure);
    }

    @Override public boolean acceptItem(Building source, Item item) {
      return this.core != null && this.outputItem == null && !(source instanceof LinkCoreBuild);
    }

    @Override public void handleItem(Building source, Item item) {
      if (core == null) {
        return;
      }
      if (core.items.get(item) >= core.getMaximumAccepted(item)) {
        StorageBlock.incinerateEffect(this, source);
      } else {
        core.items.add(item, 1);
      }
    }

    @Override public Item config() {
      return this.outputItem;
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
