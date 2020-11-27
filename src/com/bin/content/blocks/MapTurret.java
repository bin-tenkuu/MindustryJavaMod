package com.content.blocks;

import mindustry.entities.bullet.BulletType;
import mindustry.gen.Posc;
import mindustry.world.blocks.defense.turrets.ChargeTurret;

/**
 * @author bin
 * @version 1.0.0
 */
public class MapTurret extends ChargeTurret {

  public MapTurret(String name) {
    super(name);
  }

  @Override protected void initBuilding() {
    this.buildType = MapTurretBuild::new;
  }

  public class MapTurretBuild extends ChargeTurretBuild {
    public MapTurretBuild() {

    }

    @Override protected void turnToTarget(float targetRot) {
      this.rotation = targetRot;
    }

    @Override protected void bullet(BulletType type, float angle) {
      float x = this.targetPos.x;
      float y = this.targetPos.y;
//      Time.run(10, () -> {
      type.create(this, this.team, x, y, angle);
//      });
    }

    @Override public void targetPosition(Posc pos) {
      this.targetPos.set(this.target);
    }
  }

}
