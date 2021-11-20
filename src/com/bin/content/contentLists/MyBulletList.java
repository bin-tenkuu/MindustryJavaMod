package com.bin.content.contentLists;

import arc.graphics.Color;
import mindustry.content.Fx;
import mindustry.ctype.ContentList;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.entities.bullet.BulletType;
import mindustry.entities.bullet.LaserBoltBulletType;

/**
 * @author bin
 * @version 1.0.0
 */
public class MyBulletList implements ContentList {
    public static BulletType Bin_Bullet2;

    @Override
    public void load() {
        Bin_Bullet2 = new LaserBoltBulletType(5, 100) {{
            lifetime = 20;
            hitSize = 30;
            pierce = true;
            knockback = 0.1F;
            width = 30;
            height = 30;
            shootEffect = Fx.shootHeal;
            frontColor = Color.valueOf("00fff7");
            backColor = Color.valueOf("808080");
            splashDamage = 500;
            splashDamageRadius = 50;
        }};
    }
}
