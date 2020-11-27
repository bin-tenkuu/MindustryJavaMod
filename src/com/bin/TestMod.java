package com;

import arc.Events;
import arc.util.Log;
import com.content.contentLists.MyBulletList;
import com.content.contentLists.MyContextList;
import com.content.contentLists.MyLogicList;
import com.content.contentLists.MyTechTreeList;
import com.ui.HelpDialog;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.Icon;
import mindustry.mod.Mod;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.modules.ItemModule;

/**
 * @author bin
 */
public class TestMod extends Mod {
  public static TestMod instance;

  public TestMod() {
    Log.info(("����TestMod������"));
    TestMod.instance = this;
  }

  @Override
  public void init() {
    Log.info(("����TestMod init"));

    Events.on(EventType.BlockDestroyEvent.class, this::blockDestroyEvent);

    Vars.ui.logic.buttons.button("help", Icon.bookOpen, () -> {
      HelpDialog dialog = new HelpDialog("help");
      dialog.cont.add("����һ��text");
      dialog.show();
    });

    Log.info(("����TestMod init���"));
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
    Log.info("����TestMod����");

    new MyLogicList().load();

    new MyBulletList().load();

    new MyContextList().load();

    new MyTechTreeList().load();

    Log.info("����TestMod�������");
  }

}
