package com;

import arc.Core;
import arc.Events;
import arc.scene.actions.Actions;
import arc.scene.event.Touchable;
import arc.scene.ui.layout.Table;
import arc.util.CommandHandler;
import arc.util.Log;
import com.content.MyContextList;
import com.content.MyTechTreeList;
import mindustry.Vars;
import mindustry.core.Logic;
import mindustry.core.NetClient;
import mindustry.game.EventType;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import mindustry.gen.Unitc;
import mindustry.mod.Mod;
import mindustry.type.ItemStack;
import mindustry.ui.ItemDisplay;
import mindustry.ui.Styles;
import mindustry.world.Block;
import mindustry.world.meta.values.ItemListValue;
import mindustry.world.modules.ItemModule;

import java.util.Arrays;

/**
 * @author bin
 */
@SuppressWarnings("unused")
public class TestMod extends Mod {
  private static boolean isOpen = false;
  private int times = 100;

  public TestMod() {
    Log.info(("加载TestMod构造器"));
    init();
  }

  @Override
  public void init() {
    if (isOpen) {
      Log.err(("加载TestMod构造器"));
      return;
    }
    isOpen = true;
    Events.on(EventType.UnitDestroyEvent.class, this::unitDestroyEvent);
    Events.on(EventType.BlockDestroyEvent.class, this::blockDestroyEvent);
  }

  @Override
  public void registerClientCommands(CommandHandler handler) {
    handler.register("fire", "全图迅速灭火", ModCommander::fire);
    handler.<Player>register("kill", "自杀", (args, player) -> player.unit().destroy());
    handler.register("killAll", "击杀全图单位", (args) -> Groups.unit.forEach(Unitc::destroy));
    handler.<Player>register("gameOver", "立即结束", (args, player) -> Logic.updateGameOver(player.team()));
    handler.register("waveSpacing", "[Int]", "显示/设置波数间隔", ModCommander::waveSpacing);
    handler.register("buildSpeed", "[Int]", "显示/设置建造速度", ModCommander::buildSpeed);
    handler.<Player>register("setItemDrop", "[Int]", "显示/设置单位死亡掉落资源倍数,默认100", (args, player) -> {
      if (args.length == 1) {
        try {
          times = Integer.parseInt(args[0]);
        } catch (NumberFormatException ignored) {
          chat(("arg must be INT : %s"), args[0]);
        }
      }
      chat(("当前单位掉落:波数*%d"), times);
    });
    handler.register("callUnit", "<@Unit.name>", "呼叫一个对应的单位刷新在玩家位置", ModCommander::callUnit);
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

  private void unitDestroyEvent(EventType.UnitDestroyEvent e) {
    if (Vars.player.team().isEnemy(e.unit.team)) {
      ItemStack stack = Tools.getRandomItemStack(Vars.state.wave * times);
      Vars.player.core().items.add(stack.item, stack.amount);
      Table t = new Table(Styles.black3);
      t.touchable = Touchable.disabled;
      t.setPosition(e.unit.x, e.unit.y);
      t.add(new ItemDisplay(stack.item, stack.amount, false)).padRight(5.0F);
      t.actions(Actions.show(), Actions.delay(5, Actions.remove()));
      t.pack();
      Core.scene.add(t);
    }
  }

  private void chat(String format, Object... args) {
    NetClient.sendMessage(String.format(format, args), "Admin", Vars.player);
  }

  @Override
  public void loadContent() {
    Log.info("加载TestMod方块");
    new MyContextList().load();
    new MyTechTreeList().load();
  }

}
