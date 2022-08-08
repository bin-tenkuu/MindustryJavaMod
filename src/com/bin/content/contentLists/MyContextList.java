package com.bin.content.contentLists;

import arc.graphics.Color;
import arc.scene.ui.layout.Table;
import arc.util.Time;
import com.bin.Tools;
import com.bin.content.blocks.*;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.content.Items;
import mindustry.entities.bullet.LaserBoltBulletType;
import mindustry.entities.effect.MultiEffect;
import mindustry.gen.*;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.ui.Styles;
import mindustry.world.Block;
import mindustry.world.blocks.defense.turrets.PowerTurret;
import mindustry.world.blocks.logic.SwitchBlock;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.meta.BuildVisibility;

/**
 * @author bin
 * @version 1.0.0
 */
public class MyContextList {
    public static Block
            Bin_LinkCore,
            Bin_Commend1,
            Bin_CommendCall,
            Bin_Block1,
            Bin_LaserTurret;

    public void load() {
        Bin_LinkCore = new LinkCoreBlock("Bin_LinkCore") {
            {
                localizedName = "量子核心连接器";
                description = "通过量子通道与核心相连,同时装载了装卸器";
                requirements(Category.distribution, BuildVisibility.shown, ItemStack.with(Items.copper, 50));
                size = 1;
                health = 320;
            }
        };
        Bin_Commend1 = new CommendBlock("Bin_Commend1") {
            {
                localizedName = "指令器";
                description = "集成各种指令";
                size = 2;
                requirements(Category.effect, BuildVisibility.shown, ItemStack.empty);
                commend = MyContextList::display;
            }
        };//*/
        Bin_CommendCall = new CommendBlock("Bin_CommendCall") {
            {
                localizedName = "召唤器";
                description = "召唤指定单位";
                size = 2;
                requirements(Category.effect, BuildVisibility.shown, ItemStack.empty);

                commend = (building, table) -> Tools.buildItemSelectTable(
                        table,
                        Vars.content.units(),
                        () -> null,
                        unitType -> {
                            Unit unit = unitType.create(building.team());
                            unit.set(building.x, building.y + 1);
                            unit.add();
                        },
                        false
                );
            }
        };
        Bin_Block1 = new SwitchBlock("Bin_Block1") {
            {
                localizedName = "灭霸の响指";
                description = "随机销毁全图约一半的建筑\n" +
                        "[red]核心[]除外,[red]墙与传送带[]由于技术原因获取不到,不进入计算";
                requirements(Category.logic, BuildVisibility.shown, ItemStack.with(Items.copper, 5));
                size = 2;
            }

            @Override
            protected void initBuilding() {
                buildType = () -> new SwitchBuild() {
                    @Override
                    public boolean configTapped() {
                        kill();
                        Sounds.click.at(this);
                        Time.run(10, () -> {
                            boolean b = true;
                            for (int i = 0, n = 10; i < Groups.build.size(); i++) {
                                Building building = Groups.build.index(i);
                                if (building instanceof CoreBlock.CoreBuild) {
                                    continue;
                                }
                                b = !b;
                                if (b) {
                                    Time.run(n++, building::kill);
                                }
                            }
                        });
                        return false;
                    }
                };
            }
        };

        Bin_LaserTurret = new PowerTurret("Bin_LaserTurret") {
            {
                requirements(Category.turret, BuildVisibility.shown, ItemStack.with(Items.copper, 50));
                size = 2;
                health = 3600;
                ammoPerShot = 2;
                range = 420;
                liquidCapacity = 60;
                inaccuracy = 0;
                cooldownTime = 30;
                shootEffect = Fx.lancerLaserShoot;
                smokeEffect = Fx.none;
                heatColor = Color.red;
                consumePower(1);
                reload = 30;
                shootSound = Sounds.laserbig;
                shootType = new LaserBoltBulletType(25, 100) {{
                    lifetime = 20;
                    hitSize = 30;
                    pierce = true;
                    knockback = 0.1F;
                    width = 30;
                    height = 30;
                    chargeEffect = new MultiEffect(Fx.lancerLaserCharge, Fx.lancerLaserChargeBegin);
                    shootEffect = Fx.shootHeal;
                    frontColor = Color.valueOf("00fff7");
                    backColor = Color.valueOf("808080");
                    splashDamage = 500;
                    splashDamageRadius = 50;
                }};
            }
        };
    }

    private static void display(Building building, Table table) {
        table.button(Icon.upOpen, Styles.clearNonei, (() -> Vars.logic.skipWave())).size(50).tooltip("下一波");
        table.button("跳10波", Icon.warningSmall, Styles.cleart, (() -> {
            for (int i = 0; i < 10; i++) {
                Vars.logic.runWave();
            }
        })).size(50);
        table.button(Icon.file, Styles.clearNonei, () ->
                Groups.all.each(Firec.class::isInstance, Entityc::remove)
        ).size(50).tooltip("快速灭火");
        table.button(Icon.cancel, Styles.clearNonei, () ->
                Groups.unit.each(Unitc::destroy)
        ).size(50).tooltip("清除所有单位");
        table.row();
    }
}
