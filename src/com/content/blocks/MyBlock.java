package com.content.blocks;

import arc.math.Mathf;
import arc.util.io.Reads;
import arc.util.io.Writes;
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
public class MyBlock extends Block {
  public ItemStack[] results;
  public float craftTime;

  public MyBlock(String name) {
    super(name);
    this.update = true;
    this.solid = true;
    this.hasItems = true;
    this.hasLiquids = false;
  }

  @Override
  protected void initBuilding() {
    this.buildType = MyBlockBuild::new;
  }

  @Override
  public void setStats() {
    super.setStats();
    if (this.results != null) {
      for (ItemStack i : this.results) {
        this.stats.add(Stat.output, i);
      }
    }
    this.stats.add(Stat.productionTime, this.craftTime / 60.0F, StatUnit.seconds);
  }

  public class MyBlockBuild extends Building {
    public float progress;
    public float warmup;

    public MyBlockBuild() {
    }

    @Override
    public boolean shouldIdleSound() {
      return this.cons.valid();
    }

    @Override
    public boolean shouldConsume() {
//      int total = this.items.total();
//      if (MyBlock.this.consumes.has(ConsumeType.item) &&
//          MyBlock.this.consumes.get(ConsumeType.item) instanceof ConsumeItems) {
//        ConsumeItems c = MyBlock.this.consumes.get(ConsumeType.item);
//
//        for (ItemStack stack : c.items) {
//          total -= this.items.get(stack.item);
//        }
//      }
//
//      return total < MyBlock.this.itemCapacity && this.enabled;
      return this.enabled;
    }

    @Override
    public void updateTile() {
      if (this.consValid()) {
        this.progress += this.getProgressIncrease(MyBlock.this.craftTime);
        this.warmup = Mathf.lerpDelta(this.warmup, 1.0F, 0.02F);
      } else {
        this.warmup = Mathf.lerpDelta(this.warmup, 0.0F, 0.02F);
      }

      if (this.progress >= 1) {
        this.progress %= 1;

        this.consume();
        for (ItemStack stack : MyBlock.this.results) {
          Item item = stack.item;
          this.items.add(item, stack.amount);
        }
      }
      if (this.timer(MyBlock.this.timerDump, 5)) {
        this.dump();
      }
    }

    @Override
    public boolean canDump(Building to, Item item) {
      for (ItemStack result : MyBlock.this.results) {
        Item item1 = result.item;
        if (item1.equals(item)) {
          return true;
        }
      }
      return false;
    }

    @Override
    public int getMaximumAccepted(Item item) {
      for (ItemStack stack : MyBlock.this.results) {
        if (stack.item.equals(item)) {
          return stack.amount * MyBlock.this.itemCapacity;
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
