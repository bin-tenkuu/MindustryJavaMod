package com.content.blocks;

import arc.scene.ui.layout.Table;
import mindustry.gen.Building;
import mindustry.world.Block;
import mindustry.world.meta.BlockGroup;
import mindustry.world.meta.StatValue;

/**
 * @author bin
 * @version 1.0.0
 */
public class CommendBlock extends Block {
  public StatValue commend;

  public CommendBlock(String name) {
    super(name);
    this.hasItems = false;
    this.update = true;
    this.solid = false;
    this.configurable = true;
    this.group = BlockGroup.transportation;
    this.alwaysUnlocked = true;
  }

  @Override protected void initBuilding() {
    this.buildType = CommendBuild::new;
  }

  public class CommendBuild extends Building {
    @Override public void buildConfiguration(Table table) {
      if (commend != null) {
        commend.display(table);
      }
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

  }
}
