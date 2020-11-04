package com.ui;

import arc.scene.ui.layout.Table;
import mindustry.type.ItemStack;
import mindustry.world.meta.StatValue;

/**
 * @author bin
 * @version 1.0.0
 */
public class RangeItemStatValue implements StatValue {
  private final ItemStack[] stacks;
  private final int total;

  public RangeItemStatValue(ItemStack... stacks) {
    this.stacks = stacks;
    int total = 0;
    for (ItemStack stack : stacks) {
      total += stack.amount;
    }
    this.total = total;
  }

  @Override public void display(Table table) {
    for (ItemStack stack : stacks) {
      table.add(new RangeItemImage(stack.item, stack.amount, total));
      table.add(stack.item.localizedName).padLeft(4 + stack.amount > 99 ? 4.0F : 0.0F);
    }
  }
}
