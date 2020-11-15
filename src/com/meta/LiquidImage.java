//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.meta;

import arc.graphics.g2d.TextureRegion;
import arc.scene.ui.Image;
import arc.scene.ui.layout.Stack;
import arc.scene.ui.layout.Table;
import mindustry.type.LiquidStack;
import mindustry.ui.Cicon;
import mindustry.ui.Styles;

/**
 * @author bin
 */
public class LiquidImage extends Stack {
  public LiquidImage(TextureRegion region, int amount) {
    this.add(new Table((o) -> {
      o.left();
      o.add(new Image(region)).size(32.0F);
    }));
    this.add(new Table((t) -> {
      t.left().bottom();
      t.add(amount + "");
      t.pack();
    }));
  }

  public LiquidImage(TextureRegion region) {
    Table t = (new Table()).left().bottom();
    this.add(new Image(region));
    this.add(t);
  }

  public LiquidImage(LiquidStack stack) {
    this.add(new Table((o) -> {
      o.left();
      o.add(new Image(stack.liquid.icon(Cicon.medium))).size(32.0F);
    }));
    if (stack.amount != 0) {
      this.add(new Table((t) -> {
        t.left().bottom();
        t.add(stack.amount + "").style(Styles.outlineLabel);
        t.pack();
      }));
    }

  }
}
