package com;

import arc.Events;
import arc.util.CommandHandler;
import arc.util.Log;
import com.content.MyContextList;
import mindustry.Vars;
import mindustry.core.Logic;
import mindustry.core.NetClient;
import mindustry.game.EventType;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import mindustry.gen.Unitc;
import mindustry.mod.Mod;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.modules.ItemModule;

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
    handler.register("runWave", "[Int]", "下一波,默认参数1", ModCommander::runWave);
    handler.register("skipWave", "跳一波(置零计时器)", (args) -> Vars.logic.skipWave());
    handler.<Player>register("gameOver", "立即结束", (args, player) -> Logic.gameOver(player.team()));
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
      StringBuilder sb = new StringBuilder();
      for (ItemStack itemStack : block.requirements) {
        items.add(itemStack.item, itemStack.amount);
        sb.append("  ")
           .append(itemStack.item.localizedName)
           .append(" : ")
           .append(itemStack.amount);
      }
      chat(("击毁 %s 获得物资:%s"), block.localizedName, sb.toString());
    }
  }

  private void unitDestroyEvent(EventType.UnitDestroyEvent e) {
    if (Vars.player.team().isEnemy(e.unit.team)) {
      ItemStack stack = Tools.getRandomItemStack(Vars.state.wave * times);
      Vars.player.team().items().add(stack.item, stack.amount);
      String s = stack.item.localizedName;
      chat(("击败 %s 获得物资 %s : %d"), e.unit.type().localizedName, s, stack.amount);

    }
  }

  private void chat(String format, Object... args) {
    NetClient.sendMessage(String.format(format, args), "Admin", Vars.player);
  }

  @Override
  public void loadContent() {
    Log.info("加载TestMod方块");
    new MyContextList().load();
  }

}
