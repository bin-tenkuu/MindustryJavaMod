package com;

import mindustry.Vars;
import mindustry.content.UnitTypes;
import mindustry.gen.Firec;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import mindustry.gen.Unit;
import mindustry.type.UnitType;

import java.lang.reflect.Field;

/**
 * @author bin
 * @version 1.0.0
 */
public class ModCommander {
  public static void fire(String[] args, Player player) {
    Groups.all.forEach(syncs -> {
      if (syncs instanceof Firec) {
        syncs.remove();
      }
    });
    Tools.chat("[green]全图迅速灭火[]");
  }

  public static void runWave(String[] args, Player player) {
    int n = 1;
    try {
      if (args.length == 1) {
        n = Integer.parseInt(args[0]);
      }
    } catch (NumberFormatException numberFormatException) {
      Tools.chat(("arg must be Int : %s"), args[0]);
    }
    for (; n > 0; n--) {
      Vars.logic.runWave();
    }
  }

  public static void waveSpacing(String[] args, Player player) {
    try {
      if (args.length == 1) {
        Vars.state.rules.waveSpacing = Integer.parseInt(args[0]);
      }
      Tools.chat("当前波数间隔: %s", Vars.state.rules.waveSpacing);
    } catch (NumberFormatException numberFormatException) {
      Tools.chat(("arg must be Int : %s"), args[0]);
    }
  }

  public static void buildSpeed(String[] args, Player player) {
    try {
      if (args.length == 1) {
        Vars.state.rules.buildSpeedMultiplier = Integer.parseInt(args[0]);
      }
      Tools.chat("当前建造速度: %s", Vars.state.rules.buildSpeedMultiplier);
    } catch (NumberFormatException numberFormatException) {
      Tools.chat(("arg must be Int : %s"), args[0]);
    }
  }

  public static void callUnit(String[] args, Player player) {
    try {
      if (args.length >= 1) {
        Field field = UnitTypes.class.getField(args[0]);
        Object o = field.get(UnitTypes.class);
        if (o instanceof UnitType) {
          Unit unit = ((UnitType)o).create(player.team());
          unit.set(player);
          unit.add();
          Tools.chat("召唤 %s 成功", ((UnitType)o).localizedName);
        }else {
          Tools.chat("召唤失败:对应单位类型错误");
        }
      } else {
        Tools.chat("需要参数");
      }
    } catch (NoSuchFieldException e) {
      Tools.chat("对应单位未找到");
    } catch (IllegalAccessException e) {
      Tools.chat(e.getLocalizedMessage());
    }
  }
}
