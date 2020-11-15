package com;

import arc.Events;
import arc.util.Log;
import com.content.MyContextList;
import com.content.MyTechTreeList;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.mod.Mod;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.modules.ItemModule;

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
    Log.info(("加载TestMod init完成"));
  }

  private void blockDestroyEvent(EventType.BlockDestroyEvent e) {
    if (Vars.player.team().isEnemy(e.tile.team())) {
      Block block = e.tile.block();
      ItemModule items = Vars.player.team().items();
      if (items.length() == 0) {
        return;
      }
      CoreBlock.CoreBuild core = Vars.player.team().core();
      for (ItemStack stack : block.requirements) {
        if (core.acceptItem(null, stack.item)) {
          core.items.add(stack.item, stack.amount);
        }
      }
//      items.add(Arrays.asList(block.requirements));

//      Table t = new Table(Styles.black3);
//      t.touchable = Touchable.disabled;
//      t.setPosition(e.tile.x, e.tile.y);
//      new LiquidListValue(false, block.requirements).display(t);
//      t.actions(Actions.show(), Actions.delay(5, Actions.remove()));
//      t.pack();
//      Core.scene.add(t);
    }
  }

  @Override
  public void loadContent() {
    Log.info("加载TestMod方块");
    new MyContextList().load();
    new MyTechTreeList().load();
    Log.info("加载TestMod方块完成");
  }

}
