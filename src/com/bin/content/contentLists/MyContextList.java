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
            Bin_ItemChange,
            Bin_LiquidChange,
            Bin_LinkCore,
            Bin_Commend1,
            Bin_CommendCall,
            Bin_SourceDrill,
            Bin_Block1,
            Bin_LaserTurret;

    public void load() {
        Bin_ItemChange = new ItemChange("Bin_ItemChange") {
            {
                localizedName = "等量转换器";
                description = "输入物品,转换为等量的另一个物品";
                requirements(Category.distribution, BuildVisibility.shown, ItemStack.with(Items.copper, 50));
                size = 1;
                health = 320;
            }
        };
        Bin_LiquidChange = new LiquidChange("Bin_LiquidChange") {
            {
                localizedName = "液体转换器";
                description = "输入物品,转换为另一个液体";
                requirements(Category.distribution, BuildVisibility.shown, ItemStack.with(Items.copper, 50));
                size = 1;
                health = 320;
            }
        };
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
        Bin_SourceDrill = new FastestDrill("Bin_SourceDrill") {
            {
                localizedName = "量子钻头";
                description = "[red]极限产出,不可加速[]\n" +
                        "[green]我在短暂的钻头生涯当中学到了一件事,越是想要钻的更快,越是会受到各种限制,除非超越钻头,我不做钻头了![]\n" +
                        "(变相物品源";
                size = 2;
                requirements(Category.production, BuildVisibility.shown, ItemStack.with(Items.copper, 20));
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

    /*
    Bin_LargeCore = new CoreBlock("Bin_LargeCore") {
      {
        this.localizedName = "次元核心";
        this.description = "高强度核心,内有仓库";
        this.size = 6;
        this.health = 10000;
        this.itemCapacity = 100000;
        requirements(Category.effect, ItemStack.with(Items.copper, 64));
      }
    };
    Bin_LargeStorage = new StorageBlock("Bin_LargeStorage") {
      {
        this.localizedName = "次元仓库";
        this.description = "高强度仓库,内有核心";
        this.requirements(Category.effect, ItemStack.with(Items.copper, 50));
        this.size = 4;
        this.itemCapacity = 10000;
        this.flags = EnumSet.of(BlockFlag.storage);
      }
    };
    //*/
    /*
    Bin_LiquidPower = new LiquidPowerDriver("Bin_LiquidPower") {
      {
        this.localizedName = "液体温差发电站";
        this.description = "使用液体间温度反应发电";
        this.requirements(Category.distribution, BuildVisibility.shown, ItemStack.with(Items.copper, 50));
        this.size = 3;
        this.liquidDuration = 2;
        this.liquidCapacity = 30;
        this.powerProduction = 50;
        this.requirements(Category.power, ItemStack.with(Items.copper, 50));
        this.consumes.add(new ConsumeLiquids(new LiquidStack[]{
            new LiquidStack(Liquids.slag, 3),
//            new LiquidStack(Liquids.oil, 3),
            new LiquidStack(Liquids.water, 3),
        }));
      }
    };
    //*/
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
