package com.bin.entities.bullet;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import com.bin.Tools;
import mindustry.entities.bullet.BulletType;
import mindustry.gen.Bullet;

/**
 * @author bin
 * @version 1.0.0
 */
public class MyBulletType extends BulletType {
  public float width;
  public float height;
  public String sprite;
  public Color frontColor;
  public TextureRegion frontRegion;

  public MyBulletType(float speed, float damage, String bulletSprite) {
    super(speed, damage);
    this.frontColor = Color.white;
    this.width = 10;
    this.height = 10;
    this.sprite = bulletSprite;
  }

  @Override public void load() {
    this.frontRegion = Core.atlas.find(Tools.ModName + "-" + this.sprite);
  }

  @Override public void draw(Bullet b) {
    float height = this.height * (1.0F + b.fout());
    float width = this.width * (1.0F + b.fout());
    Draw.color(this.frontColor);
    Draw.rect(this.frontRegion, b.x, b.y, width, height);
    Draw.reset();
  }
}
