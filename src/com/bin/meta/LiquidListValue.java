package com.bin.meta;

import arc.scene.ui.layout.Table;
import mindustry.type.LiquidStack;
import mindustry.world.meta.StatValue;

/**
 * @author bin
 */
public class LiquidListValue implements StatValue {
  private final LiquidStack[] stacks;
  private final boolean displayName;

  public LiquidListValue(LiquidStack... stacks) {
    this(true, stacks);
  }

  public LiquidListValue(boolean displayName, LiquidStack... stacks) {
    this.stacks = stacks;
    this.displayName = displayName;
  }

  @Override public void display(Table table) {
    for (LiquidStack stack : this.stacks) {
      table.add(new LiquidDisplay(stack.liquid, stack.amount, this.displayName)).padRight(5.0F);
    }

  }
}
