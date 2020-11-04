package com.ui;

import arc.scene.ui.layout.Table;
import mindustry.type.Item;
import mindustry.type.ItemStack;

/**
 * @author bin
 * @version 1.0.0
 */
public class RandomItemDisplay extends Table {
  public final Item item;
  public final int amount;

  public RandomItemDisplay(Item item, int amount, boolean showName) {
    this.add(new RangeItemImage(item, amount));
    if (showName) {
      this.add(item.localizedName).padLeft(4 + amount > 99 ? 4.0F : 0.0F);
    }

    this.item = item;
    this.amount = amount;
  }

  public RandomItemDisplay(ItemStack stack) {
    this(stack.item, stack.amount, true);
  }


}
