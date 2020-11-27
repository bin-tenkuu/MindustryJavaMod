//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.bin.meta;

import arc.scene.ui.layout.Table;
import mindustry.type.Liquid;
import mindustry.type.LiquidStack;

/**
 * @author bin
 */
public class LiquidDisplay extends Table {
  public final Liquid item;
  public final float amount;

  public LiquidDisplay(Liquid item) {
    this(item, 0);
  }

  public LiquidDisplay(Liquid item, float amount, boolean showName) {
    this.add(new LiquidImage(new LiquidStack(item, amount)));
    if (showName) {
      this.add(item.localizedName).padLeft(4 + amount > 99 ? 4.0F : 0.0F);
    }

    this.item = item;
    this.amount = amount;
  }

  public LiquidDisplay(Liquid item, float amount) {
    this(item, amount, true);
  }
}
