package com.content.blocks;

import arc.math.Mathf;
import arc.util.io.Reads;
import arc.util.io.Writes;
import com.ui.RangeItemStatValue;
import mindustry.gen.Building;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;

/**
 * @author bin
 * @version 1.0.0
 */
public class GambleMachine extends Block {
  public ItemStack[] results;
  public float craftTime;

  public GambleMachine(String name) {
    super(name);
    this.update = true;
    this.solid = true;
    this.hasItems = true;
    this.hasLiquids = false;
  }

  @Override
  protected void initBuilding() {
    this.buildType = GambleMachineBuild::new;
  }

  @Override
  public void setStats() {
    super.setStats();
    if (this.results != null) {
      this.stats.add(Stat.output, new RangeItemStatValue(this.results));
    }
    this.stats.add(Stat.productionTime, this.craftTime / 60.0F, StatUnit.seconds);
  }

  public class GambleMachineBuild extends Building {
    public float progress;
    public float warmup;

    public GambleMachineBuild() {
    }

    @Override
    public boolean shouldIdleSound() {
      return this.cons.valid();
    }

    @Override
    public boolean shouldConsume() {
      return this.enabled;
    }

    @Override
    public void updateTile() {
      if (this.consValid()) {
        this.progress += this.getProgressIncrease(GambleMachine.this.craftTime);
        this.warmup = Mathf.lerpDelta(this.warmup, 1.0F, 0.02F);
      } else {
        this.warmup = Mathf.lerpDelta(this.warmup, 0.0F, 0.02F);
      }

      if (this.progress >= 1) {
        this.progress %= 1;

        this.consume();
        ItemStack[] results = GambleMachine.this.results;
        int total = 0;
        for (ItemStack stack : results) {
          total += stack.amount;
        }
        int random = Mathf.random(total);
        total = 0;
        for (ItemStack result : results) {
          total += result.amount;
          if (random < total) {
            this.offload(result.item);
            break;
          }
        }
      }
      if (this.timer(GambleMachine.this.timerDump, 5)) {
        this.dump();
      }
    }

    @Override
    public boolean canDump(Building to, Item item) {
      for (ItemStack result : GambleMachine.this.results) {
        Item item1 = result.item;
        if (item1.equals(item)) {
          return true;
        }
      }
      return false;
    }

    @Override
    public int getMaximumAccepted(Item item) {
      for (ItemStack stack : GambleMachine.this.results) {
        if (stack.item.equals(item)) {
          return stack.amount * GambleMachine.this.itemCapacity;
        }
      }
      return 10;
    }

    @Override
    public void write(Writes write) {
      super.write(write);
      write.f(this.progress);
      write.f(this.warmup);
    }

    @Override
    public void read(Reads read, byte revision) {
      super.read(read, revision);
      this.progress = read.f();
      this.warmup = read.f();
    }
  }
}
