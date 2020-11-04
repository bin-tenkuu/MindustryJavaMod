package com;

import arc.math.Mathf;
import arc.math.Rand;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.core.NetClient;
import mindustry.type.Item;
import mindustry.type.ItemStack;

/**
 * @author bin
 */
public final class Tools {
  public static final Rand R = Mathf.rand;

  public static ItemStack getRandomItemStack(int num) {
    Seq<Item> items = Vars.content.items();
    return new ItemStack(items.random(), nextInt(num));
  }

  public static int nextInt(int bound) {
    return R.nextInt(bound);
  }

  public static int nextInt() {
    return R.nextInt();
  }

  public static int nextInt(int start, int end) {
    if (start >= end) {
      return start;
    }
    return R.nextInt(end - start) + start;
  }

  public static void chat(String format, Object... args) {
    NetClient.sendMessage(String.format(format, args), "Admin", Vars.player);
  }
}
