package com.content.contentLists;

import com.entities.bullet.MyBulletType;
import mindustry.ctype.ContentList;
import mindustry.entities.bullet.BulletType;
import mindustry.gen.Sounds;

/**
 * @author bin
 * @version 1.0.0
 */
public class MyBulletList implements ContentList {
  public static BulletType Bin_Bullet1;

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
  }
}
