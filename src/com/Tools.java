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

  public static ItemStack getRandomItemStack(int num) {
    Seq<Item> items = Vars.content.items();
    return new ItemStack(items.random(), Mathf.random(num));
  }

  public static void chat(String format, Object... args) {
    NetClient.sendMessage(String.format(format, args), "Admin", Vars.player);
  }
}
