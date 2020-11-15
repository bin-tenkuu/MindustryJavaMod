package com.content.blocks;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.scene.ui.layout.Table;
import arc.struct.EnumSet;
import com.meta.LiquidImage;
import mindustry.gen.Building;
import mindustry.type.Liquid;
import mindustry.type.LiquidStack;
import mindustry.ui.Cicon;
import mindustry.world.blocks.power.PowerGenerator;
import mindustry.world.consumers.ConsumeType;
import mindustry.world.meta.BlockFlag;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;

/**
 * @author bin
 */
public class PowerDriver extends PowerGenerator {
  public final int timerFuel;
  public float liquidDuration;
  public Color idleColor;
  public int explosionRadius;
  public int explosionDamage;

  public PowerDriver(String name) {
    super(name);
    this.timerFuel = this.timers++;
    this.idleColor = Color.valueOf("646464");
    this.explosionRadius = 40;
    this.explosionDamage = 1350;
    this.liquidDuration = 1;
    this.liquidCapacity = 30;
    this.hasItems = false;
    this.hasLiquids = true;
    this.flags = EnumSet.of(BlockFlag.reactor);
  }

  @Override public void setStats() {
    super.setStats();
    if (this.hasLiquids) {
      this.stats.add(Stat.productionTime, PowerDriver.this.liquidDuration, StatUnit.seconds);
    }
  }

  @Override public void setBars() {
    super.setBars();
    this.bars.remove("liquid");
  }

  @Override protected void initBuilding() {
    this.buildType = PowerDriverBuild::new;
  }

  public class PowerDriverBuild extends GeneratorBuild {

    @Override public void updateTile() {
      if (this.shouldConsume() && this.enabled) {
        if (this.timer(PowerDriver.this.timerFuel, PowerDriver.this.liquidDuration / this.timeScale())) {
          this.consume();
          this.productionEfficiency = 1;
        }
      } else {
        this.productionEfficiency = 0;
      }
    }

    @Override public void display(Table table) {
      super.display(table);
    }

    @Override public void updateTableAlign(Table table) {
      super.updateTableAlign(table);
      this.liquids.each((liquid, v) -> {
        table.add(new LiquidImage(new LiquidStack(liquid, v)));
        table.image(liquid.icon(Cicon.medium)).size(30);
      });
    }

    @Override public boolean shouldConsume() {
      return PowerDriver.this.consumes.get(ConsumeType.liquid).valid(this);
    }

    @Override public boolean acceptLiquid(Building source, Liquid liquid) {
      return this.liquids.get(liquid) <= PowerDriver.this.liquidCapacity;
    }

    @Override public void draw() {
      super.draw();
      if (this.productionEfficiency == 0) {
        Draw.color(PowerDriver.this.idleColor);
        Draw.rect(PowerDriver.this.region, this.x, this.y);
        Draw.reset();
      }
    }

  }
}