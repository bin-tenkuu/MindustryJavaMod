package com.bin;

import arc.Events;
import arc.util.Log;
import com.bin.content.contentLists.MyBulletList;
import com.bin.content.contentLists.MyContextList;
import com.bin.content.contentLists.MyLogicList;
import com.bin.content.contentLists.MyTechTreeList;
import com.bin.ui.HelpDialog;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.Icon;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.modules.ItemModule;

/**
 * @author bin
 */
public class TestMod extends mindustry.mod.Mod {
  public static TestMod instance;

  public TestMod() {
    Log.info(("加载TestMod构造器"));
    TestMod.instance = this;
  }

  @Override
  public void init() {
    Log.info(("加载TestMod init"));

    Events.on(EventType.BlockDestroyEvent.class, this::blockDestroyEvent);

    Vars.ui.logic.buttons.button("help", Icon.bookOpen, () -> {
      HelpDialog dialog = new HelpDialog("help");
      dialog.cont.add("这是一个text");
      dialog.show();
    });

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
    }
  }

  @Override
  public void loadContent() {
    Log.info("加载TestMod方块");

    new MyLogicList().load();

    new MyBulletList().load();

    new MyContextList().load();

    new MyTechTreeList().load();

    Log.info("加载TestMod方块完成");
  }

}
