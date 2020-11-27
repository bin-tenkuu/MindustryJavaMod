package com.bin.content.contentLists;

import arc.graphics.Color;
import com.bin.entities.bullet.MyBulletType;
import mindustry.content.Fx;
import mindustry.ctype.ContentList;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.entities.bullet.BulletType;
import mindustry.gen.Sounds;

/**
 * @author bin
 * @version 1.0.0
 */
public class MyBulletList implements ContentList {
  public static BulletType Bin_Bullet1,
      Bin_Bullet2;

  @Override public void load() {
    Bin_Bullet1 = new MyBulletType(0, 1000, "Bin_MapBullet") {
      {
        this.splashDamageRadius = 50;
        this.splashDamage = 1000;
        this.pierce = false;
        this.collidesTiles = false;
        this.collidesTeam = false;
        this.collidesAir = true;
        this.collides = false;
        this.keepVelocity = false;
        this.lifetime = 30;
        this.hitSize = 1;
        this.hittable = false;
        this.hitSound = Sounds.explosion;
        this.width = 50;
        this.height = 50;
      }
    };
    Bin_Bullet2 = new BasicBulletType(25, 100) {{
      lifetime = 20;
      hitSize = 30;
      pierce = true;
      knockback = 0.1F;
      width = 30;
      height = 30;
      shootEffect = Fx.shootHeal;
      smokeEffect = Fx.hitLaser;
      hitEffect = Fx.hitLaser;
      despawnEffect = Fx.hitLaser;
      frontColor = Color.valueOf("00fff7");
      backColor = Color.valueOf("808080");
      splashDamage = 500;
      splashDamageRadius = 50;

    }};
  }
}
