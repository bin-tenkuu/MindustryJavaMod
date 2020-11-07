package com;

import arc.Core;
import arc.Events;
import arc.scene.actions.Actions;
import arc.scene.event.Touchable;
import arc.scene.ui.layout.Table;
import arc.util.Log;
import com.content.MyContextList;
import com.content.MyTechTreeList;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.mod.Mod;
import mindustry.ui.Styles;
import mindustry.world.Block;
import mindustry.world.meta.values.ItemListValue;
import mindustry.world.modules.ItemModule;

import java.util.Arrays;

/**
 * @author bin
 */
public class TestMod extends Mod {

  public TestMod() {
    Log.info(("加载TestMod构造器"));
  }

  @Override
  public void init() {
    Log.info(("加载TestMod init"));
    Events.on(EventType.BlockDestroyEvent.class, this::blockDestroyEvent);
  }

  private void blockDestroyEvent(EventType.BlockDestroyEvent e) {
    if (Vars.player.team().isEnemy(e.tile.team())) {
      Block block = e.tile.block();
      ItemModule items = Vars.player.team().items();
      if (items.length() == 0) {
        return;
      }
      items.add(Arrays.asList(block.requirements));
      Table t = new Table(Styles.black3);
      t.touchable = Touchable.disabled;
      t.setPosition(e.tile.x, e.tile.y);
      new ItemListValue(false, block.requirements).display(t);
      t.actions(Actions.show(), Actions.delay(5, Actions.remove()));
      t.pack();
      Core.scene.add(t);
    }
  }

  @Override
  public void loadContent() {
    Log.info("加载TestMod方块");
    new MyContextList().load();
    new MyTechTreeList().load();
  }

}
