package com.ui;

import arc.scene.ui.Image;
import arc.scene.ui.layout.Stack;
import arc.scene.ui.layout.Table;
import mindustry.type.Item;
import mindustry.ui.Cicon;
import mindustry.ui.Styles;

/**
 * @author bin
 */
public class RangeItemImage extends Stack {

  public RangeItemImage(Item item, int amount, int total) {
    this.add(new Table((o) -> {
      o.left();
      o.add(new Image(item.icon(Cicon.medium))).size(32.0F);
    }));
    if (amount != 0) {
      this.add(new Table((t) -> {
        t.left().bottom();
        t.add(String.format("%d%%", amount * 100 / total)).style(Styles.outlineLabel);
        t.pack();
      }));
    }
  }

  public RangeItemImage(Item item, int amount) {
    this.add(new Table((o) -> {
      o.left();
      o.add(new Image(item.icon(Cicon.medium))).size(32.0F);
    }));
    if (amount != 0) {
      this.add(new Table((t) -> {
        t.left().bottom();
        t.add("0~" + amount).style(Styles.outlineLabel);
        t.pack();
      }));
    }
  }
}