package com.consume;

import arc.scene.ui.layout.Table;
import arc.struct.Bits;
import com.meta.LiquidImage;
import com.meta.LiquidListValue;
import mindustry.gen.Building;
import mindustry.type.LiquidStack;
import mindustry.ui.Cicon;
import mindustry.ui.ReqImage;
import mindustry.world.consumers.Consume;
import mindustry.world.consumers.ConsumeType;
import mindustry.world.meta.Stat;
import mindustry.world.meta.Stats;

/**
 * @author bin
 */
public class ConsumeLiquids extends Consume {
  public final LiquidStack[] liquids;

  public ConsumeLiquids(LiquidStack[] liquids) {
    this.liquids = liquids;
  }

  protected ConsumeLiquids() {
    this(new LiquidStack[0]);
  }

  @Override public void applyLiquidFilter(Bits filter) {
    for (LiquidStack stack : this.liquids) {
      filter.set(stack.liquid.id);
    }
  }

  @Override public ConsumeType type() {
    return ConsumeType.liquid;
  }

  @Override public void build(Building tile, Table table) {
    for (LiquidStack stack : this.liquids) {
      table.add(new ReqImage(new LiquidImage(stack.liquid.icon(Cicon.medium), (int)stack.amount), () -> {
        return tile.liquids != null && tile.liquids.get(stack.liquid) >= stack.amount;
      })).padRight(8.0F);
    }
  }

  @Override public String getIcon() {
    return "icon-liquid-consume";
  }

  @Override public void update(Building entity) {
  }

  @Override public void trigger(Building entity) {
    for (LiquidStack stack : this.liquids) {
      entity.liquids.remove(stack.liquid, stack.amount / 60);
    }
  }

  @Override public boolean valid(Building entity) {
    if (entity.liquids == null) {
      return false;
    }
    for (LiquidStack liquid : this.liquids) {
      if (entity.liquids.get(liquid.liquid) < liquid.amount) {
        return false;
      }
    }
    return true;
  }

  @Override public void display(Stats stats) {
    stats.add(this.booster ? Stat.booster : Stat.input, new LiquidListValue(this.liquids));
  }
}
